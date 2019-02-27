package com.yihu.hos.service;

import com.yihu.hos.IRetCode;

public interface CommonServiceRetCode extends IRetCode {
	/**
	 * 调用接口返回代码定义
	 * 定义范围： -14001 开始
	 * @author daiys
	 * @date 2015-1-4
	 */
	enum Common implements IRetCode  {
		
		/**
		 * 业务逻辑公共返回 Code 所有定义为 10000
		 */
		RET_10000(10000,"成功"),
		
		/**
		 * -20000 参数错误
		 */
		ERROR_PARAM(-20000,"参数错误"),
		
		/**
		 * "获取当前操作时间异常。"
		 */
		GetDate(-14001, "获取当前操作时间异常。"),
		/**
		 * "初始化配置文件出错."
		 */
		Init(-14002,"初始化配置文件出错."),
		/**
		 * 解析日期类型数据出错"
		 */
		ParseDate(-14004, "解析日期类型出现异常"),
		/**
		 * 调用医院端接口异常返回
		 */
		CallHosClientError(-14005,"调用医院端接口异常返回"),
		/**
		 * 调用Hop接口网关异常
		 */
		CallHOPClientError(-14006,"服务端接口异常"),
		/**
		 * -14007 数据库脚本执行查询异常
		 */
		EXECSqlError(-14007,"数据库脚本执行查询异常"),
		/**
		 * -14008请求的参数为空
		 */
		NULLERROR(-14008,"请求的参数为空"),
		/**
		 * -63001 XML解析异常
		 */
		ERROR_XMLERROR(-63001,"XML解析异常"),
		/**
		 * -14011健康卡号不存在
		 */
		ERROR_HEALTHNONOTEXIST(-14011,"健康卡号不存在"),
		/**
		 * -63001 XML解析异常
		 */
		ERROR_INIT_SYSCONFIG(-14009,"初始化系统配置信息异常。"),
		/**
		 * -63001 XML解析异常
		 */
		SysCfgNot(-14010,"系统解析模块参数未配置。"),
		/**
		 * -14012 hisId数据不存在
		 */
		ERROR_HISIDCANNOTEXIST(-14012,"hisId不存在"),
		/**
		 * -14013住院号不存在
		 */
		ERROR_HOSPITALIZATIONNONOEXIST(-14013,"住院号不存在"),
		/**
		 * -14014已经不存
		 */
		ERROR_EXIST(-14014,"已经不存"),
		/**
		 * -14015已经不存
		 */
		ERROR_ROUTE_CHANGE(-14015,"更新Route配置文件出错。"),
		/**
		 * -14016 数据库中不存在表 Sys_Route
		 */
		ERROR_ROUTE_NOT(-14016,"数据库中不存在表 Sys_Route。"),
		/**
		 * -14017 解析Sys_Route 节点内容，异常。
		 */
		ERROR_ROUTE_PARSE(-14017,"解析Sys_Route 节点内容，异常。"),
		;
		/** 代码 */
		private Integer code;
		/** 消息 */
		private String message;
		
		public String getMessage() {
			return message;
		}
		Common(Integer code,String message) {
			this.code = code;
			this.message = message;
		}
		public Integer getCode() {
			return code;
		}
		@Override
		public String getExceptMsg() {
			// TODO Auto-generated method stub
			return message;
		}
		@Override
		public String getMessage(String... params) {
			// TODO Auto-generated method stub
			return message;
		}
	}
}