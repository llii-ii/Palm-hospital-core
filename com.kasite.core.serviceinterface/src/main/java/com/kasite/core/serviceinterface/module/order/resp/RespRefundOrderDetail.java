package com.kasite.core.serviceinterface.module.order.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

public class RespRefundOrderDetail extends AbsResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	 * 发起退款时间
	 */
	private String refundBeginDate;
	
	/**
	 * 退款完成时间
	 */
	private String refundEndDate;
	
	/**
	 * 退款状态 3.退款中  4.已退款
	 */
	private Integer refundState;
	
	/**
	 * 智付订单号
	 */
	private String orderId;
	
	/**
	 * 渠道流水号
	 */
	private String channelSerialNo;
	
	/**
	 * 医院流水号
	 */
	private String hisSerialNo;
	
	/**
	 * 退款发起人
	 */
	private String operatorName;
	
	/**
	 * 退款流水号
	 */
	private String refundNo;
	
	/**
	 * 医院流水号(退款)
	 */
	private String hisRefundSerialNo;
	
	/**
	 * 支付的渠道ID
	 */
	private String channelId;
	
	/**
	 * 支付的渠道
	 */
	private String channelName;
	
	/**
	 * 支付方式Code
	 */
	private String payMethod;
	
	/**
	 * 支付方式名称
	 */
	private String payMethodName;
	
	/**
	 * 订单类型
	 */
	private String orderType;
	
	/**
	 * 订单金额
	 */
	private Integer orderMoney;
	
	/**
	 * 实付金额
	 */
	private Integer payMoney;
	
	/**
	 * 退款订单ID
	 */
	private String refundOrderId;
	
	/**
	 * 退款渠道
	 */
	private String refundChannelId;
	
	/**
	 * 退款渠道
	 */
	private String refundChannelName;
	
	/**
	 * 退款金额
	 */
	private Integer refundPrice;
	
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
	 * 服务ID 006:住院号 007:就诊卡
	 */
	private String serviceId;
	
	/**
	 * 退费渠道ID
	 */
	private String clientId;

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

	public String getRefundBeginDate() {
		return refundBeginDate;
	}

	public void setRefundBeginDate(String refundBeginDate) {
		this.refundBeginDate = refundBeginDate;
	}

	public String getRefundEndDate() {
		return refundEndDate;
	}

	public void setRefundEndDate(String refundEndDate) {
		this.refundEndDate = refundEndDate;
	}

	public Integer getRefundState() {
		return refundState;
	}

	public void setRefundState(Integer refundState) {
		this.refundState = refundState;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getHisRefundSerialNo() {
		return hisRefundSerialNo;
	}

	public void setHisRefundSerialNo(String hisRefundSerialNo) {
		this.hisRefundSerialNo = hisRefundSerialNo;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getRefundChannelId() {
		return refundChannelId;
	}

	public void setRefundChannelId(String refundChannelId) {
		this.refundChannelId = refundChannelId;
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
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

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getRefundChannelName() {
		return refundChannelName;
	}

	public void setRefundChannelName(String refundChannelName) {
		this.refundChannelName = refundChannelName;
	}

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}
