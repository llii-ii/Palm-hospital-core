package com.kasite.core.serviceinterface.module.pay.req;

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
public class ReqMerchantNotifyForceRetry extends AbsReq{

	private String orderId;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqMerchantNotifyForceRetry(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(dataEl, "OrderId",true);
	}
	
	/**
	 * @param msg
	 * @param orderId
	 * @throws AbsHosException
	 */
	public ReqMerchantNotifyForceRetry(InterfaceMessage msg, String orderId) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
		
}
