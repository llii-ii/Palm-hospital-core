package com.kasite.core.serviceinterface.module.order.dto;

import java.util.List;

/**
 * 
 * <p>Title: OrderQueryBean</p>  
 * <p>Description: 交易管理-->订单列表查询条件bean</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年09月08日  
 * @version 1.0
 */
public class OrderQuery{
	    
	/**
	 * 查询的开始日期
	 */
	private String startDate;
	
	/**
	 * 查询的结束日期
	 */
	private String endDate;
	
	/**
	 * 全流程订单号
	 */
	private String orderId;
	
	/**
	 * 渠道流水号
	 */
	private String channelSerialNo;
	
	/**
	 * 医院的流水号
	 */
	private String hisSerialNo;
	
	/**
	 * 就诊卡号/住院号
	 */
	private String cardNo;
	
	/**
	 * 患者姓名
	 */
	private String nickName;
	
	/**
	 * 患者手机号
	 */
	private String nickMobile;
	
	/**
	 * 交易场景
	 */
	private String channelId;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 服务号
	 */
	private List<String> serviceIdList;
	
	/**
	 * 渠道号
	 */
	private List<String> channelIdList;
	
	/**
	 * 商户号
	 */
	private String configKey;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChannelSerialNo() {
		return channelSerialNo;
	}

	public void setChannelSerialNo(String channelSerialNo) {
		this.channelSerialNo = channelSerialNo;
	}

	public String getHisSerialNo() {
		return hisSerialNo;
	}

	public void setHisSerialNo(String hisSerialNo) {
		this.hisSerialNo = hisSerialNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickMobile() {
		return nickMobile;
	}

	public void setNickMobile(String nickMobile) {
		this.nickMobile = nickMobile;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public List<String> getChannelIdList() {
		return channelIdList;
	}

	public void setChannelIdList(List<String> channelIdList) {
		this.channelIdList = channelIdList;
	}

	public List<String> getServiceIdList() {
		return serviceIdList;
	}

	public void setServiceIdList(List<String> serviceIdList) {
		this.serviceIdList = serviceIdList;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	
}
