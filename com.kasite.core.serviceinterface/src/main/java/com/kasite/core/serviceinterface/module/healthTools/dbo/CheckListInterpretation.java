package com.kasite.core.serviceinterface.module.healthTools.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 检验单解读
 * 
 * @author 無
 *
 */
@Table(name = "check_list_Interpretation")
public class CheckListInterpretation {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;

	@Column(name = "item_code")
	private String itemCode;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "interpretation")
	private String interpretation;

	@Column(name = "status")
	private int status;

	@Column(name = "org_Code")
	private String orgCode;

	@Column(name = "create_Time")
	private String createTime;
	
	@Column(name = "update_Time")
	private String  updateTime;
	
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getInterpretation() {
		return interpretation;
	}

	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
