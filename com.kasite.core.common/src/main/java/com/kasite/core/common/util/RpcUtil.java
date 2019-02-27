//package com.kasite.core.common.util;
//
//import com.kasite.core.common.bean.bo.AuthInfoVo;
//import com.kasite.core.common.exception.RRException;
//import com.kasite.core.common.sys.KBus;
//import com.kasite.core.httpclient.http.SoapResponseVo;
//
///**
// * rpc调用工具类
// * 
// * @author 無
// * @version V1.0
// * @date 2018年4月24日 下午3:39:59
// */
//public class RpcUtil {
//
//	/**
//	 * 调用RPC
//	 * 
//	 * @param apiName
//	 * @param clientId
//	 * @param param
//	 * @param paramType
//	 * @param outType
//	 * @return
//	 * @throws Exception 
//	 * @throws RRException
//	 */
//	public static String callRpc(String apiName,AuthInfoVo authInfo, String param, int paramType, int outType) throws Exception {
//		SoapResponseVo resp = KBus.create(apiName, param).authInfoString(authInfo).outType(outType).paramType(paramType).send();
////		String resp = ServiceBus.getInstance("", KasiteConfig.getAppId())
////				.call(authInfo.toString(), apiName, param, paramType, outType, 30000, null).getRet();
//		return resp.getResult();
//	}
//
//}
