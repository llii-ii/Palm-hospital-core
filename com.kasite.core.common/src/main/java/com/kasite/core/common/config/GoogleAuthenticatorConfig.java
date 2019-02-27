package com.kasite.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="googleauth") 
public class GoogleAuthenticatorConfig {
	
	private GoogleAuthConfigVo auth;

	public GoogleAuthenticatorConfig() {
		if(null != auth) {
			KasiteConfig.setValue("googleauth.auth.appId", auth.getAppId());
			KasiteConfig.setValue("googleauth.auth.appSecret", auth.getAppSecret());
		}
	}
	
	public GoogleAuthConfigVo getAuth() {
		return auth;
	}

	public void setAuth(GoogleAuthConfigVo auth) {
		this.auth = auth;
	}
	
}
