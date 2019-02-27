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
public class ReqGetRole extends AbsReq{

	private Long roleId;

	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public ReqGetRole(InterfaceMessage msg, Long roleId) throws AbsHosException {
		super(msg);
		this.roleId = roleId;
	}
	/**
	 * @Title: ReqQueryRoleList
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqGetRole(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.roleId = getDataJs().getLong("RoleId");
		}
	}

	
}
