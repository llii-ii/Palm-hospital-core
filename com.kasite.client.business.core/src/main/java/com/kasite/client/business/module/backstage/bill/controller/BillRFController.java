package com.kasite.client.business.module.backstage.bill.controller;

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
import com.kasite.core.serviceinterface.module.pay.IBillRFService;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryBill;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillForMonth;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillRFForDate;
import com.yihu.wsgw.api.InterfaceMessage;

@RequestMapping("/billRf")
@RestController
public class BillRFController extends AbstractController {

	@Autowired
	IBillRFService billRFService;
	
	@PostMapping("/queryBillRFListForDate.do")
	@RequiresPermissions("bill:billRf:query")
	@SysLog(value="对账统计(日统计)", isSaveResult=false)
	R queryBillRFListForDate(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillRFListForDate", reqParam, request);
		CommonResp<RespQueryBillRFForDate> resp = billRFService.queryBillRFListForDate(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryBillRFListForMonth.do")
	@RequiresPermissions("bill:billRf:query")
	@SysLog(value="对账统计(月统计)", isSaveResult=false)
	R queryBillRFListForMonth(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillDetail", reqParam, request);
		CommonResp<RespQueryBillForMonth> resp = billRFService.queryBillRFListForMonth(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/downloadBillRFListData.do")
	@RequiresPermissions("bill:billRf:query")
	@SysLog(value="下载对账统计账单", isSaveResult=false)
	R downloadBillRFListData(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillDetail", reqParam, request);
		CommonResp<RespMap> resp = billRFService.downloadBillRFListData(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
}
