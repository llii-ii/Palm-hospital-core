package com.kasite.core.serviceinterface.module.qywechat.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * RESP投票、问卷分析
 * 
 * @author 無
 *
 */
public class RespVoteQuestionAnalyze extends AbsResp {
	/**
	 * 问题ID
	 */
	private int id;
	/**
	 * 问题UID
	 */
	private String themeId;
	/**
	 * 问题名称
	 */
	private String questName;
	/**
	 * 问题类型
	 */
	private String questType;
	
	/**
	 * 选项值
	 */
	private String itemValue;
	/**
	 * 答案
	 */
	private String answer;
	/**
	 * 人数
	 */
	private String num;
	/**
	 * 占比
	 */
	private String lv;
	private String sortNum;
	private String sortNum2;

	public String getQuestType() {
		return questType;
	}

	public void setQuestType(String questType) {
		this.questType = questType;
	}

	public String getSortNum() {
		return sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}

	public String getSortNum2() {
		return sortNum2;
	}

	public void setSortNum2(String sortNum2) {
		this.sortNum2 = sortNum2;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getThemeId() {
		return themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getQuestName() {
		return questName;
	}

	public void setQuestName(String questName) {
		this.questName = questName;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getLv() {
		return lv;
	}

	public void setLv(String lv) {
		this.lv = lv;
	}

}
