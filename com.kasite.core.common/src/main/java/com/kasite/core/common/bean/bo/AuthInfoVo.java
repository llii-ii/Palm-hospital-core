package com.kasite.core.common.bean.bo;

import com.alibaba.fastjson.JSON;

/**
 * json.put("ClientId", KstHosConstant.ClientId);				 渠道ID
		json.put("ClientVersion", KstHosConstant.ClientVersion); 保存医院id
		json.put("Sign", KstHosConstant.Sign);					 用户唯一ID
		json.put("SessionKey", KstHosConstant.SessionKey);		 会话ID
 * 
 * @author daiyanshui
 *
 */
public class AuthInfoVo {
	private String ConfigKey;
	private String ClientVersion;
	private String Sign;
	private String SessionKey;
	private String ClientId;
	private String Uuid;
	
	public String getUuid() {
		return Uuid;
	}
	public void setUuid(String uuid) {
		Uuid = uuid;
	}
	public String getConfigKey() {
		return ConfigKey;
	}
	public void setConfigKey(String configKey) {
		ConfigKey = configKey;
	}
	public String getClientVersion() {
		return ClientVersion;
	}
	public void setClientVersion(String clientVersion) {
		ClientVersion = clientVersion;
	}
	public String getSign() {
		return Sign;
	}
	public void setSign(String sign) {
		Sign = sign;
	}
	public String getSessionKey() {
		return SessionKey;
	}
	public void setSessionKey(String sessionKey) {
		SessionKey = sessionKey;
	}
	public String getClientId() {
		return ClientId;
	}
	public void setClientId(String clientId) {
		ClientId = clientId;
	}
	
	
	
	@Override
	public String toString() {
		return JSON.toJSON(this).toString();
	}
	
}
