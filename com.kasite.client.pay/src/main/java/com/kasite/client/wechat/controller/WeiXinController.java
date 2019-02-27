package com.kasite.client.wechat.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kasite.core.common.util.wxmsg.IDSeed;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.client.wechat.message.MessageContent;
import com.kasite.client.wechat.message.MessageService;
import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.HttpClientService;
import com.kasite.core.common.util.wechat.constants.WeiXinConstant;
import com.kasite.core.serviceinterface.common.controller.AbsAddUserController;
import com.kasite.core.serviceinterface.module.wechat.IWxMsgService;

import net.sf.json.JSONObject;

/**
 * 微信接入
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/weixin/{clientId}/{configKey}")
public class WeiXinController extends AbsAddUserController{
	private static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY_WX);
	private static final String GET_METHOD = "GET";
//	private static final String SCOPE = "scope";
//	private static final String SNSAPI_USERINFO = "snsapi_userinfo";
	public static final String ERRORPAGE= "/pageError.html?code={0}&msg={1}";
	
	/**
	 * 微信接入的URL
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/welcome.do")
	@ResponseBody
	@SysLog(value="wechat_welcome")
	public void welcome(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			if (GET_METHOD.equals(request.getMethod().toUpperCase())) {
				// 验证服务器地址的有效性，验证后屏蔽此代码
				if (WeiXinService.checkSignature(configKey, request)) {
					// 通过验证
					// 随机字符串
					String echostr = request.getParameter("echostr");
					// 判断是否是微信接入激活验证，只有首次接入验证时才会收到echostr参数，此时需要把它直接返回
					// 返回后成功接入
					KasiteConfig.print("checkSignature=====>true");
					// 首次验证
					if (StringUtil.isNotEmpty(echostr)) {
						print(response, echostr);
					}
				} else {// 验证失败
					KasiteConfig.print("checkSignature=====>false");
					print(response, WeiXinConstant.CHECK_SIGNA_FALSE_MSG);
				}
			} else {
				// 第一次接入验证后开启此代码，屏蔽上面验证代码
				// 读取微信端发送的消息模式，并且做出响应
				Document doc = null;
				String content = (String) request.getSession().getAttribute("content");
				if(StringUtil.isBlank(content)) {
					// 从request中取得输入流  
				    InputStream inputStream = request.getInputStream();
				    // 读取输入流  
				    SAXReader reader = new SAXReader();  
					String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
					reader.setFeature(FEATURE, true);
					doc = reader.read(inputStream);
				}else {
					doc = DocumentHelper.parseText(content);
				}
				

				AuthInfoVo vo = super.createAuthInfoVo(request);
				Logger.get().info(new com.kasite.core.common.log.LogBody(vo).set("微信消息回调：", clientId));
				readWeiXinRequest(vo, doc, request, response);
			}

		} catch (Exception e) {
			LogUtil.error(log, e);
			e.printStackTrace();
		}
	}

	/**com.kasite.client.pay.job.MerchantOrderCheckJob
	 * 跳转到微信授权接口
	 */
	@RequestMapping(value = "/gotoOauth.do")
	@ResponseBody
	public void gotoOauth(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) {
		String toUrl = request.getParameter("toUrl");
		KasiteConfig.print("--toUrl-->" + toUrl);
		String redirectUri = KasiteConfig.getOauthCallBackUrl(ChannelTypeEnum.wechat, clientId, configKey) + "?toUrl="
				+ toUrl;
		gotoWeChatOauth(clientId, configKey,null, redirectUri, request, response);
	}
	
	/**
	 * 获取用户OpenId
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getOpenId.do")
	@ResponseBody
	public void getOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.getOutputStream().print(request.getParameter("openid"));
	}
	
	/**
	 * 授权获取用户信息
	 * @throws Exception 
	 */
	@RequestMapping(value = "/userinfo.do")
	@ResponseBody
	public R getOauth2UserInfo(@PathVariable("clientId") String clientId,
			@PathVariable("configKey") String configKey,String authCode,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		R r = null;
		String openid = null;
		JSONObject userJs = null; 
		if (StringUtils.isNotBlank(authCode)) {
			JSONObject json = WeiXinService.getAuthToken(authCode, configKey);
			if (json != null && null != json.get("openid") && StringUtil.isNotBlank(json.getString("openid"))) {
				openid = json.getString("openid");
				String access_token = json.getString("access_token");
				userJs = WeiXinService.getSnsapiUserInfo(openid, access_token);
				if(null == userJs) {
					return R.error("调用微信获取用户信息接口异常，请核实与微信端的请求是否正常。wechat_user_info");
				}
			}else {
				return R.error("未获取到微信用户信息 OpenId 请联系管理员。wechat_sns_oauth2_access_token");
			}
		}else {
			r = R.error("未传入授权码：code = "+ authCode);
		}
		if(null != userJs && StringUtil.isNotBlank(openid)) {
			r = R.ok();
			r.put("result", userJs);
		}else {
			r = R.error("调用微信获取用户信息接口异常。");
		}
		return r;
	}
	
	/**
	 * 授权回调
	 */
	@RequestMapping(value = "/oauthCallback.do")
	@ResponseBody
	public void oauthCallback(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) {
		String openid = null;
		String toUrl = request.getParameter("toUrl");
		String authCode = request.getParameter("code");
		JSONObject userJs = null;
		AuthInfoVo vo = null;
		try {
			if (StringUtils.isNotBlank(authCode)) {
				// 通过code换取网页授权access_token
				JSONObject json = WeiXinService.getAuthToken(authCode, configKey);
				
				if (json != null && null != json.get("openid") && StringUtil.isNotBlank(json.getString("openid"))) {
					openid = json.getString("openid");
					String access_token = json.getString("access_token");
//					userJs = WeiXinService.getUserInfo(openid, configKey);
					userJs = WeiXinService.getSnsapiUserInfo(openid, access_token);
					if(null == userJs) {
						throw new RRException("调用微信获取用户信息接口异常，请核实与微信端的请求是否正常。wechat_user_info");
					}
					vo = login(true, openid, clientId, configKey,null, request, response);
					String token = vo.getSessionKey();
					int index = toUrl.lastIndexOf("?");
					if(index >0) {
						toUrl = toUrl+"&token="+token+"&openid="+openid;
					}else {
						toUrl = toUrl+"?token="+token+"&openid="+openid;
					}
					if(null != userJs) {
						addWeChatUser(openid, vo, userJs);
					}
				}else {
					throw new RRException("未获取到微信用户信息 OpenId 请联系管理员。wechat_sns_oauth2_access_token");
					
				}
			}
		} catch (Exception e) {
			logger.error("微信授权回调异常",e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			try {
				// 不管登陆成功与否，都先跳转到后续页面
				sendRedirect(response,toUrl);
				Logger.get().info(new com.kasite.core.common.log.LogBody(vo).set("微信登录：", clientId).set("configKey", configKey).set("openid", openid));
			} catch (IOException e) {
				logger.error("微信授权回调异常",e);
				LogUtil.error(log, e);
				e.printStackTrace();
			}

		}
	}
	/**
	 * 第三方跳转（将页面嵌入到第三方）
	 */
	@RequestMapping(value = "/thirdparty.do")
	@ResponseBody
	public void thirdparty(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) {
		String openid = null;
		String toUrl = request.getParameter("toUrl");
		openid = request.getParameter("openId");
		AuthInfoVo vo = null;
		try {
			if(openid!=null){
				vo = login(true, openid, clientId, configKey,null, request, response);
				String token = vo.getSessionKey();
				int index = toUrl.lastIndexOf("?");
				if(index >0) {
					toUrl = toUrl+"&token="+token+"&openid="+openid;
				}else {
					toUrl = toUrl+"?token="+token+"&openid="+openid;
				}
			}
		} catch (Exception e) {
			logger.error("微信授权回调异常",e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			try {
				// 不管登陆成功与否，都先跳转到后续页面
				sendRedirect(response,toUrl);
				Logger.get().info(new com.kasite.core.common.log.LogBody(vo).set("微信登录：", clientId).set("configKey", configKey).set("openid", openid));
			} catch (IOException e) {
				logger.error("微信授权回调异常",e);
				LogUtil.error(log, e);
				e.printStackTrace();
			}

		}
	}
	/**
	 * 输出
	 * 
	 * @param response
	 * @param echostr
	 */
	private void print(HttpServletResponse response, String content) {

		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print(content);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			pw.flush();
			pw.close();
		}
	}
	/**
	 * 处理微信发来的请求
	 *
	 * @param request
	 * @return
	 */
	public String processRequest(AuthInfoVo vo,Document doc,HttpServletRequest request) {
		String respMessage = null;
		MessageService msg = new MessageService();
		String respContent = "请求处理异常，请稍候尝试！";
		try {
			MessageContent msgc = new MessageContent(vo,doc);
			respMessage = msg.onMessage(msgc);
			IWxMsgService wxMsgService = SpringContextUtil.getBean(IWxMsgService.class);
			if(null != wxMsgService) {
				try {
					String configKey=vo.getConfigKey();
					String appId = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, configKey);
					wxMsgService.save(appId, IDSeed.next()+"="+doc.asXML()+"=WX");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			KasiteConfig.print(respMessage);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			// 日志收集
			return respContent;
		}
		// 日志收集
		return respMessage;
	}

	/**
	 * 读取微信请求信息
	 * 
	 * @param response
	 * @param request
	 */
	private void readWeiXinRequest(AuthInfoVo vo,Document doc,HttpServletRequest request, HttpServletResponse response) {
		String respMessage = processRequest(vo,doc,request);
		KasiteConfig.print("=============>backMessage：" + respMessage);
		// 响应消息
		print(response, respMessage);
	}
	
	/**
	 * 获取用户TOKEN
	 */
	@RequestMapping(value = "/getToken.do")
	@ResponseBody
	@SysLog(value="wechat_getToken")
	public String getToken(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response,String secret) {
		String accessToken = "";
		try {
			accessToken = HttpClientService.getToken(configKey);
		} catch (Exception e) {
			logger.error("获取用户TOKEN",e);
			e.printStackTrace();
		}
		return accessToken;
	}

}
