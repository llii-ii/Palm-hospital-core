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
public class HisQueryExamItemList  extends AbsResp{
	private String hisKey;//单据号
	private String date;//开单时间
	private String labName;//项目名称
	private String friendlyReminder;//注意事项
	private String printAppointmentID;//预约id
	private String appointmentStatus;//预约状态
	private String appointmentTime;//预约时间
	private String isBook;//项目是否可预约 1可以
	private String isNeedServerCenter;//是否需要预约中心操作（0为不需要，1为需要。当为1时则接口无法进行预约操作）
	private String labPrice;//检查项目单价
	private String orderID;//订单
	private String billDept;//开单科室
	private String examDept;//检查科室
	private String labCode;//项目代码
	private String store;
	
	
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getHisKey() {
		return hisKey;
	}
	public void setHisKey(String hisKey) {
		this.hisKey = hisKey;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public String getFriendlyReminder() {
		return friendlyReminder;
	}
	public void setFriendlyReminder(String friendlyReminder) {
		this.friendlyReminder = friendlyReminder;
	}
	public String getPrintAppointmentID() {
		return printAppointmentID;
	}
	public void setPrintAppointmentID(String printAppointmentID) {
		this.printAppointmentID = printAppointmentID;
	}
	public String getAppointmentStatus() {
		return appointmentStatus;
	}
	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public String getIsBook() {
		return isBook;
	}
	public void setIsBook(String isBook) {
		this.isBook = isBook;
	}
	public String getIsNeedServerCenter() {
		return isNeedServerCenter;
	}
	public void setIsNeedServerCenter(String isNeedServerCenter) {
		this.isNeedServerCenter = isNeedServerCenter;
	}
	public String getLabPrice() {
		return labPrice;
	}
	public void setLabPrice(String labPrice) {
		this.labPrice = labPrice;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getBillDept() {
		return billDept;
	}
	public void setBillDept(String billDept) {
		this.billDept = billDept;
	}
	public String getExamDept() {
		return examDept;
	}
	public void setExamDept(String examDept) {
		this.examDept = examDept;
	}
	public String getLabCode() {
		return labCode;
	}
	public void setLabCode(String labCode) {
		this.labCode = labCode;
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
