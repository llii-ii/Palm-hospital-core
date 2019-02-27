package com.kasite.server.verification.module.app.service;

import java.util.List;

import com.kasite.server.verification.module.app.entity.AppNotify;

public interface AppNotifyService {
	List<AppNotify> queryAppNotifyList(String appId,String lastReadTime) throws Exception;
	void deleteAppNotify(long id) throws Exception;
}
