var BillCheckId = undefined;
var IsDeal = false;
var BackPage = 0;
$(function(){
	BillCheckId = QueryString('BillCheckId');
	IsDeal = QueryString('IsDeal');
	BackPage = QueryString('BackPage');
	if(IsDeal=='true'){
		loadDealBillData(BillCheckId);
	}else{
		loadbillData(BillCheckId);
	}
	/*$(".d-sync").hide();
	$(".d-corre").hide();
	$(".d-refund").hide();
	$(".d-account").hide();
	$(".d-data").hide();*/

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
	
	//左右等高
	var lH = $('.detl-left').height();
	var rH = $('.detl-right').height();
	if(lH > rH){
		$('.detl-right').height(lH);
	}else{
		$('.detl-left').height(rH);	
	}
	
	var CheckState = $("#checkState").val();
	var HisOrderNo = $("#hisOrderNo").val();
	var MerchNo = $("#merchNo").val();
	openOpr(CheckState, HisOrderNo, MerchNo);
});

//返回上一页
function goBack(){
	if(BackPage == 1){
		window.location.href = "/module/payment/bill/bill-list-exception.html?IsDeal=true";
	}else{
		window.history.go(-1);
	}
}

//加载未处理的账单数据
function loadbillData(billCheckId){
	var Service = {};
	Service.BillCheckId = billCheckId;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/bill/queryBillDetail.do', param, function(d){
		if(d.RespCode == 10000){
			var billDetail = d.Data[0];
			var checkState = billDetail.CheckState;
			var hisOrderNo = billDetail.HisOrderNo;
			var merchNo = billDetail.MerchNo;
			$("#billDate").val(billDetail.BillDate);
			$("#orderId").val(billDetail.OrderId);
			$("#orderNo").val(billDetail.OrderNo);
			$("#refundOrderId").val(billDetail.BillType==1?"":billDetail.OrderNo);
			$("#billType").val(billDetail.BillType);
			$("#billSingleType").val(Commonjs.isEmpty(billDetail.MerchNo)?1:2);
			$("#channelId").val(billDetail.ChannelId);
			$("#refundPrice").val(billDetail.DiffPrice);
			$("#price").val(billDetail.PayMoney);
			$("#reason").val(billDetail.DiffReason);
			$("#checkState").val(checkState);
			$("#hisOrderNo").val(hisOrderNo);
			$("#merchNo").val(merchNo);
			var checkStateHtml = '';
			var debgClass = "";
			var colorCss = '';
			if(checkState == 0){
				debgClass = "debg1";
				checkStateHtml = '账平';
			}else if(checkState == 1){
				debgClass = "debg3";
				checkStateHtml = '长款';
				colorCss = 'style="color: #3bca53;"';
			}else if(checkState == -1){
				debgClass = "debg2";
				checkStateHtml = '短款';
				colorCss = 'style="color: #ec6941;"';
			}
			if(!Commonjs.isEmpty(hisOrderNo)){
				if(hisOrderNo.length > 13){
					hisOrderNo = hisOrderNo.replace(hisOrderNo.substring(5,hisOrderNo.length-3),"******");
				}
				hisOrderNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+billDetail.HisOrderNo+'">复制</button>';
			}
			if(!Commonjs.isEmpty(merchNo)){
				merchNo = merchNo.replace(merchNo.substring(5,merchNo.length-3),"******");
				merchNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+billDetail.MerchNo+'">复制</button>';
			}
			//orderId, orderState, refundOrderId
			var boxHtml = '<div class="detl-box clearfix">'
				+'<div class="detl-left '+debgClass+'">'
					+'<span style="margin-left: 62%;" id="opr">'
						+'<a style="text-decoration:underline;" href="javascript:doPrint();">打印账单</a>&nbsp;&nbsp;'
						+'<a style="text-decoration:underline;" href="javascript:findOrderDetail(\''+billDetail.OrderId+'\','+billDetail.BillType+',\''+billDetail.OrderNo+'\');">关联订单</a>'
					+'</span>'
					+'<div class="dl-hold">'
						+'<h4><i class="icon icon-zhang"></i>对账结果：'+checkStateHtml+'</h4>'
						+'<div class="mt40">'
							+'<p>对账时间：'+billDetail.BillDate+'</p>'
							+'<p>对账方式：自动对账</p>'
						+'</div>'
						+'<div class="h80">'
							+ '<div class="dl-btn">'
								+'<a href="javascript:;" class="c-btn c-btn-blue c-btn-tiny d-data">交易日志</a>&nbsp;&nbsp;'
								+'<a href="javascript:;" class="c-btn c-btn-white c-btn-tiny d-sync">同步</a>'
								+'<a href="javascript:;" class="c-btn c-btn-white c-btn-tiny ml15 d-corre">冲正</a>'
								+'<a href="javascript:;" class="c-btn c-btn-white c-btn-tiny ml15 d-refund refund-all">退款</a>'
								+'<a href="javascript:;" class="c-btn c-btn-white c-btn-tiny ml15 d-refund refund-diff">差额退款</a>'
								+'<a href="javascript:;" class="c-btn c-btn-white c-btn-tiny ml20 d-account">财务登帐</a>'
							+'</div>'
						+'</div>'
					+'</div>'
				+'</div>'
				+'<div class="detl-right">'
					+'<div class="p20">'
						+'<table class="tb tb-l c-t-center">'
							+'<tr>'
								+'<th colspan="4" class="c-f14">原始凭证</th>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">交易渠道</th>'
								+'<td>'+billDetail.ChannelName+'&nbsp;&nbsp;</td>'
								+'<th width="25%">支付方式</th>'
								+'<td>'+billDetail.PayMethodName+'&nbsp;&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">医院流水号</th>'
								+'<td>'+hisOrderNo+'&nbsp;&nbsp;</td>'
								+'<th width="25%">渠道流水号</th>'
								+'<td>'+merchNo+'&nbsp;&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">医院'+(billDetail.BillType==1?'应收':'应退')+'（元)</th>'
								+'<td>'+Commonjs.centToYuan(billDetail.HisPrice)+'&nbsp;&nbsp;</td>'
								+'<th width="25%">渠道'+(billDetail.BillType==1?'实收':'实退')+'（元）</th>'
								+'<td>'+Commonjs.centToYuan(billDetail.MerchPrice)+'&nbsp;&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">业务执行时间</th>'
								+'<td>'+billDetail.HisbizDate+'&nbsp;&nbsp;</td>'
								+'<th width="25%">支付完成时间</th>'
								+'<td>'+billDetail.PayDate+'&nbsp;&nbsp;</td>'
							+'</tr>'
						+'</table>'
						+'<table class="tb tb-l c-t-center mt20">'
							+'<tr>'
								+'<th colspan="2" class="c-f14">财务对账</th>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">处置时间</th>'
								+'<td>&nbsp;&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">对账方式</th>'
								+'<td>自动对账</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">对账结果</th>'
								+'<td '+colorCss+'>'+checkStateHtml+'&nbsp;&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">差错金额</th>'
								+'<td '+colorCss+'>'+Commonjs.centToYuan(billDetail.DiffPrice)+'&nbsp;&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">差错原因</th>'
								+'<td '+colorCss+'>'+billDetail.DiffReason+'&nbsp;&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">差错描述</th>'
								+'<td '+colorCss+'>'+billDetail.DiffDesc+'&nbsp;&nbsp;</td>'
							+'</tr>'
						+'</table>'
					+'</div>'
				+'</div>'
			+'</div>';
			
			var $boxHtml = $(boxHtml);
			$(".wrapper").append($boxHtml);
		}else{
			Commonjs.alert(d.RespMessage);
		}
	});
}

