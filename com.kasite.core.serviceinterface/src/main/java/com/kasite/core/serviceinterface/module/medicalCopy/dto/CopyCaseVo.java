package com.kasite.core.serviceinterface.module.medicalCopy.dto;

public class CopyCaseVo {
	
	private String caseNumber;//复印数
	private String copyContentNames;//复印名称
	private String copyUtility;//复印用途
	private String inHosDate;//住院时间
	private String outHosDate;//出院时间
	private String caseId;//病例号
	private String inHosDay;//住院天数
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getCopyContentNames() {
		return copyContentNames;
	}
	public void setCopyContentNames(String copyContentNames) {
		this.copyContentNames = copyContentNames;
	}
	public String getCopyUtility() {
		return copyUtility;
	}
	public void setCopyUtility(String copyUtility) {
		this.copyUtility = copyUtility;
	}
	public String getInHosDate() {
		return inHosDate;
	}
	public void setInHosDate(String inHosDate) {
		this.inHosDate = inHosDate;
	}
	public String getOutHosDate() {
		return outHosDate;
	}
	public void setOutHosDate(String outHosDate) {
		this.outHosDate = outHosDate;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getInHosDay() {
		return inHosDay;
	}
	public void setInHosDay(String inHosDay) {
		this.inHosDay = inHosDay;
	}
	
	

}
