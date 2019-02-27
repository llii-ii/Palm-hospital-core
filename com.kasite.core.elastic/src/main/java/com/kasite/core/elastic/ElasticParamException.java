package com.kasite.core.elastic;


/**
 * 参数异常
 * 
 * @author Administrator
 * 
 */
public class ElasticParamException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4953101753824966168L;
	public ElasticParamException(String message) {
		super(message);
	}
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
