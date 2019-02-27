var billInfoList = undefined;
var channelInfoList = undefined;
var payTypeInfoList = undefined;
var billCountInfo = {};
var pagenumber = 0;
var TransChannelId = undefined;
var PayMenthod = undefined;
var StartDate = undefined;
var EndDate = undefined;
var QueryDate = undefined;
var CheckState = undefined;
var IsException = true; // 是否是异常账单
var IsDeal = false;  // 是否已处置
var TransType = undefined;
var ConfigKey = undefined;

$(function(){
	TransChannelId = QueryString('TransChannelId');
	PayMenthod = QueryString('PayMenthod');
	StartDate = QueryString('StartDate');
	EndDate = QueryString('EndDate');
	CheckState = QueryString('CheckState');
	QueryDate = QueryString('QueryDate');
	TransType = QueryString('TransType');
	ConfigKey = QueryString('ConfigKey');
	IsDeal = QueryString('IsDeal');
	if(!Commonjs.isEmpty(IsDeal)){
		$('#isDeal').val(IsDeal);
	}
	var isDeal = $('#isDeal').val();
	if(isDeal == true || isDeal == 'true'){  // 已处置-账平
		$("#check-bill").addClass("curr");
		$("#exception-bill").removeClass("curr");
		$("#dealWay").show();
		$("#orderRule").val("5");
	}else{   //未处置-账不平
		$("#check-bill").removeClass("curr");
		$("#exception-bill").addClass("curr");
		$("#dealWay").hide();
		$("#orderRule").val("1");
	}
	if(!Commonjs.isEmpty(TransChannelId)){
		$("#channelId").val(TransChannelId);
	}
	if(!Commonjs.isEmpty(PayMenthod)){
		$("#payMenthod").val(PayMenthod);
	}
	if(!Commonjs.isEmpty(TransType)){
		$("#transType_").val(TransType);
	}
	if(!Commonjs.isEmpty(ConfigKey)){
		$("#configKey").val(ConfigKey);
	}
	$("#dealWay").hide();
	findAllPayType();
	$("#payType").append("<option value=\"\">全部支付方式</option>");
	if(!Commonjs.isEmpty(payTypeInfoList) && payTypeInfoList.length > 0){
		for(var i=0;i<payTypeInfoList.length;i++){
			var obj = payTypeInfoList[i];
			var $payTypeSel = $("<option value=\""+obj.PayType+"\">"+obj.PayTypeName+"</option>");
			$("#payType").append($payTypeSel);
		}
	}
	var transType_ = $("#transType_").val();
	if(!Commonjs.isEmpty(transType_)){
		$("input[name='transType'][value='"+transType_+"']").attr("checked","checked");
	}
	var payType = $("#payMenthod").val();
	if(!Commonjs.isEmpty(payType)){
		$("#payType").find("option[value='"+payType+"']").attr("selected","selected");
	}
	if(!Commonjs.isEmpty(StartDate) && !Commonjs.isEmpty(EndDate)){
		$('#queryDate').val(StartDate + " ~ " + EndDate);
	}
	if(!Commonjs.isEmpty(QueryDate)){
//		var queryDate = Commonjs.getDateAdd(QueryDate, 1);
		$('#queryDate').val(QueryDate + " ~ " + QueryDate);
	}
	var defaultDate = $('#queryDate').val();
	if(Commonjs.isEmpty(defaultDate)){
		var endTime = Commonjs.getDate(-1);
		var startTime = Commonjs.getDateAdd(endTime, -30);
		queryDate = startTime + " ~ " + endTime;
		$('#queryDate').val(queryDate);
	}
	pagenumber = $("#pagenumber_").val();
	findBillCountHtml();
	IsException = $('#isException').val();  //是否是异常账单(暂时保留)
	loadTb(pagenumber, 0);
	
	$(".bill-state").click(function(){
		var id = $(this).attr("id");
		if(id == 'check-bill'){  // 已处置-账平
			$("#check-bill").addClass("curr");
			$("#exception-bill").removeClass("curr");
			$("#dealWay").show();
			$("#orderRule").val("5");
			$('#isDeal').val("true");
			findBillCountHtml();
			loadTb(1, 0);
		}else if(id == 'exception-bill'){   //未处置-账不平
			$("#check-bill").removeClass("curr");
			$("#exception-bill").addClass("curr");
			$("#dealWay").hide();
			$("#orderRule").val("1");
			$('#isDeal').val("false");
			findBillCountHtml();
			loadTb(1, 0);
		}
	});
});

//搜索按钮
function search(){
	findBillCountHtml();
	$('#totalData').html(billCountInfo.totalCount);
	$('#0Data').html(billCountInfo.checkState0Count);
	$('#1Data').html(billCountInfo.checkState1Count);
	$('#T1Data').html(billCountInfo.checkStateT1Count);
	loadTb(1, 0);
}

