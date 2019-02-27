package com.kasite.core.serviceinterface.module.order.dbo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="O_ORDER_CHECK")
public class OrderCheck implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private String id;
	
	private String orderId;
	
	private String refundOrderId;
	
	private String serviceId;
	
	private Integer totalPrice;
	
	private Integer price;
	
	private Integer payPrice;
	
	private String channelId;
	
	private String channelName;
	
	private String hosId;
	
	private String cardNo;
	
	private String cardtype;
	
	private String prescno;
	
	private String transactionNo;
	
	private String configKey;
	
	private Integer orderState;
	
	private Integer orderType;
	
	private String priceName;
	
	private String orderMemo;
	
	private Timestamp orderDatetime;
	
	private Timestamp payDatetime; 
	
	private Timestamp bizDatetime;
	
	private Integer refundPrice;
	
	private String refundNo;
	
	private Timestamp refundDatetime;
	
	private String refundChannelId;
	
	private String refundChannelName;
	
	private String outRefundOrderId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Integer payPrice) {
		this.payPrice = payPrice;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getPrescno() {
		return prescno;
	}

	public void setPrescno(String prescno) {
		this.prescno = prescno;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getOrderMemo() {
		return orderMemo;
	}

	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
	}

	public Timestamp getOrderDatetime() {
		return orderDatetime;
	}

	public void setOrderDatetime(Timestamp orderDatetime) {
		this.orderDatetime = orderDatetime;
	}

	public Timestamp getPayDatetime() {
		return payDatetime;
	}

	public void setPayDatetime(Timestamp payDatetime) {
		this.payDatetime = payDatetime;
	}

	public Timestamp getBizDatetime() {
		return bizDatetime;
	}

	public void setBizDatetime(Timestamp bizDatetime) {
		this.bizDatetime = bizDatetime;
	}

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public Timestamp getRefundDatetime() {
		return refundDatetime;
	}

	public void setRefundDatetime(Timestamp refundDatetime) {
		this.refundDatetime = refundDatetime;
	}

	public String getRefundChannelId() {
		return refundChannelId;
	}

	public void setRefundChannelId(String refundChannelId) {
		this.refundChannelId = refundChannelId;
	}

	public String getRefundChannelName() {
		return refundChannelName;
	}

	public void setRefundChannelName(String refundChannelName) {
		this.refundChannelName = refundChannelName;
	}

	public String getOutRefundOrderId() {
		return outRefundOrderId;
	}

	public void setOutRefundOrderId(String outRefundOrderId) {
		this.outRefundOrderId = outRefundOrderId;
	}
}
