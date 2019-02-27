package com.kasite.core.serviceinterface.module.pay.resp;

import com.kasite.core.common.resp.ApiKey;
import com.yihu.hos.IRetCode;

/**
 * @author linjf
 * 支付网关-统一下单出参
 * 某些渠道的特殊出参字段，请注释清楚
 */
public class PgwRespQueryOrder {

	private IRetCode respCode;
	
	private String respMsg;
	
	/**
	 * 全流程订单号
	 */
	private String orderId;
	
	/**
	 * 商户订单号
	 */
	private String transactionNo;
	
	/**
	 * 支付时间
	 * 微信yyyyMMddHHmmss
	 */
	private String payTime;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 支付金额
	 */
	private Integer price;
	
	public IRetCode getRespCode() {
		return respCode;
	}

	public void setRespCode(IRetCode respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

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

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	
	
	
	
}
