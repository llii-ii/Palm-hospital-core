package com.kasite.core.serviceinterface.module.msg.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryAutoReplayArbitrarily
 * @author: lcz
 * @date: 2018年7月25日 上午11:40:53
 */
public class RespQueryAutoReplayArbitrarily extends AbsResp{

	
	private String id;
	private Integer replayType;
	private String keyWord;
	private String replayMsg;
	private Integer matchRule;
	private String createDate;
	private String updateDate;
	private Integer state;
	
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
