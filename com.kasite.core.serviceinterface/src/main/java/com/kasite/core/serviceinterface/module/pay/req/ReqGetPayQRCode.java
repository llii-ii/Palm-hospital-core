package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO 获取原生支付二维码请求对象
 */
public class ReqGetPayQRCode extends AbsReq{

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
	 * 前端发起统一下单的远程ip
	 */
	private String remoteIp;
	
	/**
	 * 是否信用卡,channelId=100123时生效
	 */
	private Integer isLimitCredit;
	
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
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqGetPayQRCode(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(dataEl, "OrderId",true);
		this.price =  XMLUtil.getInt(dataEl, "Price",true);
		this.body =  XMLUtil.getString(dataEl, "Body",true);
		this.subject =  XMLUtil.getString(dataEl, "Subject",true);
		this.isLimitCredit = XMLUtil.getInt(dataEl, "IsLimitCredit", false);
		this.remoteIp = XMLUtil.getString(dataEl, "RemoteIp", true);
	}

	/**
	 * @Title: ReqGetPayQRCode
	 * @Description: 
	 * @param msg
	 * @param orderId
	 * @param price
	 * @param body
	 * @param subject
	 * @param merchantType
	 * @param remoteIp
	 * @param isLimitCredit
	 * @throws AbsHosException
	 */
	public ReqGetPayQRCode(InterfaceMessage msg, String orderId, Integer price, String body, String subject,String remoteIp, Integer isLimitCredit) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.price = price;
		this.body = body;
		this.subject = subject;
		this.remoteIp = remoteIp;
		this.isLimitCredit = isLimitCredit;
	}

}
