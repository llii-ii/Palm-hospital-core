package com.kasite.core.serviceinterface.module.report.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespGetReportInfo
 * @author: lcz
 * @date: 2018年7月25日 下午4:59:01
 */
public class RespGetReportInfo extends AbsResp{

	private String reportTitle;
	private String clinicCard;
	private String userType;
	private String hosBedNo;
	private String signingTime;
	private String checker;
	private String barCode;
	private String sex;
	private String age;
	private String isEmergency;
	private String patientName;
	private String sampleNumber;
	private String sampleType;
	private String applicationDepartment;
	private String checkToSee;
	private String submissionTime;
	private String Remark;
	private String reportingPhysicians;
	private String reportTime;
	private String inspector;
	private String itemNum;
	private String clinicNo;
	private String hosUserNo;
	
	private List<RespReportItemDetail> data_1;

	
	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getClinicCard() {
		return clinicCard;
	}

	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getHosBedNo() {
		return hosBedNo;
	}

	public void setHosBedNo(String hosBedNo) {
		this.hosBedNo = hosBedNo;
	}

	public String getSigningTime() {
		return signingTime;
	}

	public void setSigningTime(String signingTime) {
		this.signingTime = signingTime;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getIsEmergency() {
		return isEmergency;
	}

	public void setIsEmergency(String isEmergency) {
		this.isEmergency = isEmergency;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public String getApplicationDepartment() {
		return applicationDepartment;
	}

	public void setApplicationDepartment(String applicationDepartment) {
		this.applicationDepartment = applicationDepartment;
	}

	public String getCheckToSee() {
		return checkToSee;
	}

	public void setCheckToSee(String checkToSee) {
		this.checkToSee = checkToSee;
	}

	public String getSubmissionTime() {
		return submissionTime;
	}

	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}


	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getReportingPhysicians() {
		return reportingPhysicians;
	}

	public void setReportingPhysicians(String reportingPhysicians) {
		this.reportingPhysicians = reportingPhysicians;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}

	public String getClinicNo() {
		return clinicNo;
	}

	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}

	public String getHosUserNo() {
		return hosUserNo;
	}

	public void setHosUserNo(String hosUserNo) {
		this.hosUserNo = hosUserNo;
	}

	public List<RespReportItemDetail> getData_1() {
		return data_1;
	}

	public void setData_1(List<RespReportItemDetail> data_1) {
		this.data_1 = data_1;
	}
	
	
	
}
