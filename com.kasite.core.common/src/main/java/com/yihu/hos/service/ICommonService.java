package com.yihu.hos.service;

import com.yihu.hos.DModule;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 公共接口类
 * @author Administrator
 *
 */
public interface ICommonService {
	
	IEvent getEvent(String methodName);

	void setModule(DModule module);
	
	DModule getModule();
	
	String testConn();
	
	void setDebug(boolean isDebug);
	
	public String testConn(InterfaceMessage msg);
}
