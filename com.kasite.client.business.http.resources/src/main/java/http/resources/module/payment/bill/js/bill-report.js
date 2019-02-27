var channelBillRFList = undefined;
var merchBillRFList = undefined;
var QueryDate = undefined;
$(function(){
	QueryDate = QueryString('QueryDate');
	if(Commonjs.isEmpty(QueryDate)){
		getCurrentDate();
	}
	$('#queryDate').val(QueryDate);
	loadChannelBillRFListTb();
	loadMerchBillRFListTb();
	
	$("#openType").click(function(){
		
	})
});

function getCurrentDate(){
	var param = {};
	var Service = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/billReport/queryCurrentDate.do',param,function(d){
		QueryDate = d.Data[0].CurrentDate;
	});
}

//搜索按钮
function search(){
	loadChannelBillRFListTb();
	loadMerchBillRFListTb();
}
//交易场景报表数据
function loadChannelBillRFListTb(){
	$(".timestamp").empty();
	var queryDate = $('#queryDate').val();
	if(!Commonjs.isEmpty(queryDate)){
		var time = new Date(queryDate);  
		$(".timestamp").append("<i class=\"icon icon-time\"></i>" + timestampFormat(time, "yyyy年MM月dd日"));
	}
	
	var param = {};
	var Service = {};
	Service.QueryDate = queryDate;
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/billReport/queryBillChannelRFList.do',param,function(d){
		var str = "<thead><tr><th>交易场景</th><th>医院流水(笔)</th><th>渠道流水(笔)</th><th>已勾兑(笔)</th><th>医院单边账(笔)</th><th>渠道单边账(笔)</th><th>金额不一致(笔)</th><th>医院应净收(元)</th><th>渠道实净收(元)</th><th>差额(元)</th><th>对账结果</th><th>详情</th></tr></thead><tbody>";
		if(d.RespCode == 10000){
			channelBillRFList = d.Data;
			if(!Commonjs.isEmpty(channelBillRFList) && channelBillRFList.length > 0){
				var hisBillCountSum = 0;
				var channelBillCountSum = 0;
				var checkCountSum = 0;
				var hisSingleBillCountSum = 0;
				var channelSingleBillCountSum = 0;
				var differPirceCountSum = 0;
				var hisBillSum = 0;
				var merchBillSum = 0;
				var diffMoney = 0;
				var checkStateS = "账平";
				
				var zyHisBillCount = 0;
				var zyChannelBillCount = 0;
				var zyCheckCount = 0;
				var zyHisSingleBillCount = 0;
				var zyChannelSingleBillCount = 0;
				var zyDifferPirceCount = 0;
				var zyHisBillSum = 0;
				var zyMerchBillSum = 0;
				var zyDifferMoney = 0;
				var zyCheckState = "账平";
				var zyCheckColor = 'c-green';
				var zyHtml = '';
				
				for(var i=0;i<channelBillRFList.length;i++){
					var obj = channelBillRFList[i];
					hisBillCountSum += Number(obj.HisBillCount);
					channelBillCountSum += Number(obj.ChannelBillCount);
					checkCountSum += Number(obj.CheckCount);
					hisSingleBillCountSum += Number(obj.HisSingleBillCount);
					channelSingleBillCountSum += Number(obj.ChannelSingleBillCount);
					differPirceCountSum += Number(obj.DifferPirceCount);
					hisBillSum += Number(obj.HisBillSum);
					merchBillSum += Number(obj.MerchBillSum);
					if(obj.CheckState == 0) checkStateS = "账不平";
					str += "<tr>";
					str +="<td>"+obj.ChannelName+"</td>";
					str +="<td>"+obj.HisBillCount+"</td>";
					str +="<td>"+obj.ChannelBillCount+"</td>";
					str +="<td>"+obj.CheckCount+"</td>";
					str +="<td>"+obj.HisSingleBillCount+"</td>";
					str +="<td>"+obj.ChannelSingleBillCount+"</td>";
					str +="<td>"+obj.DifferPirceCount+"</td>";
					str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(obj.HisBillSum)+"</span></td>";
					str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(obj.MerchBillSum)+"</span></td>";
					str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(obj.MerchBillSum-obj.HisBillSum)+"</span></td>";
					str +="<td><span class=\"c-bold "+(obj.DealState==1?"c-green":(obj.CheckState==0?"c-red":"c-green"))+"\">"+(obj.DealState==1?"账平(处置后)":(obj.CheckState==0?"账不平":"账平"))+"</span></td>";
					str +="<td><a href=\"javascript:findChannelBillrfDetail('"+obj.ChannelId+"','"+obj.CheckState+"');\" class=\"alinks-unline alinks-blue\">查看明细</a></td>";
					str +="</tr>";
				}
				str += "<tr>";
				str +="<td><span class=\"c-bold\">合计</span></td>";
				str +="<td><span class=\"c-bold\">"+hisBillCountSum+"</span></td>";
				str +="<td><span class=\"c-bold\">"+channelBillCountSum+"</span></td>";
				str +="<td><span class=\"c-bold\">"+checkCountSum+"</span></td>";
				str +="<td><span class=\"c-bold\">"+hisSingleBillCountSum+"</span></td>";
				str +="<td><span class=\"c-bold\">"+channelSingleBillCountSum+"</span></td>";
				str +="<td><span class=\"c-bold\">"+differPirceCountSum+"</span></td>";
				str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(hisBillSum)+"</span></td>";
				str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(merchBillSum)+"</span></td>";
				str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(merchBillSum-hisBillSum)+"</span></td>";
				str +="<td colspan=\"2\"><span class='c-bold "+(checkStateS=="账平"?"c-green":"c-red")+"'>"+checkStateS+"</span></td>";
				str +="</tr>";
			}else{
				Commonjs.alert("没有查询到当日交易场景下的订单对账报表信息!");
			}
		}else{
			//异常提示
			Commonjs.alert(d.RespMessage);
		}
		$("#channelBillrfList").html(str + "</tbody>");
	});
}

