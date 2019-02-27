package com.kasite.server.verification.module.app.service;

public interface AppOnLineService {
	final static Integer ONLINE = 1;
	final static Integer NOT_ONLINE = -1;
	/**
	 * 系统是否在线 不在线则新增 在线则更新最后修改时间。 
	 * @param appId
	 * @throws Exception 
	 */
	void appOnLine(String appId) throws Exception;
}
