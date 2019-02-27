var orderId = Commonjs.getUrlParam("id");
var caseMoney = Commonjs.getUrlParam("caseMoney");
var caseNumber = Commonjs.getUrlParam("caseNumber");


var orderState = '';
var payState = '';
var totalMoney = '';

var wxPayOrderId = Commonjs.getUrlParam("wxOrderId");

var courierCompanyCode = '';
var courierNumber = '';

$(function(){
	queryExpressOrderById();
	
});


function queryExpressOrderById() {
	var apiData = {};	
	apiData.id = orderId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/QueryExpressOrderById/callApi.do',param,function(dd){
		if(dd.RespCode == -14008){
			mui.alert('无订单');
		}else if(dd.RespCode == 10000){
			console.log(dd);
			$(".name").text(dd.Data[0].Addressee);
			$(".phone").text(dd.Data[0].Telephone);
			$(".addr").html('<img src="../mCopy/img/wechat/icon-3.png"/>'+dd.Data[0].Province+dd.Data[0].Address);
			
			courierCompanyCode = dd.Data[0].CourierCompanyCode;
			courierNumber = dd.Data[0].CourierNumber;
			totalMoney = dd.Data[0].TotalMoney;
			$("#totalMoney").text('￥ '+totalMoney);
			$("#courierNumber").text(orderId);
			if(dd.Data[0].Authentication == 1){
				//身份认证成功
				$("#authentication").append('<img class="icon-7" src="../mCopy/img/wechat/icon-7.png"/>认证通过');
			}else if(dd.Data[0].Authentication == 0){
				//身份认证失败
				$("#authentication").append('<img class="icon-7" src="../mCopy/img/wechat/close.png"/>认证失败');
			}

			orderState = dd.Data[0].OrderState;
			payState =dd.Data[0].PayState;
			//支付按钮
			if(dd.Data[0].PayState == 0){
				
			}else if(dd.Data[0].PayState == 1){
				//var redictUrl="/business/mCopy/pay-success.html?oid=" +orderId+"_"+totalMoney;
				//redictUrl = encodeURIComponent(redictUrl);
				//window.location.href = Commonjs.goToUrl("../pay/payment.html?orderId=" + wxOrderId +"&toUrl="+redictUrl);
			}else if(dd.Data[0].PayState == 2){
				//已支付
				$(".b-2").hide();
				$(".status").text("已支付");
			}else if(dd.Data[0].PayState == 3){
				$(".i-footer").hide();
				$(".status").text("退款中");
			}else if(dd.Data[0].PayState == 4){
				$(".i-footer").hide();
				$(".status").text("已退款");
			}
			
			//取消复印按钮
			if(dd.Data[0].OrderState == 1){
				
			}else if(dd.Data[0].OrderState == 2){
				$(".i-footer").hide();
			}else if(dd.Data[0].OrderState == 3){
				$(".i-footer").hide();
			}else if(dd.Data[0].OrderState == 4){
				$(".i-footer").hide();
			}
			
			getCaseInfo(dd.Data[0].CaseIdAll);
		}
	});
}


//
function getCaseInfo(caseId){
	var apiData = {};	
	apiData.caseId = caseId;
	apiData.patientId = "1";
	apiData.orgCode = "AH01";
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/GetMedicalRecordsById/callApi.do',param,function(dd){
		if(dd.RespCode == -14008){
			mui.alert('无选择病历');
		}else if(dd.RespCode == 10000){
			loadData(dd.Data);
		}
	});
}

//表格渲染
function loadData(caseList){
	var html = '';
	$("#caseLise").html(html);
	$.each(caseList,function(i,o) {
		var caseMon = caseMoney.split(",")[i];
		var caseNum = caseNumber.split(",")[i];
		
		i = i+1;
		html+= '<div class="list"><div class="list-date"><span>病历'+i+'</span></div>';
		//html+= '<span class="right">出院日期：'+o.OutHosDate+'</span>';
		html+= '<div class="list-li" ><label>出院日期</label><span>'+o.OutHosDate+'</span></div>';
		if(o.OutDeptName == ''){
			html+= '<div class="list-li"><label>住院科室</label><span>暂无</span></div>';
		}else{
			html+= '<div class="list-li"><label>住院科室</label><span>'+o.OutDeptName+'</span></div>';
		}
		
		//html+= '<div class="list-li"><label>住院科室</label><span>'+o.DeptName+'</span></div>';
//		html+= '<div class="list-li" ><label>住院天数</label><span>'+o.Hospitalization+'</span></div>';
//		if(o.OperationName != '' && o.OperationName != null){
//			html+= '<div class="list-li"><label>是否手术</label><span>是</span></div>';
//			html+= '<div class="list-li" ><label>手术名称</label><span>'+o.OperationName+'</span></div>';	
//		}else if(o.Isoperation == 1){
//			html+= '<div class="list-li"><label>是否手术</label><span>无</span></div>';			
//		}

		html+= '<div class="list-li"><label class="select"><div class="round"></div>全套病历 <span class="blue">￥ '+caseMon+'</span></label>';
		html+= '<span style="font-weight: bold;">x '+caseNum+'份</span>';
		html+= '</div></div>';
	});
	$("#caseLise").append(html);
}