//展开掌医
function openDetail(type){
	var type = $("#openType").attr("val");
	if(type=='1'){
		$(".zy_content").show();
		$("#openType").attr("val",'2');
		$("#openType").text("收起");
	}else if(type=='2'){
		$(".zy_content").hide();
		$("#openType").attr("val",'1');
		$("#openType").text("展开");
	}
}

//支付方式报表数据
function loadMerchBillRFListTb(){
	var queryDate = $('#queryDate').val();	
	var param = {};
	var Service = {};
	Service.QueryDate = queryDate;
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/billReport/queryBillMerchRFList.do',param,function(d){
		var str = "<thead><tr><th>支付方式</th><th>医院商户号</th><th>所属银行</th><th>银行账号</th><th>医院应净收(元)</th><th>渠道实净收(元)</th><th>对账结果</th><th>详情</th></tr></thead><tbody>";
		if(d.RespCode == 10000){
			merchBillRFList = d.Data;
			if(!Commonjs.isEmpty(merchBillRFList) && merchBillRFList.length > 0){
				var hisBillSum = 0;
				var merchBillSum = 0;
				var checkStateS = "账平";
				for(var i=0;i<merchBillRFList.length;i++){
					var obj = merchBillRFList[i];
					hisBillSum += Number(obj.HisBillSum);
					merchBillSum += Number(obj.MerchBillSum);
					if(obj.CheckState == 0) checkStateS = "账不平";
					str += "<tr>";
					str +="<td>"+obj.PayMethodName+"</td>";
					str +="<td>"+obj.Configkey+"</td>";
					str +="<td>"+obj.BankName+"(" + obj.BankShortName + ")</td>";
					str +="<td>" + obj.BankNo + "</td>";
					str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(obj.HisBillSum)+"</span></td>";
					str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(obj.MerchBillSum)+"</span></td>";
					str +="<td><span class=\"c-bold "+(obj.DealState==1?"c-green":(obj.CheckState=="0"?"c-red":"c-green"))+"\">"+(obj.DealState==1?"账平(处置后)":(obj.CheckState=="0"?"账不平":"账平"))+"</span></td>";
					str +="<td><a href=\"javascript:findMerchBillrfDetail('"+obj.PayMethod+"','"+obj.CheckState+"');\" class=\"alinks-unline alinks-blue\">查看明细</a></td>";
					str +="</tr>";
				}
				str += "<tr>";
				str +="<td><span class=\"c-bold\">合计</span></td>";
				str +="<td></td>";
				str +="<td></td>";
				str +="<td></td>";
				str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(hisBillSum)+"</span></td>";
				str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(merchBillSum)+"</span></td>";
				str +="<td colspan=\"2\"><span class='c-bold "+(checkStateS=="账平"?"c-green":"c-red")+"'>"+checkStateS+"</span></td>";
				str +="</tr>";
			}else{
				//异常提示
				Commonjs.alert("没有查询到当日支付方式下的资金对账报表信息!");
			}
		}else{
			//异常提示
			Commonjs.alert(d.RespMessage);
		}
		$("#merchBillrfList").html(str + "</tbody>");
		$("#merchBillrfList").rowspan(0);
	});
}

//查看交易场景账单明细
function findChannelBillrfDetail(transChannelId, checkState){
	var queryDate = $("#queryDate").val();
	if(checkState == 1){
		window.location.href = "../bill/bill-list-date.html?TransChannelId=" + transChannelId + "&QueryDate=" + queryDate;
	}else{
		window.location.href = "../bill/bill-list-exception.html?TransChannelId=" + transChannelId + "&QueryDate=" + queryDate;
	}
}

//查看支付方式账单明细
function findMerchBillrfDetail(payMenthod, checkState){
	var queryDate = $("#queryDate").val();
	if(checkState == 1){
		window.location.href = "../bill/bill-list-date.html?PayMenthod=" + payMenthod + "&QueryDate=" + queryDate;
	}else{
		window.location.href = "../bill/bill-list-exception.html?PayMenthod=" + payMenthod + "&QueryDate=" + queryDate;
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
	var Service = {};
	Service.QueryDate = queryDate;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/billReport/downloadBillReportListData.do",param, function(d){
		if(d.RespCode==10000){
			var filePath = d.Data[0].FilePath;
			window.location.href = location.protocol + '//' + location.host + "/" + filePath + "?token=" + Commonjs.getToken();
			Commonjs.alert("下载完成!");
		}
	});
}