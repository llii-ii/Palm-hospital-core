package com.kasite.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="msgcenter") 
public class MsgCenterConfig {
	
	private MsgCenterConfigVo auth;

	public MsgCenterConfig() {
		if(null != auth) {
			KasiteConfig.setValue("msgcenter.auth.state", auth.getState());
			KasiteConfig.setValue("msgcenter.auth.sendMsgUrl", auth.getSendMsgUrl());
			KasiteConfig.setValue("msgcenter.auth.queryWxTemplateListUrl", auth.getQueryWxTemplateListUrl());
		}
	}
	
	public MsgCenterConfigVo getAuth() {
		return auth;
	}

	public void setAuth(MsgCenterConfigVo auth) {
		this.auth = auth;
	}
	
}
