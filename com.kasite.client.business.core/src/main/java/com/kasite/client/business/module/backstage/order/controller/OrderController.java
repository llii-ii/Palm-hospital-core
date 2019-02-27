package com.kasite.client.business.module.backstage.order.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.sys.req.ReqQueryUserKey;
import com.kasite.core.common.util.R;
import com.kasite.core.serviceinterface.module.order.IOrderRFService;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqForceCorrectOrderBiz;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryLocalOrderInfo;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderPayState;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderRFList;
import com.kasite.core.serviceinterface.module.order.resp.RespPayOrderDetail;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryLocalTransLogInfo;
import com.kasite.core.serviceinterface.module.order.resp.RespRefundOrderDetail;
import com.yihu.wsgw.api.InterfaceMessage;

@RequestMapping("/order")
@RestController
public class OrderController extends AbstractController {

	@Autowired
	IOrderRFService orderRFService;
	
	@Autowired
	IOrderService orderService;
	
	@PostMapping("/refundAgin.do")
	@RequiresPermissions("order:refund:update")
	@SysLog(value="退款重试")
	R refundAgin(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("refundAgin", reqParam, request);
		CommonResp<RespMap> resp = orderService.refundOrderAgin(new CommonReq<ReqOrderIsCancel>(new ReqOrderIsCancel(msg, "refund")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryAllOrderList.do")
	@RequiresPermissions("order:order:query")
	@SysLog(value="交易明细列表", isSaveResult=false)
	R queryAllOrderList(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryAllOrderList", reqParam, request);
		CommonResp<RespOrderRFList> resp = orderRFService.queryOrderListLocal(new CommonReq<ReqQueryLocalOrderInfo>(new ReqQueryLocalOrderInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryTotalCountMoney.do")
	@RequiresPermissions("order:order:query")
	@SysLog(value="统计交易金额", isSaveResult=false)
	R queryTotalCountMoney(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryTotalCountMoney", reqParam, request);
		CommonResp<RespMap> resp = orderRFService.queryTotalCountMoney(new CommonReq<ReqQueryLocalOrderInfo>(new ReqQueryLocalOrderInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryPayOrderDetail.do")
	@RequiresPermissions("order:order:query")
	@SysLog(value="支付订单详情", isSaveResult=false)
	R queryPayOrderDetail(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryOrderDetail", reqParam, request);
		CommonResp<RespPayOrderDetail> resp = orderRFService.queryPayOrderDetail(new CommonReq<ReqQueryLocalOrderInfo>(new ReqQueryLocalOrderInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryRefundOrderDetail.do")
	@RequiresPermissions("order:order:query")
	@SysLog(value="退款订单详情", isSaveResult=false)
	R queryRefundOrderDetail(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryRefundOrderDetail", reqParam, request);
		CommonResp<RespRefundOrderDetail> resp = orderRFService.queryRefundOrderDetail(new CommonReq<ReqQueryLocalOrderInfo>(new ReqQueryLocalOrderInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/downloadOrderListData.do")
	@RequiresPermissions("order:order:download")
	@SysLog(value="下载交易订单列表数据", isSaveResult=false)
	R downloadOrderListData(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("downloadOrderListData", reqParam, request);
		CommonResp<RespMap> resp = orderRFService.downloadOrderListData(new CommonReq<ReqQueryLocalOrderInfo>(new ReqQueryLocalOrderInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/orderRefund.do")
	@RequiresPermissions("order:order:update")
	@SysLog(value="退款")
	R orderRefund(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("orderRefund", reqParam, request);
		//1.先验证二级支付密码是否正确
		CommonResp<RespMap> userPayKeyResp = sysUserService.validateUserPayKey(new CommonReq<ReqQueryUserKey>(new ReqQueryUserKey(msg)));
		JSONObject resultPayKeyJson = userPayKeyResp.toJSONResult();
		if(resultPayKeyJson.getInteger("RespCode") < RetCode.Success.RET_10000.getCode()) {
			return R.error("支付密码为空或输入错误!");
		}
		//2.执行退款操作
		msg = createInterfaceMsg("orderIsCancel", reqParam, request);
		CommonResp<RespMap> respMap = orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(new ReqOrderIsCancel(msg,"refund")));
		return R.ok(respMap.toJSONResult());
	}
	
	@PostMapping("/orderReverse.do")
	@RequiresPermissions("order:order:update")
	@SysLog(value="强制冲正订单")
	R orderReverse(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//执行冲正操作
		InterfaceMessage msg = createInterfaceMsg("forceCorrectOrderBiz", reqParam, request);
		CommonResp<RespMap> respMap = orderService.forceCorrectOrderBiz(new CommonReq<ReqForceCorrectOrderBiz>(new ReqForceCorrectOrderBiz(msg, "billReverse")));
		return R.ok(respMap.toJSONResult());
	}
	
	@PostMapping("/orderSynchro.do")
	@RequiresPermissions("order:order:update")
	@SysLog(value="同步")
	R orderSynchro(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return R.ok();
	}
	
	@PostMapping("/queryOrderState.do")
	@RequiresPermissions("order:order:query")
	@SysLog(value="获取订单状态", isSaveResult=false)
	R queryOrderState(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryOrderState", reqParam, request);
		CommonResp<RespMap> resp = orderService.queryOrderState(new CommonReq<ReqQueryOrderPayState>(new ReqQueryOrderPayState(msg, "query")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryTransLog.do")
	@RequiresPermissions("order:order:query")
	@SysLog(value="交易日志", isSaveResult=false)
	R queryTransLog(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryTransLog", reqParam, request);
		CommonResp<RespQueryLocalTransLogInfo> resp = orderService.queryTransLogInfo(new CommonReq<ReqQueryLocalOrderInfo>(new ReqQueryLocalOrderInfo(msg)));
		return R.ok(resp.toJSONResult());
	}
	
}
