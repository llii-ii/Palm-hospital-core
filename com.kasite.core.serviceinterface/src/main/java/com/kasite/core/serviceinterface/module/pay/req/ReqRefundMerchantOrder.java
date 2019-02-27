package com.kasite.core.serviceinterface.module.pay.req;

import java.sql.Timestamp;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqRefundMerchantOrder extends AbsReq {

private String orderId;
	
	private String refundOrderId;
	
	private Integer refundPrice;

	private Integer totalPrice;
	
	private String channelId;
	
	private String operatorId;
	
	private String operatorName;
	
	private String remark;
	
	private Integer state;
	
	private Timestamp endDate;
	
	private Timestamp beginDate;
	
	private String refundNo;
	
	private Integer retryNum;

	public String getOrderId() {
		return orderId;
	}

	public Timestamp getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Timestamp beginDate) {
		this.beginDate = beginDate;
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

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public Integer getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(Integer retryNum) {
		this.retryNum = retryNum;
	}
	
	public ReqRefundMerchantOrder(
			InterfaceMessage msg, String orderId, String refundOrderId, Integer refundPrice,
			Integer totalPrice, String channelId, String operatorId, String operatorName, 
			String remark, Integer state,Timestamp endDate, Timestamp beginDate, 
			String refundNo, Integer retryNum) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.refundOrderId = refundOrderId;
		this.refundPrice = refundPrice;
		this.totalPrice = totalPrice;
		this.channelId = channelId;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.remark = remark;
		this.state = state;
		this.endDate = endDate;
		this.beginDate = beginDate;
		this.refundNo = refundNo;
		this.retryNum = retryNum;
	}

}
