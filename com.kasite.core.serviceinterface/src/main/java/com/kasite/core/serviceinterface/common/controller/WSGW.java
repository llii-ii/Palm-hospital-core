package com.kasite.core.serviceinterface.common.controller;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.Version;
import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiList;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.log.LogLevel;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.log.NetworkUtil;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserRoleEntity;
import com.kasite.core.common.sys.verification.Base64;
import com.kasite.core.common.sys.verification.RSA;
import com.kasite.core.common.util.DataUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.WhiteListUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.common.validator.Assert;
import com.kasite.core.httpclient.http.HttpRequest;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.pay.GetPayConfigKeyUtil;
import com.kasite.core.serviceinterface.module.webui.IWebUiDiyConfig;
import com.yihu.wsgw.api.InterfaceMessage;


//@Api(value = "接口网关 /wsgw/{module}/{name}/{method}/call.do")
@RestController
@RequestMapping("/wsgw") 
public class WSGW extends AbstractController{
	
	@Autowired
	private WhiteListUtil whiteListUtil;
	
	//wsgw/MiniPay/gh_123123123123_123123/59/getToken2.do
	@AuthIgnore
    @PostMapping("/{clientId}/{configKey}/{orgCode}/getToken2.do")
//	@ApiOperation(value = "获取调用的token", notes = "BASE64加密方式获取token接口")
	@SysLog("第三方登录鉴权获取Token")
    R getToken2(
    @PathVariable("clientId")
//	@ApiParam(value = "渠道ID", required = true) 
    String clientId,
	@PathVariable("configKey")
//	@ApiParam(value = "渠道配置信息", required = true) 
    String configKey,
	@PathVariable("orgCode")
//	@ApiParam(value = "机构代码信息", required = true) 
	String orgCode,
	
	String encrypt,
	String appId,
	HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getToken(clientId,configKey,encrypt, orgCode, appId, "BASE64",request,response);
	}
	@AuthIgnore
    @PostMapping("/{clientId}/{configKey}/{orgCode}/smallProgramLogin.do")
//	@ApiOperation(value = "获取调用的token", notes = "微信小程序渠道登录接口")
	@SysLog("第三方登录鉴权获取Token")
    R smallProgramLogin(@PathVariable("clientId")
//	@ApiParam(value = "渠道ID", required = true) 
    String clientId,
	@PathVariable("configKey")
//	@ApiParam(value = "渠道配置信息", required = true) 
    String configKey, 
	@PathVariable("orgCode")
	String orgCode,
	String openId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getToken(clientId, configKey, null, orgCode, openId, "SMALLPRO", request, response);
	}
	
	@AuthIgnore
    @PostMapping("/{clientId}/{configKey}/{orgCode}/getToken.do")
//	@ApiOperation(value = "获取调用的token", notes = "第三方公私钥加密获取Token接口")
    R getTokendo(
    		@PathVariable("clientId")
//    		@ApiParam(value = "渠道ID", required = true) 
    		String clientId,
    		@PathVariable("configKey")
//    		@ApiParam(value = "渠道配置信息", required = true) 
    		String configKey,
    		String encrypt,String orgCode,String appId,String encryptType,HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getToken(clientId,configKey,encrypt, orgCode, appId, encryptType,request,response);
	}
	
