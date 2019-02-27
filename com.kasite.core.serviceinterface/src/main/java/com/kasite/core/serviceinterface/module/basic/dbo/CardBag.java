package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 卡信息表
 * @author lcz
 *
 */
@Table(name="B_CARDBAG")
public class CardBag extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys = true)
	private Long cardId;
	private String cardType;
	private String cardNo;
	private String cardTypeName;
	private String hosId;
	
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
	
	
}
