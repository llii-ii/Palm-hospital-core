var billInfoList = undefined;
var channelInfoList = undefined;
var payTypeInfoList = undefined;
var billCountInfo = new Array();
var pagenumber = undefined;
var StartDate = undefined;
var EndDate = undefined;
var QueryDate = undefined;
var CheckState = undefined;
var TransChannelId = undefined;
var PayMenthod = undefined;
var TransType = undefined;
var ConfigKey = undefined;

$(function(){
	StartDate = QueryString('StartDate');
	EndDate = QueryString('EndDate');
	TransChannelId = QueryString('TransChannelId');
	PayMenthod = QueryString('PayMenthod');
	CheckState = QueryString('CheckState');
	QueryDate = QueryString('QueryDate');
	TransType = QueryString('TransType');
	ConfigKey = QueryString('ConfigKey');
	
	if(!Commonjs.isEmpty(PayMenthod)){
		$("#payMenthod").val(PayMenthod);
	}
	if(!Commonjs.isEmpty(TransChannelId)){
		$("#transChannelId").val(TransChannelId);
	}
	if(!Commonjs.isEmpty(TransType)){
		$("#transType_").val(TransType);
	}
	if(!Commonjs.isEmpty(ConfigKey)){
		$("#configKey").val(ConfigKey);
	}
	pagenumber = $("#pagenumber_").val();
	findAllChennel();
	$("#channelId").append("<option value=\"\">全部交易场景</option>");
	if(!Commonjs.isEmpty(channelInfoList) && channelInfoList.length > 0){
		for(var i=0;i<channelInfoList.length;i++){
			var obj = channelInfoList[i];
			var $chennelSel = $("<option value=\""+obj.ChannelId+"\">"+obj.ChannelName+"</option>");
			$("#channelId").append($chennelSel);
		}
	}
	findAllPayType();
	$("#payType").append("<option value=\"\">全部支付方式</option>");
	if(!Commonjs.isEmpty(payTypeInfoList) && payTypeInfoList.length > 0){
		for(var i=0;i<payTypeInfoList.length;i++){
			var obj = payTypeInfoList[i];
			var $payTypeSel = $("<option value=\""+obj.PayType+"\">"+obj.PayTypeName+"</option>");
			$("#payType").append($payTypeSel);
		}
	}
	var transChannelId = $("#transChannelId").val();
	if(!Commonjs.isEmpty(transChannelId)){
		$("#channelId").find("option[value='"+transChannelId+"']").attr("selected","selected");
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
		//var queryDate = Commonjs.getDateAdd(QueryDate,1);
		$('#queryDate').val(QueryDate + " ~ " + QueryDate);
	}
	$("#initCheckState").val(CheckState);
	if(!Commonjs.isEmpty(CheckState) && CheckState == 1){
		$("#checkState").val(0);
	}
	findBillCountHtml();
	loadTb(pagenumber,0);
});

//搜索按钮
function search(){
	
	findBillCountHtml();
	$('#totalData').html(billCountInfo.TotalCount);
	$('#0Data').html(billCountInfo.CheckState0Count);
	$('#1Data').html(billCountInfo.CheckState1Count);
	$('#T1Data').html(billCountInfo.CheckStateT1Count);
	loadTb(1,0);
}

function loadTb(index, totalCount){
	pagenumber = index;
	$("#pagenumber_").val(index);
	//对账明细列表的查询条件
	var pageIndex = index;
	var pageSize = 10;
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
	var checkState = $("#checkState").val();
	var initCheckState = $("#initCheckState").val();
	var orderId = $("#orderId").val();
	var merchno = $("#merchno").val();
	var hisOrderno = $("#hisOrderno").val();
	var orderRule = $("#orderRule").val();
	var configKey = $("#configKey").val();
	var transType = $("input[name='transType']:checked").val();
	
	var Service = {};
	var page = {};
	page.PIndex = pageIndex;
	page.PSize = pageSize;
	Service.Page = page;
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.ChannelId = channelId;
	Service.PayType = payType;
	Service.OrderRule = orderRule;
	Service.CheckState = checkState;
	if(!Commonjs.isEmpty(initCheckState)){
		Service.InitCheckState = initCheckState;
	}
	Service.OrderId = orderId;
	Service.MerchNo = merchno;
	Service.HisOrderNo = hisOrderno;
	Service.ConfigKey = configKey;
	Service.TransType = transType;
	
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/bill/queryBillList.do',param,function(d){
		var str = "<thead><tr><th>订单交易时间</th><th>订单类型</th><th>订单号</th><th>医院流水号</th><th>渠道流水号</th><th>应收/退</th><th>应收/退金额（元）</th><th>支付方式</th><th>实收/退</th><th>实收/退金额（元）</th><th>对账结果</th><th>差错金额</th><th>对账时间</th><th>详情</th></tr></thead><tbody>";
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
			PageModel(totalCount, page.PSize,'pager');
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
					str +="<td>"+obj.CreateDate+"</td>";
					str +="<td><a href=\"javascript:findBillDetail('"+obj.Id+"','"+obj.DealWay+"');\" class=\"alinks-unline alinks-blue\">查看</a></td>";
					str +="</tr>";
				}
			}else{
				Commonjs.alert("没有查询到有效的对账数据!");
			}
		}else{
			//异常提示
			PageModel(0,pageSize,'pager');
			Commonjs.alert(d.RespMessage);
		}
		$("#billList").html(str + "</tbody>");
	});
}

