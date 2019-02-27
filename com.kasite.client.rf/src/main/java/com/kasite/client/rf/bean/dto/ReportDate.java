package com.kasite.client.rf.bean.dto;

import javax.persistence.Table;

/**
 * @author linjf 2017年11月14日 17:37:18
 * TODO 日报表数据对象
 */
@Table(name="RF_REPORTDATE")
public class ReportDate {
	private String channelId;
	private String channelName;
	private Integer dataType;
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	private String dataValue;
	private String sumDate;
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
}
