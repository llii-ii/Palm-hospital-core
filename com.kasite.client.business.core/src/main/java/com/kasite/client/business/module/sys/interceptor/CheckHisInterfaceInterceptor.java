package com.kasite.client.business.module.sys.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kasite.core.common.config.KasiteConfig;

/**
 * 判断HIS接口是否可用，不可用的时候直接重定向到异常页面
 */
@Component("checkHisInterfaceInterceptor")
public class CheckHisInterfaceInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	KasiteConfig.print("checkHisInterfaceInterceptor:"+request.getContextPath());
    	//判断HIS接口是否可以正常通讯，如果可以正常则继续执行 否则则跳转到异常页面
    	
    	
    	
    	
    	
    	
    	
        return true;
    }
}
