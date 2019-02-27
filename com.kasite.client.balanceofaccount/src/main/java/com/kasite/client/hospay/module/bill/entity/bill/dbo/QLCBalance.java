package com.kasite.client.hospay.module.bill.entity.bill.dbo;

import java.sql.Timestamp;

/**
 * @author cc 全流程对账表实体
 */
public class QLCBalance {
	/** 订单ID */
	private String orderId;
	/** 服务ID 具体标识可参考全流程文档 */
	private String serviceId;
	/** 保留字段 */
	private String reserveId;
	/** 收费项目名 */
	private String priceName;
	/** 收费金额 */
	private String price;
	/** 订单收费目录 */
	private String orderMemo;
	/** 卡号 */
	private String cardNo;
	/** 操作人员标识 */
	private String operator;
	/** 操作人姓名 */
	private String operatorName;
	/** 订单创建时间 */
	private Timestamp createDate;
	/** 渠道号 */
	private String channelId;
	/** 第三方交易/退款成功流水号 */
	private String payUpdateKey;
	/** 全流程订单状态 1 交易成功 2 退款成功 */
	private String orderState;

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

	public String getReserveId() {
		return reserveId;
	}

	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPayUpdateKey() {
		return payUpdateKey;
	}

	public void setPayUpdateKey(String payUpdateKey) {
		this.payUpdateKey = (payUpdateKey==null?"":payUpdateKey);
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
}
