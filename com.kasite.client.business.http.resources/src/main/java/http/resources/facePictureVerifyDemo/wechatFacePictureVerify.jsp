<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URL"%>
<%@page import="com.kasite.core.common.util.wechat.Ticket"%>
<%@page import="com.kasite.core.common.util.wechat.constants.WeiXinConstant"%>
<%@page import="com.kasite.core.common.util.StringUtil"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.kasite.core.httpclient.http.HttpRequestBus"%>
<%@page import="com.kasite.core.httpclient.http.SoapResponseVo"%>
<%@page import="com.kasite.core.httpclient.http.RequestType"%>
<%@page import="com.kasite.client.wechat.service.WeiXinService"%>
<%@page import="com.kasite.core.common.util.HttpsClientUtils"%>
<%@page import="com.kasite.core.common.constant.ApiModule"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	
	String appId = "wx53e1cfb23f11f0b0";
	String secret = "5b470c89da9fd6e269143d91b754300f";
	String tokenUrl = "http://weixin.fyyy.com/Hos-WebApp/weixin/getToken.do?grant_type=client_credential&appId="+appId+"&secret="+secret;
	
	SoapResponseVo soapResponseVo = HttpRequestBus.create(tokenUrl, RequestType.post).send();
	String accessToken = soapResponseVo.getResult();
	
	String key = appId+secret+"jsapi";
	String jsapiTicket = null;
	Ticket ticket =  WeiXinConstant.tickets.get(key);
	if(StringUtil.isNotEmpty(ticket)){
		long currentTime = System.currentTimeMillis();
		if(currentTime >= ticket.getTime()){//超时 
			String tickUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
			String tickResp = HttpsClientUtils.httpsPost(ApiModule.WeChat.token,tickUrl,null);
			JSONObject jsonobject = JSONObject.parseObject(tickResp);
			jsapiTicket = jsonobject.getString("ticket");
			ticket = new Ticket();
			ticket.setValue(jsonobject.getString("ticket"));
			int expiresIn = jsonobject.getInteger("expires_in");
			ticket.setTime(new Date(System.currentTimeMillis()+expiresIn*1000).getTime());
			WeiXinConstant.tickets.put(key,ticket);
		}else{//未超时
			jsapiTicket = ticket.getValue();
		}
	}else{
		String tickUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
		String tickResp = HttpsClientUtils.httpsPost(ApiModule.WeChat.token,tickUrl,null);
		JSONObject jsonobject = JSONObject.parseObject(tickResp);
		jsapiTicket = jsonobject.getString("ticket");
		ticket = new Ticket();
		ticket.setValue(jsonobject.getString("ticket"));
		int expiresIn = jsonobject.getInteger("expires_in");
		ticket.setTime(new Date(System.currentTimeMillis()+expiresIn*1000).getTime());
		WeiXinConstant.tickets.put(key,ticket);
	}
	
	
	//String tickUrl = "http://weixin.fyyy.com/Hos-WebApp/weixin/getTicket.do?appId="+appId+"&secret="+secret+"&type=jsapi";
	//String jsapiTicket = HttpsClientUtils.httpsPost(ApiModule.WeChat.token,tickUrl,null);
	String name = request.getParameter("name");
	String idCardNumber = request.getParameter("idCardNumber");
	String mobile = request.getParameter("mobile");
	
	
	String url = "http://weixin.fyyy.com/facePictureVerifyDemo/wechatFacePictureVerify.jsp?name="+URLEncoder.encode(name,"utf-8")+"&idCardNumber="+idCardNumber+"&mobile="+mobile;
	Map<String, String> signMap = WeiXinService.jsapiSign(jsapiTicket, url);

	String signature = signMap.get("signature"); 
	String timestamp = signMap.get("timestamp");
	String nonceStr = signMap.get("nonceStr");
	
	
	
%>


<!doctype html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="format-detection" content="telephone=no" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
	<meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <title>实名认证</title>
    <link rel="stylesheet" href="../../common/css/base.css" type="text/css" />
    <link rel="stylesheet" href="../../common/css/qlcstyle.css" type="text/css" />  
	<script type="text/javascript" src="../../common/js/jquery.min.js"></script>
	<script type="text/javascript" src="../../common/js/base.js"></script>
	<script type="text/javascript" src="../../common/js/common.js?v=20180813"></script>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.4.0.js"></script>
	<script type="text/javascript">
	wx.config({
		beta: true, // 必填，开启内测接口调用，注入 wx.invoke 和 wx.on 方法 
	    //debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    	appId: '<%=appId %>', // 必填，公众号的唯一标识
	    timestamp:'<%=timestamp %>' , // 必填，生成签名的时间戳
	    nonceStr: '<%=nonceStr %>', // 必填，生成签名的随机串
	    signature: '<%=signature %>',// 必填，签名
	    jsApiList: ['checkIsSupportFaceDetect','requestWxFacePictureVerify'] // 必填，需要使用的JS接口列表
	});
	
	wx.ready(function(){
	    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
	    //是否支持人脸识别
		wx.invoke("checkIsSupportFaceDetect",{},function(res){
			//0 支持人脸采集 support 
			//10001 不支持人脸采集：设备没有前置摄像头 No front camera 
			//10002 不支持人脸采集：没有下载到必要模型 No necessary model found 
			//10003 不支持人脸采集：后台黑名单控制 Device is blacklisted

			if(res.errCode == 0) {
				//0 支持人脸采集
				
			} else {
				myLayer.alert("对不起，您的手机不支持人脸采集！",30000);
			}
		});
	    
		wx.invoke("requestWxFacePictureVerify", {"request_verify_pre_info": "{\"name\": \"<%=name %>\", \"id_card_number\": \"<%=idCardNumber %>\", \"mobile\": \"<%=mobile %>\"}"},function(res){
			if(res.err_code == 0) {
				var verify_identifier = res.verify_result;
				//alert(verify_identifier);
				//if( 'ok' == verify_identifier){
					location.href="https://hiscloud.yihu.com/JkzlApp/Doctor/doctorIndex.shtml?memberSn=9603559&sourceId=1";
				//}else{
				//	myLayer.alert("人脸识别异常！请重新刷新页面！",30000);
				//}
				// 开发者选择是否使用后台文档获取本次认证的其他信息
			} else {
				myLayer.alert("人脸识别异常！请重新刷新页面！",30000);
			}
		});
	});
	
	wx.error(function(res){
	    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
	    alert(res);
	});
	</script>
</head>
<body>
	



</body>
</html>