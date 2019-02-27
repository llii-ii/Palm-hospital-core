package com.yihu.hos.exception;

import com.yihu.hos.IRetCode;

/**
 * 
 * @author daiys
 * @date 2015-1-4
 */
public interface NotRetCode extends IRetCode {
	/**
	 * 调用接口返回代码定义
	 * @date 2015-1-4
	 */
	enum Common implements IRetCode  {
		NOTSETRETCODE(-990001,"未定义的类型异常"),;
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
			// TODO 获取其它异常信息
			return message ;
		}
		@Override
		public String getMessage(String... params) {
			// TODO 获取其它异常信息
			return null;
		}
	}
		 
}
