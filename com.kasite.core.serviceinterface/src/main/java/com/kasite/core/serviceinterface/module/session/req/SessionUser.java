package com.kasite.core.serviceinterface.module.session.req;

import java.sql.Timestamp;

import com.kasite.core.common.bean.bo.AuthInfoVo;
/**
 * 用户调用接口的时候需要先创建一个会话id 保存用户会话信息
 * @author daiyanshui
 *
 */
public class SessionUser {

	/**
	 * 登录后创建的会话ID
	 */
	private String sessionId;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 最后登录时间
	 */
	private Timestamp updateTime;
	/**
	 * 会话信息
	 */
	private AuthInfoVo authInfoVo;
	
	public AuthInfoVo getAuthInfoVo() {
		return authInfoVo;
	}
	public void setAuthInfoVo(AuthInfoVo authInfoVo) {
		this.authInfoVo = authInfoVo;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
