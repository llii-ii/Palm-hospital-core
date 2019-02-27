var startDate = null;
var endDate = null;
var pCount = 0;
$(function(){
    initWidget();
});


/**
 * 退费
 * @param value
 */
function refund(orderId, channelId) {
        var param = {};
        param.orderId = orderId,
        param.channelId = channelId,

        jkzl.common.ajaxNoLayer("../../billRefundController/refund.do?v=" + (new Date()), param, true, 30000, function (data) {
            myLayer.clear();
            /** 删除弹窗的整个DIV样式*/
            $("#menu").hide();
            //提示信息
            showResult(data.RespMessage);
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
function dataShow() {

    var channelId = $("#channelId").val();
    var orderId = $("#orderId").val();
    if (jkzl.common.isNull(channelId)) {
        showResult("请选择渠道!!!");
        return;
    }
    if (jkzl.common.isNull(orderId)) {
        showResult("请传入订单号!!!");
        return;
    }
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
        title: "验证码验证",
        content: str,
        ok: function(){
            oauthCode(orderId,channelId);
        },
        cancel: true,
    });

}
/**
 * 展示更多订单的详情
 * 用于操作按钮
 */
function dataShowDetail(title,orderId,channelId) {
    var str = "<div id=\"deal\" class=\"tb\" style=\"width: 100%;height: 100%\">" +
        "<span class=\"c-btn c-btn-gray\" style=\"float: left;margin-left: 90px;margin-top: 10px\" onclick=\"refund('" + orderId + "','" + channelId + "')\">退费</span>" +
        "</div>";

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
        "<span id=\"auCode\" style=\"width: 100%;height: 45px;display: block;line-height: 45px;text-align: center;color:red;\">"+msg+"</span>"
    "</div>";
    art.dialog({
        artIcon:'edit',
        opacity: 0.4,
        width: 320,
        height: 100,
        title: "操作结果",
        content: str,
        ok: function(){
        },
        cancel: true,
    });
}



/**
 * 验证当前验证码的有效性
 * @param code
 */
function oauthCode(orderId, channelId) {
    //获取验证码
    var code = $("#auCode").val();
    var param = {};
    param.code = code;
    jkzl.common.ajaxNoLayer("../../billOauthController/oauthCode.do?v=" + (new Date()), param, false, 30000, function (data) {
        myLayer.clear();
        /** 如果验证成功则进行后续业务*/
        if ('10000' == data.RespCode) {
            onFlag = true;
            dataShowDetail("订单退费操作!!!",orderId,channelId);
        }else {
            showResult(data.RespMessage);
        }
    });
}

