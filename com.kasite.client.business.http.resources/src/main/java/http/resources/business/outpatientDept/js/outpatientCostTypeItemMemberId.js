$(function(){
	var expenseTypeCode = Commonjs.getUrlParam("expenseTypeCode");
	var date = Commonjs.getUrlParam("date");
	var cardNo =  Commonjs.getUrlParam("cardNo");
	var memberId =  Commonjs.getUrlParam("memberId");
	queryOutpatientCostTypeItem(expenseTypeCode,date,memberId);
});


/**
 * 查询订单明细
 */
function queryOutpatientCostTypeItem(expenseTypeCode,date,memberId) {
	$(".nomess").hide();
	var apiData = {};
	var param = {};
	$("#content").html();
	var listHtml = "";
	 if(!Commonjs.isEmpty(expenseTypeCode)){
		apiData.ExpenseTypeCode=expenseTypeCode;
		apiData.Date=date;
		apiData.MemberId=memberId;
		apiData.CardType=Commonjs.constant.cardType_1;
 	}else{
		myLayer.alert("传入参数为空!",3000);
		return;
 	}
	param.apiParam = Commonjs.getApiReqParams(apiData,"","");
	Commonjs.ajax('../../wsgw/order/QueryOutpatientCostTypeItem/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	 Commonjs.BaseForeach(jsons.Data, function (i, item) {
                		listHtml += '<tr>';
                     	listHtml += 	'<td>'+item.Project+'</td>';
                     	listHtml +=  	'<td>'+item.Specifications+'</td>';
                     	var num = item.Number;//数量
                     	var unit = item.Unit;//单位
                     	var ht = num;
                     	if(Commonjs.isNotNull(unit)){
                     		ht = num + "("+unit+")";
                     	}
                     	listHtml += 	'<td>'+ ht +'</td>';
                     	listHtml += 	'<td>'+Commonjs.centToYuan(item.UnitPrice)+'</td>';
                     	listHtml += 	'<td>'+Commonjs.centToYuan(item.SumOfMoney)+'</td>';
                     	listHtml +=  '</tr>';
                	 });
                } else {
                	$(".nomess").show();
                }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		$("#content").html(listHtml);
    });
}
