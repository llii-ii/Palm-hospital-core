var memberPickerDefaultlValue;
var selectedMember;
$(function() {
	var stateName = Commonjs.getUrlParam("stateName");
	var selfRefundRecordId = Commonjs.getUrlParam("selfRefundRecordId");
	var refundAmount = Commonjs.getUrlParam("refundAmount");
	var refundCount = Commonjs.getUrlParam("refundCount");
	$("#stateName").html(stateName)
	$("#refundAmount").html("￥"+Commonjs.centToYuan(refundAmount)+"  共"+refundCount+"笔");
	querySelfRefundRecordInfo(selfRefundRecordId);
});

function querySelfRefundRecordInfo(selfRefundRecordId){
	var listHtml = '';
	var apiData = {};
  	apiData.SelfRefundRecordId = selfRefundRecordId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	$("#orderList").html('');
	$("#noData").hide();
	$("#loading").show();
	Commonjs.ajax('/wsgw/smartPay/QuerySelfRefundRecordInfo/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
            	 if (Commonjs.ListIsNotNull(jsons.Data)) {
            		 Commonjs.BaseForeach(jsons.Data, function(i, item) { 
            			 	listHtml += "<li class='c-list-a'>";
							listHtml += "<div class='pm-title'><span class='fr c-ffac0b' style='padding-right:.75rem'>"+getPayStateDesc(item.PayState)+"</span>交易单号:"+item.OrderNum+"</div><ul class='visit-mess c-f14'>";
							listHtml += "<li><div class='vm-key2 c-666'>退款方式：</div><div class='vm-info'><span class='fr c-ffac0b' style='padding-right:.75rem'>￥"+ Commonjs.centToYuan(item.RefundPrice)+"</span>"+item.ChannelType+"</div>";
							listHtml += "<li><div class='vm-key2 c-666'>退款原因：</div><div class='vm-info'><span >"+item.RefundRemark+"</span></div></li>";
							listHtml += "<li> <div class='vm-key2 c-666'>退款发起时间：</div><div class='vm-info'><span>"+ifNullDesc(item.BeginDate)+"</span></div></li>";
							listHtml += "<li><div class='vm-key2 c-666'>退款到账时间：</div><div class='vm-info'>"+ifNullDesc(item.EndDate)+"</div> </li>";
							if( item.PayState != 4 &&  item.PayState !=3 ){
								listHtml += "<li><div class='vm-key2 c-666'>失败原因：</div><div class='vm-info'>"+item.FailReason+"</div> </li>";
							}
							listHtml +=	"</ul></li>";
						});
            		 $("#noData").hide();
            	 }else{
            		 $("#noData").show();
            	 }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	//$("#loading").hide();
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		$("#loading").hide();
		$("#orderList").append(listHtml);
    });
}

function getPayStateDesc(state){
	var str = '未知';
	if( state == 3 ){
		str = '退费中';
	}else if( state == 4 ){
		str = '退费成功';
	}else if( state == 7 ){
		str = '退费失败';
	}
	return str;
}

function ifNullDesc(val){
	if (Commonjs.isEmpty(val)) {
		return "无";
	}else{
		return  val;
	}
}