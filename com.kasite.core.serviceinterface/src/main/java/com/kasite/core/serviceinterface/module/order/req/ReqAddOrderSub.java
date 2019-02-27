package com.kasite.core.serviceinterface.module.order.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public class ReqAddOrderSub extends AbsReq{

	private String orderId;
	 
 	private String subHisOrderId;
 	
 	private Integer price;
 	
 	private String priceName;
 	
 	private String hisRegId;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqAddOrderSub(InterfaceMessage msg) throws AbsHosException {
		super(msg);
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSubHisOrderId() {
		return subHisOrderId;
	}

	public void setSubHisOrderId(String subHisOrderId) {
		this.subHisOrderId = subHisOrderId;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getHisRegId() {
		return hisRegId;
	}

	public void setHisRegId(String hisRegId) {
		this.hisRegId = hisRegId;
	}

	/**
	 * @param msg
	 * @param orderId
	 * @param subHisOrderId
	 * @param price
	 * @param priceName
	 * @throws AbsHosException
	 */
	public ReqAddOrderSub(InterfaceMessage msg, String orderId, String subHisOrderId, Integer price,
			String priceName,String hisRegId) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.subHisOrderId = subHisOrderId;
		this.price = price;
		this.priceName = priceName;
		this.hisRegId = hisRegId;
	}

	

}
