//package com.yihu.wsgw.bus;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicReference;
//import java.util.concurrent.locks.LockSupport;
//
//import com.alibaba.fastjson.JSON;
//import com.common.json.JSONObject;
//import com.coreframework.log.LogBody;
//import com.coreframework.log.LogLevel;
//import com.coreframework.log.Logger;
//import com.coreframework.remoting.RemoteInvokeException;
//import com.coreframework.remoting.Url;
//import com.coreframework.remoting.reflect.GIDHolder;
//import com.coreframework.remoting.reflect.LockMap;
//import com.coreframework.remoting.reflect.Request;
//import com.coreframework.remoting.reflect.Response;
//import com.coreframework.remoting.reflect.ResultLock;
//import com.coreframework.remoting.reflect.RpcException;
//import com.coreframework.util.AppConfig;
//import com.kasite.core.common.bean.bo.AuthInfoVo;
//import com.kasite.core.common.config.KasiteConfig;
//import com.kasite.core.common.exception.ParamException;
//import com.kasite.core.common.exception.RRException;
//import com.kasite.core.common.sys.KBus;
//import com.kasite.core.common.sys.verification.VerificationBuser;
//import com.kasite.core.common.sys.verification.vo.TokenVo;
//import com.kasite.core.common.util.DesUtil;
//import com.kasite.core.httpclient.http.HttpRequestBus;
//import com.kasite.core.httpclient.http.HttpRequstBusSender;
//import com.kasite.core.httpclient.http.RequestType;
//import com.kasite.core.httpclient.http.SoapResponseVo;
//import com.kasite.core.httpclient.http.StringUtils;
//import com.yihu.config.Version;
//import com.yihu.wsgw.api.DownUrlHolder;
//import com.yihu.wsgw.api.InterfaceMessage;
//import com.yihu.wsgw.api.KasiteRouteParser;
//import com.yihu.wsgw.vo.MappingRouteVo;
//
//public class KBusSender {
//	
//	final String callRpcApi = "api/rpc";
//	
//	static class CallInfo {
//		InterfaceMessage message;
//		Request request;
//		Url url;
//		long start;
//	}
//
//	private static final Class<?>[] PARAM_TYPE = new Class<?>[] { InterfaceMessage.class };
//	private static final AtomicInteger URL_SEED = new AtomicInteger(0);
//	
//	
//	final String params;
//	final String api;
//	private int paramType;
//	private AuthInfoVo authInfoString;
//	private int outType;// 0是json,1 xml
//	private String appId;
//	private String gid;
//	private String orgCode;
//	
//	// private int reSendPolicy;
//	// int sendTimeout;
//	private int totalTimeout = AppConfig.getInteger("abus.timeout", 30000);
//	/**
//	 * 如果指定了这个值，那么只会用这个值进行通信
//	 */
//	private Url[] servers;
//
//	private boolean failIfServerIsUnEnable;
//
//	/**
//	 * 如果这个对象不为空（绝对不会为null），就表示已经发送过请求了。目前是重试
//	 */
//	private final List<CallInfo> rpcInfos = new ArrayList<CallInfo>(2);
//
//	final AtomicReference<ABusLocker> lock = new AtomicReference<ABusLocker>();
//
//	private final AtomicReference<FutureImpl2> future = new AtomicReference<FutureImpl2>();
//
//	private long startRPC;
//	private CallBack callback;
//
//	long endTime() {
//		return this.startRPC + this.totalTimeout;
//	}
//
//	public int paramType() {
//		return paramType;
//	}
//
//	public KBusSender paramType(int paramType) {
//		this.paramType = paramType;
//		return this;
//	}
//
//	public AuthInfoVo authInfoString() {
//		return authInfoString;
//	}
//
//	public KBusSender authInfoString(AuthInfoVo authInfoString) {
//		this.authInfoString = authInfoString;
//		return this;
//	}
//
//	public int outType() {
//		return outType;
//	}
//
//	public KBusSender outType(int outType) {
//		this.outType = outType;
//		return this;
//	}
//
//	public int totalTimeout() {
//		return totalTimeout;
//	}
//
//	public KBusSender totalTimeout(int totalTimeout) {
//		if (totalTimeout < 1000) {
//			throw new RuntimeException("timeout 设得太小了");
//		}
//		this.totalTimeout = totalTimeout;
//		return this;
//	}
//
//	public KBusSender callback(CallBack back) {
//		this.callback = back;
//		return this;
//	}
//
//	CallBack callback() {
//		return this.callback;
//	}
//
//	String gid() {
//		return this.gid;
//	}
//
//	/**
//	 * 
//	 * @param failIfServerisUnEnable
//	 *            如果为true，当server已经加入黑名单的情况下，请求会直接失败。 注意这个开关不是精确控制的
//	 * @param servers
//	 *            直接调用的地址
//	 * @return
//	 */
//	public KBusSender directCallServer(boolean failIfServerIsUnEnable, Url... servers) {
//		this.servers = servers;
//		this.failIfServerIsUnEnable = failIfServerIsUnEnable;
//		return this;
//	}
//
//	// public KBusSender reSendIfSendFail(){
//	// this.reSendPolicy |=RESEND_IF_SEND_FAIL;
//	// return this;
//	// }
//	//
//	// /**
//	// * 需要设置了sendTimeout，这个方法才会起作用
//	// * @return
//	// */
//	// public KBusSender reSendIfSendTimeout(){
//	// this.reSendPolicy |=RESEND_IF_SEND_TIMEOUT;
//	// return this;
//	// }
//	/**
//	 * @param api
//	 * @param params
//	 * @throws Exception 
//	 */
//	public KBusSender(String orgCode, String appId, String api, String params) throws Exception {
//		this.api = api;
//		this.params = params;
//		this.orgCode = orgCode;
//		this.appId = appId;
//	}
//
////	/**
////	 * send()之后就不能调用任何KBusSender的方法
////	 * 
////	 * @return
////	 */
////	public ABusFuture send() {
////		FutureImpl2 future = new FutureImpl2(this);
////		if (!this.future.compareAndSet(null, future)) {
////			RuntimeException ex = new RuntimeException("不能多次发送");
////			ex.printStackTrace();
////			Logger.get().error(ex);
////			throw ex;
////		}
////		this.startRPC = System.currentTimeMillis();
////		this.gid = GIDHolder.get();
////		Result callResult = this.send0();
////		if (callResult.state != CallState.SENDING) {// 暂时不做重试，所以只要不是成功，就是失败
////			return fatal(future, callResult.result);
////		}
////		return future;
////	}
//	/**
//	 * send()之后就不能调用任何KBusSender的方法
//	 * 
//	 * @return
//	 * @throws Exception 
//	 */
//	public SoapResponseVo send() throws Exception {
//		return sendhttp(appId);
//	}
//
//	FutureImpl2 fatal(FutureImpl2 future, String result) {
//		ABusLocker locker = this.lock.get();
//		if(locker==null){
//			locker=new ABusLocker("", this);
//		}
//		locker.msgResult=result;
//		locker.wakeup(locker.getId(), new RuntimeException(result));
//		future.result = result;
//		return future;
//	}
//
//	private static String DEFAULT_AUTH_INFO;
//
////	private String createAuthInfo() {
////		if (DEFAULT_AUTH_INFO != null) {
////			return DEFAULT_AUTH_INFO;
////		}
////		JSONObject authInfo = new JSONObject();
////		try {
////			authInfo.put("ClientId", this.appId);
////			authInfo.put("ClientVersion", "abus-" + Version.ver);
////			DEFAULT_AUTH_INFO = authInfo.toString();
////		} catch (Exception e) {
////		}
////
////		return DEFAULT_AUTH_INFO;
////	}
//	
//	public SoapResponseVo sendhttp(String appId) throws Exception {
//		if (authInfoString == null ) {
//			throw new ParamException("鉴权参数： authInfo 不能为空。");
//		}
//		String[] tmp = api.split("\\.");
//		
//		boolean isHttp = false;
//		
//		MappingRouteVo vo = KasiteRouteParser.getInstance().queryMappingRouteVo_NoException(api+"@"+appId, params);
//		String clazz = vo.getRemoteClass();
//		if(clazz!=null && clazz.indexOf("/")>=0) {
//			//如果路径是 api/b 说明是通过 rest方式调用。如果是  class的全路径说明是通过 rpc的方式访问。但是在对端都是通过 http进行访问只是2者访问的路径不一样
//			clazz = clazz +"/"+tmp[2];
//			isHttp = true;
//		}
//		Url[] urlss = vo.getUrl();
//		String[] urls = new String[urlss.length];
//		for (int i = 0; i < urlss.length; i++) {
//			Url url = urlss[i];
//			if(null != url && StringUtils.isNotBlank(url.getIp())) {
//				if(isHttp) {
//					urls[i] = url.getIp()+"/"+clazz;
//				}else {
//					urls[i] = url.getIp()+"/"+callRpcApi;
//				}
//			}else {
//				throw new RRException("未找到机构路由对应的接口地址，请查看配置文件中是否包含该模块的地址：【 proxyUrl 】 orgCode,module = "+orgCode+","+tmp[0]);
//			}
//		}
//		
//		String access_token = null;
//		//如果是连接中心访问的
////		String getTokenApi = KasiteConfig.getTokenApi();
//		String getTokenApi = "api.verificat.getToken";
//		String getTokenApiTemp = getTokenApi+"@"+appId;//获取指定应用的 access_token 的api调用地址。
//		TokenVo tokenVo = VerificationBuser.create().getToken(getTokenApiTemp);
//		if(null == tokenVo) {
//			MappingRouteVo vo2 = KasiteRouteParser.getInstance().queryMappingRouteVo_NoException(getTokenApiTemp, params);
//			tokenVo = VerificationBuser.create().getToken(appId, getTokenApi, vo2);
//		}
//		if(null == tokenVo) {
//			throw new RRException("请求鉴权失败，未获取到对方的 access_token 认证。");
//		}
//		String p = DesUtil.encrypt(params, "utf-8");
//		access_token = tokenVo.getAccess_token();
//		HttpRequstBusSender sender = HttpRequestBus.create("", RequestType.post);
//		sender.addHttpParam("params", p)
//		.addHttpParam("api", api)
//		.addHttpParam("outType", "1")
//		.addHttpParam("paramType", "1")
////		.addHttpParam("authInfoString", "{'clientId':"+ appId +"}")
//		.addHttpParam("authInfoString", authInfoString.toString())
//		.setHeaderHttpParam("access_token", access_token)
//		.setHeaderHttpParam("app_id", appId)
//		.setHeaderHttpParam("org_code", orgCode);
//		SoapResponseVo retVo = sender.sendCluster(urls);
//		if(null != retVo) {
//			String result = retVo.getResult();
//			if(StringUtils.isNotBlank(result)) {
//				com.alibaba.fastjson.JSONObject json = JSON.parseObject(result);
//				if(json.getInteger("code")==0 || json.getInteger("code") == 10000) {
//					String res = json.getString("result");
//					if(StringUtils.isNotBlank(res)) {
//						retVo.setResult(DesUtil.decrypt(res, "utf-8"));
//					}
//				}else {
//					throw new RRException("请求失败："+json.getString("msg"));
//				}
//			}
//		}
//		return retVo;
//	}
//
////	private Result send0() {
////		CallInfo info = new CallInfo();
////		info.start = System.currentTimeMillis();
////		if (authInfoString == null || authInfoString.isEmpty()) {
////			authInfoString = createAuthInfo();
////		}
////		if (api == null || api.split("\\.").length != 3) {
////			return Result.fatal(getRetVal(outType, 20000, "api参数缺失"));
////		}
////		String[] tmp = api.split("\\.");
////		if (tmp.length != 3) {
////			return Result.fatal(getRetVal(outType, 20000, "api参数值格式错误"));
////		}
////		MappingRouteVo vo = KasiteRouteParser.getInstance().queryMappingRouteVo_NoException(api, params);
//////		MappingRouteVo vo = RouteParser.getInstance(this.zkService).queryMappingRouteVo_NoException(api, params);
////		if (vo == null) {
////			return Result.fatal(getRetVal(outType, 20001, api + ",路由解析错误"));
////		}
////		String className = vo.getRemoteClass();
////		String methodName = tmp[2];
////		if (vo.getUrl() == null) {
////			return Result.fatal(getRetVal(outType, 20002, "未找到" + api + "可用的路由地址"));
////		}
////		Url url = null;
////		Url[] _urls = vo.getUrl();// 不能改这里面的东西
////		url = chooseUrl(_urls);
////		if (url == null) {
////			if (_urls == null || _urls.length == 0) {
////				return Result.fatal(getRetVal(outType, 20002, "未找到" + api + "可用的路由地址len == 0"));
////			}
////			String msg = api + ",目前的url都无法正常使用";
////			return Result.fatal(getRetVal(outType, 20013, msg));
////		}
////		if (className == null || className.equals("")) {
////			return Result.fatal(getRetVal(outType, 20012, api + ",className不能为空"));
////		}
////
////		InterfaceMessage message = new InterfaceMessage();
////		message.setApiName(api);
////		message.setAuthInfo(authInfoString);
////		message.setOutType(outType);
////		message.setParam(params);
////		message.setParamType(paramType);
////		message.setSeq(Long.toString(info.start));
////		message.setClientIp(NetworkUtil.getLocalIP());
////
////		try {
////			info.url = url;
////			info.message = message;
////			String reqId = IDSeed.next();
////			Request request = new Request(reqId);
////			request.setReceiveResult(true);
////			request.setRemoteInterfaceClassName(className);
////			request.setReqVersion(this.gid);
////			request.setMethodName(methodName);
////			request.setParamTypes(PARAM_TYPE);
////			request.setArgs(new Object[] { info.message });
////			info.request = request;
////			this.rpcInfos.add(info);
////
////			ABusLocker locker = new ABusLocker(reqId, this);
////			if (!this.lock.compareAndSet(null, locker)) {// 说明已经存在locker了
////				locker = this.lock.get();
////				locker.updateId(request.getReqId());
////			}
////
////			if (!LockMap.store(locker.getId(), locker)) {
////				RuntimeException ex = new RuntimeException(locker.getId()+" - reqId重复了 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
////				ex.printStackTrace();
////				Logger.get().error("SYS", ex);
////				throw ex;
////			}
////			TimeoutHandler.inst.add(reqId, this.startRPC + this.totalTimeout);
////			Client client = Client.getInstance(info.url, 5000);// 5000是socket的connectionTimeout
////			WriteFuture writeFuture = client.sendAsync(request);
////			writeFuture.addListener(new WriteFutureListener(this, reqId));// 跟setValue之间是同步的，所以不存在并发问题
////			return Result.result(null, CallState.SENDING);
////		} catch (Exception e) {
////			// 添加到失败列表中
////			if (e instanceof ConnectException) {
////				DownUrlHolder.instance().addDownUrl(url);
////				return Result.result(getRetVal(outType, 25000, e.getMessage()), CallState.TIMEOUT_SEND);
////			}
////			if (e instanceof TimeOutException)// 如果是超时异常
////			{
////				return Result.result(getRetVal(outType, 30000, e.getMessage()), CallState.TIMEOUT_RECEIVE);
////			}
////			if (e instanceof RemoteInvokeException) {
////				if (e.getCause() != null && e.getCause() instanceof java.lang.NoSuchMethodException) {
////					String msg = e.getClass() + ",url=" + url + " error:" + e.getCause().getMessage();
////					String ret = getRetVal(outType, -14444, msg);
////					return Result.result(ret, CallState.RECEIVED_BUT_INVOKE_ERROR);
////				}
////			}
////			String msg = e.getClass() + ",url=" + url + " error:" + RpcException.getExceptionInfo(e);
////			String ret = getRetVal(outType, 29999, msg);
////			return Result.result(ret, CallState.RECEIVED_BUT_INVOKE_ERROR);
////		}
////
////	}
//
//	private Url chooseUrl(final Url[] _urls) {
//		int seed = URL_SEED.incrementAndGet();
//		if (seed < 0) {
//			URL_SEED.set(0);
//			seed = 2;
//		}
//		
//		if (this.servers != null && this.servers.length > 0) {
//			int len=this.servers.length;
//			for (int i = 0; i < len; i++) {
//				Url server=this.servers[(seed+i)%len];
//				if (!DownUrlHolder.instance().isDowned(server)) {
//					return server;// 存在有效的自定义server
//				}
//			}
//			if (this.failIfServerIsUnEnable) {// servers全部不可用
//				return null;
//			}
//			// 如果server不可用，但是failIfServerisUnEnable==false，继续下面的流程
//		}
//		if (_urls == null || _urls.length == 0) {
//			return null;
//		}
//		int len=_urls.length;
//		for (int i = 0; i < len; i++) {
//			int index = (seed + i) % len;
//			Url url = _urls[index];
//			for (CallInfo info : this.rpcInfos) {
//				if (url.equals(info.url)) {
//					continue;
//				}
//			}
//			if (DownUrlHolder.instance().isDowned(url)) {
//				continue;
//			}
//			return url;
//		}
//		return null;
//	}
//
//	private static String getRetVal(int outType, int code, String message) {
//		if (outType == 0) {
//			JSONObject json = new JSONObject();
//			try {
//				json.put("Code", code);
//				json.put("Message", message);
//			} catch (Exception e) {
//
//			}
//			return json.toString();
//		} else {
//			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<OutPut><Code>" + code + "</Code><Message>" + message
//					+ "</Message></OutPut>";
//		}
//	}
//
//
//	static CallInfo callInfo(String reqId, KBusSender sender) {
//		if (sender == null) {
//			return null;
//		}
//		for (CallInfo callInfo : sender.rpcInfos) {
//			if (callInfo.request != null && callInfo.request.getReqId().equals(reqId)) {
//				return callInfo;
//			}
//		}
//		return null;
//	}
//
//	// 只能用于返回值是String的情况
//	public static class ABusLocker extends ResultLock {
//
//		private final AtomicReference<Thread> thread = new AtomicReference<Thread>();
//		/**
//		 * 大于0就表示已经醒来
//		 */
//		private volatile AtomicInteger status = new AtomicInteger();
//		private final KBusSender sender;
//
//		private volatile Throwable exception;
//		private volatile String msgResult;
//
//		public Throwable exception() {
//			return exception;
//		}
//
//		/**
//		 * 要在wakeup()之后调用才有意义
//		 * 
//		 * @return
//		 */
//		String msgResult() {
//			if (!this.waked()) {
//				throw new RuntimeException("这个方法要在wakeup()之后才能调用");
//			}
//			if (this.msgResult != null) {
//				return this.msgResult;
//			}
//			Throwable e = this.exception;
//			if (e == null) {
//				return (String) this.result.getResult();
//			}
//			if (e instanceof RpcTimeoutException)// 如果是超时异常
//			{
//				return getRetVal(sender.outType, 30000, e.getMessage());
//			}
//			CallInfo info = callInfo(this.id, sender);
//			if (info == null) {
//				return getRetVal(sender.outType, 29998, RpcException.getExceptionInfo(e));
//			}
//			if (e instanceof RemoteInvokeException) {
//				if (e.getCause() != null && e.getCause() instanceof java.lang.NoSuchMethodException) {
//					String msg = e.getClass() + ",url=" + info.url + " error:" + e.getCause().getMessage();
//					return getRetVal(sender.outType, -14444, msg);
//				}
//			}
//			String msg = e.getClass() + ",url=" + info.url + " error:" + RpcException.getExceptionInfo(e);
//			String ret = getRetVal(sender.outType, 29999, msg);
//			this.msgResult = ret;
//			return ret;
//		}
//
//		public boolean waked() {
//			return this.status.get()>0;
//		}
//
//		private ABusLocker(String id, KBusSender aBusSender) {
//			super(id);
//			this.sender = aBusSender;
//			KBus.notNull(id);
//		}
//
//		/**
//		 * 如果多个线程在等待，会抛出异常。
//		 * 但是该方法返回之后，无论是否当前线程，都可以再次调用
//		 */
//		void awaitUntil(long endTime) {
//
//			Thread t = Thread.currentThread();
//			if (this.waked()) {// 已经唤醒过，不能再等待了
//				return;
//			}
//			if (!this.thread.compareAndSet(null, t)) {
//				throw new RuntimeException("can not await in twice thread");
//			}
//			while (LockMap.pick(id) == this && endTime > System.currentTimeMillis()) {
//				LockSupport.parkUntil(endTime);
//			}
//
//			this.thread.compareAndSet(t, null); //当前线程已经睡醒，可以移除掉了
//		}
//
//		/**
//		 * resp不能为null
//		 * 
//		 * @param resp
//		 */
//		public void wakeup(Response resp) {
//			Exception ex = null;
//			if (resp.getStatus() != Response.OK) {
//				ex = new RemoteInvokeException(resp.getErrorMsg());
//			}
//			this.wakeup(resp.getReqID(), ex);
//		}
//
//		public void timeout(String reqId) {
//			//如果是建立socket失败，也会调用这里。因为它已经添加到TimeoutHandler里面
//			this.wakeup(reqId, new RpcTimeoutException("rpc time out"));
//		}
//
//		private void log(long respMills) {
//			CallInfo info = callInfo(this.id, sender);
//			String url = "", className = "", methodName = "", authInfo = "{}";
//			long start = sender.startRPC;
//			if (info != null) {
//				url = String.valueOf(info.url);
//				className = info.request.getRemoteInterfaceClassName();
//				methodName = info.request.getMethodName();
//				start = info.start;
//				if (info.message != null) {
//					authInfo = info.message.getAuthInfo();
//				}
//			}
//			long times = System.currentTimeMillis() - start;
//			String ret = this.msgResult();
//			// 对大于10M的文件，进行特殊处理
//			if (ret.length() > 10000000) {
//				ret = "{\"length\":" + ret.length() + "}";
//			}
//			Logger.get().log(LogLevel.INFO, "wsgw-log",
//					LogBody.me().set("AuthInfo", authInfo)// 鉴权头
//							.set("Api", sender.api)//
//							.set("Param", sender.params.length() > 10000000 ? "" : sender.params)// 入参
//							.set("ParamType", sender.paramType)// 入参类型 0 json
//							.set("OutType", sender.outType)// 出参类型 0 json
//							.set("V", Version.ver)// 版本号,默认1
//							.set("Result", ret)// 返回结果
//							.set("Url", url)// 目标子系统
//							.set("ClassName", className)// 目标系统类名
//							.set("MethodName", methodName)// 目标系统方法名
//							.set("Times", times)// 耗时
//							.set("ClientUrl", com.yihu.monitor.sdk.NetworkUtil.getLocalIP())// 客户端IP
//							// .set("CurrTimes", System.currentTimeMillis())
//							.set("resp_mills", respMills));
//		}
//
//		void wakeup(String reqId, Throwable ex) {
//			if (!this.id.equals(reqId)) {
//				KasiteConfig.print(this.id+" != "+reqId+",以下错误用于调试");
//				new Throwable().printStackTrace();
//				return;
//			}
//			if(!this.status.compareAndSet(0, 1)){//将未唤醒的设置为已唤醒，这是为了保证回调函数只会被调用一次
//				return;
//			}
//			try {
//				this.exception = ex;
//				Thread t = this.thread.get();
//				if (t != null) {
//					LockSupport.unpark(t);
//					this.thread.compareAndSet(t, null);// 这个不设其实也没啥关系
//				}
//				CallBackRunner2.invoke(this,this.sender);
//			} finally {
//				Response resp = this.result;
//				long time = resp == null ? -1L : resp.getConsumeMills();
//				String old = GIDHolder.get();
//				try {
//					GIDHolder.set(sender.gid);
//					log(time);
//				} finally {
//					GIDHolder.set(old);
//				}
//			}
//		}
//
//
//		void updateId(String reqId) {
//			this.id = reqId;
//		}
//
//	}
//
//	//测试chooseUrl
////	public static void main(String[] args) {
////		Url url;
////		for(int i=1;i<100;i++){
////			Url[] servers=new Url[]{new Url("127.0.0.1",i*100),new Url("127.0.0.1",i*100+1)};
////			ABusSender sender=new ABusSender(null,null,null,null)
////					.directCallServer(true, servers);
////			Url[] urls=new Url[]{new Url("127.0.0.1",i*100+50),new Url("127.0.0.1",i*100+51)};
////			for(int j=0;j<10;j++){
////				url=sender.chooseUrl(urls);
////				Assert.assertTrue(url.equals(servers[0])||url.equals(servers[1]));
////				DownUrlHolder.instance().addDownUrl(servers[0]);
////				Assert.assertTrue(sender.chooseUrl(urls).equals(servers[1]));
////				Assert.assertTrue(sender.chooseUrl(null).equals(servers[1]));//表示原来没有可用的
////			}
////			
////			DownUrlHolder.instance().addDownUrl(servers[1]);
////			for(int j=0;j<10;j++){
////				Assert.assertNull(sender.chooseUrl(urls));//没有可以用的
////				Assert.assertNull(sender.chooseUrl(null));
////			}
////			sender.directCallServer(false, servers);//改为用默认的
////			for(int j=0;j<10;j++){
////				url=sender.chooseUrl(urls);
////				Assert.assertTrue(url.equals(urls[0])||url.equals(urls[1]));
////				Assert.assertNull(sender.chooseUrl(null));
////			}
////		}
////		
////		KasiteConfig.print("directServer测试通过");
////		System.exit(-1);
////	}
//}
