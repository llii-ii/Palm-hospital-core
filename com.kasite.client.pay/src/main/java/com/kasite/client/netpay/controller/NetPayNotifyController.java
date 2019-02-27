package com.kasite.client.netpay.controller;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.netpay.constants.NetPayConstant;
import com.kasite.client.netpay.util.NetPay;
import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 招行一网通回调
 */
@Controller
@RequestMapping(value = "/netPay/{clientId}/{configKey}")
public class NetPayNotifyController extends AbstractController {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);

	@Autowired
	private PayBackCallUtil payBackCallUtil;

	@RequestMapping(value = "/{openId}/{token}/{orderId}/payNotify.do")
	public void payNotify(@PathVariable("configKey") String configKey, @PathVariable("openId") String openId,
			@PathVariable("token") String token, @PathVariable("clientId") String clientId,
			@PathVariable("orderId") String orderId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String uuid = "NetPayNotify_" + IDSeed.next();
		AuthInfoVo authInfoVo = createAuthInfoVo(uuid, token, getUser());
		authInfoVo.setSign(openId);

		String jsonRequestData = request.getParameter("jsonRequestData");

		if (!StringUtil.isEmpty(jsonRequestData)) {
			JSONObject jsonObject = JSONObject.parseObject(jsonRequestData);
			String reqSign = jsonObject.getString("sign");
			//String reqSignType = jsonObject.getString("signType");
			//招行一网通支付异步通知为RSA签名
			if (!StringUtil.isEmpty(reqSign)) {
				JSONObject noticeDataJosn = jsonObject.getJSONObject("noticeData");
				String sign = null;
				Map<String, Object> map = (Map<String, Object>) noticeDataJosn;
				SortedMap<String, Object> signParam = new TreeMap<String, Object>(map);
				boolean isVerify = NetPay.signMapRSA(signParam,reqSign,
						NetPay.getPubKey(configKey, false));//第一次验签
				if (isVerify) {
					String noticeSerialNo = noticeDataJosn.getString("noticeSerialNo");
					String bankSerialNo = noticeDataJosn.getString("bankSerialNo");
					String amount = noticeDataJosn.getString("amount");
					InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(NetPayNotifyController.class,
							"TenpayNotifyServlet", null, orderId, null, clientId, configKey, openId);
					payBackCallUtil.addPayNotify(msg, orderId, bankSerialNo, clientId,
							StringUtil.yuanChangeFenInt(amount), configKey);
					// 调用订单状态更新接口
					// 返回状态成功 交易结果成功
					response.getWriter().write("SUCCESS");
					LogUtil.info(log, new LogBody(authInfoVo).set("orderId", orderId).set("msg", "招行一网通支付成功异步通知签名成功！")
							.set("jsonRequestData", jsonRequestData).set("noticeSerialNo", noticeSerialNo));
				} else {
					//签名失败，则更新招行公钥,再次验签
					NetPayConstant.publicKeyMap.put(configKey, NetPay.getPubKey(configKey, true));
					isVerify = NetPay.signMapRSA(signParam,reqSign,
							NetPay.getPubKey(configKey, false));//第二次验签
					if (isVerify) {
						String noticeSerialNo = noticeDataJosn.getString("noticeSerialNo");
						String bankSerialNo = noticeDataJosn.getString("bankSerialNo");
						String amount = noticeDataJosn.getString("amount");
						InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(NetPayNotifyController.class,
								"TenpayNotifyServlet", null, orderId, null, clientId, configKey, openId);
						payBackCallUtil.addPayNotify(msg, orderId, bankSerialNo, clientId,
								StringUtil.yuanChangeFenInt(amount), configKey);
						// 调用订单状态更新接口
						// 返回状态成功 交易结果成功
						response.getWriter().write("SUCCESS");
						LogUtil.info(log, new LogBody(authInfoVo).set("orderId", orderId).set("msg", "招行一网通支付成功异步通知签名成功！")
								.set("jsonRequestData", jsonRequestData).set("noticeSerialNo", noticeSerialNo));
					}else {
						LogUtil.warn(log, new LogBody(authInfoVo).set("orderId", orderId).set("msg", "招行一网通支付成功异步通知签名异常！")
								.set("jsonRequestData", jsonRequestData).set("sign", sign));
					}
				}
			} else {
				LogUtil.warn(log, new LogBody(authInfoVo).set("orderId", orderId).set("msg", "招行一网通支付成功异步通知异常！sign为空！")
						.set("jsonRequestData", jsonRequestData));
			}
		} else {
			LogUtil.warn(log,
					new LogBody(authInfoVo).set("orderId", orderId).set("msg", "招行一网通支付成功异步通知异常！jsonRequestData为空！"));
		}
	}

}
