	var session = null;
 	var pageSize = 3;
	var newReplayHtml = null;
	var releaseHtml = null;
    var fileSize;
    var fileName;
    
    
$(function(){
	getHos();
	newReplayHtml = $("#newReplay").html();
	$("#newReplay").html("");
	releaseHtml = $("#release").html();
	$("#release").html("");
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
	var d = _ajax('../../standard/getStandardList.do',param,false);
	Page(d.data.totalProperty,pageSize,'pager');
	loadData(d.data.result);
}

function loadData(d){
	var html = '';
	
	if(d.length){
		$.each(d,function(i,o) {			
			html+= '<tr><td>'+o.name+'</td>';
			html+= '<td>'+o.describe+'</td>';
			html+= '<td>'+o.version+'</td>'; 
			html+= '<td><a href="javascript:;" style="color:#4095ce" onclick="releaseStandard(\''+o.id+'\',\''+o.name+'\')">发布</a></td>'; 
	     	html+= '<td><a href="javascript:;" style="color:#4095ce" onclick="delStandard(\''+o.id+'\',\''+o.name+'\')">删除</a></td></tr>';
		});
	
	}else{
		html += '<tr align=center><td colspan=7><font color=red>未找到相关数据!</font></td></tr>';
	}
	
	$("#dataList tr").eq(0).nextAll().remove();
	$(html).insertAfter($("#dataList tr").eq(0));
}


function getHos(){
	var html = '';
	var param = {};
	var d = _ajax('../../online/getAllHos.do',param,false);
	$.each(d.data.result,function(i,o) {			
		html+= '<option value="'+o.url+'">'+o.name+'</option>';
	});
	$("#formmatchrule option").eq(0).nextAll().remove();
	$(html).insertAfter($("#formmatchrule option").eq(0));
}


function release(id,name){
	var hosUrl = $('#formmatchrule option:selected').val();
    if (!hosUrl) {
    	Commonjs.alert("请选择医院!");
    	return false;
	}
	art.dialog({
 		id: 'testID',
 	    width: '245px',
 	    height: '109px',
 	    content: '您要发布标准集“'+name+'”吗？',
 	    lock: true,
 	    button: [{
 	      	name: '确定',
 	       	callback: function () {
 	       		var param = {};
		  		param.id = id;
		  		param.url = hosUrl;
		  		var _d = _ajax('../../standard/releaseFile.do',param,false);
		  		if(_d.respCode == 10000){
		  			ComWbj.artTips("提示","succeed","成功",2,null);
				}else{
					ComWbj.artTips("提示","warning","操作异常",2,null);
				}
 	       	}
 	 	},{
 	 		name: '取消'
 	 	}]
 	});	
}



function delStandard(id,name){
	art.dialog({
 		id: 'testID',
 	    width: '245px',
 	    height: '109px',
 	    content: '您要删除标准集“'+name+'”吗？注意：删除后无法恢复',
 	    lock: true,
 	    button: [{
 	      	name: '确定',
 	       	callback: function () {
 	       		var param = {};
		  		param.id = id;
		  		var _d = _ajax('../../standard/delStandard.do',param,false);
		  		if(_d.Code == 10000){
//					 ComWbj.alertIcon('提示：','成功','succeed');
		  			ComWbj.artTips("提示","succeed","成功",2,null);
						queryWJData($("#pagenumber").val());
				}else{
					ComWbj.artTips("提示","warning","操作异常",2,null);
//					ComWbj.alertIconNo('提示：','操作异常','warning');
				}
 	       	}
 	 	},{
 	 		name: '取消'
 	 	}]
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
	

	function releaseStandard(id,name){
	    var artBox=art.dialog({
	        lock: true,
	        opacity:0.4,
	        top : 8, 
	        width: 620,
	        title:'所发医院',
	        content: releaseHtml,
	        ok: function () {
	        	return release(id,name);
	        },
	        cancel: true
	    });	
	}
	

	function insertStandard(){
	    var artBox=art.dialog({
	        lock: true,
	        opacity:0.4,
	        top : 8, 
	        width: 620,
	        title:'新增标准',
	        content: newReplayHtml,
	        ok: function () {
	        	return uploadFile();
	        },
	        cancel: true
	    });	
	}


    // 动态获取文件名和文件大小，如果要做判断的话
    $("#uploadFile").change(function(){
        var file = this.files[0];
        fileName = file.name;
        fileSize = file.size;
    });

    function uploadFile() {
        var formData = new FormData();
        var file = document.getElementById("uploadFile").files[0];
        if (!file) {
        	Commonjs.alert("请选择标准集文件!");
        	return false;
		}
        if (!$("#version").val()) {
        	Commonjs.alert("请输入版本号!");
        	return false;
		}        
        
        formData.append("file",file);
        formData.append("fileName",fileName);
        formData.append("fileSize",fileSize);
        formData.append("version",$("#version").val());
        formData.append("describe",$("#describe").val());

    // 省略掉了success和error的处理
        sendFile("../../standard/uploadFile.do",formData,true,
			        function (res) {
			        	if(res.respCode==10000){
			        		queryWJData(1);
			        		Commonjs.alert(res.respMessage);
			        	}else{
			        		Commonjs.alert(res.respMessage);
			        	}

			        },function (XMLHttpRequest, textStatus, errorThrown) {
			        	Commonjs.alert("操作异常");
			        });
    }

    function sendFile(_url, _data, _async, _successCallback, _errorCallback) {
	    $.ajax({
	        type: "post",
	        async: _async,
	            url: _url,
	            dataType: 'json',
	            // 告诉jQuery不要去处理发送的数据
	            processData : false,
	            // 告诉jQuery不要去设置Content-Type请求头
	            contentType: false,
	            data: _data,
	            success: function (msg) {
	                _successCallback(msg);
	            },
	            error: function (error) {
	                _errorCallback(error);
	            }
	    });
    }
