package com.kasite.client.wechat.controller;

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
import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
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
import com.kasite.core.serviceinterface.module.pay.req.ReqUniteOrder;
import com.kasite.core.serviceinterface.module.pay.req.ReqWapUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.RespUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.RespWapUniteOrder;
import com.yihu.hos.util.JSONUtil;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;

/**
 * @author linjianfa
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0 2017-7-17 下午2:57:03
 */
@Controller
@RequestMapping(value = "/wxPay/{clientId}/{configKey}")
public class WxPayController extends AbstractController {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY_WX);

	@Autowired
	private IPayService payService;
	
	@Autowired
	PayBackCallUtil payBackCallUtil;
	
	/**
	 * 调用统一支付接口
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getUnitePay.do")
	@ResponseBody
	@SysLog(value="wechat_getUnitePay")
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
					JSONObject accessTokenJson = WeiXinService.getAuthToken(code, configKey);
					openid = JSONUtil.getJsonString(accessTokenJson, "openid");
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

	/**
	 * 前端支付调用成功回调
	 * @throws Exception 
	 */
	@RequestMapping(value = "/payCallBack.do")
	@ResponseBody
	@SysLog(value="wechat_payCallBack")
	public void payCallBack(@PathVariable("clientId") String clientId,
			@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String orderId = request.getParameter("orderId");
		String toUrl = request.getParameter("toUrl");
		SysUserEntity user = getUser();
		String openid = user.getUsername();
		String channelId = clientId;
		AuthInfoVo authInfo = createAuthInfoVo(request);
		authInfo.setConfigKey(configKey);
		LogUtil.info(log, toUrl + "||openId=" + openid + "||orderId=" + orderId, authInfo);
		InterfaceMessage msg = createInterfaceMsg(ApiModule.Order.OrderIsPayment, null, authInfo);
		CommonResp<RespMap> resp = null;
		try {
			//支付通知:前端页面支付通知 直接调用订单接口的 OrderIsPayment 保存状态 订单正在支付
			IOrderService orderService = SpringContextUtil.getBean(IOrderService.class);
			if(null != orderService) {
				ReqOrderIsPayment t = new ReqOrderIsPayment(msg, orderId, authInfo.getSign(),  authInfo.getSign(), channelId, null, null);
				CommonReq<ReqOrderIsPayment> req = new CommonReq<ReqOrderIsPayment>(t);
				resp = orderService.orderIsPayment(req);
				//写入商户订单确认表，由MerchantOrderCheckJob主动查询商户订单。
				payBackCallUtil.addMerchantOrderCheck(t.getAuthInfo(), orderId, null, KstHosConstant.I0, configKey, clientId);
				LogUtil.info(log, resp.toResult(),authInfo);
			}else {
				LogUtil.info(log, "未定义订单处理类",authInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e,authInfo);
		}
		payCallBack(clientId, configKey, orderId, toUrl, authInfo, resp, request, response);
	}
	
	@RequestMapping(value = "/getWapUnitePay.do")
	@ResponseBody
	@SysLog(value="wechat_getWapUnitePay")
	public String getWapUnitePay(
			@PathVariable("clientId") String clientId,
			@PathVariable("configKey") String configKey,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AuthInfoVo authInfo = null;
		try {
			SysUserEntity user = getUser();
			if(null != user) {
				authInfo = createAuthInfoVo(IDSeed.next(),request);
				authInfo.setConfigKey(configKey);
				// 获取openid
				//openid = user.getUsername();
			}
			
			String orderId = request.getParameter("orderId");
			String priceName = request.getParameter("priceName");
			InterfaceMessage msg = createInterfaceMsg(ApiModule.Pay.WapUniteOrder, "", authInfo);
			ReqWapUniteOrder t = new ReqWapUniteOrder(msg, orderId, null,
					priceName, priceName, null,
					clientId, IPUtils.getIpAddr(request), null, 
					null, null);
			JSONObject obj = new JSONObject();
			CommonResp<RespWapUniteOrder> resp = payService.wapUniteOrder(new CommonReq<ReqWapUniteOrder>(t));
			if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
				RespWapUniteOrder uniteOrder = resp.getDataCaseRetCode();
				KasiteConfig.print("reJson==" + uniteOrder.getPayInfo());
				obj.put("RespCode", "10000");
				obj.put("RespMessage", "成功");
				obj.put("Data", uniteOrder.getPayInfo());
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

// 	2019年1月2日 15:06:18 百度小程序调用微信H5支付demo，过期可删除	
//	@RequestMapping(value = "/getTestWapUnitePay.do")
//	@SysLog(value="wechat_getUnitePay")
//	public  void getTestWapUnitePay(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		try {
//			// 随机数
//			SortedMap<String, String> paramMap = new TreeMap<String, String>();
//			paramMap.put("appid", "wx37fc28118aba7599");
//			paramMap.put("mch_id", "1427867702");
//			paramMap.put("sub_appid", "wx53e1cfb23f11f0b0");//微信分配的子商户公众账号ID，如需在支付完成后获取sub_openid则此参数必传。
//			paramMap.put("sub_mch_id", "1310409501");//
//			//paramMap.put("device_info", "");//终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
//			paramMap.put("nonce_str", StringUtil.getUUID());//随机字符串
//			paramMap.put("body", "测试");//商品描述
//			//paramMap.put("detail", "");//商品详情
//			paramMap.put("attach","测试");//附加数据
//			paramMap.put("out_trade_no", StringUtil.getUUID());//商户订单号
//			paramMap.put("fee_type", "CNY");//标价币种
//			paramMap.put("total_fee", 1+"");//标价金额
//			paramMap.put("spbill_create_ip", getIPAddress(request));//终端IP
//			//paramMap.put("time_start", "");//交易起始时间
//			//paramMap.put("time_expire", "");//交易结束时间
//			//paramMap.put("goods_tag", "");//订单优惠标记
//			paramMap.put("notify_url", "https://cs001.kasitesoft.com/notfiy.do");//通知地址
//			paramMap.put("trade_type", "MWEB");//交易类型
//			paramMap.put("limit_pay", "no_credit");//指定支付方式
//			com.alibaba.fastjson.JSONObject scene_info = new com.alibaba.fastjson.JSONObject();
//			com.alibaba.fastjson.JSONObject h5_info = new com.alibaba.fastjson.JSONObject();
//			h5_info.put("type", "Android");
//			h5_info.put("app_name", "卡思特测试APP");
//			h5_info.put("package_name", "com.kasite.client.wechat.controller");
//			scene_info.put("h5_info", h5_info);
//			
//			paramMap.put("scene_info",scene_info.toJSONString());//场景信息
//			//paramMap.put("sub_openid",);//场景信息
//			paramMap.put("sign_type", "MD5");//签名类型
//			String sign = createSign(paramMap);
//			paramMap.put("sign", sign);//签名
//			//拼装最终入参字符串
//			String param = toXML("xml", paramMap);
//			// 根据订单获取支付相关信息（调用支付统一接口）
//			String unifiedOrderResult = HttpsClientUtils.httpsPost(ApiModule.WeChat.pay_unifiedorder,
//					TenpayConstant.UNIFIEDORDER_URL, param);
//			com.alibaba.fastjson.JSONObject unifiedOrderRetJson = com.alibaba.fastjson.JSONObject.parseObject(TenpayUtil.xml2JSON(unifiedOrderResult));
//			JSONObject retJson = new JSONObject();
//			String returnCode = unifiedOrderRetJson.getString(TenpayConstant.RETURN_CODE);
//			String resultCode = unifiedOrderRetJson.getString(TenpayConstant.RESULT_CODE);
//			String returnMsg = unifiedOrderRetJson.getString(TenpayConstant.RETURN_MSG);
//			retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
//			retJson.put(KstHosConstant.RESPMESSAGE,
//					"微信统一下单返回结果：returnCode:" + returnCode + "|resultCode:" + resultCode + "|returnMsg:" + returnMsg);
//			if (!StringUtil.isEmpty(returnCode) && TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)
//					&& !StringUtil.isEmpty(resultCode) && TenpayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
//				 
//				String prepayId = unifiedOrderRetJson.getString("prepay_id");
//				String mwebUrl = unifiedOrderRetJson.getString("mweb_url");
//				String tradeType = unifiedOrderRetJson.getString("trade_type");
//				// 返回给前端的数据集
//				JSONObject data = new JSONObject();
//				data.put("trade_type", tradeType);
//				data.put("prepay_id", prepayId);
//				data.put("mweb_url", mwebUrl+"&redirect_url="+URLEncoder.encode("https://cs001.kasitesoft.com/business/pay/paySuccess.html"));
//				retJson.put(KstHosConstant.DATA, data.toString());
//				response.getWriter().write(retJson.toString());
//				//return retJson.toString(); 
//				//response.setHeader("referer", "https://cs001.kasitesoft.com");
//				//response.sendRedirect(mwebUrl+"&redirect_url="+URLEncoder.encode("https://cs001.kasitesoft.com/pay/paySuccess.html"));
////				response.getWriter().write( 
////						"<html><head><script type=\"text/javascript\">"
////						+ "location.href='"+mwebUrl+"&redirect_url="+URLEncoder.encode("https://cs001.kasitesoft.com/business/pay/paySuccess.html")+"'"
////						+ "</script> </head><body></body></html>");
//				data.put("mweb_url", mwebUrl+"&redirect_url="+URLEncoder.encode("https://cs001.kasitesoft.com/pay/paySuccess.html"));
//				retJson.put(KstHosConstant.DATA, data.toString());
//				//return retJson.toString();
//				//response.setHeader("referer", "https://cs001.kasitesoft.com");
//				//response.sendRedirect(mwebUrl+"&redirect_url="+URLEncoder.encode("https://cs001.kasitesoft.com/pay/paySuccess.html"));
//				response.getWriter().write( 
//						"<html><head><script type=\"text/javascript\">"
//						+ "location.href='"+mwebUrl+"&redirect_url="+URLEncoder.encode("https://cs001.kasitesoft.com/pay/paySuccess.html")+"'"
//						+ "</script> </head><body></body></html>");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			LogUtil.error(log, e);
//			throw e;
//		}
//	}
//	
//	public String toXML(String root,Map<String, String> map){
//		StringBuffer buffer = new StringBuffer("");
//		if(map!=null){
//			for(Entry<String, String> entry:map.entrySet()){
//				buffer.append("<").append(entry.getKey()).append("><![CDATA[");
//				buffer.append(StringUtils.defaultString(entry.getValue()).trim());
//				buffer.append("]]></").append(entry.getKey()).append(">");
//			}
//		}
//		if(StringUtils.isNotBlank(root)){
//			return new StringBuffer("<").append(root.trim()).append(">")
//					.append(buffer)
//					.append("</").append(root.trim()).append(">").toString();
//		}
//		return buffer.toString();
//	}
//	
//	private String createSign(SortedMap<String, String> paramMap) throws Exception {
//		StringBuffer sb = new StringBuffer();
//		for(Map.Entry<String, String> entry:paramMap.entrySet()){
//			String k = entry.getKey();
//			String v = entry.getValue();
//			if (StringUtils.isNotEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
//				sb.append(k + "=" + v + "&");
//			}
//		}
//		sb.append("key=" + "127cc0f147c1432eaa780ace0d78a12a");
//		  java.security.MessageDigest md = MessageDigest.getInstance("MD5");
//	        byte[] array = md.digest(sb.toString().getBytes("UTF-8"));
//	        StringBuilder sb2 = new StringBuilder();
//	        for (byte item : array) {
//	            sb2.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
//	        }
//	        return sb2.toString().toUpperCase();
//		
//	}
//	
//	public static String getIPAddress(HttpServletRequest request) {
//	    String ip = null;
//
//	    //X-Forwarded-For：Squid 服务代理
//	    String ipAddresses = request.getHeader("X-Forwarded-For");
//	if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//	        //Proxy-Client-IP：apache 服务代理
//	        ipAddresses = request.getHeader("Proxy-Client-IP");
//	    }
//	if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//	        //WL-Proxy-Client-IP：weblogic 服务代理
//	        ipAddresses = request.getHeader("WL-Proxy-Client-IP");
//	    }
//	if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//	        //HTTP_CLIENT_IP：有些代理服务器
//	        ipAddresses = request.getHeader("HTTP_CLIENT_IP");
//	    }
//	if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//	        //X-Real-IP：nginx服务代理
//	        ipAddresses = request.getHeader("X-Real-IP");
//	    }
//
//	    //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
//	    if (ipAddresses != null && ipAddresses.length() != 0) {
//	        ip = ipAddresses.split(",")[0];
//	    }
//
//	    //还是不能获取到，最后再通过request.getRemoteAddr();获取
//	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//	        ip = request.getRemoteAddr();
//	    }
//	    return ip;
//	}
}
