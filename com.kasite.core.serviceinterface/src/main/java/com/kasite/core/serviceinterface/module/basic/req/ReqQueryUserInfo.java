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
 * 查询用户入参类
 * @author lcy
 * @version 1.0 
 * 2017-6-29下午2:01:08
 */
public class ReqQueryUserInfo extends AbsReq {
	private String mobile;
	private String cardNo;
	private String cardType;
	private String patientId;
	private String memberName;
	private String idCardNo;
	private String cardTypeName;
	private String memberId;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public ReqQueryUserInfo(InterfaceMessage msg)throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.patientId = XMLUtil.getString(ser, "PatientId", false);
		this.memberId=XMLUtil.getString(ser, "MemberId", false);
		this.mobile = XMLUtil.getString(ser, "Mobile", false);
		this.cardType = XMLUtil.getString(ser, "CardType", false);
		this.cardNo = XMLUtil.getString(ser, "CardNo", true);
		this.memberName=XMLUtil.getString(ser, "MemberName", true);
		this.idCardNo=XMLUtil.getString(ser, "IdCardNo", true);
		this.cardTypeName=XMLUtil.getString(ser, "CardTypeName", false);
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	
}
