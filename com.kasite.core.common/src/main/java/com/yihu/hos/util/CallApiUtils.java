//package com.yihu.hos.util;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//
//import com.kasite.core.common.bean.bo.AuthInfoVo;
//import com.kasite.core.common.constant.KstHosConstant;
//import com.kasite.core.common.sys.KBus;
//import com.kasite.core.common.util.DataUtils;
//import com.kasite.core.common.util.XMLUtil;
//import com.kasite.core.httpclient.http.SoapResponseVo;
//
///**
// * 接口调用帮助类
// * @className: CallApiUtils
// * @author: lcz
// * @date: 2018年5月29日 下午4:54:14
// */
//public class CallApiUtils {
//	private static int paramType;
//	private static int outType;
////	public static void setAuthInfo(String authInfo) {
////		CallApiUtils.authInfo = authInfo;
////	}
//	public static void setParamType(int paramType) {
//		CallApiUtils.paramType = paramType;
//	}
//	public static void setOutType(int outType) {
//		CallApiUtils.outType = outType;
//	}
//
////	static {
////		JSONObject json = new JSONObject();
////		json.put("ClientId", KstHosConstant.ClientId);
////		json.put("ClientVersion", KstHosConstant.ClientVersion);
////		json.put("Sign", KstHosConstant.Sign);
////		json.put("SessionKey", KstHosConstant.SessionKey);
////		authInfo = json.toString();
////		paramType = KstHosConstant.INTYPE;
////		outType = KstHosConstant.OUTTYPE;
////	}
//	
//	public static String callApiReturnXml(String api,String params,AuthInfoVo authInfo,int paramType,int outType) throws Exception {
//		SoapResponseVo resp = KBus.create(api, params).authInfoString(authInfo).paramType(paramType).outType(outType).send();
//		if(resp==null || resp.getCode()!=200) {
//			Document doc = DocumentHelper.createDocument();
//			Element respEl = doc.addElement(KstHosConstant.RESP);
//			XMLUtil.addElement(respEl, KstHosConstant.RESPCODE, -14444);
//			XMLUtil.addElement(respEl, KstHosConstant.RESPMESSAGE, "http请求异常：Status="+(resp!=null?resp.getCode():""));
//			return doc.asXML();
//		}
//		return resp.getResult();
//	}
//	
//	
//	public static String callApi(AuthInfoVo authInfo,String api,String params) throws Exception {
//		return callApi(api, params, authInfo, paramType, outType);
//	}
//	public static String callApi(String api,String params,AuthInfoVo authInfo,int paramType,int outType) throws Exception {
//		SoapResponseVo resp = KBus.create(api, params).authInfoString(authInfo).paramType(paramType).outType(outType).send();
//		if(resp==null || resp.getCode()!=200) {
//			com.alibaba.fastjson.JSONObject resJs = new com.alibaba.fastjson.JSONObject();
//			resJs.put(KstHosConstant.RESPCODE, -14444);
//			resJs.put(KstHosConstant.RESPMESSAGE, "http请求异常：Status="+(resp!=null?resp.getCode():""));
//			return resJs.toString();
//		}
//		return DataUtils.xml2JSON(resp.getResult());
//	}
//	public static String callApi(String api,String params,AuthInfoVo authInfo,HttpServletRequest request) throws Exception {
////		String userAget = request.getHeader("User-Agent").toLowerCase();
////		String clientId = KstHosConstant.WX_CHANNEL_ID;
////		if (userAget.contains(KstHosConstant.USERAGET_ALIPAY)) {
////			clientId = KstHosConstant.ZFB_CHANNEL_ID;
////		} else if (userAget.contains(KstHosConstant.USERAGET_WX)) {
////			clientId = KstHosConstant.WX_CHANNEL_ID;
////		}
////		JSONObject json = new JSONObject();
////		json.put("ClientId", clientId);
////		json.put("ClientVersion", KstHosConstant.ClientVersion);
////		json.put("Sign", KstHosConstant.Sign);
////		json.put("SessionKey", KstHosConstant.SessionKey);
//		SoapResponseVo resp = KBus.create(api, params).authInfoString(authInfo).paramType(paramType).outType(outType).send();
//		if(resp==null || resp.getCode()!=200) {
//			com.alibaba.fastjson.JSONObject resJs = new com.alibaba.fastjson.JSONObject();
//			resJs.put(KstHosConstant.RESPCODE, -14444);
//			resJs.put(KstHosConstant.RESPMESSAGE, "http请求异常：Status="+(resp!=null?resp.getCode():""));
//			return resJs.toString();
//		}
//		return DataUtils.xml2JSON(resp.getResult());
//	}
//	
//	public static SoapResponseVo syncCallApi(AuthInfoVo authInfo,String api,String params) throws Exception {
//		return KBus.create(api, params).authInfoString(authInfo).paramType(paramType).outType(outType).send();
//	}
//	
//}
