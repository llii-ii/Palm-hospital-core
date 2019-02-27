package com.kasite.client.crawler.modules.hospitalization.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.group.AddGroup;

public class HospitalizationSummaryEntity {
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	/**病人ID patientId **/
	private String patientId;
	@NotBlank(message="住院号 inHospitalNum 不能为空", groups = {AddGroup.class})
	/**住院号 inHospitalNum **/
	private String inHospitalNum;
	@NotBlank(message="床位号 bunkId 不能为空", groups = {AddGroup.class})
	/**床位号 bunkId **/
	private String bunkId;
	@NotBlank(message="入院科室名称 deptName 不能为空", groups = {AddGroup.class})
	/**入院科室名称 deptName **/
	private String deptName;
	@NotBlank(message="入院日期时间 inHosDate 不能为空", groups = {AddGroup.class})
	@CheckDate(message="入院日期时间 inHosDate 数据格式不正确", groups = {AddGroup.class})
	/**入院日期时间 inHosDate **/
	private String inHosDate;
	@NotBlank(message="出院日期时间 outHosDate 不能为空", groups = {AddGroup.class})
	@CheckDate(message="出院日期时间 outHosDate 数据格式不正确", groups = {AddGroup.class})
	/**出院日期时间 outHosDate **/
	private String outHosDate;
	@NotBlank(message="出院结算日期 settleDate 不能为空", groups = {AddGroup.class})
	@CheckDate(message="出院结算日期 settleDate 数据格式不正确", groups = {AddGroup.class})
	/**出院结算日期 settleDate **/
	private String settleDate;
	@NotBlank(message="出院科室 deptName 不能为空", groups = {AddGroup.class})
	/**出院科室 outDeptName **/
	private String outDeptName;
	@NotBlank(message="出院原因 dischCause 不能为空", groups = {AddGroup.class})
	/**出院原因 dischCause **/
	private String dischCause;
	@NotBlank(message="出院医嘱 doctorRemark 不能为空", groups = {AddGroup.class})
	/**出院医嘱 doctorRemark **/
	private String doctorRemark;
	@NotBlank(message="实际住院天数 inHosDays 不能为空", groups = {AddGroup.class})
	/**实际住院天数 inHosDays **/
	private String inHosDays;
	@NotBlank(message="离院诊断 leaveMedicalSick 不能为空", groups = {AddGroup.class})
	/**离院诊断 leaveMedicalSick **/
	private String leaveMedicalSick;
	@NotBlank(message="检查结果 mainTestResults 不能为空", groups = {AddGroup.class})
	/**检查结果 mainTestResults **/
	private String mainTestResults;
	@NotBlank(message="总费用 totalMedicalCost 不能为空", groups = {AddGroup.class})
	@CheckCurrency(message="总费用 totalMedicalCost 数据格式不正确", groups = {AddGroup.class})
	/**总费用 totalMedicalCost **/
	private String totalMedicalCost;
	@NotBlank(message="自费金额 selfamnt 不能为空", groups = {AddGroup.class})
	@CheckCurrency(message="自费金额 selfamnt 数据格式不正确", groups = {AddGroup.class})
	/**自费金额 selfamnt **/
	private String selfamnt;
	@NotBlank(message="社保类别 socialInsurType 不能为空", groups = {AddGroup.class})
	/**社保类别 socialInsurType **/
	private String socialInsurType;
	/**社保类型原始值 originalSocialInsurType **/
	private String originalSocialInsurType;
	@NotBlank(message="社保扣除金额 insuranceDeduct 不能为空", groups = {AddGroup.class})
	@CheckCurrency(message="社保扣除金额 insuranceDeduct 数据格式不正确", groups = {AddGroup.class})
	/**社保扣除金额 insuranceDeduct **/
	private String insuranceDeduct;
	@NotBlank(message="起付线 underwayCriterion 不能为空", groups = {AddGroup.class})
	@CheckCurrency(message="起付线 underwayCriterion 数据格式不正确", groups = {AddGroup.class})
	/**起付线 underwayCriterion **/
	private String underwayCriterion;
	@NotBlank(message="基本医疗赔付金额 baseInsurance 不能为空", groups = {AddGroup.class})
	@CheckCurrency(message="基本医疗赔付金额 baseInsurance 数据格式不正确", groups = {AddGroup.class})
	/**基本医疗赔付金额 baseInsurance **/
	private String baseInsurance;
	@NotBlank(message="补充医疗赔付金额 complementarityInsurance 不能为空", groups = {AddGroup.class})
	@CheckCurrency(message="补充医疗赔付金额 complementarityInsurance 数据格式不正确", groups = {AddGroup.class})
	/**补充医疗赔付金额 complementarityInsurance **/
	private String complementarityInsurance;
	@CheckDate(message="重监病房离开日期 wardshipEndDate 数据格式不正确", groups = {AddGroup.class})
	/**重监病房离开日期 wardshipEndDate **/
	private String wardshipEndDate;
	@CheckDate(message="重监病房入住日期 wardshipStartDate 数据格式不正确", groups = {AddGroup.class})
	/**重监病房入住日期 wardshipStartDate **/
	private String wardshipStartDate;
	@NotBlank(message="诊治经过 diagnosisTreatment 不能为空", groups = {AddGroup.class})
	/**诊治经过 diagnosisTreatment **/
	private String diagnosisTreatment;
	/**出院确认医生工号 doctorId **/
	private String doctorId;
	/**出院确认医生姓名 doctorName **/
	private String doctorName;
	@NotBlank(message="出院小结 medicalAbstract 不能为空", groups = {AddGroup.class})
	/**出院小结 medicalAbstract **/
	private String medicalAbstract;
	@NotBlank(message="最后修改时间 lastmodify 不能为空", groups = {AddGroup.class})
	@CheckDate(message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class})
	/**最后修改时间 lastmodify **/
	private String lastmodify;
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getInHospitalNum() {
		return inHospitalNum;
	}
	public void setInHospitalNum(String inHospitalNum) {
		this.inHospitalNum = inHospitalNum;
	}
	public String getBunkId() {
		return bunkId;
	}
	public void setBunkId(String bunkId) {
		this.bunkId = bunkId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
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
	public String getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}
	public String getOutDeptName() {
		return outDeptName;
	}
	public void setOutDeptName(String outDeptName) {
		this.outDeptName = outDeptName;
	}
	public String getDischCause() {
		return dischCause;
	}
	public void setDischCause(String dischCause) {
		this.dischCause = dischCause;
	}
	public String getDoctorRemark() {
		return doctorRemark;
	}
	public void setDoctorRemark(String doctorRemark) {
		this.doctorRemark = doctorRemark;
	}
	public String getInHosDays() {
		return inHosDays;
	}
	public void setInHosDays(String inHosDays) {
		this.inHosDays = inHosDays;
	}
	public String getLeaveMedicalSick() {
		return leaveMedicalSick;
	}
	public void setLeaveMedicalSick(String leaveMedicalSick) {
		this.leaveMedicalSick = leaveMedicalSick;
	}
	public String getMainTestResults() {
		return mainTestResults;
	}
	public void setMainTestResults(String mainTestResults) {
		this.mainTestResults = mainTestResults;
	}
	public String getTotalMedicalCost() {
		return totalMedicalCost;
	}
	public void setTotalMedicalCost(String totalMedicalCost) {
		this.totalMedicalCost = totalMedicalCost;
	}
	public String getSelfamnt() {
		return selfamnt;
	}
	public void setSelfamnt(String selfamnt) {
		this.selfamnt = selfamnt;
	}
	public String getSocialInsurType() {
		return socialInsurType;
	}
	public void setSocialInsurType(String socialInsurType) {
		this.socialInsurType = socialInsurType;
	}
	public String getOriginalSocialInsurType() {
		return originalSocialInsurType;
	}
	public void setOriginalSocialInsurType(String originalSocialInsurType) {
		this.originalSocialInsurType = originalSocialInsurType;
	}
	public String getInsuranceDeduct() {
		return insuranceDeduct;
	}
	public void setInsuranceDeduct(String insuranceDeduct) {
		this.insuranceDeduct = insuranceDeduct;
	}
	public String getUnderwayCriterion() {
		return underwayCriterion;
	}
	public void setUnderwayCriterion(String underwayCriterion) {
		this.underwayCriterion = underwayCriterion;
	}
	public String getBaseInsurance() {
		return baseInsurance;
	}
	public void setBaseInsurance(String baseInsurance) {
		this.baseInsurance = baseInsurance;
	}
	public String getComplementarityInsurance() {
		return complementarityInsurance;
	}
	public void setComplementarityInsurance(String complementarityInsurance) {
		this.complementarityInsurance = complementarityInsurance;
	}
	public String getWardshipEndDate() {
		return wardshipEndDate;
	}
	public void setWardshipEndDate(String wardshipEndDate) {
		this.wardshipEndDate = wardshipEndDate;
	}
	public String getWardshipStartDate() {
		return wardshipStartDate;
	}
	public void setWardshipStartDate(String wardshipStartDate) {
		this.wardshipStartDate = wardshipStartDate;
	}
	public String getDiagnosisTreatment() {
		return diagnosisTreatment;
	}
	public void setDiagnosisTreatment(String diagnosisTreatment) {
		this.diagnosisTreatment = diagnosisTreatment;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getMedicalAbstract() {
		return medicalAbstract;
	}
	public void setMedicalAbstract(String medicalAbstract) {
		this.medicalAbstract = medicalAbstract;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}
}
