package com.kasite.core.serviceinterface.module.order.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 账单同步请求实体类
 * 
 * @author zhaoy
 *
 */
public class ReqSynchroBill extends AbsReq {

	public ReqSynchroBill(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.orderNo = getDataJs().getString("OrderNo");
			this.orderId = getDataJs().getString("OrderId");
			this.refundOrderId = getDataJs().getString("RefundOrderId");
			this.billType = getDataJs().getInteger("BillType");
			this.configKey = getDataJs().getString("ConfigKey");
			this.merchNo = getDataJs().getString("MerchNo");
			this.hisOrderNo = getDataJs().getString("HisOrderNo");
			this.billDate = getDataJs().getString("BillDate");
		}
	}

	/**全流程订单ID*/
	private String orderNo;
	
	/**全流程支付订单ID*/
	private String orderId;
	
	/**全流程退款订单ID*/
	private String refundOrderId;
	
	/**单边账类型:1.His单边账 2.商户单边账*/
	private int billSingleType;
	
	/**账单类型: 1支付 2退费*/
	private Integer billType;
	
	/**商户配置*/
	private String configKey;
	
	private String merchNo;
	
	private String hisOrderNo;
	
	private String billDate;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

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

	public int getBillSingleType() {
		return billSingleType;
	}

	public void setBillSingleType(int billSingleType) {
		this.billSingleType = billSingleType;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public String getHisOrderNo() {
		return hisOrderNo;
	}

	public void setHisOrderNo(String hisOrderNo) {
		this.hisOrderNo = hisOrderNo;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	
}
