/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
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
public class ReqAddMember extends AbsReq {
	private String opId;
	private String hosId;
	private String memberName;
	private String mobile;
	private String idCardNo ;
	private String birthDate;
	private String channelId;
	private Integer sex;
	private String cardNo;
	private String cardType;
	private String cardTypeName;
	private String address;
	private String mcardNo;
	private String birthNumber;
	private Integer isChildren;
	private String pCId;
	private String code;
	private String hisMemberId;
	private String certType;
	private String certNum;
	private String guardianName;
	private Integer guardianSex;
	private String guardianCertType;
	private String guardianCertNum;
	/**
	 * 是否需要校验卡是否有效 默认是 true 如果设置成false 则不校验
	 * 对应前端传入为IsValidateCard 是否验证卡信息  1是（默认）   2否
	 * */
	private Boolean isValidateCard = true;
	
	public Boolean getIsValidateCard() {
		return isValidateCard;
	}

	public void setIsValidateCard(Boolean isValidateCard) {
		this.isValidateCard = isValidateCard;
	}
	/**
	 * 是否支持新增虚拟卡  0为不支持  1为支持
	 */
	private String isVirtualCard;
	private String hospitalNo;
	
	
	public ReqAddMember(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.opId = XMLUtil.getString(ser, "OpId", false,super.getOpenId());
		this.hosId = XMLUtil.getString(ser, "HosId", false,super.getHosId());
		this.memberName=XMLUtil.getString(ser, "MemberName", true);
		this.mobile = XMLUtil.getString(ser, "Mobile", true);
		this.channelId = XMLUtil.getString(ser, "ChannelId", false,super.getClientId());
		this.sex=XMLUtil.getInt(ser, "Sex", false);
		this.cardType = XMLUtil.getString(ser, "CardType", false);
		this.cardTypeName = XMLUtil.getString(ser, "CardTypeName", false);
		this.idCardNo = XMLUtil.getString(ser, "IdCardNo", false);
		this.birthDate=XMLUtil.getString(ser, "BirthDate", false);
		this.cardNo = XMLUtil.getString(ser, "CardNo", false);
		this.address = XMLUtil.getString(ser, "Address", false);
		this.mcardNo=XMLUtil.getString(ser, "McardNo", false);
		this.birthNumber = XMLUtil.getString(ser, "BirthNumber", false);
		this.isChildren=XMLUtil.getInt(ser, "IsChildren", false);
		this.pCId=XMLUtil.getString(ser, "PCId", false);
		this.code=XMLUtil.getString(ser, "ProvingCode", false);
		this.isVirtualCard=XMLUtil.getString(ser, "IsVirtualCard", false);
		this.hisMemberId=XMLUtil.getString(ser, "HisMemberId", false);
		this.hospitalNo=XMLUtil.getString(ser, "HospitalNo", false);
		this.certType = XMLUtil.getString(ser, "CertType", false);
		this.certNum = XMLUtil.getString(ser, "CertNum", false);
		this.guardianName = XMLUtil.getString(ser, "GuardianName", false);
		this.guardianCertType = XMLUtil.getString(ser, "GuardianCertType", false);
		this.guardianCertNum = XMLUtil.getString(ser, "GuardianCertNum", false);
		this.guardianSex = XMLUtil.getInt(ser, "GuardianSex", false);
		//IsValidateCard 是否验证卡信息  1是  默认为1   2否
		this.isValidateCard = (XMLUtil.getInt(ser, "IsValidateCard", false,1)==1);
		if(this.isChildren==null || this.isChildren==-1) {
			if(StringUtil.isBlank(this.certType) || StringUtil.isBlank(this.certNum)) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"证件类型及证件号码不能为空。");
			}
		}else if(this.isChildren==1 && StringUtil.isBlank(this.certNum)){
			if(StringUtil.isBlank(this.guardianCertType) || StringUtil.isBlank(this.guardianCertNum)) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"儿童无证件类型时，监护人的证件类型及证件号码不能为空。");
			}
		}
	}
	
	
	

	




	/**
	 * @Title: ReqAddMember
	 * @Description: 
	 * @param msg
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
	 * @param pCId
	 * @param code
	 * @param hisMemberId
	 * @param certType
	 * @param certNum
	 * @param guardianName
	 * @param guardianSex
	 * @param guardianCertType
	 * @param guardianCertNum
	 * @param isVirtualCard
	 * @param hospitalNo
	 * @throws AbsHosException
	 */
	public ReqAddMember(InterfaceMessage msg, String opId, String hosId, String memberName, String mobile, String idCardNo, String birthDate, String channelId, Integer sex, String cardNo, String cardType, String cardTypeName, String address, String mcardNo, String birthNumber, Integer isChildren, String pCId, String code, String hisMemberId, String certType, String certNum, String guardianName, Integer guardianSex, String guardianCertType, String guardianCertNum, String isVirtualCard, String hospitalNo) throws AbsHosException {
		super(msg);
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
		this.pCId = pCId;
		this.code = code;
		this.hisMemberId = hisMemberId;
		this.certType = certType;
		this.certNum = certNum;
		this.guardianName = guardianName;
		this.guardianSex = guardianSex;
		this.guardianCertType = guardianCertType;
		this.guardianCertNum = guardianCertNum;
		this.isVirtualCard = isVirtualCard;
		this.hospitalNo = hospitalNo;
	}









	/**
	 * @return the guardianSex
	 */
	public Integer getGuardianSex() {
		return guardianSex;
	}









	/**
	 * @param guardianSex the guardianSex to set
	 */
	public void setGuardianSex(Integer guardianSex) {
		this.guardianSex = guardianSex;
	}









	/**
	 * @return the certType
	 */
	public String getCertType() {
		return certType;
	}


	/**
	 * @param certType the certType to set
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}


	/**
	 * @return the certNum
	 */
	public String getCertNum() {
		return certNum;
	}


	/**
	 * @param certNum the certNum to set
	 */
	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}


	/**
	 * @return the guardianName
	 */
	public String getGuardianName() {
		return guardianName;
	}


	/**
	 * @param guardianName the guardianName to set
	 */
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}


	/**
	 * @return the guardianCertType
	 */
	public String getGuardianCertType() {
		return guardianCertType;
	}


	/**
	 * @param guardianCertType the guardianCertType to set
	 */
	public void setGuardianCertType(String guardianCertType) {
		this.guardianCertType = guardianCertType;
	}


	/**
	 * @return the guardianCertNum
	 */
	public String getGuardianCertNum() {
		return guardianCertNum;
	}


	/**
	 * @param guardianCertNum the guardianCertNum to set
	 */
	public void setGuardianCertNum(String guardianCertNum) {
		this.guardianCertNum = guardianCertNum;
	}
	public String getHospitalNo() {
		return hospitalNo;
	}
	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
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
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
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
	public String getpCId() {
		return pCId;
	}
	public void setpCId(String pCId) {
		this.pCId = pCId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIsVirtualCard() {
		return isVirtualCard;
	}
	public void setIsVirtualCard(String isVirtualCard) {
		this.isVirtualCard = isVirtualCard;
	}

}
