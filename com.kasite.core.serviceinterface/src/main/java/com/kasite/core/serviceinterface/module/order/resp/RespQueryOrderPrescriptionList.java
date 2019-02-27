package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class RespQueryOrderPrescriptionList extends AbsResp{

	private String serviceId;
	
	private String hisOrderId;
	
	private String prescNo;
	
	private Integer payMoney;
	
	private Integer totalMoney;
	
	private String cardNo;
	
	private Integer cardType;
	
	private Integer ifOnlinePay;
	
	private String serviceName;
	/**
	 * 处方时间
	 */
	private String prescTime;
	
	private Integer orderState;
	
	private String deptName;
	
	private String doctorName;
	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}

	public Integer getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public Integer getIfOnlinePay() {
		return ifOnlinePay;
	}

	public void setIfOnlinePay(Integer ifOnlinePay) {
		this.ifOnlinePay = ifOnlinePay;
	}

	public String getPrescTime() {
		return prescTime;
	}

	public void setPrescTime(String prescTime) {
		this.prescTime = prescTime;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	
}
