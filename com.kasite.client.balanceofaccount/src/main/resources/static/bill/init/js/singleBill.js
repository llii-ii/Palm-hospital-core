var startDate = null;
var endDate = null;
var pCount = 0;
var userid = null;
var billCheckTable = null;
$(function(){
	
	
	initWidget();
	
	//loadData();
	
});

/**
 * 加载数据
 */
function loadData(){
	var orderType = $('#selOrderType').val();
	var allOrderId = $('#allOrderId').val();
	var orderId = null;
	var hisOrderId = null;
	var merchNo = null;
	if( !ComWbj.isNull(orderType) && !ComWbj.isNull(allOrderId)){
		if( orderType == 1 ){
			hisOrderId = allOrderId;
		}else if( orderType == 2){
			orderId = allOrderId;
		}else if( orderType == 3){
			merchNo = allOrderId;
		}
		//初始化表格
		 $('#billDateTable').DataTable({
			destroy:true,	
			"serverSide": true,
	        "ajax" : {
	        	url:"./bill_queryDailyBill.do",
	        	// 提交参数
	            data: function (param) {
	            	param.orderId = orderId;
	            	param.hisOrderId = hisOrderId;
	            	param.merchNo = merchNo;
	            	param.count = pCount;
	                return param;
	            },
	            dataSrc: function (data) {
	            	pCount = data.recordsTotal;
	                return data.data;
	            }
	        },
	        "columns": [
	                    {"data": "orderId", title:"订单号",width:"380px"},
	                    {"data": "transDate",title:"交易时间",width:"150px"},
	                    //{"data": "checkBillCount",title:"业务类型"},
	                    {"data": "priceName",title:"服务内容"},
	                    {"data": "serviceState",title:"服务状态",render:function(data, type, row, meta ){
	                    	if( data == 0 ){
	                    		return "<span class=\"c-f00\">未完成</span>";
	                    	}else if( data == 1){
	                    		return "<span class=\"c-4dcd70\">已完成</span>";
	                    	}else{
	                    		return "<span class=\"c-4dcd70\">无</span>";
	                    	}
	                    	
	                    }},
	                    {"data": "receivableMoney",title:"应收款"},
	                    {"data": "payState",title:"收款状态",render:function(data, type, row, meta ){
	                    	//0，未收款1，已收款，2，已退款
	                    	if( data == 1 ){
	                    		return "<span class=\"c-4dcd70\">已收款</span>";
	                    	}else if( data == 2){
	                    		return "<span class=\"c-f00\">已退款</span>";
	                    	}else if( data == 0 ){
	                    		return "<span class=\"c-f00\">未收款</span>";
	                    	}
	                    	
	                    }},
	                    {"data": "receivedMoney",title:"已收款"},
	                    {"data": "channelname",title:"渠道"},
	                    {"data": "payEqpt",title:"支付终端"},
	                    {"data": "checkState",title:"校验结果",render:function(data, type, row, meta ){   	
	                    	if( data ==0 ){
	                    		return "<span class=\"c-4dcd70\">正常</span>";
	                    	}else if( data == 1){
	                    		return "<span class=\"c-f00\">长款</span>";
	                    	}else if( data == -1){
	                    		return "<span class=\"c-f00\">短款</span>";
	                    	}
	                    }}
	                    ,{"data": "payState",title:"操作",render:function(data, type, row, meta ){
	                    	var html = "<a href=\"billDetail.html?orderId="+row.orderId+"\" class=\"alinks-unline alinks-blue mlr5\">详情</a>";
	                    	//目前没有退款先注释
//	                    	if( row.checkState==1 ){
//	                    		html= html+"<a href=\"javascript:Refund('"+row.orderId+"','"+row.channelId+"','"+row.receivedMoney+"');\"  class=\"alinks-unline alinks-blue mlr5\">退款</a>";
//	                    	}
	                    	return html;
	                    }}
	                ],
	        "bPaginate" : true,//分页工具条显示  
	        "bProcessing" : true
	        
		});
	}else{
		Commonjs.alert('订单类型或者订单号不能为空！');
	}
}


/**
 * 初始化控件
 */
function initWidget(){
	//初始化表格
	billCheckTable = $('#billDateTable').DataTable({
		destroy:true,	
        "columns": [
            {"data": "orderId", title:"订单号",width:"380px"},
            {"data": "transDate",title:"交易时间",width:"150px"},
            //{"data": "checkBillCount",title:"业务类型"},
            {"data": "priceName",title:"服务内容"},
            {"data": "serviceState",title:"服务状态",render:function(data, type, row, meta ){
            	if( data == 0 ){
            		return "<span class=\"c-f00\">未完成</span>";
            	}else if( data == 1){
            		return "<span class=\"c-4dcd70\">已完成</span>";
            	}else{
            		return "<span class=\"c-4dcd70\">无</span>";
            	}
            	
            }},
            {"data": "receivableMoney",title:"应收款"},
            {"data": "payState",title:"收款状态",render:function(data, type, row, meta ){
            	//0，未收款1，已收款，2，已退款
            	if( data == 1 ){
            		return "<span class=\"c-4dcd70\">已收款</span>";
            	}else if( data == 2){
            		return "<span class=\"c-f00\">已退款</span>";
            	}else if( data == 0 ){
            		return "<span class=\"c-f00\">未收款</span>";
            	}
            	
            }},
            {"data": "receivedMoney",title:"已收款"},
            {"data": "channelname",title:"渠道"},
            {"data": "payEqpt",title:"支付终端"},
            {"data": "checkState",title:"校验结果",render:function(data, type, row, meta ){   	
            	if( data ==0 ){
            		return "<span class=\"c-4dcd70\">正常</span>";
            	}else if( data == 1){
            		return "<span class=\"c-f00\">长款</span>";
            	}else if( data == -1){
            		return "<span class=\"c-f00\">短款</span>";
            	}
            }}
            ,{"data": "payState",title:"操作",render:function(data, type, row, meta ){
            	var html = "<a href=\"billDetail.html?orderId="+row.orderId+"\" class=\"alinks-unline alinks-blue mlr5\">详情</a>";
            	if( row.checkState==1 ){
            		html= html+"<a href=\"javascript:Refund('"+row.orderId+"','"+row.channelId+"','"+row.receivedMoney+"');\"  class=\"alinks-unline alinks-blue mlr5\">退款</a>";
            	}
            	return html;
            }}
        ],
        "bPaginate" : true,//分页工具条显示  
        "bProcessing" : true
	});
	
}


/**
 * 退费
 */
function Refund(orderId,channelId,price){
	art.dialog({
		lock: true,
		artIcon:'edit',
		opacity:0.4,
		width: 400,
		title:'提示',
		content: '<span class="c-333 c-f14">该订单是否已经人工核实确认可以退款？</span>',
		ok: function () {
			$.ajax({
				type: 'POST',
				url: './bill_billRefund.do?v='+(new Date()),
				data: {orderId:orderId,
					channelId:channelId,
					price:price*100},
				async: true,
				timeout : 6000,
				cache : false,
				dataType: 'json',
				success: function(data){
					
					
					
					
				}
			});
		},
		cancel: true
	});
}


/**
 * 下载Excel
 */
function download(){
	var startDate = $('#date_timepicker_start').val();
	var endDate = $('#date_timepicker_end').val();
	var checkState = $("#checkState").val();
	window.location.href = encodeURI("./bill_exportDailyBill.do?startDate=" + startDate + "&endDate=" + endDate+ "&checkState=" + checkState);
}