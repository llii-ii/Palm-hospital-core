package com.kasite.client.zfb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.kasite.client.zfb.constants.AlipayServiceEnvConstants;
import com.kasite.client.zfb.service.AlipayService;
import com.kasite.client.zfb.util.AliUserToBatUserCache;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.util.CookieTool;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.RequestUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.serviceinterface.common.controller.AbsAddUserController;
import com.kasite.core.serviceinterface.module.basic.cache.BatUserCache;
import com.kasite.core.serviceinterface.module.basic.cache.IBatUserLocalCache;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * @author linjianfa
 * @Description: TODO 支付宝接入 生活号接入
 * @version: V1.0 2017年10月18日 下午3:11:04
 */
@Controller
@EnableAutoConfiguration
@ComponentScan
@RequestMapping(value = "/alipay/{clientId}/{configKey}")
public class AlipayController extends AbsAddUserController{
	private static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY_ALI);
	private static final String POST_METHOD = "post";
//	private static final String URLTYPE_WLZX = "WLZX";
//	private static final String URLTYPE_ZNDZ = "ZNDZ";
	
	/**
	 * 支付宝接入的URL
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/welcome.do")
	@ResponseBody
	@SysLog(value="alipay_welcome")
	public void welcome(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!POST_METHOD.equalsIgnoreCase(request.getMethod())) {
			response.getWriter().write("welcome!!!");
			return;
		}
		// 支付宝响应消息
		String responseMsg = "";
		// 1. 解析请求参数
		Map<String, String> params = RequestUtil.getRequestParams(request);
		// 打印本次请求日志，开发者自行决定是否需要
		KasiteConfig.print("支付宝请求串=====>" + request.getMethod() + "--" + params.toString());
		try {
			// 2. 验证签名
			AlipayService.checkSignature(configKey,params);
			// 3. 获取业务执行器 根据请求中的 service, msgType, eventType, actionParam 确定执行器
			//获取服务信息
	        String service = params.get("service");
	        if (StringUtils.isEmpty(service)) {
	            throw new Exception("无法取得服务名");
	        }
	        //获取内容信息
	        String bizContent = params.get("biz_content");
	        if (StringUtils.isEmpty(bizContent)) {
	            throw new Exception("无法取得业务内容信息");
	        }
	        JSONObject bizContentJson = (JSONObject) new XMLSerializer().read(bizContent);
			//获取FromUserId 做登录用
	        String openid = bizContentJson.getString("FromUserId");
	        if(service.equals("alipay.service.check")) {
	        	openid = configKey;
	        }
			AuthInfoVo vo = login(false,openid, clientId, configKey, null,request, response);
			responseMsg = AlipayService.processRequest(bizContentJson,vo,params);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			// 5. 响应结果加签及返回
			try {
				// 对响应内容加签
				responseMsg = AlipayService.encryptAndSign(configKey,responseMsg,
						KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_charset, configKey)
						, false, true,
						KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signType, configKey)
						);
				// http 内容应答
				response.reset();
				response.setContentType("text/xml;charset=GBK");
				PrintWriter printWriter = response.getWriter();
				printWriter.print(responseMsg);
				response.flushBuffer();
			} catch (AlipayApiException e) {
				e.printStackTrace();
				LogUtil.error(log, e);
			}
		}
	}

	
	
	/**
	 * 跳转到支付宝授权接口
	 */
	@RequestMapping(value = "/gotoOauth.do")
	@ResponseBody
	public void gotoOauth(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) {
		String toUrl = request.getParameter("toUrl");
		KasiteConfig.print("--toUrl-->" + toUrl);
		String redirectUri = KasiteConfig.getOauthCallBackUrl(ChannelTypeEnum.zfb, clientId, configKey) + "?toUrl=" + toUrl;
		gotoAliOauth(clientId, configKey, redirectUri,request, response);
	}
	
	/**
	 * 授权回调 TODO accessToken跟用户信息，后期升级为缓存，尽量少用cookie --linjf。
	 */
	@RequestMapping(value = "/oauthCallback.do")
	@ResponseBody
	public void oauthCallback(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) {
		String toUrl = request.getParameter("toUrl");
		KasiteConfig.print("跳转地址" + toUrl);
		String authCode = request.getParameter("auth_code");
		AlipayUserInfoShareResponse userResp = null;
		AuthInfoVo vo = null;
		String openid = null;
		try {
			if (StringUtils.isNotBlank(authCode)) {
				// 使用auth_code换取接口access_token及用户userId
				AlipaySystemOauthTokenResponse tokenResp = AlipayService.getAuthTokenFromAlipay(configKey,authCode);
				if (tokenResp != null && StringUtils.isNotBlank(tokenResp.getUserId())) {
					// 保存用户openid到cookie
					openid = tokenResp.getUserId();
					//是否支持该渠道的调用
					String zfbId = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, configKey);
					userResp = AlipayService.getUserInfo(configKey,openid,tokenResp.getAccessToken());
					vo = login(true,openid, clientId, configKey, userResp.getNickName(),request, response);
					if (null != userResp && userResp.isSuccess()) {
						BatUserCache cache = AliUserToBatUserCache.parse(response,tokenResp, userResp, configKey);
						addUser(openid, zfbId, vo, cache);
					}else {
						throw new RRException("未获取到支付宝用户信息 UserId 请联系管理员。resp="+ (null != userResp?userResp.getBody():""));
					}
					String token = vo.getSessionKey();
					int index = toUrl.lastIndexOf("?");
					if(index >0) {
						toUrl = toUrl+"&token="+token+"&openid="+openid;
					}else {
						toUrl = toUrl+"?token="+token+"&openid="+openid;
					}
					
				}
			}
		} catch (Exception e) {
			LogUtil.error(log, e);
			e.printStackTrace();
		} finally {
			try {
				// 不管登陆成功与否，都先跳转到后续页面
				sendRedirect(response,toUrl);
				Logger.get().info(new com.kasite.core.common.log.LogBody(vo).set("支付宝登录：", clientId).set("configKey", configKey).set("openid", openid));
			} catch (IOException e) {
				LogUtil.error(log, e);
				e.printStackTrace();
			}

		}
	}

	/**
	 * 鉴权，并且新增用户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/authAndAddBatUser.do")
	@ResponseBody
	public String authAndAddBatUser(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
//			String openId = CookieTool.getCookieValue(request, KstHosConstant.COMM_OPENID);
			SysUserEntity user = getUser();
			String openid = user.getUsername();
			AuthInfoVo vo = createAuthInfoVo(request);
			IBatUserLocalCache userLocalCache =  SpringContextUtil.getBean(IBatUserLocalCache.class);
			String zfbId = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, configKey);
			//如果内存中未获取到用户信息则从cookie中获取 acesstoken 再调用微信获取一次
			if(null != userLocalCache && userLocalCache.get(openid)==null) {
				String aplipayAccessToken = CookieTool.getCookieValue(request, AliUserToBatUserCache.APLIPAY_ACCESS_TOKEN);
				JSONObject aplipayAccessTokenJosnObj = JSONObject.fromObject(aplipayAccessToken);
				
				BatUserCache cache = new BatUserCache();
				AlipayUserInfoShareResponse userResp = AlipayService.getUserInfo(configKey,openid,aplipayAccessTokenJosnObj.getString("accessToken"));
				if (userResp.isSuccess()) {
					cache.setOpenId(userResp.getUserId());
					cache.setNickName(java.net.URLEncoder.encode(
							null == userResp.getNickName() ? "" : userResp.getNickName(), "utf-8"));
					if (AlipayServiceEnvConstants.GENDER_M.equals(userResp.getGender())) {
						cache.setSex(1);
					} else if (AlipayServiceEnvConstants.GENDER_F.equals(userResp.getGender())) {
						cache.setSex(2);
					} else {
						cache.setSex(0);
					}
					cache.setCity(userResp.getCity());
					cache.setProvince(userResp.getProvince());
					cache.setSubscribe(1);
					cache.setRemark(userResp.getIsCertified());
					cache.setHeadImgUrl(userResp.getAvatar());
					cache.setSysId(userResp.getUserType());
					cache.setConfigKey(configKey);
					userLocalCache.put(openid, cache);
					return addUser(openid, zfbId, vo, cache);
				}else {
					throw new RRException(userResp.getCode());
				}
			}else {
				return addUser(openid, zfbId, vo, userLocalCache.get(openid));
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			throw e;
		}
	}
	@GetMapping(value = "/createMenu")
	@ResponseBody
	public String createMenu(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			return AlipayService.updateMenu(configKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
//	
//	public String addUser(String openId,AuthInfoVo vo,String aplipayAccessToken,AlipayUserInfoShareResponse userinfoShareResponse) throws Exception {
//		String configKey = vo.getConfigKey();
////		String aplipayAccessToken = CookieTool.getCookieValue(request, APLIPAY_ACCESS_TOKEN);
//
//		if(null == userinfoShareResponse) {
//			JSONObject aplipayAccessTokenJosnObj = JSONObject.fromObject(aplipayAccessToken);
//			userinfoShareResponse = AlipayService.getUserInfo(configKey,openId,
//					aplipayAccessTokenJosnObj.getString("accessToken"));
//			if (!userinfoShareResponse.isSuccess()) {
//				// 获取信息失败
//				throw new Exception(userinfoShareResponse.getSubMsg());
//			}
//		}
//		
//		Document document = DocumentHelper.createDocument();
//		Element req = document.addElement(KstHosConstant.REQ);
//		XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "15102");
//		Element data = req.addElement(KstHosConstant.DATA);
//		XMLUtil.addElement(data, "OpenId", userinfoShareResponse.getUserId());
//		XMLUtil.addElement(data, "NickName", java.net.URLEncoder.encode(
//				null == userinfoShareResponse.getNickName() ? "" : userinfoShareResponse.getNickName(), "utf-8"));
//		// 看下要从哪过来
//		XMLUtil.addElement(data, "AccountId", KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId,configKey));
//		if (AlipayServiceEnvConstants.GENDER_M.equals(userinfoShareResponse.getGender())) {
//			XMLUtil.addElement(data, "Sex", 1);
//		} else if (AlipayServiceEnvConstants.GENDER_F.equals(userinfoShareResponse.getGender())) {
//			XMLUtil.addElement(data, "Sex", 2);
//		} else {
//			XMLUtil.addElement(data, "Sex", 0);
//		}
//		XMLUtil.addElement(data, "City", userinfoShareResponse.getCity());
//		XMLUtil.addElement(data, "Country", null);
//		XMLUtil.addElement(data, "Province", userinfoShareResponse.getProvince());
//		XMLUtil.addElement(data, "Language", null);
//		// 获取到信息后默认设置为1关注
//		XMLUtil.addElement(data, "Subscribe", 1);
//		XMLUtil.addElement(data, "HeadImgUrl", userinfoShareResponse.getAvatar());
//		XMLUtil.addElement(data, "Subscribe", null);
//		XMLUtil.addElement(data, "SubscribeTime", null);
//		XMLUtil.addElement(data, "UnionId", null);
//		XMLUtil.addElement(data, "Remark", userinfoShareResponse.getIsCertified());
//		XMLUtil.addElement(data, "GroupId", null);
//		XMLUtil.addElement(data, "SyncTime", DateOper.getNow("yyyy-MM-dd HH:mm:ss"));
//		XMLUtil.addElement(data, "SysId", userinfoShareResponse.getUserType());
//		XMLUtil.addElement(data, "State", "1");
//		InterfaceMessage msg = createInterfaceMsg(ApiModule.Bat.AddBatUser, KstHosConstant.OUTTYPE, KstHosConstant.INTYPE,
//				document.asXML(), vo);
//		msg.setAuthInfo(vo.toString());
//		IBatService batService = SpringContextUtil.getBean(IBatService.class);
//		if(null != batService) {
//			return DataUtils.xml2JSON(batService.AddBatUser(msg));
//		}else {
//			throw new RRException("无法新增batUser，请与管理员核实。");
//		}
//	}
//	

	@RequestMapping(value = "/sendRedirect.do")
	@ResponseBody
	public void sendRedirect(@PathVariable("clientId") String clientId,@PathVariable("configKey") String configKey,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String urlType = request.getParameter("urlType");
			String url = "";
			if (urlType != null) {
//				// 获取openid
//				// 获取openid
//				String openid = AlipayService.getOpenIdFromCookie(request);
//				if (URLTYPE_WLZX.equals(urlType)) {
//					url = KasiteConfig.getKasiteWlzxUrl();
//					// 网络咨询
//					String opkey = MD5Util.md5Encode(openid + "yyqlcsyy", "UTF-8").toUpperCase();
//					// url = url
//					// +"?platformType=57&sourceType=1&sourceId=1024727&openid="+openid+"&opkey="+opkey;
//					url = url + "&openid=" + openid + "&opkey=" + opkey + "&resereType=" + 2;
//
//				} else if (URLTYPE_ZNDZ.equals(urlType)) {
//					// 智能导诊
//					url = KasiteConfig.getKasiteZndzUrl();
//					url = url + "&openid=" + openid + "&resereType=" + 2;
//				} else {
//				}
			}
			sendRedirect(response,url);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			throw e;
		}

	}
	
	public static void main(String[] args) throws Exception {
		// aplipayAccessTokenJosnObj{"accessToken":"authbseB745a450d80c6427f8f0699f4a9ca8B2
		// 8","expiresIn":"31536000","userId":"","reExpiresIn":"31536000","
		// refreshToken":""}
//		ConfigUtil.getInstance();
		AlipayUserInfoShareResponse userinfoShareResponse = AlipayService.getUserInfo("","2088012488506215",
				"authusrB00e4a8cc30d44f3bb07f7848ddf9aX21");
		KasiteConfig.print("userinfoShareResponse" + JSONObject.fromObject(userinfoShareResponse).toString());
	}

}
