package com.kasite.client.business.module.sys.oauth2;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.spring.HttpContextUtils;

/**
 * oauth2过滤器
 *
 * @author daiys
 * @email 343675979@qq.com
 */
public class OAuth2Filter extends AuthenticatingFilter {

	private static final Logger logger = LoggerFactory.getLogger(OAuth2Filter.class);
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);
        if(StringUtils.isBlank(token)){
            return null;
        }
        return new OAuth2Token(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回401
    	HttpServletRequest req = (HttpServletRequest) request;
    	if("OPTIONS".equals(req.getMethod())) {
    		//默认通过跨域访问你发起的OPTIONS请求
    		return true;
    	}
        String token = getRequestToken((HttpServletRequest) request);
        if(StringUtils.isBlank(token)){
        	JSONObject param = new JSONObject();
        	//请求的参数
			try{
				param = (JSONObject) JSON.toJSON(request.getParameterMap());
			}catch (Exception e){
				e.printStackTrace();
				logger.error("解析入参异常",e);
			}
        	String urlPath = req.getRequestURI();
        	LogUtil.info(logger, new LogBody(KasiteConfig.createAuthInfoVo(getClass())
        			).set("urlPath", urlPath)
        			.set(param)
    			);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Content-Type:application/json", "charset=UTF-8");
            httpResponse.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
            String json = new Gson().toJson(R.error(KstHosConstant.OAUTH_TOKEN_INVALID, "invalid token urlPath ="+ urlPath));
            httpResponse.getWriter().print(json);
            return false;
        }
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            R r = R.error(HttpStatus.SC_UNAUTHORIZED, throwable.getMessage());
            String json = new Gson().toJson(r);
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {

        }

        return false;
    }

    /**
     * 获取请求的token
     */
    public static String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = httpRequest.getParameter("token");
        }
        return token;
    }


}
