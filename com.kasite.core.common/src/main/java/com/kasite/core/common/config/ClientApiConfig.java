package com.kasite.core.common.config;

import java.util.Set;
/**
 * 渠道对应的Api的配置信息
 * @author daiyanshui
 *
 */
public class ClientApiConfig {

	private Set<String> closeApi;

	public Set<String> getCloseApi() {
		return closeApi;
	}

	public void setCloseApi(Set<String> closeApi) {
		this.closeApi = closeApi;
	}
	
}
