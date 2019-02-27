package com.kasite.server.verification.common.dao;

import com.coreframework.db.TableEnum;

/**
 * 表名
 */
public enum MyTableEnum implements TableEnum{
	tb_token,
	tb_user,
	
	schedule_job,
	schedule_job_log,
	
	sys_user,
	sys_user_role,
	sys_user_token,
	sys_role,
	sys_role_menu,
	sys_role_dept,
	sys_menu,
	sys_dept,
	sys_log,
	sys_config,
	sys_dict,
	sys_message,
	sys_push_config,
	sys_push_log,
	
	
	app_access_token,
	app_access_token_d,

	app_online, app_route,
}
