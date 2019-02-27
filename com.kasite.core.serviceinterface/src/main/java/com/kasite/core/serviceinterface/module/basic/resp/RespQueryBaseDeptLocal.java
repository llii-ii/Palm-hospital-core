package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryBaseDeptLocal
 * @author: lcz
 * @date: 2018年7月23日 上午11:02:47
 */
public class RespQueryBaseDeptLocal extends AbsResp{
	
	private String deptCode;
	private String deptName;
	private String parentId;
	private String remark;
	private String intro;
	private String address;
	
	/**
	 * 排序
	 */
	private Integer orderCol;
	
	/**
	 * 科室电话
	 */
	private String deptTel;
	/**
	 * 科室类别
	 */
	private Integer deptType;
	
	public Integer getDeptType() {
		return deptType;
	}

	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
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
	
	public String getParentId() {
		return parentId;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getIntro() {
		return intro;
	}
	
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getOrderCol() {
		return orderCol;
	}
	
	public void setOrderCol(Integer orderCol) {
		this.orderCol = orderCol;
	}
	
	public String getDeptTel() {
		return deptTel;
	}
	
	public void setDeptTel(String deptTel) {
		this.deptTel = deptTel;
	}
}
