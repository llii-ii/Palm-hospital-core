$(function() {
	loadReportDetails();
});



/** 获取报告单详细*/
function loadReportDetails() {
	var apiData = {};
	apiData.ReportId = Commonjs.getUrlParam('reportId');
	apiData.ReportType = Commonjs.getUrlParam('reportType');
	apiData.EventNo = Commonjs.getUrlParam('eventNo');
	apiData.SampleNo = Commonjs.getUrlParam('sampleNo');
    apiData.SubmissionTime = Commonjs.getUrlParam('submissionTime');
	if(Commonjs.isEmpty(apiData.ReportId) || Commonjs.isEmpty(apiData.ReportType)) {
		myLayer.alert("无报告");
		return;
	}
	var page = {};
	page.PIndex = 0;
	page.PSize = 0;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/report/GetReportInfo/callApi.do', param, function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					reportInfo = '';
					var hasData = false;
					if(Commonjs.ListIsNotNull(jsons.Data)) {
						Commonjs.BaseForeach(jsons.Data, function(i, item) {
							var data = item;
							reportInfo += '<div class="inqui-head c-pack"><p class="c-f15"><i class="sicon icon-rt">';
							reportInfo += '</i>' + data.PatientName + '</p>';
							reportInfo += '<p class="mt5">' + getSex(data.Sex) + ' ' + data.Age + '岁  住院号 ' + data.HosUserNo + ' </p>  <ul class="inqui-mess clearfix mt10">';
							reportInfo += '<li style = "width:40%">报告单号：' + apiData.ReportId + '</li>';
							reportInfo += '<li style = "width:60%">报告医生：' + data.ReportingPhysicians + '</li>';
							reportInfo += '<li style = "width:40%">是否急诊：' + (data.IsEmergency == 1 ? '是' : '否') + '</li>';
							reportInfo += '<li style = "width:60%">报告时间：' + data.ReportTime + '</li></ul></div><div class="c-666 c-t-center ptb5">检查明细</div>';
							reportInfo += '<div class="inqui-detail"><h4 class="c-333">检查所见</h4>';
							reportInfo += '<p>' + data.CheckToSee + '</p>';
							reportInfo += '</div><div class="c-t-right plr15 mt10 c-0093ff">具体结果以纸质报告为准</div>';
							if(Commonjs.isEmpty(data.PatientName)&& Commonjs.isEmpty(data.Sex)&&Commonjs.isEmpty(data.Age) && Commonjs.isEmpty(data.HosUserNo) && Commonjs.isEmpty(data.ReportingPhysicians) && Commonjs.isEmpty(data.IsEmergency)&& Commonjs.isEmpty(data.ReportTime)) {
								hasData = true;
							}
						});
					}
					if(hasData) { //如果没有数据,则显示无数据页面
						$(".nomess").css("display", "block");
						hasData = false;
					} else {
						$(".nomess").css("display", "none");
						$(".c-main").html(reportInfo);
					}
				} else {
					myLayer.alert(jsons.RespMessage, 3000);
				}
			}else{
				myLayer.alert("返回码为空");
			}
		} else {
			// 通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	});

}
/**
 * 获取性别
 * @param sex 1男2女3未知
 * @returns {String}
 */
function getSex(sex) {
	if(sex == 1) {
		return '男';
	} else if(sex == 2) {
		return '女';
	} else {
		return '未知';
	}
}