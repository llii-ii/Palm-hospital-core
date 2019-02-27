package com.kasite.core.common.sys.oauth.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.Version;
import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.log.LogLevel;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.oauth.service.AccessTokenService;
import com.kasite.core.common.sys.verification.Base64;
import com.kasite.core.common.sys.verification.RSA;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.common.validator.Assert;

@RestController
@RequestMapping("/api/verificat")
public class VerificatController extends OauthAbstractController{
	public final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_SYSTEM);
	@Autowired
    private AccessTokenService accessTokenServiceApi;
	@AuthIgnore
    @PostMapping("/refreshToken.do")
    R refreshToken(String encrypt,String orgCode,String appId) throws Exception {
		return R.ok();
	}
    
	@AuthIgnore
    @PostMapping("/getToken.do")
//	@ApiOperation(value = "获取调用的token", notes = "获取调用的token")
    R getTokendo(String encrypt,String orgCode,String appId,String encryptType,HttpServletRequest request) throws Exception {
		return getToken(encrypt, orgCode, appId, encryptType,request);
	}
	@AuthIgnore
    @PostMapping("/getToken")
    R getToken(String encrypt,String orgCode,String appId,String encryptType,HttpServletRequest request) throws Exception {
		long start = System.currentTimeMillis();
    	String uuid = IDSeed.next();
    	String referer = request.getHeader("referer");
		String user_agent =  request.getHeader("user-agent");
		String methodName = "getToken";
		String clientUrl = getIpAddress(request);
		String api = "api.verificat.getToken";
		AuthInfoVo vo = KasiteConfig.createAuthInfoVo(uuid);
		vo.setClientId(appId);
		vo.setSign(appId);
		vo.setClientVersion(orgCode);
		String className = api;
		String params = "appId|"+appId+"|orgCode|"+orgCode+"|encryptType|"+encryptType+"|encrypt|"+encrypt;
		R r = R.error(-14444, "No Call Service");
		try {
			if(null == orgCode) {
				String body = ReadAsChars(request);
				if(StringUtil.isNotBlank(body)) {
					try {
						JSONObject json = JSON.parseObject(body);
						orgCode = json.getString("orgCode");
						encrypt = json.getString("encrypt");
						appId = json.getString("appId");
						encryptType = json.getString("encryptType");
					}catch (Exception e) {
					}
				}
			}
			String decryptStr = null;
			if(com.kasite.core.httpclient.http.StringUtils.isNotBlank(encrypt)) {
				encrypt = encrypt.replace(" ","+");
			}
			logger.debug(encrypt);
			Assert.isBlank2(orgCode, "orgCode");
			Assert.isBlank2(appId, "appId");
			
			if(StringUtil.isNotBlank(encryptType) && encryptType.equals("BASE64")) {
				byte[] d = Base64.decode(encrypt);
				decryptStr = new String(d,"utf-8");
			}else {
				//通过私钥解密
				decryptStr = RSA.decrypt(encrypt, RSA.genPrivateKey(getPrivateKey(orgCode,appId)));
			}
			
			String[] strs = decryptStr.split(",");
			if(strs.length > 3) {
				String appIds = strs[0];
				String appSecret = strs[1];
				String orgCodeS = strs[2];
				String now = strs[3];//子系统当前时间
				Assert.isBlank2(appId, "appId");
				Assert.isBlank2(appSecret, "appSecret");

				if(checkAppIdAndAppSecret(orgCode, appId, appSecret) && appId.equals(appIds) && orgCode.equals(orgCodeS)) {
					KasiteConfig.print(appId+" 登陆成功.");
					String format = "yyyy-MM-dd HH:mm:ss";
					String createTime = DateOper.getNow(format);
					String invalidTime = DateOper.addMinute(createTime, KasiteConfig.getAccessTokenValidTime(), format);
					Date inv = DateOper.parse(invalidTime);
					Date n = DateOper.getNowDate();
					long lv = inv.getTime() - n.getTime();
					String access_token = accessTokenServiceApi.crateAccessToken(lv,appId, orgCode,createTime,invalidTime,now);
					JSONObject obj = new JSONObject();
					obj.put("access_token", access_token);
					obj.put("create_time", createTime);
					obj.put("invalid_time", invalidTime);
					vo.setSign(access_token);
					r =  R.ok(obj); 
				} else {
					r = R.error(40001,"获取Token异常,鉴权失败，请确认 appId 和 appSecret 是有效的。appId="+appId);
				}
			}else {
				r = R.error(20000,"获取Token异常,鉴权失败，请确认 appId 和 appSecret 是有效的。appId="+appId);
			}
		}catch (Exception e) {
			e.printStackTrace();
			r = R.error(e.getMessage());
			throw e;
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
							.set("Result", r.toString())// 返回结果
							.set("Url", user_agent)// 目标子系统
							.set("ClassName",
									className == null ? "" : className)// 目标系统类名
							.set("MethodName",
									methodName == null ? "" : methodName)// 目标系统方法名
							.set("Times", System.currentTimeMillis() - start)// 耗时
							.set("ClientUrl", clientUrl)// 客户端IP
							.set("CurrTimes", System.currentTimeMillis())
//    							.set("resp_mills",ResptimeHolder.removeRespTime(uniqueReqId))
							.set("WsgwUrl", referer));// 网关（指接口网关或开放平台）IP，未经网关的填写空
			
		}
		return r;
    }
	
	
	
	
	
	/**
	 * 校验 是否正确
	 * @param appId
	 * @param appSecret
	 * @return
	 */
	private boolean checkAppIdAndAppSecret(String orgCode,String appId, String appSecret) {
		String pwd = LocalOAuthUtil.getInstall().getAppSecret(appId);
		if(appSecret.equals(pwd)) {
			return true;
		}
		return false;
	}

	/**
	 * 返回本地配置的私钥
	 * @param appId
	 * @return
	 * @throws Exception
	 */
	public String getPrivateKey(String orgCode,String appId) throws Exception {
		LocalOAuthUtil util = LocalOAuthUtil.getInstall();
		String pk = util.getPrivateKey(appId);
		if(StringUtil.isBlank(pk)) {
			throw new RRException("未找到机构：【"+orgCode+"】 下 应用: 【"+ appId+"】 的私钥");
		}
		return pk;
	}
	
}
