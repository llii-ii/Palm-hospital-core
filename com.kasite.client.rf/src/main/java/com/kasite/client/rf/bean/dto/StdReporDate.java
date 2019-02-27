package com.kasite.client.rf.bean.dto;

import javax.persistence.Table;

@Table(name="RF_STD_REPORTDATE")
public class StdReporDate {

	/**
	 * 数据父类型（1-支付方式-金额 2-支付方式-笔数 其他自定义）
	 */
	private Integer dataParentType;
	
	/**
	 * 数据子类型 
	 * 当DATAPARENTTYPE = 1时 1=微信，2=支付宝，3=银联
	 * 当DATAPARENTTYPE = 2时 1=微信，2=支付宝，3=银联
	 * 剩余根据需求自定义
	 */
	private Integer dataType;
	
	/**
	 * 数据值
	 */
	private String dataValue;
	
	/**
	 * 统计时间
	 */
	private String sumDate;
	
	/**
	 * 备注
	 */
	private String remark;

	public Integer getDataParentType() {
		return dataParentType;
	}

	public void setDataParentType(Integer dataParentType) {
		this.dataParentType = dataParentType;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getSumDate() {
		return sumDate;
	}

	public void setSumDate(String sumDate) {
		this.sumDate = sumDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
