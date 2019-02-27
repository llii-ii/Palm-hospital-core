package com.kasite.core.serviceinterface.module.pay.req;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.validator.Assert;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO 业务支付订单，在下单前进行支付校验，确认支付订单时候为该金额，如果是则返回成功
 */
public class ReqPayStartBizOrderExecute extends AbsReq{

	/**订单ID*/
	private String orderId; 
	/**操作人员ID*/
	private String operatorId; 
	/**操作人员姓名*/
	private String operatorName;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * @param msg
	 * @param orderId
	 * @param operatorId
	 * @param operatorName
	 * @throws AbsHosException
	 */
	public ReqPayStartBizOrderExecute(InterfaceMessage msg, String orderId, String operatorId, String operatorName) throws AbsHosException {
		super(msg);
		Assert.isBlank(orderId, "订单号不能为空。");
		this.orderId = orderId;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
	}

	
}