//开启账单操作
function openOpr(checkState, hisOrderNo, merchNo){
	if(checkState == 1 && !Commonjs.isEmpty(hisOrderNo) && !Commonjs.isEmpty(merchNo)){ //长款-金额不一致(极少出现)
		$(".d-data").show();  //交易日志
		$(".refund-diff").show(); //差额退款
		$(".d-sync").hide();   //同步
		$(".d-corre").hide();  //冲正
		$(".refund-all").hide();  //退款
		$(".d-account").hide();   //财务登帐
	}else if(checkState == 1 && Commonjs.isEmpty(hisOrderNo)){ //长款-商户单边账(支付订单)
		$(".d-sync").hide();   //同步
		$(".refund-all").show();  //退款
		$(".d-corre").show();  //冲正
		$(".d-data").hide();  //交易日志
		$(".refund-diff").hide(); //差额退款
		$(".d-account").hide();   //财务登帐
	}else if(checkState == 1 && Commonjs.isEmpty(merchNo)){//长款-His单边账(退款订单)
		$(".d-data").show();  //交易日志
		$(".d-sync").hide();   //同步
		$(".refund-all").show();  //退款
		$(".d-corre").hide();  //冲正
		$(".refund-diff").hide(); //差额退款
		$(".d-account").hide();   //财务登帐
	}else if(checkState == -1){
		//短款-His单边账(支付订单;支付流程是先商户支付成功，His再生成订单)
		//短款-商户单边账(退款订单;这种情况不会出现,退款流程都是先退His，后退商户)
		//所有的短款的处理方式都是由财务登账
		$(".d-data").show();  //交易日志
		$(".d-account").show();   //财务登帐
		$(".refund-diff").hide(); //差额退款
		$(".d-sync").hide();   //同步
		$(".d-corre").hide();  //冲正
		$(".refund-all").hide();  //退款
	}else if(checkState == 0){
		$(".d-data").show();  //交易日志
		$(".d-account").hide();   //财务登帐
		$(".refund-diff").hide(); //差额退款
		$(".d-sync").hide();   //同步
		$(".d-corre").hide();  //冲正
		$(".refund-all").hide();  //退款
	}
}

