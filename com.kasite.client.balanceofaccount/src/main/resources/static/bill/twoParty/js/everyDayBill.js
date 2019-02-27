var pCount = 0;
var billTable = null;
var isDefault = 1;
$(function(){

    initWdiget();

    loadData();

});


/**
 * 加载数据
 */
function loadData(){
    var startDate = $('#date_timepicker_start').val();
    var endDate = $('#date_timepicker_end').val();
    var checkState = $("#checkState").val();

    //初始化表格
    billTable = $('#billTable').DataTable({
        destroy:true,
        //retrieve:true,
        "serverSide": true,
        "bPaginate": true,
        "ajax" : {
            url:"../../billController/queryEveryDayBills.do?v="+(new Date()),
            // 提交参数
            data: function (param) {
                param.startDate = startDate;
                param.endDate = endDate;
                param.checkState = checkState;
                param.count = pCount;
                return param;
            },
            dataSrc: function (data) {
                pCount = data.recordsTotal;
                isDefault = data.isDefault;
                return data.data;
            }
        },
        "columns": [
            {"data": "billDate", title:"对账日期"},
            {"data": "channelBills",title:"渠道订单笔数"},
            {"data": "qlcBills",title:"全流程订单笔数"},
            {"data": "checkNum",title:"已核对笔数"},
            {"data": "abnormalNum",title:"总异常笔数",render:function(data, type, row, meta ){
                return "<a href=\"everyDayBillDetail.html?errorState=-1&exeState=&date="+row.billDate+"\" class=\"alinks-unline alinks-blue\">"+data+"</a>";
            }},
            {"data": "overPlusErrNum",title:"剩余异常笔数",width:"5%",render:function(data, type, row, meta ){
                return "<a href=\"everyDayBillDetail.html?errorState=-1&exeState=2&date="+row.billDate+"\" class=\"alinks-unline alinks-blue\">"+data+"</a>";
            }},
            {"data": "receivableMoney",title:"应收金额（元）"},
            {"data": "alreadyReceivedMoney",title:"实收金额（元）"},
            {"data": "refundMoney",title:"应退金额（元）"},
            {"data": "alreadyRefundMoney",title:"实退金额（元）"},
            {"data": "checkResult",title:"对账结果",render:function(data, type, row, meta ){
                if( data == 1 ){
                    return "<span class=\"c-4dcd70\">账平</span>";
                }else if( data == -1 ){
                    return "<span class=\"c-f00\">账不平</span>";
                }else if( data == 2 ){
                    return "<span class=\"c-4dcd70\">账平(处置后)</span>";
                }

            }},
            {"data": "checkState",title:"操作",render:function(data, type, row, meta ){
                return "<a href=\"everyDayBillDetail.html?date="+row.billDate+"\" class=\"alinks-unline alinks-blue\">查看</a>"
            }}
        ]
    });


}

/**
 * 初始化控件
 */
function initWdiget(){

    //账单开始日期
    $('#date_timepicker_start').datetimepicker({
        format:'Y-m-d',
        lang:'ch',
        onShow:function( ct ){
            this.setOptions({
                maxDate:$('#date_timepicker_end').val()?$('#date_timepicker_end').val():false
            })
        },
        timepicker:false,
        value:jkzl.common.getDate(-8)
    });
    //账单结束日期
    $('#date_timepicker_end').datetimepicker({
        format:'Y-m-d',
        lang:'ch',
        onShow:function( ct ){
            this.setOptions({
                minDate:$('#date_timepicker_start').val()?$('#date_timepicker_start').val():false
            })
        },
        timepicker:false,
        value:jkzl.common.getDate(-1)
    });



    $('#showChart').on('click', function(){
        art.dialog({
            lock: true,
            opacity:0.4,
            width: 800,
            height: 400,
            title:'每日账单趋势图',
            content: '<div id="showchart" style="height:400px;"></div>'
        });

        var startDate = $('#date_timepicker_start').val();
        var endDate = $('#date_timepicker_end').val();
        var checkState = $("#checkState").val();
        $.ajax({
            type: 'POST',
            url: '../../billController/queryEveryDayTrendMap.do?v='+(new Date()),
            data: {'startDate':startDate,'endDate':endDate,'checkState':checkState},
            timeout : 6000,
            cache : false,
            dataType: 'json',
            success: function(data){
                //查看趋势图
                var option = {
                    tooltip : {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            label: {
                                backgroundColor: '#6a7985'
                            }
                        }
                    },
                    legend: {
                        data:['每日订单笔数']
                    },
                    grid: {
                        left: '3%',
                        right: '6%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis : [
                        {
                            type : 'category',
                            boundaryGap : false,
                            data :data.billDates
                        }
                    ],
                    yAxis : [
                        {
                            type : 'value'
                        }
                    ],
                    series : [
                        {
                            name:'每日订单笔数',
                            type:'line',
                            stack: '总量',
                            itemStyle: {
                                normal: {color:'#83c0df'}
                            },
                            lineStyle: {
                                normal: {color:'#83c0df'}
                            },
                            areaStyle: {
                                normal: {color:'#83c0df'}
                            },
                            data:data.billCount.reverse()
                        }
                    ]
                };

                var myChart = echarts.init(document.getElementById('showchart'));
                myChart.setOption(option);
            }
        });

    });
}


