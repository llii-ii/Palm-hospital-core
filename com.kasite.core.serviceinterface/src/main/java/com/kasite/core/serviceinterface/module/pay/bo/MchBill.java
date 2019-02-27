package com.kasite.core.serviceinterface.module.pay.bo;

import java.sql.Timestamp;

/**
 * @author linjf
 * TODO
 */
public class MchBill {


	/** 微信订单号 */
	private String mchTradeNo;

	/** 全流程订单号 */
	private String orderId;

	/** 微信退款订单号 */
	private String refundMchTradeNo;

	/** 全流程退款订单号 */
	private String refundOrderId;

	
	/** 商户配置Key */
	private String configKey;

	/** 订单类型1支付2退款 */
	private Integer orderType;

	/** 交易日期 */
	private Timestamp transDate;

	/** 交易金额 */
	private Integer payPrice;

	private Integer refundPrice;

	private String tradeType;


	public void setMchTradeNo(String mchTradeNo) {
		this.mchTradeNo = mchTradeNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}



	public String getRefundMchTradeNo() {
		return refundMchTradeNo;
	}

	public void setRefundMchTradeNo(String refundMchTradeNo) {
		this.refundMchTradeNo = refundMchTradeNo;
	}

	public String getMchTradeNo() {
		return mchTradeNo;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Timestamp getTransDate() {
		return transDate;
	}

	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}

	public Integer getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Integer payPrice) {
		this.payPrice = payPrice;
	}

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	
	
	
}
