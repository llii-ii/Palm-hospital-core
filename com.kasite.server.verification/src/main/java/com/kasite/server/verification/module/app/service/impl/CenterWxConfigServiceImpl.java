package com.kasite.server.verification.module.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.server.verification.common.dao.MyDatabaseEnum;
import com.kasite.server.verification.module.app.entity.CenterWxConfig;
import com.kasite.server.verification.module.app.service.CenterWxConfigService;

@Service("centerWxConfigService")
public class CenterWxConfigServiceImpl implements CenterWxConfigService{

	@Override
	public List<CenterWxConfig> queryWxConfigList(String ids) throws Exception {
		Sql sql = new Sql("select * from center_wx_config where id in ("+ ids +") ");
		return DB.me().queryForBeanList(MyDatabaseEnum.kasite_center, sql, CenterWxConfig.class);
	}

}
