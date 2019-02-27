package com.kasite.core.common.util;

import org.apache.commons.logging.Log;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.kasite.core.common.Version;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.log.LogLevel;
import com.kasite.core.common.log.LogSourceKey;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.httpclient.http.RequestType;
import com.yihu.hos.IRetCode;

/**
 * 日志工具类
 * 
 * @author 無
 * @version V1.0
 * @date 2018年4月27日 下午4:47:50
 */
public class LogUtil {
	public final static org.slf4j.Logger logger = LoggerFactory.getLogger(LogUtil.class);

	/**
	 * 解析异常，判断异常并最终抛出异常 RRException 
	 * @param e
	 * @param code
	 * @throws RRException
	 */
	public static RRException throwRRException(Throwable e,IRetCode code) throws RRException {
		return throwRRException(e, code,null);
	}
	/**
	 * 解析异常，判断异常并最终抛出异常 RRException 
	 * @param e
	 * @param code
	 * @throws RRException
	 */
	public static RRException throwRRException(Throwable e,IRetCode code,String msg) throws RRException {
		if(e instanceof RRException){
    		RRException exp = (RRException) e;
    		throw exp;
    	}else {
    		Throwable e2 = e.getCause();
    		int i = 0;//避免死循环
    		while(null != e2 && i < 20) {
    			i = i+1;
    			if( null != e2 && RRException.class.getName().equals(e2.getClass().getName())) {
    				RRException exp = (RRException) e2;
    	    		throw exp;
    			}
    			e2 = e2.getCause();
    		}
    		if(StringUtil.isNotBlank(msg)) {
    			throw new RRException(msg,code.getCode(),e);
    		}
    		throw new RRException(code.getMessage(),code.getCode(),e);
    	}
	}
	
	
	/**
	 * 追踪
	 *
	 * @param module
	 * @param body
	 * @author 無
	 * @date 2018年4月27日 下午6:05:05
	 */
	public static void trace(Log log, LogBody body) {
		log(log, "trace", body,null);
	}
	
