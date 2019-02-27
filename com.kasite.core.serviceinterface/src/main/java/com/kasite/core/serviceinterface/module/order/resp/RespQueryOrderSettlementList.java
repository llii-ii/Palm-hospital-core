package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class RespQueryOrderSettlementList extends AbsResp{

	
	private String prescTime;
	
	private Integer price;
	
	private Integer isSettlement;
	
	private String hisOrderId;
	
	private String prescNo;
	
	private String prescType;
	
	private String doctorName;
	
	private String deptName;

	public String getPrescTime() {
		return prescTime;
	}

	public void setPrescTime(String prescTime) {
		this.prescTime = prescTime;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getIsSettlement() {
		return isSettlement;
	}

	public void setIsSettlement(Integer isSettlement) {
		this.isSettlement = isSettlement;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public String getPrescType() {
		return prescType;
	}

	public void setPrescType(String prescType) {
		this.prescType = prescType;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	
	
}
