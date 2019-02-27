var refundableBalance = null;
$(function() {
	var cardNo = Commonjs.getUrlParam('cardNo');
	var cardType = Commonjs.getUrlParam('cardType');
	var name = Commonjs.getUrlParam('name');
	var memberId = Commonjs.getUrlParam('memberId');
	var hisMemberId = Commonjs.getUrlParam('hisMemberId');
	$('.c-main').on('click','.ri-title',function(){
		$(this).find('.ui-arrow').toggleClass('ahide');
		$(this).siblings().slideToggle(200);
	});
	
	$("#memberName").html(name);
	$("#cardNo").html(cardNo);
	
	//查询可退余额
	queryOutpatientCardInfo(cardNo,cardType,hisMemberId);
	selfRefundTimeLimit();
	
	
	
	$("#applyRefund").on('click',function(){
		applyRefund(cardNo,cardType,hisMemberId,memberId);
	});
	
	//退费记录
	$("#selfReundRecordBtn").on('click',function(){
		location.href = Commonjs.goToUrl("selfServiceRefundRecordList.html?cardNo="+cardNo
				+"&cardType="+cardType+"&hisUserId="+hisMemberId+"&name="+name+"&memberId="+memberId);
	});
	
});

/**
 * 查询用户信息
 * 
 * @param member
 */
function queryOutpatientCardInfo(cardNo,cardType,hisMemberId) {
	if (Commonjs.isEmpty(cardNo)) {
		//myLayer.alert('住院号不能为空');
		return;
	}
	var apiData = {};
	apiData.CardNo = cardNo;
	apiData.CardType =cardType;
	if (!Commonjs.isEmpty(hisMemberId)) {
		apiData.HisMemberId =hisMemberId;
	}
	var page = {};
	page.PIndex = 0;
	page.PSize = 1;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/smartPay/QueryMemberRefundableMoney/callApi.do', param,function(jsons) {
		if (!Commonjs.isEmpty(jsons)) {
			if (!Commonjs.isEmpty(jsons.RespCode)) {
				if (jsons.RespCode == 10000) {
					if (Commonjs.ListIsNull(jsons.Data)) {
						var data = jsons.Data[0];
						$("#balance").html(Commonjs.centToYuan(data.Balance));
						$("#refundableBalance").html(Commonjs.centToYuan(data.RefundableBalance));
						refundableBalance=data.RefundableBalance;
						
						divApplyRefund
					}
					//执行完把弹出层clear掉
					myLayer.clear();
				} else {
					myLayer.clear();
					myLayer.alert(jsons.RespMessage, 3000);
				}
			} else {
				myLayer.alert("返回码为空");
			}
		} else {
			myLayer.clear();
			//通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	});
}

function applyRefund(cardNo,cardType,hisMemberId,memberId){
	if (Commonjs.isEmpty(cardNo)) {
		//myLayer.alert('住院号不能为空');
		return;
	}
	if(refundableBalance<=0){
		myLayer.alert('可退金额必须大于0才能申请退款','2000');
		return;
	}
	var apiData = {};
	apiData.CardNo = cardNo;
	apiData.CardType =cardType;
	if (!Commonjs.isEmpty(hisMemberId)) {
		apiData.HisMemberId =hisMemberId;
	}
	if (!Commonjs.isEmpty(memberId)) {
		apiData.MemberId =memberId;
	}
	apiData.RefundableBalance = refundableBalance;
	var page = {};
	page.PIndex = 0;
	page.PSize = 1;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/smartPay/ApplySelfServiceRefund/callApi.do', param,function(jsons) {
		if (!Commonjs.isEmpty(jsons)) {
			if (!Commonjs.isEmpty(jsons.RespCode)) {
				if (jsons.RespCode == 10000) {
					location.href = Commonjs.goToUrl("selfServiceRefundApplySuccess.html");
				} else {
					myLayer.alert(jsons.RespMessage, 3000);
				}
			} else {
				myLayer.alert("返回码为空");
			}
		} else {
			myLayer.clear();
			//通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	});
}

function selfRefundTimeLimit(){
	//自助退费时间限制
	var selfRefundTimeLimit = Commonjs.getDiyConfig().selfRefundTimeLimit;
	if( Commonjs.isEmpty(selfRefundTimeLimit) ){//如果没有自定义配置自助退款时间,则设置默认值
		selfRefundTimeLimit =  "07:00~22:00"
	}
	//自助退费说明
	var selfRefundExplain = Commonjs.getDiyConfig().selfRefundExplain;
	if( Commonjs.isEmpty(selfRefundExplain) ){//如果没有自定义配置说明
		$("#refundDesc").html("用户自主退款时间："+selfRefundTimeLimit);
	}else{
		$("#refundDesc").html(selfRefundExplain);
	}
	
	var beginTime = selfRefundTimeLimit.split("~")[0];
	var endTime = selfRefundTimeLimit.split("~")[1];
	var beginDate = new Date();
	beginDate.setHours(beginTime.split(":")[0]);
	beginDate.setMinutes(beginTime.split(":")[1]);
	
	var endDate = new Date();
	endDate.setHours(endTime.split(":")[0]);
	endDate.setMinutes(endTime.split(":")[1]);
	
	var nowDateTimes = new Date().getTime();
	if( nowDateTimes < endDate.getTime() && nowDateTimes>beginDate.getTime()){
		$("#divApplyRefund").show();
	}
	
}
