package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryPatientInfo
 * @author: lcz
 * @date: 2018年7月23日 下午6:02:03
 */
public class RespQueryPatientInfo extends AbsResp{

	private String memberName;
	private String mobile;
	private String idCardNo;
	private String clinicCard;
	private String sex;
	private String birthDate;
	private String address;
	private String mcardNo;
	private String birthNumber;
	private String isChildren;
	private String opId;
	private String channelId;
	private Integer balance;
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMcardNo() {
		return mcardNo;
	}
	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}
	public String getBirthNumber() {
		return birthNumber;
	}
	public void setBirthNumber(String birthNumber) {
		this.birthNumber = birthNumber;
	}
	public String getIsChildren() {
		return isChildren;
	}
	public void setIsChildren(String isChildren) {
		this.isChildren = isChildren;
	}
	public String getOpId() {
		return opId;
	}
	public void setOpId(String opId) {
		this.opId = opId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	
	
}
