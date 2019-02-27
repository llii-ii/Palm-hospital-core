package com.kasite.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="kdniao") 
public class KdniaoAuthConfig {
	private KdniaoAuthConfigVo auth;
	
	public KdniaoAuthConfig() {
		if(null != auth) {
			KasiteConfig.setValue("kdniao.auth.appKey", auth.getAppKey());
			KasiteConfig.setValue("kdniao.auth.EBusinessID",auth.getEBusinessID());
			KasiteConfig.setValue("kdniao.auth.eepUrl",auth.getRepUrl());
		}
	}

	public KdniaoAuthConfigVo getAuth() {
		return auth;
	}

	public void setAuth(KdniaoAuthConfigVo auth) {
		this.auth = auth;
	}

}
