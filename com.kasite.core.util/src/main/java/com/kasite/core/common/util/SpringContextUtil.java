package com.kasite.core.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author linjianfa
 * @Description: TODO Spring 全局变量工具类
 * @version: V1.0  
 * 2017年10月23日 下午2:53:42
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {  

    /** Spring应用上下文环境*/
    private static ApplicationContext applicationContext;  
  
    /** 
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境 
     *  
     * @param applicationContext 
     */  
    public void setApplicationContext(ApplicationContext applicationContext) {  
    	SpringContextUtil.applicationContext = applicationContext;  
    }  
  
    /** 
     * @return ApplicationContext 
     */  
    public ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
    /** 
     * 获取对象 
     * 这里重写了bean方法，起主要作用 
     * @param name 
     * @return Object 一个以所给名字注册的bean的实例 
     * @throws BeansException 
     */  
    public static  <T> T getBean(Class<T> requiredType) throws BeansException {
    	try {
    		return applicationContext.getBean(requiredType);  
    	}catch (Exception e) {
			// TODO: handle exception
    		return null;
		}
        
    }  
    /** 
     * 获取对象 
     * 这里重写了bean方法，起主要作用 
     * @param name 
     * @return Object 一个以所给名字注册的bean的实例 
     * @throws BeansException 
     */  
    public static Object getBean(String name) throws BeansException {  
        return applicationContext.getBean(name);  
    }  
    /**
     * 	根据Bean名称获取对象，异常时返回空
     * @param name
     * @return
     */
    public static Object getBeanByName(String name){  
    	try {
    		 return applicationContext.getBean(name);
    	}catch (Exception e) {
//    		e.printStackTrace();
    		return null;
		}
    }  
}  