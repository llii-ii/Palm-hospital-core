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
public class ReqBizForCancel extends AbsReq{

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

	public ReqBizForCancel(InterfaceMessage msg, String orderId, String operatorId, String operatorName)
			throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
	}

	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqBizForCancel(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(service, "OrderId", true);
		this.operatorId =  XMLUtil.getString(service, "OperatorId", false,super.getOpenId());
		this.operatorName =  XMLUtil.getString(service, "OperatorName", false,super.getOperatorName());
	}

}
