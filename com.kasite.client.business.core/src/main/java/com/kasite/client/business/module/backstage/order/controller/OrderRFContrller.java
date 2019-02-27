package com.kasite.client.business.module.backstage.order.controller;

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
import com.kasite.core.serviceinterface.module.order.IOrderRFService;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryLocalOrderInfo;
import com.yihu.wsgw.api.InterfaceMessage;

@RequestMapping("/orderReport")
@RestController
public class OrderRFContrller extends AbstractController {
	
	@Autowired
	IOrderRFService orderRFService;
	
	@PostMapping("/queryTransCountList.do")
	@RequiresPermissions("order:orderReport:query")
	@SysLog(value="交易汇总日报表", isSaveResult=false)
	R queryTransCountList(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryTransCountList", reqParam, request);
		CommonResp<RespMap> resp = orderRFService.queryTransCountList(new CommonReq<ReqQueryLocalOrderInfo>(new ReqQueryLocalOrderInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/downloadOrderReportData.do")
	@RequiresPermissions("order:orderReport:download")
	@SysLog(value="交易日报表下载", isSaveResult=false)
	R downloadOrderReportData(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rootDir = request.getSession().getServletContext().getRealPath("/");
		InterfaceMessage msg = createInterfaceMsg("downloadOrderReportData", reqParam, request);
		CommonResp<RespMap> resp = orderRFService.downloadOrderReportData(new CommonReq<ReqQueryLocalOrderInfo>(new ReqQueryLocalOrderInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
}
