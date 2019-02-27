package com.kasite.client.crawler.modules.hospitalization.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.client.crawler.config.CheckDictBuser;
import com.kasite.client.crawler.modules.EntityID;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.CheckDict;
import com.kasite.core.common.validator.group.AddGroup;

public class HospitalizationEntity {
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**住院号 inHospitalNum **/
	@NotBlank(message="住院号 inHospitalNum 不能为空", groups = {AddGroup.class})
	@EntityID()
	private String inHospitalNum;
	/**床位号 bunkId **/
	@NotBlank(message="床位号 bunkId 不能为空", groups = {AddGroup.class})
	private String bunkId;
	/**病房号 homsNum **/
//	@NotBlank(message="病房号 homsNum 不能为空", groups = {AddGroup.class})
	private String homsNum;
	/**病区名称 lesionName **/
	private String lesionName;
	/**住院次数 inHosTimes **/
	private String inHosTimes;
	/**入院科室名称 deptName **/
	@NotBlank(message="入院科室名称 deptName 不能为空", groups = {AddGroup.class})
	private String deptName;
	@CheckDate(message="入院日期时间 inHosDate 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**入院日期时间 inHosDate **/
	private String inHosDate;
	/**主诉 complained **/
//	@NotBlank(message="主诉 complained 不能为空", groups = {AddGroup.class})
	private String complained;
	/**入院科室编码 deptCod **/
	private String deptCod;
	/**就诊卡号 clinicCard **/
	private String clinicCard;
	/**就诊卡类型 clinicCardType **/
	private String clinicCardType;
	@CheckDict(inf=CheckDictBuser.class,type="certType",isNotNull = true,message="*证件类别 “certType” 字典值不合法",groups=AddGroup.class)
	/***证件类别 certType **/
	private String certType;
	/***证件号码 certNum **/
	@NotBlank(message="*证件号码 certNum 不能为空", groups = {AddGroup.class})
	private String certNum;
	@CheckDict(inf=CheckDictBuser.class,type="medicalType",isNotNull = true,message="就诊方式 “medicalType” 字典值不合法",groups=AddGroup.class)
	/**就诊方式 medicalType **/
	private String medicalType;
	/**入院诊断医生编号 inHosDoctorCode **/
	private String inHosDoctorCode;
	/**入院诊断医生姓名 inHosDoctorName **/
	private String inHosDoctorName;
	@CheckDict(inf=CheckDictBuser.class,type="clientStatus",isNotNull = false,message="患者现状 “clientStatus” 字典值不合法",groups=AddGroup.class)
	/**患者现状 clientStatus **/
	private String clientStatus;
	@CheckDict(inf=CheckDictBuser.class,type="firstVisit",isNotNull = true,message="初诊标志代码 “firstVisit” 字典值不合法",groups=AddGroup.class)
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
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class}, isNotNull =false)
	/**最后修改时间 lastmodify **/
	private String lastmodify;
	/**就诊流水号**/
	private String medicalNum;
	/**经办人**/
	private String updateBy;
	/**经办时间**/
	private String updateDate;
	
	
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getMedicalNum() {
		return medicalNum;
	}
	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
	}
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
	public String getHomsNum() {
		return homsNum;
	}
	public void setHomsNum(String homsNum) {
		this.homsNum = homsNum;
	}
	public String getLesionName() {
		return lesionName;
	}
	public void setLesionName(String lesionName) {
		this.lesionName = lesionName;
	}
	public String getInHosTimes() {
		return inHosTimes;
	}
	public void setInHosTimes(String inHosTimes) {
		this.inHosTimes = inHosTimes;
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
	public String getComplained() {
		return complained;
	}
	public void setComplained(String complained) {
		this.complained = complained;
	}
	public String getDeptCod() {
		return deptCod;
	}
	public void setDeptCod(String deptCod) {
		this.deptCod = deptCod;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	public String getClinicCardType() {
		return clinicCardType;
	}
	public void setClinicCardType(String clinicCardType) {
		this.clinicCardType = clinicCardType;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getCertNum() {
		return certNum;
	}
	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public String getInHosDoctorCode() {
		return inHosDoctorCode;
	}
	public void setInHosDoctorCode(String inHosDoctorCode) {
		this.inHosDoctorCode = inHosDoctorCode;
	}
	public String getInHosDoctorName() {
		return inHosDoctorName;
	}
	public void setInHosDoctorName(String inHosDoctorName) {
		this.inHosDoctorName = inHosDoctorName;
	}
	public String getClientStatus() {
		return clientStatus;
	}
	public void setClientStatus(String clientStatus) {
		this.clientStatus = clientStatus;
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
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}

}
