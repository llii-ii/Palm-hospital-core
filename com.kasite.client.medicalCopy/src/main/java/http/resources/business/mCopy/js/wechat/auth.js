var totalMoney = Commonjs.getUrlParam("totalMoney");
var McopyUserId = Commonjs.getUrlParam("McopyUserId");
var caseId = Commonjs.getUrlParam("caseId");
var caseMoney = Commonjs.getUrlParam("caseMoney");
var caseNumber = Commonjs.getUrlParam("caseNumber");

var addressee = decodeURI(Commonjs.getUrlParam("addressee"));
var address = decodeURI(Commonjs.getUrlParam("address"));
var province = decodeURI(Commonjs.getUrlParam("province"));	
var telephone = Commonjs.getUrlParam("telephone");
var authentication = Commonjs.getUrlParam("authentication");

var mediaName = Commonjs.getUrlParam("mediaName");
var mediaUrl = '';
var wxKey = '';

var phone = '';
var PCId = '';
var idCardSide1 = '';
var idCardSide2 = '';
var idCardNum = '';
var patientName = '';
$(function(){
	if(mediaName != '' && mediaName != null && mediaName != undefined && mediaName != "null"){
		var imgUrl = 'http://'+location.host +"/"+mediaName;
		$("#side1").css("background","url(\""+imgUrl+"1.png\")");
		$("#side1").css("background-size","100% 100%");
		$("#side2").css("background","url(\""+imgUrl+"2.png\")");
		$("#side2").css("background-size","100% 100%");
		mediaUrl = mediaName;
	}
	getPatientInfoById();
	if(authentication == "1"){
		//已经验证过的将获取手机验证码隐藏；
		$(".auth-phone").hide();
		$("#phone").hide();
	}else{
		var apiData = {};	
		apiData.type = "jsapi";
		apiData.url = location.href.split('#')[0];
		var param = {};
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('/wsgw/medicalCopy/GetWxConfigInfo/callApi.do',param,function(dd){
			var data = JSON.parse(dd.RespMessage);
			wxKey = data.configKey;
			wx.config({
				debug: false,
				appId: data.appId,
				timestamp: data.timestamp,
				nonceStr: data.nonceStr,
				signature: data.signature,
		    jsApiList: [
		    	'checkJsApi',
		    	'chooseImage',
		    	'uploadImage',
		    	'previewImage'
		    	]
			});
		},{async:false});
	}
});

//微信选择照片接口
function OCRIDCard(side){
	 var images = {  
	          localId: [],  
	          serverId: []  
	      }; 
	      wx.chooseImage({
	  		count: 1, // 默认9
	  		sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
	  		sourceType: ['album', 'camera'],   
	          success: function(res) {  
	          	console.log(res);
	              images.localId = res.localIds;  	  
	              if (images.localId.length > 1) {  
	                  alert("只允许上传一张图片！");
	                  return;  
	              }  
	              uploadImg(images.localId,side);  
	          }
	          
	      }); 
}

function uploadImg(localIds,side){
	wx.uploadImage({
		localId: localIds.toString(), // 需要上传的图片的本地ID，由chooseImage接口获得  
		isShowProgressTips: 1, // 默认为1，显示进度提示  
		success: function(res) {
			var serverId = res.serverId; // 返回图片的服务器端ID
			getPic(serverId,side);	
		}
	});
}

function getPic(serverId,side){
	if(mediaName == '' || mediaName == null || mediaName == undefined || mediaName == "null"){
		var timestamp = (new Date()).getTime();
		mediaName = McopyUserId + "_" + timestamp + "_";
	}
	var param = {};
	param.configKey = wxKey;
	param.mediaId = serverId;
	param.mediaName = mediaName + side + ".png";
	Commonjs.ajax('/upload/uploadWxFile.do',param,function(dd){
		if(dd.RespCode == 10000){
			mediaUrl = dd.url.substring(0,dd.url.length-5);
			var imgUrl = 'http://'+location.host +"/"+dd.url;
			$("#side"+side).css("background","");
	    	$("#side"+side).css("background","url(\""+imgUrl+"\")");
	    	$("#side"+side).css("background-size","100% 100%");
		}else{
			mui.alert('身份证上传失败请联系管理员！');
		}
	},{async:false});
}

