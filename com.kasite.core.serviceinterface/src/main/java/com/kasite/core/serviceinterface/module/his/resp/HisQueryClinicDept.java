package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

/**排班科室出参
 * @author lsq
 * version 1.0
 * 2017-6-21上午11:16:21
 */
public class HisQueryClinicDept  extends AbsResp{
	/**科室编码*/
	@NotBlank(message="科室编码 deptCode 不能为空", groups = {AddGroup.class})
	private String deptCode;
	/**科室名称*/
	@NotBlank(message="科室名称 deptName 不能为空", groups = {AddGroup.class})
	private String deptName;
	/**上级科室代码 -1表示没有上级科室*/
	private String parentID;
	/**科室简介*/
	private String intro;
	/**备注*/
	private String remark;
	/**科室位置*/
	private String address;
	/**当天对应的出诊医生数*/
	private Integer deptDoctors;
	/**是否可以预约，1可以0不可以主要用于判断对应科室下是否还有号源*/
	private Integer canAppoint;
	
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	public Integer getCanAppoint() {
		return canAppoint;
	}
	public void setCanAppoint(Integer canAppoint) {
		this.canAppoint = canAppoint;
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
	public String getParientID() {
		return parentID;
	}
	public void setParientID(String parientID) {
		this.parentID = parientID;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getDeptDoctors() {
		return deptDoctors;
	}
	public void setDeptDoctors(Integer deptDoctors) {
		this.deptDoctors = deptDoctors;
	}
	
	
}
