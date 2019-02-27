package com.kasite.core.serviceinterface.module.order.req;
import org.dom4j.Element;

import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.util.wxmsg.StringUtil;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author lq
 * @Description: API，订单取消
 * @version: V1.0  
 * 2017-7-11 下午13:58:57
 */
public class ReqOrderIsCancel extends AbsReq{
	private String orderId;
	private String operatorId;
	private String operatorName;
	private String channelId;
	private String reason;
	private Integer price;
	private Integer refundPrice;
	private String outRefundOrderId;
	private String transactionNo;
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return this.operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
	public String getOutRefundOrderId() {
		return outRefundOrderId;
	}

	public void setOutRefundOrderId(String outRefundOrderId) {
		this.outRefundOrderId = outRefundOrderId;
	}
	
	public ReqOrderIsCancel(InterfaceMessage msg, String type) throws AbsHosException {
		super(msg);
		this.orderId = getDataJs().getString("OrderId");
		this.price = getDataJs().getInteger("Price");
		this.refundPrice = getDataJs().getInteger("RefundPrice");
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.channelId = getDataJs().getString("ChannelId");
		this.reason = getDataJs().getString("Reason");
		this.outRefundOrderId = getDataJs().getString("outRefundOrderId");
	}

	public ReqOrderIsCancel(InterfaceMessage msg,String orderId,Integer price,Integer refundPrice,
			String operatorId,String operatorName,String channelId,String reason) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.price = price;
		this.refundPrice = refundPrice;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.channelId = channelId;
		this.reason = reason;
	}
	public ReqOrderIsCancel(InterfaceMessage msg,String orderId,Integer price,Integer refundPrice,
			String operatorId,String operatorName,String channelId,String reason,String outRefundOrderId) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.price = price;
		this.refundPrice = refundPrice;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.channelId = channelId;
		this.reason = reason;
		this.outRefundOrderId = outRefundOrderId;
	}
	public ReqOrderIsCancel(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = this.__DATA__;
		if(dataEl==null){
			throw new ParamException("传入参数中["+ IConstant.DATA +"]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(dataEl, "OrderId", false);
		this.price = XMLUtil.getInt(dataEl, "Price", false);
		this.refundPrice = XMLUtil.getInt(dataEl, "RefundPrice", true);
		this.operatorId = XMLUtil.getString(dataEl, "OperatorId", false,super.getOpenId());
		this.operatorName = XMLUtil.getString(dataEl, "OperatorName", false,super.getOperatorName());
//		this.operatorId = XMLUtil.getString(dataEl, "OperatorId", false);
//		//兼容附二旧版本当面付
//		if(StringUtil.isEmpty(operatorId)){
//			this.operatorId = XMLUtil.getString(dataEl, "operatorId", false,super.getOpenId());
//		}
//		this.operatorName = XMLUtil.getString(dataEl, "OperatorName", false);
//		//兼容附二旧版本当面付
//		if(StringUtil.isEmpty(operatorName)){
//			this.operatorName = XMLUtil.getString(dataEl, "operatorName", false,super.getOperatorName());
//		}
		this.channelId = XMLUtil.getString(dataEl, "ChannelId", false);
		this.reason = XMLUtil.getString(dataEl, "Reason", false);
		this.outRefundOrderId = XMLUtil.getString(dataEl, "OutRefundOrderId", false);
//		if(StringUtil.isEmpty(outRefundOrderId)){
//			this.outRefundOrderId = XMLUtil.getString(dataEl, "hisRefundId", true);
//		}
		this.transactionNo = XMLUtil.getString(dataEl, "TransactionNo", false);
		if( StringUtil.isEmpty(orderId) && StringUtil.isEmpty(transactionNo)) {
			throw new ParamException("[OrderId][TransactionNo]不能同时为空！");
		}
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
	
	
}
