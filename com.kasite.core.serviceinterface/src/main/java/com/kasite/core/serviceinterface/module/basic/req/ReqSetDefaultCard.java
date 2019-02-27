/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 设置默认就诊人入参类
 * @author lcy
 * @version 1.0 
 * 2017-7-4上午9:11:45
 */
public class ReqSetDefaultCard extends AbsReq{
	/**
	 * 唯一ID
	 */
	private String memberId;
	/**
	 * 用户在渠道的唯一id
	 */
	private String opId;
	/**
	 * 默认卡0为非  1为是
	 */
	private Integer isDefault;
	/**
	 * 卡类型
	 */
	private String cardType;
	/**
	 * 卡号
	 */
	private String cardNo;
	
	public ReqSetDefaultCard(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.memberId=XMLUtil.getString(ser, "MemberId", true);
		this.opId=XMLUtil.getString(ser, "OpId", false,super.getOpenId());
		this.isDefault=XMLUtil.getInt(ser, "IsDefault", true);
		this.cardType=XMLUtil.getString(ser, "CardType", true);
		this.cardNo=XMLUtil.getString(ser, "CardNo", true);
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


	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	
}
