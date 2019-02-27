package com.kasite.core.serviceinterface.job;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.job.req.ReqAddSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqDelSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqPauseSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqQuerySchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqResumeSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqRunSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqUpdateSchedulerJob;
import com.kasite.core.serviceinterface.job.resp.RespSchedulerJob;

public interface ISchedulerJobService {
	
	/**
	 * 查询调度作业
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespSchedulerJob> querySchedulerJob(CommonReq<ReqQuerySchedulerJob> commReq)throws Exception;
	
	/**
	 * 添加调度作业
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> addSchedulerJob(CommonReq<ReqAddSchedulerJob> commReq) throws Exception;
	
	/**
	 * 	根据主键ID,修改调度作业
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> updateSchedulerJob(CommonReq<ReqUpdateSchedulerJob> commReq) throws Exception;
	
	
	/**
	 * 删除调度作业，支持批量删除
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> delSchedulerJobs(CommonReq<ReqDelSchedulerJob> commReq) throws Exception;
	
	/**
	 * 立即执行调度作业
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> runSchedulerJobs(CommonReq<ReqRunSchedulerJob> commReq) throws Exception;
	/**
	 * 暂停运行调度作业
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> pauseSchedulerJobs(CommonReq<ReqPauseSchedulerJob> commReq) throws Exception;
	/**
	 * 恢复运行调度作业
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> resumeSchedulerJobs(CommonReq<ReqResumeSchedulerJob> commReq) throws Exception;
	
}
