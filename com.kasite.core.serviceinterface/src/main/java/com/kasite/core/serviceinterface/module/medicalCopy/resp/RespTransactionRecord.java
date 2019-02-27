package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import com.kasite.core.common.resp.AbsResp;

public class RespTransactionRecord extends AbsResp{

	private String id;//uuid
	private String orderType;//订单类型 1-支付订单
	private String orderId;//订单id/申请单号
	private String payChannelId;//渠道支付单号
	private String serviceContent;//服务内容
	private String actualReceipts;//实际收款
	private String shouldRefunds;//应退
	private String actualRefunds;//实退
	private String receiptsType;//收款类型 1-已收款
	private String payChannel;//渠道
	private String accountResult;//对账结果 1-长款 2-账平 3-短款
	
	private String wxOrderId;
	private String eoTime;//订单创建时间
	private Integer orderNum;//订单数
	private String totalMoney;//总额
	private String totalRefundMoney;//总退款
	private String totalInnerMoney;//净利润
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPayChannelId() {
		return payChannelId;
	}
	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}
	public String getServiceContent() {
		return serviceContent;
	}
	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}
	public String getActualReceipts() {
		return actualReceipts;
	}
	public void setActualReceipts(String actualReceipts) {
		this.actualReceipts = actualReceipts;
	}
	public String getShouldRefunds() {
		return shouldRefunds;
	}
	public void setShouldRefunds(String shouldRefunds) {
		this.shouldRefunds = shouldRefunds;
	}
	public String getActualRefunds() {
		return actualRefunds;
	}
	public void setActualRefunds(String actualRefunds) {
		this.actualRefunds = actualRefunds;
	}
	public String getReceiptsType() {
		return receiptsType;
	}
	public void setReceiptsType(String receiptsType) {
		this.receiptsType = receiptsType;
	}
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	public String getAccountResult() {
		return accountResult;
	}
	public void setAccountResult(String accountResult) {
		this.accountResult = accountResult;
	}
	public String getEoTime() {
		return eoTime;
	}
	public void setEoTime(String eoTime) {
		this.eoTime = eoTime;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getWxOrderId() {
		return wxOrderId;
	}
	public void setWxOrderId(String wxOrderId) {
		this.wxOrderId = wxOrderId;
	}
	public String getTotalRefundMoney() {
		return totalRefundMoney;
	}
	public void setTotalRefundMoney(String totalRefundMoney) {
		this.totalRefundMoney = totalRefundMoney;
	}
	public String getTotalInnerMoney() {
		return totalInnerMoney;
	}
	public void setTotalInnerMoney(String totalInnerMoney) {
		this.totalInnerMoney = totalInnerMoney;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	
}
