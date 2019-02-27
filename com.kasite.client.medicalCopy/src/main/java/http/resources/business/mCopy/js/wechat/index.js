function getCaseList() {
	if($("#name").val() == '' || $("#name").val() == null || $("#name").val() == undefined){
		mui.alert('姓名不能为空');
	}else if($("#number").val() == '' || $("#number").val() == null || $("#number").val() == undefined){
		mui.alert('病案号不能为空');
	}else{
		var apiData = {};	
		apiData.name = $("#name").val();
		apiData.cardNo = $("#number").val();
		apiData.cardType = "6";
		apiData.orgCode = "AH01";
		var param = {};
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('/wsgw/medicalCopy/QueryPatientInfoByNos/callApi.do',param,function(dd){
			if(dd.RespCode == -14018){
				mui.alert('查无此病人');
			}else if(dd.RespCode == 10000){
				var id = dd.Data[0].McopyUserId;
				if(id == "noUser"){
					mui.alert('姓名与病案号不一致请重新输入');
				}else{
					var url = 'list.html?McopyUserId='+id;
				    location.href = Commonjs.goToUrl(url); 
				}
			}
		});
	}
}