package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 就诊人信息-数据库实体类
 * 
 * @author 無
 * @version V1.0
 * @date 2018年5月21日 下午4:58:33
 */
@Table(name="B_PATIENT")
public class Patient extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys = true)
	private String id;
	/**
	 * 成员ID
	 */
	private String memberId;
	/**
	 * 卡号
	 */
	private String cardNo;
	/**
	 * 卡类型
	 */
	private String cardType;
	/**
	 * 卡类型名称
	 */
	private String cardTypeName;
	/**
	 * 医保卡
	 */
	private String mcardNo;
	/**
	 * 出生证
	 */
	private String birthNumber;
	/**
	 * 医院id
	 */
	private String hosId;
	
	/**
	 * 医院患者ID
	 */
	private String hisMemberId;
	/**
	 * 默认卡0为非 1为是
	 */
	private Integer isDefault;
	/**
	 * 帐号ID
	 */
	private String accountId;
	/**
	 * 住院号
	 */
	private String hospitalNo;
	/**
	 * 状态值 1为正常- 1为无效
	 */
	private Integer state;
	
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
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

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}


	public String getHisMemberId() {
		return hisMemberId;
	}

	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getHospitalNo() {
		return hospitalNo;
	}

	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}

}
