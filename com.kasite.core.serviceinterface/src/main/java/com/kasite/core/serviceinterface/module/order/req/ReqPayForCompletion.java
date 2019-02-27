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
 * @Description: API，订单完成
 * @version: V1.0  
 * 2017-7-11 下午13:58:57
 */
public class ReqPayForCompletion extends AbsReq{
	private String orderId;
	private String transActionNo;
	private Integer price;
	private String accNo;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getTransActionNo() {
		return transActionNo;
	}
	public void setTransActionNo(String transActionNo) {
		this.transActionNo = transActionNo;
	}
	
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public ReqPayForCompletion(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = this.__DATA__;
		if(dataEl==null){
			throw new ParamException("传入参数中["+ IConstant.DATA +"]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(dataEl, "OrderId", true);
		this.transActionNo = XMLUtil.getString(dataEl, "TransActionNo", true);
		this.price = XMLUtil.getInt(dataEl, "Price", false);
	}
	
	/**
	 * @param msg
	 * @param orderId
	 * @param transActionNo
	 * @param price
	 * @throws AbsHosException
	 */
	public ReqPayForCompletion(InterfaceMessage msg, String orderId, String transActionNo, Integer price)
			throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.transActionNo = transActionNo;
		this.price = price;
	}
	
	public ReqPayForCompletion(InterfaceMessage msg, String orderId, String transActionNo, Integer price,String accNo)
			throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.transActionNo = transActionNo;
		this.price = price;
		this.accNo = accNo;
	}
	
}
