package com.kasite.core.serviceinterface.module.survey.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author caiyouhong
 * @version 1.0
 * @time 2017-7-6 下午3:32:02
 **/
public class RespQuerySubject extends AbsResp{

	

	private Integer subjectId;
	
	private Integer questionId;
	/**问卷题目*/
	private String subjectTitle; 

	/**
	 * 调查对象
	 */
	private Integer objtype;
	private String remark;
	/**
	 * 开场白
	 */
	private String beginIntro;
	/**
	 * 结束语
	 */
	private String endingIntro;
	/**
	 * 状态(0:无效，1:有效，2:调查中，3:已结束 4：待审核 5：审核不通过)
	 */
	private Integer status;
	/**
	 * 需要问卷量
	 */
	private Integer quantity;
//	/**
//	 * 省份ID
//	 */
//	private String provinceid;
//	/**
//	 * 城市ID
//	 */
//	private String cityid;
	/**
	 * 调查类型
	 */
	private Integer surveyType;
	/**
	 * 结束时间
	 */
	private String overtime;
	/**
	 * 结束类型 1 收集多少份结束 2 到什么时间结束 3无限制
	 */
	private Integer overtype;
	/**
	 * 回复类型设置 1 一个手机或电脑只能回复一次 2每个IP只能回复一次 3无限制
	 */
	private Integer replytype;
	
	private String createTime;
	
	private Integer countsample;
	
	private String subjectTotal;

	private List<RespQueryQuestion> result;
	
	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getSubjectTitle() {
		return subjectTitle;
	}

	public void setSubjectTitle(String subjectTitle) {
		this.subjectTitle = subjectTitle;
	}
	
	public String getSubjectTotal() {
		return subjectTotal;
	}

	public void setSubjectTotal(String subjectTotal) {
		this.subjectTotal = subjectTotal;
	}

	public Integer getObjtype() {
		return objtype;
	}

	public void setObjtype(Integer objtype) {
		this.objtype = objtype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBeginIntro() {
		return beginIntro;
	}

	public void setBeginIntro(String beginIntro) {
		this.beginIntro = beginIntro;
	}

	public String getEndingIntro() {
		return endingIntro;
	}

	public void setEndingIntro(String endingIntro) {
		this.endingIntro = endingIntro;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(Integer surveyType) {
		this.surveyType = surveyType;
	}

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getOvertype() {
		return overtype;
	}

	public void setOvertype(Integer overtype) {
		this.overtype = overtype;
	}

	public Integer getReplytype() {
		return replytype;
	}

	public void setReplytype(Integer replytype) {
		this.replytype = replytype;
	}

	public Integer getCountsample() {
		return countsample;
	}

	public void setCountsample(Integer countsample) {
		this.countsample = countsample;
	}

	public List<RespQueryQuestion> getResult() {
		return result;
	}

	public void setResult(List<RespQueryQuestion> result) {
		this.result = result;
	}

}
