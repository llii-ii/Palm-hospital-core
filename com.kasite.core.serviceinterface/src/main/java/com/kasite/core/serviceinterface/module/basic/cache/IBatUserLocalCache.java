package com.kasite.core.serviceinterface.module.basic.cache;

/**
 * 第三方用户缓存
 * @className: IBatUserLocalCache
 * @author: lcz
 * @date: 2018年8月8日 下午4:49:16
 */
public interface IBatUserLocalCache {
	
	BatUserCache get(String key);
 
	/**
	 * 设置第三方用户缓存，默认保留1天数据
	 * @Description: 
	 * @param key openId
	 * @param user
	 * @return
	 */
	BatUserCache put(String key, BatUserCache user);
	/**
	 * 清除缓存
	 */
	void clear();
	
}
