package com.kasite.client.order.bean.dbo;

import javax.persistence.Table;


/**
 * @author linjf
 * TODO
 */
@Table(name="O_SELFREFUND_RECORD_ORDER")
public class SelfRefundRecordOrder {

	private String orderId;
	
	private Long selfRefundRecordId;

	private String refundOrderId;
	
	private String remark;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getSelfRefundRecordId() {
		return selfRefundRecordId;
	}

	public void setSelfRefundRecordId(Long selfRefundRecordId) {
		this.selfRefundRecordId = selfRefundRecordId;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
