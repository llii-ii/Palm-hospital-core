package com.kasite.client.job.utils;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.SpringContextUtil;


/**
 * 执行定时任务
 * 
 */
public class SchedulerRunnable implements Runnable {
	private Object target;
	private Method method;
	private String params;
	
	public SchedulerRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
		this.target = SpringContextUtil.getBean(beanName);
		this.params = params;
		
		if(StringUtils.isNotBlank(params)){
			this.method = target.getClass().getDeclaredMethod(methodName, String.class);
		}else{
			this.method = target.getClass().getDeclaredMethod(methodName);
		}
	}

	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(method);
			if(StringUtils.isNotBlank(params)){
				method.invoke(target, params);
			}else{
				method.invoke(target);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RRException("执行定时任务方法抛出异常："+e.getLocalizedMessage(), e);
		}
	}

}
