package com.kasite.client.netpay.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.util.IPUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsPayment;
import com.kasite.core.serviceinterface.module.pay.IPayService;
import com.kasite.core.serviceinterface.module.pay.req.ReqWapUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.RespWapUniteOrder;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;


/**
 * @author linjf
 * 招行一网通控制类
 */
@Controller
@RequestMapping(value = "/netPay/{clientId}/{configKey}")
public class NetPayController extends AbstractController {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY_WX);

	@Autowired
	private IPayService payService;
	
	@Autowired
	PayBackCallUtil payBackCallUtil;
	
	@RequestMapping(value = "/getWapUnitePay.do")
	@SysLog(value="netpay_getWapUnitePay")
	public void getWapUnitePay(
			@PathVariable("clientId") String clientId,
			@PathVariable("configKey") String configKey,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String hosWebAppUrl =  KasiteConfig.getKasiteHosWebAppUrl();
		AuthInfoVo authInfo = null;
		String openid = null;
		try {
			SysUserEntity user = getUser();
			if(null != user) {
				authInfo = createAuthInfoVo(IDSeed.next(),request);
				authInfo.setConfigKey(configKey);
				// 获取openid
				openid = user.getUsername();
			}
			if (StringUtils.isBlank(openid)) {
					throw new RRException(RetCode.Common.ERROR_SYSTEM,"获取openid为空");
			}
			
			String orderId = request.getParameter("orderId");
			String priceName = request.getParameter("priceName");
			String toUrl = request.getParameter("toUrl");
			if(StringUtil.isBlank(toUrl)){
				toUrl = "/business/pay/paySuccess.html?clientId="+clientId+"&orderId=" + orderId;
			}
			StringBuffer returnUrl = new StringBuffer();	
			try {
				String sp = "/";
				returnUrl.append(hosWebAppUrl)
				.append("/netPay/")
				.append(clientId).append(sp)
				.append(configKey).append(sp)
				.append("payCallBack.do?orderId=").append(orderId).append("&token=").append(authInfo.getSessionKey()).append("&toUrl=").append(hosWebAppUrl).append(toUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			InterfaceMessage msg = createInterfaceMsg(ApiModule.Pay.WapUniteOrder, "", authInfo);
			ReqWapUniteOrder t = new ReqWapUniteOrder(msg, orderId, null,
					priceName, priceName, openid,
					clientId, IPUtils.getIpAddr(request), null, 
					returnUrl.toString(), null);
			JSONObject obj = new JSONObject();
			CommonResp<RespWapUniteOrder> resp = payService.wapUniteOrder(new CommonReq<ReqWapUniteOrder>(t));
			if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
				RespWapUniteOrder uniteOrder = resp.getDataCaseRetCode();
				obj.put("RespMessage", "成功");
				response.getWriter().write(uniteOrder.getPayInfo());
			}else {
				obj.put("RespCode", resp.getCode());
				obj.put("RespMessage", resp.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			throw e;
		}
	}
	
	/**
	 * 前端支付调用成功回调
	 * @throws Exception 
	 */
	@RequestMapping(value="/payCallBack.do")
	@ResponseBody
	@SysLog(value="netpay_payCallBack")
	public void payCallBack(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		String orderId = request.getParameter("orderId");
		String toUrl = request.getParameter("toUrl");
		String merchantReturnUrlParam = request.getParameter("MerchantReturnUrlParam");
		AuthInfoVo authInfo = createAuthInfoVo(request);
		authInfo.setConfigKey(configKey);
		CommonResp<RespMap> resp = null;
		if(null != authInfo && !StringUtil.isEmpty(merchantReturnUrlParam)) {
			try {
				//支付通知:前端页面支付通知 直接调用订单接口的 OrderIsPayment 保存状态 订单正在支付
				IOrderService orderService = SpringContextUtil.getBean(IOrderService.class);
				if(null != orderService) {
					InterfaceMessage msg = createInterfaceMsg(ApiModule.Order.OrderIsPayment, null, authInfo);
					ReqOrderIsPayment t = new ReqOrderIsPayment(msg, orderId, authInfo.getSign(),  authInfo.getSign(), clientId, null, null);
					CommonReq<ReqOrderIsPayment> req = new CommonReq<ReqOrderIsPayment>(t);
					resp = orderService.orderIsPayment(req);
					
					payBackCallUtil.addMerchantOrderCheck(t.getAuthInfo(), orderId, null, KstHosConstant.I0, configKey, clientId);
					LogUtil.info(log, resp.toResult(),authInfo);
				}else {
					LogUtil.info(log, "未定义订单处理类",authInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log, e,authInfo);
			}
		}else {
			toUrl = "/business/pay/payError.html";
		}
		payCallBack(clientId, configKey, orderId, toUrl, authInfo, resp, request, response);
	}
	
	@RequestMapping(value = "/getDirectWapUnitePay.do")
	@SysLog(value="netpay_getDirectWapUnitePay")
	public void getDirectWapUnitePay(
			@PathVariable("clientId") String clientId,
			@PathVariable("configKey") String configKey,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AuthInfoVo authInfo = null;
		String openId = request.getParameter("openId");
		String returnUrl = request.getParameter("returnUrl");
		try {
			authInfo = createAuthInfoVo(IDSeed.next(),request);
			authInfo.setConfigKey(configKey);
			// 获取openid
			if (StringUtils.isBlank(openId)) {
					throw new RRException(RetCode.Common.ERROR_SYSTEM,"入参openId为空");
			}
			
			String orderId = request.getParameter("orderId");
			String priceName = request.getParameter("priceName");
			InterfaceMessage msg = createInterfaceMsg(ApiModule.Pay.WapUniteOrder, "", authInfo);
			ReqWapUniteOrder t = new ReqWapUniteOrder(msg, orderId, null,
					priceName, priceName, openId,
					clientId, IPUtils.getIpAddr(request), null, 
					returnUrl.toString(), null);
			JSONObject obj = new JSONObject();
			CommonResp<RespWapUniteOrder> resp = payService.wapUniteOrder(new CommonReq<ReqWapUniteOrder>(t));
			if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
				RespWapUniteOrder uniteOrder = resp.getDataCaseRetCode();
				obj.put("RespMessage", "成功");
				response.getWriter().write(uniteOrder.getPayInfo());
			}else {
				obj.put("RespCode", resp.getCode());
				obj.put("RespMessage", resp.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			throw e;
		}
	}
}
