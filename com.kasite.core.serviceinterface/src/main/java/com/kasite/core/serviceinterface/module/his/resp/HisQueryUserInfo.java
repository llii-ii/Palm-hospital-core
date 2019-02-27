/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author lcy
 * @Description: HIS就诊人对象
 * @version 1.0 
 * 2017-6-26上午10:47:07
 */
public class HisQueryUserInfo  extends AbsResp{
	private String respCode;
	private String transactionCode;
	private String respMessage;
	/**
	 * 就诊卡号
	 */
	private String clinicCard;
	/**
	 * 用户姓名
	 */
	private String name;
	/**
	 * 联系电话
	 */
	private String mobile;
	/**
	 * 病人id
	 */
	private String patientId;
	/**
	 * 医保卡
	 */
	private String mcardNo;
	/**
	 * 家庭住址
	 */
	private String address;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 就诊卡余额
	 */
	private Integer fee;
	/**
	 * 身份证
	 */
	private String idCardId;
	/**
	 * 身份证
	 */
	private String cardNo;
	
	
	
	
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	public String getClinicCard() {
		return clinicCard;
	}
	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getMcardNo() {
		return mcardNo;
	}
	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public String getIdCardId() {
		return idCardId;
	}
	public void setIdCardId(String idCardId) {
		this.idCardId = idCardId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	

	
}
