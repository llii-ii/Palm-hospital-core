var orderId = "";
var toUrl = "";
var priceName = "";
var authCode = "";
var canPay = true;
var clientId = Commonjs.getClientId();
var token = "";
var overState = 0;
/*
 *		JSONObject payConfig = new JSONObject();
		payConfig.put("WxPayConfigKey", wxPayConfigKey);
		payConfig.put("ZfbConfigKey", zfbConfigKey);
 */
var payConfig = {};
$(function() {
	var logoUrl = Commonjs.getLogoUrl();
	var logohtml = '<img width="50px" height="50px" src="'+logoUrl+'" id="icon">';
	$("#logoImgHtml").html(logohtml);
	var session = Commonjs.getSession();
	var orgName = session.orgName;
	$("#orgNameSpan").html(orgName);
	orderId = Commonjs.getUrlParam("orderId");
	if(Commonjs.isEmpty(orderId)) {
		orderId = Commonjs.getUrlParam("state");
	} 
	toUrl = Commonjs.getUrlParam("toUrl");
	token = Commonjs.getToken();
	getOrderDetail();
	
	
});
function showPay(){
	var userAgent = navigator.userAgent.toLowerCase();
	var localChannelId = null;//根据浏览器判断渠道id
	var localChannelName = null;//根据浏览器判断渠道名称
	var isWeChat = true;
	if(userAgent.indexOf("alipay")>-1){
		$("#payImg").attr('src','https://v1kstcdn.kasitesoft.com/common/images/pay-zfb.jpg')
		$("#paySpan").html('支付宝支付');
		$("#payDiv").show();
		authCode = Commonjs.getUrlParam("auth_code");
		isWeChat=false;
	}else if(userAgent.indexOf("micromessenger")>-1){
		$("#payImg").attr('src','https://v1kstcdn.kasitesoft.com/common/images/pay-wx.png')
		$("#paySpan").html('微信支付');
		$("#payDiv").show();
		authCode = Commonjs.getUrlParam("code");
	}else{//默认微信
		$("#payImg").attr('src','https://v1kstcdn.kasitesoft.com/common/images/pay-wx.png')
		$("#paySpan").html('微信支付');
		$("#payDiv").show();
		authCode = Commonjs.getUrlParam("code");
	}
	if(!Commonjs.isEmpty(payConfig.NetPayConfigKey)){//查询后端配置存在一网通支付配置
		$("#netPay").show();
	}
	$("#netPayBtn").on('click', function() {
		if(!canPay){
			return;
		}
		canPay = false;
		netPayH5();
	});
	
	$("#wxPay").on('click', function() {
		if(!canPay){
			return;
		}
		canPay = false;
		if(isWeChat == true){
			PayWeixin();
		}else if(isWeChat == false){
			PayAlipayJs();//JS方式
			//PayAlipay();//WAP方式，服务窗不建议使用
		}else{
			myLayer.alert("请在微信或者支付宝浏览器支付！", 3000);
		}
	})
}
//获取订单详细信息
function getOrderDetail() {
	var apiData = {};
	var param = {};
	var page = {};
	page.PIndex = 0;
	page.PSize = 1;
	apiData.OrderId = orderId;
	param.api = 'OrderListLocal';
	param.apiParam = Commonjs.getApiReqParams(apiData, page, 6003);
	Commonjs.ajax('../../wsgw/order/OrderListLocal/callApi.do?t=' + new Date().getTime(), param,function(jsons) {
		
		if(!Commonjs.isEmpty(jsons)) {
			if(jsons.RespCode == 10000) {
				if(Commonjs.ListIsNotNull(jsons.Data)) {
					var htmls = "";
					Commonjs.BaseForeach(jsons.Data, function(i, item) {
						priceName = item.PriceName;
						var cardType = item.CardType;
						var itemName = '项目名称';
						var itemValue = priceName;
						htmls = '<li><div class="c-list-key c-999 pr15">'+itemName+'</div><div class="c-list-info c-t-right">'+itemValue+'</div></li>';
						itemName = '支付金额';
						itemValue = Commonjs.centToYuan(item.PayMoney);
						htmls += '<li><div class="c-list-key c-999 pr15">'+itemName+'</div><div class="c-list-info c-t-right">￥<span class="c-ffac0b">'+itemValue+'</span></div></li>';
						
						var serviceId = item.ServiceId;
						itemName = '卡号';
						if(!Commonjs.isEmpty(cardType)){
							if(cardType*1 == 14){
								itemName = "住院号";
							}else if(cardType*1 == 1){
								itemName = "就诊卡";
							}
						}
						itemValue = item.CardNo;
						if(!Commonjs.isEmpty(itemValue)){
							htmls += '<li><div class="c-list-key c-999 pr15">'+itemName+'</div><div class="c-list-info c-t-right">'+itemValue+'</div></li>';
						}
						itemName = '姓名';
						itemValue = item.MemberName;
						if(!Commonjs.isEmpty(itemValue)){
							htmls += '<li><div class="c-list-key c-999 pr15">'+itemName+'</div><div class="c-list-info c-t-right">'+itemValue+'</div></li>';
						}
						clientId = item.ChannelId;
						if(!Commonjs.isEmpty(clientId)){
							var clientIdName = Commonjs.getChannelIdName(clientId);
							$("#zhifuNameSpan").html(clientIdName);
						}
						var orderId = item.OrderId;
						payConfig = Commonjs.getConfigKey(clientId,orderId,serviceId);
						var payState = item.PayState;

						
						var isOnlinePay = item.IsOnlinePay;
						
						if(isOnlinePay == 2){
							$("#wxPay").hide();
							myLayer.alert("该订单不支持在线支付！", 3000);
						}
						var overState = item.OverState;
						if(overState == 5){
							$("#wxPay").hide();
							$("#payTimeSrc").html("该订单已经超过或已取消");
							myLayer.alert("该订单已经超过或已取消，请重新下单支付！", 3000);
						}
						if(payState == 2){
							$("#wxPay").hide();
							myLayer.alert("该订单已经支付成功！", 3000);
						}else{
							showPay();
						}
					});
					$("#payItems").html(htmls);
				}
			} else {
				myLayer.alert("不存在需要支付的订单！", 3000);
			}
		}
	},{async:false});

}

