var startDate = null;
var endDate = null;
var pCount = 0;
var errorState = jkzl.common.getUrlParam("errorState");
var exeState = jkzl.common.getUrlParam("exeState");
var onFlag = false;
$(function(){

    initWidget();

    loadData();

});

/**
 * 加载数据
 */
function loadData(flag){
	 //如是从异常订单的入口进入,errorState则需要为-1，否则恢复该字段，以免读取其它数据出错
	if(flag == 1){
		errorState = "";
        exeState = "";
	}
    startDate = $('#date_timepicker_start').val();
    endDate = $('#date_timepicker_end').val();
    var checkState = $("#checkState").val();
    var channelOrderType = $("#channelOrderType").val();
    var orderType = $("#orderType").val();
    var orderId = $("#orderId").val();
    var channelId = $("#channelId").val();

    //初始化表格
    var billDateTable = $('#billDateTable').DataTable({
        destroy:true,
        "serverSide": true,
        "bPaginate" : true,
        "bProcessing" : true,
        "ajax" : {
            url:"../../billController/queryThreePartyBillDetail.do?v="+(new Date()),
            // 提交参数
            data: function (param) {
                param.startDate = startDate;
                param.endDate = endDate;
                param.orderType = orderType;
                param.orderId = orderId;
                param.channelId = channelId;
                param.channelOrderType = channelOrderType;
                param.checkState = checkState;
                param.exeState = exeState;
                param.errorState = errorState;
                param.count = pCount;
                return param;
            },
            dataSrc: function (data) {
                pCount = data.recordsTotal;
                return data.data;
            }
        },
        "columns": [
            {"data": "qlcOrderState",title:"订单类型",width:"6%",render:function(data){
                if( data == 1 ){
                    return "<span class=\"c-4dcd70\">支付订单</span>";
                }else if( data == 2){
                    return "<span class=\"c-4dcd70\">退款订单</span>";
                }

            }},
            {"data": "orderId", title:"全流程订单号",width:"6%",render:function (data) {
                var html = "<p onclick=\"show('"+data+"','全流程订单号')\">"+data+"</p>";
                return html;
            }},
            {"data": "hisOrderId", title:"His订单号",width:"6%",render:function (data) {
                var html = "<p onclick=\"show('"+data+"','His订单号')\">"+data+"</p>";
                return html;
            }},
            {"data": "channelOrderId", title:"渠道订单号",width:"6%",render:function (data) {
                var html = "<p onclick=\"show('"+data+"','渠道订单号')\">"+data+"</p>";
                return html;
            }},
            {"data": "createDate",title:"订单创建时间",width:"8%"},
            {"data": "lastDate",title:"订单最后操作时间",width:"8%"},
            {"data": "priceName",title:"服务内容",width:"7%"},
            {"data": "operatorName",title:"操作人",width:"6%",render:function (data, type, row, meta ) {
                if ("0" == row.operationJson){
                    return "<span class=\"c-4dcd70\">"+data+"</span>";
                }else if ("1" == row.operationJson){
                    /** 格式化json字符串*/
                    Process(data, "jsonFormat");
                    var html = "<a href='#' onclick=\"showJsonResult()\">详情</a>";
                    return html;
                }
            }},
            {"data": "receivableMoney",title:"应收金额（元）",width:"5%"},
            {"data": "alreadyReceivedMoney",title:"实收金额（元）",width:"5%"},
            {"data": "refundMoney",title:"应退金额（元）",width:"5%"},
            {"data": "alreadyRefundMoney",title:"实退金额（元）",width:"5%"},
            {"data": "channelOrderState",title:"收款状态",width:"5%",render:function(data){
                if( data == 1 ){
                    return "<span class=\"c-4dcd70\">已收款</span>";
                }else if( data == 2){
                    return "<span class=\"c-4dcd70\">已退款</span>";
                }else{
                    return "<span class=\"c-f00\">未收款</span>";
                }
            }},
            {"data": "channelId",title:"渠道",width:"4%",render:function(data){
                if (data == 100123) {
                    return "<span class=\"c-4dcd70\">微信</span>";
                } else if (data == 100125) {
                    return "<span class=\"c-4dcd70\">支付宝</span>";
                }else {
                    return "<span class=\"c-f00\">暂无</span>";
                }
            }},
            {"data": "bizType",title:"业务结果",width:"6%",render:function(data){
                if( data == 1){
                    return "<span class=\"c-37a6ed\">已冲正</span>";
                }else if( data == 2){
                    return "<span class=\"c-37a6ed\">已退款</span>";
                }else if( data == 3){
                    return "<span class=\"c-37a6ed\">正在冲正中</span>";
                }else if (data == 4){
                    return "<span class=\"c-37a6ed\">正在退款中</span>";
                }else if (data == 5){
                    return "<span class=\"c-37a6ed\">待冲正</span>";
                }else if (data == 6){
                    return "<span class=\"c-37a6ed\">待退费</span>";
                }else if( data == 7){
                    return "<span class=\"c-37a6ed\">待调账</span>";
                }else if( data == 8){
                    return "<span class=\"c-37a6ed\">正在调账中</span>";
                }else if( data == 9){
                    return "<span class=\"c-37a6ed\">已调账</span>";
                }else if (data == 0){
                    return "<span>暂无</span>";
                }
            }},
            {"data": "checkState",title:"对账结果",width:"7%",render:function(data){
                if( data == 0){
                    return "<span class=\"c-4dcd70\">账平</span>";
                }else if( data == 1){
                    return "<span class=\"c-f00\">长款</span>";
                }else if( data == -1){
                    return "<span class=\"c-f00\">短款</span>";
                }else if( data == 2){
                    return "<span class=\"c-4dcd70\">账平(处置后)</span>";
                }
            }},
            {"data": "errorState",title:"操作",width:"4%",render:function(data, type, row, meta ){
                if (data==-1){
                    return "<a  href='#' onclick=\"dataShow('订单操作','"+row.orderId+"','"+row.hisOrderId+"','"+row.channelOrderId+"','"+row.checkState+"','"+row.operationButton+"')\">操作</a>";
                }else if(data == 1){
                    return "<span></span>";
                }

            }}
        ]
    });


}


