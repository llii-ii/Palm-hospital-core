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
 * 2017-7-5 下午4:56:26
 */
@Table(name="SV_QUESTION")
public class Question extends BaseDbo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@Id
	@KeySql(useGeneratedKeys=true)
	private Integer questId;
	/**
	 * 问卷ID
	 */
	private Integer subjectId;
	/**
	 * 问题题目/短信开头
	 */
	private String question;
	/**
	 * 指标类型 
	 * 1:医生服务,
	 * 2:护士服务,
	 * 3:医技服务,
	 * 4:药房服务,
	 * 5:收费服务,
	 * 6:就诊环境,
	 * 7:后勤保障,
	 * 8:导医导诊,
	 * 9:就诊流程,
	 * 10:医风医德,
	 * 11:忠诚指数,
	 * 12:其他问题,
	 * 13:通用指标
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
	 * 状态(0:无效，1:有效)
	 */
	private Integer status;
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
	 * 调查对象(1: 门诊患者,2: 住院患者,3: 手术患者,4: 体检客户,5: 通用类型)
	 */
	private Integer objType;
	/**
	 * 是否审阅(0：未审，1：已审)
	 */
	private Integer checkFlag;
	/**
	 * 该题是否可跳过不回答 1是 0否
	 */
	private String mustQuest;
	/**
	 * 最少项
	 */
	private String minOption;
	/**
	 * 最多项
	 */
	private String maxOption;
	public Integer getQuestId() {
		return questId;
	}
	public void setQuestId(Integer questId) {
		this.questId = questId;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Integer getObjType() {
		return objType;
	}
	public void setObjType(Integer objType) {
		this.objType = objType;
	}
	public Integer getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getMustQuest() {
		return mustQuest;
	}
	public void setMustQuest(String mustQuest) {
		this.mustQuest = mustQuest;
	}
	public String getMinOption() {
		return minOption;
	}
	public void setMinOption(String minOption) {
		this.minOption = minOption;
	}
	public String getMaxOption() {
		return maxOption;
	}
	public void setMaxOption(String maxOption) {
		this.maxOption = maxOption;
	}
	
	

	

//	public String toString() {
//		return new ReflectionToStringBuilder(this).toString();
//	}
}
