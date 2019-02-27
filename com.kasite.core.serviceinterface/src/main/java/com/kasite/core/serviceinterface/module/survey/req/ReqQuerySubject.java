package com.kasite.core.serviceinterface.module.survey.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author caiyouhong
 * @version 1.0
 * @time 2017-7-6 上午11:03:19
 **/
public class ReqQuerySubject extends AbsReq {

	/**
	 * @param isNullOrgId
	 *            OrgId是否可以为空;isNullSubId SubjectId 是否可以为空 true 不能为空,false 能为空
	 * */
	public ReqQuerySubject(InterfaceMessage msg, boolean isNullOrgId,
			boolean isNullSubId) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		if (ser == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.orgId = XMLUtil.getString(ser, "OrgId", false,super.getHosId());
		this.subjectId = XMLUtil.getString(ser, "SubjectId", isNullSubId);
		this.surveyType = XMLUtil.getString(ser, "SurveyType", false);
		this.objType = XMLUtil.getString(ser, "ObjType", false);
	}
	
	public ReqQuerySubject(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.orgId = super.getHosId();
			this.subjectId = getDataJs().getString("SubjectId");
			this.questionId = getDataJs().getInteger("Questid");
			this.surveyType = getDataJs().getString("SurveyType");
			this.objType = getDataJs().getString("ObjType");
		}
	}

	private String orgId;
	private String subjectId;
	private Integer questionId;
	private String surveyType;
	/**问卷类型*/
	private String objType;


	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

}
