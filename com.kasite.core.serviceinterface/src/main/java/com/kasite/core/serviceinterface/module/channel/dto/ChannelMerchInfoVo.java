package com.kasite.core.serviceinterface.module.channel.dto;

public class ChannelMerchInfoVo {

	/**
	 * 渠道ID
	 */
	private String channelId;
	
	/**
	 * 渠道名称
	 */
	private String channelName;
	
	/**
	 * 商户类型
	 */
	private String merchType;
	
	private String merchTypeName;
	
	/**
	 * 商户代码
	 */
	private String merchCode;
	
	/**
	 * 商户帐号
	 */
	private String merchAccount;
	
	/**
	 * 银行帐号
	 */
	private String bankNo;
	
	/**
	 * 银行名称
	 */
	private String bankName;
	
	/**
	 * 是否启用
	 */
	private Integer isEnable;
	
	private String singlePriceLimit;
	
	private String payTimeLimit;
	
	private Integer isCredit;
	
	private String refundTimeLimit;
	
	private Integer status;

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

	public String getMerchType() {
		return merchType;
	}

	public void setMerchType(String merchType) {
		this.merchType = merchType;
	}

	public String getMerchTypeName() {
		return merchTypeName;
	}

	public void setMerchTypeName(String merchTypeName) {
		this.merchTypeName = merchTypeName;
	}

	public String getMerchCode() {
		return merchCode;
	}

	public void setMerchCode(String merchCode) {
		this.merchCode = merchCode;
	}

	public String getMerchAccount() {
		return merchAccount;
	}

	public void setMerchAccount(String merchAccount) {
		this.merchAccount = merchAccount;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getSinglePriceLimit() {
		return singlePriceLimit;
	}

	public void setSinglePriceLimit(String singlePriceLimit) {
		this.singlePriceLimit = singlePriceLimit;
	}

	public String getPayTimeLimit() {
		return payTimeLimit;
	}

	public void setPayTimeLimit(String payTimeLimit) {
		this.payTimeLimit = payTimeLimit;
	}

	public Integer getIsCredit() {
		return isCredit;
	}

	public void setIsCredit(Integer isCredit) {
		this.isCredit = isCredit;
	}

	public String getRefundTimeLimit() {
		return refundTimeLimit;
	}

	public void setRefundTimeLimit(String refundTimeLimit) {
		this.refundTimeLimit = refundTimeLimit;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
