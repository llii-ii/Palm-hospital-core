package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

/**
 * @author linjf TODO
 */
public class HisQueryInHospitalCostType extends AbsResp {
	
	@NotBlank(message="日期[date]不能为空", groups = {AddGroup.class})
	private String date;
	private Integer fee;
	private String doctor;
	private String dept;
	private String deptStation;
	@NotBlank(message="费用类型代码[expenseTypeCode]不能为空", groups = {AddGroup.class})
	private String expenseTypeCode;
	@NotBlank(message="费用类型名称[expenseTypeName]不能为空", groups = {AddGroup.class})
	private String expenseTypeName;


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDeptStation() {
		return deptStation;
	}

	public void setDeptStation(String deptStation) {
		this.deptStation = deptStation;
	}

	public String getExpenseTypeCode() {
		return expenseTypeCode;
	}

	public void setExpenseTypeCode(String expenseTypeCode) {
		this.expenseTypeCode = expenseTypeCode;
	}

	public String getExpenseTypeName() {
		return expenseTypeName;
	}

	public void setExpenseTypeName(String expenseTypeName) {
		this.expenseTypeName = expenseTypeName;
	}

}
