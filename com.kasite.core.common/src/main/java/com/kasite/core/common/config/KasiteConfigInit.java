package com.kasite.core.common.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kasite.core.common.sys.verification.VerificationBuser;
import com.kasite.core.common.validator.Assert;

@Configuration
@ConfigurationProperties(prefix="kasite") 
public class KasiteConfigInit {
	private static Logger logger = LoggerFactory.getLogger(KasiteConfigInit.class);
	private Map<String, String> appConfig = new HashMap<>(); 
	private Map<String, String> log = new HashMap<>(); 
	@Bean("kasiteConfig")
	public KasiteConfig getKasiteConfig() {

		logger.info("====================================================================================================================");
		logger.info("............................................ 载入 AppConfig 配置信息（start）........................................................");
		logger.info("====================================================================================================================");
//		Assert.isBlank(hosId, "hosId 不能为空，请修改配置，填写医院 ID。");
//		Assert.isBlank(hosName, "hosName 不能为空，请修改配置，填写医院名称。");
		
		for (Map.Entry<String, String> entry : appConfig.entrySet()) {
			KasiteConfig.setValue("kasite.appConfig."+entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, String> entry : log.entrySet()) {
			KasiteConfig.setValue("kasite.log."+entry.getKey(), entry.getValue());
		}
		Assert.isBlank(KasiteConfig.getValue(KasiteConfig.APPID), "appId 不能为空，请修改配置。");
		Assert.isBlank(KasiteConfig.getValue(KasiteConfig.APPSECRET), "appSecret 不能为空，请修改配置。");
		Assert.isBlank(KasiteConfig.getValue(KasiteConfig.PUBLICKEY), "publicKey 不能为空，请修改配置。");
		Assert.isBlank(KasiteConfig.getValue(KasiteConfig.ORGCODE), "orgCode 不能为空，请修改配置。");
		Assert.isBlank(KasiteConfig.getValue(KasiteConfig.LOCALHOSTROUTEPATH), "configPath 不能为空，请修改配置。");
		Assert.isBlank(KasiteConfig.getValue(KasiteConfig.CONNECTIONKASTIECENTER), "isConnectionKastieCenter 不能为空，请修改配置。");
		
	
		
		
//		KasiteConfig.setAppId(appId);
//		KasiteConfig.setAppSecret(appSecret);
//		KasiteConfig.setCenterServerUrl(centerUrl); 
//		KasiteConfig.localConfigPath(configPath);
//		KasiteConfig.isConnectionKastieCenter(isConnectionKastieCenter);
//		KasiteConfig.setPublicKey(publicKey);
//		KasiteConfig.setOrgCode(orgCode);
//		if(hosId!=null) {
//			KasiteConfig.setHosId(hosId);
//		}else {
//			KasiteConfig.setHosId(orgCode);
//		}
//		KasiteConfig.setHosName(hosName);
		try {
			if(KasiteConfig.isConnectionKastieCenter()) {
				Assert.isBlank(KasiteConfig.getCenterServerUrl()[0], "centerUrl 不能为空，请修改配置。");
			}
			logger.info("是否连接到配置中心："+KasiteConfig.isConnectionKastieCenter());
			logger.info("连接中心地址："+KasiteConfig.getCenterServerUrl()[0]);
			logger.info("注册appId :"+KasiteConfig.getAppId());
			logger.info("安全码appSecret :"+KasiteConfig.getAppSecret());
			logger.info("公钥publicKey :"+KasiteConfig.getPublicKey());
			logger.info("组织机构代码orgCode :"+KasiteConfig.getOrgCode());
			VerificationBuser.create().init();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化路由信息异常。",e);
		}
		logger.info("====================================================================================================================");
		logger.info("............................................ 载入 AppConfig 配置信息（end)....................................................");
		logger.info("====================================================================================================================");
//
//		KasiteConfig.setKasiteWlzxUrl(wlzxUrl);
//		KasiteConfig.setKasiteZndzUrl(zndzUrl);
//		KasiteConfig.setKasiteHosWebAppUrl(hosWebAppUrl);
//		if(StringUtil.isNotBlank(customLog)) {
//			KasiteConfig.setValue(KasiteConfig.CUSTOMLOG, customLog);
//		}
//		if(StringUtil.isNotBlank(esLog)) {
//			KasiteConfig.setValue(KasiteConfig.ESLOG, esLog);
//		}
//		if(StringUtil.isNotBlank(esUrl)) {
//			KasiteConfig.setValue(KasiteConfig.ESURL, esUrl);
//		}
//		if(StringUtil.isNotBlank(logPath)) {
//			KasiteConfig.setValue(KasiteConfig.LOGPATH, logPath);
//		}else {
//			KasiteConfig.setValue(KasiteConfig.LOGPATH, configPath);
//		}
		
		return KasiteConfig.inst;
	}
	public Map<String, String> getAppConfig() {
		return appConfig;
	}
	public void setAppConfig(Map<String, String> appConfig) {
		this.appConfig = appConfig;
	}
	public Map<String, String> getLog() {
		return log;
	}
	public void setLog(Map<String, String> log) {
		this.log = log;
	}
	
	
}
