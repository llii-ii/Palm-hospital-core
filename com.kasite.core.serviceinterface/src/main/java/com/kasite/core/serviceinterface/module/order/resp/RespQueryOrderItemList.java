package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

public class RespQueryOrderItemList extends AbsResp{
	
	private String orderId;
	 
 	private String hisOrderId;
 	
 	private Integer unitPrice;
 	
 	private Integer sumOfMoney;
 	
 	private String specifications;
 	
 	private String project;
 	
 	private String number;
 	
 	private String unit;

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the hisOrderId
	 */
	public String getHisOrderId() {
		return hisOrderId;
	}

	/**
	 * @param hisOrderId the hisOrderId to set
	 */
	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}

	/**
	 * @return the unitPrice
	 */
	public Integer getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the sumOfMoney
	 */
	public Integer getSumOfMoney() {
		return sumOfMoney;
	}

	/**
	 * @param sumOfMoney the sumOfMoney to set
	 */
	public void setSumOfMoney(Integer sumOfMoney) {
		this.sumOfMoney = sumOfMoney;
	}

	/**
	 * @return the specifications
	 */
	public String getSpecifications() {
		return specifications;
	}

	/**
	 * @param specifications the specifications to set
	 */
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
 	
 	
 	
	
}
