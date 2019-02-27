package com.kasite.core.common.util.wechat;

public interface IWxAccessTokenService {

	/**
	 * 根据机构代码获取token 如果token无效则申请一个信息的如果有效则返回
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	public String getWxAccessTokenByAppId(String appId,String secret) throws Exception;

}
