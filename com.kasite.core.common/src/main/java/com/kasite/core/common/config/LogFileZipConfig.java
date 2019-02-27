package com.kasite.core.common.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "kasite")
@Component("logFileZipConfig")
public class LogFileZipConfig {
	
	private List<LogfilezipVo> logfilezip;

	public List<LogfilezipVo> getLogfilezip() {
		return logfilezip;
	}

	public void setLogfilezip(List<LogfilezipVo> logfilezip) {
		this.logfilezip = logfilezip;
	}
 
	
}
