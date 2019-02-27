package com.kasite.core.serviceinterface.module.pay.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespRefund
 * @author: lcz
 * @date: 2018年7月10日 上午11:33:26
 */
public class RespRefund extends AbsResp{
	
	/**
	 * 退款订单号
	 */
	private String refundOrderId;
	
	/**
	 * 退款时间
	 */
	private String refundTime;
	
	/**
	 *  退款状态:3退费申请中 4退费完成 7退费失败
	 */
 	private Integer payState;
	
	/**
	 * 退款发起人
	 */
	private String operatorName;
	
	/**
	 * 退款流水号
	 */
	private String refundNo;
	
	/**
	 * 退款金额
	 */
	private String refundPrice;
	
	/**
	 * 退款渠道
	 */
	private String refundChannelName;

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(String refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getRefundChannelName() {
		return refundChannelName;
	}

	public void setRefundChannelName(String refundChannelName) {
		this.refundChannelName = refundChannelName;
	}
}
