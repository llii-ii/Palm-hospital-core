var PCID="";
var flag=Commonjs.getUrlParam("flag");
var deptCode=Commonjs.getUrlParam("deptCode");
var doctorCode=Commonjs.getUrlParam("doctorCode");
var stime=Commonjs.getUrlParam("stime");
var hosId=Commonjs.getUrlParam("HosId");
var memberId="";
var scheduleUrl = "/business/yygh/yyghScheduleList.html";//跳转的页面地址
var callBackUrl;
var isCheckPorvingCode=true;//是否加上验证吗校验用户
$(function(){
	//成功回调地址
	callBackUrl = Commonjs.getUrlParam("callBackUrl");
	showGuardian(false);
	initWidget();
	var orgName = Commonjs.getSession().orgName;
	var diyConfig = Commonjs.getDiyConfig();
	if(diyConfig  && diyConfig.checkPorvingCode == false){
		isCheckPorvingCode = false;
		$("#provingCode_li").hide();
	}
	$("#orgName").html(orgName);
});
/**
 * 是否显示监护人信息
 * @param isShow
 * @returns
 */
function showGuardian(isShow){
	if(isShow){
		$("#guardian_li_name").show();
		$("#guardian_li_certtype").show();
		$("#guardian_li_certnum").show();
	}else{
		$("#guardian_li_name").hide();
		$("#guardian_li_certtype").hide();
		$("#guardian_li_certnum").hide();
	}
}

/**
 * 初始化控件，事件等
 */
