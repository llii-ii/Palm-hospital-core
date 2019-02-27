package com.kasite.core.common.sys.req;

import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqAddRole
 * @author: lcz
 * @date: 2018年8月29日 下午10:26:52
 */
public class ReqAddRole extends AbsReq{
	
	private String roleName;
	private String remark;
	private List<String> menuList;
	
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


	public List<String> getMenuList() {
		return menuList;
	}


	public void setMenuList(List<String> menuList) {
		this.menuList = menuList;
	}


	/**
	 * @Title: ReqAddRole
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqAddRole(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.roleName = getDataJs().getString("RoleName");
			if(StringUtil.isBlank(this.roleName)) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"参数RoleName不能为空");
			}
			this.remark = getDataJs().getString("Remark");
			this.menuList = getDataJs().getObject("MenuList", TypeReference.LIST_STRING);
		}
	}
 
}
