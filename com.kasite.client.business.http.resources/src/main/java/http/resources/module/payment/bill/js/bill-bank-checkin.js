var bankCheckInfoList = undefined;
var bankNoList = undefined;
var bankCheckCountInfo = new Array();
var pagenumber = 0;
$(function(){
	findAllBankNo();
	$("#bankNo").append("<option value=\"\">全部银行</option>");
	if(!Commonjs.isEmpty(bankNoList) && bankNoList.length > 0){
		for(var i=0;i<bankNoList.length;i++){
			var obj = bankNoList[i];
			var $bankNoSel = $("<option value=\""+obj.BankNo+"\">"+obj.BankName + ":" + obj.BankNo+"</option>");
			$("#bankNo").append($bankNoSel);
		}
	}
	$("#isCheckOut").change(function(){
		  var isCheckOut = $(this).val();
		  if(isCheckOut == '0'){
			  $("#checkState").attr("disabled", true);  //关闭
		  }else{
			  $("#checkState").removeAttr("disabled", false); //开启
		  }
	});
	findBankCheckCountHtml();
	loadTb(1,0);
	
});

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
	var checkState = $("#checkState").val();
	var isCheckOut = $("#isCheckOut").val();
	var bankNo = $("#bankNo").val();
	
	var Service = {};
	var page = {};
	page.PIndex = pageIndex;
	page.PSize = pageSize;
	Service.Page = page;
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.IsCheckOut = isCheckOut;
	Service.CheckState = checkState;
	Service.BankNo = bankNo;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/bank/queryBankCheckList.do',param,function(d){
		var str = "<thead><tr><th>到款日期</th><th>开户银行</th><th>银行账号</th><th>应到款(元)</th><th>实际到款(元)</th><th>勾兑状态</th><th>勾兑人</th><th>勾兑结果</th><th>差错金额(元)</th><th>详情</th></tr></thead><tbody>";
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
			bankCheckInfoList = d.Data;
			if(!Commonjs.isEmpty(bankCheckInfoList) && bankCheckInfoList.length > 0){
				for(var i=0;i<bankCheckInfoList.length;i++){
					var obj = bankCheckInfoList[i];
					var arr = formateData(obj.CheckState, obj.IsCheck);
					str += "<tr>";
					str +="<td>"+obj.Date+"</td>";
					str +="<td>"+obj.BankName+"</td>";
					str +="<td>"+obj.BankNo+"</td>";
					str +="<td><span class=\"c-bold\">"+Commonjs.centToYuan(obj.PayAbleMoney)+"</span></td>";
					str +="<td><span class=\"c-bold\">"+(obj.IsCheck==0?"--":Commonjs.centToYuan(obj.PaideMoney))+"</span></td>";
					str +="<td>"+arr.isCheck+"</td>";
					str +="<td>"+(obj.IsCheck == 0?"--":(obj.UpdateBy))+"</td>";
					var colorHtml = "";
					if(obj.CheckState == 0) colorHtml = "<span class=\"c-green c-bold\">";
					if(obj.CheckState == 1) colorHtml = "<span class=\"c-blue c-bold\">";
					if(obj.CheckState == -1) colorHtml = "<span class=\"c-red c-bold\">";
					str +="<td>" + colorHtml + arr.checkState + "</span></td>";
					str +="<td>"+ colorHtml + (obj.IsCheck == 0? "" :getDiffPrice(obj.PayAbleMoney,obj.PaideMoney))+"</span></td>";
					if(obj.isCheck == 0){
						str += "<td><a href=\"javascript:checkIn('"+obj.BankName+"','"+obj.BankNo+"','"+obj.Date+"');\" class=\"alinks-unline alinks-blue d-blend\">勾兑</a></td>";
					}else{
						str += "<td><a href=\"javascript:findBankCheckDetail('"+obj.BankName+"','"+obj.BankNo+"','"+obj.Date+"','"+obj.PaideMoney+"','"+obj.BankFlowNo+"','"+obj.UpdateBy+"','"+obj.UpdateDate+"');\" class=\"alinks-unline alinks-blue mr10 d-detail\">查看</a>" +
						"<a href=\"javascript:updateBankCheckDetail('"+obj.BankName+"','"+obj.BankNo+"','"+obj.Date+"','"+obj.PaideMoney+"','"+obj.BankFlowNo+"','"+obj.UpdateBy+"','"+obj.UpdateDate+"');\" class=\"alinks-unline alinks-blue d-change\">修改</a></td>";
					}
					
					str +="</tr>";
				}
			}
		}else{
			//异常提示
			PageModel(0,pageSize,'pager');
			Commonjs.alert(d.RespMessage);
		}
		$("#bankCheckList").html(str + "</tbody>");
	});
}

//获取账单统计数据
function findBankCheckCountHtml(){
	var queryDate = $('#queryDate').val();
	var date = queryDate.split("~");
	var startDate = date[0];
	var endDate = date[1];
	
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/bank/queryBankCheckCount.do',param,function(d){
		if(d.RespCode==10000){
			bankCheckCountInfo = d.Data;
			$('#totalData').html(bankCheckCountInfo[0].TotalCount);
			$('#isCheck0Data').html(bankCheckCountInfo[0].IsCheck0Count);
			$('#isCheck1Data').html(bankCheckCountInfo[0].IsCheck1Count);
			$('#checkState0Data').html(bankCheckCountInfo[0].CheckState0Count);
			$('#checkState1Data').html(bankCheckCountInfo[0].CheckState1Count);
			$('#checkStateT1Data').html(bankCheckCountInfo[0].CheckStateT1Count);
		}
	});
}

