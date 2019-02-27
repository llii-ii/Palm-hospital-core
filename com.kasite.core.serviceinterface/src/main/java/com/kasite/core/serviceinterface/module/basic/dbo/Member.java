package com.kasite.core.serviceinterface.module.basic.dbo;


import javax.persistence.Id;
import javax.persistence.Transient;

import com.kasite.core.common.bean.dbo.BaseDbo;
/**
 * 成员和就诊信息
 * @author lcy
 * @version 1.0 
 * 2017-6-19下午6:18:37
 */
public class Member extends BaseDbo{
	
	@Id
	private String memberId;
	/**
	 * 姓名
	 */
	private String memberName;
	
	/**
	 * 姓名like查询使用
	 */
	@Transient
	private String memberNameLike;
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
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 身份证号
	 */
	private String idCardNo;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 出生日期
	 */
	private String birthDate;
	/**
	 * 地址
	 */
	private String address;
//	/**
//	 * 医保卡
//	 */
//	private String mcardNo;
//	/**
//	 * 出生证
//	 */
//	private String birthNumber;
	/**
	 * 是否小孩
	 */
	private Integer isChildren;
	/**
	 * 用户在渠道的唯一id
	 */
	private String openId;
	/**
	 * 渠道Id
	 */
	private String channelId;
	/**
	 * 余额
	 */
	private Integer balance;
	/**
	 * 医院id
	 */
	private String hosId;
//	/**
//	 * 状态 1为正常 -1为无效
//	 */
//	private Integer state;
	/**
	 * 医院患者ID
	 */
	private String hisMemberId;
	/**
	 * 默认卡0为非  1为是
	 */
	private Integer isDefault;
	/**
	 * 默认就诊人0为非  1为是
	 */
	private Integer isDefaultMember;
	/**
	 * 名族
	 */
	private String nation;
//	/**
//	 * 帐号ID
//	 */
//	private String accountId;
	/**
	 * 住院号
	 */
	private String hospitalNo;
	
	/**
	 *  证件类型  01、居民身份证，02、居民户口簿，03、护照，04、军官证，05、驾驶证，06、港澳居民来往内地通行证，07、台湾居民来往内地通行证，08、士兵证，09、返乡证，10、组织机构代码，11、港澳通行证，12、台湾通行证，13、户口簿，14、学生证，15、国际海员证，16、外国人永久居留证，17、旅行证，18、警官证，19、微信号，20、港澳居民来往内地通行证，21、台胞证，22、电子就医码 23、社会保障号码  99、其他法定有效证件
	 */
	private String certType;
	/**
	 * 证件号码
	 */
	private String certNum;
	/**
	 * 监护人姓名
	 */
	private String guardianName;
	/**
	 * 监护人性别
	 */
	private Integer guardianSex;
	/**
	 * 监护人证件类型
	 */
	private String guardianCertType;
	/**
	 * 监护人证件号码
	 */
	private String guardianCertNum;
	
	
	/**
	 * @return the isDefaultMember
	 */
	public Integer getIsDefaultMember() {
		return isDefaultMember;
	}
	/**
	 * @param isDefaultMember the isDefaultMember to set
	 */
	public void setIsDefaultMember(Integer isDefaultMember) {
		this.isDefaultMember = isDefaultMember;
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
	public String getMemberNameLike() {
		return memberNameLike;
	}
	public void setMemberNameLike(String memberNameLike) {
		this.memberNameLike = memberNameLike;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
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
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
//	public String getMcardNo() {
//		return mcardNo;
//	}
//	public void setMcardNo(String mcardNo) {
//		this.mcardNo = mcardNo;
//	}
//	public String getBirthNumber() {
//		return birthNumber;
//	}
//	public void setBirthNumber(String birthNumber) {
//		this.birthNumber = birthNumber;
//	}
	public Integer getIsChildren() {
		return isChildren;
	}
	public void setIsChildren(Integer isChildren) {
		this.isChildren = isChildren;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
//	public Integer getState() {
//		return state;
//	}
//	public void setState(Integer state) {
//		this.state = state;
//	}
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
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
//	public String getAccountId() {
//		return accountId;
//	}
//	public void setAccountId(String accountId) {
//		this.accountId = accountId;
//	}
	public String getHospitalNo() {
		return hospitalNo;
	}
	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}
}
