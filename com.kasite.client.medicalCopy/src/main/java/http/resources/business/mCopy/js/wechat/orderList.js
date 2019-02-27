
$(function(){
	getOrderList();
});


function getOrderList(){
	var apiData = {};	
	apiData.applyOpenId = Commonjs.getOpenId();
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/GetOrderList/callApi.do',param,function(dd){
		if(dd.RespCode == -14018){
			$(".order-list").html('');
		}else if(dd.RespCode == 10000){
			loadData(dd.Data);
		}
	});
}

function loadData(list){
	var payState = '';
	var orderState = '';
	var html = '';
	$(".order-list").html(html);  
	$.each(list,function(i,o) {
		html += '<li><div class="order-list-top" onclick="gotoDetail(\''+o.Id+'\',\''+o.CaseMoneyAll+'\',\''+o.CaseNumberAll+'\',\''+o.WxOrderId+'\')">';
		html += '<p><span><span class="flag">申请单号</span>：</span>'+o.Id+'</p>';
		html += '<p><span><span class="flag">姓名</span>： </span>'+o.Name+'</p>';
		html += '<p><span><span class="flag">下单时间</span>：  </span>'+o.ApplyTime+'</p>';
		html += '<div class="right"><img src="../mCopy/img/wechat/icon-right.png"/></div></div>';

		if(o.PayState == 0){
			payState = '<span class="red">待支付¥ '+o.TotalMoney;
		}else if(o.PayState == 1){
			payState = '<span class="red">支付中¥ '+o.TotalMoney;
		}else if(o.PayState == 2){
			payState = '<span >已支付¥ '+o.TotalMoney;
		}else if(o.PayState == 3){
			payState = '<span >退款中¥ '+o.TotalMoney;
		}else if(o.PayState == 4){
			payState = '<span >已退款¥ '+o.TotalMoney;
		}else{
			payState = '<span class="red">待支付¥ '+o.TotalMoney;
		}
		
		if(o.OrderState == 1){
			orderState = '<label class="green">申请成功';
		}else if(o.OrderState == 2){
			orderState = '<label class="green">已确认';
		}else if(o.OrderState == 3){
			orderState = '<label class="blue">已寄送';
		}else if(o.OrderState == 4){
			orderState = '<label class="gray">已取消';
			//payState = '<span>';
		}
		
		html += '<div class="order-list-con">'+orderState+'</label>'+payState+'</span></div></li>';
	});
	$(".order-list").append(html);
	
}

//确认筛选
function sureFilter(){
	var payState = '';
	var payStateLi = $("#payState li");
	$.each(payStateLi,function(i,o){
		if($(o).attr("class") == "cur"){
			payState = $(o).val();
		}
	});
	
	var orderState = '';
	var orderStateLi = $("#orderState li");
	$.each(orderStateLi,function(i,o){
		if($(o).attr("class") == "cur"){
			orderState = $(o).val();
		}
	});
	if(orderState == 0){
		orderState = '';
	}
	if(payState == 0){
		payState = '';
	}

	var apiData = {};	
	apiData.applyOpenId = Commonjs.getOpenId();
	apiData.payState = parseInt(payState)-1;
	apiData.orderState = orderState;
	apiData.startTime = $("#st").val();
	//apiData.endTime = $("#et").val();
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/GetOrderList/callApi.do',param,function(dd){
		if(dd.RespCode == -14018){
			$(".order-list").html('');
		}else if(dd.RespCode == 10000){
			if(dd.Data == undefined){
				loadData([]);
			}else{
				loadData(dd.Data);
			}
			$('.mask').hide();
		}
	});
}

//点击跳到详情页
function gotoDetail(orderId,caseMoney,caseNumber,wxOrderId){

	var url = 'detail.html?id='+orderId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&wxOrderId="+wxOrderId;
	location.href = Commonjs.goToUrl(url);
}
