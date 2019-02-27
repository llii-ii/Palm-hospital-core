package com.kasite.core.common.sys.oauth.service;

public interface AccessTokenService {

//	AppAccessToken queryByToken(String token) throws Exception;

	String crateAccessToken(long lv, String appId, String orgCode, String createTime, String invalidTime, String now);

	String refreshToken(String token);

}
