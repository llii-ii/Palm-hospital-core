package com.kasite.core.serviceinterface.module.yy.req;

import java.io.Serializable;

public class ReqYyLimit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String limitId;
	
	private String ruleId;
	
	private Integer countNum;
	
	private String sqlMessage;
	
	private String textMessage;
	
	private Integer state;

	public String getLimitId() {
		return limitId;
	}

	public void setLimitId(String limitId) {
		this.limitId = limitId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getCountNum() {
		return countNum;
	}

	public void setCountNum(Integer countNum) {
		this.countNum = countNum;
	}

	public String getSqlMessage() {
		return sqlMessage;
	}

	public void setSqlMessage(String sqlMessage) {
		this.sqlMessage = sqlMessage;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
}
