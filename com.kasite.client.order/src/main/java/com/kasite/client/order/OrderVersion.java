package com.kasite.client.order;

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
public class OrderVersion {
	protected static Logger logger = LoggerFactory.getLogger(OrderVersion.class);
	private static String v = "1.0";
	static {
		logger.info("订单模块版本号："+v);
		for (ApiMethodName apiModule : ApiModule.Order.values()) {
			logger.info("订单模块接口api：{}|{}",apiModule.getName(),apiModule.getApiName());
			ApiList.me().addApi(apiModule.getModuleName(),apiModule);
		}
		for (ApiMethodName apiModule : ApiModule.SmartPay.values()) {
			logger.info("智付模块接口api：{}|{}",apiModule.getName(),apiModule.getApiName());
			ApiList.me().addApi(apiModule.getModuleName(),apiModule);
		}
	}
}
