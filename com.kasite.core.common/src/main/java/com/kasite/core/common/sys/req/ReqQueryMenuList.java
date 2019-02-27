package com.kasite.core.common.sys.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryMenuList
 * @author: lcz
 * @date: 2018年8月28日 下午7:36:32
 */
public class ReqQueryMenuList extends AbsReq{
	private Long menuId;
    private String name;
    private Long userId;
	/**
     * 类型 0：目录 1：菜单 2：按钮
     */
    private Integer type;

    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @Title: ReqQueryMenuList
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryMenuList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0 && null != getDataJs()) {
			this.menuId = getDataJs().getLong("MenuId");
			this.name = getDataJs().getString("Name");
			this.type = getDataJs().getInteger("Type");
			this.userId = getDataJs().getLong("UserId");
		}
	}
	public ReqQueryMenuList(InterfaceMessage msg, Long menuId, String name, Long userId, Integer type)
			throws AbsHosException {
		super(msg);
		this.menuId = menuId;
		this.name = name;
		this.userId = userId;
		this.type = type;
	}

}
