package com.kasite.core.serviceinterface.module.his.req;

import java.util.Map;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicSchedule;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryNumbers;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqHisLock extends AbsReq{

	private String orderId;
	private HisQueryClinicSchedule sch;
	private HisQueryNumbers num;
	private Map<String, String> paramMap;
	
	
	
	public ReqHisLock(InterfaceMessage msg,String orderId,HisQueryClinicSchedule sch,HisQueryNumbers num,
			Map<String, String> paramMap) throws AbsHosException {
		super(msg);
		this.orderId = orderId;
		this.sch = sch;
		this.num = num;
		this.paramMap = paramMap;
	}



	public String getOrderId() {
		return orderId;
	}



	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}



	public HisQueryClinicSchedule getSch() {
		return sch;
	}



	public void setSch(HisQueryClinicSchedule sch) {
		this.sch = sch;
	}



	public HisQueryNumbers getNum() {
		return num;
	}



	public void setNum(HisQueryNumbers num) {
		this.num = num;
	}



	public Map<String, String> getParamMap() {
		return paramMap;
	}



	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

}
