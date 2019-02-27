package com.kasite.client.business.module.sys.dao;

import java.util.List;

import com.kasite.core.common.sys.service.pojo.SysUserRoleEntity;

import tk.mybatis.mapper.common.Mapper;

/**
 * 用户与角色对应关系
 */
public interface SysUserRoleDao extends Mapper<SysUserRoleEntity> {
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);


	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
}
