package com.kasite.client.yy.bean.dbo;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 数据库实体类(锁号)
 * 
 * @author lsq version 1.0 2017-7-4下午4:58:13
 */
@Table(name="YY_LOCK")
public class YyLock extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys = true)
	private String orderId;
	private String scheduleId;
	private String sourceCode;
	private String hislockOrderId;
	private String hosId;
	private String cardNo;
	private Integer cardTypeCode;
	private Timestamp invalidDate;
	private String doctorCode;
	private String doctorName;
	private String deptCode;
	private String deptName;
	private String commendTime;
	private Integer sqNo;
	private String registerDate;
	private Integer timeId;
	private String openId;
	private String memberId;
	private String store;
	
	
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	public String getHislockOrderId() {
		return hislockOrderId;
	}
	public void setHislockOrderId(String hislockOrderId) {
		this.hislockOrderId = hislockOrderId;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public Integer getTimeId() {
		return timeId;
	}
	public void setTimeId(Integer timeId) {
		this.timeId = timeId;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Integer getCardTypeCode() {
		return cardTypeCode;
	}
	public void setCardTypeCode(Integer cardTypeCode) {
		this.cardTypeCode = cardTypeCode;
	}
	public Timestamp getInvalidDate() {
		return invalidDate;
	}
	public void setInvalidDate(Timestamp invalidDate) {
		this.invalidDate = invalidDate;
	}
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
	public String getCommendTime() {
		return commendTime;
	}
	public void setCommendTime(String commendTime) {
		this.commendTime = commendTime;
	}
	public Integer getSqNo() {
		return sqNo;
	}
	public void setSqNo(Integer sqNo) {
		this.sqNo = sqNo;
	}


	
}
