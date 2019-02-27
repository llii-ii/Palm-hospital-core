package com.kasite.core.serviceinterface.module.order.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqOrderRefundAfter  extends AbsReq{
	public ReqOrderRefundAfter(InterfaceMessage msg) throws AbsHosException {
		super(msg);
	}
	
	public ReqOrderRefundAfter(InterfaceMessage msg, String refundNo, String refundOrderId,
			ReqOrderIsCancel reqOrderCancel, String orderId, String channelId, String serviceId, String isOnlinePay)
			throws AbsHosException {
		super(msg);
		this.refundNo = refundNo;
		this.refundOrderId = refundOrderId;
		this.reqOrderCancel = reqOrderCancel;
		this.orderId = orderId;
		this.channelId = channelId;
		this.serviceId = serviceId;
		this.isOnlinePay = isOnlinePay;
	}


	private String refundNo;
	private String refundOrderId;
	private ReqOrderIsCancel reqOrderCancel;
	private String orderId;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	private String channelId;
	private String serviceId;
	private String isOnlinePay;
	
	public String getIsOnlinePay() {
		return isOnlinePay;
	}
	public void setIsOnlinePay(String isOnlinePay) {
		this.isOnlinePay = isOnlinePay;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getRefundNo() {
		return refundNo;
	}
	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
	public String getRefundOrderId() {
		return refundOrderId;
	}
	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}
	public ReqOrderIsCancel getReqOrderCancel() {
		return reqOrderCancel;
	}
	public void setReqOrderCancel(ReqOrderIsCancel reqOrderCancel) {
		this.reqOrderCancel = reqOrderCancel;
	}
	
	
}
