package com.kasite.client.wechat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.core.common.Version;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.log.LogLevel;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.RequestHandler;
import com.kasite.core.common.util.wechat.TenpayConstant;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.common.util.wxmsg.WxMsgUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil.Channel;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 财付通后台支付通知处理
 * @author MECHREV
 */

@Controller
@RequestMapping(value = "/wxPay/{clientId}/{configKey}")
public class TenpayNotifyServlet  extends AbstractController {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	private String logTag;
	
	@Autowired
	private PayBackCallUtil payBackCallUtil;
	
	@RequestMapping(value = "/{openId}/{token}/{orderId}/payNotify.do")
	@ResponseBody
	@SysLog(value="wxPay.payNotify")
	public void payNotify(
			@PathVariable("configKey") String configKey, 
			@PathVariable("openId") String openId,
			@PathVariable("token") String token,
			@PathVariable("clientId") String clientId,
			@PathVariable("orderId") String orderId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		long t = System.currentTimeMillis();
		String uuid = "TenpayNotifyServlet_payNotify_"+IDSeed.next();
		AuthInfoVo authInfo = createAuthInfoVo(uuid, token, getUser());
		authInfo.setSign(openId);
		String referer = "";
		String uri = "";
		String api = "wechat_payNotify";
		String user_agent =  "";
		String className = "";
		String clientUrl = "";
		String params = "";
		String result = "";
		String methodName = "";
		RequestHandler reqHandler = null;
		try {
			referer = request.getHeader("referer");
			user_agent =  request.getHeader("user-agent");
			user_agent = user_agent + ",referer=" +referer;
			uri = request.getRequestURI();
			clientUrl = AbstractController.getIpAddress(request);
			
			//KasiteConfig.getWeChatPayNotifyAuthInfo(configKey, openId, token);
			String xmlContent = StringUtil.inputStreamTOString(request.getInputStream(), "utf8");
			LogUtil.info(log, "[商户端接收微信支付通知]:" + xmlContent, authInfo);
			// 入参转换成map
			// Map<String,String> map = TenpayXMLUtil.doXMLParse(xmlContent);

			// 微信商户信息
			String appId = KasiteConfig.getWxPay(WXPayEnum.wx_app_id, configKey);
			String merchantKey = KasiteConfig.getWxPayMchKey(configKey);

			// 入参转换成RequestHandler对象
			reqHandler = new RequestHandler(request, response);
			reqHandler.init(appId, "", merchantKey);
			reqHandler.doParse(xmlContent);

			String returnCode = reqHandler.getParameter(TenpayConstant.RETURN_CODE);
			String returnMsg = reqHandler.getParameter(TenpayConstant.RETURN_MSG);
			String outTradeNo = reqHandler.getParameter(TenpayConstant.OUT_TRADE_NO);
			String transactionId = reqHandler.getParameter(TenpayConstant.TRANSACTION_ID);
			String resultCode = reqHandler.getParameter(TenpayConstant.RESULT_CODE);
			Integer price = new Integer(reqHandler.getParameter(TenpayConstant.TOTAL_FEE));
			
			JSONObject noticeLogJson = new JSONObject();
			noticeLogJson.put("OP_TYPE", 1);
			noticeLogJson.put("CHANEL_TYPE",1);
			noticeLogJson.put("MESSAGE", xmlContent);
			noticeLogJson.put("OUT_TRADE_NO", outTradeNo);
			noticeLogJson.put("TRANSACTION_ID", transactionId);
			noticeLogJson.put("RETURN_CODE", returnCode);
			noticeLogJson.put("RESULT_CODE", resultCode);
			noticeLogJson.put("FINAL_RESULT", returnMsg);

			logTag = "TAG[" + "out_trade_no:" + outTradeNo + "|" + "transaction_id:" + transactionId + "]";
			boolean flag = reqHandler.isValidWXSign();
			LogUtil.info(log, new LogBody(authInfo).set("openid", openId).set("微信支付通知MD5鉴权", logTag + " 鉴权结果：" + flag)
					.set("out_trade_no", outTradeNo).set("transaction_id", transactionId));
			LogUtil.info(log, logTag + "鉴权结果：" + flag, authInfo);
			if (flag) {
				// 验证微信签名成功
				if (returnCode.equals(TenpayConstant.RETURN_CODE_SUCCESS)) {
					// 返回状态码 通信状态码 成功
					if (resultCode.equals(TenpayConstant.RETURN_CODE_SUCCESS)) {
						InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(TenpayNotifyServlet.class, "TenpayNotifyServlet", null, outTradeNo, null, clientId, configKey,openId);
						payBackCallUtil.addPayNotify(msg,outTradeNo, transactionId,clientId,price,configKey);
						// 调用订单状态更新接口
						// 返回状态成功 交易结果成功
						reqHandler.sendToCFT(this.getReturnXml());
					} else {
						LogUtil.info(log, logTag + "INFO[微信业务失败！|resultCode:" + resultCode + "]",authInfo);
					}
				} else {
					LogUtil.info(log, logTag + "INFO[微信接口通讯失败！|returnMsg:" + returnMsg + "]",authInfo);
				}
			} else {
				LogUtil.info(log, logTag + "INFO[微信MD5校验失败！]",authInfo);
			}
			LogUtil.info(log, logTag + "INFO[写入微信通知回调日志！]",authInfo);
			WxMsgUtil.create(KasiteConfig.wechatAndzfbMsgPath()).write(xmlContent,Channel.WXPay);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, logTag + "INFO[" + e.toString() + "]",authInfo,e);
		}finally {
			try {
				if(null != reqHandler) {
					params = JSONObject.toJSONString(reqHandler.getParameters()); 
				}
				Logger.get().log(LogLevel.INFO,
						"wsgw-log",
						LogBody.me(authInfo)
								.set("AuthInfo", authInfo.toString())// 鉴权头
								.set("UserAgent", user_agent)
								.set("uniqueReqId",uuid)// RPC请求时间戳
								.set("Api", api)//
								.set("Param", params)// 入参
								.set("ParamType", 0)// 入参类型 0 json
								.set("OutType", 0)// 出参类型 0 json
								.set("V", Version.V)// 版本号,默认1
								.set("Result", result)// 返回结果
								.set("Url", user_agent)// 目标子系统
								.set("ClassName",
										className == null ? "" : className)// 目标系统类名
								.set("MethodName",
										methodName == null ? "" : methodName)// 目标系统方法名
								.set("Times", (System.currentTimeMillis() - t))// 耗时
								.set("ClientUrl", clientUrl)// 客户端IP
								.set("CurrTimes", System.currentTimeMillis())
//	    							.set("resp_mills",ResptimeHolder.removeRespTime(uniqueReqId))
								.set("WsgwUrl", uri));// 网关（指接口网关或开放平台）IP，未经网关的填写空
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * 构造返回XML数据
	 * 
	 * @return
	 */
	private String getReturnXml() {
		StringBuffer msg = new StringBuffer();
		// 子商户号(受理模式下必填)
		// Object sub_mch_id_obj = configMap.get("sub_merchantid");
		// if(sub_mch_id_obj!=null &&
		// StringUtils.isNotBlank(sub_mch_id_obj.toString())){
		// msg.append("SUCCESS");
		// }else{
		msg.append("<xml>");
		msg.append("<return_code>");
		msg.append("<![CDATA[SUCCESS]]>");
		msg.append("</return_code>");
		msg.append("<return_msg>");
		msg.append("<![CDATA[OK]]>");
		msg.append("</return_msg>");
		msg.append("</xml>");
		// }
		return msg.toString();
	}

}
