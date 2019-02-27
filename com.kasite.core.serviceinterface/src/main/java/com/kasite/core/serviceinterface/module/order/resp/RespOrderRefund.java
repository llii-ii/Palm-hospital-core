package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespOrderRefund
 * @author: lcz
 * @date: 2018年7月12日 下午9:40:22
 */
public class RespOrderRefund extends AbsResp{
	
	private String refundOrderId;
	private String refundNo;
	
	public String getRefundOrderId() {
		return refundOrderId;
	}
	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}
	public String getRefundNo() {
		return refundNo;
	}
	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
	
	
	
}
