package com.kasite.core.common.sys.service.pojo;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 用户与角色对应关系
 */
@Table(name="SYS_USER_ROLE")
public class SysUserRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
	@KeySql(useGeneratedKeys=true)
    private Long id;

	/**
	 * 用户ID
	 */
    @Column(name="USER_ID")
	private Long userId;

	/**
	 * 角色ID
	 */
    @Column(name="ROLE_ID")
	private Long roleId;

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
	 * 设置：用户ID
	 * @param userId 用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 获取：用户ID
	 * @return Long
	 */
	public Long getUserId() {
		return userId;
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
	
}
