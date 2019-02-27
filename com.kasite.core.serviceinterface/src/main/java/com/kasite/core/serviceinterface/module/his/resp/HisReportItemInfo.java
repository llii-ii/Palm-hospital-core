package com.kasite.core.serviceinterface.module.his.resp;

import java.util.ArrayList;
import java.util.List;

import com.kasite.core.common.resp.AbsResp;


/**
 * @author linjf
 * TODO
 */
public class HisReportItemInfo  extends AbsResp{
	private String rmark;
	private String itemNo;
	private String reportId;
	private String reportTitle;
	private String userType;   
	private String clinicCard; 
	private String hosUserNo;  
	private String hosBedNo;   
	private String clinicNo;   
	private String checkSite;
	private String clinicAldiagnosis;
	private String checkName;    
	private String checkMethod;  
	private String checkTosee;
	private String reportingPhysicians;
	private String auditingPhysicians;
	private String deviceNumber; 
	private String signingTime;    
	private String checkSee;
	private String age;
	
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
	private String isEmergency;
	public String getCheckSee() {
		return checkSee;
	}
	public void setCheckSee(String checkSee) {
		this.checkSee = checkSee;
	}
	public String getSigningTime() {
		return signingTime;
	}
	public void setSigningTime(String signingTime) {
		this.signingTime = signingTime;
	}
	public String getSubmissionTime() {
		return submissionTime;
	}
	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	private String barCode;
	private String sex; 
	private String patientName; 
	private String sampleNumber;
	private String sampleType;
	private String applicationDepartment;
	private String applicationDoctor;   
	private String submissionTime;
	private String remark;    
	private String reportTime;
	private String inspector;
	private String checker;  
	private String itemNum;
	private List<HisReportItemDetails> reportItemDetails=new ArrayList<HisReportItemDetails>();
	private String  words;
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	public List<HisReportItemDetails> getReportItemDetails() {
		return reportItemDetails;
	}
	public void setReportItemDetails(List<HisReportItemDetails> reportItemDetails) {
		this.reportItemDetails = reportItemDetails;
	}
	public String getRmark() {
		return rmark;
	}
	public void setRmark(String rmark) {
		this.rmark = rmark;
	}
	public String getItemNo() {
		return itemNo;
	}
	public String getReportId() {
		return reportId;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	public String getUserType() {
		return userType;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public String getHosUserNo() {
		return hosUserNo;
	}
	public String getHosBedNo() {
		return hosBedNo;
	}
	public String getClinicNo() {
		return clinicNo;
	}
	public String getCheckSite() {
		return checkSite;
	}
	public String getClinicAldiagnosis() {
		return clinicAldiagnosis;
	}
	public String getCheckName() {
		return checkName;
	}
	public String getCheckMethod() {
		return checkMethod;
	}
	public String getCheckTosee() {
		return checkTosee;
	}
	public String getReportingPhysicians() {
		return reportingPhysicians;
	}
	public String getAuditingPhysicians() {
		return auditingPhysicians;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	
	public String getBarCode() {
		return barCode;
	}
	public String getSex() {
		return sex;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getSampleNumber() {
		return sampleNumber;
	}
	public String getSampleType() {
		return sampleType;
	}
	public String getApplicationDepartment() {
		return applicationDepartment;
	}
	public String getApplicationDoctor() {
		return applicationDoctor;
	}

	public String getRemark() {
		return remark;
	}
	
	public String getInspector() {
		return inspector;
	}
	public String getChecker() {
		return checker;
	}
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	public void setHosUserNo(String hosUserNo) {
		this.hosUserNo = hosUserNo;
	}
	public void setHosBedNo(String hosBedNo) {
		this.hosBedNo = hosBedNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public void setCheckSite(String checkSite) {
		this.checkSite = checkSite;
	}
	public void setClinicAldiagnosis(String clinicAldiagnosis) {
		this.clinicAldiagnosis = clinicAldiagnosis;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}
	public void setCheckTosee(String checkTosee) {
		this.checkTosee = checkTosee;
	}
	public void setReportingPhysicians(String reportingPhysicians) {
		this.reportingPhysicians = reportingPhysicians;
	}
	public void setAuditingPhysicians(String auditingPhysicians) {
		this.auditingPhysicians = auditingPhysicians;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public void setApplicationDepartment(String applicationDepartment) {
		this.applicationDepartment = applicationDepartment;
	}
	public void setApplicationDoctor(String applicationDoctor) {
		this.applicationDoctor = applicationDoctor;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public void setInspector(String inspector) {
		this.inspector = inspector;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	
}
