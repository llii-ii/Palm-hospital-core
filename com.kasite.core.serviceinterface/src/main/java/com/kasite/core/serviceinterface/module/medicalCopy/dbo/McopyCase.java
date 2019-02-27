package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 
 * @className: ExpressoOrder
 * @author: cjy
 * @date: 2018年9月13日 下午2:53:04
 */
@Table(name="TB_MCOPY_CASE")
public class McopyCase extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id; //病历id
	private String erdat;//创建日期
	private String ertim;//创建时间
	private String falar;//病历类型
	private String inHosDate;//入院时间
	private String outHosDate;//出院时间
	private String deptCode;//入院科室代码
	private String deptName;//入院科室名
	private String outDeptCode;//出院科室代码
	private String outDeptName;//出院科室名
	private String isoperation;
	private String operationName;
	private String operationIds;
	private String patientId;//病案号
	private String remark;//备注
	private String inHospitalState;//出院状态0未出院1出院
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getErdat() {
		return erdat;
	}
	public void setErdat(String erdat) {
		this.erdat = erdat;
	}
	public String getErtim() {
		return ertim;
	}
	public void setErtim(String ertim) {
		this.ertim = ertim;
	}
	public String getFalar() {
		return falar;
	}
	public void setFalar(String falar) {
		this.falar = falar;
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
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getOutDeptCode() {
		return outDeptCode;
	}
	public void setOutDeptCode(String outDeptCode) {
		this.outDeptCode = outDeptCode;
	}
	public String getOutDeptName() {
		return outDeptName;
	}
	public void setOutDeptName(String outDeptName) {
		this.outDeptName = outDeptName;
	}
	public String getIsoperation() {
		return isoperation;
	}
	public void setIsoperation(String isoperation) {
		this.isoperation = isoperation;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getOperationIds() {
		return operationIds;
	}
	public void setOperationIds(String operationIds) {
		this.operationIds = operationIds;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInHospitalState() {
		return inHospitalState;
	}
	public void setInHospitalState(String inHospitalState) {
		this.inHospitalState = inHospitalState;
	}

}
