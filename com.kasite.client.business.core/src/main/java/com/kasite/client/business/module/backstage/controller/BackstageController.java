package com.kasite.client.business.module.backstage.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.sys.req.ReqQueryMenuTree;
import com.kasite.core.common.sys.resp.RespQueryMenuTree;
import com.kasite.core.common.sys.service.RedisUtil;
import com.kasite.core.common.sys.service.ShiroService;
import com.kasite.core.common.sys.service.SysMenuService;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;
import com.kasite.core.common.util.CookieTool;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.httpclient.http.HttpRequest;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.IPorvingCodeService;
import com.kasite.core.serviceinterface.module.basic.req.ReqCheckPorvingCode;
import com.kasite.core.serviceinterface.module.basic.req.ReqGetProvingCode;
import com.kasite.core.serviceinterface.module.basic.resp.RespGetprovingCode;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: BackstageController
 * @author: lcz
 * @date: 2018年8月23日 上午10:57:16
 */
@RestController
public class BackstageController extends AbstractController{
	private static final String loginHtml = "/backstage/wechatLogin.do";
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;
	@Autowired
	private IBasicService basicService;
	@Autowired
	private IPorvingCodeService provingCodeService;
	
	@RequestMapping("/")
	void goLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		sendRedirect(response, loginHtml);
	}
	
	@PostMapping("/backstage/login.do")
	@SysLog("管理后台登录")
	R login(String params,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uuid = IDSeed.next();
		JSONObject dataJs = null;
		String requestType = request.getHeader("Content-Type");
		if(requestType!=null && requestType.contains(HttpRequest.Content_Type_JSON)) {
			String body = readAsChars(request);
			if(StringUtil.isNotBlank(body)) {
				dataJs = JSONObject.parseObject(body);
			}
		}else {
			JSONObject paramJs = JSONObject.parseObject(params);
			JSONObject reqJs = paramJs.getJSONObject("Req");
			dataJs = reqJs.getJSONObject("Data");
		}
//		JSONObject paramJs = JSONObject.parseObject(params);
//		JSONObject reqJs = paramJs.getJSONObject("Req");
//		JSONObject dataJs = reqJs.getJSONObject("Data");
//		String hosId = dataJs.getString("HosId");
		String userName = dataJs.getString("UserName");
		String password = dataJs.getString("PassWord");
		AuthInfoVo vo = super.backstageLogin(uuid,userName,password, request, response);
		return R.ok().put("token", vo.getSessionKey());
	}
	
	@RequestMapping("/backstage/logout.do")
	@SysLog("管理后台登出")
	void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//清除cookie中的token
		CookieTool.delCookie(response, request, "token");
		//重定向到登录页面
		sendRedirect(response, loginHtml);
	}
	
	@RequestMapping("/backstage/logoutPayment.do")
	@SysLog("智付管理后台登出")
	void logoutPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//重定向到登录页面
		sendRedirect(response, loginHtml);
	}
	
	@PostMapping("/backstage/getMenuList.do")
	@SysLog(value="查询菜单列表",isSaveResult=false)
	R getMenuList(String params,HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject dataJs = null;
		String requestType = request.getHeader("Content-Type");
		if(requestType!=null && requestType.contains(HttpRequest.Content_Type_JSON)) {
			String body = readAsChars(request);
			if(StringUtil.isNotBlank(body)) {
				dataJs = JSONObject.parseObject(body);
			}
		}else {
			JSONObject paramJs = JSONObject.parseObject(params);
			JSONObject reqJs = paramJs.getJSONObject("Req");
			dataJs = reqJs.getJSONObject("Data");
		}
		
//		JSONObject paramJs = JSONObject.parseObject(params);
//		JSONObject reqJs = paramJs.getJSONObject("Req");
//		JSONObject dataJs = reqJs.getJSONObject("Data");
		String userName = dataJs.getString("UserName");
		
		//查询用户信息
		SysUserEntity user = sysUserService.queryByUserName(userName);
		
		//查询用户是否管理员
		boolean isAdmin = sysUserService.isAdmin(user.getId());
		
		//查询用户菜单
		InterfaceMessage msg = createInterfaceMsg("queryMenuTree", params, request);
		ReqQueryMenuTree menuReq = new ReqQueryMenuTree(msg,user.getId(),isAdmin);
		CommonResp<RespQueryMenuTree> menuResp = sysMenuService.queryMenuTree(new CommonReq<ReqQueryMenuTree>(menuReq));
		
		//查询用户所有权限信息
		JSONObject permissions = shiroService.getUserPermsForJSONObject(user.getId(),isAdmin);
		
		return R.ok().put("menuList", menuResp.getData()).put("permissions", permissions);
	}
	@RequestMapping("/backstage/ssoLogin.do")
	@SysLog("云平台登录")
	public void ssoLogin(String mainUrl,String toUrl,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uuid = IDSeed.next();
		super.backstageSSOLogin(uuid, mainUrl, toUrl, request, response);//(uuid,toUrl,request, response);
	}
	

	/**
	 * 获取登录页面 含二维码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/backstage/wechatLogin.do")
	public void login(
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = "https://open.weixin.qq.com/connect/qrconnect?appid=wxc89392b731d6f359&redirect_uri=https://wechatcenter.kasitesoft.com/login.html&response_type=code&scope=snsapi_login&state=STATE";
		String responseTxt = HttpRequestBus.create(url, RequestType.get).send().getResult();
		String strS = responseTxt.substring(responseTxt.indexOf("uuid=")+5, responseTxt.length());
		String uuid = (strS.substring(0, strS.indexOf("+")-1));

		String loginHtml = LOGINHTML.replaceAll("_UUID", uuid);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Content-Type","javascript");
		response.setHeader("Cache-Control", "no-cache"); 
		response.setHeader("Content-Type","text/html; charset=utf-8");
		response.getOutputStream().write(loginHtml.getBytes("UTF-8"));
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	}
	
	
	/**
	 * 获取登录页面 含二维码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/backstage/{uuid}/connectQrcode.do")
	public void connectQrcode(
			@PathVariable("uuid") String uuid,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		URL imageUrl =new URL("https://open.weixin.qq.com/connect/qrcode/"+uuid);
	    InputStream inStream = imageUrl.openConnection().getInputStream();//通过输入流获取图片数据
	    byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
	    response.setHeader("Content-Type","application/octet-stream");
	    response.setHeader("Content-Disposition","attachment;filename=gaga.jpg");
	    response.getOutputStream().write(btImg);
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
		
		
	}
	
	/**
	 * 从输入流中获取数据
	 * @param inStream 输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception{
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int len = 0;
	    while( (len=inStream.read(buffer)) != -1 ){
	        outStream.write(buffer, 0, len);
	    }
	    inStream.close();
	    return outStream.toByteArray();
	}
	
	
//	public static void main(String[] args) {
//	    String responseTxt = "window.wx_errcode=402;window.wx_code='';";
//	    //window.wx_errcode=405;window.wx_code='01102ohl230CDE0Fijkl2j7zhl202ohf';
//	    //window.wx_errcode=404;window.wx_code='';
//	    //window.wx_errcode=408;window.wx_code='';
//	    String[] strs = responseTxt.split(";");
//	    String statusCode = strs[0].split("=")[1];
//	    String code = strs[1].split("=")[1];
//	    System.out.println(statusCode);
//	    System.out.println(code);
//	}
	/**
	 * 发送验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/backstage/bind.do")
	@SysLog(value="绑定用户", isSaveResult=false)
	public R bind(String reqParam,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("backstage.bind", reqParam, request);
		JSONObject obj = JSONObject.parseObject(msg.getParam());
		JSONObject data = obj.getJSONObject("Req").getJSONObject("Data");
		String openId = data.getString("OpenId");
		String realName = data.getString("RealName");
		String pcId = data.getString("PCId");
		String provingCode = data.getString("ProvingCode");
		String mobile = data.getString("Mobile");
		CommonReq<ReqCheckPorvingCode> reqComm = new CommonReq<ReqCheckPorvingCode>(new ReqCheckPorvingCode(msg, pcId, mobile, provingCode));
		CommonResp<RespMap> resp = provingCodeService.checkPorvingCode(reqComm);
		resp.getDataCaseRetCode();
		SysUserEntity user = sysUserService.queryByUserName(openId);
		//账号不存在或密码错误
		SysUserEntity updateUser = new SysUserEntity();
		updateUser.setUsername(openId);
		updateUser.setRealName(realName);
		updateUser.setMobile(mobile);
		updateUser.setOrgName(KasiteConfig.getOrgName());
		updateUser.setPassword("1");
		updateUser.setSalt("1");
		updateUser.setId(user.getId());
		sysUserService.update(updateUser);
		return R.ok();
	}
	/**
	 * 发送验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/backstage/sendProvingCode.do")
	@SysLog(value="发送验证码", isSaveResult=false)
	public R sendProvingCode(String reqParam,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("sendProvingCode", reqParam, request);
		ReqGetProvingCode t = new ReqGetProvingCode(msg);
		CommonReq<ReqGetProvingCode> reqComm = new CommonReq<ReqGetProvingCode>(t);
		CommonResp<RespGetprovingCode> resp = basicService.getProvingCode(reqComm);
		return R.ok(resp.toJSONResult());
	}
	
	
	/**
	 * 获取登录页面 含二维码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/backstage/qrconnect.do")
	public void qrconnect(String uuid,String last ,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = "https://long.open.weixin.qq.com/connect/l/qrconnect?uuid="+uuid+"&_="+System.currentTimeMillis();
		String responseTxt = HttpRequestBus.create(url, RequestType.get).send().getResult();
		//返回状态 = 405 并且有code的时候 去中心取用户的openId==》返回对应的 openid，手机号，姓名
		//没有手机号的时候 通过初始化进行添加 
	    String[] strs = responseTxt.split(";");
	    String statusCode = strs[0].split("=")[1];
	    String code = strs[1].split("=")[1];
		if("405".equals(statusCode)) {
			//获取微信openId 
			String authCode = code.substring(1, code.length()-1);
			url = "https://wechatcenter.kasitesoft.com/weixin/100123/WXKASIETOPEN/userinfo.do?authCode="+authCode;
			String userInfo = HttpRequestBus.create(url, RequestType.post)
					.addHttpParam("orgCode", KasiteConfig.getOrgCode())
					.addHttpParam("appId", KasiteConfig.getAppId())
					.addHttpParam("orgName", KasiteConfig.getOrgName())
					.send().getResult();
			System.out.println(userInfo);
			//{"RespCode":10000,"result":{"openid":"oxFLS0gAFXKfoB89ptr0LIgFbJp4","nickname":"半生沙","sex":1,"language":"zh_CN","city":"福州","province":"福建","country":"中国","headimgurl":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqp2Vu0lVcnCxUBpClUtpYGpMvN4aH7UEv4IBmib3xqLM5hI6W8rBmhicZqo93S3Hict1Bymn78IdBxg/132","privilege":[],"unionid":"oVhRq1F9c1tKOCIXHw8z2VhjEWvc"},"code":0}
			JSONObject json = JSONObject.parseObject(userInfo);
			Integer respCode = json.getInteger("RespCode");
			if(KstHosConstant.SUCCESS_CODE.equals(respCode)) {
				JSONObject userJson = json.getJSONObject("result");
//				String openId = userJson.getString("openid");
				//开放平台所有用户openId都转为 unionid
				String openId = userJson.getString("unionid");
//				String nickname = userJson.getString("nickname");
				//注册用户 window.wx_errcode=405;window.wx_code='01102ohl230CDE0Fijkl2j7zhl202ohf';
//				responseTxt= responseTxt+"window.token='123123'"; 
				//获取用户是否已经存在，如果不存在则提示绑定用户手机号信息 如果存在则直接登录成功。
				SysUserEntity user = sysUserService.queryByUserName(openId);
		    	if(null == user) {
					String salt = "NoBin"+System.currentTimeMillis();
		    		user = new SysUserEntity();
		    		user.setUsername(openId);
		    		user.setChannelName("管理系统");
		    		user.setChannelId("systemmanager");
		    		user.setClientId("systemmanager");
		    		user.setCreateTime(DateOper.getNowDateTime());
		    		user.setOperatorId(openId);
		    		user.setOperatorName("微信扫码登录");
		    		user.setOrgId(KasiteConfig.getOrgCode());
		    		user.setStatus(1);
		    		String pwd = com.yihu.hos.util.MD5Util.getMD5Str(openId).toUpperCase();
		    		user.setPassword(new Sha256Hash(pwd, salt).toHex());
		    		user.setSalt(salt);
		    		user.setOrgName(KasiteConfig.getOrgName());
		    		user.setChannelId("systemmanager");
		    		sysUserService.save(user);
		    	}
		    	String salt = user.getSalt();
		    	if(salt.startsWith("NoBin")) {
		    		//无密码的用户需要进入绑定页面 初次登录需要 输入 手机号，姓名，密码
					responseTxt= responseTxt+"window.url = '/module/backstage/login/wechat/bind.html?openid="+openId+"';";
		    	}else {
		    		responseTxt = responseTxt+"window.login=true;";
		    	}
		    	//判断token是否存在，如果不存在或失效了则创建新的token 如果存在则使用旧的
				SysUserTokenEntity tokenEntity = sysUserTokenService.queryByUserId(user.getId());
				//生成token，并保存到数据库
				R r = sysUserTokenService.createToken(user.getId(),"",0,null);
				// 保存用户token到cookie
				String token = (String)r.get("token");
		    	RedisUtil.create().setUserToken(user.getId(), tokenEntity);
				CookieTool.addCookie(response, "token",token, KstHosConstant.COOKIE_TOKEN_DAYS);
//				AuthInfoVo vo = createAuthInfoVo(uuid,token, user);
			}
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Content-Type","javascript");
		response.setHeader("Cache-Control", "no-cache");  
	    response.getOutputStream().write(responseTxt.getBytes());
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	}
	/*
第一步：请求CODE
第三方使用网站应用授权登录前请注意已获取相应网页授权作用域（scope=snsapi_login），则可以通过在PC端打开以下链接：
https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
若提示“该链接无法访问”，请检查参数是否填写错误，如redirect_uri的域名与审核时填写的授权域名不一致或scope不为snsapi_login。
	 */
	private final static String LOGINHTML = " <!DOCTYPE html>" + 
			"<html>" + 
			"	<head>" + 
			"		<title>微信登录</title>" + 
			"		<meta charset=\"utf-8\">		"+
			"		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" + 
			"		<link rel=\"stylesheet\" href=\"/module/backstage/login/wechat/css/impowerApp.css\">" + 
			"		<script src=\"/module/backstage/commons/js/jquery-1.8.3.min.js\"></script>" + 
			"		<script src=\"/module/backstage/commons/js/common.js\"></script>" + 
			"	</head>" + 
			"	<body>" + 
			"		<div class=\"main impowerBox\">" + 
			"			<div class=\"loginPanel normalPanel\">" + 
			"				<div class=\"title\">微信登录</div>" + 
			"				<div class=\"waiting panelContent\">" + 
			"					<div class=\"wrp_code\"><img class=\"qrcode lightBorder\" src=\"/backstage/_UUID/connectQrcode.do\" /></div>" + 
			"					<div class=\"info\">" + 
			"						<div class=\"status status_browser js_status\" id=\"wx_default_tip\">" + 
			"			                <p>请使用微信扫描二维码登录</p>" + 
			"                            <p>“卡思特智付中心”</p>" + 
			"			            </div>" + 
			"			            <div class=\"status status_succ js_status\" style=\"display:none\" id=\"wx_after_scan\">" + 
			"			                <i class=\"status_icon icon38_msg succ\"></i>" + 
			"			                <div class=\"status_txt\">" + 
			"			                    <h4>扫描成功</h4>" + 
			"			                    <p>请在微信中点击确认即可登录</p>" + 
			"			                </div>" + 
			"			            </div>" + 
			"			            <div class=\"status status_fail js_status\" style=\"display:none\" id=\"wx_after_cancel\">" + 
			"			                <i class=\"status_icon icon38_msg warn\"></i>" + 
			"			                <div class=\"status_txt\">" + 
			"			                    <h4>您已取消此次登录</h4>" + 
			"			                    <p>您可再次扫描登录，或关闭窗口</p>" + 
			"			                </div>" + 
			"			            </div>" + 
			"			        </div>" + 
			"				</div>" + 
			"			</div>" + 
			"		</div>" + 
			"        <script>" + 
			"function AQ_SECAPI_ESCAPE(a,b){for(var c=new Array,d=0;d<a.length;d++)if(\"&\"==a.charAt(d)){var e=[3,4,5,9],f=0;for(var g in e){var h=e[g];if(d+h<=a.length){var i=a.substr(d,h).toLowerCase();if(b[i]){c.push(b[i]),d=d+h-1,f=1;break}}}0==f&&c.push(a.charAt(d))}else c.push(a.charAt(d));return c.join(\"\")}function AQ_SECAPI_CheckXss(){for(var a=new Object,b=\"'\\\"<>`script:daex/hml;bs64,\",c=0;c<b.length;c++){for(var d=b.charAt(c),e=d.charCodeAt(),f=e,g=e.toString(16),h=0;h<7-e.toString().length;h++)f=\"0\"+f;a[\"&#\"+e+\";\"]=d,a[\"&#\"+f]=d,a[\"&#x\"+g]=d}a[\"&lt\"]=\"<\",a[\"&gt\"]=\">\",a[\"&quot\"]='\"';var i=location.href,j=document.referrer;i=decodeURIComponent(AQ_SECAPI_ESCAPE(i,a)),j=decodeURIComponent(AQ_SECAPI_ESCAPE(j,a));var k=new RegExp(\"['\\\"<>`]|script:|data:text/html;base64,\");if(k.test(i)||k.test(j)){var l=\"1.3\",m=\"http://zyjc.sec.qq.com/dom\",n=new Image;n.src=m+\"?v=\"+l+\"&u=\"+encodeURIComponent(i)+\"&r=\"+encodeURIComponent(j),i=i.replace(/['\\\"<>`]|script:/gi,\"\"),i=i.replace(/data:text\\/html;base64,/gi,\"data:text/plain;base64,\"),location.href=i}}AQ_SECAPI_CheckXss();" + 
			"</script>" + 
			"        <script>" + 
			"!function(){function a(d){jQuery.ajax({type:\"GET\",url:\"/backstage/qrconnect.do?uuid=_UUID\"+(d?\"&last=\"+d:\"\"),dataType:\"script\",cache:!1,timeout:6e4,success:function(d,e,f){var g=window.wx_errcode;var url=window.url; var isLogin = window.login; if(isLogin){url = Commonjs.getLoginUrl();} switch(g){case 405:var h=url;h=h.replace(/&amp;/g,\"&\"),h+=(h.indexOf(\"?\")>-1?\"&\":\"?\")+\"code=\"+wx_code+\"&state=STATE\";var i=b(\"self_redirect\");if(c)if(\"true\"!==i&&\"false\"!==i)try{document.domain=\"qq.com\";var j=window.top.location.host.toLowerCase();j&&(window.location=h)}catch(k){window.top.location=h}else if(\"true\"===i)try{window.location=h}catch(k){window.top.location=h}else window.top.location=h;else window.location=h;break;case 404:jQuery(\".js_status\").hide(),jQuery(\"#wx_after_scan\").show(),setTimeout(a,100,g);break;case 403:jQuery(\".js_status\").hide(),jQuery(\"#wx_after_cancel\").show(),setTimeout(a,2e3,g);break;case 402:case 500:window.location.reload();break;case 408:setTimeout(a,2e3)}},error:function(b,c,d){var e=window.wx_errcode;408==e?setTimeout(a,5e3):setTimeout(a,5e3,e)}})}function b(a,b){b||(b=window.location.href),a=a.replace(/[\\[\\]]/g,\"\\\\$&\");var c=new RegExp(\"[?&]\"+a+\"(=([^&#]*)|&|#|$)\"),d=c.exec(b);return d?d[2]?decodeURIComponent(d[2].replace(/\\+/g,\" \")):\"\":null}var c=window.top!=window;if(c){var d=\"\";\"white\"!=d&&(document.body.style.color=\"#373737\")}else{document.getElementsByClassName||(document.getElementsByClassName=function(a){for(var b=[],c=new RegExp(\"(^| )\"+a+\"( |$)\"),d=document.getElementsByTagName(\"*\"),e=0,f=d.length;f>e;e++)c.test(d[e].className)&&b.push(d[e]);return b}),document.body.style.backgroundColor=\"#333333\",document.body.style.padding=\"50px\";for(var e=document.getElementsByClassName(\"status\"),f=0,g=e.length;g>f;++f){var h=e[f];h.className=h.className+\" normal\"}}var i=\"\";if(i){var j=document.createElement(\"link\");j.rel=\"stylesheet\",j.href=i.replace(new RegExp(\"javascript:\",\"gi\"),\"\"),document.getElementsByTagName(\"head\")[0].appendChild(j)}setTimeout(a,100)}();" + 
			"</script>" + 
			"	</body>" + 
			"</html>";
}
