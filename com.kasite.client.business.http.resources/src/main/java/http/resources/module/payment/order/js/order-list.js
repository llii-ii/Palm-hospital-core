var orderList = undefined;
var configKeyList = undefined;
var channelArray = new Array();
var pagenumber = 0;
var ChannelId = undefined;
$(function(){
	pagenumber = $("#pagenumber_").val();
	$('#selectBox').hide();
	ChannelId = QueryString('ChannelId');
	if(!Commonjs.isEmpty(ChannelId)){
		$("#channelId").val(ChannelId);
	}
	var channelId_ = $("#channelId").val();
	var channelArr = channelId_.split(',');
	//获取所有的商户号
	findAllConfigKey();
	$("#configKey_").append("<option selected=\"selected\" value=\"\">全部支付商户</option>");
	if(!Commonjs.isEmpty(configKeyList) && configKeyList.length > 0){
		for(var i=0;i<configKeyList.length;i++){
			var obj = configKeyList[i];
			var $configKeySel = $("<option value=\""+obj.MerchCode+"\">"+ obj.MerchCode + "("+ obj.BankName +")</option>");
			$("#configKey_").append($configKeySel);
		}
	}
	//交易场景下拉复选框
	findAllChannel();
	$('[data-combo="checkbox"]').comboFormSelect({  //初始化
        jsonData:channelArray,
        initialValue:channelArr,
        jsonDataId:'id',
        jsonDataText:'name'
    });
	$('.checkbox').on("click",function(){
		var checkVal = $(this).find("input[type=checkbox]").val();
		if(checkVal == ''){
			var classCss = $(this).attr("class");
			$("input[type=checkbox]").prop("checked",false);
			if(classCss.indexOf("checked") > -1){
				$(".checkbox").removeClass("checked");
				$(this).addClass("checked");
			}
			if(classCss.indexOf("checked") < 0){
				$(".checkbox").addClass("checked");
				$(this).removeClass("checked");
			}
		}
	});
	initLoad();
});

//搜索按钮
function search(){
	$("#pagenumber_").val(1);
	pagenumber = 1;
	initLoad();
}
//初始化页面
function initLoad(){
	var Service = buildService();
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	loadTb(pagenumber,0);
	Commonjs.ajax('/order/queryTotalCountMoney.do',param,function(data){
		if(data.RespCode == 10000){
			var a = new Array();
			a = data.Data;
			var totalMoney = a[0].TotalMoney;
			$('#allPayMoneyCount').html(Commonjs.isEmpty(totalMoney)?0:Commonjs.centToYuan(totalMoney));
		}else{
			Commonjs.alert(data.RespMessage);
		}
	});
}
//封装参数
function buildService(){
	var startDate = "";
	var endDate = "";
	var queryDate = $('#queryDate').val();
	if(Commonjs.isEmpty(queryDate)){
		var startTime = Commonjs.getDate(0);
		var queryDate = startTime + " ~ " + startTime;
		$('#queryDate').val(queryDate);
	}
	var date = queryDate.split("~");
	startDate = date[0];
	endDate = date[1];
	var channel_serial_no = $("#channel_serial_no").val();
	var his_serial_no = $("#his_serial_no").val();
	var orderId = $("#orderId").val();
	var card_no = $("#card_no").val();
	var nick_name = $("#nick_name").val();
	var nick_mobile = $("#nick_mobile").val();
	var channelId = getChannelId();
	var orderState = $("#orderState").val();
	var transType = $("input[name='transType']:checked").val();
	var configKey = $("#configKey_").val();
	
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.Channel_Serial_No = channel_serial_no;
	Service.His_Serial_No = his_serial_no;
	Service.Card_No = card_no;
	Service.Nick_Name = nick_name;
	Service.Nick_Mobile = nick_mobile;
	Service.ChannelId = channelId;
	Service.OrderState = orderState;
	Service.OrderId = orderId;
	Service.TransType = transType;
	Service.ConfigKey = configKey;
	
	return Service;
}
//加载列表数据
function loadTb(index, totalCount){
	pagenumber = index;
	$("#pagenumber_").val(index);
	//对账明细列表的查询条件
	var pageIndex = index;
	var pageSize = 10;
	
	var Service = buildService();
	var page = {};
	page.PIndex = pageIndex;
	page.PSize = pageSize;
	Service.Page = page;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/order/queryAllOrderList.do', param, function(d){
		var str = "<thead><tr><th>交易时间</th><th>渠道流水号</th><th>医院业务流水号</th><th>银行简拼</th><th>就诊卡号/住院号</th><th>患者姓名</th><th>患者手机号</th><th>订单类型</th><th>订单金额</th><th>实付金额</th><th>订单状态</th><th>详情</th></tr></thead><tbody>";
		if(d.RespCode == 10000){
			var page = d.Page;
			if(page != undefined && page.PCount != undefined){
				if(page.PCount!=0){
					totalCount = page.PCount;
				}else{
					if(page.PIndex==0) totalCount = 0;
				}
			}else{
				totalCount = 0;
			}
			PageModel(totalCount, page.PSize, 'pager');
			orderList = d.Data;
			if(!Commonjs.isEmpty(orderList) && orderList.length > 0){
				for(var i=0;i<orderList.length;i++){
					var obj = orderList[i];
					var channelSerialNo = obj.ChannelSerialNo;
					if(!Commonjs.isEmpty(channelSerialNo)){
						channelSerialNo = channelSerialNo.replace(channelSerialNo.substring(3,channelSerialNo.length-3),"***");
					}
					var hisSerialNo = obj.HisSerialNo;
					if(!Commonjs.isEmpty(hisSerialNo)){
						hisSerialNo = hisSerialNo.replace(hisSerialNo.substring(3,hisSerialNo.length-3),"***");
					}
					var cardNo = Commonjs.subCardNo(obj.CardNo);
					str += "<tr>";
					str +="<td>"+obj.TransTime+"</td>";
					str +="<td>"+channelSerialNo+"</td>";
					str +="<td>"+hisSerialNo+"</td>";
					str +="<td>"+ obj.BankName +"("+obj.BankShortName+")</td>";
					str +="<td>"+cardNo+"</td>";
					str +="<td>"+obj.NickName+"</td>";
					str +="<td>"+obj.NickMobile+"</td>";
					str +="<td>"+obj.TransType+"</td>";
					var colorHtml = "";
					if(obj.OrderState != 1 && obj.OrderState != 2) colorHtml="<span class=\"c-bold c-red\">";
					else colorHtml="<span class=\"c-bold c-green\">";
					str +="<td>" + colorHtml + Commonjs.centToYuan(obj.OrderMoney) + "</span></td>";
					str +="<td>" + colorHtml + Commonjs.centToYuan(obj.TransMoney) + "</span></td>";
					str +="<td>" + colorHtml + (obj.OrderState==1?"已支付":(obj.OrderState==2?"已完成":(obj.OrderState==3?"退款中":(obj.OrderState==4?"已退款":"退款失败")))) + "</span></td>";
					str +="<td><a href=\"javascript:findOrderDetail('"+obj.OrderId+"','"+obj.OrderState+"','"+obj.RefundOrderId+"');\" class=\"alinks-unline alinks-blue\">查看</a></td>";
					str +="</tr>";
				}
			}else{
				Commonjs.alert("没有查询到有效的订单数据!");
			}
		}else{
			//异常提示
			PageModel(0,pageSize,'pager');
			Commonjs.alert(d.RespMessage);
		}
		$("#orderList").html(str + "</tbody>");
		$('#allCount').html(totalCount);
	});
}

