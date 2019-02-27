package com.kasite.core.common.dao.vo;

/**
 * 渠道信息入参
 * @created 2016-05-27
 * @author lcz
 *
 */
public class ReqAuthInfo{

	private String ClientId;
	private String ClientVersion;
	private String Sign;
	private String SessionKey;
	
	public String getClientId() {
		return ClientId;
	}
	public String getClientVersion() {
		return ClientVersion;
	}
	public String getSign() {
		return Sign;
	}
	public String getSessionKey() {
		return SessionKey;
	}
	public void setClientId(String clientId) {
		ClientId = clientId;
	}
	public void setClientVersion(String clientVersion) {
		ClientVersion = clientVersion;
	}
	public void setSign(String sign) {
		Sign = sign;
	}
	public void setSessionKey(String sessionKey) {
		SessionKey = sessionKey;
	}
	
	
	

	
}
