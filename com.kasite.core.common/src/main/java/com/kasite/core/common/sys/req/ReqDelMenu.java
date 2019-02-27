package com.kasite.core.common.sys.req;

import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqDelMenu
 * @author: lcz
 * @date: 2018年8月30日 下午9:22:00
 */
public class ReqDelMenu extends AbsReq{
	private Long menuId;
	
	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	/**
	 * @Title: ReqDelMenu
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqDelMenu(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.menuId = getDataJs().getLong("MenuId");
			if(this.menuId==null || this.menuId<=0) {
				throw new RRException(RetCode.Common.ERROR_PARAM,"参数菜单ID不能为空。");
			}
		}
	}

}
