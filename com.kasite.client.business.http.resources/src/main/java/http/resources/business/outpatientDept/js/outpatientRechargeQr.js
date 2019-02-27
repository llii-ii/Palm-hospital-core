var ihMaxPayMoney = 1000000;//默认最大支付金额1000000分,10000元
var ihMinPayMoney = 100;//默认最小支付金额100分,1元
$(function() {
	//获取二维码信息Id
	var guideId = Commonjs.getUrlParam("guid");
	//查询支付配置
	queryFrontPayLimit();
	if( !Commonjs.isEmpty(guideId)){
		//获取二维码信息点
		getGuide(guideId);
	}else{
		var cardInfoStr = Commonjs.getDecodeUrlParam("cardInfo");
		var cardInfo = JSON.parse(cardInfoStr);
		$("#hisMemberId").val(cardInfo.hisMemberId);
		queryPatientInfo(cardInfo.cardNo);
	}

	//初始化控件
	iniWidget();
});

function iniWidget(){

	$("#imgLogo").attr('src',logoUrl);
	//初始化医院名称
	$("#pHosName").html(Commonjs.getSession().orgName);
	
	//输入金额
	$('.charge-text').on('input', function() {
		var cval = $(this).val();
		if($.trim(cval) == '') {
			$('#chargeBtn').addClass('disable');
		} else {
			$('.charge-box li').removeClass('curr');
			$('#chargeBtn').removeClass('disable');
		}
		$('#chargeNum').html(cval);
	});
	
	$('.c-main').on('click', '.charge-box li', function() {
		if(!$(this).hasClass('charge-input')) {
			$(this).addClass('curr').siblings().removeClass('curr');
			$('.charge-text').val('');
			$('#chargeNum').html($(this).find('label').html());
			$('#chargeBtn').removeClass('disable');
		}
	});
	
}

/**
 * 获取信息点
 * @param guideId
 * @returns
 */
function getGuide(guideId){
	var param={};
	param.api="GetGuide";
	var data={};
	data.GuideId = guideId;
	param.apiParam = Commonjs.getApiReqParams(data);
	Commonjs.ajax('/wsgw/smartPay/GetGuide/callApi.do',param, function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					if(Commonjs.ListIsNull(jsons.Data)) {
						var data = jsons.Data[0];
						if(Commonjs.isEmpty(data.Content)) {
							myLayer.alert("获取用户信息错误！请重新扫描！");
							return;
						}
						var guideContent = JSON.parse(data.Content)
						if(!Commonjs.isEmpty( guideContent.hisMemberId)){
							$("#hisMemberId").val(guideContent.hisMemberId);
						}
						queryPatientInfo(guideContent.cardNo);
					}
				} else {
					myLayer.alert(jsons.RespMessage, 3000);
				}
			} else {
				myLayer.alert("返回码为空");
			}
		} else {
			//通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	},{async:false});
}

/**
 * 查询住院信息
 * @returns
 */
function queryPatientInfo(cardNo){
	var apiData = {};
	apiData.ClinicCard = cardNo;
	var page = {};
	page.PIndex = 0;
	page.PSize = 1;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/basic/QueryPatientInfo/callApi.do', param, function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					if(Commonjs.ListIsNull(jsons.Data)) {
						var data = jsons.Data[0];
						$("#cardBalance").html("￥"+Commonjs.centToYuan(data.Balance));
						$("#memberName").html(data.MemberName);
						$("#cardNo").html(cardNo);
					}
				} else {
					myLayer.alert(jsons.RespMessage, 3000);
				}
			} else {
				myLayer.alert("返回码为空");
			}
		} else {
			//通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	},{async:false});
}

function queryFrontPayLimit(){
	var param={};
	param.api="QueryPayConfig";
	var data={};
	param.apiParam = Commonjs.getApiReqParams(data);
	Commonjs.ajax('/wsgw/pay/QueryFrontPayLimit/callApi.do',param,function(jsons) {
		if(jsons.RespCode==10000 && !Commonjs.isEmpty(jsons.Data)){
			ihMaxPayMoney=jsons.Data[0].IhMaxPayMoney;
			ihMinPayMoney=jsons.Data[0].IhMinPayMoney;
		}
	},{async:false});
}

/**
 * 充值
 */
$('#chargeBtn').on('click', function() {
	//debugger;
	var disable = $(this).hasClass('disable');
	if(disable) {
		myLayer.alert('请输入充值金额');
		return;
	}
	if(Commonjs.isEmpty($("#cardNo").html())){
		myLayer.alert("就诊卡不能为空");
		return;
	}	
	if(Number($('#chargeNum').html())>Number(Commonjs.centToYuan(ihMaxPayMoney))){
		$('#chargeNum').html(Commonjs.centToYuan(ihMaxPayMoney));
		$('#chargeInput').val(Commonjs.centToYuan(ihMaxPayMoney));
		myLayer.alert('充值金额不能大于'+Commonjs.centToYuan(ihMaxPayMoney)+"元");
		return;
	}
	if(Number($('#chargeNum').html())<Number(Commonjs.centToYuan(ihMinPayMoney))){
		$('#chargeNum').html(Commonjs.centToYuan(ihMinPayMoney));
		$('#chargeInput').val(Commonjs.centToYuan(ihMinPayMoney));
		myLayer.alert('充值金额不能少于'+Commonjs.centToYuan(ihMinPayMoney)+"元");
		return;
	}

	var param = {};
	param.api = "AddOrderLocal";
	var data = {};
	data.HosId = Commonjs.getUrlParam("HosId");
	data.PayMoney = $('#chargeNum').html()*100; //患者应付金额(单位：分)
	data.TotalMoney = $('#chargeNum').html()*100; //缴费单应收金额(单位：分)
	data.OperatorId = Commonjs.getOpenId();
	data.EqptType = 1;
	data.PriceName = '卡面付就诊卡充值';
	data.CardNo = $("#cardNo").html();
	data.CardType = Commonjs.constant.cardType_1;
	data.OperatorName = Commonjs.getOpenId();
	data.OrderMemo = '卡面付就诊卡充值';
	data.ChannelId = Commonjs.getClientId();
	data.ServiceId = Commonjs.constant.serviceId_006; //订单类型
	data.IsOnlinePay = '1'; //是否线上支付订单 1是2否; //是否线上支付订单 1是2否
	if(!Commonjs.isEmpty( $("#hisMemberId").val())){
		data.HisMemberId = $("#hisMemberId").val();
	}
	if(!Commonjs.isEmpty( $("#memberName").val())){
		data.MemberName = $("#memberName").val();
	}
	param.apiParam = Commonjs.getApiReqParams(data);
	Commonjs.ajax('../../wsgw/order/AddOrderLocal/callApi.do', param, function(jsons) {
		if(!Commonjs.isEmpty(jsons.RespCode)) {
			if(jsons.RespCode == '10000') {
				if(!Commonjs.isEmpty(jsons.Data)){
					window.location.href = Commonjs.goToUrl("/business/pay/payment.html?orderId="+jsons.Data[0].OrderId+"&toUrl=%2Fbusiness%2Fpay%2FpaySuccess.html%3FshowClose%3Dtrue%26ServiceId%3D006");
				}else{
					myLayer.alert("返回数据为空");
				}
			} else {
				$("body").load("/business/pay/payError.html"); //页面跳转
			}
		} else {
			myLayer.alert("返回码为空");
		}
	},{async:false});
});
