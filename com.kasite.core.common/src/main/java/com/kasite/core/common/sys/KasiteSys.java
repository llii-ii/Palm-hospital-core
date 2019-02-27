//package com.kasite.core.common.sys;
//
//import com.kasite.core.common.Version;
//import com.kasite.core.common.validator.Assert;
//
//public class KasiteSys {
//	
//	private final static Object obj = new Object();
//	private String centerUrls;
//	private static KasiteSys instance;
////
////	/**
////	 * 默认有效期3小时
////	 */
////	private static ExpiryMap<String, AppAccessToken> accessTokenMap = new ExpiryMap<>(3*60*60000);
//	private KasiteSys(String centerUrls) throws Exception {
//		synchronized (obj) {
//			this.centerUrls = centerUrls;
//			initKasiteConfig();
//		}
//	}
////	
////	public void addAppAccessToken(AppAccessToken token) {
////		String accessToken = token.getAccessToken();
////		Assert.isBlank(accessToken, "accessToken 不能为空");
////		//保存token 并且保存的有效期内
////		accessTokenMap.put(accessToken, token,token.getLiveTime());
////	}
////	
////	public AppAccessToken getAppAccessToken(String accessToken) {
////		Assert.isBlank(accessToken, "accessToken 不能为空");
////		return accessTokenMap.get(accessToken);
////	}
//	/**
//	 * init KasiteConfig
//	 * @param centerUrls
//	 * @return
//	 * @throws Exception 
//	 */
//	public void initKasiteConfig() throws Exception {
//		Assert.isBlank(centerUrls, "卡斯特中心地址不能为空。");
//	}
//	
//	public static KasiteSys getInstall(String centerUrls) throws Exception {
//		Version.print();
//		if(null == instance) {
//			instance = new KasiteSys(centerUrls);
//		}
//		return instance;
//	}
//	
//	public static KasiteSys getInstall() {
//		return instance;
//	}
//	
//}
