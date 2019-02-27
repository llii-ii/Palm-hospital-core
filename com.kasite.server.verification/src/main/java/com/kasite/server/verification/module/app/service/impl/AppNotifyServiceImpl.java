package com.kasite.server.verification.module.app.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.server.verification.common.dao.MyDatabaseEnum;
import com.kasite.server.verification.module.app.entity.AppNotify;
import com.kasite.server.verification.module.app.service.AppNotifyService;
@Service("appNotifyService")
public class AppNotifyServiceImpl implements AppNotifyService{

	@Override
	public List<AppNotify> queryAppNotifyList(String appId,String lastReadTime) throws Exception {
		List<AppNotify> list = DB.me().queryForBeanList(MyDatabaseEnum.kasite_center, 
				new Sql("select * from app_notify where appId = ? and createTime > ?").addParamValue(appId).addParamValue(lastReadTime),
				AppNotify.class);
		return list;
	}

	@Override
	public void deleteAppNotify(long id) throws SQLException {
		DB.me().update(MyDatabaseEnum.kasite_center, new Sql("insert app_notify_d (id,appId,status,gid,createTime) select id,appId,status,gid,createTime from app_notify where id = ?").addParamValue(id));
		DB.me().update(MyDatabaseEnum.kasite_center, new Sql("delete from app_notify where id = ?").addParamValue(id));
	}

}
