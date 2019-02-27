package com.kasite.client.job.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.R;
import com.kasite.core.serviceinterface.job.ISchedulerJobService;
import com.kasite.core.serviceinterface.job.req.ReqAddSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqDelSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqPauseSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqQuerySchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqResumeSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqRunSchedulerJob;
import com.kasite.core.serviceinterface.job.req.ReqUpdateSchedulerJob;
import com.kasite.core.serviceinterface.job.resp.RespSchedulerJob;
import com.yihu.wsgw.api.InterfaceMessage;

@RestController
@RequestMapping("/job")
public class SchedulerJobController extends AbstractController{
	
	@Autowired
	ISchedulerJobService schedulerJobService;
	
	@RequiresPermissions("job:list")
	@PostMapping("/queryList.do")
	@SysLog(value="查询定时任务列表",isSaveResult=false)
	R queryList(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("querySchedulerJob", reqParam, request);
		ReqQuerySchedulerJob req = new ReqQuerySchedulerJob(msg);
		CommonResp<RespSchedulerJob> resp = schedulerJobService.querySchedulerJob(new CommonReq<ReqQuerySchedulerJob>(req));
		return R.ok(resp.toJSONResult());
	}
	
	@RequiresPermissions("job:info")
	@PostMapping("/queryInfo.do")
	@SysLog(value="查询定时任务详情",isSaveResult=false)
	R queryInfo(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("querySchedulerJob", reqParam, request);
		ReqQuerySchedulerJob req = new ReqQuerySchedulerJob(msg);
		CommonResp<RespSchedulerJob> resp = schedulerJobService.querySchedulerJob(new CommonReq<ReqQuerySchedulerJob>(req));
		return R.ok(resp.toJSONResult());
	}
	
	@RequiresPermissions("job:save")
	@PostMapping("/save.do")
	@SysLog(value="新增定时任务",isSaveResult=true)
	R save(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addSchedulerJob", reqParam, request);
		ReqAddSchedulerJob req = new ReqAddSchedulerJob(msg);
		CommonResp<RespMap> resp = schedulerJobService.addSchedulerJob(new CommonReq<ReqAddSchedulerJob>(req));
		return R.ok(resp.toJSONResult());
	}
	
	@RequiresPermissions("job:update")
	@PostMapping("/update.do")
	@SysLog(value="修改定时任务",isSaveResult=true)
	R update(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateSchedulerJob", reqParam, request);
		ReqUpdateSchedulerJob req = new ReqUpdateSchedulerJob(msg);
		CommonResp<RespMap> resp = schedulerJobService.updateSchedulerJob(new CommonReq<ReqUpdateSchedulerJob>(req));
		return R.ok(resp.toJSONResult());
	}
	
	@RequiresPermissions("job:delete")
	@PostMapping("/delete.do")
	@SysLog(value="删除定时任务",isSaveResult=true)
	R delete(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("delSchedulerJobs", reqParam, request);
		ReqDelSchedulerJob req = new ReqDelSchedulerJob(msg);
		CommonResp<RespMap> resp = schedulerJobService.delSchedulerJobs(new CommonReq<ReqDelSchedulerJob>(req));
		return R.ok(resp.toJSONResult());
	}
	
	@RequiresPermissions("job:run")
	@PostMapping("/run.do")
	@SysLog(value="立即执行定时任务",isSaveResult=true)
	R run(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("runSchedulerJobs", reqParam, request);
		ReqRunSchedulerJob req = new ReqRunSchedulerJob(msg);
		CommonResp<RespMap> resp = schedulerJobService.runSchedulerJobs(new CommonReq<ReqRunSchedulerJob>(req));
		return R.ok(resp.toJSONResult());
	}
	
	@RequiresPermissions("job:pause")
	@PostMapping("/pause.do")
	@SysLog(value="暂停定时任务",isSaveResult=true)
	R pause(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("pauseSchedulerJobs", reqParam, request);
		ReqPauseSchedulerJob req = new ReqPauseSchedulerJob(msg);
		CommonResp<RespMap> resp = schedulerJobService.pauseSchedulerJobs(new CommonReq<ReqPauseSchedulerJob>(req));
		return R.ok(resp.toJSONResult());
	}

	@RequiresPermissions("job:resume")
	@PostMapping("/resume.do")
	@SysLog(value="恢复定时任务",isSaveResult=true)
	R resume(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("resumeSchedulerJobs", reqParam, request);
		ReqResumeSchedulerJob req = new ReqResumeSchedulerJob(msg);
		CommonResp<RespMap> resp = schedulerJobService.resumeSchedulerJobs(new CommonReq<ReqResumeSchedulerJob>(req));
		return R.ok(resp.toJSONResult());
	}
}
