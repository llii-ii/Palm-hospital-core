package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqOrderCase extends AbsReq{

	
	private String id;
	private String order_id;
	private String case_id;
	private String money;
	private String case_number;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getCase_id() {
		return case_id;
	}
	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCase_number() {
		return case_number;
	}
	public void setCase_number(String case_number) {
		this.case_number = case_number;
	}
	
	
	
	public ReqOrderCase(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id=XMLUtil.getString(ser, "id", false);
		this.order_id=XMLUtil.getString(ser, "order_id", false);
		this.case_id=XMLUtil.getString(ser, "case_id", false);
		this.money=XMLUtil.getString(ser, "money", false);
		this.case_number=XMLUtil.getString(ser, "case_number", false);
		
	}
}
