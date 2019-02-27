//package com.yihu.wsgw.bus;
//
//import com.coreframework.log.LogBody;
//import com.coreframework.log.Logger;
//import com.yihu.wsgw.bus.KBusSender.ABusLocker;
//
//public class FutureImpl2 implements ABusFuture {
//
//	final KBusSender sender;
//	/**
//	 * 已经被唤醒的情况下，它才有值
//	 */
//	String result;
//	
//	FutureImpl2(KBusSender sender) {
//		ABus.notNull(sender);
//		this.sender=sender;
//	}
//
//	@Override
//	public String awaitReceive() {
//		ABusLocker locker=this.sender.lock.get();
//		if(locker!=null && this.result ==null){
//			locker.awaitUntil(this.sender.endTime()+2000);
//			if(!locker.waked()){
//				Logger.get().warn("SYS", LogBody.me().set("abus","显式唤醒"));
//				locker.timeout(locker.getId());
//			}
//			this.result = locker.msgResult();
//			return this.result;
//		}
//		return this.result;
//	}
//
//	@Override
//	public String optMessage() {
//		ABusLocker locker=this.sender.lock.get();
//		if(this.result==null && locker!=null && locker.waked()){
//			return locker.msgResult();
//		}
//		return this.result;
//	}
//
//	@Override
//	public boolean isReceived() {
//		ABusLocker locker=this.sender.lock.get();
//		if(locker!=null){
//			return locker.waked();
//		}
//		return this.result != null;
//	}
//
//	@Override
//	public String awaitReceive(long arg0) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
