package com.yihu.hos.exception;


/**
 * 参数表达式转义 异常
 * 
 * @author Administrator
 * 
 */
public class XmlUtilException extends AbsHosException {
	private Exception e;
	
	public Exception getException() {
		return e;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4953101753824966168L;

	public XmlUtilException(String message) {
		super(message);
	}
	
	public XmlUtilException(Exception e,String message) {
		super(message);
		this.e = e;
	}
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
