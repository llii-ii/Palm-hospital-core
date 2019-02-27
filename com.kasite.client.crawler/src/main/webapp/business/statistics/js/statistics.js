	var session = null;
 	var pageSize = 3;
 	
$(function(){
	initWdiget();
	queryWJData(1);
});


/**
 * 加载数据
 */
function queryWJData(num){
	var param = {};
	param.hospitalName = $("#name").val();
	param.startDate=$("#date_timepicker_start").val();
	param.endDate=$("#date_timepicker_end").val();
	param.pageSize = pageSize;
	param.PageStart = (num-1);
	var d = _ajax('../../statistics/getStatisticsList.do',param,false);
	Page(d.data.totalProperty,pageSize,'pager');
	loadData(d.data);
}

function loadData(d){
	var html = '';
	var allNum = 0;
	var number = 0;
	var flag = false;
	var start = $("#date_timepicker_start").val();
	var end = $("#date_timepicker_end").val();
	
	//th添加完毕
	html+= '<tr><th width="28%">设备名称</th><th width="9%">总计</th>';
	for (var k = 0; k < 7; k++) {
		html+= '<th width="9%">'+start+'</th>';
		start = addDate(start,'');
	}
	html+= '</tr>';
	
	
	if(d.allHospital.length){
		$.each(d.allHospital,function(i,o) {
			start = $("#date_timepicker_start").val();
			html+= '<tr><td>'+o.hospitalName+'</td>';
			number = parseInt(o.reportTrue) + parseInt(o.reportFalse);
			html+= '<td>'+number+'</td>';
			number = 0;
			for (var k = 0; k < 7; k++) {
				$.each(d.result,function(j,r) {
					if(r.hospitalId == o.hospitalId && r.date.substring(0,10) == start){
						allNum = parseInt(r.reportTrue) + parseInt(r.reportFalse);
						flag = true;
						return false;
					}else{
						flag = false;
					}
				});
				
				if(flag){
					html+= '<td>'+allNum+'</td>'; 
					flag = false;
					allNum = 0;
				}else{
					html+= '<td>0</td>'; 
				}
				start = addDate(start,'');
			}		
			
			html+= '</tr>';
		});
	
	}else{
		html += '<tr align=center><td colspan=7><font color=red>未找到相关数据!</font></td></tr>';
	}
		
	$("#dataList tr").remove();
	//$(html).insertAfter($("#dataList tr").eq(0));
	$("#dataList").append(html);
}

function _ajax(url,param,flag){
	
	var obj = null;
	try{
		$.ajax({
			type: 'POST',
			url: url,
			data: param,
			async: flag,
			timeout : 8000,
			dataType: 'json',
			success: function(data){
				obj = data;
 			}
		});
	}catch(err){
		ComWbj.artTips("提示","error",err,2,null);
//		ComWbj.alertIconNo('提示：',err,'error');
	}
	if(!flag) return obj;
}
    
	function isEmpty(s){
		
		if(s == undefined){
			return true;
		}else{
			if(s == null || s == '' ||
				s == 'null' || s.length < 1){
				return true;
			}
		}
		return false;
	}  

  	//分页
	function Page(totalcounts,pagecount,pager) {
		$("#"+pager).pager( {
			totalcounts : totalcounts,
			pagesize : pagecount,
			pagenumber : $("#pagenumber").val(),
			pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
			buttonClickCallback : function(al) {
				$("#pagenumber").val(al);
				queryWJData(al);
			}
		});
	}
	
	
	
	// 日期，在原有日期基础上，增加days天数，默认增加1天
    function addDate(date, days) {
        if (days == undefined || days == '') {
            days = 1;
        }
        var date = new Date(date);
        date.setDate(date.getDate() + days);
        var month = date.getMonth() + 1;
        var day = date.getDate();
        return date.getFullYear() + '-' + getFormatDate(month) + '-' + getFormatDate(day);
    }

    // 日期月份/天的显示，如果是1位数，则在前面加上'0'
    function getFormatDate(arg) {
        if (arg == undefined || arg == '') {
            return '';
        }

        var re = arg + '';
        if (re.length < 2) {
            re = '0' + re;
        }

        return re;
    }
	
/**
 * 初始化控件
 */
function initWdiget(){
	//账单开始日期
	$('#date_timepicker_start').datetimepicker({
		format:'Y-m-d',
		lang:'ch',
		onSelectDate: function(ct,$i){
			$('#date_timepicker_end').val(addDate(ct,6));
		},
		timepicker:false,
		value:Commonjs.getDate(-6)
	});
	//账单结束日期
	$('#date_timepicker_end').datetimepicker({
		format:'Y-m-d',
		lang:'ch',
		onSelectDate: function(ct,$i){
			$('#date_timepicker_start').val(addDate(ct,-6));
		},
		timepicker:false,
		value:Commonjs.getDate(0)
	});
	
}

