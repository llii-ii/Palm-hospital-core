package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespOrderIsCancel
 * @author: lcz
 * @date: 2018年7月10日 上午10:40:59
 */
public class RespOrderIsCancel extends AbsResp{
	
	private String refundOrderId;
	
	public String getRefundOrderId() {
		return refundOrderId;
	}
	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}
	
}
