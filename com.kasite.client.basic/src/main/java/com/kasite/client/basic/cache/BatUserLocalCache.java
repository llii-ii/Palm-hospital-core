package com.kasite.client.basic.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.kasite.core.serviceinterface.module.basic.cache.BatUserCache;
import com.kasite.core.serviceinterface.module.basic.cache.IBatUserLocalCache;

/**
 * 
 * @className: BatUserLocalCache
 * @author: lcz
 * @date: 2018年8月8日 下午4:49:49
 */
@Component("batUserLocalCache")
public class BatUserLocalCache implements IBatUserLocalCache{
	
	/**
	 * 默认缓存过期时间 单位ms，默认为1天
	 */
	private static final long DEFAULT_TIMEOUT = 24*60*60*1000;
	/**
	 * 
	 */
	private Map<String, BatUserCache> cacheMap;
	
	public void clear() {
		long now = System.currentTimeMillis();
		for (Entry<String, BatUserCache> entry : cacheMap.entrySet()) {
			BatUserCache cache = entry.getValue();
			if (now - cache.getSyncTime() >= DEFAULT_TIMEOUT) {
				synchronized (cacheMap) {
					cacheMap.remove(entry.getKey());
				}
			}
		}
	}
	
	private class ClearThread extends Thread {
		ClearThread() {
			setName("batUser local cache listener thread");
		}
		public void run() {
			while (true) {
				try {
					clear();
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					try {
						//休眠30分钟
						Thread.sleep(30*60*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
 
	
	@PostConstruct
	public void init() {
		cacheMap = new ConcurrentHashMap<String, BatUserCache>();
		Thread t = new ClearThread();
		t.setDaemon(true);
		t.start();
	}
 
	@Override
	public BatUserCache get(String key) {
		return cacheMap.get(key);
	}
	
	@Override
	public BatUserCache put(String key, BatUserCache user) {
		user.setSyncTime(System.currentTimeMillis());
		synchronized (cacheMap) {
			cacheMap.put(key, user);
		}
		return user;
	}


}
