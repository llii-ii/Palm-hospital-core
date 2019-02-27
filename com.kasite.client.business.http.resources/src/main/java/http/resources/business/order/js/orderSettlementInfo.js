//处方信息对象
var settlementInfo = null;
var memberId = null;
$(function(){
	var hisOrderId = Commonjs.getUrlParam("hisOrderId");
	memberId = Commonjs.getUrlParam("memberId");
	queryOrderSettlementInfo(hisOrderId,memberId);
	//结算按钮
	$("#addBtn").click(function(){
		myLayer.confirm({
			title:'',
			con:'是否进行结算?',
			cancel: function(){
			},
			cancelValue:'\u53d6\u6d88',
			ok: function(){
				settleOrderSettlement();
			},
			okValue:'\u786e\u5b9a'
		});
	});
	
});
/**
 * 查询订单明细
 */
function queryOrderSettlementInfo(hisOrderId,memberId) {
	var apiData = {};
	var param = {};
	apiData.HisOrderId = hisOrderId;
	apiData.MemberId = memberId;
	//apiData.PrescNo = prescNo;
	param.api = 'QueryOrderSettlementInfo';
	param.apiParam = Commonjs.getApiReqParams(apiData,"","");
	Commonjs.ajax('/wsgw/order/QueryOrderSettlementInfo/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	settlementInfo = jsons.Data[0];
                	$("#menberName").html(settlementInfo.MemberName);
                	memberName = settlementInfo.MemberName;
                	$("#cardNo").html(settlementInfo.CardNo);
                	$("#doctorName").html(settlementInfo.DoctorName);
                	$("#prescNo").html(settlementInfo.PrescNo);
                	$("#presc").html(settlementInfo.PrescType);
                	$("#orderType").html(settlementInfo.PrescType);
                	$("#prescFee").html(Commonjs.centToYuan(settlementInfo.Price));
                	$("#price").html(Commonjs.centToYuan(settlementInfo.Price));
                	if(settlementInfo.IsSettlement == 1){
                		$("#orderState").html("已结算");
                	}else if(settlementInfo.IsSettlement == 0){
                		$("#addBtn").show();
                		$("#orderState").html("未结算");
                	}else{
                		$("#orderState").html("其他");
                	}
                	var listHtml = "";
                	Commonjs.BaseForeach(settlementInfo.Data_1, function (i, item) {
                		listHtml += '<tr>';
                     	listHtml += 	'<td>'+item.Project+'</td>';
                     	listHtml +=  	'<td>'+item.Specifications+'</td>';
                     	listHtml += 	'<td>'+item.Number+'</td>';
                     	listHtml += 	'<td>'+Commonjs.centToYuan(item.UnitPrice)+'</td>';
                     	listHtml += 	'<td>'+Commonjs.centToYuan(item.SumOfMoney)+'</td>';
                     	listHtml +=  '</tr>';
                	 });
                	$("#content").html(listHtml);
                } else {
                	myLayer.alert("暂无可用号源",3000);
                }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		
    });
}

function settleOrderSettlement(){
	var apiData = {};
	var param = {};
	apiData.MemberId= memberId;
	apiData.CardNo=settlementInfo.CardNo;
	apiData.CardType=settlementInfo.CardType;
	apiData.TotalPrice = settlementInfo.Price;
	apiData.HisOrderIds = settlementInfo.HisOrderId;
	apiData.PrescNos = settlementInfo.PrescNo;
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax("/wsgw/order/SettleOrderSettlement/callApi.do?t=" + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
            	location.href = Commonjs.goToUrl('/business/order/settlementSuccess.html)');
            }else if(jsons.RespCode == -63011) { //钱不够
            	myLayer.alert(jsons.RespMessage,3000);
            	//跳去充值界面
            	location.href = Commonjs.goToUrl("/business/outpatientDept/outpatientRecharge.html?&cardNo=" + settlementInfo.CardNo + "&cardType=" +settlementInfo.CardType);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    },{
    	error:function(xhr, textStatus, errorThrown){
    		myLayer.alert('网络繁忙,请刷新后重试',3000);
    	}
    });
}