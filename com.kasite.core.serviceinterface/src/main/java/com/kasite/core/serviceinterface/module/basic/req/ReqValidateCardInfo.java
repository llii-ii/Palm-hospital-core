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
 * 	验证卡信息
 * @author lcz
 */
public class ReqValidateCardInfo extends AbsReq {
	private String opId;
	private String hosId;
	private String memberName;
	private String mobile;
	private String idCardNo ;
	private String cardNo;
	private String cardType;
	private String cardTypeName;
	private String memberId;
	
	
	public ReqValidateCardInfo(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.opId = XMLUtil.getString(ser, "OpId", false,super.getOpenId());
		this.hosId = XMLUtil.getString(ser, "HosId", false,super.getHosId());
		this.memberName=XMLUtil.getString(ser, "MemberName", true);
		this.mobile = XMLUtil.getString(ser, "Mobile", true);
		this.cardType = XMLUtil.getString(ser, "CardType", false);
		this.cardTypeName = XMLUtil.getString(ser, "CardTypeName", false);
		this.idCardNo = XMLUtil.getString(ser, "IdCardNo", false);
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
		this.memberId = XMLUtil.getString(ser, "MemberId", false);
	}


	public ReqValidateCardInfo(InterfaceMessage msg, String opId, String hosId, String memberName, String mobile,
			String idCardNo, String cardNo, String cardType, String cardTypeName, String memberId)
			throws AbsHosException {
		super(msg);
		this.opId = opId;
		this.hosId = hosId;
		this.memberName = memberName;
		this.mobile = mobile;
		this.idCardNo = idCardNo;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.cardTypeName = cardTypeName;
		this.memberId = memberId;
	}


	/**
	 * @return the opId
	 */
	public String getOpId() {
		return opId;
	}


	/**
	 * @param opId the opId to set
	 */
	public void setOpId(String opId) {
		this.opId = opId;
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


	/**
	 * @return the memberName
	 */
	public String getMemberName() {
		return memberName;
	}


	/**
	 * @param memberName the memberName to set
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}


	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	/**
	 * @return the idCardNo
	 */
	public String getIdCardNo() {
		return idCardNo;
	}


	/**
	 * @param idCardNo the idCardNo to set
	 */
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
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



	




	

}