/**
 * 初始化控件
 */
function initWidget(){
    var date = jkzl.common.getUrlParam("date");
    if( !jkzl.common.isNull(date)){
        startDate = date;
        endDate = date;
    }else{
        startDate = jkzl.common.getDate(-8);
        endDate = jkzl.common.getDate(-1);
    }
    //startDate = "2015-01-01";
    $('#date_timepicker_start').datetimepicker({
        format:'Y-m-d',
        lang:'ch',
        onShow:function( ct ){
            this.setOptions({
                maxDate:$('#date_timepicker_end').val()?$('#date_timepicker_end').val():false
            })
        },
        timepicker:false,
        value:startDate
    });
    $('#date_timepicker_end').datetimepicker({
        format:'Y-m-d',
        lang:'ch',
        onShow:function( ct ){
            this.setOptions({
                minDate:$('#date_timepicker_start').val()?$('#date_timepicker_start').val():false
            })
        },
        timepicker:false,
        value:endDate
    });

}

/**
 * 通过订单号的下拉框事件改变触发事件
 */
$("#channelOrderType").bind("change",function(){
    var channelOrderType = $("#channelOrderType").val();
    if (channelOrderType!=''){
        $("#orderId").css("display","block");
    }else {
        $("#orderId").css("display","none");
    }


});

/**
 * 点击弹窗展示数据
 * @param obj
 * @param title
 */
function show(value, title) {
    var str = '<table class="tb" width="100%"><tr><td class="last">' + value + '</td></tr></table>';
    $("#dealContent").html(str);
    art.dialog({
        lock: true,
        opacity: 0.4,
        width: 320,
        height: 100,
        title: title,
        content: $('#logMessage').html(),
        ok: true
    });
}

/**
 * 用于验证码的操作操作按钮
 */
function dataShow(title,orderId,hisOrderId,channelOrderId,checkState,operationButton) {
    var str = "<div id=\"deal\" class=\"tb\">" +
        "<input placeholder=\"请输入验证码\" style=\"float: left;margin-left: 80px;margin-top: 10px\" class=\"input-text input-text-w110\" id=\"auCode\">"+
        "<span  style=\"float: left;margin-left: 10px;margin-top: 10px;color: red\"id=\"auCode\">为了资金安全，请慎重操作！谢谢您的合作!</span>"
    "</div>";
    art.dialog({
        lock: true,
        artIcon:'edit',
        opacity: 0.4,
        width: 320,
        height: 100,
        title: title,
        content: str,
        ok: function(){
            oauthCode(orderId,hisOrderId,channelOrderId,checkState,operationButton);
        },
        cancel: true,
    });

}
/**
 * 展示更多订单的详情
 * 用于操作按钮
 * 判断checkState的值，来展示返回给前端的页面
 * 长款
 *    checkState【1】和operationButton【0】 展示 [冲正]，[退费]
 *    checkState【1】和operationButton【1】 不展示[冲正]，展示 [退费]
 *    checkState【1】和operationButton【2】 不展示[退费]，展示 [冲正]
 * 短款
 *    checkState【-1】和operationButton【0】展示 [财务调账]
 *    checkState【-1】和operationButton【3】不展示
 */
