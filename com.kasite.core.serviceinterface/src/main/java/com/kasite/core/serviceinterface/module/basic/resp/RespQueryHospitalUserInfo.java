package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryHospitalUserInfo
 * @author: lcz
 * @date: 2018年7月23日 下午6:00:04
 */
public class RespQueryHospitalUserInfo extends AbsResp{
	
	private String idCardId;
	private String mobile;
	private String hospitalNo;
	private String name;
	private String deptCode;
	private String deptName;
	private String bedNo;
	private String inHospitalDate;
	private Integer inHospitalDays;
	private Integer inHospitalTotalFee;
	private Integer balance;
	/**
	 * 性别 1男 2女 0未知
	 */
	private Integer sex;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 住院流水号
	 */
	private String hospitalTransNo;
	/**
	 * 住院状态                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
	 */
	private String state;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getHospitalTransNo() {
		return hospitalTransNo;
	}
	public void setHospitalTransNo(String hospitalTransNo) {
		this.hospitalTransNo = hospitalTransNo;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getIdCardId() {
		return idCardId;
	}
	public void setIdCardId(String idCardId) {
		this.idCardId = idCardId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getHospitalNo() {
		return hospitalNo;
	}
	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getInHospitalDate() {
		return inHospitalDate;
	}
	public void setInHospitalDate(String inHospitalDate) {
		this.inHospitalDate = inHospitalDate;
	}
	public Integer getInHospitalDays() {
		return inHospitalDays;
	}
	public void setInHospitalDays(Integer inHospitalDays) {
		this.inHospitalDays = inHospitalDays;
	}
	public Integer getInHospitalTotalFee() {
		return inHospitalTotalFee;
	}
	public void setInHospitalTotalFee(Integer inHospitalTotalFee) {
		this.inHospitalTotalFee = inHospitalTotalFee;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
}
