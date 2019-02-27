package com.kasite.server.verification.module.app.service;

import java.util.List;

import com.kasite.server.verification.module.app.entity.AppEntity;

public interface AppService {
	AppEntity getApp(String appId) throws Exception;
	List<AppEntity> queryAppList(String appId) throws Exception;
	List<AppEntity> queryAppListByCallAppId(String appId) throws Exception;
	List<AppEntity> queryAppListByOrgCode(String orgCode) throws Exception;
}
