package com.kasite.client.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class SchedulerJobVersion {
	protected static Logger logger = LoggerFactory.getLogger(SchedulerJobVersion.class);
	private static String v = "1.0";
	static {
		logger.info("定时任务模块版本号："+v);
	}
}
