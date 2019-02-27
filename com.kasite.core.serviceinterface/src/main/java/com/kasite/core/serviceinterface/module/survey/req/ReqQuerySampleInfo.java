package com.kasite.core.serviceinterface.module.survey.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqQuerySampleInfo extends AbsReq {

	public ReqQuerySampleInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.subjectId = getDataJs().getInteger("SubjectId");
			this.sampleId = getDataJs().getString("SampleId");
			this.status = getDataJs().getInteger("Status");
			this.orgId = super.getHosId();
		}
	}
	
	private String sampleId;
	private Integer subjectId;
	private Integer status;
	private String orgId;

	
	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	
}
