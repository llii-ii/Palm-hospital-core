var orderId = null;
$(function(){
	orderId = Commonjs.getUrlParam("orderId");
	queryLocalOrder(orderId);
	querySubOrderList(orderId);
});

function queryLocalOrder(orderId){
	var apiData = {};
  	apiData.OrderId=orderId;
  	var param = {};
  	param.apiParam =Commonjs.getApiReqParams(apiData);
  	Commonjs.ajax('/wsgw/order/OrderDetailLocal/callApi.do',param,function(jsons){
  		if(!Commonjs.isEmpty(jsons)){
  			if(!Commonjs.isEmpty(jsons.RespCode)){
	  			if(jsons.RespCode==10000){
	  				if(Commonjs.ListIsNotNull(jsons.Data)){
	  					var data = jsons.Data[0];
	  					$("#orderNum").html(data.OrderNum);
	  					$("#serviceName").html(getServiceTyep(data.ServiceId))
	  					$("#beginDate").html(data.BeginDate);
	  					$("#payMoney").html("￥"+Commonjs.centToYuan(data.PayMoney));
	  					$("#orderState").html();
	  					if( data.PayState == 0 ){
	  						$("#orderState").html("未支付");
	  					}
	  					if(data.PayState == 1){
    	              		$("#orderState").html("支付中");
    	              	}
                		if(data.PayState == 2){
    	              		$("#orderState").html("已支付,结算中");
    	              		if( data.BizState == 1 ){
    	              			$("#orderState").html("已支付,已结算");
    	              		}
    	              	}
                		if(data.PayState == 3){
    	              		$("#orderState").html("退费中");
    	              	}
                		if(data.PayState == 4){
    	              		$("#orderState").html("已退费");
    	              	}
                		if(data.OverState == 5){
    	              		$("#orderState").html("已取消");
    	              	}
	  				}
	  			}
	  		}
  		}
    });
}

function querySubOrderList(orderId){
	
	var listHtml = '';
	var apiData = {};
	var page = {};
  	apiData.OrderId = orderId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData,page,"");
	$("#orderList").html("");
	Commonjs.ajax('/wsgw/order/QueryOrderSubList/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
            	 if (Commonjs.ListIsNotNull(jsons.Data)) {
            		 Commonjs.BaseForeach(jsons.Data, function (i, data) {
            			 listHtml += '<li>';
        				 listHtml += '<a href="javascript:void(0)">';
        				 listHtml += '<h4><span class="fr c-ffac0b">￥'+Commonjs.centToYuan(data.Price);
        				 listHtml += '</span>'+data.PriceName+'</h4>';
        				 listHtml += '<div class="ui-grid ol-mess"  >';
        				 listHtml += '<table class="charges-detail"><thead>'
        				 listHtml += '<tr><td width="20%">项目</td><td width="20%">规格</td><td width="20%">数量</td><td width="20%">单价(元）</td>';	
        				 listHtml += '<td width="20%">金额(元）</td></tr></thead>';	 	
        				 listHtml += '<tbody>'; 
        				 if(Commonjs.ListIsNotNull(data.Data_1)  ){
        					 Commonjs.BaseForeach(data.Data_1, function (i, item) {
                           		listHtml += '<tr>';
                                	listHtml += 	'<td>'+item.Project+'</td>';
                                	listHtml +=  	'<td>'+item.Specifications+'</td>';
                                	listHtml += 	'<td>'+item.Number+'</td>';
                                	listHtml += 	'<td>'+Commonjs.centToYuan(item.UnitPrice)+'</td>';
                                	listHtml += 	'<td>'+Commonjs.centToYuan(item.SumOfMoney)+'</td>';
                                	listHtml +=  '</tr>';
                           	 });  
        				 }else{
        					 listHtml += '<tr><td colspan="5">无</td></tr>';
        				 }
                     	 
        				 listHtml += '</tbody></table>';         	
        				 listHtml += '</div>';
        				 listHtml += '</a>';
        				 listHtml += ' </li>';
            		 });
            	 }
            } else {
            	//$("#loading").hide();
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	//$("#loading").hide();
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		$("#orderList").append(listHtml);
		
    });
}


function getServiceTyep(service){
	if(service == '0'){
			//挂号订单
			return '挂号';
		}else if(service == '008'){
			//医嘱订单
			return '医嘱';			
		}else if(service == '007'){
			//医嘱订单
			return '预交金充值';			
		}else if(service == '011'){
			return '单据';			
		}else{
			return '其他';
		}
}

