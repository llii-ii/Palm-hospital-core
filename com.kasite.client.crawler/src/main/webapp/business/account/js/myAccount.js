$(function(){
	$('.edit-phone').on('click',function(){
		$(this).parent().hide();
		$('.phone-form').show();
	});
	$('.edit-btn-cell').on('click',function(){
		$(this).parent().hide();
		$('.phone-text').show();	
	});
	$('.edit-btn-confirm').on('click',function(){
		var newPhone = $("#newPhone").val();
		updatePhone(newPhone);
		$(this).parent().hide();
		$('.phone-text').show();	
	});
	$('.password-text').on('click',function(){
		var contents=$('.password-box').get(0);
		var artBox=art.dialog({
			lock: true,
			artIcon:'add',
			opacity:0.4,
			width: 400,
			padding:'0px 0px',
			title:'修改密码',
			content: contents,
			ok: function () {
				return updatePassword();	
			},
			cancel: true
		});	
	})
	loadUserInfo()
})

//初始化页面信息
function loadUserInfo(){
	var param = {};
	var d = Commonjs.getSession("../../login/getSession.do");
	$("#userId").attr("value",d.UserID);
	$("#password").attr("value","");
	$("#userName").html(d.UserName);
	$("#name").html(d.Name);
	$("#phone").html(d.Phone);
}

//修改用户手机号
function updatePhone(newPhone){
	var Service = {};
	var code = 2005;
	Service.UserId=$("#userId").val();
	Service.Phone = newPhone;
	//param.Api = "SaveOrUpdateUser";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveOrUpdateUser.do",param);
	if(d.respCode=10000){
		//修改Session
		$("#phone").html(newPhone);
		var d = Commonjs.getSession("../../login/getSession.do");
		d.Phone = newPhone;
		Commonjs.updateSession(d,"../../login/updateSession.do");
	}else{
		//异常提示
		Commonjs.alert(d.respMessage);
	}
}

function updatePassword(){
	var password = $("#password").val();
	var currentPassword = $("#currentPassword").val();
	var newPassword = $("#newPassword").val();
	var newPassword1 = $("#newPassword1").val();
	//对密码进行验证
	if(currentPassword==undefined || currentPassword==''){
		Commonjs.alert('当前密码不能为空');
		document.getElementById("currentPassword").focus();
		return false;
	}else if(newPassword==undefined || newPassword==''){
		Commonjs.alert('新密码不能为空');
		document.getElementById("newPassword").focus();
		return false;
	}else if(newPassword1==undefined || newPassword1=='' ){
		Commonjs.alert('新密码不能为空');
		document.getElementById("newPassword1").focus();
		return false;
	}else if(newPassword!=newPassword1){
		Commonjs.alert('两次密码输入不一致，请重新输入');
		document.getElementById("newPassword").focus();
		return false;
	}/*else if(password!=currentPassword){
		Commonjs.alert('密码输入错误，请重新输入！');
		document.getElementById("currentPassword").focus();
		return false;
	}*/
	//校验用户密码对错
	var Service = {};
	var page = {};
	var code = 2006;
	Service.UserName = $("#userName").val();
	Service.PassWord = $.md5(currentPassword).toUpperCase();
	var param = {};
	var params = Commonjs.getParams(code,Service);//获取参数
	param.Api = "doLogin";
	param.Params = Commonjs.jsonToString(params);
	var d = Commonjs.ajax('../../login/doLogin.do',param,false);
	console.log(d);
	if(d.RespCode != 10000){
		Commonjs.alert('密码输入错误，请重新输入！');
		document.getElementById("currentPassword").focus();
		return false;	
	}
	//修改用户密码
	var Service = {};
	var code = 2005;
	Service.UserId=$("#userId").val();
	Service.Password = $.md5(newPassword).toUpperCase();
	//param.Api = "SaveOrUpdateUser";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveOrUpdateUser.do",param);
	if(d.respCode=10000){
		//更新Session
		var d = Commonjs.getSession("../../login/getSession.do");
		d.Password = $.md5(newPassword).toUpperCase();
		Commonjs.updateSession(d,"../../login/updateSession.do");
	}else{
		//异常消息提示
		Commonjs.alert(d.respMessage);
	}
	//清空所填写信息
	$("#currentPassword").val("");
	$("#newPassword").val("");
	$("#newPassword1").val("");
	return true;
}
function myPrint(msg){
	art.dialog({
		lock : true,
		artIcon : 'error',
		opacity : 0.4,
		width : 250,
		title : '提示',
		time : 3,
		content : msg,
		ok : function() {

		}
	});			
}