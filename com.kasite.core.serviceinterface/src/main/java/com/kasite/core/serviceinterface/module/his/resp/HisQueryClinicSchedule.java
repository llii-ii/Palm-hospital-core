/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

/**查询门诊排班出参
 * @author lsq
 * @version 1.0 
 * 2017-6-29下午3:51:35
 */
public class HisQueryClinicSchedule  extends AbsResp{
	/**排班id*/
	@NotBlank(message="排班ID scheduleId 不能为空", groups = {AddGroup.class})
	private String scheduleId;
	/**科室代码*/
	private String deptCode;
	/**科室名称*/
	private String deptName;
	/**医生工号*/
	private String doctorCode;
	/**医生名称*/
	private String doctorName;
	/**号源类型*/
	private Integer regType;
	/**出诊日期*/
	private String regDate;
	/**1:出诊、2:停诊、3:替诊、4:可约、5:可挂、6:申请*/
	private Integer isHalt;
	/**班次：0全天，1上午，2下午*/
	private Integer timeSlice;
	/**是否有分时号源数据，1有0无*/
	private Integer isTimeFlag;
	/**剩余号数*/
	private Integer leaveCount;
	/**挂号费(单位：分)*/
	private Integer regFee;
	/**诊疗费(单位：分)*/
	private Integer treatFee;
	/**其它费用(单位：分)*/
	private Integer otherFee;
	/**诊疗地点*/
	private String workPlace;
	/**取号地点*/
	private String drawPoint;
	/**备注*/
	private String remark;
	/**
	 * HIS返回的结果集
	 */
	private String store;
	/**个性化需要显示的内容 返回节点目前包含： { "scheduleStr": "排班显示内容"}*/
	private String diyJson;
	
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDiyJson() {
		return diyJson;
	}
	public void setDiyJson(String diyJson) {
		this.diyJson = diyJson;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
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
	public Integer getRegType() {
		return regType;
	}
	public void setRegType(Integer regType) {
		this.regType = regType;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public Integer getIsHalt() {
		return isHalt;
	}
	public void setIsHalt(Integer isHalt) {
		this.isHalt = isHalt;
	}
	public Integer getTimeSlice() {
		return timeSlice;
	}
	public void setTimeSlice(Integer timeSlice) {
		this.timeSlice = timeSlice;
	}
	public Integer getIsTimeFlag() {
		return isTimeFlag;
	}
	public void setIsTimeFlag(Integer isTimeFlag) {
		this.isTimeFlag = isTimeFlag;
	}
	public Integer getLeaveCount() {
		return leaveCount;
	}
	public void setLeaveCount(Integer leaveCount) {
		this.leaveCount = leaveCount;
	}
	public Integer getRegFee() {
		return regFee;
	}
	public void setRegFee(Integer regFee) {
		this.regFee = regFee;
	}
	public Integer getTreatFee() {
		return treatFee;
	}
	public void setTreatFee(Integer treatFee) {
		this.treatFee = treatFee;
	}
	public Integer getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(Integer otherFee) {
		this.otherFee = otherFee;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	public String getDrawPoint() {
		return drawPoint;
	}
	public void setDrawPoint(String drawPoint) {
		this.drawPoint = drawPoint;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
