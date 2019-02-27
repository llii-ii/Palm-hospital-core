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
 * @Description: API，订单支付
 * @version: V1.0  
 * 2017-7-11 下午13:58:57
 */
public class ReqOrderIsPayment extends AbsReq{
	private String orderId;
	private String operatorId;
	private String operatorName;
	private String channelId;
	private String remark;
	private Integer price;

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
		return operatorName;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	public ReqOrderIsPayment(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = this.__DATA__;
		if(dataEl==null){
			throw new ParamException("传入参数中["+ IConstant.DATA +"]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(dataEl, "OrderId", true);
		this.price = XMLUtil.getInt(dataEl, "Price", false);
		this.operatorId = XMLUtil.getString(dataEl, "OperatorId", false,super.getOpenId());
		this.operatorName = XMLUtil.getString(dataEl, "OperatorName", false,super.getOperatorName());
		this.channelId = XMLUtil.getString(dataEl, "ChannelId", true);
		this.remark = XMLUtil.getString(dataEl, "Remark", false);
	}
	public ReqOrderIsPayment(InterfaceMessage msg, String orderId, String operatorId, String operatorName,
			String channelId, String remark, Integer price) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		this.channelId = channelId;
		this.remark = remark;
		this.price = price;
	}
	
	
}
