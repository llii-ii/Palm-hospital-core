var hosId;
var deptName;
var deptCode;
var pageIndex = 0;
var pageSize = 0;
var loading = false;
var scheduleUrl = "/business/yygh/yyghScheduleList.html";
var deptDoctorSearch = "searchDocList.html";
$(function(){
	hosId = Commonjs.getUrlParam("HosId");
	deptCode = Commonjs.getUrlParam("deptCode");
	initWidget();
	$("#detpName").html(deptName);
	queryDoctor(); //查询医生
});
function initWidget(){
	//分页
	/*$(document.body).infinite().on("infinite", function() {
		if(loading) return;
		loading = true;
		setTimeout(function() {
			queryDoctor(false);
		}, 300);
	});*/
	//lab
	$('.c-main').on('click','.ser-into',function(){
		$('.ser-into').hide();
		$('.ser-main').show();
		$('.ser-input').focus();
	});
}
function goToDeptDoctorSearch() {
	if(!Commonjs.ListIsNotNull($("#searchName").val())){
		myLayer.alert("请输入查询条件",3000);
		return;
	}
    if (deptDoctorSearch.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(deptDoctorSearch + "&HosId="+hosId+"&searcheName=" + escape($("#searchName").val()));// + "&t="+ new Date().getTime();
    } else {
        location.href = Commonjs.goToUrl(deptDoctorSearch + "?HosId="+hosId+"&searcheName=" + escape($("#searchName").val()));// + "&t="+ new Date().getTime();
    }
}
//查询医t
function queryDoctor(){
	var apiData = {};
	var param = {};
	apiData.HosId = hosId;
	param.apiParam = Commonjs.getApiReqParams(apiData,"",2007);
	Commonjs.ajax('../../wsgw/yy/QueryHistoryDoctor/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		$("#doctors ul").html("");
		if (Commonjs.ListIsNotNull(jsons)) {
			var listHtml = '';
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	Commonjs.BaseForeach(jsons.Data, function (i, item) {
                		if(item.Url =="" || item.Url==null){
                			item.Url='../../common/images/d-male.png';
                		}
                		listHtml += '<li class="islink">' +
                    	'<div class="c-list-img"><img src="'+item.Url+'" alt="" /></div>' +
                        '<div class="c-list-mess">' +
                        	'<h4><span class="c-f15 c-333 mr5">'+item.DoctorName+'</span>'+item.DoctorTitle+'</h4>' +
                            '<p  class="c-nowrap mt5">'+item.Spec+'</p>' +
                        '</div>';
                        listHtml +=  '<a href="javascript:void(0)" ';
                		if(item.DoctorIsHalt == '2'){
                			listHtml += ' class="btn-docfr disable"> 停诊';
                		}else if(item.DoctorIsHalt == '7'){
                			listHtml += ' class="btn-docfr disable"> 约满';
                		}else{
                			listHtml += ' onclick="goToSchedule(\'' + item.DoctorCode + '\',\'' + item.DeptCode + '\')" class="btn-docfr ">  预约';
                		}
                		listHtml += '</a></li>';
                    });
                } else {
                	myLayer.alert("无数据",3000);
                }
               $("#doctors ul").append(listHtml);
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		myLayer.clear();
    });
}
function londingHide(){
	$("#loading").hide();
}
function londingShow(){
	$("#loading").show();
}
function goToSchedule(doctorCodeParam,deptCodeParam) {
//	localStorage.removeItem("stime");
	Commonjs.removeLocalItem("stime",false);
    if (scheduleUrl.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(scheduleUrl + "&HosId="+hosId+"&doctorCode=" + doctorCodeParam + "&deptCode=" + deptCodeParam);// + "&t="+ new Date().getTime();
    } else {
        location.href = Commonjs.goToUrl(scheduleUrl + "?HosId="+hosId+"&doctorCode=" + doctorCodeParam + "&deptCode=" + deptCodeParam);// + "&t="+ new Date().getTime();
    }
}

function gotoDeptSearch(){
	 location.href = Commonjs.goToUrl("/business/yygh/yyghDeptList.html?HosId="+hosId);// + "&t="+ new Date().getTime();
}

