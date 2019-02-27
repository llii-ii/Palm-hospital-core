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
public class ReqRevoke extends AbsReq {
	
	/**本地订单ID*/
	private String orderId;
	
	private String payConfigKey;
	
	/**
	 * 支付订单的支付时间
	 */
	private String payTime;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
	
	public String getPayConfigKey() {
		return payConfigKey;
	}

	public void setPayConfigKey(String payConfigKey) {
		this.payConfigKey = payConfigKey;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public ReqRevoke(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		/**本地订单ID*/
		this.orderId = XMLUtil.getString(dataEl, "OrderId", true);
		this.payConfigKey = XMLUtil.getString(dataEl, "PayConfigKey", true);
		this.payTime = XMLUtil.getString(dataEl, "payTime", false);
	}

	/**
	 * @param msg
	 * @param orderId
	 * @throws AbsHosException
	 */
	public ReqRevoke(InterfaceMessage msg, String orderId,String payConfigKey) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.payConfigKey = payConfigKey;
	}

	public ReqRevoke(InterfaceMessage msg, String orderId,String payConfigKey,String payTime) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.payConfigKey = payConfigKey;
		this.payTime = payTime;
	}
	
	public PgwReqRevoke toPgwReqRevoke() {
		PgwReqRevoke pgwReqRevoke = new PgwReqRevoke();
		pgwReqRevoke.setOrderId(this.orderId);
		return pgwReqRevoke;
	}
}
