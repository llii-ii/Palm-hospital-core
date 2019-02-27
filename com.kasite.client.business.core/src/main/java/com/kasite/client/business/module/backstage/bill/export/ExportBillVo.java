package com.kasite.client.business.module.backstage.bill.export;

/**
 * 
 * <p>Title: ExportBillVo</p>  
 * <p>Description: 导出交易渠道(商户:微信、支付宝、银联等)的账单信息的Excel对象实体类</p> 
 * <p>Company: KST</p> 
 *
 * @author zhaoy  
 * @date 2018年5月29日  
 * @version 1.0
 */
public class ExportBillVo {

	/**
	 * 商户订单号
	 */
	private String merchNo;
	
	/**
	 * 全流程订单号
	 */
	private String orderId;
	
	/**
	 * 商户退款单号
	 */
	private String refundMerchNo;
	
	/**
	 * 全流程退款订单ID
	 */
	private String refundOrderId;
	
	/**
	 * 渠道号
	 */
	private String channelName;
	
	/**
	 * 1支付订单2退款订单
	 */
	private String orderType;
	
	/**
	 * 交易日期
	 */
	private String transDate;
	
	/**
	 * 退费金额
	 */
	private String refundPrice;
	
	/**
	 * 支付金额(元)
	 */
	private String transactions;
	
	/**
	 * 医院名称
	 */
	private String hosName;
	
	/**
	 * 调用口提交的交易类型，取值如下：JSAPI，NATIVE，APP
	 */
	private String tradeType;

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(String refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getTransactions() {
		return transactions;
	}

	public void setTransactions(String transactions) {
		this.transactions = transactions;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
}
