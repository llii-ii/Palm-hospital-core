package com.kasite.client.job.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coreframework.util.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kasite.client.job.dao.SchedulerJobLogMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.serviceinterface.job.ISchedulerJobLogService;
import com.kasite.core.serviceinterface.job.dbo.SchedulerJob;
import com.kasite.core.serviceinterface.job.dbo.SchedulerJobLog;
import com.kasite.core.serviceinterface.job.req.ReqQuerySchedulerJobLog;
import com.kasite.core.serviceinterface.job.req.ReqSaveSchedulerJobLog;
import com.kasite.core.serviceinterface.job.resp.RespSchedulerJobLog;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ScheduleJobLogServiceImpl implements ISchedulerJobLogService{
	
	@Autowired
	SchedulerJobLogMapper schedulerJobLogMapper;
	
	@Override
	public CommonResp<RespSchedulerJobLog> querySchedulerJobLog(CommonReq<ReqQuerySchedulerJobLog> commReq)
			throws Exception {
		ReqQuerySchedulerJobLog req = commReq.getParam();
		Example example = new Example(SchedulerJobLog.class);
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
		if (StringUtil.isNotBlank(req.getParams())) {
			criteria.andLike("params", "%" + req.getParams() + "%");
		}
		if (req.getStatus() != null) {
			criteria.andEqualTo("status", req.getStatus());
		}
		if(StringUtil.isNotBlank(req.getStartTime())) {
			criteria.andGreaterThanOrEqualTo("createTime", req.getStartTime());
		}
		if(StringUtil.isNotBlank(req.getEndTime())) {
			criteria.andLessThanOrEqualTo("createTime", req.getEndTime());
		}
		if(req.getGteTimes()!=null) {
			criteria.andGreaterThanOrEqualTo("times", req.getGteTimes());
		}
		if(req.getLteTimes()!=null) {
			criteria.andLessThanOrEqualTo("times", req.getLteTimes());
		}
		List<SchedulerJobLog> list = null;
		if (req.getPage() != null && req.getPage().getPIndex() >= 0 && req.getPage().getPSize() > 0) {
			Page<SchedulerJob> page = PageHelper.startPage((req.getPage().getPIndex() + 1), req.getPage().getPSize());
			list = schedulerJobLogMapper.selectByExample(example);
			req.getPage().setPCount(Long.valueOf(page.getTotal()).intValue());
		} else {
			// 默认每次最多查询200条
			PageHelper.startPage(1, 200);
			list = schedulerJobLogMapper.selectByExample(example);
		}
		
		List<RespSchedulerJobLog> respList = new ArrayList<>();
		for (SchedulerJobLog jobLog : list) {
			RespSchedulerJobLog resp = new RespSchedulerJobLog();
			BeanCopyUtils.copyProperties(jobLog, resp, null);
			respList.add(resp);
		}
		return new CommonResp<>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,
				req.getPage());
	}

	@Override
	public CommonResp<RespMap> saveSchedulerJobLog(CommonReq<ReqSaveSchedulerJobLog> commReq) throws Exception {
		ReqSaveSchedulerJobLog req = commReq.getParam();
		
		SchedulerJobLog jobLog = new SchedulerJobLog();
		
		BeanCopyUtils.copyProperties(req, jobLog, null);
		
		schedulerJobLogMapper.insertSelective(jobLog);
		
		return new CommonResp<>(commReq, RetCode.Success.RET_10000);
	}
	
}
