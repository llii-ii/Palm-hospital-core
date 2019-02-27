package com.kasite.core.serviceinterface.module.medicalCopy.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 
 * @className: PriceManage
 * @author: cjy
 * @date: 2018年9月27日 下午2:53:04
 */
@Table(name="TB_PRICE_MANAGE")
public class PriceManage extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys=true)
	private String id;//uuid
	private String priceType;//费用类型 1-科室复印  2-手术复印
	private String name;//科室名/手术名
	private String money;//费用
	private String state;//状态 0-删除1-未上架2-上架
	private String deptIds;//科室id
	private String preMoney;//预付款
	private String description;//描述
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	public String getPreMoney() {
		return preMoney;
	}
	public void setPreMoney(String preMoney) {
		this.preMoney = preMoney;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
