package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class CommonPrescriptionItem extends AbsResp{

	/**
	 * 单价
	 */
	private Integer unitPrice;
	/**
	 * 总额
	 */
	private Integer sumOfMoney;
	/**
	 * 规格
	 */
	private String specifications;
	/**
	 * 项目名
	 */
	private String project;
	/**
	 * 数量
	 */
	private String number;

	/**
	 * 单位
	 * @return
	 */
	private String unit;
	
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getSumOfMoney() {
		return sumOfMoney;
	}

	public void setSumOfMoney(Integer sumOfMoney) {
		this.sumOfMoney = sumOfMoney;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}
