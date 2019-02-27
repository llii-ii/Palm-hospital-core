package com.kasite.client.wechat.service;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.HttpsClientUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.ClientCustomSSL;
import com.kasite.core.common.util.wechat.RequestHandler;
import com.kasite.core.common.util.wechat.TenpayConstant;
import com.kasite.core.common.util.wechat.TenpayUtil;
import com.kasite.core.common.util.wechat.TenpayXMLUtil;
import com.kasite.core.serviceinterface.module.pay.IPaymentGateWayService;
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

@Service("wechatPay")
public class WechatPayGateWayServiceImpl implements IPaymentGateWayService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);

	/**
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespUniteOrder uniteOrder(AuthInfoVo authInfoVo, PgwReqUniteOrder pgwReqUniteOrder) throws Exception {
		PgwRespUniteOrder pgwRespUniteOrder = new  PgwRespUniteOrder();
		// 随机数
		String nonceStr = TenpayUtil.getNonceStr();
		SortedMap<String, String> packageParams = null;
		String app_id = null;
		String configKey = authInfoVo.getConfigKey();
		if ("T".equals(KasiteConfig.getWxPay(WXPayEnum.is_parent_mode, configKey))) {
			// 服务商模式
			app_id = KasiteConfig.getWxPay(WXPayEnum.wx_parent_app_id, configKey);
			
			packageParams = TenpayUtil.getPackageParamsByParent(authInfoVo.getClientId(),configKey,pgwReqUniteOrder.getOpenId(),
					pgwReqUniteOrder.getOrderId(),pgwReqUniteOrder.getPrice(),pgwReqUniteOrder.getBody(), 
					pgwReqUniteOrder.getRemoteIp(),pgwReqUniteOrder.getIsLimitCredit(),TenpayConstant.TRADE_TYPE_JSAPI,
					authInfoVo.getSessionKey());
		} else {
			app_id = KasiteConfig.getWxPay(WXPayEnum.wx_app_id, configKey);
			packageParams = TenpayUtil.getPackageParams(authInfoVo.getClientId(),configKey,pgwReqUniteOrder.getOpenId(),
					pgwReqUniteOrder.getOrderId(),pgwReqUniteOrder.getPrice(), pgwReqUniteOrder.getBody(), 
					pgwReqUniteOrder.getRemoteIp(),pgwReqUniteOrder.getIsLimitCredit(),TenpayConstant.TRADE_TYPE_JSAPI,
					authInfoVo.getSessionKey());
		}
		String mch_key = KasiteConfig.getWxPayMchKey(configKey);
		// #.md5编码并转成大写 签名：
		String sign = TenpayUtil.createSign(packageParams, mch_key);
		// #.最终的提交xml：
		String xml = TenpayUtil.setMapToXml(packageParams, sign);
		// 根据订单获取支付相关信息（调用支付统一接口）
		String unifiedOrderResult = HttpsClientUtils.httpsPost(pgwReqUniteOrder.getOrderId(),ApiModule.WeChat.pay_unifiedorder,TenpayConstant.UNIFIEDORDER_URL, xml);
		JSONObject unifiedOrderRetJson = JSONObject.parseObject(TenpayUtil.xml2JSON(unifiedOrderResult));
		String returnCode = unifiedOrderRetJson.getString(TenpayConstant.RETURN_CODE);
		String resultCode = unifiedOrderRetJson.getString(TenpayConstant.RESULT_CODE);
		String returnMsg = unifiedOrderRetJson.getString(TenpayConstant.RETURN_MSG);
		if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
			if( TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)){
				pgwRespUniteOrder.setRespCode(RetCode.Success.RET_10000);
				pgwRespUniteOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
				String prepayId = unifiedOrderRetJson.getString("prepay_id");
				// 返回给前端的数据集
				SortedMap<String, String> returnPackage = TenpayUtil.getReturnPackage(app_id,nonceStr, prepayId);
				
				String finalSign = TenpayUtil.createSign(returnPackage, mch_key);
				returnPackage.put("paySign", finalSign);
				// String params = DataUtils.getParams(finalpackage,finalSign);
				JSONObject data = JSONObject.parseObject(JSON.toJSONString(returnPackage));//把map转为json数据
				pgwRespUniteOrder.setPayInfo(data.toString());
			}else {
				/** 错误代码 */
				String errCodeDes = unifiedOrderRetJson.getString(TenpayConstant.ERR_CODE_DES);
				RetCode errorRetCode =weChatErrorCodeParseRetCode(unifiedOrderRetJson.getString(TenpayConstant.ERR_CODE));
				pgwRespUniteOrder.setRespCode(errorRetCode);
				pgwRespUniteOrder.setRespMsg(errorRetCode.getMessage()+"|err_code_des:"+errCodeDes);
			}
		}else {
			pgwRespUniteOrder.setRespCode(RetCode.Common.ERROR_CALL_WEIXIN_PAY);
			pgwRespUniteOrder.setRespMsg(RetCode.Common.ERROR_CALL_WEIXIN_PAY.getMessage()+"|return_msg:"+returnMsg);
		}
		return pgwRespUniteOrder;
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
		String configKey = pgwReqRefund.getPayConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);

		if (!StringUtil.isEmpty(pgwReqRefund.getOrderId())) {
			packageParams.put("out_trade_no", pgwReqRefund.getOrderId());
		} else if (!StringUtil.isEmpty(pgwReqRefund.getTransactionNo())) {
			packageParams.put("transaction_id", pgwReqRefund.getTransactionNo());
		}

		packageParams.put("nonce_str", TenpayUtil.getNonceStr());
		packageParams.put("out_refund_no", pgwReqRefund.getRefundOrderId());
		packageParams.put("total_fee", pgwReqRefund.getTotalPrice().toString());
		packageParams.put("refund_fee", pgwReqRefund.getRefundPrice().toString());
