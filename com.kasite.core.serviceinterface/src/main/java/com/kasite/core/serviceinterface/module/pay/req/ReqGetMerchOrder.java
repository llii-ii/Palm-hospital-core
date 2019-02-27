package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjianfa
 * @Description: TODO 请求对象
 * @version: V1.0  
 * 2017年10月13日 下午7:05:00
 */
public class ReqGetMerchOrder extends AbsReq{

	
	private String orderId;
	
	private String refundOrderId;
	
	private String channelId;
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public ReqGetMerchOrder(InterfaceMessage msg) throws AbsHosException, ParamException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(dataEl, "OrderId", true);
		this.refundOrderId =  XMLUtil.getString(dataEl, "RefundOrderId", true);
		this.channelId =  XMLUtil.getString(dataEl, "ChannelId", false);
	}
}
