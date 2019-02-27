package com.kasite.core.serviceinterface.module.his.req;

import java.util.Map;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqHisMemberRefundable extends AbsReq{

	private Map<String, String> paramMap;
	
	public ReqHisMemberRefundable(InterfaceMessage msg, Map<String, String> paramMap) throws AbsHosException {
		super(msg);
		this.paramMap = paramMap;
	}

	/**
	 * @return the paramMap
	 */
	public Map<String, String> getParamMap() {
		return paramMap;
	}

	/**
	 * @param paramMap the paramMap to set
	 */
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	
	
	
}
