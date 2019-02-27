package com.kasite.client.job.utils;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.serviceinterface.job.dbo.SchedulerJob;


/**
 * 定时任务工具类
 * 
 */
public class SchedulerUtils {
	
    private final static String JOB_NAME = "TASK_";
    
    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }
    
    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
        	e.printStackTrace();
            throw new RRException("获取定时任务CronTrigger出现异常", e);
        }
    }

    /**
     * 创建定时任务
     */
    public static void createSchedulerJob(Scheduler scheduler, SchedulerJob scheduleJob) {
        try {
        	//构建job信息
            JobDetail jobDetail = JobBuilder.newJob(SchedulerJobBean.class).withIdentity(getJobKey(scheduleJob.getJobId())).build();

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
            		.withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob.getJobId())).withSchedule(scheduleBuilder).build();

            //放入参数，运行时的方法可以获取
            
            jobDetail.getJobDataMap().put(KstHosConstant.SCHEDULER_JOB_PARAM_KEY, JSONObject.toJSONString(scheduleJob));
            
            scheduler.scheduleJob(jobDetail, trigger);
            
            //暂停任务
            if(scheduleJob.getStatus() == KstHosConstant.SchedulerStatus.PAUSE.getValue()){
            	pauseJob(scheduler, scheduleJob.getJobId());
            }
        } catch (SchedulerException e) {
        	e.printStackTrace();
            throw new RRException("创建定时任务失败", e);
        }
    }
    
    /**
     * 更新定时任务
     */
    public static void updateSchedulerJob(Scheduler scheduler, SchedulerJob scheduleJob) {
        try {
            TriggerKey triggerKey = getTriggerKey(scheduleJob.getJobId());

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
            		.withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = getCronTrigger(scheduler, scheduleJob.getJobId());
            
            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            
            //参数
            trigger.getJobDataMap().put(KstHosConstant.SCHEDULER_JOB_PARAM_KEY, JSONObject.toJSONString(scheduleJob));
            
            scheduler.rescheduleJob(triggerKey, trigger);
            
            //暂停任务
            if(scheduleJob.getStatus() == KstHosConstant.SchedulerStatus.PAUSE.getValue()){
            	pauseJob(scheduler, scheduleJob.getJobId());
            }
            
        } catch (SchedulerException e) {
        	e.printStackTrace();
            throw new RRException("更新定时任务失败", e);
        }
    }

    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, SchedulerJob scheduleJob) {
        try {
        	//参数
        	JobDataMap dataMap = new JobDataMap();
        	dataMap.put(KstHosConstant.SCHEDULER_JOB_PARAM_KEY, JSONObject.toJSONString(scheduleJob));
            scheduler.triggerJob(getJobKey(scheduleJob.getJobId()), dataMap);
            System.out.println("-----------任务执行成功-----------");
        } catch (SchedulerException e) {
        	e.printStackTrace();
            throw new RRException("立即执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
        	e.printStackTrace();
            throw new RRException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
        	e.printStackTrace();
            throw new RRException("暂停定时任务失败", e);
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteSchedulerJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
        	e.printStackTrace();
            throw new RRException("删除定时任务失败", e);
        }
    }
}