//获取所有的银行卡号
function findAllBankNo(){
	var Service = {};
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync("/channel/queryAllBankNo.do",param, function(d){
		if(d.RespCode==10000){
			bankNoList = d.Data;
		}
	});
}

//勾兑
function checkIn(bankName, bankNo, checkTime){
	$("#bankFlowNo1").val("");
	$("#paideMoney1").val("");
	$("#bankName1").html(bankName);
	$("#bankNo1").html(bankNo);
	$("#checkTime1").html(checkTime);
	
	$("#paideMoney1").html("<input type=\"text\" style=\"margin-top: 5px;\" class=\"input-text input-text-w180 mr10\" id=\"paideMoney\" />");
	$("#bankFlowNo1").html("<input type=\"text\" style=\"margin-top: 5px;\" class=\"input-text input-text-w180 mr10\" id=\"bankFlowNo\" />");
	
	var artBox=art.dialog({	
		lock: true,
		width: 400,
		title:'银行实际到款',
		content:$('#dBlend').get(0),
		ok: function () {
			var bankFlowNo = $("#bankFlowNo").val();
			var paideMoney = $("#paideMoney").val();
			saveBankCheckInfo(bankNo, checkTime, bankFlowNo, paideMoney);
			loadTb(1,0);
		},
		cancel: true
	});
}

//查看银行勾兑详情
function findBankCheckDetail(bankName, bankNo, checkTime, paideMoney, bankFlowNo, updateBy, updateDate){
	$("#bankName2").html(bankName);
	$("#bankNo2").html(bankNo);
	$("#checkTime2").html(checkTime);
	$("#paideMoney2").html(Commonjs.centToYuan(paideMoney));
	$("#bankFlowNo2").html(bankFlowNo);
	$("#updateBy").html(updateBy);
	$("#updateDate").html(updateDate);
	var artBox=art.dialog({	
		lock: true,
		width: 400,
		title:'银行到款勾兑',
		content:$('#dDetail').get(0)
	});
}

//修改银行勾兑详情
function updateBankCheckDetail(bankName, bankNo, checkTime, paideMoney, bankFlowNo, updateBy, updateDate){
	$("#bankName2").html(bankName);
	$("#bankNo2").html(bankNo);
	$("#checkTime2").html(checkTime);
	$("#paideMoney2").html("<input type=\"text\" style=\"margin-top: 5px;\" class=\"input-text input-text-w180 mr10\" id=\"paideMoney\" value=\""+Commonjs.centToYuan(paideMoney)+"\">");
	$("#bankFlowNo2").html("<input type=\"text\" style=\"margin-top: 5px;\" class=\"input-text input-text-w180 mr10\" id=\"bankFlowNo\" value=\""+bankFlowNo+"\">");
	$("#updateBy").html(updateBy);
	$("#updateDate").html(updateDate);
	var artBox=art.dialog({	
		lock: true,
		width: 400,
		title:'银行到款勾兑',
		content:$('#dDetail').get(0),
		ok: function () {
			var bankFlowNo = $("#bankFlowNo").val();
			var paideMoney = $("#paideMoney").val();
			saveBankCheckInfo(bankNo, checkTime, bankFlowNo, paideMoney);
			loadTb(1,0);
		},
		cancel: true
	});
}
//保存银行勾兑信息
function saveBankCheckInfo(bankNo, checkTime, bankFlowNo, paideMoney){
	$("#paideMoney").remove();
	$("#bankFlowNo").remove();
	var Service = {};
	Service.BankNo = bankNo;
	Service.Date = checkTime;
	Service.BankFlowNo = bankFlowNo;
	if(Commonjs.isEmpty(paideMoney)){
		paideMoney = 0;
	}
	Service.PaideMoney = paideMoney;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/bank/saveBankCheckInfo.do",param,function(d){
		if(d.RespCode == 10000){
			window.location.reload(); //当前页面 
		}else{
			//异常提示
			Commonjs.alert(d.RespMessage);
		}
	});
}

//导出Excel文件
function downloadFile(){
	var queryDate = $('#queryDate').val();
	var date = queryDate.split("~");
	var startDate = date[0];
	var endDate = date[1];
	var checkState = $("#checkState").val();
	var bankNo = $("#bankNo").val();
	
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.BankNo = bankNo;
	Service.CheckState = checkState;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/bank/downloadBankBillListData.do",param, function(d){
		if(d.RespCode==10000){
			var filePath = d.Data[0].FilePath;
			window.location.href = location.protocol + '//' + location.host + "/" + filePath + "?token=" + Commonjs.getToken();
			Commonjs.alert("下载完成!");
		}
	});
}

//数据格式化
function formateData(checkState, isCheck){
	var arr = {};
	arr.isCheck = (isCheck == 0?"未勾兑":"已勾兑");
	arr.checkState = (checkState == 1?"长款":(checkState == -1?"短款":(checkState == 0?"账平":"--")));
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
	var diffPrice = 0;
	if(parseInt(payAbleMoney) > parseInt(paideMoney)){
		diffPrice = payAbleMoney - paideMoney;
	}else{
		diffPrice = paideMoney - payAbleMoney;
	}
	return diffPrice/100;
}