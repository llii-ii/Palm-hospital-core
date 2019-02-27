package com.kasite.core.serviceinterface.module.pay.dto;

import java.sql.Timestamp;

/**
 * 原始账单视图(商户账单和His账单的联合查询实体类)
 * 
 * @author zhaoy
 *
 */
public class BillVo {

	/** 商户账单主键ID **/
	private String billId;
	
	/** 商户支付订单号 **/
	private String merchNo;
	
	/** 全流程支付/退费订单号  **/
	private String orderId;
	
	/** 商户退款单号  **/
	private String refundMerchNo;
	
	/** 全流程退款订单ID  **/
	private String refundOrderId;
	
	/** 账单类型: 1支付，2退费  **/
	private int orderType;
	
	/** 订单的交易日期 ***/
	private Timestamp transDate;
	
	/** 订单的支付时间 ***/
	private Timestamp payTransDate;
	
	/*** 商户退费金额（分） ***/
	private Integer merchRefundPrice;
	
	/*** 商户支付金额（分） **/
	private Integer merchPayPrice;
	
	/** 商户配置key ***/
	private String configkey;
	
	/** 渠道ID ***/
	private String channelId;
	
	/** 渠道名称 ***/
	private String channelName;
	
	/*** His记录的商户订单ID ***/
	private String merchOrderNo;
	
	/** HIS订单号（支付/退费） ***/
	private String hisOrderId;
	
	/** HIS支付金额 ***/
	private Integer hisPayPrice;
	
	/** His全部金额（分） ***/
	private Integer hisTotalMoney;
	
	/** HIS退费金额 ***/
	private Integer hisRefundMoney;
	
	/** HIS业务状态 ***/
	private int hisBizState;
	
	/** His账单的插入时间 ***/
	private Timestamp hisBizDate;
	
	/** 订单余额（分） ***/
	private Integer diffPrice;
	
	/** 交易笔数 ***/
	private Integer billCount;

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundMerchNo() {
		return refundMerchNo;
	}

	public void setRefundMerchNo(String refundMerchNo) {
		this.refundMerchNo = refundMerchNo;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public Timestamp getTransDate() {
		return transDate;
	}

	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}

	public Timestamp getPayTransDate() {
		return payTransDate;
	}

	public void setPayTransDate(Timestamp payTransDate) {
		this.payTransDate = payTransDate;
	}

	public Integer getMerchRefundPrice() {
		return merchRefundPrice;
	}

	public void setMerchRefundPrice(Integer merchRefundPrice) {
		this.merchRefundPrice = merchRefundPrice;
	}

	public Integer getMerchPayPrice() {
		return merchPayPrice;
	}

	public void setMerchPayPrice(Integer merchPayPrice) {
		this.merchPayPrice = merchPayPrice;
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

	public String getMerchOrderNo() {
		return merchOrderNo;
	}

	public void setMerchOrderNo(String merchOrderNo) {
		this.merchOrderNo = merchOrderNo;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public Integer getHisPayPrice() {
		return hisPayPrice;
	}

	public void setHisPayPrice(Integer hisPayPrice) {
		this.hisPayPrice = hisPayPrice;
	}

	public Integer getHisTotalMoney() {
		return hisTotalMoney;
	}

	public void setHisTotalMoney(Integer hisTotalMoney) {
		this.hisTotalMoney = hisTotalMoney;
	}

	public Integer getHisRefundMoney() {
		return hisRefundMoney;
	}

	public void setHisRefundMoney(Integer hisRefundMoney) {
		this.hisRefundMoney = hisRefundMoney;
	}

	public int getHisBizState() {
		return hisBizState;
	}

	public void setHisBizState(int hisBizState) {
		this.hisBizState = hisBizState;
	}

	public Timestamp getHisBizDate() {
		return hisBizDate;
	}

	public void setHisBizDate(Timestamp hisBizDate) {
		this.hisBizDate = hisBizDate;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public String getConfigkey() {
		return configkey;
	}

	public void setConfigkey(String configkey) {
		this.configkey = configkey;
	}

	public Integer getDiffPrice() {
		return diffPrice;
	}

	public void setDiffPrice(Integer diffPrice) {
		this.diffPrice = diffPrice;
	}

	public Integer getBillCount() {
		return billCount;
	}

	public void setBillCount(Integer billCount) {
		this.billCount = billCount;
	}
	
}
