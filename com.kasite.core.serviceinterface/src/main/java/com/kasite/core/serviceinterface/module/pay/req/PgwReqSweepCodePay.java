package com.kasite.core.serviceinterface.module.pay.req;

/**
 * @author linjf
 * 支付网关-统一下单出入参
 * 某些渠道的特殊入参字段，请注释清楚
 */
public class PgwReqSweepCodePay {
	
	/*************公共参数*****************/
	private String authCode;

	private String orderId;
	
	private String body;
	
	private Integer totalPrice;
	
	private String deviceInfo;
	/**
	 * 前端发起统一下单的远程ip
	 */
	private String remoteIp;
	
	private String subject;
	
	private Integer isLimitCredit;
	/*************支付宝-入参*****************/
	
	/*************微信-参入参****************/
	private String attach;
	/*************威富通-支付宝参数*****************/
	
	/*************威富通-微信参数*****************/

	/*************银联-入参*****************/

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getIsLimitCredit() {
		return isLimitCredit;
	}

	public void setIsLimitCredit(Integer isLimitCredit) {
		this.isLimitCredit = isLimitCredit;
	}
	
	
	
	
}
