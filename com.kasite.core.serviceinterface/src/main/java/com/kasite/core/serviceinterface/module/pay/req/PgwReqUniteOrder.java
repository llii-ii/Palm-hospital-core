package com.kasite.core.serviceinterface.module.pay.req;

/**
 * @author linjf
 * 支付网关-统一下单出入参
 * 某些渠道的特殊入参字段，请注释清楚
 */
public class PgwReqUniteOrder {
	
	/*************公共参数*****************/
	/**
	 * 全流程订单ID
	 */
	private String orderId;
	
	/**
	 * 实际支付金额
	 */
	private Integer price;
	
	/**
	 * 订单描述
	 */
	private String body;
	
	/**
	 * 订单标题
	 */
	private String subject;
	
	/**
	 * 下单用户的openId
	 */
	private String openId;
	
	/**
	 * 前端发起统一下单的远程ip
	 */
	private String remoteIp;
	
	/**
	 * 是否信用卡
	 */
	private Integer isLimitCredit;

	
	/*************支付宝入参*****************/
	
	/*************微信参入参*****************/
	/**
	 * 微信自定义参数
	 */
	private String attach;
	/*************威富通-支付宝参数*****************/
	
	/*************威富通-微信参数*****************/

	/*************银联入参*****************/
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public Integer getIsLimitCredit() {
		return isLimitCredit;
	}

	public void setIsLimitCredit(Integer isLimitCredit) {
		this.isLimitCredit = isLimitCredit;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	
}
