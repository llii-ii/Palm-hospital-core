package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO 退费请求入参对象
 */
public class ReqRefund extends AbsReq{
	
	private String IS_CALL_BACK = "IsCallBack";
	
	/**
	 *  商户订单号
	 */
	private String orderId;
	
	/**
	 *退款订单号
	 */
	private String refundOrderId;
	
	/**
	 * 订单总金额
	 */
	private Integer totalPrice;
	
	/**
	 * 退款总金额
	 */
	private Integer refundPrice;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 商户订单号
	 */
	private String transactionNo;
	
	private Integer isCallBack;
	
	private String payConfigKey;
	
	/**
	 * 支付订单的支付时间	YYYYMMDDhhmmss	
	 */
	private String payTime;

	public String getPayConfigKey() {
		return payConfigKey;
	}

	public void setPayConfigKey(String payConfigKey) {
		this.payConfigKey = payConfigKey;
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

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsCallBack() {
		return isCallBack;
	}

	public void setIsCallBack(Integer isCallBack) {
		this.isCallBack = isCallBack;
	}

	
	
	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	/**
	 * @param msg
	 * @throws AbsHosException
	 * @throws ParamException 
	 */
	public ReqRefund(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(dataEl, "OrderId",true);
		this.refundOrderId =  XMLUtil.getString(dataEl, "RefundOrderId",true);
		this.totalPrice =  XMLUtil.getInt(dataEl, "TotalPrice",true);
		this.refundPrice =  XMLUtil.getInt(dataEl, "RefundPrice",true);
		this.remark = XMLUtil.getString(dataEl, "Remark", false);
		this.payConfigKey = XMLUtil.getString(dataEl, "PayConfigKey", true);
		this.transactionNo = XMLUtil.getString(dataEl, "TransactionNo", false);
		this.payTime = XMLUtil.getString(dataEl, "PayTime", false);
		//IsCallBack默认1回调
		if(dataEl.element(IS_CALL_BACK) == null ) {
			this.isCallBack = 1;
		}else {
			this.isCallBack = XMLUtil.getInt(dataEl, "IsCallBack",true);
		}
	}
	
	public ReqRefund(InterfaceMessage msg,String orderId,String refundOrderId,Integer totalPrice,
			Integer refundPrice,String remark,Integer isCallBack,String payConfigKey) throws AbsHosException{
		super(msg);
		this.orderId =  orderId;
		this.refundOrderId =  refundOrderId;
		this.totalPrice =  totalPrice;
		this.refundPrice =  refundPrice;
		this.remark = remark;
		//IsCallBack默认1回调
		if(isCallBack == null) {
			this.isCallBack = 1;
		}else {
			this.isCallBack = isCallBack;
		}
		this.payConfigKey = payConfigKey;
	}
	public ReqRefund(InterfaceMessage msg,String orderId,String refundOrderId,Integer totalPrice,
			Integer refundPrice,String remark,Integer isCallBack,String payConfigKey,String payTime) throws AbsHosException{
		super(msg);
		this.orderId =  orderId;
		this.refundOrderId =  refundOrderId;
		this.totalPrice =  totalPrice;
		this.refundPrice =  refundPrice;
		this.remark = remark;
		//IsCallBack默认1回调
		if(isCallBack == null) {
			this.isCallBack = 1;
		}else {
			this.isCallBack = isCallBack;
		}
		this.payConfigKey = payConfigKey;
		this.payTime = payTime;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public PgwReqRefund toPgwReqRefund() {
		PgwReqRefund pgwReqRefund = new PgwReqRefund();
		pgwReqRefund.setOrderId(this.orderId);
		pgwReqRefund.setPayTime(this.payTime);
		pgwReqRefund.setRefundOrderId(this.refundOrderId);
		pgwReqRefund.setRefundPrice(this.refundPrice);
		pgwReqRefund.setRemark(this.remark);
		pgwReqRefund.setTotalPrice(this.totalPrice);
		pgwReqRefund.setTransactionNo(this.transactionNo);
		pgwReqRefund.setPayConfigKey(this.payConfigKey);
		return pgwReqRefund;
	}
	
	
}
