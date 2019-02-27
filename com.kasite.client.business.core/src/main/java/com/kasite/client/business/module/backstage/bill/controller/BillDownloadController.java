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
import com.yihu.wsgw.api.InterfaceMessage;

@RequestMapping("/billDownload")
@RestController
public class BillDownloadController extends AbstractController {

	@Autowired
	IBillRFService billRFService;
	
	@PostMapping("/downloadAllBillFile.do")
	@RequiresPermissions("bill:billDownload:download")
	@SysLog(value="对账文件打包下载", isSaveResult=false)
	R downloadAllBillFile(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("downloadBankBillListData", reqParam, request);
		CommonResp<RespMap> resp = billRFService.downloadAllBillFile(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
}
