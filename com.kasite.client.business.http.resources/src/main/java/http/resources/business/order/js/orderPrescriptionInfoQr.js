var guideContent;
$(function(){
	//获取二维码信息Id
	var guideId = Commonjs.getUrlParam("guid");
	//获取二维码信息点
	getGuide(guideId);
	
	//初始化控件
	iniWidget(guideId);
});

function iniWidget(guideId){
	$("#imgLogo").attr('src',logoUrl);
	//初始化医院名称
	$("#pHosName").html(Commonjs.getSession().orgName);
	$("#pDescHosName").html(Commonjs.getSession().orgName);
	
	$("#btnCheckIdCardNo").on('click',function(){
		validateIdCardNo(guideId);
	});
	
}

/**
 * 获取信息点
 * @param guideId
 * @returns
 */
function getGuide(guideId){
	var param={};
	param.api="GetGuide";
	var data={};
	data.GuideId = guideId;
	param.apiParam = Commonjs.getApiReqParams(data);
	Commonjs.ajax('/wsgw/smartPay/GetGuide/callApi.do',param, function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					if(Commonjs.ListIsNull(jsons.Data)) {
						var data = jsons.Data[0];
						if(Commonjs.isEmpty(data.Content)) {
							myLayer.alert("获取处方用户信息错误！请重新扫描！");
							return;
						}
					    guideContent = JSON.parse(data.Content);
						$("#checkMemberName").html(guideContent.memberName);
						$("#checkDoctorName").html(guideContent.doctorName);
					}
				} else {
					myLayer.alert(jsons.RespMessage, 3000);
				}
			} else {
				myLayer.alert("返回码为空");
			}
		} else {
			//通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	},{async:false});
}

function validateIdCardNo(guideId){
	var idcardNo = $("#preIdCard").val();
	if(Commonjs.isEmpty(idcardNo)){
    	myLayer.alert("请输入处方患者身份证后六位",3000);
    	return;
	}
	if(idcardNo.length!=6){
    	myLayer.alert("请输入处方患者身份证后六位",3000);
    	return;
	}
	var apiData = {};
	apiData.GuideId = guideId;
	apiData.IdCardNo = idcardNo;
	var page = {};
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData,page);
	Commonjs.ajax('/wsgw/smartPay/PrescQrValidateBefore/callApi.do', param, function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					var prescInfoUrl = "/business/order/orderPrescriptionInfo.html?1=1";
					if( !Commonjs.isEmpty(guideContent.hisOrderId)){
						prescInfoUrl += "&hisOrderId="+guideContent.hisOrderId;
					}
					
					if( !Commonjs.isEmpty(guideContent.prescNo)){
						prescInfoUrl += "&prescNo="+guideContent.prescNo;
					}
					location.href =Commonjs.goToUrl(prescInfoUrl);
				}else{
	            	myLayer.alert(jsons.RespMessage,3000);
				}
			}else{
	        	myLayer.alert('网络错误，请刷新后重试',3000);
			}
		}else{
        	myLayer.alert('网络错误，请刷新后重试',3000);
		}
	},{async:false});
}

