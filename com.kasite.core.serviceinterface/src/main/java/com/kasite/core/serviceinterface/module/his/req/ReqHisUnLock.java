package com.kasite.core.serviceinterface.module.his.req;

import java.util.Map;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqHisUnLock extends AbsReq{

	private String orderId;
	private Map<String, String> paramMap;
	
	public ReqHisUnLock(InterfaceMessage msg,String orderId,
			Map<String, String> paramMap) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.paramMap = paramMap;
	}

	public String getOrderId() {
		return orderId;
	}



	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public Map<String, String> getParamMap() {
		return paramMap;
	}



	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

}
