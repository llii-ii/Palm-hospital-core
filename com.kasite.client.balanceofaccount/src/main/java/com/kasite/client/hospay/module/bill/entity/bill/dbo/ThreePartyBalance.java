package com.kasite.client.hospay.module.bill.entity.bill.dbo;

import java.sql.Timestamp;

/**
 * @author cc 三方每日汇总账单明细实体类
 */
public class ThreePartyBalance {

	/** 全流程订单ID */
	private String orderId;
	/** HIS订单ID */
	private String hisOrderId;
	/** 渠道订单ID */
	private String channelOrderId;
	/** 服务ID 具体标识可参考全流程文档 */
	private String serviceId;
	/** 保留字段 */
	private String reserveId;
	/** 订单创建时间 */
	private Timestamp createDate;
	/** 订单最后操作时间 */
	private Timestamp lastDate;
	/** 收费项目名 */
	private String priceName;
	/** 应收费金额 */
	private String receivableMoney;
	/** 实收费金额 */
	private String alreadyReceivedMoney;
	/** 应退金额 */
	private String refundMoney;
	/** 实退金额 */
	private String alreadyRefundMoney;

	/** 订单收费目录 */
	private String orderMemo;
	/** 卡号 */
	private String cardNo;
	/** 操作人员标识 */
	private String operator;
	/** 操作人姓名 */
	private String operatorName;
	/** 渠道交易订单状态 如：微信/支付宝 1 交易成功 2 退款成功 */
	private String channelOrderState;
	/** 渠道号 */
	private String channelId;
	/** 第三方交易/退款成功流水号 */
	private String payUpdateKey;
	/** 全流程订单状态 1 交易成功 2 退款成功 */
	private String qlcOrderState;
	/** HIS订单状态 1 交易成功 2 退款成功 */
	private String hisOrderState;

	/** 订单异常状态 0异常 1正常 */
	private String errorState;

	/** 检验结果 -1短款 0正常 1长款 */
	private String checkState;
	/** 业务类型 1冲正 2退费*/
	private String bizType;
	/** 业务执行状态  0 待执行 1 正在执行 2 执行完成*/
	private String exeState;
	/** 是否同步的订单 0未同步正常订单 1同步异常订单*/
	private String isSyn;

	public String getOrderId() {
		return orderId==null?"":orderId;
	}

	public String getHisOrderId() {
		return hisOrderId==null?"":hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getChannelOrderId() {
		return channelOrderId==null?"":channelOrderId;
	}

	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getServiceId() {
		return serviceId==null?"":serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getReserveId() {
		return reserveId==null?"":reserveId;
	}

	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}

	public String getPriceName() {
		return priceName==null?"":priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getReceivableMoney() {
		return receivableMoney==null?"0":receivableMoney;
	}

	public void setReceivableMoney(String receivableMoney) {
		this.receivableMoney = (receivableMoney==null?"0":receivableMoney);
	}

	public String getBizType() {
		return bizType==null?"":bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getExeState() {
		return exeState==null?"":exeState;
	}

	public void setExeState(String exeState) {
		this.exeState = exeState;
	}

	public Timestamp getLastDate() {
		return lastDate;
	}

	public void setLastDate(Timestamp lastDate) {
		this.lastDate = lastDate;
	}

	public String getAlreadyReceivedMoney() {
		return alreadyReceivedMoney==null?"0":alreadyReceivedMoney;
	}

	public void setAlreadyReceivedMoney(String alreadyReceivedMoney) {
		this.alreadyReceivedMoney = (alreadyReceivedMoney==null?"0":alreadyReceivedMoney);
	}

	public String getRefundMoney() {
		return refundMoney==null?"0":refundMoney;
	}

	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney==null?"0":refundMoney;
	}

	public String getAlreadyRefundMoney() {
		return alreadyRefundMoney==null?"0":alreadyRefundMoney;
	}

	public void setAlreadyRefundMoney(String alreadyRefundMoney) {
		this.alreadyRefundMoney = (alreadyRefundMoney==null?"0":alreadyRefundMoney);
	}

	public String getOrderMemo() {
		return orderMemo==null?"":orderMemo;
	}

	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
	}

	public String getCardNo() {
		return cardNo==null?"":cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getOperator() {
		return operator==null?"":operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorName() {
		return operatorName==null?"":operatorName;
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
		return channelId==null?"":channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPayUpdateKey() {
		return payUpdateKey==null?"":payUpdateKey;
	}

	public void setPayUpdateKey(String payUpdateKey) {
		this.payUpdateKey = payUpdateKey;
	}

	public String getQlcOrderState() {
		return qlcOrderState==null?"":qlcOrderState;
	}

	public void setQlcOrderState(String qlcOrderState) {
		this.qlcOrderState = qlcOrderState;
	}

	public String getHisOrderState() {
		return hisOrderState==null?"":hisOrderState;
	}

	public void setHisOrderState(String hisOrderState) {
		this.hisOrderState = hisOrderState;
	}

	public String getChannelOrderState() {
		return channelOrderState==null?"":channelOrderState;
	}

	public void setChannelOrderState(String channelOrderState) {
		this.channelOrderState = channelOrderState;
	}

	public String getErrorState() {
		return errorState;
	}

	public void setErrorState(String errorState) {
		this.errorState = errorState;
	}

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getIsSyn() {
		return isSyn;
	}

	public void setIsSyn(String isSyn) {
		this.isSyn = isSyn;
	}
}
