package com.kasite.core.common.util.wechat;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.util.HttpsClientUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil.Channel;

/**
 * @author linjf TODO 腾讯支付相关服务
 */
/**
 * @author linjf
 * TODO
 */
/**
 * @author linjf
 * TODO
 */
public class TenpayService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);

	/**
	 * 调用商户退费接口
	 * 
	 * @param transactionId
	 *            微信订单号
	 * @param outTradeNo
	 *            商户单号
	 * @param outRefundNo
	 *            商户退款单号
	 * @param totalFee
	 *            订单金额
	 * @param refundFee
	 *            退款金额
	 * @param refundDesc
	 *            退款金额
	 * @return
	 * @throws Exception
	 */
	public static JSONObject refund(AuthInfoVo vo,String configKey,String transactionId, String outTradeNo, String outRefundNo, Integer totalFee,
			Integer refundFee, String refundDesc) throws Exception {
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);

		if (!StringUtil.isEmpty(outTradeNo)) {
			packageParams.put("out_trade_no", outTradeNo);
		} else if (!StringUtil.isEmpty(transactionId)) {
			packageParams.put("transaction_id", transactionId);
		}

		packageParams.put("nonce_str", TenpayUtil.getNonceStr());
		packageParams.put("out_refund_no", outRefundNo);
		packageParams.put("total_fee", totalFee + "");
		packageParams.put("refund_fee", refundFee + "");
