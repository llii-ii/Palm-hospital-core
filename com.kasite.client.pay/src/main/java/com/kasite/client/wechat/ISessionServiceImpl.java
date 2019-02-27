package com.kasite.client.wechat;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.sys.oauth.entity.AppEntity;
import com.kasite.core.serviceinterface.module.session.ISessionService;
import com.kasite.core.serviceinterface.module.session.req.SessionUser;

public class ISessionServiceImpl implements ISessionService{

	
	
	
	
	@Override
	public SessionUser createUser(AppEntity app, String sessionId) {
		// TODO Auto-generated method stub
		// 微信/支付宝 鉴权回调的时候 创建一个用户的会话ID并返回
		
		return null;
	}

	@Override
	public SessionUser getUser(AuthInfoVo authInfoVo) {
		// TODO Auto-generated method stub
		// 微信/支付宝 通过查询登录用户的会话信息
		
		
		
		return null;
	}

}
