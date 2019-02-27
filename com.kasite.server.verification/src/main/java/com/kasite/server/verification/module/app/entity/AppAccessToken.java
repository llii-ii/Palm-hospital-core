package com.kasite.server.verification.module.app.entity;

import java.util.Date;

public class AppAccessToken {

	private String appId;
	private String accessToken;
	private long id;
	private Date createTime;
	private Date invalidTime;
	private String appNowTime;
	public String getAppNowTime() {
		return appNowTime;
	}
	public void setAppNowTime(String appNowTime) {
		this.appNowTime = appNowTime;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getInvalidTime() {
		return invalidTime;
	}
	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}
	
	
}
