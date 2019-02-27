package com.kasite.core.common.config;

import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class GoogleAuthConfigVo {
	private String appId;
	private String appSecret;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public JSONObject print(boolean isAll) {
		JSONObject o = (JSONObject) JSON.toJSON(this);
		if(!isAll) {
			Set<String> keys = o.keySet();
			for (String key : keys) {
				if (key.toLowerCase().indexOf("key") >= 0 || key.toLowerCase().indexOf("password") >= 0
						|| key.toLowerCase().indexOf("secret") >= 0 || key.toLowerCase().indexOf("pwd") >= 0
						|| key.toLowerCase().indexOf("cert") >= 0) {
					o.put(key, "******");
				}
			}
		}
		return o;
	}
}
