package com.kasite.client.zfb.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.internal.util.AlipaySignature;
import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.client.zfb.util.AlipayConstant;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.RequestUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil.Channel;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;

/**
 * 新版支付宝支付接口后台异步通知处理 2.0版本
 * 支付回调通知
 * @author lsl
 *
 */
@Controller
@RequestMapping(value = "/alipay/{clientId}/{configKey}/{openid}")
public class AliPayNotifyServlet  extends AbstractController {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	private String logTag;
	
	@Autowired
	private PayBackCallUtil payBackCallUtil;
	
	@RequestMapping(value = "/{token}/{orderId}/payNotify.do")
	@ResponseBody
	@SysLog(value="alipay_payNotify")
	public void payNotify(@PathVariable("clientId") String clientId,
			@PathVariable("configKey") String configKey,
			@PathVariable("openid") String openid,
			@PathVariable("orderId") String orderId,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			AuthInfoVo vo = login(false, openid, clientId, configKey,null, request, response);
			vo.setSign(openid);
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = RequestUtil.getRequestParams(request);
			LogBody logBody = new LogBody(vo);
			logBody.set("openid", openid);
			logBody.set("resp", "支付宝异步通知返回参数:\r\n" + JSONObject.fromObject(params).toString(2));
			LogUtil.info(log, logBody);
			// 商户订单号
			String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			String totalAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
			String buyerId = new String(request.getParameter("buyer_id").getBytes("ISO-8859-1"), "UTF-8");
			logTag = "TAG[" + "out_trade_no:" + outTradeNo + "|" + "trade_no:" + tradeNo + "]";
			String alipaypublckey = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_alipayPublicKey, configKey);
			String zfb_signType = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signType, configKey);
			
			boolean verifyResult = AlipaySignature.rsaCheckV1(params, alipaypublckey, "UTF-8", zfb_signType);
			
			JSONObject noticeLogJson = new JSONObject();
			noticeLogJson.put("OP_TYPE", 1);
			noticeLogJson.put("CHANEL_TYPE", 2);
			noticeLogJson.put("CHANEL_TYPE", 2);
			noticeLogJson.put("MESSAGE", JSONObject.fromObject(params).toString(2));
			noticeLogJson.put("OUT_TRADE_NO", outTradeNo);
			noticeLogJson.put("TRANSACTION_ID", tradeNo);
			noticeLogJson.put("RETURN_CODE", tradeStatus);
			noticeLogJson.put("FINAL_RESULT", 0);
			if (verifyResult) {
				// 验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				// 请在这里加上商户的业务逻辑程序代码

				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				// TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
				// TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
				if (AlipayConstant.TRADE_STATUS_FINISHED.equals(tradeStatus)
						|| AlipayConstant.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
					LogUtil.info(log, logTag + "INFO[支付宝订单正确！|tradeStatus：" + tradeStatus + "]");
					InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(AliPayNotifyServlet.class, "AliPayNotifyServlet", 
							null, outTradeNo, null, clientId, configKey,openid);
					payBackCallUtil.addPayNotify(msg,outTradeNo, tradeNo, clientId,StringUtil.yuanChangeFenInt(totalAmount),configKey,buyerId);
					response.getWriter().write("success");
				} else {
					LogUtil.info(log, logTag + "INFO[支付宝订单状态错误！|tradeStatus：" + tradeStatus + "]");
				}
			} else {// 验证失败
				LogUtil.info(log, logTag + "INFO[支付宝MD5签名校验失败！]");
				noticeLogJson.put("FINAL_RESULT","支付宝签名验证失败");
				response.getWriter().write("fail");
				;
			}
//			PayDao.insertNoticeLog(noticeLog);
			WxMsgUtil.create(KasiteConfig.wechatAndzfbMsgPath()).write(noticeLogJson.toString(),Channel.ZFBPay);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, "支付宝回调异常",e);
		}
	}

}
