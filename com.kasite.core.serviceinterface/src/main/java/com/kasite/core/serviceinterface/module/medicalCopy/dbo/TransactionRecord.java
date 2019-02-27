package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 
 * @className: TransactionRecord
 * @author: cjy
 * @date: 2018年9月28日 下午2:53:04
 */
@Table(name="TB_TRANSACTION_RECORD")
public class TransactionRecord extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;//uuid
	
	private String orderType;//订单类型 1-支付订单
	private String orderId;//订单id/申请单号
	private String payChannelId;//渠道支付单号
	private String serviceContent;//服务内容
	private String actualReceipts;//实际收款
	private String shouldRefunds;//应退
	private String actualRefunds;//实退
	private String receiptsType;//收款类型 1-已收款
	private String payChannel;//渠道
	private String accountResult;//对账结果 1-长款 2-账平 3-短款
	private String state;//状态：0未启用1启用
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPayChannelId() {
		return payChannelId;
	}
	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}
	public String getServiceContent() {
		return serviceContent;
	}
	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}
	public String getActualReceipts() {
		return actualReceipts;
	}
	public void setActualReceipts(String actualReceipts) {
		this.actualReceipts = actualReceipts;
	}
	public String getShouldRefunds() {
		return shouldRefunds;
	}
	public void setShouldRefunds(String shouldRefunds) {
		this.shouldRefunds = shouldRefunds;
	}
	public String getActualRefunds() {
		return actualRefunds;
	}
	public void setActualRefunds(String actualRefunds) {
		this.actualRefunds = actualRefunds;
	}
	public String getReceiptsType() {
		return receiptsType;
	}
	public void setReceiptsType(String receiptsType) {
		this.receiptsType = receiptsType;
	}
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	public String getAccountResult() {
		return accountResult;
	}
	public void setAccountResult(String accountResult) {
		this.accountResult = accountResult;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	
}
