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
import com.kasite.core.serviceinterface.module.survey.req.ReqUpdateSubject;
import com.yihu.wsgw.api.InterfaceMessage;

@RestController
public class CjwjController extends AbstractController {

	@Autowired
	SurveyService surveyService;
	
	@PostMapping("/survey/addSubject.do")
	@RequiresPermissions("survey:subject:insert")
	@SysLog(value="新建问卷")
	R addSubject(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addSubject", reqParam, request);
		CommonResp<RespMap> resp = surveyService.addSubject(new CommonReq<ReqUpdateSubject>(new ReqUpdateSubject(msg, "add")));
		return R.ok(resp.toJSONResult());
	}
}
