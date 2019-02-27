package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqTransactionRecord extends AbsReq{

	private String id;//uuid
	private String orderType;//订单类型 1-支付订单
	private String orderId;//订单id/申请单号
	private String payChannelId;//渠道支付单号
	private String serviceContent;//服务内容
	private String actualReceipts;//实际收款
	private String shouldRefunds;//应退
	private String actualRefunds;//实退
	private String receiptsType;//收款类型 1-已收款
	private String payChannel;//渠道
	private String accountResult;//对账结果 1-长款 2-账平 3-短款
	
	private String startTime;
	private String endTime;
	private String type;//1时间查询2渠道查询
	
	public ReqTransactionRecord(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id=XMLUtil.getString(ser, "id", false);
		this.orderType=XMLUtil.getString(ser, "orderType", false);
		this.orderId=XMLUtil.getString(ser, "orderId", false);
		this.payChannelId=XMLUtil.getString(ser, "payChannelId", false);
		this.serviceContent=XMLUtil.getString(ser, "serviceContent", false);
		this.actualReceipts=XMLUtil.getString(ser, "actualReceipts", false);
		this.shouldRefunds=XMLUtil.getString(ser, "shouldRefunds", false);
		this.actualRefunds=XMLUtil.getString(ser, "actualRefunds", false);
		this.receiptsType=XMLUtil.getString(ser, "receiptsType", false);
		this.payChannel=XMLUtil.getString(ser, "payChannel", false);
		this.accountResult=XMLUtil.getString(ser, "accountResult", false);
		
		this.startTime=XMLUtil.getString(ser, "startTime", false);
		this.endTime=XMLUtil.getString(ser, "endTime", false);
		this.type=XMLUtil.getString(ser, "type", false);
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getPayChannelId() {
		return payChannelId;
	}


	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}


	public String getServiceContent() {
		return serviceContent;
	}


	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}


	public String getActualReceipts() {
		return actualReceipts;
	}


	public void setActualReceipts(String actualReceipts) {
		this.actualReceipts = actualReceipts;
	}


	public String getShouldRefunds() {
		return shouldRefunds;
	}


	public void setShouldRefunds(String shouldRefunds) {
		this.shouldRefunds = shouldRefunds;
	}


	public String getActualRefunds() {
		return actualRefunds;
	}


	public void setActualRefunds(String actualRefunds) {
		this.actualRefunds = actualRefunds;
	}


	public String getReceiptsType() {
		return receiptsType;
	}


	public void setReceiptsType(String receiptsType) {
		this.receiptsType = receiptsType;
	}


	public String getPayChannel() {
		return payChannel;
	}


	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}


	public String getAccountResult() {
		return accountResult;
	}


	public void setAccountResult(String accountResult) {
		this.accountResult = accountResult;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	
	
}
