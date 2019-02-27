package com.kasite.core.common.sys.oauth.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.oauth.entity.AppAccessToken;

/**
 * 权限(Token)验证
 * @email 343675979@qq.com
 * @date 2017-03-23 15:38
 */
@Component("authorizationInterceptorApi")
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";
    public static final String LOGIN_USER_ORGCODE_KEY = "LOGIN_USER_ORGCODE_KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthIgnore annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthIgnore.class);
        }else{
            return true;
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
        String appId = request.getHeader("app_id");
        String orgCode = request.getHeader("org_code");
//        boolean downLevel = false;
        //token为空
        if(StringUtils.isBlank(token)){
//            throw new RRException("access_token不能为空");
          JSONObject retJson = new JSONObject();
      	  retJson.put("RespCode", 40003);
      	  retJson.put("RespMsg", "access_token不能为空");
      	  response.getOutputStream().write(retJson.toJSONString().getBytes());
        }
//        try {
        	  //查询token信息
          AppAccessToken tokenEntity = LocalOAuthUtil.getInstall().getAccessToken(token);
          if(tokenEntity == null){
//              throw new RRException("access_token失效，请重新登录", 40000);
        	  JSONObject retJson = new JSONObject();
        	  retJson.put("RespCode", 40000);
        	  retJson.put("RespMsg", "access_token失效，请重新登录");
        	  response.getOutputStream().write(retJson.toJSONString().getBytes());
        	  return false;
          }
          
//        }catch (RRException e) {
//        		e.printStackTrace();
//        		//需要降级处理  服务端接口无法正常访问
//        		if(e.getCode() == -9999) {
//        			
//        			
//        			
//        		}
//		}
//        //如果无法获验证token的有效性，即 卡斯特中心系统挂了  则进行安全降级处理 先保证 使用者双方的调用链路是正常通信的待可以联通服务端后再进行访问校验
//        if(downLevel) {
//	        String oauthPwd = DesUtil.decrypt(oauthInfo, "GBK");
//	        if("downLevelPwdCheck_KASITE".equals(oauthPwd)) {
//	            //设置appId到request里，后续根据appId，获取用户信息
//	            request.setAttribute(LOGIN_USER_KEY, appId);
//	            request.setAttribute(LOGIN_USER_ORGCODE_KEY, orgCode);
//	        		return true;
//	        }
//        }
        //设置appId到request里，后续根据appId，获取用户信息
        request.setAttribute(LOGIN_USER_KEY, appId);
        request.setAttribute(LOGIN_USER_ORGCODE_KEY, orgCode);
        return true;
    }
}
