var patientType;
var patientNo;
var labCode;
var departCode;
var LabName;
var ExamDept;
var BookDate;
var LabName;
var HisKey;
var sourceCode="";
var mid="";
$(function () {
	 patientType = Commonjs.getUrlParam("patientType");
	 patientNo = Commonjs.getUrlParam("patientNo");
	 mid = Commonjs.getUrlParam("mid");
	 labCode = unescape(Commonjs.getUrlParam("labCode"));    
	 departCode=Commonjs.getUrlParam("departCode");   
	 BookDate=Commonjs.getUrlParam("BookDate"); 
	 HisKey=Commonjs.getUrlParam("HisKey"); 
	 ExamDept=Commonjs.getUrlParam("ExamDept"); 
	 LabName=Commonjs.getUrlParam("LabName"); 
	 LabName = unescape(LabName);
     info();

});
var sorChoo;
function loaded() {
    sorChoo = new iScroll('sorChoo');
}
document.addEventListener('DOMContentLoaded', loaded, false);

//选择号源
function sorShow() {
    var totH = $(window).height();
    $("body").height(totH).css("overflow", "hidden");
    $(".pop_screen").show();
    $(".dmtpop").show();
    $("#sorChoo").css("max-height", totH / 2 - 38);
    sorChoo.refresh();
}
//取消
function modclose() {
    $(".pop_screen").hide();
    $(".dmtpop").hide();
    $("body").height("auto").css("overflow", "auto");
}
//选择号源
function selectnum() {
		var param = {};
		var apiData = {};
		apiData.CardType=patientType;
		apiData.CardNo=patientNo;
		apiData.LabCode=labCode;
		apiData.MemberId = mid;
		apiData.DepartCode=departCode;
		apiData.BookDate=BookDate;
		apiData.ExamDept=ExamDept;
		param.api = '';
		param.apiParam = Commonjs.getApiReqParams(apiData);
		var url = '../../wsgw/yy/QuerySignalSourceList/callApi.do';
		Commonjs.ajax(url, param, function(d) {
		 if (d.RespCode == 10000) {
			 var time='';
			 var listHtml='';
			 Commonjs.BaseForeach(d.Data, function(i, item) {
                 if (item != "") {
                 	if(item.Time==time){
                 		time = item.Time;
                 	}
                 	else{
                 		time = item.Time;
                 		listHtml += '<li sourceCode="' + item.SourceId + '" SqNo="' + item.Sequence + '">' + item.Time +'</li>';
                         
                 	}
                 }
             });
             $("#numlist").append(listHtml);
             sorShow();
             num();
        } else {
            alert(jsons.RespMessage);
        }
		})
}
//页面信息
function info() {
    var listHtml = '';
    listHtml += '<li class="boxs bor_b"><div class="ptit">项目</div><span class="c909090">' + LabName + '</span></li>';
    listHtml += '<li class="boxs bor_b"><div class="ptit">预约日期</div><span class="c909090">' + BookDate + '</span></li>';
    listHtml += '<li class="boxs bor_b"><div class="ptit">卡号</div><span class="c909090">' + patientNo + '</span></li>';
    $("#infoid ul").append(listHtml);
}

//挂号操作
function BookService() {
	if(sourceCode==''){
		alertNew('请选择号源');
		return;
	}
	var param = {};
	var apiData = {};
	apiData.CardType=patientType;
	apiData.CardNo=patientNo;
	apiData.HisKey=HisKey;
	apiData.MemberId = mid;
	apiData.SourceCode = sourceCode;
	apiData.LabName = escape(LabName);
	param.api = '';
	param.apiParam = Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/yy/MedicalAppoint/callApi.do';
	Commonjs.ajax(url, param, function(d) {
		 if (d.RespCode == 10000) {
         	alert("预约成功");
         	location.href = Commonjs.goToUrl('bookinfo.html?HisKey='+HisKey+'&LabName='+escape(LabName)+'&CardType='+patientType);
         }
         else {
             alert(d.RespMessage+"预约失败");
         }
	})
	
}

//号源选择赋值页面
function num() {
    $.each($('#numlist li'), function (element, index) {
        var $this = $(this);
        $this.bind('click', function () {
        	var data = $this.html();
        	$("#sapnNum").html(data);
            sourceCode = $this.attr("sourceCode");
            $("#default ul li").eq(1).attr("sourceCode", sourceCode);
            modclose();
        });
    });
}
 