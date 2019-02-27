var t_img; // 定时器
var isLoad = true; // 控制变量（判断图片是否 加载完成）
var Resource = undefined;
var OrderId = undefined;
var RefundOrderId = undefined;
var OrderState = undefined;
$(function(){
	Resource = QueryString('Resource');
	OrderId = QueryString('OrderId');
	RefundOrderId = QueryString('RefundOrderId');
	OrderState = QueryString('OrderState');
	if(OrderState == 1 || OrderState == 2){
		loadPayOrderData(OrderId, OrderState);
	}else{
		loadRefundOrderData(RefundOrderId);
	}
	btnShow(OrderState);
	//左右等高
	var lH = $('.detl-left').height();
	var rH = $('.detl-right').height();
	if(lH > rH){
		$('.detl-right').height(lH);
	}else{
		$('.detl-left').height(rH);	
	}
	if (window.matchMedia) {
	    var mediaQueryList = window.matchMedia('print');
	    mediaQueryList.addListener(function(mql) {
	        if (mql.matches) {
	        	window.location.reload();
	        } else {
	        	window.location.reload();
	        }
	    });
	}
});
//关闭按钮
function btnShow(orderState){
	//按钮关掉
	$(".d-sync").hide();
	$(".d-corre").hide();
	$(".d-refund").hide();
	$(".d-account").hide();
	$(".d-data").hide();
	if(orderState == 1){
		$(".d-corre").show();
		$(".d-refund").show();
	}else if(orderState == 7){
		$(".d-refund").show();
	}else{
		$(".dl-btn").hide();
	}
}
//加载支付订单数据
function loadPayOrderData(Orderid, Orderstate){
	var Service = {};
	Service.OrderId = Orderid;
	Service.OrderState = Orderstate;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/order/queryPayOrderDetail.do', param, function(d){
		if(d.RespCode == 10000){
			var orderDetail = d.Data[0];
			$("#orderId").val(orderDetail.OrderId);
			$("#orderType").val(1);
			$("#channelId").val(orderDetail.ChannelId);
			$("#refundPrice").val(orderDetail.PayMoney);
			$("#price").val(orderDetail.PayMoney);
			$("#reason").val("HIS充值失败");
			$("#hisOrderNo").val(orderDetail.HisSerialNo);
			$("#merchNo").val(orderDetail.ChannelSerialNo);
			
			var orderId = orderDetail.OrderId;
			orderId = orderId.replace(orderId.substring(5,orderId.length-3),"******");
			orderId += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+orderDetail.OrderId+'">复制</button>';
			var channelSerialNo = orderDetail.ChannelSerialNo;
			if(Commonjs.isEmpty(channelSerialNo)){
				channelSerialNo = "&nbsp;&nbsp;";
			}else{
				channelSerialNo = channelSerialNo.replace(channelSerialNo.substring(5,channelSerialNo.length-3),"******");
				channelSerialNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+orderDetail.ChannelSerialNo+'">复制</button>';
			}
			var hisSerialNo = orderDetail.HisSerialNo;
			if(Commonjs.isEmpty(hisSerialNo)){
				hisSerialNo = "&nbsp;&nbsp;";
			}else{
				hisSerialNo = hisSerialNo.replace(hisSerialNo.substring(5,hisSerialNo.length-3),"******");
				hisSerialNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+orderDetail.HisSerialNo+'">复制</button>';
			}
			var stepClass = "";
			if(OrderState == 2){
				stepClass = 'class="finish"';
			}
			var channelName = orderDetail.ChannelName;
			var stepHtml = '<div class="ptb50">' 
				+'<ul class="step-list clearfix step3">'
					+'<li class="first finish">'
						+'<div class="step-num">1</div>'
						+'<div class="step-bar"></div>'
						+'<h4>交易下单</h4>'
						+'<p>'+orderDetail.OrderDate+'</p>'
					+'</li>'
					+'<li class="finish">'
						+'<div class="step-num">2</div>'
						+'<div class="step-bar"></div>'
						+'<h4>用户支付</h4>'
						+'<p>'+orderDetail.PayDate+'</p>'
					+'</li>'
					+'<li '+stepClass+'>'
						+'<div class="step-num">3</div>'
						+'<div class="step-bar"></div>'
						+'<h4>交易完成</h4>'
						+'<p>'+orderDetail.BizDate+'</p>'
					+'</li>'
				+'</ul>'
				+'</div>';
			var boxHtml = '<div class="detl-box barg-box clearfix">'
				+'<div class="detl-left debg3">'
					+'<span style="margin-left: 62%;" id="opr">'
						+'<a style="text-decoration:underline;" href="javascript:doPrint();">打印订单</a>&nbsp;&nbsp;'
						+'<a class="d-data" style="text-decoration:underline;" href="javascript:void(0);">交易日志</a>'
					+'</span>'
					+'<div class="dl-hold">'
						+'<h4><i class="icon icon-ok"></i>交易状态：用户已支付</h4>'
						+'<div class="mt30">'
							+'<p>智付订单号：'+orderId+'</p>'
							+'<p>渠道流水号：'+channelSerialNo+'</p>'
							+'<p>医院流水号：'+hisSerialNo+'</p>'
						+'</div>'
						+'<div class="h80">'
							+'<div class="dl-btn">'
								+'<a href="javascript:;" class="c-btn c-btn-blue c-btn-tiny d-sync">同步</a>'
								+'<a href="javascript:;" class="c-btn c-btn-white c-btn-tiny ml15 d-corre">冲正</a>'
								+'<a href="javascript:;" class="c-btn c-btn-white c-btn-tiny ml15 d-refund">退款</a>'
							+'</div>'
						+'</div>'
					+'</div>'
				+'</div>'
				+'<div class="detl-right">'
		            +'<div class="p20">'
			            +'<table class="tb tb-l c-t-center">'
				            +'<tr>'
					            +'<th width="90" rowspan="2" class="c-f14">订单详情</th>'
					            +'<th>交易渠道</th>'
					            +'<th>订单类型</th>'
					            +'<th>订单金额</th>'
					            +'<th>支付方式</th>'
					            +'<th>实付金额</th>'
				            +'</tr>' 
				            +'<tr>'
				            	+'<td>'+orderDetail.ChannelName+'</td>'
				            	+'<td>'+orderDetail.OrderType+'</td>'
				            	+'<td><span class="c-bold">'+Commonjs.centToYuan(orderDetail.OrderMoney)+'</span></td>'
				            	+'<td>'+orderDetail.PayMethodName+'</td>'
				            	+'<td><span class="c-bold">'+Commonjs.centToYuan(orderDetail.PayMoney)+'</span></td>'
				            +'</tr>'
			            +'</table>'
			            +'<table class="tb tb-l c-t-center mt20">'
			            	+'<tr>'
			            		+'<th width="90" rowspan="2" class="c-f14">患者信息</th>'
			            		+'<th>就诊卡号/住院号</th>'
			            		+'<th>就诊人姓名</th>'
			            		+'<th>就诊人联系电话</th>'
			            	+'</tr>'
			            	+'<tr>'
				            	+'<td>'+Commonjs.subCardNo(orderDetail.CardNo)+'</td>'
			            		+'<td>'+orderDetail.NickName+'</td>'
			            		+'<td>'+orderDetail.NickMobile+'</td>'
			            	+'</tr>'
			            +'</table>'
		            +'</div>'
	            +'</div>'
			+'</div>';
			$(".wrapper").html(stepHtml + boxHtml);
		}else{
			Commonjs.alert(d.RespMessage);
		}
	});
}

