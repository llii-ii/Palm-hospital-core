package com.kasite.core.common.sys.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryRoleList
 * @author: lcz
 * @date: 2018年8月28日 下午4:15:56
 */
public class RespQueryRoleList extends AbsResp{
	
	private Long roleId;
	private String roleName;
	private String remark;
	
	
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
