package com.kasite.core.serviceinterface.module.order.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * 根据订单号查询订单视图
 * @author daiyanshui
 *
 */
public class ReqGetOrderView extends AbsReq{

	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public ReqGetOrderView(InterfaceMessage msg,String orderId) throws AbsHosException {
		// TODO Auto-generated constructor stub
		super(msg);
		this.orderId = orderId;
	}

}