//加载退款订单数据
function loadRefundOrderData(RefundOrderId){
	var Service = {};
	Service.RefundOrderId = RefundOrderId;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/order/queryRefundOrderDetail.do', param, function(d){
		if(d.RespCode == 10000){
			var refundOrderDetail = d.Data[0];
			
			$("#orderId").val(refundOrderDetail.OrderId);
			$("#refundOrderId").val(refundOrderDetail.RefundOrderId);
			$("#orderType").val(2);
			$("#channelId").val(refundOrderDetail.RefundChannelId);
			$("#refundPrice").val(refundOrderDetail.RefundPrice);
			$("#price").val(refundOrderDetail.PayMoney);
			$("#reason").val("退款失败");
			$("#hisOrderNo").val(refundOrderDetail.HisRefundSerialNo);
			$("#merchNo").val(refundOrderDetail.ChannelSerialNo);
			
			var orderId = refundOrderDetail.OrderId;
			orderId = orderId.replace(orderId.substring(5,orderId.length-3),"******");
			orderId += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+refundOrderDetail.OrderId+'">复制</button>';
			var refundNo = refundOrderDetail.RefundNo;
			if(Commonjs.isEmpty(refundOrderDetail)){
				refundNo = "&nbsp;&nbsp;";
			}else{
				refundNo = refundNo.replace(refundNo.substring(5,refundNo.length-3),"******");
				refundNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+refundOrderDetail.RefundNo+'">复制</button>';
			}
			var channelSerialNo = refundOrderDetail.ChannelSerialNo;
			if(Commonjs.isEmpty(channelSerialNo)){
				channelSerialNo = "&nbsp;&nbsp;";
			}else{
				channelSerialNo = channelSerialNo.replace(channelSerialNo.substring(5,channelSerialNo.length-3),"******");
				channelSerialNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+refundOrderDetail.ChannelSerialNo+'">复制</button>';
			}
			var hisSerialNo = refundOrderDetail.HisSerialNo;
			if(Commonjs.isEmpty(hisSerialNo)){
				hisSerialNo = "&nbsp;&nbsp;";
			}else{
				hisSerialNo = hisSerialNo.replace(hisSerialNo.substring(5,hisSerialNo.length-3),"******");
				hisSerialNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+refundOrderDetail.HisSerialNo+'">复制</button>';
			}
			var stepClass = "";
			var refundState = refundOrderDetail.RefundState;
			if(refundState == 4 || refundState == 7){
				stepClass = 'class="finish"';
			}
			var isRefundPrice = 0;
			//退款中和退款失败
			if(refundState == 4){
				isRefundPrice = Commonjs.centToYuan(refundOrderDetail.RefundPrice);
			}
			var operatorName = refundOrderDetail.OperatorName;
			var refundChannelId = refundOrderDetail.RefundChannelId;
			if(refundChannelId == 'wincancel'){
				operatorName = "窗口退费(" + operatorName + ")";
			}else if(refundChannelId == Commonjs.getClientId()){
				operatorName = "财务科(" + operatorName + ")";
			}else{
				operatorName = "用户自助退费";
			}
			var bizHtml = '';
			var stepHtml = 'step4';
			if(!Commonjs.isEmpty(refundOrderDetail.BizDate)){
				stepHtml = 'step5';
				bizHtml = '<li class="finish"><div class="step-num">3</div><div class="step-bar"></div><h4>交易完成</h4><p>'+refundOrderDetail.BizDate+'</p></li>';
			}
			var refundStateHtml = refundState == 3?"退款中":(refundState == 4?"已退款":"退款失败");
			var stepHtml = '<div class="ptb50">'
					+'<ul class="step-list clearfix '+stepHtml+'">'
						+'<li class="first finish">'
							+'<div class="step-num">1</div>'
							+'<div class="step-bar"></div>'
							+'<h4>交易下单</h4>'
							+'<p>'+refundOrderDetail.OrderDate+'</p>'
						+'</li>'
						+'<li class="finish">'
							+'<div class="step-num">2</div>'
							+'<div class="step-bar"></div>'
							+'<h4>用户支付</h4>'
							+'<p>'+refundOrderDetail.PayDate+'</p>'
						+'</li>'
						+ bizHtml
						+'<li class="finish">'
							+'<div class="step-num">4</div>'
							+'<div class="step-bar"></div>'
							+'<h4>发起退款</h4>'
							+'<p>'+refundOrderDetail.RefundBeginDate+'</p>'
						+'</li>'
						+'<li '+stepClass+'>'
							+'<div class="step-num">5</div>'
							+'<div class="step-bar"></div>'
							+'<h4>'+(refundState == 7?"退款失败":"退款完成")+'</h4>'
							+'<p>'+refundOrderDetail.RefundEndDate+'</p>'
						+'</li>'
					+'</ul>'
				+'</div>';
			var boxHtml = '<div class="detl-box barg-box clearfix">'
					+'<div class="detl-left debg2">'
						+'<span style="margin-left: 62%;" id="opr">'
							+'<a style="text-decoration:underline;" href="javascript:doPrint();">打印订单</a>&nbsp;&nbsp;'
							+'<a class="d-data" style="text-decoration:underline;" href="javascript:void(0);">交易日志</a>'
						+'</span>'
						+'<div class="dl-hold">'
							+'<h4><i class="icon icon-barg"></i>交易状态：'+refundStateHtml+'</h4>'
							+'<div class="mt30">'
								+'<p>智付订单号：'+orderId+'</p>'
								+'<p>渠道流水号：'+channelSerialNo+'</p>'
								+'<p>医院流水号：'+hisSerialNo+'</p>'
								+'<p>退款发起人：'+ operatorName +'</p>'
								+'<p>退款流水号：'+refundNo+'</p>'
							+'</div>';
							if(refundState == 7){
								boxHtml += '<div class="h80">'
												+'<div class="dl-btn">'
													+'<a href="javascript:;" class="c-btn c-btn-white c-btn-tiny ml15 d-refund">再次发起退款</a>'
												+'</div>'
											+'</div>';
							}
			boxHtml	+= '</div>'
					+'</div>'
					+'<div class="detl-right">'
						+'<div class="p20">'
							+'<table class="tb tb-l c-t-center">'
								+'<tr>'
									+'<th width="90" rowspan="2" class="c-f14">订单详情</th>'
									+'<th>交易渠道</th>'
									+'<th>订单类型</th>'
									+'<th>订单金额</th>'
									+'<th>支付方式</th>'
									+'<th>实付金额</th>'
								+'</tr>'
								+'<tr>'
									+'<td>'+refundOrderDetail.ChannelName+'</td>'
									+'<td>'+refundOrderDetail.OrderType+'</td>'
									+'<td><span class="c-bold">'+Commonjs.centToYuan(refundOrderDetail.OrderMoney)+'</span></td>'
									+'<td>'+refundOrderDetail.PayMethodName+'</td>'
									+'<td><span class="c-bold">'+Commonjs.centToYuan(refundOrderDetail.PayMoney)+'</span></td>'
								+'</tr>'
							+'</table>'
							+'<table class="tb tb-l c-t-center mt20">'
								+'<tr>'
									+'<th width="90" rowspan="2" class="c-f14">退费详情</th>'
									+'<th>交易渠道</th>'
									+'<th>订单类型</th>'
									+'<th>退款金额</th>'
									+'<th>支付方式</th>'
									+'<th>实退金额</th>'
								+'</tr>'
								+'<tr>'
									+'<td>'+refundOrderDetail.RefundChannelName+'</td>'
									+'<td>'+refundOrderDetail.OrderType+'</td>'
									+'<td><span class="c-bold">'+Commonjs.centToYuan(refundOrderDetail.RefundPrice)+'</span></td>'
									+'<td>'+refundOrderDetail.PayMethodName+'</td>'
									+'<td><span class="c-bold">'+isRefundPrice+'</span></td>'
								+'</tr>'
							+'</table>'
							+'<table class="tb tb-l c-t-center mt20">'
								+'<tr>'
									+'<th width="90" rowspan="2" class="c-f14">患者信息</th>'
									+'<th>就诊卡号/住院号</th>'
									+'<th>就诊人姓名</th>'
									+'<th>就诊人联系电话</th>'
								+'</tr>'
								+'<tr>'
									+'<td>'+Commonjs.subCardNo(refundOrderDetail.CardNo)+'</td>'
									+'<td>'+refundOrderDetail.NickName+'</td>'
									+'<td>'+refundOrderDetail.NickMobile+'</td>'
								+'</tr>'
							+'</table>'
						+'</div>'
					+'</div>'
				+'</div>';
			
			$(".wrapper").html(stepHtml + boxHtml);
		}else{
			Commonjs.alert(d.RespMessage);
		}
	});
}

