package com.kasite.core.serviceinterface.module.basic;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.basic.req.ReqCheckPorvingCode;
import com.kasite.core.serviceinterface.module.basic.req.ReqSaveProvingCode;
import com.kasite.core.serviceinterface.module.basic.resp.RespGetprovingCode;

public interface IPorvingCodeService {
	
	CommonResp<RespGetprovingCode> saveProvingCode(CommonReq<ReqSaveProvingCode> reqCommon)throws Exception;
	CommonResp<RespMap> checkPorvingCode(CommonReq<ReqCheckPorvingCode> reqComm) throws Exception;
}
