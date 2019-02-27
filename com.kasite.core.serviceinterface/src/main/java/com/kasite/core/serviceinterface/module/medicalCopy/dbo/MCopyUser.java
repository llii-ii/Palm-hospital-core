package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import java.math.BigDecimal;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 
 * @className: ExpressoOrder
 * @author: cjy
 * @date: 2018年9月13日 下午2:53:04
 */
@Table(name="TB_MCOPY_USER")
public class MCopyUser extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys=true)
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
	private String identity;//就诊人身份，为空
	private String idCard;//就诊人身份证
	private String balance;	//就诊卡余额，0.00
	private String outPara1; //年龄，3位数字如062
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
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	
	
	
}
