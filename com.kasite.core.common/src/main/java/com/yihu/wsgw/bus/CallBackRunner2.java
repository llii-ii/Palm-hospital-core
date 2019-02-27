//package com.yihu.wsgw.bus;
//
//import com.coreframework.log.Logger;
//import com.coreframework.remoting.Url;
//import com.coreframework.remoting.reflect.GIDHolder;
//import com.coreframework.remoting.reflect.TimeOutException;
//import com.coreframework.util.AppThreadPool;
//import com.kasite.core.common.config.KasiteConfig;
//import com.yihu.wsgw.api.DownUrlHolder;
//import com.yihu.wsgw.bus.KBusSender.ABusLocker;
//import com.yihu.wsgw.bus.KBusSender.CallInfo;
//
//public class CallBackRunner2 {
//	/**
//	 * 两个参数都不能为null
//	 * 
//	 * @param locker
//	 * @param sender
//	 */
//	static void invoke(final ABusLocker locker,final KBusSender sender) {
//		final CallBack callback = sender.callback();
//		//无论超时还是有回调方法，都要启动新线程进行处理
//		if (callback == null && !RpcTimeoutException.class.isInstance(locker.exception())) {
//			return;
//		}
//
//		CallInfo info = KBusSender.callInfo(locker.getId(), sender);
//		final KBusResponse resp = new KBusResponse(locker.msgResult(), locker.exception(), sender.gid(), info);
//		AppThreadPool.CACHE.execute(new Runnable() {
//			@Override
//			public void run() {
//				if (callback != null) {
//					String old = GIDHolder.get();
//					try {
//						try {
//							GIDHolder.set(resp.gid());
//							callback.invoke(resp);
//						} finally {
//							GIDHolder.set(old);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//						Logger.get().error("callback",e);
//					}
//					
//				}
//				if(RpcTimeoutException.class.isInstance(resp.ex)){
//					Url url = resp.server();
//					if (url != null) {
//						DownUrlHolder.instance().check(url);
//					}
//				}
//			}
//
//		});
//	}
//
//
//}
