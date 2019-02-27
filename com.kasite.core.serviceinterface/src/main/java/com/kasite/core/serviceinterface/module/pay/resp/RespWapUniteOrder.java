package com.kasite.core.serviceinterface.module.pay.resp;

import com.kasite.core.common.resp.AbsResp;
/**
 * 订单统一下单
 * 
 * @author daiyanshui
 *
 */
public class RespWapUniteOrder extends AbsResp{
	private String orderId;
	/**
	 * 支付信息
	 */
	private String payInfo;

	
	
	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
