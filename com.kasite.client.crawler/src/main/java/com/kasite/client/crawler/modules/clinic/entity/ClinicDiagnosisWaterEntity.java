package com.kasite.client.crawler.modules.clinic.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.client.crawler.modules.EntityID;
import com.kasite.core.common.validator.CheckCase;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.PrivateMode;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.common.validator.group.UpdateGroup;

public class ClinicDiagnosisWaterEntity {
	/**病人ID patientId **/
	@NotBlank(message="诊断流水ID diagnosisId 不能为空", groups = {AddGroup.class})
	@EntityID
	private String diagnosisId;
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**门（急）诊号 clinicNum **/
	@NotBlank(message="门（急）诊号 clinicNum 不能为空", groups = {AddGroup.class})
	private String clinicNum;
	/**就诊流水号 medicalNum **/
	@NotBlank(message="就诊流水号 medicalNum 不能为空", groups = {AddGroup.class})
	private String medicalNum;
	/**诊断名称 diagnosisName **/
	@NotBlank(message="诊断名称 diagnosisName 不能为空", groups = {AddGroup.class})
	private String diagnosisName;
	/**诊断编码 diagnosisCode **/
	private String diagnosisCode;
	/**诊断序号 sort **/
	private String sort;
	/**诊断类型代码 diagnosisType **/
	@CheckCase(value = PrivateMode.diagnosisType, message = "诊断类型代码必须是字典值范围内", 
			groups= {AddGroup.class,UpdateGroup.class})	
	private String diagnosisType;
	@CheckDate(message="诊断日期 diagnosisDate 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**诊断日期 diagnosisDate **/
	private String diagnosisDate;
	/**签名医生 doctor **/
	private String doctor;
	/**诊断结果代码 resultCode **/
	private String resultCode;
	/**医生医院内部工号 doctorCode **/
	private String doctorCode;
	/**性质代码 diagnosisNatureCode **/
	private String diagnosisNatureCode;
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class})
	/**最后修改时间 lastmodify **/
	private String lastmodify;
	
	
	
	public String getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(String diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
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
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	public String getDiagnosisCode() {
		return diagnosisCode;
	}
	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getDiagnosisType() {
		return diagnosisType;
	}
	public void setDiagnosisType(String diagnosisType) {
		this.diagnosisType = diagnosisType;
	}
	public String getDiagnosisDate() {
		return diagnosisDate;
	}
	public void setDiagnosisDate(String diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	public String getDiagnosisNatureCode() {
		return diagnosisNatureCode;
	}
	public void setDiagnosisNatureCode(String diagnosisNatureCode) {
		this.diagnosisNatureCode = diagnosisNatureCode;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}


	 

}
