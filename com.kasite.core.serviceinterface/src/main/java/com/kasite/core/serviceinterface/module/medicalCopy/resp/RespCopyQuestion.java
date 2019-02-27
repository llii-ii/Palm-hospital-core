package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import java.sql.Timestamp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespConsigneeAddress
 * @author: cjy
 * @date: 2018年9月18日 上午10:20:19
 */
public class RespCopyQuestion extends AbsResp{
	
	private String id;
	private String question;//字段标题（多个以逗号隔开）
	private String answer;//排序
	private String state;//状态0删除1未发布2已发布
	private Timestamp releaseTime;//发布时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Timestamp getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Timestamp releaseTime) {
		this.releaseTime = releaseTime;
	}
	
	
}
