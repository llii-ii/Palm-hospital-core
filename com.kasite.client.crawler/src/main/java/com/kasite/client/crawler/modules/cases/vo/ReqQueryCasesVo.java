package com.kasite.client.crawler.modules.cases.vo;

import com.kasite.client.crawler.modules.patient.entity.PatientEntity;

/**
 * 
 * @className: ReqQueryCasesVo
 * @author: lcz
 * @date: 2018年6月11日 下午7:32:06
 */
public class ReqQueryCasesVo {
	protected int pageIndex;
	protected int pageSize;
	protected String startDate;
	protected String endDate;
	protected PatientEntity patient;
	protected String patientId;
	private String eventNo;
	private String eventType;
	
	public ReqQueryCasesVo(String patientId) {
		this.patientId = patientId;
	}
	public ReqQueryCasesVo(PatientEntity patient) {
		this.patient = patient;
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
	public String getEventNo() {
		return eventNo;
	}
	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	
}
