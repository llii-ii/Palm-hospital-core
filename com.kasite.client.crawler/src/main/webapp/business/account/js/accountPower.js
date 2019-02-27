var userInfoList = undefined;
var roleInfoList = undefined;
var menuInfoList = undefined;
var roleMsg1 = undefined;
var roleMsg2 = undefined;
$(function(){
	$('#menuDiv').height(250).jScrollPane({"autoReinitialise": true,"overflow":true});
	queryAllRole();
	var str2 = getSelectOfficeBoxHtml(2);
	roleMsg2 = str2;
	var str1 = getSelectOfficeBoxHtml(1);
	roleMsg1 = str1;
	if(!roleInfoList.length){
		$("#roleId2").attr("value",roleInfoList.RoleId);
		$("#selectOffice2").html(roleInfoList.RoleName);
		$("#roleId1").attr("value",roleInfoList.RoleId);
		$("#selectOffice1").html(roleInfoList.RoleName);
	}else{
		$("#roleId2").attr("value",roleInfoList[0].RoleId);
		$("#selectOffice2").html(roleInfoList[0].RoleName);
		$("#roleId1").attr("value",roleInfoList[0].RoleId);
		$("#selectOffice1").html(roleInfoList[0].RoleName);
	}
	var menuStr = getMenuTbHtml();
	$("#menuTb").html(menuStr);
	loadTb(1);
	$('#resetPassword').click(function(){
		if($('#resetPassword').attr("class")=="my-switch-box"){
			$('#resetTr').show();
		}else{
			$('#resetTr').hide();
		}
    })
})
function showorhide(obj){
	var a = $(obj).next('div');
	var id = a.attr('id');
	var idx = id.substring(id.length-1,id.length);
	if(a.css('display')=="none"){
		a.show();
		var api = a.jScrollPane({"autoReinitialise": true}).data('jsp');
		if(idx==1){
			api.getContentPane().html(roleMsg1);
		}else{
			api.getContentPane().html(roleMsg2);
		}
		api.reinitialise();
	}else{
		a.hide();
	}
}
function addRole(index){
	$("#roleName").val("");
	$("#roleDesc").val("");
	$("#roleMark").val("");
	$("#roleId").attr("value",null);
	//加载菜单选项
	var contents=$('#newRol').get(0);
	var artBox=art.dialog({
		lock: true,
		artIcon:'add',
		opacity:0.4,
		width: 600,
		padding:'0px 0px',
		title:'新增职务',
		content: contents,
		ok: function () {
			var roleName = $("#roleName").val();
			var roleDesc = $("#roleDesc").val();
			var roleMark = $("#roleMark").val();
			if(!roleName||Commonjs.isEmpty(roleName)){
				Commonjs.alert('职务名称不能为空');
				document.getElementById("roleName").focus();
				return false;
			}
			if(roleName.length>30){
				Commonjs.alert('职务名称不能超过30个字符');
				document.getElementById("roleName").focus();
				return false;
			}
			if(!checkRoleByName(roleName)){
				return false;
			}
			if(roleDesc.length>50){
				Commonjs.alert('职务描述不能超过50个字符');
				document.getElementById("roleDesc").focus();
				return false;
			}
			if(roleMark.length>50){
				Commonjs.alert('职务标识不能超过50个字符');
				document.getElementById("roleMark").focus();
				return false;
			}
			var roleId = saveOrUpdateRole(null,roleMark,roleName,roleDesc);
			if(!menuInfoList.length){
				var state = 0;
				var menuList = $("input[name='"+menuInfoList.Key+"']");
				for(var i=0 ;i<menuList.length;i++){
					menu = menuList[i];
					if(menu.checked){
						state +=parseInt(menu.value);
					}
				}
				var menuId = getMenuIdByKey(menuInfoList.Key);
				saveRoleMenu(menuId,roleId,state)
			}else{
				for(var i=0;i<menuInfoList.length;i++){
					var obj = menuInfoList[i];
					var state = 0;
					var menuList = $("input[name='"+obj.Key+"']");
					for(var j=0 ;j<menuList.length;j++){
						menu = menuList[j];
						if(menu.checked){
							state +=parseInt(menu.value);
						}
					}
					var menuId = getMenuIdByKey(obj.Key);
					saveRoleMenu(menuId,roleId,state)
				}
			}
			queryAllRole();
			var str = getSelectOfficeBoxHtml(index);
			if(index==1){
				roleMsg1 =str;
			}else{
				roleMsg2 =str;
			}
			if(!roleInfoList.length){
				$("#roleId"+index).attr("value",roleInfoList.RoleId);
				$("#selectOffice"+index).html(roleInfoList.RoleName);
			}else{
				for(var i=0;i<roleInfoList.length;i++){
					if(roleInfoList[i].RoleName==roleName){
						$("#roleId"+index).attr("value",roleInfoList[i].RoleId);
						$("#selectOffice"+index).html(roleInfoList[i].RoleName);
						break;
					}
				}
			}
			$('#selectOfficeBox'+index).hide();	
		},
		cancel: function(){
			$('#selectOfficeBox'+index).hide();	
		}
	});	
}
function addUser(){
	$("#userName2").val('');
	$("#name2").val('');
	$("#mobile2").val('');
	$("#password2").val('');
	var contents=$('#addUerBox').get(0);
	var artBox=art.dialog({
		lock: true,
		artIcon:'add',
		opacity:0.4,
		width: 400,
		padding:'0px 0px',
		title:'新增用户',
		header:false,
		content: contents,
		ok: function () {
			var userName = $("#userName2").val();
			var name = $("#name2").val();
			var phone = $("#mobile2").val();
			var password = $("#password2").val();
			var roleId = $("#roleId2").val();
			if(!checkUser()) return false;
			var userId = saveOrUpdateUser(null,userName,phone,name,$.md5(password).toUpperCase());
			saveRoleUser(userId,roleId);
			loadTb(1);
			$('#selectOfficeBox2').hide();
		},
		cancel: function(){
			$('#selectOfficeBox2').hide();
		}
	});
}
function loadTb(index){
	var Service = {};
	$('#pagenumber').val(index);
	var pageIndex = index-1;
	var pageSize = 10;
	Service.HosId = Commonjs.hospitalId;
	var code = 2005;
	var param = Commonjs.makeParam("",code,Service,pageIndex,pageSize);
	var d =Commonjs.ajaxJson("../../account/querUserList.do",param);
	if(d.respCode=10000){
		if(d.page.pCount!=undefined){
			if(d.page.pCount!=0){
				$("#totalcount").val(d.page.pCount);
			}else{
				if(d.page.pIndex==0)$("#totalcount").val(0);
			}
		}else{
			$("#totalcount").val(0);
		}
		PageModel($("#totalcount").val(),d.page.pSize,'pager');
		userInfoList = d.data;
		var str = "<tr><th>用户账号</th><th>实名</th><th>职务</th><th>电话号码</th><th class=\"last\">操作</th></tr>";
		if(!userInfoList.length){
			str += "<tr>";
			str +="<td>"+userInfoList.UserName+"</td>";
			str +="<td>"+userInfoList.Name+"</td>";
			str +="<td>"+userInfoList.RoleName+"</td>";
			str +="<td>"+userInfoList.Phone+"</td>";
			str +="<td><ul class=\"i-btn-list\"><li><a href=\"javascript:updateUserInfo('"+userInfoList.UserID+"');\" class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a href=\"javascript:deleteUser('"+userInfoList.UserID+"','"+userInfoList.RoleId+"');\" class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
			//str +="<td><a href=\"javascript:;\" class=\"operate\" onclick='updateUserInfo(\""+userInfoList.UserID+"\")'>编辑</a> <a href=\"javascript:;\" class=\"operate\" onclick='deleteUser(\""+userInfoList.UserID+"\",\""+userInfoList.RoleId+"\")'>删除</a></td>";
			str +="</tr>";
		}else{
			for(var i=0;i<userInfoList.length;i++){
				var obj = userInfoList[i];
				str += "<tr>";
				str +="<td>"+obj.UserName+"</td>";
				str +="<td>"+obj.Name+"</td>";
				str +="<td>"+obj.RoleName+"</td>";
				str +="<td>"+obj.Phone+"</td>";
				str +="<td><ul class=\"i-btn-list\"><li><a href=\"javascript:updateUserInfo('"+obj.UserID+"');\" class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a href=\"javascript:deleteUser('"+obj.UserID+"','"+obj.RoleId+"');\" class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
				//str +="<td><ul class=\"i-btn-list\"><li><a onclick='updateUserInfo(\""+obj.UserID+"\")' class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a onclick='deleteUser(\""+obj.UserID+"\",\""+obj.RoleId+"\")' class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
				//str +="<td><a href=\"javascript:;\" class=\"operate\" onclick='updateUserInfo(\""+obj.UserID+"\")'>编辑</a> <a href=\"javascript:;\" class=\"operate\" onclick='deleteUser(\""+obj.UserID+"\",\""+obj.RoleId+"\")'>删除</a></td>";
				str +="</tr>";
			}
		}
		$("#tb").html(str);
	}else{
		//异常提示
		PageModel(0,pageSize,'pager');
		Commonjs.alert(d.RespMessage);
	}
}
function updateUserInfo(userId){
	$('#resetTr').attr("display","none");
	var d = undefined;
	if(!userInfoList.length){
		d = userInfoList;
	}else{
		for(var i=0;i<userInfoList.length;i++){
			var obj = userInfoList[i];
			var uId = obj.UserID;
			if(uId==userId){
				d = obj;
			}
		}
	}
	$("#userName1").val(d.UserName);
	$("#name1").val(d.Name);
	$("#mobile1").val(d.Phone);
	$("#password1").val("");
	$("#roleId1").attr("value",d.RoleId);
	$("#selectOffice1").html(d.RoleName);
	var contents=$('#updateUerBox').get(0);
	var artBox=art.dialog({
			lock: true,
			artIcon:'add',
			opacity:0.4,
			width: 400,
			padding:'0px 0px',
			title:'修改用户',
			header:false,
			content: contents,
			ok: function () {
				var userName = $("#userName1").val();
				var phone = $("#mobile1").val();
				var name = $("#name1").val();
				var roleId = $("#roleId1").val();
				var updatePasswordState = undefined;
				if($('#resetPassword').attr("class")=="my-switch-box"){
					updatePasswordState=1;//1为要重置密码
				}else{
					updatePasswordState=2;
				}
				var password = $("#password1").val();
				if(Commonjs.isEmpty(userName)){
					Commonjs.alert('用户账号不能为空');
					document.getElementById("userName1").focus();
					return false;
				}
				if(userName.length>30){
					Commonjs.alert('用户账号不能超过30个字符');
					document.getElementById("userName1").focus();
					return false;
				}
				if(name.length>30){
					Commonjs.alert('用户实名不能超过30个字符');
					document.getElementById("name1").focus();
					return false;
				}
				if(!Commonjs.isEmpty(phone)&&!checkTel(phone)){
					Commonjs.alert('请输入正确的电话号码！');
					document.getElementById("mobile1").focus();
					return false;
				}
				if(updatePasswordState==1){
					if(Commonjs.isEmpty(password)){
						Commonjs.alert('登录密码不能为空');
						document.getElementById("password1").focus();
						return false;
					}
					if(password.length>30){
						Commonjs.alert('登录密码不能超过30个字符');
						document.getElementById("password1").focus();
						return false;
					}
				}
				saveOrUpdateUser(userId,userName,phone,name,updatePasswordState==1?$.md5(password).toUpperCase():null);
				updateRoleUser(userId,roleId);
				Commonjs.alert("修改成功！","add");
				loadTb(1);
				$('#selectOfficeBox1').hide();
			},
			cancel: function(){
				$('#selectOfficeBox1').hide();
			}
		});	
	//$('#selectOfficeBox1').hide();	
}

