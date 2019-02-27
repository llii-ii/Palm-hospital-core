package com.kasite.core.common.sys.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqAddUser
 * @author: lcz
 * @date: 2018年8月29日 下午4:44:42
 */
public class ReqAddUser extends AbsReq {

	private String username;
	private String realName;
	private String mobile;
	private String password;
	private Long roleId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @Title: ReqAddUser
	 * @Description:
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqAddUser(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.username = getDataJs().getString("Username");
			this.realName = getDataJs().getString("RealName");
			this.mobile = getDataJs().getString("Mobile");
			this.password = getDataJs().getString("Password");
			this.roleId = getDataJs().getLong("RoleId");
		}
	}

}
