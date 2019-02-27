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
var isCopyBy30Day;

$(function(){
	mui.alert('请认真确认需复印的病历份数！');
	var apiData = {};	
	apiData.id = McopyUserId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/QueryPatientInfoById/callApi.do',param,function(dd){
		if(dd.RespCode == -14018){
			$("#name").text('查无此人');
			$("#PatientId").text('暂无');
			$("#IdCard").text('暂无');
		}else if(dd.RespCode == 10000){
			patientId = dd.Data[0].PatientId;
			$("#name").text(dd.Data[0].Name);
			$("#PatientId").text(patientId);
			$("#IdCard").text(dd.Data[0].IdCardFormat);
			$("#Phone").text(dd.Data[0].MobileFormat);
		}
	});
	
	$("#addressee").text(addressee);
	$(".phone").text(telephone);
	$(".addr").html('<img src="../mCopy/img/wechat/icon-3.png"/>'+province+address);
	
	var apiData = {};	
	apiData.caseId = caseId;
	apiData.patientId = McopyUserId;
	apiData.orgCode = "AH01";
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/GetMedicalRecordsById/callApi.do',param,function(dd){
		if(dd.RespCode == 10000){
			loadData(dd.Data);
		}else if(dd.RespCode == -14018){
			mui.alert('没有选择任何订单');
		}
	});
	
});


//表格渲染
function loadData(caseList){
	var html = '';
	$("#caseLise").append(html);
	$.each(caseList,function(i,o) {
		var caseMon = caseMoney.split(",")[i];
		var caseNum = caseNumber.split(",")[i];
		
		i = i+1;
		html+= '<div class="list"><div class="list-date"><span>病历'+i+'</span></div>';
		html+= '<div class="list-li" ><label>出院日期</label><span>'+o.OutHosDate+'</span></div>';
		//html+= '<span class="right">出院日期：'+o.OutHosDate+'</span>';
		if(o.OutDeptName == ''){
			html+= '<div class="list-li"><label>住院科室</label><span>暂无</span></div>';
		}else{
			html+= '<div class="list-li"><label>住院科室</label><span>'+o.OutDeptName+'</span></div>';
		}
		//html+= '<div class="list-li" ><label>住院天数</label><span>'+o.Hospitalization+'</span></div>';
		
/*		if(o.Isoperation == 0){
			html+= '<div class="list-li"><label>是否手术</label><span>是</span></div>';
			html+= '<div class="list-li" ><label>手术名称</label><span>'+o.OperationName+'</span></div>';	
		}else if(o.Isoperation == 1){
			html+= '<div class="list-li"><label>是否手术</label><span>无</span></div>';			
		}*/

		html+= '<div class="list-li"><label class="select"><div class="round cur"></div>全套病历 <span class="red">￥ '+caseMon+'</span></label>';
		html+= '<span class="red" style="font-weight: bold;font-size: 0.36rem;">x '+caseNum+'份</span>';
		
		html+= '</div></div>';

	});
	html += '<div class="list-date"><span class="edit" onclick="returnList()">重选病历及份数</span></div>';
	$("#caseLise").append(html);
	$(".price").text('￥ '+totalMoney);
}


//重填地址
function sureAddress(){
	var url = '../mCopy/html/wechat/address.html?totalMoney='+totalMoney+"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+
		"&caseId="+caseId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&authentication="+authentication+"&mediaName="+mediaName+
		"&addressee="+addressee+"&telephone="+telephone+"&address="+address+"&province="+province+"&reSelectAddress=1";
	location.href = Commonjs.goToUrl(url);
}

//立即支付
function submit(){
	if(caseNumber == '' || caseNumber == null){
		mui.alert('没有选择任何病历！');
	}else{
//		var allCase = 0;
//		var caseNumList = caseNumber.split(",");
//		for(var i = 0;i<caseNumList.length;i++){
//			allCase = parseInt(allCase) + parseInt(caseNumList[i]);
//		}
//		mui.confirm('您共打印'+allCase+'份病历，是否进行支付？',function(e){
//			if(e.index == '1'){
				if(totalMoney == 0){
					mui.alert("未选择病历");
				}else{
					//新增一条订单
					var apiData = {};	
					apiData.totalMoney = totalMoney;
					apiData.caseIdAll = caseId;
					apiData.caseMoneyAll = caseMoney;
					apiData.caseNumberAll = caseNumber;
					//apiData.wxOrderId = guid();
					apiData.patientId = McopyUserId;
					apiData.authentication = authentication;
					apiData.addressee = addressee;
					apiData.telephone = telephone;
					//apiData.applyName = '';
					apiData.applyOpenId = Commonjs.getOpenId();
					apiData.address = address;
					apiData.province = province;
					apiData.idcardImgName = mediaName;
					var param = {};
					param.apiParam = Commonjs.getApiReqParams(apiData);
					Commonjs.ajax('/wsgw/medicalCopy/AddExpressOrder/callApi.do',param,function(dd){
						if(dd.RespCode == -14008){
							mui.alert('无病历信息');
						}else if(dd.RespCode == 10000){
							//var url = 'detail.html?id='+dd.RespMessage+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber;
							//location.href = Commonjs.goToUrl(url);		
							wxPay(dd.RespMessage);
						}
					},{async:false});
				}
//			}
			//		});
	}

	//isCopyBy30Day();
//	else if(!isCopyBy30Day){
//		mui.alert("有病历在30内已申请复印过，请重新选择");
//	}
	//mui.alert('请认真确认需复印的病历份数,病历复印30天内只能申请一次',function(){

	//});
}

