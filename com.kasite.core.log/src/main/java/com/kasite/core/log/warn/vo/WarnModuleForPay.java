package com.kasite.core.log.warn.vo;

/**
 * 支付告警模板-(企微)
 * 
 * @author zhaoy
 *
 */
public class WarnModuleForPay {

	private String title;
	
	private String desc;
	
	private String url;
	
	private String touser;
	
	private String toparty;
	
	private String hosid;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(String toparty) {
		this.toparty = toparty;
	}

	public String getHosid() {
		return hosid;
	}

	public void setHosid(String hosid) {
		this.hosid = hosid;
	}
	
}
