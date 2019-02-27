package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqCancelOrder extends AbsReq{

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
	 * @throws AbsHosException
	 */
	public ReqCancelOrder(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(dataEl, "OrderId", true);
		this.operatorId =  XMLUtil.getString(dataEl, "OperatorId", true,super.getOpenId());
		this.operatorName =  XMLUtil.getString(dataEl, "OperatorName", true,super.getOperatorName());
	}

	/**
	 * @Title: ReqCancelOrder
	 * @Description: 
	 * @param msg
	 * @param orderId
	 * @param operatorId
	 * @param operatorName
	 * @throws AbsHosException
	 */
	public ReqCancelOrder(InterfaceMessage msg, String orderId, String operatorId, String operatorName) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
	}

	
}