	/**
	 * 保存请求日志
	 * @param vo 请求鉴权头
	 * @param orderId 订单ID 如果没有的话 会生成一个 NoOrderId_ +UUID 前缀的随机ID
	 * @param api 请求HIS的api 枚举
	 * @param params 请求参数
	 * @param paramType 请求参数类型
	 * @param outType 返回结果类型
	 * @param result 返回的结果集
	 * @param methodName 请求的类的方法名
	 * @param times  请求耗时
	 * @param hisUrl 请求HIS接口的地址
	 * @param callType 请求类型 SOAP Post Get 等等
	 * @param isSuccess 调用接口是否成功
	 * @param httpStausCode soapHttp 请求方式调用返回的code 必须是公司统一封装的包进行soap调用控制，如果非公司统一封装的无法保证告警的准确性，无法准确告警，请个性化实现告警
	 */
	public static void saveCallHisLog(ISaveCallHisOrder saveCallHisOrder,AuthInfoVo vo,String orderId,ApiModule.His api,String params,String result, 
			String methodName,long times,String hisUrl,RequestType callType,boolean isSuccess,int httpStausCode) {
		LogBody body = null;
		boolean isSaveLog2OrderInfo = true;
		try {
			if(null == params ) {
				params = "";
			}
			if(StringUtil.isBlank(orderId)) {
				//如果订单号为空则不保存 
				isSaveLog2OrderInfo = false;
				orderId = "NotOrderId_"+ IDSeed.next();
			}
			String code = KstHosConstant.SUCCESSCODE;
			if(!isSuccess) {
				code = "-400010";
			}
			boolean isSaveResult = KasiteConfig.getIsSaveCallResult(vo,api);
			//如果不需要保存结果集则将结果集赋值为{code:10000,message:'成功'}
			if(!isSaveResult && isSuccess) {
				logger.debug("HIS接口调用日志：{}{}{}{} ",api.getApiName(),vo,params,result);
				result = "<Resp><Code>10000</Code><Message>不保存该接口His的请求结果</Message></Resp>";
			}
			body  = LogBody.me(vo)
					.set(LogSourceKey.OrderId, orderId)
					.set(LogSourceKey.AuthInfo, vo.toString())// 鉴权头
					.set(LogSourceKey.uniqueReqId, vo.getUuid())
					.set(LogSourceKey.Api, api.getName())//
					.set(LogSourceKey.Param, params.length()>10000000?"":params)// 入参
					.set(LogSourceKey.ParamType, 1)// 入参类型 0 json
					.set(LogSourceKey.OutType, 1)// 出参类型 0 json
					.set(LogSourceKey.V, Version.V)// 版本号,默认1
					.set(LogSourceKey.Result, result)// 返回结果
					.set(LogSourceKey.Url, hisUrl)// 目标子系统
					.set(LogSourceKey.ClassName,api.getRemark())// 目标系统类名
					.set(LogSourceKey.MethodName,methodName == null ? "" : methodName)// 目标系统方法名
					.set(LogSourceKey.Times, times)// 耗时
					.set(LogSourceKey.ClientUrl, callType.name())// 客户端IP
					.set(LogSourceKey.CurrTimes, System.currentTimeMillis())
					.set(LogSourceKey.Code, code);
			Logger.get().log(LogLevel.INFO,
					"wsgw-log",
					body// 客户端IP
//								.set("resp_mills",ResptimeHolder.removeRespTime(uniqueReqId))
//							.set("WsgwUrl", referer));// 网关（指接口网关或开放平台）IP，未经网关的填写空
					);
		}catch (Exception e) {
			e.printStackTrace();
			if(null != body) {
				logger.error(body.toString()+"|调用his接口保存日志异常|",e);
			}else {
				logger.error("调用his接口保存日志异常：",e);
			}
		}finally {
			if(null != saveCallHisOrder && isSaveLog2OrderInfo) {
				try {
					saveCallHisOrder.saveOrderCallHisLog(orderId, api, params, result);
				}catch (Exception e) {
					logger.error("保存调用日志到数据库异常",e);
				}
			}
			if(null != body) {
				logger.info("HIS接口调用日志：{} ",body.toString());
			}else {
				logger.info("HIS接口调用日志：{}{}{}{} ",api.getApiName(),vo,params,result);
			}
		}
		
	}
	/**
	 * 保存向微信服务端请求日志
	 * @param vo 请求鉴权头
	 * @param orderId 订单ID 如果没有的话 会生成一个 NoOrderId_ +UUID 前缀的随机ID
	 * @param api 请求HIS的api 枚举
	 * @param params 请求参数
	 * @param paramType 请求参数类型
	 * @param outType 返回结果类型
	 * @param result 返回的结果集
	 * @param methodName 请求的类的方法名
	 * @param times  请求耗时
	 * @param hisUrl 请求HIS接口的地址
	 * @param callType 请求类型 SOAP Post Get 等等
	 * @param isSuccess 调用接口是否成功
	 */
	public static void saveCallWeChatLog(String orderId,ApiModule.WeChat api,String params,String result, 
			String methodName,long times,String url,RequestType callType,boolean isSuccess) {
		saveCallWsgwLog(orderId, api, params, result, methodName, times, url, callType, isSuccess);
	}
	
