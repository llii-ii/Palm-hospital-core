package com.kasite.client.crawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfig {
    @Value("${elastic.url:#{null}}")
    private String url;
    
    public String getUrl() {
		return url;
	}
    
}
