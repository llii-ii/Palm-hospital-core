package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

public class HisQueryPatientInfoByNo extends AbsResp{
 	/**姓名*/
	@NotBlank(message="姓名name不能为空", groups = {AddGroup.class})
	private String name;

	/**病案号*/
	@NotBlank(message="病案号patientId不能为空", groups = {AddGroup.class})
	private String patientId;//病案号，又叫病人唯一号

	/**手机号*/
	@NotBlank(message="手机号phone不能为空", groups = {AddGroup.class})
	private String phone;//手机号，建档时留的
	
	private String identity;//就诊人身份，为空
	private String balance;	//就诊卡余额，0.00
	private String outPara1; //年龄，3位数字如062
	private String outPara2; //身份证
	private String cardNo;//卡号
	private String gender;//性别
	private String idType;//证件类型2：就诊卡
	private String idCode;//就诊卡号
	private String birthday;//出生日期，yyyy-MM-dd
	private String chargeType;//费用类别，为空
	
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
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
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
	public String getOutPara2() {
		return outPara2;
	}
	public void setOutPara2(String outPara2) {
		this.outPara2 = outPara2;
	}

	
	

	
}
