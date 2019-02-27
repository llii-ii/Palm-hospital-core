package com.kasite.core.serviceinterface.module.channel.dto;

/**
 * 商户管理
 * 
 * @author zhaoy
 *
 */
public class MerchConfigVo {

	private Integer serialId;
	
	private String merchId;
	
	private String configkey;
	
	private String merchType;
	
	private String merchTypeName;
	
	private String bankNo;
	
	private String bankShortName;
	
	private String merchPayConfig;
	
	private Integer rowNum;

	public Integer getSerialId() {
		return serialId;
	}

	public void setSerialId(Integer serialId) {
		this.serialId = serialId;
	}

	public String getMerchId() {
		return merchId;
	}

	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}

	public String getConfigkey() {
		return configkey;
	}

	public void setConfigkey(String configkey) {
		this.configkey = configkey;
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

	public String getMerchPayConfig() {
		return merchPayConfig;
	}

	public void setMerchPayConfig(String merchPayConfig) {
		this.merchPayConfig = merchPayConfig;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}
	
	
}
