package com.kasite.server.verification.module.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.server.verification.common.dao.MyDatabaseEnum;
import com.kasite.server.verification.module.app.entity.CenterWxPay;
import com.kasite.server.verification.module.app.service.CenterWxPayService;

@Service("centerWxPayService")
public class CenterWxPayServiceImpl implements CenterWxPayService{

	@Override
	public List<CenterWxPay> queryWxPayList(String ids) throws Exception {
		Sql sql = new Sql("select * from center_wx_pay where id in ("+ ids +") ");
		return DB.me().queryForBeanList(MyDatabaseEnum.kasite_center, sql, CenterWxPay.class);
	}


}
