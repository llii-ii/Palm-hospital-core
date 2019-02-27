package com.kasite.server.verification.module.app.interceptor;


import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.exception.RRException;
import com.kasite.server.verification.module.app.entity.AppAccessToken;
import com.kasite.server.verification.module.app.service.AccessTokenService;

/**
 * 权限(Token)验证
 * @email 343675979@qq.com
 * @date 2017-03-23 15:38
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
	protected static final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
    @Autowired
    private AccessTokenService accessTokenService;

    public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthIgnore annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthIgnore.class);
        }else{
            return true;
        }
        try {
        	Enumeration<String> h = request.getHeaderNames();
      		if(null != h) {
          		String pathInfo =request.getServletPath();
      			StringBuffer hstr = new StringBuffer().append("PATH=").append(pathInfo);
      			while(h.hasMoreElements()) {
      				String element = h.nextElement();
      				String requestHeaderValue = request.getHeader(element);
      				hstr.append("|").append(element).append("=").append(requestHeaderValue);
      			}
      			logger.info(hstr.toString());
      		}
        }catch (Exception e) {
        	e.printStackTrace();
        	logger.error("获取请求头信息异常",e);
		}
        //如果有@IgnoreAuth注解，则不验证token
        if(annotation != null){
            return true;
        }

        //从header中获取token
        String token = request.getHeader("access_token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = request.getParameter("access_token");
        }
        //token为空
        if(StringUtils.isBlank(token)){
            throw new RRException("access_token不能为空");
        }
        //查询token信息
        AppAccessToken tokenEntity = accessTokenService.queryByToken(token);
        if(tokenEntity == null || tokenEntity.getInvalidTime().getTime() < System.currentTimeMillis()){
            throw new RRException("access_token失效，请重新登录", 40000);
        }
        //设置appId到request里，后续根据appId，获取用户信息
        request.setAttribute(LOGIN_USER_KEY, tokenEntity.getAppId());

        return true;
    }
}
