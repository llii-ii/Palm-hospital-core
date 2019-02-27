package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.CheckCurrency;
import com.kasite.core.common.validator.group.AddGroup;

/**
 *@author caiyouhong
 *@version 1.0 
 *@time 2017-7-26 下午2:32:30 
 **/
public class HisQueryOrderPrescriptionList  extends AbsResp{
	
	/** 卡号 */
	@NotBlank(message="卡号[cardNo]  不能为空", groups = {AddGroup.class})
	String cardNo;
	/** 卡类型 */
	@CheckCurrency(message="卡类型[cardType]  不能为空", groups = {AddGroup.class})
	Integer cardType;
	/** 是否支持线上支付*/
	Integer ifOnlinePay;
	/** 订单时间 */
	@NotBlank(message="卡号[cardNo]  不能为空", groups = {AddGroup.class})
	String orderTime;
	/** 订单状态 0待支付  1、正在支付2、支付完成3、正在退费4、退费完成 */
	@CheckCurrency(message="订单状态[orderState]  不能为空", groups = {AddGroup.class})
	Integer orderState;
	/** 处方详情说明 */
	String orderMemo;
	/** 应付金额 */
	@CheckCurrency(message="应付金额[payMoney]  不能为空", groups = {AddGroup.class})
	Integer payMoney;
	/** 总金额 */
	@CheckCurrency(message="总金额[totalMoney]  不能为空", groups = {AddGroup.class})
	Integer totalMoney;
	/** id */
	@NotBlank(message="his订单号[hisOrderId]  不能为空", groups = {AddGroup.class})
	String hisOrderId;
	/** 订单类型 */
	@NotBlank(message="订单类型[serviceId]  不能为空", groups = {AddGroup.class})
	String serviceId;
	/** 处方编号 */
	String prescNo;
	/** 订单关联医生 */
	String doctorCode;
	/** 订单关联医生名称 */
	@NotBlank(message="订单关联医生[doctorName]  不能为空", groups = {AddGroup.class})
	String doctorName;
	/** 订单关联科室 */
	String deptCode;
	/** 订单关联科室名称 */
	@NotBlank(message="订单关联科室[deptName]  不能为空", groups = {AddGroup.class})
	String deptName;
	
	/**
	 * 处方 类型
	 */
	String prescType;
	
	public String getPrescType() {
		return prescType;
	}

	public void setPrescType(String prescType) {
		this.prescType = prescType;
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

	public String getHisOrderId() {
		return hisOrderId;
	}

	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
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

	public void setOrderTime(String begin) {
		this.orderTime = begin;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getOrderMemo() {
		return orderMemo;
	}

	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
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
	

}
