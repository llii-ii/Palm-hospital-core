package com.kasite.client.job.utils;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.serviceinterface.job.ISchedulerJobLogService;
import com.kasite.core.serviceinterface.job.dbo.SchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqSaveSchedulerJobLog;
import com.yihu.wsgw.api.InterfaceMessage;


/**
 * 定时任务
 * 
 */
public class SchedulerJobBean extends QuartzJobBean {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ExecutorService service = Executors.newSingleThreadExecutor(); 
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		String job = context.getMergedJobDataMap().getString(KstHosConstant.SCHEDULER_JOB_PARAM_KEY);
		
		SchedulerJob scheduleJob = JSONObject.parseObject(job, SchedulerJob.class);
		
		ReqSaveSchedulerJobLog log = null;
		
		//获取spring bean
		ISchedulerJobLogService scheduleJobLogService = (ISchedulerJobLogService) SpringContextUtil.getBean(ISchedulerJobLogService.class);
		 //任务开始时间
        long startTime = System.currentTimeMillis();
        
        try {
        	InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(SchedulerJobBean.class,scheduleJob.getMethodName(), 
    				null, String.valueOf(UUID.randomUUID()),null, null);
    		
            //数据库保存执行记录日志
    		log = new ReqSaveSchedulerJobLog(msg, 
    				scheduleJob.getJobId(), 
    				scheduleJob.getBeanName(), 
    				scheduleJob.getMethodName(), 
    				scheduleJob.getParams(), null, null, null);
    				
            //执行任务
        	logger.info("任务准备执行，任务ID：" + scheduleJob.getJobId());
            SchedulerRunnable task = new SchedulerRunnable(scheduleJob.getBeanName(), 
            		scheduleJob.getMethodName(), scheduleJob.getParams());
            Future<?> future = service.submit(task);
            
			future.get();
			
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int)times);
			//任务状态    0：成功    1：失败
			log.setStatus(0);
			
			logger.info("任务执行完毕，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);
			
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int)times);
			
			//任务状态    0：成功    1：失败
			log.setStatus(1);
			log.setError(StringUtils.substring(e.getMessage(), 0, 2000));
		}finally {
			try {
				scheduleJobLogService.saveSchedulerJobLog(new CommonReq<ReqSaveSchedulerJobLog>(log));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
}
