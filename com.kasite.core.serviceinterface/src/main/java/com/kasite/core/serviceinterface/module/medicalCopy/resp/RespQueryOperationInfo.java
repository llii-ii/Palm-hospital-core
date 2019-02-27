package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import com.kasite.core.common.resp.AbsResp;

public class RespQueryOperationInfo extends AbsResp{

	private String rspCode; 
	private String rspMsg; 
	private String hospitalId;//病历号
	private String operatorId;//手术号
	private String operationName;//手术名
	private String deptCode;//科室编号
	private String deptName;//科室名
	private String isOutOperation;
	public String getRspCode() {
		return rspCode;
	}
	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}
	public String getRspMsg() {
		return rspMsg;
	}
	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
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
	public String getIsOutOperation() {
		return isOutOperation;
	}
	public void setIsOutOperation(String isOutOperation) {
		this.isOutOperation = isOutOperation;
	}
	
	
}
