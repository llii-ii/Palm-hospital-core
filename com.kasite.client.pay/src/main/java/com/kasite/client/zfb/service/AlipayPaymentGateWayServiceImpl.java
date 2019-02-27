package com.kasite.client.zfb.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.coreframework.util.DateOper;
import com.kasite.client.zfb.constants.AlipayServiceEnvConstants;
import com.kasite.client.zfb.factory.AlipayAPIClientFactory;
import com.kasite.client.zfb.util.AlipayConstant;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.httpclient.http.RequestType;
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

@Service("alipay")
public class AlipayPaymentGateWayServiceImpl implements IPaymentGateWayService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);

	/**
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespUniteOrder uniteOrder(AuthInfoVo authInfoVo, PgwReqUniteOrder pgwReqUniteOrder) throws Exception {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String result = "";
		PgwRespUniteOrder pgwRespUniteOrder = new PgwRespUniteOrder();
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(authInfoVo.getConfigKey());// .getAlipayClientInstance();
		// 创建API对应的request类
		AlipayTradeCreateRequest  request = new AlipayTradeCreateRequest();
		String zfb_signPartner = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signPartner, authInfoVo.getConfigKey());
		// 接收微信支付异步通知回调地址 "/wxPay/{clientId}/{configKey}/{openId}/{token}/payNotify.do";
//			packageParams.put("notify_url", wx_pay_notify_url+"/wxPay/"+clientId+"/"+configKey+"/"+openId+"/"+token+"/payNotify.do");
		// 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
		request.setNotifyUrl(
				KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.zfb, authInfoVo.getConfigKey(), authInfoVo.getClientId(),
						pgwReqUniteOrder.getOpenId(), authInfoVo.getSessionKey(), pgwReqUniteOrder.getOrderId()));
		JSONObject bizContent = new JSONObject();
		// 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
		bizContent.put("out_trade_no", pgwReqUniteOrder.getOrderId());
		// 订单标题
		bizContent.put("subject", pgwReqUniteOrder.getSubject());
		// 订单描述
		bizContent.put("body", pgwReqUniteOrder.getBody());
		bizContent.put("buyer_id", pgwReqUniteOrder.getOpenId());
		//TODO 如果限制信用卡
		if (KstHosConstant.I1.equals(pgwReqUniteOrder.getIsLimitCredit())) {
			bizContent.put("disable_pay_channels", "creditCard,pcredit");
		}
		// 该笔订单允许的最晚付款时间，逾期将关闭交易。
		bizContent.put("timeout_express", "24h");
		/** 支付以元为单位,传入的参数是以分为单位,转化为元,除以100 **/
		BigDecimal totalAmount = new BigDecimal(pgwReqUniteOrder.getPrice()).divide(new BigDecimal(100));
		// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
		bizContent.put("total_amount", totalAmount.toString());
		JSONObject extendParams = new JSONObject();
		// 父商户 appid
		extendParams.put("sys_service_provider_id", zfb_signPartner);
		bizContent.put("extend_params", extendParams);
		String reqParamStr = bizContent.toString();
		request.setBizContent(bizContent.toString());
		// 通过alipayClient调用API，获得对应的response类
		AlipayTradeCreateResponse  response = null;
		try {
			response = alipayClient.execute(request);
			result = JSONObject.toJSONString(response.getBody());
			if(response.isSuccess()){
				isSuccess = true;
				pgwRespUniteOrder.setPayInfo(response.getTradeNo());
				pgwRespUniteOrder.setRespCode(RetCode.Success.RET_10000);
				pgwRespUniteOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
			}else{
				pgwRespUniteOrder.setRespCode(RetCode.Pay.ERROR_UNITEORDER);
				pgwRespUniteOrder.setRespMsg(RetCode.Pay.ERROR_UNITEORDER.getMessage()
						+"|Code:"+response.getCode()+"|Msg"+response.getMsg()
						+"|SubCode："+response.getSubCode()+"|SubMsg："+response.getSubMsg());
			}
			return pgwRespUniteOrder;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, authInfoVo, e);
			throw e;
		} finally {
			LogUtil.saveCallZfbLog(pgwReqUniteOrder.getOrderId(), ApiModule.Zfb.pay_unifiedorder, reqParamStr, result, null,
					System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, RequestType.post,
					isSuccess);
		}
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqRefund
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespRefund refund(AuthInfoVo authInfoVo, PgwReqRefund pgwReqRefund) throws Exception {
		long start = System.currentTimeMillis();
		PgwRespRefund pgwRespRefund = new PgwRespRefund();
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(pgwReqRefund.getPayConfigKey());
		AlipayTradeRefundRequest refundRequest = new AlipayTradeRefundRequest();
		// 参数 类型 是否必填 最大长度 描述 示例值
		// out_trade_no String 特殊可选 64 订单支付时传入的商户订单号,不能和 trade_no同时为空。
		// 20150320010101001
		// trade_no String 特殊可选 64 支付宝交易号，和商户订单号不能同时为空
		// 2014112611001004680073956707
		// refund_amount Price 必须 9 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数 200.12
		// refund_reason String 可选 256 退款的原因说明 正常退款
		// out_request_no String 可选 64 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
		// HZ01RF001
		// operator_id String 可选 30 商户的操作员编号 OP001
		// store_id String 可选 32 商户的门店编号 NJ_S_001
		// terminal_id String 可选 32 商户的终端编号 NJ_T_001
		JSONObject bizContent = new JSONObject();
		if (!StringUtil.isEmpty(pgwReqRefund.getOrderId())) {
			bizContent.put("out_trade_no", pgwReqRefund.getOrderId());
		} else if (!StringUtil.isEmpty(pgwReqRefund.getTransactionNo())) {
			bizContent.put("trade_no", pgwReqRefund.getTransactionNo());
		}
		bizContent.put("refund_amount",StringUtil.fenChangeYuan(pgwReqRefund.getRefundPrice()));
		bizContent.put("refund_reason", pgwReqRefund.getRemark());
		bizContent.put("out_request_no", pgwReqRefund.getRefundOrderId());
		bizContent.put("operator_id", "");
		bizContent.put("store_id", "");
		bizContent.put("terminal_id", "");
		refundRequest.setBizContent(bizContent.toString());
		String reqParamStr = bizContent.toString();
		AlipayTradeRefundResponse response = null;
		try {
			response = alipayClient.execute(refundRequest);
			if (response.isSuccess()) {
				pgwRespRefund.setRefundId(pgwReqRefund.getRefundOrderId());
				pgwRespRefund.setRespCode(RetCode.Success.RET_10000);
				pgwRespRefund.setRespMsg( "申请退款成功！");
			} else {
				pgwRespRefund.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespRefund.setRespMsg(response.getMsg() + "|" + response.getSubMsg());
			}
			return pgwRespRefund;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, authInfoVo, e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(pgwReqRefund.getOrderId(),ApiModule.Zfb.pay_refund, reqParamStr,response!=null?response.getBody():"", null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, true);
		}
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqSweepCodePay
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespSweepCodePay sweepCodePay(AuthInfoVo authInfoVo,PgwReqSweepCodePay pgwReqSweepCodePay)
			throws Exception {
		PgwRespSweepCodePay pgwRespSweepCodePay = new PgwRespSweepCodePay();
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String zfbKey = authInfoVo.getConfigKey();
		String clientId = authInfoVo.getClientId();
		String openId = authInfoVo.getSign();
		String token = authInfoVo.getSessionKey();
		String reqParamStr = null;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);//ClientFactory.getAlipayClientInstance();

			String zfb_payNotifyUrl = KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.zfb, zfbKey, clientId,
					openId, token,pgwReqSweepCodePay.getOrderId());//(ZFBConfigEnum.zfb_payNotifyUrl, zfbKey);
			String zfb_signPartner = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signPartner, zfbKey);
			
			JSONObject bizContent = new JSONObject();
			bizContent.put("out_trade_no", pgwReqSweepCodePay.getOrderId());
			bizContent.put("scene", "bar_code");//支付场景  条码支付，取值：bar_code 声波支付，取值：wave_code
			bizContent.put("auth_code", pgwReqSweepCodePay.getAuthCode());
			bizContent.put("subject", pgwReqSweepCodePay.getSubject());
			/** 注意:单位【元】 */
			bizContent.put("total_amount", StringUtil.fenChangeYuan(pgwReqSweepCodePay.getTotalPrice()));
			bizContent.put("body", pgwReqSweepCodePay.getBody());
			bizContent.put("terminal_id", pgwReqSweepCodePay.getDeviceInfo());
			// 如果限制信用卡
			if (KstHosConstant.I1.equals(pgwReqSweepCodePay.getIsLimitCredit())) {
				bizContent.put("disable_pay_channels", "creditCard,pcredit");//信用卡/花呗
			}
			JSONObject extendParams = new JSONObject();
			extendParams.put("sys_service_provider_id", zfb_signPartner);
			bizContent.put("extend_params", extendParams);

			AlipayTradePayRequest request = new AlipayTradePayRequest();
			/** 回调地址 */
			request.setNotifyUrl(zfb_payNotifyUrl);
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradePayResponse response = alipayClient.execute(request);
			result = response.getBody();
			if( response.isSuccess()) {
				isSuccess = true;
				if( AlipayConstant.CODE_10000.equals(response.getCode())) {//直接支付成功
					pgwRespSweepCodePay.setRespCode(RetCode.Success.RET_10000);
					pgwRespSweepCodePay.setRespMsg(RetCode.Success.RET_10000.getMessage());
					pgwRespSweepCodePay.setTransactionNo(response.getTradeNo());
				}else if(AlipayConstant.CODE_10003.equals(response.getCode())){
					pgwRespSweepCodePay.setRespCode(RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING);
					pgwRespSweepCodePay.setRespMsg(RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING.getMessage());
				}else {
					//TODO 其他情况待补充！
					pgwRespSweepCodePay.setRespCode(RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING);
					pgwRespSweepCodePay.setRespMsg(RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING.getMessage());
				}
			}else {
				isSuccess = false;
				if( AlipayConstant.SUB_CODE_PAYMENT_AUTH_CODE_INVALID.equals(response.getSubCode())) {
					//无效的二维码，直接返回错误
					pgwRespSweepCodePay.setRespCode(RetCode.Pay.ERROR_SWEEPCODEPAY_AUTHCODEINVALID);
					pgwRespSweepCodePay.setRespMsg(RetCode.Pay.ERROR_SWEEPCODEPAY_AUTHCODEINVALID.getMessage()
							+"|alipayMsg:"+response.getMsg()+"|alipaySubMsg:"+response.getSubMsg());
				}else {
					pgwRespSweepCodePay.setRespCode(RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING);
					pgwRespSweepCodePay.setRespMsg(RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING.getMessage());
				}
			}
			return pgwRespSweepCodePay;
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, authInfoVo, e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(pgwReqSweepCodePay.getOrderId(),ApiModule.Zfb.alipay_trade_pay, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqRevoke
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespRevoke revoke(AuthInfoVo authInfoVo, PgwReqRevoke pgwReqRevoke) throws Exception {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String result = "";
		String reqParamStr = null;
		PgwRespRevoke pgwRespRevoke = new PgwRespRevoke();
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(authInfoVo.getConfigKey());//ClientFactory.getAlipayClientInstance();
			JSONObject bizContent = new JSONObject();
			if (!StringUtil.isEmpty(pgwReqRevoke.getTransactionNo())) {
				bizContent.put("trade_no", pgwReqRevoke.getTransactionNo());
			}
			if (!StringUtil.isEmpty(pgwReqRevoke.getOrderId())) {
				bizContent.put("out_trade_no", pgwReqRevoke.getOrderId());
			}
			AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradeCancelResponse response = alipayClient.execute(request);
			if( response.isSuccess() ) {
				pgwRespRevoke.setRespCode(RetCode.Success.RET_10000);
				pgwRespRevoke.setRespMsg(RetCode.Success.RET_10000.getMessage());
			}else {
				pgwRespRevoke.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespRevoke.setRespMsg(RetCode.Common.ERROR_SYSTEM.getExceptMsg()
						+"|subCode"+response.getSubCode()+"|subMsg"+response.getSubMsg());
			}
			result = response.getBody();
			return pgwRespRevoke;
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, authInfoVo, e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(pgwReqRevoke.getOrderId(),ApiModule.Zfb.alipayTradeCancel, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqClose
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespClose close(AuthInfoVo authInfoVo, PgwReqClose pgwReqClose) throws Exception {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = null;
		String result = "";
		PgwRespClose pgwRespClose = new PgwRespClose();
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(authInfoVo.getConfigKey());//ClientFactory.getAlipayClientInstance();
			JSONObject bizContent = new JSONObject();
			if (!StringUtil.isEmpty(pgwReqClose.getOrderId())) {
				bizContent.put("out_trade_no", pgwReqClose.getOrderId());
			}
			if (!StringUtil.isEmpty(pgwReqClose.getTransactionNo())) {
				bizContent.put("trade_no", pgwReqClose.getTransactionNo());
			}
			AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradeCloseResponse response = alipayClient.execute(request);
			if( response.isSuccess()) {
				pgwRespClose.setRespCode(RetCode.Success.RET_10000);
				pgwRespClose.setRespMsg(RetCode.Success.RET_10000.getMessage());
			}else {
				pgwRespClose.setRespCode(RetCode.Common.ERROR_SYSTEM);
				pgwRespClose.setRespMsg(RetCode.Common.ERROR_SYSTEM.getExceptMsg()
						+"|subCode"+response.getSubCode()+"|subMsg"+response.getSubMsg());
			}
			result = response.getBody();
			isSuccess = true;
			return pgwRespClose;
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, authInfoVo, e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(pgwReqClose.getOrderId(),ApiModule.Zfb.pay_closeorder, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
	}

	/**
	 * @param authInfoVo
	 * @param pgwReqQueryOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public PgwRespQueryOrder queryOrder(AuthInfoVo authInfoVo, PgwReqQueryOrder pgwReqQueryOrder) throws Exception {
		long start = System.currentTimeMillis();
		PgwRespQueryOrder pgwRespQueryOrder = new PgwRespQueryOrder();
		boolean isSuccess = false;
		String reqParamStr = null;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(authInfoVo.getConfigKey());
			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
			JSONObject bizContent = new JSONObject();
			if (!StringUtil.isEmpty(pgwReqQueryOrder.getTransactionNo())) {
				bizContent.put("trade_no", pgwReqQueryOrder.getTransactionNo());
			}
			if (!StringUtil.isEmpty(pgwReqQueryOrder.getOrderId())) {
				bizContent.put("out_trade_no", pgwReqQueryOrder.getOrderId());
			}
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				pgwRespQueryOrder.setRespCode(RetCode.Success.RET_10000);
				pgwRespQueryOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
				String tradeStatus = response.getTradeStatus();
				switch (tradeStatus) {
				case AlipayConstant.TRADE_STATUS_FINISHED://交易结束，不可退费
					pgwRespQueryOrder.setOrderState(KstHosConstant.I8);
					break;
				case AlipayConstant.TRADE_STATUS_SUCCESS://支付成功
					pgwRespQueryOrder.setOrderState(KstHosConstant.I2);
					break;
				case AlipayConstant.TRADE_STATUS_CLOSED://付款交易超时关闭，或支付完成后全额退款
					pgwRespQueryOrder.setOrderState(KstHosConstant.I5);
					break;
				case AlipayConstant.TRADE_STATUS_WAIT://交易创建，等待买家付款
					pgwRespQueryOrder.setOrderState(KstHosConstant.I1);
					break;
				default:
					break;
				}
				pgwRespQueryOrder.setOrderId(response.getOutTradeNo());
				if( response.getSendPayDate()!=null ) {
					pgwRespQueryOrder.setPayTime(DateOper.formatDate(response.getSendPayDate(),"yyyyMMddHHmmss"));
				}
				if( StringUtil.isNotBlank(response.getTotalAmount()) ) {
					pgwRespQueryOrder.setPrice(StringUtil.yuanChangeFenInt(response.getTotalAmount()));
				}
				// 单位:元 转分
				pgwRespQueryOrder.setTransactionNo(response.getTradeNo());
				// 其他参数，酌情自己添加
			} else {
				RetCode retCode = alipayRetrunCodeParseRetCode(response.getSubCode());
				pgwRespQueryOrder.setRespCode(retCode);
				pgwRespQueryOrder.setRespMsg(retCode.getMessage()
						+"|subCode:"+response.getSubCode() + "|subMsg:" + response.getSubMsg());
			}
			isSuccess = true;
			result = response.getBody();
			return pgwRespQueryOrder;
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, authInfoVo, e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(pgwReqQueryOrder.getOrderId(),ApiModule.Zfb.pay_orderquery, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
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
		long start = System.currentTimeMillis();
		PgwRespQueryRefundOrder pgwRespQueryRefundOrder = new PgwRespQueryRefundOrder();
		boolean isSuccess = false;
		String reqParamStr = null;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(authInfoVo.getConfigKey());
			AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
			JSONObject bizContent = new JSONObject();
			if (!StringUtil.isEmpty(pgwReqQueryRefundOrder.getTransactionNo())) {
				bizContent.put("trade_no", pgwReqQueryRefundOrder.getTransactionNo());
			}
			if (!StringUtil.isEmpty(pgwReqQueryRefundOrder.getOrderId())) {
				bizContent.put("out_trade_no", pgwReqQueryRefundOrder.getOrderId());
			}
			if(!StringUtil.isEmpty(pgwReqQueryRefundOrder.getRefundOrderId())) {
				bizContent.put("out_request_no", pgwReqQueryRefundOrder.getRefundOrderId());
			}
			if(!StringUtil.isEmpty(pgwReqQueryRefundOrder.getRefundId())) {
				bizContent.put("out_request_no", pgwReqQueryRefundOrder.getRefundId());
			}
//			if (!StringUtil.isEmpty(orgPid)) {
//				bizContent.put("org_pid", orgPid);
//			}
			
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
			if (response.isSuccess() && !StringUtil.isEmpty(response.getOutRequestNo())) {
				pgwRespQueryRefundOrder.setRespCode(RetCode.Success.RET_10000);
				pgwRespQueryRefundOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
				pgwRespQueryRefundOrder.setRefundStatus(KstHosConstant.I4);
				pgwRespQueryRefundOrder.setRefundId(response.getOutRequestNo());
				pgwRespQueryRefundOrder.setRefundOrderId(response.getOutRequestNo());
				pgwRespQueryRefundOrder.setRefundPrice(StringUtil.yuanChangeFenInt( response.getRefundAmount()));
				//pgwRespQueryRefundOrder.setRefundTime("无");
			} else {
				RetCode retCode = alipayRetrunCodeParseRetCode(response.getSubCode());
				pgwRespQueryRefundOrder.setRespCode(retCode);
				pgwRespQueryRefundOrder.setRespMsg(retCode.getMessage()
						+"|subCode:"+response.getSubCode() + "|subMsg:" + response.getSubMsg());
			}
			isSuccess = true;
			result = response.getBody();
			return pgwRespQueryRefundOrder;
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, authInfoVo, e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.pay_refundquery, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
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
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String result = "";
		PgwRespWapUniteOrder pgwRespWapUniteOrder = new PgwRespWapUniteOrder();
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(authInfoVo.getConfigKey());// .getAlipayClientInstance();
		// 创建API对应的request类
		AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
		String zfb_signPartner = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signPartner, authInfoVo.getConfigKey());
		// 接收微信支付异步通知回调地址 "/wxPay/{clientId}/{configKey}/{openId}/{token}/payNotify.do";
//			packageParams.put("notify_url", wx_pay_notify_url+"/wxPay/"+clientId+"/"+configKey+"/"+openId+"/"+token+"/payNotify.do");
		// 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
		request.setNotifyUrl(
				KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.zfb, authInfoVo.getConfigKey(), authInfoVo.getClientId(),
						pgwReqUniteOrder.getOpenId(), authInfoVo.getSessionKey(), pgwReqUniteOrder.getOrderId()));
		// 前台回跳
		request.setReturnUrl(pgwReqUniteOrder.getReturnUrl());

		JSONObject bizContent = new JSONObject();
		// 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
		bizContent.put("out_trade_no", pgwReqUniteOrder.getOrderId());
		// 订单标题
		bizContent.put("subject", pgwReqUniteOrder.getSubject());
		// 订单描述
		bizContent.put("body", pgwReqUniteOrder.getBody());
		// 如果限制信用卡
		if (KstHosConstant.I1.equals(pgwReqUniteOrder.getIsLimitCredit())) {
			bizContent.put("disable_pay_channels", "creditCard,pcredit");
		}
		// 销售产品码，商家和支付宝签约的产品码。该产品请填写固定值：QUICK_WAP_WAY
		bizContent.put("product_code", "QUICK_WAP_WAY");
		// 该笔订单允许的最晚付款时间，逾期将关闭交易。
		bizContent.put("timeout_express", "24h");
		/** 支付以元为单位,传入的参数是以分为单位,转化为元,除以100 **/
		BigDecimal totalAmount = new BigDecimal(pgwReqUniteOrder.getPrice()).divide(new BigDecimal(100));
		// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
		bizContent.put("total_amount", totalAmount.toString());
		JSONObject extendParams = new JSONObject();
		// 父商户 appid
		extendParams.put("sys_service_provider_id", zfb_signPartner);
		bizContent.put("extend_params", extendParams);
		String reqParamStr = bizContent.toString();
		request.setBizContent(bizContent.toString());
		// 通过alipayClient调用API，获得对应的response类
		AlipayTradeWapPayResponse response = null;
		try {
			response = alipayClient.pageExecute(request);
			result = JSONObject.toJSONString(response.getParams());
			if(response.isSuccess()){
				isSuccess = true;
				pgwRespWapUniteOrder.setPayInfo(response.getBody());
				pgwRespWapUniteOrder.setRespCode(RetCode.Success.RET_10000);
				pgwRespWapUniteOrder.setRespMsg(RetCode.Success.RET_10000.getMessage());
			}else{
				pgwRespWapUniteOrder.setRespCode(RetCode.Pay.ERROR_UNITEORDER);
				pgwRespWapUniteOrder.setRespMsg(RetCode.Pay.ERROR_UNITEORDER.getMessage()
						+"|Code:"+response.getCode()+"|Msg"+response.getMsg()
						+"|SubCode："+response.getSubCode()+"|SubMsg："+response.getSubMsg());
			}
			return pgwRespWapUniteOrder;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, authInfoVo, e);
			throw e;
		} finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.pay_unifiedorder, reqParamStr, result, null,
					System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, RequestType.post,
					isSuccess);
		}
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

	private RetCode alipayRetrunCodeParseRetCode(String subCode) {
		if( StringUtil.isEmpty(subCode) ) {
			return RetCode.Common.ERROR_SYSTEM;
		}
		RetCode retCode = null;
		switch (subCode) {
		case AlipayConstant.SUB_CODE_PAYMENT_AUTH_CODE_INVALID:
			//当面付二维码失效
			retCode = RetCode.Pay.ERROR_SWEEPCODEPAY_AUTHCODEINVALID;
			break;
		case AlipayConstant.SUB_CODE_SYSTEM_ERROR:
			//支付宝系统错误
			retCode = RetCode.Common.ERROR_SYSTEM;
			break;
		case AlipayConstant.SUB_CODE_INVALID_PARAMETER:
			//调用支付宝入参错误
			retCode = RetCode.Common.ERROR_PARAM;
			break;
		case AlipayConstant.SUB_CODE_TRADE_NOT_EXIST:
			//不存在支付订单
			retCode = RetCode.Pay.ERROR_MERCHANTORDER;
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
		return ChannelTypeEnum.zfb.name();
	}
}
