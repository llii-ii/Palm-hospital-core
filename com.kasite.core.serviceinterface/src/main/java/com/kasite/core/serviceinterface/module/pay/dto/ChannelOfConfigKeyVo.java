package com.kasite.core.serviceinterface.module.pay.dto;

/**
 * 渠道关联商户配置查询实体类
 * 
 * @author zhaoy
 *
 */
public class ChannelOfConfigKeyVo {

	/**
	 * 渠道号
	 */
	private String channelId;
	
	/**
	 * 渠道名称
	 */
	private String channelName;
	
	/**
	 * 商户配置
	 */
	private String configKey;
	
	/**
	 * 服务号
	 */
	private String serviceId;
	
	/**
	 * 银行账号
	 */
	private String bankNo;
	
	/**
	 * 开户行
	 */
	private String bankName;
	
	/**
	 * 银行简拼
	 */
	private String bankShortName;
	
	/**
	 * 应收/应退金额(His账单统计-分)
	 */
	private Long payableMoney;
	
	/**
	 * 实收/实退金额(商户账单统计-分)
	 */
	private Long paideMoney;
	
	/**
	 * 差额(实收-应收)
	 */
	private Long diffMoney;

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

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getBankShortName() {
		return bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}

	public Long getPayableMoney() {
		return payableMoney;
	}

	public void setPayableMoney(Long payableMoney) {
		this.payableMoney = payableMoney;
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
	
}
