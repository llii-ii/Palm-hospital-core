package com.kasite.client.crawler.modules.api.vo;

import com.alibaba.fastjson.JSONObject;

public class PingAnC100 {
	
	private JSONObject json;

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public String getCredentialType() {
		return json.getString("credentialType");
	}

	public String getBirthday() {
		return json.getString("birthday");
	}

	public String getAreaCode() {
		return json.getString("areaCode");
	}

	public String getCaseNum() {
		return json.getString("caseNum");
	}

	public String getCredentialNum() {
		return json.getString("credentialNum");
	}

	public String getGender() {
		return json.getString("gender");
	}

	public String getTreatEndDate() {
		return json.getString("treatEndDate");
	}

	public String getPhone() {
		return json.getString("phone");
	}

	public String getAreaName() {
		return json.getString("areaName");
	}

	public String getName() {
		return json.getString("name");
	}

	public String getHospNum() {
		return json.getString("hospNum");
	}

	public String getTreatBeginDate() {
		return json.getString("treatBeginDate");
	}

}
