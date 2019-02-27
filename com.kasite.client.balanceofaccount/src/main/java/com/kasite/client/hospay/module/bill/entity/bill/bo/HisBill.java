package com.kasite.client.hospay.module.bill.entity.bill.bo;

import java.sql.Timestamp;

/**
 * HIS账单对象
 * @author cc
 *
 */
public class HisBill {

	/**全流程订单ID*/
	private String orderId;

	/**全流程退款订单ID*/
	private String refundOrderId;

	/**微信订单号*/
	private String merchOrderNo;

	/**HIS订单号*/
	private String hisOrderId;

	/**支付金额*/
	private String payMoney;

	/**全部金额*/
	private String totalMoney;

	/**退费金额*/
	private String refundMoney;

	/**金额名称*/
	private String priceName;

	/**订单说明*/
	private String orderMemo;

	/**HIS订单类型1支付2退费*/
	private String hisOrderType;

	/**HIS订单业务状态0未消费1已消费-1未知*/
	private String hisBizState;

	/**是否允许退费*/
	private Integer isAllowRefund;

	/**新增时间*/
	private Timestamp createDate;

	/**订单交易时间*/
	private Timestamp transDate;

	/**渠道ID*/
	private String channelId;

	/** 渠道名称*/
	private String channelName;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Timestamp getTransDate() {
		return transDate;
	}

	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
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

	public String getMerchOrderNo() {
		return merchOrderNo;
	}

	public void setMerchOrderNo(String merchOrderNo) {
		this.merchOrderNo = merchOrderNo;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
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

	public String getHisOrderType() {
		return hisOrderType;
	}

	public void setHisOrderType(String hisOrderType) {
		this.hisOrderType = hisOrderType;
	}

	public String getHisBizState() {
		return hisBizState;
	}

	public void setHisBizState(String hisBizState) {
		this.hisBizState = hisBizState;
	}

	public Integer getIsAllowRefund() {
		return isAllowRefund;
	}

	public void setIsAllowRefund(Integer isAllowRefund) {
		this.isAllowRefund = isAllowRefund;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}


}