function queryAllRole(){
	var Service = {};
	var pageIndex = 0;
	var pageSize = 100;
	Service.HosId = Commonjs.hospitalId;
	var code = 2005;
	//param.Api = "QueryAllRole";
	var param = Commonjs.makeParam("",code,Service,pageIndex,pageSize);
	var d =Commonjs.ajaxJson("../../account/queryAllRole.do",param);
	if(d.respCode==10000){
		roleInfoList = d.data;
	}
}

function setSelectOffice(index,roleId,roleName){
	$("#roleId"+index).attr("value",roleId);
	$("#selectOffice"+index).html(decodeURI(decodeURI(roleName)));
	$('#selectOfficeBox'+index).hide();
}
function getSelectOfficeBoxHtml(index){
	//queryAllRole();
	var str = "<ul>";
	if(!roleInfoList.length){
		str +="<li><a onclick=setSelectOffice("+index+",\""+roleInfoList.RoleId+"\",\""+encodeURI(encodeURI(roleInfoList.RoleName))+"\")>"+roleInfoList.RoleName+"</a></li>";
	}else{
		for(var i=0;i<roleInfoList.length;i++){
			var obj = roleInfoList[i];
			str +="<li><a onclick=setSelectOffice("+index+",\""+obj.RoleId+"\",\""+encodeURI(encodeURI(obj.RoleName))+"\")>"+obj.RoleName+"</a></li>";
		}
	}
	str +="</ul>"
	str +="<p><a href=\"javascript:;\" onclick=\"addRole("+index+");\" class=\"office-btns\"><i class=\"icon icon-office-add\"></i>添加职务</a></p>";
	//alert(str);
	return str;
}

