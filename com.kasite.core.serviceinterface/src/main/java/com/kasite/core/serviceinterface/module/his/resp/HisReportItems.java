package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class HisReportItems  extends AbsResp{

	private String reportId;
	private String reportType;
	private String itemName;
	private String submissionTime;
	private String clinicCard;
	private String patientName;
	private String mobile;
	private String idCardNo;
	private String state;
	/**事件号**/
	private String eventNo;
	/**样本号**/
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
	public String getReportType() {
		return reportType;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getItemName() {
		return itemName;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getMobile() {
		return mobile;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public String getState() {
		return state;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSubmissionTime() {
		return submissionTime;
	}
	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}
	
	
	
	
	
}
