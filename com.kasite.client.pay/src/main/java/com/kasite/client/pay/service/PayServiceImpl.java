package com.kasite.client.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.client.zfb.service.AlipayService;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.PayRule;
import com.kasite.core.common.config.SwiftpassEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.TenpayConstant;
import com.kasite.core.common.util.wechat.TenpayService;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.pay.IPayService;
import com.kasite.core.serviceinterface.module.pay.IPaymentGateWayService;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqQueryOrder;
import com.kasite.core.serviceinterface.module.pay.req.ReqClose;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetPayQRCode;
import com.kasite.core.serviceinterface.module.pay.req.ReqPayStartBizOrderExecute;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryMerchantOrder;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryMerchantRefund;
import com.kasite.core.serviceinterface.module.pay.req.ReqRefund;
import com.kasite.core.serviceinterface.module.pay.req.ReqRevoke;
import com.kasite.core.serviceinterface.module.pay.req.ReqSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.req.ReqUniteOrder;
import com.kasite.core.serviceinterface.module.pay.req.ReqWapUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespClose;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespQueryOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespQueryRefundOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRefund;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRevoke;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespWapUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.RespRefund;
import com.kasite.core.serviceinterface.module.pay.resp.RespUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.RespWapUniteOrder;
import com.yihu.hos.service.CommonService;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 
 * 林建法-2018年12月18日 14:20:12-此service禁止抛出业务类型的异常，只可抛出系统级异常（后面慢慢收）。
 * 
 */
