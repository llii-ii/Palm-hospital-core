package com.kasite.core.serviceinterface.module.pay.dto;

import java.sql.Timestamp;

public class BillDetailVo {

	/**
	 * 主键ID
	 */
	private String id;
	
	/**
	 * 全流程支付/退费订单号
	 */
	private String orderNo;
	
	/**
	 * 全流程ID
	 */
	private String orderId;
	
	/**
	 * 对账时间
	 */
	private Timestamp billDate;
	
	/**
	 * 核对状态0账平1长款-1短款2处理
	 */
	private int checkState;
	
	/**
	 * 是否处理
	 */
	private int dealState;
	
	/**
	 * 处理方式
	 */
	private Integer dealWay;
	
	/**
	 * 处理人
	 */
	private String dealBy;
	
	/**
	 * 处理时间
	 */
	private Timestamp dealDate;
	
	/**
	 * 处理结果备注
	 */
	private String dealRemark;
	
	/**
	 * 交易场景ID
	 */
	private String channelId;
	
	/**
	 * 交易场景
	 */
	private String channelName;
	
	/**
	 * 商户代码
	 */
	private String merchCode;
	
	/**
	 * 支付方式
	 */
	private String payMethod;
	
	private String payMethodName;
	
	/**
	 * HIS支付/退费订单号
	 */
	private String hisOrderNo;
	
	/**
	 * his应收/应退金额（分）
	 */
	private Integer hisPrice;
	
	/**
	 * 商户支付/退费订单号
	 */
	private String merchNo;
	
	/**
	 * 商户实收/实退金额（分）
	 */
	private Integer merchPrice;
	
	/**
	 * 服务号
	 */
	private String serviceId;
	/**
	 * 业务执行时间
	 */
	private Timestamp hisbizDate;
	
	/**
	 * 支付完成时间
	 */
	private Timestamp payDate;
	
	/**
	 * 账单类型1支付，2退费
	 */
	private int billType;
	
	/**
	 * 更新时间
	 */
	private Timestamp updateDate;
	
	/**
	 * 差错金额
	 */
	private Integer diffPrice;
	
	/**
	 * 差错原因
	 */
	private String diffReason;
	
	/**
	 * 差错描述
	 */
	private String diffDesc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Timestamp getBillDate() {
		return billDate;
	}

	public void setBillDate(Timestamp billDate) {
		this.billDate = billDate;
	}

	public int getCheckState() {
		return checkState;
	}

	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}

	public int getDealState() {
		return dealState;
	}

	public void setDealState(int dealState) {
		this.dealState = dealState;
	}

	public Integer getDealWay() {
		return dealWay;
	}

	public void setDealWay(Integer dealWay) {
		this.dealWay = dealWay;
	}

	public String getDealBy() {
		return dealBy;
	}

	public void setDealBy(String dealBy) {
		this.dealBy = dealBy;
	}

	public Timestamp getDealDate() {
		return dealDate;
	}

	public void setDealDate(Timestamp dealDate) {
		this.dealDate = dealDate;
	}

	public String getDealRemark() {
		return dealRemark;
	}

	public void setDealRemark(String dealRemark) {
		this.dealRemark = dealRemark;
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

	public String getMerchCode() {
		return merchCode;
	}

	public void setMerchCode(String merchCode) {
		this.merchCode = merchCode;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getHisOrderNo() {
		return hisOrderNo;
	}

	public void setHisOrderNo(String hisOrderNo) {
		this.hisOrderNo = hisOrderNo;
	}

	public Integer getHisPrice() {
		return hisPrice;
	}

	public void setHisPrice(Integer hisPrice) {
		this.hisPrice = hisPrice;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public Integer getMerchPrice() {
		return merchPrice;
	}

	public void setMerchPrice(Integer merchPrice) {
		this.merchPrice = merchPrice;
	}

	public Timestamp getHisbizDate() {
		return hisbizDate;
	}

	public void setHisbizDate(Timestamp hisbizDate) {
		this.hisbizDate = hisbizDate;
	}

	public Timestamp getPayDate() {
		return payDate;
	}

	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}

	public int getBillType() {
		return billType;
	}

	public void setBillType(int billType) {
		this.billType = billType;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getDiffPrice() {
		return diffPrice;
	}

	public void setDiffPrice(Integer diffPrice) {
		this.diffPrice = diffPrice;
	}

	public String getDiffReason() {
		return diffReason;
	}

	public void setDiffReason(String diffReason) {
		this.diffReason = diffReason;
	}

	public String getDiffDesc() {
		return diffDesc;
	}

	public void setDiffDesc(String diffDesc) {
		this.diffDesc = diffDesc;
	}
	
}