/*微信支付*/
function PayWeixin() {
	var param = {}; 
	param.orderId = orderId;
	param.priceName = priceName;
	param.code = authCode;
	if(!payConfig){
		myLayer.alert("该渠道不支持微信支付！", 3000);
		return;
	}
	var wxPayConfigKey = payConfig.WxPayConfigKey;
	if(!wxPayConfigKey){
		myLayer.alert("该订单不支持微信支付！", 3000);
		return;
	}
	
	var url = '../../wxPay/'+clientId+'/'+wxPayConfigKey+'/getUnitePay.do?t=' + new Date().getTime();
	Commonjs.ajax(url, param,function(retJson) {
		if(!Commonjs.isEmpty(retJson)) {
			if(retJson.RespCode == 10000) {
				if(!Commonjs.isEmpty(retJson.Data)) {
					if (typeof WeixinJSBridge == "undefined"){
						   if( document.addEventListener ){
						       document.addEventListener('WeixinJSBridgeReady',function(){
						    	   ExecWxZf(retJson.Data);
						       }, false);
						   }else if (document.attachEvent){
						       document.attachEvent('WeixinJSBridgeReady', function(){
						    	   ExecWxZf(retJson.Data);
						       }); 
						       document.attachEvent('onWeixinJSBridgeReady', function(){
						    	   ExecWxZf(retJson.Data);
						       });
						   }else{
								canPay = true;
						   }
						}else{
							ExecWxZf(retJson.Data);
						} 
				}
			} else {
				canPay = true;
				myLayer.alert(retJson.RespMessage, 3000);
			}
		}
	},{async:false});
}

/*支付宝服务窗支付-js*/
function PayAlipayJs() {
	var param = {}; 
	param.orderId = orderId;
	param.priceName = priceName;
	param.code = authCode;
	var zfbConfigKey = payConfig.ZfbConfigKey;
	if(!zfbConfigKey){
		myLayer.alert("该订单不支持支付宝支付！", 3000);
		return;
	}
	var url = '../../alipay/'+clientId+'/'+zfbConfigKey+'/getUnitePay.do?t=' + new Date().getTime();
	Commonjs.ajax(url, param,function(retJson) {
		if(!Commonjs.isEmpty(retJson)) {
			if(retJson.RespCode == 10000) {
				if(!Commonjs.isEmpty(retJson.Data)) {
					if (window.AlipayJSBridge) {
						alipayJsTradePay(retJson.Data)
			         } else {
			             document.addEventListener('AlipayJSBridgeReady', alipayJsTradePay(retJson.Data), false);
			         }
				}
			} else {
				canPay = true;
				myLayer.alert(retJson.RespMessage, 3000);
			}
		}
	},{async:false});
};


