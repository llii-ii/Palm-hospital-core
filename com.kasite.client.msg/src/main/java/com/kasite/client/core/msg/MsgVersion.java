package com.kasite.client.core.msg;

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
public class MsgVersion {
	protected static Logger logger = LoggerFactory.getLogger(MsgVersion.class);
	private static String v = "1.0";
	static {
		logger.info("消息模块版本号："+v);
		for (ApiMethodName apiModule : ApiModule.Msg.values()) {
			logger.info("消息模块接口api：{}|{}",apiModule.getName(),apiModule.getApiName());
			ApiList.me().addApi(apiModule.getModuleName(),apiModule);
		}
	}
}
