package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;


/**
 * @author linjianfa
 * @Description: 查询订单信息 根据订单号查询订单表 o_order 不加状态，所有状态都查询，业务自己判断状态
 * @version: V1.0  
 * 2017-7-4 下午8:27:45
 */
public class ReqGetOrder extends AbsReq{
	
	private String orderId;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public ReqGetOrder(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element service = root.element(KstHosConstant.DATA);
		this.orderId =  XMLUtil.getString(service, "OrderId", true);
	}
	/**
	 * @Title: ReqAddOrderLocal
	 * @Description: 
	 * @param msg
	 * @param orderId
	 * @param prescNo
	 * @param payMoney
	 * @param totalMoney
	 * @param priceName
	 * @param orderMemo
	 * @param cardNo
	 * @param cardType
	 * @param operatorId
	 * @param operatorName
	 * @param serviceId
	 * @param isOnlinePay
	 * @param eqptType
	 * @param merchantType
	 * @param configKey
	 * @param reqOrderExtension
	 * @throws AbsHosException
	 */
	public ReqGetOrder(InterfaceMessage msg, String orderId) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
	}

	
	
}
