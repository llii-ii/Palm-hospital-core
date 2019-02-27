	var session = null;
 	var PageSize = 3;
 	
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
	param.PageSize = PageSize;
	param.PageStart = (num-1);
	var d = _ajax('../../report/getReportList.do',param,false);
	Page(d.data.totalProperty,PageSize,'pager');
	$("#allTrueNum").text(d.data.allTrueNum);
	$("#allFalseNum").text(d.data.allFalseNum);
	loadData(d.data.result,num);
}

function loadData(d,num){
	var html = '';
	var allNum = 0;
	var businessId = '';
	var hospitalId = '';
	var pieNames = [];
	var pieData = [];
	
	if(d.length){	
		$.each(d,function(i,o) {
			i = (num - 1) * PageSize + i + 1;
		
			allNum = parseInt(o.reportTrue) + parseInt(o.reportFalse);
					
			html+= '<tr><td>'+i+'</td>';
			html+= '<td>'+o.businessName+'</td>';
			html+= '<td>'+allNum+'</td></tr>'; 
			pieData.push({value:allNum , name:o.businessName});
			allNum = 0;
			pieNames.push(o.businessName);
			
		});
	
	}else{
		html += '<tr align=center><td colspan=7><font color=red>未找到相关数据!</font></td></tr>';
	}
		
	echartsPie(pieNames,pieData);
	
	$("#dataList tr").eq(0).nextAll().remove();
	$(html).insertAfter($("#dataList tr").eq(0));
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
	
	
	
/**
 * 初始化控件
 */
function initWdiget(){
	
	//账单开始日期
	$('#date_timepicker_start').datetimepicker({
		format:'Y-m-d',
		lang:'ch',
		onShow:function( ct ){
//			this.setOptions({
//				maxDate:$('#date_timepicker_end').val()?$('#date_timepicker_end').val():false
//			})
		},
		timepicker:false,
		value:Commonjs.getDate(0)
	});
	//账单结束日期
	$('#date_timepicker_end').datetimepicker({
		format:'Y-m-d',
		lang:'ch',
		onShow:function( ct ){
//			this.setOptions({
//				minDate:$('#date_timepicker_start').val()?$('#date_timepicker_start').val():false
//			})
		},
		timepicker:false,
		value:Commonjs.getDate(0)
	});
	
}


function echartsPie(pieNames,pieData){
	var echartsPie;
	var option = {
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: pieNames
		    },
		    series : [
		        {
		            name: '数据占比',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            data:pieData,
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            }
		        }
		    ]
		};

	echartsPie = echarts.init(document.getElementById('echartsPie'));
	echartsPie.setOption(option);
}
