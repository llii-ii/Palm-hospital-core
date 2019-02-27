package com.kasite.client.job.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coreframework.util.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kasite.client.job.dao.SchedulerJobMapper;
import com.kasite.client.job.utils.SchedulerUtils;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.serviceinterface.job.ISchedulerJobService;
import com.kasite.core.serviceinterface.job.dbo.SchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqAddSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqDelSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqPauseSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqQuerySchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqResumeSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqRunSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqUpdateSchedulerJob;
import com.kasite.core.serviceinterface.job.resp.RespSchedulerJob;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class SchedulerJobServiceImpl implements ISchedulerJobService {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	SchedulerJobMapper scheduleJobMapper;
	
	/**
	 * 	项目启动时，初始化定时任务
	 */
	@PostConstruct
	public void init() {
		List<SchedulerJob> list = scheduleJobMapper.selectAll();
		for (SchedulerJob job : list) {
			CronTrigger cronTrigger = SchedulerUtils.getCronTrigger(scheduler, job.getJobId());
			if (cronTrigger == null) {
				SchedulerUtils.createSchedulerJob(scheduler, job);
			} else {
				SchedulerUtils.updateSchedulerJob(scheduler, job);
			}
		}
	}

	@Override
	public CommonResp<RespSchedulerJob> querySchedulerJob(CommonReq<ReqQuerySchedulerJob> commReq) throws Exception {
		ReqQuerySchedulerJob req = commReq.getParam();
		Example example = new Example(SchedulerJob.class);
		Criteria criteria = example.createCriteria();
		if (req.getJobId() != null && req.getJobId() > 0) {
			criteria.andEqualTo("jobId", req.getJobId());
		}
		if (StringUtil.isNotBlank(req.getBeanName())) {
			criteria.andEqualTo("beanName", req.getBeanName());
		}
		if (StringUtil.isNotBlank(req.getBeanNameLike())) {
			criteria.andLike("beanName", "%" + req.getBeanNameLike() + "%");
		}
		if (StringUtil.isNotBlank(req.getMethodName())) {
			criteria.andEqualTo("methodName", req.getMethodName());
		}
		if (StringUtil.isNotBlank(req.getMethodNameLike())) {
			criteria.andLike("methodName", "%" + req.getMethodNameLike() + "%");
		}
		if (StringUtil.isNotBlank(req.getRemark())) {
			criteria.andLike("remark", "%" + req.getRemark() + "%");
		}
		if (StringUtil.isNotBlank(req.getCronExpression())) {
			criteria.andLike("cronExpression", "%" + req.getCronExpression() + "%");
		}
		if (StringUtil.isNotBlank(req.getParams())) {
			criteria.andLike("params", "%" + req.getParams() + "%");
		}
		if (req.getStatus() != null) {
			criteria.andEqualTo("status", req.getStatus());
		}
		List<SchedulerJob> list = null;
		if (req.getPage() != null && req.getPage().getPIndex() >= 0 && req.getPage().getPSize() > 0) {
			Page<SchedulerJob> page = PageHelper.startPage((req.getPage().getPIndex() + 1), req.getPage().getPSize());
			list = scheduleJobMapper.selectByExample(example);
			req.getPage().setPCount(Long.valueOf(page.getTotal()).intValue());
		} else {
			// 默认每次最多查询200条
			PageHelper.startPage(1, 200);
			list = scheduleJobMapper.selectByExample(example);
		}
		List<RespSchedulerJob> respList = new ArrayList<>();
		for (SchedulerJob schedulerJob : list) {
			RespSchedulerJob resp = new RespSchedulerJob();
			BeanCopyUtils.copyProperties(schedulerJob, resp, null);
			respList.add(resp);
		}
		return new CommonResp<>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,
				req.getPage());
	}

	@Override
	@Transactional
	public CommonResp<RespMap> addSchedulerJob(CommonReq<ReqAddSchedulerJob> commReq) throws Exception {
		ReqAddSchedulerJob req = commReq.getParam();
		boolean isCron = CronExpression.isValidExpression(req.getCronExpression());
		if (!isCron) {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_PARAM, "添加定时任务失败，CronExpression表达式错误。");
		}

		Object obj = SpringContextUtil.getBeanByName(req.getBeanName());
		if (null == obj) {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_NOT_METHOD,
					"添加定时任务失败，没有找到Bean名称为" + req.getBeanName() + "的Bean对象");
		}
		Class<?> clazz = obj.getClass();
		Method[] ms = clazz.getMethods();
		boolean isMethod = false;
		String methodName = req.getMethodName();
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				if(StringUtil.isNotBlank(req.getParams())) {
					if(m.getParameterTypes()!=null 
							&& m.getParameterTypes().length==1 
							&& String.class.equals(m.getParameterTypes()[0])) {
						//有入参时，需验证是否存在有一个String类型参数的方法。
						isMethod = true;
						break;
					}
				}else {
					isMethod = true;
					break;
				}
			}
		}
		if (!isMethod) {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_NOT_METHOD,
					"添加定时任务失败，" + req.getBeanName() + "对象中没有找到名称为" + req.getMethodName() + "的方法");
		}
		SchedulerJob job = new SchedulerJob();
		BeanCopyUtils.copyProperties(req, job, null);
		job.setCreateTime(DateOper.getNowDateTime());
		job.setStatus(KstHosConstant.SchedulerStatus.NORMAL.getValue());
		job.setOperatorId(req.getOpenId());
		job.setOperatorName(req.getOperatorName());
		scheduleJobMapper.insertSelective(job);
		SchedulerUtils.createSchedulerJob(scheduler, job);
		return new CommonResp<RespMap>(commReq, RetCode.Success.RET_10000);
	}

	@Override
	@Transactional
	public CommonResp<RespMap> updateSchedulerJob(CommonReq<ReqUpdateSchedulerJob> commReq) throws Exception {
		ReqUpdateSchedulerJob req = commReq.getParam();
		Object obj = null;
		SchedulerJob oldJob = scheduleJobMapper.selectByPrimaryKey(req.getJobId());
		if (oldJob == null) {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_NOT_METHOD, "修改定时任务失败，没有找到Bean对象");
		}
		if (StringUtil.isNotBlank(req.getCronExpression())) {
			boolean isCron = CronExpression.isValidExpression(req.getCronExpression());
			if (!isCron) {
				return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_PARAM, "修改定时任务失败，CronExpression表达式错误。");
			}
		}

		if (StringUtil.isNotBlank(req.getBeanName())) {
			obj = SpringContextUtil.getBeanByName(req.getBeanName());
		} else {
			obj = SpringContextUtil.getBeanByName(oldJob.getBeanName());
		}
		if (null == obj) {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_NOT_METHOD,
					"修改定时任务失败，没有找到Bean名称为" + req.getBeanName() + "的Bean对象");
		}
		Class<?> clazz = obj.getClass();
		Method[] ms = clazz.getMethods();
		boolean isMethod = false;
		String methodName = null;
		String params = null;
		if (StringUtil.isNotBlank(req.getMethodName())) {
			methodName = req.getMethodName();
		} else {
			methodName = oldJob.getMethodName();
		}
		if (StringUtil.isNotBlank(req.getParams())) {
			params = req.getParams();
		} else {
			params = oldJob.getParams();
		}
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				if(StringUtil.isNotBlank(params)) {
					if(m.getParameterTypes()!=null 
							&& m.getParameterTypes().length==1 
							&& String.class.equals(m.getParameterTypes()[0])) {
						//有入参时，需验证是否存在有一个String类型参数的方法。
						isMethod = true;
						break;
					}
				}else {
					isMethod = true;
					break;
				}
			}
		}
		if (!isMethod) {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_NOT_METHOD,
					"修改定时任务失败，" + req.getBeanName() + "对象中没有找到名称为" + req.getMethodName() + "的方法");
		}

		SchedulerJob job = new SchedulerJob();
		BeanCopyUtils.copyProperties(req, job, null);

		int cc = scheduleJobMapper.updateByPrimaryKeyIncludeEmpty(job);
		if(cc>0) {
			job = scheduleJobMapper.selectByPrimaryKey(req.getJobId());
			SchedulerUtils.updateSchedulerJob(scheduler, job);
		}
		return new CommonResp<RespMap>(commReq, RetCode.Success.RET_10000);
	}

	@Override
	@Transactional
	public CommonResp<RespMap> delSchedulerJobs(CommonReq<ReqDelSchedulerJob> commReq) throws Exception {
		ReqDelSchedulerJob req = commReq.getParam();
		if (req.getJobId() != null && req.getJobId() > 0) {
			SchedulerUtils.deleteSchedulerJob(scheduler, req.getJobId());
			scheduleJobMapper.deleteByPrimaryKey(req.getJobId());
		} else if (req.getJobIds() != null && req.getJobIds().size() > 0) {
			for (Long jobId : req.getJobIds()) {
				SchedulerUtils.deleteSchedulerJob(scheduler, jobId);
				scheduleJobMapper.deleteByPrimaryKey(jobId);
			}
		} else {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_PARAM, "删除失败，参数JobId不能为空。");
		}
		return new CommonResp<RespMap>(commReq, RetCode.Success.RET_10000);
	}

	@Override
	@Transactional
	public CommonResp<RespMap> runSchedulerJobs(CommonReq<ReqRunSchedulerJob> commReq) throws Exception {
		ReqRunSchedulerJob req = commReq.getParam();

		if (req.getJobId() != null && req.getJobId() > 0) {
			SchedulerJob job = scheduleJobMapper.selectByPrimaryKey(req.getJobId());
			if (job != null) {
				SchedulerUtils.run(scheduler, job);
			}
		} else if (req.getJobIds() != null && req.getJobIds().size() > 0) {
			Example example = new Example(SchedulerJob.class);
			example.createCriteria().andIn("jobId", req.getJobIds());
			List<SchedulerJob> list = scheduleJobMapper.selectByExample(example);
			for (SchedulerJob job : list) {
				SchedulerUtils.run(scheduler, job);
			}
		} else {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_PARAM, "执行失败，参数JobId不能为空。");
		}

		return new CommonResp<RespMap>(commReq, RetCode.Success.RET_10000);
	}

	@Override
	@Transactional
	public CommonResp<RespMap> pauseSchedulerJobs(CommonReq<ReqPauseSchedulerJob> commReq) throws Exception {
		ReqPauseSchedulerJob req = commReq.getParam();

		if (req.getJobId() != null && req.getJobId() > 0) {
			SchedulerUtils.pauseJob(scheduler, req.getJobId());
			SchedulerJob upJob = new SchedulerJob();
			upJob.setJobId(req.getJobId());
			upJob.setStatus(KstHosConstant.SchedulerStatus.PAUSE.getValue());
			upJob.setOperatorId(req.getOpenId());
			upJob.setOperatorName(req.getOperatorName());

			scheduleJobMapper.updateByPrimaryKeySelective(upJob);
		} else if (req.getJobIds() != null && req.getJobIds().size() > 0) {
			for (Long jobId : req.getJobIds()) {
				SchedulerUtils.pauseJob(scheduler, jobId);
			}
			SchedulerJob upJob = new SchedulerJob();
			upJob.setStatus(KstHosConstant.SchedulerStatus.PAUSE.getValue());
			upJob.setOperatorId(req.getOpenId());
			upJob.setOperatorName(req.getOperatorName());

			Example example = new Example(SchedulerJob.class);
			example.createCriteria().andIn("jobId", req.getJobIds());
			scheduleJobMapper.updateByExampleSelective(upJob, example);
		} else {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_PARAM, "暂停失败，参数JobId不能为空。");
		}
		return new CommonResp<RespMap>(commReq, RetCode.Success.RET_10000);
	}

	@Override
	@Transactional
	public CommonResp<RespMap> resumeSchedulerJobs(CommonReq<ReqResumeSchedulerJob> commReq) throws Exception {
		ReqResumeSchedulerJob req = commReq.getParam();

		if (req.getJobId() != null && req.getJobId() > 0) {
			SchedulerUtils.resumeJob(scheduler, req.getJobId());

			SchedulerJob upJob = new SchedulerJob();
			upJob.setJobId(req.getJobId());
			upJob.setStatus(KstHosConstant.SchedulerStatus.NORMAL.getValue());
			upJob.setOperatorId(req.getOpenId());
			upJob.setOperatorName(req.getOperatorName());
			scheduleJobMapper.updateByPrimaryKeySelective(upJob);

		} else if (req.getJobIds() != null && req.getJobIds().size() > 0) {
			for (Long jobId : req.getJobIds()) {
				SchedulerUtils.resumeJob(scheduler, jobId);
			}
			SchedulerJob upJob = new SchedulerJob();
			upJob.setStatus(KstHosConstant.SchedulerStatus.NORMAL.getValue());
			upJob.setOperatorId(req.getOpenId());
			upJob.setOperatorName(req.getOperatorName());

			Example example = new Example(SchedulerJob.class);
			example.createCriteria().andIn("jobId", req.getJobIds());
			scheduleJobMapper.updateByExampleSelective(upJob, example);
		} else {
			return new CommonResp<RespMap>(commReq, RetCode.Common.ERROR_PARAM, "恢复失败，参数JobId不能为空。");
		}
		return new CommonResp<RespMap>(commReq, RetCode.Success.RET_10000);
	}

}
