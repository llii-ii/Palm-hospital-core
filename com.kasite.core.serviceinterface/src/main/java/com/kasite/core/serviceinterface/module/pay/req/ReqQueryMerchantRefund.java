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
public class ReqQueryMerchantRefund extends AbsReq {
	
	/**本地订单ID*/
	private String orderId;
	
	/**
	 * 商户订单号
	 */
	private String transactionNo;
	
	/**本地退款订单ID
	 * 在支付宝下，该值与refundNo一样的*/
	private String refundOrderId;
	
	/**
	 * 商户退款订单号
	 */
	private String refundNo;
	
	/**
	 * 退费时间YYYYMMDDhhmmss
	 */
	private String refundTime;
	
	
	

	public ReqQueryMerchantRefund(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.orderId = XMLUtil.getString(dataEl, "OrderId", false);
		this.transactionNo = XMLUtil.getString(dataEl, "TransactionNo", false);
		this.refundNo = XMLUtil.getString(dataEl, "RefundNo", false);
		this.refundOrderId = XMLUtil.getString(dataEl, "RefundOrderId", false);
		this.refundTime = XMLUtil.getString(dataEl, "RefundTime", false);
		if( StringUtil.isEmpty(orderId)&& StringUtil.isEmpty(transactionNo)
				&& StringUtil.isEmpty(refundNo)&& StringUtil.isEmpty(refundOrderId) ) {
			throw new ParamException("参数[OrderId][TransactionNo][RefundNo][RefundOrderId]不能同时为空！");
		}
		if( StringUtil.isEmpty(refundNo)&& StringUtil.isEmpty(refundOrderId) ) {
			throw new ParamException("参数[RefundNo][RefundOrderId]不能同时为空！");
		}
	}
	
	public ReqQueryMerchantRefund(InterfaceMessage msg, String orderId, String refundOrderId, String transactionNo) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.transactionNo = transactionNo;
		this.refundOrderId = refundOrderId;
	}
	
	public ReqQueryMerchantRefund(InterfaceMessage msg, String orderId, String refundOrderId, String transactionNo,String refundTime) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.transactionNo = transactionNo;
		this.refundOrderId = refundOrderId;
		this.refundTime = refundTime;
	}


	public String getRefundOrderId() {
		return refundOrderId;
	}


	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}


	public String getRefundNo() {
		return refundNo;
	}


	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
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

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}
	
	public PgwReqQueryRefundOrder toPgwReqQueryRefundOrder() {
		PgwReqQueryRefundOrder pgwReqQueryRefundOrder = new PgwReqQueryRefundOrder();
		pgwReqQueryRefundOrder.setOrderId(this.orderId);
		pgwReqQueryRefundOrder.setRefundId(this.refundNo);
		pgwReqQueryRefundOrder.setRefundOrderId(this.refundOrderId);
		pgwReqQueryRefundOrder.setTransactionNo(this.transactionNo);
		return pgwReqQueryRefundOrder;
	}
}
