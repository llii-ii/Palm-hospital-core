package com.kasite.server.verification.module.app.service;

import java.util.List;

import com.kasite.server.verification.module.app.entity.AppConfig;

public interface AppConfigService {
	public  List<AppConfig> getAppConfig(String appId) throws Exception;
}
