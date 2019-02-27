
var subjectId = "";
var objtype = "";
$(function(){
	objtype = Commonjs.getUrlParam('objtype');
	subjectId = Commonjs.getUrlParam('subjectId');
	gotoUrl();
	QuerySubjectById();
 });
 function gotoUrl(){
 	$("#sureID").attr("href","survey_home.html?objtype="+objtype);
 }
 
 function QuerySubjectById() {
	    //调查问卷ID
	    if (subjectId==null || subjectId == "0") {
	        alertNew('调查问卷ID为空!');
	        return;
	    }
		var apiData = {};
		apiData.SubjectId= subjectId;
		apiData.HosId = Commonjs.getUrlParam("HosId");
		var param = {};
		param.api = ''; 
		param.apiParam =Commonjs.getApiReqParams(apiData);
	    //查询调查问卷信息
		var url = '../../wsgw/survey/QuerySubjectById/callApi.do?t=' + new Date().getTime();
		Commonjs.ajax(url,param,function(d){
		    if (d) {
		        if (d.RespCode == 10000) {
		        	if(!Commonjs.isEmpty(d.Data)){
		        		var jsons = d.Data;
			            if (jsons.Status == "0" || jsons.Status == "4" || jsons.Status == "5") {
			                //0:无效，4:待审核，5:审核不通过
			                $("#divNoSurvey").show();
			                $("#divSurveyList").hide();
			                $("#divNoSurvey").html('<i class="icon"></i>该问卷不存在，感谢您的参与！');
			                 $(".satihold").hide();
			                 $("#divNoSurvey").show();
			            } else if (jsons.Status == "1") {
			                //1:有效(未发布)
			                $("#divNoSurvey").show();
			                $("#divSurveyList").hide();
			                $("#divNoSurvey").html('<i class="icon"></i>该问卷已关闭，感谢您的参与！');
			                 $(".satihold").hide();
			                 $("#divNoSurvey").show();
			            } else if (jsons.Status == "3") {
			                //3:已结束
			                $("#divNoSurvey").show();
			                $("#divSurveyList").hide();
			                $("#divNoSurvey").html('<i class="icon"></i>该问卷已结束，感谢您的参与！');
			                 $(".satihold").hide();
			                 $("#divNoSurvey").show();
			            } else {
			            	$(".sat_succ p").html(jsons.EndingIntro);
			            }
		        	}
		        } else {
		            //方法错误
					myLayer.alert(d.RespMessage);
		        }
		    } else {
		        //通信失败 
		    	myLayer.alert('网络繁忙,请刷新后重试');
		    }
		});
	} 