package com.yihu.hos.constant;


/**
 * 常量类 接口定义
 * @author Administrator
 *
 */
public interface IConstant {
	/**
	 * 40000 access_token无效或过期
	 */
	public static final int OAUTH_TOKEN_INVALID = 40000;
	/**
	 * 40001 AppSecret错误或者AppSecret不属于这个AppId，请确认AppSecret的正确性
	 */
	public static final int OAUTH_APPSECRET_ERROR = 40001;
	/**
	 * 40002 PublicKey 无法进行解密，解密失败publickey需要确认正确性
	 */
	public static final int OAUTH_PUBLICKEY_ERROR = 40002;
	/**
	 * 40164 调用接口的IP地址不在白名单中。 
	 */
	public static final int OAUTH_IP_ERROR = 40164;
	
	
	public static final String SysId = "SysId";
	public static final String FAILCODE = "-10000";
	public static final String SUCCESSSTR = "成功";
	public static final String FAILSTR = "失败";
	
	/******************各个模块简称定义*********************/
	public static final String DEFAULTKEY = "DEFAULT";
	/**入参类型  1 为xml*/
	public static final Integer PARAMTYPE_XML = 1;
	/**入参类型 0 为json*/
	public static final Integer PARAMTYPE_JSON = 0;
	
	/*********************返回XML节点定义********************/
	public static final Integer SUCCESSCODE = 10000;
	public static final String SUCCESSMSG = "成功";
	
	public static final String RESP = "Resp";
	public static final String RESPCODE = "RespCode";
	public static final String RESPMESSAGE = "RespMessage";
	public static final String TRANSACTIONCODE = "TransactionCode";
	public static final String DATA = "Data";
	public static final String COLUMNS = "Columns";
	public static final String PINDEX = "PIndex";
	public static final String PSIZE = "PSize";
	public static final String PCOUNT = "PCount";
	public static final String PAGE = "Page";
	public static final String ORDERBY = "OrderBy";
	public static final String REQ = "Req";
	
	/*****记录到数据库中的日志对象中  用户 id*****/
	public static final String LOGNAME = "LogName";
	public static final String SEQ = "LOGGUID";
	public static final String ORDERID = "Orderid";
	public static final String USERNAME = "UserName";
	public static final String SYS = "sys";
	public static final String COMM_OPENID = "openid";
	/*****日志记录名称*******/
	public static final String LOG = "LOG";
	public static final String JOB = "JOB";
	
	
	/**
	 * 大括号左边 " { "
	 */
	public static final String BRACE_LEFT = "{";
	
	/**
	 * 大括号右边 " } "
	 */
	public static final String BRACE_RIGHT = "}";

}
