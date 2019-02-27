/**
 * 
 */
package com.kasite.core.serviceinterface.module.survey.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-7 下午1:56:23
 */
public class ReqAnswers extends AbsReq{
	private String answer;
	private int questId;
	private String blank;
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getQuestId() {
		return questId;
	}
	public void setQuestId(int questId) {
		this.questId = questId;
	}
	public String getBlank() {
		return blank;
	}
	public void setBlank(String blank) {
		this.blank = blank;
	}
	public ReqAnswers(InterfaceMessage msg, String answer, int questId, String blank) throws AbsHosException {
		super(msg);
		this.answer = answer;
		this.questId = questId;
		this.blank = blank;
	}
	
}
