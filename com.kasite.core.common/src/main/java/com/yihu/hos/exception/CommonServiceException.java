package com.yihu.hos.exception;

import com.yihu.hos.IRetCode;



/**
 * 参数表达式转义 异常
 * 
 * @author Administrator
 * 
 */
public class CommonServiceException extends AbsHosException {
	private Exception e;
	
	public Exception getException() {
		return e;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4953101753824966168L;

	public CommonServiceException(String message) {
		super(message);
	}
	
	public CommonServiceException(IRetCode retCode,String message) {
		super(message);
	}
	public CommonServiceException(Exception e,String message) {
		super(message);
		this.e = e;
	}
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
