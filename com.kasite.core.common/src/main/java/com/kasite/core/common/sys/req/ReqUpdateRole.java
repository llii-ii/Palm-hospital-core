package com.kasite.core.common.sys.req;

import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqUpdateRole
 * @author: lcz
 * @date: 2018年8月29日 下午10:27:20
 */
public class ReqUpdateRole extends AbsReq{
	
	private Long roleId;
	private String roleName;
	private String remark;
	private List<String> menuList;
	
	
	public List<String> getMenuList() {
		return menuList;
	}


	public void setMenuList(List<String> menuList) {
		this.menuList = menuList;
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


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	/**
	 * @Title: ReqUpdateRole
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqUpdateRole(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.roleId = getDataJs().getLong("RoleId");
			if(this.roleId==null || this.roleId<=0) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"参数RoleId不能为空");
			}
			this.roleName = getDataJs().getString("RoleName");
			this.remark = getDataJs().getString("Remark");
			this.menuList = getDataJs().getObject("MenuList", TypeReference.LIST_STRING);
		}
	}

}
