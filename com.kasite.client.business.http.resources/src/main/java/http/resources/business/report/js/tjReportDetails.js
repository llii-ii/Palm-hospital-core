var TJH000="" ;
$(function () {
	queryBatUser();
	loadReportDetails();
})
function queryBatUser(){
	var apiData = {};
	var param = {};
	param.api = 'QueryBatUser'; 
	param.apiParam = Commonjs.getApiReqParams(apiData,"",15103);
	var url = '../../wsgw/bat/QueryBatUser/callApi.do';
	Commonjs.ajax(url,param,function(d){
		var html = '';
		if(d.RespCode == 10000){
			if(!Commonjs.isEmpty(d.Data)){
				var data = d.Data[0];
				if(!Commonjs.isEmpty(data.HeadImgUrl)){
					$("#batImg").attr("src",data.HeadImgUrl);
				}
			}
		}else{
			myLayer.alert(d.RespMessage,3000);
		}
	},{async:false});
}
/**
 * 获取报告单详细
 */
function loadReportDetails() {
	var apiData = {};
	apiData.ReportId = Commonjs.getUrlParam('reportId');
	var page = {};
	page.PIndex = 0;
	page.PSize = 0;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/report/GetTjReportInfo/callApi.do', param, function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					var html = '';
					reportInfo = '';
					var hasData = false;
					if(Commonjs.ListIsNotNull(jsons.Data)) {
						Commonjs.BaseForeach(jsons.Data, function(i, item) {
							var d = item;
							var submissionTime = d.TjDate;
							if (Commonjs.isNull(submissionTime)) {
								submissionTime = Commonjs.getUrlParam('submissionTime');
							}
							$("#reporttitle").html(d.ReportTitle);
							$("#submissionTime").html(submissionTime);
							html += '<table class="table">';
							html += '<thead>';
							html += '<tr>\r\n\r\n\r\n';
							html += '<th>综述：</th>';
							html += '</tr>';
							html += '<thead/>';
							html += '<tbody>';
							html += '<tr>';
							html += '<td>' + d.Summary + '</td>';
							html += '</tr>';
							html += '</tbody>';
							html += '</table>';
							html += '<div style="background-color:Green;height:2px;"></div>';
							html += '<table class="table">';
							html += '<thead>';
							html += '<tr>';
							html += '<th>建议：</th>';
							html += '</tr>';
							html += '<thead/>';
							html += '<tbody>';
							html += '<tr>';
							html += '<td>' + d.Advice + '</td>';
							html += '</tr>';
							html += '</tbody>';
							html += '</table>';
							html += '<div style="background-color:Green;height:2px;"></div>';
							Commonjs.BaseForeach(d.Data_1, function (i, item) {
				                html += '<table class="table">';
				                html += '<thead>';
				                html += '<tr>';
				                html += '<th class="span12" colspan="2">体检项目：' + item.ItemTitle + '</th>';
				                html += '</tr>';
				                if(item.Data_2!=undefined){
				                	 html += '<tr style="background-color:Green; color:White;">';
						                html += '<th class="span9">项目名称</th>';
						                html += '<th class="span9">结果</th>';
						                if(item.ItemType==1){
						                	 html += '<th class="span9">单位</th>';
						                     html += '<th class="span9">参考范围</th>';
						                }
						                html += '</tr>';
						                html += '</thead>';
						                html += '<tbody>';
						                Commonjs.BaseForeach(item.Data_2, function (i1, item1) {
						                    html += '<tr>';
						                    html += '<td>' + item1.ItemDetailTitle + '</td>';
						                    html += '<td>' + item1.ResultVal;
						                    html += '</td>';
						                    if(item.ItemType==1){
						                    	 html += '<td>' + item1.Unit + '</td>';
						                         html += '<td>' + item1.ReferenceValues + '</td>';
						                    }
						                    html += '</tr>';
						                });
				                }
				                if(item.ItemType==1){
				                	html += '<th class="span12" colspan="4">科室小结：' + item.DeptAdvice + '</th>';
				                }
				                else{
				                	 html += '<th class="span12" colspan="2">科室小结：' + item.DeptAdvice + '</th>';
				                }
				                html += '</tbody>';
				                html += '</table>';
				                html += '<div style="background-color:Green;height:2px;"></div>';
					        });
							hasData = true;
						});
					}
					if(hasData) { // 如果没有数据,则现实无数据页面
						$(".nomess").css("display", "block");
						hasData = false;
					} else {
						$(".nomess").css("display", "none");
						$(".c-main").html(reportInfo);
					}
					$("#tj_report_list").html(html);
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
