$(function(){
		var param = {};
		var Data = {};
		Data.Page={};
		Data.Page.PIndex = 0;
		Data.Page.PSize = 10;
		param.reqParam = Commonjs.getApiReqParams(Data);
		Commonjs.ajaxSync('/job/queryList.do',param,function(d){
			if(d.RespCode==10000){
				$("#scheduler").val(Commonjs.jsonToString(d.Data));
			}else{
				alert(d.RespMessage);
			}
		});
})
/**
 * 添加任务
 */
function addSchedulerJob(){
	var param = {};
	var data = {};
	data.BeanName='testJob';
	data.MethodName='testPrint';
	data.Params='{"message":"测试定时任务"}';
	data.CronExpression='0/20 * * * * ?';
	data.Remark='测试定时任务';
	param.reqParam = Commonjs.getApiReqParams(data);
	Commonjs.ajaxSync('/job/save.do',param,function(d){
		if(d.RespCode == '10000'){
			alert("添加成功");
		}else{
			alert(d.RespMessage);
		}
	});
}

/**
 * 更新定时任务
 */
function updateSchedulerJob(){
	if(Commonjs.isEmpty($("#jobId").val())){
		alert('请输入要定时任务ID');
		return;
	}
	var param = {};
	var data = {};
	data.JobId = $("#jobId").val();
	data.Params='';
	data.CronExpression='0/20 * * * * ?';
	param.reqParam = Commonjs.getApiReqParams(data);
	Commonjs.ajaxSync('/job/update.do',param,function(d){
		if(d.RespCode == '10000'){
			alert("修改成功");
		}else{
			alert(d.RespMessage);
		}
	});
}
/**
 * 删除定时任务
 */
function delSchedulerJob(){
	if(Commonjs.isEmpty($("#jobId").val())){
		alert('请输入要定时任务ID');
		return;
	}
	var param = {};
	var data = {};
	data.JobId = $("#jobId").val();
	param.reqParam = Commonjs.getApiReqParams(data);
	Commonjs.ajaxSync('/job/delete.do',param,function(d){
		if(d.RespCode == '10000'){
			alert("删除成功");
		}else{
			alert(d.RespMessage);
		}
	});
}

/**
 * 立即执行
 */
function runSchedulerJob(){
	if(Commonjs.isEmpty($("#jobId").val())){
		alert('请输入要定时任务ID');
		return;
	}
	var param = {};
	var data = {};
	data.JobId = $("#jobId").val();
	param.reqParam = Commonjs.getApiReqParams(data);
	Commonjs.ajaxSync('/job/run.do',param,function(d){
		if(d.RespCode == '10000'){
			alert("执行成功");
		}else{
			alert(d.RespMessage);
		}
	});
}
/**
 * 暂停任务
 */
function pauseSchedulerJob(){
	if(Commonjs.isEmpty($("#jobId").val())){
		alert('请输入要定时任务ID');
		return;
	}
	var param = {};
	var data = {};
	data.JobId = $("#jobId").val();
	param.reqParam = Commonjs.getApiReqParams(data);
	Commonjs.ajaxSync('/job/pause.do',param,function(d){
		if(d.RespCode == '10000'){
			alert("暂停成功");
		}else{
			alert(d.RespMessage);
		}
	});
}
/**
 * 恢复任务
 */
function resumeSchedulerJob(){
	if(Commonjs.isEmpty($("#jobId").val())){
		alert('请输入要定时任务ID');
		return;
	}
	var param = {};
	var data = {};
	data.JobId = $("#jobId").val();
	param.reqParam = Commonjs.getApiReqParams(data);
	Commonjs.ajaxSync('/job/resume.do',param,function(d){
		if(d.RespCode == '10000'){
			alert("恢复成功");
		}else{
			alert(d.RespMessage);
		}
	});
}