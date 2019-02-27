package com.yihu.hos;


public interface IRetCode {
	String getMessage();
	String getExceptMsg() ;
	String getMessage(String... params);
	Integer getCode();
}
