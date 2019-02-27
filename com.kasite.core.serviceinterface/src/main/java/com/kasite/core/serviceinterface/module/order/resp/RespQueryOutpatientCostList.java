package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class RespQueryOutpatientCostList extends AbsResp{

	private String date;
	
	private Integer fee;
	
	private String doctor;
	
	private String dept;
	
	private String deptStation;

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

	
}
