package com.kasite.core.common.sys.oauth.entity;

import java.util.Date;

public class AppEntity {

	private String appId;
	private String appName;
	private String appNickName;
	private String orgCode;
	private Date createTime;
	private Date updateTime;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppNickName() {
		return appNickName;
	}
	public void setAppNickName(String appNickName) {
		this.appNickName = appNickName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}
