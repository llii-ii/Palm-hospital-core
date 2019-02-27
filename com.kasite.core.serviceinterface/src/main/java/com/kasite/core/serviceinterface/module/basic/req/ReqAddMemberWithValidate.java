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
public class ReqAddMemberWithValidate extends AbsReq {
	private String mobile;
	private String cardNo;
	private String cardType;
	private String patientId;
	private String memberName;
	private String idCardNo;
	private String opId;
	private String cardTypeName;
	private String code;
	private String pCId;
	/**
	 * 是否支持新增虚拟卡  //0为不支持  1为支持
	 */
	private String isVirtualCard;
	
	
	
	
	public ReqAddMemberWithValidate(InterfaceMessage msg)throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.patientId = XMLUtil.getString(ser, "PatientId", false);
		this.mobile = XMLUtil.getString(ser, "Mobile", false);
		this.cardType = XMLUtil.getString(ser, "CardType", false);
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
		this.memberName=XMLUtil.getString(ser, "MemberName", true);
		this.idCardNo=XMLUtil.getString(ser, "IdCardNo", true);
		this.cardTypeName= XMLUtil.getString(ser, "CardTypeName", false);
		this.opId= XMLUtil.getString(ser, "OpId", false);
		this.code= XMLUtil.getString(ser, "ProvingCode", true);
		this.pCId= XMLUtil.getString(ser, "PCId", true);
		this.isVirtualCard= XMLUtil.getString(ser, "IsVirtualCard", true);
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
	public String getOpId() {
		return opId;
	}
	public void setOpId(String opId) {
		this.opId = opId;
	}
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getpCId() {
		return pCId;
	}
	public void setpCId(String pCId) {
		this.pCId = pCId;
	}
	public String getIsVirtualCard() {
		return isVirtualCard;
	}
	public void setIsVirtualCard(String isVirtualCard) {
		this.isVirtualCard = isVirtualCard;
	}
	
}
