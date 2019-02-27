package com.kasite.client.yy.bean.dbo;

import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

/**取消预约
 * @author lsq
 * version 1.0
 * 2017-7-7下午2:55:24
 */
@Table(name="YY_CANCEL")
public class YyCancel extends BaseDbo{
	
	private String orderId;
	private String cancelReason;
	private String operator;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}
