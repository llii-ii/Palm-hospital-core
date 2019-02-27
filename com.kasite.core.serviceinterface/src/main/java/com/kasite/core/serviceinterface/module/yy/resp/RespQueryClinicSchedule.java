package com.kasite.core.serviceinterface.module.yy.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.CheckEnum;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.serviceinterface.module.his.resp.HisSchIsHalt;
import com.kasite.core.serviceinterface.module.his.resp.HisSchIsTimeFlag;
import com.kasite.core.serviceinterface.module.his.resp.HisSchTimeId;

/**
 * 
 * @className: RespQueryClinicSchedule
 * @author: lcz
 * @date: 2018年7月24日 下午3:08:02
 */
public class RespQueryClinicSchedule extends AbsResp{
	@NotBlank(message="排班ID scheduleId 不能为空", groups = {AddGroup.class})
	private String scheduleId;
	private String deptCode;
	private String deptName;
	private Integer regType;
	private String regDate;
	@CheckEnum(message="排班状态",groups=AddGroup.class,inf=HisSchIsHalt.class)
	private Integer isHalt;
	@CheckEnum(message="排班时段",groups=AddGroup.class,inf=HisSchTimeId.class)
	private Integer timeSlice;
	private String timeSliceStr;
	private String isTimeFlagStr;
	private String isHaltStr;

	@CheckEnum(message="是否分时段",groups=AddGroup.class,inf=HisSchIsTimeFlag.class)
	private Integer isTimeFlag;
	private Integer leaveCount;
	private Integer regFee;
	private Integer treatFee;
	private Integer otherFee;
	private String workPlace;
	private String drawPoint;
	private String remark;
	private String diyJson;
	
	public String getDiyJson() {
		return diyJson;
	}
	public void setDiyJson(String diyJson) {
		this.diyJson = diyJson;
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
	public String getTimeSliceStr() {
		return timeSliceStr;
	}
	public void setTimeSliceStr(String timeSliceStr) {
		this.timeSliceStr = timeSliceStr;
	}
	public String getIsTimeFlagStr() {
		return isTimeFlagStr;
	}
	public void setIsTimeFlagStr(String isTimeFlagStr) {
		this.isTimeFlagStr = isTimeFlagStr;
	}
	public String getIsHaltStr() {
		return isHaltStr;
	}
	public void setIsHaltStr(String isHaltStr) {
		this.isHaltStr = isHaltStr;
	}
	
	
}
