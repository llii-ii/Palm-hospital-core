package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author lq
 * @Description: API，订单取消完成
 * @version: V1.0  
 * 2017-7-11 下午13:58:57
 */
public class ReqCancelForCompletion extends AbsReq{
	private String orderId;
	private String refundOrderId;
	private String refundNo;
	private String operatorId;
	private String operatorName;
	private Integer refundPrice;
	private Integer totalPrice;
	public String getRefundOrderId() {
		return refundOrderId;
	}
	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getRefundNo() {
		return refundNo;
	}
	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
	public ReqCancelForCompletion(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = this.__DATA__;
		if(dataEl==null){
			throw new ParamException("传入参数中["+ IConstant.DATA +"]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(dataEl, "OrderId", true);
		this.refundOrderId = XMLUtil.getString(dataEl, "RefundOrderId", true);
		this.refundNo = XMLUtil.getString(dataEl, "RefundNo", true);
		this.operatorId =  XMLUtil.getString(dataEl, "OperatorId", false,super.getOpenId());
		this.operatorName =  XMLUtil.getString(dataEl, "OperatorName", false,super.getOperatorName());
	}
	
	
	/**
	 * @param msg
	 * @param orderId
	 * @param refundOrderId
	 * @param refundNo
	 * @param operatorId
	 * @param operatorName
	 * @param refundPrice
	 * @param totalPrice
	 * @throws AbsHosException
	 */
	public ReqCancelForCompletion(InterfaceMessage msg, String orderId, String refundOrderId, String refundNo,
			String operatorId, String operatorName, Integer refundPrice, Integer totalPrice) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.refundOrderId = refundOrderId;
		this.refundNo = refundNo;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		this.refundPrice = refundPrice;
		this.totalPrice = totalPrice;
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
	public Integer getRefundPrice() {
		return refundPrice;
	}
	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
}
