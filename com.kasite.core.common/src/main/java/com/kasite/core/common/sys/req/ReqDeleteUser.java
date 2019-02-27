package com.kasite.core.common.sys.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqDeleteUser
 * @author: lcz
 * @date: 2018年8月29日 下午4:34:22
 */
public class ReqDeleteUser extends AbsReq{
	
	private Long Id;
	private Long roleId;
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @Title: ReqDeleteUser
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqDeleteUser(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.Id = getDataJs().getLong("Id");
			this.roleId = getDataJs().getLong("RoleId");
		}
	}

}
