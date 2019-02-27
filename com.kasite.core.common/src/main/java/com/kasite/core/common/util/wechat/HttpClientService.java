package com.kasite.core.common.util.wechat;


import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.DataUtils;
import com.kasite.core.common.util.HttpsClientUtils;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wechat.constants.WeiXinConstant;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.hos.util.JSONUtil;

import net.sf.json.JSONObject;

/**
 * http客户端服务
 * @author Administrator
 *
 */
public class HttpClientService {
	private static final String UNKNOWN = "unknown";
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY_WX);

	/**
	 * 获取客户端ip地址
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	} 
	public static JSONObject doHttpsGet(ApiModule.WeChat api,String url,String wxKey) throws Exception{
		//ConfigUtil.print("--get-->"+URL); configKey ==  null的时候就是不获取token的时候
		String callUrl = url;
		if(null != wxKey) {
			String access_token = getToken(wxKey);
			if(null != access_token && !"".equals(access_token)) {
				callUrl = MessageFormat.format(url, access_token);
			}else {
				throw new RRException("无法获取到微信的 access_token. 请联系管理员。");
			}
		}
		String response = HttpsClientUtils.httpsGet(api,callUrl,null);
		JSONObject json = JSONObject.fromObject(response);
		//如果返回的异常为 token失效 则重新获取一份最新的微信token 再调用一次
		if(null != json && null != json.get("errcode") && 40001 == json.getInt("errcode")) {
			String access_token = getTokenForWeiXin(wxKey);
			if(null != access_token && !"".equals(access_token)) {
				callUrl = MessageFormat.format(url, access_token);
			}else {
				throw new RRException("无法获取到微信的 access_token. 请联系管理员。");
			}
			response = HttpsClientUtils.httpsGet(api,callUrl,null);
			json = JSONObject.fromObject(response);
		}
		return json;
	}
	
	/**
	 * 微信获取临时素材的doHttpsGet请求
	 * */
	public static void doHttpsGet(ApiModule.WeChat api,String url,String wxKey,String materPath) throws Exception{
		//ConfigUtil.print("--get-->"+URL); configKey ==  null的时候就是不获取token的时候
		String callUrl = url;
		if(null != wxKey) {
			String access_token = getToken(wxKey);
			if(null != access_token && !"".equals(access_token)) {
				callUrl = MessageFormat.format(url, access_token);
			}else {
				throw new RRException("无法获取到微信的 access_token. 请联系管理员。");
			}
		}
		String response = HttpsClientUtils.materialDownload(api,callUrl,null,materPath);
		JSONObject json = null;
		char charAt = response.charAt(0);
		char c = '{';
		if (c == charAt) {
			json = JSONObject.fromObject(response);
		}
		//如果返回的异常为 token失效 则重新获取一份最新的微信token 再调用一次
		if(null != json && null != json.get("errcode") && 40001 == json.getInt("errcode")) {
			String access_token = getTokenForWeiXin(wxKey);
			if(null != access_token && !"".equals(access_token)) {
				callUrl = MessageFormat.format(url, access_token);
			}else {
				throw new RRException("无法获取到微信的 access_token. 请联系管理员。");
			}
			response = HttpsClientUtils.materialDownload(api,callUrl,null,materPath);
			//json = JSONObject.fromObject(response);
		}
		//return json;
	}
	
	
	/**
	 * 获取token
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public static String getToken(String wxKey) throws Exception {
		String appid = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, wxKey);
		String secret = KasiteConfig.getWxConfig(WXConfigEnum.wx_secret, wxKey);
		if(StringUtil.isEmpty(appid) || StringUtil.isEmpty(secret)) {
			throw new RRException("未获取到 微信配置信息中的AppId 和 AppSecret 请确认 configkey 是否正确。wxKey="+wxKey);
		}
		String key = appid + secret;
		Token token = WeiXinConstant.tokens.get(key);
		if (StringUtil.isNotEmpty(token)) {
			long currentTime = System.currentTimeMillis();
			if (currentTime >= token.getTime()) {
				// 超时
				// 请求微信获得票据
				return getTokenForWeiXin(appid, secret).getValue();
			} else {
				// 未超时
				return token.getValue();
			}
		} else {
			// 请求微信获得票据
			return getTokenForWeiXin(appid, secret).getValue();
		}
	}

	/**
	 * 重新获取新的微信token 这个方法只有在 getToken 返回 code ===40001 的时候重新发起调用的时候使用。
	 * @param appid
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public static String getTokenForWeiXin(String wxKey) throws Exception {
		String appid = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, wxKey);
		String secret = KasiteConfig.getWxConfig(WXConfigEnum.wx_secret, wxKey);
		Token t = getTokenForWeiXin(appid, secret);
		return t.getValue();
	}
	/**
	 * 异常处理
	 * 
	 * @param json
	 * @throws Exception 
	 */
	private static void throwExcptionByErrorResult(JSONObject json) {
		LogBody logBody = new LogBody(KasiteConfig.createAuthInfoVo(IDSeed.next()));
		logBody.set("info", "异常处理");
		logBody.set("resp", json);
		if (StringUtil.isEmpty(json)) {
			logBody.set("error", "请求微信无响应异常");
			log.info(logBody.toString());
			throw new RRException(RetCode.Common.CallWeiXinError, "请求微信无响应异常");
		} else {
			try {
				String msg = JSONUtil.getJsonString(json, "errmsg");
				int code = JSONUtil.getJsonInt(json, "errcode", false);
				if (StringUtil.isNotEmpty(msg) && code != 0) {
//					logBody.set("error", msg);
//					LogUtil.error(log, logBody);
					log.error(logBody.toString());
					throw new RRException(msg);
				}
			} catch (RRException e) {
				throw e;
			} catch (AbsHosException e) {
				throw new RRException("解析微信返回结果集参数异常:"+json.toString());
			}
		}
	}
	/**
	 * 获取token
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 */
	private static Token getTokenForWeiXin(String appid, String secret) throws Exception {
		String key = appid + secret;
		Token token1 = new Token();
		JSONObject json = new JSONObject();
		IWxAccessTokenService tokenService = SpringContextUtil.getBean(IWxAccessTokenService.class);
		if(null != tokenService) {
			String access_token = tokenService.getWxAccessTokenByAppId(appid, secret);
			int expiresIn = 2400;//缓存微信的1/3的时间 再从服务端获取
			json.put("access_token", access_token);
			json.put("expires_in", expiresIn);
			throwExcptionByErrorResult(json);
		} else {
			String url = WeiXinConstant.token_url + "?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
			json = HttpClientService.doHttpsGet(ApiModule.WeChat.token,url,null);
			throwExcptionByErrorResult(json);
		}
		if(null != json) {
			int expiresIn = JSONUtil.getJsonInt(json, "expires_in", false);
			if(JSONUtil.getJsonString(json, "access_token")!=null&&!"".equals(JSONUtil.getJsonString(json, "access_token"))) {
				token1.setTime(new Date(System.currentTimeMillis() + expiresIn * 1000).getTime());
				token1.setValue(JSONUtil.getJsonString(json, "access_token"));
				WeiXinConstant.tokens.put(key, token1);
			}
			
		}else {
			throw new RRException("获取微信的 access_token 异常");
		}
		return token1;
	
	}
	/**
	 * @param URL 微信请求的url 如果需要传入 access_token 的请求 请在url 后面加上 &access_token={0} 并传入 configKey
	 * @param configKey 获取微信token的必须的id
	 * @param content 推送内容
	 * @param requestMethod POST,GET
	 * @return
	 * @throws Exception
	 */
	public static JSONObject doHttpsPost(ApiModule.WeChat api,String url,String wxKey,String context) throws Exception{
		KasiteConfig.print("--post-->"+url);
		String callUrl = url;
		if(null != wxKey) {
			String access_token = getToken(wxKey);
			if(null != access_token && !"".equals(access_token)) {
				callUrl = MessageFormat.format(url, access_token);
			}else {
				throw new RRException("无法获取到微信的 access_token. 请联系管理员。");
			}
		}
		
		String responsetxt = HttpsClientUtils.httpsPost(api,callUrl,context);
		KasiteConfig.print("Response content:" + responsetxt);
		JSONObject json = JSONObject.fromObject(responsetxt);
		//如果返回的异常为 token失效 则重新获取一份最新的微信token 再调用一次
		if(null != json && null != json.get("errcode") && 40001 == json.getInt("errcode")) {
			String access_token = getTokenForWeiXin(wxKey);
			if(null != access_token && !"".equals(access_token)) {
				callUrl = MessageFormat.format(url, access_token);
			}else {
				throw new RRException("无法获取到微信的 access_token. 请联系管理员。");
			}
			responsetxt = HttpsClientUtils.httpsGet(api,callUrl,null);
			json = JSONObject.fromObject(responsetxt);
		}

		Integer code = json.get("errcode") == null ? -9 :json.getInt("errcode");
		WeiXinConstant.errorsCallback(json, code);
		return json;
	}
	
	/**
	 * 返回xml格式的dopost请求
	 * @param url
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static JSONObject doHttpsPostForPay(ApiModule.WeChat api,String url,String context) throws Exception{
		KasiteConfig.print("--post-->"+url);
		String responsetxt = HttpsClientUtils.httpsPost(api,url,context);
		KasiteConfig.print("Response content:" + responsetxt);
		JSONObject json = JSONObject.fromObject(DataUtils.xml2JSON(responsetxt));
		return json;
	}
	
}
