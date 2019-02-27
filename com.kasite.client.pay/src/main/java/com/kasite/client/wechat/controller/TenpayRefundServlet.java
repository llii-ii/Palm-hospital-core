package com.kasite.client.wechat.controller;

import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.sys.verification.Base64;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.util.wechat.TenpayConstant;
import com.kasite.core.common.util.wxmsg.WxMsgUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil.Channel;
import com.yihu.hos.util.MD5Util;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;

/**
 * 微信支付退款处理
 * 
 * @author leo
 *
 */
@Controller
@RequestMapping(value = "/wxpay/{clientId}/{configKey}")
public class TenpayRefundServlet  extends AbstractController {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";
	@Autowired
	private PayBackCallUtil payBackCallUtil;
	
	@RequestMapping(value = "/{openid}/refundNotify.do")
	@ResponseBody
	@SysLog(value="wechat_refundNotify")
	public void payNotify(
			@PathVariable("configKey") String configKey, 
			@PathVariable("openid") String openid,
			@PathVariable("clientId") String clientId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element req = document.addElement("xml");
		AuthInfoVo authInfo = login(false, openid, clientId, configKey, null,request, response);
		authInfo.setSign(openid);
		LogBody logBody = new LogBody(authInfo);
		logBody.set("resp", "微信退款异步通知");
		LogUtil.info(log, logBody);
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();

			String returnCode = request.getParameter("return_code");
			String returnMsg = request.getParameter("return_msg");
			logBody = new LogBody(authInfo);
			logBody.set("returnCode", returnCode);
			logBody.set("returnMsg", returnMsg);
			LogUtil.info(log, logBody);

			/**
			 * returnCode = success 返回的加密信息。失败返回空 解密方式: （1）对加密串A做base64解码，得到加密串B
			 * 
			 * （2）对商户key做md5，得到32位小写key* (
			 * key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置 )
			 * 
			 * （3）用key*对加密串B做AES-256-ECB解密
			 */
			String reqInfo = request.getParameter("req_info");
			String refundStatus = "";

			JSONObject noticeLogJson = new JSONObject();
			noticeLogJson.put("OP_TYPE", 3);
			noticeLogJson.put("OP_TYPE", 3);
			noticeLogJson.put("CHANEL_TYPE", 1);

			if (TenpayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				reqInfo = decodeInfo(configKey,reqInfo);
				JSONObject reparam = JSONObject.fromObject(reqInfo);
				refundStatus = reparam.get("refund_status").toString();
				if (TenpayConstant.RETURN_CODE_SUCCESS.equals(refundStatus)) {
					String orderId = reparam.get("out_trade_no").toString();
					String refundOrderId = reparam.get("out_refund_no").toString();
					String totalFee = reparam.get("total_fee").toString();
					// 微信退款单号
					String refundId = reparam.get("refund_id").toString();
					String refundFee = reparam.get("settlement_refund_fee").toString();
					InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(TenpayRefundServlet.class, 
							"TenpayRefundServlet", null, orderId, null, clientId, configKey,openid);
					payBackCallUtil.addRefundNotify(msg,orderId, refundOrderId, refundId, clientId,new Integer(totalFee),new Integer(refundFee),configKey);
					noticeLogJson.put("RESULT_CODE", 1);
					noticeLogJson.put("OUT_TRADE_NO", refundId);
					noticeLogJson.put("TRANSACTION_ID", orderId);
					noticeLogJson.put("MESSAGE", "微信退款异步回调通知成功");
					WxMsgUtil.create(KasiteConfig.localConfigPath()).write(noticeLogJson.toString(),Channel.WXRefund);
					XMLUtil.addElement(req, "return_code", "SUCCESS");
					XMLUtil.addElement(req, "return_msg","");
					logBody = new LogBody(authInfo);
					logBody.set("resp", "微信退款异步回调通知成功");
					LogUtil.info(log, logBody);

				} else {
					String orderId = reparam.get("out_trade_no").toString();
					// 微信退款单号
					String refundId = reparam.get("refund_id").toString();
					noticeLogJson.put("RESULT_CODE", "1");
					noticeLogJson.put("OUT_TRADE_NO", refundId);
					noticeLogJson.put("TRANSACTION_ID", orderId);
					noticeLogJson.put("MESSAGE", "微信退款异步回调通知失败refund_status：" + refundStatus);
					String fileDir = this.getClass().getClassLoader().getResource("").getPath()+"\\logs";
					WxMsgUtil.create(fileDir).write(noticeLogJson.toString(),Channel.WXRefund);
					
					XMLUtil.addElement(req, "return_code", "FAIL");
					XMLUtil.addElement(req, "return_msg", "");

					logBody = new LogBody(authInfo);
					logBody.set("resp", "微信退款异步回调通知失败refund_status：" + refundStatus);
					LogUtil.info(log, logBody);

				}

			} else {
				LogUtil.info(log, "微信退款异步回调通知失败return_msg：" + returnMsg,authInfo);
				XMLUtil.addElement(req, "return_code", "FAIL");
				XMLUtil.addElement(req, "return_msg", returnMsg);

				logBody = new LogBody(authInfo);
				logBody.set("resp", "微信退款异步回调通知失败return_msg：" + returnMsg);
				LogUtil.info(log, logBody);
			}
			out.println(document.asXML());

		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e,authInfo);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}

	}

	/**
	 * 
	 * 解密方式: （1）对加密串A做base64解码，得到加密串B
	 * 
	 * （2）对商户key做md5，得到32位小写key* (
	 * key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置 )
	 * 
	 * （3）用key*对加密串B做AES-256-ECB解密
	 */
	private String decodeInfo(String wxPayKey,String info) {
		try {
			byte[] infoB = Base64.decode(info);
			String key = KasiteConfig.getWxPayMchKey(wxPayKey);//getTenPayMerchantKey();
			key = MD5Util.getMD5Str(key);
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] decbbdt = cipher.doFinal(infoB);
			return new String(decbbdt);
		}catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
}
