package com.kasite.core.serviceinterface.module.order.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

public class RespPayOrderDetail extends AbsResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 支付订单ID
	 */
	private String orderId;
	
	/**
	 * 订单金额
	 */
	private Integer orderMoney;
	
	/**
	 * 支付金额
	 */
	private Integer payMoney;
	
	/**
	 * 交易场景ID
	 */
	private String channelId;
	
	/**
	 * 交易场景
	 */
	private String channelName;
	
	/**
	 * 支付方式
	 */
	private String payMethod;
	
	/**
	 * 支付方式
	 */
	private String payMethodName;
	
	/**
	 * 支付状态
	 */
	private String payState;
	
	/**
	 * 交易时间
	 */
	private String orderDate;
	
	/**
	 * 支付时间
	 */
	private String payDate;
	
	/**
	 * 交易完成时间
	 */
	private String bizDate;
	
	/**
	 * 退款状态
	 */
	private Integer refundState;
	
	/**
	 * 渠道流水号
	 */
	private String channelSerialNo;
	
	/**
	 * 医院的流水号
	 */
	private String hisSerialNo;
	
	/**
	 * 就诊卡号/住院号
	 */
	private String cardNo;
	
	/**
	 * 患者姓名
	 */
	private String nickName;
	
	/**
	 * 患者手机号
	 */
	private String nickMobile;
	
	/**
	 * 订单类型(订单表中的订单标签，备注ORDERMEMO这个字段)
	 */
	private String orderType;
	
	/**
	 * 服务ID 006:住院号 007:就诊卡
	 */
	private String serviceId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(Integer orderMoney) {
		this.orderMoney = orderMoney;
	}

	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
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

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayMethodName() {
		return payMethodName;
	}

	public void setPayMethodName(String payMethodName) {
		this.payMethodName = payMethodName;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}

	public Integer getRefundState() {
		return refundState;
	}

	public void setRefundState(Integer refundState) {
		this.refundState = refundState;
	}

	public String getChannelSerialNo() {
		return channelSerialNo;
	}

	public void setChannelSerialNo(String channelSerialNo) {
		this.channelSerialNo = channelSerialNo;
	}

	public String getHisSerialNo() {
		return hisSerialNo;
	}

	public void setHisSerialNo(String hisSerialNo) {
		this.hisSerialNo = hisSerialNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickMobile() {
		return nickMobile;
	}

	public void setNickMobile(String nickMobile) {
		this.nickMobile = nickMobile;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
}