function loadTb(index, totalCount, isDeal){
	pagenumber = index;
	$("#pagenumber_").val(index);
	//对账明细列表的查询条件
	var pageIndex = index;
	var pageSize = 10;
	var queryDate = $('#queryDate').val();
	var date = queryDate.split("~");
	var startDate = date[0];
	var endDate = date[1];
	var payType = $("#payType").val();
	var orderRule = $("#orderRule").val();
	var channelId = $("#channelId").val();
	var orderId = $("#orderId").val();
	var merchno = $("#merchno").val();
	var hisOrderno = $("#hisOrderno").val();
	var configKey = $("#configKey").val();
	var transType = $("input[name='transType']:checked").val();
	
	var dealState = 0;
	var dealWay = 0;
	var isDeal = $('#isDeal').val();
	if(isDeal==true || isDeal=='true'){
		dealState = 1;
		dealWay = $("#dealWay").val();
	}
	var Service = {};
	var page = {};
	page.PIndex = pageIndex;
	page.PSize = pageSize;
	Service.Page = page;
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.PayType = payType;
	Service.OrderRule = orderRule;
	Service.ChannelId = channelId;
	Service.DealState = dealState;
	Service.OrderId = orderId;
	Service.MerchNo = merchno;
	Service.HisOrderNo = hisOrderno;
	Service.TransType = transType;
	Service.ConfigKey = configKey;
	
	var timeType = '对账';
	if(dealState == 1){
		timeType = '处置';
	}
	Service.DealWay = dealWay;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/bill/queryBillListForException.do',param,function(d){
		var str = "<thead><tr><th>订单交易时间</th><th>订单类型</th><th>订单号</th><th>医院流水号</th><th>渠道流水号</th><th>应收/退</th><th>应收/退金额（元）</th><th>支付方式</th><th>实收/退</th><th>实收/退金额（元）</th><th>对账结果</th><th>差错金额</th><th>"+timeType+"时间</th><th>详情</th></tr></thead><tbody>";
		if(d.RespCode == 10000){
			var page = d.Page;
			if(page.PCount!=undefined){
				if(page.PCount!=0){
					totalCount = page.PCount;
				}else{
					if(page.PIndex==0) totalCount = 0;
				}
			}else{
				totalCount = 0;
			}
			PageModel(totalCount, page.PSize, 'pager');
			billInfoList = d.Data;
			if(!Commonjs.isEmpty(billInfoList) && billInfoList.length > 0){
				for(var i=0;i<billInfoList.length;i++){
					var obj = billInfoList[i];
					var orderNo = obj.OrderNo;
					if(!Commonjs.isEmpty(orderNo)){
						if(orderNo.length > 20){
							orderNo = orderNo.replace(orderNo.substring(5,orderNo.length-3),"******");
						}
					}
					var merchNo = obj.MerchNo;
					if(!Commonjs.isEmpty(merchNo)){
						if(merchNo.length > 20){
							merchNo = merchNo.replace(merchNo.substring(5,merchNo.length-3),"******");
						}
					}
					var dateTime = obj.CreateDate;
					if(dealState == 1){
						dateTime = obj.DealDate;
					}
					var arr = formateData(obj.BillType, obj.CheckState, obj.BillType, obj.DealState);
					str += "<tr>";
					str +="<td>"+obj.TransDate+"</td>";
					str +="<td>"+obj.Service+"</td>";
					str +="<td>"+orderNo+"</td>";
					str +="<td>"+obj.HisOrderNo+"</td>";
					str +="<td>"+merchNo+"</td>";
					str +="<td>"+arr.hisOrderType+"</td>";
					str +="<td>"+Commonjs.centToYuan(obj.HisPrice)+"</td>";
					str +="<td>"+obj.PayMethodName+"</td>";
					str +="<td>"+arr.channelOrderType+"</td>";
					str +="<td>"+Commonjs.centToYuan(obj.MerchPrice)+"</td>";
					var colorHtml = "";
					var diffPrice = 0;
					if(obj.CheckState == 0) {
						colorHtml = "<span class=\"c-bold\">";
					}
					if(obj.CheckState == 1) {
						colorHtml = "<span class=\"c-bold c-green\">";
						diffPrice = getDiffPrice(obj.HisPrice,obj.MerchPrice);
					}
					if(obj.CheckState == -1) {
						colorHtml = "<span class=\"c-bold c-red\">";
						diffPrice = getDiffPrice(obj.HisPrice,obj.MerchPrice);
					}
					str +="<td>" + colorHtml +arr.checkState+"</span></td>";
					str +="<td>" + colorHtml + Commonjs.centToYuan(diffPrice) +"</td>";
					str +="<td>"+dateTime+"</td>";
					str +="<td><a href=\"javascript:findBillDetail('"+obj.Id+"','"+obj.DealWay+"');\" class=\"alinks-unline alinks-blue\">查看</a></td>";
					str +="</tr>";
				}
			}else{
				Commonjs.alert("没有查询到有效的对账数据!");
			}
		}else{
			//异常提示
			PageModel(0,pageSize,'pager');
			Commonjs.alert(d.respMessage);
		}
		$("#billList").html(str + "</tbody>");
	});
}

