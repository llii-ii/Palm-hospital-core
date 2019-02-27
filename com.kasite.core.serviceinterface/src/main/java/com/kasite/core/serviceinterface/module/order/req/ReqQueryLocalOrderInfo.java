package com.kasite.core.serviceinterface.module.order.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqQueryLocalOrderInfo extends AbsReq{

	public ReqQueryLocalOrderInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.orderId = getDataJs().getString("OrderId");
			this.refundOrderId = getDataJs().getString("RefundOrderId");
			this.hisOrderId = getDataJs().getString("HisOrderId");
			this.startDate = getDataJs().getString("StartDate");
			this.endDate = getDataJs().getString("EndDate");
			this.payType = getDataJs().getString("PayType");
			this.channelSerialNo = getDataJs().getString("Channel_Serial_No");
			this.hisSerialNo = getDataJs().getString("His_Serial_No");
			this.cardNo = getDataJs().getString("Card_No");
			this.nickName = getDataJs().getString("Nick_Name");
			this.nickMobile = getDataJs().getString("Nick_Mobile");
			this.channelId = getDataJs().getString("ChannelId");
			this.orderState = getDataJs().getInteger("OrderState");
			this.transType = getDataJs().getString("TransType");
			this.configKey = getDataJs().getString("ConfigKey");
		}
	}
	
	public ReqQueryLocalOrderInfo(InterfaceMessage msg, String orderId, String transationNo, String refundOrderId) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.refundOrderId = refundOrderId;
		this.transationNo = transationNo;
	}
	
	private String orderId;
	
	private String refundOrderId;
	
	private String transationNo;
	
	private String hisOrderId;

	/**
	 * 查询的开始日期
	 */
	private String startDate;
	
	/**
	 * 查询的结束日期
	 */
	private String endDate;
	
	/**
	 * 支付类型 WX;ZFB;YL
	 */
	private String payType;
	
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
	 * 交易场景
	 */
	private String channelId;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 服务类型
	 */
	private String transType;
	
	/**
	 * 商户号
	 */
	private String configKey;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransationNo() {
		return transationNo;
	}

	public void setTransationNo(String transationNo) {
		this.transationNo = transationNo;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
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

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
}
