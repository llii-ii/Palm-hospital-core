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
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillChannelRF;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillMerchRF;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryChannelBankCheck;
import com.yihu.wsgw.api.InterfaceMessage;

@RequestMapping("/billReport")
@RestController
public class BillReportController extends AbstractController {
	
	@Autowired
	IBillRFService billRFService;
	
	@PostMapping("/queryBillChannelRFList.do")
	@RequiresPermissions("bill:billRf:query")
	@SysLog(value="交易渠道的对账报表", isSaveResult=false)
	R queryBillChannelRFList(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillChannelRFList", reqParam, request);
		CommonResp<RespQueryBillChannelRF> resp = billRFService.queryBillChannelRF(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryBillMerchRFList.do")
	@RequiresPermissions("bill:billRf:query")
	@SysLog(value="支付方式的对账报表", isSaveResult=false)
	R queryBillMerchRFList(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillMerchRFList", reqParam, request);
		CommonResp<RespQueryBillMerchRF> resp = billRFService.queryBillMerchRF(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryCurrentDate.do")
	@RequiresPermissions("bill:billRf:query")
	@SysLog(value="最新对账报表的时间", isSaveResult=false)
	R queryCurrentDate(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryCurrentDate", reqParam, request);
		CommonResp<RespMap> resp = billRFService.queryCurrentDate(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/downloadBillReportListData.do")
	@RequiresPermissions("bill:billRf:download")
	@SysLog(value="下载对账报表列表数据", isSaveResult=false)
	R downloadBillReportListData(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("downloadBillReportListData", reqParam, request);
		CommonResp<RespMap> resp = billRFService.downloadBillReportListData(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryChannelBankCheckList.do")
	@RequiresPermissions("bill:channelBankCheck:query")
	@SysLog(value="渠道下银行账号金额统计列表", isSaveResult=false)
	R queryChannelBankCheckList(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryChannelBankCheckList", reqParam, request);
		CommonResp<RespQueryChannelBankCheck> resp = billRFService.queryChannelBankCheckList(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/downloadChannelBankReportListData.do")
	@RequiresPermissions("bill:channelBankCheck:download")
	@SysLog(value="下载渠道关联银行报表数据", isSaveResult=false)
	R downloadChannelBankReportListData(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("downloadBillReportListData", reqParam, request);
		CommonResp<RespMap> resp = billRFService.downloadChannelBankReportListData(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
}
