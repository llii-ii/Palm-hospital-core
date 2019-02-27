package com.kasite.core.common.bean.bo;

import java.sql.Timestamp;

/**
 * @author linjf
 * TODO 渠道信息实体类
 */
public class ChannelInfo {

	private String channelId;
	
	private String type;
	
	private String channelName;
	
	private String keyPassword;
	
	private String name;
	
	private String mobile;
	
	private Integer status;
	
	private Timestamp lastModify; 
	
	private Integer md5;
	
	private String md5Key;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getLastModify() {
		return lastModify;
	}

	public void setLastModify(Timestamp lastModify) {
		this.lastModify = lastModify;
	}

	public Integer getMd5() {
		return md5;
	}

	public void setMd5(Integer md5) {
		this.md5 = md5;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}

	
	
}
