package com.kasite.client.survey;

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
public class SurveyVersion {
	protected static Logger logger = LoggerFactory.getLogger(SurveyVersion.class);
	private static String v = "1.0";
	static {
		logger.info("满意度调查模块版本号："+v);
		for (ApiMethodName apiModule : ApiModule.Survey.values()) {
			logger.info("满意度调查模块接口api：{}|{}",apiModule.getName(),apiModule.getApiName());
			ApiList.me().addApi(apiModule.getModuleName(),apiModule);
		}
	}
}
