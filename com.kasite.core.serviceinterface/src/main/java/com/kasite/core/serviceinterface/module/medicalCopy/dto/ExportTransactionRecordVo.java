package com.kasite.core.serviceinterface.module.medicalCopy.dto;

public class ExportTransactionRecordVo {

	private String eoTime;//订单创建时间
	private Integer orderNum;//订单数
	private String totalMoney;//总额
	private String totalRefundMoney;//总退款
	private String totalInnerMoney;//净利润
	public String getEoTime() {
		return eoTime;
	}
	public void setEoTime(String eoTime) {
		this.eoTime = eoTime;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getTotalRefundMoney() {
		return totalRefundMoney;
	}
	public void setTotalRefundMoney(String totalRefundMoney) {
		this.totalRefundMoney = totalRefundMoney;
	}
	public String getTotalInnerMoney() {
		return totalInnerMoney;
	}
	public void setTotalInnerMoney(String totalInnerMoney) {
		this.totalInnerMoney = totalInnerMoney;
	}
	
	
}
