package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 
 * @className: OrderCase
 * @author: cjy
 * @date: 2018年9月20日 下午2:53:04
 */
@Table(name="TB_ORDER_CASE")
public class OrderCase extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;
	private String orderId;
	private String caseId;
	private String money;
	private String caseNumber;
	private String copyContentNames;
	private String copyContentIds;
	private String copyUtility;
	private String priceManageId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getCopyContentNames() {
		return copyContentNames;
	}
	public void setCopyContentNames(String copyContentNames) {
		this.copyContentNames = copyContentNames;
	}
	public String getCopyContentIds() {
		return copyContentIds;
	}
	public void setCopyContentIds(String copyContentIds) {
		this.copyContentIds = copyContentIds;
	}
	public String getCopyUtility() {
		return copyUtility;
	}
	public void setCopyUtility(String copyUtility) {
		this.copyUtility = copyUtility;
	}
	public String getPriceManageId() {
		return priceManageId;
	}
	public void setPriceManageId(String priceManageId) {
		this.priceManageId = priceManageId;
	}

	
}
