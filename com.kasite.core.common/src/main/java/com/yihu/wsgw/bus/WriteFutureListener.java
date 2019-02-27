//package com.yihu.wsgw.bus;
//
//import org.apache.mina.core.future.IoFutureListener;
//import org.apache.mina.core.future.WriteFuture;
//
//import com.coreframework.log.Logger;
//import com.coreframework.remoting.reflect.LockMap;
//import com.coreframework.util.AppThreadPool;
//import com.yihu.wsgw.api.DownUrlHolder;
//
//public class WriteFutureListener implements IoFutureListener<WriteFuture> {
//
//	private final KBusSender sender;
//	private final String reqId;
//
//	WriteFutureListener(KBusSender sender, String reqId) {
//		ABus.notNull(sender);
//		this.sender = sender;
//		this.reqId = reqId;
//	}
//
//	//在客户的send线程或者mina的processor线程中执行
//	@Override
//	public void operationComplete(final WriteFuture future) {
//		if (future.getException() == null) {
//			return;
//		}
//		AppThreadPool.CACHE.execute(new Runnable() {
//			@Override
//			public void run() {
//				//如果没有locker，就不会进入到这里。url无法联通也不会调用这个
//				sender.lock.get().wakeup(reqId, future.getException());
//				if(reqId!=null && reqId.length()>0){
//					LockMap.remove(reqId);
//				}
//			}
//		});
//		final com.yihu.wsgw.bus.KBusSender.CallInfo info = KBusSender.callInfo(reqId, sender);
//		if (info != null && info.url != null) {
//			DownUrlHolder.instance().addDownUrl(info.url);
//			Logger.get().console("{} write failed",info.url);
//			AppThreadPool.SINGLE.execute(new Runnable() {
//				@Override
//				public void run() {
//					DownUrlHolder.instance().check(info.url);
//				}
//			});
//		}
//		
//	}
//}
