package com.kasite.core.serviceinterface.module.his.resp;

import java.sql.Timestamp;

import com.kasite.core.common.resp.AbsResp;

public class RespQueryHisBill extends AbsResp{

	/**
	 * 全流程订单ID
	 */
	private String orderId;
	
	/**
	 * 全流程退款订单ID
	 */
	private String refundOrderId;
	
	/**
	 * 商户支付订单ID
	 */
	private String merchOrderNo;
	
	/**
	 * HIS订单号
	 */
	private String hisOrderId;
	
	/**
	 * 支付金额/退费金额
	 */
	private String payMoney;
	
	/**
	 * 全部金额
	 */
	private String totalMoney;
	
	/**
	 * 退款金额
	 */
	private String refundMoney;
	
	/**
	 * 价格名称
	 */
	private String priceName;
	
	/**
	 * 订单说明
	 */
	private String orderMemo;
	
	/**
	 * HIS订单类型1支付2退费
	 */
	private Integer hisOrderType;
	
	/**
	 * HIS订单业务状态 0未消费 1已消费 -1未知
	 */
	private Integer hisBizState;
	
//	/**
//	 * 是否允许退费
//	 */
//	private Integer isAllowRefund;
	
	/**
	 * 插入时间
	 */
	private Timestamp createDate;
	
	/**
	 * 订单时间
	 */
	private Timestamp transDate;
	
	/**
	 * 支付渠道ID
	 */
	private String channelId;
	
	private String channelName;
	
	/**
	 * 住院-住院号;门诊-病历号
	 */
	private String caseHistory;

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

	public Integer getHisOrderType() {
		return hisOrderType;
	}

	public void setHisOrderType(Integer hisOrderType) {
		this.hisOrderType = hisOrderType;
	}

	public Integer getHisBizState() {
		return hisBizState;
	}

	public void setHisBizState(Integer hisBizState) {
		this.hisBizState = hisBizState;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCaseHistory() {
		return caseHistory;
	}

	public void setCaseHistory(String caseHistory) {
		this.caseHistory = caseHistory;
	}
	
}
