package com.kasite.core.serviceinterface.module.pay.dbo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Table;

@Table(name="P_BANK_MONEY_CHECK")
public class BankMoneyCheck implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 日期字符串yyyy-mm-dd
	 */
	private String date;
	
	/**
	 * 开户行
	 */
	private String bankName;
	
	/**
	 * 银行账号
	 */
	private String bankNo;
	
	/**
	 * 应到金额（分）
	 */
	private Long payAbleMoney;
	
	/**
	 * 已到金额（分）
	 */
	private Long paideMoney;
	
	/**
	 * 勾兑结果: 0账平，1长款，-1短款
	 */
	private Integer checkState;
	
	/**
	 * 是否勾兑 0未勾兑 1已勾兑
	 */
	private Integer isCheck;
	
	/**
	 * 银行到款流水号
	 */
	private String bankFlowNo;
	
	/**
	 * 新增日期
	 */
	private Timestamp createDate;
	
	/**
	 * 新增用户
	 */
	private String createBy;
	
	/**
	 * 更新日期
	 */
	private Timestamp updateDate;
	
	/**
	 * 更新用户
	 */
	private String updateBy;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public Long getPayAbleMoney() {
		return payAbleMoney;
	}

	public void setPayAbleMoney(Long payAbleMoney) {
		this.payAbleMoney = payAbleMoney;
	}

	public Long getPaideMoney() {
		return paideMoney;
	}

	public void setPaideMoney(Long paideMoney) {
		this.paideMoney = paideMoney;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

	public String getBankFlowNo() {
		return bankFlowNo;
	}

	public void setBankFlowNo(String bankFlowNo) {
		this.bankFlowNo = bankFlowNo;
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
	
}
