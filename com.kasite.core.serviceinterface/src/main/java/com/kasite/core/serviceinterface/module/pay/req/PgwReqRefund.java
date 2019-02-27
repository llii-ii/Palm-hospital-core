package com.kasite.core.serviceinterface.module.pay.req;

/**
 * @author linjf
 * 支付网关-统一下单出入参
 * 某些渠道的特殊入参字段，请注释清楚
 */
public class PgwReqRefund {
	
	/*************公共参数*****************/
	/**
	 *  商户订单号
	 */
	private String orderId;
	
	/**
	 *退款订单号
	 */
	private String refundOrderId;
	
	/**
	 * 订单总金额
	 */
	private Integer totalPrice;
	
	/**
	 * 退款总金额
	 */
	private Integer refundPrice;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 商户订单号
	 */
	private String transactionNo;
	
	private String payConfigKey;

	
	/*************支付宝入参*****************/
	
	/*************微信参入参*
	/*************威富通-支付宝参数*****************/
	
	/*************威富通-微信参数*****************/

	/*************银联入参*****************/
	/**
	 * 支付订单的支付时间	YYYYMMDDhhmmss	
	 */
	private String payTime;




	public String getOrderId() {
		return orderId;
	}




	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}




	public String getRefundOrderId() {
		return refundOrderId;
	}




	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}




	public Integer getTotalPrice() {
		return totalPrice;
	}




	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}




	public Integer getRefundPrice() {
		return refundPrice;
	}




	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}




	public String getRemark() {
		return remark;
	}




	public void setRemark(String remark) {
		this.remark = remark;
	}




	public String getTransactionNo() {
		return transactionNo;
	}




	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}




	public String getPayTime() {
		return payTime;
	}




	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}




	public String getPayConfigKey() {
		return payConfigKey;
	}




	public void setPayConfigKey(String payConfigKey) {
		this.payConfigKey = payConfigKey;
	}
	
	
	
}
