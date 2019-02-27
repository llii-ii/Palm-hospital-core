var hosId;
var deptName;
var deptCode;
var doctorName;
var doctorTitle = "";
var workDateStart = "";
var workDateEnd = "";
var pageIndex = 0;
var pageSize = 0;
var loading = false;
var levelObj;
var doctorTitleCode = '';
var d;
var stime;
var scheduleUrl = "/business/yygh/yyghScheduleList.html";
//var clinicMember = "/business/member/addMember.html";
var clinicMember = "/business/member/memberList.html";
$(function(){
		hosId = Commonjs.getUrlParam("HosId");
		deptCode = Commonjs.getUrlParam("deptCode");
		deptName = unescape(Commonjs.getUrlParam("deptName"));
		$("#detpName").html(deptName);
		initWidget();
		queryDept(); //初始化第一级科室
		queryDoctorLevel();
		queryDoctor(false); //查询医生
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
	$('.doc-filter li').on('click',function(){
		var index = $(this).index();
		$(this).toggleClass('curr').siblings().removeClass('curr');
		if(index == '0'){
			//筛选科室
			if($(this).hasClass('curr')){
				$('.dept-box').show().height($(window).height() - $('.doc-filter').height() - 1);
				$('.c-main').height($(window).height()).css('overflow','hidden');
			}else{
				deptHide();
			}
		}else{
			deptHide();	
		}
	});
	//筛选日期
	/*$("#filter-date").datetimePicker({
		title: '选择就诊日期',
        yearSplit: '年',
        monthSplit: '月',
        dateSplit: '日',
		times: function () {},
		onChange:function($k, values, displayValues){
			$('#dateword').html(values[0] + '-' + values[1] + '-' + values[2]);
		},
		onClose:function(){
			$('.doc-filter li').removeClass('curr');
			workDateStart = $('#dateword').html();
			workDateEnd = $('#dateword').html();
			queryDoctor(true);
		}
	});*/
	//科室选择
	$('.c-main').on('click','.dept li',function(){
		$(this).addClass('curr').siblings().removeClass('curr');
	});
	$('.c-main').on('click','.dept-right li',function(){
		$('#filter-dept').removeClass('curr').find('label').html($(this).find('a').html());
		deptHide();
		//执行筛选
	});
	$("#noData").hide();
}


//日历
function Draw(dateval, n, type) {
    var nowDate = dateval.getTime();
	if(type == '1'){
		var data = new Array('不限');
	}else{
		var data = new Array('不限日期');	
	}
	var show_day = new Array('星期日','星期一','星期二','星期三','星期四','星期五','星期六'); 
    for(var i=0;i<=n;i++){
        nowDate = dateval.getTime() + 86400000*i;
        var resDate = new Date(nowDate);
        var day = resDate.getDate();
        var mouth = resDate.getMonth() + 1;
        var year = resDate.getFullYear();
        var week = resDate.getDay();
		
        day = (day<10)?'0'+day:day;
        mouth = (mouth<10)?'0'+mouth:mouth;
		if(type == '1'){
			if(i == 0){
				data.push(year + '年 ' + mouth + '月 ' + day + '日 ' +' ' + show_day[week] + '' + '（今天）');		
			}else{
				data.push(year + '年 ' + mouth + '月 ' + day + '日 ' +' ' + show_day[week]);		
			}
		}else{
			data.push(year + '-' + mouth + '-' + day);
		}
    }
    return data;
}

;(function() {
    var day = queryYYRule();
    
})();

//隐藏科室
function deptHide(){
	$('.dept-box').hide();
	$('.c-main').height('auto').css('overflow','auto');
}
function queryDept(){
	var apiData = {};
	var param = {};
	apiData.HosId = hosId;
	param.apiParam = Commonjs.getApiReqParams(apiData,"",2008);
	Commonjs.ajax('../../wsgw/yy/QueryClinicDept/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
			var listHtml = '';
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	Commonjs.BaseForeach(jsons.Data, function (i, item) {
                		listHtml += '<li ';
                		if(deptCode == item.DeptCode){
                			listHtml += 'class="curr"';
                		}
                		listHtml += ' ><a href="javascript:;" onclick="findDoctor(\'' + item.DeptCode + '\',\'' + item.DeptName + '\');">'+item.DeptName+'</a></li>';
                    });
                }
                $(".dept ul").html(listHtml);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    },{showLoadding:false});
}
function queryDoctor(reload){
	if(reload){
		$("#doctors ul").html("");
	}
	var apiData = {};
	var param = {};
	apiData.HosId=hosId;
	apiData.DeptCode = deptCode;
	apiData.DeptName = deptName;
	apiData.SyncDoctor = 1;//同步医生信息
	apiData.DoctorTitle = doctorTitle;
	apiData.DoctorTitleCode=doctorTitleCode;
	apiData.WorkDateStart = workDateStart;
	apiData.WorkDateEnd = workDateEnd;
	param.apiParam = Commonjs.getApiReqParams(apiData,"",2007);
	Commonjs.ajax('../../wsgw/yy/QueryClinicDoctor/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
			var listHtml = '';
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	d = jsons.Data;
                	Commonjs.BaseForeach(jsons.Data, function (i, item) {
                		if(item.Url =="" || item.Url==null){
                			item.Url='https://v1kstcdn.kasitesoft.com/common/images/d-male.png';
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
                		}else if(item.DoctorIsHalt == '0'){
                			listHtml += ' class="btn-docfr disable"> 未开放';
                		}else{
                			listHtml += ' onclick="goToSchedule(\'' + item.DoctorCode + '\',\'' + deptCode + '\')" class="btn-docfr ">  预约';
                		}
                		listHtml += '</a></li>';
                	});
                	noDataHide();
                } else {
                	d = null;
                	noDataShow();
//                	myLayer.alert("无数据",3000);
                }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
            $("#doctors ul").html(listHtml);
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    });
}
function noDataHide(){
	$("#noData").hide();
}
function noDataShow(){
	$("#noData").show();
}
function londingHide(){
	$("#loading").hide();
}
function londingShow(){
	$("#loading").show();
}
function findDoctor(deptCodeParam,deptNameParam) {
	deptCode = deptCodeParam;
	deptName = deptNameParam;
	doctorTitleCode = "";
	doctorTitle = "";
	queryDoctorLevel();
	queryDoctor(true);
	$('#filter-dept').removeClass('curr').find('label').html($(this).find('a').html());
	deptHide();
	$("#detpName").html(deptName);
}
function goToSchedule(doctorCodeParam,deptCodeParam) {
//	localStorage.removeItem("stime");
	Commonjs.removeLocalItem("stime",false);
    if (scheduleUrl.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(scheduleUrl + "&HosId="+hosId+"&stime="  + workDateStart + "&doctorCode=" + doctorCodeParam + "&deptCode=" + deptCodeParam+ "&deptName=" + escape(deptName));
    } else {
        location.href = Commonjs.goToUrl(scheduleUrl + "?HosId="+hosId+"&stime="  + workDateStart + "&doctorCode=" + doctorCodeParam + "&deptCode=" + deptCodeParam+ "&deptName=" + escape(deptName));
    }
}

