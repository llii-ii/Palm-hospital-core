package com.yihu.hos.web;


public class ResponseMessage implements java.io.Serializable {

	public ResponseMessage(boolean success, Object result, String exception) {
		super();
		this.success = success;
		this.result = result;
		this.exception = exception;
	}
	public ResponseMessage()
	{
		this.success=false;
		this.exception="";
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public void setException(Exception e) {
		this.exception = e.getMessage()==null?"null":e.getMessage();
	}
	private boolean success;
	private Object  result;
	private String exception;
	@Override
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("isSuccess=").append(this.isSuccess())
		.append("|exception=").append(this.getException()).append("|Result=").append(this.getResult());
		
		return sb.toString();
	}
}
