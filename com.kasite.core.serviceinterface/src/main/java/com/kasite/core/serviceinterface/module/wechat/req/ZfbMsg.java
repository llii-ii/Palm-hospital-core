package com.kasite.core.serviceinterface.module.wechat.req;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;


@Table(name="zfb_msg")
public class ZfbMsg {
	
	@Id
	@KeySql(useGeneratedKeys=true)
	private long id;
	
	private String AppId;
	
	private String FromUserId;
	
	private String FromAlipayUserId;
	
	@Column(name="CreateTime")
	private String CreateTime;
	
	private String MsgType;
	
	private String EventType;
	
	private String ActionParam;
	
	private String AgreementId;
	
	private String AccountNo;
	
	private String MsgId;
	
	private String UserInfo;
	
	@Column(name="create_time")
	private Timestamp createTime;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAppId() {
		return AppId;
	}
	public void setAppId(String appId) {
		AppId = appId;
	}
	public String getFromUserId() {
		return FromUserId;
	}
	public void setFromUserId(String fromUserId) {
		FromUserId = fromUserId;
	}
	public String getFromAlipayUserId() {
		return FromAlipayUserId;
	}
	public void setFromAlipayUserId(String fromAlipayUserId) {
		FromAlipayUserId = fromAlipayUserId;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public String getEventType() {
		return EventType;
	}
	public void setEventType(String eventType) {
		EventType = eventType;
	}
	public String getActionParam() {
		return ActionParam;
	}
	public void setActionParam(String actionParam) {
		ActionParam = actionParam;
	}
	public String getAgreementId() {
		return AgreementId;
	}
	public void setAgreementId(String agreementId) {
		AgreementId = agreementId;
	}
	public String getAccountNo() {
		return AccountNo;
	}
	public void setAccountNo(String accountNo) {
		AccountNo = accountNo;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	public String getUserInfo() {
		return UserInfo;
	}
	public void setUserInfo(String userInfo) {
		UserInfo = userInfo;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
