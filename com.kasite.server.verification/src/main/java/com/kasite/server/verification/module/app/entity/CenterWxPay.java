package com.kasite.server.verification.module.app.entity;

public class CenterWxPay {
	private Long id;
	private String wx_pay_key;
	private String is_parent_mode;
	private String wx_app_id;
	private String wx_mch_id;
	private String wx_mch_key;
	private String wx_cert;
	private String wx_parent_app_id;
	private String wx_parent_mch_id;
	private String wx_parent_mch_key;
	private String wx_parent_cert;
	private String wx_pay_notify_url;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWx_pay_key() {
		return wx_pay_key;
	}
	public void setWx_pay_key(String wx_pay_key) {
		this.wx_pay_key = wx_pay_key;
	}
	public String getWx_app_id() {
		return wx_app_id;
	}
	public void setWx_app_id(String wx_app_id) {
		this.wx_app_id = wx_app_id;
	}
	public String getWx_mch_id() {
		return wx_mch_id;
	}
	public void setWx_mch_id(String wx_mch_id) {
		this.wx_mch_id = wx_mch_id;
	}
	public String getWx_mch_key() {
		return wx_mch_key;
	}
	public void setWx_mch_key(String wx_mch_key) {
		this.wx_mch_key = wx_mch_key;
	}
	public String getWx_parent_app_id() {
		return wx_parent_app_id;
	}
	public void setWx_parent_app_id(String wx_parent_app_id) {
		this.wx_parent_app_id = wx_parent_app_id;
	}
	public String getWx_parent_mch_id() {
		return wx_parent_mch_id;
	}
	public void setWx_parent_mch_id(String wx_parent_mch_id) {
		this.wx_parent_mch_id = wx_parent_mch_id;
	}
	public String getWx_parent_mch_key() {
		return wx_parent_mch_key;
	}
	public void setWx_parent_mch_key(String wx_parent_mch_key) {
		this.wx_parent_mch_key = wx_parent_mch_key;
	}
	public String getWx_pay_notify_url() {
		return wx_pay_notify_url;
	}
	public void setWx_pay_notify_url(String wx_pay_notify_url) {
		this.wx_pay_notify_url = wx_pay_notify_url;
	}
	public String getWx_cert() {
		return wx_cert;
	}
	public void setWx_cert(String wx_cert) {
		this.wx_cert = wx_cert;
	}
	public String getWx_parent_cert() {
		return wx_parent_cert;
	}
	public void setWx_parent_cert(String wx_parent_cert) {
		this.wx_parent_cert = wx_parent_cert;
	}
	public String getIs_parent_mode() {
		return is_parent_mode;
	}
	public void setIs_parent_mode(String is_parent_mode) {
		this.is_parent_mode = is_parent_mode;
	}
	
}
