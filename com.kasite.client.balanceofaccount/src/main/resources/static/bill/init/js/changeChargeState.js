var pCount = 0;
$(function(){

	iniWidget();
	
	loadData();
	
});

function iniWidget(){
	
	//账单开始日期
	$('#date_timepicker_start').datetimepicker({
		format:'Y-m-d',
		lang:'ch',
		onShow:function( ct ){
			this.setOptions({
				maxDate:$('#date_timepicker_end').val()?$('#date_timepicker_end').val():false
			});
		},
		timepicker:false,
		value:Commonjs.getDate(-30)
	});
	//账单结束日期
	$('#date_timepicker_end').datetimepicker({
		format:'Y-m-d',
		lang:'ch',
		onShow:function( ct ){
			this.setOptions({
				minDate:$('#date_timepicker_start').val()?$('#date_timepicker_start').val():false
			});
		},
		timepicker:false,
		value:Commonjs.getDate(0)
	});
	
	
	
}


/**
 * 加载数据
 */
function loadData(){
	
	var startDate = $('#date_timepicker_start').val();
	var endDate = $('#date_timepicker_end').val();
	var cardNo = $('#cardNo').val();
	  //初始化表格
	 $('#billNotify').DataTable({
		 destroy:true,	
		 "serverSide": true,
		 "ajax" : {
			url:"./bill_getChargeList.do",
			// 提交参数
		    data: function (param) {
		    	param.startDate = startDate;
            	param.endDate = endDate;
		    	param.count = pCount;
		    	param.cardNo = cardNo;
		        return param;
		    },
		    dataSrc: function (data) {
		    	pCount = data.recordsTotal;
		        return data.data;
		    }
		},
		"columns": [
		            {"data": "name",title:"患者姓名" },
		            {"data": "cardNo",title:"就诊卡号" },
		            {"data": "orderId",title:"全流程订单流水号" },
		            {"data": "wxid",title:"微信订单流水号"},
		            {"data": "chargeData",title:"操作",render:function(data, type, row, meta ){
		            	return "<a href=\"javascript:void(0);\" onClick=\"Correct('"+row.orderId+"','"+row.wxid+"')\" class=\"alinks-unline alinks-blue briedel\">冲正</a>";
		            }}
		        ],
		"bPaginate" : true,//分页工具条显示  
		"bProcessing" : true
		});
}


function show(obj,title,hisID){
//	var value = $(obj).html();
	var str = '<table class="tb" width="100%"><tr><td class="last">'+hisID+'</td></tr></table>';
	$("#dealContent").html(str);
	var artBox=art.dialog({
        lock: true,
        opacity:0.4,
        width: 320,
        height:100,
        title:title,
        content: $('#logMessage').html(),
        ok: true
    });    
}

function Correct(orderid,wxid){
	//删除
	art.dialog({
		lock: true,
		artIcon:'edit',
		opacity:0.4,
		width: 400,
		title:'提示',
		content: '<span class="c-333 c-f14">信息是否已核实？</span>',
		ok: function () {
			$.ajax({
				type: 'POST',
				url: './bill_Correct.do?v='+(new Date()),
				data: {'orderid':orderid,'wxid':wxid},
				timeout : 6000,
				cache : false,
				dataType: 'json',
				success: function(data){
					if( data.respCode == 10000 ){
						show((this),"冲正成功！医院系统支付流水号",data.hisID);
//						Commonjs.alert('冲正成功！医院系统支付流水号:<br/>'+data.hisID);
					}else{
						Commonjs.alert(data.respMsg);
					}
					loadData();
	 			}
			});
		},
		cancel: true
	});
}