package com.kasite.server.verification.module.app.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.annotation.ApiCallLog;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.R;
import com.kasite.server.verification.common.controller.AbstractController;
import com.kasite.server.verification.config.ZkConfig;
import com.kasite.server.verification.module.app.entity.AppConfig;
import com.kasite.server.verification.module.app.entity.AppEntity;
import com.kasite.server.verification.module.app.entity.CenterWxConfig;
import com.kasite.server.verification.module.app.entity.CenterWxPay;
import com.kasite.server.verification.module.app.entity.CenterZfbConfig;
import com.kasite.server.verification.module.app.service.AppConfigService;
import com.kasite.server.verification.module.app.service.AppService;
import com.kasite.server.verification.module.app.service.CenterWxConfigService;
import com.kasite.server.verification.module.app.service.CenterWxPayService;
import com.kasite.server.verification.module.app.service.CenterZfbConfigService;
import com.kasite.server.verification.module.zk.KasiteServiceRouteParser;

@RestController
@RequestMapping("/gateway/route")
public class AppRouteController extends AbstractController {
	
	@Autowired
	private ZkConfig zkConfig;	
	@Autowired
	private AppService appService;
	@Autowired
	private AppConfigService appConfigService;
	@Autowired
	private CenterWxConfigService centerWxConfigService;
	@Autowired
	private CenterWxPayService centerWxPayService;
	@Autowired
	private CenterZfbConfigService centerZfbConfigService;
	
	@ApiCallLog("调用 查询AppRouteXml 相应接口")
	@RequestMapping(value = "/getAppRoute", method = RequestMethod.POST)
	public R getAppRoute(String appId) throws Exception {
		String result = KasiteServiceRouteParser.instance(zkConfig.getZkUrl()).queryAllEncryptRoute(appId);
		return R.ok().put("result", result);
	}
	
	
	
	@ApiCallLog("调用 查询RouteXml 相应接口")
	@RequestMapping(value = "/getOrgCodeAppRoute", method = RequestMethod.POST)
	public R getAllAppRoute(String orgCode) throws Exception {
//		String result = KasiteServiceRouteParser.instance(zkConfig.getZkUrl(), orgCode).queryAllEncryptRoute();
		return R.ok().put("result", null);
	}
	
	@ApiCallLog("调用 获取配置信息接口 相应接口")
	@RequestMapping(value = "/getAppConfig", method = RequestMethod.POST)
	public R getAppConfig(String appId)  throws Exception {
		
		JSONArray wxConfigListJson = new JSONArray();
		JSONArray wxPayListJson = new JSONArray();
		JSONArray zfbConfigListJson = new JSONArray();
		List<AppConfig> configList = appConfigService.getAppConfig(appId);
		if(null != configList && configList.size() > 0 ) {
			JSONArray array = new JSONArray();
			for (AppConfig config : configList) {
				JSONObject json = new JSONObject();
				String wxConfigIds = config.getCenter_wx_config_ids();
				String wxPayIds = config.getCenter_wx_pay_ids();
				String zfbIds = config.getCenter_zfb_config_ids();
				if(StringUtil.isNotBlank(wxConfigIds)) {
					List<CenterWxConfig> wxConfigList = centerWxConfigService.queryWxConfigList(wxConfigIds);
					if(null != wxConfigList) {
						for (CenterWxConfig centerWxConfig : wxConfigList) {
							wxConfigListJson.add( JSON.toJSON(centerWxConfig));
						}
					}
				}
				if(StringUtil.isNotBlank(wxPayIds)) {
					List<CenterWxPay> wxPayList = centerWxPayService.queryWxPayList(wxPayIds);
					if(null != wxPayList) {
						for (CenterWxPay centerWxPay : wxPayList) {
							wxPayListJson.add(JSON.toJSON(centerWxPay));
						}
					}
				}
				if(StringUtil.isNotBlank(zfbIds)) {
					List<CenterZfbConfig> zfbList = centerZfbConfigService.queryZfbConfigList(zfbIds);
					if(null != zfbList) {
						for (CenterZfbConfig zfb : zfbList) {
							zfbConfigListJson.add(JSON.toJSON(zfb));
						}
					}
				}
				json.put("wx", wxConfigListJson);
				json.put("wxpay", wxPayListJson);
				json.put("zfb", zfbConfigListJson);
				json.put("unionPay", "");
				json.put("clientId", config.getClientId());
				array.add(json);
			}
			
			return R.ok().put("result",  DesUtil.encrypt(array.toString(), "utf-8"));
		}else {
			return R.error(504, "应用无相应的微信和支付宝配置文件");
		}
		
	}
	
	@ApiCallLog("调用 机构对应的 app 公钥列表相应接口")
	@RequestMapping(value = "/queryPublicKeyList", method = RequestMethod.POST)
	public R queryPublicKeyList(String appId) throws Exception {
		//获取配置信息。
		List<AppEntity> list = appService.queryAppList(appId);
		Set<String> orgCodes = new HashSet<>();
		for (AppEntity app : list) {
			app.setPrivateKey(null);
			app.setPublicKey(null);
			app.setAppSecret(null);
			orgCodes.add(app.getOrgCode());
		}
		return R.ok(orgCodes);
	}
//	public static void main(String[] args) throws Exception {
//		System.out.println(	java.net.URLEncoder.encode("=", "UTF-8"));
//		System.out.println(	java.net.URLDecoder.decode("%3D", "UTF-8"));
//	}
	@ApiCallLog("调用 机构对应的 app 私钥列表相应接口")
	@RequestMapping(value = "/queryPrivateKeyList", method = RequestMethod.POST)
	public R queryPrivateKeyList(String appId) throws Exception {
		//获取配置信息。
		List<AppEntity> list = appService.queryAppListByCallAppId(appId);
	
		for (AppEntity app : list) {
			app.setPrivateKey(java.net.URLEncoder.encode(app.getPrivateKey(), "UTF-8"));
			app.setPublicKey(null);
		}
		return R.ok(list);
	}
}