	/**
	 * 保存向招行一网通服务端请求日志
	 * @param vo 请求鉴权头
	 * @param orderId 订单ID 如果没有的话 会生成一个 NoOrderId_ +UUID 前缀的随机ID
	 * @param api 请求HIS的api 枚举
	 * @param params 请求参数
	 * @param paramType 请求参数类型
	 * @param outType 返回结果类型
	 * @param result 返回的结果集
	 * @param methodName 请求的类的方法名
	 * @param times  请求耗时
	 * @param hisUrl 请求HIS接口的地址
	 * @param callType 请求类型 SOAP Post Get 等等
	 * @param isSuccess 调用接口是否成功
	 */
	public static void saveCallLog(String orderId,ApiModule api,String params,String result, 
			String methodName,long times,String url,RequestType callType,boolean isSuccess) {
		saveCallWsgwLog(orderId, api, params, result, methodName, times, url, callType, isSuccess);
	}
	
	
	/**
	 * 保存向招行一网通服务端请求日志
	 * @param vo 请求鉴权头
	 * @param orderId 订单ID 如果没有的话 会生成一个 NoOrderId_ +UUID 前缀的随机ID
	 * @param api 请求HIS的api 枚举
	 * @param params 请求参数
	 * @param paramType 请求参数类型
	 * @param outType 返回结果类型
	 * @param result 返回的结果集
	 * @param methodName 请求的类的方法名
	 * @param times  请求耗时
	 * @param hisUrl 请求HIS接口的地址
	 * @param callType 请求类型 SOAP Post Get 等等
	 * @param isSuccess 调用接口是否成功
	 */
	public static void saveCallNetPayLog(String orderId,ApiModule.NetPay api,String params,String result, 
			String methodName,long times,String url,RequestType callType,boolean isSuccess) {
		saveCallWsgwLog(orderId, api, params, result, methodName, times, url, callType, isSuccess);
	}
	
	/**
	 * 保存向银联服务端请求日志
	 * @param vo 请求鉴权头
	 * @param orderId 订单ID 如果没有的话 会生成一个 NoOrderId_ +UUID 前缀的随机ID
	 * @param api 请求HIS的api 枚举
	 * @param params 请求参数
	 * @param paramType 请求参数类型
	 * @param outType 返回结果类型
	 * @param result 返回的结果集
	 * @param methodName 请求的类的方法名
	 * @param times  请求耗时
	 * @param hisUrl 请求HIS接口的地址
	 * @param callType 请求类型 SOAP Post Get 等等
	 * @param isSuccess 调用接口是否成功
	 */
	public static void saveCallWsgwLog(String orderId,ApiModule api,String params,String result, 
			String methodName,long times,String url,RequestType callType,boolean isSuccess) {
		LogBody body = null;
		try {
			if(null == params ) {
				params = "";
			}
			AuthInfoVo vo = KasiteConfig.createAuthInfoVo(HttpsClientUtils.class);
			if(StringUtil.isBlank(orderId)) {
				orderId = "NotOrderId_"+ System.currentTimeMillis()+"_"+IDSeed.next();
			}
			String code = KstHosConstant.SUCCESSCODE;
			if(!isSuccess) {
				code = "-400010";
			}else {
				code= "10000";
			}
			boolean isSaveResult = KasiteConfig.getIsSaveCallResult(vo,api);
			//如果不需要保存结果集则将结果集赋值为{code:10000,message:'成功'}
			if(!isSaveResult && isSuccess) {
				logger.debug("HIS接口调用日志：{}{}{}{} ",api.getName(),vo,params,result);
				result = "<Resp><Code>10000</Code><Message>不保存该接口His的请求结果</Message></Resp>";
			}
			
			body = LogBody.me(vo)
					.set(LogSourceKey.OrderId, orderId)
					.set(LogSourceKey.AuthInfo, vo.toString())// 鉴权头
					.set(LogSourceKey.uniqueReqId, vo.getUuid())
					.set(LogSourceKey.Api, api.getName())//
					.set(LogSourceKey.Param, params.length()>10000000?"":params)// 入参
					.set(LogSourceKey.ParamType, 1)// 入参类型 0 json
					.set(LogSourceKey.OutType, 1)// 出参类型 0 json
					.set(LogSourceKey.V, Version.V)// 版本号,默认1
					.set(LogSourceKey.Result, result)// 返回结果
					.set(LogSourceKey.Url, url)// 目标子系统
//					.set(LogSourceKey.ClassName,)// 目标系统类名
					.set(LogSourceKey.MethodName,methodName == null ? "" : methodName)// 目标系统方法名
					.set(LogSourceKey.Times, times)// 耗时
					.set(LogSourceKey.ClientUrl, callType.name())// 客户端IP
					.set(LogSourceKey.CurrTimes, System.currentTimeMillis())
					.set(LogSourceKey.Code, code);
			Logger.get().log(LogLevel.INFO,
					"wsgw-log",
					body
					);
		}catch (Exception e) {
			e.printStackTrace();
			if(null != body) {
				logger.error(body.toString()+"|调用银联接口保存日志发生异常|",e);
			}else {
				logger.error("调用银联接口保存日志发生异常：",e);
			}
		}finally {
			if(null != body) {
				logger.info("银联接口调用日志：{} ",body.toString());
			}else {
				logger.info("银联接口调用日志：{}{}{}",api.getName(),params,result);
			}
		}
		
	}
	/**
	 * 保存向银联服务端请求日志
	 * @param vo 请求鉴权头
	 * @param orderId 订单ID 如果没有的话 会生成一个 NoOrderId_ +UUID 前缀的随机ID
	 * @param api 请求HIS的api 枚举
	 * @param params 请求参数
	 * @param paramType 请求参数类型
	 * @param outType 返回结果类型
	 * @param result 返回的结果集
	 * @param methodName 请求的类的方法名
	 * @param times  请求耗时
	 * @param hisUrl 请求HIS接口的地址
	 * @param callType 请求类型 SOAP Post Get 等等
	 * @param isSuccess 调用接口是否成功
	 */
	public static void saveCallUnionPayLog(String orderId,ApiModule.UnionPay api,String params,String result, 
			String methodName,long times,String url,RequestType callType,boolean isSuccess) {
		 saveCallWsgwLog(orderId, api, params, result, methodName, times, url, callType, isSuccess);
	}
	
