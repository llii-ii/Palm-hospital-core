var hosId;
var doctorCode;
var deptCode;
var doctorName;
var deptName;
var doctorTitle;
var cDate;
var regDate;
var timeSlice;
var scheduleId;
var orderid;
var sqNo;
var sourceCode;
var fee;
var zs;
var xw;
var totalFee;
var stime = "";
var weekday=new Array('星期天','星期一','星期二','星期三','星期四','星期五','星期六');
var clinicMsg = "/business/yygh/yyghClinicMsg.html";
var clinicMember = "/business/member/memberList.html";
var isShowRegFee;//是否显示挂号费
$(function(){
	hosId = Commonjs.getUrlParam("HosId");
	doctorCode = Commonjs.getUrlParam("doctorCode");
	deptCode = Commonjs.getUrlParam("deptCode");
	deptName = unescape(Commonjs.getUrlParam("deptName"));
	$("#detpName").html(deptName);
	stime = Commonjs.getUrlParam("stime");
	//是否显示挂号费 true 显示  false 不显示。
	isShowRegFee = Commonjs.getIsShowRegFee();
	if(!isShowRegFee){
		$("#isShowRegFee").html("");
	}
	initWidget();
	getDoctorInfo(); //获取医生信息
	querySchedule();
});
function initWidget(){
	$('.c-main').on('click','.di-sche a',function(){
		 var ishalt = $(this).attr("data-ishalt");
		 //1出诊，2停诊，3替诊，4可约，5:可挂，6:申请 7:已约满，
		//停诊和暂停预约都不允许挂号   预约申请： 目前不支持预约申请，后续有做再开放
		 if(ishalt==2 || ishalt==1 || ishalt==7 || ishalt==6 ){
			 return;
		 }
		fee = $(this).attr("data-money");
		treatFee = $(this).attr("data-treatFee");
		$("#fee").html(Number(Commonjs.centToYuan(fee)));
		if(treatFee*1 > 0){
			$("#treatFee").html(Number(Commonjs.centToYuan(treatFee)));
			$("#span_treatFee").show();
		}
		regDate = $(this).attr("data-regDate");
		timeSlice = $(this).attr("data-timeSlice");
		scheduleId = $(this).attr("data-id");
		totalFee = $(this).attr("data-totalFee");
		cDate =$(this).attr("data-cData");
		$('.di-sche a').removeClass('curr');
		$(this).addClass('curr');
		$('.btn-appoin').removeClass('disable');
	});
	$('.c-main').on('click','.btn-appoin',function(){
		var disable = $(this).hasClass('disable');
		if(disable){
			myLayer.alert("请选择排班");
			return;
		}
		goToClinicMsg();
	});
}
//查询排班
function querySchedule(){
	var apiData = {};
	var param = {};
	var page = {};
	page.pageIndex = 0;
	page.pageSize = 100;
	apiData.HosId = hosId;
	apiData.DeptCode = deptCode;
	apiData.DoctorCode = doctorCode;
	apiData.WorkDateStart = stime;
	apiData.WorkDateEnd = stime;
	param.api = 'QueryClinicSchedule'; 
	param.apiParam = Commonjs.getApiReqParams(apiData,"",2001);
	Commonjs.ajax('../../wsgw/yy/QueryClinicSchedule/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
			var listHtml = '';
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	var mini = 0;
                	var maxi = queryYYRule();
                	var listHtml = '<div class="ui-col-0"> '+
                	'<dl class="di-left"> '+
                	'<dd>排班</dd> '+
                	'<dd>上午</dd> '+
                	'<dd>下午</dd> '+
	                '</dl> '+
	            	'</div> '+
            		'<div class="ui-col-1 di-list" >';
//                	if(Commonjs.isEmpty(stime) &&  !Commonjs.isEmpty(localStorage.getItem("stime"))){
//                		stime = localStorage.getItem("stime");
//                	}
                	if(Commonjs.isEmpty(stime) &&  !Commonjs.isEmpty(Commonjs.getLocalItem("stime",false,false))){
                		stime = Commonjs.getLocalItem("stime",false,false);
                	}
                	if(!Commonjs.isEmpty(stime)){
                		var sTimeArray = stime.split("-");
                		var dateNow = new Date();
                		var beginDate = new Date(dateNow.getFullYear(), dateNow.getMonth(), dateNow.getDate());
                		var endDate = new Date(sTimeArray[0], Number(sTimeArray[1])-1, sTimeArray[2]);
                		var result = (endDate-beginDate)/(24*60*60*1000);
                		mini = result;
                		maxi = result+1;
                	}
                	for (var i = mini; i < maxi; i++) {
                		var dateObj = getTimeRenct(i).split(",");
            			var dateStr = dateObj[0];
            			var weekStr = dateObj[1];
            			var sdataStr = dateObj[2];
            			zs = '<dd></dd>';
            			xw = '<dd></dd>';
            			listHtml += '<dl class="di-sche">';
            			listHtml +=  	'<dd>'+sdataStr+'<br />'+weekStr+'</dd>';
            			Commonjs.BaseForeach(jsons.Data, function (i, item) {
                    		if(item.RegDate == dateStr){
                    			var totalFee = Number(item.RegFee)+Number(item.TreatFee)+Number(item.OtherFee);
                    			var leftCount = item.LeaveCount;
                    			var leftNumHtml = '';
                    			if(Commonjs.isNotNull(leftCount) && leftCount * 1 > 0){
                    				leftNumHtml= '余号' + leftCount+'<br />';
                    			}
                    			var regFeeHtml = leftNumHtml+'￥'+Number(Commonjs.centToYuan(item.RegFee));
                    			//如果不显示挂号费 则显示状态 //1出诊，2停诊，3替诊，4可约，5:可挂，6:申请 7:已约满，
                    			if(!isShowRegFee){
                    				regFeeHtml = "可挂";
                    			}
                    			var isHalt = item.IsHalt * 1;
                    			if(isHalt != 3 && isHalt != 4 && isHalt != 5){
                    				regFeeHtml = item.IsHaltStr;
                    			}
                    			var diyJson = item.DiyJson;
                    			if(Commonjs.isNotNull(diyJson)){
                    				var diyJsonObj = JSON.parse(diyJson);
                    				regFeeHtml = diyJsonObj.scheduleStr;
                    			}
                    			
                				if(item.TimeSlice == '1'){
                    				zs= 	'<dd><a data-totalFee="'+totalFee+'" data-cData = "'+dateObj[3]+'早上" data-timeSlice="'+item.TimeSlice+'" data-regDate="'+item.RegDate+'" data-id="'+item.ScheduleId+'" data-money="'+item.RegFee+'" data-ishalt="'+item.IsHalt+'" data-treatFee="'+item.TreatFee+'" href="javascript:void(0);">'+ regFeeHtml +'</a></dd>';
                    			}else if(item.TimeSlice == '2'){
                    				xw = 	'<dd><a data-totalFee="'+totalFee+'" data-cData = "'+dateObj[3]+'下午" data-timeSlice="'+item.TimeSlice+'" data-regDate="'+item.RegDate+'" data-id="'+item.ScheduleId+'" data-money="'+item.RegFee+'" data-ishalt="'+item.IsHalt+'" data-treatFee="'+item.TreatFee+'" href="javascript:void(0);">'+ regFeeHtml +'</a></dd>';
                    			}else if(item.TimeSlice == '3'){
                    				ws = 	'<dd><a data-totalFee="'+totalFee+'" data-cData = "'+dateObj[3]+'晚上" data-timeSlice="'+item.TimeSlice+'" data-regDate="'+item.RegDate+'" data-id="'+item.ScheduleId+'" data-money="'+item.RegFee+'" data-ishalt="'+item.IsHalt+'" data-treatFee="'+item.TreatFee+'" href="javascript:void(0);">'+ regFeeHtml +'</a></dd>';
                    			}else if(item.TimeSlice == '0'){
                    				zs = 	'<dd><a data-totalFee="'+totalFee+'" data-cData = "'+dateObj[3]+'早上" data-timeSlice="'+item.TimeSlice+'" data-regDate="'+item.RegDate+'" data-id="'+item.ScheduleId+'" data-money="'+item.RegFee+'" data-ishalt="'+item.IsHalt+'" data-treatFee="'+item.TreatFee+'" href="javascript:void(0);">'+ regFeeHtml +'</a></dd>';
                    				xw = 	'<dd><a data-totalFee="'+totalFee+'" data-cData = "'+dateObj[3]+'下午" data-timeSlice="'+item.TimeSlice+'" data-regDate="'+item.RegDate+'" data-id="'+item.ScheduleId+'" data-money="'+item.RegFee+'" data-ishalt="'+item.IsHalt+'" data-treatFee="'+item.TreatFee+'" href="javascript:void(0);">'+ regFeeHtml +'</a></dd>';
                    			}
                    		}
                        });
            			listHtml += (zs+xw);
            			listHtml += '</dl>';
            		}
                	listHtml += '</div>';
                	$("#schedule").html(listHtml);
                } else {
                	myLayer.alert("无数据",3000);
                }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    });
}
//处理时间
function  getTimeRenct(count){
	  var week = '';
	  var dateStr=''; 
	  var cdateStr = '';
	  var sdateStr = '';
	  var curDate = new Date();;
	  var d=new Date(curDate.setDate(Number(curDate.getDate())+Number(count)));
	  week =weekday[d.getDay()];
	  dateStr+=d.getFullYear()+'-'; 
	  dateStr+=(d.getMonth() + 1 +'').length == 1 ? '0'+(d.getMonth() + 1) : d.getMonth() + 1; 
	  dateStr+='-';
	  dateStr+=(d.getDate()+'').length == 1 ? '0'+d.getDate() : d.getDate();
	  cdateStr+=d.getFullYear()+'年'; 
	  cdateStr+=(d.getMonth() + 1 +'').length == 1 ? '0'+(d.getMonth() + 1) : d.getMonth() + 1; 
	  cdateStr+='月';
	  cdateStr+=(d.getDate()+'').length == 1 ? '0'+d.getDate() : d.getDate();
	  cdateStr+='日 '+week+' ';
	  sdateStr+=(d.getMonth() + 1 +'').length == 1 ? '0'+(d.getMonth() + 1) : d.getMonth() + 1; 
	  sdateStr+='-';
	  sdateStr+=(d.getDate()+'').length == 1 ? '0'+d.getDate() : d.getDate();
	  return dateStr+','+week+','+sdateStr+','+cdateStr; 
}
//查询医生
function getDoctorInfo() {
	var apiData = {};
	var param = {};
	var page = {};
	page.pageIndex = 0;
	page.pageSize = 1;
	apiData.HosId = hosId;
	apiData.DeptCode = deptCode;
	apiData.DoctorCode = doctorCode;
	param.api = 'QueryClinicDoctor';
	param.apiParam = Commonjs.getApiReqParams(apiData,"",1002);
	Commonjs.ajax('../../wsgw/yy/QueryClinicDoctor/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	var docObj = jsons.Data[0];
                	$("#doctorName").html(docObj.DoctorName);
                	$("#spec").html(docObj.Spec);
                	$("#doctorTitle").html(docObj.DoctorTitle+'<span id="deptNames" class="ml10">'+docObj.DeptName+'</span>');
                	$("#deptName").html(docObj.DeptName);
            		if(docObj.Url =="" || docObj.Url==null){
            			docObj.Url='https://v1kstcdn.kasitesoft.com/common/images/d-male.png';
            		}
            		$("#headImg").attr('src',docObj.Url)
                	deptName = docObj.DeptName;
                	doctorName = docObj.DoctorName;
                	doctorTitle = docObj.DoctorTitle;
                } else {
                	myLayer.alert("无数据",3000);
                }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    },{showLoadding:false});
}
function noPerson(){
	var phtml = '';
	phtml += '<div class="addpsonpop">';
	phtml += '	<div class="addpson">';
	phtml += '		<i class="sicon icon-anclose"></i>';
	phtml += '		<i class="an-jzrtb"></i>';
	phtml += '		<div class="h50 c-999 c-f15">执行该业务操作前需先添加就诊人</div>';
	phtml += '		<a href="javascript:void(0);" onclick="goToClinicMember(\'addm\')" class="btn-anadd mt10">立即添加</a>';
	phtml += '	</div>';
	phtml += '	<div class="addpson-mb"></div>';
	phtml += '</div>';
	$('body').append(phtml);
	$('.icon-anclose').on('click',function(){
		$('.addpsonpop').remove();
	});
}

