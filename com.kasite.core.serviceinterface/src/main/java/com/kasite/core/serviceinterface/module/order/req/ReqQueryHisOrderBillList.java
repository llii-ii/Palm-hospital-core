package com.kasite.core.serviceinterface.module.order.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjianfa
 * @Description: 查询His账单
 * @version: V1.0  
 * 2017-9-30 下午4:21:01
 */
public class ReqQueryHisOrderBillList extends AbsReq{

	public ReqQueryHisOrderBillList(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);

		Element service = root.element(KstHosConstant.DATA);
		if (service == null) {
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		this.orderId = XMLUtil.getString(service, "OrderId", false);
		this.refundOrderId = XMLUtil.getString(service, "RefundOrderId", false);
		this.merchOrderNo = XMLUtil.getString(service, "MerchOrderNo", false);
		this.hisOrderId = XMLUtil.getString(service, "HisOrderId", false);
		this.hisOrderType = XMLUtil.getString(service, "HisOrderType", false);
		this.beginDate = XMLUtil.getString(service, "BeginDate", false);
		this.endDate = XMLUtil.getString(service, "EndDate", false);
	}

	/**全流程订单ID*/
	private String orderId;
	/**全流程退款ID*/
	private String refundOrderId;
	/**商户订单号*/
	private String merchOrderNo;
	/**HIS的orderID*/
	private String hisOrderId;
	/**查询开始日期*/
	private String beginDate;
	/** 查询结束日期*/
	private String endDate;
	/**his订单类型*/
	private String hisOrderType;


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getMerchOrderNo() {
		return merchOrderNo;
	}

	public void setMerchOrderNo(String merchOrderNo) {
		this.merchOrderNo = merchOrderNo;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getHisOrderType() {
		return hisOrderType;
	}

	public void setHisOrderType(String hisOrderType) {
		this.hisOrderType = hisOrderType;
	}
	
	
}