function dataShowDetail(title,orderId,hisOrderId,channelOrderId,checkState,operationButton) {
	 var str;
	 if (checkState == "1"){
         if ("0"==operationButton) {
             str = "<div id=\"deal\" class=\"tb\" style=\"width: 100%;height: 100%\">" +
                 "<span class=\"c-btn c-btn-gray\" style=\"float: left;margin-left: 30px\" onclick=\"addPendingOrder('1','"+orderId+"','"+hisOrderId+"','"+channelOrderId+"')\">冲正</span>" +
                 "<span class=\"c-btn c-btn-gray\" style=\"float: right;margin-right: 30px\" onclick=\"addPendingOrder('2','"+orderId+"','"+hisOrderId+"','"+channelOrderId+"')\">退费</span>" +
                 "</div>";
         } else if ("1"==operationButton){
             str = "<div id=\"deal\" class=\"tb\" style=\"width: 100%;height: 100%\">" +
                 "<span class=\"c-btn c-btn-gray\" style=\"float: left;margin-left: 90px;margin-top: 10px\" onclick=\"addPendingOrder('2','" + orderId + "','" + hisOrderId + "','" + channelOrderId + "')\">退费</span>" +
                 "</div>";
         }else if ("2"==operationButton){
             str = "<div id=\"deal\" class=\"tb\" style=\"width: 100%;height: 100%\">" +
                 "<span class=\"c-btn c-btn-gray\" style=\"float: left;margin-left: 90px;margin-top: 10px\" onclick=\"addPendingOrder('1','" + orderId + "','" + hisOrderId + "','" + channelOrderId + "')\">冲正</span>" +
                 "</div>";
         }
	}else if (checkState == "-1"){
         if ("3" == operationButton) {
             str = "";
         }else {
             str = "<div id=\"deal\" class=\"tb\" style=\"width: 100%;height: 100%\">" +
                 "<span class=\"c-btn c-btn-gray\" style=\"float: left;margin-left: 90px;margin-top: 10px\" onclick=\"addPendingOrder('3','"+orderId+"','"+hisOrderId+"','"+channelOrderId+"')\">财务调账</span>" +
                 "</div>";
         }

    }
    art.dialog({
        opacity: 0,
        width: 320,
        height: 100,
        title: title,
        content: str,
    });
}

/**
 * 展示操作按钮触发后，后台处理的结果描述
 * @param value
 */
function showResult(msg) {
    var str = "<div id=\"deal\" style=\"width: 100%;height: 50%\">" +
        "<span id=\"auCode\" style=\"width: 100%;height: 45px;display: block;line-height: 45px;text-align: center;\">"+msg+"</span>"
    "</div>";
    art.dialog({
        artIcon:'edit',
        opacity: 0.4,
        width: 320,
        height: 100,
        title: "操作结果",
        content: str,
        ok: function(){
            if (onFlag){
                window.location.reload();
            }
        },
        cancel: true,
    });
}

/**
 * 新增待执行业务的订单
 * @param value
 */
function addPendingOrder(bizType,orderId,hisOrderId,channelOrderId) {
    var param = {};
    param.bizType = bizType,
        param.orderId = orderId,
        param.hisOrderId = hisOrderId,
        param.channelOrderId = channelOrderId,

        jkzl.common.ajaxNoLayer("../../billController/addPendingOrder.do?v="+(new Date()), param, true, 30000, function (data) {
            myLayer.clear();
            /** 删除弹窗的整个DIV样式*/
            $("#menu").hide();
            //提示信息
            showResult(data.RespMessage);
        });

}

/**
 * 验证当前验证码的有效性
 * @param code
 */
function oauthCode(orderId, hisOrderId, channelOrderId,checkState,operationButton) {
    //获取验证码
    var code = $("#auCode").val();
    var param = {};
    param.code = code;
    jkzl.common.ajaxNoLayer("../../billOauthController/oauthCode.do?v=" + (new Date()), param, false, 30000, function (data) {
        myLayer.clear();
        /** 如果验证成功则进行后续业务*/
        if ('10000' == data.RespCode) {
            onFlag = true;
            dataShowDetail("新增业务状态",orderId, hisOrderId, channelOrderId,checkState,operationButton);
        }else {
            showResult(data.RespMessage);
        }
    });
}

//JSON格式化
function showJsonResult() {
        var contents = $('#showReqResultJsonDiv').get(0);

        art.dialog({
            lock : true,
            artIcon : 'edit',
            opacity : 0.6,
            width : 450,
            overflow : true,
            title : "操作人详情",
            fixed : false,
            content : contents,
            ok : null,
            cancel : null
        });

}

/**
 * 下载Excel
 */
function download(){
    var startDate = $('#date_timepicker_start').val();
    var endDate = $('#date_timepicker_end').val();
    var checkState = $("#checkState").val();
    var channelOrderType = $("#channelOrderType").val();
    var orderType = $("#orderType").val();
    var orderId = $("#orderId").val();
    var channelId = $("#channelId").val();
    var errState = jkzl.common.getUrlParam("errorState");
    var fileName = "每日汇总账单明细报表";
    window.location.href = encodeURI("../../billDownload/downloadPartyBillsExcel.do?fileName="+fileName+"&startDate=" + startDate
        + "&endDate=" + endDate+ "&checkState=" + checkState+"&exeState="+exeState+"&channelOrderType=" + channelOrderType+"&orderId=" + orderId+"&orderType=" + orderType+"&channelId="+ channelId+"&errorState="+errState);
    showResult("下载成功!!!");

}