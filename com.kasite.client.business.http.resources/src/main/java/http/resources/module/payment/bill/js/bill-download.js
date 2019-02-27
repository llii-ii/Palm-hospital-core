var channelInfoList = undefined;
$(function(){
	laydate.render({
		elem: '.datetimepicker',
  		range: true
	});
	findAllChennel();
	if(channelInfoList.length > 0){
		for(var i=0;i<channelInfoList.length;i++){
			var obj = channelInfoList[i];
			var $label = $("<label class=\"radio mr30\" data-toggle=\"radio\"></label>");
			var $radio = $("<input type=\"radio\" class=\"c-hide\" name=\"qd\" value=\""+obj.ChannelId+"\" />");
			var $li = $("<i class=\"icon-radio\"></i>");
			$label.append($radio);
			$label.append($li);
			$label.append(obj.ChannelName);
			$("#channelId").append($label);
		}
	}
	var yeastDayTime = Commonjs.getDate(-1);
	$("#pointThis").after("每日11:00前完成数据更新，当前数据更新至"+yeastDayTime+"；");
	var startTime = Commonjs.getDate(0);
	var queryDate = startTime + " ~ " + startTime;
	$('#queryDate').val(queryDate);
});
//导出Excel文件
function downloadFile(){
	var queryDate = $('#queryDate').val();
	var date = queryDate.split("~");
	var startDate = date[0];
	var endDate = date[1];
	var channelId = $("input[name='qd']:checked").val();
	var fileType = $("input[name='wj']:checked").val();
	
	
	var Service = {};
	Service.StartDate = startDate;
	Service.EndDate = endDate;
	Service.ChannelId = channelId;
	Service.FileType = fileType;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/billDownload/downloadAllBillFile.do",param, function(d){
		if(d.RespCode==10000){
			var filePath = d.Data[0].FilePath;
			window.location.href = location.protocol + '//' + location.host + "/" + filePath + "?token=" + Commonjs.getToken();
			Commonjs.alert("下载完成!");
		}
	});
}

//获取所有的渠道场景
function findAllChennel(){
	var Service = {};
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync("/channel/queryAllChennel.do",param,function(d){
		if(d.RespCode==10000){
			channelInfoList = d.Data;
		}
	});
}