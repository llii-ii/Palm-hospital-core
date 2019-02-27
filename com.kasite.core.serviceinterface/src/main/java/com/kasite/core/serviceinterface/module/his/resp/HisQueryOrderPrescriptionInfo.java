package com.kasite.core.serviceinterface.module.his.resp;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.serviceinterface.module.order.resp.CommonPrescriptionItem;


public class HisQueryOrderPrescriptionInfo extends AbsResp{
	
	@NotBlank(message="his订单号[hisOrderId]  不能为空", groups = {AddGroup.class})
	private String hisOrderId;
	@NotBlank(message="订单类型[serviceId]  不能为空", groups = {AddGroup.class})
	private String serviceId;
	
	private String prescNo;
	@CheckCurrency(message="支付金额[payMoney]  不能为空", groups = {AddGroup.class})
	private Integer payMoney;
	@CheckCurrency(message="订单总金额[totalMoney]  不能为空", groups = {AddGroup.class})
	private Integer totalMoney;
	
	private String priceName;
	
	/**
	 * 订单描述
	 */
	private String orderMemo;
	@NotBlank(message="卡号[cardNo]  不能为空", groups = {AddGroup.class})
	private String cardNo;
	@CheckCurrency(message="卡类型[cardType]  不能为空", groups = {AddGroup.class})
	private Integer cardType;
	
	/**
	 * 是否线上支付订单1是2否
	 */
	@CheckCurrency(message="是否在线缴费[ifOnlinePay]  不能为空", groups = {AddGroup.class})
	private Integer ifOnlinePay;
	@NotBlank(message="订单日期[orderTime]  不能为空", groups = {AddGroup.class})
	private String orderTime;
	@CheckCurrency(message="订单状态[orderState]  不能为空", groups = {AddGroup.class})
	private Integer orderState;
	
	private String doctorCode;
	
	private String doctorName;
	
	private String deptCode;
	
	private String deptName;
	@NotBlank(message="姓名[memberName]  不能为空", groups = {AddGroup.class})
	private String memberName;

	/**
	 * 处方明细项
	 */
	private List<CommonPrescriptionItem> data_1;

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

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getOrderMemo() {
		return orderMemo;
	}

	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
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

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public List<CommonPrescriptionItem> getData_1() {
		return data_1;
	}

	public void setData_1(List<CommonPrescriptionItem> data_1) {
		this.data_1 = data_1;
	}
	
	
	
}
