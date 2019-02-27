package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class RespOrderLocalList extends AbsResp{
	 private String hosId;
	 private String orderId;
	 private String prescNo;
	 //短的订单流水号  默认7位数 1天内不重复
	 private String orderNum;
	 //支付的时候的支付订单号
	 private String transactionNo;
	 private String memberId;
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
	 /**
	 * payState 总共有4个状态 0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成 
	 */
	 private Integer payState;
	 /**
	  * bizState 总共有2个状态 0:未执行业务  1:订单业务完成  2:订单业务取消
	  */
	 private Integer bizState;
	 private String beginDate;
	 private String channelId;
	 /**
	  * overState 总共有2个状态 5:订单取消  6:订单撤销  7:已关闭
	  */
	 private Integer overState;
	 private String memberName;
	 private String openId;
	 
	 
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}
	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
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
	/**
	 * payState 总共有4个状态 0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成 
	 * @return
	 */
	public Integer getPayState() {
		return payState;
	}
	/**
	 * payState 总共有4个状态 0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成 
	 * @return
	 */
	public void setPayState(Integer payState) {
		this.payState = payState;
	}
	 /**
	  * bizState 总共有2个状态 0:未执行业务  1:订单业务完成  2:订单业务取消
	  */
	public Integer getBizState() {
		return bizState;
	}
	 /**
	  * bizState 总共有2个状态 0:未执行业务  1:订单业务完成  2:订单业务取消
	  */
	public void setBizState(Integer bizState) {
		this.bizState = bizState;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	/**
	  * overState 总共有2个状态 5:订单取消  6:订单撤销  7:已关闭
	  */
	public Integer getOverState() {
		return overState;
	}
	/**
	  * overState 总共有2个状态 5:订单取消  6:订单撤销  7:已关闭
	  */
	public void setOverState(Integer overState) {
		this.overState = overState;
	}
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
	
}
