package com.kasite.client.basic.cache;

import com.kasite.core.common.util.ExpiryMap;

public class MemberAutoUnbindCache {
	private static MemberAutoUnbindCache install;
	private final static Object LOCK = new Object();
	/**
	 * 默认缓存1小时
	 */
	private static long defaultExpiryTime = 1 * 60 * 60 * 1000;
	
	private ExpiryMap<String, String> arrayMap = new ExpiryMap<>(defaultExpiryTime);
	
	private MemberAutoUnbindCache() {
		
	}
	
	public boolean hasKey(String key) {
		return arrayMap.containsKey(key);
	}
	
	public void put(String key,String value) {
		arrayMap.put(key, value);
	}
	
	public static MemberAutoUnbindCache me() {
		if(null == install) {
			synchronized (LOCK) {
				install = new MemberAutoUnbindCache();
			}
		}
		return install;
	}
}