//获取账单统计数据
function findBillCountHtml(){
	var Service = {};
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
	var configKey = $("#configKey").val();
	var transType = $("input[name='transType']:checked").val();
	
	Service.ChannelId = channelId;
	Service.PayType = payType;
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.ConfigKey = configKey;
	Service.TransType = transType;
	
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/bill/queryBillCount.do',param,function(d){
		if(d.RespCode==10000){
			billCountInfo = d.Data[0];
			$('#totalData').html(billCountInfo.TotalCount);
			$('#0Data').html(billCountInfo.CheckState0Count);
			$('#1Data').html(billCountInfo.CheckState1Count);
			$('#T1Data').html(billCountInfo.CheckStateT1Count);
		}
	});
}

//获取所有的渠道场景
function findAllChennel(){
	var Service = {};
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync("/channel/queryAllChennel.do",param,function(d){
		if(d.RespCode==10000){
			channelInfoList = d.Data;
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
	var channelId = $("#channelId").val();
	$("#transChannelId").val(channelId);
	var payType = $("#payType").val();
	$("#payMenthod").val(payType);
	var isDeal = false;
	if(!Commonjs.isEmpty(dealWay)){
		isDeal = true;
	}
	$("#pagenumber_").val(pagenumber);
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
function PageModel(totalcounts, pagecount,pager) {
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
//获取差价
function getDiffPrice(payAbleMoney, paideMoney){
	if(parseInt(payAbleMoney) > parseInt(paideMoney)){
		return payAbleMoney - paideMoney;
	}else{
		return paideMoney - payAbleMoney;
	}
}
function QueryString(val) {
	var uri = window.location.search;
	var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
	return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
}

//导出Excel文件
function downloadFile(){
	var queryDate = $('#queryDate').val();
	if(Commonjs.isEmpty(queryDate)){
		var startTime = Commonjs.getDate(0);
		queryDate = startTime + " ~ " + startTime;
		$('#queryDate').val(queryDate);
	}
	var date = queryDate.split("~");
	var startDate = date[0];
	var endDate = date[1];
	var channelId = $("#channelId").val();
	var payType = $("#payType").val();
	var checkState = $("#checkState").val();
	var initCheckState = $("#initCheckState").val();
	var orderId = $("#orderId").val();
	var merchno = $("#merchno").val();
	var hisOrderno = $("#hisOrderno").val();
	var configKey = $("#configKey").val();
	var transType = $("input[name='transType']:checked").val();
	
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.ChannelId = channelId;
	Service.PayType = payType;
	Service.CheckState = checkState;
	if(!Commonjs.isEmpty(initCheckState)){
		Service.InitCheckState = initCheckState;
	}
	Service.OrderId = orderId;
	Service.Merchno = merchno;
	Service.HisOrderno = hisOrderno;
	Service.ConfigKey = configKey;
	Service.TransType = transType;
	
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/bill/downloadBillDetailListData.do",param, function(d){
		if(d.RespCode==10000){
			var filePath = d.Data[0].FilePath;
			window.location.href = location.protocol + '//' + location.host + "/" + filePath + "?token=" + Commonjs.getToken();
			Commonjs.alert("下载完成!");
		}
	});
}
function checkDateType(dateType){
	window.location.href = "/bill/init.do?dateType=" + dateType;
}