package com.kasite.core.common.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;
import com.kasite.core.common.util.ExpiryMap;
/**
 * 暂时用本地缓存map做 redis 方式
 * @author daiyanshui
 *
 */
public class RedisUtil {
	private static RedisUtil redisUtil = null;
	//默认有效期 10 秒
	private static ExpiryMap<Long, SysUserEntity> map = new ExpiryMap<>(10000);
	//默认有效期 3 秒
	private static ExpiryMap<Long, SysUserTokenEntity> map2 = new ExpiryMap<>(3000);
	/** 30 分钟 */
	private final static long EXPIRY_30 = 1000 * 60 * 30;
	private static ExpiryMap<String, JSONObject> maptemp = new ExpiryMap<>(EXPIRY_30);
	
	public void setK(Long key,SysUserEntity value) {
		map.put(key, value, 30000);
	}
	public void setUserToken(Long key,SysUserTokenEntity value) {
		map2.put(key, value, 5000);
	}
	public void removeToken(Long key) { 
		map2.remove(key);
	}
	public SysUserTokenEntity getUserToken(Long key) {
		return map2.get(key);
	}
	public void setK(Long key,SysUserEntity value,long expiryTime) {
		map.put(key, value, expiryTime);
	}
	public SysUserEntity get(Long key) {
		return map.get(key);
	}
	public static synchronized RedisUtil create() {
		if(null == redisUtil) {
			redisUtil = new RedisUtil();
		}
		return redisUtil;
	}
	private RedisUtil() {
		
	}

	public JSONObject getValue(String key) {
		return maptemp.get(key);
	}
	/**
	 * 默认缓存30分钟 
	 * @param key
	 * @param value
	 */
	public void setJsonObj(String key,JSONObject value) {
		maptemp.put(key, value);
	}
	/**
	 * 
	 * @param key
	 * @param value
	 * @param expiryTime
	 *            键值对有效期 毫秒
	 */
	public void setJsonObj(String key,JSONObject value,long expiryTime) {
		maptemp.put(key, value,expiryTime);
	}
}
