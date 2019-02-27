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
import com.kasite.core.serviceinterface.module.pay.req.ReqUpdateBankCheckInfo;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBankCheckCount;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBankMoneyCheck;
import com.yihu.wsgw.api.InterfaceMessage;

@RequestMapping("/bank")
@RestController
public class BankCheckController extends AbstractController {

	@Autowired
	IBillRFService billRFService;
	
	@PostMapping("/queryBankCheckList.do")
	@RequiresPermissions("bill:bankCheck:query")
	@SysLog(value="银行勾兑列表", isSaveResult=false)
	R queryBankCheckList(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBankCheckList", reqParam, request);
		CommonResp<RespQueryBankMoneyCheck> resp = billRFService.queryBankMoneyCheckList(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryBankCheckCount.do")
	@RequiresPermissions("bill:bankCheck:query")
	@SysLog(value="银行勾兑统计", isSaveResult=false)
	R queryBankCheckCount(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillChannelRFList", reqParam, request);
		CommonResp<RespQueryBankCheckCount> resp = billRFService.queryBankCheckCount(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/saveBankCheckInfo.do")
	@RequiresPermissions("bill:bankCheck:update")
	@SysLog(value="修改银行勾兑信息")
	R saveBankCheckInfo(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("saveBankCheckInfo", reqParam, request);
		CommonResp<RespMap> resp = billRFService.saveBankCheckInfo(new CommonReq<ReqUpdateBankCheckInfo>(new ReqUpdateBankCheckInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/downloadBankBillListData.do")
	@RequiresPermissions("bill:bankCheck:download")
	@SysLog(value="下载银行勾兑列表", isSaveResult=false)
	R downloadBankBillListData(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("downloadBankBillListData", reqParam, request);
		CommonResp<RespMap> resp = billRFService.downloadBankBillListData(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
}