function saveOrUpdateUser(userId,userName,phone,name,password){
	var Service = {};
	var code = 2005;
	Service.UserId = userId;
	Service.UserName = userName;
	Service.Phone = phone;
	Service.Name = name;
	Service.Password = password;
	Service.HosId = Commonjs.hospitalId;
	//param.Api = "SaveOrUpdateUser";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveOrUpdateUser.do",param);
	if(d.respCode=10000){
		//如果修改的是当前用户信息更新最新信息到session中 ,已保持session中的用户信息正确
		var session = Commonjs.getSession("../../login/getSession.do");
		if(session.UserID==userId){
			session.Phone =  phone;
			session.Name = name;
			session.UserName = userName;
			Commonjs.updateSession(session,"../../login/updateSession.do");
		}
		console.log(d);
		return d.data.UserId;
	}else{
		//异常提示
		Commonjs.alert(d.respMessage);
	}
}

function updateRoleUser(userId,roleId){
	var Service = {};
	var page = {};
	var code = 2005;
	Service.UserId = userId;
	Service.RoleId = roleId;
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/updateRoleUser.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.respMessage);
	}
}

function saveRoleUser(userId,roleId){
	var Service = {};
	var code = 2005;
	Service.UserId = userId;
	Service.RoleId = roleId;
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveRoleUser.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.respMessage);
	}
}

