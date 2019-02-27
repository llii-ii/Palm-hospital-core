package com.kasite.core.serviceinterface.module.order.dto;

import java.sql.Timestamp;

/**
 * 交易管理-退款订单详情
 * @author zhaoy
 *
 */
public class RefundOrderDetailVo {

	/**
	 * 交易时间
	 */
	private Timestamp orderDate;
	
	/**
	 * 支付时间
	 */
	private Timestamp payDate;
	
	/**
	 * 交易完成时间
	 */
	private Timestamp bizDate;
	
	/**
	 * 发起退款时间
	 */
	private Timestamp refundBeginDate;
	
	/**
	 * 退款完成时间
	 */
	private Timestamp refundEndDate;
	
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
	 * 支付的时候对应的使用的支付渠道配置信息
	 */
	private String configkey;
	
	/**
	 * 交易渠道
	 */
	private String channelId;
	
	/**
	 * 退款渠道
	 */
	private String refundChannelId;
	
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

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public Timestamp getPayDate() {
		return payDate;
	}

	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}

	public Timestamp getBizDate() {
		return bizDate;
	}

	public void setBizDate(Timestamp bizDate) {
		this.bizDate = bizDate;
	}

	public Timestamp getRefundBeginDate() {
		return refundBeginDate;
	}

	public void setRefundBeginDate(Timestamp refundBeginDate) {
		this.refundBeginDate = refundBeginDate;
	}

	public Timestamp getRefundEndDate() {
		return refundEndDate;
	}

	public void setRefundEndDate(Timestamp refundEndDate) {
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

	public String getConfigkey() {
		return configkey;
	}

	public void setConfigkey(String configkey) {
		this.configkey = configkey;
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
