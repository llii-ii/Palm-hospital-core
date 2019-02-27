//全流程订单对象
var localOrderObj = null;
//his订单对象，新增订单使用
var hisOrderObj = null;
var itemsUrl ="";
var hisOrderId = null;
var prescNo = null;
var memberId = null;
$(function(){
	hisOrderId = Commonjs.getUrlParam("hisOrderId");
	prescNo =  Commonjs.getUrlParam("prescNo");
	memberId =  Commonjs.getUrlParam("memberId");
	queryOrderPrescriptionInfo(hisOrderId,prescNo);
});

/**
 * 查询HIS处方订单明细
 */
function queryOrderPrescriptionInfo(hisOrderId,prescNo) {
	var apiData = {};
	var param = {};
	apiData.HisOrderId = hisOrderId;
	apiData.PrescNo = prescNo; 
	apiData.MemberId = memberId;
	apiData.HosId = Commonjs.getUrlParam("HosId");
	param.apiParam = Commonjs.getApiReqParams(apiData,"","");
	Commonjs.ajax('/wsgw/order/QueryOrderPrescriptionInfo/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	hisOrderObj = jsons.Data[0];
                	var orderNum = hisOrderObj.OrderNum;
                	//如果订单号存在则显示在页面上。
                	if(Commonjs.isNotNull(orderNum)){
                		$("#orderNum").show();
                    	$("#orderNum").html("("+orderNum+")");
                	}
                	$("#menberName").html(hisOrderObj.MemberName);
                	$("#cardNo").html(hisOrderObj.CardNo);
                	$("#doctorName").html(hisOrderObj.DoctorName);
                	$("#prescNo").html(hisOrderObj.PrescNo);
                	$("#presc").html(hisOrderObj.PriceName);
                	$("#prescFee").html("￥"+Commonjs.centToYuan(hisOrderObj.PayMoney));
                	$("#price").html(Commonjs.centToYuan(hisOrderObj.PayMoney));
                	$("#serviceName").html(hisOrderObj.ServiceName);
                	var listHtml = "";
                	Commonjs.BaseForeach(hisOrderObj.Data_1, function (i, item) {
                		listHtml += '<tr>';
                     	listHtml += 	'<td>'+item.Project+'</td>';
                     	listHtml += 	'<td>'+item.Number+'</td>';
                     	listHtml += 	'<td>'+Commonjs.centToYuan(item.UnitPrice)+'</td>';
                     	listHtml +=  	'<td>'+item.Specifications+'</td>';
                     	listHtml += 	'<td>'+Commonjs.centToYuan(item.SumOfMoney)+'</td>';
                     	listHtml +=  '</tr>';
                	 });
                	$("#content").html(listHtml);
                	var hisOrderState = hisOrderObj.OrderState;
                	//先查询本地看是否创建过订单
                	queryLocalOrder(hisOrderObj.HisOrderId);
                	if( localOrderObj == null || localOrderObj.PayState == 0 ){
                		//如果全流程订单未存在（第一次进此界面，全流程未新增订单）||未支付，则以HIS订单的状态为准。
                		if(hisOrderState == 0){	
                			$("#payBtn").show();
                    		$("#orderState").html("待支付");
                		}else if(hisOrderState == 1){
    	              		$("#orderState").html("支付中(线下)");
    	              	}else if(hisOrderState == 2){
    	              		$("#orderState").html("已支付(线下)");
    	              	}else if(hisOrderState == 3){
    	              		$("#orderState").html("退费中(线下)");
    	              	}else if(hisOrderState == 4){
    	              		$("#orderState").html("已退费(线下)");
    	              	}else if(hisOrderState == 5){
    	              		$("#orderState").html("已完成");
    	              	}else if(hisOrderState == 6){
    	              		$("#orderState").html("已失效");
    	              	}else{
    	              		$("#orderState").html("其他");
    	              	}
                	}else{//如果全流程订单已经有支付状态，说明进行过充值，退费等操作
                		if(localOrderObj.PayState == 1){//如果正在支付，则直接显示正在支付。
    	              		$("#orderState").html("支付中");
    	              	}
                		if(localOrderObj.PayState == 2){//如果已支付
    	              		$("#orderState").html("已支付,结算中");
    	              		if( hisOrderState == 2 ){
    	              			$("#orderState").html("已支付,已结算");
    	              		}
    	              		if( hisOrderState == 5 ){
    	              			$("#orderState").html("已完成");
    	              			
    	              		}
    	              		if( hisOrderState == 4 || hisOrderState == 3){
    	              			//线上支付成功，但是HIS却退费了
    	              			$("#orderState").html("已退费(线下)");
    	              		}
    	              		if( hisOrderState == 6 ){
    	              			$("#orderState").html("已失效");
    	              		}
    	              	}
                		if(localOrderObj.PayState == 3){//如果退费中，则直接显示退费中。
    	              		$("#orderState").html("退费中");
    	              		$("#refundHref").attr('href','/business/pay/refundDetail.html?orderId='+localOrderObj.OrderId
    	              				+"&refundPrice="+localOrderObj.PayMoney+"&serviceId="+localOrderObj.ServiceId);
    	              	}
                		if(localOrderObj.PayState == 4){//如果已退费
    	              		$("#orderState").html("已退费");
    	              		$("#refundHref").attr('href','/business/pay/refundDetail.html?orderId='+localOrderObj.OrderId
    	              				+"&refundPrice="+localOrderObj.PayMoney+"&serviceId="+localOrderObj.ServiceId);
    	              		if(hisOrderState == 6){
    	    	              $("#orderState").html("已失效");
    	    	            }
    	              	}
                	}
                	
                } else {
                	myLayer.alert("查无数据",3000);
                }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    });
}

