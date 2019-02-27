package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.config.DefaultClientEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 当面付请求入参对象
 * 
 * @author 無
 *
 */
public class ReqSweepCodePay extends AbsReq {
	
	/** 调用方订单id,用以标识唯一标识(必填) */
	private String hisOrderId;
	/** 订单金额(以分为单位,整数)(必填) */
	private int totalFee;
	/** 订单标题(必填) */
	private String subject;
	/** 订单说明(必填) */
	private String orderMemo;
	/** 扫码支付授权码(必填) */
	private String authCode;
	/** 终端设备号(商户自定义) */
	private String deviceInfo;
	
	private String cardNo;
	private String cardType;
	private String operatorId;
	private String operatorName;
	private String serviceId;
	private Integer eqptType;
	private String idCardId;
	private String mobile;
	private String name;
	private Integer sex;
	/*
	   <IdCardId>身份证号</ IdCardId >
  <Mobile>手机号</Mobile>
  <Name>姓名</Name>
  <Sex>性别1,男2女3未知</Sex>

	 */
	
	private String hisMemberId;
	/**
	 * 是否校验就诊卡
	 * 出现如果 his 未开卡 提前充值的情况 这个时候卡无效
	 * */
	private String isCheckCardNo;
	
	public String getIdCardId() {
		return idCardId;
	}

	public void setIdCardId(String idCardId) {
		this.idCardId = idCardId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getEqptType() {
		return eqptType;
	}

	public void setEqptType(Integer eqptType) {
		this.eqptType = eqptType;
	}

	public String getIsCheckCardNo() {
		return isCheckCardNo;
	}

	public void setIsCheckCardNo(String isCheckCardNo) {
		this.isCheckCardNo = isCheckCardNo;
	}

	public ReqSweepCodePay(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.hisOrderId = XMLUtil.getString(dataEl, "HisOrderId",true);
		this.totalFee = XMLUtil.getInt(dataEl, "TotalFee", true);
		this.subject = XMLUtil.getString(dataEl, "Subject", true);
		this.orderMemo = XMLUtil.getString(dataEl, "OrderMemo", true);
		this.authCode = XMLUtil.getString(dataEl, "AuthCode", true);
		this.deviceInfo = XMLUtil.getString(dataEl, "DeviceInfo", false);
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.cardType = XMLUtil.getString(dataEl, "CardType", true);
		this.name = XMLUtil.getString(dataEl, "Name", false);
		this.idCardId = XMLUtil.getString(dataEl, "IdCardId", false);
		this.sex = XMLUtil.getInt(dataEl, "Sex", false);
		this.mobile = XMLUtil.getString(dataEl, "Mobile", false);
		this.operatorId = XMLUtil.getString(dataEl, "OperatorId", false,super.getOpenId());
		this.operatorName = XMLUtil.getString(dataEl, "OperatorName", false,super.getOpenId());
		this.serviceId = XMLUtil.getString(dataEl, "ServiceId", true);
		this.eqptType = XMLUtil.getInt(dataEl, "EqptType", false);//设备类型
		if (0 == eqptType && null != getAuthInfo()
				&& DefaultClientEnum.sweepcodepay.getClientId().equals(getAuthInfo().getClientId())) {
			// 如果是当面付渠道 并且没有传入EqptType 则默认 设备类型 为 5 扫码枪
			// 设备类型 发起订单设备ID 1.微信公众号/服务窗2.MINI机3.MobileApp 4.PCWEB 5.扫码枪 6.PCAPP
			eqptType = 5;
		} else if (0 == eqptType) {
			throw new RRException("EqptType 参数不能为空。");
		}
		this.hisMemberId = XMLUtil.getString(dataEl, "HisMemberId",false);
		this.isCheckCardNo = XMLUtil.getString(dataEl, "IsCheckCardNo",false);
	}
	
	/**
	 * @Title: ReqSweepCodePay
	 * @Description: 
	 * @param msg
	 * @param channelId
	 * @param hisOrderId
	 * @param totalFee
	 * @param subject
	 * @param orderMemo
	 * @param authCode
	 * @param deviceInfo
	 * @param orderId
	 * @param chargeType
	 * @param cardNo
	 * @param cardType
	 * @param operatorId
	 * @param operatorName
	 * @param serviceId
	 * @param isOnlinePay
	 * @param eqptType
	 * @param merchantType
	 * @throws AbsHosException
	 */
	public ReqSweepCodePay(InterfaceMessage msg, String channelId, String hisOrderId, int totalFee, String subject, String orderMemo, String authCode, String deviceInfo,String cardNo, String cardType, String operatorId, String operatorName, String serviceId, String isOnlinePay, Integer eqptType, String merchantType) throws AbsHosException {
		super(msg);
		this.hisOrderId = hisOrderId;
		this.totalFee = totalFee;
		this.subject = subject;
		this.orderMemo = orderMemo;
		this.authCode = authCode;
		this.deviceInfo = deviceInfo;
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.serviceId = serviceId;
		this.eqptType = eqptType;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getOrderMemo() {
		return orderMemo;
	}

	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getHisMemberId() {
		return hisMemberId;
	}

	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}

	
	
}
