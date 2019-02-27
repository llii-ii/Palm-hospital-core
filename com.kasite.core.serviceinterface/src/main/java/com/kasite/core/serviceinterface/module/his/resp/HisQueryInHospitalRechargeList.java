/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author lcy
 * @version 1.0 
 * 2017-7-20下午1:58:39
 */
public class HisQueryInHospitalRechargeList  extends AbsResp{
	
	private String orderId;
	
	private String hisRechargeDate;
	
	private String merchOrderNo;
	
	private String hisOrderId;
	
	private String refundOrderId;
	
	private Integer payMoney;
	
	private Integer totalMoney;
	
	private Integer refundMoney;
	
	private String orderMemo;
	
	private Integer hisOrderState;
	
	private Integer isAllowRefund;
	
	private String channelId;
	
	private Integer chargeType;
	
	private Integer orderType;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getHisRechargeDate() {
		return hisRechargeDate;
	}

	public void setHisRechargeDate(String hisRechargeDate) {
		this.hisRechargeDate = hisRechargeDate;
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

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}

	public Integer getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(Integer refundMoney) {
		this.refundMoney = refundMoney;
	}

	public String getOrderMemo() {
		return orderMemo;
	}

	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
	}

	public Integer getHisOrderState() {
		return hisOrderState;
	}

	public void setHisOrderState(Integer hisOrderState) {
		this.hisOrderState = hisOrderState;
	}

	public Integer getIsAllowRefund() {
		return isAllowRefund;
	}

	public void setIsAllowRefund(Integer isAllowRefund) {
		this.isAllowRefund = isAllowRefund;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	
	
}
