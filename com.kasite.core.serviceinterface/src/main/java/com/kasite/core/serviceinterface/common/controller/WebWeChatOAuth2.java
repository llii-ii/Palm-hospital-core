//package com.kasite.core.serviceinterface.common.controller;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSONObject;
//import com.kasite.core.common.config.KasiteConfig;
//import com.kasite.core.common.constant.KstHosConstant;
//import com.kasite.core.httpclient.http.HttpRequestBus;
//import com.kasite.core.httpclient.http.RequestType;
//
///**
// * 微信网站Oauth2.0鉴权
// * @author daiyanshui
// *
//1. 第三方发起微信授权登录请求，微信用户允许授权第三方应用后，微信会拉起应用或重定向到第三方网站，并且带上授权临时票据code参数；
//2. 通过code参数加上AppID和AppSecret等，通过API换取access_token；
//3. 通过access_token进行接口调用，获取用户基本数据资源或帮助用户实现基本操作。
// */
//@RestController
//@RequestMapping("/wechatOauth2/") 
//public class WebWeChatOAuth2 {
//	
//
//	/**
//	 * 获取登录页面 含二维码
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception 
//	 */
//	@GetMapping("/login.do")
//	public void login(
//			HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String url = "https://open.weixin.qq.com/connect/qrconnect?appid=wxc89392b731d6f359&redirect_uri=https://wechatcenter.kasitesoft.com/login.html&response_type=code&scope=snsapi_login&state=STATE";
//		String responseTxt = HttpRequestBus.create(url, RequestType.get).send().getResult();
//		String strS = responseTxt.substring(responseTxt.indexOf("uuid=")+5, responseTxt.length());
//		String uuid = (strS.substring(0, strS.indexOf("+")-1));
//		String loginHtml = LOGINHTML.replaceAll("_UUID", uuid);
//		
//		response.setHeader("Content-Type","text/html; charset=utf-8");
//		response.getOutputStream().write(loginHtml.getBytes());
//	    response.getOutputStream().flush();
//	    response.getOutputStream().close();
//	}
//	
//	
//	/**
//	 * 获取登录页面 含二维码
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception 
//	 */
//	@GetMapping("{uuid}/connectQrcode.do")
//	public void connectQrcode(
//			@PathVariable("uuid") String uuid,
//			HttpServletRequest request, HttpServletResponse response) throws Exception {
//		URL imageUrl =new URL("https://open.weixin.qq.com/connect/qrcode/"+uuid);
//	    InputStream inStream = imageUrl.openConnection().getInputStream();//通过输入流获取图片数据
//	    byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
//	    response.setHeader("Content-Type","application/octet-stream");
//	    response.setHeader("Content-Disposition","attachment;filename=gaga.jpg");
//	    response.getOutputStream().write(btImg);
//	    response.getOutputStream().flush();
//	    response.getOutputStream().close();
//		
//		
//	}
//	
//	/**
//	 * 从输入流中获取数据
//	 * @param inStream 输入流
//	 * @return
//	 * @throws Exception
//	 */
//	public static byte[] readInputStream(InputStream inStream) throws Exception{
//	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//	    byte[] buffer = new byte[1024];
//	    int len = 0;
//	    while( (len=inStream.read(buffer)) != -1 ){
//	        outStream.write(buffer, 0, len);
//	    }
//	    inStream.close();
//	    return outStream.toByteArray();
//	}
//	
//	
////	public static void main(String[] args) {
////	    String responseTxt = "window.wx_errcode=402;window.wx_code='';";
////	    //window.wx_errcode=405;window.wx_code='01102ohl230CDE0Fijkl2j7zhl202ohf';
////	    //window.wx_errcode=404;window.wx_code='';
////	    //window.wx_errcode=408;window.wx_code='';
////	    String[] strs = responseTxt.split(";");
////	    String statusCode = strs[0].split("=")[1];
////	    String code = strs[1].split("=")[1];
////	    System.out.println(statusCode);
////	    System.out.println(code);
////	}
//	
//	/**
//	 * 获取登录页面 含二维码
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@GetMapping("qrconnect.do")
//	public void getExecute(String uuid,String last ,
//			HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String url = "https://long.open.weixin.qq.com/connect/l/qrconnect?uuid="+uuid+"&_="+System.currentTimeMillis();
//		String responseTxt = HttpRequestBus.create(url, RequestType.get).send().getResult();
//		//返回状态 = 405 并且有code的时候 去中心取用户的openId==》返回对应的 openid，手机号，姓名
//		//没有手机号的时候 通过初始化进行添加 
//	    String[] strs = responseTxt.split(";");
//	    String statusCode = strs[0].split("=")[1];
//	    String code = strs[1].split("=")[1];
//		if("405".equals(statusCode)) {
//			//获取微信openId 
//			String authCode = code.substring(1, code.length()-1);
//			url = "https://wechatcenter.kasitesoft.com/weixin/100123/WXKASIETOPEN/userinfo.do?authCode="+authCode;
//			String userInfo = HttpRequestBus.create(url, RequestType.post)
//					.addHttpParam("orgCode", KasiteConfig.getOrgCode())
//					.addHttpParam("appId", KasiteConfig.getAppId())
//					.addHttpParam("orgName", KasiteConfig.getOrgName())
//					.send().getResult();
//			System.out.println(userInfo);
//			//{"RespCode":10000,"result":{"openid":"oxFLS0gAFXKfoB89ptr0LIgFbJp4","nickname":"半生沙","sex":1,"language":"zh_CN","city":"福州","province":"福建","country":"中国","headimgurl":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqp2Vu0lVcnCxUBpClUtpYGpMvN4aH7UEv4IBmib3xqLM5hI6W8rBmhicZqo93S3Hict1Bymn78IdBxg/132","privilege":[],"unionid":"oVhRq1F9c1tKOCIXHw8z2VhjEWvc"},"code":0}
//			JSONObject json = JSONObject.parseObject(userInfo);
//			Integer respCode = json.getInteger("RespCode");
//			if(KstHosConstant.SUCCESS_CODE.equals(respCode)) {
//				JSONObject userJson = json.getJSONObject("result");
//				String openId = userJson.getString("openid");
//				String unionid = userJson.getString("unionid");
//				String nickname = userJson.getString("nickname");
//				//注册用户 window.wx_errcode=405;window.wx_code='01102ohl230CDE0Fijkl2j7zhl202ohf';
//				responseTxt= responseTxt+"window.token='123123'";
//			}
//		}
//		response.setHeader("Content-Type","javascript");
//	    response.getOutputStream().write(responseTxt.getBytes());
//	    response.getOutputStream().flush();
//	    response.getOutputStream().close();
//	}
//	/*
//第一步：请求CODE
//第三方使用网站应用授权登录前请注意已获取相应网页授权作用域（scope=snsapi_login），则可以通过在PC端打开以下链接：
//https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
//若提示“该链接无法访问”，请检查参数是否填写错误，如redirect_uri的域名与审核时填写的授权域名不一致或scope不为snsapi_login。
//	 */
//	private final static String LOGINHTML = " <!DOCTYPE html>" + 
//			"<html>" + 
//			"	<head>" + 
//			"		<title>微信登录</title>" + 
//			"		<meta charset=\"utf-8\">		" + 
//			"		<link rel=\"stylesheet\" href=\"/module/login/wechat/common/css/impowerApp.css\">" + 
//			"		<script src=\"/module/login/wechat/common/js/jquery.min.js\"></script>" + 
//			"	</head>" + 
//			"	<body>" + 
//			"		<div class=\"main impowerBox\">" + 
//			"			<div class=\"loginPanel normalPanel\">" + 
//			"				<div class=\"title\">微信登录</div>" + 
//			"				<div class=\"waiting panelContent\">" + 
//			"					<div class=\"wrp_code\"><img class=\"qrcode lightBorder\" src=\"/wechatOauth2/_UUID/connectQrcode.do\" /></div>" + 
//			"					<div class=\"info\">" + 
//			"						<div class=\"status status_browser js_status\" id=\"wx_default_tip\">" + 
//			"			                <p>请使用微信扫描二维码登录</p>" + 
//			"                            <p>“卡思特智付中心”</p>" + 
//			"			            </div>" + 
//			"			            <div class=\"status status_succ js_status\" style=\"display:none\" id=\"wx_after_scan\">" + 
//			"			                <i class=\"status_icon icon38_msg succ\"></i>" + 
//			"			                <div class=\"status_txt\">" + 
//			"			                    <h4>扫描成功</h4>" + 
//			"			                    <p>请在微信中点击确认即可登录</p>" + 
//			"			                </div>" + 
//			"			            </div>" + 
//			"			            <div class=\"status status_fail js_status\" style=\"display:none\" id=\"wx_after_cancel\">" + 
//			"			                <i class=\"status_icon icon38_msg warn\"></i>" + 
//			"			                <div class=\"status_txt\">" + 
//			"			                    <h4>您已取消此次登录</h4>" + 
//			"			                    <p>您可再次扫描登录，或关闭窗口</p>" + 
//			"			                </div>" + 
//			"			            </div>" + 
//			"			        </div>" + 
//			"				</div>" + 
//			"			</div>" + 
//			"		</div>" + 
//			"        <script>" + 
//			"function AQ_SECAPI_ESCAPE(a,b){for(var c=new Array,d=0;d<a.length;d++)if(\"&\"==a.charAt(d)){var e=[3,4,5,9],f=0;for(var g in e){var h=e[g];if(d+h<=a.length){var i=a.substr(d,h).toLowerCase();if(b[i]){c.push(b[i]),d=d+h-1,f=1;break}}}0==f&&c.push(a.charAt(d))}else c.push(a.charAt(d));return c.join(\"\")}function AQ_SECAPI_CheckXss(){for(var a=new Object,b=\"'\\\"<>`script:daex/hml;bs64,\",c=0;c<b.length;c++){for(var d=b.charAt(c),e=d.charCodeAt(),f=e,g=e.toString(16),h=0;h<7-e.toString().length;h++)f=\"0\"+f;a[\"&#\"+e+\";\"]=d,a[\"&#\"+f]=d,a[\"&#x\"+g]=d}a[\"&lt\"]=\"<\",a[\"&gt\"]=\">\",a[\"&quot\"]='\"';var i=location.href,j=document.referrer;i=decodeURIComponent(AQ_SECAPI_ESCAPE(i,a)),j=decodeURIComponent(AQ_SECAPI_ESCAPE(j,a));var k=new RegExp(\"['\\\"<>`]|script:|data:text/html;base64,\");if(k.test(i)||k.test(j)){var l=\"1.3\",m=\"http://zyjc.sec.qq.com/dom\",n=new Image;n.src=m+\"?v=\"+l+\"&u=\"+encodeURIComponent(i)+\"&r=\"+encodeURIComponent(j),i=i.replace(/['\\\"<>`]|script:/gi,\"\"),i=i.replace(/data:text\\/html;base64,/gi,\"data:text/plain;base64,\"),location.href=i}}AQ_SECAPI_CheckXss();" + 
//			"</script>" + 
//			"        <script>" + 
//			"!function(){function a(d){jQuery.ajax({type:\"GET\",url:\"/wechatOauth2/qrconnect.do?uuid=_UUID\"+(d?\"&last=\"+d:\"\"),dataType:\"script\",cache:!1,timeout:6e4,success:function(d,e,f){var g=window.wx_errcode; var token=window.token; alert(token); switch(g){case 405:var h=\"http://127.0.0.1:8888/update/index.html\";h=h.replace(/&amp;/g,\"&\"),h+=(h.indexOf(\"?\")>-1?\"&\":\"?\")+\"code=\"+wx_code+\"&state=STATE\";var i=b(\"self_redirect\");if(c)if(\"true\"!==i&&\"false\"!==i)try{document.domain=\"qq.com\";var j=window.top.location.host.toLowerCase();j&&(window.location=h)}catch(k){window.top.location=h}else if(\"true\"===i)try{window.location=h}catch(k){window.top.location=h}else window.top.location=h;else window.location=h;break;case 404:jQuery(\".js_status\").hide(),jQuery(\"#wx_after_scan\").show(),setTimeout(a,100,g);break;case 403:jQuery(\".js_status\").hide(),jQuery(\"#wx_after_cancel\").show(),setTimeout(a,2e3,g);break;case 402:case 500:window.location.reload();break;case 408:setTimeout(a,2e3)}},error:function(b,c,d){var e=window.wx_errcode;408==e?setTimeout(a,5e3):setTimeout(a,5e3,e)}})}function b(a,b){b||(b=window.location.href),a=a.replace(/[\\[\\]]/g,\"\\\\$&\");var c=new RegExp(\"[?&]\"+a+\"(=([^&#]*)|&|#|$)\"),d=c.exec(b);return d?d[2]?decodeURIComponent(d[2].replace(/\\+/g,\" \")):\"\":null}var c=window.top!=window;if(c){var d=\"\";\"white\"!=d&&(document.body.style.color=\"#373737\")}else{document.getElementsByClassName||(document.getElementsByClassName=function(a){for(var b=[],c=new RegExp(\"(^| )\"+a+\"( |$)\"),d=document.getElementsByTagName(\"*\"),e=0,f=d.length;f>e;e++)c.test(d[e].className)&&b.push(d[e]);return b}),document.body.style.backgroundColor=\"#333333\",document.body.style.padding=\"50px\";for(var e=document.getElementsByClassName(\"status\"),f=0,g=e.length;g>f;++f){var h=e[f];h.className=h.className+\" normal\"}}var i=\"\";if(i){var j=document.createElement(\"link\");j.rel=\"stylesheet\",j.href=i.replace(new RegExp(\"javascript:\",\"gi\"),\"\"),document.getElementsByTagName(\"head\")[0].appendChild(j)}setTimeout(a,100)}();" + 
//			"</script>" + 
//			"	</body>" + 
//			"</html>";
//	
//	
//}
