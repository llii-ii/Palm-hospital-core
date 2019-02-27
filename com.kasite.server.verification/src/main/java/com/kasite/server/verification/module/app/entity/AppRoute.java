package com.kasite.server.verification.module.app.entity;

public class AppRoute {

	private Long id;
	private String appId;
	private String appNickName;
	private String routeXml;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppNickName() {
		return appNickName;
	}
	public void setAppNickName(String appNickName) {
		this.appNickName = appNickName;
	}
	public String getRouteXml() {
		return routeXml;
	}
	public void setRouteXml(String routeXml) {
		this.routeXml = routeXml;
	}
	
}
