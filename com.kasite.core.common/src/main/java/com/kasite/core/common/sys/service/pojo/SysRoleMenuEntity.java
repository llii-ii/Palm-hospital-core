package com.kasite.core.common.sys.service.pojo;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;


/**
 * 角色与菜单对应关系
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:28:13
 */
@Table(name="SYS_ROLE_MENU") 
public class SysRoleMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys=true)
	private Long id;

	/**
	 * 角色ID
	 */
	@Column(name="ROLE_ID")
	private Long roleId;

	/**
	 * 菜单ID
	 */
	@Column(name="MENU_ID")
	private Long menuId;

	/**
	 * 设置：
	 * @param id 
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取：
	 * @return Long
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * 设置：角色ID
	 * @param roleId 角色ID
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取：角色ID
	 * @return Long
	 */
	public Long getRoleId() {
		return roleId;
	}
	
	/**
	 * 设置：菜单ID
	 * @param menuId 菜单ID
	 */
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	/**
	 * 获取：菜单ID
	 * @return Long
	 */
	public Long getMenuId() {
		return menuId;
	}
	
}