/**
 * 用于验证码的操作操作按钮
 */
function dataShow(code) {
    var starDate = $('#date_timepicker_start').val();
    var endDate = $('#date_timepicker_end').val();
    /** 为1时默认开启汇总时间限制*/
    if (isDefault == 1) {
        /** 判断时间的差额是否大于7天，大于则不允许操作*/
        if (jkzl.common.dateDifference(starDate, endDate) > 7) {
            //提示信息
            showResult("汇总时间超过一周,为防止数据量过大导致系统崩溃,请重新选择,谢谢您的合作!!!");
            return;
        }
    }
    var str = "<div id=\"deal\" class=\"tb\">" +
        "<input placeholder=\"请输入验证码\" style=\"float: left;margin-left: 80px;margin-top: 10px\" class=\"input-text input-text-w110\" id=\"auCode\">" +
        "</div>";
    art.dialog({
        lock: true,
        artIcon: 'edit',
        opacity: 0.4,
        width: 320,
        height: 100,
        title: "验证码验证",
        content: str,
        ok: function () {
            oauthCode(code);
        },
        cancel: true,
    });
}

/**
 * 验证当前验证码的有效性
 * @param code
 */
function oauthCode(outhCode) {
    //获取验证码
    var code = $("#auCode").val();
    var param = {};
    param.code = code;
    jkzl.common.ajaxNoLayer("../../billOauthController/oauthCode.do?v=" + (new Date()), param, true, 300000, function (data) {
        myLayer.clear();
        /** 如果验证成功则进行后续业务*/
        if ('10000' == data.RespCode) {
            if ("1" == outhCode){
                downloadHisBill();
            }else if ("2" == outhCode){
                synQclBill();
            }else if ("3" == outhCode){
                synThreeBill();
            }else if ("4" == outhCode){
                synEveryBill();
            }
        }else {
            myLayer.clear();
            showResult(data.RespMessage);
        }
    });
}

/**
 * 下载HIS账单
 */
function downloadHisBill(){
    var param ={};
    param.startDate = $('#date_timepicker_start').val();
    param.endDate = $('#date_timepicker_end').val();
    jkzl.common.ajaxNoLayer("../../billDownload/downloadHisBill.do?v="+(new Date()), param, false, 30000, function (data) {
        myLayer.clear();
        //提示信息
        showResult(data.RespMessage);
    });
}

/**
 * 汇总全流程账单
 */
function synQclBill(){
    var param ={};
    param.startDate = $('#date_timepicker_start').val();
    param.endDate = $('#date_timepicker_end').val();
    jkzl.common.ajaxNoLayer("../../billSummaryController/summary.do?v="+(new Date()), param, false, 30000, function (data) {
        myLayer.clear();
        //提示信息
        showResult(data.RespMessage);
    });
}

/**
 * 汇总三方账单
 */
function synThreeBill(){
    var param ={};
    param.startDate = $('#date_timepicker_start').val();
    param.endDate = $('#date_timepicker_end').val();
    jkzl.common.ajaxNoLayer("../../billSummaryController/three.do?v="+(new Date()), param, false, 30000, function (data) {
        myLayer.clear();
        //提示信息
        showResult(data.RespMessage);
    });
}

/**
 * 汇总每日账单
 */
function synEveryBill(){
    var param ={};
    param.startDate = $('#date_timepicker_start').val();
    param.endDate = $('#date_timepicker_end').val();
    jkzl.common.ajaxNoLayer("../../billSummaryController/every.do?v="+(new Date()), param, false, 30000, function (data) {
        myLayer.clear();
        //提示信息
        showResult(data.RespMessage);
    });
}

/**
 * 展示操作按钮触发后，后台处理的结果描述
 * @param value
 */
function showResult(msg) {
    myLayer.clear();
    var str = "<div id=\"deal\" style=\"width: 100%;height: 50%\">" +
        "<span id=\"auCode\" style=\"width: 100%;height: 45px;display: block;line-height: 45px;text-align: center;color: red;\">"+msg+"</span>"
    "</div>";
    art.dialog({
        lock: true,
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
    var checkState = $("#checkState").val();
    var fileName = "每日汇总账单报表";
    window.location.href = encodeURI("../../billDownload/downloadEveryDayBillsExcel.do?fileName="+fileName+"&startDate=" + startDate + "&endDate=" + endDate+ "&checkState=" + checkState);
    showResult("下载成功!!!");
}