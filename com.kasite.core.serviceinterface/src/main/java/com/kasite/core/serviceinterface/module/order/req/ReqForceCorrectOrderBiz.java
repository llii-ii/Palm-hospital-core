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
public class ReqForceCorrectOrderBiz extends AbsReq{

	private String orderId;
	
	private Integer orderType;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqForceCorrectOrderBiz(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.orderId = XMLUtil.getString(dataEl, "OrderId", true);
		this.orderType = XMLUtil.getInt(dataEl, "OrderType", true);
	}
	
	public ReqForceCorrectOrderBiz(InterfaceMessage msg, String source) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.orderId = getDataJs().getString("OrderId");
			this.orderType = getDataJs().getInteger("OrderType");
		}
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	
}
