package com.kasite.core.serviceinterface.module.pay.dbo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Table;

@Table(name="P_CHANNEL_BANK_CHECK")
public class ChannelBankCheck implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 日期字符串yyyy-mm-dd
	 */
	private String date;
	
	/**
	 * 渠道号
	 */
	private String channelId;
	
	/**
	 * 渠道名称
	 */
	private String channelName;
	
	/**
	 * 服务类型
	 */
	private String serviceId;
	
	/**
	 * 商户配置KEY
	 */
	private String configKey;
	
	/**
	 * 支付方式
	 */
	private String payMethod;
	
	/**
	 * 支付方式名称
	 */
	private String payMethodName;
	
	/**
	 * 开户行
	 */
	private String bankName;
	
	/**
	 * 银行简拼
	 */
	private String bankShortName;
	
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
	 * 差额(分)
	 */
	private Long diffMoney;
	
	/**
	 * 勾兑结果: 0账平，1长款，-1短款
	 */
	private Integer checkState;

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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankShortName() {
		return bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
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

	public Long getDiffMoney() {
		return diffMoney;
	}

	public void setDiffMoney(Long diffMoney) {
		this.diffMoney = diffMoney;
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
	
}
