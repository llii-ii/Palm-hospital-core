package com.kasite.core.serviceinterface.module.yy;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryClinicDoctor;

/**
 * 
 * @className: ISyncDoctorService
 * @author: lcz
 * @date: 2018年9月27日 下午8:34:49
 */
public interface ISyncClinicDoctorService {
	/**
	 * 同步门诊医生信息并查询出所有本地的医生
	 * @Description: 
	 * @param reqComm
	 * @param callHisService
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryClinicDoctor> queryAndSyncClinicDoctor(CommonReq<ReqQueryClinicDoctor> reqComm) throws Exception;
}
