/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**取消预约出参
 * @author lsq
 * version 1.0
 * 2017-7-7下午3:00:04
 */
public class HisGetAppointReceiptInfo  extends AbsResp{
	private String serialNo;//预约卡号
	private String startAppointmentTime;//预约开始时间
	private String endAppointmentTime;//预约结束时间
	private String departmentName;//执行科室
	private String address;//地址
	private String reminderMsg;//注意事项
	private String hisName;//项目名
	private String enqueueNum;//排队号
	private String patientName;//排队号
	
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getStartAppointmentTime() {
		return startAppointmentTime;
	}
	public void setStartAppointmentTime(String startAppointmentTime) {
		this.startAppointmentTime = startAppointmentTime;
	}
	public String getEndAppointmentTime() {
		return endAppointmentTime;
	}
	public void setEndAppointmentTime(String endAppointmentTime) {
		this.endAppointmentTime = endAppointmentTime;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getReminderMsg() {
		return reminderMsg;
	}
	public void setReminderMsg(String reminderMsg) {
		this.reminderMsg = reminderMsg;
	}
	public String getHisName() {
		return hisName;
	}
	public void setHisName(String hisName) {
		this.hisName = hisName;
	}
	public String getEnqueueNum() {
		return enqueueNum;
	}
	public void setEnqueueNum(String enqueueNum) {
		this.enqueueNum = enqueueNum;
	}
	/**返回信息*/
	private String respCode;
	private String respMessage;

	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
}
