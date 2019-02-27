package com.kasite.core.common.sys.verification;

import com.kasite.core.common.util.ExpiryMap;
/**
 * 暂时用本地缓存map做 redis 方式
 * @author daiyanshui
 */
public class VerificaRedisUtil {
	private static VerificaRedisUtil redisUtil = null;
	//默认有效期 1 秒
	private static ExpiryMap<String, String> map = new ExpiryMap<>(5000);
	
	public void setK(String key,String value,long expiryTime) {
		map.put(key, value, expiryTime);
	}
	public String get(String key) {
		return map.get(key);
	}
	public static synchronized VerificaRedisUtil create() {
		if(null == redisUtil) {
			redisUtil = new VerificaRedisUtil();
		}
		return redisUtil;
	}
	private VerificaRedisUtil() {
		
	}
	
}
