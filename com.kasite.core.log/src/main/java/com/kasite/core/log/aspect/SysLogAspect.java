package com.kasite.core.log.aspect;


import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.Version;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.log.GIDHolder;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.log.LogLevel;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.spring.HttpContextUtils;
import com.kasite.core.common.util.wxmsg.IDSeed;


/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class SysLogAspect {
	private static final R NOTRESULTR = R.ok("不保存结果集");
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
	@Pointcut("@annotation(com.kasite.core.common.annotation.SysLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		Object result = null;
		//执行方法
		try {
			result = point.proceed();
		}catch (Exception e) {
			e.printStackTrace();
			result = "{\"RespCode\":-14444,\"RespMessage\":\"接口调用异常："+StringUtil.getException(e)+"\"}";
			throw e;
		}finally {
			//执行时长(毫秒)
			long time = System.currentTimeMillis() - beginTime;
			//保存日志
			saveSysLog(point, time, result);
		}
		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time,Object result) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		//获取request
		HttpServletRequest request = null;
		Method method = signature.getMethod();
    	String uuid = IDSeed.next();
    	GIDHolder.handleGID(uuid);
    	AuthInfoVo vo = KasiteConfig.createAuthInfoVo(uuid);
		String referer = "";
		String uri = "";
		String api = "";
		String user_agent =  "";
		String className = "";
		String clientUrl = "";
		String params = "";
		String methodName = "";
		try {
			request = HttpContextUtils.getHttpServletRequest();
			referer = request.getHeader("referer");
			user_agent =  request.getHeader("user-agent");
			user_agent = user_agent + ",referer=" +referer;
			uri = request.getRequestURI();
			clientUrl = AbstractController.getIpAddress(request);
			SysLog syslog = method.getAnnotation(SysLog.class);
			if(syslog != null){
				//注解上的描述
				String res = syslog.value();
				api = res;
				//如果 设置的是不保存日志，则默认返回的code ==0 的话就不保存结果集
				if(!syslog.isSaveResult() && null != result) {
					if(result instanceof R) {
						R r = (R) result;
						int code = (int) r.get("code");
						if(code == 0) {
							result = NOTRESULTR;
						}
					}
					
				}
			}
			//请求的方法名
			className = joinPoint.getTarget().getClass().getName();
			methodName = signature.getName();
			//请求的参数
			try{
				JSONObject json = (JSONObject) JSON.toJSON(request.getParameterMap());
				params = json.toJSONString();
			}catch (Exception e){
				e.printStackTrace();
				logger.error("解析入参异常",e);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("日志保存异常",e);
		}finally {
			Logger.get().log(LogLevel.INFO,
					"wsgw-log",
					LogBody.me(vo)
							.set("AuthInfo", vo.toString())// 鉴权头
							.set("UserAgent", user_agent)
							.set("uniqueReqId",uuid)// RPC请求时间戳
							.set("Api", api)//
							.set("Param", params)// 入参
							.set("ParamType", 0)// 入参类型 0 json
							.set("OutType", 0)// 出参类型 0 json
							.set("V", Version.V)// 版本号,默认1
							.set("Result", result)// 返回结果
							.set("Url", user_agent)// 目标子系统
							.set("ClassName",
									className == null ? "" : className)// 目标系统类名
							.set("MethodName",
									methodName == null ? "" : methodName)// 目标系统方法名
							.set("Times", time)// 耗时
							.set("ClientUrl", clientUrl)// 客户端IP
							.set("CurrTimes", System.currentTimeMillis())
//    							.set("resp_mills",ResptimeHolder.removeRespTime(uniqueReqId))
							.set("WsgwUrl", uri));// 网关（指接口网关或开放平台）IP，未经网关的填写空
		}
		
		
	}

	
}
