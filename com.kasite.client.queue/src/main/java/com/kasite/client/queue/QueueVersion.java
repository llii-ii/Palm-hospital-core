package com.kasite.client.queue;

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
public class QueueVersion {
	protected static Logger logger = LoggerFactory.getLogger(QueueVersion.class);
	private static String v = "1.0";
	static {
		logger.info("排队模块版本号："+v);
		for (ApiMethodName apiModule : ApiModule.Queue.values()) {
			logger.info("排队模块接口api：{}|{}",apiModule.getName(),apiModule.getApiName());
			ApiList.me().addApi(apiModule.getModuleName(),apiModule);
		}
	}
}
