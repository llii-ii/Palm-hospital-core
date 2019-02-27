package com.kasite.client.business.module.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.core.common.sys.service.pojo.SysUserEntity;

import tk.mybatis.mapper.common.Mapper;

/**
 */
public interface SysUserDao extends Mapper<SysUserEntity>{
	
	//查询用户是否管理员角色
	@Select("SELECT COUNT(1) FROM SYS_USER U INNER JOIN SYS_USER_ROLE UR ON U.ID=UR.USER_ID INNER JOIN SYS_ROLE R ON UR.ROLE_ID= R.ROLE_ID WHERE U.ID=#{userId} AND UR.ROLE_ID=1")
	Integer isAdmin(long userId);
	
	//查询用户所有菜单ID集合
	@Select("SELECT DISTINCT RM.MENU_ID FROM SYS_USER_ROLE UR LEFT JOIN SYS_ROLE_MENU RM ON UR.ROLE_ID = RM.ROLE_ID WHERE UR.USER_ID = #{userId}")
	List<Long> queryUserAllMenuId(long userId);
	
	//查询用户所有权限ID集合
	@Select("SELECT M.PERMS FROM SYS_USER_ROLE UR LEFT JOIN SYS_ROLE_MENU RM ON UR.ROLE_ID = RM.ROLE_ID LEFT JOIN SYS_MENU M ON RM.MENU_ID = M.MENU_ID WHERE UR.USER_ID = #{userId} AND M.PERMS IS NOT NULL AND M.PERMS<>''")
	List<String> queryUserAllPerms(long userId);
	
	
	@Select({"<script>",
		"SELECT U.*,R.ROLE_ID,R.ROLE_NAME FROM SYS_USER U LEFT JOIN SYS_USER_ROLE UR ON U.ID=UR.USER_ID LEFT JOIN SYS_ROLE R ON UR.ROLE_ID = R.ROLE_ID ",
		"<where>",
			"<if test=\"user.username!=null and user.username!=''\">",
			"	AND  U.USERNAME = #{user.username}" ,
			"</if>" ,
			"<if test=\"user.id!=null and user.id>0\">",
			"	AND  U.ID = #{user.id}" ,
			"</if>" ,
			"<if test=\"user.mobile!=null and user.mobile!=''\">",
			"	AND  U.MOBILE = #{user.mobile}" ,
			"</if>" ,
			"<if test=\"isManager == 1 \">",
			"	AND  U.PASSWORD is not null" ,
			"</if>" ,
		"</where>",
		"</script>"})
	List<SysUserEntity> queryUserList(@Param("user")SysUserEntity user, @Param("isManager")int isManager);
	
	
}
