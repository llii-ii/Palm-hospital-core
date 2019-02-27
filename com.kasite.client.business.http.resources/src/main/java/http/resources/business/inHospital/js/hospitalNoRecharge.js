var memberName;
var memberId;
var ihMinPayMoney;
var ihMaxPayMoney;
$(function() {
	memberName = Commonjs.getUrlParam("name");
	memberId = Commonjs.getUrlParam("memberId");
	//$("#recordList").attr("href",appConfig.rechargeConfig.hRechargeRecordUrl);
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

	queryPayConfig();//充值配置
	//去充值
	$('#chargeBtn').on('click', function() {
		
		var disable = $(this).hasClass('disable');
		if(disable) {
			myLayer.alert('请输入充值金额');
			return;
		}

		if(Commonjs.isEmpty(Commonjs.getUrlParam("hospitalNo"))){
			myLayer.alert("住院号不能为空");
			return;
		}	
	
		if(Number($('#chargeNum').html())>Number(Commonjs.centToYuan(ihMaxPayMoney))){
			$('#chargeNum').html(Commonjs.centToYuan(ihMaxPayMoney));
			$('#chargeInput').val(Commonjs.centToYuan(ihMaxPayMoney));
			myLayer.alert('充值金额不能大于'+Commonjs.centToYuan(ihMaxPayMoney));
			return;
		}
		
		if(Number($('#chargeNum').html())<Number(Commonjs.centToYuan(ihMinPayMoney))){
			$('#chargeNum').html(Commonjs.centToYuan(ihMinPayMoney));
			$('#chargeInput').val(Commonjs.centToYuan(ihMinPayMoney));
			myLayer.alert('充值金额不能少于'+Commonjs.centToYuan(ihMinPayMoney));
			return;
		}
	
		var param = {};
		param.api = "AddOrderLocal";
		var data = {};
		data.PayMoney = Commonjs.accMul($('#chargeNum').html(), "100"); //患者应付金额(单位：分)
		data.TotalMoney = Commonjs.accMul($('#chargeNum').html(), "100"); //缴费单应收金额(单位：分)
		data.HosId = Commonjs.getUrlParam('HosId');
		data.OperatorId = Commonjs.getOpenId();
		data.EqptType = 1;
		data.PriceName = '住院预交金充值';
		data.CardNo = Commonjs.getUrlParam("hospitalNo");
		data.CardType = Commonjs.constant.cardType_14;
		data.OperatorName = memberName;
		data.MemberId = memberId;
		data.OrderMemo = '住院预交金充值';
		data.ChannelId = Commonjs.getClientId();
		data.ServiceId = Commonjs.constant.serviceId_007; //订单类型
		data.IsOnlinePay = '1'; //是否线上支付订单 1是2否
		param.apiParam = Commonjs.getApiReqParams(data);
		Commonjs.ajax('../../wsgw/order/AddOrderLocal/callApi.do', param, function(jsons) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == '10000') {
					if(!Commonjs.isEmpty(jsons.Data)){
						var url =  "/business/pay/payment.html?orderId="+jsons.Data[0].OrderId+"&toUrl=/business/pay/paySuccess.html";
						window.location.href = Commonjs.goToUrl(url);;
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
});

function queryPayConfig() {
	var param = {};
	var data = {};
	param.apiParam = Commonjs.getApiReqParams(data);
	Commonjs.ajax('/wsgw/pay/QueryFrontPayLimit/callApi.do', param,
			function(jsons) {
				if(jsons.RespCode==10000 && !Commonjs.isEmpty(jsons.Data)){
					ihMaxPayMoney=jsons.Data[0].IhMaxPayMoney;
					ihMinPayMoney=jsons.Data[0].IhMinPayMoney;
				}
			},{async:false});
}

$('.c-main').on('click', '.charge-box li', function() {
	if(!$(this).hasClass('charge-input')) {
		$(this).addClass('curr').siblings().removeClass('curr');
		$('.charge-text').val('');
		$('#chargeNum').html($(this).find('label').html());
		$('#chargeBtn').removeClass('disable');
	}
});