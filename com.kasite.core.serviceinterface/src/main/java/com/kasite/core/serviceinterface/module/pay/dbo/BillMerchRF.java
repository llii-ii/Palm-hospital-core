package com.kasite.core.serviceinterface.module.pay.dbo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Table;

@Table(name="P_BILL_MERCH_RF")
public class BillMerchRF implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 对账时间
	 */
	private String date;
	
	/**
	 * 支付方式:微信,wechat;支付宝,zfb;银联：yl等
	 */
	private String payMethod;
	
	private String payMethodName;
	
	/**
	 * 商户配置
	 */
	private String configkey;
	
	/**
	 * 关联银行账号
	 */
	private String bankNo;
	
	/**
	 * 银行简称ICBC等
	 */
	private String bankShortName;
	
	/**
	 * 所属银行
	 */
	private String bankName;
	
	/**
	 * his账单金额（分）
	 */
	private Long hisBillSum;
	
	/**
	 * 渠道账单金额（分）
	 */
	private Long merchBillSum;
	
	/**
	 * 核对状态，1账平0账不平
	 */
	private Integer checkState;
	
	
	private Timestamp createDate;
	
	private String createBy;
	
	private Timestamp updateDate;
	
	private String updateBy;
	
	/**
	 * 处理状态:0,已处理 1,未处理
	 */
	private Integer dealState;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getConfigkey() {
		return configkey;
	}

	public void setConfigkey(String configkey) {
		this.configkey = configkey;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getHisBillSum() {
		return hisBillSum;
	}

	public void setHisBillSum(Long hisBillSum) {
		this.hisBillSum = hisBillSum;
	}

	public Long getMerchBillSum() {
		return merchBillSum;
	}

	public void setMerchBillSum(Long merchBillSum) {
		this.merchBillSum = merchBillSum;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getDealState() {
		return dealState;
	}

	public void setDealState(Integer dealState) {
		this.dealState = dealState;
	}
	
}
