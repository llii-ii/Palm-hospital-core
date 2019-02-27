package com.kasite.client.wechat.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.HttpsClientUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.HttpClientService;
import com.kasite.core.common.util.wechat.MD5Util;
import com.kasite.core.common.util.wechat.Ticket;
import com.kasite.core.common.util.wechat.constants.WeiXinConstant;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.yihu.hos.util.JSONUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 微信服务
 * 
 * @author Administrator
 */
public class WeiXinService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY_WX);
//
//	/**
//	 * 获取支付信息
//	 * 
//	 * @param appid
//	 * @param secret
//	 * @param menuJson
//	 * @return
//	 * @throws Exception
//	 */
//	public static JSONObject getPrepayID(ApiModule.WeChat api,String url, String content) throws Exception {
//
//		JSONObject json = HttpClientService.doHttpsPostForPay(api,url, content);
//		return json;
//	}

//	/**
//	 * ͨ��OAuthЭ���获取用户信息
//	 * 
//	 * @param openid
//	 * @param refreshToken
//	 * @param appid
//	 * @return
//	 */
//	public static JSONObject getUserDetailFromOAuth(String code, String appid, String secret) throws Exception {
//
//		JSONObject json = getAuthToken(code, appid, secret);
//		String openid = json.getString("openid");
//		String accessToken = json.getString("access_token");
//		String url = WeiXinConstant.OAUTH2_ACCESS_TOKEN_USER_URL+ "&openid=" + openid;
//		return HttpClientService.doHttpsGet(ApiModule.WeChat.sns_userinfo,url);
//	}
//
	/**
	 * ͨ��OAuthЭ���获取网页授权token
	 * 
	 * @param openid
	 * @param refreshToken
	 * @param appid
	 * @return
	 */
	public static JSONObject getAuthToken(String code, String wxKey) throws Exception {
		StringBuffer url = new StringBuffer();
		String appid = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, wxKey);
		String secret = KasiteConfig.getWxConfig(WXConfigEnum.wx_secret, wxKey);
		url.append(WeiXinConstant.OAUTH2_ACCESS_TOKEN_URL).append("?appid=").append(appid);
		url.append("&secret=").append(secret).append("&code=").append(code).append("&grant_type=authorization_code");
		JSONObject json = HttpClientService.doHttpsGet(ApiModule.WeChat.sns_oauth2_access_token,url.toString(),null);
