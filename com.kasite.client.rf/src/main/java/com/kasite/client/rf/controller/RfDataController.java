package com.kasite.client.rf.controller;

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
import com.kasite.core.common.req.ReqString;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.R;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.kasite.core.serviceinterface.module.rf.req.ReqGetDCLine;
import com.kasite.core.serviceinterface.module.rf.req.ReqGetDCbar;
import com.kasite.core.serviceinterface.module.rf.req.ReqGetDataCollectionGrid;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: RfDataController
 * @author: lcz
 * @date: 2018年8月31日 下午4:09:08
 */
@RequestMapping("/rf")
@RestController
public class RfDataController extends AbstractController{
	
	@Autowired
	private IReportFormsService rfService;
	
	@PostMapping("/getDCSummary.do")
	@SysLog(value="查询报表汇总",isSaveResult=false)
	R getDCSummary(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("getDCSummary", reqParam, request);
		CommonResp<RespMap>  resp = rfService.getDCSummary(new CommonReq<ReqString>(new ReqString(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/getDataCollectionGrid.do")
	@SysLog(value="查询报表表格数据",isSaveResult=false)
	R getDataCollectionGrid(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("getDataCollectionGrid", reqParam, request);
		CommonResp<RespMap>  resp = rfService.getDataCollectionGrid(new CommonReq<ReqGetDataCollectionGrid>(new ReqGetDataCollectionGrid(msg)));
		return R.ok(resp.toJSONResult());
	}
	@PostMapping("/getDCLine.do")
	@SysLog(value="查询报表折线图数据",isSaveResult=false)
	R getDCLine(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("getDCLine", reqParam, request);
		CommonResp<RespMap>  resp = rfService.getDCLine(new CommonReq<ReqGetDCLine>(new ReqGetDCLine(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/getDCbar.do")
	@SysLog(value="查询报表折线图数据",isSaveResult=false)
	R getDCbar(String reqParam,HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("getDCbar", reqParam, request);
		CommonResp<RespMap>  resp = rfService.getDCbar(new CommonReq<ReqGetDCbar>(new ReqGetDCbar(msg)));
		return R.ok(resp.toJSONResult());
	}
	
}
