package com.kasite.core.serviceinterface.module.survey.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMatriStamp;

/**
 * @author caiyouhong
 * @version 1.0
 * @time 2017-7-6 下午7:47:35
 **/
public class RespQueryQuestion extends AbsResp{

	/**
	 * 
	 */
	private Integer questId;
	/**
	 * 问题题目/短信开头
	 */
	private String question;
	/**
	 * 指标类型 1:医生服务, 2:护士服务, 3:医技服务, 4:药房服务, 5:收费服务, 6:就诊环境, 7:后勤保障, 8:导医导诊,
	 * 9:就诊流程, 10:医风医德, 11:忠诚指数, 12:其他问题, 13:通用指标
	 */
	private Integer contentType;
	/**
	 * 题型(1:单选,2:多选,3:填空,4:矩阵单选题,5:矩阵多选题)
	 */
	private Integer questType;
	/**
	 * 排序编号
	 */
	private Integer sortNum;

	/**
	 * 题库类型(1:题库(被收录)，2:自定义)
	 */
	private Integer subType;
	/**
	 * 
	 */
	private Integer parentQuestId;
	/**
	 * 推荐(1:是，2:否)
	 */
	private Integer recommend;
	/**
	 * 矩阵题父题目ID
	 */
	private Integer matrixQuestId;
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

	private List<RespQuestionItem> svQuestionItems;

	private List<RespMartrQueryQuestion> childrenMatrixQuestion;
	
	private List<RespQuestionItem> categories;
	
	private List<RespQueryMatriStamp> series;
	
	private Integer totalSamp;

	public Integer getQuestId() {
		return questId;
	}

	public void setQuestId(Integer questId) {
		this.questId = questId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public Integer getQuestType() {
		return questType;
	}

	public void setQuestType(Integer questType) {
		this.questType = questType;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Integer getSubType() {
		return subType;
	}

	public void setSubType(Integer subType) {
		this.subType = subType;
	}

	public Integer getParentQuestId() {
		return parentQuestId;
	}

	public void setParentQuestId(Integer parentQuestId) {
		this.parentQuestId = parentQuestId;
	}

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	public Integer getMatrixQuestId() {
		return matrixQuestId;
	}

	public void setMatrixQuestId(Integer matrixQuestId) {
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

	public List<RespQuestionItem> getSvQuestionItems() {
		return svQuestionItems;
	}

	public void setSvQuestionItems(List<RespQuestionItem> svQuestionItems) {
		this.svQuestionItems = svQuestionItems;
	}

	public List<RespMartrQueryQuestion> getChildrenMatrixQuestion() {
		return childrenMatrixQuestion;
	}

	public void setChildrenMatrixQuestion(List<RespMartrQueryQuestion> childrenMatrixQuestion) {
		this.childrenMatrixQuestion = childrenMatrixQuestion;
	}

	public Integer getTotalSamp() {
		return totalSamp;
	}

	public void setTotalSamp(Integer totalSamp) {
		this.totalSamp = totalSamp;
	}

	public List<RespQuestionItem> getCategories() {
		return categories;
	}

	public void setCategories(List<RespQuestionItem> categories) {
		this.categories = categories;
	}

	public List<RespQueryMatriStamp> getSeries() {
		return series;
	}

	public void setSeries(List<RespQueryMatriStamp> series) {
		this.series = series;
	}

}
