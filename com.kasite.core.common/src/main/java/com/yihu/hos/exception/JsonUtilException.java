package com.yihu.hos.exception;


/**
 * 参数表达式转义 异常
 * 
 * @author Administrator
 * 
 */
public class JsonUtilException extends AbsHosException {
	private Exception e;
	
	public Exception getException() {
		return e;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4953101753824966168L;

	public JsonUtilException(String message) {
		super(message);
	}
	
	public JsonUtilException(Exception e,String message) {
		super(message);
		this.e = e;
	}
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
