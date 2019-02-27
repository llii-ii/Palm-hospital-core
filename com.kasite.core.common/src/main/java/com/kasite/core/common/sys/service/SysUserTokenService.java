package com.kasite.core.common.sys.service;

import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;
import com.kasite.core.common.util.R;

/**
 * 用户Token
 * 
 * @author chenshun
 * @email 343675979@qq.com
 * @date 2017-03-23 15:22:07
 */
public interface SysUserTokenService {

	SysUserTokenEntity queryByUserId(Long userId) throws Exception;

	SysUserTokenEntity queryByToken(String token) throws Exception;
	
	void save(SysUserTokenEntity token) throws Exception;
	
	void update(SysUserTokenEntity token) throws Exception;

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId, String user_agent, long exptimes,String token) throws Exception;

	
}
