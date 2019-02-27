package com.kasite.core.serviceinterface.module.order.resp;

import java.sql.Timestamp;
import com.kasite.core.common.resp.AbsResp;

/**
 * 下载全流程订单
 * 
 * @author zhaoy
 *
*/ 
public class RespQueryQLCOrder extends AbsResp {
	
	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 退款订单号
	 */
	private String refundOrderId;
	
	/**
	 * 服务ID 006:住院号 007:就诊卡
	 */
	private String serviceId;
	
	/**
	 * 交易时间
	 */
	private Timestamp transTime;
	
	/**
	 * 渠道流水号
	 */
	private String channelSerialNo;
	
	/**
	 * 医院的流水号
	 */
	private String hisSerialNo;
	
	/**
	 * 交易类型
	 */
	private String transType;
	
	/**
	 * 交易金额
	 */
	private Integer transMoney;
	
	private Integer payMoney;
	
	/**
	 * 订单金额
	 */
	private Integer orderMoney;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 业务状态
	 */
	private Integer bizState;
	
	/**
	 * 就诊卡号/住院号
	 */
	private String cardNo;
	
	private Integer refundPrice;
	
	private String refundNo;
	
	private String transactionNo;
	
	private String priceName;
	
	private String orderMemo;
	
	private String channelId;
	
	private String channelName;
	
	private String refundChannelId;
	
	private String refundChannelName;
	
	/**外部退费订单号记录**/
	private String outRefundorderId; 
	
	/**
	 * 支付完成时间
	 */
	private Timestamp payDate;
	
	/**
	 * 业务执行时间
	 */
	private Timestamp hisbizDate;
	
	/**
	 * 退款完成时间
	 */
	private Timestamp refundDate;
	
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

	public Timestamp getTransTime() {
		return transTime;
	}

	public void setTransTime(Timestamp transTime) {
		this.transTime = transTime;
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

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}

	public Integer getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(Integer transMoney) {
		this.transMoney = transMoney;
	}

	public Integer getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(Integer orderMoney) {
		this.orderMoney = orderMoney;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public Integer getBizState() {
		return bizState;
	}

	public void setBizState(Integer bizState) {
		this.bizState = bizState;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	public String getOutRefundorderId() {
		return outRefundorderId;
	}

	public void setOutRefundorderId(String outRefundorderId) {
		this.outRefundorderId = outRefundorderId;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
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

	public Timestamp getPayDate() {
		return payDate;
	}

	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}

	public Timestamp getHisbizDate() {
		return hisbizDate;
	}

	public void setHisbizDate(Timestamp hisbizDate) {
		this.hisbizDate = hisbizDate;
	}

	public Timestamp getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Timestamp refundDate) {
		this.refundDate = refundDate;
	}

}