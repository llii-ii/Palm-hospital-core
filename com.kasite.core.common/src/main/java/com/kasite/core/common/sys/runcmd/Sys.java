package com.kasite.core.common.sys.runcmd;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Sys {
	@Value("${sys.uuid:#{null}}")
    private String uuid;
	@Value("${sys.name:#{null}}")
    private String name;
	@Value("${sys.centerurl:#{null}}")
    private String centerurl;
	@Value("${sys.source_dir:#{null}}")
    private String source_dir;
	@Value("${sys.version_file:#{null}}")
    private String version_file;
	@Value("${sys.runcmd_time:#{null}}")
    private String runcmd_time;
	
	public String getRuncmd_time() {
		return runcmd_time;
	}
	
	public String getVersion_file() {
		return version_file;
	}
	
	public String getSource_dir() {
		return source_dir;
	}
	public String getUuid() {
		return uuid;
	}
	public String getName() {
		return name;
	}
	public String getCenterurl() {
		return centerurl;
	}
	
}
