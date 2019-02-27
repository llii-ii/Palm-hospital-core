package com.kasite.core.serviceinterface.module.his.handler;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.req.ReqHisMemberRefundable;

public interface IQueryMemberRefundableService extends ICallHis{
	
	CommonResp<RespMap> queryMemberRefundable(CommonReq<ReqHisMemberRefundable> commReq) throws Exception;
	
	
}