//同步
$(document).on('click','.d-sync',function(){
	var artLoading=art.dialog({
		lock: true, 
		padding:'25px 60px 5px 60px', 
		content: '<img src="/widget/artDialog/4.1.7/images/loading.gif" class="mr10" />正在同步数据…', 
		tips:true
		});
	var orderId = $("#orderId").val();
	var Service = {};
	Service.OrderId = orderId;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/order/orderSynchro.do',param,function(data){
		if(data.RespCode == 10000){
			Commonjs.alert(data.RespMessage);
			window.setTimeout(function(){
				window.location.href = "../order/order_list.html";
			},2000);
			
		}else{
			Commonjs.alert("同步失败!" + data.RespMessage);
		}
	});
});
//确认冲正
$(document).on('click','.d-corre',function(){
	if(Resource == 1){
		Commonjs.alert("<p><font size='3px' color='red'>此处只允许当日订单冲正操作,确认冲正请前往对账详情页面进行操作！</font></p>");
		return;
	}
	var artBox=art.dialog({	
		lock: true,
		width: 500,
		padding:'10px 50px 20px 50px',
		title:'确认冲正该笔交易？',
		content:$('#dCorre').get(0),
		ok: function () {
			var orderId = $("#orderId").val();
			var orderType = $("#orderType").val();
			var Service = {};
			Service.OrderId = orderId;
			Service.OrderType = orderType;
			var param = {};
			param.reqParam = Commonjs.getApiReqParams(Service);
			Commonjs.ajax('/order/orderReverse.do',param,function(data){
				if(data.RespCode == 10000){
					Commonjs.alert("冲正" + data.RespMessage);
					window.setTimeout(function(){
						window.location.href = "../order/order_list.html";
					},2000);
					
				}else{
					Commonjs.alert("冲正失败!" + data.RespMessage);
				}
			});
		},
		cancel: true
	});
});
//退款
$(document).on('click','.d-refund',function(){
	if(Resource == 1){
		Commonjs.alert("<p><font size='3px' color='red'>此处只允许当日订单退款,确认退款请前往对账详情页面进行操作！</font></p>");
		return;
	}
	var param1 = {};
	param1.reqParam = Commonjs.getApiReqParams({});
	var artBox1=art.dialog({	
		lock: true,
		width: 500,
		padding:'10px 50px 20px 50px',
		title:'确认退款？',
		content:'<font size="3px" color="red">注意！此处为平台操作退款，未进入HIS校验，退款操作无法撤销，请谨慎操作！</font>',
		ok: function () {
			Commonjs.ajax('/sys/queryUserPayKey.do',param1,function(resp){
				if( resp.RespCode == 10000 ){
					if(Commonjs.isEmpty(resp.Data)){
						var artBox2=art.dialog({	
							lock: true,
							width: 450,
							title:'风控校验',
							content:$('#dRisk').get(0),
							ok: function () {
								var loginKey = $("#login_key").val();
								var payKey = $("#pay_key").val();
								var repeatPayKey = $("#repeat_pay_key").val();
								if(payKey != repeatPayKey){
									Commonjs.alert("两次输入的密码不一致,请重新输入!");
									return;
								}
								var param2 = {};
								var Service2 = {};
								Service2.LoginKey = $.md5(loginKey).toUpperCase();
								Service2.PayKey = $.md5(payKey).toUpperCase();
								param2.reqParam = Commonjs.getApiReqParams(Service2);
								Commonjs.ajax('/sys/updateUserForPayKey.do', param2, function(data){
									artBox2.close();
									if(data.RespCode == 10000 ){
										art.dialog({
											lock : true,
											artIcon : "info",
											opacity : 0.4,
											width : 250,
											title : '提示',
											content : "支付密码设置成功！",
											ok : function() {
												var artBox3=art.dialog({	
													lock: true,
													width: 450,
													title:'风控校验',
													content:$('#dRisk_').get(0),
													ok: function () {
														var orderId = $("#orderId").val();
														if(Commonjs.isEmpty(orderId)){
															Commonjs.alert("未查询到本地全流程账单，请联系技术人员排查。。。");
														}
														var reason = $("#reason").val();
														var price = $("#price").val();
														var refundPrice = $("#refundPrice").val();
														var hisOrderNo = $("#hisOrderNo").val();

														var Service = {};
														var payKey = $("#payKey").val();
														Service.PayKey = $.md5(payKey).toUpperCase();
														Service.OrderId = orderId;
														Service.Price = price;
														Service.RefundPrice = refundPrice;
														Service.Reason = reason;
														Service.ChannelId = Commonjs.getClientId();
														Service.OutRefundOrderId = hisOrderNo;
														
														var param = {};
														param.reqParam = Commonjs.getApiReqParams(Service);
														Commonjs.ajaxSync('/order/orderRefund.do',param,function(data){
															if(data.RespCode==10000){
																Commonjs.alert("退款成功!");
																window.setTimeout(function(){
																	window.location.href = "../order/order_list.html";
																},2000);
																
															}else{
																Commonjs.alert("退款失败!" + data.RespMessage);
															}
														});
													},
													cancel: true
												});
											}
										});			
									}else{	
										Commonjs.alert(data.RespMessage);
									}
								});
							},
							cancel: true
						});
					}else{
						var artBox3=art.dialog({	
							lock: true,
							width: 450,
							title:'风控校验',
							content:$('#dRisk_').get(0),
							ok: function () {
								var orderId = $("#orderId").val();
								if(Commonjs.isEmpty(orderId)){
									Commonjs.alert("未查询到本地全流程账单，请联系技术人员排查。。。");
								}
								var reason = $("#reason").val();
								var price = $("#price").val();
								var refundPrice = $("#refundPrice").val();
								var hisOrderNo = $("#hisOrderNo").val();

								var Service = {};
								var payKey = $("#payKey").val();
								Service.PayKey = $.md5(payKey).toUpperCase();
								Service.OrderId = orderId;
								Service.Price = price;
								Service.RefundPrice = refundPrice;
								Service.Reason = reason;
								Service.ChannelId = Commonjs.getClientId();
								Service.OutRefundOrderId = hisOrderNo;
								
								var param = {};
								param.reqParam = Commonjs.getApiReqParams(Service);
								Commonjs.ajaxSync('/order/orderRefund.do',param,function(data){
									if(data.RespCode==10000){
										Commonjs.alert("退款成功!");
										window.setTimeout(function(){
											window.location.href = "../order/order_list.html";
										},2000);
										
									}else{
										Commonjs.alert("退款失败!" + data.RespMessage);
									}
								});
							},
							cancel: true
						});
					}
				}else{
					Commonjs.alert(resp.RespMessage);	
				}
			});
		},
		cancel: true
	});
});