//查看订单详情
function findOrderDetail(orderId, orderState, refundOrderId){
	var channelId = getChannelId();
	$("#channelId").val(channelId);
	$("#pagenumber_").val(pagenumber);
	window.location.href = "../order/order_detail.html?OrderId=" + orderId + "&OrderState=" + orderState + "&RefundOrderId=" + refundOrderId;
}
//获取所有选中的交易场景
function getChannelId(){
	var channelId = "";
	$("label.checked").each(function(){ 
		var val = $(this).find(":checkbox").val() + ",";
		channelId += val; 
	}) 
	channelId = channelId.substring(0,channelId.length-1);	
	return channelId;
}
//获取所有的商户号
function findAllConfigKey(){
	var param = {};
	param.reqParam = Commonjs.getApiReqParams({});
	Commonjs.ajaxSync("/channel/queryMerchConfigList.do",param,function(d){
		if(d.RespCode==10000){
			configKeyList = d.Data;
		}
	});
}

//获取所有的渠道场景
function findAllChannel(){
	var Service = {};
	Service.OrderState = 999;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync("/channel/queryAllChennel.do",param,function(d){
		if(d.RespCode==10000){
			var channelList = d.Data;
			channelArray.push({"id":"","name":"全部交易渠道"});
			for(var i=0;i<channelList.length;i++){
				var obj = channelList[i];
				var jsonObj = {"id":obj.ChannelId,"name":obj.ChannelName};
				channelArray.push(jsonObj);
			}
		}
	});
}


function QueryString(val) {
	var uri = window.location.search;
	var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
	return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
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

//导出Excel文件
function downloadFile(){
	var Service = buildService();
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/order/downloadOrderListData.do",param, function(d){
		if(d.RespCode==10000){
			var filePath = d.Data[0].FilePath;
			window.location.href = location.protocol + '//' + location.host + "/" + filePath + "?token=" + Commonjs.getToken();
			Commonjs.alert("下载完成!");
		}
	});
}

var arr = [];//定义一个数组用来接收多选框的值   
//单击下拉列表时显示/隐藏下拉列表  
$('#box').click(function () {  
    $('#selectBox').toggle();  
})  
//监听checkbox的value值 改变则执行下列操作  
$("input").change(function () {  
    if ($(this).prop("checked")) {  
        arr.push($(this).val());//将选中的选项添加到数组中  
    } else {  
        var index = getIndex(arr, $(this).val())//找到没有选中的选项在数组中的索引  
        arr.splice(index, 1);//在数组中删除该选项  
    }  
})  
//这个函数用于获取指定值在数组中的索引  
function getIndex(arr, value) {  
    for (var i = 0; i < arr.length; i++) {  
        if (arr[i] == value) {  
            return i  
        }  
    }  
}  
//点击按钮是弹出数组  
$('button').click(function () {  
    alert(arr)  
})  