function initWidget(){
	//证件类型初始化
	$("#certType").picker({
		title: "请选择证件类型",
		cols: [{
				textAlign: 'center',
				values: ['01','02','03','04','08','23','09','24','11','12','99'],
				displayValues:['居民身份证','居民户口簿','护照','军官证','士兵证','社保卡','返乡证','暂住证','港澳通行证','台湾通行证','其他']
			}],
		onChange: function($k, values, displayValues) {
			$("#certType").val(values);
			$('#certTypeName').html(displayValues);
		},
		onClose:function($k){
			
		}
	});
	//监护人证件类型初始化
	$("#guardianCertType").picker({
		title: "请选择证件类型",
		cols: [{
				textAlign: 'center',
				values: ['01','02','03','04','08','23','09','24','11','12','99'],
				displayValues:['居民身份证','居民户口簿','护照','军官证','士兵证','社保卡','返乡证','暂住证','港澳通行证','台湾通行证','其他']
			}],
		onChange: function($k, values, displayValues) {
			$("#guardianCertType").val(values);
			$('#guardianCertTypeName').html(displayValues);
		},
		onClose:function($k){
			
		}
	});
	//是否儿童
	$("#isChildren").picker({
		title: "请选择证件类型",
		cols: [{
			textAlign: 'center',
			values: ['0','1'],
			displayValues:['否','是']
		}],
		onChange: function($k, values, displayValues) {
			$("#isChildren").val(values);
			$('#isChildrenName').html(displayValues);
		},
		onClose:function($k){
			if($k.value == '1'){
				showGuardian(true);
			}else{
				showGuardian(false);
			}
		}
	});
//	//选择是否一起绑定就诊卡
//	$("#picker-jzk").picker({
//		title: "选择是否拥有就诊卡",
//		cols: [
//			{
//				textAlign: 'center',
//				values: ['有','无']
//			},
//		],
//		onClose:function($k){
//			if($k.value == '无'){
//				$('#cardNum').hide().find('input').val('');
//			}else{
//				$('#cardNum').show();
//			}
//		}
//	});
	
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
			var apiData={};
			apiData.Mobile =mobile;
			param.apiParam = Commonjs.getApiReqParams(apiData);
			Commonjs.ajax('../../wsgw/basic/GetProvingCode/callApi.do?time=' + new Date().getTime(),param,function(dd){
				if(dd.RespCode!=10000 || Commonjs.isEmpty(dd.Data)){
					myLayer.alert('获取验证码信息异常',3000);
					return;
				}
				PCID=dd.Data[0].PCId;
			});
		}
	});
	
	//提交按钮
	$('#bindbtn').on('click',function(){
		var memberName = $("#memberName").val();
		var certType =  $("#certType").val();
		var certNum =  $("#certNum").val();
		var mobile =  $("#mobile").val();
		var isChildren = $("#isChildren").val();
		var guardianName = $("#guardianName").val();
		var guardianCertType = $("#guardianCertType").val();
		var guardianCertNum = $("#guardianCertNum").val();
		var cardNo = $("#cardNo").val();
		var hospitalNo = $("#hospitalNo").val();
		var provingCode = $("#provingCode").val();
		var idCardNo = '';
		
		//姓名不能为空
		if(Commonjs.isEmpty(memberName) ){
			myLayer.tip({
				con:'请输入就诊人姓名'
			});
			return;
		}
		//患者为儿童时，患者证件号码 或者 监护人的姓名及监护人证件号码   二者必填其中一项
		if(isChildren==1){
			if(Commonjs.isEmpty(certNum) && (Commonjs.isEmpty(guardianName) 
					|| Commonjs.isEmpty(guardianCertNum))){
				myLayer.tip({
					con:'患者为儿童时，必须输入患者的证件号码或监护人姓名及监护人证件号码'
				});
				return;
			}
			
		}else{
			if(Commonjs.isEmpty(certNum)){
				myLayer.tip({
					con:'请输入证件号码'
				});
				return;
			}
		}
		//身份证号码格式验证
		if(certType=='01' && !Commonjs.isEmpty(certNum)){
			if(!checkFormJs.isIdCardNo(certNum)){
				myLayer.tip({
					con:'请输入正确的身份证号码'
				});
				return;
			}
		}else if(guardianCertType=='01' && !Commonjs.isEmpty(guardianCertNum)){
			if(!checkFormJs.isIdCardNo(guardianCertNum)){
				myLayer.tip({
					con:'请输入正确的身份证号码'
				});
				return;
			}
		}
		
		//证件号码为身份证时，赋值给idCardNo
		if(certType=='01' && !Commonjs.isEmpty(certNum)){
			idCardNo = certNum;
		}else if(isChildren==1 && guardianCertType=='01' && !Commonjs.isEmpty(guardianCertNum)){
			//用户为儿童并且输入的是监护人的身份证时，将监护人身份证号码赋值给idCardNo
			idCardNo = guardianCertNum;
		}
		//手机号码验证
		if(Commonjs.isEmpty(mobile)){
			myLayer.tip({
				con:'请输入手机号'
			});
			return;
		}else if (!checkFormJs.chinaMobile(mobile) && !checkFormJs.ChinaTelecomNew(mobile) && 
				 !checkFormJs.chinaUnicom(mobile)) {
			myLayer.tip({
				con:'请输入正确手机号码！'
			});
			return;
		}
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
//		if( $("#picker-jzk").val()=='有' && Commonjs.isEmpty($("#cardNo").val())){
//			myLayer.tip({
//				con:'请输入就诊卡'
//			});
//			return;
//		}
		if( $("#isReadProtocol").attr('checked')!='checked' ){		
			myLayer.tip({
				con:'请阅读协议'
			});
			return;
		}
		
		var isCheckHis=appConfig.memberConfig.isCheckHis;//是否校验HIS
		var isVirtualCard=appConfig.memberConfig.isVirtualCard;//是否支持新增虚拟卡
		var apiData = {};
		apiData.HosId = Commonjs.getUrlParam("HosId");
		apiData.MemberName = memberName;
		apiData.IdCardNo =  idCardNo;
		apiData.Mobile =  mobile;
		apiData.CertType = certType;
		apiData.CertNum = certNum;
		apiData.IsChildren = isChildren;
		apiData.GuardianName = guardianName;
		apiData.GuardianCertType = guardianCertType;
		apiData.GuardianCertNum = guardianCertNum;
		apiData.CardNo = cardNo;
		apiData.CardType =  1;
		apiData.CardTypeName =  '就诊卡';
		apiData.HospitalNo = hospitalNo;
		apiData.PCId = PCID;
		apiData.ProvingCode = provingCode;
		var param = {};
		var url;
		if(isCheckHis==0){
			url = '../../wsgw/basic/AddMember/callApi.do';
		}else if(isCheckHis==1){
			url = '../../wsgw/basic/AddMemberWithValidate/callApi.do';
		}
		param.apiParam = Commonjs.getApiReqParams(apiData);
		
		Commonjs.ajax(url,param,function(resp){
			if(!Commonjs.isEmpty(resp.RespCode)){			
			if( resp.RespCode == 10000){
				myLayer.alert('新增就诊人成功！',1500,function(){
					if(!Commonjs.isEmpty(callBackUrl)){
						window.location.href=Commonjs.goToUrl(decodeURIComponent(callBackUrl));
					}else{
						if(flag=="addm"){
							goToSchedule(doctorCode,deptCode,scheduleUrl,stime);
						}else{
							window.location.href=Commonjs.goToUrl('/business/member/memberList.html');
						}
					}
				});		
			}
			else if( resp.RespCode == -100021){
				myLayer.alert('新增就诊人成功,未查到卡信息,不支持新增虚拟卡！',1500,function(){	
					window.location.href=Commonjs.goToUrl('/business/member/memberList.html');
				});
			}
			else if( resp.RespCode == -100019||resp.RespCode == -100020){
				myLayer.alert('不支持新增虚拟卡',1500,function(){	
					window.location.href=Commonjs.goToUrl('/business/member/memberList.html');
				});
			}
			else if( resp.RespCode == -100016 ){
				var retError = JSON.parse(resp.RespMessage);
				memberId = retError.memberId;
				var mobile = retError.mobile;
				//设置手机号  
				$("#mobile").val(mobile);
				//原来的位置设置成不可用
				$('#mobile').attr("disabled","disabled");
				//发送验证码到旧手机号
				myLayer.tip({
					//con:'身份证号已被他人绑定<br />请至医院服务窗口解除绑定';
					con:retError.msg
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
	});
		
}

//跳转到页面函数
function goToSchedule(doctorCodeParam,deptCodeParam,scheduleUrl,stime) {
	myLayer.load('正在加载');
    if (scheduleUrl.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(scheduleUrl + "&HosId="+hosId+"&doctorCode="  + doctorNameParam + "&deptCode=" + deptCodeParam + "&stime="+stime);
    } else {
        location.href = Commonjs.goToUrl(scheduleUrl + "?HosId="+hosId+"&doctorCode="  + doctorCodeParam + "&deptCode=" + deptCodeParam + "&stime="+stime);
    }
}




//协议勾选
function agreePop(){
	$('.agreebox').css('max-height',$(document).height() - 180);
	var ghtml = $('#agreeCon').html();
	myLayer.confirm({
        con:ghtml,
        ok: function(){}
    });
	$('.confcontent').css('padding','.25rem 1rem .75rem 1rem');
}

//验证码重新获取倒计时
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