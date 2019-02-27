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
 * 绑定就诊卡入参类
 * @author lcy
 * @version 1.0 
 * 2017-6-29下午2:01:08
 */
public class ReqBindClinicCard extends AbsReq {
	private String pCId;
	private String cardNo;
	private String cardType;
	private String cardTypeName;
	private String memberId;
	private String mobile;
	private String code;
	private String isVirtualCard;
	private String openId;
	
	public ReqBindClinicCard(InterfaceMessage msg,String pcId,String cardType,String cardNo,
			String cardTypeName,String memberId,String provingCode,String mobile,
			String isVirtualCard,String openId
			)throws AbsHosException {
		super(msg);
		this.pCId = pcId;
		this.cardType = cardType;
		this.cardNo = cardNo;
		this.cardTypeName = cardTypeName;
		this.memberId=memberId;
		this.code=provingCode;
		this.mobile=mobile;
		this.isVirtualCard=isVirtualCard;
		this.openId = openId;
	}
	public ReqBindClinicCard(InterfaceMessage msg)throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.pCId = XMLUtil.getString(ser, "PCId", false);
		this.cardType = XMLUtil.getString(ser, "CardType", true);
		this.cardNo = XMLUtil.getString(ser, "CardNo", true);
		this.cardTypeName = XMLUtil.getString(ser, "CardTypeName", true);
		this.memberId=XMLUtil.getString(ser, "MemberId", true);
		this.code=XMLUtil.getString(ser, "ProvingCode", false);
		this.mobile=XMLUtil.getString(ser, "Mobile", true);
		this.isVirtualCard=XMLUtil.getString(ser, "IsVirtualCard", false);
		this.openId=XMLUtil.getString(ser, "OpenId", false,super.getOpenId());
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
	public String getpCId() {
		return pCId;
	}

	public void setpCId(String pCId) {
		this.pCId = pCId;
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIsVirtualCard() {
		return isVirtualCard;
	}

	public void setIsVirtualCard(String isVirtualCard) {
		this.isVirtualCard = isVirtualCard;
	}


}