	public static void saveCallSwiftpassLog(String orderId,ApiModule.Swiftpass api,String params,String result, 
			String methodName,long times,String url,RequestType callType,boolean isSuccess) {
		 saveCallWsgwLog(orderId, api, params, result, methodName, times, url, callType, isSuccess);
	}
	/**
	 * 保存向支付宝服务端请求日志
	 * @param vo 请求鉴权头
	 * @param orderId 订单ID 如果没有的话 会生成一个 NoOrderId_ +UUID 前缀的随机ID
	 * @param api 请求HIS的api 枚举
	 * @param params 请求参数
	 * @param paramType 请求参数类型
	 * @param outType 返回结果类型
	 * @param result 返回的结果集
	 * @param methodName 请求的类的方法名
	 * @param times  请求耗时
	 * @param hisUrl 请求HIS接口的地址
	 * @param callType 请求类型 SOAP Post Get 等等
	 * @param isSuccess 调用接口是否成功
	 */
	public static void saveCallZfbLog(ApiModule.Zfb api,String params,String result,
			String methodName,long times,String url,RequestType callType,boolean isSuccess) {
		saveCallZfbLog(null, api, params, result, methodName, times, url, callType, isSuccess);
	}
	/**
	 * 保存向支付宝服务端请求日志
	 * @param vo 请求鉴权头
	 * @param orderId 订单ID 如果没有的话 会生成一个 NoOrderId_ +UUID 前缀的随机ID
	 * @param api 请求HIS的api 枚举
	 * @param params 请求参数
	 * @param paramType 请求参数类型
	 * @param outType 返回结果类型
	 * @param result 返回的结果集
	 * @param methodName 请求的类的方法名
	 * @param times  请求耗时
	 * @param hisUrl 请求HIS接口的地址
	 * @param callType 请求类型 SOAP Post Get 等等
	 * @param isSuccess 调用接口是否成功
	 */
	public static void saveCallZfbLog(String orderId, ApiModule.Zfb api,String params,String result,
			String methodName,long times,String url,RequestType callType,boolean isSuccess) {
		 saveCallWsgwLog(orderId, api, params, result, methodName, times, url, callType, isSuccess);
	}
	/**
	 * 调试
	 *
	 * @param module
	 * @param body
	 * @author 無
	 * @date 2018年4月27日 下午6:04:59
	 */
	public static void debug(Log log, LogBody body) {
		log(log, "debug", body,null);
	}
	