    R getToken(String clientId,String configKey,String encrypt,String orgCode,String appId,String encryptType,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	long start = System.currentTimeMillis();
    	String uuid = IDSeed.next();
    	String referer = request.getHeader("referer");
		String user_agent =  request.getHeader("user-agent");
		String methodName = "getToken";
		String clientUrl = getIpAddress(request);
		String api = "api.verificat.getToken";
		
		AuthInfoVo vo = KasiteConfig.createAuthInfoVo(uuid);
		vo.setClientId(clientId);
		vo.setSign(appId);
		vo.setClientVersion(orgCode);
		
		String className = api;
		String params = "appId|"+appId+"|orgCode|"+orgCode+"|encryptType|"+encryptType+"|encrypt|"+encrypt;
		R r = R.error(-14444, "No Call Service");
		try {
			String clientName = KasiteConfig.getClientConfig(ClientConfigEnum.clientName, clientId);
			String appSecret = KasiteConfig.getClientAppSecret(clientId, appId);
			if(null == clientName) {
				r =  R.error(RetCode.Common.OAUTH_ERROR_NOPROMIIME,"该渠道ID【"+ clientId +"】，未开放请联系管理员开放。");
			}else if(StringUtil.isBlank(appSecret)) {
				r = R.error(RetCode.Common.OAUTH_ERROR_NOPROMIIME,"渠道【"+clientName+"|"+clientId+"】的应用ID【"+ appId +"】，未开放请联系管理员开放。");
			}else {
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
				Assert.isBlank2(orgCode, "orgCode");
				Assert.isBlank2(appId, "appId/openId");
				boolean isCheckSign = true;
				if(StringUtil.isNotBlank(encryptType) && encryptType.equals("BASE64")) {
					byte[] d = Base64.decode(encrypt);
					decryptStr = new String(d,"utf-8");
				}else if(StringUtil.isNotBlank(encryptType) && encryptType.equals("SMALLPRO")) {
					isCheckSign = false;
				}else{
					//通过私钥解密
					decryptStr = RSA.decrypt(encrypt, RSA.genPrivateKey(getPrivateKey(orgCode,appId)));
				}
				if(StringUtil.isNotBlank(decryptStr) && isCheckSign) {
					String[] strs = decryptStr.split(",");
					if(strs.length > 3) {
						String appIds = strs[0];
						String appSecret2 = strs[1];
						String orgCodeS = strs[2];
						String now = strs[3];//子系统当前时间
						Assert.isBlank2(appId, "appId");
						Assert.isBlank2(appSecret2, "appSecret");

						if( appSecret2.equals(appSecret) && appId.equals(appIds) && orgCode.equals(orgCodeS)) {
							KasiteConfig.print(appId+" 登陆成功.当前时间为："+now);
							r = apiLogin(uuid, true, appId, clientId, appSecret, configKey, request, response);
						} else {
							r = R.error(40001,"获取Token异常,鉴权失败，请确认 appId 和 appSecret 是有效的。appId="+appId);
						}
					}else {
						r = R.error(20000,"获取Token异常,鉴权失败，请确认 appId 和 appSecret 是有效的。appId="+appId);
					}
				}else if(isCheckSign){
					r = R.error(20000,"获取Token异常,鉴权失败，请确认 appId 和 appSecret 是有效的。appId="+appId);
				}else {
					KasiteConfig.print(appId+" 登陆成功");
					r = apiLogin(uuid, true, appId, clientId, orgCode, configKey, request, response);
				}
			}
		}catch (Exception e) {
			r = parseException(e);
			logger.error("系统异常",e);
		}finally {
			String result = r.toString();
			r.remove("Stack");
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
							.set("Times", System.currentTimeMillis() - start)// 耗时
							.set("ClientUrl", clientUrl)// 客户端IP
							.set("CurrTimes", System.currentTimeMillis())
//    							.set("resp_mills",ResptimeHolder.removeRespTime(uniqueReqId))
							.set("WsgwUrl", referer));// 网关（指接口网关或开放平台）IP，未经网关的填写空
			
		}
		return r;
    }
    
    
    
	@PostMapping("/{module}/{name}/{method}/call.do")
//    @ApiOperation(value = "接口网关入口", notes = "第三方接口网关入口")
    R rpc( 
    		@PathVariable("module")
//    		@ApiParam(value = "需要请求的 Api 的模块", required = true) 
    		String module,
    		@PathVariable("name")
//    		@ApiParam(value = "需要请求的 Api 的名称", required = true) 
    		String name,
    		@PathVariable("method")
//    		@ApiParam(value = "需要请求的 Api 方法名", required = true) 
    		String method,
//    		@ApiParam(value = "需要请求的 参数内容", required = true) 
    		String param,
//    		@ApiParam(value = "需要请求的 出参类型 1:XML,0:JSON", required = true) 
    		Integer outType,
//    		@ApiParam(value = "需要请求的 入参类型 1:XML,0:JSON", required = true) 
    		Integer paramType,
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(outType == null) {
    		outType = 1;
    	}
    	if(paramType == null) {
    		paramType = 1;
    	}
    	R r = R.error(-14444, "No Call Service");
		String uuid = IDSeed.next();
		InterfaceMessage message = new InterfaceMessage();
    	AuthInfoVo vo = createAuthInfoVo(uuid,request);
    	try {
			String api = getApi(module, method);
			message.setApiName(api);
			message.setAuthInfo(vo.toString());
			message.setOutType(1);
			message.setParam(param);
			message.setParamType(1);
			message.setSeq(Long.toString(System.currentTimeMillis()));
			message.setClientIp(com.kasite.core.common.log.NetworkUtil.getLocalIP());
			r = invoke(vo, api,paramType,outType, message, request);
		}catch (Exception ex) {
			r = parseException(ex);
		}
		return r;
	}

	
	@PostMapping("/{clientId}/{orderId}/{serviceId}/getConfigKey.do")
