package com.kasite.core.common.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.ApiList;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.service.RedisUtil;
import com.kasite.core.common.sys.service.ShiroService;
import com.kasite.core.common.sys.service.SysRoleService;
import com.kasite.core.common.sys.service.SysUserService;
import com.kasite.core.common.sys.service.SysUserTokenService;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;
import com.kasite.core.common.util.CookieTool;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.httpclient.http.HttpRequest;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.yihu.hos.constant.IConstant;
import com.yihu.wsgw.api.InterfaceMessage;


/**
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	public static final String ERRORPAGE= "/pageError.html?code={0}&msg={1}";
	private final static long tokenTimes = 25 * 60 * 60 ; //25小时失效
	@Autowired(required=false)
	protected SysUserService sysUserService;
	@Autowired(required=false)
	protected SysRoleService sysRoleService;
	@Autowired(required=false)
	protected ShiroService shiroService;
	@Autowired(required=false)
	protected SysUserTokenService sysUserTokenService;
	@Autowired(required=false)
	private SysUserTokenService tokenService;
	 /** 基于@ExceptionHandler异常处理 */  
    @ExceptionHandler  
    public R exp(HttpServletRequest request, Exception ex) {  
    	try{
	    	if(null != ex && ex.getStackTrace().length > 0){
	    		StringBuffer sbf = new StringBuffer();
	        	//请求的方法名
	    		String className ="";
	    		String methodName ="";
	    		int line = 0;
	        	methodName = ex.getStackTrace()[0].getMethodName();
	        	className = ex.getStackTrace()[0].getClassName();
	        	line = ex.getStackTrace()[0].getLineNumber();
	        	String params = new Gson().toJson(request.getParameterMap());
	        	String exception = StringUtil.getExceptionStack(ex);
	        	if(StringUtil.isNotBlank(exception) && exception.length() > 200){
	        		exception = exception.substring(0, 200);
	        	}
	        	sbf.append(className).append(".").append(methodName).append("(").append(line).append(")");
	        	sbf.append("\r\n").append(params).append("\r\n").append(exception);
	    		logger.error(sbf.toString());
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    		LogUtil.error(logger, e);
    	}
    	ex.printStackTrace();
    	LogUtil.error(logger, ex);
    	if(ex instanceof RRException){
    		RRException exp = (RRException) ex;
    		return R.error(exp.getCode(),exp.getMsg());
    	}
    	if(null != ex.getCause() && "org.apache.shiro.authz.AuthorizationException".equals(ex.getCause().getClass().getName())) {
    		return R.error( RetCode.Common.OAUTH_ERROR_NOPROMIIME.getCode(),RetCode.Common.OAUTH_ERROR_NOPROMIIME.getMessage()+":"+ex.getMessage());
    	}
        return R.error(ex);
    }  
    /**
     * 如果有用户的openid或userid的时候进行登录动作
     * @param openid
     * @param clientId
     * @param configKey
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected AuthInfoVo login(boolean isNewLogin,String openid,String clientId,String configKey,String realName,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	return login(IDSeed.next(), isNewLogin, openid, clientId, configKey, realName,request, response);
    }

    /**
	 * 把http请求的参数处理成<String,String>形式
	 * @param paraMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected static Map<String, String> changeMap(Map paraMap){
		Map<String, String> resultMap = new HashMap<String, String>();
		if(paraMap!=null && !paraMap.isEmpty()){
			@SuppressWarnings("unchecked")
			Set<String> keySet = paraMap.keySet();
			for(String key:keySet){
				Object value = paraMap.get(key);
				if(value==null){//空
					resultMap.put(key, "");
				}else if(value.getClass().isArray()){//数组
					int length = Array.getLength(value);
					if(length==0){
						resultMap.put(key, "");
					}else if(length==1){
						resultMap.put(key, Array.get(value, 0).toString());
					}else{
						StringBuffer buffer = new StringBuffer("");
						for(int i=0;i<length;i++){
							buffer.append(Array.get(value, i).toString()).append("#");//多个用井号隔开
						}
						resultMap.put(key, buffer.toString());
					}
				}else{//非数组
					resultMap.put(key, value.toString());
				}
			}
		}
		return resultMap;
	}
    /**
     * 第三方调用的时候生成的Token
     * @param openid
     * @param clientId
     * @param configKey
     * @param request
     * @param response
     * @param tokenTimes  token失效时间
     * @return
     * @throws Exception
     */
    protected R apiLogin(String uuid,boolean isNewLogin,String appId,String clientId,
    		String password,String configKey,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	SysUserEntity user = sysUserService.queryByUserName(appId);
    	R r = R.error("not login");
    	String clientName = KasiteConfig.getClientConfig(ClientConfigEnum.clientName, clientId);
		//账号不存在、密码错误
		if(user == null) {
//			return R.error("账号或密码不正确");
			user = new SysUserEntity();
			user.setUsername(appId);
			user.setPassword(password);
			user.setOperatorId(appId);
			user.setOperatorName(LocalOAuthUtil.getInstall().getAppName(appId));
			user.setStatus(1);
			user.setOrgId(KasiteConfig.getOrgCode());
			user.setOrgName(KasiteConfig.getOrgName());
			user.setChannelId(configKey);
			user.setChannelName(clientName);
			user.setClientId(clientId);
			sysUserService.save(user);
		}else {
			if(	!user.getChannelId().equals(configKey)  || !user.getClientId().equals(clientId) ) {
				SysUserEntity u = new SysUserEntity();
				u.setClientId(clientId);
				u.setChannelId(configKey);
				u.setChannelName(clientName);
				sysUserService.updateUserClientIdAndConfigKey(user.getId(), u);
			}
			user.setChannelId(configKey);
			user.setClientId(clientId);
			user.setChannelName(clientName);
		}
		String useragent = request.getHeader("user-agent");
		if(null != useragent && useragent.length() > 200){
			useragent = useragent.substring(0,199);
		}
		//判断是否生成过token
		SysUserTokenEntity tokenEntity = tokenService.queryByUserId(user.getId());
		String token = tokenEntity.getToken();
		//判断是否过期如果没有过期都是用旧的token
		if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
			token = null;
        }
    	//生成token，并保存到数据库 失效后需要重新申请
		r = sysUserTokenService.createToken(user.getId(),useragent,tokenTimes,token);
		return r;
    }
    /**
     * 如果有用户的openid或userid的时候进行登录动作
     * @param openid
     * @param clientId
     * @param configKey
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected AuthInfoVo login(String uuid,boolean isNewLogin,String openid,String clientId,String configKey,String realName,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	SysUserEntity user = sysUserService.queryByUserName(openid);
    	String channelName = KasiteConfig.getClientConfig(ClientConfigEnum.clientName, clientId);
		String operatorName = "未知";
		String orgCode = KasiteConfig.getOrgCode();
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(clientId,configKey);
		operatorName = channelTypeEnum.getTitle();
		//账号不存在、密码错误
		if(user == null) {
//			return R.error("账号或密码不正确");
			user = new SysUserEntity();
			user.setUsername(openid);
			user.setOperatorId(configKey);
			user.setOperatorName(operatorName);
			user.setStatus(1);
			user.setOrgId(KasiteConfig.getOrgCode());
			user.setOrgName(KasiteConfig.getOrgName());
			user.setChannelId(configKey);
			user.setChannelName(channelName);
			user.setClientId(clientId);
			sysUserService.save(user);
		}else {
			SysUserEntity u = new SysUserEntity();
			u.setClientId(clientId);
			u.setChannelId(configKey);
			u.setOrgId(orgCode);
			u.setOrgName(KasiteConfig.getOrgName());
			u.setChannelName(channelName);
			sysUserService.updateUserClientIdAndConfigKey(user.getId(), u);
			user.setChannelId(configKey);
			user.setClientId(clientId);
			user.setChannelName(channelName);
			user.setOperatorName(operatorName);
		}
		String useragent = request.getHeader("user-agent");
		if(null != useragent && useragent.length() > 200){
			useragent = useragent.substring(0,199);
		}
		String token =  null;
		//判断token是否存在，如果不存在或失效了则创建新的token 如果存在则使用旧的
		SysUserTokenEntity tokenEntity = sysUserTokenService.queryByUserId(user.getId());
		//token失效
        if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
        	isNewLogin = true;
        	RedisUtil.create().removeToken(user.getId());
        }else {
        	token = tokenEntity.getToken();
        	RedisUtil.create().setUserToken(user.getId(),tokenEntity);
        }
        if(isNewLogin) {
        	//生成token，并保存到数据库
    		R r = sysUserTokenService.createToken(user.getId(),useragent,0,null);
    		// 保存用户token到cookie
    		token = (String)r.get("token");
        }
		CookieTool.addCookie(response, "token",token, KstHosConstant.COOKIE_TOKEN_DAYS);
		AuthInfoVo vo = createAuthInfoVo(uuid,token, user);
		if(StringUtil.isNotBlank(realName) && realName.length() > 21) {
			realName = realName.substring(0, 19);
		}
		LogUtil.info(logger, 
				new LogBody(vo).set("openid", openid)
				.set("nickName", realName)
				.set("clientId", clientId)
				.set("configKey", configKey)
				.set("token", token)
				);
		return vo;
    }
    /**
     * 后台单点登录
     * @Description: 
     * @param uuid
     * @param orgId
     * @param userName
     * @param password
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected AuthInfoVo ssoBackstageLogin(String uuid,String token,long exptime,SysUserEntity user,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
    	SysUserEntity userLocal = sysUserService.queryByUserName(user.getUsername());
		//账号不存在 则新增用户
		if(userLocal == null) {
			userLocal = new SysUserEntity();
			userLocal.setUsername(user.getUsername());
//			user.setPassword(password);
			userLocal.setOperatorId(user.getOperatorId());
			userLocal.setOperatorName(user.getOperatorName());
			userLocal.setStatus(1);
			userLocal.setOrgId(user.getOrgId());
			userLocal.setOrgName(user.getOrgName());
			userLocal.setChannelId(user.getChannelId());
			userLocal.setChannelName(user.getChannelName());
			userLocal.setClientId(user.getClientId());
			sysUserService.save(userLocal);
		}else {
			SysUserEntity u = new SysUserEntity();
			u.setClientId(user.getClientId());
			u.setChannelId(user.getChannelId());
			u.setChannelName(user.getChannelName());
			sysUserService.updateUserClientIdAndConfigKey(user.getId(), u);
			userLocal.setClientId(user.getClientId());
			userLocal.setChannelId(user.getChannelId());
			userLocal.setChannelName(user.getChannelName());
		}
		String useragent = request.getHeader("user-agent");
		if(null != useragent && useragent.length() > 200){
			useragent = useragent.substring(0,199);
		}
		//判断token是否存在，如果不存在或失效了则创建新的token 如果存在则使用旧的
		SysUserTokenEntity tokenEntity = sysUserTokenService.queryByToken(token);
		//token失效
        if(tokenEntity == null){
        	RedisUtil.create().removeToken(userLocal.getId());
        	//生成token，并保存到数据库
    		R r = sysUserTokenService.createToken(userLocal.getId(),useragent,exptime,token);
    		// 保存用户token到cookie
    		token = (String)r.get("token");
        }else {
        	token = tokenEntity.getToken();
        	RedisUtil.create().setUserToken(user.getId(),tokenEntity);
        }
		AuthInfoVo vo = createAuthInfoVo(uuid,token, user);
		return vo;
    }
    

    
    
    /**
     * 后台登录 医院端 需要跳转到云平台 做登录调用 先模拟登录接口进行登录鉴权。
     * 然后再跳转到指定页面传入token
     * @Description: 
     * @param uuid
     * @param orgId
     * @param userName
     * @param password
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected void backstageSSOLogin(String uuid,String mainUrl,String toUrl,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
    	SysUserEntity user = getUser();
		String token = getRequestToken(request);
		SysUserTokenEntity tokenEntity = sysUserTokenService.queryByToken(token);
		Date ex = tokenEntity.getExpireTime();
		//当前时间
		Date now = new Date();
    	long exptime = (ex.getTime() - now.getTime())/1000;
    	JSONObject json = new JSONObject();
    	json.put("token", token);
    	json.put("exptime", exptime);
    	json.put("uuid", uuid);
    	json.put("toUrl", toUrl);
    	user.setPassword(null);
    	json.put("user", JSONObject.toJSON(user));
    	String reqParam = DesUtil.encrypt(json.toJSONString(), "UTF-8");
    	request.setAttribute("reqParam", reqParam);
    	SoapResponseVo vo = HttpRequestBus.create(mainUrl+"/sys/hosLogin.do", RequestType.post)
    			.setHeaderHttpParam("token", token).addHttpParam("reqParam", reqParam).send();
    	if(vo.getCode() == 200) {
    		response.sendRedirect(mainUrl+"/sys/ssoLogin.do?token="+token+"&toUrl="+toUrl);
    	}else {
    		response.sendRedirect("/pageError.html?errorcode=-50000&msg=Not Connection WeChat or Ali msg ="+ URLEncoder.encode(vo.getResult(), "utf-8"));
    	}
    }
    
    
    /**
     * 后台登录
     * @Description: 
     * @param uuid
     * @param orgId
     * @param userName
     * @param password
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected AuthInfoVo backstageLogin(String uuid,String userName,String password,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
    	SysUserEntity user = sysUserService.queryByUserName(userName);
		//账号不存在或密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
			throw new RRException(RetCode.Login.NotUserOrPassErr);
		}
		String useragent = request.getHeader("user-agent");
		if(null != useragent && useragent.length() > 200){
			useragent = useragent.substring(0,199);
		}
		//判断token是否存在，如果不存在或失效了则创建新的token 如果存在则使用旧的
		SysUserTokenEntity tokenEntity = sysUserTokenService.queryByUserId(user.getId());
		
		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getId(),useragent,0,null);
		// 保存用户token到cookie
		String token = (String)r.get("token");
    	RedisUtil.create().setUserToken(user.getId(), tokenEntity);
		CookieTool.addCookie(response, "token",token, KstHosConstant.COOKIE_TOKEN_DAYS);
		AuthInfoVo vo = createAuthInfoVo(uuid,token, user);
		return vo;
    }
    
    /**
     * 获取当前登录的用户信息
     * @return
     */
   	protected SysUserEntity getUser() {
   		return shiroService.getUser();
   	}
    
    /**
   	 * 鉴权登录信息获取
   	 * @param sysUser
   	 * @param token
   	 * @return
   	 */
   	protected static AuthInfoVo createAuthInfoVo(String uuid,String token,SysUserEntity sysUser) {
   		return KasiteConfig.createAuthInfoVo(uuid,token, sysUser);
   	}

   	/**
	 * 鉴权登录信息获取
	 * @param sysUser
	 * @param token
	 * @return
	 */
	protected AuthInfoVo createAuthInfoVo(HttpServletRequest request) {
		return createAuthInfoVo(IDSeed.next(), request);
	}
   	/**
	 * 鉴权登录信息获取
	 * @param sysUser
	 * @param token
	 * @return
	 */
	protected AuthInfoVo createAuthInfoVo(String uuid,HttpServletRequest request) {
		SysUserEntity sysUser = shiroService.getUser();
		if(null == sysUser) {
			throw new RRException("会话失效，请重新从首页进入。");
		}
		String token = getRequestToken(request);
		return createAuthInfoVo(uuid,token, sysUser);
	}
	
	 /**
     * 获取请求的token
     */
	protected static String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = httpRequest.getParameter("token");
        }
        if(StringUtil.isBlank(token)) {
        	token = (String) httpRequest.getSession().getAttribute("token");
        }
        return token;
    }
	
	protected InterfaceMessage createInterfaceMsg(String uuid,ApiModule api,String params,HttpServletRequest httpRequest) {
		return createInterfaceMsg(uuid,api, KstHosConstant.INTYPE, KstHosConstant.OUTTYPE, params, httpRequest);
	}
	protected InterfaceMessage createInterfaceMsg(ApiModule api,String params,AuthInfoVo vo) {
		return createInterfaceMsg(api, KstHosConstant.INTYPE, KstHosConstant.OUTTYPE, params, vo);
	}
	protected InterfaceMessage createInterfaceMsg(ApiModule api,int outType,int paramType,String params,AuthInfoVo vo) {
		return createInterfaceMsg(api.getName(), outType, paramType, params, vo);
	}
	protected InterfaceMessage createInterfaceMsg(String api,int outType,int paramType,String params,AuthInfoVo vo) {
		InterfaceMessage message = new InterfaceMessage();
		message.setApiName(api);
		message.setAuthInfo(vo.toString());
		message.setOutType(outType);
		message.setParam(params);
		message.setParamType(paramType);
		message.setSeq(Long.toString(System.currentTimeMillis()));
		message.setClientIp(com.kasite.core.common.log.NetworkUtil.getLocalIP());
   		return message;
	}
	
	protected InterfaceMessage createInterfaceMsg(String uuid,ApiModule api,int outType,int paramType,String params,HttpServletRequest httpRequest) {
		SysUserEntity user = shiroService.getUser();
   		String token = getRequestToken(httpRequest);
   		if(null == user) {
   			throw new RRException("鉴权不通过，请重新登录");
   		}
		AuthInfoVo vo = createAuthInfoVo(uuid,token,user);
   		return createInterfaceMsg(api, outType, paramType, params, vo);
   	}
	protected InterfaceMessage createInterfaceMsg(String uuid,String api,int outType,int paramType,String params,HttpServletRequest httpRequest) {
		SysUserEntity user = shiroService.getUser();
   		String token = getRequestToken(httpRequest);
   		if(null == user) {
   			throw new RRException("鉴权不通过，请重新登录");
   		}
		AuthInfoVo vo = createAuthInfoVo(uuid,token,user);
   		return createInterfaceMsg(api, outType, paramType, params, vo);
   	}
	protected InterfaceMessage createInterfaceMsg(String api,String params,HttpServletRequest httpRequest) {
		SysUserEntity user = shiroService.getUser();
   		String token = getRequestToken(httpRequest);
   		if(null == user) {
   			throw new RRException("鉴权不通过，请重新登录");
   		}
		AuthInfoVo vo = createAuthInfoVo(IDSeed.next(),token,user);
   		return createInterfaceMsg(api, 0, 0, params, vo);
   	}
	protected InterfaceMessage createInterfaceMsgByParam(String api,HttpServletRequest request) {
		SysUserEntity user = shiroService.getUser();
   		String token = getRequestToken(request);
   		if(null == user) {
   			throw new RRException("鉴权不通过，请重新登录");
   		}
		AuthInfoVo vo = createAuthInfoVo(IDSeed.next(),token,user);
		Map<String,String[]> paraMap =request.getParameterMap();
		JSONObject json = new JSONObject();
		JSONObject reqJson = new JSONObject();
		JSONObject dataJson = new JSONObject();
		if(null != paraMap && paraMap.size() > 0) {
			if(paraMap!=null && !paraMap.isEmpty()){
				Set<String> keySet = paraMap.keySet();
				for(String key:keySet){
					Object value = paraMap.get(key);
					if(null != value) {
						dataJson.put(key, value);
					}
				}
			}
		}
		
		JSONObject page =null;
		String limit = request.getParameter("limit");
		String pageIndex = request.getParameter("page");
		if(StringUtil.isNotBlank(limit) && StringUtil.isNotBlank(pageIndex)) {
			page = new JSONObject();
			page.put(IConstant.PINDEX, Integer.parseInt(pageIndex)-1);
			page.put(IConstant.PSIZE, limit);
		}
		if(null != page) {
			dataJson.put(IConstant.PAGE, page);
		}
		reqJson.put(IConstant.DATA, dataJson);
		json.put(IConstant.REQ, reqJson);
   		return createInterfaceMsg(api, 0, 0, json.toJSONString(), vo);
   	}
	/**
	 * 获取客户端IP
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    } 
	    return ip; 
	  }
	  // 字符串读取
    // 方法一
    public static String ReadAsChars(HttpServletRequest request)
    {
 
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
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
	
	
	  /** 支付宝oauth2.0 地址 **/
    private static final String ALIPAYSERVICEENVCONSTANTS_AUTH_URL     =	   "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";
	protected void gotoAliOauth(String clientId, String configKey,String redirectUri,HttpServletRequest request,HttpServletResponse response) {
		try {
			KasiteConfig.print(redirectUri);
			redirectUri = URLEncoder.encode(redirectUri, "utf-8");
			StringBuffer url = new StringBuffer();
			url.append(ALIPAYSERVICEENVCONSTANTS_AUTH_URL).append("?app_id=").append(KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId,configKey));
			// 关于scope的说明：
			// auth_base：以auth_base为scope发起的网页授权，是用来获取进入页面的用户的userId的，并且是静默授权并自动跳转到回调页的。用户感知的就是直接进入了回调页（通常是业务页面）。
			// auth_user：以auth_user为scope发起的网页授权，是用来获取用户的基本信息的（比如头像、昵称等）。但这种授权需要用户手动同意，用户同意后，就可在授权后获取到该用户的基本信息。
			String scope = "auth_user";
			url.append("&scope=").append(scope);
			url.append("&redirect_uri=").append(redirectUri);
			KasiteConfig.print("--url-->" + url);
			// 客户端跳转
			response.sendRedirect(url.toString());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(logger, e);
		}
	}
	  /** 微信oauth2.0 地址 **/
	public static final String WeiXinConstant_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize"; 
	protected void gotoWeChatOauth(String clientId, String configKey,String scope,String redirectUri,HttpServletRequest request, HttpServletResponse response) {
		try {
			redirectUri = URLEncoder.encode(redirectUri, "utf-8");
			StringBuffer url = new StringBuffer();
			String appId = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, configKey);
			if(StringUtil.isBlank(appId)) {
				LogUtil.info(logger, "获取微信配置appId为空： configKey = "+configKey);//("");
			}
			url.append(WeiXinConstant_CODE_URL).append("?appid=").append(appId);
			// scope应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
			// snsapi_userinfo
			// （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
			// 如果使用snsapi_userinfo网页版每次都会弹出授权页面...
			if(null == scope) {
				scope = "snsapi_userinfo";
			}
			url.append("&redirect_uri=").append(redirectUri)
					.append("&response_type=code&scope=" + scope + "&state=1#wechat_redirect");
			KasiteConfig.print("--url-->" + url);
			// request.getRequestDispatcher(url.toString());//服务器跳转
			// 客户端跳转
			response.sendRedirect(url.toString());
			return;
		} catch (Exception e) {
			LogUtil.error(logger, e);
			e.printStackTrace();
		}
	}
	/**
	 * 获取异常页面
	 * @param errorcode
	 * @param errormsg
	 * @return
	 * @throws Exception
	 */
	protected String getErrorUrl(int errorcode,String errormsg) throws Exception {
		return MessageFormat.format(ERRORPAGE, errorcode,URLEncoder.encode(errormsg, "utf-8"));
	}
	/**
	 * 重定向到异常页面
	 * @param errorcode
	 * @param errormsg
	 * @param response
	 * @throws Exception 
	 */
	protected void sendRedirectToErrorPage(int errorcode,String errormsg,HttpServletResponse response) throws Exception {
		String errorPage = getErrorUrl(errorcode, errormsg);
		String homeUrl = KasiteConfig.getKasiteHosWebAppUrl();
		LogUtil.info(logger, homeUrl+errorPage);
		response.sendRedirect(homeUrl+errorPage);
	}
	protected void sendRedirect(HttpServletResponse response,String toUrl) throws IOException {
		LogUtil.info(logger, toUrl);
		response.sendRedirect(toUrl);
	}
	
	
	/**
	 * 判断是否微信渠道，如果返回为null说明即不是微信也不是支付宝
	 * @param request
	 * @return
	 */
	protected Boolean isWeChat(HttpServletRequest request) {
		// 判断是微信还是支付宝  通过配置获取 configKey
    	String useragent = request.getHeader("User-Agent").toLowerCase();
    	Boolean isWeChat = null;
    	if(null != useragent && useragent.indexOf("alipay") >= 0) {//支付宝
    		isWeChat = false;
    	}else if(null != useragent && useragent.indexOf("micromessenger") >= 0) {//微信
    		isWeChat = true;
    	}
    	return isWeChat;
	}
	
	/**
	 * 解析异常信息
	 * @param ex
	 * @return
	 */
	protected R parseException(Exception ex) {
		return LogUtil.parseException(ex);
	}
	
	 /**
     * 确认api存在
     * @param module
     * @param methodName
     * @return
     */
	protected String getApi(String module, String methodName) {
		String apiFullName = ApiList.me().getApi(module, methodName);
		if(null == apiFullName) {
			throw new RRException("Api 未定义，请联系管理员确认。module="+module+"||methodName="+methodName);
		}
		return apiFullName;
	}
	/**
	 * 前端页面支付通知回调支付成功（微信／支付宝／银联。。。）
	 * @throws Exception 
	 */
	public void payCallBack(String clientId,String configKey,String orderId,String toUrl,
			AuthInfoVo authInfo,
			CommonResp<RespMap> resp,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		String fullPath =  KasiteConfig.getKasiteHosWebAppUrl();
		authInfo.setConfigKey(configKey);		
		String errorMsg = "未知异常";
		if(resp!=null && KstHosConstant.SUCCESSCODE.equals(resp.getCode())){
			if( !StringUtil.isEmpty(toUrl)){
				if(!toUrl.startsWith("http")) {
					toUrl = fullPath + toUrl;
				}
				
			}else{
				//判断是否有个性化回调地址跳转 如果没有就走默认收银台如果有的话跳转到指定页面
				String diyToUrl = KasiteConfig.getClientConfig(ClientConfigEnum.paySuccessToUrl, clientId);
				if(StringUtil.isNotBlank(diyToUrl)) {
					//如果带上了全路径 则不是在我们系统内部地址 如果是系统内部相对路径则直接 加上系统域名。
					if(!diyToUrl.startsWith("http")) {
						diyToUrl = fullPath + diyToUrl;
					}
					toUrl = diyToUrl;
				}else {
					//页面不存在先放着
					toUrl = fullPath + "/business/pay/paySuccess.html";
				}
			}

			if(StringUtil.isNotBlank(orderId) && StringUtil.isNotBlank(toUrl) && toUrl.indexOf("orderId=")<0) {
				if(toUrl.indexOf("?") > 0) {
					toUrl += "&";
				}else {
					toUrl += "?";
				}
				toUrl +="orderId="+orderId;
			}
			if(StringUtil.isNotBlank(clientId) && StringUtil.isNotBlank(toUrl) && toUrl.indexOf("clientId=")<0) {
				if(toUrl.indexOf("?") > 0) {
					toUrl += "&";
				}else {
					toUrl += "?";
				}
				toUrl +="clientId="+clientId;
			}
			toUrl = URLDecoder.decode(toUrl,"UTF-8");
			sendRedirect(response,toUrl);
			return;
		}else{
			errorMsg = null != resp ? resp.getMessage():errorMsg;
			toUrl = fullPath+"/business/pageError.html";
		}
		sendRedirectToErrorPage(-10000, errorMsg, response);
		LogUtil.info(logger, toUrl,authInfo);
	}
	/**
	 * 判断请求头 header  请求类型： Content-Type = application/json 
	 * 则从body中获取 json 字符串报文，如果无此报文则返回 null
	 * 如果无 application/json 也直接返回 null 
	 * @param request
	 * @return
	 */
	protected JSONObject readRequestBodyJsonCaseHeader(HttpServletRequest request) {
		String type = request.getHeader("Content-Type");
		if(null != type && type.toLowerCase().startsWith(HttpRequest.Content_Type_JSON)) {
			String reqBody = readAsChars(request);
			if(StringUtil.isNotBlank(reqBody)) {
				return JSONObject.parseObject(reqBody);
			}
		}
		return null;
	}
	
	// 从requestbody 中读取字符串读取
    public static String readAsChars(HttpServletRequest request)
    {
 
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
