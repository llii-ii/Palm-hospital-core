package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
    /**
 *@author caiyouhong
 *@version 1.0 
 *@time 2017-7-26 下午3:49:02 医嘱订单支付
 **/
public class ReqPaySettlementOrder extends AbsReq {
	/** 就诊卡用户名称 */
	private String memberName;
	/** 订单总价 */
	private String price;
	/** 待支付订单id */
	private String orderId;
	/** 支付方式 */
	private String payWay;
	/** 交易流水号 */
	private String transNo;
	/** 交易时间 */
	private String transTime;
	/** 渠道 */
	private String channelId;
//	/** 操作人id */
//	private String operatorId;
//	/** 操作人名称 */
//	private String operatorName;

	public ReqPaySettlementOrder(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		if (service == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.memberName = XMLUtil.getString(service, "MemberName", true);
		this.price = XMLUtil.getString(service, "Price", true);
		this.orderId = XMLUtil.getString(service, "OrderId", true);
		this.payWay = XMLUtil.getString(service, "PayWay", false);
		this.transNo = XMLUtil.getString(service, "TransNo", true);
		this.transTime = XMLUtil.getString(service, "TransTime", true);
		this.channelId = XMLUtil.getString(service, "ChannelId", true);
//		this.operatorId = XMLUtil.getString(service, "OperatorId", false);
//		this.operatorName = XMLUtil.getString(service, "OperatorName", false);
	}


	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getPayWay() {
		return payWay;
	}


	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}


	public String getTransNo() {
		return transNo;
	}


	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}


	public String getTransTime() {
		return transTime;
	}


	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}


	public String getChannelId() {
		return channelId;
	}


	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}


}
