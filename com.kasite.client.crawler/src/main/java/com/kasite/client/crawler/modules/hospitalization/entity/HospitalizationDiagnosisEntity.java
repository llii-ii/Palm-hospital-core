package com.kasite.client.crawler.modules.hospitalization.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.kasite.client.crawler.config.CheckDictBuser;
import com.kasite.client.crawler.modules.EntityID;
import com.kasite.core.common.validator.CheckDate;
import com.kasite.core.common.validator.CheckDict;
import com.kasite.core.common.validator.group.AddGroup;

public class HospitalizationDiagnosisEntity {
	/**病人ID patientId **/
	@NotBlank(message="病人ID patientId 不能为空", groups = {AddGroup.class})
	private String patientId;
	/**诊断流水ID diagnosisId **/
	@NotBlank(message="诊断流水ID diagnosisId 不能为空", groups = {AddGroup.class})
	@EntityID
	private String diagnosisId;
	/**住院号 inHospitalNum **/
	@NotBlank(message="住院号 inHospitalNum 不能为空", groups = {AddGroup.class})
	private String inHospitalNum;
	/**诊断名称 diagnosisName **/
	@NotBlank(message="诊断名称 diagnosisName 不能为空", groups = {AddGroup.class})
	private String diagnosisName;
	@CheckDict(inf=CheckDictBuser.class,type="diagnosisCode",isNotNull = false,message="诊断编码 “diagnosisCode” 字典值不合法",groups=AddGroup.class)
	/**诊断编码 diagnosisCode **/
	private String diagnosisCode;
	/**诊断序号 sort **/
	private String sort;
	@CheckDict(inf=CheckDictBuser.class,type="diagnosisType",isNotNull = false,message="诊断类型代码 “diagnosisType” 字典值不合法",groups=AddGroup.class)
	/**诊断类型代码 diagnosisType **/
	private String diagnosisType;
	@CheckDate(message="诊断日期 diagnosisDate 数据格式不正确", groups = {AddGroup.class} , isNotNull =true)
	/**诊断日期 diagnosisDate **/
	private String diagnosisDate;
	/**签名医生 doctor **/
	private String doctor;
	@CheckDict(inf=CheckDictBuser.class,type="resultCode",isNotNull = false,message="诊断结果代码 “resultCode” 字典值不合法",groups=AddGroup.class)
	/**诊断结果代码 resultCode **/
	private String resultCode;
	/**诊断结果 result **/
	private String result;
	/**医生医院内部工号 doctorCode **/
	private String doctorCode;
	@CheckDict(inf=CheckDictBuser.class,type="diagnosisResultCode",isNotNull = false,message="性质代码 “diagnosisResultCode” 字典值不合法",groups=AddGroup.class)
	/**性质代码 diagnosisResultCode **/
	private String diagnosisResultCode;
	@CheckDate(format="YYYY-MM-dd HH:mm:ss", message="最后修改时间 lastmodify 数据格式不正确", groups = {AddGroup.class}, isNotNull =true)
	/**最后修改时间 lastmodify **/
	private String lastmodify;
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(String diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	public String getInHospitalNum() {
		return inHospitalNum;
	}
	public void setInHospitalNum(String inHospitalNum) {
		this.inHospitalNum = inHospitalNum;
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
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	public String getDiagnosisResultCode() {
		return diagnosisResultCode;
	}
	public void setDiagnosisResultCode(String diagnosisResultCode) {
		this.diagnosisResultCode = diagnosisResultCode;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}

}
