package com.kasite.server.verification.module.app.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.server.verification.common.dao.MyDatabaseEnum;
import com.kasite.server.verification.common.dao.MyTableEnum;
import com.kasite.server.verification.module.app.entity.AppAccessToken;
import com.kasite.server.verification.module.app.service.AccessTokenService;

@Service("accessTokenService")
public class AccessTokenServiceImpl implements AccessTokenService {

	@Override
	public int save(String access_token, String app_id,Date create_time, Date invalid_time,String appNowTime) throws Exception {
		DB.me().update(MyDatabaseEnum.kasite_center, 
				new Sql("insert into app_access_token_d (id,appId,accessToken,createTime,invalidTime,appNowTime) select id,appId,accessToken,createTime,invalidTime,appNowTime from app_access_token where appId = ?")
				.addParamValue(app_id)
				);
		DB.me().update(MyDatabaseEnum.kasite_center, 
				new Sql("delete from app_access_token where appid = ?")
				.addParamValue(app_id)
				); 
		AppAccessToken tokenVo = new AppAccessToken();
		tokenVo.setAccessToken(access_token);
		tokenVo.setAppId(app_id);
		tokenVo.setCreateTime(create_time);
		tokenVo.setInvalidTime(invalid_time);
		tokenVo.setAppNowTime(appNowTime);
		Sql sql = DB.me().createInsertSql(tokenVo, MyTableEnum.app_access_token);
		return DB.me().insert(MyDatabaseEnum.kasite_center, sql);
	}

	@Override
	public AppAccessToken queryByToken(String token) throws Exception {
		return DB.me().queryForBean(MyDatabaseEnum.kasite_center, new Sql("select * from app_access_token where accessToken = ?").addParamValue(token), AppAccessToken.class);
	}

}
