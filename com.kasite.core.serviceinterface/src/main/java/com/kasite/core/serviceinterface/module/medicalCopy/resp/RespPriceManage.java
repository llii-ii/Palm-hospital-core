package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import java.sql.Timestamp;

import com.kasite.core.common.resp.AbsResp;

public class RespPriceManage extends AbsResp{

	private String id;//uuid
	private String priceType;//费用类型 1-科室复印  2-手术复印
	private String name;//科室名/手术名
	private String money;//费用
	private Timestamp updateTime;//变更时间
	private String deptIds;//科室id
	private String preMoney;//预付款
	private String description;//描述
	private String state;//状态 0-删除1-未上架2-上架
	
	
	
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
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
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
