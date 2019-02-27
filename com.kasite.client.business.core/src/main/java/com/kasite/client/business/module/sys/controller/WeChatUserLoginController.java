package com.kasite.client.business.module.sys.controller;

import org.springframework.stereotype.Controller;

import com.kasite.core.serviceinterface.common.controller.AbsAddUserController;

/**
 * 依赖微信的用户OpenId的登录 对方通过oauth2调用获取到openId后调用此接口进行登录
 * @author Administrator
 */
@Controller
public class WeChatUserLoginController extends AbsAddUserController{
	public static final String ERRORPAGE= "/pageError.html?code={0}&msg={1}";
	
	
	

}
