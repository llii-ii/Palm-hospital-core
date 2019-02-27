/**
 * 
 */
package com.kasite.client.survey.bean.dbo;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-5 下午5:58:12
 */
@Table(name="SV_SAMPLEANSWER")
public class SampleAnswer extends BaseDbo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@KeySql(useGeneratedKeys=true)
	private Integer answerId;
	private Integer sampleId;
	private Integer questId;
	private String answer;
	private String otherAnswer;
	
	public String getOtherAnswer() {
		return otherAnswer;
	}
	public void setOtherAnswer(String otherAnswer) {
		this.otherAnswer = otherAnswer;
	}


	private String replyMsg;
	

	public Integer getAnswerId() {
		return answerId;
	}
	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}
	public Integer getSampleId() {
		return sampleId;
	}
	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}
	public Integer getQuestId() {
		return questId;
	}
	public void setQuestId(Integer questId) {
		this.questId = questId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getReplyMsg() {
		return replyMsg;
	}
	public void setReplyMsg(String replyMsg) {
		this.replyMsg = replyMsg;
	}
//	
//	
//	public String toString() {
//		return new ReflectionToStringBuilder(this).toString();
//	}
}

