package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * 
 * @className: ReqMCopyUser
 * @author: cjy
 * @date: 2018年9月18日 上午10:20:19
 */
public class ReqMCopyUser extends AbsReq{

	private String id;//主键
	private String name;//姓名
	private String cardNo;//卡号
	private String gender;//性别
	private String idType;//证件类型2：就诊卡
	private String idCode;//就诊卡号
	private String birthday;//出生日期，yyyy-MM-dd
	private String patientId;//病案号，又叫病人唯一号
	private String chargeType;//费用类别，为空
	private String phone;//手机号，建档时留的
	private String idCard;//就诊人身份，为空
	private String balance;	//就诊卡余额，0.00
	private String outPara1; //年龄，3位数字如062
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdCode() {
		return idCode;
	}
	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getOutPara1() {
		return outPara1;
	}
	public void setOutPara1(String outPara1) {
		this.outPara1 = outPara1;
	}
	

	
	public ReqMCopyUser(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id=XMLUtil.getString(ser, "id", false);
		this.name=XMLUtil.getString(ser, "name", false);
		this.cardNo=XMLUtil.getString(ser, "cardNo", false);
		this.gender=XMLUtil.getString(ser, "gender", false);
		this.idType=XMLUtil.getString(ser, "idType", false);
		this.idCode=XMLUtil.getString(ser, "idCode", false);
		this.birthday=XMLUtil.getString(ser, "birthday", false);
		this.patientId=XMLUtil.getString(ser, "patientId", false);
		this.chargeType=XMLUtil.getString(ser, "chargeType", false);
		this.phone=XMLUtil.getString(ser, "phone", false);
		this.idCard=XMLUtil.getString(ser, "idCard", false);
		this.balance=XMLUtil.getString(ser, "balance", false);
		this.outPara1=XMLUtil.getString(ser, "outPara1", false);
	}

}
