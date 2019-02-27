package com.kasite.server.verification.module.app.service.impl;

import org.springframework.stereotype.Service;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.coreframework.remoting.standard.DateOper;
import com.kasite.server.verification.common.dao.MyDatabaseEnum;
import com.kasite.server.verification.common.dao.MyTableEnum;
import com.kasite.server.verification.module.app.entity.AppOnLine;
import com.kasite.server.verification.module.app.service.AppOnLineService;

@Service("appOnLineService")
public class AppOnLineServiceImpl implements AppOnLineService{

	@Override
	public void appOnLine(String appId) throws Exception {
		Sql sql = new Sql("update app_online set updateTime = ? where appId = ?")
				.addParamValue(DateOper.getNow("yyyy-MM-dd HH:mm:ss"))
				.addParamValue(appId);
		int i = DB.me().update(MyDatabaseEnum.kasite_center, sql);
		if(i<=0) {
			AppOnLine online = new AppOnLine();
			online.setAppId(appId);
			online.setOnLine(AppOnLineService.ONLINE);
			online.setCreateTime(DateOper.getNowDateTime());
			DB.me().insert(MyDatabaseEnum.kasite_center, DB.me().createInsertSql(online, MyTableEnum.app_online));
		}
		
	}

}