function queryDoctorLevel() {
	var disObj=new Array();
	var valObj=new Array();
	var apiData = {};
	var param = {};
	apiData.HosId = hosId;
	apiData.DeptCode=deptCode;
	param.apiParam = Commonjs.getApiReqParams(apiData,"",1022);
	disObj.push("全部");
	valObj.push('');
	Commonjs.ajax('../../wsgw/basic/QueryTitleInfo/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	var merberList = "";
                    if (jsons.Data != null) {
                    	Commonjs.BaseForeach(jsons.Data, function (i, item) {
                    		disObj.push(item.DoctorTitle);
                    		valObj.push(item.DoctorTitleCode);
                        });
                    }
                }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		initDoctorLevel(disObj,valObj);
    },{showLoadding:false});
	
}
//筛选职称
function initDoctorLevel(disObj,valObj){
	try{
		$("#filter-rank").picker("destroy");
	}catch(e){
		//防止职称控件未加载销毁报错而无法继续初始化;
	}
	$("#filter-rank").picker({
		title: "选择医生职称",
		cols: [
			{
				textAlign: 'center',
				displayValues:disObj,
				values: valObj
			}
		],
		onChange:function($k, values, displayValues){
			doctorTitleCode = values[0];
			doctorTitle = displayValues[0];
			$('#rankword').html(displayValues[0]);
		},
		onClose:function(){
			$('.doc-filter li').removeClass('curr');
			queryDoctor(true);
		},
		formatValue:function (p, values, displayValues){
			return displayValues;
		}
	});
	$('#rankword').html("医生职称");
	doctorTitleCode = '';
	doctorTitle = '';
}

function queryDoctorByData(){
	if(d == null){
		return;
	}
	var listHtml = '';
	Commonjs.BaseForeach(d, function (i, item) {
		if(!doctorTitleCode || item.DoctorTitleCode == doctorTitleCode){
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
			}else if(item.DoctorIsHalt == '0'){
    			listHtml += ' class="btn-docfr disable"> 未开放';
    		}else{
				listHtml += ' onclick="goToSchedule(\'' + item.DoctorCode + '\',\'' + deptCode + '\')" class="btn-docfr ">  预约';
			}
			listHtml += '</a></li>';
		}
    });
	$("#doctors ul").html(listHtml);
}
//查询预约规则的执行日期
function queryYYRule() {
    var formdate = new Date();
	var day = 14;
	var apiData = {};
	apiData.Param = Commonjs.getUrlParam("HosId");
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData,"","");
	Commonjs.ajax('../../wsgw/yy/QueryYYRule/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	Commonjs.BaseForeach(jsons.Data, function (i, item) {
                		var merberList = "";
                        if (item != null) {
                        	if(item.StartDay){
                        		day=item.StartDay;
                        	}
                        }
                	});
                }
            } 
        }
		$("#filter-date").picker({
	        title: "选择就诊日期",
	        cols: [
	            {
	                textAlign: 'center',
	                values: Draw(formdate,day,0),
					displayValues:Draw(formdate,day,1)
	            }
	        ],
	        onChange:function($k, values, displayValues){
	            $('#dateword').html(values);
	        },
			onClose:function(p){
				$('.doc-filter li').removeClass('curr');
				//执行筛选
				if('不限日期' == p.cols[0].value){
					workDateStart = "";
					workDateEnd = "";
				}else{
					workDateStart = p.cols[0].value;
					workDateEnd = p.cols[0].value;
				}
				queryDoctor(true);
				queryDoctorByData();
			}
	    });
    },{showLoadding:false});
	return day;
}
