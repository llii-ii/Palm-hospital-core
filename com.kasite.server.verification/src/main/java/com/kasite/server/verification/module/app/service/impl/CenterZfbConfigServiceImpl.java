package com.kasite.server.verification.module.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.server.verification.common.dao.MyDatabaseEnum;
import com.kasite.server.verification.module.app.entity.CenterZfbConfig;
import com.kasite.server.verification.module.app.service.CenterZfbConfigService;

@Service("centerZfbConfigService")
public class CenterZfbConfigServiceImpl implements CenterZfbConfigService{

	@Override
	public List<CenterZfbConfig> queryZfbConfigList(String ids) throws Exception {
		Sql sql = new Sql("select * from center_zfb_config where id in ("+ ids +") ");
		return DB.me().queryForBeanList(MyDatabaseEnum.kasite_center, sql, CenterZfbConfig.class);
	}


}
