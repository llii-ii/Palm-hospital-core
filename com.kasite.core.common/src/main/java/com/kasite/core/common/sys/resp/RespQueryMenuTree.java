package com.kasite.core.common.sys.resp;

import java.util.List;
import java.util.Map;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryMenuList
 * @author: lcz
 * @date: 2018年8月28日 下午7:36:53
 */
public class RespQueryMenuTree extends AbsResp{
	
    private Long menuId;
    private Long parentId;
    private String parentName;
    private String name;
    private String url;

    /**
     * 权限列表
     */
    private List<Map<String, Object>> perms;

    /**
     * 类型 0：目录 1：菜单 2：按钮
     */
    private Integer type;
    private String icon;
    private Integer orderNum;
    private String routerName;
    private String routerPath;
    
    /**
     * 子菜单
     */
    private List<?> list;
    
    
	
	public String getRouterName() {
		return routerName;
	}
	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}
	public String getRouterPath() {
		return routerPath;
	}
	public void setRouterPath(String routerPath) {
		this.routerPath = routerPath;
	}
	public List<Map<String, Object>> getPerms() {
		return perms;
	}
	public void setPerms(List<Map<String, Object>> perms) {
		this.perms = perms;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
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
