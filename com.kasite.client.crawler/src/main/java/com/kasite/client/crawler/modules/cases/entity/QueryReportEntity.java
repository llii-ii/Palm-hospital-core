package com.kasite.client.crawler.modules.cases.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.group.AddGroup;

/**
 * 
 * @className: QueryReportEntity
 * @author: lcz
 * @date: 2018年6月11日 下午7:44:24
 */
public class QueryReportEntity {
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**对于门诊病人，此标识就是门诊病人的挂号号，对于住院病人，此标识就是住院病人的住院号，对于健康体检人，此标识就是体检号**/
	private String eventNo;
	/**患者就诊类型的的分类名称代码**/
	private String eventType;
	
	/**文档创建时间**/
	private String startDate;
	/**检查申请单号**/
	private String labNum;
	/**报告单号**/
	private String reportNo;
	/**检查单来源**/
	private String outOrInIdentify;
	/**门诊号**/
	private String outpatientNo;
	/**住院号**/
	private String inHospitalNum;
	/**临床诊断**/
	private String clinicalDiagnosis;
	/**报告的标题**/
	private String reportTitleName;
	/**报告生成时间**/
	private String reportTime;
	/**填报者：姓名**/
	private String userName;
	/**审核者：编号**/
	private String auditDoctorCode;
	/**审核者：审核时间**/
	private String auditTime;
	/**审核者：姓名**/
	private String auditDoctorName;
	/**申请科室**/
	private String auditDept;
	/**申请科室代码**/
	private String auditDeptCode;
	/**申请医生：编号**/
	private String applyDoctorCode;
	/**申请医生：申请时间**/
	private String applyTime;
	/**申请医生：姓名**/
	private String applyDoctorName;
	/**对应的医嘱**/
	private String doctorRemark;
	/**检查的执行科室名称**/
	private String deptName;
	/**检查部位的标准编码**/
	private String inspectionPartCode;
	/**检查部位名称**/
	private String inspectionPart;
	/**检查项目的标准编码**/
	private String inspectionItemCode;
	/**检查项目名称**/
	private String inspectionItem;
	/**检查类别名称**/
	private String categoryName;
	/**检查所见**/
	private String findings;
	/**检查建议**/
	private String inspectExamResult;
	/**检查印象**/
	private String inspectImpression;
	/**病理诊断**/
	private String diagnosis;
	/**是否异常**/
	private String isException;
	/**全量检查信息**/
	private String totalRecordInfo;
	/**归档日期**/
	private String lastDate;
	/**备注**/
	private String remark;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getLabNum() {
		return labNum;
	}
	public void setLabNum(String labNum) {
		this.labNum = labNum;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getOutOrInIdentify() {
		return outOrInIdentify;
	}
	public void setOutOrInIdentify(String outOrInIdentify) {
		this.outOrInIdentify = outOrInIdentify;
	}
	public String getOutpatientNo() {
		return outpatientNo;
	}
	public void setOutpatientNo(String outpatientNo) {
		this.outpatientNo = outpatientNo;
	}
	public String getInHospitalNum() {
		return inHospitalNum;
	}
	public void setInHospitalNum(String inHospitalNum) {
		this.inHospitalNum = inHospitalNum;
	}
	public String getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}
	public void setClinicalDiagnosis(String clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
	}
	public String getReportTitleName() {
		return reportTitleName;
	}
	public void setReportTitleName(String reportTitleName) {
		this.reportTitleName = reportTitleName;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAuditDoctorCode() {
		return auditDoctorCode;
	}
	public void setAuditDoctorCode(String auditDoctorCode) {
		this.auditDoctorCode = auditDoctorCode;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditDoctorName() {
		return auditDoctorName;
	}
	public void setAuditDoctorName(String auditDoctorName) {
		this.auditDoctorName = auditDoctorName;
	}
	public String getAuditDept() {
		return auditDept;
	}
	public void setAuditDept(String auditDept) {
		this.auditDept = auditDept;
	}
	public String getAuditDeptCode() {
		return auditDeptCode;
	}
	public void setAuditDeptCode(String auditDeptCode) {
		this.auditDeptCode = auditDeptCode;
	}
	public String getApplyDoctorCode() {
		return applyDoctorCode;
	}
	public void setApplyDoctorCode(String applyDoctorCode) {
		this.applyDoctorCode = applyDoctorCode;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getApplyDoctorName() {
		return applyDoctorName;
	}
	public void setApplyDoctorName(String applyDoctorName) {
		this.applyDoctorName = applyDoctorName;
	}
	public String getDoctorRemark() {
		return doctorRemark;
	}
	public void setDoctorRemark(String doctorRemark) {
		this.doctorRemark = doctorRemark;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getInspectionPartCode() {
		return inspectionPartCode;
	}
	public void setInspectionPartCode(String inspectionPartCode) {
		this.inspectionPartCode = inspectionPartCode;
	}
	public String getInspectionPart() {
		return inspectionPart;
	}
	public void setInspectionPart(String inspectionPart) {
		this.inspectionPart = inspectionPart;
	}
	public String getInspectionItemCode() {
		return inspectionItemCode;
	}
	public void setInspectionItemCode(String inspectionItemCode) {
		this.inspectionItemCode = inspectionItemCode;
	}
	public String getInspectionItem() {
		return inspectionItem;
	}
	public void setInspectionItem(String inspectionItem) {
		this.inspectionItem = inspectionItem;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getFindings() {
		return findings;
	}
	public void setFindings(String findings) {
		this.findings = findings;
	}
	public String getInspectExamResult() {
		return inspectExamResult;
	}
	public void setInspectExamResult(String inspectExamResult) {
		this.inspectExamResult = inspectExamResult;
	}
	public String getInspectImpression() {
		return inspectImpression;
	}
	public void setInspectImpression(String inspectImpression) {
		this.inspectImpression = inspectImpression;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getIsException() {
		return isException;
	}
	public void setIsException(String isException) {
		this.isException = isException;
	}
	public String getTotalRecordInfo() {
		return totalRecordInfo;
	}
	public void setTotalRecordInfo(String totalRecordInfo) {
		this.totalRecordInfo = totalRecordInfo;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}
	
	
	
}