//加载处理过的账单数据
function loadDealBillData(billCheckId){
	var Service = {};
	Service.BillCheckId = billCheckId;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/bill/queryDealBillDetail.do', param, function(d){
		if(d.RespCode == 10000){
			var billDealDetail = d.Data[0];
			var dealWay = billDealDetail.DealWay;
			var dealWayHtml = dealWay==1?'退款':(dealWay==2?'冲正':(dealWay==3?'登账':'其他处理方式'));
			var checkState = billDealDetail.CheckState;
			var checkStateHtml = checkState==0?'账平':(checkState==1?'长款':'短款');
			var hisOrderNo = billDealDetail.HisOrderNo;
			var merchNo = billDealDetail.MerchNo;
			var orderNo = billDealDetail.OrderNo;
			$("#billDate").val(billDealDetail.BillDate);
			$("#orderId").val(billDealDetail.OrderId);
			$("#orderNo").val(orderNo);
			$("#refundOrderId").val(billDealDetail.BillType==1?"":orderNo);
			$("#billType").val(billDealDetail.BillType);
			$("#billSingleType").val(Commonjs.isEmpty(merchNo)?1:2);
			$("#channelId").val(billDealDetail.ChannelId);
			$("#refundPrice").val(billDealDetail.DiffPrice);
			$("#price").val(billDealDetail.PayMoney);
			$("#reason").val(billDealDetail.DiffReason);
			$("#checkState").val(checkState);
			$("#hisOrderNo").val(hisOrderNo);
			$("#merchNo").val(merchNo);
			if(!Commonjs.isEmpty(hisOrderNo)){
				if(hisOrderNo.length > 13){
					hisOrderNo = hisOrderNo.replace(hisOrderNo.substring(5,hisOrderNo.length-3),"******");
				}
				hisOrderNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+billDealDetail.HisOrderNo+'">复制</button>';
			}
			if(!Commonjs.isEmpty(merchNo)){
				merchNo = merchNo.replace(merchNo.substring(5,merchNo.length-3),"******");
				merchNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+billDealDetail.MerchNo+'">复制</button>';
			}
			if(!Commonjs.isEmpty(orderNo)){
				orderNo = orderNo.replace(orderNo.substring(5,orderNo.length-3),"******");
				orderNo += '&nbsp;&nbsp;<button class="btn" data-clipboard-text="'+billDealDetail.OrderNo+'">复制</button>';
			}
			var orderType = billDealDetail.BillType;
			var refundOrderId;
			if(orderType == 2){
				refundOrderId = billDealDetail.OrderNo;
			}else if(orderType == 1 && dealWay==1){
				orderType = 2;
				refundOrderId = billDealDetail.OrderNo;
			}
			var boxHtml = '<div class="detl-box clearfix">'
				+'<div class="detl-left debg1">'
					+'<span style="margin-left: 62%;" id="opr">'
						+'<a style="text-decoration:underline;" href="javascript:doPrint();">打印订单</a>&nbsp;&nbsp;'
						+'<a style="text-decoration:underline;" href="javascript:findOrderDetail(\''+billDealDetail.OrderId+'\','+orderType+',\''+refundOrderId+'\');">关联订单</a>'
					+'</span>'
					+'<div class="dl-hold">'
						+'<h4><i class="icon icon-zhang"></i>对账结果：账平（处置后）</h4>'
						+'<div class="mt40">'
							+'<p>初次对账时间：'+billDealDetail.BillDate+'&nbsp;</p>'
							+'<p>对账方式：自动对账</p>'
							+'<p>对账结果：'+(dealWay==3?'短款':'长款')+'</p>'
						+'</div>'
						+'<div class="mt40">'
							+'<p>差错处置时间：'+billDealDetail.DealDate+'&nbsp;</p>'
							+'<p>处置方式：'+dealWayHtml+'&nbsp;</p>'
							+'<p>处置人：'+billDealDetail.DealBy+'&nbsp;</p>'
						+'</div>'
						+'<div class="mt40">'
							+'<p>再次对账时间：'+billDealDetail.UpdateDate+'</p>'
							+'<p>对账方式：自动对账</p>'
							+'<p>对账结果：'+checkStateHtml+'&nbsp;</p>'
						+'</div>'
					+'</div>'
				+'</div>'
				+'<div class="detl-right">'
					+'<div class="p20">'
						+'<table class="tb tb-l c-t-center">'
							+'<tr>'
								+'<th colspan="4" class="c-f14">原始凭证</th>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">交易渠道</th>'
								+'<td>'+billDealDetail.ChannelName+'&nbsp;</td>'
								+'<th width="25%">支付方式</th>'
								+'<td>'+billDealDetail.PayMethodName+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">医院流水号</th>'
								+'<td>'+hisOrderNo+'&nbsp;</td>'
								+'<th width="25%">渠道流水号</th>'
								+'<td>'+merchNo+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">医院'+(billDealDetail.BillType==1?'应收':'应退')+'（元)</th>'
								+'<td>'+Commonjs.centToYuan(billDealDetail.HisPrice)+'&nbsp;</td>'
								+'<th width="25%">渠道'+(billDealDetail.BillType==1?'实收':'实退')+'（元）</th>'
								+'<td>'+Commonjs.centToYuan(billDealDetail.MerchPrice)+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">业务执行时间</th>'
								+'<td>'+billDealDetail.HisbizDate+'&nbsp;</td>'
								+'<th width="25%">支付完成时间</th>'
								+'<td>'+billDealDetail.PayDate+'&nbsp;</td>'
							+'</tr>'
						+'</table>'
						+'<table class="tb tb-l c-t-center mt20">'
							+'<tr>'
								+'<th colspan="2" class="c-f14">财务对账</th>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">对账时间</th>'
								+'<td>'+billDealDetail.BillDate+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">对账方式</th>'
								+'<td>自动对账</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">对账结果</th>'
								+'<td>'+checkStateHtml+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">差错金额</th>'
								+'<td>'+Commonjs.centToYuan(billDealDetail.DiffPrice)+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">差错原因</th>'
								+'<td>'+billDealDetail.DiffReason+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">差错描述</th>'
								+'<td>'+billDealDetail.DiffDesc+'&nbsp;</td>'
							+'</tr>'
						+'</table>'
						+'<table class="tb tb-l c-t-center mt20">'
							+'<tr>'
								+'<th colspan="2" class="c-f14">差错处置</th>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">处置时间</th>'
								+'<td>'+billDealDetail.DealDate+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">处置方式</th>'
								+'<td>'+dealWayHtml+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">处置结果</th>'
								+'<td>'+billDealDetail.DealRemark+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">订单流水号</th>'
								+'<td>'+orderNo+'&nbsp;</td>'
							+'</tr>'
							+'<tr>'
								+'<th width="25%">处置人</th>'
								+'<td>'+billDealDetail.DealBy+'&nbsp;</td>'
							+'</tr>'
						+'</table>'
					+'</div>'
				+'</div>'
			+'</div>';
			
			var $boxHtml = $(boxHtml);
			$(".wrapper").append($boxHtml);
		}else{
			Commonjs.alert(d.RespMessage);
		}
	});
}

