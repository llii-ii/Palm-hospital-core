var PCID = "";
var isCheckPorvingCode=true;//是否加上验证吗校验用户
var callBackUrl;

$(function(){
	
	initWidget();
	var memberName = unescape(Commonjs.getUrlParam ('memberName'));
	memberId = Commonjs.getUrlParam ('memberId');
	callBackUrl = Commonjs.getUrlParam("callBackUrl");
	$("#memberName").html(memberName);
	var diyConfig = Commonjs.getDiyConfig();
	if(diyConfig  && diyConfig.checkPorvingCode == false){
		isCheckPorvingCode = false;
		$("#provingCode_li").hide();
	}
	
});

function initWidget(){
	
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
			param.api = ''; 
			param.apiParam = Commonjs.getApiReqParams(apiData);
			Commonjs.ajax('../../wsgw/basic/GetProvingCode/callApi.do?time=' + new Date().getTime(),param,function(dd){
				if(dd.RespCode==10000 && !Commonjs.isEmpty(dd.Data)){
					PCID=dd.Data[0].PCId;
				}else{
					myLayer.alert("获取验证码信息异常");
				}
			})
		}
	});
	
	
	//绑定
	$('#bindbtn').on('click',function(){
		if(  Commonjs.isEmpty($("#hospitalNo").val())){
			myLayer.tip({
				con:'请输入住院号'
			});
			return;
		}
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
		
		
		var apiData = {};
		apiData.MemberId=memberId;
		apiData.HospitalNo =  $("#hospitalNo").val();
		apiData.Mobile=$("#mobile").val();
		apiData.PCId=PCID;
		apiData.ProvingCode=$("#provingCode").val();
		var param = {};
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('../../wsgw/basic/BindHospitalNo/callApi.do',param,function(resp){
			if(!Commonjs.isEmpty(resp.RespCode)){
				if( resp.RespCode == 10000 ){
					myLayer.alert('绑定成功！',1500,function(){
						if(!Commonjs.isEmpty(callBackUrl)){
							window.location.href=Commonjs.goToUrl(decodeURIComponent(callBackUrl));
						}else{
							window.location.href=Commonjs.goToUrl('/business/member/memberList.html');
						}
					});
				}else{
					myLayer.tip({
						//con:'身份证号已被他人绑定<br />请至医院服务窗口解除绑定';
						con:resp.RespMessage
					});
				}
			}
			else{
				myLayer.alert("返回码为空");
			}
	    });
//		myLayer.tip({
//			con:'住院号与手机号不匹配',
//		});
	});
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