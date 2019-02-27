package com.kasite.core.common.sys.oauth.service.impl;

import org.springframework.stereotype.Service;

import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.oauth.entity.AppAccessToken;
import com.kasite.core.common.sys.oauth.service.AccessTokenService;
import com.kasite.core.common.sys.verification.TokenGenerator;

@Service("accessTokenServiceApi")
public class AccessTokenServiceImpl implements AccessTokenService{

//	@Override
//	public AppAccessToken queryByToken(String access_token) throws Exception {
//		
//		AppAccessToken token = KasiteSys.getInstall().getAppAccessToken(access_token);
//		if(null != token) {
//			return token;
//		}
//		token = VerificationBuser.create().getAppAccessTokenByAccessToken(access_token);
//		KasiteSys.getInstall().addAppAccessToken(token);
//		return token;
//	}

	@Override
	public String crateAccessToken(long lv, String appId , String orgCode, String createTime,
			String invalidTime, String now) {
		String access_token = TokenGenerator.generateValue();
		LocalOAuthUtil.getInstall().addAccessToken(lv,appId, access_token, orgCode, createTime, invalidTime);
		return access_token;
	}

	@Override
	public String refreshToken(String access_token) {
		AppAccessToken accessToken = LocalOAuthUtil.getInstall().getAccessToken(access_token);
		accessToken.getInvalidTime();
		return null;
	}

}
