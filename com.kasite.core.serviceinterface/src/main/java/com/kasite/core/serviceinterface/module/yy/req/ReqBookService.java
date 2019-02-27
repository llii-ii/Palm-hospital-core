/**
 * 
 */
package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**挂号入参
 * @author lsq
 * version 1.0
 * 2017-7-6下午1:59:38
 */
public class ReqBookService extends AbsReq {
	/**订单号*/
	private String orderId;
//	/**我们系统的用户的唯一ID*/
//	private String memberId;
//	/**身份证号*/
//	private String idCardNo;
//	/**联系电话*/
//	private String mobile;
//	/**患者姓名*/
//	private String name;
//	/**性别*/
//	private Integer sex;
//	/**家庭住址*/
//	private String address;
//	/**证件号,没有的时候传：初诊*/
//	private String cardNo;
//	/**证件类型*/
//	private Integer cardType;
//	/**是否已经付费1是0否*/
//	private Integer payFee;
//	/**电子交易流水号*/
//	private String transNo;
//	/**支付时间*/
//	private String transTime;
//	/**1-当天挂号  2-预约*/
//	private Integer regFlag;
	/**操作人id(微信Openid)*/
	private String operatorId;
	/**操作人姓名*/
	private String operatorName;

	
	public ReqBookService(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(ser, "OrderId", true);
//		this.idCardNo = XMLUtil.getString(ser, "IdCardNo", false);;
//		this.mobile = XMLUtil.getString(ser, "Mobile", false);;
//		this.name = XMLUtil.getString(ser, "Name", false);;
//		this.sex = XMLUtil.getInt(ser, "Sex", false);;
//		this.address = XMLUtil.getString(ser, "Address", false);;
//		this.cardNo = XMLUtil.getString(ser, "CardNo", false);;
//		this.cardType = XMLUtil.getInt(ser, "CardType", false);;
//		this.payFee = XMLUtil.getInt(ser, "PayFee", false);;
//		this.transNo = XMLUtil.getString(ser, "TransNo", false);;
//		this.transTime = XMLUtil.getString(ser, "TransTime", false);;
//		this.regFlag = XMLUtil.getInt(ser, "RegFlag", false);;
		this.operatorId = XMLUtil.getString(ser, "OperatorId", false,super.getOpenId());;
		this.operatorName = XMLUtil.getString(ser, "OperatorName", false,super.getOperatorName());
//		this.memberId = XMLUtil.getString(ser, "MemberId", false);;
	}

//	
//	public String getMemberId() {
//		return memberId;
//	}
//
//
//	public void setMemberId(String memberId) {
//		this.memberId = memberId;
//	}
//
//
//	/**
//	 * @Title: ReqBookService
//	 * @Description: 
//	 * @param msg
//	 * @param orderId
//	 * @param idCardNo
//	 * @param mobile
//	 * @param name
//	 * @param sex
//	 * @param address
//	 * @param cardNo
//	 * @param cardType
//	 * @param payFee
//	 * @param transNo
//	 * @param transTime
//	 * @param regFlag
//	 * @param operatorId
//	 * @param operatorName
//	 * @throws AbsHosException
//	 */
//	public ReqBookService(InterfaceMessage msg, String orderId, String idCardNo, String mobile,
//			String name, Integer sex, String address, String cardNo, Integer cardType, 
//			Integer payFee, String transNo, String transTime, Integer regFlag, String operatorId,
//			String operatorName,String memberId) throws AbsHosException {
//		super(msg);
//		this.orderId = orderId;
//		this.idCardNo = idCardNo;
//		this.mobile = mobile;
//		this.name = name;
//		this.sex = sex;
//		this.address = address;
//		this.cardNo = cardNo;
//		this.cardType = cardType;
//		this.payFee = payFee;
//		this.transNo = transNo;
//		this.transTime = transTime;
//		this.regFlag = regFlag;
//		this.operatorId = operatorId;
//		this.operatorName = operatorName;
//		this.memberId = memberId;
//	}


	public ReqBookService(InterfaceMessage msg, String orderId,String operatorId, String operatorName)
			throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

//	public String getIdCardNo() {
//		return idCardNo;
//	}
//
//	public void setIdCardNo(String idCardNo) {
//		this.idCardNo = idCardNo;
//	}
//
//	public String getMobile() {
//		return mobile;
//	}
//
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public int getSex() {
//		return sex;
//	}
//
//	public void setSex(Integer sex) {
//		this.sex = sex;
//	}
//
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public String getCardNo() {
//		return cardNo;
//	}
//
//	public void setCardNo(String cardNo) {
//		this.cardNo = cardNo;
//	}
//
//	public Integer getCardType() {
//		return cardType;
//	}
//
//	public void setCardType(Integer cardType) {
//		this.cardType = cardType;
//	}
//
//	public Integer getPayFee() {
//		return payFee;
//	}
//
//	public void setPayFee(Integer payFee) {
//		this.payFee = payFee;
//	}
//
//	public String getTransNo() {
//		return transNo;
//	}
//
//	public void setTransNo(String transNo) {
//		this.transNo = transNo;
//	}
//
//	public String getTransTime() {
//		return transTime;
//	}
//
//	public void setTransTime(String transTime) {
//		this.transTime = transTime;
//	}
//
//	public int getRegFlag() {
//		return regFlag;
//	}
//
//	public void setRegFlag(Integer regFlag) {
//		this.regFlag = regFlag;
//	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
}
