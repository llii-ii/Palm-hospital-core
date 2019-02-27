package com.kasite.core.common.exception;

import com.yihu.hos.IRetCode;

/**
 * 自定义异常
 * 
 * @author daiys

 */
public class RRException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    private IRetCode retCode;
    
    public RRException(IRetCode retCode) {
		super(retCode.getMessage());
		this.code = retCode.getCode();
		this.msg = retCode.getMessage();
		this.retCode = retCode;
	}

    public RRException(IRetCode retCode,String msg) {
		super(msg);
		this.code = retCode.getCode();
		this.msg = msg;
	}
	public RRException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	public RRException(Throwable e,String msg) {
		super(msg, e);
		this.msg = msg;
	}
	public RRException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public RRException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public RRException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public IRetCode getRetCode() {
		return retCode;
	}

	public void setRetCode(IRetCode retCode) {
		this.retCode = retCode;
	}
	
	
}
