package com.kasite.client.crawler.micro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.kasite.core.common.annotation.LoginUser;
import com.kasite.core.common.sys.oauth.entity.AppEntity;
import com.kasite.core.common.sys.oauth.interceptor.AuthorizationInterceptor;
import com.kasite.core.common.sys.oauth.service.AppService;

/**
 */
@Component("sysUpdateHandlerMethodArgumentResolverApi")
public class SysUpdateHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private AppService appServiceApi;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(AppEntity.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        //获取用户ID
        Object object = request.getAttribute(AuthorizationInterceptor.LOGIN_USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if(object == null){
            return null;
        }
        //获取用户信息
        AppEntity user = appServiceApi.getApp((String)object);

        return user;
    }
}
