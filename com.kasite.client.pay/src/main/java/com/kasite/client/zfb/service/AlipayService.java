package com.kasite.client.zfb.service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenPublicFollowBatchqueryRequest;
import com.alipay.api.request.AlipayOpenPublicMenuBatchqueryRequest;
import com.alipay.api.request.AlipayOpenPublicMenuModifyRequest;
import com.alipay.api.request.AlipayOpenPublicMessageCustomSendRequest;
import com.alipay.api.request.AlipayOpenPublicMessageSingleSendRequest;
import com.alipay.api.request.AlipayOpenPublicQrcodeCreateRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayOpenPublicFollowBatchqueryResponse;
import com.alipay.api.response.AlipayOpenPublicMenuBatchqueryResponse;
import com.alipay.api.response.AlipayOpenPublicMenuModifyResponse;
import com.alipay.api.response.AlipayOpenPublicMessageCustomSendResponse;
import com.alipay.api.response.AlipayOpenPublicMessageSingleSendResponse;
import com.alipay.api.response.AlipayOpenPublicQrcodeCreateResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.coreframework.util.ArithUtil;
import com.kasite.client.zfb.bo.ZfbAddMenu;
import com.kasite.client.zfb.constants.AlipayServiceEnvConstants;
import com.kasite.client.zfb.dispatcher.Dispatcher;
import com.kasite.client.zfb.executor.ActionExecutor;
import com.kasite.client.zfb.factory.AlipayAPIClientFactory;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.CookieTool;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil;
import com.kasite.core.common.util.wxmsg.WxMsgUtil.Channel;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.serviceinterface.module.pay.req.ReqAlipayTradePay;



/**
 * @author linjianfa
 * @Description: TODO 支付宝服务
 * @version: V1.0  
 * 2017年10月18日 下午3:22:05
 */
public class AlipayService {
	
	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(AlipayService.class);
	
