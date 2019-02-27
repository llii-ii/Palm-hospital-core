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
 * 添加用户入参类
 * @author lcy
 * @version 1.0 
 * 2017-6-29下午2:01:08
 */
public class ReqUpdateMember extends AbsReq {
	
	private String memberId;
	private String opId;
	private String hosId;
	private String memberName;
	private String mobile;
	private String idCardNo ;
	private String birthDate;
	private String channelId;
	private String sex;
	private String cardNo;
	private String cardType;
	private String cardTypeName;
	private String address;
	private String mcardNo;
	private String birthNumber;
	private Integer isChildren;
	private String hisMemberId;
	private String hospitalNo;
	private String state;
	/**
	 * 是否支持新增虚拟卡  0为不支持  1为支持
	 */
	private String isVirtualCard;
	public ReqUpdateMember(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.opId = XMLUtil.getString(ser, "OpId", false);
		this.hosId = XMLUtil.getString(ser, "HosId", false);
		this.memberName=XMLUtil.getString(ser, "MemberName", false);
		this.mobile = XMLUtil.getString(ser, "Mobile", false);
		this.channelId = XMLUtil.getString(ser, "ChannelId", false);
		this.sex=XMLUtil.getString(ser, "Sex", false);
		this.cardType = XMLUtil.getString(ser, "CardType", false);
		this.cardTypeName = XMLUtil.getString(ser, "CardTypeName", false);
		this.idCardNo = XMLUtil.getString(ser, "IdCardNo", true);
		this.birthDate=XMLUtil.getString(ser, "BirthDate", false);
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
		this.address = XMLUtil.getString(ser, "Address", false);
		this.mcardNo=XMLUtil.getString(ser, "McardNo", false);
		this.birthNumber = XMLUtil.getString(ser, "BirthNumber", false);
		this.isChildren=XMLUtil.getInt(ser, "IsChildren", false);
		this.isVirtualCard=XMLUtil.getString(ser, "IsVirtualCard", false);
		this.hisMemberId=XMLUtil.getString(ser, "HisMemberId", false);
		this.memberId = XMLUtil.getString(ser, "MemberId", true);
		this.hospitalNo = XMLUtil.getString(ser, "HospitalNo", false);
		this.state = XMLUtil.getString(ser, "State", false);
	}
	
	
	/**
	 * @Title: ReqUpdateMember
	 * @Description: 
	 * @param msg
	 * @param memberId
	 * @param opId
	 * @param hosId
	 * @param memberName
	 * @param mobile
	 * @param idCardNo
	 * @param birthDate
	 * @param channelId
	 * @param sex
	 * @param cardNo
	 * @param cardType
	 * @param cardTypeName
	 * @param address
	 * @param mcardNo
	 * @param birthNumber
	 * @param isChildren
	 * @param hisMemberId
	 * @param hospitalNo
	 * @param state
	 * @param isVirtualCard
	 * @throws AbsHosException
	 */
	public ReqUpdateMember(InterfaceMessage msg, String memberId, String opId, String hosId, String memberName, String mobile, String idCardNo, String birthDate, String channelId, String sex, String cardNo, String cardType, String cardTypeName, String address, String mcardNo, String birthNumber, Integer isChildren, String hisMemberId, String hospitalNo, String state, String isVirtualCard) throws AbsHosException {
		super(msg);
		this.memberId = memberId;
		this.opId = opId;
		this.hosId = hosId;
		this.memberName = memberName;
		this.mobile = mobile;
		this.idCardNo = idCardNo;
		this.birthDate = birthDate;
		this.channelId = channelId;
		this.sex = sex;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.cardTypeName = cardTypeName;
		this.address = address;
		this.mcardNo = mcardNo;
		this.birthNumber = birthNumber;
		this.isChildren = isChildren;
		this.hisMemberId = hisMemberId;
		this.hospitalNo = hospitalNo;
		this.state = state;
		this.isVirtualCard = isVirtualCard;
	}


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getHospitalNo() {
		return hospitalNo;
	}

	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getHisMemberId() {
		return hisMemberId;
	}
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}
	public String getOpId() {
		return opId;
	}
	public void setOpId(String opId) {
		this.opId = opId;
	}
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getChannelId() {
		return this.channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public Integer getIsChildren() {
		return isChildren;
	}
	public void setIsChildren(Integer isChildren) {
		this.isChildren = isChildren;
	}
	public String getIsVirtualCard() {
		return isVirtualCard;
	}
	public void setIsVirtualCard(String isVirtualCard) {
		this.isVirtualCard = isVirtualCard;
	}

}
