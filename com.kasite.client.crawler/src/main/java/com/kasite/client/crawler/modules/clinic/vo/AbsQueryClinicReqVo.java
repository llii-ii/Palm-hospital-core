package com.kasite.client.crawler.modules.clinic.vo;

import com.kasite.client.crawler.modules.clinic.ClinicTable;
import com.kasite.client.crawler.modules.patient.entity.PatientEntity;
import com.kasite.core.common.exception.RRException;

public class  AbsQueryClinicReqVo {
	
	protected ClinicTable table;
	
	protected String whereSql;
	
	protected int pageIndex;
	protected int pageSize;
	protected String startDate;
	protected String endDate;
	protected String medicalNum;
	
	
	
	public String getMedicalNum() {
		return medicalNum;
	}
	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
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
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
	public String getWhereSql() {
		return whereSql;
	}
	
	public void setTable(ClinicTable table) {
		this.table = table;
	}
	
	public ClinicTable getTable() {
		return table;
	}
	
	public AbsQueryClinicReqVo(String patientId) {
		this.patientId = patientId;
	}

	public AbsQueryClinicReqVo(PatientEntity patient) throws Exception {
		if(null == patient) {
			throw new RRException("未找到用户。");
		}
		this.patient = patient;
		this.patientId = patient.getPatientId();
	}

	protected PatientEntity patient;
	
	public PatientEntity getPatient() {
		return patient;
	}

	public void setPatient(PatientEntity patient) {
		this.patient = patient;
	}

	/**
	 * 医院内部的病人ID
	 */
	protected String patientId;

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	
}
