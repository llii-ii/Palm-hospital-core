package com.kasite.client.business.config;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.kasite.client.business.module.backstage.UploadFileController;
import com.kasite.client.business.module.sys.oauth2.OAuth2Filter;
import com.kasite.client.business.module.sys.oauth2.OAuth2Realm;
import com.kasite.client.business.module.sys.oauth2.PayCallBackFilter;
import com.kasite.client.business.module.sys.oauth2.ThirdPartyCallFilter;
import com.kasite.client.business.module.sys.oauth2.WelcomeCallBackFilter;
import com.kasite.core.common.config.BusinessResourceServlet;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigInit;
import com.kasite.core.common.config.LogResourceServlet;
import com.kasite.core.common.config.MCopyResourceServlet;
import com.kasite.core.common.config.MsgCenterResourceServlet;
import com.kasite.core.common.config.QyWeChatResourceServlet;
import com.kasite.core.common.config.ResourceServlet;
import com.kasite.core.common.config.SchedulerJobResourceServlet;


/**
 * Shiro配置
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤 正常请求
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        //oauth过滤 支付回调
        filters.put("callback", new PayCallBackFilter());
        //oauth过滤 消息回调
        filters.put("msgcallback", new WelcomeCallBackFilter());
        //oauth过滤 第三方接口调用
        filters.put("thirdparty", new ThirdPartyCallFilter());
        
        shiroFilter.setFilters(filters);
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/api/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/v2/api-docs/**", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/**/*.css", "anon");
        filterMap.put("/**/*.js", "anon");
        filterMap.put("/**/*.html", "anon");
        filterMap.put("/**/*.gif", "anon");
        filterMap.put("/**/*.jpg", "anon");
        filterMap.put("/**/*.jpeg", "anon");
        filterMap.put("/**/*.jsp", "anon");
        filterMap.put("/**/*.png", "anon");
        filterMap.put("/**/*.eot", "anon");
        filterMap.put("/**/*.svg", "anon");
        filterMap.put("/**/*.ttf", "anon");
        filterMap.put("/**/*.woff", "anon");
        filterMap.put("/**/*.ico", "anon");
        filterMap.put("/*.txt", "anon");
        filterMap.put("/*.json", "anon");
        
        //系统参数信息
        filterMap.put("/prometheus/**", "anon");
        filterMap.put("/actuator/**", "anon");
        
        filterMap.put("/", "anon");//首页不拦截，默认重定向到登录页面/module/sys/login.html
        filterMap.put("/backstage/login.do", "anon");//后台登录请求不拦截
        filterMap.put("/backstage/wechatLogin.do", "anon");//后台登录请求不拦截
        filterMap.put("/backstage/**/connectQrcode.do", "anon");//后台登录请求不拦截
        filterMap.put("/backstage/qrconnect.do", "anon");//后台登录请求不拦截
        filterMap.put("/bill/qrconnect.do", "anon");//后台退款扫码请求不拦截
        filterMap.put("/backstage/logout.do", "anon");//后台退出请求不拦截
        filterMap.put("/backstage/logoutPayment.do", "anon");//智付后台退出请求不拦截
        
        filterMap.put("/ueditor/**", "anon"); //百度编辑器注册请求不拦截
        filterMap.put("/**/msgDeal.do", "anon"); //企业微信推送消息请求不拦截
        filterMap.put("/**/validation.do", "anon"); //企业微信验证消息请求不拦截
        
        filterMap.put("/**/getOpenId.do", "anon");//后台登录请求不拦截
        filterMap.put("/**/oauthCallback.do", "anon");//后台登录请求不拦截
        filterMap.put("/**/thirdparty.do", "anon");//第三方页面嵌入请求不拦截
        filterMap.put("/**/gotoOauth.do", "anon");//后台登录请求不拦截
        filterMap.put("/**/oauth.do", "anon");//后台登录请求不拦截
        filterMap.put("/**/getToken2.do", "anon");//第三方登录获取Token
        filterMap.put("/**/getToken.do", "anon");//第三方登录获取Token
        filterMap.put("/**/smallProgramLogin.do", "anon");//小程序登录获取Token
        
        filterMap.put("/log/api/querySliceCallLogInfo.do", "anon");//告警日志详情请求不拦截
        filterMap.put("/**/qrPay.do", "anon");//扫码付入口默认通过
        filterMap.put("/**/payNotify.do", "callback");//微信回调类接口特殊鉴权
        filterMap.put("/**/welcome.do", "msgcallback");//微信回调类接口特殊鉴权 
        filterMap.put("/thirdparty/**/execute.do", "thirdparty");//第三方接口调用
        filterMap.put("/thirdparty/**/call.do", "thirdparty");//第三方接口调用
        filterMap.put("/systemApi/*.do", "anon");//系统远程更新
        filterMap.put("/**", "oauth2");
        
        
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    @Bean
    @Primary
    public ServletRegistrationBean<BusinessResourceServlet> businessResourceServlet(KasiteConfigInit config) {
		ServletRegistrationBean<BusinessResourceServlet> servletRegistrationBean = new ServletRegistrationBean<BusinessResourceServlet>();
		config.getKasiteConfig();
		BusinessResourceServlet startViewServlet = new BusinessResourceServlet("http/resources/");
		servletRegistrationBean.setServlet(startViewServlet);
		servletRegistrationBean.addUrlMappings("/business/*");
		servletRegistrationBean.addUrlMappings("/common/*");
		servletRegistrationBean.addUrlMappings("/module/*");
		servletRegistrationBean.addUrlMappings("/pageError.html");
		return servletRegistrationBean;
	}
    
    @Bean
    @Primary
    /**
     * 初始化个性化医院配置页面路径
     * 如果医院有个性化页面从这个路径走
     * @param config
     * @return
     */
    public ServletRegistrationBean<ResourceServlet> resourceServlet(KasiteConfigInit config) { 
		ServletRegistrationBean<ResourceServlet> servletRegistrationBean = new ServletRegistrationBean<ResourceServlet>();
		config.getKasiteConfig();
		String orgCode = KasiteConfig.getOrgCode();
		ResourceServlet startViewServlet = new ResourceServlet("http/resources/business_"+orgCode);
		servletRegistrationBean.setServlet(startViewServlet); 
        servletRegistrationBean.addUrlMappings("/business_"+orgCode+"/*");
    	return servletRegistrationBean; 
    }
    
    
    @Bean
    @Primary
    /**
     * 	定时任务页面
     * @param config
     * @return
     */
    public ServletRegistrationBean<SchedulerJobResourceServlet> schedulerJobResourceServlet(KasiteConfigInit config) { 
		ServletRegistrationBean<SchedulerJobResourceServlet> servletRegistrationBean = new ServletRegistrationBean<SchedulerJobResourceServlet>();
		config.getKasiteConfig();
		SchedulerJobResourceServlet startViewServlet = new SchedulerJobResourceServlet("http/resources/module/job");
		servletRegistrationBean.setServlet(startViewServlet); 
        servletRegistrationBean.addUrlMappings("/module/job/*");
    	return servletRegistrationBean; 
    }
    
    @Bean
    @Primary
    /**
     * 病历复印页面资源
     * @param config
     * @return 
     */
    public ServletRegistrationBean<MCopyResourceServlet> medicalCopyResourceServlet(KasiteConfigInit config) { 
		ServletRegistrationBean<MCopyResourceServlet> servletRegistrationBean = new ServletRegistrationBean<MCopyResourceServlet>();
		config.getKasiteConfig();
		MCopyResourceServlet startViewServlet = new MCopyResourceServlet("http/resources/business/mCopy");
		servletRegistrationBean.setServlet(startViewServlet); 
        servletRegistrationBean.addUrlMappings("/business/mCopy/*");
    	return servletRegistrationBean; 
    }
    
    @Bean
    @Primary
    /**
     * 企业微信页面资源
     * @param config
     * @return 
     */
    public ServletRegistrationBean<QyWeChatResourceServlet> qyWeChatResourceServlet(KasiteConfigInit config) { 
		ServletRegistrationBean<QyWeChatResourceServlet> servletRegistrationBean = new ServletRegistrationBean<QyWeChatResourceServlet>();
		config.getKasiteConfig();
		QyWeChatResourceServlet startViewServlet = new QyWeChatResourceServlet("http/resources/business/qywechat");
		servletRegistrationBean.setServlet(startViewServlet); 
        servletRegistrationBean.addUrlMappings("/business/qywechat/*");
    	return servletRegistrationBean; 
    }
    
    @Bean
    @Primary
    /**
     * 消息中心面资源
     * @param config
     * @return 
     */
    public ServletRegistrationBean<MsgCenterResourceServlet> msgCenterResourceServlet(KasiteConfigInit config) { 
		ServletRegistrationBean<MsgCenterResourceServlet> servletRegistrationBean = new ServletRegistrationBean<MsgCenterResourceServlet>();
		config.getKasiteConfig();
		MsgCenterResourceServlet startViewServlet = new MsgCenterResourceServlet("http/resources/module/msgcenter");
		servletRegistrationBean.setServlet(startViewServlet); 
        servletRegistrationBean.addUrlMappings("/module/msgcenter/*");
    	return servletRegistrationBean; 
    }
    @Bean
    @Primary
    /**
     * 日志查询面资源
     * @param config
     * @return 
     */
    public ServletRegistrationBean<LogResourceServlet> logResourceServlet(KasiteConfigInit config) { 
		ServletRegistrationBean<LogResourceServlet> servletRegistrationBean = new ServletRegistrationBean<LogResourceServlet>();
		config.getKasiteConfig();
		LogResourceServlet startViewServlet = new LogResourceServlet("http/resources/module/log");
		servletRegistrationBean.setServlet(startViewServlet); 
        servletRegistrationBean.addUrlMappings("/module/log/*");
    	return servletRegistrationBean; 
    }
    @Bean
    @Primary
    /**
     * 上传文件的资源路径读取
     * @param config
     * @return 
     */
    public ServletRegistrationBean<UploadFileResourceServlet> upFileResourceServlet(KasiteConfigInit config) { 
		ServletRegistrationBean<UploadFileResourceServlet> servletRegistrationBean = new ServletRegistrationBean<UploadFileResourceServlet>();
		config.getKasiteConfig();
		UploadFileResourceServlet startViewServlet = new UploadFileResourceServlet(KasiteConfig.localConfigPath()+File.separator+(UploadFileController.fileDir));
		servletRegistrationBean.setServlet(startViewServlet); 
        servletRegistrationBean.addUrlMappings("/"+ UploadFileController.fileDir+"/*");
    	return servletRegistrationBean; 
    }
    
}
