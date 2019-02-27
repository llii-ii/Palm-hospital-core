var userInfoList = undefined;
var roleInfoList = undefined;
var menuInfoList = undefined;
var roleMsg1 = undefined;
var roleMsg2 = undefined;

$(function(){
	$('#menuDiv').height(250).jScrollPane({"autoReinitialise": true,"overflow":true});
	//查询角色信息
	loadRole();
	
	//加载菜单Tab
	loadMenuTab();
	
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
//新增角色/职务
function addRole(index){
	$("#roleName").val("");
	$("#roleDesc").val("");
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
			if(roleDesc.length>50){
				Commonjs.alert('职务描述不能超过50个字符');
				document.getElementById("roleDesc").focus();
				return false;
			}
			//遍历所有选中的权限checkbox
			var menuIds = new Array();
			var rolePerms = $("input[name='permsChkBoxName']");
			$.each(rolePerms,function(i,o){
				if($(o).attr('checked')){
					var value = $(o).attr('value');
					if(!Commonjs.isEmpty(value)){
						$.each(value.split('_'),function(j,val){
							if($.inArray(val,menuIds)==-1){
								menuIds.push(val);
							}
						})
					}
				}
			})
			//新增角色/职务，及权限
			var param = {};
			param.reqParam = Commonjs.getReqParams({'RoleName':roleName,'Remark':roleDesc,'MenuList':menuIds});
			Commonjs.ajax("/sys/addRole.do",param,function(d){
				if(d.RespCode==10000){
					loadRole(index);
					$('#selectOfficeBox'+index).hide();	
					artBox.close();
				}else{
					//异常提示
					Commonjs.alert(d.RespMessage);
				}
			});
			
			return false;
		},
		cancel: function(){
			$('#selectOfficeBox'+index).hide();	
		}
	});	
}
//新增用户信息
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
			var bb = checkUser();
			if(!bb){
				return false;
			}
			//验证用户名是否已经存在。
			var param = {};
			param.reqParam = Commonjs.getReqParams({'Username':userName,'Mobile':phone,'RealName':name,'Password':$.md5(password).toUpperCase(),'RoleId':roleId});
			Commonjs.ajax('/sys/addUser.do',param,function(d){
				if(d.RespCode==10000){
					loadTb(1);
					$('#selectOfficeBox2').hide();
					Commonjs.alert("添加成功！","add");
					//关闭弹出层
					artBox.close();
				}else{
					//异常提示
					Commonjs.alert(d.RespMessage);
				}
			});
			return false;
		},
		cancel: function(){
			$('#selectOfficeBox2').hide();
		}
	});
}
//加载用户管理tab
function loadTb(index){
	var param = {};
	$('#pagenumber').val(index);
	var pageIndex = index-1;
	var pageSize = 10;
	param.reqParam = Commonjs.getReqParams({'Page':{'PIndex':pageIndex,'PSize':pageSize}});
	Commonjs.ajax("/sys/queryUserList.do",param,function(d){
		if(d.RespCode=10000){
			if(d.Page.PCount!=undefined){
				if(d.Page.PCount!=0){
					$("#totalcount").val(d.Page.PCount);
				}else{
					if(d.Page.PIndex==0)$("#totalcount").val(0);
				}
			}else{
				$("#totalcount").val(0);
			}
			PageModel($("#totalcount").val(),d.Page.PSize,'pager');
			userInfoList = d.Data;
			var str = "<tr><th>用户账号</th><th>实名</th><th>职务</th><th>电话号码</th><th class=\"last\">操作</th></tr>";
			for(var i=0;i<userInfoList.length;i++){
				var obj = userInfoList[i];
				str += "<tr>";
				str +="<td>"+obj.Username+"</td>";
				str +="<td>"+obj.RealName+"</td>";
				str +="<td>"+obj.RoleName+"</td>";
				str +="<td>"+obj.Mobile+"</td>";
				str +="<td><ul class=\"i-btn-list\"><li><a href=\"javascript:updateUserInfo('"+obj.Id+"');\" class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a href=\"javascript:deleteUser('"+obj.Id+"');\" class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
				str +="</tr>";
			}
			$("#tb").html(str);
		}else{
			//异常提示
			PageModel(0,pageSize,'pager');
			Commonjs.alert(d.RespMessage);
		}
	});
	
}

//更新用户信息
function updateUserInfo(userId){
	$('#resetTr').attr("display","none");
	var d = undefined;
	if(!userInfoList.length){
		d = userInfoList;
	}else{
		for(var i=0;i<userInfoList.length;i++){
			var obj = userInfoList[i];
			var uId = obj.Id;
			if(uId==userId){
				d = obj;
			}
		}
	}
	$("#userName1").val(d.Username);
	$("#name1").val(d.RealName);
	$("#mobile1").val(d.Mobile);
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
				var param = {};
				param.reqParam = Commonjs.getReqParams({'Id':userId,'Username':userName,'Mobile':phone,'RealName':name,'Password':(updatePasswordState==1?$.md5(password).toUpperCase():null),'RoleId':roleId});
				Commonjs.ajax('/sys/updateUser.do',param,function(d){
					if(d.RespCode=10000){
						//如果修改的是当前用户信息更新最新信息到session中 ,已保持session中的用户信息正确
						Commonjs.getSession(1);
						loadTb(1);
						Commonjs.alert("修改成功！","add");
					}else{
						//异常提示
						Commonjs.alert(d.RespMessage);
					}
				});
				$('#selectOfficeBox1').hide();
			},
			cancel: function(){
				$('#selectOfficeBox1').hide();
			}
		});	
	//$('#selectOfficeBox1').hide();	
}

