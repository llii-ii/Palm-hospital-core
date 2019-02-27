package com.kasite.core.serviceinterface.module.msg.resp;


import java.sql.Timestamp;

import javax.persistence.Id;
import com.kasite.core.common.resp.AbsResp;

import tk.mybatis.mapper.annotation.KeySql;


/**
 * @author zwl 2018年11月13日 13:34:44 
 * TODO 消息队对象
 */
public class RespMsgTemp extends AbsResp{
	private String modeId;
	private String modeContent;
	private String userHos;
	private String useDept;
	private String ifUseAllDept;
	private String useChannel;
	private String ifUseAllChannel;
	private String useTime;
	private String ifUseAllTime;
	private String modeType;
	private String modeUrl;
	private Timestamp begin;
	private Timestamp end;
	private String state;
	private String msgType;
	private String pushMode;
	private String msgTempName;
	private String msgTempDemo;
	
	public String getMsgTempDemo() {
		return msgTempDemo;
	}
	public void setMsgTempDemo(String msgTempDemo) {
		this.msgTempDemo = msgTempDemo;
	}
	public String getMsgTempName() {
		return msgTempName;
	}
	public void setMsgTempName(String msgTempName) {
		this.msgTempName = msgTempName;
	}
	
	private String templateId;
	public String getModeId() {
		return modeId;
	}
	public void setModeId(String modeId) {
		this.modeId = modeId;
	}
	public String getModeContent() {
		return modeContent;
	}
	public void setModeContent(String modeContent) {
		this.modeContent = modeContent;
	}
	public String getUserHos() {
		return userHos;
	}
	public void setUserHos(String userHos) {
		this.userHos = userHos;
	}
	public String getUseDept() {
		return useDept;
	}
	public void setUseDept(String useDept) {
		this.useDept = useDept;
	}
	public String getIfUseAllDept() {
		return ifUseAllDept;
	}
	public void setIfUseAllDept(String ifUseAllDept) {
		this.ifUseAllDept = ifUseAllDept;
	}
	public String getUseChannel() {
		return useChannel;
	}
	public void setUseChannel(String useChannel) {
		this.useChannel = useChannel;
	}
	public String getIfUseAllChannel() {
		return ifUseAllChannel;
	}
	public void setIfUseAllChannel(String ifUseAllChannel) {
		this.ifUseAllChannel = ifUseAllChannel;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	public String getIfUseAllTime() {
		return ifUseAllTime;
	}
	public void setIfUseAllTime(String ifUseAllTime) {
		this.ifUseAllTime = ifUseAllTime;
	}
	public String getModeType() {
		return modeType;
	}
	public void setModeType(String modeType) {
		this.modeType = modeType;
	}
	public String getModeUrl() {
		return modeUrl;
	}
	public void setModeUrl(String modeUrl) {
		this.modeUrl = modeUrl;
	}
	
	public Timestamp getBegin() {
		return begin;
	}
	public void setBegin(Timestamp begin) {
		this.begin = begin;
	}
	public Timestamp getEnd() {
		return end;
	}
	public void setEnd(Timestamp end) {
		this.end = end;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getPushMode() {
		return pushMode;
	}
	public void setPushMode(String pushMode) {
		this.pushMode = pushMode;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