//交易日志
$(document).on('click','.d-data',function(){
	setLogContent();
	var artBox=art.dialog({	
		lock: true,
		width: 450,
		title:'交易日志',
		content:$('#dData').get(0)
	});
});

//初始化交易日志列表
function setLogContent() {
	var Service = {};
//	Service.OrderId = "YYGH_D31674E407FB83BDC7D026D9BBF";
	Service.OrderId = $("#orderId").val();
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/order/queryTransLog.do',param,function(d){
		if(d.RespCode == 10000){
			var orderTransaLog = d.Data[0];
			var transTime = orderTransaLog.TransTime;
			var payTime = orderTransaLog.PayTime;
			var bizTime = orderTransaLog.BizTime;
			if(!Commonjs.isEmpty(transTime)){
				$("#transation-log").append("<tr><td>"+transTime+"</td><td>智付调用"+orderTransaLog.ChannelName+"交易下单接口，进行预下单；</br>订单号:"+orderTransaLog.OrderId+";</br>订单金额:"+Commonjs.centToYuan(orderTransaLog.OrderMoney)+"</td><td><span class=\"c-green\">成功</span></td></tr>");
			}
			if(!Commonjs.isEmpty(payTime)){
				$("#transation-log").append("<tr><td>"+payTime+"</td><td>智付调用"+orderTransaLog.PayMethod+"接口，进行支付；</br>支付金额:"+Commonjs.centToYuan(orderTransaLog.PayMoney)+"</td><td><span class=\"c-green\">成功</span></td></tr>");
			}
			if(!Commonjs.isEmpty(bizTime)){
				$("#transation-log").append("<tr><td>"+bizTime+"</td><td>智付调用业务处理接口，进行订单的业务操作；</td><td><span class=\"c-green\">成功</span></td></tr>");
			}
			if(!Commonjs.isEmpty(orderTransaLog.RefundList)){
				var refundList = orderTransaLog.RefundList;
				for(var i=0;i<refundList.length;i++){
					var refundOrder = refundList[i];
					var refundState = refundOrder.PayState==3?"退费中":(refundOrder.PayState==4?"退费成功":"退费失败");
					$("#transation-log").append("<tr><td>"+refundOrder.RefundTime+"</td><td>智付调用"+refundOrder.RefundChannelName+"交易退款接口，进行退款操作;</br>" +
							"退款流水号:"+refundOrder.RefundNo+";</br>退款金额:"+Commonjs.centToYuan(refundOrder.RefundPrice)+";</br>退款发起人:"+refundOrder.OperatorName+";</td>" +
									"<td><span class=\"c-green\">"+refundState+"</span></td></tr>");
				}
			}
		}else{
			Commonjs.alert(d.respMessage);
		}
	});
}

