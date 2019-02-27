package com.kasite.client.job.test;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.kasite.core.common.util.DateOper;

@Component
public class TestJob {
	
	private final static Log log = LogFactory.getLog(TestJob.class);
	
	public void print() {
		
		try {
			System.out.println(DateOper.getNow("yyyy-MM-dd HH:mm:ss")+"____定时任务测试........");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void testPrint(String params) {
		try {
			System.out.println(DateOper.getNow("yyyy-MM-dd HH:mm:ss")+"____定时任务测试........+++"+params);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