//	@ApiOperation(value = "获取当前订单支持的支付方式配置信息", notes = "获取当前订单支持的支付方式配置信息")
	@SysLog(value="getConfigKey")
	R getConfigKey(
	@PathVariable("clientId")
	//@ApiParam(value = "渠道ID", required = true) 
	String clientId,
	@PathVariable("orderId") 
	//@ApiParam(value = "订单号", required = true) 
	String orderId,
	@PathVariable("serviceId") 
	//@ApiParam(value = "业务类型", required = true) 
	String serviceId,
	HttpServletRequest request) throws Exception {
		//返回 TODO 订单可以支持的支付方式 后续有其它的支付方式需要前端支持支付的需要在这里扩展 
		// TODO 如果有不同支付场景需要支持不同的商户支付的时候在这里进行修改，通过订单详情获取到 对应的支付商户返回
		R r = R.ok();
		AuthInfoVo vo = createAuthInfoVo(IDSeed.next(),request);
		Boolean type = isWeChat(request);
		if(null != type) {
			RespMap resp = GetPayConfigKeyUtil.get(clientId, null, orderId, vo);
			r.put("ZfbConfigKey", resp.getString(ApiKey.GetPayConfigKey.ZfbConfigKey));
			r.put("WxPayConfigKey", resp.getString(ApiKey.GetPayConfigKey.WxPayConfigKey));
			r.put("NetPayConfigKey", resp.getString(ApiKey.GetPayConfigKey.NetPayConfigKey));
			r.put("RespCode", 10000);
		}else {
			throw new RRException("请在微信或支付宝中进行支付操作。");
		}
		return r;
	}
	
	
	@SysLog(value="getSession",isSaveResult=true)
	@PostMapping("/getSession.do")
//	@ApiOperation(value = "获取当前登录用户的session信息", notes = "获取当前登录用户的session信息")
	R getSession(HttpServletRequest request) throws Exception {
		R r = R.ok();
		SysUserEntity user = getUser();
		String channelId = user.getChannelId();
		String clientId = user.getClientId();
		String orgId = user.getOrgId();
		String orgName = user.getOrgName();
		String wxConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.WeChatConfigKey, clientId);
		String zfbConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
		String qyWeChatConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.QyWeChatConfigKey, clientId);
		r.put("clientId", clientId);//渠道ID
		r.put("configKey", channelId);//配置信息
		r.put("weChatConfigKey", wxConfigKey);
		r.put("zfbConfigKey", zfbConfigKey);
		r.put("qyWeChatConfigKey", qyWeChatConfigKey);
		r.put("orgId", orgId);//机构ID
		if(StringUtil.isBlank(orgName)) {
			orgName = KasiteConfig.getOrgName();
		}
		r.put("orgName", orgName);
		r.put("sign", user.getUsername());//当前登录用户对openid
		r.put("realName", user.getRealName());//真实姓名
		//获取角色ID
		SysUserRoleEntity role = sysUserService.queryUserRoleById(user.getId());
		if(null!=role) {
			RespMap map = sysRoleService.getRole(role.getRoleId());
			if(null != map) {
				String roleName = map.getString(ApiKey.GetSysRoleResp.roleName);
				r.put("roleName", roleName);
			}
			r.put("roleId", role.getRoleId());
			 //用户权限列表
	        Set<String> permsSet = null;
			try {
				permsSet = shiroService.getUserPermissions(user.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("获取权限列表异常",e);
			}
			r.put("permissions", permsSet);
		}
		JSONObject config = new JSONObject();
		config.put(ClientConfigEnum.isOpen.name(), KasiteConfig.getClientConfig(ClientConfigEnum.isOpen, clientId));
		config.put(ClientConfigEnum.isOpenYYClinicCard.name(), KasiteConfig.getClientConfig(ClientConfigEnum.isOpenYYClinicCard, clientId));
		// 是否显示挂号费 1 不显示  0或空则显示
		config.put(ClientConfigEnum.lockShowFee.name(), KasiteConfig.getClientConfig(ClientConfigEnum.lockShowFee, clientId));//锁号的时候显示挂号费
		BusinessTypeEnum[] buss = BusinessTypeEnum.values();
		//是否开启线上支付
		for (BusinessTypeEnum bs : buss) {
			Integer isOnlinePay = KasiteConfig.getIsOnlinePay(clientId, bs);
			config.put(bs.name(), isOnlinePay);
		}
		r.put("clientConfig", config);
		r.put("RespCode", 10000);
		return r;
	}
	@PostMapping("/getWebAppMenu.do")
