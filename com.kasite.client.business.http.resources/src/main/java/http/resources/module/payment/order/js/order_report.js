var payTypeInfoList = undefined;
var orderReportList = undefined;
$(function(){
	var nowTime = Commonjs.getDate(-1);
	$("#queryDate").val(nowTime + " ~ " + nowTime);
	findAllPayType();
	if(!Commonjs.isEmpty(payTypeInfoList) && payTypeInfoList.length > 0){
		for(var i=0;i<payTypeInfoList.length;i++){
			var obj = payTypeInfoList[i];
			var selected = "";
			if(i==0){
				selected = "selected";
			}
			var $payTypeSel = $("<option "+selected+" value=\""+obj.PayType+"\">"+obj.PayTypeName+"</option>");
			$("#payType").append($payTypeSel);
		}
	}
	loadOrderReportTb();
	
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

//搜索按钮
function search(){
	loadOrderReportTb();
}
//订单交易日报表数据
function loadOrderReportTb(){
	var startDate = "";
	var endDate = "";
	var queryDate = $('#queryDate').val();
	if(!Commonjs.isEmpty(queryDate)){
		var date = queryDate.split("~");
		startDate = date[0];
		endDate = date[1];
	}
	var payType = $("#payType").val();
	var payTypeName = $("#payType option:selected").text();
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.PayType = payType;
	Service.OrderState = 999;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/orderReport/queryTransCountList.do',param,function(d){
		var payTimeLimit = d.Data[0].PayTimeLimit
		var str = "<thead><tr><th colspan='6' style='font-size: 16px;'>智付系统-"+payTypeName+"报表</th></tr><tr><td colspan='3'>交易日期   : "+queryDate+"</td><td colspan='3'>交易时间  : "+payTimeLimit+"</td></tr>"+
		"<tr><th>交易渠道</th><th>交易类型</th><th>交易笔数</th><th>交易金额</th><th>待冲正笔数</th><th>待冲正金额</th></tr></thead><tbody>";
		if(d.RespCode == 10000){
			orderReportList = d.Data[0].OrderReport;
			var transCountA = 0;
			var transMoneyA = 0;
			var reverseCountA = 0;
			var reverseMoneyA = 0;
			var outpatientArr = [0,0,0,0];
			var hospitalizationArr = [0,0,0,0];
			$.each(orderReportList, function (index,item) {
				var transCount = 0;
				var transMoney = 0;
				var reverseCount = 0;
				var reverseMoney = 0;
				for(var i=0;i<item.length;i++){
					var clazz = "";
					if(item[i].serviceId.indexOf("门诊") > -1){
						clazz = "MZCZ";
						outpatientArr[0] += item[i].transCount;
						outpatientArr[1] += Number(item[i].transMoney);
						if(i>1){
							outpatientArr[2] = "--";
							outpatientArr[3] = "--";
						}else{
							outpatientArr[2] += item[i].reverseCount;
							outpatientArr[3] += Number(item[i].reverseMoney);
						}
					}else if(item[i].serviceId.indexOf("住院") > -1){
						clazz = "ZYCZ";
						hospitalizationArr[0] += item[i].transCount;
						hospitalizationArr[1] += Number(item[i].transMoney);
						if(i>1){
							hospitalizationArr[2] = "--";
							hospitalizationArr[3] = "--";
						}else{
							hospitalizationArr[2] += item[i].reverseCount;
							hospitalizationArr[3] += Number(item[i].reverseMoney);
						}
					}
					
					str += "<tr>";
					str +="<td><span class=\"c-bold\">"+index+"</span></td>";
					str +="<td>"+item[i].serviceId+"</td>";
					str +="<td>"+item[i].transCount+"</td>";
					str +="<td>"+Commonjs.centToYuan(item[i].transMoney)+"</td>";
					if(i>1){
						str +="<td>--</td>";
						str +="<td>--</td>";
					}else{
						reverseCount = reverseCount + item[i].reverseCount;
						reverseMoney = reverseMoney + Number(item[i].reverseMoney);
						str +="<td>"+item[i].reverseCount+"</td>";
						str +="<td>"+Commonjs.centToYuan(item[i].reverseMoney)+"</td>";
					}
					transCount = transCount + item[i].transCount;
					transMoney = transMoney + Number(item[i].transMoney);
					str +="</tr>";
					
					/*for(var j=0;j<item[i].respOrderReportList.length;j++){
						var reportDetail = item[i].respOrderReportList[j];
						if(i<1){
							str += "<tr class="+clazz+">";
								str +="<td><span class=\"c-bold\">"+index+"</span></td>";
								str +="<td>&nbsp;&nbsp;&nbsp;&nbsp;"+reportDetail.serviceValue+"</td>";
								str +="<td>&nbsp;&nbsp;&nbsp;&nbsp;"+reportDetail.transCount+"</td>";
								str +="<td>&nbsp;&nbsp;&nbsp;&nbsp;"+Commonjs.centToYuan(reportDetail.transMoney)+"</td>";
								str +="<td>&nbsp;&nbsp;&nbsp;&nbsp;"+reportDetail.reverseCount+"</td>";
								str +="<td>&nbsp;&nbsp;&nbsp;&nbsp;"+Commonjs.centToYuan(reportDetail.reverseMoney)+"</td>";
							str +="</tr>";
						}
					}*/
				}
				str += "<tr>";
				str +="<td><span class=\"c-bold\">"+index+"</span></td>";
				str +="<td>小计</td>";
				str +="<td>"+transCount+"</td>";
				str +="<td>"+Commonjs.centToYuan(transMoney)+"</td>";
				str +="<td>"+reverseCount+"</td>";
				str +="<td>"+Commonjs.centToYuan(reverseMoney)+"</td>";
				str +="</tr>";
				
				transCountA = transCountA + transCount;
				transMoneyA = transMoneyA + Commonjs.centToYuan(transMoney);
				reverseCountA = reverseCountA + reverseCount;
				reverseMoneyA = reverseMoneyA + Commonjs.centToYuan(reverseMoney);
			});
			/*********************门诊合计***************/
			str += "<tr>";
			str +="<td><span class=\"c-bold\">合计<span></td>";
			str +="<td>门诊合计</td>";
			str +="<td><span class=\"c-bold c-green\">"+outpatientArr[0]+"</span></td>";
			str +="<td><span class=\"c-bold c-green\">"+Commonjs.centToYuan(outpatientArr[1])+"</span></td>";
			str +="<td><span class=\"c-bold c-red\">"+outpatientArr[2]+"</span></td>";
			str +="<td><span class=\"c-bold c-red\">"+Commonjs.centToYuan(outpatientArr[3])+"</span></td>";
			str +="</tr>";
			/*********************住院合计***************/
			str += "<tr>";
			str +="<td><span class=\"c-bold\">合计<span></td>";
			str +="<td>住院合计</td>";
			str +="<td><span class=\"c-bold c-green\">"+hospitalizationArr[0]+"</span></td>";
			str +="<td><span class=\"c-bold c-green\">"+Commonjs.centToYuan(hospitalizationArr[1])+"</span></td>";
			str +="<td><span class=\"c-bold c-red\">"+hospitalizationArr[2]+"</span></td>";
			str +="<td><span class=\"c-bold c-red\">"+Commonjs.centToYuan(hospitalizationArr[3])+"</span></td>";
			str +="</tr>";
			/*********************总合计***************/
			str += "<tr>";
			str +="<td><span class=\"c-bold\">合计<span></td>";
			str +="<td>总合计</td>";
			str +="<td><span class=\"c-bold c-green\">"+transCountA+"</span></td>";
			str +="<td><span class=\"c-bold c-green\">"+transMoneyA+"</span></td>";
			str +="<td><span class=\"c-bold c-red\">"+reverseCountA+"</span></td>";
			str +="<td><span class=\"c-bold c-red\">"+reverseMoneyA+"</span></td>";
			str +="</tr>";
		}else{
		//异常提示
		Commonjs.alert("没有查询到当日交易场景下的订单对账报表信息!");
		}
		$("#orderReportList").html(str + "</tbody>");
		$("#orderReportList").rowspan(0);
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

function QueryString(val) {
	var uri = window.location.search;
	var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
	return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
}
//导出Excel文件
function downloadFile(){
	var startDate = "";
	var endDate = "";
	var queryDate = $('#queryDate').val();
	if(!Commonjs.isEmpty(queryDate)){
		var date = queryDate.split("~");
		startDate = date[0];
		endDate = date[1];
	}
	var payType = $("#payType").val();
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.PayType = payType;
	
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/orderReport/downloadOrderReportData.do",param, function(d){
		if(d.RespCode==10000){
			var filePath = d.Data[0].FilePath;
			window.location.href = location.protocol + '//' + location.host + "/" + filePath + "?token=" + Commonjs.getToken();
			Commonjs.alert("下载完成!");
		}
	});
}
//打印
function doPrint1() {  
    var bdhtml=window.document.body.innerHTML;   
    sprnstr="<!--startprint-->";   
    eprnstr="<!--endprint-->";   
    prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17);   
    prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr)); 
    prnhtml = prnhtml.replace(new RegExp("c-bold c-green","gm"),"");
    prnhtml = prnhtml.replace(new RegExp("c-bold c-red","gm"),"");
    prnhtml = prnhtml.replace("orderReportList","orderReportList_print");
    window.document.body.innerHTML=prnhtml;  
    window.print();  
    window.location.reload();
}
//打印
function doPrint() {  
	$("#orderReportList").printPreview();
	$("#orderReportList").print({iframe:true,prepend:'<br/>'});
}