function deleteUser(userId,roleId){
	art.dialog({
		lock: true,
        width: '300px',
		height: '100px',
        title:'删除确认',
        content: '您确定要删除?',
        ok : function() {
			var Service = {};
			var code = 2005;
			Service.UserId = userId;
			Service.RoleId = roleId;
			var param = Commonjs.makeParam("",code,Service,"","");
			var d =Commonjs.ajaxJson("../../account/deleteUser.do",param);
			if(d.respCode!=10000){
				//异常提示
				Commonjs.alert(d.respMessage);
				return;
			}
			Commonjs.alert("删除成功！","add");
			loadTb(1);
        },
        cancel : true
    });    
}

function saveOrUpdateRole(roleId,roleMark,roleName,roleDesc){
	var Service = {};
	var code = 2005;
	Service.RoleId = roleId;
	Service.RoleMark = roleMark;
	Service.RoleName = roleName;
	Service.RoleDesc = roleDesc;
	Service.HosId = Commonjs.hospitalId;
	//param.Api = "SaveOrUpdateRole";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveOrUpdateRole.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.respMessage);
	}
	return d.data.RoleId;
}

function queryAllMenu(){
	var Service = {};
	Service.HosId = Commonjs.hospitalId;
	var page = {};
	var code = 2005;
	var param = {};
	//param.Api = "QueryAllMenu";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/queryAllMenu.do",param);
	if(d.respCode==10000){
		menuInfoList = d.data;
	}
}

