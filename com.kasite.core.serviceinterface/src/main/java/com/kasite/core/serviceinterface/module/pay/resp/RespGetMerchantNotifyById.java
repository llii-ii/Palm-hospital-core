package com.kasite.core.serviceinterface.module.pay.resp;

import java.sql.Timestamp;

import com.kasite.core.common.resp.AbsResp;


/**
 * @author linjf
 */
public class RespGetMerchantNotifyById extends AbsResp{


	private Long id;
	
	/**
	 * 全流程订单id
	 */
	private String orderId;
	
	/**
	 * 全流程退款订单号
	 */
	private String refundOrderId;
	
	/**
	 * 1支付notfiy2退费notify
	 */
	private Integer orderType;
	
	/**
	 * 商户支付订单号
	 */
	private String transactionNo;
	
	/**
	 * 商户退款订单号
	 */
	private String refundNo;
	
	/**
	 * 订单金额
	 */
	private Integer price;
	
	/**
	 * 退费金额
	 */
	private Integer refundPrice;
	
	/**
	 * 新增时间
	 */
	private Timestamp createTime;
	
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 重试次数
	 */
	private Integer retryNum;
	
	/**
	 * 状态0重试中1重试成功-1重试失败
	 */
	private Integer state;
	
	/**
	 * clientId
	 */
	private String clientId;
	
	/**
	 * 
	 */
	private String configKey;

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(Integer retryNum) {
		this.retryNum = retryNum;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}
	
}
