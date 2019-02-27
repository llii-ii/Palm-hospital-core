
$(function(){
	getList();
});


//获得列表
function getList(){
	$('#exampleTablePagination').bootstrapTable({
		url: '/wsgw/medicalCopy/GetTransactionRecord/callApi.do',  //请求后台的URL（*）
        method: 'POST',                      //请求方式（*）
        contentType:"application/x-www-form-urlencoded; charset=UTF-8",
        pagination: true,                   //是否显示分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 10,                     //每页的记录行数（*）
        queryParams : function (params) {
        	var apiData = {};	
        	
        	if($('#choose option:selected').val() == "orderId"){
        		apiData.orderId = $('#chooseText').val();
        	}
        	apiData.orderType = $('#orderType option:selected').val();
        	apiData.payChannel = $('#payChannel option:selected').val();
        	apiData.accountResult = $('#accountResult option:selected').val();
        	apiData.endTime = $("#et").val();
        	apiData.startTime = $("#st").val();

        	var temp = {   
                     rows: params.limit,                         //页面大小
                     page: (params.offset / params.limit) + 1,   //页码
                     sort: params.sort,      //排序列名  
                     sortOrder: params.order, //排位命令（desc，asc） 
                     token : Commonjs.getToken(),
                     apiParam : Commonjs.getApiReqParams(apiData)
                 };
            return temp;
        },
        //CourierNumber
        columns: [{
            field: 'OrderType', 	    
            title: '订单类型',
            formatter : function(value, row, index){
            	if(value == 1){
            		return "支付订单";
            	}
            }
        }, {
            field: 'OrderId',
            title: '申请单号'
        }, {
            field: 'WxOrderId',
            title: '渠道支付单号'
        }, {
            field: 'EoTime',
            title: '订单创建时间',
            sortable: true,
        }, {
            field: 'ServiceContent',
            title: '服务内容',
            sortable: true,
        }, {
            field: 'ActualReceipts',
            title: '应收金额',
            sortable: true,
        }, {
            field: 'TotalMoney',
            title: '实收金额',
            sortable: true,
        }, {
            field: 'ShouldRefunds',
            title: '应退金额',
            sortable: true,
        }, {
            field: 'ActualRefunds',
            title: '实退金额',
            sortable: true,
        }, {
            field: 'ReceiptsType',
            title: '收款状态',
            sortable: true,
            formatter : function(value, row, index){
            	if(value == 1){
            		return "已收款";
            	}else if(value == 2){
            		return "退款中";
            	}else if(value == 3){
            		return "已退款";
            	}
            }
        },{
            field: 'PayChannel',
            title: '渠道',
            sortable: true,
            formatter : function(value, row, index){
            	if(value == 1){
            		return "微信";
            	}
            }
        }, {
            field: 'AccountResult',
            title: '对账结果',
            sortable: true,
            formatter : function(value, row, index){
            	if(value == 1){
            		return "长款";
            	}else if(value == 2){
            		return "账平";
            	}else if(value == 3){
            		return "短款";
            	}else if(value == 0){
            		return "未对账";
            	}
            }
        }],
        responseHandler: function(res) {
        	if(res.RespCode == 401){
        		alert(res.RespMessage);
        		top.location = "http://"+window.location.host;
        	}else{
        		if(res.Data == undefined){
        			return [];
        		}else{
        			return res.Data;
        		}
        	}
        },
        onLoadSuccess: function (result) {
        	console.log(result);
        },
        onLoadError: function () {
            alert("数据加载失败！");
        },
	});
}


//查询刷新
function refresh(){
	$("#exampleTablePagination").bootstrapTable('destroy');
	getList();
}