package com.kasite.client.order.bean.dto;

import java.util.List;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.serviceinterface.module.basic.dbo.Member;

/**
 * @author linjf
 * TODO
 */
public class QueryOrderViewParam  {

	/**成员ID*/
	private String memberId; 
	/**卡号*/
	private String cardNo; 
	/**卡类型*/
	private Integer cardType;
	/**订单类型*/
	private String serviceId; 
	/**订单ID*/
	private String orderId; 
	/**处方单号*/
	private String prescNo; 
	/**微信ID*/
	private String openId; 
	/** 开始时间*/
	private String beginDate;
	/**支付状态*/
	private String payState; 
	/**业务状态*/
	private String bizState;
	/**订单终结状态*/
	private String overState;
	/**结束时间*/
	private String endDate;
	/**渠道id*/
	private String channelId;
	/**交易流水号：微信／支付宝／银联*/
	private String transactionNo;
	/**用户列表*/
	private List<Member> memberList;
	/**业务类型*/
	private List<String> serviceIds;
	
	/**
	 * 订单查询限制时间
	 */
	private Integer orderLimitDate;
	
	public Integer getOrderLimitDate() {
		if(orderLimitDate == null ) {
			orderLimitDate = KasiteConfig.getHistoryOrderListDays();
		}
		return orderLimitDate;
	}
	public void setOrderLimitDate(Integer orderLimitDate) {
		this.orderLimitDate = orderLimitDate;
	}
	public List<String> getServiceIds() {
		return serviceIds;
	}
	public void setServiceIds(List<String> serviceIds) {
		this.serviceIds = serviceIds;
	}
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
	
	public List<Member> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<Member> memberList) {
		this.memberList = memberList;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Integer getCardType() {
		return cardType;
	}
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getPayState() {
		return payState;
	}
	public void setPayState(String payState) {
		this.payState = payState;
	}
	public String getBizState() {
		return bizState;
	}
	public void setBizState(String bizState) {
		this.bizState = bizState;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	public String getOverState() {
		return overState;
	}
	public void setOverState(String overState) {
		this.overState = overState;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
}