//		LogBody logBody = new LogBody();
//		logBody.set("info", "获取token");
//		logBody.set("resp", json);
//		LogUtil.info(log, logBody);
		return json;
	}
	/**
	 * 
	 * 创建一个临时二维码
	 * 
	 * @param access_token 微信token
	 * @param expire_seconds 失效事件 以秒为单位
	 * @param scene_str 二维码主键id
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject createTempQrCode(String wxKey,int expire_seconds,String scene_str) throws Exception {
		//{"expire_seconds": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
		//{"expire_seconds": 604800, "action_name": "QR_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
		StringBuffer url = new StringBuffer();
		url.append(WeiXinConstant.CREATE_QRCODE_URL);
		com.alibaba.fastjson.JSONObject param = new com.alibaba.fastjson.JSONObject();
		param.put("expire_seconds", expire_seconds);
		param.put("action_name", "QR_STR_SCENE");
		com.alibaba.fastjson.JSONObject action_info = new com.alibaba.fastjson.JSONObject();
		com.alibaba.fastjson.JSONObject scene = new com.alibaba.fastjson.JSONObject();
		scene.put("scene_str", scene_str);
		action_info.put("scene", scene);
		param.put("action_info", action_info);
		/*
		返回   {"ticket":"gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm
3sUw==","expire_seconds":60,"url":"http://weixin.qq.com/q/kZgfwMTm72WWPkovabbI"}
		 */
		return HttpClientService.doHttpsPost(ApiModule.WeChat.qrcode_create,url.toString(),wxKey, param.toJSONString());
	}
	/** 
	 * 
	 * 创建一个永久二维码
	 * 
	 * @param access_token 微信token
	 * @param scene_str 场景值id
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject createLimitQrCode(String wxKey,String scene_str) throws Exception {
		StringBuffer url = new StringBuffer();
		url.append(WeiXinConstant.CREATE_QRCODE_URL);
		com.alibaba.fastjson.JSONObject param = new com.alibaba.fastjson.JSONObject();
		//{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
		param.put("action_name", "QR_LIMIT_STR_SCENE");
		com.alibaba.fastjson.JSONObject action_info = new com.alibaba.fastjson.JSONObject();
		com.alibaba.fastjson.JSONObject scene = new com.alibaba.fastjson.JSONObject();
		scene.put("scene_str", scene_str);
		action_info.put("scene", scene);
		param.put("action_info", action_info);
		return HttpClientService.doHttpsPost(ApiModule.WeChat.qrcode_create,url.toString(),wxKey, param.toJSONString());
	}
	/**
	 * 
	 * 通过微信的地址下载一个微信二维码并保存到本地
	 * 
	 * @param access_token 微信token
	 * @param expire_seconds 失效事件 以秒为单位
	 * @param scene_str 二维码主键id
	 * @return
	 * @throws Exception 
	 */
	public static boolean getQrPicByTick(String ticket,File file) throws Exception {
		StringBuffer url = new StringBuffer();
		url.append(WeiXinConstant.SHOWQRCODE).append("?ticket=").append(ticket);
		HttpRequestBus.create(url.toString(), RequestType.get).downLoad(file);//send();
		if( !file.exists() || !file.isFile()) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * 长链接转短链接接口
	 * 
	 * @param access_token 微信token
	 * @param expire_seconds 失效事件 以秒为单位
	 * @param scene_str 二维码主键id
	 * @return
	 * @throws Exception 
	 */
	public static String getShortUrl(String wxKey,String long_url) throws Exception {
		//action	是	此处填long2short，代表长链接转短链接
		//		long_url	是	需要转换的长链接，支持http://、https://、weixin://wxpay 格式的url
		com.alibaba.fastjson.JSONObject content = new com.alibaba.fastjson.JSONObject();
		content.put("action", "long2short");
		content.put("long_url", long_url);
		JSONObject json = HttpClientService.doHttpsPost(ApiModule.WeChat.shorturl,WeiXinConstant.SHORTURL,
				wxKey,content.toJSONString());
		if(json.containsKey("errcode") && json.getInt("errcode") != 0) {
			throw new RRException("请求微信端接口异常："+json.toString());
		}else {
			return json.getString("short_url");
		}
		
	}
	/**
	 * 获取网页授权支付地址
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String getAuthPay(String appId,String param) throws Exception {
		StringBuffer url = new StringBuffer();
		url.append(WeiXinConstant.CODE_URL).append("?appid=").append(appId);
		url.append("&redirect_uri=").append(URLEncoder.encode(param, "UTF-8"));
		url.append("&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
		return url.toString();
	}

	/**
	 * 获取用户信息
	 * 
	 * @param openid
	 * @param token
	 * @return
	 */
	public static JSONObject getUserInfo(String openid, String wxKey) throws Exception {
		String url = "";
		url = WeiXinConstant.USER_URL+ "&openid=" + openid;
		JSONObject json = HttpClientService.doHttpsGet(ApiModule.WeChat.user_info,url,wxKey);
		return json;
	}

	/**
	 * 网页授权时拉取用户信息 如果网页授权作用域(scope)为snsapi_userinfo
	 * 
	 * @param openid
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getSnsapiUserInfo(String openid, String access_token) throws Exception {
		String url = "";
		url = WeiXinConstant.GET_USERINFO_URL  + "&openid=" + openid + "&lang=zh_CN";
		JSONObject json = HttpClientService.doHttpsGet(ApiModule.WeChat.sns_userinfo,MessageFormat.format(url, access_token),null);
		return json;
	}

	/**
	 * 创建菜单
	 * 
	 * @param appid
	 * @param secret
	 * @param menuJson
	 * @return
	 * @throws Exception
	 */
	public static JSONObject createMenu(String wxKey, String menuJson) throws Exception {
		getMenu(wxKey);
		String url = WeiXinConstant.ADDMENU_URL;
		JSONObject json = HttpClientService.doHttpsPost(ApiModule.WeChat.menu_create,url,wxKey, menuJson);
		Integer code = json.get("errcode") == null ? -9 :json.getInt("errcode");
		WeiXinConstant.errorsCallback(json,code);
		return json;
	}
	
	/**
	 * 获取菜单
	 * 
	 * @param appid
	 * @param secret
	 * @param menuJson
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getMenu(String wxKey) throws Exception {
		
//		JSONObject json = WeiXinConstant.menus.get(appid + secret);
//		if (StringUtil.isNotEmpty(json)) {
//			return json;
//		} else {
			String url = WeiXinConstant.GETMENU_URL;
			JSONObject ret = HttpClientService.doHttpsGet(ApiModule.WeChat.menu_get,url,wxKey);
//			int code = JSONUtil.getJsonInt(ret, "errcode", false);
//			if (code == 0) {
//				WeiXinConstant.menus.put(appid + secret, ret);
//			}
			return ret;
//		}
	}

	/**
	 * 删除菜单
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public static JSONObject delMenu(String wxKey) throws Exception {
		String url = WeiXinConstant.DELMENU_URL ;
		JSONObject json = new JSONObject();
		getMenu(wxKey);
		int code = HttpClientService.doHttpsGet(ApiModule.WeChat.menu_delete,url,wxKey).getInt("errcode");
		WeiXinConstant.errorsCallback(json, code);
		if (code == 0) {
			// 成功返回
//			WeiXinConstant.menus.remove(appid + secret);
		}
		return json;
	}

	/**
	 * 获得ticket
	 * 
	 * @param appid
	 * @param secret
	 * @param type
	 *            (config签名jsapi_ticket：jsapi，微信卡券api_ticket：wx_card)
	 * @return
	 * @throws Exception
	 */
	public static String getTicket(String wxKey, String type) throws Exception {
		String appid = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, wxKey);
		String secret = KasiteConfig.getWxConfig(WXConfigEnum.wx_secret, wxKey);
		String key = appid + secret + type;
		Ticket ticket = WeiXinConstant.tickets.get(key);
		if (StringUtil.isNotEmpty(ticket)) {

			long currentTime = System.currentTimeMillis();
			if (currentTime >= ticket.getTime()) {
				// 超时
				// 请求微信获得ticket
				return getTicketForWeiXin(wxKey, type).getValue();
			} else {
				// 未超时
				return ticket.getValue();
			}
		} else {
			// 请求微信获得ticket
			return getTicketForWeiXin(wxKey, type).getValue();
		}
	}
	/**
	 * 下载微信对账单
	 * @param WxConfig
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String downloadbill(String wxKey, java.util.Date date) throws Exception {
		String context = getXmlInfo(wxKey, date);
		KasiteConfig.print(context);
		String url = WeiXinConstant.DOWNLOADBILL;
		return HttpsClientUtils.httpsPost(ApiModule.WeChat.pay_downloadbill, url,context);
	}

	public static String getXmlInfo(String wxKey, java.util.Date date)
			throws Exception {
		// 公众账号id
		// String appid = map.get("accountid").toString();
//		 String appid = "wx53cf738a20d74bb5";
		
		String appid = KasiteConfig.getWxPay(WXPayEnum.wx_app_id, wxKey);//wxConfig.getWx_app_id();
		// 商户号
		// String bargainor_id = map.get("merchantid").toString();
		// String bargainor_id = "1312955301";
		String bargainor_id =  KasiteConfig.getWxPay(WXPayEnum.wx_mch_id, wxKey);//wxConfig.getWx_merchantId();
		// 子商户号(受理模式下必填)
		// Object sub_mch_id_obj = map.get("sub_merchantid");
		String sub_mch_id = KasiteConfig.getWxPay(WXPayEnum.wx_parent_mch_id, wxKey);//wxConfig.getWx_parentMerchantId();
		// 密钥
		// String key = map.get("merchantkey").toString();
		// String key = "f11ccc57788046239e3225eb37dfbe63";
		String key =  KasiteConfig.getWxPay(WXPayEnum.wx_mch_key, wxKey);//wxConfig.getWx_merchantKey();
		// 账单日期
		String bill_date = DateFormatUtils.format(date, "yyyyMMdd");
		// 随机字符串
		String nonce_str = (UUID.randomUUID().toString().replaceAll("-", "")
				.toUpperCase());
		StringBuilder sb1 = new StringBuilder();
		sb1.append("appid=" + appid);
		sb1.append("&bill_date=" + bill_date);
		sb1.append("&bill_type=ALL");
		sb1.append("&mch_id=" + bargainor_id);
		sb1.append("&nonce_str=" + nonce_str);
		if (StringUtils.isNotBlank(sub_mch_id)) {
			sb1.append("&sub_mch_id=" + sub_mch_id);
		}
		sb1.append("&key=" + key);

		KasiteConfig.print(sb1.toString());
		String sign = MD5Util.md5Encode(sb1.toString(), "").toUpperCase();
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<appid>");
		sb.append(appid);
		sb.append("</appid>");
		sb.append("<bill_date>" + bill_date + "</bill_date>");
		sb.append("<bill_type>ALL</bill_type>");
		sb.append("<mch_id>");
		sb.append(bargainor_id);
		sb.append("</mch_id>");
		sb.append("<nonce_str>");
		sb.append(nonce_str);
		sb.append("</nonce_str>");
		if (StringUtils.isNotBlank(sub_mch_id)) {
			sb.append("<sub_mch_id>");
			sb.append(sub_mch_id);
			sb.append("</sub_mch_id>");
		}
		sb.append("<sign>");
		sb.append(sign);
		sb.append("</sign>");
		sb.append("</xml>");
		return sb.toString();
	}
	/**
	 * 获得ticket
	 * 
	 * @param appid
	 * @param secret
	 * @param type
	 *            (config签名jsapi_ticket：jsapi，微信卡券api_ticket：wx_card)
	 * @return
	 * @throws Exception
	 */
	public static Ticket getTicketForWeiXin(String wxKey, String type) throws Exception {
		String appid = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, wxKey);
		String secret = KasiteConfig.getWxConfig(WXConfigEnum.wx_secret, wxKey);
		Ticket ticket = new Ticket();
		String key = appid + secret + type;
		String url = WeiXinConstant.GETTICKET_URL + "&type=" + type;
		JSONObject json = HttpClientService.doHttpsGet(ApiModule.WeChat.ticket_getticket,url,wxKey);
		if (json != null) {
			int expiresIn = JSONUtil.getJsonInt(json, "expires_in", false);
			ticket.setTime(new Date(System.currentTimeMillis() + expiresIn * 1000).getTime());
			ticket.setValue(JSONUtil.getJsonString(json, "ticket"));
			WeiXinConstant.tickets.put(key, ticket);
		}
		return ticket;
	}

	/**
	 * 验证服务器地址的有效性
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSignature(String configKey, HttpServletRequest request) throws Exception {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String token = KasiteConfig.getWxConfig(WXConfigEnum.wx_token, configKey);
		log.info("--signature-->" + signature + "---timestamp-->" + timestamp + "--nonce-->" + nonce
				+ "--token-->" + token);

		String[] tmpArr = { token, timestamp, nonce };
		Arrays.sort(tmpArr);
		String tmpStr = arrayToString(tmpArr);
		tmpStr = encodeSHA1(tmpStr);
		if (tmpStr.equalsIgnoreCase(signature)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * sha1加密
	 * 
	 * @param sourceString
	 * @return
	 */
	public static String encodeSHA1(String sourceString) {
		String resultString = null;
		try {
			resultString = new String(sourceString);
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			resultString = byte2hexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

	public static final String byte2hexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}

	/** 转化 */
	public static String arrayToString(String[] arr) {

		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			bf.append(arr[i]);
		}
		return bf.toString();
	}

	/**
	 * 消息封装
	 * 
	 * @param fromUsername
	 * @param toUsername
	 */
	public static String responseToSubscribe(String fromUsername, String toUsername, String contentStr, String type) {

		String time = System.currentTimeMillis() + "";
		String textTpl = "<xml>" + "<ToUserName><![CDATA[%1$s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%2$s]]></FromUserName>" + "<CreateTime>%3$s</CreateTime>"
				+ "<MsgType><![CDATA[%4$s]]></MsgType>" + "<Content><![CDATA[%5$s]]></Content>"
				+ "<FuncFlag>0</FuncFlag>" + "</xml>";
		String resultStr = String.format(textTpl, fromUsername, toUsername, time, type, contentStr);
		return resultStr;
	}

	
	/**
	 * 转发
	 */
	public void sendRedirect(HttpServletRequest request) {

		/**
		 * var code = 6003; Service.CardNo = '430304197908182056'; Service.CType
		 * = 1; Service.ServiceID = 0; page.PSize = 5; page.PIndex = 0;
		 */
//		String s = "?TransactionCode=6003&model=order&CardNo=430304197908182056&CType=1&ServiceID=0&PSize=5&PIndex=0";
		String toUrl = request.getParameter("toUrl");
		KasiteConfig.print(toUrl);
		request.getRequestDispatcher(toUrl);
	}
	
	public static String getGotoOauthUrl(String configKey,String clientId,String toUrl) throws Exception {
		String hosUrl = KasiteConfig.getKasiteHosWebAppUrl();
		String toUrlStr = hosUrl + toUrl;
		toUrlStr = URLEncoder.encode(toUrlStr, "utf-8");
		return hosUrl + "/weixin/"+clientId+"/"+configKey+"/gotoOauth.do?toUrl="+toUrlStr;
	}

	/**
	 * 创建菜单
	 * 
	 * @throws Exception
	 */
	public static JSONObject initMenu(String wxKey,String clientId) throws Exception {
		JSONObject menuJson = new JSONObject();
		JSONArray button = new JSONArray();
		setSubButton(button, "view", "我的门诊",getGotoOauthUrl(wxKey, clientId, "/business/index/clinicTab.html"),"wdmz");
		setSubButton(button, "view", "我的住院",getGotoOauthUrl(wxKey, clientId, "/business/index/hospitalizationTab.html"),"wdzy");
		setSubButton(button, "view", "个人中心",getGotoOauthUrl(wxKey, clientId, "/business/index/myCenterTab.html"),"grzx");
//		setSubButton(button, "view", "病历复印",getGotoOauthUrl(wxKey, clientId, "/business/mCopy/html/wechat/notice.html"),"grzx");
		
		
		menuJson.put("button", button);
		return createMenu(wxKey, menuJson.toString());
	}

	
	public static JSONObject createYdfeMenu(String wxKey,String clientId) throws Exception {
		JSONObject menuJson = new JSONObject();
		JSONArray btnArray = new JSONArray();
		
		JSONArray secondBtn_1 = new JSONArray();
		setSubButton(secondBtn_1, "view", "预约挂号", getGotoOauthUrl(wxKey, clientId, "/business_59/hospitalInfo/hospitalDistrict.html?toType=yygh"));
		setSubButton(secondBtn_1, "view", "门诊缴费", getGotoOauthUrl(wxKey, clientId, "/business/outpatientDept/outpatientCardAccount.html"));
		setSubButton(secondBtn_1, "view", "排队候诊", getGotoOauthUrl(wxKey, clientId, "/business/queue/queueList.html"));
		setSubButton(secondBtn_1, "view", "门诊结算", getGotoOauthUrl(wxKey, clientId, "/business_59/order/orderSingleSettlementList.html"));
		setSubButton(secondBtn_1, "view", "检查预约", getGotoOauthUrl(wxKey, clientId, "/business/yjyy/yjyy_lablist.html"));
		
		JSONObject firstBtnJs1 = new JSONObject();
		firstBtnJs1.put("sub_button", secondBtn_1);
		firstBtnJs1.put("name", "就医服务");
		btnArray.add(firstBtnJs1);
		
		
		JSONArray secondBtn_2 = new JSONArray();
		setSubButton(secondBtn_2, "view", "门诊信息", getGotoOauthUrl(wxKey, clientId, "/business/index/clinicTab.html"));
		setSubButton(secondBtn_2, "view", "住院信息", getGotoOauthUrl(wxKey, clientId, "/business/index/hospitalizationTab.html"));
		setSubButton(secondBtn_2, "view", "预约记录", getGotoOauthUrl(wxKey, clientId, "/business/order/orderLocalList.html?queryServiceId=0"));
		setSubButton(secondBtn_2, "view", "报告查询", getGotoOauthUrl(wxKey, clientId, "/business/report/reportIndex.html"));
		setSubButton(secondBtn_2, "view", "清单查询", getGotoOauthUrl(wxKey, clientId, "/business_59/order/orderSettlementLocalList.html"));
		
		JSONObject firstBtnJs2 = new JSONObject();
		firstBtnJs2.put("sub_button", secondBtn_2);
		firstBtnJs2.put("name", "个人中心");
		btnArray.add(firstBtnJs2);
		
		JSONArray secondBtn_3 = new JSONArray();
		setSubButton(secondBtn_3, "view", "医院简介", getGotoOauthUrl(wxKey, clientId, "/business_59/hospitalInfo/hospitalDistrict.html?toType=yygk"));
		setSubButton(secondBtn_3, "view", "科室介绍", getGotoOauthUrl(wxKey, clientId, "/business_59/hospitalInfo/hospitalDistrict.html?toType=ksjs"));
		setSubButton(secondBtn_3, "view", "来院导航", getGotoOauthUrl(wxKey, clientId, "/business_59/hospitalInfo/hospitalDistrict.html?toType=lydh"));
		setSubButton(secondBtn_3, "view", "院内导航", getGotoOauthUrl(wxKey, clientId, "https://his.ipalmap.com/send-template-new/index.html#/search?name=fujianyikedafueryuandonghai"));
		setSubButton(secondBtn_3, "view", "满意度调查", getGotoOauthUrl(wxKey, clientId, "/business/survey/survey_home.html"));
		
		JSONObject firstBtnJs3 = new JSONObject();
		firstBtnJs3.put("sub_button", secondBtn_3);
		firstBtnJs3.put("name", "关于医院");
		btnArray.add(firstBtnJs3);
		
		menuJson.put("button", btnArray);
		return createMenu(wxKey, menuJson.toString());
	}
	
	
	/**
	 * 设置子菜单
	 * 
	 * @param subButton
	 * @param type
	 *            类型
	 * @param name
	 *            名称
	 * @param url
	 *            地址
	 */
	public static void setSubButton(JSONArray subButton, String type, String name, String url) {
		JSONObject menu = new JSONObject();
		menu.put("type", type);
		menu.put("name", name);
		menu.put("url", url);
		subButton.add(menu);
	}

	/**
	 * 设置子菜单
	 * 
	 * @param subButton
	 * @param type
	 *            类型
	 * @param name
	 *            名称
	 * @param url
	 *            地址
	 */
	public static void setSubButton(JSONArray subButton, String type, String name, String url, String key) {
		JSONObject menu = new JSONObject();
		menu.put("type", type);
		menu.put("name", name);
		menu.put("url", url);
		menu.put("key", key);
		subButton.add(menu);
	}

	/**
	 * 发送客户消息
	 * 
	 * @param accessToken
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static JSONObject sendCustomMessage(String wxKey, String content) throws Exception {
		String url = WeiXinConstant.SEND_CUSTOM_MESSAGE_URL;
		return HttpClientService.doHttpsPost(ApiModule.WeChat.message_custom_send,url, wxKey,content);
	}
	/**
	 * 发送客户消息，通过wxKey获取accessToken后发送
	 * @Description: 
	 * @param wxKey
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static JSONObject sendCustomMessageByWxKey(String wxKey,String content) throws Exception {
		return sendCustomMessage(wxKey, content);
	}

	/**
	 * 发送模板消息
	 * 
	 * @param accessToken
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static JSONObject sendTemplateMessage(String wxKey, String content) throws Exception {
		String url = WeiXinConstant.SEND_TEMPLATE_MESSAGE_URL;
		return HttpClientService.doHttpsPost(ApiModule.WeChat.message_template_send,url,wxKey, content);
	}
	/**
	 * 发送模板消息，通过wxKey获取accessToken后发送
	 * @Description: 
	 * @param wxKey
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static JSONObject sendTemplateMessageByWxKey(String wxKey, String content) throws Exception {
		return sendTemplateMessage(wxKey, content);
	}
	
	/**
	 * 获取临时素材
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @return
	 * @throws Exception
	 */
	public static void getMedia(String wxKey,String mediaId, String savePath) throws Exception {
		String requestUrl = WeiXinConstant.GETMEDIA_URL + "&media_id=" + mediaId;
		HttpClientService.doHttpsGet(ApiModule.WeChat.media_get, requestUrl, wxKey, savePath);
	}

	/**
	 * 查询所有分组
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getGroups(String wxKey) throws Exception {
		String url = WeiXinConstant.GET_GROUP_URL;
		return HttpClientService.doHttpsGet(ApiModule.WeChat.groups_get,url,wxKey);
	}

	/**
	 * 获取用户列表
	 * 
	 * @param appid
	 * @param secret
	 * @param nextOpenid
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getUserList(String wxKey, String nextOpenid) throws Exception {
		String nextopenid = StringUtil.isEmpty(nextOpenid) ? "" : ("&next_openid=" + nextOpenid);
		String url = WeiXinConstant.GET_USERLIST_URL + nextopenid;
		return HttpClientService.doHttpsGet(ApiModule.WeChat.user_get,url,wxKey);
	}

	/**
	 * 批量获取用户信息
	 * 
	 * @param appid
	 * @param secret
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static JSONObject batchGetUserInfo(String wxKey, String jsonStr) throws Exception {
		String url = WeiXinConstant.BATCH_GETUSERINFO_URL;
		JSONObject json = HttpClientService.doHttpsPost(ApiModule.WeChat.user_info_batchget,url,wxKey,jsonStr);
		return json;
	}
	/**
	 * 批量获取微信模板列表
	 * 
	 * @param wxKey
	 * @return
	 * @throws Exception
	 */
	public static JSONObject QueryWxTemplateList(String wxKey) throws Exception {
		String url = WeiXinConstant.GET_ALL_PRIVATE_TEMPLATE;
		JSONObject json = HttpClientService.doHttpsPost(ApiModule.WeChat.GET_ALL_PRIVATE_TEMPLATE,url,wxKey,"");
		return json;
	}
	
	public static Map<String, String> jsapiSign(String jsapiTicket, String url) {
		 Map<String, String> ret = new HashMap<String, String>();
	        String nonce_str = StringUtil.getUUID();
	        String timestamp =  System.currentTimeMillis()/1000+"";
	        String string1;
	        String signature = "";

	        //注意这里参数名必须全部小写，且必须有序
	        string1 = "jsapi_ticket=" + jsapiTicket +
	                  "&noncestr=" + nonce_str +
	                  "&timestamp=" + timestamp +
	                  "&url=" + url;
	        System.out.println(string1);

	        try
	        {
	            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	            crypt.reset();
	            crypt.update(string1.getBytes("UTF-8"));
	            signature = byte2hexString(crypt.digest());
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }

	        ret.put("url", url);
	        ret.put("jsapi_ticket", jsapiTicket);
	        ret.put("nonceStr", nonce_str);
	        ret.put("timestamp", timestamp);
	        ret.put("signature", signature);
	        return ret;
	}
	
	/**
	 * 微信端不做支付相关，只提供服务-linjf 订单 公众账号ID appid 是 String(32) wx8888888888888888
	 * 微信分配的公众账号ID 商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号 微信订单号
	 * transaction_id 否 String(32) 013467007045764 微信的订单号，优先使用 商户订单号
	 * out_trade_no 否 String(32) 1217752501201407033233368018
	 * 商户系统内部的订单号，当没提供transaction_id时需要传这个。 随机字符串 nonce_str 是 String(32)
	 * C380BEC2BFD727A4B6845133519F3AD6 随机字符串，不长于32位。推荐随机数生成算法 签名 sign 是
	 * String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 签名，详见签名生成算法
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 */
	// public static JSONObject queryOrder(String orderID)throws Exception{
	//
	// String url = WeiXinConstant.order_url;
	// SortedMap<String, String> packageParams = new TreeMap<String, String>();
	// packageParams.put("appid", WeiXinConstant.APPID);
	// packageParams.put("mch_id", WeiXinConstant.MCH_ID);
	// packageParams.put("nonce_str",DataUtils.getNonceStr(32));
	// packageParams.put("out_trade_no",orderID);
	//
	// //#.md5编码并转成大写 签名：
	// String sign = DataUtils.createSign(packageParams);
	//
	// //#.最终的提交xml：
	// String xml = DataUtils.setMapToXml(packageParams, sign);
	// return HttpClientService.doPostForPay(url, xml);
	// }

	/**
	 * 微信端不做支付相关，只提供服务-linjf 关闭订单(当未支付，或者取消，网络异常，等等引起的交易失败)
	 * 
	 * @param orderID
	 * @return
	 * @throws Exception
	 */
	// public static JSONObject closeOrder(String orderID)throws Exception{
	//
	// String url = WeiXinConstant.order_close_url;
	// SortedMap<String, String> packageParams = new TreeMap<String, String>();
	// packageParams.put("appid", WeiXinConstant.APPID);
	// packageParams.put("mch_id", WeiXinConstant.MCH_ID);
	// packageParams.put("nonce_str",DataUtils.getNonceStr(32));
	// packageParams.put("out_trade_no",orderID);
	//
	// //#.md5编码并转成大写 签名：
	// String sign = DataUtils.createSign(packageParams);
	//
	// //#.最终的提交xml：
	// String xml = DataUtils.setMapToXml(packageParams, sign);
	// return HttpClientService.doPostForPay(url, xml);
	// }

	/**
	 * 退款 微信端不做退费，只提供服务-linjf
	 * 
	 * @param orderID
	 * @param operatorID
	 * @return
	 * @throws Exception
	 */
	// public static JSONObject refund(String orderID,String operatorID)throws
	// Exception{
	//
	// JSONObject json = queryOrder(orderID);
	// // "result_code": "SUCCESS", "return_code": "SUCCESS",
	// String resultCode = JSONUtil.getJsonString(json, "result_code");
	// String returnCode = JSONUtil.getJsonString(json, "return_code");
	//
	// if(StringUtil.isNotEmpty(resultCode) && "SUCCESS".equals(resultCode)
	// && StringUtil.isNotEmpty(returnCode) && "SUCCESS".equals(returnCode)){
	// String url = WeiXinConstant.refund_url;
	// SortedMap<String, String> packageParams = new TreeMap<String, String>();
	// packageParams.put("appid", WeiXinConstant.APPID);
	// packageParams.put("mch_id", WeiXinConstant.MCH_ID);
	//// packageParams.put("device_info", ""); //设备号
	// packageParams.put("nonce_str",DataUtils.getNonceStr(32));
	// packageParams.put("transaction_id",JSONUtil.getJsonString(json,
	// "transaction_id"));
	// packageParams.put("out_trade_no",orderID);
	// packageParams.put("out_refund_no",orderID);//退款单号
	// packageParams.put("total_fee",JSONUtil.getJsonString(json,
	// "total_fee"));//退款单号
	// packageParams.put("refund_fee",JSONUtil.getJsonString(json,
	// "cash_fee"));// cash_fee 现金支付金额
	// packageParams.put("refund_fee_type",JSONUtil.getJsonString(json,
	// "fee_type"));// 币种
	// packageParams.put("op_user_id",operatorID);// 操作员
	//
	// //#.md5编码并转成大写 签名：
	// String sign = DataUtils.createSign(packageParams);
	//
	// //#.最终的提交xml：
	// String xml = DataUtils.setMapToXml(packageParams, sign);
	// return HttpClientService.doPostForSSL(url, xml);
	// }else{
	// return json;
	// }
	// }

	public static void main(String[] args) throws Exception {
		// String orderID = "201506251511520";
		// ConfigUtil.print(refund(orderID,"84529007"));
		// ConfigUtil.print("GgUuOo000410GUOtjhhkjGUO00041011".length());

		// ConfigUtil.print("oqsJyuHhhSubxSCuLHK6OD8I_9J8".length());

		// refund();

		// 啊啊
		// ConfigUtil.print(delMenu(WeiXinConstant.appid,
		// WeiXinConstant.secret));
		// String a =
		// "{\"button\":[{\"name\":\"大菜单\",\"sub_button\":[{\"name\":\"链接\",\"sub_button\":[],\"type\":\"view\",\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/hospital/hospital.html\"},{\"name\":\"事件\",\"sub_button\":[],\"type\":\"click\",\"key\":\"11\"}]},{\"name\":\"测试6\",\"sub_button\":[{\"name\":\"测试2\",\"sub_button\":[],\"type\":\"click\",\"key\":\"12\"}]},{\"name\":\"链接\",\"sub_button\":[],\"type\":\"view\",\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/wxpay/pay.html\"}]}";
		// String a =
		// "{\"button\":[{\"name\":\"服务\",\"sub_button\":[{\"name\":\"获取openid\",\"type\":\"view\",\"key\":null,\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxee7eaeb7c0cd0526&redirect_uri=http%3A%2F%2Fqlctest.yihu.com.cn%2FHosWiKi%2Fweixin_getCode.do&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect\"},{\"name\":\"支付测试\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/wxpay/pay.html\"},{\"name\":\"挂号\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/weixin_sendRedirect.do?toUrl=qlctest.yihu.com.cn/HosWiKi/business/weixin_sendRedirect.do\"},{\"name\":\"查看报告单\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/\"},{\"name\":\"排队叫号\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/\"}]},{\"name\":\"关于医院\",\"sub_button\":[{\"name\":\"医院介绍\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/hospital/hospital.html\"},{\"name\":\"科室介绍\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/hospital/hosdept.html\"},{\"name\":\"科室分布\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/hospital/change_dept.html\"},{\"name\":\"医院动态\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/hospital/hospital_detail.html\"},{\"name\":\"院外导航\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/\"}]},{\"name\":\"我的\",\"sub_button\":[{\"name\":\"就诊人管理\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/\"},{\"name\":\"住院号管理\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/\"},{\"name\":\"待支付订单\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/\"},{\"name\":\"个人中心\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/\"},{\"name\":\"满意度调查\",\"type\":\"view\",\"key\":null,\"url\":\"http://qlctest.yihu.com.cn/HosWiKi/business/\"}]}]}";
		// ConfigUtil.print(createMenu(WeiXinConstant.appid,
		// WeiXinConstant.secret, a));
		// ConfigUtil.print(getMenu(WeiXinConstant.appid,
		// WeiXinConstant.secret));

		// String a = "oYOqfuCOloSF_OWOeYBbGAyXdS3o";
		// String tokenStr = getToken(WeiXinConstant.appid,
		// WeiXinConstant.secret);

		// ConfigUtil.print(getUserInfo(a, tokenStr));

		// ConfigUtil.print(WeiXinConstant.menus.get(WeiXinConstant.appid+""+WeiXinConstant.secret));

		// JSONObject a = JSONObject.fromObject("{\"errmsg\":\"menu no
		// exist\",\"1\":46003}");

		// ConfigUtil.print(JSONUtil.getJsonInt(a, "errcode",false));

		// ConfigUtil.print(delMenu(WeiXinConstant.appid,
		// WeiXinConstant.secret));
		//
		// ConfigUtil.print(getMenu(WeiXinConstant.appid,
		// WeiXinConstant.secret));

		// ConfigUtil.print(responseToSubscribe("fromUsername",
		// "toUsername","��ע�ɹ�","text"));

		// String openid = "ok94Ss7ecEs_Eja6DY3etZI9tZjQ";
		// String token =
		// "la8j8LMJpwVRdCwxFNTAEucB2pQh5wXES9IR62UI-BRVn7mLOThJ7MIr8a3HOIxI2upqUA-oy9sV8km6_mnLWnQtzMQfiKCguHFMuebKqv4";
		//
		// ConfigUtil.print(getUserInfo(openid, token).getString("openid"));

		// String appid = "wx7b739a344a69a410";
		// String secret = "9296286bb73ac0391d2eaf2b668c668a";
		// ConfigUtil.print(getToken(appid, secret));

		// String a = "{\"access_token\":\"aaaa\",\"expires_in\":7200}";
		// String b = "{\"errcode\":40002,\"errmsg\":\"invalid grant_type\"}";
		// //

		// JSONObject jsona = new JSONObject(a);
		// JSONObject jsonb = new JSONObject(b);

		// ConfigUtil.print(StringUtil.isEmpty(JSONUtil.getJsonString(jsona,
		// "errmsg")));
		// ConfigUtil.print(JSONUtil.getJsonInt(jsona, "expires_in1", false) );
		// ConfigUtil.print(StringUtil.isEmpty(JSONUtil.getJsonInt(jsona,
		// "expires_in1", false) ) );
		// ConfigUtil.print(JSONUtil.getJsonString(jsona, "access_token"));

		// ConfigUtil.print(getToken(SysConstant.appid, SysConstant.secret));
		//
		// String tokenStr =
		// "KtUP7DiwzFy9PNxslAo_WAwfbdhkVm9TZZFiKthje2TfaxbktO5c-wbddf7cz9gVsU8ane1X-cUy5dScJfSQk3BScAOCVmMpooH-2XgkfMI";
		// String url = WeiXinConstant.getMenu_url+"?access_token="+tokenStr;
		// JSONObject ret = HttpClientService.doGet(url);
		// ConfigUtil.print(ret);

		// String oauthAccessToken="oauthAccessToken";

		// ConfigUtil.print(WeiXinConstant.oauth2_access_token_user_url+"?access_token="+oauthAccessToken+"&openid=openid");

		// ConfigUtil.print(getUserInfo("openid", "token"));
		// https://api.weixin.qq.com/cgi-bin/user/info?lang=zh_CN&access_token=token&openid=openid
		// https://api.weixin.qq.com/sns/userinfo?access_token=oauthAccessToken&openid=openid

		// ConfigUtil.print(WeiXinConstant.oauth2_url+WeiXinConstant.appid+"&secret="+WeiXinConstant.secret+"&code=code&grant_type=authorization_code";);
		// http%3A%2F%2Fwxpay.weixin.qq.com%2Fpub_v2%2Fpay%2Fnotify.v2.php
		// http%3A%2F%2Fwxpay.weixin.qq.com%2Fpub_v2%2Fpay%2Fnotify.v2.php
		// ConfigUtil.print(URLEncoder.encode("params={\"productName\":\"apple\",\"productPrice\":1,\"productRemark\":\"remark\",\"productID\":1102221,\"productNO\":0001}","UTF-8"));
		// ConfigUtil.print(URLDecoder.decode("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxee7eaeb7c0cd0526&redirect_uri=http%3A%2F%2Fqlctest.yihu.com.cn%2FHosWiKi%2Fweixin_getCodeForPay.do?params%3D%7B%22productName%22%3A%22apple%22%2C%22productPrice%22%3A0.01%2C%22productRemark%22%3A%22remark%22%2C%22productID%22%3A1102221%2C%22productNO%22%3A2015143089223423%7D&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect"));
		// ConfigUtil.print(URLDecoder.decode("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxee7eaeb7c0cd0526&redirect_uri=http%3A%2F%2Fqlctest.yihu.com.cn%2FHosWiKi%2Fweixin_getCode&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect"));

		// String code ="0212031c6d5cf8fdb45402e33e06a2cV";
		// ConfigUtil.print(getAuthToken(code,WeiXinConstant.appid,
		// WeiXinConstant.secret));

		// 发送模板信息
		// String url =
		// WeiXinConstant.send_template_message_url+"?access_token="+getToken(WeiXinConstant.appid,WeiXinConstant.secret);

		// 指定用户
		// String xml =
		// "{\"touser\":[\"oYOqfuJaHtKYDo3Zi1PsC6pKTE1o\"],\"msgtype\":
		// \"text\",\"text\": {\"content\": \"hello from boxer.\"}}";

		// 指定用户模板信息
		// String xml =
		// "{\"touser\":\"oYOqfuJaHtKYDo3Zi1PsC6pKTE1o\",\"template_id\":\"0kRe4GDYGJ6ZxCqHzup9qnxThWyKVViLOAE6vfOsR6w\",\"url\":\"http://weixin.qq.com/download\",\"topcolor\":\"#FF0000\",\"data\":{\"first\":{\"value\":\"恭喜你购买成功！\",\"color\":\"#173177\"},\"keynote1\":{\"value\":\"巧克力\",\"color\":\"#173177\"},\"keynote2\":{\"value\":\"39.8元\",\"color\":\"#173177\"},\"keynote3\":{\"value\":\"2014年9月16日\",\"color\":\"#173177\"},\"remark\":{\"value\":\"欢迎再次购买！\",\"color\":\"#173177\"}}}";

		// ConfigUtil.print(HttpClientService.doPost(url, xml));
		// ConfigUtil.print("---->"+getToken("wx55371848f63b6035","6a9db457b851a21cbd41a5493ee02de4"));
		// ConfigUtil.print("---->"+getMenu("wx55371848f63b6035","6a9db457b851a21cbd41a5493ee02de4"));
		// ConfigUtil.print("---->"+delMenu("wx55371848f63b6035","6a9db457b851a21cbd41a5493ee02de4"));
		// String accessToken =
		// WeiXinService.getToken(WeiXinConstant.APPID,WeiXinConstant.SECRET);
		// ConfigUtil.print(accessToken);
		// ConfigUtil.print(getMedia(access_token,
		// "WPcC815oPsvt4czvdiDZrqhWHtseKA5GzqR3JT6ht4NsDob5-0X6yolSMUR9yp1A","http://127.0.0.1:8080\\Hos-Process\\upload\\filePic"));
//		createMenu("wx55371848f63b6035");
//		KasiteConfig.print("end..");//https://cs001.kasitesoft.com/business/index/clinicTab.html
//		String hosUrl = "https://cs001.kasitesoft.com";
//		KasiteConfig.print(hosUrl+"/weixin/gotoOauth.do?toUrl="+URLEncoder.encode(hosUrl+"/business/index/clinicTab.html"));
		
		
		JSONObject menuJson = new JSONObject();
		JSONArray btnArray = new JSONArray();
		
		JSONArray secondBtn_1 = new JSONArray();
		setSubButton(secondBtn_1, "view", "检查预约", "http://fjydfe.kasitesoft.com/business/yjyy/yjyy_lablist.html");
		setSubButton(secondBtn_1, "view", "门诊结算", "http://fjydfe.kasitesoft.com/business_59/order/orderSingleSettlementList.html");
		setSubButton(secondBtn_1, "view", "排队候诊", "http://fjydfe.kasitesoft.com/business/queue/queueList.html");
		setSubButton(secondBtn_1, "view", "门诊缴费", "http://fjydfe.kasitesoft.com/business/outpatientDept/outpatientCardAccount.html");
		setSubButton(secondBtn_1, "view", "预约挂号", "http://fjydfe.kasitesoft.com/business_59/hospitalInfo/hospitalDistrict.html?toType=yygh");
		
		JSONObject firstBtnJs1 = new JSONObject();
		firstBtnJs1.put("sub_button", secondBtn_1);
		firstBtnJs1.put("name", "就医服务");
		btnArray.add(firstBtnJs1);
		
		
		JSONArray secondBtn_2 = new JSONArray();
		setSubButton(secondBtn_2, "view", "清单查询", "http://fjydfe.kasitesoft.com/business_59/order/orderSettlementLocalList.html");
		setSubButton(secondBtn_2, "view", "报告查询", "http://fjydfe.kasitesoft.com/business/report/reportIndex.html");
		setSubButton(secondBtn_2, "view", "预约记录", "http://fjydfe.kasitesoft.com/business/order/orderLocalList.html?queryServiceId=0");
		setSubButton(secondBtn_2, "view", "住院信息", "http://fjydfe.kasitesoft.com/business/index/hospitalizationTab.html");
		setSubButton(secondBtn_2, "view", "门诊信息", "http://fjydfe.kasitesoft.com/business/index/clinicTab.html");
		
		JSONObject firstBtnJs2 = new JSONObject();
		firstBtnJs2.put("sub_button", secondBtn_2);
		firstBtnJs2.put("name", "个人中心");
		btnArray.add(firstBtnJs2);
		
		JSONArray secondBtn_3 = new JSONArray();
		setSubButton(secondBtn_3, "view", "满意度调查", "http://fjydfe.kasitesoft.com/business/survey/survey_home.html");
		setSubButton(secondBtn_3, "view", "院内导航", "https://his.ipalmap.com/send-template-new/index.html#/search?name=fujianyikedafueryuandonghai");
		setSubButton(secondBtn_3, "view", "来院导航", "http://fjydfe.kasitesoft.com/business_59/hospitalInfo/hospitalDistrict.html?toType=lydh");
		setSubButton(secondBtn_3, "view", "科室介绍", "http://fjydfe.kasitesoft.com/business_59/hospitalInfo/hospitalDistrict.html?toType=ksjs");
		setSubButton(secondBtn_3, "view", "医院简介", "http://fjydfe.kasitesoft.com/business_59/hospitalInfo/hospitalDistrict.html?toType=yygk");
		
		JSONObject firstBtnJs3 = new JSONObject();
		firstBtnJs3.put("sub_button", secondBtn_3);
		firstBtnJs3.put("name", "关于医院");
		btnArray.add(firstBtnJs3);
		
		menuJson.put("button", btnArray);
		System.out.println(menuJson.toString());
	}

}
