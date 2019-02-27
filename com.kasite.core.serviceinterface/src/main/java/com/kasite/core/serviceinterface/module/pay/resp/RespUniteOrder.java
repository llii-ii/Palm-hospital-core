package com.kasite.core.serviceinterface.module.pay.resp;

import com.kasite.core.common.resp.AbsResp;
/**
 * 订单统一下单
 * 
 * @author daiyanshui
 *
 */
public class RespUniteOrder extends AbsResp{
	private String orderId;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
