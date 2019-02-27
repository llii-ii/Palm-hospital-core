var PCID = "";
var memberId = null;
var flag=Commonjs.getUrlParam("flag");
var deptCode=Commonjs.getUrlParam("deptCode");
var doctorCode=Commonjs.getUrlParam("doctorCode");
var hosId=Commonjs.getUrlParam("HosId");
var scheduleUrl = "/business/yygh/yyghScheduleList.html";
var callBackUrl;
var stime=Commonjs.getUrlParam("stime");
var isCheckPorvingCode=true;//是否加上验证吗校验用户
$(function(){
	
	iniWidget();
	var memberName = unescape(Commonjs.getUrlParam ('memberName'));
	callBackUrl =Commonjs.getUrlParam("callBackUrl");
	memberId = Commonjs.getUrlParam ('memberId');
	$("#memberName").html(memberName);
	var diyConfig = Commonjs.getDiyConfig();
	if(diyConfig  && diyConfig.checkPorvingCode == false){
		isCheckPorvingCode = false;
		$("#provingCode_li").hide();
	}
});

/**
 * 初始化空间
 */
function iniWidget(){
	//获取验证码
	$('.btn-yzm').on('click',function(){
		var disable = $(this).hasClass('disable');
		if(!disable){
			var mobile = $("#mobile").val();
			if (!checkFormJs.chinaMobile(mobile) && !checkFormJs.ChinaTelecomNew(mobile) && 
					 !checkFormJs.chinaUnicom(mobile)) {
				myLayer.alert('请输入正确手机号码！',3000);
				return;
			} 
			$(this).addClass('disable');
			settime();
			var param = {};
			var apiData = {};
			apiData.Mobile =mobile;
			param.apiParam =Commonjs.getApiReqParams(apiData);
			Commonjs.ajax('../../wsgw/basic/GetProvingCode/callApi.do?time=' + new Date().getTime(),param,function(dd){
				if(dd.RespCode==10000 && !Commonjs.isEmpty(dd.Data)){
					PCID=dd.Data[0].PCId;
				}else{
					myLayer.alert("获取验证码信息异常");
				}
			});
		}
	});
	
	//绑定就诊卡
	$('#bindbtn').on('click',function(){
		if( Commonjs.isEmpty($("#mobile").val())){			
			myLayer.tip({
				con:'请输入手机号'
			});
			return;
		}else if (!checkFormJs.chinaMobile($("#mobile").val()) && !checkFormJs.ChinaTelecomNew($("#mobile").val()) && 
				 !checkFormJs.chinaUnicom($("#mobile").val())) {
			myLayer.tip({
				con:'请输入正确手机号码！'
			});
			return;
		}
		
		var provingCode = $("#provingCode").val();
		
		//验证码验证
		if(isCheckPorvingCode && Commonjs.isEmpty(PCID)){
			myLayer.tip({
				con:'请先获取验证码'
			});
			return;
		}
		
		if(isCheckPorvingCode && Commonjs.isEmpty(provingCode)){
			myLayer.tip({
				con:'请输入验证码'
			});
			return;
		}
		if(  Commonjs.isEmpty($("#cardNo").val())){
			myLayer.tip({
				con:'请输入就诊卡'
			});
			return;
		}
		var isVirtualCard=appConfig.memberConfig.isVirtualCard;//是否支持新增虚拟卡
		var apiData = {};
		apiData.MemberId=memberId;
		apiData.CardNo =  $("#cardNo").val();
		apiData.CardType =  1;//就诊卡
		apiData.CardTypeName =  '就诊卡';
		apiData.PCId=PCID;
		apiData.ProvingCode=$("#provingCode").val();
		apiData.Mobile=$("#mobile").val();
		apiData.IsVirtualCard=isVirtualCard;
		var param = {};
		param.api = 'BindClinicCard'; 
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('../../wsgw/basic/BindClinicCard/callApi.do',param,function(resp){
			if(!Commonjs.isEmpty(resp.RespCode)){
			if( resp.RespCode == 10000 ){
				myLayer.alert('绑定成功！',1500,function(){
					if(!Commonjs.isEmpty(callBackUrl)){
						window.location.href=Commonjs.goToUrl(decodeURIComponent(callBackUrl));
					}else{
						if(flag=='addc'){
							goToSchedule(doctorCode,deptCode,scheduleUrl,stime);
						}else{
							window.location.href=Commonjs.goToUrl('/business/member/memberList.html');
						}
					}
					
				});
			}else if(resp.RespCode == -100021){
				myLayer.tip({
					con:resp.RespMessage
				});
			}
			else{
				myLayer.tip({
					//con:'身份证号已被他人绑定<br />请至医院服务窗口解除绑定';
					con:resp.RespMessage
			
				});
			}
			}
		
	    },{async:false});
//		myLayer.tip({
//			con:'就诊卡与手机号不匹配',
//		});
	});
}
function goToSchedule(doctorCodeParam,deptCodeParam,scheduleUrl,stime) {
	myLayer.load('正在加载');
    if (scheduleUrl.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(scheduleUrl + "&HosId="+hosId+"&doctorCode="  + doctorNameParam + "&deptCode=" + deptCodeParam + "&stime="+stime);
    } else {
        location.href = Commonjs.goToUrl(scheduleUrl + "?HosId="+hosId+"&doctorCode="  + doctorCodeParam + "&deptCode=" + deptCodeParam + "&stime="+stime);
    }
}

var countdown=60; 
function settime() { 
	if (countdown == 0) { 
		$('.btn-yzm').html('重新获取').removeClass('disable'); 
		countdown = 60;
		return;
	} else {
		$('.btn-yzm').html(countdown + 's后重试'); 
		countdown--; 
	} 
	setTimeout(function() { 
		settime();
	},1000); 
}