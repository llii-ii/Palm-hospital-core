package com.kasite.client.crawler.modules.api.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kasite.client.crawler.config.HisHearthDataCrawlerConfig;
import com.kasite.client.crawler.modules.api.service.IPingAnService;
import com.kasite.core.common.util.DateOper;
 
@Component
public class PingAnJob {
	private static final Logger logger = LoggerFactory.getLogger(PingAnJob.class);
	private static boolean isNotRun = true;
	private static boolean isNotRun_dealData = true;
	private static boolean isNotRun_ReadLogFile = true;
	
	@Autowired
	private HisHearthDataCrawlerConfig hisHearthDataCrawlerConfig;
	@Autowired
	private IPingAnService pingAnServiceImpl;
	
//	@Scheduled(cron = "0/3 * * * * ?") // 每3秒执行一次
	public void runC100() {
		if(isNotRun) {
			isNotRun = false;
				try {
					Date time = hisHearthDataCrawlerConfig.getStartTime();
					if(null == time) {
						time = DateOper.getNowDate();
						hisHearthDataCrawlerConfig.setStartTime(time);
					}
					pingAnServiceImpl.doC100(time);
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("执行C100异常",e);
				}finally {
					Date time = DateOper.getNowDate();
					hisHearthDataCrawlerConfig.setStartTime(time);
				}
			isNotRun = true;
		}
	}
	
//	@Scheduled(cron = "0/3 * * * * ?") // 每3秒执行一次
//	public void runC100_001() {
//		if(isNotRun) {
//			isNotRun = false;
//				try {
//					Date time = hisHearthDataCrawlerConfig.getStartTime();
//					pingAnServiceImpl.doC100(time);
//				}catch (Exception e) {
//					e.printStackTrace();
//					logger.error("执行C100异常",e);
//				}
//			isNotRun = true;
//		}
//	}
	
	
//	@Scheduled(cron = "0/3 * * * * ?") // 每3秒执行一次
	public void runC220() {
		if(isNotRun) {
			isNotRun = false;
				try {
					Date time = hisHearthDataCrawlerConfig.getStartTime();
					if(null == time) {
						time = DateOper.getNowDate();
						hisHearthDataCrawlerConfig.setStartTime(time);
					}
					pingAnServiceImpl.doC220(time);
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("执行C100异常",e);
				}finally {
					Date time = DateOper.getNowDate();
					hisHearthDataCrawlerConfig.setStartTime(time);
				}
			isNotRun = true;
		}
	}

}