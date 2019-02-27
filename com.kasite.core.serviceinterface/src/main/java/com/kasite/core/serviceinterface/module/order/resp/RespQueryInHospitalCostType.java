package com.kasite.core.serviceinterface.module.order.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class RespQueryInHospitalCostType extends AbsResp{

	private String date;
	
	private Integer fee;
	
	private String doctor;
	
	private String dept;
	
	private String deptStation;
	
	private String expenseTypeCode;
	
	private String expenseTypeName;

	/**单位**/
	private String unit;
	/**单价**/
	private Integer unitPrice;
	/**数量**/
	private Integer objNumber;
	/**发票项目名称**/
	private String invoiceItemName;
	/**规格**/
	private String specifications;
	
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
	 * @return the objNumber
	 */
	public Integer getObjNumber() {
		return objNumber;
	}

	/**
	 * @param objNumber the objNumber to set
	 */
	public void setObjNumber(Integer objNumber) {
		this.objNumber = objNumber;
	}

	/**
	 * @return the invoiceItemName
	 */
	public String getInvoiceItemName() {
		return invoiceItemName;
	}

	/**
	 * @param invoiceItemName the invoiceItemName to set
	 */
	public void setInvoiceItemName(String invoiceItemName) {
		this.invoiceItemName = invoiceItemName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDeptStation() {
		return deptStation;
	}

	public void setDeptStation(String deptStation) {
		this.deptStation = deptStation;
	}

	public String getExpenseTypeCode() {
		return expenseTypeCode;
	}

	public void setExpenseTypeCode(String expenseTypeCode) {
		this.expenseTypeCode = expenseTypeCode;
	}

	public String getExpenseTypeName() {
		return expenseTypeName;
	}

	public void setExpenseTypeName(String expenseTypeName) {
		this.expenseTypeName = expenseTypeName;
	}

	
	
}
