var QueryDate = undefined;
$(function(){
	QueryDate = QueryString('QueryDate');
	if(Commonjs.isEmpty(QueryDate)){
		getCurrentDate();
	}
	$('#queryDate').val(QueryDate);
	initData();
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
	initData();
}

function initData(){
	var queryDate = $('#queryDate').val();
	var transType = $("input[name='transType']:checked").val();
	
	var param = {};
	var Service = {};
	Service.QueryDate = queryDate;
	Service.OrderRule = 1;
	Service.TransType = transType;
	
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/billReport/queryChannelBankCheckList.do',param,function(d){
		var str = "<thead><tr><th>关联银行</th><th>银行账号</th><th>交易场景</th><th>服务类型</th><th>支付方式</th><th>医院应净收</th><th>渠道实净收</th><th>差额(渠道实净收-医院应净收)</th><th>对账结果</th><th>查看</th></tr></thead><tbody>";
		if(d.RespCode == 10000) {
			if(Commonjs.ListIsNull(d.Data)) {
				var paideMoneySum = 0;
				var payAbleMoneySum = 0;
				var paideMoney = 0;
				var payAbleMoney = 0;
				var checkState = 1;
				var checkState_ = 1;
				d.Data[d.Data.length] = {};
				Commonjs.BaseForeach(d.Data, function(i, item) {
					var bankName = item.BankName;
					if(item.CheckState==0){
						checkState_ = 0;
						checkState = 0;
					}
					if(i==0){
						paideMoney = item.PaideMoney;
						payAbleMoney = item.PayAbleMoney;
					}else{
						var beforeObj = d.Data[i-1];
						var bankName_ = beforeObj.BankName;
						if(bankName != bankName_){
							str+="<tr>"
								+"<td><span class=\"c-bold\">小计</span></td>"
								+"<td><span class=\"c-bold\">--</span></td>"
								+"<td><span class=\"c-bold\">--</span></td>"
								+"<td><span class=\"c-bold\">--</span></td>"
								+"<td><span class=\"c-bold\">--</span></td>"
								+"<td><span class=\"c-bold\">"+Commonjs.centToYuan(payAbleMoney)+"</span></td>"
								+"<td><span class=\"c-bold\">"+Commonjs.centToYuan(paideMoney)+"</span></td>"
								+"<td><span class=\"c-bold\">"+Commonjs.centToYuan(paideMoney - payAbleMoney)+"</span></td>"
								+"<td colspan=\"2\"><span class=\"c-bold "+(checkState==0?"c-red":"c-green")+"\">"+(checkState==1?"账平":"账不平")+"</span></td>"
							+"</tr>";
							paideMoney = item.PaideMoney;
							payAbleMoney = item.PayAbleMoney;
							checkState = 1;
						}else{
							paideMoney += item.PaideMoney;
							payAbleMoney += item.PayAbleMoney;
						}
					}
					if(i < d.Data.length-1){
						paideMoneySum += item.PaideMoney;
						payAbleMoneySum += item.PayAbleMoney;
						str+="<tr>"
								+"<td><span class=\"c-bold\">"+item.BankName+"</span></td>"
								+"<td>"+item.BankNo+"</td>"
								+"<td>"+item.ChannelName+"</td>"
								+"<td>"+item.TransType+"</td>"
								+"<td>"+item.PayMethodName+"</td>"
								+"<td>"+Commonjs.centToYuan(item.PayAbleMoney)+"</td>"
								+"<td>"+Commonjs.centToYuan(item.PaideMoney)+"</td>"
								+"<td>"+Commonjs.centToYuan(item.PaideMoney- item.PayAbleMoney)+"</td>"
								+"<td><span class=\"c-bold "+(item.CheckState==0?"c-red":"c-green")+"\">"+(item.CheckState==1?"账平":(item.CheckState==2?"处置后账平":"账不平"))+"</span></td>"
								+"<td><a href=\"javascript:findBillDetail('"+item.ChannelId+"','"+item.Service+"','"+item.ConfigKey+"',"+item.CheckState+");\" class=\"alinks-unline alinks-blue\">查看明细</a></td>"
							+"</tr>";
					}
				});
				str+="<tr>"
					+"<td><span class=\"c-bold\">总计</span></td>"
					+"<td><span class=\"c-bold\">--</span></td>"
					+"<td><span class=\"c-bold\">--</span></td>"
					+"<td><span class=\"c-bold\">--</span></td>"
					+"<td><span class=\"c-bold\">--</span></td>"
					+"<td><span class=\"c-bold\">"+Commonjs.centToYuan(payAbleMoneySum)+"</span></td>"
					+"<td><span class=\"c-bold\">"+Commonjs.centToYuan(paideMoneySum)+"</span></td>"
					+"<td><span class=\"c-bold\">"+Commonjs.centToYuan(paideMoneySum - payAbleMoneySum)+"</span></td>"
					+"<td colspan=\"2\"><span class=\"c-bold "+(checkState_==0?"c-red":"c-green")+"\">"+(checkState_==1?"账平":"账不平")+"</span></td>"
				+"</tr>";
			}else{
				Commonjs.alert("未查询到有效数据");
			}
		}else{
			//异常提示
			Commonjs.alert(d.RespMessage);
		}
		$("#bankBillrfList").html(str + "</tbody>");
		$("#bankBillrfList").rowspan(0);
	});
}

//查询明细
function findBillDetail(channelId, service, configKey, checkState){
	var queryDate = $('#queryDate').val();
	if(checkState == 0){
		if("59" == Commonjs.getOrgId()){
			window.location.href = "/business_59/bill/bill-list-exception.html?TransChannelId="+channelId+"&QueryDate="+queryDate
			+"&TransType="+service+"&ConfigKey="+configKey;
			return;
		}
		window.location.href = "../bill/bill-list-exception.html?TransChannelId="+channelId+"&QueryDate="+queryDate
		+"&TransType="+service+"&ConfigKey="+configKey;
	}else{
		if("59" == Commonjs.getOrgId()){
			window.location.href = "/business_59/bill/bill-list-date.html?TransChannelId="+channelId+"&QueryDate="+queryDate
			+"&TransType="+service+"&ConfigKey="+configKey;
			return;
		}
		window.location.href = "../bill/bill-list-date.html?TransChannelId="+channelId+"&QueryDate="+queryDate
		+"&TransType="+service+"&ConfigKey="+configKey;
		
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
	var transType = $("input[name='transType']:checked").val();
	
	var param = {};
	var Service = {};
	Service.QueryDate = queryDate;
	Service.OrderRule = 1;
	Service.TransType = transType;
	
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/billReport/downloadChannelBankReportListData.do",param, function(d){
		if(d.RespCode==10000){
			var filePath = d.Data[0].FilePath;
			window.location.href = location.protocol + '//' + location.host + "/" + filePath + "?token=" + Commonjs.getToken();
			Commonjs.alert("下载完成!");
		}
	});
}