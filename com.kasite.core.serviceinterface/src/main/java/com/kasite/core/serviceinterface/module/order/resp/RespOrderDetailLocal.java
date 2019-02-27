package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class RespOrderDetailLocal extends AbsResp{
	
	private String orderId;
	
	private String orderNum;
	
	private String hosId;
	
	private String prescNo;
	
	private Integer payMoney;
	
	private Integer totalMoney;
	
	private String priceName;
	
	private String orderMemo;
	
	private String cardNo;
	
	private String cardType;
	
	private Integer isOnlinePay;
	
	private String operatorId;
	
	private String operatorName;
	
	private String serviceId;
	
	private Integer eqptType;
	
	private String transactionNo;
	
	private String payChannelId;
	
	private Integer bizState;
	
	private Integer payState;
	
	private String idCardNo;
	
	private Integer overState;
	
	private String memberName;
	
	private String address;
	
	private String mobile;
	
	private Integer sex;
	
	private String beginDate;
	
	private String endDate;
	
	private String channelId;
	
	private String memberId;
	
	private String hisMemberId;
	
	
	
	public String getHisMemberId() {
		return hisMemberId;
	}

	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

//	private RespOrderExtension data_1;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}

	public Integer getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
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

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getPayChannelId() {
		return payChannelId;
	}

	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}

	public Integer getBizState() {
		return bizState;
	}

	public void setBizState(Integer bizState) {
		this.bizState = bizState;
	}

	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer paystate) {
		this.payState = paystate;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Integer getOverState() {
		return overState;
	}

	public void setOverState(Integer overState) {
		this.overState = overState;
	}

//	public RespOrderExtension getData_1() {
//		return data_1;
//	}
//
//	public void setData_1(RespOrderExtension data_1) {
//		this.data_1 = data_1;
//	}
	

}
