package com.kasite.core.serviceinterface.module.session;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.sys.oauth.entity.AppEntity;
import com.kasite.core.serviceinterface.module.session.req.SessionUser;

/**
 * 调用 Api 接口的时候需要建立会话，然后调用。
 * @author daiyanshui
 *
 */
public interface ISessionService {

	
	SessionUser createUser(AppEntity app,String sessionId);
	
	SessionUser getUser(AuthInfoVo authInfoVo);
	
}
