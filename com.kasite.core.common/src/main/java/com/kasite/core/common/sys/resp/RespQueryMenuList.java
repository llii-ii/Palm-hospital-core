package com.kasite.core.common.sys.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryMenuList
 * @author: lcz
 * @date: 2018年8月28日 下午7:36:53
 */
public class RespQueryMenuList extends AbsResp{
	
    private Long menuId;
    private Long parentId;
    private String parentName;
    private String name;
    private String url;

    /**
     * 授权(多个用逗号分隔，如：a.b.c,a.b.hello)
     */
    private String perms;

    /**
     * 类型 0：目录 1：菜单 2：按钮
     */
    private Integer type;
    private String icon;
    private Integer orderNum;
    
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPerms() {
		return perms;
	}
	public void setPerms(String perms) {
		this.perms = perms;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

    
    
}
