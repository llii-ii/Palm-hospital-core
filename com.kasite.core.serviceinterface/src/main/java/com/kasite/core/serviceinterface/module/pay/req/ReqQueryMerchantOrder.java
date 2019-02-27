package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 当面付请求入参对象
 * 
 * @author 無
 *
 */
public class ReqQueryMerchantOrder extends AbsReq {
	
	/**本地订单ID*/
	private String orderId;
	
	/**
	 * 商户订单号
	 */
	private String transactionNo;
	
	/**
	 * 订单支付时间YYYYMMDDhhmmss,查询银联订单时，必传
	 */
	private String payTime;
	
	/**
	 * 查询商户订单支付成功，是否重新发起商户通知回调（全流程的回调通知）
	 * payClientId,payConfigKey不能为空
	 */
	private Integer isPaySuccessReNotfiy;
	
	private String payClientId;
	
	private String payConfigKey;
	
	
	

	public String getPayClientId() {
		return payClientId;
	}


	public void setPayClientId(String payClientId) {
		this.payClientId = payClientId;
	}


	public String getPayConfigKey() {
		return payConfigKey;
	}


	public void setPayConfigKey(String payConfigKey) {
		this.payConfigKey = payConfigKey;
	}


	public ReqQueryMerchantOrder(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		/**本地订单ID*/
		this.orderId = XMLUtil.getString(dataEl, "OrderId", false);
		this.transactionNo = XMLUtil.getString(dataEl, "TransactionNo", false);
		this.payTime = XMLUtil.getString(dataEl, "PayTime", false);
		if( StringUtil.isEmpty(orderId) && StringUtil.isEmpty(transactionNo)) {
			throw new ParamException("参数[OrderId][TransactionNo]不能同时为空！");
		}
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getTransactionNo() {
		return transactionNo;
	}


	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	

	public String getPayTime() {
		return payTime;
	}


	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}


	/**
	 * @param msg
	 * @param orderId
	 * @param transactionNo
	 * @throws AbsHosException
	 */
	public ReqQueryMerchantOrder(InterfaceMessage msg, String orderId, String transactionNo) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.transactionNo = transactionNo;
	}

	public ReqQueryMerchantOrder(InterfaceMessage msg, String orderId, String transactionNo,String payTime) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.transactionNo = transactionNo;
		this.payTime = payTime;
	}


	/**
	 * @param msg
	 * @param orderId
	 * @param transactionNo
	 * @param payTime
	 * @param isPaySuccessReNotfiy
	 * @throws AbsHosException
	 */
	public ReqQueryMerchantOrder(InterfaceMessage msg, String orderId, String transactionNo, String payTime,
			Integer isPaySuccessReNotfiy,String payClientId,String payConfigKey) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.transactionNo = transactionNo;
		this.payTime = payTime;
		this.isPaySuccessReNotfiy = isPaySuccessReNotfiy;
		this.payClientId = payClientId;
		this.payConfigKey = payConfigKey;
	}


	public Integer getIsPaySuccessReNotfiy() {
		return isPaySuccessReNotfiy;
	}


	public void setIsPaySuccessReNotfiy(Integer isPaySuccessReNotfiy) {
		this.isPaySuccessReNotfiy = isPaySuccessReNotfiy;
	}
	
	public PgwReqQueryOrder toPgwReqQueryOrder() {
		PgwReqQueryOrder pgwReqQueryOrder = new PgwReqQueryOrder();
		pgwReqQueryOrder.setOrderId(this.orderId);
		pgwReqQueryOrder.setTransactionNo(this.transactionNo);
		return pgwReqQueryOrder;
	}
	
	
}
