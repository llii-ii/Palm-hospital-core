package com.yihu.hos.exception;

import com.yihu.hos.IRetCode;



/**
 * 参数表达式转义 异常
 * 
 * @author Administrator
 * 
 */
public class SysParamConfigException extends AbsHosException {
	private Exception e;
	
	public Exception getException() {
		return e;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4953101753824966168L;

	public SysParamConfigException(String message) {
		super(message);
	}
	
	public SysParamConfigException(IRetCode retCode,String message) {
		super(message);
	}
	public SysParamConfigException(Exception e,String message) {
		super(message);
		this.e = e;
	}
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