//获取异常账单统计数据
function findBillCountHtml(){
	var Service = {};
	var queryDate = $('#queryDate').val();
	if(Commonjs.isEmpty(queryDate)){
		var startTime = Commonjs.getDate(0);
		queryDate = startTime + " ~ " + startTime;
		$('#queryDate').val(queryDate);
	}
	var date = queryDate.split("~");
	var startDate = date[0];
	var endDate = date[1];
	var payType = $("#payType").val();
	var channelId = $("#channelId").val();
	var configKey = $("#configKey").val();
	var transType = $("input[name='transType']:checked").val();
	
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.PayType = payType;
	Service.ChannelId = channelId;
	Service.TransType = transType;
	Service.ConfigKey = configKey;
	
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/bill/queryExceptionBillCount.do',param,function(d){
		if(d.RespCode==10000){
			billCountInfo = d.Data;
			$('#all-deal').html(billCountInfo[0].TotalCount);
			$('#is-deal').html(billCountInfo[0].IsDealCount);
			$('#no-deal').html(billCountInfo[0].NoDealCount);
		}
	});
}

//获取所有的支付方式
function findAllPayType(){
	var Service = {};
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync("/channel/queryAllPayType.do",param,function(d){
		if(d.RespCode==10000){
			payTypeInfoList = d.Data;
		}
	});
}

//查看账单详情
function findBillDetail(billCheckId, dealWay){
	var payType = $("#payType").val();
	$("#payMenthod").val(payType);
	$("#pagenumber_").val(pagenumber);
	var isDeal = $('#isDeal').val();
	window.location.href = "../bill/bill_detail.html?BillCheckId="+billCheckId+"&IsDeal="+isDeal;
}

//数据格式化
function formateData(hisOrderType, checkState, channelOrderType, dealState){
	var arr = {};
	arr.hisOrderType = (hisOrderType==1?"应收":"应退");
	arr.checkState = (checkState=="0"?"账平":(checkState=="1"?"长款":(checkState=="-1"?"短款":"其他")));
	if(dealState == '1'){
		arr.checkState = "账平(处置后)";
	}
	arr.channelOrderType = (channelOrderType==1?"实收":"实退");
	return arr;
}

//分页
function PageModel(totalcounts, pagecount, pager) {
	$("#"+pager).pager( {
		totalcounts : totalcounts,
		pagesize : 10,
		pagenumber : pagenumber,
		pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
		buttonClickCallback : function(al) {
			loadTb(al, totalcounts);
		}
	});
}

function QueryString(val) {
	var uri = window.location.search;
	var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
	return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
}

//获取差价
function getDiffPrice(payAbleMoney, paideMoney){
	if(parseInt(payAbleMoney) > parseInt(paideMoney)){
		return payAbleMoney - paideMoney;
	}else{
		return paideMoney - payAbleMoney;
	}
}
//导出Excel文件
function downloadFile(){
	var queryDate = $('#queryDate').val();
	if(Commonjs.isEmpty(queryDate)){
		var startTime = Commonjs.getDate(-1);
		queryDate = startTime + " ~ " + startTime;
		$('#queryDate').val(queryDate);
	}
	var date = queryDate.split("~");
	var startDate = date[0];
	var endDate = date[1];
	var channelId = $("#channelId").val();
	var payType = $("#payType").val();
	var dealWay = $("#dealWay").val();
	var dealState = 0;
	var isException = $('#isException').val();
	if(!isException || isException=='false'){
		dealState = 1;
	}
	var orderId = $("#orderId").val();
	var merchno = $("#merchno").val();
	var hisOrderno = $("#hisOrderno").val();
	var configKey = $("#configKey").val();
	var transType = $("input[name='transType']:checked").val();
	
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.PayType = payType;
	Service.DealWay = dealWay;
	Service.DealState = dealState;
	Service.OrderId = orderId;
	Service.MerchNo = merchno;
	Service.HisOrderNo = hisOrderno;
	Service.ConfigKey = configKey;
	Service.TransType = transType;
	
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/bill/downloadExceptionBillData.do",param, function(d){
		if(d.RespCode==10000){
			var filePath = d.Data[0].FilePath;
			window.location.href = location.protocol + '//' + location.host + "/" + filePath + "?token=" + Commonjs.getToken();
			Commonjs.alert("下载完成!");
		}
	});
}