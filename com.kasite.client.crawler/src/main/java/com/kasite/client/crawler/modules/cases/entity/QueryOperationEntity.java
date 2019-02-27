package com.kasite.client.crawler.modules.cases.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.group.AddGroup;

/**
 * 
 * @className: QueryOperationEntity
 * @author: lcz
 * @date: 2018年6月11日 下午7:44:13
 */
public class QueryOperationEntity {
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**对于门诊病人，此标识就是门诊病人的挂号号，对于住院病人，此标识就是住院病人的住院号，对于健康体检人，此标识就是体检号**/
	private String eventNo;
	/**患者就诊类型的的分类名称代码**/
	private String eventType;
	/**医院病历 ID**/
	private String clinicCaseId;
	/**手术申请单号**/
	private String departmentNo;
	/**就诊科室名称**/
	private String departmentName;
	/**手术时间**/
	private String operationDate;
	/**手术编码**/
	private String operationCode;
	/**手术名称**/
	private String operationName;
	/**手术部位**/
	private String operationSite;
	/**手术级别名称**/
	private String operationLevelName;
	/**手术切口类别名称**/
	private String operationIncisionCategory;
	/**麻醉方法名称**/
	private String anesthesiaMethodName;
	/**麻醉医师：姓名**/
	private String anesthesiaDoctorName;
	/**主刀医生姓名**/
	private String doctorName;
	/**Ⅰ助手医生姓名**/
	private String assistantDoctorNameA;
	/**Ⅱ助手医生姓名**/
	private String assistantDoctorNameB;
	/**III助手医生姓名**/
	private String assistantDoctorNameC;
	/**术前诊断**/
	private String preoperativeDiagnosis;
	/**术中诊断**/
	private String intraoperativeDiagnosis;
	/**手术经过**/
	private String operationProcess;
	/**全量手术记录信息**/
	private String totalOperationRecordInfo;
	/**手术记录表单号**/
	private String operationFromNo;
	/**最后修改时间**/
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class}, isNotNull =false)
	private String lastmodify;
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
	public String getDepartmentNo() {
		return departmentNo;
	}
	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(String operationDate) {
		this.operationDate = operationDate;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getOperationSite() {
		return operationSite;
	}
	public void setOperationSite(String operationSite) {
		this.operationSite = operationSite;
	}
	public String getOperationLevelName() {
		return operationLevelName;
	}
	public void setOperationLevelName(String operationLevelName) {
		this.operationLevelName = operationLevelName;
	}
	public String getOperationIncisionCategory() {
		return operationIncisionCategory;
	}
	public void setOperationIncisionCategory(String operationIncisionCategory) {
		this.operationIncisionCategory = operationIncisionCategory;
	}
	public String getAnesthesiaMethodName() {
		return anesthesiaMethodName;
	}
	public void setAnesthesiaMethodName(String anesthesiaMethodName) {
		this.anesthesiaMethodName = anesthesiaMethodName;
	}
	public String getAnesthesiaDoctorName() {
		return anesthesiaDoctorName;
	}
	public void setAnesthesiaDoctorName(String anesthesiaDoctorName) {
		this.anesthesiaDoctorName = anesthesiaDoctorName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getAssistantDoctorNameA() {
		return assistantDoctorNameA;
	}
	public void setAssistantDoctorNameA(String assistantDoctorNameA) {
		this.assistantDoctorNameA = assistantDoctorNameA;
	}
	public String getAssistantDoctorNameB() {
		return assistantDoctorNameB;
	}
	public void setAssistantDoctorNameB(String assistantDoctorNameB) {
		this.assistantDoctorNameB = assistantDoctorNameB;
	}
	public String getAssistantDoctorNameC() {
		return assistantDoctorNameC;
	}
	public void setAssistantDoctorNameC(String assistantDoctorNameC) {
		this.assistantDoctorNameC = assistantDoctorNameC;
	}
	public String getPreoperativeDiagnosis() {
		return preoperativeDiagnosis;
	}
	public void setPreoperativeDiagnosis(String preoperativeDiagnosis) {
		this.preoperativeDiagnosis = preoperativeDiagnosis;
	}
	public String getIntraoperativeDiagnosis() {
		return intraoperativeDiagnosis;
	}
	public void setIntraoperativeDiagnosis(String intraoperativeDiagnosis) {
		this.intraoperativeDiagnosis = intraoperativeDiagnosis;
	}
	public String getOperationProcess() {
		return operationProcess;
	}
	public void setOperationProcess(String operationProcess) {
		this.operationProcess = operationProcess;
	}
	public String getTotalOperationRecordInfo() {
		return totalOperationRecordInfo;
	}
	public void setTotalOperationRecordInfo(String totalOperationRecordInfo) {
		this.totalOperationRecordInfo = totalOperationRecordInfo;
	}
	public String getOperationFromNo() {
		return operationFromNo;
	}
	public void setOperationFromNo(String operationFromNo) {
		this.operationFromNo = operationFromNo;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}
	
	
	
	
}
