/**
 * 
 */
package com.kasite.core.serviceinterface.module.yy.req;

import java.util.List;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.serviceinterface.module.his.resp.HisStopClinicList;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * 医院推送停诊信息入参
 */
public class ReqStop extends AbsReq {

	private List<HisStopClinicList> list;
	public ReqStop(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
	}
	
	public ReqStop(InterfaceMessage msg, List<HisStopClinicList> list) throws AbsHosException {
		super(msg);
		this.list = list;
	}


	public List<HisStopClinicList> getList() {
		return list;
	}
	public void setList(List<HisStopClinicList> list) {
		this.list = list;
	}
	
	
}
