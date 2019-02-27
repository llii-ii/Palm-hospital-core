package com.kasite.core.serviceinterface.module.his.resp;

import java.sql.Timestamp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 查询外部服务的全流程订单
 * 
 * @author zhaoy
 *
 */
public class RespQueryOrderCheck extends AbsResp {

	/**订单ID***/
	private String orderId;
	/**退款订单号***/
	private String refundOrderId;
	/**服务ID***/
	private String serviceId;
	/**全部费用（分）***/
	private Integer totalPrice;
	/**需要支付金额(分)***/
	private Integer price;
	/**支付金额***/
	private Integer payPrice;
	/**下单渠道ID***/
	private String channelId;
	/**下单渠道名称***/
	private String channelName;
	/**医院ID***/
	private String hosId;
	/**卡号***/
	private String cardNo;
	/**卡类型***/
	private String cardtype;
	/**his订单号，或者处方号（HIS有就存，没有就置空）***/
	private String prescno;
	/**支付的交易流水号***/
	private String transactionNo;
	/**支付的时候对应的使用的支付渠道配置信息***/
	private String configKey;
	/**订单状态0未支付，1支付中，2已支付，3，退款中，4已退款，5已取消，6已撤销，7退款失败，8已完成，9未完成***/
	private Integer orderState;
	/**订单类型 1，支付 2，退款***/
	private Integer orderType;
	/**费用名称***/
	private String priceName;
	/**订单标签，备注***/
	private String orderMemo;
	/**下单时间***/
	private Timestamp orderDatetime;
	/**支付时间***/
	private Timestamp payDatetime; 
	/**业务处理时间***/
	private Timestamp bizDatetime;
	/**退款金额***/
	private Integer refundPrice;
	/**退款流水号***/
	private String refundNo;
	/**退款时间***/
	private Timestamp refundDatetime;
	/**退款渠道ID***/
	private String refundChannelId;
	/**退款渠道名称***/
	private String refundChannelName;
	/**外部退费订单号记录-(His的退款流水号)***/
	private String outRefundOrderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Integer payPrice) {
		this.payPrice = payPrice;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getPrescno() {
		return prescno;
	}

	public void setPrescno(String prescno) {
		this.prescno = prescno;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
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

	public Timestamp getOrderDatetime() {
		return orderDatetime;
	}

	public void setOrderDatetime(Timestamp orderDatetime) {
		this.orderDatetime = orderDatetime;
	}

	public Timestamp getPayDatetime() {
		return payDatetime;
	}

	public void setPayDatetime(Timestamp payDatetime) {
		this.payDatetime = payDatetime;
	}

	public Timestamp getBizDatetime() {
		return bizDatetime;
	}

	public void setBizDatetime(Timestamp bizDatetime) {
		this.bizDatetime = bizDatetime;
	}

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public Timestamp getRefundDatetime() {
		return refundDatetime;
	}

	public void setRefundDatetime(Timestamp refundDatetime) {
		this.refundDatetime = refundDatetime;
	}

	public String getRefundChannelId() {
		return refundChannelId;
	}

	public void setRefundChannelId(String refundChannelId) {
		this.refundChannelId = refundChannelId;
	}

	public String getRefundChannelName() {
		return refundChannelName;
	}

	public void setRefundChannelName(String refundChannelName) {
		this.refundChannelName = refundChannelName;
	}

	public String getOutRefundOrderId() {
		return outRefundOrderId;
	}

	public void setOutRefundOrderId(String outRefundOrderId) {
		this.outRefundOrderId = outRefundOrderId;
	}
	
}
