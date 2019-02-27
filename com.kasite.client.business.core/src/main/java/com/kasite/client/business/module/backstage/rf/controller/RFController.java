package com.kasite.client.business.module.backstage.rf.controller;

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
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.kasite.core.serviceinterface.module.rf.req.ReqQueryBillRf;
import com.kasite.core.serviceinterface.module.rf.resp.RespQueryStdReport;
import com.yihu.wsgw.api.InterfaceMessage;

@RequestMapping("/report")
@RestController
public class RFController extends AbstractController {

	@Autowired
	IReportFormsService reportFormsService;
	
	@PostMapping("/init.do")
	@RequiresPermissions("bill:rf:query")
	@SysLog(value="运营状况-昨日交易数据", isSaveResult=false)
	R init(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("init", reqParam, request);
		CommonResp<RespMap> resp = reportFormsService.queryTransationInfo(new CommonReq<ReqQueryBillRf>(new ReqQueryBillRf(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/echatsShow.do")
	@RequiresPermissions("bill:rf:query")
	@SysLog(value="运营状况-趋势走向", isSaveResult=false)
	R echatsShow(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("echatsShow", reqParam, request);
		CommonResp<RespQueryStdReport> resp = reportFormsService.queryEchatsShow(new CommonReq<ReqQueryBillRf>(new ReqQueryBillRf(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/yearCount.do")
	@RequiresPermissions("bill:rf:query")
	@SysLog(value="运营状况-年度统计", isSaveResult=false)
	R yearCount(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("yearCount", reqParam, request);
		CommonResp<RespMap> resp = reportFormsService.queryYearCount(new CommonReq<ReqQueryBillRf>(new ReqQueryBillRf(msg)));
		return R.ok(resp.toJSONResult());
	}
	
}
