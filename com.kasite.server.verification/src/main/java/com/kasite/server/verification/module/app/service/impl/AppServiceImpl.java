package com.kasite.server.verification.module.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.server.verification.common.dao.MyDatabaseEnum;
import com.kasite.server.verification.module.app.entity.AppEntity;
import com.kasite.server.verification.module.app.service.AppService;
@Service("appService")
public class AppServiceImpl implements AppService {

	@Override
	public AppEntity getApp(String appId) throws Exception {
		return DB.me().queryForBean(MyDatabaseEnum.kasite_center, 
				new Sql("select * from app where appId = ?")
				.addParamValue(appId), AppEntity.class);
	}

	@Override
	public List<AppEntity> queryAppListByOrgCode(String orgCode) throws Exception {
		return DB.me().queryForBeanList(MyDatabaseEnum.kasite_center,
				new Sql("select * from app where 1=1 and status = 1 and orgCode = ? ")
				.addParamValue(orgCode), AppEntity.class);
	}
	
	@Override
	public List<AppEntity> queryAppList(String appId) throws Exception {
		return DB.me().queryForBeanList(MyDatabaseEnum.kasite_center,
				new Sql("select * from app where 1=1 and status = 1 and appid in (select callAppId from app_oauth where appId = ?) ")
				.addParamValue(appId), AppEntity.class);
	}
	
	@Override
	public List<AppEntity> queryAppListByCallAppId(String appId) throws Exception {
		return DB.me().queryForBeanList(MyDatabaseEnum.kasite_center,
				new Sql("select * from app where 1=1 and status = 1 and appid in (select appId from app_oauth where callAppId = ?) ")
				.addParamValue(appId), AppEntity.class);
	}
}
