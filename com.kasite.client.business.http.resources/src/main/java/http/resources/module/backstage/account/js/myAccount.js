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
		if(!checkTel(newPhone)){
			Commonjs.alert("请输入正确的手机号码");
			document.getElementById("newPhone").focus();
			return;
		}
		var _self = $(this);
		var param = {};
		param.reqParam = Commonjs.getReqParams({'Id':$("#userId").val(),'Mobile':newPhone});
		Commonjs.ajax("/sys/updateUser.do",param,function(d){
			if(d.RespCode=10000){
				//修改Session
				$("#phone").html(newPhone);
				_self.parent().hide();
				$('.phone-text').show();
			}else{
				//异常提示
				Commonjs.alert(d.respMessage);
			}
		});
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
				return updatePassword(artBox);	
			},
			cancel: true
		});	
	})
	loadUserInfo()
})

//初始化页面信息
function loadUserInfo(){
	var param = {};
	var userName = Commonjs.getUserName();
	param.reqParam = Commonjs.getReqParams({'Username':userName});
	Commonjs.ajax('/sys/getUserInfo.do',param,function(d){
		if(d.RespCode==10000){
			console.log();
			$("#userId").attr("value",d.Data[0].Id);
			$("#password").attr("value","");
			$("#userName").html(d.Data[0].Username);
			$("#name").html(d.Data[0].RealName);
			$("#phone").html(d.Data[0].Mobile);
		}else{
			Commonjs.alert(d.RespMessage);
		}
	})
}

//修改密码
function updatePassword(artBox){
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
	}
	var userName = Commonjs.getUserName();
	var param = {};
	param.reqParam = Commonjs.getReqParams({'Username':userName,'OldPassword':$.md5(currentPassword).toUpperCase(),'NewPassword':$.md5(newPassword).toUpperCase()});
	Commonjs.ajax("/sys/updateUserPassword.do",param,function(d){
		if(d.RespCode==10000){
			//清空所填写信息
			$("#currentPassword").val("");
			$("#newPassword").val("");
			$("#newPassword1").val("");
			artBox.close();
		}else{
			//异常提示
			Commonjs.alert(d.RespMessage);
		}
	});
	return false;
}

//手机号码验证
function checkTel(value){   
	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;   
	//var isMob=/^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[012356789][0-9]{8}|18[012356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;   
	var isMob=/^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[0123456789][0-9]{8}|18[0123456789][0-9]{8}|147[0-9]{8})$/;
	if(isMob.test(value)||isPhone.test(value)){  
		return true;   
	}else{
		return false;
	}   
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