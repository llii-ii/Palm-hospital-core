package com.kasite.core.common.sys;

public enum KasiteSysApiEnum {
	API_GETPUBLICKEY("/gateway/verificat/getPublicKey"),
	API_GETTOKENURL("/api/verificat/getToken"),
	API_GETCEBETRTOKENURL("/gateway/verificat/getToken"),
	API_GETMESSAGESTATUS("/gateway/message/queryMessageList"),
	API_GETORGROUTE("/gateway/route/getAppRoute"),
	API_GETAPPCONFIG("/gateway/route/getAppConfig"),
	API_queryPrivateKeyList("/gateway/route/queryPrivateKeyList"),
	;
	private String api;
	KasiteSysApiEnum(String api){
		this.api = api;
	}
	public String getApis(String[] urls) {
		StringBuffer apiUrl = new StringBuffer();
		for (String url : urls) {
			if(url.indexOf("http") < 0) {
				apiUrl.append("http://");
			}
			apiUrl.append(url).append(api).append(",");
		}
		apiUrl.deleteCharAt(apiUrl.length()-1);
		return apiUrl.toString();
	}
}
