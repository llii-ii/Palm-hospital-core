package com.kasite.client.business.module.backstage.bill.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.sys.req.ReqQueryUserKey;
import com.kasite.core.common.sys.service.RedisUtil;
import com.kasite.core.common.sys.service.SysUserService;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserRoleEntity;
import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;
import com.kasite.core.common.util.CookieTool;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.R;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.StringUtils;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqForceCorrectOrderBiz;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqSyncLocalOrderState;
import com.kasite.core.serviceinterface.module.order.req.ReqSynchroBill;
import com.kasite.core.serviceinterface.module.pay.IBillRFService;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryBill;
import com.kasite.core.serviceinterface.module.pay.resp.RespBillCheckCount;
import com.kasite.core.serviceinterface.module.pay.resp.RespExceptionBillCount;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillDetail;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillForDate;
import com.yihu.wsgw.api.InterfaceMessage;

@RequestMapping("/bill")
@RestController
public class BillController extends AbstractController {
	
	@Autowired
	IBillRFService billRFService;
	
	@Autowired
	IOrderService orderService;
	
	@Autowired
	SysUserService sysUserService;
	
	@PostMapping("/queryBillCount.do")
	@RequiresPermissions("bill:bill:query")
	@SysLog(value="账单统计", isSaveResult=false)
	R queryBillCount(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillCount", reqParam, request);
		CommonResp<RespBillCheckCount> resp = billRFService.queryBillCheckCount(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryBillList.do")
	@RequiresPermissions("bill:billDetail:query")
	@SysLog(value="对账账单明细列表", isSaveResult=false)
	R queryBillList(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillList", reqParam, request);
		CommonResp<RespQueryBillForDate> resp = billRFService.queryBillList(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryExceptionBillCount.do")
	@RequiresPermissions("bill:exceptionBill:query")
	@SysLog(value="异常账单统计", isSaveResult=false)
	R queryExceptionBillCount(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryExceptionBillCount", reqParam, request);
		CommonResp<RespExceptionBillCount> resp = billRFService.queryExceptionBillCount(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryBillListForException.do")
	@RequiresPermissions("bill:exceptionBill:query")
	@SysLog(value="异常账单明细列表", isSaveResult=false)
	R queryBillListForException(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillListForException", reqParam, request);
		CommonResp<RespQueryBillForDate> resp = billRFService.queryBillListForException(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}

	@PostMapping("/queryBillDetail.do")
	@RequiresPermissions("bill:billDetail:query")
	@SysLog(value="账单详情(未处理)", isSaveResult=false)
	R queryBillDetail(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryBillDetail", reqParam, request);
		CommonResp<RespQueryBillDetail> resp = billRFService.queryBillDetail(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/queryDealBillDetail.do")
	@RequiresPermissions("bill:billDetail:query")
	@SysLog(value="账单详情(已处理)", isSaveResult=false)
	R queryDealBillDetail(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryDealBillDetail", reqParam, request);
		CommonResp<RespQueryBillDetail> resp = billRFService.queryDealBillDetail(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/downloadBillDetailListData.do")
	@RequiresPermissions("bill:billDetail:download")
	@SysLog(value="对账明细账单下载", isSaveResult=false)
	R downloadBillDetailListData(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("downloadBillDetailListData", reqParam, request);
		CommonResp<RespMap> resp = billRFService.downloadBillDetailListData(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/downloadExceptionBillData.do")
	@RequiresPermissions("bill:exceptionBill:download")
	@SysLog(value="异常账单明细下载", isSaveResult=false)
	R downloadExceptionBillData(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("downloadExceptionBillData", reqParam, request);
		CommonResp<RespMap> resp = billRFService.downloadExceptionBillData(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/billSynchro.do")
	@RequiresPermissions("bill:billDetail:update")
	@SysLog(value="异常账单-同步", isSaveResult=false)
	R billSynchro(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("checkBillSynchro", reqParam, request);
		CommonResp<RespMap> resp = billRFService.checkBillSynchro(new CommonReq<ReqSynchroBill>(new ReqSynchroBill(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/billReverse.do")
	@RequiresPermissions("bill:billDetail:update")
	@SysLog(value="商户单边账长款-账单冲正", isSaveResult=false)
	R billReverse(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.执行冲正操作
		InterfaceMessage msg = createInterfaceMsg("forceCorrectOrderBiz", reqParam, request);
		CommonResp<RespMap> respMap = orderService.forceCorrectOrderBiz(new CommonReq<ReqForceCorrectOrderBiz>(new ReqForceCorrectOrderBiz(msg, "billReverse")));
		JSONObject resultJson = respMap.toJSONResult();
		if(resultJson.getInteger("RespCode") < RetCode.Success.RET_10000.getCode()) {
			return R.error(resultJson.getInteger("RespCode"), resultJson.getString("RespMessage"));
		}
		//冲正业务完成
		//2.查询o_bizorder业务是否处理完成
		msg = createInterfaceMsg("queryBizState", reqParam, request);
		int bizState = orderService.queryBizState(new CommonReq<ReqSyncLocalOrderState>(
				new ReqSyncLocalOrderState(msg, null)));
		if(bizState != 0) {
			//3.处理完成则更新对账账单信息(处置后)
			msg = createInterfaceMsg("checkBillReverse", reqParam, request);
			CommonResp<RespMap> resp = billRFService.checkBillReverse(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
			return R.ok(resp.toJSONResult());
		}else {
			return R.error("冲正失败!");
		}
	}
	
	@PostMapping("/billRefund.do")
	@RequiresPermissions("bill:billDetail:update")
	@SysLog(value="长款-账单退款")
	R billRefund(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("validateUserPayKey", reqParam, request);
		//1.先验证二级支付密码是否正确
		CommonResp<RespMap> userPayKeyResp = sysUserService.validateUserPayKey(new CommonReq<ReqQueryUserKey>(new ReqQueryUserKey(msg)));
		JSONObject resultPayKeyJson = userPayKeyResp.toJSONResult();
		if(resultPayKeyJson.getInteger("RespCode") < RetCode.Success.RET_10000.getCode()) {
			return R.error("支付密码为空或输入错误!");
		}
		//2.执行退款操作
		msg = createInterfaceMsg("orderIsCancel", reqParam, request);
		CommonResp<RespMap> respMap = orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(new ReqOrderIsCancel(msg,"refund")));
		JSONObject resultJson = respMap.toJSONResult();
		if(resultJson.getInteger("RespCode") < RetCode.Success.RET_10000.getCode()) {
			return R.error(resultJson.getInteger("RespCode"), resultJson.getString("RespMessage"));
		}
		//退款成功
		JSONObject resultData = resultJson.getJSONArray("Data").getJSONObject(0);
		if(resultData != null && !resultData.isEmpty()) {
			//3.更新对账账单信息(处置后)
			String refundOrderId = resultData.getString("RefundOrderId");
			String refundNo = resultData.getString("RefundNo");
			msg = createInterfaceMsg("checkBillRefund", reqParam, request);
			CommonResp<RespMap> resp = billRFService.checkBillRefund(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg, refundOrderId, refundNo)));
			return R.ok("refundOrderId=====>" + refundOrderId + "refundNo=====>" + refundNo + resp.toJSONResult());
		}
		return R.error("退款失败!");
	}
	
	@PostMapping("/billCheckIn.do")
	@RequiresPermissions("bill:billDetail:update")
	@SysLog(value="短款-账单登账", isSaveResult=false)
	R billCheckIn(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("validateUserPayKey", reqParam, request);
		//1.先验证二级支付密码是否正确
		CommonResp<RespMap> userPayKeyResp = sysUserService.validateUserPayKey(new CommonReq<ReqQueryUserKey>(new ReqQueryUserKey(msg)));
		JSONObject resultPayKeyJson = userPayKeyResp.toJSONResult();
		if(resultPayKeyJson.getInteger("RespCode") < RetCode.Success.RET_10000.getCode()) {
			return R.error("支付密码为空或输入错误!");
		}
		//2.更新对账账单信息(处置后)
		msg = createInterfaceMsg("billCheckIn", reqParam, request);
		CommonResp<RespMap> resp = billRFService.billCheckIn(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg)));
		return R.ok(resp.toJSONResult());
	}

	/**
	 * 二维码扫码登账/退款(暂时不用)
	 * 
	 * @param opera
	 * @param reqParam
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/secondPwd.do")
	@RequiresPermissions("bill:billDetail:secondPwd")
	@SysLog(value="异常账单退款/登账操作", isSaveResult=true)
	void secondPwd(String opera, String[] reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = "https://open.weixin.qq.com/connect/qrconnect?appid=wxc89392b731d6f359&redirect_uri=https://wechatcenter.kasitesoft.com/login.html&response_type=code&scope=snsapi_login&state=STATE";
		String responseTxt = HttpRequestBus.create(url, RequestType.get).send().getResult();
		String strS = responseTxt.substring(responseTxt.indexOf("uuid=")+5, responseTxt.length());
		String uuid = (strS.substring(0, strS.indexOf("+")-1));
		StringBuilder paramSB = new StringBuilder("");
		if(reqParam != null && reqParam.length > 0) {
			for (String str : reqParam) {
				paramSB.append(str + ",");
			}
		}
		if(paramSB.length() > 0) {
			paramSB.deleteCharAt(paramSB.length() - 1);
		}
		String token = getRequestToken(request);
		String secondHtml = this.buildSecondHtml(token, opera, paramSB.toString(), reqParam[0]).replaceAll("_UUID", uuid);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Content-Type","javascript");
		response.setHeader("Cache-Control", "no-cache"); 
		response.setHeader("Content-Type","text/html; charset=utf-8");
		response.getOutputStream().write(secondHtml.getBytes("UTF-8"));
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	}
	
	/**
	 * 获取二级密码支付页面 含二维码(暂时不用)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/qrconnect.do")
	void qrconnect(String uuid, String last, String opera, String reqParam,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] paramArr = reqParam.split(",");
		String billCheckId = paramArr[0];
		String orderId = "";
		Integer price = 0;
		Integer refundPrice = 0;
		String reason = "";
		String channelId = "";
		String outRefundOrderId = "";
		Integer billSingleType = 0;
		if("refund".equals(opera)) {
			orderId = paramArr[1];
			price = StringUtils.isBlank(paramArr[2])?0:new Integer(paramArr[2]);
			refundPrice = paramArr[3] == null?0:new Integer(paramArr[3]);
			reason = paramArr[4];
			channelId = paramArr[5];
			outRefundOrderId = paramArr.length==7?paramArr[6]:"";
		}else {
			billSingleType = paramArr[1]==null?0:new Integer(paramArr[1]);
		}
		String url = "https://long.open.weixin.qq.com/connect/l/qrconnect?uuid="+uuid+"&_="+System.currentTimeMillis();
		String responseTxt = HttpRequestBus.create(url, RequestType.get).send().getResult();
		//返回状态 = 405 并且有code的时候 去中心取用户的openId==》返回对应的 openid，手机号，姓名
		//没有手机号的时候,不能继续操作
	    String[] strs = responseTxt.split(";");
	    String statusCode = strs[0].split("=")[1];
	    String code = strs[1].split("=")[1];
		if("405".equals(statusCode)) {
			//获取微信openId 
			String authCode = code.substring(1, code.length()-1);
			url = "https://wechatcenter.kasitesoft.com/weixin/100123/WXKASIETOPEN/userinfo.do?authCode="+authCode;
			String userInfo = HttpRequestBus.create(url, RequestType.post)
					.addHttpParam("orgCode", KasiteConfig.getOrgCode())
					.addHttpParam("appId", KasiteConfig.getAppId())
					.addHttpParam("orgName", KasiteConfig.getOrgName())
					.send().getResult();
			JSONObject json = JSONObject.parseObject(userInfo);
			Integer respCode = json.getInteger("RespCode");
			if(KstHosConstant.SUCCESS_CODE.equals(respCode)) {
				JSONObject userJson = json.getJSONObject("result");
				//开放平台所有用户openId都转为 unionid
				String openId = userJson.getString("unionid");
				//获取用户的基本信息
				SysUserEntity user = sysUserService.queryByUserName(openId);
				SysUserRoleEntity userRole = sysUserService.queryUserRoleById(user.getId());
				if(userRole != null && userRole.getRoleId() == 1016) {
					if("refund".equals(opera)) {
						//1.执行退款操作
						InterfaceMessage msg = createInterfaceMsg("orderIsCancel", reqParam, request);
						msg.setParamType(2);
						CommonResp<RespMap> respMap = orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(new ReqOrderIsCancel(msg, 
								orderId, price, refundPrice, null, null, channelId, reason, outRefundOrderId)));
						JSONObject refundJson = respMap.toJSONResult();
						if(RetCode.Success.RET_10000.getCode().equals(refundJson.getInteger("RespCode"))) {
							//退款成功
							JSONObject resultData = refundJson.getJSONArray("Data").getJSONObject(0);
							if(resultData != null && !resultData.isEmpty()) {
								//2.更新对账账单信息(处置后)
								String refundOrderId = resultData.getString("RefundOrderId");
								String refundNo = resultData.getString("RefundNo");
								msg = createInterfaceMsg("checkBillRefund", reqParam, request);
								msg.setParamType(2);
								CommonResp<RespMap> resp = billRFService.checkBillRefund(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg, 
										billCheckId, orderId, billSingleType, refundOrderId, refundNo)));
								JSONObject resultJson = resp.toJSONResult();
								if(RetCode.Success.RET_10000.getCode().equals(resultJson.getInteger("RespCode"))) {
									responseTxt = responseTxt + "window.hisBack=true;";
								}else {
									responseTxt = resp.toResult() + "window.hisBack=true;";
								}
							}
						}else {
							responseTxt = respMap.toResult() + "window.hisBack=true;";
						}
					}else {
						//更新对账账单信息(处置后)
						InterfaceMessage msg = createInterfaceMsg("billCheckIn", reqParam, request);
						msg.setParamType(2);
						CommonResp<RespMap> resp = billRFService.billCheckIn(new CommonReq<ReqQueryBill>(new ReqQueryBill(msg, 
								billCheckId, orderId, billSingleType, null, null)));
						JSONObject resultJson = resp.toJSONResult();
						if(RetCode.Success.RET_10000.getCode().equals(resultJson.getInteger("RespCode"))) {
							responseTxt = responseTxt + "window.hisBack=true;";
						}else {
							responseTxt = resp.toResult() + "window.hisBack=true;";
						}
					}
				}
			}
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Content-Type","javascript");
		response.setHeader("Cache-Control", "no-cache");  
	    response.getOutputStream().write(responseTxt.getBytes());
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	}
	
	/**
	 * 构建退款二维码页面
	 * 
	 * @param opera
	 * @param reqParam
	 * @param billId
	 * @return
	 */
	private String buildSecondHtml(String token, String opera, String reqParam, String billId) {
		String operType = "refund".equals(opera)?"退款":"登账";
		String secondPwdHtml = " <!DOCTYPE html>" + 
				"<html>" + 
				"	<head>" + 
				"		<title>微信扫码"+operType+"</title>" + 
				"		<meta charset=\"utf-8\">		"+
				"		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" + 
				"		<link rel=\"stylesheet\" href=\"/module/backstage/login/wechat/css/impowerApp.css\">" + 
				"		<link rel=\"stylesheet\" href=\"/module/backstage/commons/css/artDialog.css\" type=\"text/css\" />"+
				"		<script src=\"/module/backstage/commons/js/jquery-1.8.3.min.js\"></script>" + 
				"		<script src=\"/module/backstage/commons/js/jquery.artDialog.js\"></script>" + 
				"		<script src=\"/module/backstage/commons/js/common.js\"></script>" + 
				"	</head>" + 
				"	<body>" + 
				"		<div class=\"main impowerBox\">" + 
				"			<div class=\"loginPanel normalPanel\">" + 
				"				<div class=\"title\">微信扫码"+operType+"</div>" + 
				"				<div class=\"waiting panelContent\">" + 
				"					<div class=\"wrp_code\"><img class=\"qrcode lightBorder\" src=\"/backstage/_UUID/connectQrcode.do\" /></div>" + 
				"					<div class=\"info\">" + 
				"						<div class=\"status status_browser js_status\" id=\"wx_default_tip\">" + 
				"			                <p>请使用微信扫描二维码操作"+operType+"</p>" + 
				"                            <p>“卡思特智付中心”</p>" + 
				"			            </div>" + 
				"			            <div class=\"status status_succ js_status\" style=\"display:none\" id=\"wx_after_scan\">" + 
				"			                <i class=\"status_icon icon38_msg succ\"></i>" + 
				"			                <div class=\"status_txt\">" + 
				"			                    <h4>扫描成功</h4>" + 
				"			                    <p>请在微信中点击确认即可操作"+operType+"</p>" + 
				"			                </div>" + 
				"			            </div>" + 
				"			            <div class=\"status status_fail js_status\" style=\"display:none\" id=\"wx_after_cancel\">" + 
				"			                <i class=\"status_icon icon38_msg warn\"></i>" + 
				"			                <div class=\"status_txt\">" + 
				"			                    <h4>您已取消此次操作</h4>" + 
				"			                    <p>您可再次扫描操作，或关闭窗口</p>" + 
				"			                </div>" + 
				"			            </div>" + 
				"			        </div>" + 
				"				</div>" + 
				"			</div>" + 
				"		</div>" + 
				"        <script>" + 
				"function AQ_SECAPI_ESCAPE(a,b){for(var c=new Array,d=0;d<a.length;d++)if(\"&\"==a.charAt(d)){var e=[3,4,5,9],f=0;for(var g in e){var h=e[g];if(d+h<=a.length){var i=a.substr(d,h).toLowerCase();if(b[i]){c.push(b[i]),d=d+h-1,f=1;break}}}0==f&&c.push(a.charAt(d))}else c.push(a.charAt(d));return c.join(\"\")}function AQ_SECAPI_CheckXss(){for(var a=new Object,b=\"'\\\"<>`script:daex/hml;bs64,\",c=0;c<b.length;c++){for(var d=b.charAt(c),e=d.charCodeAt(),f=e,g=e.toString(16),h=0;h<7-e.toString().length;h++)f=\"0\"+f;a[\"&#\"+e+\";\"]=d,a[\"&#\"+f]=d,a[\"&#x\"+g]=d}a[\"&lt\"]=\"<\",a[\"&gt\"]=\">\",a[\"&quot\"]='\"';var i=location.href,j=document.referrer;i=decodeURIComponent(AQ_SECAPI_ESCAPE(i,a)),j=decodeURIComponent(AQ_SECAPI_ESCAPE(j,a));var k=new RegExp(\"['\\\"<>`]|script:|data:text/html;base64,\");if(k.test(i)||k.test(j)){var l=\"1.3\",m=\"http://zyjc.sec.qq.com/dom\",n=new Image;n.src=m+\"?v=\"+l+\"&u=\"+encodeURIComponent(i)+\"&r=\"+encodeURIComponent(j),i=i.replace(/['\\\"<>`]|script:/gi,\"\"),i=i.replace(/data:text\\/html;base64,/gi,\"data:text/plain;base64,\"),location.href=i}}AQ_SECAPI_CheckXss();" + 
				"</script>" + 
				"        <script>" + 
				"!function(){function a(d){jQuery.ajax({type:\"GET\",url:\"/bill/qrconnect.do?token="+token+"&opera="+opera+"&reqParam="+reqParam+"&uuid=_UUID\"+(d?\"&last=\"+d:\"\"),dataType:\"script\",cache:!1,timeout:6e4,success:function(d,e,f){var g=window.wx_errcode;var url=window.url; var isSucess = window.hisBack; if(isSucess){Commonjs.alert('"+operType+"成功!');url = Commonjs.getBackUrl('"+billId+"',true);}else {Commonjs.alert('您没有"+operType+"权限,"+operType+"失败!');url = Commonjs.getBackUrl('"+billId+"',false);} switch(g){case 405:var h=url;h=h.replace(/&amp;/g,\"&\"),h+=(h.indexOf(\"?\")>-1?\"&\":\"?\")+\"code=\"+wx_code+\"&state=STATE\";var i=b(\"self_redirect\");if(c)if(\"true\"!==i&&\"false\"!==i)try{document.domain=\"qq.com\";var j=window.location.host.toLowerCase();j&&(window.location.href=h)}catch(k){window.location.href=h}else if(\"true\"===i)try{window.location.href=h}catch(k){window.location.href=h}else window.location.href=h;else window.location.href=h;break;case 404:jQuery(\".js_status\").hide(),jQuery(\"#wx_after_scan\").show(),setTimeout(a,100,g);break;case 403:jQuery(\".js_status\").hide(),jQuery(\"#wx_after_cancel\").show(),setTimeout(a,2e3,g);break;case 402:case 500:window.location.reload();break;case 408:setTimeout(a,2e3)}},error:function(b,c,d){var e=window.wx_errcode;408==e?setTimeout(a,5e3):setTimeout(a,5e3,e)}})}function b(a,b){b||(b=window.location.href),a=a.replace(/[\\[\\]]/g,\"\\\\$&\");var c=new RegExp(\"[?&]\"+a+\"(=([^&#]*)|&|#|$)\"),d=c.exec(b);return d?d[2]?decodeURIComponent(d[2].replace(/\\+/g,\" \")):\"\":null}var c=window.top!=window;if(c){var d=\"\";\"white\"!=d&&(document.body.style.color=\"#373737\")}else{document.getElementsByClassName||(document.getElementsByClassName=function(a){for(var b=[],c=new RegExp(\"(^| )\"+a+\"( |$)\"),d=document.getElementsByTagName(\"*\"),e=0,f=d.length;f>e;e++)c.test(d[e].className)&&b.push(d[e]);return b}),document.body.style.backgroundColor=\"#333333\",document.body.style.padding=\"50px\";for(var e=document.getElementsByClassName(\"status\"),f=0,g=e.length;g>f;++f){var h=e[f];h.className=h.className+\" normal\"}}var i=\"\";if(i){var j=document.createElement(\"link\");j.rel=\"stylesheet\",j.href=i.replace(new RegExp(\"javascript:\",\"gi\"),\"\"),document.getElementsByTagName(\"head\")[0].appendChild(j)}setTimeout(a,100)}();" + 
				"</script>" + 
				"	</body>" + 
				"</html>";
		
		return secondPwdHtml;
	}
}
