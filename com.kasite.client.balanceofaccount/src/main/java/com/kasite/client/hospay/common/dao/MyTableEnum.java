package com.kasite.client.hospay.common.dao;

/**
 * 表名
 */
public enum MyTableEnum{
	tb_token,
	tb_user,
	
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


	//***********对账相关数据表**********
	P_HIS_BILL,
	P_QLCBALANCE,
	P_THREEPARTYBALANCE,
	P_EVERYDAYBALANCE,
	P_EXCEPTIONBILL,
	P_NOTICE_LOG,
	P_CLASSIFYSUMMARYBALANCE,
	;
	//*******************************

	public static final String TB_USER= "tb_user";
	public static final String TB_TOKEN="tb_token";
	
	public static final String SYS_USER="sys_user";
	public static final String SYS_USER_ROLE="sys_user_role";
	public static final String SYS_USER_TOKEN="sys_user_token";
	public static final String SYS_ROLE="sys_role";
	public static final String SYS_ROLE_MENU="sys_role_menu";
	public static final String SYS_ROLE_DEPT="sys_role_dept";
	public static final String SYS_MENU="sys_menu";
	public static final String SYS_DEPT="sys_dept";
	public static final String SYS_LOG="sys_log";
	public static final String SYS_CONFIG="sys_config";
	public static final String SYS_DICT="sys_dict";
	public static final String SYS_MESSAGE="sys_message";
	public static final String SYS_PUSH_CONFIG="sys_push_config";
	public static final String SYS_PUSH_LOG="sys_push_log";
	
}
