package com.kasite.core.serviceinterface.common.cache.service;

import com.kasite.core.serviceinterface.common.cache.dbo.SysCache;

public interface CacheService {

	/**
	 * 保存缓存对象
	 * @param vo
	 * @return 
	 */
	SysCache save(SysCache vo);
	/**
	 * 移除缓存对象
	 * @param id
	 */
	void remove(String id);
	/**
	 * 获取缓存对象
	 * @param id
	 * @return
	 */
	SysCache getSysCache(String id);

	/**
	 * 获取缓存对应的key
	 * @param keyEnum
	 * @param id
	 * @return
	 */
	String getKey(CacheKeyEnum keyEnum, String id);
	
}
