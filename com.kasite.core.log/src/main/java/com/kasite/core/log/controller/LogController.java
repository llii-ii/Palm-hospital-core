package com.kasite.core.log.controller;

import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.Version;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiModule.His;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.log.DiyLogVo;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.log.LogLevel;
import com.kasite.core.common.log.LogSourceKey;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.log.WsgwLogVo;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
@RestController
@RequestMapping("/save")
public class LogController {
	@RequestMapping("/wsgwlog.do")
	R wsgwlog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String reqParam = request.getParameter("reqParam");
		JSONObject json = JSONObject.parseObject(reqParam);
		WsgwLogVo vo = JSON.toJavaObject(json, WsgwLogVo.class);
		if(null != vo) {
			Logger.get().log(LogLevel.INFO,
				"wsgw-log",
				LogBody.me(KasiteConfig.getAuthInfo(vo.getAuthInfo()))
				.set("UserAgent", vo.getUserAgent())
				.set("AuthInfo", vo.getAuthInfo())// 鉴权头
				.set("uniqueReqId",vo.getUniqueReqId())// RPC请求时间戳
				.set("Api", vo.getApi())//
				.set("Param", vo.getParam().length()>10000000?"":vo.getParam())// 入参
				.set("ParamType", vo.getParamType())// 入参类型 0 json
				.set("OutType", vo.getOutType())// 出参类型 0 json
				.set("V", Version.V)// 版本号,默认1
				.set("Result", vo.getResult())// 返回结果
				.set("Url", vo.getUserAgent())// 目标子系统
				.set("ClassName",
						vo.getClassName() == null ? "" : vo.getClassName())// 目标系统类名
				.set("MethodName",
						vo.getMethodName() == null ? "" : vo.getMethodName())// 目标系统方法名
				.set("Times", vo.getMills())// 耗时
				.set("ClientUrl", vo.getClientIp())// 客户端IP
				.set("CurrTimes", System.currentTimeMillis())
				.set("WsgwUrl", vo.getReferer()));// 网关（指接口网关或开放平台）IP，未经网关的填写空
		}
		return R.ok();
	} 
	
	
	
	@RequestMapping("/hislog.do")
	R hislog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String reqParam = request.getParameter("reqParam");
		JSONObject json = JSONObject.parseObject(reqParam);
		WsgwLogVo vo = JSON.toJavaObject(json, WsgwLogVo.class);
		if(null != vo) {
			String params = vo.getParam();
			String orderId = vo.getOrderId();
			Boolean isSuccess = vo.getIsSuccess();
			AuthInfoVo authvo = KasiteConfig.getAuthInfo(vo.getAuthInfo());
			LogBody body = null;
			try {
				if(null == params ) {
					params = "";
				}
				if(StringUtil.isBlank(orderId)) {
					//如果订单号为空则不保存 
					orderId = "NotOrderId_"+ IDSeed.next();
				}
				String code = KstHosConstant.SUCCESSCODE;
				if(null != isSuccess && !isSuccess) {
					code = "-400010";
				}
				His api = His.valueOf(vo.getApi());
				boolean isSaveResult = KasiteConfig.getIsSaveCallResult(authvo,api);
				//如果不需要保存结果集则将结果集赋值为{code:10000,message:'成功'}
				if(!isSaveResult && isSuccess) {
				}else {
					body  = LogBody.me(authvo)
							.set(LogSourceKey.OrderId, orderId)
							.set(LogSourceKey.AuthInfo, vo.toString())// 鉴权头
							.set(LogSourceKey.uniqueReqId, vo.getUniqueReqId())
							.set(LogSourceKey.Api, vo.getApi())//
							.set(LogSourceKey.Param, params.length()>10000000?"":params)// 入参
							.set(LogSourceKey.ParamType, 1)// 入参类型 0 json
							.set(LogSourceKey.OutType, 1)// 出参类型 0 json
							.set(LogSourceKey.V, Version.V)// 版本号,默认1
							.set(LogSourceKey.Result, vo.getResult())// 返回结果
							.set(LogSourceKey.Url, vo.getUserAgent())// 目标子系统
							.set(LogSourceKey.ClassName,vo.getClassName())// 目标系统类名
							.set(LogSourceKey.MethodName,vo.getMethodName())// 目标系统方法名
							.set(LogSourceKey.Times, vo.getMills())// 耗时
							.set(LogSourceKey.ClientUrl, vo.getCallType())// 客户端IP
							.set(LogSourceKey.CurrTimes, System.currentTimeMillis())
							.set(LogSourceKey.Code, code);
					Logger.get().log(LogLevel.INFO,
							"wsgw-log",
							body
							);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return R.ok();
	}
	
	
	@RequestMapping("/diylog.do")
	R diylog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String reqParam = request.getParameter("reqParam");
		JSONObject json = JSONObject.parseObject(reqParam);
		DiyLogVo vo = JSON.toJavaObject(json, DiyLogVo.class);
		if(null != vo && StringUtil.isNotBlank(vo.getModule())) {
			AuthInfoVo authvo = KasiteConfig.getAuthInfo(vo.getAuthInfo());
			LogBody body = LogBody.me(authvo);
			JSONObject bodyJson = JSONObject.parseObject(vo.getLogBody());
			for (Entry<String, Object>  entity : bodyJson.entrySet()) {
				body.set(entity.getKey(), entity.getValue());
			}
			Logger.get().info(vo.getModule(), body);
		}
		return R.ok();
	} 
}
