/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.kasite.client.business.module.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.kasite.core.common.mappers.CommonMapper;
import com.kasite.core.common.sys.service.pojo.SysMenuEntity;

/**
 * 菜单管理
 */
public interface SysMenuDao extends CommonMapper<SysMenuEntity> {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	@Select("SELECT * FROM SYS_MENU WHERE PARENT_ID = #{parentId} ORDER BY ORDER_NUM ASC")
	List<SysMenuEntity> queryListParentId(Long parentId);
	/**
	 * 获取不包含按钮的菜单列表
	 */
	@Select("SELECT * FROM SYS_MENU WHERE TYPE != 2 ORDER BY ORDER_NUM ASC")
	List<SysMenuEntity> queryNotButtonList();
	
	@Select("SELECT PERMS FROM SYS_MENU WHERE PERMS IS NOT NULL AND PERMS<>''")
	List<String> queryAllPerms();
	
	@Select("SELECT MENU_ID AS MenuId,Name,Perms FROM SYS_MENU WHERE TYPE=2 AND PERMS IS NOT NULL AND PERMS<>'' AND PARENT_ID=#{menuId}")
	List<Map<String, Object>> queryMenuPerms(Long menuId);
}