/*支付宝支付-网页版*/
function PayAlipay() {
	canPay = true;
	var zfbConfigKey = payConfig.ZfbConfigKey;
	if(!zfbConfigKey){
		myLayer.alert("该订单不支持支付宝支付！", 3000);
		return;
	}
	
	location.href = Commonjs.getRootPath + "/alipay/"+clientId+"/"+zfbConfigKey+"/tradeWapPay.do?orderId=" + orderId+"&toUrl=" + toUrl+"&priceName=" + priceName
					+"&authCode="+authCode+"&token="+token;
}

///执行微信支付
function ExecWxZf(reJson) {
	var appId = reJson.appId;
	var timeStamp = reJson.timeStamp;
	var nonceStr = reJson.nonceStr;
	var package1 = reJson.package;
	var paySign = reJson.paySign;
	var signType = reJson.signType;
	WeixinJSBridge.invoke('getBrandWCPayRequest', {
		"appId": appId, //公众号名称，由商户传入
		"timeStamp": timeStamp, //时间戳
		"nonceStr": nonceStr, //随机串
		"package": package1, //扩展包
		"signType": signType, //微信签名方式
		"paySign": paySign //微信签名
	}, function(res) {
		//如果一直异常并且无法确认抛出异常是什么原因的时候这里可以
		//通过直接alert的方式打印下res 这个是个json对象需要转成字符串打印下确认问题
		if(res.err_msg == "get_brand_wcpay_request:ok") {
			canPay = true;
			myLayer.alert("支付成功", 3000);
			var wxPayConfigKey = payConfig.WxPayConfigKey;
			var url = Commonjs.getRootPath + "/wxPay/"+clientId+"/"+wxPayConfigKey+"/payCallBack.do?token="+Commonjs.getToken()+"&orderId=" + orderId + "&toUrl=" + toUrl;
			window.location.href = url;
		} else if(res.err_msg == "get_brand_wcpay_request:cancel") {
			canPay = true;
			myLayer.alert("支付取消", 3000);
		} else if(res.err_msg == "get_brand_wcpay_request:fail") {
			canPay = true;
			myLayer.alert("支付失败", 3000);
		}else{
			canPay = true;
			myLayer.alert("支付失败，未知异常："+res.errMsg, 3000);
		}
	});
}

/**
 * 唤起支付宝js支付控件
 * @param tradeNO
 * @returns
 */
function alipayJsTradePay(tradeNO) {
	 // 通过传入交易号唤起快捷调用方式(注意tradeNO大小写严格)
    AlipayJSBridge.call("tradePay", {
         'tradeNO': tradeNO
    }, function (data) {
       // alert(JSON.stringify(data));
        if ("9000" == data.resultCode) {
        	canPay = true;
			myLayer.alert("支付成功", 3000);
			var zfbConfigKey = payConfig.ZfbConfigKey;
			var url = Commonjs.getRootPath + "/alipay/"+clientId+"/"+zfbConfigKey+"/payCallBack.do?token="+Commonjs.getToken()+"&orderId=" + orderId + "&toUrl=" + toUrl;
			window.location.href = url;
        }else if ("6001" == data.resultCode) {
        	canPay = true;
			myLayer.alert("支付取消", 3000);
        }else if ("8000" == data.resultCode) {
        	canPay = true;
			myLayer.alert("支付中", 3000);
        }else if ("9000" == data.resultCode) {
        	canPay = true;
			myLayer.alert("支付成功", 3000);
        }else{
        	canPay = true;
        	myLayer.alert("支付失败:"+data.resultCode+"|"+data.result, 3000);
        }
    });
}

function netPayH5(){
	var netPayConfigKey = payConfig.NetPayConfigKey;
	if(!netPayConfigKey){
		myLayer.alert("该订单不支持一网通支付！", 3000);
		return;
	}
	location.href = Commonjs.getRootPath + "/netPay/"+clientId+"/"+netPayConfigKey+"/getWapUnitePay.do?orderId=" + orderId+"&priceName=" + priceName
	+"&authCode="+authCode+"&token="+token+"&toUrl=" + toUrl;
}

