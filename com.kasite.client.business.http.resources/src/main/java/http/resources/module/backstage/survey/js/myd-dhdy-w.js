$( function() {
	judgeSurvey();
});
var session = YihuUtil.getSession();
//判断该机构是否具有问卷调查
function judgeSurvey(){
	var param = {};
	param.Api="survey.SurveyApiImpl.querySubjectsByOrgId";
	param.Param = "{'OrgId':"+ session.orgid +"}";
	doAjaxLoadData("../MydDhdy_doAll.do", param, function(resp) {
		 if(resp.Code == 10000){
			Commonjs.alert(resp.Result,"add");
			alertwmk("提示",resp.Result,"succeed");
		 } else{
				if(resp.Message==undefined){
//					YihuUtil.art.warning('获取科室请求错误或超时');
					alertwmk("警告","获取科室请求错误或超时","warning");
				}else{
//					YihuUtil.art.error('获取科室失败'+ resp.Result);
					alertwmk("警告","获取科室失败","error");
				}
			}
	})
	}

//自定义弹出框
function alertwmk(a, b,icon) {
	ComWbj.artTips(a,icon,b,3,null);
}

// 消除特殊字符
function stripscript(id) {
	  var  val=$("#"+id).val();
// alert(val);
    var pattern = new RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\￥)(\……)(\*)(\&)(\【)(\】)(\。)(\，)(\%)(\^)(\&)(\*)(\-)(\_)(\+)(\=)(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/)(\?)]+/);
        var rs = "";
      
    for (var i = 0; i < val.length; i++) {
        rs = rs + val.substr(i, 1).replace(pattern, '');
    }
    return $("#"+id).val(rs);
}

function setTime() {
	setTimeout( function() {
	}, 2000);
}