//加载角色信息
function loadRole(index){
	var param = {};
	var pageIndex = 0;
	var pageSize = 100;
	var data = {'Page':{'PIndex':pageIndex,'PSize':pageSize}};
	param.reqParam = Commonjs.getReqParams(data);
	Commonjs.ajax("/sys/queryRoleList.do",param,function(dd){
		if(dd.RespCode==10000){
			roleInfoList = dd.Data;
			if(index){
				if(index==1){
					roleMsg1 = getSelectOfficeBoxHtml(index);
				}else{
					roleMsg2 = getSelectOfficeBoxHtml(index);
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
			}else{
				roleMsg2 = getSelectOfficeBoxHtml(2);
				roleMsg1 = getSelectOfficeBoxHtml(1);
				$("#roleId2").attr("value",roleInfoList[0].RoleId);
				$("#selectOffice2").html(roleInfoList[0].RoleName);
				$("#roleId1").attr("value",roleInfoList[0].RoleId);
				$("#selectOffice1").html(roleInfoList[0].RoleName);
			}
		}
	});
}
//选择角色
function setSelectOffice(index,roleId,roleName){
	$("#roleId"+index).attr("value",roleId);
	$("#selectOffice"+index).html(decodeURI(decodeURI(roleName)));
	$('#selectOfficeBox'+index).hide();
}
//角色下拉框html
function getSelectOfficeBoxHtml(index){
	var str = "<ul>";
	for(var i=0;i<roleInfoList.length;i++){
		var obj = roleInfoList[i];
		str +="<li><a onclick=setSelectOffice("+index+",\""+obj.RoleId+"\",\""+encodeURI(encodeURI(obj.RoleName))+"\")>"+obj.RoleName+"</a></li>";
	}
	str +="</ul>"
	str +="<p><a href=\"javascript:;\" onclick=\"addRole("+index+");\" class=\"office-btns\"><i class=\"icon icon-office-add\"></i>添加职务</a></p>";
	return str;
}
//删除用户
function deleteUser(userId){
	art.dialog({
		lock: true,
        width: '300px',
		height: '100px',
        title:'删除确认',
        content: '您确定要删除?',
        ok : function() {
			var param = {};
			param.reqParam = Commonjs.getReqParams({'Id':userId});
			Commonjs.ajax("/sys/deleteUser.do",param,function(d){
				if(d.RespCode==10000){
					loadTb(1);
					Commonjs.alert("删除成功！","add");
				}else{
					//异常提示
					Commonjs.alert(d.RespMessage);
				}
			});
        },
        cancel : true
    });    
}

//function saveOrUpdateRole(roleId,roleName,roleDesc){
//	var Service = {};
//	var code = 2005;
//	Service.RoleId = roleId;
//	Service.RoleName = roleName;
//	Service.RoleDesc = roleDesc;
//	Service.HosId = Commonjs.hospitalId;
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("/sys/saveOrUpdateRole.do",param);
//	if(d.respCode!=10000){
//		//异常提示
//		Commonjs.alert(d.respMessage);
//	}
//	return d.data.RoleId;
//}
//加载菜单权限
function loadMenuTab(){
	var param = {};
	var data = {};
	param.reqParam = Commonjs.getReqParams(data);
	Commonjs.ajax("/sys/queryMenuTree.do",param,function(d){
		if(d.RespCode==10000){
			menuInfoList = d.Data;
			var html = "";
			for(var i=0;i<menuInfoList.length;i++){
				var obj = menuInfoList[i];
				//目录
				if(obj.Type==0){
					$.each(obj.List,function(i,menu){
						//菜单
						if(menu.Type==1){
							html +="<tr>";
							html +="<td class=\"w\">"+menu.Name+"</td>";
							html +="<td>";
							if(!Commonjs.isEmpty(menu.Perms)){
								$.each(menu.Perms,function(j,pp){
									//权限
									chkBoxValue = menu.ParentId+"_"+menu.MenuId+"_"+pp.MenuId;
									html +="<input type=\"checkbox\" value=\""+chkBoxValue+"\" name=\"permsChkBoxName\" id=\"chk_"+chkBoxValue+"\"/><label for=\"chk_"+chkBoxValue+"\" class=\"\" style=\"cursor:pointer;\">"+pp.Name+"</label></br>";
								})
							}
							html +="</td>"
							html +="</tr>";
						}
					})
				}
			}
			$("#menuTb").html(html);
		}
	});
}


//function saveRoleMenu(menuId,roleId,state){
//	if(state<=0) return;
//	var Service = {};
//	var code = 2005;
//	Service.MenuId = menuId;
//	Service.RoleId = roleId;
//	Service.State = state;
//	//param.Api = "SaveRoleMenu";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d = Commonjs.ajaxJson("../../account/saveRoleMenu.do",param);
//	if(d.respCode!=10000){
//		//异常提示
//		Commonjs.alert(d.respMessage);
//	}
//}
//function getMenuIdByKey(key){
//	for(var i=0;i<menuInfoList.length;i++){
//		var obj = menuInfoList[i];
//		if(key==obj.Key){
//			return obj.MenuId;
//		}
//	}
//}
//新增用户验证
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