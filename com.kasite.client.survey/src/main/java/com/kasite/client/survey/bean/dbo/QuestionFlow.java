/**
 * 
 */
package com.kasite.client.survey.bean.dbo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-5 下午5:00:38
 */
@Table(name="SV_QUESTIONFLOW")
public class QuestionFlow implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@KeySql(useGeneratedKeys=true)
	private Integer flowId;
	private Integer itemId;
	private Integer nextQuestId;
	private String operatorId;
	private String operatorName;
	private Timestamp operTime;
	private Integer status;
	public Integer getFlowId() {
		return flowId;
	}
	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getNextQuestId() {
		return nextQuestId;
	}
	public void setNextQuestId(Integer nextQuestId) {
		this.nextQuestId = nextQuestId;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Timestamp getOperTime() {
		return operTime;
	}
	public void setOperTime(Timestamp operTime) {
		this.operTime = operTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	

//	public String toString() {
//		return new ReflectionToStringBuilder(this).toString();
//	}
}

