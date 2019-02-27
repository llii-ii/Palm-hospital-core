package com.kasite.client.business.module.sys.oauth2;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.util.UriTemplate;

import com.google.gson.Gson;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.spring.HttpContextUtils;

/**
 * 微信或支付宝回调的请求拦截
 * @author daiys
 * @email 343675979@qq.com
 */
public class PayCallBackFilter extends AuthenticatingFilter {
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
        String token = getRequestToken((HttpServletRequest) request);
        if(StringUtils.isBlank(token)){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Content-Type:application/json", "charset=UTF-8");
            httpResponse.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
            String urlPath = req.getRequestURI();
            String json = new Gson().toJson(R.error(KstHosConstant.OAUTH_TOKEN_INVALID, "invalid token urlPath = "+urlPath));
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
    public final static String payCallbackStart = "callback_";
    /**
     * 获取请求的token
     */
    public static String getRequestToken(HttpServletRequest httpRequest){
    	String path = "";
        String wxPath = "/wxPay/{clientId}/{configKey}/{openId}/{token}/{orderId}/payNotify.do";
        String zfbPath = "/alipay/{clientId}/{configKey}/{openId}/{token}/{orderId}/payNotify.do";
        String unionPayPath = "/unionPay/{clientId}/{configKey}/{openId}/{token}/{orderId}/payNotify.do";
        String swiftpassPayPath = "/swiftpass/{clientId}/{configKey}/{openId}/{token}/{orderId}/payNotify.do";
        String netPayPath = "/netPay/{clientId}/{configKey}/{openId}/{token}/{orderId}/payNotify.do";
    	String urlPath = httpRequest.getRequestURI();
    	if(null != urlPath && urlPath.startsWith("/alipay")) {
    		path = zfbPath;
    	}else if(null != urlPath && urlPath.startsWith("/wxPay")) {
    		path = wxPath;
    	}else if(null != urlPath && urlPath.startsWith("/unionPay")) {
    		path = unionPayPath;
    	}else if(null != urlPath && urlPath.startsWith("/netPay")) {
    		path = netPayPath;
    	}else if(null != urlPath && urlPath.startsWith("/swiftpass")) {
    		path = swiftpassPayPath;
    	}
    	
    	UriTemplate template = new UriTemplate(path);
    	Map<String, String> map = template.match(urlPath);
    	String openid = map.get("openId");
        String token = map.get("token");
        String clientId = map.get("clientId");
        String configKey = map.get("configKey");
        token = KasiteConfig.setCallBackToken(payCallbackStart, clientId, configKey, openid, KasiteConfig.getOrgCode());
        return token;//"callback_"+token+SPLIT+openid;
    	
    }


}
