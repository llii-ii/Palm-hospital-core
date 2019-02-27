package com.kasite.core.common.sys.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqUpdateUser
 * @author: lcz
 * @date: 2018年8月29日 下午5:08:09
 */
public class ReqUpdateUser extends AbsReq{

	private Long id;
	private String username;
	private String realName;
	private String mobile;
	private String password;
	private Long roleId;
	private Integer status;
	
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @Title: ReqUpdateUser
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqUpdateUser(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.id = getDataJs().getLong("Id");
			this.username = getDataJs().getString("Username");
			this.realName = getDataJs().getString("RealName");
			this.mobile = getDataJs().getString("Mobile");
			this.password = getDataJs().getString("Password");
			this.roleId = getDataJs().getLong("RoleId");
			this.status = getDataJs().getInteger("Status");
		}
	}

}