//微信支付
function wxPay(orderId){
	var param = {};
	param.api = "AddOrderLocal";
	var data = {};
	data.HosId = Commonjs.hospitalId();
	data.PayMoney = Commonjs.accMul(totalMoney,100); //患者应付金额(单位：分)
	data.TotalMoney = Commonjs.accMul(totalMoney,100); //缴费单应收金额(单位：分)
	data.OperatorId = Commonjs.getOpenId();
	data.EqptType = 1;
	data.PriceName = '病历复印费用';
	data.OrderId = guid();
	data.CardNo = "";
	data.CardType = "";
	data.OperatorName = "微信";

	var exapiData = {};	
	exapiData.id = orderId;
	var exparam = {};
	exparam.apiParam = Commonjs.getApiReqParams(exapiData);
	Commonjs.ajax('/wsgw/medicalCopy/GetExpressOrderList/callApi.do',exparam,function(dd){
		if(dd.RespCode == 10000){
			data.Data_1 = {};
			data.Data_1.病案号=dd.Data[0].PatientId;
			data.Data_1.病人姓名=dd.Data[0].Name;
			data.Data_1.申请时间=dd.Data[0].ApplyTime;
		}
	},{async:false});

	data.ChannelId = Commonjs.getClientId();
	if(data.ChannelId=='100123'){
		//订单发起的商户类型 订单商户类型0本地订单1微信2支付宝
		data.MerchantType	= '1';
	}else if(data.ChannelId=='100125'){
		data.MerchantType	= '2';
	}
	data.ServiceId = '010'; //订单类型
	data.IsOnlinePay = '1'; //是否线上支付订单 1是2否
	param.apiParam = Commonjs.getApiReqParams(data);
	Commonjs.ajax('/wsgw/order/AddOrderLocal/callApi.do', param, function(jsons) {
		if(!Commonjs.isEmpty(jsons.RespCode)) {
			if(jsons.RespCode == '10000') {
			
				wxPayOrderId = jsons.Data[0].OrderId;
				var apiData = {};	
				apiData.id = orderId;
				//apiData.payState = "2";
				apiData.wxOrderId = wxPayOrderId;
				param = {};
				param.apiParam = Commonjs.getApiReqParams(apiData);
				Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
					if(dd.RespCode == 10000){
						var redictUrl="/business/mCopy/pay-success.html?oid=" +orderId+"_"+totalMoney;
						redictUrl = encodeURIComponent(redictUrl);
						window.location.href = Commonjs.goToUrl("../pay/payment.html?orderId=" + wxPayOrderId +"&toUrl="+redictUrl);
					}
				});
			} else {
				$("body").load("../pay/payError.html"); //页面跳转
			}
		} else {
			mui.alert("返回码为空");
		}
	},{async:false});
}


//确认病历是否在30天之内打印过的
//function isCopyBy30Day(){
//	var apiData = {};	
//	apiData.case_id = caseId;
//	var param = {};
//	param.apiParam = Commonjs.getApiReqParams(apiData);
//	Commonjs.ajax('/wsgw/medicalCopy/IsCopyBy30Day/callApi.do',param,function(dd){
//		if(dd.RespCode == 10000){
//			isCopyBy30Day = true;
//		}else{
//			isCopyBy30Day = false;
//		}
//	});
//} 

//重选病历
function returnList(){
	var url = '../mCopy/html/wechat/list.html?totalMoney='+totalMoney+"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+
		"&caseId="+caseId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&authentication="+authentication+"&mediaName="+mediaName+
		"&addressee="+addressee+"&telephone="+telephone+"&address="+address+"&province="+province+"&reSelectCase=1";
	location.href = Commonjs.goToUrl(url);
}

//用于生成uuid
function S4() {
    return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}
function guid() {
    return ("BAFY_"+S4()+S4()+S4()+S4()+S4()+S4());
}

