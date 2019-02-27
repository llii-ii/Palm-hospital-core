package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespConsigneeAddress
 * @author: cjy
 * @date: 2018年9月18日 上午10:20:19
 */
public class RespConsigneeAddress extends AbsResp{
	
	private String id;
	private String openId;//openId
	private String addressee;//收件人
	private String telephone;//联系电话
	private String province;//省
	private String municipal;//市
	private String county;//区
	private String address;//详细地址
	private String state;//状态：0删除1启用
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
