package com.kasite.core.serviceinterface.module.medicalCopy.dto;

public class FengQiaoOrderVo {
	
	private String orderId;//病案订单
	private String expressType;//快件产品编码 默认2
	private String jProvince;//发件人省
	private String jCity;//发件人市
	private String jContact;//发件人姓名
	private String jTel;//发件人电话
	private String jAddress;//发件人相信地址
	private String dProvince;//收件人省
	private String dCity;//收件人市
	private String dContact;//收件人姓名
	private String dTel;//收件人电话
	private String dAddress;//收件人详细地址
	private String parcelQuantity;//包裹数默认1
	private String payMethod;//支付方式
	private String custid;//月卡号
	private String cargo;//寄件名称
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getExpressType() {
		return expressType;
	}
	public void setExpressType(String expressType) {
		this.expressType = expressType;
	}
	public String getjProvince() {
		return jProvince;
	}
	public void setjProvince(String jProvince) {
		this.jProvince = jProvince;
	}
	public String getjCity() {
		return jCity;
	}
	public void setjCity(String jCity) {
		this.jCity = jCity;
	}
	public String getjContact() {
		return jContact;
	}
	public void setjContact(String jContact) {
		this.jContact = jContact;
	}
	public String getjTel() {
		return jTel;
	}
	public void setjTel(String jTel) {
		this.jTel = jTel;
	}
	public String getjAddress() {
		return jAddress;
	}
	public void setjAddress(String jAddress) {
		this.jAddress = jAddress;
	}
	public String getdProvince() {
		return dProvince;
	}
	public void setdProvince(String dProvince) {
		this.dProvince = dProvince;
	}
	public String getdCity() {
		return dCity;
	}
	public void setdCity(String dCity) {
		this.dCity = dCity;
	}
	public String getdContact() {
		return dContact;
	}
	public void setdContact(String dContact) {
		this.dContact = dContact;
	}
	public String getdTel() {
		return dTel;
	}
	public void setdTel(String dTel) {
		this.dTel = dTel;
	}
	public String getdAddress() {
		return dAddress;
	}
	public void setdAddress(String dAddress) {
		this.dAddress = dAddress;
	}
	public String getParcelQuantity() {
		return parcelQuantity;
	}
	public void setParcelQuantity(String parcelQuantity) {
		this.parcelQuantity = parcelQuantity;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	

}