 	/**
	 * 调试
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:24:49
	 */
	public static void debug(Log log, String msg) {
		AuthInfoVo vo = createAuthInfoVo();
		log(log, "debug", new LogBody(vo).set("Msg", msg),null);
	}	
	/**
	 * info
	 *
	 * @param module
	 * @param body
	 * @author 無
	 * @date 2018年4月27日 下午6:04:46
	 */
	public static void debug(org.slf4j.Logger log, String msg) {
		AuthInfoVo vo = createAuthInfoVo();
		log(log, "debug", new LogBody(vo).set("Msg", msg),null);
	}
	/**
	 * info
	 *
	 * @param module
	 * @param body
	 * @author 無
	 * @date 2018年4月27日 下午6:04:46
	 */
	public static void info(org.slf4j.Logger log, LogBody body) {
		log(log, "info", body);
	}
	/**
	 * info
	 *
	 * @param module
	 * @param body
	 * @author 無
	 * @date 2018年4月27日 下午6:04:46
	 */
	public static void info(Log log, LogBody body) {
		log(log, "info", body,null);
	}
//	/**
//	 * error
//	 *
//	 * @param module
//	 * @param body
//	 * @date 2018年4月27日 下午6:04:46
//	 */
//	private static void error(Log log, com.coreframework.log.LogBody body) {
//		//通过系统获取当前线程执行的用户的信息。
//		AuthInfoVo vo = createAuthInfoVo();
//		JSONObject obj = JSON.parseObject(body.toString());
//		if(null != obj) {
//			log(log, "info", new LogBody(vo).set(obj));
//		}
//	}
//	/**
//	 * info
//	 *
//	 * @param module
//	 * @param body
//	 * @date 2018年4月27日 下午6:04:46
//	 */
//	public static void info(Log log, com.coreframework.log.LogBody body) {
//		//通过系统获取当前线程执行的用户的信息。
//		String uuid = com.kasite.core.common.log.GIDHolder.get();
//		AuthInfoVo vo = KasiteConfig.createAuthInfoVo(uuid);
//		JSONObject obj = JSON.parseObject(body.toString());
//		if(null != obj) {
//			log(log, "info", new LogBody(vo).set(obj),null);
//		}
//	}
	/**
	 * 错误
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:11:49
	 */
	public static void error(org.slf4j.Logger log, Throwable e) {
		AuthInfoVo vo = createAuthInfoVo();
		log(log, "error", new LogBody(vo),e);
	}
	/**
	 * 错误
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:11:49
	 */
	public static void error(org.slf4j.Logger log, String msg,Throwable e) {
		AuthInfoVo vo = createAuthInfoVo();
		log(log, "error", new LogBody(vo).set(e).set("Msg", msg));
	}
	/**
	 * 错误
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:11:49
	 */
	public static void error(Log log, String msg,Throwable e) {
		AuthInfoVo vo = createAuthInfoVo();
		log(log, "error", new LogBody(vo).set(e).set("Msg", msg),null);
	}
	/**
	 * 错误
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:11:49
	 */
	public static void error(Log log, String msg,AuthInfoVo vo,Throwable e) {
		log(log, "error", new LogBody(vo).set(e).set("Msg", msg),null);
	}
	 /**
   	 * 无法获取登录用户信息的  比如通过job进入调用系统／通过 微信回调等方式统一的一个鉴权参数赋值 用于做日志的不能用于业务处理
   	 * @param sysUser
   	 * @param token
   	 * @return
   	 */
   	public static AuthInfoVo createAuthInfoVo() {
   		String uuid = com.kasite.core.common.log.GIDHolder.get();
   		return KasiteConfig.createAuthInfoVo(uuid);
   	}
   	/**
	 * 信息
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:24:49
	 */
	public static void info(Log log, String msg) {
		AuthInfoVo vo = createAuthInfoVo();
		log(log, "info", new LogBody(vo).set("Msg", msg),null);
	}
	/**
	 * 信息
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:24:49
	 */
	public static void info(Log log, String mudule,String message) {
		AuthInfoVo vo = createAuthInfoVo();
		log(log, "info", new LogBody(vo).set("Msg", message),mudule);
	}
	/**
	 * 信息
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:24:49
	 */
	public static void info(org.slf4j.Logger log, String msg) {
		AuthInfoVo vo = createAuthInfoVo();
		log(log, "info", new LogBody(vo).set("Msg", msg));
	}
	/**
	 * 信息
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:24:49
	 */
	public static void info(Log log, String msg,AuthInfoVo vo) {
		log(log, "info", new LogBody(vo).set("Msg", msg),null);
	}
	/**
	 * 信息
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:24:49
	 */
	public static void info(org.slf4j.Logger log, String msg,AuthInfoVo vo) {
		log(log, "info", new LogBody(vo).set("Msg", msg),null);
	}
	/**
	 * 信息
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:24:49
	 */
	public static void info(AuthInfoVo vo,Log log, String msg) {
		log(log, "info", new LogBody(vo).set("Msg", msg),null);
	}
	/**
	 * 警告
	 *
	 * @param module
	 * @param body
	 * @author 無
	 * @date 2018年4月27日 下午6:05:11
	 */
	public static void warn(Log log, LogBody body) {
		log(log, "warn", body,null);
	}
	/**
	 * 警告
	 *
	 * @param module
	 * @param body
	 * @author 無
	 * @date 2018年4月27日 下午6:05:11
	 */
	public static void warn(org.slf4j.Logger log, LogBody body) {
		log(log, "warn", body,null);
	}
//	/**
//	 * 错误
//	 *
//	 * @param module
//	 * @param body
//	 * @author 無
//	 * @date 2018年4月27日 下午6:05:20
//	 */
//	public static void error(Log log, LogBody body,Throwable e) {
//		log(log, "error", body,null);
//	}

