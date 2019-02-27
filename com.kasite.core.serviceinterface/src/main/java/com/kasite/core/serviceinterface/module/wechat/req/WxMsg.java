package com.kasite.core.serviceinterface.module.wechat.req;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="wx_msg")
public class WxMsg {

	@Id
	@KeySql(useGeneratedKeys=true)
	private long id;
	
	@Column(name="wx_app_id")
	private String wxAppId;
	
	
	private String toUserName;
	private String fromUserName;
	private String msgType;
	private String createTime;
	private String event;
	private String eventKey;
	
	@Column(name="create_time_add")
	private Timestamp createTimeAdd;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getWxAppId() {
		return wxAppId;
	}
	public void setWxAppId(String wxAppId) {
		this.wxAppId = wxAppId;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public Timestamp getCreateTimeAdd() {
		return createTimeAdd;
	}
	public void setCreateTimeAdd(Timestamp createTimeAdd) {
		this.createTimeAdd = createTimeAdd;
	}
	
	
}
