var orderId = null;

$(function(){
	
	orderId = Commonjs.getUrlParam('orderId');
	
	//加载数据
	loadData(orderId);
	
	$('#refund').on('click', function(){
		art.dialog({
			lock: true,
			artIcon:'edit',
			opacity:0.4,
			width: 400,
			title:'提示',
			content: '<span class="c-333 c-f14">该订单是否已经人工核实确认可以退款？</span>',
			ok: function () {
				$.ajax({
					type: 'POST',
					url: './bill_billRefund.do?v='+(new Date()),
					data: {orderId:orderId,
						channelId:channelId,
						price:price*100},
					async: true,
					timeout : 6000,
					cache : false,
					dataType: 'json',
					success: function(data){			
						if(d.respCode == 10000){
							Commonjs.alert('退款成功！');
						}else{
							Commonjs.alert('退款失败！失败信息如下：'+d.respMessage);
						}
					}
				});
			},
			cancel: true
		});
	});
	
});

/**
 * 加载数据
 */
function loadData(orderId){
	
	$.ajax({
		type: 'POST',
		url: './bill_getBill.do?v='+(new Date()),
		data: {orderId:orderId},
		async: true,
		timeout : 6000,
		cache : false,
		dataType: 'json',
		success: function(data){
			if( data.respCode == "10000"){
				//HIS的orderId
				if( ComWbj.isNull( data.data.HISORDERID ) ){
					$("#hisOrderId").html("无");
				}else{
					$("#hisOrderId").html(data.data.HISORDERID );
				}
				
				//商户号订单
				if( ComWbj.isNull( data.data.MERCHNO ) ){
					$("#merchNo").html("无");
				}else{
					$("#merchNo").html(data.data.MERCHNO);
				}
				
				//全流程订单
				if( ComWbj.isNull( data.data.ORDERID ) ){
					$("#orderId").html("无");
				}else{
					$("#orderId").html(data.data.ORDERID);
				}
				
				//提交时间
				if( ComWbj.isNull( data.data.beginDate ) ){
					$("#beginDate").html("无");
				}else{
					$("#beginDate").html(data.data.beginDate);
				}
				
				//交易时间
				if( ComWbj.isNull( data.data.TRANSDATESTR ) ){
					$("#transDate").html("无");
				}else{
					$("#transDate").html(data.data.TRANSDATESTR);
				}
				
				//就诊卡
				if( ComWbj.isNull( data.data.cardNo ) ){
					$("#cardNo").html("无");
				}else{
					$("#cardNo").html(data.data.cardNo);
				}
				
				//就诊人
				if( ComWbj.isNull( data.data.memberName ) ){
					$("#memberName").html("无");
				}else{
					$("#memberName").html(data.data.memberName);
				}
				
				//服务内容
				if( ComWbj.isNull( data.data.serviceName ) ){
					$("#serviceName").html("无");
				}else{
					var serviceName;
					if(data.data.serviceName=="006"){
						serviceName = "门诊预交金交纳";
					}else{
						serviceName = "住院预交金交纳";
					}
					$("#serviceName").html(serviceName);
				}
				//业务状态
				if( ComWbj.isNull( data.data.HISBIZSTATE ) || data.data.HISBIZSTATE==-1  ){
					$("#hisBizState").html("无");
				}else{
					var hisBizState;
					if(data.data.HISBIZSTATE=="1"){
						hisBizState = "已消费";
					}else{
						hisBizState = "未消费";
					}
					$("#hisBizState").html(hisBizState);
				}
				
				//应收款项
				if( ComWbj.isNull( data.data.RECEIVABLEMONEY ) ){
					$("#receivableMoney").html("0元");
				}else{
					$("#receivableMoney").html(parseFloat(data.data.RECEIVABLEMONEY/100).toFixed(2)/*Number(data.data.RECEIVABLEMONEY)*/+"元");
				}
				
				//已收款项
				if( ComWbj.isNull( data.data.RECEIVEDMONEY ) ){
					$("#receivedMoney").html("0元");
				}else{
					$("#receivedMoney").html(parseFloat(data.data.RECEIVEDMONEY/100).toFixed(2)/*Number(data.data.RECEIVEDMONEY)*/+"元");
				}

				//渠道
				if( ComWbj.isNull( data.data.channelname ) ){
					$("#channelName").html("未知");
				}else{
					$("#channelName").html(data.data.channelname);
				}
				
				//订单状态
//				if( ComWbj.isNull( data.data.orderStateName ) ){
//					$("#orderState").html("未知");
//				}else{
//					$("#orderState").html(data.data.orderStateName);
//				}
				
				//订单状态
				if( ComWbj.isNull( data.data.CHECKSTATE ) ){
					$("#orderState").html("<span class=\"c-f00\">异常</span>");
				}else{
					if(  data.data.CHECKSTATE ==0 ){
						$("#orderState").html("<span class=\"c-4dcd70\">正常</span>");
	            	}else if(  data.data.CHECKSTATE== 1){
	            		$("#orderState").html("<span class=\"c-f00\">长款</span>");
	            		//$("#divRefund").show();
	            	}else if(  data.data.CHECKSTATE == -1){
	            		$("#orderState").html("<span class=\"c-f00\">短款</span>");
	            	}else if(  data.data.CHECKSTATE == 2){
	            		$("#orderState").html("<span class=\"c-4dcd70\">已处理</span>");
	            		$("#dealBillTable").show();
	            		$("#operatorName").html(data.data.OPERATORNAME);
	            		$("#operatorId").html(data.data.OPERATOR);
	            		$("#remark").html(data.data.REMARK);
	            		$("#updateDate").html(data.data.UPDATEDATESTR);
	            	}
				}
				
				//收款状态
				if( ComWbj.isNull( data.data.PAYSTATE ) ){
					$("#payState").html("未收款");
				}else{
					if( data.data.PAYSTATE == 1 ){
						$("#payState").html("已收款");
					}else if( data.data.PAYSTATE == 2 ){
						$("#payState").html("已退款");
					}
					
				}

				var exceptionDesc = "";
				//异常说明
				if( ComWbj.isNull( data.data.HISORDERID ) ){
					exceptionDesc += "HIS订单不存在";
				}
				if( ComWbj.isNull( data.data.ORDERID ) ){
					exceptionDesc += "全流程订单不存在";
				}
				if( ComWbj.isNull( data.data.MERCHNO ) ){
					exceptionDesc += "微信订单不存在";
				}
				$("#exceptionDesc").html(exceptionDesc);
				
				
			}else{
				alert(data.respMsg);
			}
			
			
			
			
		}
	});
}

