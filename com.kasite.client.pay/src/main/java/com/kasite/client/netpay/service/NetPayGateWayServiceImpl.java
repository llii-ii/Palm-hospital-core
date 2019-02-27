package com.kasite.client.netpay.service;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.netpay.constants.NetPayConstant;
import com.kasite.client.netpay.util.NetPay;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.NetPayEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.HttpsClientUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.pay.IPaymentGateWayService;
import com.kasite.core.serviceinterface.module.pay.bo.MchBill;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqClose;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqQueryOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqQueryRefundOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqRefund;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqRevoke;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqUniteOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqWapUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespClose;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespDownloadBill;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespQueryOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespQueryRefundOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRefund;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRevoke;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespWapUniteOrder;

/**
 * @author linjf
 * 招商银行一网通对接
 */
@Service
public class NetPayGateWayServiceImpl implements IPaymentGateWayService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);

	/**
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespUniteOrder uniteOrder(AuthInfoVo authInfoVo, PgwReqUniteOrder pgwReqUniteOrder) throws Exception {
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqRefund
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespRefund refund(AuthInfoVo authInfoVo, PgwReqRefund pgwReqRefund) throws Exception {
		PgwRespRefund pgwRespRefund = new PgwRespRefund();
		SortedMap<String,String> signParam =  new TreeMap<String, String>();
		signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
		signParam.put("branchNo", KasiteConfig.getNetPay(NetPayEnum.branchNo, authInfoVo.getConfigKey()));//分行号，4位数字
		signParam.put("merchantNo", KasiteConfig.getNetPay(NetPayEnum.merchantNo, authInfoVo.getConfigKey()));//商户号，6位数字
		signParam.put("orderNo", pgwReqRefund.getOrderId());
		signParam.put("refundSerialNo", pgwReqRefund.getRefundOrderId());
		signParam.put("date",  pgwReqRefund.getPayTime().substring(0,7));
		signParam.put("amount",StringUtil.fenChangeYuan(pgwReqRefund.getRefundPrice()));
		signParam.put("desc",pgwReqRefund.getRemark());
		//signParam.put("operatorNo",KasiteConfig.getNetPay(NetPayEnum.operatorNo, authInfoVo.getConfigKey()));
		//signParam.put("pwd",KasiteConfig.getNetPay(NetPayEnum.pwd, authInfoVo.getConfigKey()));
		
		String sign = NetPay.sign(signParam, KasiteConfig.getNetPay(NetPayEnum.secretKey, authInfoVo.getConfigKey()));//sIJ7bZAVetd1p1cE
		
		JSONObject inParam = new JSONObject();
		inParam.put("version", NetPayConstant.VSERSION_1_0);
		inParam.put("charset", NetPayConstant.CHARSET_UTF8);
		inParam.put("sign", sign);
		inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
		inParam.put("reqData", JSONObject.toJSON(signParam));
		
		StringBuffer param = new StringBuffer();
		param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
		
		String resp = HttpsClientUtils.httpsPost(pgwReqRefund.getOrderId(),ApiModule.NetPay.NETPAY_DOREFUND, NetPayConstant.URL_DOREFUND, param.toString());
		if( !StringUtil.isEmpty(resp)) {
			JSONObject retJson = JSONObject.parseObject(resp);
			JSONObject rspDataJson = retJson.getJSONObject("rspData");
			if( rspDataJson != null ) {
				if(NetPayConstant.RSPCODE_SUC0000.equals(rspDataJson.getString("rspCode"))) {//成功
					pgwRespRefund.setRespCode(RetCode.Success.RET_10000);
					pgwRespRefund.setRespMsg(RetCode.Success.RET_10000.getMessage());
					pgwRespRefund.setRefundId(retJson.getString("bankSerialNo"));
				}else {//失败
					RetCode errorRetCode =netPayErrorCodeParseRetCode(rspDataJson.getString("rspCode"));
					String netPayRspMsg  = rspDataJson.getString("rspMsg");
					pgwRespRefund.setRespCode(errorRetCode);
					pgwRespRefund.setRespMsg(errorRetCode.getMessage()+"|netPayRspMsg:"+netPayRspMsg);
				}
			}else {
				pgwRespRefund.setRespCode(RetCode.Common.ERROR_THIRD_PARTY);
				pgwRespRefund.setRespMsg(RetCode.Common.ERROR_THIRD_PARTY.getMessage()+"|调用招行一网通接口出参异常！|resp:"+resp);
			}
		}else {
			pgwRespRefund.setRespCode(RetCode.Common.ERROR_THIRD_PARTY);
			pgwRespRefund.setRespMsg(RetCode.Common.ERROR_THIRD_PARTY.getMessage()+"|调用招行一网通接口异常！");
		}
		return pgwRespRefund;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqSweepCodePay
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespSweepCodePay sweepCodePay(AuthInfoVo authInfoVo, PgwReqSweepCodePay pgwReqSweepCodePay)
			throws Exception {
		return null;
	}

	@Override
	public PgwRespRevoke revoke(AuthInfoVo authInfoVo, PgwReqRevoke pgwReqRevoke) throws Exception {
		return null;

	}

	/**
	 * @param authInfoVo
	 * @param pgwReqClose
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespClose close(AuthInfoVo authInfoVo, PgwReqClose pgwReqClose) throws Exception {
		PgwRespClose pgwRespClose = new PgwRespClose();
		pgwRespClose.setRespCode(RetCode.Success.RET_10000);
		pgwRespClose.setRespMsg(RetCode.Success.RET_10000.getMessage());
		return pgwRespClose;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqQueryOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespQueryOrder queryOrder(AuthInfoVo authInfoVo, PgwReqQueryOrder pgwReqQueryOrder) throws Exception {
		PgwRespQueryOrder pgwRespQueryOrder = new PgwRespQueryOrder();
		SortedMap<String,String> signParam =  new TreeMap<String, String>();
		signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
		signParam.put("branchNo", KasiteConfig.getNetPay(NetPayEnum.branchNo, authInfoVo.getConfigKey()));//分行号，4位数字
		signParam.put("merchantNo", KasiteConfig.getNetPay(NetPayEnum.merchantNo, authInfoVo.getConfigKey()));//商户号，6位数字
		signParam.put("date", pgwReqQueryOrder.getPayTime().substring(0,7));//商户订单日期，格式：yyyyMMdd
		if(StringUtil.isEmpty(pgwReqQueryOrder.getTransactionNo()) ) {
			signParam.put("type", NetPayConstant.TYPE_B);//B：按商户订单日期和订单号查询；
			signParam.put("orderNo", pgwReqQueryOrder.getOrderId());//B：按商户订单日期和订单号查询；
		}else {
			signParam.put("type", NetPayConstant.TYPE_A);//A：按银行订单流水号查询
			signParam.put("bankSerialNo", pgwReqQueryOrder.getTransactionNo());//A：按银行订单流水号查询
		}
		String sign = NetPay.sign(signParam, KasiteConfig.getNetPay(NetPayEnum.secretKey, authInfoVo.getConfigKey()));//sIJ7bZAVetd1p1cE
		
		JSONObject inParam = new JSONObject();
		inParam.put("version", NetPayConstant.VSERSION_1_0);
		inParam.put("charset", NetPayConstant.CHARSET_UTF8);
		inParam.put("sign", sign);
		inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
		inParam.put("reqData", JSONObject.toJSON(signParam));
		
		StringBuffer param = new StringBuffer();
		param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
		
		String resp = HttpsClientUtils.httpsPost(pgwReqQueryOrder.getOrderId(),ApiModule.NetPay.NETPAY_QUERYSINGLEORDER, NetPayConstant.URL_QUERYSINGLEORDER, param.toString());
		if( !StringUtil.isEmpty(resp)) {
			JSONObject retJson = JSONObject.parseObject(resp);
			JSONObject rspDataJson = retJson.getJSONObject("rspData");
			if( rspDataJson != null ) {
				if(NetPayConstant.RSPCODE_SUC0000.equals(rspDataJson.getString("rspCode"))) {//成功
					pgwRespQueryOrder.setRespCode(RetCode.Success.RET_10000);
					pgwRespQueryOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
					String orderStatus = rspDataJson.getString("orderStatus"); 
					switch (orderStatus) {
					case NetPayConstant.ORDERSTATUS_0://0:已结帐
						pgwRespQueryOrder.setOrderState(KstHosConstant.I2);
						pgwRespQueryOrder.setOrderId(rspDataJson.getString("orderNo"));
						pgwRespQueryOrder.setPayTime(rspDataJson.getString("settleDate"));
						pgwRespQueryOrder.setPrice(StringUtil.yuanChangeFenInt(rspDataJson.getString("orderAmount")));
						pgwRespQueryOrder.setTransactionNo(rspDataJson.getString("bankSerialNo"));
						break;
						//招行对接人说目前只有0，暂时无其他,虽然文档上有
//					case NetPayConstant.ORDERSTATUS_1://1:已撤销
//						pgwRespQueryOrder.setOrderState(KstHosConstant.I6);
//						break;
//					case NetPayConstant.ORDERSTATUS_2://2:部分结帐
//						pgwRespQueryOrder.setOrderState(KstHosConstant.I1);
//						break;
//					case NetPayConstant.ORDERSTATUS_4://4:未结帐
//						pgwRespQueryOrder.setOrderState(KstHosConstant.I0);
//						break;
//					case NetPayConstant.ORDERSTATUS_7://7:冻结金额已经全部结账
//						pgwRespQueryOrder.setOrderState(KstHosConstant.I6);
//					case NetPayConstant.ORDERSTATUS_8://8:冻结交易，冻结金额只结帐了一部分
//						pgwRespQueryOrder.setOrderState(KstHosConstant.I4);
//						break;
					default:
						//TODO 待完善
						pgwRespQueryOrder.setOrderState(KstHosConstant.I0);
						break;
				}
				}else {//失败
					RetCode errorRetCode =netPayErrorCodeParseRetCode(rspDataJson.getString("rspCode"));
					String netPayRspMsg  = rspDataJson.getString("rspMsg");
					pgwRespQueryOrder.setRespCode(errorRetCode);
					pgwRespQueryOrder.setRespMsg(errorRetCode.getMessage()+"|netPayRspMsg:"+netPayRspMsg);
				}
			}else {
				pgwRespQueryOrder.setRespCode(RetCode.Common.ERROR_THIRD_PARTY);
				pgwRespQueryOrder.setRespMsg(RetCode.Common.ERROR_THIRD_PARTY.getMessage()+"|调用招行一网通接口出参异常！|resp:"+resp);
			}
		}else {
			pgwRespQueryOrder.setRespCode(RetCode.Common.ERROR_THIRD_PARTY);
			pgwRespQueryOrder.setRespMsg(RetCode.Common.ERROR_THIRD_PARTY.getMessage()+"|调用招行一网通接口异常！");
		}
		return pgwRespQueryOrder;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqQueryRefundOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespQueryRefundOrder queryRefundOrder(AuthInfoVo authInfoVo,
			PgwReqQueryRefundOrder pgwReqQueryRefundOrder) throws Exception {
		PgwRespQueryRefundOrder pgwRespQueryRefundOrder = new PgwRespQueryRefundOrder();
		SortedMap<String,String> signParam =  new TreeMap<String, String>();
		signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
		signParam.put("branchNo", KasiteConfig.getNetPay(NetPayEnum.branchNo, authInfoVo.getConfigKey()));//分行号，4位数字
		signParam.put("merchantNo", KasiteConfig.getNetPay(NetPayEnum.merchantNo, authInfoVo.getConfigKey()));//商户号，6位数字
		signParam.put("date", pgwReqQueryRefundOrder.getPayTime().substring(0,7));
		if( !StringUtil.isEmpty(pgwReqQueryRefundOrder.getRefundId())) {
			signParam.put("type",NetPayConstant.TYPE_A);
			signParam.put("bankSerialNo",pgwReqQueryRefundOrder.getRefundId());
		}else if(!StringUtil.isEmpty(pgwReqQueryRefundOrder.getOrderId())
				&& !StringUtil.isEmpty(pgwReqQueryRefundOrder.getRefundOrderId())){
			signParam.put("type",NetPayConstant.TYPE_B);//B：按商户订单号+商户退款流水号查单笔
			signParam.put("merchantSerialNo",pgwReqQueryRefundOrder.getRefundOrderId());
			signParam.put("orderNo",pgwReqQueryRefundOrder.getOrderId());
		}
		String sign = NetPay.sign(signParam, KasiteConfig.getNetPay(NetPayEnum.secretKey, authInfoVo.getConfigKey()));//sIJ7bZAVetd1p1cE
		JSONObject inParam = new JSONObject();
		inParam.put("version", NetPayConstant.VSERSION_1_0);
		inParam.put("charset", NetPayConstant.CHARSET_UTF8);
		inParam.put("sign", sign);
		inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
		inParam.put("reqData", JSONObject.toJSON(signParam));
		
		StringBuffer param = new StringBuffer();
		param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
		
		String resp = HttpsClientUtils.httpsPost(pgwReqQueryRefundOrder.getOrderId(),ApiModule.NetPay.NETPAY_QUERYSETTLEDREFUND, NetPayConstant.URL_QUERYSETTLEDREFUND, param.toString());
		if( !StringUtil.isEmpty(resp)) {
			JSONObject retJson = JSONObject.parseObject(resp);
			JSONObject rspDataJson = retJson.getJSONObject("rspData");
			if( rspDataJson != null ) {
				if(NetPayConstant.RSPCODE_SUC0000.equals(rspDataJson.getString("rspCode"))) {//成功
					Integer dataCount = rspDataJson.getInteger("dataCount"); 
					if( dataCount!=null && dataCount.intValue()==1) {
						//目前接口只查询一笔退款订单号
						pgwRespQueryRefundOrder.setRespCode(RetCode.Success.RET_10000);
						pgwRespQueryRefundOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
						String dataList = rspDataJson.getString("dataList"); 
						String[] bills = dataList.split("\\r\\n");
						String billObj = bills[1];
						String[] values = billObj.split(",`");
						String orderStatus = values[6]; 
						pgwRespQueryRefundOrder.setRefundId(values[4]);
						pgwRespQueryRefundOrder.setRefundOrderId(values[5]);
						pgwRespQueryRefundOrder.setRefundPrice(StringUtil.yuanChangeFenInt(values[8]));
						pgwRespQueryRefundOrder.setRefundTime(values[12]+values[13]);//yyyyMMdd+HHmmss
						switch (orderStatus) {
						case NetPayConstant.ORDERSTATUS_210:
							pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
							break;
						case NetPayConstant.ORDERSTATUS_219:
							pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
							break;
						case NetPayConstant.ORDERSTATUS_240:
							pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
							break;
						case NetPayConstant.ORDERSTATUS_249:
							pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
							break;
						default:
							//TODO 待完善
							pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I7);
							break;
						}
					}else {
						pgwRespQueryRefundOrder.setRespCode(RetCode.Common.ERROR_PARAM);
						pgwRespQueryRefundOrder.setRespMsg("查询退费入参错误，返回多笔退费记录！");
					}
					
				}else {//失败
					RetCode errorRetCode =netPayErrorCodeParseRetCode(rspDataJson.getString("rspCode"));
					String netPayRspMsg  = rspDataJson.getString("rspMsg");
					pgwRespQueryRefundOrder.setRespCode(errorRetCode);
					pgwRespQueryRefundOrder.setRespMsg(errorRetCode.getMessage()+"|netPayRspMsg:"+netPayRspMsg);
				}
			}else {
				pgwRespQueryRefundOrder.setRespCode(RetCode.Common.ERROR_THIRD_PARTY);
				pgwRespQueryRefundOrder.setRespMsg(RetCode.Common.ERROR_THIRD_PARTY.getMessage()+"|调用招行一网通接口出参异常！|resp:"+resp);
			}
		}else {
			pgwRespQueryRefundOrder.setRespCode(RetCode.Common.ERROR_THIRD_PARTY);
			pgwRespQueryRefundOrder.setRespMsg(RetCode.Common.ERROR_THIRD_PARTY.getMessage()+"|调用招行一网通接口异常！");
		}
		return pgwRespQueryRefundOrder;
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespWapUniteOrder wapUniteOrder(AuthInfoVo authInfoVo, PgwReqWapUniteOrder pgwReqUniteOrder)
			throws Exception {
		PgwRespWapUniteOrder pgwRespWapUniteOrder = new PgwRespWapUniteOrder();
		SortedMap<String,String> signParam =  new TreeMap<String, String>();
		signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
		signParam.put("branchNo", KasiteConfig.getNetPay(NetPayEnum.branchNo, authInfoVo.getConfigKey()));//分行号，4位数字
		signParam.put("merchantNo", KasiteConfig.getNetPay(NetPayEnum.merchantNo, authInfoVo.getConfigKey()));//商户号，6位数字
		signParam.put("date", DateOper.getNow("yyyyMMdd"));//订单日期,格式：yyyyMMdd
		signParam.put("orderNo", pgwReqUniteOrder.getOrderId());//订单号
		signParam.put("amount", StringUtil.fenChangeYuan(pgwReqUniteOrder.getPrice()));//金额，格式：xxxx.xx
		//signParam.put("expireTimeSpan", NetPayConstant.EXPIRETIMESPAN_15);//过期时间跨度，必须为大于零的整数，单位为分钟。
		signParam.put("payNoticeUrl", KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.netpay, authInfoVo.getConfigKey(),authInfoVo.getClientId(), 
				pgwReqUniteOrder.getOpenId(), authInfoVo.getSessionKey(),pgwReqUniteOrder.getOrderId()));
		//signParam.put("payNoticePara", merchantNo);//成功支付结果通知附加参数,该参数在发送成功支付结果通知时，
		//将原样返回商户注意：该参数可为空，商户如果需要不止一个参数，可以自行把参数组合、拼装，但组合后的结果不能带有’&’字符。
		if( !StringUtil.isEmpty(pgwReqUniteOrder.getReturnUrl())) {
			signParam.put("returnUrl",pgwReqUniteOrder.getReturnUrl());//返回商户地址，支付成功页面、支付失败页面上“返回商户”按钮跳转地址。
		}
		if (null != pgwReqUniteOrder.getIsLimitCredit() && KstHosConstant.I1.equals(pgwReqUniteOrder.getIsLimitCredit())) {
			//限制使用信用卡
			signParam.put("cardType", NetPayConstant.CARDTYPE_A);
		}
		
//		signParam.put("agrNo", merchantNo);
//		signParam.put("merchantSerialNo", merchantNo);
//		signParam.put("clientIP", merchantNo);
//		signParam.put("userID", merchantNo);
//		signParam.put("mobile", merchantNo);
//		signParam.put("riskLevel", merchantNo);
//		signParam.put("signNoticeUrl", merchantNo);
//		signParam.put("signNoticePara", merchantNo);
		String sign = NetPay.sign(signParam, KasiteConfig.getNetPay(NetPayEnum.secretKey, authInfoVo.getConfigKey()));//sIJ7bZAVetd1p1cE
		//签名的时候，不用使用转译。
		signParam.put("returnUrl", URLEncoder.encode(pgwReqUniteOrder.getReturnUrl(), "UTF-8"));//返回商户地址，支付成功页面、支付失败页面上“返回商户”按钮跳转地址。
		JSONObject inParam = new JSONObject();
		inParam.put("version", NetPayConstant.VSERSION_1_0);
		inParam.put("charset", NetPayConstant.CHARSET_UTF8);
		inParam.put("sign", sign);
		inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
		inParam.put("reqData", JSONObject.toJSON(signParam));
		
		StringBuffer param = new StringBuffer();
		param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
		
		String resp = HttpsClientUtils.httpsPost(pgwReqUniteOrder.getOrderId(),ApiModule.NetPay.NETPAY_MB_EUSERPAY, NetPayConstant.URL_MB_EUSERPAY, param.toString());
		pgwRespWapUniteOrder.setRespCode(RetCode.Success.RET_10000);
		pgwRespWapUniteOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
		pgwRespWapUniteOrder.setPayInfo(resp);
		return pgwRespWapUniteOrder;
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
		String billDateStr = DateOper.formatDate(billDate, "yyyyMMdd");
		List<MchBill> mchBillList = downloadPayBillByPage(billDateStr,authInfoVo.getConfigKey());
		mchBillList.addAll(downloadRefundBillByPage(billDateStr,authInfoVo.getConfigKey()));
		pgwRespDownloadBill.setRespCode(RetCode.Success.RET_10000);
		pgwRespDownloadBill.setRespMsg(RetCode.Success.RET_10000.getMessage());
		pgwRespDownloadBill.setMchBillList(mchBillList);
		return pgwRespDownloadBill;
	}
	
	/**
	 * 下载支付账单，里面有递归
	 * @param billDate
	 * @param configKey
	 * @param nextKeyValue
	 * @return
	 * @throws Exception
	 */
	private List<MchBill> downloadPayBillByPage(String billDate,String configKey) throws Exception {
		String hasnext = NetPayConstant.HASNEXT_Y;
		String nextKeyValue = null;
		List<MchBill> mchBillList = new ArrayList<MchBill>();
		while ( NetPayConstant.HASNEXT_Y.equals(hasnext)) {
			SortedMap<String,String> signParam =  new TreeMap<String, String>();
			signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
			signParam.put("branchNo", KasiteConfig.getNetPay(NetPayEnum.branchNo, configKey));//分行号，4位数字
			signParam.put("merchantNo", KasiteConfig.getNetPay(NetPayEnum.merchantNo, configKey));//商户号，6位数字
			signParam.put("beginDate", billDate);
			signParam.put("endDate", billDate);
			signParam.put("operatorNo", KasiteConfig.getNetPay(NetPayEnum.operatorNo, configKey));
			if( !StringUtil.isEmpty(nextKeyValue)) {
				signParam.put("nextKeyValue",nextKeyValue);
			}
			String sign = NetPay.sign(signParam, KasiteConfig.getNetPay(NetPayEnum.secretKey, configKey));//sIJ7bZAVetd1p1cE
			JSONObject inParam = new JSONObject();
			inParam.put("version", NetPayConstant.VSERSION_1_0);
			inParam.put("charset", NetPayConstant.CHARSET_UTF8);
			inParam.put("sign", sign);
			inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
			inParam.put("reqData", JSONObject.toJSON(signParam));
			
			StringBuffer param = new StringBuffer();
			param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
			
			String resp = HttpsClientUtils.httpsPost(null,ApiModule.NetPay.NETPAY_QUERYSETTLEDORDERBYMERCHANTDATE, NetPayConstant.URL_QUERYSETTLEDORDERBYMERCHANTDATE, param.toString());
			if( !StringUtil.isEmpty(resp)) {
				JSONObject retJson = JSONObject.parseObject(resp);
				JSONObject rspDataJson = retJson.getJSONObject("rspData");
				if( rspDataJson != null ) {
					if(NetPayConstant.RSPCODE_SUC0000.equals(rspDataJson.getString("rspCode"))) {//成功
						Integer dataCount = rspDataJson.getInteger("dataCount");
						if( dataCount.intValue() >0 ) {//有数据
							String dataList = rspDataJson.getString("dataList");
							String[] bills = dataList.split("\\r\\n");
							if(bills==null || bills.length>0 && bills[0].indexOf(KstHosConstant.WX_BILL_FAIL)>0){
								throw new RRException(RetCode.Common.ERROR_THIRD_PARTY);
							}
							for (int i = 1; i < bills.length; i++) {
								String billObj = bills[i];
								String[] values = billObj.split(",`");
								MchBill bill = new MchBill();
								bill.setConfigKey(configKey);
								bill.setMchTradeNo(values[4]);
								bill.setOrderId(values[3]);
								bill.setOrderType(KstHosConstant.BILL_ORDER_TYPE_1);
								bill.setPayPrice(StringUtil.yuanChangeFenInt(values[11]));
								bill.setRefundMchTradeNo(null);
								bill.setRefundOrderId(null);
								bill.setRefundPrice(null);
								bill.setTradeType(null);
								bill.setTransDate(DateOper.parse2Timestamp(values[14]+values[15]));
								mchBillList.add(bill);
							}
							if(NetPayConstant.HASNEXT_Y.equals(rspDataJson.getString("hasNext")) ) {
								hasnext = NetPayConstant.HASNEXT_Y;//存在下一页
								nextKeyValue = rspDataJson.getString("nextKeyValue");
							}else {
								hasnext = NetPayConstant.HASNEXT_N;
							}
						}else {
							hasnext = NetPayConstant.HASNEXT_N;
						}
						
					}
				}
			}
		}
		return mchBillList;
	}
	
	private List<MchBill> downloadRefundBillByPage(String billDate,String configKey) throws Exception {
		String hasnext = NetPayConstant.HASNEXT_Y;
		String nextKeyValue = null;
		List<MchBill> mchBillList = new ArrayList<MchBill>();
		while ( NetPayConstant.HASNEXT_Y.equals(hasnext)) {
			SortedMap<String,String> signParam =  new TreeMap<String, String>();
			signParam.put("dateTime", DateOper.getNow("yyyyMMddHHmmss"));//请求时间,格式：yyyyMMddHHmmss商户发起该请求的当前时间，精确到秒
			signParam.put("branchNo", KasiteConfig.getNetPay(NetPayEnum.branchNo, configKey));//分行号，4位数字
			signParam.put("merchantNo", KasiteConfig.getNetPay(NetPayEnum.merchantNo, configKey));//商户号，6位数字
			signParam.put("beginDate", billDate);
			signParam.put("endDate", billDate);
			signParam.put("operatorNo", KasiteConfig.getNetPay(NetPayEnum.operatorNo, configKey));
			if( !StringUtil.isEmpty(nextKeyValue)) {
				signParam.put("nextKeyValue",nextKeyValue);
			}
			String sign = NetPay.sign(signParam, KasiteConfig.getNetPay(NetPayEnum.secretKey, configKey));//sIJ7bZAVetd1p1cE
			JSONObject inParam = new JSONObject();
			inParam.put("version", NetPayConstant.VSERSION_1_0);
			inParam.put("charset", NetPayConstant.CHARSET_UTF8);
			inParam.put("sign", sign);
			inParam.put("signType", NetPayConstant.SIGNTYPE_SHA256);
			inParam.put("reqData", JSONObject.toJSON(signParam));
			
			StringBuffer param = new StringBuffer();
			param.append("jsonRequestData="+ inParam.toJSONString() +"&charset="+NetPayConstant.CHARSET_UTF8);
			
			String resp = HttpsClientUtils.httpsPost(null,ApiModule.NetPay.NETPAY_QUERYREFUNDBYDATE, NetPayConstant.URL_QUERYREFUNDBYDATE, param.toString());
			if( !StringUtil.isEmpty(resp)) {
				JSONObject retJson = JSONObject.parseObject(resp);
				JSONObject rspDataJson = retJson.getJSONObject("rspData");
				if( rspDataJson != null ) {
					if(NetPayConstant.RSPCODE_SUC0000.equals(rspDataJson.getString("rspCode"))) {//成功
						Integer dataCount = rspDataJson.getInteger("dataCount");
						if( dataCount.intValue() >0 ) {//有数据
							String dataList = rspDataJson.getString("dataList");
							String[] bills = dataList.split("\\r\\n");
							if(bills==null || bills.length>0 && bills[0].indexOf(KstHosConstant.WX_BILL_FAIL)>0){
								throw new RRException(RetCode.Common.ERROR_THIRD_PARTY);
							}
							for (int i = 1; i < bills.length; i++) {
								String billObj = bills[i];
								String[] values = billObj.split(",`");
								MchBill bill = new MchBill();
								bill.setConfigKey(configKey);
								bill.setMchTradeNo(values[4]);
								bill.setOrderId(values[3]);
								bill.setOrderType(KstHosConstant.BILL_ORDER_TYPE_2);
								bill.setPayPrice(null);
								bill.setRefundMchTradeNo(values[6]);
								bill.setRefundOrderId(null);
								bill.setRefundPrice(StringUtil.yuanChangeFenInt(values[9]));
								bill.setTradeType(null);
								bill.setTransDate(DateOper.parse2Timestamp(values[12]+values[13]));
								mchBillList.add(bill);
							}
							if(NetPayConstant.HASNEXT_Y.equals(rspDataJson.getString("hasNext")) ) {
								hasnext = NetPayConstant.HASNEXT_Y;
								nextKeyValue = rspDataJson.getString("nextKeyValue");
							}else {
								hasnext = NetPayConstant.HASNEXT_N;
							}
						}else {
							hasnext = NetPayConstant.HASNEXT_N;
						}
					}
				}
			}
		}
		return mchBillList;
	}
	
	public static RetCode netPayErrorCodeParseRetCode(String errorCode) {
		if( StringUtil.isEmpty(errorCode) ) {
			return RetCode.Common.ERROR_SYSTEM;
		}
		RetCode retCode = null;
		switch (errorCode) {
		case NetPayConstant.RSPCODE_MSS3411:
			//签名错误
			retCode = RetCode.Pay.SIGNATURE_FAILED;
			break;
		case NetPayConstant.RSPCODE_MSS3804:
			retCode = RetCode.Pay.ERROR_REFUND;
			break;
		case NetPayConstant.RSPCODE_MSS3202:
			retCode = RetCode.Pay.ERROR_REFUND;
			break;
		default:
			retCode = RetCode.Common.ERROR_SYSTEM;
			break;
		}
		return retCode;
	}
	

	/**
	 * @return
	 */
	@Override
	public String mchType() {
		return ChannelTypeEnum.netpay.name();
	}
}
