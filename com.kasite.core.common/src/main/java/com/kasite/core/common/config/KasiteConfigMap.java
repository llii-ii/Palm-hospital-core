package com.kasite.core.common.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="kasite") 
public class KasiteConfigMap {
	private Map<String, String> jobStartStateMap = new HashMap<>(); 
	public String getValue(String key) {
		return jobStartStateMap.get(key);
	}
	public boolean isStartJob(Class<?> className){
		String jobStartStateMapV = jobStartStateMap.get(className.getSimpleName());
		if(null != jobStartStateMapV && "false".equalsIgnoreCase(jobStartStateMapV)) {
			return false;
		}
		return true;
	}
	public Map<String, String> getJobStartStateMap() {
		return jobStartStateMap;
	}
	public void setJobStartStateMap(Map<String, String> jobStartStateMap) {
		this.jobStartStateMap = jobStartStateMap;
	}
}
