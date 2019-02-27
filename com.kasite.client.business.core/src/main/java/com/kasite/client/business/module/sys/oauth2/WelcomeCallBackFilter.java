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
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.web.util.UriTemplate;

import com.google.gson.Gson;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.spring.HttpContextUtils;
import com.kasite.core.common.util.wxmsg.IDSeed;

/**
 * 微信或支付宝welcome
 *
 * @author daiys
 * @email 343675979@qq.com
 */
public class WelcomeCallBackFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((WelcomeDoHttpServletRequestWrapper) request);
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
    	//拦截微信／支付宝的请求报文，获取openid
    	WelcomeDoHttpServletRequestWrapper requestWrapper = new WelcomeDoHttpServletRequestWrapper((HttpServletRequest) request);
        return executeLogin(requestWrapper, response);
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
    public final static String welComeCallbackStart = "welcomecallback_";
    /**
     * 获取请求的token
     */
    public static String getRequestToken(WelcomeDoHttpServletRequestWrapper httpRequest){
    	String content = httpRequest.getContent();
    	String token = welComeCallbackStart+ IDSeed.next();
    	String openid = "";
    	if(null != content && !"".equals(content)) {
    		try {
				openid = DocumentHelper.parseText(content).getRootElement().elementText("FromUserName");
			} catch (DocumentException e) {
				e.printStackTrace();
			}
    	}
//        //从header中获取token
//        String token = httpRequest.getHeader("openId");
//        //如果header中不存在token，则从参数中获取token
//        if(StringUtils.isBlank(token)){
//            token = httpRequest.getParameter("openId");
//        }
    	String path = "/weixin/{clientId}/{configKey}/welcome.do";
    	String urlPath = httpRequest.getRequestURI();
    	UriTemplate template = new UriTemplate(path);
    	Map<String, String> map = template.match(urlPath);
    	String clientId = map.get("clientId");
        String configKey = map.get("configKey");
        
        if(StringUtil.isBlank(clientId) && StringUtil.isBlank(configKey)) {
        	path = "/alipay/{clientId}/{configKey}/welcome.do";
        	template = new UriTemplate(path);
        	map = template.match(urlPath);
        	clientId = map.get("clientId");
            configKey = map.get("configKey");
        }
        
        AuthInfoVo vo = new AuthInfoVo();
        vo.setClientId(clientId);
        vo.setConfigKey(configKey);
        vo.setSessionKey(token);
        vo.setClientVersion(KasiteConfig.getOrgCode());
        if(StringUtil.isBlank(openid)) {
        	openid = configKey;
        }
        vo.setSign(openid);
        vo.setUuid(token);
    	token = KasiteConfig.setCallBackToken(welComeCallbackStart, clientId, configKey, openid, KasiteConfig.getOrgCode());
        httpRequest.getSession().setAttribute("token", token);
//        tokenMap.put(key,vo);//缓存1秒如果没有读走默认删除
//        return token;//"callback_"+token+SPLIT+openid;
    	return token;
    }


}
