package com.kasite.client.qywechat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kasite.core.common.constant.ApiList;
import com.kasite.core.common.constant.ApiMethodName;
import com.kasite.core.common.constant.ApiModule;

/**
 * 版本号管理
 * 
 * @author wufadong
 */
@Component
public class QyWechatVersion {
	protected static Logger logger = LoggerFactory.getLogger(QyWechatVersion.class);
	private static String v = "1.0";
	static {
		logger.info("企业微信模块版本号：" + v);
		for (ApiMethodName apiModule : ApiModule.QyWeChat.values()) {
			logger.info("企业微信模块接口api：{}|{}",apiModule.getName(),apiModule.getApiName());
			ApiList.me().addApi(apiModule.getModuleName(),apiModule);
		}
	}
}
