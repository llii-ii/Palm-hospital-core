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
 * @author linjf
 * TODO 统一下单请求对象
 */
public class ReqWapUniteOrder extends AbsReq{

	/**
	 * 全流程订单ID
	 */
	private String orderId;
	
	/**
	 * 实际支付金额
	 */
	private Integer price;
	
	/**
	 * 订单描述
	 */
	private String body;
	
	/**
	 * 订单标题
	 */
	private String subject;
	
	/**
	 * 下单用户的openId
	 */
	private String openId;

	/**
	 * 渠道ID
	 */
	private String channelId;
	
	/**
	 * 前端发起统一下单的远程ip
	 */
	private String remoteIp;
	
	/**
	 * 是否信用卡,channelId=100123时生效
	 */
	private Integer isLimitCredit;
	
	/**
	 *支付成功前端回跳页面
	 *支付宝、招行启用
	 */
	private String returnUrl;
	
	/**
	 * 支付宝，操作人ID
	 */
	private String alipayOperatorId;

	public String getAlipayOperatorId() {
		return alipayOperatorId;
	}

	public void setAlipayOperatorId(String alipayOperatorId) {
		this.alipayOperatorId = alipayOperatorId;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}



	public Integer getIsLimitCredit() {
		return isLimitCredit;
	}



	public void setIsLimitCredit(Integer isLimitCredit) {
		this.isLimitCredit = isLimitCredit;
	}



	public String getOrderId() {
		return orderId;
	}



	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}



	public Integer getPrice() {
		return price;
	}



	public void setPrice(Integer price) {
		this.price = price;
	}



	public String getBody() {
		return body;
	}



	public void setBody(String body) {
		this.body = body;
	}





	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getOpenId() {
		return openId;
	}



	public void setOpenId(String openId) {
		this.openId = openId;
	}



	public String getChannelId() {
		return channelId;
	}



	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}



	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqWapUniteOrder(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(null != root) {
			Element dataEl = root.element(KstHosConstant.DATA);
			this.orderId =  XMLUtil.getString(dataEl, "OrderId",true);
			this.__CONFIGKEY__ =  XMLUtil.getString(dataEl, "ConfigKey",false,super.getConfigKey());
			this.price =  XMLUtil.getInt(dataEl, "Price",false);
			this.body =  XMLUtil.getString(dataEl, "Body",true);
			this.subject =  XMLUtil.getString(dataEl, "Subject",false);
			this.openId =  XMLUtil.getString(dataEl, "OpenId",false, super.getAuthInfo().getSign());
			this.channelId = XMLUtil.getString(dataEl, "ChannelId",false,super.getClientId());
			this.isLimitCredit = XMLUtil.getInt(dataEl, "IsLimitCredit", false);
			if( KstHosConstant.WX_CHANNEL_ID.equals(channelId)) {
				
			}else if( KstHosConstant.ZFB_CHANNEL_ID.equals(channelId)) {
				this.returnUrl = XMLUtil.getString(dataEl, "ReturnUrl", true);
				this.alipayOperatorId = XMLUtil.getString(dataEl, "AlipayOperatorId", false);
			}
			/** 微信下单时 body节点的入参长度不允许超127个字 */
			if (StringUtil.isNotEmpty(this.body) && this.body.length() > 127) {
				throw new ParamException("统一下单失败， body节点的入参长度不允许超127个字！");
			}
		}
	}

	public ReqWapUniteOrder(InterfaceMessage msg, String orderId, Integer price, String body, String subject,
			String openId, String channelId, String remoteIp, Integer isLimitCredit, String returnUrl,
			String alipayOperatorId) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.price = price;
		this.body = body;
		this.subject = subject;
		this.openId = openId;
		this.channelId = channelId;
		this.remoteIp = remoteIp;
		this.isLimitCredit = isLimitCredit;
		this.returnUrl = returnUrl;
		this.alipayOperatorId = alipayOperatorId;
		/** 微信下单时 body节点的入参长度不允许超127个字 */
		if (StringUtil.isNotEmpty(this.body) && this.body.length() > 127) {
			throw new ParamException("统一下单失败， body节点的入参长度不允许超127个字！");
		}
	}
	
	public PgwReqWapUniteOrder toPgwReqWapUniteOrder() {
		PgwReqWapUniteOrder pgwReqWapUniteOrder = new PgwReqWapUniteOrder();
		pgwReqWapUniteOrder.setReturnUrl(this.returnUrl);
		//pgwReqWapUniteOrder.setAttach();
		pgwReqWapUniteOrder.setBody(this.body);
		pgwReqWapUniteOrder.setIsLimitCredit(this.isLimitCredit);
		pgwReqWapUniteOrder.setOpenId(this.openId);
		pgwReqWapUniteOrder.setOrderId(this.orderId);
		pgwReqWapUniteOrder.setPrice(this.price);
		pgwReqWapUniteOrder.setSubject(this.subject);
		pgwReqWapUniteOrder.setRemoteIp(this.remoteIp);
		return pgwReqWapUniteOrder;
	}

}
