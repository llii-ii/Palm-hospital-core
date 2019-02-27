package com.kasite.core.common.util;

import java.util.ArrayList;
import java.util.List;


public class ClearExpiryMapCacheGuard {
	private static ClearExpiryMapCacheGuard me = null;
	private final static Object obj = new Object();
	public static ClearExpiryMapCacheGuard me() {
		if(null == me) {
			synchronized (obj) {
				me = new ClearExpiryMapCacheGuard();
			}
		}
		return me;
	}
	private ClearExpiryMapCacheGuard() {
		ClearThread thread = new ClearThread();
		thread.setDaemon(true);
		thread.start();
	}
	private List<ExpiryMap<?,?>> cacheList = new ArrayList<>();
	public void addGuardExpiryMap(ExpiryMap<?,?> map) {
		cacheList.add(map);
	}
	public void clear() {
		for (ExpiryMap<?, ?> expiryMap : cacheList) {
			expiryMap.entrySet();
		}
	}
	/**
	 * 启动一个清理线程，每小时运行一次，避免内存中有对象没有使用又过期了无法释放。
	 * 确保所有的 ExpiryMap 使用者都将自身 的地址存放进来
	 * @author daiyanshui
	 *
	 */
	private class ClearThread extends Thread {
		ClearThread() {
			setName("ClearLocalCache ExpiryMap");
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
	
}
