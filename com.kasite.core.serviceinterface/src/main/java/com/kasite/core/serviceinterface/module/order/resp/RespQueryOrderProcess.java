package com.kasite.core.serviceinterface.module.order.resp;
/**
* @author cyh
* @version 创建时间：2018年1月18日 下午2:09:51
* 类说明
*/
public class RespQueryOrderProcess {
	
	private String orderId;
	
	private String serviceId;
	
	private Integer state;
	
	private String type;
	
	private Integer isOnlinePay;
	
	private String beginDate;
	
	private String endDate;
	
	private Integer refundPrice;
	
	
	public Integer getRefundPrice() {
		return refundPrice;
	}
	public void setRefundPrice(Integer refundPrice) {
		this.refundPrice = refundPrice;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getIsOnlinePay() {
		return isOnlinePay;
	}
	public void setIsOnlinePay(Integer isOnlinePay) {
		this.isOnlinePay = isOnlinePay;
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
	
	
}