	/**
	 * 验证服务器地址的有效性
	 * @param signature 
	 * @param timestamp 
	 * @param nonce 
	 * @param token 
	 * @return
	 * @throws Exception 
	 */
	public static boolean checkSignature(String zfbKey,Map<String, String> params) throws AlipayApiException {
		if (!AlipaySignature.rsaCheckV2(params, 
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_alipayPublicKey, zfbKey),
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_charset, zfbKey), 
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signType, zfbKey)
				)) {
			return false;
		}else{
			return true;
		}
	}
	
	/** 
	 * 处理支付宝发来的请求、事件等 
	 * @param request 
	 * @return 
	 * @throws Exception 
	 */  
	public static String processRequest(net.sf.json.JSONObject bizContentJson,AuthInfoVo authInfo,Map<String, String> params) throws Exception {  
		ActionExecutor executor = Dispatcher.getExecutor(bizContentJson,params);
//		AuthInfoVo authInfo = KasiteConfig.getZfbAuthInfo(configKey, params);
		return executor.execute(authInfo);
    }  
	
	
	/** 
	 * //对响应内容加签
	 * @param request 
	 * @return 
	 * @throws AlipayApiException 
	 * @throws Exception 
	 */  
	public static String encryptAndSign(String zfbKey, String bizContent,String charset,boolean isEncrypt, boolean isSign, String signType) throws AlipayApiException {  
		StringBuilder sb = new StringBuilder();
		if (StringUtil.isBlank(charset)) {
			charset = AlipayConstants.CHARSET_GBK;
		}
		String zfb_appPublicKey = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_alipayPublicKey, zfbKey);//"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlijk7l95GXfUKfglIRgh8Ge7azvWtwnw1ZSfY67kEwU75gsSYsDad1tLw1cOaeU5g9nfosgFraGqo9w5WHnU+Vfzuc2vsBIhcMym50hrz/tBMEUcnlnnqwbrfAaQw+PvdaqItwBQynJwupLRh80nkp6j+4OXbc8SB+NlPhY/iAZGw5mrP/+MQLMd3mu6ZvURJriBXMbW81KpU8tcOFaQCY5Yhk6XpEYpcqK8metmT2YGGAE6d0e49ltSdcswVUl5mKADoHI4KbtOQHaq5Klnxwk9TUK/ztgl+8VaXmrGoFnDt+s/0Q1xI6Z6k2N/lhGkbrPE+gA+VU0qHzN06ivapQIDAQAB";
		zfb_appPublicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkSL5R6jKgjiXuo2cOMEK8v3t5Lk2ZLbKz/Q2eUlSeXf8hmcv202ncy/w+kds3ndXnbYUY8POb52kDF3A6+TibhJXq9cYSVT7wh9H36nJ7OkXKBtR1I8ULTW72WSM6k9N3R51QM4qMfWY6yihE/HTz/M3mSqUjbpMBbUIjnO/6LfBG1yhcOYpTZNkF3TYYoHv9Z+nOIHFjRs7gRHict5xahT3hDlehOS465xT47PyVGYmwXaqDTeIcWAw3B91yYJ+xG5F59ugDvHfvxx9QKIExg/TEBCdoLYwHHQGZNNXXGkOwOlp8VcBG3gQjxzoxStx1M2Q6J1cwekbhRptK0qsAwIDAQAB";
		sb.append("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
		if (isEncrypt) {
			// 加密
			sb.append("<alipay>");
			String encrypted = AlipaySignature.rsaEncrypt(bizContent, 
					zfb_appPublicKey
					, charset);
			sb.append("<response>" + encrypted + "</response>");
			sb.append("<encryption_type>AES</encryption_type>");
			if (isSign) {
				String sign = AlipaySignature.rsaSign(encrypted, 
						KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appPrivateKey, zfbKey)
						, charset, signType);
				sb.append("<sign>" + sign + "</sign>");
				sb.append("<sign_type>");
				sb.append(signType);
				sb.append("</sign_type>");
			}
			sb.append("</alipay>");
		} else if (isSign) {
			// 不加密，但需要签名
			sb.append("<alipay>");
			sb.append("<response>" + bizContent + "</response>");
			String sign = AlipaySignature.rsaSign(bizContent, 
					KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appPrivateKey, zfbKey)
					, charset, signType);
			sb.append("<sign>" + sign + "</sign>");
			sb.append("<sign_type>");
			sb.append(signType);
			sb.append("</sign_type>");
			sb.append("</alipay>");
		} else {// 不加密，不加签
			sb.append(bizContent);
		}
		return sb.toString();
    }  
	
	/**
	 * 通过auth_code获取网页授权token对象（实时调用）
	 * @param authCode
	 * @return
	 * @throws Exception
	 */
	public static AlipaySystemOauthTokenResponse getAuthTokenFromAlipay(String zfbKey,String authCode)throws Exception{
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey+"|"+authCode;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);
			AlipaySystemOauthTokenRequest request= new AlipaySystemOauthTokenRequest();
			request.setCode(authCode);
			request.setGrantType("authorization_code");
			AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
			isSuccess = true;
			result = response.getBody();
			return response;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝通过auth_code获取网页授权token对象接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.oauth2_authorize, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
		
	}
	
	/**
	 * 从Cookie获取openid
	 * @param request
	 * @return
	 */
	public static String getOpenIdFromCookie(HttpServletRequest request){
		return CookieTool.getCookieValue(request,KstHosConstant.COMM_OPENID);
	}
	
	/**
	 * 支付宝异步单发消息--文本消息推送
	 * @param content	内容
	 * @return
	 */
	public static net.sf.json.JSONObject sendPublicCustomMessage(String zfbKey,String content){
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey+"|"+content;
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			AlipayOpenPublicMessageCustomSendRequest  request = new AlipayOpenPublicMessageCustomSendRequest ();
			request.setBizContent(content);
			AlipayOpenPublicMessageCustomSendResponse response = AlipayAPIClientFactory.getAlipayClient(zfbKey).execute(request);
            if (null != response && response.isSuccess()) {
                result.put("code", response.getCode());
                result.put("msg", response.getMsg());
            } else {
            	result.put("code", response.getCode());
            	result.put("msg", response.getSubMsg());
            }
            isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝文本消息推送接口调用异常："+reqParamStr,e);
			result.put("code", "-1");
			result.put("msg", e.getLocalizedMessage());
		}finally {
			try {
				LogUtil.saveCallZfbLog(ApiModule.Zfb.message_custom_send, reqParamStr, result.toString(), null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
						RequestType.post, isSuccess);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;

		
	}
	
	/**
	 * 发送模板消息
	 * @param access_token
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static net.sf.json.JSONObject sendSingleMessage(String zfbKey, String content) throws Exception{
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey+"|"+content;
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			AlipayOpenPublicMessageSingleSendRequest  request = new AlipayOpenPublicMessageSingleSendRequest ();
			request.setBizContent(content);
			AlipayOpenPublicMessageSingleSendResponse  response = AlipayAPIClientFactory.getAlipayClient(zfbKey).execute(request);
            if (null != response && response.isSuccess()) {
                result.put("code", response.getCode());
                result.put("msg", response.getMsg());
            } else {
            	result.put("code", response.getCode());
            	result.put("msg", response.getSubMsg());
            }
            isSuccess =true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝发送模板消息接口调用异常："+reqParamStr,e);
			result.put("code", "-1");
			result.put("msg", e.getLocalizedMessage());
		}finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.message_template_send, reqParamStr, result.toString(), null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
		return result;
	}
	
	/**
	 * 获取用户信息
	 * @param openid
	 * @param token
	 * @return
	 */
	public static AlipayUserInfoShareResponse  getUserInfo(String zfbKey,String openid,String token)throws Exception{
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey+"|"+openid+"|"+token;
		String result = "";
		try {
			 AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);
			 AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
			 AlipayUserInfoShareResponse  userinfoShareResponse = alipayClient.execute(request,token);
			 isSuccess = true;
			 result = userinfoShareResponse.getBody();
			 return userinfoShareResponse;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝获取用户信息接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.user_info, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
	}
	/**
	 * 获取用户列表
	 * @param openid
	 * @param token
	 * @return
	 */
	public static AlipayOpenPublicFollowBatchqueryResponse  getUserInfoList(String app_id,String privateKey,String publicKey,String next_user_id)throws Exception{
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",app_id,privateKey,"json","GBK",publicKey,"RSA2");
		AlipayOpenPublicFollowBatchqueryRequest request = new AlipayOpenPublicFollowBatchqueryRequest();
		if (next_user_id != null) {
			request.setBizContent("{" +
					"\"next_user_id\":\""+next_user_id+"\"" +
					"  }");
		}
		AlipayOpenPublicFollowBatchqueryResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
		KasiteConfig.print("调用成功");
		} else {
		KasiteConfig.print("调用失败");
		}
		return response;
	}
	public static String getGotoOauthUrl(String configKey,String clientId,String toUrl) throws Exception {
		String hosUrl = KasiteConfig.getKasiteHosWebAppUrl();
		String toUrlStr = hosUrl + toUrl;
		toUrlStr = URLEncoder.encode(toUrlStr, "utf-8");
		return hosUrl + "/alipay/"+clientId+"/"+configKey+"/gotoOauth.do?toUrl="+toUrlStr;
	}
	/**
	 * 创建菜单
	 * 
	 * @throws Exception
	 */
	public static String initMenu(String wxKey,String clientId) throws Exception {
		JSONObject menuJson = new JSONObject();
		JSONArray button = new JSONArray();
		setSubButton(button, "link", "我的门诊",getGotoOauthUrl(wxKey, clientId, "/business/index/clinicTab.html"));
		setSubButton(button, "link", "我的住院",getGotoOauthUrl(wxKey, clientId, "/business/index/hospitalizationTab.html"));
		setSubButton(button, "link", "个人中心",getGotoOauthUrl(wxKey, clientId, "/business/index/myCenterTab.html"));
		menuJson.put("button", button);
		return createMenu(wxKey, menuJson.toString());
	}
	
	/**
	 * 创建菜单
	 * @throws Exception
	 */
	public static String createMenu(String zfbKey,String memuJson) throws Exception{
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);
			
			AlipayOpenPublicMenuBatchqueryRequest getMenuReq = new AlipayOpenPublicMenuBatchqueryRequest();
			AlipayOpenPublicMenuBatchqueryResponse resp = alipayClient.execute(getMenuReq);
			LogUtil.saveCallZfbLog(ApiModule.Zfb.menu_get, reqParamStr, resp.getBody(), null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
			
	        AlipayOpenPublicMenuModifyRequest request=new AlipayOpenPublicMenuModifyRequest();
			JSONObject menuJson = new JSONObject();
			request.setBizContent(memuJson);	    
			reqParamStr = menuJson.toString();
			AlipayOpenPublicMenuModifyResponse response=alipayClient.execute(request);
			isSuccess = true;
			result = response.getBody();
			return response.getMsg().toString();
//			return resp.getBody();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝创建菜单接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.menu_create, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
       
		
	}
	public static String updateYdfeMenu(String zfbKey,String clientId) throws Exception{
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey;
		String result = zfbKey;
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);

			AlipayOpenPublicMenuModifyRequest request=new AlipayOpenPublicMenuModifyRequest();
			JSONObject menuJson = new JSONObject();
			JSONArray button = new JSONArray();	
			
			JSONArray secondBtn_1 = new JSONArray();
			setSubButton(secondBtn_1, "link", "预约挂号", getGotoOauthUrl(zfbKey,clientId,"/business_59/hospitalInfo/hospitalDistrict.html?toType=yygh"));
			setSubButton(secondBtn_1, "link", "门诊缴费", getGotoOauthUrl(zfbKey,clientId,"/business/outpatientDept/outpatientCardAccount.html"));
			setSubButton(secondBtn_1, "link", "住院缴费", getGotoOauthUrl(zfbKey,clientId,"/business/inHospital/inpatientAccount.html"));
			setSubButton(secondBtn_1, "link", "门诊结算", getGotoOauthUrl(zfbKey,clientId,"/business_59/order/orderSingleSettlementList.html"));
			setSubButton(secondBtn_1, "link", "检查预约", getGotoOauthUrl(zfbKey,clientId,"/business/yjyy/yjyy_lablist.html"));
			
			JSONObject firstBtnJs1 = new JSONObject();
			firstBtnJs1.put("sub_button", secondBtn_1);
			firstBtnJs1.put("name", "就医服务");
			button.add(firstBtnJs1);
			
			
			JSONArray secondBtn_2 = new JSONArray();
			setSubButton(secondBtn_2, "link", "门诊信息", getGotoOauthUrl(zfbKey,clientId,"/business/index/clinicTab.html"));
			setSubButton(secondBtn_2, "link", "住院信息", getGotoOauthUrl(zfbKey,clientId,"/business/index/hospitalizationTab.html"));
			setSubButton(secondBtn_2, "link", "预约记录", getGotoOauthUrl(zfbKey,clientId,"/business/order/orderLocalList.html?queryServiceId=0"));
			setSubButton(secondBtn_2, "link", "报告查询", getGotoOauthUrl(zfbKey,clientId,"/business/report/reportIndex.html"));
			setSubButton(secondBtn_2, "link", "清单查询", getGotoOauthUrl(zfbKey,clientId,"/business_59/order/orderSettlementLocalList.html"));
			
			JSONObject firstBtnJs2 = new JSONObject();
			firstBtnJs2.put("sub_button", secondBtn_2);
			firstBtnJs2.put("name", "个人中心");
			button.add(firstBtnJs2);
			
			
			JSONArray secondBtn_3 = new JSONArray();
			setSubButton(secondBtn_3, "link", "医院简介", getGotoOauthUrl(zfbKey,clientId,"/business_59/hospitalInfo/hospitalDistrict.html?toType=yygk"));
			setSubButton(secondBtn_3, "link", "科室介绍", getGotoOauthUrl(zfbKey,clientId,"/business_59/hospitalInfo/hospitalDistrict.html?toType=ksjs"));
			setSubButton(secondBtn_3, "link", "来院导航", getGotoOauthUrl(zfbKey,clientId,"/business_59/hospitalInfo/hospitalDistrict.html?toType=lydh"));
			setSubButton(secondBtn_3, "link", "院内导航", getGotoOauthUrl(zfbKey,clientId,"https://his.ipalmap.com/send-template-new/index.html#/search?name=fujianyikedafueryuandonghai"));
			setSubButton(secondBtn_3, "link", "满意度调查", getGotoOauthUrl(zfbKey,clientId,"/business/survey/survey_home.html"));
			
			JSONObject firstBtnJs3 = new JSONObject();
			firstBtnJs3.put("sub_button", secondBtn_3);
			firstBtnJs3.put("name", "关于医院");
			button.add(firstBtnJs3);
			
			menuJson.put("button", button);
			
			reqParamStr+="|"+menuJson.toString();
			request.setBizContent(menuJson.toString());	   
			reqParamStr = menuJson.toString();
			AlipayOpenPublicMenuModifyResponse response=alipayClient.execute(request);
			result = response.getBody();
			KasiteConfig.print("修改菜单结果：\r\n"+JSON.toJSONString(response.getBody()));
			return response.getMsg().toString();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝修改菜单接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.menu_update, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
	}
	
	/**
	 * 修改菜单
	 * @throws Exception
	 */
	public static String updateMenu(String zfbKey) throws Exception{
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey;
		String result = zfbKey;
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);

			AlipayOpenPublicMenuModifyRequest request=new AlipayOpenPublicMenuModifyRequest();
	        String hosUrl = KasiteConfig.getKasiteHosWebAppUrl();
			JSONObject menuJson = new JSONObject();
			JSONArray button = new JSONArray();	
			
			setSubButton(button, "link", "我的门诊", hosUrl+"/alipay/gotoOauth.do?toUrl="+URLEncoder.encode(hosUrl+"/business/index/clinicTab.html", "utf-8"));
			setSubButton(button, "link", "我的住院", hosUrl+"/alipay/gotoOauth.do?toUrl="+URLEncoder.encode(hosUrl+"/business/index/hospitalizationTab.html", "utf-8"));
			setSubButton(button, "link", "个人中心", hosUrl+"/alipay/gotoOauth.do?toUrl="+URLEncoder.encode(hosUrl+"/business/index/myCenterTab.html", "utf-8"));
			menuJson.put("button", button);
			reqParamStr+="|"+menuJson.toString();
			request.setBizContent(menuJson.toString());	   
			reqParamStr = menuJson.toString();
			AlipayOpenPublicMenuModifyResponse response=alipayClient.execute(request);
			result = response.getBody();
			KasiteConfig.print("修改菜单结果：\r\n"+JSON.toJSONString(response.getBody()));
			return response.getMsg().toString();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝修改菜单接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.menu_update, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
	}
	/**
	 * 修改菜单
	 * @throws Exception
	 */
	public static String updateMenu(String app_id, String private_key, String alipay_public_key, String signType, String  code_type, List<ZfbAddMenu> list) throws Exception{
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
				app_id,
				private_key,
				"json",
				"GBK",
				alipay_public_key,
				signType);
		AlipayOpenPublicMenuModifyRequest request=new AlipayOpenPublicMenuModifyRequest();
		
		JSONObject menuJson = new JSONObject();
		JSONArray button = new JSONArray();	
		
		JSONArray sub_button1 = new JSONArray();
		 JSONArray sub_button2 = new JSONArray();
		 JSONArray sub_button3 = new JSONArray();
		 JSONObject menu1 = new JSONObject();//菜单一
		 JSONObject menu2 = new JSONObject();//菜单二
		 JSONObject menu3 = new JSONObject();//菜单三
		 JSONObject subMenu = new JSONObject();//用于判断是否有子菜单
		 subMenu.put("subMenu1", "0");
		 subMenu.put("subMenu2", "0");
		 subMenu.put("subMenu3", "0");
		 //先判断每个母菜单是否有子菜单
		 for (int i = 0; i < list.size(); i++) {
			 int location = Integer.parseInt(list.get(i).getMenuLocation());
			 if(10<location && location<20) {subMenu.put("subMenu1", "1");} 
			 if(20<location && location<30) {subMenu.put("subMenu2", "1");}
			 if(30<location) {subMenu.put("subMenu3", "1");}
		 }
		 
		for (int i = 0; i < list.size(); i++) {
			KasiteConfig.print("菜单位置++"+list.get(i).getMenuLocation());
			 int location = Integer.parseInt(list.get(i).getMenuLocation());
			 //菜单一的显示，如果为菜单一且子菜单不为空，则只加那么，如果子菜单为空则直接加载菜单一
			 if(location==1&&subMenu.get("subMenu1").toString().equals("1")) {
				menu1.put("name", list.get(i).getMenuSecondName());
			}
			 if(location==2&&subMenu.get("subMenu2").toString().equals("1")){
				menu2.put("name", list.get(i).getMenuSecondName());
			} 
			if(3==location&&"1".equals(subMenu.get("subMenu3").toString())){
				menu3.put("name", list.get(i).getMenuSecondName());
			}
			/*
			 * 判断子菜单位置
			 */
			if(10<location && location<20 &&subMenu.get("subMenu1").toString().equals("1")) {
				setSubButton(sub_button1, "link", list.get(i).getMenuSecondName(), list.get(i).getMenuUrl());
				menu1.put("sub_button", sub_button1);
			}else if(20<location && location<30 &&subMenu.get("subMenu2").toString().equals("1")) {
				setSubButton(sub_button2, "link", list.get(i).getMenuSecondName(), list.get(i).getMenuUrl());
				menu2.put("sub_button", sub_button2);
			}else if(30<location &&subMenu.get("subMenu3").toString().equals("1")) {
				setSubButton(sub_button3, "link", list.get(i).getMenuSecondName(), list.get(i).getMenuUrl());
				menu3.put("sub_button", sub_button3);
			}
		}
		//判断是否有子菜单
		if(!menu1.isEmpty()) {
			button.add(menu1);
		}
		if(!menu2.isEmpty()) {button.add(menu2);}
		if(!menu3.isEmpty()) {button.add(menu3);}
			/*
			 * 菜单顺序
			 */
		 for (int i = 0; i < list.size(); i++) {
			  int location = Integer.parseInt(list.get(i).getMenuLocation());
			  if(location==1&&"0".equals(subMenu.get("subMenu1").toString())) {
					setSubButton(button, "link", list.get(i).getMenuSecondName(), list.get(i).getMenuUrl());
				}
			  if(location==2&&"0".equals(subMenu.get("subMenu2").toString())) {
					setSubButton(button, "link", list.get(i).getMenuSecondName(), list.get(i).getMenuUrl());
				}
			  if(location==3&&"0".equals(subMenu.get("subMenu3").toString())) {
					setSubButton(button, "link", list.get(i).getMenuSecondName(), list.get(i).getMenuUrl());
				}
		}
		menuJson.put("button", button);
		
		
		request.setBizContent(menuJson.toString());	        
		AlipayOpenPublicMenuModifyResponse response=alipayClient.execute(request);
		KasiteConfig.print("修改菜单结果：\r\n"+JSON.toJSONString(response.getBody()));
		return response.getMsg().toString();
	}
	
	/**
	 * 设置子菜单
	 * @param sub_button
	 * @param type 类型
	 * @param name 名称
	 * @param url 地址
	 */
	public static void setSubButton(JSONArray subButton,String type,String name,String url){
		JSONObject menu = new JSONObject();
		menu.put("action_type", type);
		menu.put("name", name);
		menu.put("action_param", url);
		subButton.add(menu);
	}	
	/**
	 * 生成推广二维码
	 * @throws AlipayApiException 
	 */
	public static String createQrCode(String app_id,String private_key,String alipay_public_key,String signType,String scene_id,String code_type,String show_logo) throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
				app_id,
				private_key,
				"json",
				"GBK",
				alipay_public_key,
				signType);
		AlipayOpenPublicQrcodeCreateRequest request = new AlipayOpenPublicQrcodeCreateRequest();	
		/*
		 * 生成带参数二维码
		 */
		request.setBizContent("{" +
			"\"code_info\":{\"scene\":{\"scene_id\":\""+scene_id+"\"}," +
				//"\"goto_url\":\"http://www.******.com\"" +
			" }," +
			"\"code_type\":\""+code_type+"\"," +
			//"\"expire_second\":\"1000\"," +
			"\"show_logo\":\""+show_logo+"\"}");
		AlipayOpenPublicQrcodeCreateResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			KasiteConfig.print("调用成功");
			String body = response.getBody();
			KasiteConfig.print(body);
		} else {
			KasiteConfig.print("调用失败");
		}
		return response.getBody();
	}
	
	private final static Log log2 = LogFactory.getLog(KstHosConstant.LOG4J_PAY);

	/**
	 * 
	 * @param tradeNo
	 *            支付宝订单号
	 * @param outTradeNo
	 *            商户订单号
	 * @param outRefundNo
	 *            商户退费订单号
	 * @param totalFee
	 *            订单全部金额
	 * @param refundAmount
	 *            订单退费金额入参单位分，调用支付宝退费单位元要转换
	 * @param refundReason
	 *            退费原因
	 * @return
	 * @throws SQLException
	 * @throws AlipayApiException
	 */
	public static JSONObject refund(String zfbKey,String tradeNo, String outTradeNo, String outRefundNo, Integer totalFee,
			Integer refundAmount, String refundReason) throws AlipayApiException{
		long start = System.currentTimeMillis();
		String reqParamStr = zfbKey+"|tradeNo:"+tradeNo+"|outTradeNo:"+outTradeNo+"|outRefundNo:"+outRefundNo+"|refundAmount:"+refundAmount+"|refundReason:"+refundReason;
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);
		AlipayTradeRefundRequest refundRequest = new AlipayTradeRefundRequest();
		// 参数 类型 是否必填 最大长度 描述 示例值
		// out_trade_no String 特殊可选 64 订单支付时传入的商户订单号,不能和 trade_no同时为空。
		// 20150320010101001
		// trade_no String 特殊可选 64 支付宝交易号，和商户订单号不能同时为空
		// 2014112611001004680073956707
		// refund_amount Price 必须 9 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数 200.12
		// refund_reason String 可选 256 退款的原因说明 正常退款
		// out_request_no String 可选 64 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
		// HZ01RF001
		// operator_id String 可选 30 商户的操作员编号 OP001
		// store_id String 可选 32 商户的门店编号 NJ_S_001
		// terminal_id String 可选 32 商户的终端编号 NJ_T_001
		JSONObject bizContent = new JSONObject();
		if (!StringUtil.isEmpty(outTradeNo)) {
			bizContent.put("out_trade_no", outTradeNo);
		} else if (!StringUtil.isEmpty(tradeNo)) {
			bizContent.put("trade_no", tradeNo);
		}
		bizContent.put("refund_amount", CommonUtil.mul("" + refundAmount, "0.01"));
		bizContent.put("refund_reason", refundReason);
		bizContent.put("out_request_no", outRefundNo);
		bizContent.put("operator_id", "");
		bizContent.put("store_id", "");
		bizContent.put("terminal_id", "");
		refundRequest.setBizContent(bizContent.toString());
		reqParamStr = bizContent.toString();
		String logTag = "TAG[" + "out_trade_no:" + outTradeNo + "]";
		LogUtil.info(log2, logTag + "\r\n支付宝退款AlipayTradeRefund的BizContent入参：\r\n" + bizContent.toString());
		AlipayTradeRefundResponse response = alipayClient.execute(refundRequest);
		LogUtil.info(log2, logTag + "\r\n支付宝退款AlipayTradeRefund的出参:\r\n" + response.getBody());
		LogUtil.saveCallZfbLog(ApiModule.Zfb.pay_refund, reqParamStr, response.getBody(), null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
				RequestType.post, true);
		
		// 记录退款日志
		JSONObject noticeLogJson = new JSONObject();
		// 退款记录
		noticeLogJson.put("OP_TYPE", 3);
		noticeLogJson.put("CHANEL_TYPE", 2);
		JSONObject retJson = new JSONObject();
		if (response.isSuccess()) {
			start = System.currentTimeMillis();
			LogUtil.info(log2, logTag + "INFO[" + "申请支付宝退款成功|outRefundNo:" + outRefundNo + "]");
			// 退款成功的查询请求
			AlipayTradeFastpayRefundQueryRequest refundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();
			bizContent.clear();
			bizContent.put("trade_no", response.getTradeNo());
			bizContent.put("out_trade_no", outTradeNo);
			bizContent.put("out_request_no", outRefundNo);
			refundQueryRequest.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradeFastpayRefundQueryResponse refundQueryResponse = alipayClient.execute(refundQueryRequest);
			LogUtil.saveCallZfbLog(outTradeNo,ApiModule.Zfb.pay_orderquery, reqParamStr, response.getBody(), null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, true);
			
			//if (refundQueryResponse.isSuccess() && outRefundNo.equals(refundQueryResponse.getOutRequestNo())) {
			if (refundQueryResponse.isSuccess()) {
				LogUtil.info(log2, logTag + "INFO[" + "查询支付宝退款成功|outRefundNo:" + outRefundNo + "]");
				retJson.put("RefundId", outRefundNo);
				retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, "申请退款成功！");
				noticeLogJson.put("RESULT_CODE", refundQueryResponse.getCode());
				noticeLogJson.put("OUT_TRADE_NO", outRefundNo);
				noticeLogJson.put("TRANSACTION_ID", outTradeNo);
				noticeLogJson.put("FINAL_RESULT", "申请退款成功!");
			} else {
				noticeLogJson.put("RESULT_CODE", response.getCode() + "|" + response.getSubCode());
				noticeLogJson.put("OUT_TRADE_NO", outRefundNo);
				noticeLogJson.put("TRANSACTION_ID", outTradeNo);
				noticeLogJson.put("FINAL_RESULT", response.getMsg() + "|" + response.getSubMsg());
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, response.getMsg() + "|" + response.getSubMsg());
			}
		} else {
			noticeLogJson.put("RESULT_CODE", response.getCode() + "|" + response.getSubCode());
			noticeLogJson.put("OUT_TRADE_NO", outRefundNo);
			noticeLogJson.put("TRANSACTION_ID", outTradeNo);
			retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, response.getMsg() + "|" + response.getSubMsg());
		}
		WxMsgUtil.create(KasiteConfig.localConfigPath()).write(noticeLogJson.toString(),Channel.ZFBRefund);
		return retJson;
	}

	/**
	 * 统一下单
	 *
	 * @param orderId
	 * @param body
	 * @param subject
	 * @param payFee
	 * @param returnUrl
	 * @param isLimitCredit
	 * @return
	 * @throws AlipayApiException
	 * @author 無
	 * @date 2018年4月24日 下午5:41:03
	 */
	public static JSONObject tradeWapPay(AuthInfoVo vo, String zfbKey,String userId,String orderId, String body, String subject, Integer payFee, 
			String returnUrl,Integer isLimitCredit) throws AlipayApiException {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey+"|userId:"+userId+"|orderId:"+orderId+"|body:"+body+"|subject:"+subject+"|payFee:"+payFee+"|returnUrl:"+returnUrl+"|isLimitCredit:"+isLimitCredit;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);//.getAlipayClientInstance();
			// 创建API对应的request类
			AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
			String zfb_signPartner = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signPartner, zfbKey);
			// 接收微信支付异步通知回调地址 "/wxPay/{clientId}/{configKey}/{openId}/{token}/payNotify.do";