function getMenuTbHtml(){
	queryAllMenu();
	var str = "";
	if(!menuInfoList.length){
		str +="<tr>";
		str +="<td class=\"w\">"+menuInfoList.MenuName+"</td>";
		str +="<td>";
		str +="<input type=\"checkbox\" value=\"1\"  name=\""+menuInfoList.Key+"\" /><label for=\"action\" >编辑</label>";
		//str +="<input type=\"checkbox\" value=\"2\"  name=\""+menuInfoList.Key+"\" /><label for=\"action\" >查看</label>";
		str +="</td>"
		str +="</tr>";
	}else{
		for(var i=0;i<menuInfoList.length;i++){
			var obj = menuInfoList[i];
			str +="<tr>";
			str +="<td class=\"w\">"+obj.MenuName+"</td>";
			str +="<td>";
			str +="<input type=\"checkbox\" value=\"1\" name=\""+obj.Key+"\"/><label for=\"action\" class=\"\">编辑</label>";
			//str +="<input type=\"checkbox\" value=\"2\" name=\""+obj.Key+"\"/><label for=\"action\" class=\"\">查看</label>";
			str +="</td>"
			str +="</tr>";
		}
	}
	return str;
}

function saveRoleMenu(menuId,roleId,state){
	if(state<=0) return;
	var Service = {};
	var code = 2005;
	Service.MenuId = menuId;
	Service.RoleId = roleId;
	Service.State = state;
	//param.Api = "SaveRoleMenu";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveRoleMenu.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.respMessage);
	}
}
function checkUserByName(userName){
	if(userName==null||Commonjs.isEmpty(userName)) return false;
	var Service = {};
	var code = 2005;
	Service.UserName = userName;
	//param.Api = "CheckHasUserName";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/checkUserByName.do",param);
	if(d.respCode==10000){
		if(d.data.count>0){
			Commonjs.alert('用户账号已被注册!');
			document.getElementById("userName2").focus();
			return false;
		}else if(d.data.count==0){
			return true;
		}else{
			return false;
		}
	}else{
		//异常提示
		Commonjs.alert(d.respMessage);
		return false;
	}
}
function checkRoleByName(roleName){
	if(roleName==null||Commonjs.isEmpty(roleName)) return false;
	var Service = {};
	var code = 2005;
	Service.RoleName = roleName;
	//param.Api = "CheckHasRoleName";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/checkHasRoleName.do",param);
	if(d.respCode==10000){
		if(d.data>0){
			Commonjs.alert('该职务名已被注册!');
			document.getElementById("roleName").focus();
			return false;
		}else if(d.Data==0){
			return true;
		}else{
			return false;
		}
	}else{
		//异常提示
		Commonjs.alert(d.respMessage);
		return false;
	}
}
function getMenuIdByKey(key){
	if(!menuInfoList.length){
		if(key==menuInfoList.Key){
			return menuInfoList.MenuId;
		}
	}else{
		for(var i=0;i<menuInfoList.length;i++){
			var obj = menuInfoList[i];
			if(key==obj.Key){
				return obj.MenuId;
			}
		}
	}
}
function checkUser(){
	var userName = $("#userName2").val();
	var name = $("#name2").val();
	var phone = $("#mobile2").val();
	var password = $("#password2").val();
	var roleId = $("#roleId2").val();
	if(!userName||Commonjs.isEmpty(userName)){
		Commonjs.alert('用户账号不能为空');
		document.getElementById("userName2").focus();
		return false;
	}
	if(userName.length>30){
		Commonjs.alert('用户账号不能超过30个字符');
		document.getElementById("userName2").focus();
		return false;
	}
	if(!checkUserByName(userName)){
		return false;
	}
	if(name.length>30){
		Commonjs.alert('用户实名不能超过30个字符');
		document.getElementById("name2").focus();
		return false;
	}
	if(phone!=""&&!checkTel(phone)){
		Commonjs.alert('请输入正确的电话号码！');
		document.getElementById("mobile2").focus();
		return false;
	}
	if(!password||Commonjs.isEmpty(password)){
		Commonjs.alert('登录密码不能为空');
		document.getElementById("password2").focus();
		return false;
	}
	if(password.length>30){
		Commonjs.alert('登录密码不能超过30个字符');
		document.getElementById("password2").focus();
		return false;
	}
	return true;
}
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
//分页
function PageModel(totalcounts, pagecount,pager) {
	$("#"+pager).pager( {
		totalcounts : totalcounts,
		pagesize : 10,
		pagenumber : $("#pagenumber").val(),
		pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
		buttonClickCallback : function(al) {
			$("#pagenumber").val(al);
			loadTb(al);
		}
	});
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