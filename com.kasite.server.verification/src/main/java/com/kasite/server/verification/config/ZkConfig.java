package com.kasite.server.verification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.coreframework.util.StringUtil;
import com.kasite.server.verification.module.zk.ZkClientFactory;
import com.kasite.server.verification.module.zk.ZkClientWrapper;
@Component
public class ZkConfig {
	@Value("${zk.zkUrl:#{null}}")
	private String zkUrl;
	@Value("${zk.connectionTimeout:#{null}}")
	private String connectionTimeout;
	
	public String getZkUrl() {
		return zkUrl;
	}
	
	
	@Bean("zkClientWrapper")
	public ZkClientWrapper getZkWrapper() {
		if(com.kasite.core.common.util.StringUtil.isNotBlank(zkUrl)) {
			int connectionTimeout = 20000;
			if(StringUtil.isNotBlank(this.connectionTimeout)) {
				connectionTimeout = Integer.parseInt(this.connectionTimeout);
			}
			try {
				return ZkClientFactory.getInstance().getZkClient(zkUrl, connectionTimeout);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
}
