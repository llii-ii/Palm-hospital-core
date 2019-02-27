
$(function(){
	var apiData = {};	
	apiData.title = "test";
	apiData.content = "test";
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/qyWeChat/FaultDeclare/callApi.do',param,function(dd){
		console.log(dd);
	});
});