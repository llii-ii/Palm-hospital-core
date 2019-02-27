//package com.yihu.wsgw.bus;
//
//import com.coreframework.remoting.Url;
//import com.yihu.wsgw.bus.KBusSender.CallInfo;
//
//public class KBusResponse {
//	final String result;
//	final Throwable ex;
//	final String gid;
//	final CallInfo info;
//	
//	KBusResponse(String result, Throwable ex, String gid,CallInfo info) {
//		this.result = result;
//		this.ex = ex;
//		this.gid = gid;
//		this.info=info;
//	}
//	/**
//	 * 被调用的服务器地址,如果不存在就返回null
//	 * @return
//	 */
//	public Url server(){
//		if(this.info==null){
//			return null;
//		}
//		return this.info.url;
//	}
//	public String gid(){
//		return this.gid;
//	}
//	public boolean isSuccess(){
//		return this.ex==null;
//	}
//	/**
//	 * 这个不一定是服务器端返回的信息，也可能是路由找不到等错误信息。这个不为null不代表请求成功
//	 * @return
//	 */
//	public String result() {
//		return result;
//	}
//	/**
//	 * 这个值为null的话，就表示成功.
//	 * 如果超时，会抛出RpcTimeoutException
//	 * 如果远程服务器异常，会抛出RemoteInvokeException
//	 */
//	public Throwable exception() {
//		return ex;
//	}
//	
//}
