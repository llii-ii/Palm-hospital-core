package com.kasite.client.business.module.sys.oauth2;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.sys.service.ShiroService;
import com.kasite.core.common.sys.service.SysUserService;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.WhiteListUtil;
import com.kasite.core.serviceinterface.module.basic.cache.BatUserCache;
import com.kasite.core.serviceinterface.module.basic.cache.IBatUserLocalCache;
import com.yihu.hos.util.JSONUtil;

import net.sf.json.JSONObject;


/**
 * 认证
 *
 * @author daiys
 * @email 343675979@qq.com
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
	private static final Logger logger = LoggerFactory.getLogger(OAuth2Realm.class);
    @Autowired
    private ShiroService shiroService;
    @Autowired
	protected SysUserService sysUserService;
    @Autowired
	private WhiteListUtil whiteListUtil;
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
        Long userId = user.getId();

        //用户权限列表
        Set<String> permsSet = null;
		try {
			permsSet = shiroService.getUserPermissions(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取权限列表异常",e);
		}

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }
    
    private void checkCacheBatUser(String openId,AuthInfoVo vo) throws Exception {
    	String configKey = vo.getConfigKey();
    	//调用从微信获取用户信息接口保存用户信息到系统中
		IBatUserLocalCache userLocalCache = SpringContextUtil.getBean(IBatUserLocalCache.class);
		if(null != userLocalCache) {
			BatUserCache cache = userLocalCache.get(openId);
			if(cache==null) {
				ChannelTypeEnum cte = KasiteConfig.getPayTypeByConfigKey(configKey);
				//TODO 这里支付宝需要判断下 如果是支付宝渠道的则需要从支付宝那边获取用户信息。如果没有用户信息就直接只保存一个用户id
				//缓存不存在，调用微信接口获取
				switch (cte) {
				case wechat:
					JSONObject userJs = WeiXinService.getUserInfo(openId, configKey);
					cache = new BatUserCache();
					cache.setOpenId(JSONUtil.getJsonString(userJs, "openid"));
					cache.setNickName(JSONUtil.getJsonString(userJs, "nickname"));
					cache.setSex(JSONUtil.getJsonInt(userJs, "sex", false, 3));
					cache.setCity(JSONUtil.getJsonString(userJs, "city"));
					cache.setCountry(JSONUtil.getJsonString(userJs, "country"));
					cache.setProvince(JSONUtil.getJsonString(userJs, "province"));
					cache.setLanguage(JSONUtil.getJsonString(userJs, "language"));
					cache.setHeadImgUrl(JSONUtil.getJsonString(userJs, "headimgurl"));
					cache.setSubscribe(JSONUtil.getJsonInt(userJs, "subscribe", false));
					cache.setSubscribeTime(JSONUtil.getJsonInt(userJs, "subscribe_time", false));
					cache.setUnionId(JSONUtil.getJsonString(userJs, "unionid", false));
					cache.setGroupId(JSONUtil.getJsonInt(userJs, "groupid", false));
					cache.setConfigKey(configKey);
					userLocalCache.put(openId, cache);
					break;
				case zfb:{
					cache = new BatUserCache();
					cache.setOpenId(openId);
					userLocalCache.put(openId, cache);
					break;
				}
				default:
					break;
				}
			}
		}
    }
    
//    private SysUserEntity saveUser(AuthInfoVo vo) throws Exception {
//    	String openId = vo.getSign();
//    	String configKey = vo.getConfigKey();
//    	String clientId = vo.getClientId();
//		String clientName = KasiteConfig.getClientConfig(ClientConfigEnum.clientName, clientId);
//    	SysUserEntity user = new SysUserEntity();
//		user.setUsername(openId);
//		user.setOperatorId(configKey);
//		user.setOperatorName(clientName);//KasiteConfig.getWxConfig(WXConfigEnum.wx_app_name, configKey)
//		user.setStatus(1);
//		user.setOrgId(KasiteConfig.getOrgCode());
//		user.setOrgName(KasiteConfig.getOrgName());
//		user.setChannelId(configKey);
//		user.setChannelName(clientName);
//		user.setClientId(clientId);
//		sysUserService.save(user);
//		return user;
//    }
    
    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal(); 
        //如果是回调的 则 accessToken = openid；
        SimpleAuthenticationInfo info = null;
        SysUserEntity user = null;
        AuthInfoVo vo = null;
        String openId = null;
        
        if(null != accessToken && (
        		accessToken.startsWith(PayCallBackFilter.payCallbackStart)
        		||accessToken.startsWith(WelcomeCallBackFilter.welComeCallbackStart)
        		||accessToken.startsWith(ThirdPartyCallFilter.hiscallstart) )
        		) {
        	
        	try {	
	        	 vo = KasiteConfig.getAuthInfo2CallBackToken(accessToken);
	        	 if(null != vo ) {
	 	        	openId = vo.getSign();
					user = shiroService.getUser(openId);
	        	 }else {
	        		 logger.info("OAuth2 Realm 获取SysUserEntity异常");
	 				 throw new IncorrectCredentialsException("获取SysUserEntity异常，请重新发起");
	        	 }
	
	             if(user == null && (
	             		accessToken.startsWith(PayCallBackFilter.payCallbackStart)
	             		||accessToken.startsWith(WelcomeCallBackFilter.welComeCallbackStart) )
	             		) {
	     			checkCacheBatUser(openId, vo);
	             }
	             String configKey = vo.getConfigKey();
	             String clientId = vo.getClientId();
	             String clientName = KasiteConfig.getClientConfig(ClientConfigEnum.clientName, clientId);
	             //账号不存在、密码错误
	             if(user == null) {
	            	 user = new SysUserEntity();
	            	 user.setUsername(vo.getSign());
	            	 user.setOperatorId(vo.getSign());
	            	 user.setOperatorName(vo.getSign());
	            	 user.setStatus(1);
	            	 user.setOrgId(KasiteConfig.getOrgCode());
	            	 user.setOrgName(KasiteConfig.getOrgName());
	            	 user.setChannelId(configKey);
	            	 user.setChannelName(clientName);
	            	 user.setClientId(clientId);
					sysUserService.save(user);
				}else {
					if(	!user.getChannelId().equals(configKey)  || !user.getClientId().equals(clientId) ) {
						SysUserEntity u = new SysUserEntity();
						u.setClientId(clientId);
						u.setChannelId(configKey);
						u.setChannelName(clientName);
						u.setOrgId(KasiteConfig.getOrgCode());
						u.setOrgName(KasiteConfig.getOrgName());
						sysUserService.updateUserClientIdAndConfigKey(user.getId(), u);
					}
					user.setOrgId(KasiteConfig.getOrgCode());
					user.setOrgName(KasiteConfig.getOrgName());
					user.setChannelId(configKey);
					user.setClientId(clientId);
					user.setChannelName(clientName);
				}
				if(null == user || user.getStatus() == 0) {//账号锁定
	                throw new LockedAccountException("账号已被锁定,请联系管理员");
				}
				info = new SimpleAuthenticationInfo(user, accessToken, getName());
        	} catch (Exception e) {
				e.printStackTrace();
				 logger.info("OAuth2 Realm 获取SysUserEntity异常");
 				 throw new IncorrectCredentialsException("获取SysUserEntity异常，请重新发起");
			}
        }else {
            //根据accessToken，查询用户信息
            SysUserTokenEntity tokenEntity = null;
    		try {
    			tokenEntity = shiroService.queryByToken(accessToken);
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.error("获取TokenEntity异常",e);
    		}
            if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
                throw new IncorrectCredentialsException("token失效，请重新登录");
            }
            //查询用户信息
    		try {
    			user = shiroService.queryUser(tokenEntity.getId());
//    			String clientId = user.getClientId();
//    			//判断管理后台是否有配置支付渠道允许的支付商户，如果有的话允许进行对应的商户进行操作
//    			if(StringUtil.isNotBlank(clientId) && KstHosConstant.SYSTEMMANAGER_CHANNEL_ID.equalsIgnoreCase(clientId)) {
//    				String wxpayconfig = KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, clientId);
//    				String zfbpayconfig = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
//    				StringBuffer sbf = new StringBuffer();
//    				if(StringUtil.isNotBlank(wxpayconfig)) {
//    					sbf.append(wxpayconfig);
//    				}
//    				if(StringUtil.isNotBlank(zfbpayconfig)) {
//    					sbf.append(",").append(wxpayconfig);
//    				}
//    				if(StringUtil.isNotBlank(sbf.toString())) {
//    					user.setChannelId(sbf.toString());
//    				}
//    			}
    			user.setOrgId(KasiteConfig.getOrgCode());
				user.setOrgName(KasiteConfig.getOrgName());
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.error("查询用户信息异常",e);
    		}
            //账号锁定
            if(null == user || user.getStatus() == 0){
                throw new LockedAccountException("账号已被锁定,请联系管理员");
            }
            info = new SimpleAuthenticationInfo(user, accessToken, getName());
        }
        
        //TODO 用户是否能通过权限验证
        if(whiteListUtil.isOpenWhiteList() && !whiteListUtil.isWhiteUser(user.getUsername())) {
        	 throw new IncorrectCredentialsException("系统目前升级维护中");
		}
        
        return info;
    }
}
