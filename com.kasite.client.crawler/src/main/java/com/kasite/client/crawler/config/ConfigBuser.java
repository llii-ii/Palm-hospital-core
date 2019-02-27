package com.kasite.client.crawler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.crawler.config.vo.DatacloudConfigVo;
import com.kasite.client.crawler.config.vo.RpcConfigVo;
import com.kasite.client.crawler.modules.utils.SpringContextUtils;

@Component
public class ConfigBuser {

	@Autowired
	private RpcConfigVo rpcConfigVo;
	
	@Autowired
	private DatacloudConfigVo datacloudConfigVo;
	
	@Autowired
	private HisHearthDataCrawlerConfig hisHearthDataCrawlerConfig; 	
	
	public static ConfigBuser create() {
		return (ConfigBuser) SpringContextUtils.getBean("configBuser");
	}
	
	public DatacloudConfigVo getDatacloudConfigVo() {
		return datacloudConfigVo;
	}
	
	public RpcConfigVo getRpcConfigVo() {
		return rpcConfigVo;
	}

	public HisHearthDataCrawlerConfig getHisHearthDataCrawlerConfig() {
		return hisHearthDataCrawlerConfig;
	}
	
}
