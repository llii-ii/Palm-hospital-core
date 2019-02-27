package com.kasite.server.verification.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 系统启动完成后执行此类的  onApplicationEvent 方法
 * @author daiyanshui
 */

@Component
@Primary
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	private static Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		logger.debug("====================================================================================================================");
		logger.debug(".................................................系统启动完成。........................................................");
		logger.debug("====================================================================================================================");
		
	}
}