//			packageParams.put("notify_url", wx_pay_notify_url+"/wxPay/"+clientId+"/"+configKey+"/"+openId+"/"+token+"/payNotify.do");
			// 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
			request.setNotifyUrl(KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.zfb, zfbKey, vo.getClientId(), userId, vo.getSessionKey(),orderId));
			// 前台回跳
			request.setReturnUrl(returnUrl);

			JSONObject bizContent = new JSONObject();
			// 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
			bizContent.put("out_trade_no", orderId);
			// 订单标题
			bizContent.put("subject", subject);
			// 订单描述
			bizContent.put("body", body);
			// 如果限制信用卡
			if (isLimitCredit == 1) {
				bizContent.put("disable_pay_channels", "creditCard,pcredit");
			}
			// 销售产品码，商家和支付宝签约的产品码。该产品请填写固定值：QUICK_WAP_WAY
			bizContent.put("product_code", "QUICK_WAP_WAY");
			// 该笔订单允许的最晚付款时间，逾期将关闭交易。
			bizContent.put("timeout_express", "24h");
			/** 支付以元为单位,传入的参数是以分为单位,转化为元,除以100 **/
			BigDecimal totalAmount = new BigDecimal(payFee).divide(new BigDecimal(100));
			// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
			bizContent.put("total_amount", totalAmount.toString());
			JSONObject extendParams = new JSONObject();
			// 父商户 appid
			extendParams.put("sys_service_provider_id", zfb_signPartner);
			bizContent.put("extend_params", extendParams);
			reqParamStr = bizContent.toString();
			request.setBizContent(bizContent.toString());
			// 通过alipayClient调用API，获得对应的response类

			LogUtil.info(logger, new LogBody(vo).set("out_trade_no", orderId).set("bizContent", bizContent.toString()));
			AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
			result = response.getBody();
			JSONObject retJson = new JSONObject();
			retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
			retJson.put(KstHosConstant.RESPMESSAGE, "创建订单成功！");
			retJson.put(KstHosConstant.DATA, result);
			isSuccess = true;
			return retJson;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝支付宝统一下单接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.pay_unifiedorder, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
		
	}

	/**
	 * 当面付扫码支付 alipay.trade.pay
	 * 
	 * @return out_trade_no 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复 scene
	 *         支付场景 条码支付，取值：bar_code 声波支付，取值：wave_code auth_code
	 *         支付授权码，25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准 product_code
	 *         销售产品码 subject 订单标题 buyer_id 买家的支付宝用户id，如果为空，会从传入了码值信息中获取买家ID
	 *         seller_id 如果该值为空，则默认为商户签约账号对应的支付宝用户ID total_amount
	 *         订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000] 
	 *         如果同时传入【可打折金额】和【不可打折金额】，该参数可以不用传入； 
	 *         如果同时传入了【可打折金额】，【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【可打折金额】+【
	 *         不可打折金额】 discountable_amount
	 *         参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。 
	 *         如果该值未传入，但传入了【订单总金额】和【不可打折金额】，则该值默认为【订单总金额】-【不可打折金额】 body 订单描述
	 *         goods_detail 订单包含的商品列表信息，Json格式，其它说明详见商品明细说明 operator_id 商户操作员编号
	 *         store_id 商户门店编号 terminal_id 商户机具终端编号 extend_params 业务扩展参数
	 *         timeout_express
	 *         该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，
	 *         无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m
	 * @throws AlipayApiException
	 */
	public static AlipayTradePayResponse alipayTradePay(AuthInfoVo vo,ReqAlipayTradePay reqAlipayTradePay) throws AlipayApiException {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String zfbKey = vo.getConfigKey();
		String clientId = vo.getClientId();
		String openId = vo.getSign();
		String token = vo.getSessionKey();
		
		String reqParamStr = zfbKey+"|"+reqAlipayTradePay.getBody();
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);//ClientFactory.getAlipayClientInstance();

			String zfb_payNotifyUrl = KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.zfb, zfbKey, clientId, openId, token,reqAlipayTradePay.getOutTradeNo());//(ZFBConfigEnum.zfb_payNotifyUrl, zfbKey);
			String zfb_signPartner = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signPartner, zfbKey);
			
			JSONObject bizContent = new JSONObject();
			bizContent.put("out_trade_no", reqAlipayTradePay.getOutTradeNo());
			bizContent.put("scene", reqAlipayTradePay.getScene());
			bizContent.put("auth_code", reqAlipayTradePay.getAuthCode());
			bizContent.put("subject", reqAlipayTradePay.getSubject());
			/** 注意:单位【元】 */
			bizContent.put("total_amount", ArithUtil.div(reqAlipayTradePay.getTotalAmount() + "", "100", 2));
			bizContent.put("body", reqAlipayTradePay.getBody());
			bizContent.put("terminal_id", reqAlipayTradePay.getTerminalId());
			// 如果限制信用卡
			if (KstHosConstant.I1.equals(reqAlipayTradePay.getIsLimitCredit())) {
				bizContent.put("disable_pay_channels", "creditCard,pcredit");
			}
			JSONObject extendParams = new JSONObject();
			extendParams.put("sys_service_provider_id", zfb_signPartner);
			bizContent.put("extend_params", extendParams);

			AlipayTradePayRequest request = new AlipayTradePayRequest();
			/** 回调地址 */
			request.setNotifyUrl(zfb_payNotifyUrl);
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradePayResponse response = alipayClient.execute(request);
			result = response.getBody();
			LogUtil.info(log2, new LogBody(vo).set("auth_code", reqAlipayTradePay.getAuthCode())
					.set("total_amount", ArithUtil.div(reqAlipayTradePay.getTotalAmount() + "", "100", 2))
					.set("out_trade_no", reqAlipayTradePay.getOutTradeNo()).set("subject", reqAlipayTradePay.getSubject())
					.set("terminal_id", reqAlipayTradePay.getTerminalId()).set("body", reqAlipayTradePay.getBody())
					.set("result", response.getBody()));
			isSuccess = true;
			return response;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝 当面付扫码支付接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(reqAlipayTradePay.getOutTradeNo(),ApiModule.Zfb.alipay_trade_pay, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
		
	}

	/**
	 * 预下单接口，返回支付二维码
	 * 
	 * @param orderId
	 * @param body
	 * @param subject
	 * @param payFee
	 * @param isLimitCredit
	 * @return
	 * @throws AlipayApiException
	 * @throws RRException
	 */
	public static JSONObject tradePrecreate(AuthInfoVo vo,String zfbKey,String orderId, String body, String subject, Integer payFee,
			Integer isLimitCredit) throws AlipayApiException, RRException {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = vo.toString();
		String result = "";
		
		String clientId = vo.getClientId();
		String openId = vo.getSign();
		String token = vo.getSessionKey();
		
		
		try {
			String zfb_payNotifyUrl = KasiteConfig.getPayCallBackUrl(ChannelTypeEnum.zfb, zfbKey, clientId, openId, token,orderId);
			String zfb_signPartner = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signPartner, zfbKey);
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);//ClientFactory.getAlipayClientInstance();
			AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
			JSONObject bizContent = new JSONObject();
			request.setNotifyUrl(zfb_payNotifyUrl);
			bizContent.put("out_trade_no", orderId);
			bizContent.put("total_amount", new BigDecimal(payFee).divide(new BigDecimal(100)));
			bizContent.put("subject", subject);
			bizContent.put("body", body);
			// 限制信用卡支付
			if (isLimitCredit == 1) {
				bizContent.put("disable_pay_channels", "creditCard");
			}
			JSONObject extendParams = new JSONObject();
			extendParams.put("sys_service_provider_id", zfb_signPartner);
			bizContent.put("extend_params", extendParams);
			// 该笔订单允许的最晚付款时间，逾期将关闭交易。默认2小时
			bizContent.put("timeout_express", "2h");
			
			reqParamStr = bizContent.toString();
			request.setBizContent(bizContent.toString());
			AlipayTradePrecreateResponse response = alipayClient.execute(request);
			result = response.getBody();
			JSONObject retJson = new JSONObject();
			if (response.isSuccess()) {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, "预创建订单成功！");
				retJson.put("QRCodeUrl", response.getQrCode());
			} else {
				throw new RRException(RetCode.Common.ERROR_SYSTEM, response.getMsg() + "---" + response.getSubMsg());
			}
			isSuccess = true;
			return retJson;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝预下单接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(ApiModule.Zfb.tradePrecreate, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
		
	}

	/**
	 * 撤销订单
	 * 
	 * @param outTradeNo
	 *            商户订单号
	 * @param tradeNo
	 *            支付宝订单号
	 * @return
	 * @throws AlipayApiException
	 */
	public static JSONObject alipayTradeCancel(AuthInfoVo vo,String zfbKey,String outTradeNo, String tradeNo) throws AlipayApiException {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);//ClientFactory.getAlipayClientInstance();
			JSONObject bizContent = new JSONObject();
			if (!StringUtil.isEmpty(tradeNo)) {
				bizContent.put("trade_no", tradeNo);
			}
			if (!StringUtil.isEmpty(outTradeNo)) {
				bizContent.put("out_trade_no", outTradeNo);
			}
			AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradeCancelResponse response = alipayClient.execute(request);
			JSONObject retJson = new JSONObject();
			if (response.getCode().equals(KstHosConstant.SUCCESSCODE + "") && StringUtil.isEmpty(response.getSubCode())) {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, "撤销订单成功！");
				retJson.put("OutTradeNo", response.getTradeNo());
				retJson.put("TradeNo", response.getOutTradeNo());
			} else {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, response.getSubMsg());
			}
			result = response.getBody();
			LogUtil.info(log2, new LogBody(vo).set("out_trade_no", outTradeNo).set("trade_no", tradeNo).set("result",
					response.getBody()));
			return retJson;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝撤销订单接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(outTradeNo,ApiModule.Zfb.alipayTradeCancel, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
		
	}

	/**
	 * 取消支付宝订单
	 * 
	 * @param outTradeNo
	 *            商户订单号
	 * @param tradeNo
	 *            支付宝订单号
	 * @return
	 * @throws AlipayApiException
	 */
	public static JSONObject alipayTradeClose(AuthInfoVo vo,String zfbKey,String outTradeNo, String tradeNo) throws AlipayApiException {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);//ClientFactory.getAlipayClientInstance();
			JSONObject bizContent = new JSONObject();
			if (!StringUtil.isEmpty(outTradeNo)) {
				bizContent.put("out_trade_no", outTradeNo);
			}
			if (!StringUtil.isEmpty(tradeNo)) {
				bizContent.put("trade_no", tradeNo);
			}
			AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			LogUtil.info(log2, new LogBody(vo).set("outTradeNo", outTradeNo).set("inParam", request.getBizContent()));
			AlipayTradeCloseResponse response = alipayClient.execute(request);
			LogUtil.info(log2, new LogBody(vo).set("outTradeNo", outTradeNo).set("outParam", response.getBody()));
			JSONObject retJson = new JSONObject();
			if (response.getCode().equals(KstHosConstant.SUCCESSCODE + "") && StringUtil.isEmpty(response.getSubCode())) {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, "关闭订单成功！");
			} else {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, response.getSubMsg());
			}
			result = response.getBody();
			isSuccess = true;
			return retJson;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝取消支付宝订单接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(outTradeNo,ApiModule.Zfb.pay_closeorder, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
		
	}

	/**
	 * 查询支付宝订单详情
	 * 
	 * @param outTradeNo
	 *            商户订单号
	 * @param tradeNo
	 *            支付宝订单号
	 * @return
	 * @throws AlipayApiException
	 */
	public static JSONObject query(AuthInfoVo vo,String zfbKey,String outTradeNo, String tradeNo) throws AlipayApiException {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);
			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
			JSONObject bizContent = new JSONObject();
			if (!StringUtil.isEmpty(tradeNo)) {
				bizContent.put("trade_no", tradeNo);
			}
			if (!StringUtil.isEmpty(outTradeNo)) {
				bizContent.put("out_trade_no", outTradeNo);
			}
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			LogUtil.info(log2,
					new LogBody(vo).set("outTradeNo", outTradeNo).set("tradeNo", tradeNo).set("支付宝查询订单", response.getBody()));
			JSONObject retJson = new JSONObject();
			if (response.isSuccess()) {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, "查询成功！");
				retJson.put("tradeStatus", response.getTradeStatus());
				retJson.put("tradeNo", response.getTradeNo());
				retJson.put("sendPayDate", response.getSendPayDate());
				retJson.put("outTradeNo", response.getOutTradeNo());
				// 单位:元 转分
				retJson.put("totalFee", StringUtil.yuanChangeFen(response.getTotalAmount()));
				// 其他参数，酌情自己添加
			} else {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, response.getSubCode() + ":" + response.getSubMsg());
			}
			isSuccess = true;
			result = response.getBody();
			return retJson;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝查询支付宝订单详情接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(outTradeNo,ApiModule.Zfb.pay_orderquery, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
		
	}
	
	/**
	 * 该接口逻辑，访问支付宝接口成功，并且存在记录，则退费成功
	 * 访问支付宝接口成功，不存在记录，则退费失败，或者退费中
	 * @param vo
	 * @param zfbKey
	 * @param outTradeNo
	 * @param tradeNo
	 * @param outRequestNo
	 * @return
	 * @throws AlipayApiException
	 */
	public static JSONObject fastpayRefundQuery(AuthInfoVo vo,String zfbKey,String outTradeNo, String tradeNo,String outRequestNo) throws AlipayApiException {
		long start = System.currentTimeMillis();
		boolean isSuccess = false;
		String reqParamStr = zfbKey;
		String result = "";
		try {
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);
			AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
			JSONObject bizContent = new JSONObject();
			if (!StringUtil.isEmpty(tradeNo)) {
				bizContent.put("trade_no", tradeNo);
			}
			if (!StringUtil.isEmpty(outTradeNo)) {
				bizContent.put("out_trade_no", outTradeNo);
			}
			if (!StringUtil.isEmpty(outRequestNo)) {
				bizContent.put("out_request_no", outRequestNo);
			}
//			if (!StringUtil.isEmpty(orgPid)) {
//				bizContent.put("org_pid", orgPid);
//			}
			
			request.setBizContent(bizContent.toString());
			reqParamStr = bizContent.toString();
			AlipayTradeFastpayRefundQueryResponse  response = alipayClient.execute(request);
			LogUtil.info(log2,
					new LogBody(vo).set("outTradeNo", outTradeNo).set("tradeNo", tradeNo).set("outRequestNo", outRequestNo).set("支付宝查询订单", response.getBody()));
			JSONObject retJson = new JSONObject();
			if (response.isSuccess()) {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Success.RET_10000.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, "查询成功！");
				if(!StringUtil.isEmpty(response.getTradeNo()) ) {
					retJson.put("outRequestNo", response.getOutRequestNo());
					retJson.put("refundAmount",StringUtil.yuanChangeFen( response.getRefundAmount()));
					//retJson.put("refundTime", ); 支付宝没有退款时间
					retJson.put("totalAmount",StringUtil.yuanChangeFen( response.getTotalAmount()));
				}
			} else {
				retJson.put(KstHosConstant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
				retJson.put(KstHosConstant.RESPMESSAGE, response.getSubCode() + ":" + response.getSubMsg());
			}
			isSuccess = true;
			result = response.getBody();
			return retJson;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝查询支付宝退费订单详情接口调用异常："+reqParamStr,e);
			throw e;
		}finally {
			LogUtil.saveCallZfbLog(outTradeNo,ApiModule.Zfb.pay_orderquery, reqParamStr, result, null, System.currentTimeMillis() - start, AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					RequestType.post, isSuccess);
		}
		
	}
}