	/**
	 * 错误
	 *
	 * @param module
	 * @param e
	 * @author 無
	 * @date 2018年4月27日 下午6:11:49
	 */
	public static void error(Log log, Throwable e, AuthInfoVo vo) {
		log(log, "error", new LogBody(vo).set("Exception", e.getMessage()),null);
	}
	/**
	 * 错误
	 *
	 * @param module
	 * @param e
	 * @author 無
	 * @date 2018年4月27日 下午6:11:49
	 */
	public static void error(Log log, Throwable e) {
		AuthInfoVo vo = createAuthInfoVo();
		log(log, "error", new LogBody(vo).set(e),null);
	}
	/**
	 * 错误
	 *
	 * @param module
	 * @param msg
	 * @author 無
	 * @date 2018年4月27日 下午6:11:49
	 */
	public static void error(Log log, String msg,Throwable e, AuthInfoVo vo) {
		log(log, "error", new LogBody(vo).set(e).set("Msg", msg),null);
	}
	/**
	 * 错误
	 *
	 * @param module
	 * @param msg
	 * @param e
	 * @author 無
	 * @date 2018年4月28日 上午10:17:29
	 */
	public static void error(Log log,AuthInfoVo vo, Throwable e) {
		log(log, "error", new LogBody(vo).set(e),null);
	}
	/**
	 * 错误
	 *
	 * @param module
	 * @param msg
	 * @param e
	 * @author 無
	 * @date 2018年4月28日 上午10:17:29
	 */
	public static void error(Log log,AuthInfoVo vo, String msg, Throwable e) {
		log(log, "error", new LogBody(vo).set(e).set("Msg", msg),null);
	}
	/**
	 * 错误
	 *
	 * @param module
	 * @param msg
	 * @param e
	 * @author 無
	 * @date 2018年4月28日 上午10:17:29
	 */
	public static void error(org.slf4j.Logger log,AuthInfoVo vo, String msg, Throwable e) {
		log(log, "error", new LogBody(vo).set(e).set("Msg", msg),null);
	}
	public static void error(Log log, LogBody body, Throwable e) {
		log(log, "error", body.set(e),null);
	}

