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
import com.kasite.core.common.util.R;
import com.kasite.core.serviceinterface.job.ISchedulerJobLogService;
import com.kasite.core.serviceinterface.job.req.ReqQuerySchedulerJobLog;
import com.kasite.core.serviceinterface.job.resp.RespSchedulerJobLog;
import com.yihu.wsgw.api.InterfaceMessage;

@RestController
@RequestMapping("/jobLog")
public class SchedulerJobLogController extends AbstractController{
	@Autowired
	ISchedulerJobLogService schedulerJobLogService;
	
	@RequiresPermissions("jobLog:list")
	@PostMapping("/queryList.do")
	@SysLog(value="查询定时任务执行日志列表",isSaveResult=false)
	R queryList(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("querySchedulerJobLog", reqParam, request);
		ReqQuerySchedulerJobLog req = new ReqQuerySchedulerJobLog(msg);
		CommonResp<RespSchedulerJobLog>  resp = schedulerJobLogService.querySchedulerJobLog(new CommonReq<ReqQuerySchedulerJobLog>(req));
		return R.ok(resp.toJSONResult());
	}
}
