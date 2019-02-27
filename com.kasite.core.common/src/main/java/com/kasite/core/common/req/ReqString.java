package com.kasite.core.common.req;

import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqString extends AbsReq{

	private String param;
	
	public ReqString(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.param = getDataJs().getString("Param");
		}else {
			this.param = XMLUtil.getString(getData(), "Param", false);
		}
	}
	
	public ReqString(InterfaceMessage msg,String param) throws AbsHosException {
		super(msg);
		this.param = param;
	}
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
