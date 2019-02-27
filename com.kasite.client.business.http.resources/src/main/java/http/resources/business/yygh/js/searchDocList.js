var hosId;
var pageIndex = 0;
var pageSize = 0;
var scheduleUrl = "/business/yygh/yyghScheduleList.html";
var doctorUrl = "/business/yygh/yyghDoctorList.html";
$(function(){
	hosId = Commonjs.getUrlParam("HosId");
	$("#searchName").val(unescape(Commonjs.getUrlParam("searcheName")));
	
	initWidget();
	query();
	
});
function isShowNoMessage(isShow,searchLike){
	if(isShow){
		$("#nomess .c-f00").html(searchLike);
		$("#nomess").show();
	}else{
		$("#nomess").hide();
	}
}
function initWidget(){
	$(function(){
		$('.dept-box').height($(window).height() - $('.com-lab').height() - $('.searchhold').height() - $('.deptspace').height() - 84);
		$(window).resize(function(){
			$('.dept-box').height($(window).height() - $('.com-lab').height() - $('.searchhold').height() - $('.deptspace').height() - 84);
		});
	});
	//科室选择
	$('.c-main').on('click','.dept-left li',function(){
		$(this).addClass('curr').siblings().removeClass('curr');
	});
	//搜索
	$('.c-main').on('click','.ser-into',function(){
		$('.ser-into').hide();
		$('.ser-main').show();
		$('.ser-input').focus();
	});
}
function searchClinicDeptAndDoctor(){
	var apiData = {};
	apiData.HosId = hosId;
	apiData.SearchLike = $("#searchName").val();
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData,"",2008);
	Commonjs.ajax('../../wsgw/yy/SearchClinicDeptAndDoctor/callApi.do?t=' + new Date().getTime(),param,function(data){
		if (data!=null && data.RespCode == 10000) {
			var deptHtml = '';
			var doctHtml = '';
			var deptNum = 0;
			var docNum = 0;
			if(Commonjs.isEmpty(data.Data)){
				$("#dept ul").html("");
		        $("#doctors ul").html("");
				$("#deptNum").html("为您找到0个科室");
	        	$("#doctorNum").html("为您找到0个医生");
	        	isShowNoMessage(true,$("#searchName").val())
	        	return;
			}
			$.each(data.Data,function (i, item) {
				if(item.SearchType==1){
					//科室
					deptNum++;
					deptHtml += ' <li class="islink"> ';
					deptHtml += '<div onclick="goToDoctor(\''+item.DeptCode+'\',\''+item.DeptName+'\')" class="c-list-info"><i class="c-f00">'+item.DeptName+'</i></div> ';
					deptHtml += ' </li> ';
				}else{
					//医生
					docNum++;
					if(item.Url =="" || item.Url==null){
            			item.Url='../../common/images/d-male.png';
            		}
					doctHtml += '<li class="islink"> ' +
                    '<div class="c-list-img"><img src="'+item.Url+'" alt="" /></div> '+
                    '<div class="c-list-mess"> '+
                    '    <h4><span class="c-f15 c-333 mr5"><i class="c-f00">'+item.DoctorName+'</i></span>'+item.DoctorTitle+'</h4> '+
                    '    <p class="c-nowrap mt5">'+item.DeptName+'</p> '+
                    '</div> ';
					doctHtml +=  '<a href="javascript:void(0)" ';
            		if(item.DoctorIsHalt == '2'){
            			doctHtml += ' class="btn-docfr disable"> 停诊';
            		}else if(item.DoctorIsHalt == '7'){
            			doctHtml += ' class="btn-docfr disable"> 约满';
            		}else{
            			doctHtml += ' onclick="goToSchedule(\'' + item.DoctorCode + '\',\'' + item.DeptCode + '\')" class="btn-docfr ">  预约';
            		}
            		doctHtml += '</a></li>';
				}
            });
			$("#deptNum").html("为您找到"+deptNum+"个科室");
			$("#doctorNum").html("为您找到"+docNum+"个医生");
			$("#dept ul").html(deptHtml);
	        $("#doctors ul").html(doctHtml);
	        if(deptNum>0 || docNum>0){
	        	isShowNoMessage(false,$("#searchName").val())
	        }else{
	        	isShowNoMessage(true,$("#searchName").val())
	        }
		}else {
        	$("#deptNum").html("为您找到0个科室");
        	$("#doctorNum").html("为您找到0个医生");
        	$("#dept ul").html("");
	        $("#doctors ul").html("");
        	isShowNoMessage(true,$("#searchName").val())
        }
	})
}
////查询科室
//function queryDept(){
//	var apiData = {};
//	apiData.HosId=Commonjs.hospitalId();
//	apiData.DeptName = deptName;
//	var param = {};
//	param.api = 'QueryClinicDept'; 
//	param.apiParam = Commonjs.getApiReqParams(apiData,"",2008);
//	Commonjs.ajax('../../wsgw/yy/QueryClinicDept/callApi.do?t=' + new Date().getTime(),param,function(jsons){
//		$("#dept ul").html("");
//		if (Commonjs.ListIsNotNull(jsons)) {
//			var listHtml = '';
//            if (jsons.RespCode == 10000) {
//                if (Commonjs.ListIsNotNull(jsons.Data)) {
//                	Commonjs.BaseForeach(jsons.Data, function (i, item) {
//                		listHtml += ' <li class="islink"> ';
//                		listHtml += '<div onclick="goToDoctor(\''+item.DeptCode+'\',\''+item.DeptName+'\')" class="c-list-info"><i class="c-f00">'+item.DeptName+'</i></div> ';
//                		listHtml += ' </li> ';
//                    });
//                	$("#deptNum").html("为您找到"+dataNum(jsons.Data)+"个科室");
//                	deptFlag = false;
//                } else {
//                	deptFlag =true;
//                	$("#deptNum").html("为您找到0个科室");
//                }
//                $("#dept ul").html(listHtml);
//            } else {
//            	deptFlag =true;
//            	$("#deptNum").html("为您找到0个科室");
//            }
//        } else {
//        	deptFlag =true;
//        	myLayer.alert('网络繁忙,请刷新后重试',3000);
//        }
//		myLayer.clear();
//    });
//};
////查询医生
//function queryDoctor(){
//	var apiData = {};
//	var param = {};
//	apiData.HosId=Commonjs.hospitalId();
//	apiData.DoctorName = doctorName;
//	param.api = 'QueryClinicDoctor'; 
//	param.apiParam = Commonjs.getApiReqParams(apiData,"",2007);
//	Commonjs.ajax('../../wsgw/yy/QueryClinicDoctor/callApi.do?t=' + new Date().getTime(),param,function(jsons){
//		$("#doctors ul").html("");
//		if (Commonjs.ListIsNotNull(jsons)) {
//			var listHtml = '';
//            if (jsons.RespCode == 10000) {
//                if (Commonjs.ListIsNotNull(jsons.Data)) {
//                	Commonjs.BaseForeach(jsons.Data, function (i, item) {
//                	if(item.Url =="" || item.Url==null){
//            			item.Url='../../common/images/d-male.png';
//            		}
//                		listHtml += '<li class="islink"> ' +
//                        '<div class="c-list-img"><img src="'+item.Url+'" alt="" /></div> '+
//                        '<div class="c-list-mess"> '+
//                        '    <h4><span class="c-f15 c-333 mr5"><i class="c-f00">'+item.DoctorName+'</i></span>'+item.DoctorTitle+'</h4> '+
//                        '    <p class="c-nowrap mt5">'+item.Spec+'</p> '+
//                        '</div> ';
//                		listHtml +=  '<a href="javascript:void(0)" ';
//                		if(item.DoctorIsHalt == '2'){
//                			listHtml += ' class="btn-docfr disable"> 停诊';
//                		}else if(item.DoctorIsHalt == '7'){
//                			listHtml += ' class="btn-docfr disable"> 约满';
//                		}else{
//                			listHtml += ' onclick="goToSchedule(\'' + item.DoctorCode + '\',\'' + item.DeptCode + '\')" class="btn-docfr ">  预约';
//                		}
//                		listHtml += '</a></li>';
//                    });
//                	doctorFlag = false;
//                	$("#doctorNum").html("为您找到"+dataNum(jsons.Data)+"个医生");
//                }else{
//                	doctorFlag = true;
//                	$("#doctorNum").html("为您找到0个医生");
//                }
//               $("#doctors ul").html(listHtml);
//            } else {
//            	doctorFlag = true;
//            	$("#doctorNum").html("为您找到0个医生");
//            }
//        } else {
//        	doctorFlag = true;
//        	myLayer.alert('网络繁忙,请刷新后重试',3000);
//        }
//		myLayer.clear();
//    });
//}
function goToSchedule(doctorCodeParam,deptCodeParam) {
//	localStorage.removeItem("stime");
	Commonjs.removeLocalItem("stime",false);
    if (scheduleUrl.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(scheduleUrl + "&HosId="+hosId+"&doctorCode=" + doctorCodeParam + "&deptCode=" + deptCodeParam);// + "&t= "+ new Date().getTime();
    } else {
        location.href = Commonjs.goToUrl(scheduleUrl + "?HosId="+hosId+"&doctorCode=" + doctorCodeParam + "&deptCode=" + deptCodeParam);// + "&t= "+ new Date().getTime();
    }
}
//function isHasData(){
//	if(deptFlag && doctorFlag){
//		$("#nomess .c-f00").html(doctorName);
//		$("#nomess").show();
//	}else{
//		$("#nomess").hide();
//	}
//}
//查询到的对象的长度
//function dataNum(obj){
//	if(obj.length){
//		return obj.length;
//	}
//	return 1;
//}
function query(){
	if(!Commonjs.ListIsNotNull($("#searchName").val())){
		myLayer.alert("请输入查询条件",3000);
		return;
	}
	searchClinicDeptAndDoctor();
}

function goToDoctor(deptCodeParam, deptNameParam){
	if (doctorUrl.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(doctorUrl + "&HosId="+hosId+"&deptCode=" + deptCodeParam + "&deptName=" + escape(deptNameParam));// + "&t"+ new Date().getTime();
    } else {
        location.href = Commonjs.goToUrl(doctorUrl + "?HosId="+hosId+"&deptCode=" + deptCodeParam + "&deptName=" + escape(deptNameParam));// + "&t="+ new Date().getTime();
    }
}