function payNow(){
	myLayer.confirm({
		title:'',
		con:'是否立即去支付?',
		cancel: function(){
		},
		cancelValue:'\u53d6\u6d88',
		ok: function(){
			var orderId = null;
			if(localOrderObj == null){
				//全流程订单为空先创建处方订单
				orderId = addOrderPrescription();
			}else{
				//如果已经新增了全流程订单
				//判断金额是否一致
				if( hisOrderObj.PayMoney == localOrderObj.PayMoney){
					orderId = localOrderObj.OrderId;
				}else{
					//金额不一致，说明HIS对处方的金额做了修改，重新新增全流程订单，并将旧订单作废。
					orderId = addOrderPrescription(localOrderObj.OrderId);
				}
			}
			var redictUrl="/business/order/settlementSuccess.html?orderId=" + orderId;
			if( !Commonjs.isEmpty(hisOrderId) ){
				redictUrl+="&hisOrderId="+hisOrderId;
			}
			if( !Commonjs.isEmpty(prescNo) ){
				redictUrl+="&prescNo="+prescNo;
			}
			redictUrl+="&isPay="+1;
			redictUrl = encodeURIComponent(redictUrl);
			window.location.href = Commonjs.goToUrl("/business/pay/payment.html?orderId=" + orderId +"&toUrl="+redictUrl);
		},
		okValue:'\u786e\u5b9a'
	});
}

//支付
function addOrderPrescription(oldOrderId) {
    myLayer.load('正在加载');
    var apiData = {};
	var param = {};
	apiData.HosId=Commonjs.hospitalId();
	apiData.HisOrderId=hisOrderObj.HisOrderId;
	apiData.PrescNo=hisOrderObj.PrescNo;//his处方订单号
	apiData.PayMoney=hisOrderObj.PayMoney;
	apiData.TotalMoney=hisOrderObj.TotalMoney;
	apiData.OperatorId=Commonjs.getSession().sign; 
	apiData.OperatorName=hisOrderObj.MemberName; 
	apiData.MemberId = memberId;
	apiData.EqptType="1";//设备类型
	if( !Commonjs.isEmpty(oldOrderId) ){
		//金额修改过后，作废的旧订单
		apiData.OldOrderId=oldOrderId; 
	}
	param.apiParam = Commonjs.getApiReqParams(apiData,"",6001);
	var orderId = null;
	Commonjs.ajax("/wsgw/order/AddOrderPrescription/callApi.do?t=" + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
            	orderId = jsons.Data[0].OrderId;
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    },{async:false});
	return orderId;
}

function queryLocalOrder(hisOrderId){
	var apiData = {};
  	apiData.PrescNo=hisOrderId;
  	//查询有效的处方订单
	apiData.OverState=0;
  	var param = {};
  	param.apiParam =Commonjs.getApiReqParams(apiData);
  	Commonjs.ajax('/wsgw/order/OrderDetailLocal/callApi.do',param,function(jsons){
  		if(!Commonjs.isEmpty(jsons)){
  			if(!Commonjs.isEmpty(jsons.RespCode)){
	  			if(jsons.RespCode==10000){
	  				if(Commonjs.ListIsNotNull(jsons.Data)){
	  					localOrderObj = jsons.Data[0];
	  				}
	  			}
	  		}
  		}
    },{async:false});
  	return localOrderObj;
}
