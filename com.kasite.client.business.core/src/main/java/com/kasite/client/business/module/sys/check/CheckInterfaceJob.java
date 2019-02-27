package com.kasite.client.business.module.sys.check;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
 
@Component
public class CheckInterfaceJob {
	
	
	
	@Scheduled(cron = "0/30 * * * * ?") // 每30秒执行一次
	public void readLogFileJob() {
		
		
		
	}
	
}