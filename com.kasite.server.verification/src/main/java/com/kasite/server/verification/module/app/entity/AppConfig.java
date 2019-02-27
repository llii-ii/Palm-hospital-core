package com.kasite.server.verification.module.app.entity;

public class AppConfig {

	private Long id;
	private String appId;
	private String clientId;
	private String center_wx_pay_ids;
	private String center_wx_config_ids;
	private String center_zfb_config_ids;
	private String createTime;
	private String updateTime;
	private String operatorId;
	private String operaotrName;
	
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
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
	public String getCenter_wx_pay_ids() {
		return center_wx_pay_ids;
	}
	public void setCenter_wx_pay_ids(String center_wx_pay_ids) {
		this.center_wx_pay_ids = center_wx_pay_ids;
	}
	public String getCenter_wx_config_ids() {
		return center_wx_config_ids;
	}
	public void setCenter_wx_config_ids(String center_wx_config_ids) {
		this.center_wx_config_ids = center_wx_config_ids;
	}
	public String getCenter_zfb_config_ids() {
		return center_zfb_config_ids;
	}
	public void setCenter_zfb_config_ids(String center_zfb_config_ids) {
		this.center_zfb_config_ids = center_zfb_config_ids;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperaotrName() {
		return operaotrName;
	}
	public void setOperaotrName(String operaotrName) {
		this.operaotrName = operaotrName;
	}
	
}
