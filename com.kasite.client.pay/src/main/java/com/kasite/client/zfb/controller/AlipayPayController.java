package com.kasite.client.zfb.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.client.zfb.service.AlipayService;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsPayment;
import com.kasite.core.serviceinterface.module.pay.IPayService;
import com.kasite.core.serviceinterface.module.pay.req.ReqUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.RespUniteOrder;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;


/**
 * @author linjianfa
 * @Description: TODO 支付宝支付类
 * @version: V1.0  
 * 2017-7-17 下午2:57:03
 */
@Controller
@RequestMapping(value="/alipay/{clientId}/{configKey}")
public class AlipayPayController extends AbstractController{

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY_ALI);
	@Autowired
	private IPayService payService;
	@Autowired
	PayBackCallUtil payBackCallUtil;
	/**
	 * 支付下单接口
	 * @param clientId
	 * @param configKey
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/tradeWapPay.do")
	@ResponseBody
	@SysLog(value="alipay_tradeWapPay")
	public void tradeWapPay(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String fullPath =  KasiteConfig.getKasiteHosWebAppUrl();
		if(StringUtil.isBlank(configKey)) {
			try {
				sendRedirectToErrorPage(-10000, "该渠道暂未开通支付宝支付。", response);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log, e);
			}
			return ;
		}
		SysUserEntity user = getUser();
		String orderId = request.getParameter("orderId");
		String authCode = request.getParameter("authCode");
		String priceName =  request.getParameter("priceName");
		String toUrl = request.getParameter("toUrl");
		
		AuthInfoVo authInfo = createAuthInfoVo(request);
		authInfo.setConfigKey(configKey);
		if(StringUtils.isBlank(orderId)){
			try {
				sendRedirect(response,fullPath+getErrorUrl(-10000,"订单id不能为空。"));
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log, e);
			}
		}
		
		if(StringUtil.isBlank(toUrl)){
			toUrl = "/business/pay/paySuccess.html?clientId="+clientId+"&orderId=" + orderId;
		}
		//判断是否有个性化回调地址跳转 如果没有就走默认收银台如果有的话跳转到指定页面
		String diyToUrl = KasiteConfig.getClientConfig(ClientConfigEnum.paySuccessToUrl, clientId);
		if(StringUtil.isNotBlank(diyToUrl)) {
			toUrl = diyToUrl;
		}
		try {
			String sp = "/";
			StringBuffer sbf = new StringBuffer();	
			sbf.append(fullPath)
			.append("/alipay/")
			.append(clientId).append(sp)
			.append(configKey).append(sp)
			.append("payCallBack.do?orderId=").append(orderId).append("&token=").append(authInfo.getSessionKey()).append("&toUrl=").append(fullPath).append(toUrl);
			toUrl = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		获取openid
//		String openid = CookieTool.getCookieValue(request, KstHosConstant.COMM_OPENID);
		String openid = user.getUsername();
		
		if(StringUtils.isBlank(openid)){
			if( StringUtils.isBlank(authCode) ) {
				try {
					sendRedirect(response,fullPath+getErrorUrl(-10000,"获取openid失败。"));
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log, e);
				}
				return ;
			}else {
				try {
					AlipaySystemOauthTokenResponse tokenResponse = AlipayService.getAuthTokenFromAlipay(configKey,authCode);
					if(tokenResponse.isSuccess()) {
						openid = tokenResponse.getUserId();
					}else {
						try {
							sendRedirect(response,fullPath+getErrorUrl(-10000,"获取openid失败。"));
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.error(log, e);
						}
						return ;
					}
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log, e);
				}finally {
					if(StringUtils.isBlank(openid)){
						try {
							sendRedirect(response,fullPath+getErrorUrl(-10000,"获取openid失败。"));
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.error(log, e);
						}
						return ;
					}
				}
			}
		}
		
//		String productPrice = this.getOrderPrice(authInfo,orderId,request);if(productPrice==null || "".equals(productPrice)){
//			response.sendRedirect(fullPath+"/business/commonError.html?errorCode=-10000&errorMsg=订单不存在!");
//		}
		Document document = DocumentHelper.createDocument();
		Element req = document.addElement(KstHosConstant.REQ);
		XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "6003");
		Element dataEl = req.addElement(KstHosConstant.DATA);
		XMLUtil.addElement(dataEl, "OrderId", orderId);
		XMLUtil.addElement(dataEl, "Body", priceName);
		XMLUtil.addElement(dataEl, "Subject", priceName);
		XMLUtil.addElement(dataEl, "OpenId", openid);
		XMLUtil.addElement(dataEl, "ChannelId", KstHosConstant.ZFB_CHANNEL_ID);
//		XMLUtil.addElement(dataEl, "Price", productPrice);
		XMLUtil.addElement(dataEl, "ReturnUrl", toUrl);
		try {
			InterfaceMessage msg = createInterfaceMsg(ApiModule.Pay.WapUniteOrder, req.asXML(), authInfo);
			String result = payService.WapUniteOrder(msg);
			response.setContentType("text/html;charset=" + "UTF-8");
			//直接将完整的表单html输出到页面
			log.info("result："+result);
		    response.getWriter().write(result);
		    response.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e,authInfo);
			sendRedirect(response,fullPath+"/business/commonError.html?errorCode=-14444&errorMsg=接口[UniteOrder]调用异常");
		}
	}	
	
	/**
	 * 前端支付调用成功回调
	 * @throws Exception 
	 */
	@RequestMapping(value="/payCallBack.do")
	@ResponseBody
	@SysLog(value="alipay_payCallBack")
	public void payCallBack(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		String orderId = request.getParameter("orderId");
		String toUrl = request.getParameter("toUrl");
		AuthInfoVo authInfo = createAuthInfoVo(request);
		authInfo.setConfigKey(configKey);
		CommonResp<RespMap> resp = null;
		if(null != authInfo) {
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
		}
		payCallBack(clientId, configKey, orderId, toUrl, authInfo, resp, request, response);
	}
	
	/**
	 * 调用统一支付接口
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getUnitePay.do")
	@ResponseBody
	@SysLog(value="alipay_getUnitePay")
	public String getUnitePay(
			@PathVariable("clientId") String clientId,
			@PathVariable("configKey") String configKey,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		AuthInfoVo authInfo = null;
		// 获取openid
		String openid = null;
		String uuid = IDSeed.next();
//		String configKey = KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, clientId);
		if(StringUtil.isBlank(configKey)) {
			obj.put("RespCode", "-10000");
			obj.put("RespMessage", "该渠道不支持线上支付，如需要开通请联系管理员。");
			return obj.toString();
		}
		try {
			String orderId = request.getParameter("orderId");
			String priceName = request.getParameter("priceName");
			String code = request.getParameter("code");
			if (StringUtils.isBlank(orderId)) {
				obj.put("RespCode", "-10000");
				obj.put("RespMessage", "订单id不能为空");
				return obj.toString();
			}
			SysUserEntity user = getUser();
			if(null != user) {
				authInfo = createAuthInfoVo(uuid,request);
				authInfo.setConfigKey(configKey);
				// 获取openid
				openid = user.getUsername();
			}
			/** 如果openId为空 **/
			if (StringUtils.isBlank(openid)) {
				if (StringUtils.isBlank(code)) {
					obj.put("RespCode", "-10000");
					obj.put("RespMessage", "获取openid失败");
					return obj.toString();
				} else {
					/** 如果code不为空，则取accessTokenJson **/
					AlipaySystemOauthTokenResponse tokenResponse = AlipayService.getAuthTokenFromAlipay(configKey, code);
					openid = tokenResponse.getUserId();
					if (!StringUtil.isEmpty(openid)) {
						authInfo = login(uuid,false,openid, clientId, configKey, null, request, response);
					} else {
						obj.put("RespCode", "-10000");
						obj.put("RespMessage", "获取openid失败");
						return obj.toString();
					}
				}
			}
			if(null == authInfo) {
				obj.put("RespCode", "-10000");
				obj.put("RespMessage", "获取openid失败");
				return obj.toString();
			}
			authInfo.setConfigKey(configKey);
//			// 根据订单orderId获取相关信息
			InterfaceMessage msg = createInterfaceMsg(ApiModule.Pay.UniteOrder, "", authInfo);
			ReqUniteOrder t = new ReqUniteOrder(msg, orderId, null, priceName, priceName, openid, clientId, null, 1, null, null);
			t.setOrderId(orderId);
			t.setBody(priceName);
			t.setSubject(priceName);
			t.setOpenId(openid);
			t.setChannelId(clientId);
			CommonReq<ReqUniteOrder> commReq = new CommonReq<ReqUniteOrder>(t);
			/*
			 * Common:商户统一下单
			 * @Description 商户统一下单
			 */
			CommonResp<RespUniteOrder> resp = payService.uniteOrder(commReq);
			if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
				RespUniteOrder uniteOrder = resp.getDataCaseRetCode();
				KasiteConfig.print("reJson==" + uniteOrder.getContent());
				obj.put("RespCode", "10000");
				obj.put("RespMessage", "成功");
				obj.put("Data", uniteOrder.getContent());
			}else {
				obj.put("RespCode", resp.getCode());
				obj.put("RespMessage", resp.getMessage());
			}
			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			throw e;
		}
	}

}
