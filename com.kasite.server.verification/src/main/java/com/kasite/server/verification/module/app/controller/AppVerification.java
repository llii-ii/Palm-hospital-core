package com.kasite.server.verification.module.app.controller;

import java.security.Key;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.remoting.standard.DateOper;
import com.kasite.core.common.annotation.ApiCallLog;
import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.sys.verification.RSA;
import com.kasite.core.common.sys.verification.TokenGenerator;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.rsa.KasiteRSAUtil;
import com.kasite.core.common.validator.Assert;
import com.kasite.server.verification.common.controller.AbstractController;
import com.kasite.server.verification.module.app.entity.AppEntity;
import com.kasite.server.verification.module.app.service.AccessTokenService;
import com.kasite.server.verification.module.app.service.AppService;

@RestController
@RequestMapping("/gateway/verificat")
public class AppVerification extends AbstractController {
	private final static Logger logger = LoggerFactory.getLogger(AppVerification.class);
	@Autowired
	private AccessTokenService accessTokenService;
	@Autowired
	private AppService appService;
	
	/**
	 * 获取 api token
	 * @throws Exception 
	 */
	@AuthIgnore
	@ApiCallLog("获取api的token")
	@PostMapping("getToken")
	public R getToekn(String encrypt,String orgCode,String appId,String localIp,HttpServletRequest request) throws Exception {
		logger.info("/gateway/verificat/getToekn |AppId="+appId+"|OrgCode="+orgCode+"|localIp"+localIp);
		if(com.kasite.core.httpclient.http.StringUtils.isNotBlank(encrypt)) {
			encrypt = encrypt.replace(" ","+");
		}
		logger.debug(encrypt);
		Assert.isBlank2(orgCode, "orgCode");
		Assert.isBlank2(appId, "appId");
		
		AppEntity app = appService.getApp(appId);
		if(null == app) {
			throw new RRException("该应用未在中心进行注册，请联系管理人员进行注册。");
		}
		String decryptStr = null;
		if("RSA2".equals(app.getSignType())) {
			//通过RSA2 解密报文内容
			String pk = app.getPrivateKey();
			try {
				decryptStr = KasiteRSAUtil.rsaDecrypt(encrypt, pk, "UTF-8");
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("公私钥不匹配解密失败。appId="+appId);
				throw new RRException("appId 对应的公钥和私钥不匹配，请联系管理员进行确认。appId="+appId);
			}
		}else {
			Key privatekey = null;
			String pk = "";
			//通过私钥解密
			try {
				pk = app.getPrivateKey();
				privatekey = RSA.genPrivateKey(pk);
			}catch (Exception e) {
				logger.error("获取appId 对应的私钥是异常的，请联系管理员进行修改。appId="+appId +" pk="+ pk ,e);
				throw new RRException("获取appId 对应的私钥是异常的，请联系管理员进行修改。appId="+appId);
			}
			try {
				decryptStr = RSA.decrypt(encrypt, privatekey);
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("公私钥不匹配解密失败。appId="+appId);
				throw new RRException("appId 对应的公钥和私钥不匹配，请联系管理员进行确认。appId="+appId);
			}
		}
		String[] strs = decryptStr.split(",");
		if(strs.length > 3) {
			String appIds = strs[0];
			String appSecret = strs[1];
			String orgCodeS = strs[2];
			String now = strs[3];//子系统当前时间
			Assert.isBlank2(appId, "appId");
			Assert.isBlank2(appSecret, "appSecret");
			
			if(checkAppIdAndAppSecret(appId, appSecret, app) && appId.equals(appIds) && orgCode.equals(orgCodeS)) {
				logger.info(appId+" 登陆成功.");
				String format = "yyyy-MM-dd HH:mm:ss";
				String createTime = DateOper.getNow(format);
				String invalidTime = DateOper.addMinute(createTime, 120, format);
				String access_token = getAccess_Token(appId, appSecret, orgCode, createTime,invalidTime,now);
				JSONObject obj = new JSONObject();
				obj.put("access_token", access_token);
				obj.put("create_time", createTime);
				obj.put("invalid_time", invalidTime);
				obj.put("now_time", now);
				obj.put("app_id", appId);
				obj.put("org_code", orgCode);
				return R.ok(obj);
			} else {
				throw new RRException("获取Token异常,鉴权失败，请确认 appId 和 appSecret 是有效的。appId="+appId, 20005);
			}
			
			
		}else {
			throw new RRException("参数异常 : "+ decryptStr, 20000);
		}
	}
	
	/**
	 * 校验应用ID 和应用安全码的有效性。
	 * @param appId
	 * @param appSecret
	 * @return
	 */
	private boolean checkAppIdAndAppSecret(String appId,String appSecret,AppEntity app) {
		if(app.getAppSecret().equals(appSecret)) {
			logger.info("校验通过："+ appId + " 安全码："+ appSecret);
			return true;
		}
		return false;
	}
	
	public String getAccess_Token(String appId,String appSecret,String orgCode,String createTime,String invalidTime,String appNowTime) throws Exception {
		String access_token = TokenGenerator.generateValue();
		accessTokenService.save(access_token, appId, DateOper.parse(createTime),DateOper.parse(invalidTime),appNowTime);
		return access_token;
	}
	
	public String getPrivateKey(String appId) throws Exception {
		AppEntity app = appService.getApp(appId);
		if(null == app) {
			throw new RRException("应用不存在，请到卡斯特中心进行注册。appId="+appId);
		}
		int status = app.getStatus();
		if(status != 1) {
			throw new RRException("应用已经下架，请找系统管理员核实。appId="+appId);
		}
		return app.getPrivateKey();
	}
	
	public String getPublicKeyDB(String appId) throws Exception {
		AppEntity app = appService.getApp(appId);
		if(null == app) {
			throw new RRException("应用不存在，请到卡斯特中心进行注册。appId="+appId);
		}
		int status = app.getStatus();
		if(status != 1) {
			throw new RRException("应用已经下架，请找系统管理员核实。appId="+appId);
		}
		return app.getPublicKey();
	}

	/**
	 * 获取 PublicKey 公钥
	 * @throws Exception 
	 */
	@AuthIgnore
	@ApiCallLog("获取 PublicKey 公钥")
	@RequestMapping(value = "getPublicKey", method = RequestMethod.GET)
	public R getPublicKey(String appId) throws Exception {
		Assert.isBlank2(appId, "appId");
		logger.info("获取公钥 appId："+ appId);
		return R.ok().put("publicKey", getPublicKeyDB(appId));
	}
	

}
