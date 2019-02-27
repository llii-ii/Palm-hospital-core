package com.kasite.core.common.exception;

/**
 * @author MECHREV
 */
public class MsgListenerException extends Exception {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 6374075962117129486L;
	
	private String errorCode;
	private Exception linkedException;

	public MsgListenerException(String reason, String errorCode) {
		super(reason);
		this.errorCode = errorCode;
		this.linkedException = null;
	}

	public MsgListenerException(String reason) {
		this(reason, null);
		this.linkedException = null;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public Exception getLinkedException() {
		return this.linkedException;
	}

	public synchronized void setLinkedException(Exception ex) {
		this.linkedException = ex;
	}
}
