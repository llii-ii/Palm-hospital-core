$(function(){
	var nowYear = Commonjs.getYear(0);
	$("#dateChoo").val(nowYear + "年");
	$(".icon-prev").click(function(){
		var dateChoo = $("#dateChoo").val();
		dateChoo = dateChoo.replace("年","");
		var date = Commonjs.addDateFromNow(dateChoo,"Y",-1);
		$("#dateChoo").val(date + "年");
	});
	$(".icon-next").click(function(){
		var dateChoo = $("#dateChoo").val();
		dateChoo = dateChoo.replace("年","");
		var date = Commonjs.addDateFromNow(dateChoo,"Y",1);
		$("#dateChoo").val(date + "年");
	});
	var dateType = $('#dateType').val();
	//初始化日期组件
	setTime('.datetimepicker', 'month');
	$(".dateType").click(function(){
		var id = $(this).attr("id");
		if(id == 'day'){
			window.location.href = "../bill/bill-index.html";
		}else if(id == 'month'){
			window.location.href = "../bill/bill-list-month.html";
		}
	});
	loadTb();
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
	loadTb();
}

function loadTb(){
	var year = $("#dateChoo").val();
	var queryYear = year.replace("年","");
	var nowMonth = Commonjs.getMonth(0);
	var nowYear = Commonjs.getYear(0);
	var monthArr = buildMonth();
	var map = buildData();
	var billInfoHtml = "";
	for(var i=0; i<3; i++){
		billInfoHtml += "<tr>";
		for(var j=0; j<12; j=j+3){
			var checkMonthClass = " b-none";
			var mm = i+1+j;
			var queryMonth = mm;
			if(Number(nowYear) == Number(queryYear) && queryMonth == Number(nowMonth)){
				checkMonthClass = "";
			}
			billInfoHtml += "<td><div class=\"blc-div "+ checkMonthClass +"\">";
			billInfoHtml += "<div class=\"b-date\">"+(mm)+"月</div>";
			var date = year + mm + "月";
			var key = mm+"";
			if(map != undefined && map.get(key) != undefined && map.get(key) != "undefined"){
				var billInfo = map.get(key);
				var checkState = (billInfo.CheckState==0)?"账平":"账不平";
				var checkClass = (billInfo.CheckState==0)?'class=\"c-48cf8f\"':'class=\"c-f00\"';
				billInfoHtml += "<a href=\"javascript:downloadFile("+queryYear+","+queryMonth+");\" class=\"b-down\" title=\"下载\"></a>";
				billInfoHtml += "<div class=\"b-account\">";
				billInfoHtml += "<div class=\"ba-jt\"></div>";
				billInfoHtml += "<div class=\"ba-con\">";
				billInfoHtml += "<div class=\"clearfix\">";
				billInfoHtml += "<dl>";
				billInfoHtml += "<dt>"+date+"</dt>";
				billInfoHtml += "<dd>医院流水:"+billInfo.HisCount+"笔</dd>";
				billInfoHtml += "<dd>渠道流水:"+billInfo.MerchCount+"笔</dd>";
				billInfoHtml += "</dl>";
				billInfoHtml += "<dl>";
				billInfoHtml += "<dt>对账结果:<span "+checkClass+">"+checkState+"</span></dt>";
				billInfoHtml += "<dd>医院应净收:"+Commonjs.centToYuan(billInfo.HisMoney)+"元</dd>";
				billInfoHtml += "<dd>渠道实净收:"+Commonjs.centToYuan(billInfo.MerchMoney)+"元</dd>";
				billInfoHtml += "</dl>";
				billInfoHtml += "</div>";
				billInfoHtml += "<div class=\"ba-more\"><a href=\"javascript:gotoDetailList("+queryYear+","+queryMonth+");\">查看明细</a></div>";
				billInfoHtml += "</div>";
				billInfoHtml += "</div>";
			}else{
				var descr = "无数据";
				if(Number(queryYear) > Number(nowYear) || (Number(queryYear) == Number(nowYear) && queryMonth > Number(nowMonth))){
					descr = "账单未出";
				}
				billInfoHtml += "<div class=\"b-tip\">"+descr+"</div>";
				billInfoHtml += "<div class=\"b-account-none\">";
				billInfoHtml += "<div class=\"ba-jt\"></div>";
				billInfoHtml += "<div class=\"ba-con\">";
				billInfoHtml += "<i class=\"ba-warn\"></i>暂无汇总数据";
				billInfoHtml += "</div>";
				billInfoHtml += "</div>";
			}
			
			billInfoHtml += "</div></td>";
		}
		billInfoHtml += "</tr>";
	}
		
	$("#billInfo").html(billInfoHtml);
}

function buildData(){
	var map = new Map();
	var queryYear = $("#dateChoo").val();
	queryYear = queryYear.replace("年","");
	var Service = {};
	Service.QueryYear = queryYear;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/billRf/queryBillRFListForMonth.do',param,function(d){
		if(d.RespCode == 10000){
			var billInfoList = d.Data;
			if(!Commonjs.isEmpty(billInfoList) && billInfoList.length > 0){
				for(var i=0;i<billInfoList.length;i++){
					var billInfo = billInfoList[i];
					var month = billInfo.TransMonth;
					map.set(month, billInfo);
				}
			}
		}
	});
	return map;
}

function buildMonth(){
	var monthArr = new Array();
	monthArr[0] = "January";
	monthArr[1] = "April";
	monthArr[2] = "July";
	monthArr[3] = "October";
	
	monthArr[4] = "February";
	monthArr[5] = "May";
	monthArr[6] = "August";
	monthArr[7] = "November";
	
	monthArr[8] = "March";
	monthArr[9] = "June";
	monthArr[10] = "September";
	monthArr[11] = "December";
	return monthArr;
}
function gotoDetailList(queryYear, queryMonth){
	var startDate = Commonjs.getBeginDateForMonth(queryYear, queryMonth, 1);
	var endDate = Commonjs.getEndDateForMonth(queryYear, queryMonth);
	if("59" == Commonjs.getOrgId()){
		window.location.href = "/business_59/bill/bill-list-date.html?StartDate="+ startDate + "&EndDate=" + endDate;
		return;
	}
	window.location.href = "../bill/bill-list-date.html?StartDate="+ startDate + "&EndDate=" + endDate;
}
//导出Excel文件
function downloadFile(queryYear, queryMonth){
	var startDate = Commonjs.getBeginDateForMonth(queryYear, queryMonth);
	var endDate = Commonjs.getEndDateForMonth(queryYear, queryMonth);
	var checkState = 999;
	
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.CheckState = checkState;
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