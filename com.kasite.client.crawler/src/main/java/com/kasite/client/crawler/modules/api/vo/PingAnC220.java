package com.kasite.client.crawler.modules.api.vo;

import com.alibaba.fastjson.JSONObject;

public class PingAnC220 {
	
	private JSONObject json;

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public String getMedicalNum() {
		return json.getString("medicalNum");
	}
	
	public String getAffirmFlg() {
		return json.getString("affirmFlg");
	}
	
	public String getImageFlg() {
		return json.getString("imageFlg");
	}
	
	public String getIndemnitySign() {
		return json.getString("indemnitySign");
	}
	

}
