package com.kasite.core.common.sys.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryRoleList
 * @author: lcz
 * @date: 2018年8月28日 下午4:14:52
 */
public class ReqQueryRoleList extends AbsReq{

	private Long roleId;
	private String roleName;
	private String roleNameLike;
	
	
	public String getRoleNameLike() {
		return roleNameLike;
	}
	public void setRoleNameLike(String roleNameLike) {
		this.roleNameLike = roleNameLike;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}



	/**
	 * @Title: ReqQueryRoleList
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryRoleList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.roleId = getDataJs().getLong("RoleId");
			this.roleName = getDataJs().getString("RoleName");
			this.roleNameLike = getDataJs().getString("RoleNameLike");
		}
	}

	
}