	/**
	 * 致命
	 *
	 * @param module
	 * @param body
	 * @author 無
	 * @date 2018年4月27日 下午6:05:26
	 */
	public static void fatal(Log log, LogBody body) {
		log(log, "fatal", body,null);
	}

	/**
	 * 记录日志 APM的查询条件：模块名称=类名.方法
	 * 
	 * @param log
	 * @param level
	 * @param body
	 * @author 無
	 * @date 2018年4月28日 下午4:13:33
	 */
	private static void log(org.slf4j.Logger log, String level, LogBody body) {
		log(log, level, body, null);
	}

	/**
	 * 解析异常信息
	 * @param ex
	 * @return
	 */
	private static StackTraceElement getException(StackTraceElement[] sts,StringBuffer sbf) {
		String kasitePackage = "com.kasite.";
		StackTraceElement t = null;
		int i = 0;
		if(sts != null && sts.length > 0 ) {
			t = sts[i];
			String trueClassName = t.getClassName();
			if(!trueClassName.startsWith(kasitePackage) 
					) {
				while(i< 200) {
	        		if(trueClassName.startsWith(kasitePackage)  
	        				) {
	        			String trueMethodName = t.getMethodName();
	        			Integer trueLine = t.getLineNumber();
	        			if(null != sbf) {
		        	    	sbf.append(trueClassName).append(".").append(trueMethodName).append("(").append(trueLine).append(")");
	        			}
	        			return t;
	        		}
	        		i++;
	        		t = sts[i];
	        		if(null != t) {
		        		trueClassName = t.getClassName();
	        		}else {
	        			break;
	        		}
	        	}
			}
			if(null != sts && sts.length > 0) {
		 		String clazz = sts[0].getClassName();
		 		String refMethod = sts[0].getMethodName();
		 		int line = sts[0].getLineNumber();
		 		if(null != sbf) {
		 			sbf.append("[class:"+clazz+"] [method:"+refMethod+"] [lineNumber:"+line+"]");
    			}
		 		
	 		}
		}
		return t;
	}

	
	/**
	 * 解析异常信息
	 * @param ex
	 * @return
	 */
	public static R parseException(Exception ex) {
		ex.printStackTrace();
		R r = R.error();
		StringBuffer sbf = new StringBuffer();
		StackTraceElement[] sts = ex.getStackTrace();
		if(ex instanceof RRException){
	 		r =  R.error( ((RRException) ex).getCode(),ex.getMessage());
    	}else if(ex instanceof ParamException){
	 		ParamException e = (ParamException) ex;
	 		r = R.error(e.getCode(),e.getMessage());
    	}else if(null != ex.getCause() && ex.getCause() instanceof RRException) {
    		RRException e = (RRException) ex.getCause();
    		sts = e.getStackTrace();
	 		r = R.error(e.getCode(),e.getMessage());
	 	}else if(null != ex.getCause() && ex.getCause() instanceof ParamException) {
    		ParamException e = (ParamException) ex.getCause();
    		sts = e.getStackTrace();
	 		r = R.error(e.getCode(),e.getMessage());
	 	}else if( ex instanceof NoSuchBeanDefinitionException) {
	 		r.put("msg", "未找到实现类."+ex.getMessage());
	 	}else {
	 		getException(sts,sbf);
	 		sbf.append(ex.getMessage());
	 	}
		if(null != sbf && sbf.length() > 0) {
	 		r.put("ex", sbf);
	 		r.put("Stack", com.kasite.core.common.util.StringUtil.getExceptionStack(ex));
 		}
		return r;
	}
	/**
	 * 记录日志 APM的查询条件：模块名称=类名.方法
	 * 
	 * @param log
	 * @param level
	 * @param body
	 * @author 無
	 * @date 2018年4月28日 下午4:13:33
	 */
	private static void log(org.slf4j.Logger log, String level, LogBody body,Throwable e) {
		try {
			if(null != e) {
				body.set(e);
			}
			StackTraceElement[] sts = Thread.currentThread().getStackTrace();
			StackTraceElement s = getException(sts, null);
			/**com.yihu.hos.main.LogTestDemo.main(LogTestDemo.java:41): */
			String cmfl = s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + "): ";
			/**LogTestDemo.main*/
			String module = s.getClassName().substring(s.getClassName().lastIndexOf(".") + 1, s.getClassName().length()) + "." + s.getMethodName();
			String lineNumber = s.getFileName() + ":" + s.getLineNumber();
			if (KstHosConstant.LOG_LEVEL_TRACE.equals(level)) {
				log.trace(cmfl + body);
				if(null != e) {
					log.trace(module,e);
				}
				Logger.get().trace(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_DEBUG.equals(level)) {
				log.debug(cmfl + body);
				Logger.get().debug(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_INFO.equals(level)) {
				log.info(cmfl + body);
				Logger.get().info(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_WARN.equals(level)) {
				log.warn(cmfl + body);
				Logger.get().warn(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_ERROR.equals(level)) {
				log.error(cmfl + body);
				Logger.get().error(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_FATAL.equals(level)) {
				log.info(cmfl + body);
				Logger.get().fatal(module, body.set("LineNumber", lineNumber));
			} else {
				log.info(cmfl + body);
				Logger.get().info(module, body.set("LineNumber", lineNumber));
			}
		} catch (Exception ex) {
			e.printStackTrace();
			Logger.get().error("LogUtil", body.set(ex));
		}
	}
	
	/**
	 * 记录日志 APM的查询条件：模块名称=类名.方法
	 * 
	 * @param log
	 * @param level
	 * @param body
	 * @author 無
	 * @date 2018年4月28日 下午4:13:33
	 */
	private static void log(Log log, String level, LogBody body, String module) {
		try {
			StackTraceElement s = Thread.currentThread().getStackTrace()[3];
			/**com.yihu.hos.main.LogTestDemo.main(LogTestDemo.java:41): */
			String cmfl = s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + "): ";
			/**LogTestDemo.main*/
			if(StringUtil.isBlank(module)) {
				module = s.getClassName().substring(s.getClassName().lastIndexOf(".") + 1, s.getClassName().length()) + "." + s.getMethodName();
			}
			String lineNumber = s.getFileName() + ":" + s.getLineNumber();
			
			logger.debug(cmfl + body);
			if (KstHosConstant.LOG_LEVEL_TRACE.equals(level)) {
//				log.trace(cmfl + body);
				logger.trace(cmfl + body);
				Logger.get().trace(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_DEBUG.equals(level)) {
//				log.debug(cmfl + body);
				Logger.get().debug(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_INFO.equals(level)) {
//				log.info(cmfl + body);
				logger.info(cmfl + body);
				Logger.get().info(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_WARN.equals(level)) {
//				log.warn(cmfl + body);
				logger.warn(cmfl + body);
				Logger.get().warn(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_ERROR.equals(level)) {
//				log.error(cmfl + body);
				logger.error(cmfl + body);
				Logger.get().error(module, body.set("LineNumber", lineNumber));
			} else if (KstHosConstant.LOG_LEVEL_FATAL.equals(level)) {
//				log.fatal(cmfl + body);
				logger.info(cmfl + body);
				Logger.get().fatal(module, body.set("LineNumber", lineNumber));
			} else {
				logger.info(cmfl + body);
//				log.info(cmfl + body);
				Logger.get().info(module, body.set("LineNumber", lineNumber));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.get().error("LogUtil", body.set(e));
			
		}
	}
}
