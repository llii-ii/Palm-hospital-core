package com.kasite.core.serviceinterface.module.medicalCopy.dto;

import com.kasite.core.common.resp.AbsResp;

public class FengQiaoLogisticsVo extends AbsResp{
	
	private String acceptAddress;//地址
	private String remark;//备注
	private String acceptTime;//时间
	
	
	public String getAcceptAddress() {
		return acceptAddress;
	}
	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}
	
	

}
