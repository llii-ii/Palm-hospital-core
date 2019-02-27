/**
 * 
 */
package com.kasite.core.serviceinterface.module.yy.req;

import java.util.List;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.serviceinterface.module.his.resp.HisRecipeClinicList;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * 医院推送处方开药信息入参
 */
public class ReqPushRecipeIn extends AbsReq {

	private List<HisRecipeClinicList> list;
	public ReqPushRecipeIn(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
	}
	
	public ReqPushRecipeIn(InterfaceMessage msg, List<HisRecipeClinicList> list) throws AbsHosException {
		super(msg);
		this.list = list;
	}

	public List<HisRecipeClinicList> getList() {
		return list;
	}

	public void setList(List<HisRecipeClinicList> list) {
		this.list = list;
	}


	
}
