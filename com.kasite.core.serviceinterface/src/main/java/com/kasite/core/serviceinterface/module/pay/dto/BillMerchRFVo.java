package com.kasite.core.serviceinterface.module.pay.dto;

public class BillMerchRFVo {

	/**
	 * 对账时间
	 */
	private String billDate;
	
	/**
	 * 商户类型(支付渠道):微信：WX，支付宝：ZFB,银联：YL等
	 */
	private String merchType;
	
	/**
	 * 商户代码
	 */
	private String merchCode;
	
	/**
	 * his账单金额（分）
	 */
	private Integer hisBillSum;
	
	/**
	 * 渠道账单金额（分）
	 */
	private Integer merchBillSum;
	
	/**
	 * 核对状态，1账平0账不平
	 */
	private Integer checkState;
	
	
	private String createDate;
	
	private String createBy;
	
	private String updateDate;
	
	private String updateBy;
	
	/**
	 * 处理状态:0,已处理 1,未处理
	 */
	private int dealState;
	
	/**
	 * 商户号
	 */
	private String merchAccount;
	
	/**
	 * 关联银行账号
	 */
	private String bankNo;
	
	/**
	 * 银行简称ICBC等
	 */
	private String bankShortName;

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getMerchType() {
		return merchType;
	}

	public void setMerchType(String merchType) {
		this.merchType = merchType;
	}

	public String getMerchCode() {
		return merchCode;
	}

	public void setMerchCode(String merchCode) {
		this.merchCode = merchCode;
	}

	public Integer getHisBillSum() {
		return hisBillSum;
	}

	public void setHisBillSum(Integer hisBillSum) {
		this.hisBillSum = hisBillSum;
	}

	public Integer getMerchBillSum() {
		return merchBillSum;
	}

	public void setMerchBillSum(Integer merchBillSum) {
		this.merchBillSum = merchBillSum;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
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

	public int getDealState() {
		return dealState;
	}

	public void setDealState(int dealState) {
		this.dealState = dealState;
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

	public String getBankShortName() {
		return bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}
	
}
