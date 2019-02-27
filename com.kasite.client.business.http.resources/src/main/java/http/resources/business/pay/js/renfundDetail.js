$(function() {
	var orderId = Commonjs.getUrlParam("orderId");
	var refundPrice = Commonjs.getUrlParam("refundPrice");
	var serviceId = Commonjs.getUrlParam("serviceId");
	$("#refundFee").html(Commonjs.centToYuan(refundPrice));
	if(serviceId==0){
		//已支付的退号
		$("#info1").html("【预约挂号】申请退费");
	}
	if(serviceId=='008'){
		//已支付的处方订单
		$("#info1").html("【诊间处方】申请退费");
	}
	getOrderProcess(orderId,serviceId);
});

//获取订单的状态信息
function getOrderProcess(orderId,serviceId) {
	var apiData = {};
	var param = {};
	apiData.OrderId = orderId;
	param.apiParam = Commonjs.getApiReqParams(apiData, null, 6003);
	Commonjs.ajax('/wsgw/order/QueryOrderProcess/callApi.do?t=' + new Date().getTime(), param,function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(jsons.RespCode == 10000) {
				if(Commonjs.ListIsNotNull(jsons.Data)) {
					Commonjs.BaseForeach(jsons.Data, function(i, item) {
						if( item.Type=='REFUND'){
							$("#refundFee").html(Commonjs.centToYuan(item.RefundPrice));
							if(item.State == 3 ){
								$("#time1").html(item.BeginDate);
								$("#li1").addClass("finish")
								$("#time2").html(item.BeginDate);
								$("#li2").addClass("finish");
							}
							if(item.State == 4 ){
								$("#time1").html(item.BeginDate);
								$("#li1").addClass("finish")
								$("#time2").html(item.BeginDate);
								$("#li2").addClass("finish");
								$("#time3").html(item.EndDate);
								$("#li3").addClass("finish");
							}
							if(item.State == 5 ){
								$("#time1").html(item.BeginDate);
								$("#li1").addClass("finish")
								$("#time2").html(item.BeginDate);
								$("#li2").addClass("finish");
								$("#refundResult").html('退费失败');
								$("#time3").html(item.EndDate);
								$("#li3").addClass("finish");
							}
						}
					});
				}else{
					myLayer.alert(jsons.RespMessage, 3000);
				}
			} else {
				myLayer.alert(jsons.RespMessage, 3000);
			}
		}
	},{async:false});

}