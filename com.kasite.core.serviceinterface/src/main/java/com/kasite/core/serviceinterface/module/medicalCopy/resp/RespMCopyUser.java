package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespMCopyUser
 * @author: cjy
 * @date: 2018年9月18日 上午10:20:19
 */
public class RespMCopyUser extends AbsResp{
	private String id;//主键
	private String name;//姓名
	private String cardNo;//卡号
	private String gender;//性别
	private String idType;//证件类型2：就诊卡
	private String idCode;//就诊卡号
	private String birthday;//出生日期，yyyy-MM-dd
	private String patientId;//病案号，又叫病人唯一号
	private String chargeType;//费用类别，为空
	private String phone;//手机号，建档时留的
	private String idCard;//就诊人身份，为空
	private String balance;	//就诊卡余额，0.00
	private String outPara1; //年龄，3位数字如062
	
	private boolean existsIdcard;
	private String mobileFormat;
	private String idCardFormat;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdCode() {
		return idCode;
	}
	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getOutPara1() {
		return outPara1;
	}
	public void setOutPara1(String outPara1) {
		this.outPara1 = outPara1;
	}
	public boolean isExistsIdcard() {
		return existsIdcard;
	}
	public void setExistsIdcard(boolean existsIdcard) {
		this.existsIdcard = existsIdcard;
	}
	public String getMobileFormat() {
		return mobileFormat;
	}
	public void setMobileFormat(String mobileFormat) {
		this.mobileFormat = mobileFormat;
	}
	public String getIdCardFormat() {
		return idCardFormat;
	}
	public void setIdCardFormat(String idCardFormat) {
		this.idCardFormat = idCardFormat;
	}
	
	
}