@Service("pay.PayWs")
public class PayServiceImpl extends CommonService implements IPayService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);

	@Autowired
	private PayBackCallUtil payBackCallUtil; 
	
	
	@Override
	public String Refund(InterfaceMessage msg) throws Exception{
		return this.refund(new CommonReq<ReqRefund>(new ReqRefund(msg))).toResult();
	}

	@Override
	public CommonResp<RespRefund> refund(CommonReq<ReqRefund> reqParam) throws Exception{
		ReqRefund reqRefund = reqParam.getParam();
		String clientId = reqRefund.getClientId();
		/***智付网关校验 -- start ***/
		/*
		 * 判断渠道是否有退费权限，如果没有支付权限则没有退费权限。目前是一对一的
		 * 如果有对应的支付权限则可以进行退费。
		 */
		boolean isExistConfigKey = KasiteConfig.existClientPayConfig(clientId, reqRefund.getPayConfigKey());
		if(!isExistConfigKey) {
			return new CommonResp<RespRefund>(reqParam,KstHosConstant.DEFAULTTRAN,RetCode.Pay.ERROR_NOTEXISTCLIENTIDCONFIGKEY,
					"渠道不支持退款：clientId="+clientId+" || configKey="+reqRefund.getPayConfigKey());
		}
		try {
			KasiteConfig.refundRule(reqRefund.getPayConfigKey());
		}catch (RRException e) {
			LogUtil.error(log, reqRefund.getAuthInfo(),e);
			return new CommonResp<RespRefund>(reqParam,KstHosConstant.DEFAULTTRAN,e.getRetCode(),e.getMessage());
		}
		/***智付网关校验 -- end ***/
		
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(clientId,reqRefund.getPayConfigKey());
		String swiftpassMchType = KstHosConstant.STRING_EMPTY;
		if( ChannelTypeEnum.swiftpass.equals(channelTypeEnum)) {
			swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,reqRefund.getPayConfigKey());
		}
		IPaymentGateWayService payGateWayService = HandlerBuilder.get().getPayGateWayInstance(channelTypeEnum.name()+swiftpassMchType);
		if( payGateWayService == null ) {
			return new CommonResp<RespRefund>(reqParam,KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_REFUND,"退款失败，未找到渠道对应的退费方法！");
		}
		PgwRespRefund pgwRespRefund = payGateWayService.refund(reqRefund.getAuthInfo(), reqRefund.toPgwReqRefund());
		if(!KstHosConstant.SUCCESS_CODE.equals(pgwRespRefund.getRespCode().getCode())) {
			return new CommonResp<RespRefund>(reqParam, RetCode.Pay.ERROR_REFUND,pgwRespRefund.getRespMsg());
		}else {
			RespRefund respRefund = new RespRefund();
			respRefund.setRefundNo(pgwRespRefund.getRefundId());
			if (reqRefund.getIsCallBack() == 1) {
				payBackCallUtil.addRefundNotify(reqParam.getMsg(),reqRefund.getOrderId(), reqRefund.getRefundOrderId(),
						pgwRespRefund.getRefundId(),clientId,reqRefund.getTotalPrice(),
						reqRefund.getRefundPrice(),reqRefund.getPayConfigKey());
			}
			return new CommonResp<RespRefund>(reqParam,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respRefund);
		}
	}

	@Override
	public String UniteOrder(InterfaceMessage msg) throws Exception {
		return this.uniteOrder(new CommonReq<ReqUniteOrder>(new ReqUniteOrder(msg))).getDataCaseRetCode().getContent();
	}
	
	@Override
	public CommonResp<RespUniteOrder> uniteOrder(CommonReq<ReqUniteOrder> commReq) throws Exception {
		ReqUniteOrder req = commReq.getParam();
//		JSONObject retJson = null;
		String orderId = req.getOrderId();
		//加入订单校验逻辑 确认订单已经存在并且是需要线上支付的订单
		ReqPayStartBizOrderExecute t = new ReqPayStartBizOrderExecute(req.getMsg(), orderId, null, null);
		CommonReq<ReqPayStartBizOrderExecute> r = new CommonReq<ReqPayStartBizOrderExecute>(t);
		CommonResp<RespMap> resp = HandlerBuilder.get().getBizPayStartOrderCheckHandler().checkPayOrder(r);
		if(!resp.getCode().equals(KstHosConstant.SUCCESSCODE)) {
			//如果返回的不成功则直接返回前端失败并按照订单校验的失败原因返回出去
			return new CommonResp<RespUniteOrder>(commReq,resp.getRetCode(),resp.getMessage());
		} 
		//获取订单实际需要支付的金额
		Integer price = resp.getResultData().getInteger(ApiKey.checkPayOrder.price);
		String priceName =  resp.getResultData().getString(ApiKey.checkPayOrder.priceName);
		String configKey = req.getConfigKey();
		/***智付网关校验 -- start ***/
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(req.getClientId(),req.getConfigKey());
		KasiteConfig.payRule(req.getConfigKey(), price);
		PayRule rule = KasiteConfig.getPayRule(configKey);
		if(StringUtil.isEmpty(req.getPrice())) {
			req.setPrice(price);
		}
		if(null != rule) {
			String c = rule.getCreditCardsAccepted();
			if(StringUtil.isNotBlank(rule.getCreditCardsAcceptedByClientId(req.getClientId()))) {
				c = rule.getCreditCardsAcceptedByClientId(req.getClientId());
			}
			if(StringUtil.isNotBlank(c) && "true".equalsIgnoreCase(c)) {
				req.setIsLimitCredit(0);
			}else if(StringUtil.isNotBlank(c) && "false".equalsIgnoreCase(c)){
				req.setIsLimitCredit(1);
			}
		}
		/***智付网关校验 -- end ***/
		String swiftpassMchType = KstHosConstant.STRING_EMPTY;
		if( ChannelTypeEnum.swiftpass.equals(channelTypeEnum)) {
			swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,req.getAuthInfo().getConfigKey());
		}
		IPaymentGateWayService payGateWayService = HandlerBuilder.get().getPayGateWayInstance(channelTypeEnum.name()+swiftpassMchType);
		if( payGateWayService == null ) {
			return new CommonResp<RespUniteOrder>(commReq, RetCode.Common.ERROR_SYSTEM,"统一下单失败，未找到渠道对应的退费方法！");
		}
		PgwRespUniteOrder pgwRespUniteOrder = payGateWayService.uniteOrder(req.getAuthInfo(), req.toPgwReqUniteOrder());
		if(!KstHosConstant.SUCCESS_CODE.equals(pgwRespUniteOrder.getRespCode().getCode())) {
			return new CommonResp<RespUniteOrder>(commReq, RetCode.Pay.ERROR_UNITEORDER,pgwRespUniteOrder.getRespMsg());
		}else {
			RespUniteOrder rx = new RespUniteOrder();
			rx.setOrderId(orderId);
			rx.setContent(pgwRespUniteOrder.getPayInfo());
			return new CommonResp<RespUniteOrder>(commReq, RetCode.Success.RET_10000,rx);
		}
	}


	@Override
	public String GetPayQRCode(InterfaceMessage msg) throws Exception {
		return this.getPayQRCode(new CommonReq<ReqGetPayQRCode>(new ReqGetPayQRCode(msg))).toResult();
	}
	
	@Override
	public CommonResp<RespMap> getPayQRCode(CommonReq<ReqGetPayQRCode> commReq) throws Exception {
		ReqGetPayQRCode reqGetPayQRCode = commReq.getParam();
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(reqGetPayQRCode.getClientId(),reqGetPayQRCode.getConfigKey());
		JSONObject wxRetJson = null;
		JSONObject zfbRetJson = null;
		if (ChannelTypeEnum.wechat.equals(channelTypeEnum)) {
			// 进入微信统一下单
			String configKey = KasiteConfig.getClientConfig(ClientConfigEnum.WeChatConfigKey, reqGetPayQRCode.getClientId());
			wxRetJson = TenpayService.uniteOrder(reqGetPayQRCode.getAuthInfo(),configKey,reqGetPayQRCode.getOrderId(), null, reqGetPayQRCode.getPrice(),
					reqGetPayQRCode.getIsLimitCredit(), reqGetPayQRCode.getBody(), reqGetPayQRCode.getRemoteIp(),
					TenpayConstant.TRADE_TYPE_NATIVE);
		} else if (ChannelTypeEnum.zfb.equals(channelTypeEnum)) {
			// 进入支付宝预下单接口
			String configKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, reqGetPayQRCode.getClientId());
			zfbRetJson = AlipayService.tradePrecreate(reqGetPayQRCode.getAuthInfo(),configKey,reqGetPayQRCode.getOrderId(), reqGetPayQRCode.getBody(),
					reqGetPayQRCode.getSubject(), reqGetPayQRCode.getPrice(), reqGetPayQRCode.getIsLimitCredit());
		} else {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM,"生成支付二维码失败！未找到渠道对应的退费方法！");
		}
		RespMap resp = new RespMap();
		if(wxRetJson != null && wxRetJson.containsKey(KstHosConstant.QRCODEURL)) {
			resp.put(ApiKey.GetPayQRCode.QRCodeUrl, wxRetJson.getString("QRCodeUrl"));
		}
		if (zfbRetJson != null && zfbRetJson.containsKey(KstHosConstant.QRCODEURL)) {
			resp.put(ApiKey.GetPayQRCode.QRCodeUrl, zfbRetJson.getString("QRCodeUrl"));
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public String SweepCodePay(InterfaceMessage msg)  throws Exception{
		return this.sweepCodePay(new CommonReq<ReqSweepCodePay>(new ReqSweepCodePay(msg))).toResult();
	}
	
	@Override
	public CommonResp<RespMap> sweepCodePay(CommonReq<ReqSweepCodePay> commReq) throws Exception {
		ReqSweepCodePay reqSweepCodePay = commReq.getParam();
		Integer isLimitCredit = 0;
		/***智付网关校验 -- start ***/
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(reqSweepCodePay.getClientId(),reqSweepCodePay.getConfigKey());
		//KasiteConfig.payRule(reqSweepCodePay.getConfigKey(), reqSweepCodePay.getTotalFee());
		PayRule rule = KasiteConfig.getPayRule(reqSweepCodePay.getConfigKey());
		if(null != rule) {
			String c = rule.getCreditCardsAccepted();
			if(StringUtil.isNotBlank(rule.getCreditCardsAcceptedByClientId(reqSweepCodePay.getClientId()))) {
				c = rule.getCreditCardsAcceptedByClientId(reqSweepCodePay.getClientId());
			}
			if(StringUtil.isNotBlank(c) && "true".equalsIgnoreCase(c)) {
				isLimitCredit = 0;
			}else if(StringUtil.isNotBlank(c) && "false".equalsIgnoreCase(c)){
				isLimitCredit = 1;
			}
		}
		reqSweepCodePay.setIsLimitCredit(isLimitCredit);
		//支付宝的当面付，alipay.trade.pay接口不支持禁用信用卡之类的
		/***智付网关校验 -- end ***/
		String swiftpassMchType = KstHosConstant.STRING_EMPTY;
		if( ChannelTypeEnum.swiftpass.equals(channelTypeEnum)) {
			swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,reqSweepCodePay.getConfigKey());
		}
		IPaymentGateWayService payGateWayService = HandlerBuilder.get().getPayGateWayInstance(channelTypeEnum.name()+swiftpassMchType);
		if( payGateWayService == null ) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM,"支付失败，未找到渠道对应的支付方法！");
		}
		PgwRespSweepCodePay pgwRespSweepCodePay = payGateWayService.sweepCodePay(reqSweepCodePay.getAuthInfo(), reqSweepCodePay.toPgwReqSweepCodePay());
		RespMap resp = new RespMap();
		resp.put(ApiKey.SweepCodePay.OrderId, reqSweepCodePay.getOrderId());
		if(RetCode.Success.RET_10000.equals(pgwRespSweepCodePay.getRespCode())) {
			//银联的二维码（被扫）提供异步通知，这里的操作是为了做double check
			payBackCallUtil.addPayNotify(commReq.getMsg(),reqSweepCodePay.getOrderId(), pgwRespSweepCodePay.getTransactionNo(), reqSweepCodePay.getClientId(),
					reqSweepCodePay.getTotalFee(),reqSweepCodePay.getConfigKey());
			resp.put(ApiKey.SweepCodePay.TotalFee, reqSweepCodePay.getTotalFee());
			resp.put(ApiKey.SweepCodePay.TransactionId, pgwRespSweepCodePay.getTransactionNo());
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,resp);
		}else if(RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING.equals(pgwRespSweepCodePay.getRespCode())) {
			/** 用户正在支付，输入密码->将本地订单号加入商户订单核实数据 */
			payBackCallUtil.addMerchantOrderCheck(reqSweepCodePay.getAuthInfo(),reqSweepCodePay.getOrderId(), pgwRespSweepCodePay.getTransactionNo(), 
					new Integer(0), reqSweepCodePay.getConfigKey(),reqSweepCodePay.getClientId());
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING,pgwRespSweepCodePay.getRespMsg(),resp);
		}else if(RetCode.Pay.ERROR_SWEEPCODEPAY_AUTHCODEINVALID.equals(pgwRespSweepCodePay.getRespCode())) {
			//无效的二维码，直接返回错误
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_SWEEPCODEPAY_AUTHCODEINVALID,pgwRespSweepCodePay.getRespMsg());
		}else {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_SWEEPCODEPAY_USERPAYING,pgwRespSweepCodePay.getRespMsg(),resp);
		}
	}

	@Override
	public String Revoke(InterfaceMessage msg) throws Exception{
		return this.revoke(new CommonReq<ReqRevoke>(new ReqRevoke(msg))).toResult();
	}
	
	@Override
	public CommonResp<RespMap> revoke(CommonReq<ReqRevoke> reqParam) throws Exception{
		ReqRevoke reqRevoke = reqParam.getParam();
		AuthInfoVo authInfoVo = reqRevoke.getAuthInfo();
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(reqRevoke.getClientId(),reqRevoke.getConfigKey());
		String swiftpassMchType = KstHosConstant.STRING_EMPTY;
		if( ChannelTypeEnum.swiftpass.equals(channelTypeEnum)) {
			swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,reqRevoke.getConfigKey());
		}
		IPaymentGateWayService payGateWayService = HandlerBuilder.get().getPayGateWayInstance(channelTypeEnum.name()+swiftpassMchType);
		if( payGateWayService == null ) {
			return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM,"撤销失败，未找到渠道对应的支付方法！");
		}
		// 查询支付宝订单
		PgwReqQueryOrder pgwReqQueryOrder = new PgwReqQueryOrder();
		pgwReqQueryOrder.setOrderId(reqRevoke.getOrderId());
		PgwRespQueryOrder pgwRespQueryOrder = payGateWayService.queryOrder(authInfoVo, pgwReqQueryOrder);
		if(RetCode.Success.RET_10000.equals(pgwRespQueryOrder.getRespCode()) ) {
			if( KstHosConstant.I0.equals(pgwRespQueryOrder.getOrderState())
					||KstHosConstant.I1.equals(pgwRespQueryOrder.getOrderState())
							||KstHosConstant.I9.equals(pgwRespQueryOrder.getOrderState())) {
				PgwRespRevoke pgwRespRevoke = payGateWayService.revoke(authInfoVo, reqRevoke.toPgwReqRevoke());
				// 撤销成功
				if (RetCode.Success.RET_10000.equals(pgwRespRevoke.getRespCode())) {
					return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
				} else {
					return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_REVOKED, pgwRespRevoke.getRespMsg());
				}
			}else if(KstHosConstant.I2.equals(pgwRespQueryOrder.getOrderState()) ){
				ReqRefund reqRefund = new ReqRefund(reqParam.getMsg(), reqRevoke.getOrderId(), StringUtil.getUUID(), 
						pgwRespQueryOrder.getPrice(), pgwRespQueryOrder.getPrice(), 
						"撤销订单",1,reqRevoke.getPayConfigKey(),reqRevoke.getPayTime());
				CommonResp<RespRefund> refundResp = this.refund(new CommonReq<ReqRefund>(reqRefund));
				return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, refundResp.getRetCode(), refundResp.getMessage());
			}
		}else if(RetCode.Pay.ERROR_MERCHANTORDER.equals(pgwRespQueryOrder.getRespCode())) {
			// 不存在支付
			return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_MERCHANTORDER);
		}else {
			return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, pgwRespQueryOrder.getRespCode());
		}
		return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM);
	}

	@Override
	public String Close(InterfaceMessage msg) throws Exception {
		return this.close(new CommonReq<ReqClose>(new ReqClose(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> close(CommonReq<ReqClose> commReq) throws Exception {
		ReqClose reqClose = commReq.getParam();
		AuthInfoVo authInfoVo = reqClose.getAuthInfo();
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(reqClose.getClientId(),reqClose.getConfigKey());
		String swiftpassMchType = KstHosConstant.STRING_EMPTY;
		if( ChannelTypeEnum.swiftpass.equals(channelTypeEnum)) {
			swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,reqClose.getConfigKey());
		}
		IPaymentGateWayService payGateWayService = HandlerBuilder.get().getPayGateWayInstance(channelTypeEnum.name()+swiftpassMchType);
		if( payGateWayService == null ) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM,"撤销失败，未找到渠道对应的支付方法！");
		}
		PgwReqQueryOrder pgwReqQueryOrder = new PgwReqQueryOrder();
		pgwReqQueryOrder.setOrderId(reqClose.getOrderId());
		PgwRespQueryOrder pgwRespQueryOrder = payGateWayService.queryOrder(authInfoVo, pgwReqQueryOrder);
		if(RetCode.Success.RET_10000.equals(pgwRespQueryOrder.getRespCode()) ) {
			if( KstHosConstant.I0.equals(pgwRespQueryOrder.getOrderState())) {
				PgwRespClose pgwRespClose = payGateWayService.close(authInfoVo, reqClose.toPgwReqClose());
				// 取消成功
				if (RetCode.Success.RET_10000.equals(pgwRespClose.getRespCode())) {
					return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
				} else {
					return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_REVOKED, pgwRespClose.getRespMsg());
				}
			}else {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Pay.ERROR_TRADESTATE,
						RetCode.Pay.ERROR_TRADESTATE.getMessage() + "|OrderState：" + pgwRespQueryOrder.getOrderState());
			}
		}else {
			// 不存在支付
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_MERCHANTORDER);
		}
	}

	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> queryMerchantOrder(CommonReq<ReqQueryMerchantOrder> commReq) throws Exception {
		ReqQueryMerchantOrder reqQueryMerchantOrder = commReq.getParam();
		AuthInfoVo authInfoVo = reqQueryMerchantOrder.getAuthInfo();
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(reqQueryMerchantOrder.getClientId(),reqQueryMerchantOrder.getConfigKey());	
		String swiftpassMchType = KstHosConstant.STRING_EMPTY;
		if( ChannelTypeEnum.swiftpass.equals(channelTypeEnum)) {
			swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,reqQueryMerchantOrder.getConfigKey());
		}
		IPaymentGateWayService payGateWayService = HandlerBuilder.get().getPayGateWayInstance(channelTypeEnum.name()+swiftpassMchType);
		if( payGateWayService == null ) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM,"查询失败，未找到渠道对应的支付方法！");
		}
		PgwRespQueryOrder pgwRespQueryOrder = payGateWayService.queryOrder(authInfoVo, reqQueryMerchantOrder.toPgwReqQueryOrder());
		if (RetCode.Success.RET_10000.equals(pgwRespQueryOrder.getRespCode())){
			RespMap respMap = new RespMap();
			//全流程订单号
			respMap.put(ApiKey.QueryMerchantOrder.OrderId, pgwRespQueryOrder.getOrderId());
			//商户订单号
			respMap.put(ApiKey.QueryMerchantOrder.TransactionNo, pgwRespQueryOrder.getTransactionNo());
			//订单时间
			respMap.put(ApiKey.QueryMerchantOrder.PayTime, pgwRespQueryOrder.getPayTime());
			//商户订单状态
			respMap.put(ApiKey.QueryMerchantOrder.OrderState, pgwRespQueryOrder.getOrderState());
			//商户支付订单金额(分)
			respMap.put(ApiKey.QueryMerchantOrder.Fee, pgwRespQueryOrder.getPrice());
			if( KstHosConstant.I1.equals(reqQueryMerchantOrder.getIsPaySuccessReNotfiy()) 
					&& pgwRespQueryOrder.getOrderState().equals(KstHosConstant.ORDERPAY_2)
						&& StringUtil.isNotEmpty(reqQueryMerchantOrder.getPayClientId())
							&& StringUtil.isNotEmpty(reqQueryMerchantOrder.getPayConfigKey())) {
				payBackCallUtil.addPayNotify(commReq.getMsg(),pgwRespQueryOrder.getOrderId(), pgwRespQueryOrder.getTransactionNo(),
						reqQueryMerchantOrder.getPayClientId(),pgwRespQueryOrder.getPrice(),reqQueryMerchantOrder.getPayConfigKey());
			}
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respMap);
		} else {
			// 不存在支付
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_MERCHANTORDER);
		}
	}
	
	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> queryMerchantRefund(CommonReq<ReqQueryMerchantRefund> commReq) throws Exception {
		ReqQueryMerchantRefund reqQueryMerchantRefund = commReq.getParam();
		AuthInfoVo authInfoVo = reqQueryMerchantRefund.getAuthInfo();
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(reqQueryMerchantRefund.getClientId(),reqQueryMerchantRefund.getConfigKey());	
		String swiftpassMchType = KstHosConstant.STRING_EMPTY;
		if( ChannelTypeEnum.swiftpass.equals(channelTypeEnum)) {
			swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,reqQueryMerchantRefund.getConfigKey());
		}
		IPaymentGateWayService payGateWayService = HandlerBuilder.get()
				.getPayGateWayInstance(channelTypeEnum.name() + swiftpassMchType);
		if (payGateWayService == null) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM,
					"查询失败，未找到渠道对应的支付方法！");
		}
		PgwRespQueryRefundOrder pgwRespQueryRefundOrder = payGateWayService.queryRefundOrder(authInfoVo,
				reqQueryMerchantRefund.toPgwReqQueryRefundOrder());
		if (RetCode.Success.RET_10000.equals(pgwRespQueryRefundOrder.getRespCode())) {
			RespMap respMap = new RespMap();
			// 订单总金额(分)
			respMap.put(ApiKey.QueryMerchantRefund.TotalFee, KstHosConstant.I0);
			// 订单退费金额(分)
			respMap.put(ApiKey.QueryMerchantRefund.RefundFee, pgwRespQueryRefundOrder.getRefundPrice());
			// 订单退费状态
			respMap.put(ApiKey.QueryMerchantRefund.RefundStatus, pgwRespQueryRefundOrder.getRefundStatus());
			// 成功退费时间，（支付宝接口没有返回该参数）
			respMap.put(ApiKey.QueryMerchantRefund.RefundTime, pgwRespQueryRefundOrder.getRefundTime());
			// 全流程退款订单号
			respMap.put(ApiKey.QueryMerchantRefund.RefundOrderId, pgwRespQueryRefundOrder.getRefundOrderId());
			// 商户订单退款订单号（支付宝该参数与RefundOrderId一致）
			respMap.put(ApiKey.QueryMerchantRefund.RefundNo, pgwRespQueryRefundOrder.getRefundId());
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
		} else {
			// 查询微信订单返回失败，或者不存在微信订单
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_MERCHANTORDER);
		}
	}

	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public String WapUniteOrder(InterfaceMessage msg) throws Exception {
		return this.wapUniteOrder(new CommonReq<ReqWapUniteOrder>(new ReqWapUniteOrder(msg))).getDataCaseRetCode().getPayInfo();
	}

	/**
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespWapUniteOrder> wapUniteOrder(CommonReq<ReqWapUniteOrder> commReq) throws Exception {
		ReqWapUniteOrder req = commReq.getParam();
		String orderId = req.getOrderId();
		String body = req.getBody();
		//加入订单校验逻辑 确认订单已经存在并且是需要线上支付的订单
		ReqPayStartBizOrderExecute t = new ReqPayStartBizOrderExecute(req.getMsg(), orderId, null, null);
		CommonReq<ReqPayStartBizOrderExecute> r = new CommonReq<ReqPayStartBizOrderExecute>(t);
		CommonResp<RespMap> resp = HandlerBuilder.get().getBizPayStartOrderCheckHandler().checkPayOrder(r);
		if(!resp.getCode().equals(KstHosConstant.SUCCESSCODE)) {
			//如果返回的不成功则直接返回前端失败并按照订单校验的失败原因返回出去
			return new CommonResp<RespWapUniteOrder>(commReq,resp.getRetCode(),resp.getMessage());
		} 
		//获取订单实际需要支付的金额
		Integer price = resp.getResultData().getInteger(ApiKey.checkPayOrder.price);
		//String priceName =  resp.getResultData().getString(ApiKey.checkPayOrder.priceName);
		String configKey = req.getConfigKey();
		req.setPrice(price);
		/***智付网关校验 -- start ***/
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(req.getClientId(),req.getConfigKey());
		KasiteConfig.payRule(req.getConfigKey(), price);
		PayRule rule = KasiteConfig.getPayRule(configKey);
		if(null != rule) {
			String c = rule.getCreditCardsAccepted();
			if(StringUtil.isNotBlank(rule.getCreditCardsAcceptedByClientId(req.getClientId()))) {
				c = rule.getCreditCardsAcceptedByClientId(req.getClientId());
			}
			if(StringUtil.isNotBlank(c) && "true".equalsIgnoreCase(c)) {
				req.setIsLimitCredit(0);
			}else if(StringUtil.isNotBlank(c) && "false".equalsIgnoreCase(c)){
				req.setIsLimitCredit(1);
			}
		}
		/***智付网关校验 -- end ***/
		
		String swiftpassMchType = KstHosConstant.STRING_EMPTY;
		if( ChannelTypeEnum.swiftpass.equals(channelTypeEnum)) {
			swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,req.getConfigKey());
		}
		IPaymentGateWayService payGateWayService = HandlerBuilder.get()
				.getPayGateWayInstance(channelTypeEnum.name() + swiftpassMchType);
		if (payGateWayService == null) {
			return new CommonResp<RespWapUniteOrder>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SYSTEM,
					"支付失败，未找到渠道对应的支付方法！");
		}
		PgwRespWapUniteOrder pgwRespWapUniteOrder = payGateWayService.wapUniteOrder(req.getAuthInfo(),req.toPgwReqWapUniteOrder());;
		if(RetCode.Success.RET_10000.equals(pgwRespWapUniteOrder.getRespCode())) {
			RespWapUniteOrder rx = new RespWapUniteOrder();
			rx.setOrderId(req.getOrderId());
			rx.setPayInfo(pgwRespWapUniteOrder.getPayInfo());
			return new CommonResp<RespWapUniteOrder>(commReq, pgwRespWapUniteOrder.getRespCode(),rx);
		}else {
			return new CommonResp<RespWapUniteOrder>(commReq, RetCode.Pay.ERROR_UNITEORDER,pgwRespWapUniteOrder.getRespMsg());
		}
	}
}
