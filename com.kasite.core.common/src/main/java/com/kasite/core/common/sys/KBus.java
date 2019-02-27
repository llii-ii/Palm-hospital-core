//package com.kasite.core.common.sys;
//
//import com.kasite.core.common.Version;
//import com.kasite.core.common.config.KasiteConfig;
//import com.kasite.core.common.sys.verification.VerificationBuser;
//import com.yihu.wsgw.bus.KBusSender;
//
//public class KBus {
//	static String APPID;
//	static String[] CENTERURL;
//	static String ORGCODE;
//	static String APPSECRET;
//	static String PUBLICKEY;
//	
//	private static boolean inited;
//
//	public static synchronized void init(String orgCode,String appId) throws Exception {
//		if (inited) {
//			return;
//		}
//		KasiteConfig.print("KBUS init");
////		notNull(appId);
////		KBus.APPID = appId;
////		KBus.CENTERURL = centerUrl;
//		KBus.ORGCODE = orgCode;
//		KBus.APPID = appId;
////		KBus.APPSECRET = appSecret;
////		KBus.PUBLICKEY = publicKey;
//		Version.print();
//		//从服务端下载 配置文件并初始化本地目录
//		if(KasiteConfig.isConnectionKastieCenter()) {
////			VerificationBuser.create().getRouteByOrgCode(ORGCODE,KasiteConfig.getAppId());
//			VerificationBuser.create().init();
//		}
//		inited = true;
//	}
//
//	public static void init() throws Exception {
//		init(KasiteConfig.getOrgCode(),KasiteConfig.getAppId());
//	}
//	public static KBusSender create(String api, String params) throws Exception {
//		if (!inited) {
//			init();
//		}
//		notNull(api);
//		notNull(params);
//		//String api, String params, String[] centerServerUrl, String appId, String appSecret,String publicKey,String orgCode
//		return new KBusSender(ORGCODE,APPID, api, params);
//	}
//	public static KBusSender create(String orgCode,String appId,String api, String params) throws Exception {
//		if (!inited) {
//			init();
//		}
//		notNull(api);
//		notNull(params);
//		//String api, String params, String[] centerServerUrl, String appId, String appSecret,String publicKey,String orgCode
//		return new KBusSender(orgCode,appId, api, params);
//	}
//
//
//	public static KBusSender createXml(String orgCode, String api, String params) throws Exception {
//		return create(api, params).paramType(1).outType(1);
//	}
//
//	public static void notNull(Object obj) {
//		if (obj == null) {
//			throw new IllegalArgumentException("object cannot be null");
//		}
//	}
//}
