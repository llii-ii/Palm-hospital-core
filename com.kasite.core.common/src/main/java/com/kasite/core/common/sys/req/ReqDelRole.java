package com.kasite.core.common.sys.req;

import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqDelRole
 * @author: lcz
 * @date: 2018年8月30日 上午10:06:50
 */
public class ReqDelRole extends AbsReq{

	private Long roleId;
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @Title: ReqDelRole
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqDelRole(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.roleId = getDataJs().getLong("RoleId");
			if(this.roleId==null || this.roleId<=0) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"参数职务ID不能为空。");
			}
		}
	}

}
