package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author lq
 * @Description: 本地订单列表
 * @version: V1.0  
 * 2017-07-11 下午18:27:45
 */
public class ReqOrderListLocal  extends AbsReq{
	/**成员ID*/
	private String memberId; 
	/**卡号*/
	private String cardNo; 
	/**卡类型*/
	private String cardType;
	/**订单类型*/
	private String serviceId; 
	/**订单ID*/
	private String orderId; 
	/**处方单号*/
	private String prescNo; 
	/**微信ID*/
	private String openId; 
	/** 开始时间*/
	private String beginDate;
	/**支付状态*/
	private String payState; 
	/**业务状态*/
	private String bizState;
	/**订单终结状态*/
	private String overState;
	/**结束时间*/
	private String endDate;
	/**渠道id*/
	private String channelId;
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPrescNo() {
		return prescNo;
	}
	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getPayState() {
		return payState;
	}
	public void setPayState(String payState) {
		this.payState = payState;
	}
	public String getBizState() {
		return bizState;
	}
	public void setBizState(String bizState) {
		this.bizState = bizState;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	public String getOverState() {
		return overState;
	}
	public void setOverState(String overState) {
		this.overState = overState;
	}
	public ReqOrderListLocal(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		Element service = root.element(KstHosConstant.DATA);
		Element p=ser.element("Page");
		this.cardNo =  XMLUtil.getString(service, "CardNo", false);
		this.memberId =  XMLUtil.getString(service, "MemberId", false);
		this.cardType =  XMLUtil.getString(service, "CardType", false);
		this.serviceId =  XMLUtil.getString(service, "ServiceId", false);
		this.orderId =  XMLUtil.getString(service, "OrderId", false);
		this.prescNo =  XMLUtil.getString(service, "PrescNo", false);
		this.openId =  XMLUtil.getString(service, "OpenId", false);
		this.beginDate = XMLUtil.getString(service, "BeginDate", false);
		this.endDate = XMLUtil.getString(service, "EndDate", false);
		this.payState = XMLUtil.getString(service, "PayState", false);
		this.bizState = XMLUtil.getString(service, "BizState", false);
		this.overState = XMLUtil.getString(service, "OverState", false);
		this.channelId = XMLUtil.getString(service, "ChannelId", false);
		getPage().setPIndex(XMLUtil.getInt(p, "PIndex", 0));
		getPage().setPSize(XMLUtil.getInt(p, "PSize", 50));
		if(StringUtil.isEmpty(memberId) && StringUtil.isEmpty(cardNo) && StringUtil.isEmpty(cardType) &&
				StringUtil.isEmpty(openId) && StringUtil.isEmpty(orderId)) {
			throw new ParamException("传入参数中[MemberId][CardNo][CardType][OpenId][OrderId]节点不能同时为空。");
		}
	}
	/**
	 * @Title: ReqOrderListLocal
	 * @Description: 
	 * @param msg
	 * @param cardNo
	 * @param cardType
	 * @param serviceId
	 * @param orderId
	 * @param prescNo
	 * @param openId
	 * @param beginDate
	 * @param payState
	 * @param bizState
	 * @param overState
	 * @param endDate
	 * @param channelId
	 * @throws AbsHosException
	 */
	public ReqOrderListLocal(InterfaceMessage msg, String cardNo, String cardType, String serviceId, String orderId, String prescNo, String openId, String beginDate, String payState, String bizState, String overState, String endDate, String channelId) throws AbsHosException {
		super(msg);
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.serviceId = serviceId;
		this.orderId = orderId;
		this.prescNo = prescNo;
		this.openId = openId;
		this.beginDate = beginDate;
		this.payState = payState;
		this.bizState = bizState;
		this.overState = overState;
		this.endDate = endDate;
		this.channelId = channelId;
		if(StringUtil.isEmpty(memberId) && StringUtil.isEmpty(cardNo) && StringUtil.isEmpty(cardType) &&
				StringUtil.isEmpty(openId) && StringUtil.isEmpty(orderId)) {
			throw new ParamException("传入参数中[MemberId][CardNo][CardType][OpenId][OrderId]节点不能同时为空。");
		}
	}
	
	
}
