package com.kasite.server.verification.module.app.entity;

public class CenterWxConfig {
	private Long id;
	private String wx_app_id;
	private String wx_original_id;
	private String wx_app_name;
	private Integer wx_app_type;
	private String wx_token;
	private String wx_secret;
	private String wx_oauthCallbackUrl;
	private Integer state;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWx_app_id() {
		return wx_app_id;
	}
	public void setWx_app_id(String wx_app_id) {
		this.wx_app_id = wx_app_id;
	}
	public String getWx_app_name() {
		return wx_app_name;
	}
	public void setWx_app_name(String wx_app_name) {
		this.wx_app_name = wx_app_name;
	}
	public Integer getWx_app_type() {
		return wx_app_type;
	}
	public void setWx_app_type(Integer wx_app_type) {
		this.wx_app_type = wx_app_type;
	}
	public String getWx_token() {
		return wx_token;
	}
	public void setWx_token(String wx_token) {
		this.wx_token = wx_token;
	}
	public String getWx_secret() {
		return wx_secret;
	}
	public void setWx_secret(String wx_secret) {
		this.wx_secret = wx_secret;
	}
	public String getWx_oauthCallbackUrl() {
		return wx_oauthCallbackUrl;
	}
	public void setWx_oauthCallbackUrl(String wx_oauthCallbackUrl) {
		this.wx_oauthCallbackUrl = wx_oauthCallbackUrl;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getWx_original_id() {
		return wx_original_id;
	}
	public void setWx_original_id(String wx_original_id) {
		this.wx_original_id = wx_original_id;
	}
	
}