//	@ApiOperation(value = "获取前端webapp应用的菜单列表", notes = "获取前端webapp应用的菜单列表")
	R getWebAppMenu(HttpServletRequest request) throws Exception {
		R r = R.ok();
		
		//TODO 获取可以查询到菜单的用户
		SysUserEntity user = getUser();
		if(whiteListUtil.isOpenWhiteList() && !whiteListUtil.isWhiteUser(user.getUsername())) {
			r.put("result", null);
			r.put("RespCode", RetCode.Common.OAUTH_ERROR_NOAUTHODINFO.getCode());
			r.put("RespMessage", "当前用户非白名单用户，无法访问。");
			return r;
		}
		
		//判断微信或支付宝进入。如果是支付宝则判断是否有独立配置支付宝菜单，如果没有配置则用微信的
		String webAppMenu = KasiteConfig.getWxWebAppMenu();
		Boolean isWeChat = isWeChat(request);
		if(null != isWeChat && !isWeChat) {
			webAppMenu = KasiteConfig.getZfbWebAppMenu();
		}
		r.put("result", webAppMenu);
		r.put("RespCode", 10000);
		return r;
	}
	
	@PostMapping("/{orgCode}/getDiyConfig.do")
//	@ApiOperation(value = "获取前端webapp应用个性化配置")
	R getDiyConfig(
			@PathVariable("orgCode") String orgCode,
			HttpServletRequest request) throws Exception {
		R r = R.ok();
		IWebUiDiyConfig configService = HandlerBuilder.get().getCallHisService(orgCode, IWebUiDiyConfig.class);//(orgCode);
		if(null != configService) {
			JSONObject json = configService.getDiyConfig();
			r.put("result", json);
			r.put("RespCode", 10000);
		}else {
			if(StringUtil.isNotBlank(KasiteConfig.getWebUiDiy())) {
				r.put("result", KasiteConfig.getWebUiDiy());
			}else {
				r.put("result", "{}");
			}
			r.put("RespCode", 10000);
		}
		return r;
	}
