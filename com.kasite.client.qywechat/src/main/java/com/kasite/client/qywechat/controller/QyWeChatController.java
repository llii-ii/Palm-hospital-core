package com.kasite.client.qywechat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.client.qywechat.constant.QyWeChatConstant;
import com.kasite.client.qywechat.service.MessageService;
import com.kasite.client.qywechat.service.UserService;
import com.kasite.client.qywechat.util.AesException;
import com.kasite.client.qywechat.util.WXBizMsgCrypt;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.QyWeChatConfig;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 企业微信授权
 * 
 * @author 無
 *
 */
@Controller
@RequestMapping(value = "/qywechat/{clientId}/{configKey}")
public class QyWeChatController extends AbstractController {

	private static Log log = LogFactory.getLog(QyWeChatController.class);

	private static final String GET_METHOD = "GET";

	/** 微信oauth2.0 地址 **/
	public static final String WeiXinConstant_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";

	/**
	 * 跳转到企业微信登录授权接口
	 */
	@RequestMapping(value = "/gotoOauth.do")
	@ResponseBody
	public void gotoOauth(@PathVariable("clientId") String clientId, @PathVariable("configKey") String configKey,
			HttpServletRequest request, HttpServletResponse response) {
		String toUrl = request.getParameter("toUrl");
		KasiteConfig.print("--toUrl-->" + toUrl);
		String redirectUri = KasiteConfig.getOauthCallBackUrl(ChannelTypeEnum.qywechat, clientId, configKey) + "?toUrl="
				+ toUrl;
		gotoWeChatOauth(clientId, configKey, "snsapi_base", redirectUri, request, response);
	}

	/**
	 * goto企微授权
	 */
	@Override
	protected void gotoWeChatOauth(String clientId, String configKey, String scope, String redirectUri,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			redirectUri = URLEncoder.encode(redirectUri, "utf-8");
			StringBuffer url = new StringBuffer();
			String corpid = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.corpid, configKey);
			if (StringUtil.isBlank(corpid)) {
				LogUtil.info(logger, "获取微信配置corpid为空： configKey = " + configKey);
			}
			url.append(WeiXinConstant_CODE_URL).append("?appid=").append(corpid);
			// 应用授权作用域。企业自建应用固定填写：snsapi_base
			if (null == scope) {
				scope = "snsapi_base";
			}
			url.append("&redirect_uri=").append(redirectUri)
					.append("&response_type=code&scope=" + scope + "&state=1#wechat_redirect");
			KasiteConfig.print("--url-->" + url);
			// 客户端跳转
			response.sendRedirect(url.toString());
			return;
		} catch (Exception e) {
			LogUtil.error(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * 企业微信授权回调
	 */
	@RequestMapping(value = "/oauthCallback.do")
	@ResponseBody
	public void oauthCallback(@PathVariable("clientId") String clientId, @PathVariable("configKey") String configKey,
			HttpServletRequest request, HttpServletResponse response) {
		String openid = null;
		String toUrl = request.getParameter("toUrl");
		String authCode = request.getParameter("code");
		AuthInfoVo vo = null;
		try {
			if (StringUtils.isNotBlank(authCode)) {
				// 通过code获取成员UserID
				JSONObject jsonObject = UserService.getUserId(authCode, configKey);

				if (null != jsonObject && 0 == jsonObject.getInt(QyWeChatConstant.ERR_CODE)) {
					openid = jsonObject.getString("UserId");
					System.out.println("获取成员UserID成功:" + openid);
					// 登录获取token
					vo = login(true, openid, clientId, configKey, null, request, response);
					String token = vo.getSessionKey();
					int index = toUrl.lastIndexOf("?");
					if (index > 0) {
						toUrl = toUrl + "&token=" + token + "&openid=" + openid;
					} else {
						toUrl = toUrl + "?token=" + token + "&openid=" + openid;
					}
				} else {
					throw new RRException("未获取到企业微信用户信息 UserID 请联系管理员。" + jsonObject.getString("errmsg"));
				}
			}
		} catch (Exception e) {
			logger.error("企业微信授权回调异常", e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			try {
				// 不管登陆成功与否，都先跳转到后续页面
				sendRedirect(response, toUrl);
				Logger.get().info(new com.kasite.core.common.log.LogBody(vo).set("企业微信登录：", clientId)
						.set("configKey", configKey).set("openid", openid));
			} catch (IOException e) {
				logger.error("企业微信授权回调异常", e);
				LogUtil.error(log, e);
				e.printStackTrace();
			}

		}
	}

	/**
	 * 验证URL回调、消息处理
	 * 
	 * @throws UnsupportedEncodingException
	 */

	@RequestMapping(value = "/msgDeal.do")
	@ResponseBody
	public void validation(@PathVariable("clientId") String clientId, @PathVariable("configKey") String configKey,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		KasiteConfig.print("企业微信发来请求........");
		try {
			if (GET_METHOD.equals(request.getMethod().toUpperCase())) {
				KasiteConfig.print("验证URL回调........");
				// 微信加密签名
				String sVerifyMsgSig = request.getParameter("msg_signature");
				// 时间戳
				String sVerifyTimeStamp = request.getParameter("timestamp");
				// 随机数
				String sVerifyNonce = request.getParameter("nonce");
				// 随机字符串
				String sVerifyEchoStr = request.getParameter("echostr");
				String sEchoStr; // 需要返回的明文
				PrintWriter out = null;
				try {
					out = response.getWriter();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				WXBizMsgCrypt wxcpt;
				try {
					String corpid = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.corpid, configKey);
					String evenToken = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.eventoken, configKey);
					String encodingAESKey = KasiteConfig.getQyWeChatConfig(QyWeChatConfig.encodingaeskey, configKey);
					wxcpt = new WXBizMsgCrypt(evenToken, encodingAESKey, corpid);
					sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
					log.info("验证URL成功，返回EchoStr:" + sEchoStr);
					// 验证URL成功，将sEchoStr返回
					out.print(sEchoStr);
					out.close();
					out = null;
				} catch (AesException e1) {
					e1.printStackTrace();
					log.info("验证URL失败:" + e1.getMessage());
				}
			} else {
				System.out.println("处理微信服务器发来的消息........");
				// 1.调用消息业务类接收消息、处理消息
				MessageService msgsv = new MessageService();
				String respMessage = msgsv.getEncryptRespMessage(request, configKey);
				System.out.println("被动回复消息=" + respMessage);

				// 2.响应消息
				PrintWriter out = response.getWriter();
				out.print(respMessage);
				out.close();
			}
		} catch (Exception e) {
			LogUtil.error(log, e);
			e.printStackTrace();
		}
	}
}
