package com.yihu.hos.exception;

import com.yihu.hos.IRetCode;


/**
 * 数据库执行 异常
 * 
 * @author Administrator
 * 
 */
public class SQLException extends AbsHosException {
	private Exception e;
	
	public Exception getException() {
		return e;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4953101753824966168L;

	public SQLException(String message) {
		super(message);
	}
	
	public SQLException(Exception e,String message) {
		super(message);
		this.e = e;
	}
	public SQLException(IRetCode retCode,String message) {
		super(retCode,message);
	}
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
