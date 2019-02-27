package com.kasite.client.crawler.modules.cases.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.group.AddGroup;

/**
 * 
 * @className: QueryHospitalRecordList
 * @author: lcz
 * @date: 2018年6月11日 下午7:43:28
 */
public class QueryHospitalRecordEntity {
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**对于门诊病人，此标识就是门诊病人的挂号号，对于住院病人，此标识就是住院病人的住院号，对于健康体检人，此标识就是体检号**/
	private String eventNo;
	/**患者就诊类型的的分类名称代码**/
	private String eventType;
	/**医院病历 ID**/
	@NotBlank(message="病例ID clinicCaseId 不能为空", groups = {AddGroup.class})
	private String clinicCaseId;
	/**主诉**/
	private String complained;
	/**现病史**/
	private String historyPresentIllness;
	/**既往史**/
	private String pastDiseaseHistory;
	/**个人史**/
	private String socialhistory;
	/**婚育史**/
	private String obstetricalHistory;
	/**月经史**/
	private String menstruationHistory;
	/**遗传家族史**/
	private String familyHistory;
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class}, isNotNull =false)
	/**最后修改时间 lastmodify **/
	private String lastmodify;
	/**就诊流水号**/
	private String medicalNum;
	/**诊治经过**/
	private String diagnosisTreatment;
	/**主治医生**/
	private String attendingPhysician;
	/**出院情况**/
	private String dischargeStatus;
	/**出院医嘱**/
	private String dischargeOrder;
	/**出院小结**/
	private String medicalAbstract;
	/**体格检查**/
	private String physicalExamination;
	/**专科情况**/
	private String juniorCollege;
	/**辅助检查**/
	private String auxiliaryExamination;
	/**全量病历信息**/
	private String totalRecordInfo;
			
	
	public String getMedicalNum() {
		return medicalNum;
	}
	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
	}
	public String getDiagnosisTreatment() {
		return diagnosisTreatment;
	}
	public void setDiagnosisTreatment(String diagnosisTreatment) {
		this.diagnosisTreatment = diagnosisTreatment;
	}
	public String getAttendingPhysician() {
		return attendingPhysician;
	}
	public void setAttendingPhysician(String attendingPhysician) {
		this.attendingPhysician = attendingPhysician;
	}
	public String getDischargeStatus() {
		return dischargeStatus;
	}
	public void setDischargeStatus(String dischargeStatus) {
		this.dischargeStatus = dischargeStatus;
	}
	public String getDischargeOrder() {
		return dischargeOrder;
	}
	public void setDischargeOrder(String dischargeOrder) {
		this.dischargeOrder = dischargeOrder;
	}
	public String getMedicalAbstract() {
		return medicalAbstract;
	}
	public void setMedicalAbstract(String medicalAbstract) {
		this.medicalAbstract = medicalAbstract;
	}
	public String getPhysicalExamination() {
		return physicalExamination;
	}
	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}
	public String getJuniorCollege() {
		return juniorCollege;
	}
	public void setJuniorCollege(String juniorCollege) {
		this.juniorCollege = juniorCollege;
	}
	public String getAuxiliaryExamination() {
		return auxiliaryExamination;
	}
	public void setAuxiliaryExamination(String auxiliaryExamination) {
		this.auxiliaryExamination = auxiliaryExamination;
	}
	public String getTotalRecordInfo() {
		return totalRecordInfo;
	}
	public void setTotalRecordInfo(String totalRecordInfo) {
		this.totalRecordInfo = totalRecordInfo;
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
	public String getClinicCaseId() {
		return clinicCaseId;
	}
	public void setClinicCaseId(String clinicCaseId) {
		this.clinicCaseId = clinicCaseId;
	}
	public String getComplained() {
		return complained;
	}
	public void setComplained(String complained) {
		this.complained = complained;
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
	public String getSocialhistory() {
		return socialhistory;
	}
	public void setSocialhistory(String socialhistory) {
		this.socialhistory = socialhistory;
	}
	public String getObstetricalHistory() {
		return obstetricalHistory;
	}
	public void setObstetricalHistory(String obstetricalHistory) {
		this.obstetricalHistory = obstetricalHistory;
	}
	public String getMenstruationHistory() {
		return menstruationHistory;
	}
	public void setMenstruationHistory(String menstruationHistory) {
		this.menstruationHistory = menstruationHistory;
	}
	public String getFamilyHistory() {
		return familyHistory;
	}
	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}
}
