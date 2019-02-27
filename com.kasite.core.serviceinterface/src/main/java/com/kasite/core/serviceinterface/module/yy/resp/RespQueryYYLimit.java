package com.kasite.core.serviceinterface.module.yy.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryYYRule
 * @author: lcz
 * @date: 2018年7月24日 下午5:46:37
 */
public class RespQueryYYLimit extends AbsResp{

	
	private String ruleId;
	private Integer countNum;
	private String sqlMessage;
	private String textMessage;
	private Integer state;

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
