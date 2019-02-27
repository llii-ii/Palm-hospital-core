package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqPriceManage extends AbsReq{

	private String id;//uuid
	private String priceType;//费用类型 1-科室复印  2-手术复印
	private String name;//科室名/手术名
	private String money;//费用
	
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String deptIds;//科室id
	private String preMoney;//预付款
	private String description;//描述
	private String state;//状态 0-删除1-未上架2-上架
	
	public ReqPriceManage(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id=XMLUtil.getString(ser, "id", false);
		this.priceType=XMLUtil.getString(ser, "priceType", false);
		this.name=XMLUtil.getString(ser, "name", false);
		this.money=XMLUtil.getString(ser, "money", false);
		this.startTime=XMLUtil.getString(ser, "startTime", false);
		this.endTime=XMLUtil.getString(ser, "endTime", false);
		this.deptIds=XMLUtil.getString(ser, "deptIds", false);
		this.preMoney=XMLUtil.getString(ser, "preMoney", false);
		this.description=XMLUtil.getString(ser, "description", false);
		this.state=XMLUtil.getString(ser, "state", false);
	}


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


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