//		packageParams.put("op_user_id", TenpayConstant.MCH_ID);
		packageParams.put("op_user_id", KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, configKey));


		String sign = reqHandler.createSign(packageParams);
		packageParams.put("sign", sign);
		// 记录退款日志
		JSONObject noticeLogJson = new JSONObject();
		// 退款记录
		noticeLogJson.put("OP_TYPE", 3);
		noticeLogJson.put("CHANEL_TYPE", 1);
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		noticeLogJson.put("MESSAGE", arrayToXml);
		String returnXmlStr = "";
		returnXmlStr = ClientCustomSSL.call(outTradeNo,ApiModule.WeChat.pay_refund,TenpayConstant.REFUND_URL,configKey,arrayToXml);
		reqHandler.doParse(returnXmlStr);
		boolean flag = reqHandler.isValidWXSign();
		String logTag = "TAG[" + "out_trade_no:" + outTradeNo + "]";
		JSONObject retJson = new JSONObject();
		if (flag) {
			String returnCode = reqHandler.getParameter("return_code");
			String returnMsg = reqHandler.getParameter("return_msg");
			String resultCode = reqHandler.getParameter("result_code").toString();
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode.trim())) {
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode.trim())) {
					String refundId = reqHandler.getParameter("refund_id").toString();
					LogUtil.info(vo,log, logTag + "INFO[" + "申请微信退款成功|refund_id:" + refundId + "]");
					retJson.put("RefundId", refundId);
					retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, "申请退款成功！");
					noticeLogJson.put("RESULT_CODE", resultCode);
					noticeLogJson.put("OUT_TRADE_NO", refundId);
					noticeLogJson.put("TRANSACTION_ID", outTradeNo);
					noticeLogJson.put("FINAL_RESULT", "申请退款成功!");
				} else {
					LogUtil.info(vo,log, logTag + "INFO[" + "退费申请失败|" + returnMsg + "]");
					noticeLogJson.put("RESULT_CODE", resultCode);
					noticeLogJson.put("FINAL_RESULT", returnMsg);
					retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, returnMsg);
				}
			} else {
				LogUtil.info(vo,log, logTag + "INFO[" + "退费申请失败|" + returnMsg + "]");
				noticeLogJson.put("RESULT_CODE", resultCode);
				noticeLogJson.put("FINAL_RESULT", returnMsg);
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, returnMsg);
			}
		} else {
			LogUtil.info(vo,log, logTag + "INFO[" + "微信签名验证失败" + "]");
			noticeLogJson.put("RESULT_CODE", "-3");
			noticeLogJson.put("FINAL_RESULT", "微信签名验证失败");
			retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, "微信签名验证失败");
		}
		WxMsgUtil.create(KasiteConfig.localConfigPath()).write(noticeLogJson.toString(),Channel.WXRefund);
		return retJson;
	}

	/***
	 * 提交刷卡支付
	 */
	public static JSONObject microPay(AuthInfoVo vo,ReqMicroPay reqMicroPay) throws Exception {
		String configKey = vo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		packageParams.put("body", reqMicroPay.getBody());
		packageParams.put("out_trade_no", reqMicroPay.getOutTradeNo());
		packageParams.put("total_fee", reqMicroPay.getTotalFee() + "");
		packageParams.put("spbill_create_ip", reqMicroPay.getSpbillCreateIp());
		packageParams.put("auth_code", reqMicroPay.getAuthCode());
		if (null != reqMicroPay.getIsLimitCredit() && KstHosConstant.I1.equals(reqMicroPay.getIsLimitCredit())) {
			//限制使用信用卡
			packageParams.put("limit_pay", "no_credit");
		}
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		String returnXmlStr = null;
		JSONObject retJson = new JSONObject();
		
		try {
			returnXmlStr = HttpsClientUtils.httpsPost(reqMicroPay.getOutTradeNo(),ApiModule.WeChat.pay_micropay,TenpayConstant.MICROPAY_URL, arrayToXml);
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,
					new LogBody(vo).set("auth_code", reqMicroPay.getAuthCode()).set("total_fee", reqMicroPay.getTotalFee())
							.set("out_trade_no", reqMicroPay.getOutTradeNo()).set("result", "调用微信接口超时或者异常"),e);
			retJson.put(KstHosConstant.RESPCODE, RetCode.Common.CallWeiXinError.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, RetCode.Common.CallWeiXinError.getMessage());
			return retJson;
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
					retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, RetCode.Success.RET_10000.getMessage());
					retJson.put("TransactionId", reqHandler.getParameter("transaction_id").toString());
					retJson.put("PayTime", reqHandler.getParameter("time_end").toString());
					retJson.put("TotalFee", reqHandler.getParameter("total_fee").toString());
					/** 需要其他返回字段 待加 */

				} else {
					/** 业务结果FAIL */
					//微信的错误代码描述
					String errCodeDes = reqHandler.getParameter(TenpayConstant.ERR_CODE_DES);
					//微信错误代码
					String errCode= reqHandler.getParameter(TenpayConstant.ERR_CODE);
					retJson.put(KstHosConstant.RESPCODE,weChatRetrunCodeParseRetCode(errCode).getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, errCodeDes);
				}
			} else {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, returnMsg);
			}
		} else {
			retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, "微信签名验证失败");
		}
		LogUtil.info(log,
				new LogBody(vo).set("auth_code", reqMicroPay.getAuthCode()).set("total_fee", reqMicroPay.getTotalFee())
						.set("out_trade_no", reqMicroPay.getOutTradeNo()).set("result", retJson));
		return retJson;
	}

	
	/**
	 * @param configKey
	 * @param transactionId 微信订单号
	 * @param outTradeNo 商户订单号
	 * @return
	 * @throws Exception
	 */
	public static JSONObject orderQuery(AuthInfoVo vo,String transactionId, String outTradeNo) throws Exception {
		String configKey = vo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		if (!StringUtil.isEmpty(transactionId)) {
			packageParams.put("transaction_id", transactionId);
		} else {
			packageParams.put("out_trade_no", outTradeNo);
		}
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		// String returnXmlStr =
		// ClientCustomSSL.call(TenpayConstant.ORDERQUERY_URL,arrayToXml);
		String returnXmlStr = HttpsClientUtils.httpsPost(outTradeNo,ApiModule.WeChat.pay_orderquery,TenpayConstant.ORDERQUERY_URL, arrayToXml);
		reqHandler.doParse(returnXmlStr);
		/** 校验签名 */
		boolean flag = reqHandler.isValidWXSign();
		JSONObject retJson = new JSONObject();
		if (flag) {
			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE).trim();
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			/** return_code为SUCCESS 并且 返回信息为空时 */
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE).trim();
				/** return_code和result_code都为SUCCESS时 */
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, RetCode.Success.RET_10000.getMessage());
					/** 交易状态 */
					String tradeState = reqHandler.getParameter("trade_state").trim();
					retJson.put("TradeState", tradeState);
					/** 交易状态=成功 获取信息 */
					if (TenpayConstant.RETURN_CODE_SUCCESS.equals(tradeState)) {
						retJson.put("TransactionId", reqHandler.getParameter("transaction_id"));
						retJson.put("PayTime", reqHandler.getParameter("time_end"));
						retJson.put("TotalFee", reqHandler.getParameter("total_fee"));
						retJson.put("OutTradeNo", reqHandler.getParameter("out_trade_no"));
						/** 需要其他返回字段 待加 */
					}
				} else {
					retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, reqHandler.getParameter(TenpayConstant.ERR_CODE_DES));
				}
			} else {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, returnMsg);
			}
		} else {
			retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, "微信签名验证失败");
		}
		LogUtil.info(log,
				new LogBody(vo).set("transactionId", transactionId).set("outTradeNo", outTradeNo).set("result", retJson));
		return retJson;
	}

	/**
	 * 统一下单
	 *
	 * @param orderId
	 * @param openId
	 * @param price
	 * @param isLimitCredit 是否支持信用卡 0 支持  1 不支持
	 * @param body
	 * @param remoteIp
	 * @param tradeType  微信交易类型 com.kasite.client.wechat.util.TenpayConstant
	 * @return
	 * @throws Exception
	 * @author 無
	 * @date 2018年4月24日 下午5:39:26
	 */
	public static JSONObject uniteOrder(AuthInfoVo vo,String configKey,String orderId, String openId, Integer price, Integer isLimitCredit,
			String body, String remoteIp, String tradeType) throws Exception {
		// 随机数
		String nonceStr = TenpayUtil.getNonceStr();
		SortedMap<String, String> packageParams = null;
		String app_id = null;
		
		if ("T".equals(KasiteConfig.getWxPay(WXPayEnum.is_parent_mode, configKey))) {
			// 服务商模式
			app_id = KasiteConfig.getWxPay(WXPayEnum.wx_parent_app_id, configKey);
			
			packageParams = TenpayUtil.getPackageParamsByParent(vo.getClientId(),configKey,openId, orderId, 
					price, body, remoteIp, 
					isLimitCredit,
					tradeType,vo.getSessionKey());
		} else {
			app_id = KasiteConfig.getWxPay(WXPayEnum.wx_app_id, configKey);
			packageParams = TenpayUtil.getPackageParams(vo.getClientId(),configKey,openId, orderId, price, body, remoteIp, isLimitCredit,
					tradeType,vo.getSessionKey());
		}
		
		String mch_key = KasiteConfig.getWxPayMchKey(configKey);
		// #.md5编码并转成大写 签名：
		String sign = TenpayUtil.createSign(packageParams, mch_key);
		// #.最终的提交xml：
		String xml = TenpayUtil.setMapToXml(packageParams, sign);
		LogUtil.info(log, new LogBody(vo).set("outTradeNo", orderId).set("inParam", xml));
		// 根据订单获取支付相关信息（调用支付统一接口）
		String unifiedOrderResult = HttpsClientUtils.httpsPost(orderId,ApiModule.WeChat.pay_unifiedorder,TenpayConstant.UNIFIEDORDER_URL, xml);
		LogUtil.info(log, new LogBody(vo).set("outTradeNo", orderId).set("outParam", unifiedOrderResult));
		JSONObject unifiedOrderRetJson = JSONObject.parseObject(TenpayUtil.xml2JSON(unifiedOrderResult));
		JSONObject retJson = new JSONObject();
		String returnCode = unifiedOrderRetJson.getString(TenpayConstant.RETURN_CODE);
		String resultCode = unifiedOrderRetJson.getString(TenpayConstant.RESULT_CODE);
		String returnMsg = unifiedOrderRetJson.getString(TenpayConstant.RETURN_MSG);
		retJson.put(KstHosConstant.RESPCODE, RetCode.Pay.ERROR_UNITEORDER.getCode());
		retJson.put(KstHosConstant.RESPMESSAGE, "微信统一下单返回结果：returnCode:"+returnCode+"|resultCode:"+resultCode+"|returnMsg:"+returnMsg);
		if (!StringUtil.isEmpty(returnCode) && TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)
				&& !StringUtil.isEmpty(resultCode) && TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
			String prepayId = unifiedOrderRetJson.getString("prepay_id");
			// 返回给前端的数据集
			SortedMap<String, String> returnPackage = TenpayUtil.getReturnPackage(app_id,nonceStr, prepayId);
			
			String finalSign = TenpayUtil.createSign(returnPackage, mch_key);
			returnPackage.put("paySign", finalSign);
			// String params = DataUtils.getParams(finalpackage,finalSign);
			JSONObject data = JSONObject.parseObject(JSON.toJSONString(returnPackage));//把map转为json数据
			retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, RetCode.Success.RET_10000.getMessage());
			retJson.put(KstHosConstant.DATA, data.toString());
			String codeUrl = unifiedOrderRetJson.getString("code_url");
			if (!StringUtil.isEmpty(codeUrl)) {
				retJson.put(ApiKey.uniteOrder.QRCodeUrl.name(), codeUrl);
			}
		}
		return retJson;
	}

	/***
	 * 撤销订单
	 */
	public static JSONObject reverse(AuthInfoVo vo,String transactionId, String outTradeNo) throws Exception {
		String configKey = vo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		if (!StringUtil.isEmpty(transactionId)) {
			packageParams.put("transaction_id", transactionId);
		} else {
			packageParams.put("out_trade_no", outTradeNo);
		}
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		String returnXmlStr = ClientCustomSSL.call(outTradeNo,ApiModule.WeChat.secapi_pay_reverse,TenpayConstant.REVERSE_URL,configKey, arrayToXml);
		reqHandler.doParse(returnXmlStr);
		/** 校验签名 */
		boolean flag = reqHandler.isValidWXSign();
		JSONObject retJson = new JSONObject();
		if (flag) {
			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE).trim();
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			/** return_code为SUCCESS 并且 返回信息为空时 */
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE).trim();
				/** return_code和result_code都为SUCCESS时 */
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, RetCode.Success.RET_10000.getMessage());
					/** 需要其他返回字段 待加 */

				} else {
					/** 错误代码 */
					String errCode = reqHandler.getParameter(TenpayConstant.ERR_CODE);
					/** 系统超时 */
					if (TenpayConstant.ERRCODE_SYSTEMERROR.equals(errCode)) {
						retJson.put(TenpayConstant.ERR_CODE, errCode);
					}
					retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, reqHandler.getParameter(TenpayConstant.ERR_CODE_DES));
				}
			} else {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, returnMsg);
			}
		} else {
			retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, "微信签名验证失败");
		}
		LogUtil.info(log,
				new LogBody(vo).set("transactionId", transactionId).set("outTradeNo", outTradeNo).set("result", retJson));
		return retJson;
	}

	/**
	 * 关闭订单
	 * 
	 * @param outTradeNo
	 *            订单号
	 * @return
	 * @throws Exception
	 */
	public static JSONObject close(AuthInfoVo vo,String outTradeNo) throws Exception {
		String configKey = vo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		packageParams.put("out_trade_no", outTradeNo);
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		LogUtil.info( log, new LogBody(vo).set("outTradeNo", outTradeNo).set("inParam", arrayToXml));
		String returnXmlStr = HttpsClientUtils.httpsPost(outTradeNo,ApiModule.WeChat.pay_closeorder,TenpayConstant.CLOSEORDER_URL, arrayToXml);
		LogUtil.info( log, new LogBody(vo).set("outTradeNo", outTradeNo).set("outParam", returnXmlStr));
		reqHandler.doParse(returnXmlStr);
		/** 校验签名 */
		boolean flag = reqHandler.isValidWXSign();
		JSONObject retJson = new JSONObject();
		if (flag) {
			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE).trim();
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			/** return_code为SUCCESS 并且 返回信息为空时 */
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE).trim();
				/** return_code和result_code都为SUCCESS时 */
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, RetCode.Success.RET_10000.getMessage());

				} else {
					/** 错误代码 */
					String errCode = reqHandler.getParameter(TenpayConstant.ERR_CODE);
					/** 系统超时 */
					if (TenpayConstant.ERRCODE_SYSTEMERROR.equals(errCode)) {
						retJson.put(TenpayConstant.ERR_CODE, errCode);
					}
					retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, reqHandler.getParameter(TenpayConstant.ERR_CODE_DES));
				}
			} else {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, returnMsg);
			}
		} else {
			retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, "微信签名验证失败");
		}
		LogUtil.info(log, new LogBody(vo).set("outTradeNo", outTradeNo).set("result", retJson));
		return retJson;
	}
	
	
	/**
	 * 查询微信商户订单退费结果
	 * 微信原生接口支持查询多条记录
	 * 出于性能，效率考虑，本接口封装只查询一条记录
	 * @param vo
	 * @param outTradeNo
	 * @param transactionId
	 * @param outRefundNo
	 * @param refundId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject refundQuery(AuthInfoVo vo,String outTradeNo,String transactionId,
			String outRefundNo,String refundId) throws Exception {
		String configKey = vo.getConfigKey();
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		packageParams.put("nonce_str", StringUtil.getUUID());
		if( !StringUtil.isEmpty(outTradeNo)) {
			packageParams.put("out_trade_no", outTradeNo);
		}
		if( !StringUtil.isEmpty(transactionId)) {
			packageParams.put("transaction_id", transactionId);
		}
		if( !StringUtil.isEmpty(outRefundNo)) {
			packageParams.put("out_refund_no", outRefundNo);
		}
		if( !StringUtil.isEmpty(refundId)) {
			packageParams.put("refund_id", refundId);
		}
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		LogUtil.info( log, new LogBody(vo).set("outTradeNo", outTradeNo).set("inParam", arrayToXml));
		String returnXmlStr = HttpsClientUtils.httpsPost(outTradeNo,ApiModule.WeChat.pay_refundquery,TenpayConstant.REFUNDQUERY_URL, arrayToXml);
		LogUtil.info( log, new LogBody(vo).set("outTradeNo", outTradeNo).set("outParam", returnXmlStr));
		reqHandler.doParse(returnXmlStr);
		/** 校验签名 */
		boolean flag = reqHandler.isValidWXSign();
		JSONObject retJson = new JSONObject();
		if (flag) {
			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE).trim();
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			/** return_code为SUCCESS 并且 返回信息为空时 */
			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE).trim();
				/** return_code和result_code都为SUCCESS时 */
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, RetCode.Success.RET_10000.getMessage());
					retJson.put("outRefundNo", reqHandler.getParameter("out_refund_no_$0"));
					retJson.put("refundId", reqHandler.getParameter("refund_id_$0"));
					retJson.put("refundFee", reqHandler.getParameter("refund_fee_$0"));
					retJson.put("refundStatus", reqHandler.getParameter("refund_status_$0"));
					retJson.put("refundTime", reqHandler.getParameter("refund_success_time_$0"));
					retJson.put("totalFee", reqHandler.getParameter("total_fee"));
				} else {
					/** 错误代码 */
					String errCode = reqHandler.getParameter(TenpayConstant.ERR_CODE);
					/** 系统超时 */
					if (TenpayConstant.ERRCODE_SYSTEMERROR.equals(errCode)) {
						retJson.put(TenpayConstant.ERR_CODE, errCode);
					}
					retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
					retJson.put(KstHosConstant.RESPMESSAGE, reqHandler.getParameter(TenpayConstant.ERR_CODE_DES));
				}
			} else {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, returnMsg);
			}
		} else {
			retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, "微信签名验证失败");
		}
		LogUtil.info(log, new LogBody(vo).set("outTradeNo", outTradeNo).set("result", retJson));
		return retJson;
	}

	/**
	 * 初始化RequestHandler
	 *
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:39:46
	 */
	public static RequestHandler initRequestHandler(String configKey) {
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
	public static SortedMap<String, String> initSortedMap(String configKey) {
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

	public static RetCode weChatRetrunCodeParseRetCode(String errorCode) {
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
		default:
			retCode = RetCode.Common.ERROR_SYSTEM;
			break;
		}
		return retCode;
	}
	
	/***
	 * 账单下载
	 */
	public static String downloadBill(String configKey, String billDate) throws Exception {
		RequestHandler reqHandler = initRequestHandler(configKey);
		SortedMap<String, String> packageParams = initSortedMap(configKey);
		// 账单日期
		packageParams.put("bill_date", billDate);
		//账单类型
		packageParams.put("bill_type", "ALL");
		packageParams.put("nonce_str", StringUtil.getUUID());
		/** 生成签名 */
		packageParams.put("sign", reqHandler.createSign(packageParams));
		/** 参数转XML格式 */
		String arrayToXml = TenpayXMLUtil.arrayToXml(packageParams);
		return HttpsClientUtils.httpsPost(ApiModule.WeChat.pay_downloadbill,TenpayConstant.DOWNLOAD_BILL_URL, arrayToXml);
	}
}