function QueryString(val) {
	var uri = window.location.search;
	var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
	return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
}

//复制流水号
var clipboard = new ClipboardJS('.btn');
clipboard.on('success',function(e){
	e.clearSelection();
	Commonjs.alert('复制成功');
});
clipboard.on('error',function(e){
	e.clearSelection();
	Commonjs.alert('复制失败');
});

//打印
function doPrint(orderId,channelSerialNo,hisSerialNo,refundNo) {   
	$("#opr").remove();
	if(!Commonjs.isEmpty(orderId)){
		$("#orderId_").html("智付订单号:" + orderId.substring(0,13) + "<br/>" + orderId.substring(13,orderId.length));
	}
	if(!Commonjs.isEmpty(channelSerialNo)){
		$("#channelSerialNo_").html("渠道流水号:" + channelSerialNo.substring(0,13) + "<br/>" + channelSerialNo.substring(13,channelSerialNo.length));
	}
	if(!Commonjs.isEmpty(hisSerialNo)){
		$("#hisSerialNo_").html("医院流水号:" + hisSerialNo);
	}
	if(!Commonjs.isEmpty(refundNo)){
		$("#refundNo_").html("退款流水号:" + refundNo.substring(0,13) + "<br/>" + refundNo.substring(13,refundNo.length));
	}
	//按钮关掉
	$(".d-sync").hide();
	$(".d-corre").hide();
	$(".d-refund").hide();
	$(".d-account").hide();
	$(".d-data").hide();
	
	bdhtml=window.document.body.innerHTML; 
    sprnstr="<!--startprint-->";   
    eprnstr="<!--endprint-->";   
    prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17);   
    prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr)); 
    prnhtml = prnhtml.replace("wrapper","wrapper printDiv");
    prnhtml = prnhtml.replace("step-list","step-list printDivS");
    prnhtml = prnhtml.replace("detl-box","detl-box printDivBox");
    prnhtml = prnhtml.replace("ptb50","ptb5");
    prnhtml = prnhtml.replace("dl-hold","dl-hold printDivHold");
    /*prnhtml = prnhtml.replace("detl-box","detl-box printDiv");*/
    prnhtml = prnhtml.replace(new RegExp("tb tb-l c-t-center","gm"),"tb tb-l c-t-center print");
    prnhtml = prnhtml.replace(new RegExp("c-bold","gm"),"");
    prnhtml = prnhtml.replace("<i class=\"icon icon-ok\"></i>","");
    prnhtml = prnhtml.replace("<i class=\"icon icon-barg\"></i>","");
//    prnhtml = prnhtml.replace(new RegExp("c-f14","gm"),"c-f12");
    window.document.body.innerHTML=prnhtml; 
	window.print();   // 加载完成
	window.location.reload();
}
