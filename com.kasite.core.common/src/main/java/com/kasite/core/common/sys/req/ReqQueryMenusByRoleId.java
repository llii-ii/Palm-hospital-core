package com.kasite.core.common.sys.req;

import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryMenusByRoleId
 * @author: lcz
 * @date: 2018年8月30日 下午5:51:40
 */
public class ReqQueryMenusByRoleId extends AbsReq{

	private Long roleId;
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


	/**
	 * @Title: ReqQueryMenusByRoleId
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryMenusByRoleId(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.roleId = getDataJs().getLong("RoleId");
			if(this.roleId==null || this.roleId<=0) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"职务ID不能为空。");
			}
		}
	}
	
	
}
