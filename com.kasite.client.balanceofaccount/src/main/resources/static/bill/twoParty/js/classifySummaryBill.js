var startDate = null;
var endDate = null;
var pCount = 0;
$(function(){

    initWidget();

    loadData();

});

/**
 * 加载数据
 */
function loadData(){
    startDate = $('#date_timepicker_start').val();
    endDate = $('#date_timepicker_end').val();
    var serviceId = $("#serviceId").val();
    var channelId = $("#channelId").val();

    //初始化表格
    billTable = $('#billTable').DataTable({
        destroy:true,
        "serverSide": true,
        "bPaginate": true,
        "ajax" : {
            url:"../../billController/queryClassifySummaryBills.do?v="+(new Date()),
            // 提交参数
            data: function (param) {
                param.startDate = startDate;
                param.endDate = endDate;
                param.serviceId = serviceId;
                param.channelId = channelId;
                param.count = pCount;
                return param;
            },
            dataSrc: function (data) {
                pCount = data.recordsTotal;
                return data.data;
            }
        },
        "columns": [
            {"data": "billDate",title:"对账日期"},
            {"data": "channelId",title:"交易渠道",render:function(data){
                if (data == 100123) {
                    return "<span class=\"c-4dcd70\">微信</span>";
                } else if (data == 100125) {
                    return "<span class=\"c-4dcd70\">支付宝</span>";
                }else {
                    return "<span class=\"c-f00\">暂无</span>";
                }
            }},
            {"data": "serviceName",title:"服务名称"},
            {"data": "channelBills",title:"渠道订单笔数"},
            {"data": "qlcBills",title:"全流程订单笔数"},
            {"data": "receivableMoney",title:"应收金额（元）"},
            {"data": "alreadyReceivedMoney",title:"实收金额（元）"},
            {"data": "refundMoney",title:"应退金额（元）"},
            {"data": "alreadyRefundMoney",title:"实退金额（元）"}
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
        startDate = jkzl.common.getDate(-3);
        endDate = jkzl.common.getDate(0);
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
        ok: true,
        cancel: true,
    });
}

/**
 * 下载Excel
 */
function download(){
    var startDate = $('#date_timepicker_start').val();
    var endDate = $('#date_timepicker_end').val();
    var serviceId = $("#serviceId").val();
    var channelId = $("#channelId").val();
    var fileName = "分类汇总账单报表";
    window.location.href = encodeURI("../../billDownload/downloadClassifySummaryBillsExcel.do?fileName="+fileName+"&startDate=" + startDate
        + "&endDate=" + endDate+ "&serviceId=" + serviceId+"&channelId=" + channelId);
    showResult("下载成功!!!");

}