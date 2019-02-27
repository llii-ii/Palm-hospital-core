$(function() {
	loadReportDetails();
});

/**
 * 获取报告单详细
 */
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
							var submissionTime = data.SubmissionTime;
							if(Commonjs.isNull(submissionTime)){
								submissionTime= Commonjs.getUrlParam('submissionTime');
							}
							reportInfo += '<div class="inqui-head c-pack"><p class="c-f15"><i class="sicon icon-rt">';
							reportInfo += '</i>' + data.PatientName + '</p>';
							reportInfo += '<p class="mt5">' + getSex(data.Sex) + ' ' + data.Age + '岁  住院号  ' + data.HosUserNo + ' </p>  <ul class="inqui-mess clearfix mt10">';
							reportInfo += '<li style = "width:40%">报告单号：' + apiData.ReportId + '</li>';
							reportInfo += '<li style = "width:60%">报告医生：' + data.Inspector + '</li>';
							reportInfo += '<li style = "width:40%">是否急诊：' + (data.IsEmergency == 1 ? '是' : '否') + '</li>';
							reportInfo += '<li style = "width:60%">报告时间：' + submissionTime + '</li></ul></div><div class="c-666 c-t-center ptb5">检验明细</div>';
							reportInfo += '<div><table class="charges-detail"><thead><tr><td width="20%">检测指标</td><td width="20%">结果</td><td width="20%">状态</td><td width="20%">参考值</td><td width="20%">单位</td></tr></thead>';
							reportInfo += '<tbody>';
							Commonjs.BaseForeach(data.Data_1, function(i, item) {
								reportInfo += '<tr><td>' + item.ItemDetailsName + '</td><td>' + item.ResultValue + '</td><td>' + getState(item.ResultValue, item.ReferenceValues) + '</td><td>' +
									item.ReferenceValues + '</td><td>' + item.Unit + '</td></tr>';
							});
							reportInfo += '</tbody>';
							if(Commonjs.isEmpty(data.PatientName) && Commonjs.isEmpty(data.Sex) && Commonjs.isEmpty(data.Age) && Commonjs.isEmpty(data.HosUserNo) && Commonjs.isEmpty(data.ReportingPhysicians) && Commonjs.isEmpty(data.IsEmergency) && Commonjs.isEmpty(data.ReportTime)) {
								hasData = true;
							}
						});
					}
					if(hasData) { //如果没有数据,则现实无数据页面
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
 * 获取状态值是否正常
 * @param resultValue 检查结果值 11.2
 * @param referenceValues 状态区间(11.2~20.5)
 * @returns {String}
 */
function getState(resultValue, referenceValues) {
	if(Commonjs.isEmpty(referenceValues)){
		return "";
	}
	var a = "";
	if(referenceValues.indexOf("~")>=0){
		a = referenceValues.split("~");
	}else if(referenceValues.indexOf("-")>=0){
		a = referenceValues.split("-");
	}
	if(parseFloat(resultValue) > parseFloat(a[1])) {
		return("↑");
	} else if(parseFloat(resultValue) < parseFloat(a[0])) {
		return("↓");
	} else {
		return("正常");
	}
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