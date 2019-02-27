package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="TB_QUESTION")
public class CopyQuestion extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;
	private String question;//字段标题（多个以逗号隔开）
	private String answer;//排序
	private String state;//是否必填
	private Timestamp releaseTime;//是否显示
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
