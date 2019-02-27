package com.kasite.core.serviceinterface.job;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.job.req.ReqQuerySchedulerJobLog;
import com.kasite.core.serviceinterface.job.req.ReqSaveSchedulerJobLog;
import com.kasite.core.serviceinterface.job.resp.RespSchedulerJobLog;

public interface ISchedulerJobLogService {
	/**
	 * 	查询调度作业日志
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespSchedulerJobLog> querySchedulerJobLog(CommonReq<ReqQuerySchedulerJobLog> commReq)throws Exception;
	
	/**
	 * 保存作业
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> saveSchedulerJobLog(CommonReq<ReqSaveSchedulerJobLog> commReq)throws Exception;
}
