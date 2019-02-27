package com.kasite.core.log.exec.handler;

import com.alibaba.fastjson.JSONObject;

public interface LogDealHandler extends Runnable{
	boolean addLog(JSONObject jo);
	boolean accept(JSONObject jo);
	/**
	 * 单次任务执行，可以单条，也可以多条
	 * @throws Exception
	 */
	void exec() throws Exception;
	String stats();
	boolean isBusy();
}
