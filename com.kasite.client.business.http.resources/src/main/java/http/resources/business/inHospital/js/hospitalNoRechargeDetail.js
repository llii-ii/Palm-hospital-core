var memberName;
var cardNo ;
var cardType;
var isSettlement = 0;
var orderId;
var price;
var isContinue = false;
var orderMemo = '';
var priceName = '';
$(function(){
	orderId = Commonjs.getUrlParam("orderId");
	queryOrderInfo();
});
/**
 * 查询订单明细
 */
function queryOrderInfo() {
  	$("#addBtn").hide();
	var apiData = {};
	var param = {};
  	var page = {};
	apiData.OrderId = orderId;
	param.api = 'OrderListLocal';
	param.apiParam = Commonjs.getApiReqParams(apiData,page);
	Commonjs.ajax('../../wsgw/order/OrderListLocal/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
            	Commonjs.BaseForeach(jsons.Data, function(i,item){
//                	console.log(jsons.Data);
                	$("#orderNum").html(item.OrderNum);
                	$("#menberName").html(item.OperatorName);
                	memberName = item.OperatorName;
                	$("#cardNo").html(item.CardNo);
                	cardNo = item.CardNo;
                	cardType = item.CardType;
                	
                	
                	$("#prescFee").html(Commonjs.centToYuan(item.PayMoney)+'元');
                	$("#price").html(Commonjs.centToYuan(item.PayMoney));
                	price = item.PayMoney;
                	orderMemo = item.OrderMemo;
                	priceName = item.PriceName;
//                	if(item.IsSettlement == 1){
//                		$("#orderType").html("已结算");
//                	}else if(item.IsSettlement == 2){
//                		$("#addBtn").show();
//                		$("#orderType").html("未结算");
//                	}else{
//                		$("#orderType").html("其他");
//                	}
                	if((item.PayState==2 || item.PayState==0) && item.BizState==1){
                		$("#orderType").html("已支付");
  					}else if(item.PayState==4 && item.BizState==2){
                		$("#orderType").html("已退费");
  					}else if(item.PayState==1 ){
                		$("#orderType").html("退费中");
  					}else if(item.PayState==3&& item.BizState==2){
                		$("#orderType").html("退费中");
  					}else if(item.PayState==0 && (item.OverState==5||item.OverState==6)){
                		$("#orderType").html("已取消");
				 	}else if(item.PayState==0 && item.OverState==0&&item.BizState==0){
                		$("#orderType").html("未支付");
                	  	$("#addBtn").show();
				 	}else{
                		$("#orderType").html("其他");
				 	}
            	});
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
				window.location.href = Commonjs.goToUrl("/business/pay/payment.html?orderId="+orderId+"&toUrl=/business/pay/paySuccess.html?ServiceId=007");
		},
		okValue:'\u786e\u5b9a'
	});
}


function payOrderSettlement(){
    var apiData = {};
	var param = {};
	apiData.CardNo=cardNo;
	apiData.CardType=cardType;
	apiData.MemberName=memberName; 
	apiData.Price = price;
	apiData.Data_1 = {};
	apiData.Data_1.OrderId = orderId;
	param.api = 'PayOrderSettlement';
	param.apiParam = Commonjs.getApiReqParams(apiData,"","");
	Commonjs.ajax("../../wsgw/order/PayOrderSettlement/callApi.do?t=" + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
//			console.log(jsons);
            if (jsons.RespCode == 10000) {
            	addOrderOver();
            }else if(jsons.RespCode == -63011) { //钱不够
            	myLayer.alert(jsons.RespMessage,3000);
            	goToRecharge();
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    });
}


function CancelOrderSettlement(){
	if(confirm('是否确认取消该订单？')){
		var apiData = {};
	  	apiData.OperatorName=memberName;
	  	apiData.OperatorId=Commonjs.getOpenId();
	  	apiData.OrderId=orderId;
	  	var param = {};
	  	var page = {};
	  	param.api = 'CancelOrder'; 
	  	param.apiParam =Commonjs.getApiReqParams(apiData,page);
	  	Commonjs.ajax('../../wsgw/order/CancelOrder/callApi.do',param,function(jsons){
	  		if(!Commonjs.isEmpty(jsons)){
	  			if(!Commonjs.isEmpty(jsons.RespCode)){
		  			if(jsons.RespCode==10000){
		              	queryOrderInfo();
		  				}else {
	              	myLayer.alert(jsons.RespMessage,3000);
	              }
	  			}else{
	  				myLayer.alert("返回码为空");
	  			}
	  		}else{
	  			//通信失败
	          	myLayer.alert('网络繁忙,请刷新后重试',3000);
	  		}
	    },{async:false});
	}
	
}
