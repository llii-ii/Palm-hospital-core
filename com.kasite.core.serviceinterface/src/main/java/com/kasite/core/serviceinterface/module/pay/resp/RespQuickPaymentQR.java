package com.kasite.core.serviceinterface.module.pay.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQuickPaymentQR
 * @author: lcz
 * @date: 2018年7月27日 上午10:50:19
 */
public class RespQuickPaymentQR extends AbsResp{
	private String orderId;
	private Integer totalFee;
	private String qrContent;
	private String prescNo;
	
	
	
	public String getQrContent() {
		return qrContent;
	}
	public void setQrContent(String qrContent) {
		this.qrContent = qrContent;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}
	public String getPrescNo() {
		return prescNo;
	}
	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	
}