//获得验证码
function getPCId() {
		if(authentication == '1'){
			mui.alert("验证已通过无需重复验证");
		}else if(authentication == '0'){
			if(phone == ''){
				mui.alert('患者本人未登记手机无法发送验证码');
			}else{
				var apiData = {};
				apiData.Mobile = phone;
				var param = {};
				param.apiParam = Commonjs.getApiReqParams(apiData);
				Commonjs.ajax('/wsgw/basic/GetProvingCode/callApi.do?time=' + new Date().getTime(),param,function(dd){
					PCId = dd.Data[0].PCId;
					mui.alert("验证码已发送病历中留存的手机号"+plusXing(phone,3,4)+"，如手机号更改请致电400-888-4027");
					$("#phone").text("验证码已发送至病历中留存的手机号"+plusXing(phone,3,4)+"，如手机号更改请致电400-888-4027");
					$("#phone").css("font-weight","bold");
				});		
			}		
		}
}

//获得病人信息
function getPatientInfoById() {
	var apiData = {};	
	apiData.id = McopyUserId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/QueryPatientInfoById/callApi.do',param,function(dd){
		if(dd.RespCode == 10000){
			phone = dd.Data[0].Phone;
			idCardNum = dd.Data[0].IdCard;
			patientName = dd.Data[0].Name;
			if(phone == "暂无"){
				$("#phone").text("患者本人未登记手机无法发送验证码");
			}else{
				$("#phone").text("验证码将发送至病历中留存的手机号"+plusXing(phone,3,4)+"，如手机号更改请致电400-888-4027");
			}
		}else if(dd.RespCode == -14018){
			$("#phone").text("患者本人未登记手机无法发送验证码");
		}
	});
}

//下一步
function gotoPay(){
//	authentication = 1;
//	var url = '../../pay.html?totalMoney='+totalMoney+
//		"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+"&authentication="+authentication+
//		"&caseId="+caseId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&mediaName="+mediaUrl+
//		"&addressee="+addressee+"&telephone="+telephone+"&address="+address+"&province="+province;
//	location.href = Commonjs.goToUrl(url);
	if(authentication == 1){
		authentication = 1;
		var url = '../../pay.html?totalMoney='+totalMoney+
			"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+"&authentication="+authentication+
			"&caseId="+caseId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&mediaName="+mediaUrl+
			"&addressee="+addressee+"&telephone="+telephone+"&address="+address+"&province="+province;
		location.href = Commonjs.goToUrl(url);
	}else{
		if($("#side1").css("background-size") != "100% 100%"){
			mui.alert("未上传身份证正面照");
		}else if($("#side2").css("background-size") != "100% 100%"){
			mui.alert("未上传身份证反面照");
		}else if(PCId == ''){
			mui.alert("未获得手机验证码");
		}else if($("#code").val() == ''){
			mui.alert("手机验证码未填");
		}else {
			var apiData = {};	
			apiData.picId = mediaUrl;
			apiData.Mobile = phone;
			apiData.patientName = patientName;
			apiData.idCard = idCardNum;
			apiData.code = $("#code").val();
			apiData.pcId = PCId;
			var param = {};
			param.apiParam = Commonjs.getApiReqParams(apiData);
			Commonjs.ajax('/wsgw/medicalCopy/Verification/callApi.do',param,function(dd){
				if(dd.RespCode == 10000){
					authentication = 1;
					var url = '../../pay.html?totalMoney='+totalMoney+
						"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+"&mediaName="+mediaUrl+
						"&caseId="+caseId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&authentication="+authentication+
						"&addressee="+addressee+"&telephone="+telephone+"&address="+address+"&province="+province;
					location.href = Commonjs.goToUrl(url);
				}else{
					$("#code").val('');
					$("#side1").css("background","");
					$("#side1").css("background-size","");
					$("#side2").css("background","");
					$("#side2").css("background-size","");
					mui.alert(dd.RespMessage);
					$("#phone").text("验证码将发送至病历中留存的手机号"+plusXing(phone,3,4)+"，如手机号更改请致电400-888-4027");
					$("#phone").css("font-weight","");
				}
			});
		}		
	}
}

//上一步
function gotoAddress(){
	 var url = 'address.html?totalMoney='+totalMoney+
		"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+"&authentication="+authentication+
		"&caseId="+caseId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&mediaName="+mediaUrl+
		"&addressee="+addressee+"&telephone="+telephone+"&address="+address+"&province="+province;
	 location.href = Commonjs.goToUrl(url);
}

//电话和姓名身份证等进行部分隐藏处理
//str：字符串，frontLen：前面保留位数，endLen：后面保留位数。
function plusXing (str,frontLen,endLen) { 
	var len = str.length-frontLen-endLen;
	var xing = '';
	for (var i=0;i<len;i++) {
	xing+='*';
	}
	return str.substring(0,frontLen)+xing+str.substring(str.length-endLen);
}

