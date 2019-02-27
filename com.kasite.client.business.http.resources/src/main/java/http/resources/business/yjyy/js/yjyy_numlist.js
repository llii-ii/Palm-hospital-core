
var patientType;
var patientNo;
var labCode;
var departCode;
var LabName;
var ExamDept;
var HisKey="";
var mid='';
$(function() {
    patientType = Commonjs.getUrlParam("patientType");
    patientNo = Commonjs.getUrlParam("patientNo");
    mid = Commonjs.getUrlParam("mid");
    labCode = unescape(Commonjs.getUrlParam("labCode"));    
    departCode=Commonjs.getUrlParam("departCode");    
    HisKey=Commonjs.getUrlParam("HisKey"); 
    LabName=Commonjs.getUrlParam("LabName"); 
    ExamDept=Commonjs.getUrlParam("ExamDept"); 
    ShowNumList();
    
});
function gotoUrl(url){
	location.href = Commonjs.goToUrl(url)
}
// 显示排班列表
function ShowNumList(){
	var param = {};
	var apiData = {};
	apiData.CardType=patientType;
	apiData.CardNo=patientNo;
	apiData.LabCode=labCode;
	apiData.MemberId = mid;
	apiData.DepartCode=departCode;
	apiData.StartDate=getBeforeDate(0);
	apiData.EndDate=getBeforeDate(7);
	param.api = '';
	param.apiParam = Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/yy/QueryScheduleDate/callApi.do';
	Commonjs.ajax(url, param, function(d) {
		 if(d.RespCode==10000){
			 var listheml='';
			 var numdata='';
			 Commonjs.BaseForeach(d.Data, function(i, item) {
				
				 if(item.IsBook==1){
         			var url = 'yjyy_book.html?patientType='+patientType+'&mid='+mid+'&patientNo='+patientNo+'&labCode='+labCode+'&departCode='+departCode+'&LabName='+LabName+'&HisKey='+HisKey+"&ExamDept="+ExamDept;
         				numdata += '<li>';
         				numdata += '<span class="ipweek">'+item.BookDate+' <br/>'+item.WeekDay+'</span>';
         				numdata += '<span class="ipcost" style=""><i class="c909090">&nbsp</i> <i class="cff8100"></i></span>';
							
								numdata += '<span class="ipbtn"><a onclick="gotoUrl(\'' + url+'&BookDate='+item.BookDate+ '\')">马上预约</a></span>';
							
         				
         				numdata += '</li>';
         		 }
         	 });
         	 $("#list ul").html(numdata);
		 }
		 });
	
}
function getWeak(dateStr){
	var time = new Date(dateStr);
    var week = time.getDay();
    switch (week) {
        case 0:
        	week="周日";
            break;
        case 1:
        	week="周一 ";
            break;
        case 2:
        	week="周二";
            break;
        case 3:
        	week="周三";
            break;
        case 4:
        	week="周四";
            break;
        case 5:
        	week="周五";
            break;
        case 6:
        	week="周六";
            break;
    }
    return week;
}

function getBeforeDate(n) {
	var n = n;
	var d = new Date();
	var year = d.getFullYear();
	var mon = d.getMonth() + 1;
	var day = d.getDate();
	if (day <= n) {
		if (mon > 1) {
			mon = mon - 1;
		} else {
			year = year - 1;
			mon = 12;
		}
	}
	d.setDate(d.getDate() + n);
	year = d.getFullYear();
	mon = d.getMonth() + 1;
	day = d.getDate();
	s = year + "-" + (mon < 10 ? ('0' + mon) : mon) + "-"
			+ (day < 10 ? ('0' + day) : day);
	return s;
}