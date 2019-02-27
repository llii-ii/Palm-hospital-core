package com.kasite.core.serviceinterface.module.medicalCopy.req;

import java.sql.Timestamp;

import javax.persistence.Id;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.annotation.KeySql;

public class ReqConsigneeAddress extends AbsReq{


	private String id;
	private String openId;//openId
	private String addressee;//收件人
	private String telephone;//联系电话
	private String province;//省
	private String municipal;//市
	private String county;//区
	private String address;//详细地址
	private String state;//状态：0删除1启用


	
	
	public ReqConsigneeAddress(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id=XMLUtil.getString(ser, "id", false);
		this.openId=XMLUtil.getString(ser, "openId", false);
		this.addressee=XMLUtil.getString(ser, "addressee", false);
		this.telephone=XMLUtil.getString(ser, "telephone", false);
		this.province=XMLUtil.getString(ser, "province", false);
		this.municipal=XMLUtil.getString(ser, "municipal", false);
		this.county=XMLUtil.getString(ser, "county", false);
		this.address=XMLUtil.getString(ser, "address", false);
		this.state=XMLUtil.getString(ser, "applyTime", false);
	}




	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getMunicipal() {
		return municipal;
	}

	public void setMunicipal(String municipal) {
		this.municipal = municipal;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
}
