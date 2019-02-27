/// <summary>
/// 科室介绍页面JS
/// </summary>
var hosId;
$(function(){
	hosId=Commonjs.getUrlParam("HosId");
	QueryDeptDetail();
});

function goToDoctor(deptCodeParam, deptNameParam){
	var doctorUrl = "/business/yygh/yyghDoctorList.html"
    location.href = Commonjs.goToUrl(doctorUrl + "?HosId="+hosId+"&deptCode=" + deptCodeParam + "&deptName=" + escape(deptNameParam));
}
/// <summary>
/// 获取科室详情
/// </summary>
function QueryDeptDetail() {	
    //查询科室详情
	var deptCode = Commonjs.getUrlParam("deptCode");
	var deptName = unescape(Commonjs.getUrlParam("deptName"));
	$("#aRegisterBtn").attr("href","javascript:goToDoctor('"+deptCode+"','"+deptName+"')");
	//数据定义
	var param = {};
	var apiData = {};
	apiData.HosId = hosId;
	apiData.DeptCode = deptCode;
	param.apiParam = Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/basic/QueryBaseDeptLocal/callApi.do';
	Commonjs.ajax(url,param,function(d){
		if(d.RespCode == 10000){
			if (!Commonjs.isEmpty(d.Data)) {
                var dept = d.Data[0];
                document.title = dept.DeptName;                //科室名称
                $("#spanDeptAddr").html(dept.Address);    //科室位置
                $("#spanIntro").html(dept.Intro);          //科室简介
            } else {
                //没有数据
            }
		}else{
			alert(d.RespMessage)//错误提示
		}
	});
		
}