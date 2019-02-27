package com.kasite.client.crawler.micro.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.sys.oauth.entity.AppAccessToken;
import com.kasite.core.common.util.ExpiryMap;
import com.kasite.core.common.validator.Assert;

public class SystemOAuthUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(SystemOAuthUtil.class);
	private static SystemOAuthUtil install;
	public static SystemOAuthUtil getInstall() {
		if(null == install) {
			synchronized (SystemOAuthUtil.class) {
				if(null == install) {
					install = new SystemOAuthUtil();
				}
			}
		}
		return install;
	}
	private ExpiryMap<String, AppAccessToken> appAccessTokenMap = new ExpiryMap<>();
	private Map<String, JSONObject> appEntityMap = new HashMap<>();
	
	public AppAccessToken getAccessToken(String accessToken) {
		return appAccessTokenMap.get(accessToken);
	}
	public void addAccessToken(long lv, String unionId,String accessToken,String create_time,String invalidTime,JSONObject json) {
		Assert.isBlank(accessToken, "accessToken 不能为空");
		AppAccessToken token = new AppAccessToken();
		token.setAccessToken(accessToken);
		token.setAppId(unionId);
		token.setCreateTime(create_time);
		token.setInvalidTime(invalidTime);
		appAccessTokenMap.put(accessToken, token, lv + 20000);
		appEntityMap.put(unionId, json);
	}
	private SystemOAuthUtil() {
	}
}
