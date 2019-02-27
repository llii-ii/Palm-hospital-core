package com.kasite.core.serviceinterface.module.pay.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

public class RespQueryBillForDate extends AbsResp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private String id;
	
	/**
	 * HIS支付/退费订单号
	 */
	private String hisOrderNo;
	
	/**
	 * 全流程支付/退费订单号
	 */
	private String orderNo;
	
	/**
	 * 商户支付/退费订单号
	 */
	private String merchNo;
	
	/**
	 * 商户交易日期
	 */
	private String transDate;
	
	/**
	 * 支付方式:微信,wechat;支付宝,zfb;银联：yl等
	 */
	private String payMethod;
	
	private String payMethodName;
	
	/**
	 * 交易渠道
	 */
	private String channelId;
	
	private String channelName;
	
	/**
	 * 账单类型1支付，2退费
	 */
	private int billType;
	
	/**
	 * his应收/应退金额（分）
	 */
	private Integer hisPrice;
	
	/**
	 * 商户实收/实退金额（分）
	 */
	private Integer merchPrice;
	
	/**
	 * 核对状态0账平1长款-1短款
	 */
	private int checkState;
	
	/**
	 * 处理方式，1退款，2冲正，3登帐
	 */
	private Integer dealWay;
	
	/**
	 * 处理人
	 */
	private String dealBy;
	
	/**
	 * 处理结果备注
	 */
	private String dealRemark;
	
	/**
	 * 处理状态 0未处置 1已处置
	 */
	private Integer dealState;
	
	/**
	 * 处理日期
	 */
	private String dealDate;
	
	/**
	 * 新增日期
	 */
	private String createDate;
	/**
	 * 新增用户
	 */
	private String createBy;
	
	/**
	 * 更新日期
	 */
	private String updateDate;
	
	/**
	 * 更新用户
	 */
	private String updateBy;
	
	/**
	 * 病人姓名
	 */
	private String nickName;
	
	/**
	 * 住院号/病历号
	 */
	private String caseHistory;
	
	/**
	 * 服务号
	 */
	private String service;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHisOrderNo() {
		return hisOrderNo;
	}

	public void setHisOrderNo(String hisOrderNo) {
		this.hisOrderNo = hisOrderNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
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

	public int getBillType() {
		return billType;
	}

	public void setBillType(int billType) {
		this.billType = billType;
	}

	public Integer getHisPrice() {
		return hisPrice;
	}

	public void setHisPrice(Integer hisPrice) {
		this.hisPrice = hisPrice;
	}

	public Integer getMerchPrice() {
		return merchPrice;
	}

	public void setMerchPrice(Integer merchPrice) {
		this.merchPrice = merchPrice;
	}

	public int getCheckState() {
		return checkState;
	}

	public void setCheckState(int checkState) {
		this.checkState = checkState;
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

	public String getDealRemark() {
		return dealRemark;
	}

	public void setDealRemark(String dealRemark) {
		this.dealRemark = dealRemark;
	}

	public Integer getDealState() {
		return dealState;
	}

	public void setDealState(Integer dealState) {
		this.dealState = dealState;
	}

	public String getDealDate() {
		return dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCaseHistory() {
		return caseHistory;
	}

	public void setCaseHistory(String caseHistory) {
		this.caseHistory = caseHistory;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
}
