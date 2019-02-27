package com.kasite.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="ocr")
public class OcrIdCardConfig {
	
	private OcrIdCardConfigVo auth;
	
	public OcrIdCardConfig() {
		if(null != auth) {
			KasiteConfig.setValue("ocr.auth.apiKey", auth.getApiKey());
			KasiteConfig.setValue("ocr.auth.apiSecret", auth.getApiSecret());
			KasiteConfig.setValue("ocr.auth.repUrl", auth.getRepUrl());
			KasiteConfig.setValue("ocr.auth.temUrl", auth.getTemUrl());
			KasiteConfig.setValue("ocr.auth.temId", auth.getTemId());
		}
	}

	public OcrIdCardConfigVo getAuth() {
		return auth;
	}

	public void setAuth(OcrIdCardConfigVo auth) {
		this.auth = auth;
	}
	
	
}
