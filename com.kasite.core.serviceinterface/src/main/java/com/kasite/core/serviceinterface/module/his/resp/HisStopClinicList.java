/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**查询停诊信息出参
 * @author lsq
 * @version 1.0 
 * 2017-6-29下午3:51:35
 */
public class HisStopClinicList  extends AbsResp{
	/**排班id*/
	private String scheduleId;
	/**科室代码*/
	private String deptCode;
	/**科室名称*/
	private String deptName;
	/**出诊日期*/
	private String regDate;
	/**班次：0全天，1上午，2下午*/
	private Integer timeSlice;
	/**1:出诊、2:停诊、3:替诊、4:可约、5:可挂、6:申请*/
	private Integer isHalt;
	/**医生工号**/
	private String doctorCode;
	/**医生名称**/
	private String doctorName;
	/**号源编码**/
	private String sourceCode;
	/**his预约订单号**/
	private String hisOrderId;
	/** 备注信息  */
	private String remark;
	
	
	public Integer getIsHalt() {
		return isHalt;
	}
	public void setIsHalt(Integer isHalt) {
		this.isHalt = isHalt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the scheduleId
	 */
	public String getScheduleId() {
		return scheduleId;
	}
	/**
	 * @param scheduleId the scheduleId to set
	 */
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	/**
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}
	/**
	 * @param deptCode the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * @return the regDate
	 */
	public String getRegDate() {
		return regDate;
	}
	/**
	 * @param regDate the regDate to set
	 */
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	/**
	 * @return the timeSlice
	 */
	public Integer getTimeSlice() {
		return timeSlice;
	}
	/**
	 * @param timeSlice the timeSlice to set
	 */
	public void setTimeSlice(Integer timeSlice) {
		this.timeSlice = timeSlice;
	}
	/**
	 * @return the doctorCode
	 */
	public String getDoctorCode() {
		return doctorCode;
	}
	/**
	 * @param doctorCode the doctorCode to set
	 */
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	/**
	 * @return the doctorName
	 */
	public String getDoctorName() {
		return doctorName;
	}
	/**
	 * @param doctorName the doctorName to set
	 */
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	/**
	 * @return the sourceCode
	 */
	public String getSourceCode() {
		return sourceCode;
	}
	/**
	 * @param sourceCode the sourceCode to set
	 */
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	/**
	 * @return the hisOrderId
	 */
	public String getHisOrderId() {
		return hisOrderId;
	}
	/**
	 * @param hisOrderId the hisOrderId to set
	 */
	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}
	
	
	
}
