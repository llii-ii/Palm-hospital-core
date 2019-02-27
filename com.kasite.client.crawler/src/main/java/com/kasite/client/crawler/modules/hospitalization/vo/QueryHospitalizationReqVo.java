package com.kasite.client.crawler.modules.hospitalization.vo;

import com.kasite.client.crawler.modules.patient.entity.PatientEntity;
import com.kasite.core.common.exception.RRException;

/**
 * 
 * @className: AbsQueryHospitalizationReqVo
 * @author: lcz
 * @date: 2018年6月7日 下午5:46:21
 */
public class QueryHospitalizationReqVo {
	
	protected int pageIndex;
	protected int pageSize;
	protected String startDate;
	protected String endDate;
	protected PatientEntity patient;
	protected String patientId;
	private String inHospitalNum;
	private String medicalNum;
	
	
	public String getMedicalNum() {
		return medicalNum;
	}

	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
	}

	public String getInHospitalNum() {
		return inHospitalNum;
	}

	public void setInHospitalNum(String inHospitalNum) {
		this.inHospitalNum = inHospitalNum;
	}

	public PatientEntity getPatient() {
		return patient;
	}

	public void setPatient(PatientEntity patient) {
		this.patient = patient;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
	public QueryHospitalizationReqVo(PatientEntity patient) throws Exception {
		if(null == patient) {
			throw new RRException("未找到用户。");
		}
		this.patient = patient;
		this.patientId = patient.getPatientId();
	}
	public QueryHospitalizationReqVo(String patientId) {
		this.patientId = patientId;
	}
}
