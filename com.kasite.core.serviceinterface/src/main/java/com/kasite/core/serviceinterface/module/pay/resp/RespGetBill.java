package com.kasite.core.serviceinterface.module.pay.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespGetBill
 * @author: lcz
 * @date: 2018年7月26日 下午7:51:00
 */
public class RespGetBill extends AbsResp{
	
	private String merchOrderNo;
	private String orderId;
	private String channelId;
	private Integer orderType;
	private String transDate;
	private Integer transactions;
	private String refundOrderId;
	private Integer refundPrice;
	public String getMerchOrderNo() {
		return merchOrderNo;
	}
	public void setMerchOrderNo(String merchOrderNo) {
		this.merchOrderNo = merchOrderNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public Integer getTransactions() {
		return transactions;
	}
	public void setTransactions(Integer transactions) {
		this.transactions = transactions;
	}
	public String getRefundOrderId() {
		return refundOrderId;
	}
	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}
	public Integer getRefundPrice() {
		return refundPrice;
	}
	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}
	
	
	
}