function chcekMemberExsit() {
	var result;
	var apiData = {};
	apiData.HosId = Commonjs.getUrlParam("HosId");
	var param = {};
	apiData.HosId = hosId;
	apiData.OpId = Commonjs.getOpenId();
	param.apiParam = Commonjs.getApiReqParams(apiData,"",8002);
	Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	result = true;
                } else {
                	result = false;
                }
            } else {
            	result = false;
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	result = false;
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    },{async:false});
	return result;
}

function chcekMemberCardExsit() {
	var result;
	var apiData = {};
	var param = {};
	apiData.HosId = Commonjs.getUrlParam("HosId");
	apiData.OpId=Commonjs.getOpenId();
	apiData.CardType="1";
	param.api = 'QueryMemberList';
	param.apiParam = Commonjs.getApiReqParams(apiData,"",8002);
	Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	result = true;
                } else {
                	result = false;
                }
            } else {
            	result = false;
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	result = false;
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    },{async:false});
	return result;
}
function goToClinicMsg() {
	//是否开放门诊无卡预约  true 开放，false 不开放 默认false
	var isOpenYYNotClinicCard = Commonjs.getIsOpenYYNotClinicCard();
//	if(!chcekMemberExsit()){
//		noPerson();
//		return;
//	}
//	if(!isOpenYYNotClinicCard){
//		if(!chcekMemberCardExsit()){
//			myLayer.tip({
//				con:'请先绑定就诊卡',
//				cancel:function(){
//					goToClinicMember('addc');
//				}
//			});
//			return;
//		}
//	}
//	localStorage.setItem("stime",stime);//保存查询时间方便返回
	Commonjs.setLocalItem("stime",stime,false);//保存查询时间方便返回
	if (clinicMsg.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(clinicMsg   + '&HosId='+hosId+'&deptCode=' + deptCode + '&headImg=' + escape($("#headImg").attr('src')) + '&doctorCode=' + doctorCode + '&doctorTitle=' + escape(doctorTitle) + '&deptName=' + escape(deptName) + '&doctorName=' + escape(doctorName) + '&totalFee=' + totalFee + '&fee=' + fee + '&cDate=' + escape(cDate) +'&regDate=' + regDate + '&timeSlice=' + timeSlice + '&scheduleId=' + scheduleId);// +'&t=' + new Date().getTime();
    } else {
        location.href = Commonjs.goToUrl(clinicMsg   + '?HosId='+hosId+'&deptCode=' + deptCode + '&headImg=' + escape($("#headImg").attr('src')) + '&doctorCode=' + doctorCode + '&doctorTitle=' + escape(doctorTitle) + '&deptName=' + escape(deptName) + '&doctorName=' + escape(doctorName) + '&totalFee=' + totalFee +  '&fee=' + fee + '&cDate=' + escape(cDate) +'&regDate=' + regDate + '&timeSlice=' + timeSlice + '&scheduleId=' + scheduleId);// +'&t=' + new Date().getTime();
    }
}
function goToClinicMember(toFlag) {
    if (clinicMember.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(clinicMember   + '&HosId='+hosId+'&stime=' + stime + '&deptCode=' + deptCode + '&doctorCode=' + doctorCode + '&flag='+toFlag);// + '&t=' + new Date().getTime();
    } else {
        location.href = Commonjs.goToUrl(clinicMember   + '?HosId='+hosId+'&stime=' + stime + '&deptCode=' + deptCode + '&doctorCode=' + doctorCode + '&flag='+toFlag);// + '&t=' + new Date().getTime();
    }
}

//查询预约规则的执行日期
function queryYYRule() {
	var day; 
	var apiData = {};
	apiData.Param = hosId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData,"","");
	Commonjs.ajax('../../wsgw/yy/QueryYYRule/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	var merberList = "";
                    if (jsons.Data[0] != null) {
                    	if(jsons.Data[0].StartDay){
                    		day=Number(jsons.Data[0].StartDay) + 1;
                    	}else{
                    		day = 15;
                    	}
                    }
                }
            } 
        }
    },{async:false});
	return day;
}
