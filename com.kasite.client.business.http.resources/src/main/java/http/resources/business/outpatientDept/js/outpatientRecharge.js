var memberName;
var opMinPayMoney;
var opMaxPayMoney;
var memberId;
$(function() {
	memberName = Commonjs.getUrlParam("name");
	memberId = Commonjs.getUrlParam("memberId");
	if( Commonjs.isEmpty(memberId)){
		myLayer.alert('用户ID不能为空，请重新发起充值。');
		return;
	}
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

	queryPayConfig();

	//去充值
	$('#chargeBtn').on('click', function() {
		var disable = $(this).hasClass('disable');
		if(disable) {
			myLayer.alert('请输入充值金额');
			return;
		}

		if(Number($('#chargeNum').html()) > Number(Commonjs.centToYuan(opMaxPayMoney))) {
			$('#chargeNum').html(Commonjs.centToYuan(opMaxPayMoney));
			$('#chargeInput').val(Commonjs.centToYuan(opMaxPayMoney));
			myLayer.alert('充值金额不能大于' + Commonjs.centToYuan(opMaxPayMoney));
			return;
		}
		if(Number($('#chargeNum').html())<Number(Commonjs.centToYuan(opMinPayMoney))){
					$('#chargeNum').html(Commonjs.centToYuan(opMinPayMoney));
					$('#chargeInput').val(Commonjs.centToYuan(opMinPayMoney));
					myLayer.alert('充值金额不能少于'+Commonjs.centToYuan(opMinPayMoney));
					return;
				}

		var param = {};
		param.api = "AddOrderLocal";
		var data = {};
		data.PayMoney = Commonjs.accMul($('#chargeNum').html(), "100"); //患者应付金额(单位：元)
		data.TotalMoney = Commonjs.accMul($('#chargeNum').html(), "100"); //缴费单应收金额(单位：元)
		data.OperatorId = Commonjs.getOpenId();
		data.HosId = Commonjs.getUrlParam('HosId');
		data.EqptType = 1;
		data.PriceName = '门诊充值';
		data.CardNo = Commonjs.getUrlParam("cardNo");
		data.CardType = Commonjs.getUrlParam("cardType");
		data.OperatorName = memberName;
		data.OrderMemo = '门诊充值';
		data.ChannelId = Commonjs.getClientId();
		data.ServiceId = Commonjs.constant.serviceId_006; //订单类型
		data.IsOnlinePay = '1'; //是否线上支付订单 1是2否
		data.MemberId = memberId ; 
		param.apiParam = Commonjs.getApiReqParams(data);
		Commonjs.ajax('../../wsgw/order/AddOrderLocal/callApi.do', param, function(jsons) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == '10000') {
					if(!Commonjs.isEmpty(jsons.Data)){
						window.location.href = Commonjs.goToUrl("/business/pay/payment.html?orderId=" + jsons.Data[0].OrderId + "&toUrl=/business/pay/paySuccess.html?ServiceId="+Commonjs.constant.serviceId_006);
					}else{
						myLayer.alert("返回数据为空");
					}
				} else {
					$("body").load("/business/pay/payError.html"); //页面跳转
				}
			} else {
				myLayer.alert("返回码为空");
			}
		});
	});
});

function queryPayConfig() {
	var param = {};
	var data = {};
	param.apiParam = Commonjs.getApiReqParams(data);
	Commonjs.ajax('/wsgw/pay/QueryFrontPayLimit/callApi.do', param, function(jsons) {
				if(jsons.RespCode==10000 && !Commonjs.isEmpty(jsons.Data)){
					opMaxPayMoney=jsons.Data[0].OpMaxPayMoney;
					opMinPayMoney=jsons.Data[0].OpMinPayMoney;
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
/**
 * 跳转到门诊充值记录
 */
function gotoOutpatientPayRecordList(){
	
	window.location.href = Commonjs.goToUrl("/business/outpatientDept/outpatientPayRecordListHis.html?queryServiceId=006");
	 
	 
}