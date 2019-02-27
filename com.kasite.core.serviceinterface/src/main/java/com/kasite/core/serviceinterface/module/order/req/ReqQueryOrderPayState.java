package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqQueryOrderPayState extends AbsReq{

	private String orderId;
	
	private String refundOrderId;
	
	private String outRefundOrderId;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryOrderPayState(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.orderId = XMLUtil.getString(dataEl, "OrderId", false);
		if(StringUtil.isEmpty(orderId)){
			this.orderId = XMLUtil.getString(dataEl, "orderId", false);
		}
		this.refundOrderId = XMLUtil.getString(dataEl, "RefundOrderId", false);
		this.outRefundOrderId = XMLUtil.getString(dataEl, "OutRefundOrderId", false);
		if(StringUtil.isEmpty(outRefundOrderId)){
			this.outRefundOrderId = XMLUtil.getString(dataEl, "hisRefundId", false);
		}
		if( StringUtil.isEmpty(orderId)
				&& StringUtil.isEmpty(refundOrderId)
				&& StringUtil.isEmpty(outRefundOrderId)) {
			throw new ParamException("参数[OrderId][RefundOrderId][OutRefundOrderId]不能同时为空！");
		}
	}
	
	public ReqQueryOrderPayState(InterfaceMessage msg, String oprType) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.orderId = getDataJs().getString("OrderId");
			this.refundOrderId = getDataJs().getString("RefundOrderId");
			this.outRefundOrderId = getDataJs().getString("OutRefundOrderId");
		}
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getOutRefundOrderId() {
		return outRefundOrderId;
	}

	public void setOutRefundOrderId(String outRefundOrderId) {
		this.outRefundOrderId = outRefundOrderId;
	}

	/**
	 * @param msg
	 * @param orderId
	 * @param refundOrderId
	 * @param outRefundOrderId
	 * @throws AbsHosException
	 */
	public ReqQueryOrderPayState(InterfaceMessage msg, String orderId, String refundOrderId, String outRefundOrderId)
			throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.refundOrderId = refundOrderId;
		this.outRefundOrderId = outRefundOrderId;
	}
	
	

}
