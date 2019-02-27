package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 当面付请求入参对象
 * 
 * @author 無
 *
 */
public class ReqClose extends AbsReq {
	
	/**本地订单ID*/
	private String orderId;
	
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public ReqClose(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		/**本地订单ID*/
		this.orderId = XMLUtil.getString(dataEl, "OrderId", true);
	}

	public PgwReqClose toPgwReqClose() {
		PgwReqClose pgwReqClose = new PgwReqClose();
		pgwReqClose.setOrderId(this.orderId);
		return pgwReqClose;
	}
	
}
