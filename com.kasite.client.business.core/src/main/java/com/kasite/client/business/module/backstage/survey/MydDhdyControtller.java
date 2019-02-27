package com.kasite.client.business.module.backstage.survey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.R;
import com.kasite.core.serviceinterface.module.survey.SurveyService;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuerySampleInfo;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuerySubject;
import com.kasite.core.serviceinterface.module.survey.resp.RespQueryQuestion;
import com.kasite.core.serviceinterface.module.survey.resp.RespQuerySampleInfo;
import com.kasite.core.serviceinterface.module.survey.resp.RespQuerySubject;
import com.yihu.wsgw.api.InterfaceMessage;

@RestController
public class MydDhdyControtller extends AbstractController {

	@Autowired
	SurveyService surveyService;
	
	@PostMapping("/survey/querySampleBySubjectid.do")
	@RequiresPermissions("survey:subject:query")
	@SysLog(value="查询问卷模板数量", isSaveResult=false)
	R querySampleBySubjectid(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("querySampleBySubjectid", reqParam, request);
		CommonResp<RespQuerySampleInfo> respList = surveyService.querySampleBySubjectid(new CommonReq<ReqQuerySampleInfo>(new ReqQuerySampleInfo(msg)));
		CommonResp<RespMap> respMap = surveyService.querySampleCount(new CommonReq<ReqQuerySampleInfo>(new ReqQuerySampleInfo(msg)));
		return R.ok(respList.toJSONResult()).put("totalProperty", respMap.toJSONResult().getJSONArray("Data").getJSONObject(0).getString("SampleCount"));
	}
	
	@PostMapping("/survey/querySubjectForNetList.do")
	@RequiresPermissions("survey:subject:query")
	@SysLog(value="查询是否有问题列表", isSaveResult=false)
	R querySubjectForNetList(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("querySubjectForNetList", reqParam, request);
		CommonResp<RespQuerySubject> resp = surveyService.querySubjectForNet(new CommonReq<ReqQuerySubject>(new ReqQuerySubject(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/personStatisticaBySampleId.do")
	@RequiresPermissions("survey:subject:query")
	@SysLog(value="查询问卷统计概况", isSaveResult=false)
	R personStatisticaBySampleId(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("personStatisticaBySampleId", reqParam, request);
		CommonResp<RespQueryQuestion> resp = surveyService.personStatisticaBySampleId(new CommonReq<ReqQuerySampleInfo>(new ReqQuerySampleInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	/**
	 * 暂时未用到(保留)
	 * 
	 * @param reqParam
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/survey/examCopy.do")
	@RequiresPermissions("survey:subject:query")
	@SysLog(value="问卷模板复制", isSaveResult=false)
	R examCopy(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		return R.ok();
	}
	
}
