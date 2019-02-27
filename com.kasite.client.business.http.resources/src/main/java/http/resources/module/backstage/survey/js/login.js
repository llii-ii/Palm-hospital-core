
$( function() {
	var param = {};
	param.Api = "survey.SurveyApiImpl.deleteQuestion";
	param.Param ="{'QuestId':14}";
	doAjaxLoadData("../survey/MydDhdy_doAll.action", param, function(resp) {
		 if(resp.Code == 10000){
			alert(resp);
		 }});
			 
});

