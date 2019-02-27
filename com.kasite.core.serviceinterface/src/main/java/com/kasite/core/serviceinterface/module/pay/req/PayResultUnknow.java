package com.kasite.core.serviceinterface.module.pay.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 支付结果未知订单
 * 
 * @author 無
 *
 */
public class PayResultUnknow extends AbsReq{

	public PayResultUnknow(InterfaceMessage msg, String orderId) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
	}

	/**
	 * 全流程订单ID
	 */
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