//
//	public static void main(String[] args) throws Exception {
//		Object obj = new IBasicServiceImpl();
//		Method refMethod = ReflectionUtils.findMethod(obj.getClass(), "queryHospitalListLocal", new Class[]{CommonReq.class});
//		InterfaceMessage msg = new InterfaceMessage();
//		msg.setAuthInfo("{'sessionKey':'123','clientId':'22','sign':'xx','configKey':'1'}");
//		Type[] genTypes = refMethod.getGenericParameterTypes();
//		Type param1Type = refMethod.getGenericParameterTypes()[0];
//        if (!(param1Type instanceof ParameterizedType)) {
//           System.out.println();
//        }else {
//        	 Type[] params = ((ParameterizedType) param1Type).getActualTypeArguments();
//        	 Type pt = params[0];
//        	 Class<?>  entityClass  =  (Class <?> ) pt;
//        	 AbsReq o = (AbsReq) com.kasite.core.common.util.ReflectionUtils.newInstance(entityClass.getName(), new Class[] {InterfaceMessage.class}, new Object[] {msg});
//        	 CommonReq<?> t = new CommonReq<AbsReq>(o);
//        	 System.out.println(params.length);
//             Object retObj = ReflectionUtils.invokeMethod(refMethod, obj, t);
//        }
//	}
	 
	
	
	/**
	 * 调用业务逻辑
	 * @param api
	 * @param method
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected R invoke(AuthInfoVo vo,String api,int paramType,int outType,InterfaceMessage message,HttpServletRequest request) throws Exception {
		long start = System.currentTimeMillis();
		String[] tmp = api.split("\\.");
		String module = tmp[0];
		String name = tmp[1];
		String methodName = tmp[2];
    	String referer = request.getHeader("referer");
		String user_agent =  request.getHeader("user-agent");
		String clientIp = getIpAddress(request);
		String className = api;
		/**
		 * TODO  渠道调用限制  IP白名单  防刷接口限制 
		 */
		ApiList.me();
		
		R r = R.error(-14444, "No Call Service");
		try {
			Object obj = SpringContextUtil.getBean(module+"."+name);
			if(null == obj) {
				r= R.error( 40014, "【"+module+"."+name+"】没有找到接口实现类，请联系管理员。");
			}else {
				className = obj.getClass().getName();
				methodName = tmp[2];
				Method refMethod = ReflectionUtils.findMethod(obj.getClass(), methodName, new Class[]{InterfaceMessage.class});
				if(null == refMethod) {
					r= R.error( 40013, "在【"+className+"】模块下没有找到对应的方法，请联系管理员。【"+ methodName +"】");
				}else {
					Object[] param1 = new Object[1];
			        param1[0]= message;
			        Object retObj = ReflectionUtils.invokeMethod(refMethod, obj, param1);
		    		if(null != retObj) {
		    			if(outType == 1) {
		    				r = R.ok().put("result", retObj.toString());
		    			}else {
		    				JSONObject resJs = DataUtils.documentToJSONObject(retObj.toString());
			    			JSONArray pageArr = resJs.getJSONArray("Page");
			    			if(null != pageArr && pageArr.size() == 1) {
			    				resJs.put("Page", pageArr.get(0));
			    			}
			    			r = R.ok(resJs);
		    			}
		    			
		    		}else {
		    			r = R.error( 50000, "接口调用无返回值："+ obj.getClass().getName() +" : "+tmp[2]);
		    		}
				}
			}
		}catch (Exception ex) {
			r = parseException(ex);
		}finally {
			if(null == r) {
				r = R.error("请求异常，业务执行失败。");
			}
			String result = r.toString();
			r.remove("Stack");
			long mills =  System.currentTimeMillis() - start;
			Logger.get().log(LogLevel.INFO,
					"wsgw-log",
					LogBody.me(vo)
					.set("UserAgent", user_agent)
					.set("AuthInfo", vo.toString())// 鉴权头
					.set("uniqueReqId",vo.getUuid())// RPC请求时间戳
					.set("Api", api)//
					.set("Param", message.getParam().length()>10000000?"":message.getParam())// 入参
					.set("ParamType", message.getParamType())// 入参类型 0 json
					.set("OutType", message.getOutType())// 出参类型 0 json
					.set("V", Version.V)// 版本号,默认1
					.set("Result", result)// 返回结果
					.set("Url", user_agent)// 目标子系统
					.set("ClassName",
							className == null ? "" : className)// 目标系统类名
					.set("MethodName",
							methodName == null ? "" : methodName)// 目标系统方法名
					.set("Times", mills)// 耗时
					.set("ClientUrl", clientIp)// 客户端IP
					.set("CurrTimes", System.currentTimeMillis())
					.set("WsgwUrl", referer));// 网关（指接口网关或开放平台）IP，未经网关的填写空
		}
		return r;
	}
	private static Map<String, ApiModule.His> apiMap = new HashMap<>();
	static {
		ApiModule.His[] apis = ApiModule.His.values();
		for (ApiModule.His his : apis) {
			apiMap.put(his.name(), his);
		}
	}
	private static ApiModule.His getHisApi(String name){
		return apiMap.get(name);
	}
	
	@PostMapping("/{diyUrl}/{diyModule}/{api}/{callType}/{soapReturnFormat}/{format}/callApi.do")
