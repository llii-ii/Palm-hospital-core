package com.kasite.core.common.sys.oauth.service;

import com.kasite.core.common.sys.oauth.entity.AppEntity;

public interface AppService {

	AppEntity getApp(String appId);

}
