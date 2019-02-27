package com.kasite.core.serviceinterface.module.his.handler;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryClinicDoctor;

/**
 * 医生信息同步
 * @className: ISyncDoctorService
 * @author: lcz
 * @date: 2018年9月26日 下午6:05:16
 */
public interface ISyncDoctorService extends ICallHis{
	
	CommonResp<RespQueryClinicDoctor> queryAndSyncClinicDoctor(CommonReq<ReqQueryClinicDoctor> req) throws Exception;
	
}
