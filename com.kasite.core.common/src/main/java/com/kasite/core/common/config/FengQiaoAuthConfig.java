package com.kasite.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="fengqiao") 
public class FengQiaoAuthConfig {
	private FengQiaoAuthConfigVo auth;
	
	public FengQiaoAuthConfig() {
		if(null != auth) {
			KasiteConfig.setValue("kdniao.auth.repUrl", auth.getRepUrl());
			KasiteConfig.setValue("kdniao.auth.customerCode",auth.getCustomerCode());
			KasiteConfig.setValue("kdniao.auth.checkCode",auth.getCheckCode());
			KasiteConfig.setValue("kdniao.auth.custid",auth.getCustid());
		}
	}

	public FengQiaoAuthConfigVo getAuth() {
		return auth;
	}

	public void setAuth(FengQiaoAuthConfigVo auth) {
		this.auth = auth;
	}

	

}
