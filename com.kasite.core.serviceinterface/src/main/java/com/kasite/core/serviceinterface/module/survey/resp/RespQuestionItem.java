/**
 * 
 */
package com.kasite.core.serviceinterface.module.survey.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryAnswer;

/**
 * @author mhd
 * @version 1.0 2017-7-5 下午4:59:10
 */
public class RespQuestionItem extends AbsResp{
	private String itemId;
	private String questId;
	private String itemCont;
	private String sortNum;
	private String ifAddblank;
	private String ifAllowNull;
	
	private String sum;
	
	private String answer;
	
	private List<RespQueryAnswer> otherAnswers;
	
	private String otherAnswer;
	
	private String percent;

	/**
	 * 跳转题目
	 * */
	private String jumpQuest;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String questId) {
		this.questId = questId;
	}

	public String getItemCont() {
		return itemCont;
	}

	public void setItemCont(String itemCont) {
		this.itemCont = itemCont;
	}

	public String getSortNum() {
		return sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}

	public String getIfAddblank() {
		return ifAddblank;
	}

	public void setIfAddblank(String ifAddblank) {
		this.ifAddblank = ifAddblank;
	}

	public String getIfAllowNull() {
		return ifAllowNull;
	}

	public void setIfAllowNull(String ifAllowNull) {
		this.ifAllowNull = ifAllowNull;
	}

	public String getJumpQuest() {
		return jumpQuest;
	}

	public void setJumpQuest(String jumpQuest) {
		this.jumpQuest = jumpQuest;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public List<RespQueryAnswer> getOtherAnswers() {
		return otherAnswers;
	}

	public void setOtherAnswers(List<RespQueryAnswer> otherAnswers) {
		this.otherAnswers = otherAnswers;
	}

	public String getOtherAnswer() {
		return otherAnswer;
	}

	public void setOtherAnswer(String otherAnswer) {
		this.otherAnswer = otherAnswer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

}
