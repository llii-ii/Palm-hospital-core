package com.kasite.core.serviceinterface.module.pay.req;


import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.hos.exception.XmlUtilException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO 查询账单接口请求对象
 */
public class ReqGetBill extends AbsReq {

	private String billDate;
	
	private String serviceId;
	
	private String beginDate;
	
	private String orderId;
	
	private String startDate;
	private String endDate; 
	
	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqGetBill(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.billDate =  XMLUtil.getString(dataEl, "BillDate", true);
		this.serviceId =  XMLUtil.getString(dataEl, "ServiceId", false);
		this.beginDate =  XMLUtil.getString(dataEl, "BeginDate", false);
		this.orderId =  XMLUtil.getString(dataEl, "OrderId", false);
		if(StringUtil.isBlank(beginDate) && StringUtil.isBlank(orderId)){ 
			throw new XmlUtilException("BeginDate和Orderid不能同时为空!");
		}
		
	}

	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqGetBill(InterfaceMessage msg, String startDate, String endDate) throws AbsHosException {
		super(msg);
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
}
