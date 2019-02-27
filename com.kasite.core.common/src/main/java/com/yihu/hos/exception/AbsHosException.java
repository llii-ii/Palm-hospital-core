package com.yihu.hos.exception;

import com.yihu.hos.IRetCode;



/**
 * 抽象的公共异常 异常
 * 
 * @author Administrator
 * 
 */
public abstract class AbsHosException extends Exception {
	private Exception e;
	private IRetCode retcode;
	
	public IRetCode getCode() {
		if(retcode == null){
			retcode = NotRetCode.Common.NOTSETRETCODE;
		}
		return retcode;
	}

	public IRetCode getRetcode() {
		return retcode;
	}

	public void setRetcode(IRetCode retcode) {
		this.retcode = retcode;
	}

	public Exception getException() {
		return e;
	}
	private static final long serialVersionUID = -4953101753824966168L;
	public AbsHosException(String message) {
		super(message);
		e = this;
	}
	public AbsHosException(IRetCode retCode) {
		super(retCode.getMessage());
		e = this;
	}
	public AbsHosException(IRetCode retCode,String message) {
		super(message);
		this.retcode = retCode;
		e = this;
	}
	public AbsHosException(Exception e,String message) {
		super(message);
		this.e = e;
	}
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
