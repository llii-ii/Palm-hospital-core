package com.kasite.core.serviceinterface.module.order.req;

import java.util.List;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqAddOrderItem extends AbsReq{

	private String orderId;
	 
 	private String hisOrderId;
 	
 	private Integer unitPrice;
 	
 	private Integer sumOfMoney;
 	
 	private String specifications;
 	
 	private String project;
 	
 	private String number;
	
	private List<?> itemList;

	public ReqAddOrderItem(InterfaceMessage msg, String orderId, String hisOrderId, Integer unitPrice,
			Integer sumOfMoney, String specifications, String project, String number,List<?> itemList) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.hisOrderId = hisOrderId;
		this.unitPrice = unitPrice;
		this.sumOfMoney = sumOfMoney;
		this.specifications = specifications;
		this.project = project;
		this.number = number;
		this.itemList = itemList;
	}

	/**
	 * @return the itemList
	 */
	public List<?> getItemList() {
		return itemList;
	}

	/**
	 * @param itemList the itemList to set
	 */
	public void setItemList(List<?> itemList) {
		this.itemList = itemList;
	}

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
	 * @return the unitePrice
	 */
	public Integer getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitePrice the unitePrice to set
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
	
	
	
}
