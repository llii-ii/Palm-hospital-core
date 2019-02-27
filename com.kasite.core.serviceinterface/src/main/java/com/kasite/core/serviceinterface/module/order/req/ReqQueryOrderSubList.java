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
public class ReqQueryOrderSubList extends AbsReq{

	private String orderId;
	 
 	private String subHisOrderId;
 	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryOrderSubList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(service, "OrderId", true);
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSubHisOrderId() {
		return subHisOrderId;
	}

	public void setSubHisOrderId(String subHisOrderId) {
		this.subHisOrderId = subHisOrderId;
	}

	/**
	 * @param msg
	 * @param orderId
	 * @param subHisOrderId
	 * @throws AbsHosException
	 */
	public ReqQueryOrderSubList(InterfaceMessage msg, String orderId, String subHisOrderId) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.subHisOrderId = subHisOrderId;
	}

	
	
}
