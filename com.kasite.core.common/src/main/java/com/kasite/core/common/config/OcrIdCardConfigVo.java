package com.kasite.core.common.config;

public class OcrIdCardConfigVo {
	private String apiKey;  //api_key
	private String apiSecret;  //api_secret
	private String repUrl;
	private String temUrl;
	private String temId;
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getApiSecret() {
		return apiSecret;
	}
	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
	public String getRepUrl() {
		return repUrl;
	}
	public void setRepUrl(String repUrl) {
		this.repUrl = repUrl;
	}
	public String getTemUrl() {
		return temUrl;
	}
	public void setTemUrl(String temUrl) {
		this.temUrl = temUrl;
	}
	public String getTemId() {
		return temId;
	}
	public void setTemId(String temId) {
		this.temId = temId;
	}
	
	
}
