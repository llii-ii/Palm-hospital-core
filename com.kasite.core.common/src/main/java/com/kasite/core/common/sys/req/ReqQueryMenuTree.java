package com.kasite.core.common.sys.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryMenuTree
 * @author: lcz
 * @date: 2018年8月30日 上午10:51:34
 */
public class ReqQueryMenuTree extends AbsReq{
    private Long userId;
    private Boolean isAdmin;

    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	/**
	 * @Title: ReqQueryMenuTree
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryMenuTree(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.userId = getDataJs().getLong("UserId");
		}
	}
	public ReqQueryMenuTree(InterfaceMessage msg, Long userId, Boolean isAdmin) throws AbsHosException {
		super(msg);
		this.userId = userId;
		this.isAdmin = isAdmin;
	}
	
}