function printBill(){
	var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
//	LODOP.PRINT_INIT("对账信息打印"); 
//	LODOP.ADD_PRINT_TEXT(0,0,100,20,"文本内容一");
//	LODOP.PRINT(); 
}

function CreatePrintPage() {
	var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	LODOP.ADD_PRINT_TABLE(100,20,500,80,document.getElementById("content").innerHTML);
	LODOP.SAVE_TO_FILE("${datePlan.jcType}-${datePlan.jcnum}-${unitType}1111.xls");
  }; 

//同步
$(document).on('click','.d-sync',function(){
	var artLoading=art.dialog({
		lock: true, 
		padding:'25px 60px 5px 60px', 
		content: '<img src="../widget/artDialog/4.1.7/images/loading.gif" class="mr10" />正在同步数据…', 
		tips:true
		});
	var billDate = $("#billDate").val();
	var Service = {};
	Service.BillDate = billDate;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/bill/billSynchro.do',param,function(data){
		if(data.RespCode == 10000){
			Commonjs.alert(data.RespMessage);
			window.setTimeout(function(){
				window.location.href = "../bill/bill-list-date.html";
			},2000);
			
		}else if(data.RespCode == -100005){
			artLoading.close();
			$(".d-sync").hide();
			if(checkState == 1){
				Commonjs.alert(data.RespMessage + "请前往冲正或退款!");
			}else{
				Commonjs.alert(data.RespMessage + "请前往财务登账!");
			}
		}else{
			artLoading.close();
			Commonjs.alert("同步超时,"+data.respMessage+"请联系技术人员排查故障原因!");
		}
	});
});
//确认冲正
$(document).on('click','.d-corre',function(){
	var orderId_ = $("#orderId").val();
	if(Commonjs.isEmpty(orderId_)){
		Commonjs.alert("未查询到本地全流程账单，请联系技术人员排查。。。");
	}
	var artBox=art.dialog({	
		lock: true,
		width: 500,
		padding:'10px 50px 20px 50px',
		title:'确认冲正该笔交易？',
		content:$('#dCorre').get(0),
		ok: function () {
			var orderId = $("#orderId").val();
			var orderType = $("#billType").val();
			var Service = {};
			Service.OrderId = orderId;
			Service.OrderType = orderType;
			var param = {};
			param.reqParam = Commonjs.getApiReqParams(Service);
			Commonjs.ajax('/bill/billReverse.do',param,function(data){
				if(data.RespCode == 10000){
					Commonjs.alert("冲正" + data.RespMessage);
					window.setTimeout(function(){
						window.location.href = "../bill/bill_detail.html?BillCheckId="+BillCheckId+"&IsDeal=true";
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
	var param1 = {};
	param1.reqParam = Commonjs.getApiReqParams({});
	var artBox1=art.dialog({	
		lock: true,
		width: 500,
		padding:'10px 50px 20px 50px',
		title:'确认退款？',
		content:'注意！退款操作无法撤销，请谨慎操作！',
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
//								var loginKey = $("#login_key").val();
								var payKey = $("#pay_key").val();
								var repeatPayKey = $("#repeat_pay_key").val();
								if(payKey != repeatPayKey){
									Commonjs.alert("两次输入的密码不一致,请重新输入!");
									return;
								}
								var param2 = {};
								var Service2 = {};
//								Service2.LoginKey = $.md5(loginKey).toUpperCase();
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
														var channelId = $("#channelId").val();
														var reason = $("#reason").val();
														var price = $("#price").val();
														var refundPrice = $("#refundPrice").val();
														var hisOrderNo = $("#hisOrderNo").val();

														var Service = {};
														Service.BillCheckId = BillCheckId;
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
														Commonjs.ajaxSync('/bill/billRefund.do',param,function(data){
															if(data.RespCode==10000){
																Commonjs.alert("退款成功!");
																window.setTimeout(function(){
																	window.location.href = "../bill/bill_detail.html?BillCheckId="+BillCheckId+"&IsDeal=true";
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
								var channelId = $("#channelId").val();
								var reason = $("#reason").val();
								var price = $("#price").val();
								var refundPrice = $("#refundPrice").val();
								var hisOrderNo = $("#hisOrderNo").val();

								var Service = {};
								Service.BillCheckId = BillCheckId;
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
								Commonjs.ajaxSync('/bill/billRefund.do',param,function(data){
									if(data.RespCode==10000){
										Commonjs.alert("退款成功!");
										window.setTimeout(function(){
											window.location.href = "../bill/bill_detail.html?BillCheckId="+BillCheckId+"&IsDeal=true";
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

//确认退款(二维码扫描鉴权退款)
$(document).on('click','.d-refund-',function(){
	var artBox=art.dialog({	
		lock: true,
		width: 500,
		padding:'10px 50px 20px 50px',
		title:'确认退款？',
		content:'注意！退款操作无法撤销，请谨慎操作！',
		ok: function () {
			var orderId = $("#orderId").val();
			if(Commonjs.isEmpty(orderId)){
				Commonjs.alert("未查询到本地全流程账单，请联系技术人员排查。。。");
			}
			var channelId = $("#channelId").val();
			var reason = $("#reason").val();
			var price = $("#price").val();
			var refundPrice = $("#refundPrice").val();
			var hisOrderNo = $("#hisOrderNo").val();
			
			var reqParam = new Array();
			reqParam.push(BillCheckId);
			reqParam.push(orderId);
			reqParam.push(price);
			reqParam.push(refundPrice);
			reqParam.push(reason);
			reqParam.push(Commonjs.getClientId());
			reqParam.push(hisOrderNo);
			window.location.href = "/bill/secondPwd.do?token="+Commonjs.getToken()+"&opera=refund&reqParam="+reqParam;
		},
		cancel: true
	});
});

//确认登帐(二维码扫描鉴权登账)
$(document).on('click','.d-account-',function(){
	var artBox=art.dialog({	
		lock: true,
		width: 800,
		title:'确认登账？',
		content:$('#dAccount').get(0),
		ok: function () {
			var reqParam = new Array();
			reqParam.push(BillCheckId);
			reqParam.push($("#billSingleType").val());
			window.location.href = "/bill/secondPwd.do?token="+Commonjs.getToken()+"&opera=checkIn&reqParam="+reqParam;
		},
		cancel: true
	});
});

//确认登帐
$(document).on('click','.d-account',function(){
	var param1 = {};
	param1.reqParam = Commonjs.getApiReqParams({});
	var artBox=art.dialog({	
		lock: true,
		width: 800,
		title:'确认登账？',
		content:$('#dAccount').get(0),
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
//								var loginKey = $("#login_key").val();
								var payKey = $("#pay_key").val();
								var repeatPayKey = $("#repeat_pay_key").val();
								if(payKey != repeatPayKey){
									Commonjs.alert("两次输入的密码不一致,请重新输入!");
									return;
								}
								var param2 = {};
								var Service2 = {};
//								Service2.LoginKey = $.md5(loginKey).toUpperCase();
								Service2.PayKey = $.md5(payKey).toUpperCase();
								param2.reqParam = Commonjs.getApiReqParams(Service2);
								Commonjs.ajax('/sys/updateUserForPayKey.do',param2,function(data){
									artBox2.close();
									if( data.RespCode == 10000 ){
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
														var Service = {};
														Service.BillCheckId = BillCheckId;
														var payKey = $("#payKey").val();
														Service.PayKey = $.md5(payKey).toUpperCase();
														Service.BillSingleType = $("#billSingleType").val();
														var param = {};
														param.reqParam = Commonjs.getApiReqParams(Service);
														Commonjs.ajaxSync('/bill/billCheckIn.do',param,function(d){
															if(d.RespCode==10000){
																Commonjs.alert("登账成功!");
																window.setTimeout(function(){
																	window.location.href = "../bill/bill_detail.html?BillCheckId="+BillCheckId+"&IsDeal=true";
																},2000);
																
															}else{
																Commonjs.alert(d.RespMessage);
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
								var Service = {};
								Service.BillCheckId = BillCheckId;
								var payKey = $("#payKey").val();
								Service.PayKey = $.md5(payKey).toUpperCase();
								Service.BillSingleType = $("#billSingleType").val();
								var param = {};
								param.reqParam = Commonjs.getApiReqParams(Service);
								Commonjs.ajaxSync('/bill/billCheckIn.do',param,function(d){
									if(d.RespCode==10000){
										Commonjs.alert("登账成功!");
										window.setTimeout(function(){
											window.location.href = "../bill/bill-list-exception.html?IsDeal=" + true;
										},2000);
										
									}else{
										Commonjs.alert(d.RespMessage);
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
	var orderId = $("#orderId").val();
	if(Commonjs.isEmpty(orderId)){
		Commonjs.alert("未查询到本地全流程账单，请联系技术人员排查。。。");
	}
	setLogContent();
	var artBox=art.dialog({	
		lock: true,
		width: 600,
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
					$("#transation-log").append("<tr><td>"+refundOrder.RefundTime+"</td><td>智付调用"+refundOrder.RefundChannelName+"交易退款接口，进行退款操作;</br>" +
							"退款流水号:"+refundOrder.RefundNo+";</br>退款金额:"+Commonjs.centToYuan(refundOrder.RefundPrice)+";</br>退款发起人:"+refundOrder.OperatorName+";</td>" +
									"<td><span class=\"c-green\">成功</span></td></tr>");
				}
			}
		}else{
			Commonjs.alert(d.respMessage);
		}
	});
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

//关联订单
function findOrderDetail(orderId, orderType, orderNo){
	if(Commonjs.isEmpty(orderId)){
		Commonjs.alert("未查询到本地全流程账单，请联系技术人员排查。。。");
	}
	
	var Service = {};
//	orderId = "4C50AC72B98849CEBC862C0293BA97E7";
	Service.OrderId = orderId;
	if(orderType == 2){  //退款订单
		Service.RefundOrderId = orderNo;
	}
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/order/queryOrderState.do',param,function(d){
		if(d.RespCode==10000){
			var orderState = d.Data[0].OrderState;
			var hisOrderNo = $("#hisOrderNo").val();
			if(orderType == 1){  // 支付订单
				if(hisOrderNo == ''){
					orderState = 1;
				}
				window.location.href = "../order/order_detail.html?Resource=1&OrderId=" + orderId + "&OrderState=" + orderState;
			}else if(orderType == 2){  //退款订单
				window.location.href = "../order/order_detail.html?Resource=1&OrderId=" + orderId + "&OrderState=" + orderState + "&RefundOrderId=" + orderNo;
			}
		}else{
			Commonjs.alert("未查询到本地全流程账单，请联系技术人员排查。。。");
		}
	}); 
}

function QueryString(val) {
	var uri = window.location.search;
	var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
	return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
}

//打印
function doPrint(orderNo,hisOrderNo,merchNo) {   
	$("#opr").hide();
	if(!Commonjs.isEmpty(orderNo)){
		$("#orderNo_").html(orderNo);
	}
	if(!Commonjs.isEmpty(hisOrderNo)){
		$("#hisOrderNo_").html(hisOrderNo);
	}
	if(!Commonjs.isEmpty(merchNo)){
		$("#merchNo_").html(merchNo);
	}
	//按钮关掉
	$(".d-sync").hide();
	$(".d-corre").hide();
	$(".d-refund").hide();
	$(".d-account").hide();
	$(".d-data").hide();
	
    var bdhtml=window.document.body.innerHTML;   
    sprnstr="<!--startprint-->";   
    eprnstr="<!--endprint-->";   
    prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17);   
    prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));
    prnhtml = prnhtml.replace("<i class=\"icon icon-zhang\"></i>","");
    prnhtml = prnhtml.replace("wrapper","wrapper printDiv");
    prnhtml = prnhtml.replace("dl-hold","dl-hold printDivHold");
    prnhtml = prnhtml.replace(new RegExp("c-f14","gm"),"c-f12");
    prnhtml = prnhtml.replace(new RegExp("tb tb-l c-t-center","gm"),"tb tb-l c-t-center print");
    prnhtml = prnhtml.replace("</h4>","</span>");
    
    console.log(prnhtml);
    window.document.body.innerHTML=prnhtml;  
    window.print();   
    window.location.reload();
}