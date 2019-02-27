package com.kasite.core.log;

public interface LogDeal {
	boolean isBusy();
	
	/**
	 * 
	 * @param log 日志的内容，以\r\n为分隔符
	 * @return 1表示成功
	 */
	String handle(String log,String fileName);

}
