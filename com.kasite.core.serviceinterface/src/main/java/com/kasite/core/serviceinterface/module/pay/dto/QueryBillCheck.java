package com.kasite.core.serviceinterface.module.pay.dto;

import java.util.List;

public class QueryBillCheck {
	/**
	 * 核对状态0账平1长款-1短款
	 */
	private String checkState;
	
	/**
	 * HIS支付/退费订单号
	 */
	private String hisOrderNo;
	
	/**
	 * 支付方式:微信,wechat;支付宝,zfb;银联：yl等
	 */
	private String payMethod;
	
	/**
	 * 商户支付/退费订单号
	 */
	private String merchNo;
	
	/**
	 * 全流程订单号
	 */
	private String orderId;
	
	/**
	 * 交易渠道
	 */
	private String channelId;
	
	/**
	 * 全流程支付/退费订单号
	 */
	private String orderNo;
	
	/**
	 * 商户交易日期
	 */
	private String transDate;
	
	/**
	 * 商户号
	 */
	private String configKey;
	
	/**
	 * 账单类型1支付，2退费
	 */
	private int billType;
	
	/**
	 * 查询对账开始时间
	 */
	private String startDate;
	
	/**
	 * 查询对账结束时间
	 */
	private String endDate;

	/**
	 * 处理方式，1退款，2冲正，3登帐
	 */
	private Integer dealWay;
	
	/**
	 * 处理状态，0未处置 1已处置
	 */
	private Integer dealState;
	
	/**排序规则**/
	private Integer orderRule;
	
	/**
	 * 服务号
	 */
	private List<String> serviceIdList;

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getHisOrderNo() {
		return hisOrderNo;
	}

	public void setHisOrderNo(String hisOrderNo) {
		this.hisOrderNo = hisOrderNo;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public int getBillType() {
		return billType;
	}

	public void setBillType(int billType) {
		this.billType = billType;
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

	public Integer getDealWay() {
		return dealWay;
	}

	public void setDealWay(Integer dealWay) {
		this.dealWay = dealWay;
	}

	public Integer getDealState() {
		return dealState;
	}

	public void setDealState(Integer dealState) {
		this.dealState = dealState;
	}

	public Integer getOrderRule() {
		return orderRule;
	}

	public void setOrderRule(Integer orderRule) {
		this.orderRule = orderRule;
	}

	public List<String> getServiceIdList() {
		return serviceIdList;
	}

	public void setServiceIdList(List<String> serviceIdList) {
		this.serviceIdList = serviceIdList;
	}
	
}
