package com.kasite.server.verification.module.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.core.common.annotation.ApiCallLog;
import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.util.R;
import com.kasite.server.verification.common.controller.AbstractController;
import com.yihu.wsgw.api.InterfaceMessage;

@RestController
@RequestMapping("/api")
public class SysApiController extends AbstractController{
	
	@AuthIgnore
	@ApiCallLog("调用 Rpc 相应接口")
	@RequestMapping(value = "/{node}/{module}/{methodName}", method = RequestMethod.POST)
	public R call(final HttpServletRequest request,
			@PathVariable(value = "node") String node,
			@PathVariable(value = "module") String module,
			@PathVariable(value = "methodName") String methodName) {
		logger.info(node+"."+module+"."+methodName);
		logger.info(request.getHeader("token"));
		String params = request.getParameter("param");
		InterfaceMessage msg = new InterfaceMessage();
//		String result = ABus.create("api", params).send().awaitReceive();
		return R.ok();
	}
	
	
}
