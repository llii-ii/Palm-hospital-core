	var session = null;
 	var PageSize = 3;
 	
	$(function(){
		$("#pagenumber").val(1);
		queryWJData(1);
	});
	
	
	//查询按钮	
	$("#findButton").click(function(){
		queryWJData(1);
	});


	function queryWJData(num){
		var param = {};
		param.state=$("#state").val();
		param.name=$("#name").val();
		param.PageSize = PageSize;
		param.PageStart = (num-1);
		var d = _ajax('../../online/getOnlineList.do',param,false);
		Page(d.data.totalProperty,PageSize,'pager','pagenumber');
		initHtml(d.data.result,num);
	}
	
	function initHtml(d,num){
		var html = '';
		if(d.length){
			$.each(d,function(i,o) {
				i = (num - 1) * PageSize + i + 1;
				html+= '<tr><td>'+i+'</td>';
				html+= '<td>'+o.name+'</td>';
				if(o.state == 0){
					html+= '<td style="color:green">在线</td>';
				}else{
					html+= '<td style="color:red">断线</td>';
				}
				html+= '<td>'+o.dateTotal+'</td>';
				html+= '<td>'+o.dateOnline+'</td>';  
		        html+= '<td><a href="javascript:;" style="color:#4095ce" onclick="showDetail(\''+o.id+'\',\''+o.name+'\',1)">查看</a></td></tr>';       
			});
	
		}else{
			html += '<tr align=center><td colspan=7><font color=red>未找到相关数据!</font></td></tr>';
		}
		
		$("#dataList tr").eq(0).nextAll().remove();
		$(html).insertAfter($("#dataList tr").eq(0));	
	}
	
  	//分页
	function Page(totalcounts,pagecount,pager,pageNum) {
		$("#"+pager).pager( {
			totalcounts : totalcounts,
			pagesize : pagecount,
			pagenumber : $("#"+pageNum).val(),
			pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
			buttonClickCallback : function(al) {
				$("#"+pageNum).val(al);
				queryWJData(al);
			}
		});
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
//			ComWbj.alertIconNo('提示：',err,'error');
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
  	
  	


	function showDetail(id,name,num){
		$('.trend-detail').animate({'left':0},500);
		var param = {};
		var html = '';
		param.id = id;
		param.PageSize = PageSize;
		param.PageStart = (num-1);
		var d = _ajax('../../online/onlineDetailById.do',param,false);
		PageDetail(d.data.totalProperty,PageSize,id,name,'pager_del');
		detailHtml(d.data.result,name);
	}
	
	function hideDetail(){
		$('.trend-detail').animate({'left':'100%'},500);
	}
	
	function detailHtml(d,name){
		var html = '';
		if(d.length){
			$.each(d,function(i,o) {
				html+= '<tr><td>'+name+'</td>';
				if(o.state == 0){
					html+= '<td style="color:green">上线</td>';
				}else{
					html+= '<td style="color:red">断线</td>';
				}
				html+= '<td>'+o.date+'</td></tr>';      
			});
	
		}else{
			html += '<tr align=center><td colspan=7><font color=red>未找到相关数据!</font></td></tr>';
		}
		
		$("#alltableid tr").eq(0).nextAll().remove();
		$(html).insertAfter($("#alltableid tr").eq(0));	
	}	
	
  	//分页
	function PageDetail(totalcounts,pagecount,id,name,pager) {
		$("#"+pager).pager( {
			totalcounts : totalcounts,
			pagesize : pagecount,
			pagenumber : $("#pagenumber_del").val(),
			pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
			buttonClickCallback : function(al) {
				$("#pagenumber_del").val(al);
				showDetail(id,name,al);
			}
		});
	}
  	