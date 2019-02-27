package com.kasite.core.common.exception;

/**
 * @author MECHREV
 */
public class WeiXinException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int errcode;

	private String errmsg; 
	
	public WeiXinException(String message) {
		super(message);
	}

	
	public WeiXinException(int code,String errmsg) {
		super("errcode:"+code+","+errmsg);
		this.errcode = code;
		this.errmsg = errmsg;
	}
	
	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	
	
}
