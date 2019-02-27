package com.kasite.core.serviceinterface.module.report.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespGetReportList
 * @author: lcz
 * @date: 2018年7月25日 下午4:57:42
 */
public class RespGetReportList extends AbsResp{
	private String reportId;
	private String reportType;
	private String submissionTime;
	private String patientName;
	private String itemName;
	private String state;
	private String eventNo;
	private String sampleNo;
	
	
	/**
	 * @return the eventNo
	 */
	public String getEventNo() {
		return eventNo;
	}
	/**
	 * @param eventNo the eventNo to set
	 */
	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}
	/**
	 * @return the sampleNo
	 */
	public String getSampleNo() {
		return sampleNo;
	}
	/**
	 * @param sampleNo the sampleNo to set
	 */
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getSubmissionTime() {
		return submissionTime;
	}
	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
