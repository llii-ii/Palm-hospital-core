package com.kasite.server.verification.module.app.service;

import java.sql.SQLException;
import java.util.Date;

import com.kasite.server.verification.module.app.entity.AppAccessToken;

public interface AccessTokenService {
	/**
	 * 新增一个access token 
	 * 如果存在则删除旧的新增一个新的
	 * @param access_token
	 * @param app_id
	 * @param invalid_time 有效期默认2个小时
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public int save(String access_token,String app_id,Date create_time,Date invalid_time,String appNowTime) throws Exception;

	public AppAccessToken queryByToken(String token) throws Exception;
	
}
