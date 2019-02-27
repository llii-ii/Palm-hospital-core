package com.kasite.core.serviceinterface.module.pay.req;


import java.net.InetAddress;
import java.net.UnknownHostException;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.util.wechat.ReqMicroPay;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 当面付请求入参对象
 * 
 * @author 無
 *
 */
public class ReqSweepCodePay extends AbsReq {
	/** 订单金额(以分为单位,整数)(必填) */
	private int totalFee;
	/** 订单标题(必填) */
	private String subject;
	/** 订单说明(必填) */
	private String orderMemo;
	/** 扫码支付授权码(必填) */
	private String authCode;
	/** 终端设备号(商户自定义) */
	private String deviceInfo;
	/**本地订单ID*/
	private String orderId;
	
	private String remoteIp;
	
	private Integer isLimitCredit;

	public ReqSweepCodePay(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.totalFee = XMLUtil.getInt(dataEl, "TotalFee", true);
		this.subject = XMLUtil.getString(dataEl, "Subject", true);
		this.orderMemo = XMLUtil.getString(dataEl, "OrderMemo", true);
		this.authCode = XMLUtil.getString(dataEl, "AuthCode", true);
		this.deviceInfo = XMLUtil.getString(dataEl, "DeviceInfo", false);
		/**本地订单ID*/
		this.orderId = XMLUtil.getString(dataEl, "OrderId", true);
		this.remoteIp = XMLUtil.getString(dataEl, "RemoteIp", false);
	}


	/**
	 * 转换成支付宝当面付请求入参
	 * @return ReqAlipayTradePay
	 * @throws AbsHosException 
	 */
	public ReqAlipayTradePay conversionToReqAlipayTradePay(InterfaceMessage msg) throws AbsHosException{
		
		String outTradeNo = this.orderId;
		String scene = "bar_code";
		String authCode = this.authCode;
		String productCode = null;
		String subject = this.subject;
		String buyerId = null;
		String sellerId = null;
		Integer totalAmount = this.totalFee;
		Integer discountableAmount = null;
		String body = this.orderMemo;
		String goodsDetail = null;
		String operatorId = null;
		String storeId = null;
		String terminalId = null;
		String extendParams = null;
		String timeoutExpress = null;
		
		ReqAlipayTradePay reqAlipayTradePay = new ReqAlipayTradePay(msg, outTradeNo, scene, authCode, 
				productCode, subject, buyerId, sellerId, totalAmount, discountableAmount, body, goodsDetail, 
				operatorId, storeId, terminalId, extendParams, timeoutExpress);
		
		reqAlipayTradePay.setOutTradeNo(this.orderId);
		reqAlipayTradePay.setScene("bar_code");
		reqAlipayTradePay.setAuthCode(this.authCode);
		reqAlipayTradePay.setSubject(this.subject);
		reqAlipayTradePay.setTotalAmount(this.totalFee);
		reqAlipayTradePay.setBody(this.orderMemo);
		reqAlipayTradePay.setTerminalId(this.deviceInfo);
		return reqAlipayTradePay;
	}
	
	/**
	 * 转换成微信提交刷卡支付请求入参
	 * @return ReqMicroPay
	 * @throws AbsHosException 
	 */
	public ReqMicroPay conversionToReqMicroPay(InterfaceMessage msg) throws AbsHosException{
		String body = this.orderMemo;
		String detail = null;
		String outTradeNo = this.orderId;
		String attach = null;
		String spbillCreateIp = "";
		String authCode = this.authCode;
		String goodsTag = null;
		String sceneInfo = null;
		String feeType = null;
		try {
			spbillCreateIp = (InetAddress.getLocalHost().getHostAddress()+"");
		} catch (UnknownHostException e) {
			spbillCreateIp = ("127.0.0.1");
			e.printStackTrace();
		}
		ReqMicroPay reqMicroPay =new ReqMicroPay(body, detail, attach, outTradeNo, totalFee, feeType, spbillCreateIp, goodsTag, authCode, sceneInfo);
//		reqMicroPay.setBody(this.orderMemo);
//		reqMicroPay.setOutTradeNo(this.orderId);
//		reqMicroPay.setTotalFee(this.totalFee);
//		try {
//			reqMicroPay.setSpbillCreateIp(InetAddress.getLocalHost().getHostAddress()+"");
//		} catch (UnknownHostException e) {
//			reqMicroPay.setSpbillCreateIp("127.0.0.1");
//			e.printStackTrace();
//		}
//		reqMicroPay.setAuthCode(this.authCode);
		return reqMicroPay;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public Integer getIsLimitCredit() {
		return isLimitCredit;
	}


	public void setIsLimitCredit(Integer isLimitCredit) {
		this.isLimitCredit = isLimitCredit;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getOrderMemo() {
		return orderMemo;
	}

	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	/**
	 * @param msg
	 * @param totalFee
	 * @param subject
	 * @param orderMemo
	 * @param authCode
	 * @param deviceInfo
	 * @param orderId
	 * @throws AbsHosException
	 */
	public ReqSweepCodePay(InterfaceMessage msg, int totalFee, String subject, String orderMemo, String authCode,
			String deviceInfo, String orderId) throws AbsHosException {
		super(msg);
		this.totalFee = totalFee;
		this.subject = subject;
		this.orderMemo = orderMemo;
		this.authCode = authCode;
		this.deviceInfo = deviceInfo;
		this.orderId = orderId;
	}
	
	public PgwReqSweepCodePay toPgwReqSweepCodePay() {
		PgwReqSweepCodePay pgwReqSweepCodePay = new PgwReqSweepCodePay();
		pgwReqSweepCodePay.setAttach(this.orderMemo);
		pgwReqSweepCodePay.setAuthCode(this.authCode);
		pgwReqSweepCodePay.setBody(this.orderMemo);
		pgwReqSweepCodePay.setOrderId(this.orderId);
		pgwReqSweepCodePay.setSubject(this.subject);
		if(StringUtil.isEmpty(this.remoteIp)) {
			try {
				this.remoteIp = (InetAddress.getLocalHost().getHostAddress()+"");
			} catch (UnknownHostException e) {
				this.remoteIp = ("127.0.0.1");
				e.printStackTrace();
			}
		}
		pgwReqSweepCodePay.setRemoteIp(this.remoteIp);
		pgwReqSweepCodePay.setTotalPrice(this.totalFee);
		pgwReqSweepCodePay.setDeviceInfo(this.deviceInfo);
		pgwReqSweepCodePay.setIsLimitCredit(this.isLimitCredit);
		return pgwReqSweepCodePay;
	}
}
