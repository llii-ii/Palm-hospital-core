package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO 生成患者二维码请求对象
 */
public class ReqCreatePatientQRCode extends AbsReq {

	/**卡类型*/
	private String cardType;
	/**卡号*/
	private String cardNo;
	/**卡类型名称*/
	private String cardTypeName;
	/**姓名*/
	private String name;
	/**手机号*/
	private String mobile;
	/**性别*/
	private Integer sex;
	/**身份证号*/
	private String idCardNo;
	/**出生日期*/
	private String birthDate;
	/**地址*/
	private String address;
	/**医保卡号*/
	private String mcardNo;
	/**出生号码*/
	private String birthNumber;
	/**是否儿童*/
	private Integer isChildren;
	/**
	 * his就诊人唯一ID
	 */
	private String hisMemberId;
	/**
	 * 二维码使用类型
	 */
	private String usageType;
	
	
	public String getHisMemberId() {
		return hisMemberId;
	}


	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}


	public String getUsageType() {
		return usageType;
	}


	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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


	public Integer getSex() {
		return sex;
	}


	public void setSex(Integer sex) {
		this.sex = sex;
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqCreatePatientQRCode(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.name=XMLUtil.getString(dataEl, "Name", true);
		this.mobile = XMLUtil.getString(dataEl, "Mobile", true);
		this.sex=XMLUtil.getInt(dataEl, "Sex", false);
		this.cardType = XMLUtil.getString(dataEl, "CardType", true);
		this.cardTypeName = XMLUtil.getString(dataEl, "CardTypeName", true);
		this.idCardNo = XMLUtil.getString(dataEl, "IdCardNo", true);
		this.birthDate=XMLUtil.getString(dataEl, "BirthDate", false);
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.address = XMLUtil.getString(dataEl, "Address", false);
		this.mcardNo=XMLUtil.getString(dataEl, "McardNo", false);
		this.birthNumber = XMLUtil.getString(dataEl, "BirthNumber", false);
		this.isChildren=XMLUtil.getInt(dataEl, "IsChildren", false);
		this.usageType=XMLUtil.getString(dataEl, "UsageType", true);
		this.hisMemberId=XMLUtil.getString(dataEl, "HisMemberId", false);
	}

}
