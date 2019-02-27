package com.kasite.core.serviceinterface.module.order.dto;

import java.sql.Timestamp;

public class OrderTransLogVo {

	
	/**
	 * 支付订单号
	 */
	private String orderId;
	
	/**
	 * 交易场景ID
	 */
	private String channelId;
	
	/**
	 * 交易场景
	 */
	private String channelName;
	
	/**
	 * 订单交易时间
	 */
	private Timestamp transTime;
	
	/**
	 * 订单金额
	 */
	private String orderMoney;
	
	/**
	 * 支付金额
	 */
	private String payMoney;
	
	/**
	 * 支付配置
	 */
	private String configKey;
	
	/**
	 * 订单支付时间
	 */
	private Timestamp payTime;
	
	/**
	 * 订单业务执行时间
	 */
	private Timestamp bizTime;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;

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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Timestamp getTransTime() {
		return transTime;
	}

	public void setTransTime(Timestamp transTime) {
		this.transTime = transTime;
	}

	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public Timestamp getPayTime() {
		return payTime;
	}

	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}

	public Timestamp getBizTime() {
		return bizTime;
	}

	public void setBizTime(Timestamp bizTime) {
		this.bizTime = bizTime;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}
	
}
