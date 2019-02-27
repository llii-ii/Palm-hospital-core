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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriTemplate;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteDiyConfig;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.rsa.KasiteRSAException;
import com.kasite.core.common.util.rsa.KasiteRSAUtil;
import com.kasite.core.common.util.spring.HttpContextUtils;
import com.kasite.core.common.util.wxmsg.IDSeed;

/**
 * 第三方直接通过post调用接口
 *
 * @author daiys
 * @email 343675979@qq.com
 */
public class ThirdPartyCallFilter extends AuthenticatingFilter {

	private static final Logger logger = LoggerFactory.getLogger(ThirdPartyCallFilter.class);
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
    enum DiyConfig implements KasiteDiyConfig{
    	appIdConfig,
		;
		@Override
		public String getNodeName() {
			return this.name();
		}
    }
    public final static String hiscallstart = "THIRDPARTY_";
    /**
     * 获取请求的token
     */
    public static String getRequestToken(WelcomeDoHttpServletRequestWrapper httpRequest) throws Exception {
    	String clientId = null,orgCode =null,appId =null,configKey = null;
		String token = null,secret = null,cargeType = null;
    	try {
        	String path = "/thirdparty/{clientId}/{orgCode}/{appId}/{module}/{name}/{method}/execute.do";
        	String urlPath = httpRequest.getRequestURI();
        	boolean isRSACall = false;
        	if(urlPath.endsWith("/call.do")) {
        		isRSACall = true;
        		path = "/thirdparty/{clientId}/{orgCode}/{appId}/{module}/{name}/{method}/call.do";
        	}
        	//目前只支持支付宝和微信支付
        	UriTemplate template = new UriTemplate(path);
        	Map<String, String> map = template.match(urlPath);
        	clientId = map.get("clientId");
        	orgCode = map.get("orgCode");
        	appId = map.get("appId");
        	//签名
        	secret = httpRequest.getParameter("sign");
        	// 0 现金充值 1 支付宝 2 银联 3 微信
        	cargeType = httpRequest.getParameter("cargeType");
        	String openid = httpRequest.getParameter("openId");
        	if(StringUtil.isBlank(openid)) {
        		openid = appId;
        	}else {
        		openid = appId+"_"+openid;
        	}
        	//这里对个性化医院渠道限制密钥放开 需要通过医院对个性化渠道进行配置后才能生效，
        	//目前只对医科大学附属第二医院做个性化，最好不要针对其它医院有此类个性化配置避免影响系统稳定
        	String diyAppIdConfig = KasiteConfig.getDiyVal(DiyConfig.appIdConfig);
        	if(StringUtil.isNotBlank(diyAppIdConfig)) {
        		try {
        			JSONObject json = JSONObject.parseObject(diyAppIdConfig);
        			String appIdSign = json.getString(clientId+"_"+appId);
        			if(StringUtil.isBlank(secret)) {
        				secret = appIdSign;
        			}
        		}catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        	
        	//渠道 与 密钥进行校验
        	if(StringUtil.isBlank(secret)) {
        		throw new RRException("参数不能为空：【sign】");
        	}
        	token = hiscallstart + secret +"_"+ IDSeed.next();
        	String orgCodeApp = KasiteConfig.getOrgCode();
        	if(!orgCodeApp.equals(orgCode)) {
        		throw new RRException("参数机构代码不一致：【orgCode】");
        	}
        	
        	String clientName = KasiteConfig.getClientConfig(ClientConfigEnum.clientName, clientId);
    		String appSecret = KasiteConfig.getClientAppSecret(clientId, appId);
    		if(null == clientName) {
    			throw new RRException(RetCode.Common.OAUTH_ERROR_NOPROMIIME,"该渠道ID【"+ clientId +"】，未开放请联系管理员开放。");
    		}else if(StringUtil.isBlank(appSecret)) {
    			throw new RRException(RetCode.Common.OAUTH_ERROR_NOPROMIIME,"渠道【"+clientName+"|"+clientId+"】的应用ID【"+ appId +"】，未开放请联系管理员开放。");
    		}
    		if(!secret.equals(appSecret) && !isRSACall) {
    			throw new RRException(RetCode.Common.OAUTH_ERROR_NOPROMIIME,"获取Token异常,鉴权失败，请确认 appId 和 appSecret 是有效的。appId="+appId);
    		}
    		//验证签名
    		if(isRSACall) {
    			String data = httpRequest.getParameter("data");
    			if(StringUtil.isBlank(data)) {
    				throw new RRException("参数【data】不能为空。");
    			}
    			String publicKey = KasiteConfig.getAppPublicKey(appId);
    			if(StringUtil.isBlank(publicKey)) {
    				throw new RRException("应用公钥不能为空【"+appId+"_publicKey】,请联系管理员配置应用的共钥。");
    			}
    			boolean bb = KasiteRSAUtil.rsaCheck(data, secret, publicKey, "utf-8", "RSA2");
    			if(!bb) {
    				throw new RRException("签名鉴权未通过，请核对请求报文：data 与 私钥的加签是否正常，请联系管理员进行核对。");
    			}
    		}
        	if(StringUtil.isBlank(cargeType)) {
        		throw new RRException("请传入渠道类型：【cargeType】  1 支付宝 2 银联 3 微信 4 当面付支付宝 5 当面付微信 6 企业微信");
        	}
        	try {
        		Integer cargeTypeInt = Integer.valueOf(cargeType);
        		switch (cargeTypeInt) {
    			case 1://支付宝
    				//获取支付宝对应的configkey
    				configKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
    				break;
    			case 3://微信 
    				configKey = KasiteConfig.getClientConfig(ClientConfigEnum.WeChatConfigKey, clientId);
    				break;
    			case 4://4支付宝
    				configKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
    				break;
    			case 5://5微信
    				configKey = KasiteConfig.getClientConfig(ClientConfigEnum.WeChatConfigKey, clientId);
    				break;
    			case 6://企业微信
    			{
    				configKey = KasiteConfig.getClientConfig(ClientConfigEnum.QyWeChatConfigKey, clientId);
    				break;
    			}
    			default:
    				throw new RRException("目前只支持2个渠道 1 支付宝和 3 微信 6 企业微信");
    			}
        	}catch (Exception e) {
        		if(e instanceof RRException) throw e;
        		throw new RRException("渠道类型：cargeType  1 支付宝 2 银联 3 微信 6 企业微信.");
    		}
        	if(null == configKey) {
        		throw new RRException("该渠道未分配商户配置 ConfigKey ");
        	}
        	
        	//通过clientId 获取 configKey 
//            String configKey = map.get("configKey");
        	token = KasiteConfig.setCallBackToken(hiscallstart, clientId, configKey, openid, orgCodeApp);
            httpRequest.getSession().setAttribute("token", token);
//            return token;//"callback_"+token+SPLIT+openid;
        	return token;
    	}catch(KasiteRSAException e) {
    		//String clientId = null,orgCode =null,appId =null,configKey = null;
    		logger.info("clientId ={},orgCode ={},appId ={},configKey ={}", clientId,orgCode,appId,configKey);
    		logger.error("第三方调用授权异常",e);
    		throw new RRException("签名验证失败："+e.getMessage());
		}catch (Exception e) {
    		//String clientId = null,orgCode =null,appId =null,configKey = null;
    		logger.info("clientId ={},orgCode ={},appId ={},configKey ={}", clientId,orgCode,appId,configKey);
    		logger.error("第三方调用授权异常",e);
			try{
				String urlPath = httpRequest.getRequestURI();
	        	LogUtil.info(logger, new LogBody(KasiteConfig.createAuthInfoVo(ThirdPartyCallFilter.class)
	        			).set("urlPath", urlPath)
	        			.set("secret", secret)
	        			.set("cargeType", cargeType)
	        			.set(e)
	    			);
			}catch (Exception e2){
				e.printStackTrace();
			}
    		throw e;
		}
    	
    }

    
}
