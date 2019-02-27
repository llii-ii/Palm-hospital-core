package com.kasite.client.swiftpass.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.kasite.client.swiftpass.constants.SwiftpassConstants;
import com.kasite.client.swiftpass.util.MD5;
import com.kasite.client.swiftpass.util.SwiftpassUtils;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.SwiftpassEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.HttpsClientUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.TenpayUtil;
import com.kasite.core.serviceinterface.module.pay.IPaymentGateWayService;
import com.kasite.core.serviceinterface.module.pay.bo.MchBill;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqRevoke;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespDownloadBill;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRevoke;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespSweepCodePay;

/**
 * @author linjf
 * 威富通-抽象类
 */

public abstract class SwiftpassAbstractService implements IPaymentGateWayService{
  
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	
	/**
	 * @param authInfoVo
	 * @param pgwReqSweepCodePay
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespSweepCodePay sweepCodePay(AuthInfoVo authInfoVo, PgwReqSweepCodePay pgwReqSweepCodePay)
			throws Exception {
		PgwRespSweepCodePay pgwRespSweepCodePay = new PgwRespSweepCodePay();
		SortedMap<String, String> map = new TreeMap<String, String>();
		String key = KasiteConfig.getSwiftpass(SwiftpassEnum.key,authInfoVo.getConfigKey());
		map.put("service",SwiftpassConstants.UNIFIED_TRADE_MICROPAY);
		map.put("version", SwiftpassConstants.VERSION);
		map.put("charset", SwiftpassConstants.CHARSET);
		map.put("sign_type", SwiftpassConstants.SIGN_TYPE);
		map.put("auth_code", pgwReqSweepCodePay.getAuthCode());
		map.put("out_trade_no", pgwReqSweepCodePay.getOrderId());
		map.put("body", pgwReqSweepCodePay.getBody());
		map.put("attach", pgwReqSweepCodePay.getAttach());
		map.put("total_fee", pgwReqSweepCodePay.getTotalPrice().toString());
		map.put("mch_create_ip", pgwReqSweepCodePay.getRemoteIp());
		  
		map.put("nonce_str", TenpayUtil.getNonceStr());
        map.put("mch_id", KasiteConfig.getSwiftpass(SwiftpassEnum.mch_id,authInfoVo.getConfigKey()));
        //可选参数，微信公众平台基本配置中的AppID(应用ID)，传入后支付成功可返回对应公众号下的用户openid
        map.put("sub_appid", KasiteConfig.getSwiftpass(SwiftpassEnum.sub_appid,authInfoVo.getConfigKey()));
	            
	    Map<String,String> params = SwiftpassUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SwiftpassUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");

        map.put("sign", sign);
        String reqParam = SwiftpassUtils.parseXML(map);
		String respParam = HttpsClientUtils.SwiftpasshttpsPost(pgwReqSweepCodePay.getOrderId(),ApiModule.Swiftpass.unifiedtrademicropay,
				SwiftpassConstants.SWIFTPASS_GATEWAY_URL, reqParam, SwiftpassConstants.CHARSET);
		Map<String, String> resultMap = null;
		if (StringUtil.isNotEmpty(respParam)) {
			resultMap = SwiftpassUtils.xml2Map(respParam.getBytes(SwiftpassConstants.CHARSET),
					SwiftpassConstants.CHARSET);
			if (!SwiftpassUtils.checkParam(resultMap,key)) {
				// "验证签名不通过";
				pgwRespSweepCodePay.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
				pgwRespSweepCodePay.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
			} else {
				String status =  resultMap.get("status");
				String resultCode = resultMap.get("result_code");
				String message = resultMap.get("message");
				String needQuery = resultMap.get("need_query");
				String errCode = resultMap.get("err_code");
				String errMsg = resultMap.get("err_msg");
				String transactionNo = resultMap.get("transaction_id");
				if (KstHosConstant.STRING_0.equals(status)
						&& KstHosConstant.STRING_0.equals(resultCode)) {
					//status和result_code字段返回都为0时，判定订单支付成功；
					pgwRespSweepCodePay.setRespCode(RetCode.Success.RET_10000);
					pgwRespSweepCodePay.setRespMsg(RetCode.Success.RET_10000.getMessage());
					pgwRespSweepCodePay.setTransactionNo(transactionNo);
				}else {
					//支付请求后：返回的参数need_query为Y或没有该参数返回时，必须调用订单查询接口进行支付结果确认（查询接口调用建议参照第④点）；
					if( StringUtil.isEmpty(needQuery) || "Y".equals(needQuery)) {
						pgwRespSweepCodePay.setRespCode(RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING);
						pgwRespSweepCodePay.setRespMsg(RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING.getExceptMsg());
					}else if( "N".equals(needQuery)) {
						//支付请求后：返回的参数need_query为N时，可明确为失败订单；
						pgwRespSweepCodePay.setRespCode(RetCode.Pay.ERROR_UNITEORDER);
						pgwRespSweepCodePay.setRespMsg(RetCode.Pay.ERROR_UNITEORDER.getExceptMsg()
								+"|message:"+message+"|resultCode:"+resultCode+"|status:"+status
								+"|needQuery:"+needQuery+"|errCode:"+errCode+"|errMsg:"+errMsg);
					}else {
						//TODO 其他情况暂时视为支付失败,待补充
						pgwRespSweepCodePay.setRespCode(RetCode.Pay.ERROR_UNITEORDER);
						pgwRespSweepCodePay.setRespMsg(RetCode.Pay.ERROR_UNITEORDER.getExceptMsg()
								+"|message:"+message+"|resultCode:"+resultCode+"|status:"+status
								+"|needQuery:"+needQuery+"|errCode:"+errCode+"|errMsg:"+errMsg);
					}
				}
			}
		} else {
			// 返回为空
			pgwRespSweepCodePay.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespSweepCodePay.setRespMsg("调用威富通接口异常！");
		}
		return pgwRespSweepCodePay;
	}
	
	/**
	 * @param authInfoVo
	 * @param pgwReqRevoke
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespRevoke revoke(AuthInfoVo authInfoVo, PgwReqRevoke pgwReqRevoke) throws Exception {
		PgwRespRevoke pgwRespRevoke = new PgwRespRevoke();
		SortedMap<String, String> map = new TreeMap<String, String>();
		String key = KasiteConfig.getSwiftpass(SwiftpassEnum.key,authInfoVo.getConfigKey());
		map.put("service",SwiftpassConstants.UNIFIED_TRADE_MICROPAY);
		map.put("version", SwiftpassConstants.VERSION);
		map.put("charset", SwiftpassConstants.CHARSET);
		map.put("sign_type", SwiftpassConstants.SIGN_TYPE);
		map.put("out_trade_no", pgwReqRevoke.getOrderId());
		  
		map.put("nonce_str", TenpayUtil.getNonceStr());
        map.put("mch_id", KasiteConfig.getSwiftpass(SwiftpassEnum.mch_id,authInfoVo.getConfigKey()));
	            
	    Map<String,String> params = SwiftpassUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SwiftpassUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        String reqParam = SwiftpassUtils.parseXML(map);
        
		String respParam = HttpsClientUtils.SwiftpasshttpsPost(pgwReqRevoke.getOrderId(),ApiModule.Swiftpass.unifiedmicropayreverse,
				SwiftpassConstants.SWIFTPASS_GATEWAY_URL, reqParam, SwiftpassConstants.CHARSET);
		Map<String, String> resultMap = null;
		if (StringUtil.isNotEmpty(respParam)) {
			resultMap = SwiftpassUtils.xml2Map(respParam.getBytes(SwiftpassConstants.CHARSET),
					SwiftpassConstants.CHARSET);
			if (!SwiftpassUtils.checkParam(resultMap,key)) {
				// "验证签名不通过";
				pgwRespRevoke.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
				pgwRespRevoke.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
			} else {
				String status =  resultMap.get("status");
				String resultCode = resultMap.get("result_code");
				String message = resultMap.get("message");
				String needQuery = resultMap.get("need_query");
				String errCode = resultMap.get("err_code");
				String errMsg = resultMap.get("err_msg");
				if (KstHosConstant.STRING_0.equals(status)
						&& KstHosConstant.STRING_0.equals(resultCode)) {
					//status和result_code字段返回都为0时，判定撤销成功
					pgwRespRevoke.setRespCode(RetCode.Success.RET_10000);
					pgwRespRevoke.setRespMsg(RetCode.Success.RET_10000.getMessage());
				}else {
					//TODO其他异常待补充
					pgwRespRevoke.setRespCode(RetCode.Pay.ERROR_REVOKED);
					pgwRespRevoke.setRespMsg(RetCode.Pay.ERROR_REVOKED.getExceptMsg()
							+"|message:"+message+"|resultCode:"+resultCode+"|status:"+status
							+"|needQuery:"+needQuery+"|errCode:"+errCode+"|errMsg:"+errMsg);
					
				}
			}
		} else {
			// 返回为空
			pgwRespRevoke.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespRevoke.setRespMsg("调用威富通接口异常！");
		}
		return pgwRespRevoke;
	}
	
	/**
	 * @param authInfoVo
	 * @param billDate
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespDownloadBill downloadBill(AuthInfoVo authInfoVo, Date billDate) throws Exception {
		PgwRespDownloadBill pgwRespDownloadBill = new PgwRespDownloadBill();
		List<MchBill> mchBillList = new ArrayList<MchBill>();
		String billDateStr = DateOper.formatDate(billDate, "yyyyMMdd");
		SortedMap<String, String> map = new TreeMap<String, String>();
		String key = KasiteConfig.getSwiftpass(SwiftpassEnum.key, authInfoVo.getConfigKey());
		map.put("service", SwiftpassConstants.PAY_BILL_MERCHANT);
		map.put("bill_date", billDateStr);
		map.put("bill_type", SwiftpassConstants.BILL_TYPE_ALL);
		map.put("sign_type", SwiftpassConstants.SIGN_TYPE);

		map.put("mch_id", KasiteConfig.getSwiftpass(SwiftpassEnum.mch_id, authInfoVo.getConfigKey()));
		map.put("nonce_str", TenpayUtil.getNonceStr());
		Map<String, String> params = SwiftpassUtils.paraFilter(map);

		StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
		SwiftpassUtils.buildPayParams(buf, params, false);
		String preStr = buf.toString();
		String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
		map.put("sign", sign);
		String reqParam = SwiftpassUtils.parseXML(map);
		String respParam = HttpsClientUtils.SwiftpasshttpsPost(null,ApiModule.Swiftpass.filedownload,
				SwiftpassConstants.SWIFTPASS_DOWNLOAD_URL, reqParam, SwiftpassConstants.CHARSET);
		if (!StringUtil.isEmpty(respParam)) {
			String[] bills = respParam.replaceAll("`", "").split("\n");
			if(bills==null || bills.length>0 && bills[0].indexOf(KstHosConstant.WX_BILL_FAIL)>0){
				LogUtil.warn(log, new LogBody(authInfoVo).set("msg", "账单下载返回失败结果：" + respParam)
						.set("billDate", billDateStr));
				pgwRespDownloadBill.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespDownloadBill.setRespMsg("调用威富通接口异常！");
			}else {
				pgwRespDownloadBill.setRespCode(RetCode.Success.RET_10000);
				pgwRespDownloadBill.setRespMsg(RetCode.Success.RET_10000.getMessage());
				for (int i = 1; i < bills.length - 2; i++) {
					String billObj = bills[i];
					String[] param = billObj.split(",");
					MchBill mchBill = new MchBill();
					mchBill.setConfigKey(authInfoVo.getConfigKey());
					mchBill.setMchTradeNo(param[6]);
					mchBill.setOrderId(param[7]);
					mchBill.setPayPrice(StringUtil.yuanChangeFenInt(param[14]));
					mchBill.setTradeType(param[10]);
					mchBill.setTransDate(DateOper.parse2Timestamp(param[0]));
					if( SwiftpassConstants.TRADE_STATE_PAY_STR.equals(param[11])) {
						mchBill.setOrderType(KstHosConstant.BILL_ORDER_TYPE_1);
					}else if( SwiftpassConstants.TRADE_STATE_REFUND_STR.equals(param[11])){
						mchBill.setOrderType(KstHosConstant.BILL_ORDER_TYPE_2);
						mchBill.setRefundPrice(StringUtil.yuanChangeFenInt(param[18]));
						mchBill.setRefundMchTradeNo(param[16]);
						mchBill.setRefundOrderId(param[17]);
					}
					mchBillList.add(mchBill);
				}
				pgwRespDownloadBill.setMchBillList(mchBillList);;
			}
		} else {
			pgwRespDownloadBill.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespDownloadBill.setRespMsg("调用威富通接口异常！");
		}
		return pgwRespDownloadBill;
	}

}
