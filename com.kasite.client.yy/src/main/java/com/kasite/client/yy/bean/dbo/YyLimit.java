package com.kasite.client.yy.bean.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**预约限制
 * @author lsq
 * version 1.0
 * 2017-7-7上午9:46:15
 */
@Table(name="YY_LIMIT")
public class YyLimit extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys = true)
	private String ruleId;
	private Integer countNum;
	private String sqlMessage;
	private String textMessage;
	private Integer state;
	
	
	public String getRuleId() {
		return ruleId;
	}
	public Integer getCountNum() {
		return countNum;
	}
	public String getSqlMessage() {
		return sqlMessage;
	}
	public String getTextMessage() {
		return textMessage;
	}
	public Integer getState() {
		return state;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public void setCountNum(Integer countNum) {
		this.countNum = countNum;
	}
	public void setSqlMessage(String sqlMessage) {
		this.sqlMessage = sqlMessage;
	}
	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
	
}
