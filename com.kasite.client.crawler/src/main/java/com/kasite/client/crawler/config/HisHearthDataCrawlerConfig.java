package com.kasite.client.crawler.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kasite.core.common.config.ClientType;
import com.kasite.core.httpclient.http.RequestType;

@Configuration
public class HisHearthDataCrawlerConfig {
	@Value("${HearthDataCrawler.type:#{null}}")
	private String type;
	@Value("${HearthDataCrawler.wsUrl:#{null}}")
	private String wsUrl;
	@Value("${HearthDataCrawler.key:#{null}}")
	private String key;
	@Value("${HearthDataCrawler.file:#{null}}")
	private String file;
	@Value("${HearthDataCrawler.callType:#{null}}")
	private String callType;
	@Value("${pingan.senderCode:#{null}}")
	private String senderCode;
	@Value("${pingan.intermediaryCode:#{null}}")
	private String intermediaryCode;
	@Value("${pingan.intermediaryName:#{null}}")
	private String intermediaryName;
	
	private static Date startTime;
	private final static Object obj = new Object();
	public void setStartTime(Date startTime) {
		synchronized (obj) {
			HisHearthDataCrawlerConfig.startTime = startTime;
		}
	}
	public Date getStartTime() {
		return startTime;
	}
	
	@Bean("hosClientConfig")
	public HosClientConfig hosClientConfig() {
		HosClientConfig config = new HosClientConfig();
		config.setWsUrl(wsUrl);
		config.setKey(key);
		config.setSenderCode(senderCode);
		config.setIntermediaryCode(intermediaryCode);
		config.setIntermediaryName(intermediaryName);
		if(null != type) {
			config.setType(ClientType.valueOf(type));
		}
		if(null != callType) {
			config.setReqType(RequestType.valueOf(callType));
		}
		return config;
	}

	public String getFile_data() {
		return file;
	}

}
