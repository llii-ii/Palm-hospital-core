package com.kasite.core.serviceinterface.module.order.dbo;

import java.sql.Timestamp;

/**
 * @author linjf
 * TODO
 */
public class OrderView {
	private String orderId;
	private String hosid;
	private String serviceId;
	private String orderNum;
	private String reserveId;
	private String priceName;
	private Integer totalPrice;
	private Integer price;
	private Integer refundPrice;
	private String orderMemo;
	private String cardNo;
	private Integer isOnlinePay;
	private String operatorId;
	private String operatorName;
	private String cardType;
	private Timestamp beginDate;
	private String orderState;
	private String channelId;
	private String payChannelid;
	private String transactionNo;
	private Integer payState;
	private Integer bizState;
	private Integer overState;
	private Integer eqptType;
	private Integer merchantType;
	/**his订单号，或者处方号（HIS有就存，没有就置空）*/
	private String prescNo;
	private String configKey;
	
	/**用户信息**/
	private String memberName;
	/**用户Id**/
	private String memberId;
	private String mobile;
	private String address;
	private String idCardNo;
	private Integer sex;
	private String birthdate;
	private Integer isChildren;
	private String openId;
	private String hisMemberId;
	
	
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getHosid() {
		return hosid;
	}
	public void setHosid(String hosid) {
		this.hosid = hosid;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public Integer getIsChildren() {
		return isChildren;
	}
	public void setIsChildren(Integer isChildren) {
		this.isChildren = isChildren;
	}
	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	public String getPrescNo() {
		return prescNo;
	}
	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	public Integer getMerchantType() {
		return merchantType;
	}
	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}
	public Integer getEqptType() {
		return eqptType;
	}
	public void setEqptType(Integer eqptType) {
		this.eqptType = eqptType;
	}
	public Integer getOverState() {
		return overState;
	}
	public void setOverState(Integer overState) {
		this.overState = overState;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getPriceName() {
		return priceName;
	}
	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getOrderMemo() {
		return orderMemo;
	}
	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Integer getIsOnlinePay() {
		return isOnlinePay;
	}
	public void setIsOnlinePay(Integer isOnlinePay) {
		this.isOnlinePay = isOnlinePay;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorname) {
		this.operatorName = operatorname;
	}
	public Timestamp getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Timestamp begin) {
		this.beginDate = begin;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
	public Integer getPayState() {
		return payState;
	}
	public void setPayState(Integer payState) {
		this.payState = payState;
	}
	public Integer getBizState() {
		return bizState;
	}
	public void setBizState(Integer bizState) {
		this.bizState = bizState;
	}
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getRefundPrice() {
		return refundPrice;
	}
	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}
	public String getPayChannelid() {
		return payChannelid;
	}
	public void setPayChannelid(String payChannelid) {
		this.payChannelid = payChannelid;
	}
	public String getHisMemberId() {
		return hisMemberId;
	}
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}
	
	
}
