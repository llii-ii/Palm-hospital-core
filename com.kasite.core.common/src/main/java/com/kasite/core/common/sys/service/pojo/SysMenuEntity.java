package com.kasite.core.common.sys.service.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 菜单管理
 * 
 */
@Table(name="SYS_MENU") 
public class SysMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @Id
	@KeySql(useGeneratedKeys=true)
    @Column(name="MENU_ID")
    private Long menuId;

    /**
     * 父菜单ID，一级菜单为0
     */
    @Column(name="PARENT_ID")
    private Long parentId;

    /**
     * 父菜单名称
     */
    @Transient
    private String parentName;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：a.b.c,a.b.hello)
     */
    private String perms;

    /**
     * 类型 0：目录 1：菜单 2：按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    @Column(name="ORDER_NUM")
    private Integer orderNum;
    
    private String routerName;
    private String routerPath;
    /**
     * ztree属性
     */
    @Transient
    private Boolean open;

    @Transient
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

	/**
     * 设置：菜单名称
     * 
     * @param name
     *            菜单名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：菜单名称
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：菜单URL
     * 
     * @param url
     *            菜单URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取：菜单URL
     * 
     * @return String
     */
    public String getUrl() {
        return url;
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

    /**
     * 设置：菜单图标
     * 
     * @param icon
     *            菜单图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取：菜单图标
     * 
     * @return String
     */
    public String getIcon() {
        return icon;
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

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
}
