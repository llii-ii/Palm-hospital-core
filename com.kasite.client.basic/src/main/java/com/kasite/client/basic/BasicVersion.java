package com.kasite.client.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kasite.core.common.constant.ApiList;
import com.kasite.core.common.constant.ApiMethodName;
import com.kasite.core.common.constant.ApiModule;

/**
 * 版本号管理
 * @author daiyanshui
 */
@Component
public class BasicVersion {
	protected static Logger logger = LoggerFactory.getLogger(BasicVersion.class);
	private static String v = "1.0";
	static {
		logger.info("基础信息模块版本号："+v);
		for (ApiMethodName apiModule : ApiModule.Basic.values()) {
			logger.info("基础信息模块接口api：{}|{}",apiModule.getName(),apiModule.getApiName());
			ApiList.me().addApi(apiModule.getModuleName(),apiModule);
		}
		for (ApiMethodName apiModule : ApiModule.Bat.values()) {
			logger.info("第三方账户模块接口api：{}|{}",apiModule.getName(),apiModule.getApiName());
			ApiList.me().addApi(apiModule.getModuleName(),apiModule);
		}
	}
}
