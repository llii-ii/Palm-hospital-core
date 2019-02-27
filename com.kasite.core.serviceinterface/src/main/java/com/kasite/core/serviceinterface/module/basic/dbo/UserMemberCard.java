package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

@Table(name="B_USER_MEMBER_CARD")
public class UserMemberCard extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys=true)
	private Long id;
	private String openId;
	private String memberId;
	private Long cardId;
	private Integer isDefault;
	private String hisMemberId;
	private String hosId;
	
	@Transient
	private String cardType;
	@Transient
	private String cardNo;
	@Transient
	private String cardTypeName;
	
	
	/**
	 * @return the hosId
	 */
	public String getHosId() {
		return hosId;
	}
	/**
	 * @param hosId the hosId to set
	 */
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	/**
	 * @return the cardTypeName
	 */
	public String getCardTypeName() {
		return cardTypeName;
	}
	/**
	 * @param cardTypeName the cardTypeName to set
	 */
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}
	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	/**
	 * @return the hisMemberId
	 */
	public String getHisMemberId() {
		return hisMemberId;
	}
	/**
	 * @param hisMemberId the hisMemberId to set
	 */
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}
	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the cardId
	 */
	public Long getCardId() {
		return cardId;
	}
	/**
	 * @param cardId the cardId to set
	 */
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
	/**
	 * @return the isDefault
	 */
	public Integer getIsDefault() {
		return isDefault;
	}
	/**
	 * @param isDefault the isDefault to set
	 */
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	
	
	
	
	
	
	
}
