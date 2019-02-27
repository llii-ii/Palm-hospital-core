package com.kasite.client.business.module.sys.controller;

import java.net.URLEncoder;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.client.zfb.service.AlipayService;
import com.kasite.client.zfb.util.AliUserToBatUserCache;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SendQVWarnUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.common.controller.AbsAddUserController;
import com.kasite.core.serviceinterface.module.basic.cache.BatUserCache;

import net.sf.json.JSONObject;

/**
 * 嵌入第三方的应用需要 auth2.0验证的
 * 本工程  微信和支付宝的菜单跳转入口不在这里  在 WeiXinController
 * @author Administrator
 */
@Controller
public class WeChatOAuth2Controller extends AbsAddUserController{
	public static final String ERRORPAGE= "/pageError.html?code={0}&msg={1}";
	/**
	 * 授权回调
	 * @throws Exception 
	 */
	@RequestMapping(value = "{clientId}/{configKey}/oauth.do")
	@ResponseBody
	public void miniPayOauthCallback(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String toUrl = request.getParameter("toUrl");
		String errorcode = "";
    	String errormsg = "";
    	AuthInfoVo vo = null;
    	//TODO 判断是微信还是支付宝  通过配置获取 configKey
    	Boolean isWeChat = isWeChat(request);
    	if(null == isWeChat) {
    		errorcode = RetCode.Common.ERROR_TOKEN_INVOKE.getCode()+"";
    		errormsg = "请在支付宝或微信进行操作！";
    		//如果异常调转到异常页面
			if(StringUtil.isNotBlank(errorcode)) {
				toUrl = MessageFormat.format(ERRORPAGE, errorcode,URLEncoder.encode(errormsg, "utf-8"));
			}
			sendRedirect(response,KasiteConfig.getKasiteHosWebAppUrl()+toUrl);
    		return;
    	}
    	//改成通过 clientId 获取不同渠道对应的支付宝和微信的支付configKey
    	//取到对应的微信配置
		String authCode = null;
    	if(!isWeChat) {//支付宝
    		authCode = request.getParameter("auth_code");
    	}else if(isWeChat) {//微信
    		authCode = request.getParameter("code");
    	}
    	logger.info("configKey="+configKey+"&code="+authCode+"&clientId="+clientId);
    	//判断是否已经微信回调
		if(StringUtil.isBlank(authCode)) {
			String times = request.getParameter("times");
			if(null == times) {
				times = "1";
			}else {
				Integer timesi = Integer.parseInt(times);
				if(timesi > 3) {
					throw new RRException("访问地址操过3次未取到微信的token");
				}
				times = (timesi + 1) + "";
			}
    		String redirectUri = toUrl+"?times="+times;//跳转到微信进行鉴权回调
    		if(isWeChat) {
        		gotoWeChatOauth(clientId, configKey,"snsapi_userinfo", redirectUri, request, response);
    		}else {
    			gotoAliOauth(clientId, configKey, redirectUri,request, response);
    		}
    		return;
		}
		String openid = "";//微信openid。支付宝用户id
		try {
	    	//鉴权回调跳转
	    	if(isWeChat) {
    			//获取到用户微信相关信息
    			// 通过code换取网页授权access_token
				JSONObject json = WeiXinService.getAuthToken(authCode, configKey);
				if (json != null && StringUtil.isNotBlank(json.getString("openid"))) {
					openid = json.getString("openid");
					String access_token = json.getString("access_token");
			    	JSONObject userJs = WeiXinService.getSnsapiUserInfo(openid,access_token);
			    	if(null != userJs) {
			    		vo = login(false, openid, clientId, configKey, null,request, response);
						addWeChatUser(openid, vo, userJs);
					}else {
						throw new RRException("调用微信获取用户信息接口异常，请核实与微信端的请求是否正常。wechat_user_info");
					}
					String token = vo.getSessionKey();
					int index = toUrl.lastIndexOf("?");
					if(index >0) {
						toUrl = toUrl+"&token="+token;
					}else {
						toUrl = toUrl+"?token="+token;
					}//getUserInfo(openid, configKey);
					
				}else {
					throw new RRException("未获取到微信用户信息 OpenId 请联系管理员。wechat_sns_oauth2_access_token");
				}
	    	}else {//支付宝
	    		AlipaySystemOauthTokenResponse tokenResp = AlipayService.getAuthTokenFromAlipay(configKey,authCode);
				if (tokenResp != null && StringUtils.isNotBlank(tokenResp.getUserId())) {
					openid = tokenResp.getUserId();
					//调用支付宝接口获取用户信息
					AlipayUserInfoShareResponse userResp = AlipayService.getUserInfo(configKey,openid,tokenResp.getAccessToken());
					String zfbId = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, configKey);
					if (null != userResp && userResp.isSuccess()) {
						BatUserCache cache = AliUserToBatUserCache.parse(response,tokenResp, userResp, configKey);
						vo = login(false, openid, clientId, configKey, userResp.getNickName(),request, response);
						addUser(openid, zfbId, vo, cache);
					}else {
						throw new RRException("未获取到支付宝用户信息 UserId 请联系管理员。resp="+ (null != userResp?userResp.getBody():""));
					}
				}
	    	}
	    	if(null == vo) {
	    		throw new RRException("未获取到用户的鉴权信息。");
	    	}
    	}catch (Exception e) {
    		e.printStackTrace();
    		if(e instanceof RRException){
        		RRException exp = (RRException) e;
        		errorcode = exp.getCode()+"";
        		errormsg = exp.getMessage();
        	}else {
        		errorcode = "500";
        		errormsg = "系统出现异常："+e.getMessage();
        	}
    		LogUtil.error(logger, e);
    		SendQVWarnUtil.sendWarnInfo(clientId, clientId+"/"+configKey+"/oauth.do", DateOper.getNowDate().getTime(), e);
    		throw e;
		}finally {
			//如果异常调转到异常页面
			if(StringUtil.isNotBlank(errorcode)) {
				toUrl = MessageFormat.format(ERRORPAGE, errorcode,URLEncoder.encode(errormsg, "utf-8"));
			}
			if(toUrl.startsWith("http")) {
				sendRedirect(response, toUrl);
			}else {
				sendRedirect(response, KasiteConfig.getKasiteHosWebAppUrl()+toUrl);
			}
			
			Logger.get().info(new com.kasite.core.common.log.LogBody(vo).set("isWeChat", isWeChat).set("clientId", clientId).set("openId", openid));

		}
	}
	
	
}
