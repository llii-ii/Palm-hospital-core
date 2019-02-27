package com.kasite.core.serviceinterface.module.order.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.serviceinterface.module.pay.resp.RespRefund;

/**
 * 交易日志的响应实体类
 * 
 * @author zhaoy
 *
 */
public class RespQueryLocalTransLogInfo extends AbsResp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private String transTime;
	
	/**
	 * 订单金额
	 */
	private String orderMoney;
	
	/**
	 * 支付金额
	 */
	private String payMoney;
	
	/**
	 * 支付方式
	 */
	private String payMethod;
	
	/**
	 * 订单支付时间
	 */
	private String payTime;
	
	/**
	 * 订单业务执行时间
	 */
	private String bizTime;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	private List<RespRefund> refundList = new ArrayList<>();

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

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
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

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getBizTime() {
		return bizTime;
	}

	public void setBizTime(String bizTime) {
		this.bizTime = bizTime;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public List<RespRefund> getRefundList() {
		return refundList;
	}

	public void setRefundList(List<RespRefund> refundList) {
		this.refundList = refundList;
	}
	
}
