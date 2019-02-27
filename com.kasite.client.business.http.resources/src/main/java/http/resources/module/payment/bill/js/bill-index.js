var billrfList = undefined;
var pagenumber = 0;
var QueryDate = '';
$(function(){
	QueryDate = QueryString('QueryDate');
	$('#queryDate').val(QueryDate);
	//初始化日期组件
	setTime('.datetimepicker', 'date');
	$(".dateType").click(function(){
		var id = $(this).attr("id");
		if(id == 'day'){
			window.location.href = "../bill/bill-index.html";
		}else if(id == 'month'){
			window.location.href = "../bill/bill-list-month.html";
		}
	});
	loadTb(1,0);
});

function setTime(id, type) {
	laydate.render({
    	elem: id,
    	type: type,
  		range: true
    });
}

//搜索按钮
function search(){
	loadTb(1,0);
}
function loadTb(index, totalCount){
	pagenumber = index;
	//对账明细列表的查询条件
	var pageIndex = index;
	var pageSize = 10;
	var queryDate = $('#queryDate').val();
	var date = queryDate.split("~");
	var startDate = date[0];
	var endDate = date[1];
	var dateType = $('#dateType').val();
	var checkState = $("#checkState").val();
	var Service = {};
	var page = {};
	page.PIndex = pageIndex;
	page.PSize = pageSize;
	Service.Page = page;
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.DateType = dateType;
	Service.CheckState = checkState;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/billRf/queryBillRFListForDate.do',param,function(d){
		var str = "<thead><tr><th>交易时间</th><th>医院流水(笔)</th><th>渠道流水(笔)</th><th>已勾兑(笔)</th><th>医院单边账(笔)</th><th>渠道单边账(笔)</th><th>金额不一致(笔)</th><th>医院应净收(元)</th><th>渠道实净收(元)</th><th>对账结果</th><th>详情</th></tr></thead><tbody>";
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
			PageModel(totalCount,page.PSize,'pager');
			billrfList = d.Data;
			if(!Commonjs.isEmpty(billrfList) && billrfList.length > 0){
				for(var i=0;i<billrfList.length;i++){
					var obj = billrfList[i];
					str += "<tr>";
					str +="<td>"+obj.BillDate+"</td>";
					str +="<td>"+obj.HisBillCount+"</td>";
					str +="<td>"+obj.ChannelBillCount+"</td>";
					str +="<td>"+obj.CheckCount+"</td>";
					str +="<td>"+obj.HisSingleBillCount+"</td>";
					str +="<td>"+obj.ChannelSingleBillCount+"</td>";
					str +="<td>"+obj.DifferPirceCount+"</td>";
					str +="<td><span class=\"c-bold\">"+(parseInt(obj.HisBillMoneySum)/100)+"</span></td>";
					str +="<td><span class=\"c-bold\">"+(parseInt(obj.MerchBillMoneySum)/100)+"</span></td>";
					var colorHtml = "";
					if(obj.DealState == 1) colorHtml = "<span class=\"c-bold c-green\">";
					if(obj.DealState == 0) colorHtml = "<span class=\"c-bold c-red\">";
					if(obj.DealState == 2) colorHtml = "<span class=\"c-bold c-green\">";
					str +="<td>" + colorHtml +(obj.DealState==2?"账平(处置后)":(obj.DealState==0?"账不平":"账平"))+"</span></td>";
					str +="<td><a href=\"javascript:findBillrfDetailList('"+obj.BillDate+"');\" class=\"alinks-unline alinks-blue\">查看报表</a></td>";
					str +="</tr>";
				}
			}else{
				Commonjs.alert("没有查询到有效的对账统计数据!");
			}
		}else{
			//异常提示
			PageModel(0,pageSize,'pager');
			Commonjs.alert(d.RespMessage);
		}
		$("#billrfList").html(str + "</tbody>");
	});
}

//查看当日账单明细
function findBillrfDetailList(billDate){
	if("59" == Commonjs.getOrgId()){
		window.location.href = "/business_59/bill/bill-report.html?QueryDate=" + billDate;
		return;
	}
	window.location.href = "../bill/bill-report.html?QueryDate=" + billDate;
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

function QueryString(val) {
	var uri = window.location.search;
	var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
	return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
}

//导出Excel文件
function downloadFile(){
	var queryDate = $('#queryDate').val();
	var date = queryDate.split("~");
	var startDate = date[0];
	var endDate = date[1];
	var dateType = $('#dateType').val();
	var checkState = $("#checkState").val();
	
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.DateType = dateType;
	Service.CheckState = checkState;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/billRf/downloadBillRFListData.do",param, function(d){
		if(d.RespCode==10000){
			var filePath = d.Data[0].FilePath;
			window.location.href = location.protocol + '//' + location.host + "/" + filePath + "?token=" + Commonjs.getToken();
			Commonjs.alert("下载完成!");
		}
	});
}