//		packageParams.put("op_user_id", TenpayConstant.MCH_ID);
		packageParams.put("op_user_id", KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configKey));

		String sign = reqHandler.createSign(packageParams);
		packageParams.put("sign", sign);
		// 退款记录
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		String returnXmlStr = "";
		returnXmlStr = ClientCustomSSL.call(pgwReqRefund.getOrderId(),ApiModule.WeChat.pay_refund,TenpayConstant.REFUND_URL,configKey,arrayToXml);
		reqHandler.doParse(returnXmlStr);
		boolean flag = reqHandler.isValidWXSign();
		if (flag) {
			String returnCode = reqHandler.getParameter("return_code").trim();
			String returnMsg = reqHandler.getParameter("return_msg").trim();
			String resultCode = reqHandler.getParameter("result_code").toString();
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					pgwRespRefund.setRespCode(RetCode.Success.RET_10000);
					pgwRespRefund.setRespMsg( "申请退款成功！");
					String refundId = reqHandler.getParameter("refund_id").toString();
					pgwRespRefund.setRefundId(refundId);
				} else {
					/** 错误代码 */
					String errCodeDes = reqHandler.getParameter(TenpayConstant.ERR_CODE_DES);
					RetCode errorRetCode =weChatErrorCodeParseRetCode(reqHandler.getParameter(TenpayConstant.ERR_CODE));
					pgwRespRefund.setRespCode(errorRetCode);
					pgwRespRefund.setRespMsg(errorRetCode.getMessage()+"|err_code_des:"+errCodeDes);
				}
			} else {
				pgwRespRefund.setRespCode(RetCode.Common.ERROR_CALL_WEIXIN_PAY);
				pgwRespRefund.setRespMsg(RetCode.Common.ERROR_CALL_WEIXIN_PAY.getMessage()+"|return_msg:"+returnMsg);
			}
		} else {
			pgwRespRefund.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
			pgwRespRefund.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
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
		PgwRespSweepCodePay pgwRespSweepCodePay = new PgwRespSweepCodePay();
		String configKey = authInfoVo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		packageParams.put("body", pgwReqSweepCodePay.getBody());
		packageParams.put("out_trade_no", pgwReqSweepCodePay.getOrderId());
		packageParams.put("total_fee",pgwReqSweepCodePay.getTotalPrice().toString());
		packageParams.put("spbill_create_ip", pgwReqSweepCodePay.getRemoteIp());
		packageParams.put("auth_code", pgwReqSweepCodePay.getAuthCode());
		if (null != pgwReqSweepCodePay.getIsLimitCredit() && KstHosConstant.I1.equals(pgwReqSweepCodePay.getIsLimitCredit())) {
			//限制使用信用卡
			packageParams.put("limit_pay", "no_credit");
		}
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		String returnXmlStr = null;
		
		try {
			returnXmlStr = HttpsClientUtils.httpsPost(pgwReqSweepCodePay.getOrderId(),ApiModule.WeChat.pay_micropay,TenpayConstant.MICROPAY_URL, arrayToXml);
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,
					new LogBody(authInfoVo).set("auth_code", pgwReqSweepCodePay.getAuthCode()).set("total_fee", pgwReqSweepCodePay.getTotalPrice())
							.set("out_trade_no", pgwReqSweepCodePay.getOrderId()).set("result", "调用微信接口超时或者异常"),e);
			pgwRespSweepCodePay.setRespCode(RetCode.Common.ERROR_CALL_WEIXIN_PAY);
			pgwRespSweepCodePay.setRespMsg(RetCode.Common.ERROR_CALL_WEIXIN_PAY.getMessage());
			return pgwRespSweepCodePay;
		}
		
		reqHandler.doParse(returnXmlStr);
		/** 校验签名 */
		boolean flag = reqHandler.isValidWXSign();
		
		if (flag) {
			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE).trim();
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			/** 通信标识SUCCESS时 */
			if (returnCode.equals(TenpayConstant.RETURN_CODE_SUCCESS)) {
				String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE).trim();
				/** 业务结果SUCCESS时 */
				if (resultCode.equals(TenpayConstant.RETURN_CODE_SUCCESS)) {
					pgwRespSweepCodePay.setRespCode(RetCode.Success.RET_10000);
					pgwRespSweepCodePay.setRespMsg(RetCode.Success.RET_10000.getMessage());
					pgwRespSweepCodePay.setPayTime(reqHandler.getParameter("time_end").toString());
					pgwRespSweepCodePay.setTransactionNo(reqHandler.getParameter("transaction_id").toString());
					//TODO
				} else {
					String errCodeDes = reqHandler.getParameter(TenpayConstant.ERR_CODE_DES);
					RetCode errorRetCode =weChatErrorCodeParseRetCode(reqHandler.getParameter(TenpayConstant.ERR_CODE));
					pgwRespSweepCodePay.setRespCode(errorRetCode);
					pgwRespSweepCodePay.setRespMsg(errorRetCode.getMessage()+"|err_code_des:"+errCodeDes);
				}
			} else {
				pgwRespSweepCodePay.setRespCode(RetCode.Common.ERROR_CALL_WEIXIN_PAY);
				pgwRespSweepCodePay.setRespMsg(RetCode.Common.ERROR_CALL_WEIXIN_PAY.getMessage()+"|return_msg:"+returnMsg);
			}
		} else {
			pgwRespSweepCodePay.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
			pgwRespSweepCodePay.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
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
		String configKey = authInfoVo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		if (!StringUtil.isEmpty(pgwReqRevoke.getTransactionNo())) {
			packageParams.put("transaction_id", pgwReqRevoke.getTransactionNo());
		} else {
			packageParams.put("out_trade_no", pgwReqRevoke.getOrderId());
		}
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		String returnXmlStr = ClientCustomSSL.call(pgwReqRevoke.getOrderId(),ApiModule.WeChat.secapi_pay_reverse,TenpayConstant.REVERSE_URL,configKey, arrayToXml);
		reqHandler.doParse(returnXmlStr);
		/** 校验签名 */
		boolean flag = reqHandler.isValidWXSign();
		if (flag) {
			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE).trim();
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			/** return_code为SUCCESS 并且 返回信息为空时 */
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE).trim();
				/** return_code和result_code都为SUCCESS时 */
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					pgwRespRevoke.setRespCode(RetCode.Success.RET_10000);
					pgwRespRevoke.setRespMsg(RetCode.Success.RET_10000.getMessage());
					//TODO
				} else {
					/** 错误代码 */
					String errCodeDes = reqHandler.getParameter(TenpayConstant.ERR_CODE_DES);
					RetCode errorRetCode =weChatErrorCodeParseRetCode(reqHandler.getParameter(TenpayConstant.ERR_CODE));
					pgwRespRevoke.setRespCode(errorRetCode);
					pgwRespRevoke.setRespMsg(errorRetCode.getMessage()+"|err_code_des:"+errCodeDes);
				}
			} else {
				pgwRespRevoke.setRespCode(RetCode.Common.ERROR_CALL_WEIXIN_PAY);
				pgwRespRevoke.setRespMsg(RetCode.Common.ERROR_CALL_WEIXIN_PAY.getMessage()+"|return_msg:"+returnMsg);
			}
		} else {
			pgwRespRevoke.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
			pgwRespRevoke.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
		}
		return pgwRespRevoke;
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
		String configKey = authInfoVo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		packageParams.put("out_trade_no", pgwReqClose.getOrderId());
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		String returnXmlStr = HttpsClientUtils.httpsPost(pgwReqClose.getOrderId(),ApiModule.WeChat.pay_closeorder,TenpayConstant.CLOSEORDER_URL, arrayToXml);
		reqHandler.doParse(returnXmlStr);
		/** 校验签名 */
		boolean flag = reqHandler.isValidWXSign();
		if (flag) {
			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE).trim();
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			/** return_code为SUCCESS 并且 返回信息为空时 */
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE).trim();
				/** return_code和result_code都为SUCCESS时 */
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					pgwRespClose.setRespCode(RetCode.Success.RET_10000);
					pgwRespClose.setRespMsg(RetCode.Success.RET_10000.getMessage());

				} else {
					RetCode errorRetCode =weChatErrorCodeParseRetCode(reqHandler.getParameter(TenpayConstant.ERR_CODE));
					String errCodeDes  = reqHandler.getParameter(TenpayConstant.ERR_CODE_DES);
					pgwRespClose.setRespCode(errorRetCode);
					pgwRespClose.setRespMsg(errorRetCode.getMessage()+"|err_code_des:"+errCodeDes);
				}
			} else {
				pgwRespClose.setRespCode(RetCode.Common.ERROR_CALL_WEIXIN_PAY);
				pgwRespClose.setRespMsg(RetCode.Common.ERROR_CALL_WEIXIN_PAY.getMessage()+"|return_msg:"+returnMsg);
			}
		} else {
			pgwRespClose.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
			pgwRespClose.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
		}
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
		String configKey = authInfoVo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		if (!StringUtil.isEmpty(pgwReqQueryOrder.getTransactionNo())) {
			packageParams.put("transaction_id", pgwReqQueryOrder.getTransactionNo());
		} else {
			packageParams.put("out_trade_no", pgwReqQueryOrder.getOrderId());
		}
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		String returnXmlStr = HttpsClientUtils.httpsPost(pgwReqQueryOrder.getOrderId(),ApiModule.WeChat.pay_orderquery,TenpayConstant.ORDERQUERY_URL, arrayToXml);
		reqHandler.doParse(returnXmlStr);
		/** 校验签名 */
		boolean flag = reqHandler.isValidWXSign();
		if (flag) {
			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE).trim();
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			/** return_code为SUCCESS 并且 返回信息为空时 */
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE).trim();
				/** return_code和result_code都为SUCCESS时 */
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					pgwRespQueryOrder.setRespCode(RetCode.Success.RET_10000);
					pgwRespQueryOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
					/** 交易状态 */
					String tradeState = reqHandler.getParameter("trade_state").trim();
					switch (tradeState) {
						case TenpayConstant.TRADE_STATE_NOTPAY://未支付
							pgwRespQueryOrder.setOrderState(KstHosConstant.I0);
							break;
						case TenpayConstant.TRADE_STATE_USERPAYING://正在支付中
							pgwRespQueryOrder.setOrderState(KstHosConstant.I1);
							break;
						case TenpayConstant.TRADE_STATE_SUCCESS://支付成功
							pgwRespQueryOrder.setOrderState(KstHosConstant.I2);
							pgwRespQueryOrder.setOrderId(reqHandler.getParameter("out_trade_no"));
							pgwRespQueryOrder.setPayTime(reqHandler.getParameter("time_end"));
							pgwRespQueryOrder.setPrice(new Integer(reqHandler.getParameter("total_fee")));
							pgwRespQueryOrder.setTransactionNo(reqHandler.getParameter("transaction_id"));
							break;
						case TenpayConstant.TRADE_STATE_CLOSED://已关闭
							pgwRespQueryOrder.setOrderState(KstHosConstant.I5);
							break;
						case TenpayConstant.TRADE_STATE_REVOKED://已撤销
							pgwRespQueryOrder.setOrderState(KstHosConstant.I6);
						case TenpayConstant.TRADE_STATE_REFUND://已退费
							pgwRespQueryOrder.setOrderState(KstHosConstant.I4);
							break;
						default:
							//TODO 待完善
							pgwRespQueryOrder.setOrderState(KstHosConstant.I0);
							break;
					}
				} else {
					RetCode errorRetCode =weChatErrorCodeParseRetCode(reqHandler.getParameter(TenpayConstant.ERR_CODE));
					String errCodeDes  = reqHandler.getParameter(TenpayConstant.ERR_CODE_DES);
					pgwRespQueryOrder.setRespCode(errorRetCode);
					pgwRespQueryOrder.setRespMsg(errorRetCode.getMessage()+"|err_code_des:"+errCodeDes);
				}
			} else {
				pgwRespQueryOrder.setRespCode(RetCode.Common.ERROR_CALL_WEIXIN_PAY);
				pgwRespQueryOrder.setRespMsg(RetCode.Common.ERROR_CALL_WEIXIN_PAY.getMessage()+"|return_msg:"+returnMsg);
			}
		} else {
			pgwRespQueryOrder.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
			pgwRespQueryOrder.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
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
		String configKey = authInfoVo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		if( !StringUtil.isEmpty(pgwReqQueryRefundOrder.getOrderId())) {
			packageParams.put("out_trade_no", pgwReqQueryRefundOrder.getOrderId());
		}
		if( !StringUtil.isEmpty(pgwReqQueryRefundOrder.getTransactionNo())) {
			packageParams.put("transaction_id", pgwReqQueryRefundOrder.getTransactionNo());
		}
		if( !StringUtil.isEmpty(pgwReqQueryRefundOrder.getRefundOrderId())) {
			packageParams.put("out_refund_no", pgwReqQueryRefundOrder.getRefundOrderId());
		}
		if( !StringUtil.isEmpty(pgwReqQueryRefundOrder.getRefundId())) {
			packageParams.put("refund_id", pgwReqQueryRefundOrder.getRefundId());
		}
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		String returnXmlStr = HttpsClientUtils.httpsPost(pgwReqQueryRefundOrder.getOrderId(),ApiModule.WeChat.pay_refundquery,TenpayConstant.REFUNDQUERY_URL, arrayToXml);
		reqHandler.doParse(returnXmlStr);
		/** 校验签名 */
		boolean flag = reqHandler.isValidWXSign();
		if (flag) {
			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE).trim();
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			/** return_code为SUCCESS 并且 返回信息为空时 */
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE).trim();
				/** return_code和result_code都为SUCCESS时 */
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					pgwRespQueryRefundOrder.setRespCode(RetCode.Success.RET_10000);
					pgwRespQueryRefundOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
					pgwRespQueryRefundOrder.setRefundId(reqHandler.getParameter("refund_id_$0"));
					pgwRespQueryRefundOrder.setRefundOrderId(reqHandler.getParameter("out_refund_no_$0"));
					pgwRespQueryRefundOrder.setRefundPrice(new Integer(reqHandler.getParameter("refund_fee_$0")));
					
					pgwRespQueryRefundOrder.setRefundTime(reqHandler.getParameter("refund_success_time_$0"));
				    String refundStatus = reqHandler.getParameter("refund_status_$0");

					switch (refundStatus) {
						case TenpayConstant.TRADE_STATE_SUCCESS://SUCCESS—退款成功
							pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
							break;
						case TenpayConstant.TRADE_STATE_PROCESSING://PROCESSING—退款处理中
							pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I3);
							break;
						case TenpayConstant.TRADE_STATE_CHANGE:
							//CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，
							//可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。
							pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I7);
							break;
						case TenpayConstant.TRADE_STATE_REFUNDCLOSE://REFUNDCLOSE—退款关闭。
							pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I7);
							break;
						default:
							break;
					}
				} else {
					RetCode errorRetCode =weChatErrorCodeParseRetCode(reqHandler.getParameter(TenpayConstant.ERR_CODE));
					String errCodeDes  = reqHandler.getParameter(TenpayConstant.ERR_CODE_DES);
					pgwRespQueryRefundOrder.setRespCode(errorRetCode);
					pgwRespQueryRefundOrder.setRespMsg(errorRetCode.getMessage()+"|err_code_des:"+errCodeDes);
				}
			} else {
				pgwRespQueryRefundOrder.setRespCode(RetCode.Common.ERROR_CALL_WEIXIN_PAY);
				pgwRespQueryRefundOrder.setRespMsg(RetCode.Common.ERROR_CALL_WEIXIN_PAY.getMessage()+"|return_msg:"+returnMsg);
			}
		} else {
			pgwRespQueryRefundOrder.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
			pgwRespQueryRefundOrder.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
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
		String configKey = authInfoVo.getConfigKey();
		PgwRespWapUniteOrder pgwRespWapUniteOrder = new PgwRespWapUniteOrder();
		// 随机数
		SortedMap<String, String> packageParams = null;

		if ("T".equals(KasiteConfig.getWxPay(WXPayEnum.is_parent_mode, configKey))) {
			// 服务商模式

			packageParams = TenpayUtil.getPackageParamsByParent(authInfoVo.getClientId(), configKey, pgwReqUniteOrder.getOpenId(),
					pgwReqUniteOrder.getOrderId(), pgwReqUniteOrder.getPrice(),pgwReqUniteOrder.getBody(), 
					pgwReqUniteOrder.getRemoteIp(), pgwReqUniteOrder.getIsLimitCredit(), TenpayConstant.TRADE_TYPE_MWEB, authInfoVo.getSessionKey());
		} else {
			packageParams = TenpayUtil.getPackageParams(authInfoVo.getClientId(), configKey, pgwReqUniteOrder.getOpenId(),
					pgwReqUniteOrder.getOrderId(), pgwReqUniteOrder.getPrice(), pgwReqUniteOrder.getBody(),
					pgwReqUniteOrder.getRemoteIp(), pgwReqUniteOrder.getIsLimitCredit(), TenpayConstant.TRADE_TYPE_MWEB, authInfoVo.getSessionKey());
		}
		JSONObject h5Info = new JSONObject();
		h5Info.put("type", "Wap");
		h5Info.put("wap_url",KasiteConfig.getKasiteHosWebAppUrl());
		h5Info.put("wap_name", KasiteConfig.getOrgName()+"掌医充值");
		packageParams.put("h5_info",h5Info.toJSONString());
		String mch_key = KasiteConfig.getWxPayMchKey(configKey);
		// #.md5编码并转成大写 签名：
		String sign = TenpayUtil.createSign(packageParams, mch_key);
		// #.最终的提交xml：
		String xml = TenpayUtil.setMapToXml(packageParams, sign);
		LogUtil.info(log, new LogBody(authInfoVo).set("outTradeNo",pgwReqUniteOrder.getOrderId()).set("inParam", xml));
		// 根据订单获取支付相关信息（调用支付统一接口）
		String unifiedOrderResult = HttpsClientUtils.httpsPost(pgwReqUniteOrder.getOrderId(),ApiModule.WeChat.pay_unifiedorder,
				TenpayConstant.UNIFIEDORDER_URL, xml);
		LogUtil.info(log, new LogBody(authInfoVo).set("outTradeNo", pgwReqUniteOrder.getOrderId()).set("outParam", unifiedOrderResult));
		JSONObject unifiedOrderRetJson = JSONObject.parseObject(TenpayUtil.xml2JSON(unifiedOrderResult));
		JSONObject retJson = new JSONObject();
		String returnCode = unifiedOrderRetJson.getString(TenpayConstant.RETURN_CODE);
		String resultCode = unifiedOrderRetJson.getString(TenpayConstant.RESULT_CODE);
		String returnMsg = unifiedOrderRetJson.getString(TenpayConstant.RETURN_MSG);
		retJson.put(KstHosConstant.RESPCODE, RetCode.Pay.ERROR_UNITEORDER.getCode());
		retJson.put(KstHosConstant.RESPMESSAGE,
				"微信统一下单返回结果：returnCode:" + returnCode + "|resultCode:" + resultCode + "|returnMsg:" + returnMsg);
		if (!StringUtil.isEmpty(returnCode) && TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)
				&& !StringUtil.isEmpty(resultCode) && TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
			 
			String prepayId = unifiedOrderRetJson.getString("prepay_id");
			String mwebUrl = unifiedOrderRetJson.getString("mweb_url");
			String tradeType = unifiedOrderRetJson.getString("trade_type");
			// 返回给前端的数据集
			JSONObject data = new JSONObject();
			data.put("trade_type", tradeType);
			data.put("prepay_id", prepayId);
			data.put("mweb_url", mwebUrl);
			pgwRespWapUniteOrder.setRespCode(RetCode.Success.RET_10000);
			pgwRespWapUniteOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
			pgwRespWapUniteOrder.setPayInfo(retJson.toJSONString());
		}else {
			pgwRespWapUniteOrder.setRespCode(RetCode.Pay.ERROR_UNITEORDER);
			pgwRespWapUniteOrder.setRespMsg(RetCode.Pay.ERROR_UNITEORDER.getMessage());
		}
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
		
		return null;
	}
	
	
	/**
	 * 初始化RequestHandler
	 *
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:39:46
	 */
	private RequestHandler initRequestHandler(String configKey) {
		RequestHandler reqHandler = new RequestHandler(null, null);
		// 如果存在父商户，则为服务商模式
		if ("T".equals(KasiteConfig.getWxPay(WXPayEnum.is_parent_mode, configKey))) {
			reqHandler.init(
					KasiteConfig.getWxPay(WXPayEnum.wx_parent_app_id, configKey)
					, "", KasiteConfig.getWxPay(WXPayEnum.wx_parent_mch_key, configKey)
					);
		} else {
			// 如果不存在父商户，则为普通商户模式
			reqHandler.init(KasiteConfig.getWxPay(WXPayEnum.wx_app_id, configKey), "", 
					KasiteConfig.getWxPay(WXPayEnum.wx_mch_key, configKey));
		}
		return reqHandler;
	}
	
	/**
	 * 初始化SortedMap
	 *
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:40:03
	 */
	private SortedMap<String, String> initSortedMap(String configKey) {
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// 如果存在父商户，则为服务商模式
		if ("T".equals(KasiteConfig.getWxPay(WXPayEnum.is_parent_mode, configKey))) {
			packageParams.put("appid", KasiteConfig.getWxPay(WXPayEnum.wx_parent_app_id, configKey));
			packageParams.put("mch_id", KasiteConfig.getWxPay(WXPayEnum.wx_parent_mch_id, configKey));
			packageParams.put("sub_mch_id", KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configKey));
		} else {
			// 如果不存在父商户，则为普通商户模式
			packageParams.put("appid", KasiteConfig.getWxPay(WXPayEnum.wx_app_id, configKey));
			packageParams.put("mch_id", KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configKey));
		}
		return packageParams;
	}
	
	public static RetCode weChatErrorCodeParseRetCode(String errorCode) {
		if( StringUtil.isEmpty(errorCode) ) {
			return RetCode.Common.ERROR_SYSTEM;
		}
		RetCode retCode = null;
		switch (errorCode) {
		case TenpayConstant.ERRCODE_AUTHCODEINVALID:
			//当面付二维码失效
			retCode = RetCode.Pay.ERROR_SWEEPCODEPAY_AUTHCODEINVALID;
			break;
		case TenpayConstant.ERRCODE_USERPAYING:
			//当面付用户支付中
			retCode = RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING;
			break;
		case TenpayConstant.ERRCODE_SYSTEMERROR:
			//微信系统错误
			retCode = RetCode.Common.ERROR_SYSTEM;
			break;
		case TenpayConstant.ERRCODE_ORDERNOTEXIST:
			//不存在商户订单
			retCode = RetCode.Pay.ERROR_MERCHANTORDER;
			break;
		case TenpayConstant.ERRCODE_INVALIDTRANSACTIONID:
			//商户订单号错误
			retCode = RetCode.Common.ERROR_PARAM;
			break;
		case TenpayConstant.ERRCODE_PARAMERROR:
			//参数错误
			retCode = RetCode.Common.ERROR_PARAM;
			break;
		case TenpayConstant.ERRCODE_REQUIREPOSTMETHOD:
			//请使用post方法	
			retCode = RetCode.Common.ERROR_CALL_WEIXIN_PAY;
			break;
		case TenpayConstant.ERRCODE_REVERSEEXPIRE:
			//订单无法撤销	订单有7天的撤销有效期，过期将不能撤销	请检查需要撤销的订单是否超过可撤销有效期
			retCode = RetCode.Pay.ERROR_REVOKED;
			break;
		case TenpayConstant.ERRCODE_SIGNERROR:
			//订单无法撤销	订单有7天的撤销有效期，过期将不能撤销	请检查需要撤销的订单是否超过可撤销有效期
			retCode = RetCode.Pay.SIGNATURE_FAILED;
			break;
		case TenpayConstant.ERRCODE_TRADERROR:
			// 订单错误；业务错误导致交易失败；请检查用户账号是否异常、被风控、是否符合规则限制等
			retCode = RetCode.Pay.ERROR_REVOKED;
			break;
		case TenpayConstant.ERRCODE_INVALIDREQUEST:
			//无效请求
			retCode = RetCode.Common.ERROR_CALL_WEIXIN_PAY;
			break;
		case TenpayConstant.ERRCODE_BIZERRNEEDRETRY:
			//退款业务流程错误，需要商户触发重试来解决	并发情况下，业务被拒绝，商户重试即可解决	请不要更换商户退款单号，请使用相同参数再次调用API。
			retCode = RetCode.Pay.ERROR_REFUND;
			break;
		case TenpayConstant.ERRCODE_TRADEOVERDUE:
			//订单已经超过退款期限	订单已经超过可退款的最大期限(支付后一年内可退款)	请选择其他方式自行退款
			retCode = RetCode.Pay.ERROR_REFUND;
			break;
		case TenpayConstant.ERRCODE_ERROR:
			//业务错误	申请退款业务发生错误	该错误都会返回具体的错误原因，请根据实际返回做相应处理。
			retCode = RetCode.Common.ERROR_SYSTEM;
			break;
		case TenpayConstant.ERRCODE_USERACCOUNTABNORMAL:
			//退款请求失败	用户帐号注销	此状态代表退款申请失败，商户可自行处理退款。
			retCode = RetCode.Pay.REFUNDERROR_USERACCOUNT;
			break;
		case TenpayConstant.ERRCODE_INVALIDREQTOOMUCH:
			//无效请求过多	连续错误请求数过多被系统短暂屏蔽	请检查业务是否正常，确认业务正常后请在1分钟后再来重试
			retCode = RetCode.Common.ERROR_SYSTEM;
			break;
		case TenpayConstant.ERRCODE_NOTENOUGH:
			//余额不足	商户可用退款余额不足	此状态代表退款申请失败，商户可根据具体的错误提示做相应的处理。
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
		return ChannelTypeEnum.wechat.name();
	}

	
	
}
