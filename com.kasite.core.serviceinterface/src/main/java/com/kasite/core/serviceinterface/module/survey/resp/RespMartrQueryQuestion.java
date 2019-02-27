package com.kasite.core.serviceinterface.module.survey.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author caiyouhong
 * @version 1.0
 * @time 2017-7-6 下午7:47:35
 **/
public class RespMartrQueryQuestion extends AbsResp{

	/**
	 * 
	 */
	private String questId;
	/**
	 * 问题题目/短信开头
	 */
	private String question;
	/**
	 * 指标类型 1:医生服务, 2:护士服务, 3:医技服务, 4:药房服务, 5:收费服务, 6:就诊环境, 7:后勤保障, 8:导医导诊,
	 * 9:就诊流程, 10:医风医德, 11:忠诚指数, 12:其他问题, 13:通用指标
	 */
	private String contentType;
	/**
	 * 题型(1:单选,2:多选,3:填空,4:矩阵单选题,5:矩阵多选题)
	 */
	private String questType;
	/**
	 * 排序编号
	 */
	private String sortNum;

	/**
	 * 题库类型(1:题库(被收录)，2:自定义)
	 */
	private String subType;
	/**
	 * 
	 */
	private String parentQuestId;
	/**
	 * 推荐(1:是，2:否)
	 */
	private String recommend;
	/**
	 * 矩阵题父题目ID
	 */
	private String matrixQuestId;
	/**
	 * 该题是否可跳过不回答 1是 0否
	 */
	private String mustquest;
	/**
	 * 最少项
	 */
	private String minoption;
	/**
	 * 最多项
	 */
	private String maxoption;

	private List<RespQuestionItem> matrixQuestItems;

	
	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String questId) {
		this.questId = questId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

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

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getParentQuestId() {
		return parentQuestId;
	}

	public void setParentQuestId(String parentQuestId) {
		this.parentQuestId = parentQuestId;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getMatrixQuestId() {
		return matrixQuestId;
	}

	public void setMatrixQuestId(String matrixQuestId) {
		this.matrixQuestId = matrixQuestId;
	}

	public String getMustquest() {
		return mustquest;
	}

	public void setMustquest(String mustquest) {
		this.mustquest = mustquest;
	}

	public String getMinoption() {
		return minoption;
	}

	public void setMinoption(String minoption) {
		this.minoption = minoption;
	}

	public String getMaxoption() {
		return maxoption;
	}

	public void setMaxoption(String maxoption) {
		this.maxoption = maxoption;
	}

	public List<RespQuestionItem> getMatrixQuestItems() {
		return matrixQuestItems;
	}

	public void setMatrixQuestItems(List<RespQuestionItem> matrixQuestItems) {
		this.matrixQuestItems = matrixQuestItems;
	}

	


}
