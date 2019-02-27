package com.kasite.core.serviceinterface.module.order.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 账单冲正请求实体类
 * 
 * @author zhaoy
 *
 */
public class ReqCorrectBill extends AbsReq {

	public ReqCorrectBill(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.orderid = getDataJs().getString("OrderId");
			this.merchNo = getDataJs().getString("MerchNo");
		}
	}

	/**本地订单号*/
	private String orderid; 
	
	/**交易流水号*/
	private String merchNo;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	} 
	
}
