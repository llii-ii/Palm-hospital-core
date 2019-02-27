package com.kasite.server.verification.module.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.core.common.annotation.ApiCallLog;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.validator.Assert;
import com.kasite.server.verification.common.controller.AbstractController;
import com.kasite.server.verification.common.dao.MyDatabaseEnum;
import com.kasite.server.verification.common.dao.MyTableEnum;
import com.kasite.server.verification.config.ZkConfig;
import com.kasite.server.verification.module.app.entity.AppEntity;
import com.kasite.server.verification.module.app.entity.AppRoute;
import com.kasite.server.verification.module.app.entity.BaseOrganization;
import com.kasite.server.verification.module.app.service.AppService;
import com.kasite.server.verification.module.app.vo.TreeVo;
import com.kasite.server.verification.module.zk.KasiteServiceRouteParser;
@RestController
@RequestMapping("/web")
public class AppController extends AbstractController {
	@Autowired
	private ZkConfig zkConfig;	
	@Autowired
	private AppService appService;
	
	@RequestMapping(value = "/app/queryAppTreeByOrgCode", method = RequestMethod.POST)
	public R queryAppListByOrgCode(String orgCode) throws Exception {
		//获取配置信息。
		List<AppEntity> list = appService.queryAppListByOrgCode(orgCode);
		List<TreeVo> tlist = new ArrayList<>();
		for (AppEntity app : list) {
			Sql sql = new Sql("select * from app_route where appid = ?");
			sql.addParamValue(app.getAppId());
			TreeVo t = new TreeVo();
			AppRoute route = DB.me().queryForBean(MyDatabaseEnum.kasite_center, sql, AppRoute.class);
			t.setId(app.getAppId());
			t.setIsLeaf(true);
			String n = "未定义路由";
			if(null != route && StringUtil.isNotBlank(route.getAppNickName())) {
				n = route.getAppNickName();
				String routeXml = KasiteServiceRouteParser.instance(zkConfig.getZkUrl()).getRouteXml(orgCode, n);
				route.setRouteXml(routeXml);
			}
			JSONObject data = (JSONObject) JSON.toJSON(route);
			t.setData(data); 
			t.setName(app.getAppName()+"("+n+")");
			tlist.add(t);
		}
		return R.ok(tlist);
	}
	
	@ApiCallLog("获取机构树列表接口")
	@RequestMapping(value = "/base/queryAllOrgListTree", method = RequestMethod.POST)
	public R queryAllOrgListTree() throws Exception {
		Sql sql = new Sql("select * from base_organization where status = 1");
		List<BaseOrganization> list = DB.me().queryForBeanList(MyDatabaseEnum.kasite_center, sql, BaseOrganization.class);
		List<TreeVo> tlist = new ArrayList<>();
		if(null != list) {
			for (BaseOrganization org : list) {
				TreeVo t = new TreeVo();
				t.setId(org.getOrgCode());
				t.setIsLeaf(false);
				t.setName(org.getOrgName());
				tlist.add(t);
			}
		}
		return R.ok(tlist);
	}
	
	@ApiCallLog("新增路由")
	@RequestMapping(value = "/base/addRoute", method = RequestMethod.POST)
	public R addRoute(String appId,String orgCode,String nickName,String routeXml) throws Exception {
		Assert.isBlank(nickName, "nickName 不能为空");
		Assert.isBlank(orgCode, "orgCode 不能为空");
		Assert.isBlank(routeXml, "routeXml 不能为空");
		Assert.isBlank(appId, "appId 不能为空");
		Sql sql = new Sql("update app_route set routeXml = ? where appId = ? and orgCode = ? ");
		sql.addParamValue(routeXml).addParamValue(appId).addParamValue(orgCode);
		int size = DB.me().update(MyDatabaseEnum.kasite_center, sql);
		if(size <= 0) {
			AppRoute route = new AppRoute();
			route.setAppId(appId);
			route.setAppNickName(nickName);
			route.setRouteXml(routeXml);
			Sql insertSql = DB.me().createInsertSql(route, MyTableEnum.app_route);
			DB.me().insert(MyDatabaseEnum.kasite_center, insertSql);
		}
		KasiteServiceRouteParser.instance(zkConfig.getZkUrl()).createNode(orgCode, nickName, routeXml);
		return R.ok();
	}
	
	
	
}
