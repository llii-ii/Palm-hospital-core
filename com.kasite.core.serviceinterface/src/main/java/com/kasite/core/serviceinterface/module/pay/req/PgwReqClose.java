package com.kasite.core.serviceinterface.module.pay.req;

/**
 * @author linjf
 * 支付网关-统一下单出入参
 * 某些渠道的特殊入参字段，请注释清楚
 */
public class PgwReqClose {
	
	/*************公共参数*****************/
	/**
	 * 全流程订单ID
	 */
	private String orderId;

	private String transactionNo;
	
	/*************支付宝入参*****************/
	
	/*************微信参入参*
	/*************威富通-支付宝参数*****************/
	
	/*************威富通-微信参数*****************/

	/*************银联入参*****************/
	
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	
	
}
