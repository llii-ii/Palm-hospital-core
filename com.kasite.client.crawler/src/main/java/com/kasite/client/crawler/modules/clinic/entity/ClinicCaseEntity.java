package com.kasite.client.crawler.modules.clinic.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.client.crawler.modules.EntityID;
import com.kasite.core.common.validator.group.AddGroup;

public class ClinicCaseEntity {
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	/**病人ID patientId **/
	private String patientId;
	@NotBlank(message="病例ID clinicCaseId 不能为空", groups = {AddGroup.class})
	/**病人ID patientId **/
	@EntityID
	private String clinicCaseId;
	@NotBlank(message="门（急）诊号 clinicNum 不能为空", groups = {AddGroup.class})
	/**门（急）诊号 clinicNum **/
	private String clinicNum;
	@NotBlank(message="就诊流水号 medicalNum 不能为空", groups = {AddGroup.class})
	/**就诊流水号 medicalNum **/
	private String medicalNum;
	@NotBlank(message="初诊标志代码 firstVisit 不能为空", groups = {AddGroup.class})
	/**初诊标志代码 firstVisit **/
	private String firstVisit;
	/**过敏史 allergies **/
	private String allergies;
	/**个人史 socialhistory **/
	private String socialhistory;
	/**疾病史 historyOfpastillness **/
	private String historyOfpastillness;
	/**现病史 historyPresentIllness **/
	private String historyPresentIllness;
	/**既往史 pastDiseaseHistory **/
	private String pastDiseaseHistory;
	@NotBlank(message="主诉 complained 不能为空", groups = {AddGroup.class})
	/**主诉 complained **/
	private String complained;
	/**并发症 complication **/
	private String complication;
	/**体格检查 physicalExam **/
	private String physicalExam;
	/**遗传家族史 familyHistory **/
	private String familyHistory;
	/**病历描述 description **/
	private String description;
	/**医嘱备注信息 doctorRemark **/
	private String doctorRemark;
	/**初步诊断 diseases **/
	private String diseases;
	/**全量病历信息 totalRecordInfo **/
	private String totalRecordInfo;
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	public String getClinicCaseId() {
		return clinicCaseId;
	}
	public void setClinicCaseId(String clinicCaseId) {
		this.clinicCaseId = clinicCaseId;
	}
	public String getClinicNum() {
		return clinicNum;
	}
	public void setClinicNum(String clinicNum) {
		this.clinicNum = clinicNum;
	}
	public String getMedicalNum() {
		return medicalNum;
	}
	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
	}
	public String getFirstVisit() {
		return firstVisit;
	}
	public void setFirstVisit(String firstVisit) {
		this.firstVisit = firstVisit;
	}
	public String getAllergies() {
		return allergies;
	}
	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}
	public String getSocialhistory() {
		return socialhistory;
	}
	public void setSocialhistory(String socialhistory) {
		this.socialhistory = socialhistory;
	}
	public String getHistoryOfpastillness() {
		return historyOfpastillness;
	}
	public void setHistoryOfpastillness(String historyOfpastillness) {
		this.historyOfpastillness = historyOfpastillness;
	}
	public String getHistoryPresentIllness() {
		return historyPresentIllness;
	}
	public void setHistoryPresentIllness(String historyPresentIllness) {
		this.historyPresentIllness = historyPresentIllness;
	}
	public String getPastDiseaseHistory() {
		return pastDiseaseHistory;
	}
	public void setPastDiseaseHistory(String pastDiseaseHistory) {
		this.pastDiseaseHistory = pastDiseaseHistory;
	}
	public String getComplained() {
		return complained;
	}
	public void setComplained(String complained) {
		this.complained = complained;
	}
	public String getComplication() {
		return complication;
	}
	public void setComplication(String complication) {
		this.complication = complication;
	}
	public String getPhysicalExam() {
		return physicalExam;
	}
	public void setPhysicalExam(String physicalExam) {
		this.physicalExam = physicalExam;
	}
	public String getFamilyHistory() {
		return familyHistory;
	}
	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDoctorRemark() {
		return doctorRemark;
	}
	public void setDoctorRemark(String doctorRemark) {
		this.doctorRemark = doctorRemark;
	}
	public String getDiseases() {
		return diseases;
	}
	public void setDiseases(String diseases) {
		this.diseases = diseases;
	}
	public String getTotalRecordInfo() {
		return totalRecordInfo;
	}
	public void setTotalRecordInfo(String totalRecordInfo) {
		this.totalRecordInfo = totalRecordInfo;
	}


}
