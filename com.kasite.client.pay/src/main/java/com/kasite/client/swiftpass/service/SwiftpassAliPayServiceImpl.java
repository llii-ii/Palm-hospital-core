package com.kasite.client.swiftpass.service;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.swiftpass.constants.SwiftpassConstants;
import com.kasite.client.swiftpass.util.MD5;
import com.kasite.client.swiftpass.util.SwiftpassUtils;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.SwiftpassEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.HttpsClientUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.TenpayUtil;
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

/**
 * @author linjf
 * 威富通-支付宝实现类
 */
@Service("swiftpassAliPay")  
public class SwiftpassAliPayServiceImpl extends SwiftpassAbstractService implements IPaymentGateWayService{
  
	/**
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespUniteOrder uniteOrder(AuthInfoVo authInfoVo, PgwReqUniteOrder pgwReqUniteOrder) throws Exception {
		SortedMap<String, String> map = new TreeMap<String, String>();
		PgwRespUniteOrder pgwRespUniteOrder = new PgwRespUniteOrder();
		map.put("service", "pay.alipay.jspay");
		map.put("version", SwiftpassConstants.VERSION);
		map.put("charset", SwiftpassConstants.CHARSET);
		map.put("sign_type", SwiftpassConstants.SIGN_TYPE);
		map.put("mch_id", KasiteConfig.getSwiftpass(SwiftpassEnum.mch_id,authInfoVo.getConfigKey()));
		map.put("body", pgwReqUniteOrder.getBody());
		map.put("out_trade_no", pgwReqUniteOrder.getOrderId());
		map.put("total_fee", pgwReqUniteOrder.getPrice().toString());
		map.put("mch_create_ip", pgwReqUniteOrder.getRemoteIp());
		//buyer_id获取方法：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.8ujLD6&treeId=115&articleId=104114&docType=1通过网页授权获取用户信息，同步响应结果中的user_id对应文档中的buyer_id
		map.put("buyer_id", pgwReqUniteOrder.getOpenId());
		map.put("notify_url", KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.swiftpass, authInfoVo.getConfigKey(),
				authInfoVo.getClientId(), pgwReqUniteOrder.getOpenId(),authInfoVo.getSessionKey(),pgwReqUniteOrder.getOrderId()));
		map.put("nonce_str", TenpayUtil.getNonceStr());
		if (KstHosConstant.I1.equals(pgwReqUniteOrder.getIsLimitCredit())) {
			map.put("limit_credit_pay", pgwReqUniteOrder.getIsLimitCredit().toString());
			// 限定用户使用时能否使用信用卡，值为1，禁用信用卡；值为0或者不传此参数则不禁用
		}
		Map<String,String> params = SwiftpassUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SwiftpassUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString();
		String sign = MD5.sign(preStr, "&key=" + authInfoVo.getConfigKey(), SwiftpassConstants.CHARSET);
		map.put("sign", sign);
		String reqParam = SwiftpassUtils.parseXML(map);
		String respParam = HttpsClientUtils.SwiftpasshttpsPost(pgwReqUniteOrder.getOrderId(),ApiModule.Swiftpass.payalipayjspay,
				SwiftpassConstants.SWIFTPASS_GATEWAY_URL, reqParam, SwiftpassConstants.CHARSET);
		Map<String, String> resultMap = null;
		if (StringUtil.isNotEmpty(respParam)) {
			resultMap = SwiftpassUtils.xml2Map(respParam.getBytes(SwiftpassConstants.CHARSET),
					SwiftpassConstants.CHARSET);
			if (!SwiftpassUtils.checkParam(resultMap,KasiteConfig.getSwiftpass(SwiftpassEnum.key,authInfoVo.getConfigKey()))) {
				// "验证签名不通过";
				pgwRespUniteOrder.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
				pgwRespUniteOrder.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
			} else {
				String status =  resultMap.get("status");
				String resultCode = resultMap.get("result_code");
				String message = resultMap.get("message");
				if (KstHosConstant.STRING_0.equals(status)
						&& KstHosConstant.STRING_0.equals(resultCode)) {
					String payInfo = resultMap.get("pay_info");
					String tradeNO = JSONObject.parseObject(payInfo).getString("tradeNO");
					pgwRespUniteOrder.setPayInfo(tradeNO);
					pgwRespUniteOrder.setRespCode(RetCode.Success.RET_10000);
					pgwRespUniteOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
				}else{
					pgwRespUniteOrder.setRespCode(RetCode.Pay.ERROR_UNITEORDER);
					pgwRespUniteOrder.setRespMsg(RetCode.Pay.ERROR_UNITEORDER.getMessage()
							+"|status:"+status+"|resultCode:"+resultCode+"|message"+message);
				}
			}
		} else {
			// 返回为空
			pgwRespUniteOrder.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespUniteOrder.setRespMsg("调用威富通接口异常！");
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
		// TODO Auto-generated method stub
		SortedMap<String, String> map = new TreeMap<String, String>();
		PgwRespRefund pgwRespRefund = new PgwRespRefund();
		map.put("service", "unified.trade.refund");
		map.put("version", SwiftpassConstants.VERSION);
		map.put("charset", SwiftpassConstants.CHARSET);
		map.put("sign_type", SwiftpassConstants.SIGN_TYPE);
		map.put("mch_id", KasiteConfig.getSwiftpass(SwiftpassEnum.mch_id,pgwReqRefund.getPayConfigKey()));
		//商户系统内部的订单号，out_trade_no和transaction_id至少一个必填，同时存在时transaction_id优先
		map.put("out_trade_no", pgwReqRefund.getOrderId());
		map.put("transaction_id", pgwReqRefund.getTransactionNo());
		map.put("out_refund_no", pgwReqRefund.getRefundOrderId());
		map.put("total_fee", pgwReqRefund.getTotalPrice().toString());
		map.put("refund_fee", pgwReqRefund.getRefundPrice().toString());
		map.put("op_user_id", KasiteConfig.getSwiftpass(SwiftpassEnum.mch_id,pgwReqRefund.getPayConfigKey()));
		map.put("nonce_str", TenpayUtil.getNonceStr());
		Map<String,String> params = SwiftpassUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SwiftpassUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString();
		String sign = MD5.sign(preStr, "&key=" + pgwReqRefund.getPayConfigKey(), SwiftpassConstants.CHARSET);
		map.put("sign", sign);
		String reqParam = SwiftpassUtils.parseXML(map);
		String respParam = HttpsClientUtils.SwiftpasshttpsPost(pgwReqRefund.getOrderId(),ApiModule.Swiftpass.unifiedtraderefund,
				SwiftpassConstants.SWIFTPASS_GATEWAY_URL, reqParam, SwiftpassConstants.CHARSET);
		Map<String, String> resultMap = null;
		if (StringUtil.isNotEmpty(respParam)) {
			resultMap = SwiftpassUtils.xml2Map(respParam.getBytes(SwiftpassConstants.CHARSET),
					SwiftpassConstants.CHARSET);
			if (!SwiftpassUtils.checkParam(resultMap,KasiteConfig.getSwiftpass(SwiftpassEnum.key,pgwReqRefund.getPayConfigKey()))) {
				// "验证签名不通过";
				pgwRespRefund.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
				pgwRespRefund.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
			} else {
				String status =  resultMap.get("status");
				String resultCode = resultMap.get("result_code");
				String message = resultMap.get("message");
				if (KstHosConstant.STRING_0.equals(status)
						&& KstHosConstant.STRING_0.equals(resultCode)) {
					//退款成功
					pgwRespRefund.setRespCode(RetCode.Success.RET_10000);
					pgwRespRefund.setRespMsg(RetCode.Success.RET_10000.getMessage());
					pgwRespRefund.setRefundId(resultMap.get("refund_id"));
				}else{
					pgwRespRefund.setRespCode(RetCode.Pay.ERROR_REFUND);
					pgwRespRefund.setRespMsg(RetCode.Pay.ERROR_REFUND.getMessage()
							+"|status:"+status+"|resultCode:"+resultCode+"|message"+message);
				}
			}
		} else {
			// 返回为空
			pgwRespRefund.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespRefund.setRespMsg("调用威富通接口异常！");
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
		return super.sweepCodePay(authInfoVo, pgwReqSweepCodePay);
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqRevoke
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespRevoke revoke(AuthInfoVo authInfoVo, PgwReqRevoke pgwReqRevoke) throws Exception {
		return super.revoke(authInfoVo, pgwReqRevoke);
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqClose
	 * @return
	 * @throws Exception
	 * 商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
	 * 系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口
	 */
	@Override
	public PgwRespClose close(AuthInfoVo authInfoVo, PgwReqClose pgwReqClose) throws Exception {
		// TODO Auto-generated method stub
		SortedMap<String, String> map = new TreeMap<String, String>();
		PgwRespClose pgwRespClose = new PgwRespClose();
		map.put("service", "unified.trade.close");
		map.put("version", SwiftpassConstants.VERSION);
		map.put("charset", SwiftpassConstants.CHARSET);
		map.put("sign_type", SwiftpassConstants.SIGN_TYPE);
		map.put("mch_id", KasiteConfig.getSwiftpass(SwiftpassEnum.mch_id,authInfoVo.getConfigKey()));
		map.put("out_trade_no", pgwReqClose.getOrderId());
		map.put("nonce_str", TenpayUtil.getNonceStr());
		Map<String,String> params = SwiftpassUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SwiftpassUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString();
		String sign = MD5.sign(preStr, "&key=" + authInfoVo.getConfigKey(), SwiftpassConstants.CHARSET);
		map.put("sign", sign);
		String reqParam = SwiftpassUtils.parseXML(map);
		String respParam = HttpsClientUtils.SwiftpasshttpsPost(pgwReqClose.getOrderId(),ApiModule.Swiftpass.unifiedtradeclose,
				SwiftpassConstants.SWIFTPASS_GATEWAY_URL, reqParam, SwiftpassConstants.CHARSET);
		Map<String, String> resultMap = null;
		if (StringUtil.isNotEmpty(respParam)) {
			resultMap = SwiftpassUtils.xml2Map(respParam.getBytes(SwiftpassConstants.CHARSET),
					SwiftpassConstants.CHARSET);
			if (!SwiftpassUtils.checkParam(resultMap,KasiteConfig.getSwiftpass(SwiftpassEnum.key,authInfoVo.getConfigKey()))) {
				// "验证签名不通过";
				pgwRespClose.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
				pgwRespClose.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
			} else {
				String status =  resultMap.get("status");
				String resultCode = resultMap.get("result_code");
				String message = resultMap.get("message");
				String needQuery = resultMap.get("need_query");
				String errCode = resultMap.get("err_code");
				String errMsg = resultMap.get("err_msg");
				if (KstHosConstant.STRING_0.equals(status)
						&& KstHosConstant.STRING_0.equals(resultCode)) {
					pgwRespClose.setRespCode(RetCode.Success.RET_10000);
					pgwRespClose.setRespMsg(RetCode.Success.RET_10000.getMessage());
				}else{
					//TODO其他异常待补充
					pgwRespClose.setRespCode(RetCode.Pay.ERROR_CLOSE);
					pgwRespClose.setRespMsg(RetCode.Pay.ERROR_CLOSE.getExceptMsg()
							+"|message:"+message+"|resultCode:"+resultCode+"|status:"+status
							+"|needQuery:"+needQuery+"|errCode:"+errCode+"|errMsg:"+errMsg);
				}
			}
		} else {
			// 返回为空
			pgwRespClose.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespClose.setRespMsg("调用威富通接口异常！");
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
		// TODO Auto-generated method stub
		SortedMap<String, String> map = new TreeMap<String, String>();
		PgwRespQueryOrder pgwRespQueryOrder = new PgwRespQueryOrder();
		map.put("service", "unified.trade.query");
		map.put("version", SwiftpassConstants.VERSION);
		map.put("charset", SwiftpassConstants.CHARSET);
		map.put("sign_type", SwiftpassConstants.SIGN_TYPE);
		map.put("mch_id", KasiteConfig.getSwiftpass(SwiftpassEnum.mch_id,authInfoVo.getConfigKey()));
		// 商户系统内部的订单号, out_trade_no和transaction_id至少一个必填，同时存在时transaction_id优先
		map.put("transaction_id", pgwReqQueryOrder.getTransactionNo());
		map.put("out_trade_no", pgwReqQueryOrder.getOrderId());
		map.put("nonce_str", TenpayUtil.getNonceStr());
		Map<String,String> params = SwiftpassUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SwiftpassUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString();
		String sign = MD5.sign(preStr, "&key=" + authInfoVo.getConfigKey(), SwiftpassConstants.CHARSET);
		map.put("sign", sign);
		String reqParam = SwiftpassUtils.parseXML(map);
		String respParam = HttpsClientUtils.SwiftpasshttpsPost(pgwReqQueryOrder.getOrderId(),ApiModule.Swiftpass.unifiedtradequery,
				SwiftpassConstants.SWIFTPASS_GATEWAY_URL, reqParam, SwiftpassConstants.CHARSET);
		Map<String, String> resultMap = null;
		if (StringUtil.isNotEmpty(respParam)) {
			resultMap = SwiftpassUtils.xml2Map(respParam.getBytes(SwiftpassConstants.CHARSET),
					SwiftpassConstants.CHARSET);
			if (!SwiftpassUtils.checkParam(resultMap,KasiteConfig.getSwiftpass(SwiftpassEnum.key,authInfoVo.getConfigKey()))) {
				// "验证签名不通过";
				pgwRespQueryOrder.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
				pgwRespQueryOrder.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
			} else {
				String status =  resultMap.get("status");
				String resultCode = resultMap.get("result_code");
				String message = resultMap.get("message");
				if (KstHosConstant.STRING_0.equals(status)
						&& KstHosConstant.STRING_0.equals(resultCode)) {
					String tradeState = resultMap.get("trade_state");
					Integer commonOrderState = KstHosConstant.I0;
					switch (tradeState) {
						case SwiftpassConstants.TRADESTATE_NOTPAY://未支付
							commonOrderState = KstHosConstant.I0;
							break;
						case SwiftpassConstants.TRADESTATE_SUCCESS://支付成功
							commonOrderState = KstHosConstant.I2;
							String price = resultMap.get("total_fee");
							pgwRespQueryOrder.setPrice(new Integer(price));
							String payTime = resultMap.get("time_end");
							pgwRespQueryOrder.setPayTime(payTime);
							String orderId = resultMap.get("out_transaction_id");
							pgwRespQueryOrder.setOrderId(orderId);
							String transactionNo = resultMap.get("transaction_id");
							pgwRespQueryOrder.setTransactionNo(transactionNo);
							break;
						case SwiftpassConstants.TRADESTATE_CLOSED://已关闭
							commonOrderState = KstHosConstant.I5;
							break;
						case SwiftpassConstants.TRADESTATE_REVOKED://已撤销
							commonOrderState = KstHosConstant.I6;
							break;
						case SwiftpassConstants.TRADESTATE_REVERSE://已冲正(等于已撤销)
							commonOrderState = KstHosConstant.I6;
							break;
						case SwiftpassConstants.TRADESTATE_REFUND://已退费
							commonOrderState = KstHosConstant.I4;
							break;
						default:
							break;
					}
					pgwRespQueryOrder.setOrderState(commonOrderState);
					pgwRespQueryOrder.setRespCode(RetCode.Success.RET_10000);
					pgwRespQueryOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
				}else {
					pgwRespQueryOrder.setRespCode(RetCode.Pay.ERROR_QUERYORDER);
					pgwRespQueryOrder.setRespMsg(RetCode.Pay.ERROR_QUERYORDER.getMessage()
							+"|status:"+status+"|resultCode:"+resultCode+"|message"+message);
				}
			}
		} else {
			// 返回为空
			pgwRespQueryOrder.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespQueryOrder.setRespMsg("调用威富通接口异常！");
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
		// TODO Auto-generated method stub
		SortedMap<String, String> map = new TreeMap<String, String>();
		PgwRespQueryRefundOrder pgwRespQueryRefundOrder = new PgwRespQueryRefundOrder();
		map.put("service", "unified.trade.close");
		map.put("version", SwiftpassConstants.VERSION);
		map.put("charset", SwiftpassConstants.CHARSET);
		map.put("sign_type", SwiftpassConstants.SIGN_TYPE);
		map.put("mch_id", KasiteConfig.getSwiftpass(SwiftpassEnum.mch_id,authInfoVo.getConfigKey()));
		//平台退款单号 说明：refund_id、out_refund_no两个参数必填一个
		map.put("refund_id", pgwReqQueryRefundOrder.getRefundId());
		map.put("out_refund_no", pgwReqQueryRefundOrder.getRefundOrderId());
		map.put("nonce_str", TenpayUtil.getNonceStr());
		Map<String,String> params = SwiftpassUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SwiftpassUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString();
		String sign = MD5.sign(preStr, "&key=" + authInfoVo.getConfigKey(), SwiftpassConstants.CHARSET);
		map.put("sign", sign);
		String reqParam = SwiftpassUtils.parseXML(map);
		String respParam = HttpsClientUtils.SwiftpasshttpsPost(pgwReqQueryRefundOrder.getOrderId(),ApiModule.Swiftpass.unifiedtraderefundquery,
				SwiftpassConstants.SWIFTPASS_GATEWAY_URL, reqParam, SwiftpassConstants.CHARSET);
		Map<String, String> resultMap = null;
		if (StringUtil.isNotEmpty(respParam)) {
			resultMap = SwiftpassUtils.xml2Map(respParam.getBytes(SwiftpassConstants.CHARSET),
					SwiftpassConstants.CHARSET);
			if (!SwiftpassUtils.checkParam(resultMap,KasiteConfig.getSwiftpass(SwiftpassEnum.key,authInfoVo.getConfigKey()))) {
				// "验证签名不通过";
				pgwRespQueryRefundOrder.setRespCode(RetCode.Pay.SIGNATURE_FAILED);
				pgwRespQueryRefundOrder.setRespMsg(RetCode.Pay.SIGNATURE_FAILED.getMessage());
			} else {
				String status = resultMap.get("status");
				String resultCode = resultMap.get("result_code");
				String message = resultMap.get("message");
				if (KstHosConstant.STRING_0.equals(status)
						&& KstHosConstant.STRING_0.equals(resultCode)) {
					// 查询退款订单成功
					// 传支付相关订单，可查多笔，但是此接口封装本着简单的原则，只查一笔（即前端入参必须要有退款订单号）
					pgwRespQueryRefundOrder.setRefundOrderId(resultMap.get("out_refund_no_$0"));
					pgwRespQueryRefundOrder.setRefundId(resultMap.get("refund_id_$0"));
					pgwRespQueryRefundOrder.setRefundPrice(new Integer(resultMap.get("refund_fee_$0")));
					pgwRespQueryRefundOrder.setRefundTime(resultMap.get("refund_success_time_$0"));
					String refundStatus = resultMap.get("refund_status_$0");
					Integer commonRefundStatus = KstHosConstant.I0;
					switch (refundStatus) {
					case SwiftpassConstants.TRADESTATE_SUCCESS:// SUCCESS—退款成功
						commonRefundStatus = KstHosConstant.I4;
						break;
					case SwiftpassConstants.TRADESTATE_FAIL:// FAIL—退款失败
						commonRefundStatus = KstHosConstant.I7;
						break;
					case SwiftpassConstants.TRADESTATE_PROCESSING:// PROCESSING—退款处理中
						commonRefundStatus = KstHosConstant.I3;
						break;
					case SwiftpassConstants.TRADESTATE_NOTSURE:// NOTSURE—未确定，需要商户原退款单号重新发起
						commonRefundStatus = KstHosConstant.I3;
						break;
					case SwiftpassConstants.TRADESTATE_CHANGE:// CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。
						commonRefundStatus = KstHosConstant.I7;
						break;
					default:
						break;
					}
					pgwRespQueryRefundOrder.setRefundStatus(commonRefundStatus);
					pgwRespQueryRefundOrder.setRespCode(RetCode.Success.RET_10000);
					pgwRespQueryRefundOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
				} else {
					pgwRespQueryRefundOrder.setRespCode(RetCode.Pay.ERROR_QUERYORDER);
					pgwRespQueryRefundOrder.setRespMsg(RetCode.Pay.ERROR_QUERYORDER.getMessage() + "|status:" + status
							+ "|resultCode:" + resultCode + "|message" + message);
				}
			}
		} else {
			// 返回为空
			pgwRespQueryRefundOrder.setRespCode(RetCode.Common.ERROR_SYSTEM);
			pgwRespQueryRefundOrder.setRespMsg("调用威富通接口异常！");
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
		return null;
	}

	/**
	 * @param authInfoVo
	 * @param billDate
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespDownloadBill downloadBill(AuthInfoVo authInfoVo, Date billDate) throws Exception {
		return super.downloadBill(authInfoVo, billDate);
	}

	/**
	 * @return
	 */
	@Override
	public String mchType() {
		return ChannelTypeEnum.swiftpass.name()+SwiftpassConstants.MCH_TYPE_ZFB;
	}

}
