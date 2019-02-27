package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import com.kasite.core.common.resp.AbsResp;

public class RespVerification extends AbsResp{

	private String token;
	private String picId;
	private String pcId;
	private String names;
	private String idCards;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPicId() {
		return picId;
	}
	public void setPicId(String picId) {
		this.picId = picId;
	}
	public String getPcId() {
		return pcId;
	}
	public void setPcId(String pcId) {
		this.pcId = pcId;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	public String getIdCards() {
		return idCards;
	}
	public void setIdCards(String idCards) {
		this.idCards = idCards;
	}
	
	
	
}
