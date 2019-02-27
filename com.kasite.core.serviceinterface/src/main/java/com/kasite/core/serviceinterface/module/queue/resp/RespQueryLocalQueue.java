package com.kasite.core.serviceinterface.module.queue.resp;

import java.util.Date;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryLocalQueue
 * @author: lcz
 * @date: 2018年8月6日 上午10:17:10
 */
public class RespQueryLocalQueue extends AbsResp{
	private Date registerDate;
	private String timeId;
	private String location;
	/**卡类型*/
	private String cardType;
	/**卡号*/
	private String cardNo;
	private String patientName;
	private String phoneNo;
	private String deptName;
	private String doctorName;
	private String no;
	private String maxNo;
	private String nextNo;
	private String queryId;
	private String ifMsg;
	private String reMindNo;
	private String channelId;
	private Integer msgStatus;
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getMaxNo() {
		return maxNo;
	}
	public void setMaxNo(String maxNo) {
		this.maxNo = maxNo;
	}
	public String getNextNo() {
		return nextNo;
	}
	public void setNextNo(String nextNo) {
		this.nextNo = nextNo;
	}
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public String getIfMsg() {
		return ifMsg;
	}
	public void setIfMsg(String ifMsg) {
		this.ifMsg = ifMsg;
	}
	public String getReMindNo() {
		return reMindNo;
	}
	public void setReMindNo(String reMindNo) {
		this.reMindNo = reMindNo;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getMsgStatus() {
		return msgStatus;
	}
	public void setMsgStatus(Integer msgStatus) {
		this.msgStatus = msgStatus;
	}
	
	
	
}
