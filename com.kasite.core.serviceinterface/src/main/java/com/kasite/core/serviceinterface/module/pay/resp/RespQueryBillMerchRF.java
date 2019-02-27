package com.kasite.core.serviceinterface.module.pay.resp;

import java.io.Serializable;

import com.kasite.core.common.resp.AbsResp;

public class RespQueryBillMerchRF extends AbsResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 对账时间
	 */
	private String billDate;
	
	/**
	 * 支付方式:微信,wechat;支付宝,zfb;银联：yl等
	 */
	private String payMethod;
	
	private String payMethodName;
	
	/**
	 * 商户代码
	 */
	private String configkey;
	
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
	
	
	private String createDate;
	
	private String createBy;
	
	private String updateDate;
	
	private String updateBy;
	
	/**
	 * 处理状态:0,已处理 1,未处理
	 */
	private int dealState;
	
	/********************************关联属性********************************/
	
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

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
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
	
}
