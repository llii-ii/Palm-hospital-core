package com.kasite.client.zfb.util;

import javax.servlet.http.HttpServletResponse;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.kasite.client.zfb.constants.AlipayServiceEnvConstants;
import com.kasite.core.common.util.CookieTool;
import com.kasite.core.serviceinterface.module.basic.cache.BatUserCache;

import net.sf.json.JSONObject;

public class AliUserToBatUserCache {

	public static final String APLIPAY_ACCESS_TOKEN = "aplipayAccessToken";
	/**
	 * 将阿里返回的用户信息转成batCacheUser
	 * @param userResp
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	public static BatUserCache parse(
			HttpServletResponse response,
			AlipaySystemOauthTokenResponse tokenResp ,
			AlipayUserInfoShareResponse userResp,
			String configKey) throws Exception {
		BatUserCache cache = new BatUserCache();
		if (userResp.isSuccess()) {
			cache.setOpenId(userResp.getUserId());
			cache.setNickName(userResp.getNickName());
			if (AlipayServiceEnvConstants.GENDER_M.equals(userResp.getGender())) {
				cache.setSex(1);
			} else if (AlipayServiceEnvConstants.GENDER_F.equals(userResp.getGender())) {
				cache.setSex(2);
			} else {
				cache.setSex(0);
			}
			cache.setCity(userResp.getCity());
			cache.setProvince(userResp.getProvince());
			cache.setSubscribe(1);
			cache.setRemark(userResp.getIsCertified());
			cache.setHeadImgUrl(userResp.getAvatar());
			cache.setSysId(userResp.getUserType());
			cache.setConfigKey(configKey);
		}
		if(null != tokenResp) {
			JSONObject accessTokenJsonObj = new JSONObject();
			accessTokenJsonObj.put("accessToken", tokenResp.getAccessToken());
			accessTokenJsonObj.put("userId", tokenResp.getUserId());
			accessTokenJsonObj.put("expiresIn", tokenResp.getExpiresIn());
			accessTokenJsonObj.put("reExpiresIn", tokenResp.getReExpiresIn());
			accessTokenJsonObj.put("refreshToken", tokenResp.getRefreshToken());
			CookieTool.addCookie(response, APLIPAY_ACCESS_TOKEN, accessTokenJsonObj.toString(), 1);
		}
		return cache;
	}
}