function wxPay(){
	if(wxPayOrderId == '' || wxPayOrderId == null || wxPayOrderId == undefined || wxPayOrderId == '0'){
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
		
		//data.OrderMemo = '病历复印费用';
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
	}else{
		var redictUrl="/business/mCopy/pay-success.html?oid=" +orderId+"_"+totalMoney;
		redictUrl = encodeURIComponent(redictUrl);
		window.location.href = Commonjs.goToUrl("../pay/payment.html?orderId=" + wxPayOrderId +"&toUrl="+redictUrl);
	}
}

//取消复印
function cancel(){
	mui.confirm('您的病历复印已申请成功，是否取消复印？',function(e){
		if(e.index == '1'){
			var apiData = {};	
			apiData.id = orderId;
			var param = {};
			param.apiParam = Commonjs.getApiReqParams(apiData);
			Commonjs.ajax('/wsgw/medicalCopy/QueryExpressOrderById/callApi.do',param,function(dd){
				if(dd.RespCode == -14008){
					mui.alert('该订单已不存在');
				}else if(dd.RespCode == 10000){
					orderState = dd.Data[0].OrderState;
					payState = dd.Data[0].PayState;
					
					if(payState == 2 && orderState == 1){
						//退款
						//updateState(4);
						updateTransactionRecord(4);
						var apiData = {};				
						apiData.OrderId = wxPayOrderId;
						apiData.Price = Commonjs.accMul(totalMoney,100);
						apiData.RefundPrice = Commonjs.accMul(totalMoney,100);
						apiData.ChannelId = Commonjs.getClientId();
						apiData.Reason = "取消病历复印";
						apiData.OutRefundOrderId = wxPayOrderId;
						//apiData.PayConfigKey = Commonjs.getConfigKey(Commonjs.getClientId(),wxPayOrderId,"010").WxPayConfigKey;
						//apiData.RefundOrderId = "0";
						var param = {};
						param.apiParam = Commonjs.getApiReqParams(apiData);
						Commonjs.ajax('/wsgw/medicalCopy/Refund/callApi.do',param,function(dd){
							if(dd.RespCode == 10000){
								//updateState(5);
								var apiData = {};	
								apiData.id = orderId;
								//apiData.payState = "5";
								apiData.orderState = "4";
								apiData.msgUrl = location.href.split('#')[0];
								var param = {};
								param.apiParam = Commonjs.getApiReqParams(apiData);
								Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
									updateTransactionRecord(5);
									Commonjs.ajax('/wsgw/medicalCopy/WxSendMsg/callApi.do',param,function(dd){});
									mui.alert("退款成功",function(){
										var url = 'orderList.html';
										location.href = Commonjs.goToUrl(url);
									});									
								},{async:false});
							}else{
								//updateState(3);
								mui.alert("退款失败,请联系管理员");
							}
						},{async:false}); 
					}else{
						if(orderState == 1){
							var apiData = {};	
							apiData.id = orderId;
							apiData.orderState = "4";
							apiData.msgUrl = location.href.split('#')[0];
							var param = {};
							param.apiParam = Commonjs.getApiReqParams(apiData);
							Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
								if(dd.RespCode == 10000){
									var url = '../mCopy/html/wechat/notice.html';
									location.href = Commonjs.goToUrl(url);
								}
							}); 
						}else{
							mui.alert('不符合要求无法取消复印订单！');
						}
					}
				}
			});
		}
	});
}


//修改订单状态
function updateState(state){
	var apiData = {};	
	apiData.id = orderId;
	apiData.payState = state;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){});
}

//修改交易内容
function updateTransactionRecord(type){
	var apiData = {};	
	apiData.orderId = orderId;
	if(type == 4){
		apiData.shouldRefunds = totalMoney;
		//apiData.accountResult = "0";
		apiData.receiptsType = "2";
	}
	if(type == 5){
		apiData.actualRefunds = totalMoney;
		//apiData.accountResult = "2";
		apiData.receiptsType = "3";
	}	
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/UpdateTransactionRecord/callApi.do',param,function(dd){});
}

//快递信息
function expressInfo(){
	if(courierCompanyCode == '' && courierNumber == ''){
		mui.alert('尚未派送');
	}else{
		var url = '../mCopy/html/wechat/KDN.html?code='+courierCompanyCode+'&cNum='+courierNumber;
		location.href = Commonjs.goToUrl(url);
	}
}


//用于生成uuid
function S4() {
    return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}
function guid() {
    return ("BAFY_"+S4()+S4()+S4()+S4()+S4()+S4());
}
