package com.kasite.core.common.sys.runcmd;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Api {
	@Value("${api.getCmdUrl:#{null}}")
    private String getCmdUrl;
	@Value("${api.getVersion:#{null}}")
    private String version;

	@Value("${api.downUrl:#{null}}")
    private String downUrl;
	@Value("${api.fileListUrl:#{null}}")
    private String fileListUrl;
	@Value("${api.cmdDownUrl:#{null}}")
	private String cmdDownUrl;
	@Value("${api.cmdUpUrl:#{null}}")
	private String cmdUpUrl;

	public String getToken() {
		return "token0987654321";
	}
	
	public String getdownUrl() {
		return downUrl;
	}
	
	public String getFileList() {
		return fileListUrl;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getGetCmdUrl() {
		return getCmdUrl;
	}

	public String getCmdDownUrl() {
		return cmdDownUrl;
	}

	public String getCmdUpUrl() {
		return cmdUpUrl;
	}
}
