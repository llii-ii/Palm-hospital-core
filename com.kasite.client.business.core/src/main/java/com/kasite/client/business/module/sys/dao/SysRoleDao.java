package com.kasite.client.business.module.sys.dao;

import java.util.List;

import com.kasite.core.common.sys.service.pojo.SysRoleEntity;

import tk.mybatis.mapper.common.Mapper;

/**
 * 角色管理
 */
public interface SysRoleDao extends Mapper<SysRoleEntity> {
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
