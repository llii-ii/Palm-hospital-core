package com.kasite.client.crawler.micro.controller;

import java.util.Collection;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.kasite.core.common.config.IApplicationStartUp;
import com.kasite.core.common.config.KasiteConfigInit;
import com.kasite.core.common.config.LogResourceServlet;
import com.kasite.core.common.constant.ApiList;


/**
 * 系统启动完成后执行此类的  onApplicationEvent 方法
 * @author daiyanshui
 */

@Component
@Primary
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	private static Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		logger.info("====================================================================================================================");
		logger.info("............................................ 载入全局配置信息........................................................");
		logger.info("====================================================================================================================");
		ApiList.me();
		Collection<IApplicationStartUp> list=new LinkedList<>(event.getApplicationContext().getBeansOfType(IApplicationStartUp.class).values());
        for (IApplicationStartUp obj :list){
        	try {
        		logger.info("执行初始化任务："+ obj.getClass().getName());
        		obj.init(event);
        	}catch (Exception e) {
        		e.printStackTrace();
			}
        }
		logger.info("====================================================================================================================");
		logger.info(".................................................系统启动完成。........................................................");
		logger.info("====================================================================================================================");
		
	}
	
	@Bean
    @Primary
    /**
     * 日志查询面资源
     * @param config
     * @return 
     */
    public ServletRegistrationBean<LogResourceServlet> logResourceServlet(KasiteConfigInit config) { 
		ServletRegistrationBean<LogResourceServlet> servletRegistrationBean = new ServletRegistrationBean<LogResourceServlet>();
		config.getKasiteConfig();
		LogResourceServlet startViewServlet = new LogResourceServlet("http/resources/module/log");
		servletRegistrationBean.setServlet(startViewServlet); 
        servletRegistrationBean.addUrlMappings("/module/log/*");
    	return servletRegistrationBean; 
    }
}
