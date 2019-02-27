package com.kasite.server.verification.module.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.server.verification.common.dao.MyDatabaseEnum;
import com.kasite.server.verification.module.app.entity.AppConfig;
import com.kasite.server.verification.module.app.service.AppConfigService;
@Service("appConfigService")
public class AppConfigServiceImpl implements AppConfigService{

	@Override
	public List<AppConfig> getAppConfig(String appId) throws Exception {
		Sql sql = new Sql("select * from app_config where appId = ? and status = 1");
		sql.addParamValue(appId);
		return DB.me().queryForBeanList(MyDatabaseEnum.kasite_center, sql, AppConfig.class);
	}

	
	
	
}
