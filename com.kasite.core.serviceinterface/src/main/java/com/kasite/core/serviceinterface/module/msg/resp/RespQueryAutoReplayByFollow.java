package com.kasite.core.serviceinterface.module.msg.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryAutoReplayByFollow
 * @author: lcz
 * @date: 2018年7月25日 上午10:46:11
 */
public class RespQueryAutoReplayByFollow extends AbsResp{

	
	private String id;
	//公众号／服务窗 appId
	private String appId;
	private Integer replayType;
	//消息类型 1 文本消息，2图文消息
	private Integer msgType;
	private String keyWord;
	private String replayMsg;
	private Integer matchRule;
	private String createDate;
	private String updateDate;
	private Integer state;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getReplayType() {
		return replayType;
	}
	public void setReplayType(Integer replayType) {
		this.replayType = replayType;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getReplayMsg() {
		return replayMsg;
	}
	public void setReplayMsg(String replayMsg) {
		this.replayMsg = replayMsg;
	}
	public Integer getMatchRule() {
		return matchRule;
	}
	public void setMatchRule(Integer matchRule) {
		this.matchRule = matchRule;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
	
}