//    @ApiOperation(value = "接口网关入口", notes = "轻应用接口网关入口")
    String callHisApi( 
    		@PathVariable("diyUrl")
//    		@ApiParam(value = "需要请求的 Api 的接口Url枚举", required = true) 
    		String diyUrl,
    		@PathVariable("diyModule")
//    		@ApiParam(value = "需要请求的 diyModule his方法的diy请求模版", required = true) 
    		String diyModule,
    		@PathVariable("api")
//    		@ApiParam(value = "需要请求的 HisApi 方法名", required = true) 
    		String api,
    		@PathVariable("callType")
//    		@ApiParam(value = "需要请求的 callType soap1,soap2,post,get", required = true) 
    		String callType,
    		@PathVariable("soapReturnFormat")
//    		@ApiParam(value = "需要请求的 returnFormat SoapFormat：ret,ns1out,cdata", required = true) 
    		String soapReturnFormat,
    		@PathVariable("format")
//    		@ApiParam(value = "需要请求的 returnFormat json,xml", required = true)
    		String format,
//    		@ApiParam(value = "需要请求的 参数内容", required = true) 
    		String apiParam,
    		HttpServletRequest request, HttpServletResponse httpResponse) throws Exception {
		R r = R.error(-14444, "未执行业务");
		String uuid = IDSeed.next();
    	AuthInfoVo vo = createAuthInfoVo(uuid,request);
    	try {
			JSONObject json = JSONObject.parseObject(apiParam);
			String callHisUrl = KasiteConfig.getDiyVal(diyUrl);
			String callHisReqModule = KasiteConfig.getDiyVal(diyModule);
			//=============================================================================================================================
			String result = null;
			long startTime = System.currentTimeMillis();
			RequestType type = RequestType.valueOf(callType);
			boolean isSuccess = false;
			int httpStausCode = -14444;
			String params  = KasiteConfig.getCallModule(json.getJSONObject("Req").getJSONObject("Data"), callHisReqModule);
			SoapResponseVo response = null;
			try {
				HttpRequstBusSender sender = HttpRequestBus.create(callHisUrl, type);
				sender.setParam(params);
				response =  sender.send();
				httpStausCode = response.getCode();
				if(response==null || httpStausCode!=200) {
					//HTTP 请求失败
					throw new RRException(RetCode.HIS.ERROR_14444,"调用HIS接口异常。HttpStatus:"+ httpStausCode +" 返回："+ response.getResult());
				}
				isSuccess = true;
				r = R.ok();
				result = response.getResult();
				//解析soap请求返回串，去除soap格式
				if(StringUtil.isNotBlank(result)) {
    				result = HttpRequest.formateSoapResp(result, HttpRequest.ResultFormat.valueOf(soapReturnFormat));
    				if(StringUtil.isNotBlank(result)) {
    					if("xml".equals(format)) {
    						result = DataUtils.documentToJSONObject(result).toJSONString();
    					}else if("json".equals(format)) {
    						JSONObject o = JSONObject.parseObject(result);
    						result = o.toJSONString();
    					}
    				}
				}
			}catch (Exception e) {
				e.printStackTrace();LogUtil.error(logger, e);
				LogUtil.error(logger, vo, callHisUrl, e);
				throw LogUtil.throwRRException(e, RetCode.HIS.ERROR_14444);
			}finally {
				String retVal = "未获取到结果";
				if(null != response) {
					retVal = response.getResult();
				}
				StringBuffer sbf = new StringBuffer();
				sbf.append(diyUrl).append(KstHosConstant.COMSP)
				.append(diyModule).append(KstHosConstant.COMSP)
				.append(api).append(KstHosConstant.COMSP)
				.append(callType).append(KstHosConstant.COMSP)
				.append(soapReturnFormat).append(KstHosConstant.COMSP)
				.append(format).append(KstHosConstant.COMSP)
				;
				LogUtil.saveCallHisLog(null,vo, null, getHisApi(api), params, retVal,  sbf.toString() ,
						System.currentTimeMillis() - startTime, callHisUrl ,type,isSuccess,httpStausCode);
			}
			//=============================================================================================================================
			r.put("result", result);
		}catch (Exception ex) {
			r = parseException(ex);
		}
		return r.toString();
	}
	
	

	/**
	 * 掌医入口
	 * @param module
	 * @param method
	 * @param apiParam
	 * @param outType
	 * @param paramType
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    @PostMapping("/{module}/{method}/callApi.do")
    String rpc( 
    		@PathVariable("module")
    		String module,
    		@PathVariable("method")
    		String method,
    		String apiParam,
    		Integer outType,
    		Integer paramType,
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		R r = R.error(-14444, "No Call Service");
		JSONObject bodyJson = readRequestBodyJsonCaseHeader(request);
		String uuid = IDSeed.next();
		if(null == paramType && null == bodyJson) {
			paramType = 1;
		}else if(null != bodyJson) {
			paramType = 0;
		}
		InterfaceMessage message = new InterfaceMessage();
    	AuthInfoVo vo = createAuthInfoVo(uuid,request);
    	try {
    		//默认返回结果为json
    		if(null == outType) {
    			outType = 0;
    		}
    		String api = getApi(module, method);
    		//是管理后台的请求，需进行shiro权限验证
//    		if(vo.getClientId()!=null && KstHosConstant.SYSTEMMANAGER_CHANNEL_ID.equals(vo.getClientId())) {
//    			boolean isPerm = isPermitted(api);
//    			if(!isPerm) {
//    				return R.error(RetCode.Common.OAUTH_ERROR_NOPROMIIME, "暂无访问该操作的权限，请联系管理员开通").toString();
//    			}
//    		}
			message.setApiName(api);
			message.setAuthInfo(vo.toString());
			message.setOutType(outType);//返回JSON
			
			if(paramType == 2) {
				Map<String,String[]> paraMap =request.getParameterMap();
				JSONObject doc = new JSONObject();
				JSONObject reqJson = new JSONObject();
				JSONObject data = new JSONObject();
				if(null != paraMap && paraMap.size() > 0) {
					if(paraMap!=null && !paraMap.isEmpty()){
						Set<String> keySet = paraMap.keySet();
						for(String key:keySet){
							Object value = paraMap.get(key);
							if("configKey".equals(key) || "appId".equals(key) || "cargeType".equals(key) || "sign".equals(key)) {
								continue;
							}
							if(value!=null){
								if(value.getClass().isArray()){//数组
									int length = Array.getLength(value);
									if(length==1){
										data.put(key, Array.get(value, 0).toString());
									}else{
										//目前不支持数组形式入参
										StringBuffer buffer = new StringBuffer("");
										for(int i=0;i<length;i++){
											buffer.append(Array.get(value, i).toString()).append("#");//多个用井号隔开
										}
										data.put(key, buffer.toString());
									}
								}else{//非数组
									data.put(key, value.toString());
								}
							}
						}
					}
				}
				reqJson.put("Data", data);
				doc.put("Req", reqJson);
//				message.setJsonParam(doc);				
				message.setParam(doc.toJSONString());
				message.setParamType(0); 
			}else if(null != bodyJson) {
				JSONObject doc = new JSONObject();
				JSONObject reqJson = new JSONObject();
				reqJson.put("Data", bodyJson);
				doc.put("Req", reqJson);
				message.setParam(doc.toJSONString());
//				message.setJsonParam(doc);
				message.setParamType(0); 
			}else {
				message.setParam(DataUtils.json2XML(apiParam));
				message.setParamType(paramType);
			}
			message.setSeq(Long.toString(System.currentTimeMillis()));
			message.setClientIp(NetworkUtil.getLocalIP());
			r = invoke(vo, api,paramType,outType, message, request);
		}catch (Exception ex) {
			r = parseException(ex);
		}
		return r.toString();
	}
   

    private boolean isPermitted(String api) {
    	Subject subject = SecurityUtils.getSubject();
    	return subject.isPermitted(api);
    }
    
}
