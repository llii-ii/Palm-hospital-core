package com.kasite.client.business.module.sys.dao;

import java.util.List;

import com.kasite.core.common.sys.service.pojo.SysRoleMenuEntity;

import tk.mybatis.mapper.common.BaseMapper;

/**
 * 角色与菜单对应关系
 */
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Long> queryMenuIdList(Long roleId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
}
