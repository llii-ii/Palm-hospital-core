package com.kasite.client.pay.bean.dbo;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author linjf
 * TODO
 */
@Table(name="P_BILL")
public class Bill extends BaseDbo{

	/** 对账id */
	@Id
	@KeySql(useGeneratedKeys=true)
	private String billId;

	/** 微信订单号 */
	private String merchNo;

	/** 全流程订单号 */
	private String orderId;

	/** 微信退款订单号 */
	private String refundMerchNo;

	/** 全流程退款订单号 */
	private String refundOrderId;

	/** 渠道ID */
	private String channelId;
	
	/** 渠道 */
	private String channelName;
	
	/** 商户配置Key */
	private String configKey;

	/** 订单类型1支付2退款 */
	private Integer orderType;

	/** 交易日期 */
	private Timestamp transDate;

	/** 交易金额 */
	private Integer transactions;

	private Integer refundPrice;
	
	private String hosId;
	
	private String appId;

	private String tradeType;
	
	
	
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

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

	public Integer getTransactions() {
		return transactions;
	}

	public void setTransactions(Integer transactions) {
		this.transactions = transactions;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}
	
	
	
	

	
}
