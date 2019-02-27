package com.kasite.core.common.service;

import com.yihu.wsgw.api.InterfaceMessage;

public interface IGuardApi {

	String query(InterfaceMessage msg);
	
	String update(InterfaceMessage msg);
	
	String ddl(InterfaceMessage msg);
	
	String add(InterfaceMessage msg);
	
}