package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

public class HisQueryMedicalRecords extends AbsResp{
//	/**病历编号*/
//	@NotBlank(message="病历编号hospitalId不能为空", groups = {AddGroup.class})
	private String hospitalId; //病历id
	
// 	/**入院科室名*/
//	@NotBlank(message="入院科室名deptName不能为空", groups = {AddGroup.class})
	private String deptName;//入院科室名
	
	private String erdat;//创建日期
	private String ertim;//创建时间
	private String falar;//病历类型
	private String inHosDate;//入院时间
	private String outHosDate;//出院时间
	private String deptCode;//入院科室代码
	private String outDeptCode;//出院科室代码
	private String outDeptName;//出院科室名
	private String isoperation;
	
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
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
	
	
	
}
