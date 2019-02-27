package com.kasite.core.common.sys.oauth.service.impl;

import org.springframework.stereotype.Service;

import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.oauth.entity.AppEntity;
import com.kasite.core.common.sys.oauth.service.AppService;

@Service("appServiceApi")
public class AppServiceImpl implements AppService {

	@Override
	public AppEntity getApp(String appId) {
		return LocalOAuthUtil.getInstall().getApp(appId);
	}

}
