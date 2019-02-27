var roleInfoList = undefined;
var menuInfoList = undefined;
var menuInfo = undefined;
$(function(){
	$('.office-box-list').height(250).jScrollPane({"autoReinitialise": true,"overflow":true});
	$('.office-btns').on('click',function(){
		//设置对话框中的值
		$("#roleName").val("");
		$("#roleName").attr('readOnly',null);
		$("#roleDesc").val("");
		$("#roleMark").val("");
		$("#roleId").attr("value",null);
		//加载菜单选项
		var menuStr = getMenuTbHtml();
		$("#menuTb").html(menuStr);
		var contents=$('.office-box').get(0);
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
				if(!roleName){
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
				/*if(!roleDesc){
					Commonjs.alert('职务描述不能为空');
					document.getElementById("roleDesc").focus();
					return false;
				}*/
				if(roleDesc.length>50){
					('职务描述不能超过50个字符');
					document.getElementById("roleDesc").focus();
					return false;
				}
				/*if(!roleMark){
					Commonjs.alert('职务标识不能为空');
					document.getElementById("roleMark").focus();
					return false;
				}*/
				if(roleMark.length>50){
					Commonjs.alert('职务标识不能超过50个字符');
					document.getElementById("roleMark").focus();
					return false;
				}
				var roleId = saveOrUpdateRole(null,roleMark,roleName,roleDesc)
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
					var menuuuId = getMenuuuIdByKey(obj.Key);
					saveRoleMenu(menuId,roleId,state,menuuuId);
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
						var menuuuId = getMenuuuIdByKey(obj.Key);
						saveRoleMenu(menuId,roleId,state,menuuuId);
					}
				}
				loadTb(1);
			},
			cancel: true
		});	
		return false;	
	})
	loadTb(1);
})

function loadTb(index){
	var Service = {};
	$('#pagenumber').val(index);
	var pageIndex = index-1;
	var pageSize = 10;
	Service.HosId = Commonjs.hospitalId;
	var code = 2005;
	//param.Api = "QueryAllRole";
	var param = Commonjs.makeParam("",code,Service,pageIndex,pageSize);
	var d =Commonjs.ajaxJson("../../account/queryAllRole.do",param);
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
		roleInfoList = d.data;
		var str = "<tr><th>职务名称</th><th>职务描述</th><th>职务标识</th><th class=\"w160p last\">操作</th></tr>";
		if(!roleInfoList.length){
			str += "<tr>";
			str +="<td>"+roleInfoList.RoleName+"</td>";
			str +="<td>"+roleInfoList.RoleDesc+"</td>";
			str +="<td>"+roleInfoList.RoleMark+"</td>";
			str +="<td><ul class=\"i-btn-list\"><li><a href=\"javascript:updateRoleInfo('"+roleInfoList.RoleId+"');\"  class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a href=\"javascript:deleteRole('"+roleInfoList.RoleId+"');\" class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
			//str +="<td><a href=\"javascript:;\" class=\"operate\" onclick='updateRoleInfo(\""+roleInfoList.RoleId+"\")'>编辑</a> <a href=\"javascript:;\" class=\"operate\" onclick='deleteRole(\""+userInfoList.RoleId+"\")'>删除</a></td>";
			str +="</tr>";
		}else{
			for(var i=0;i<roleInfoList.length;i++){
				var obj = roleInfoList[i];
				str += "<tr>";
				str +="<td>"+obj.RoleName+"</td>";
				str +="<td>"+obj.RoleDesc+"</td>";
				str +="<td>"+obj.RoleMark+"</td>";
				str +="<td><ul class=\"i-btn-list\"><li><a href=\"javascript:updateRoleInfo('"+obj.RoleId+"');\"  class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a href=\"javascript:deleteRole('"+obj.RoleId+"');\" class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
				//str +="<td><ul class=\"i-btn-list\"><li><a onclick='updateRoleInfo(\""+obj.RoleId+"\")'  class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a onclick='deleteRole(\""+obj.RoleId+"\")' class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
				//str +="<td><a href=\"javascript:;\" class=\"operate\" onclick='updateRoleInfo(\""+obj.RoleId+"\")'>编辑</a> <a href=\"javascript:;\" class=\"operate\" onclick='deleteRole(\""+obj.RoleId+"\")'>删除</a></td>";
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

function updateRoleInfo(roleId){
	var d = undefined;
	if(!roleInfoList.length){
		d = roleInfoList;
	}else{
		for(var i=0;i<roleInfoList.length;i++){
			var obj = roleInfoList[i];
			var rId = obj.RoleId;
			if(rId==roleId){
				d = obj;
			}
		}
	}
	//设置对话框中的值
	$("#roleName").val(d.RoleName);
	$("#roleName").attr('readOnly','true');
	$("#roleDesc").val(d.RoleDesc);
	$("#roleMark").val(d.RoleMark);
	$("#roleId").attr("value",d.RoleId);
	//加载菜单选项
	var menuStr = getMenuTbHtml();
	$("#menuTb").html(menuStr);
	//设置该角色的菜单
	queryMenuByRoleId(d.RoleId);
	console.log(menuInfo)
	if(!menuInfo.length){
		var state = menuInfo.State;
		var key = menuInfo.Key;
		selectMenu(key,state)
	}else{
		for(var i=0;i<menuInfo.length;i++){
			var menu = menuInfo[i];
			var state = menu.State;
			var key = menu.Key;
			selectMenu(key,state)
		}
	}
	
	var contents=$('.office-box').get(0);
	var artBox=art.dialog({
			lock: true,
			artIcon:'add',
			opacity:0.4,
			width: 600,
			padding:'0px 0px',
			title:'修改职务',
			content: contents,
			ok: function () {
				var roleName = $("#roleName").val();
				var roleDesc = $("#roleDesc").val();
				var roleMark = $("#roleMark").val();
				var roleId = $("#roleId").val();
				if(Commonjs.isEmpty(roleName)){
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
				if(roleMark.length>50){
					Commonjs.alert('职务标识不能超过50个字符');
					document.getElementById("roleMark").focus();
					return false;
				}
				saveOrUpdateRole(roleId,roleMark,roleName,roleDesc)
				deleteRoleMenuByRoleId(roleId);
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
					var menuuuId = getMenuuuIdByKey(obj.Key);
					Commonjs.alert(menuId)
					saveRoleMenu(menuId,roleId,state,menuuuId);
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
						var menuuuId = getMenuuuIdByKey(obj.Key);
						saveRoleMenu(menuId,roleId,state,menuuuId)
					}
				}
				Commonjs.alert("修改成功！","add");
				loadTb(1);
			},
			cancel: true
		});	
		$('.select-office-box').hide();
}

function saveOrUpdateRole(roleId,roleMark,roleName,roleDesc){
	var Service = {};
	var code = 2005;
	Service.RoleId = roleId;
	Service.RoleMark = roleMark;
	Service.RoleName = roleName;
	Service.RoleDesc = roleDesc;
	Service.HosId = Commonjs.hospitalId;
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveOrUpdateRole.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.respMessage);
	}
	//alert(d.data.RoleId)
	return d.data.RoleId;
}

function queryAllMenu(){
	var Service = {};
	Service.HosId = Commonjs.hospitalId;
	var code = 2005;
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

function saveRoleMenu(menuId,roleId,state,menuuuid){
	if(state<=0) return;
	var Service = {};
	var code = 2005;
	Service.MenuId = menuId;
	Service.RoleId = roleId;
	Service.State = state;
	Service.MenuUUID = menuuuid;
	//param.Api = "SaveRoleMenu";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveRoleMenu.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.respMessage);
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


function getMenuuuIdByKey(key){
	if(!menuInfoList.length){
		if(key==menuInfoList.Key){
			return menuInfoList.MenuUUID;
		}
	}else{
		for(var i=0;i<menuInfoList.length;i++){
			var obj = menuInfoList[i];
			if(key==obj.Key){
				return obj.MenuUUID;
			}
		}
	}
}
function queryMenuByRoleId(roleId){
	var Service = {};
	var code = 2005;
	Service.RoleId = roleId;
	//param.Api = "QueryMenuByRoleId";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/queryMenuByRoleId.do",param);
	if(d.RespCode!=10000){
		//异常提示
		Commonjs.alert(d.RespMessage);
	}
	menuInfo = d.data;
}

function selectMenu(key,state){
	var menuList = $("input[name='"+key+"']");
	for(var i=0 ;i<menuList.length;i++){
		var menu = menuList[i];
		if(state==(i+1) || state==3){
			menu.checked=true;
		}
	}
}

function deleteRoleMenuByRoleId(roleId){
	var Service = {};
	var code = 2005;
	Service.RoleId = roleId;
	//param.Api = "DeleteRoleMenuByRoleId";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/deleteRoleMenuByRoleId.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.respMessage);
	}
}

function deleteRole(roleId){
	//判断该职务是否有用户
	if(!checkHasUserByRole(roleId)){
		return;
	}
	art.dialog({
		lock: true,
        width: '300px',
		height: '100px',
        title:'删除确认',
        content: '您确定要删除?',
        ok : function() {
			var Service = {};
			var code = 2005;
			Service.RoleId = roleId;
			//param.Api = "DeleteRole";
			var param = Commonjs.makeParam("",code,Service,"","");
			var d =Commonjs.ajaxJson("../../account/deleteRole.do",param);
			if(d.respCode!=10000){
				//异常提示
				Commonjs.alert(d.respMessage);
			}
			Commonjs.alert("删除成功！","add");
			loadTb(1);
        },
        cancel : true
    }); 
}
function checkHasUserByRole(roleId){
	if(roleId==null||roleId=="") return false;
	var Service = {};
	var code = 2005;
	Service.RoleId = roleId;
	//param.Api = "CheckHasUserByRole";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/checkHasUserByRole.do",param);
	if(d.respCode==10000){
		if(d.data.count>0){
			Commonjs.alert('该职务下已有用户，不能删除!');
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
	if(Commonjs.isEmpty(roleName)) return false;
	var Service = {};
	var code = 2005;
	Service.RoleName = roleName;
	//param.Api = "CheckHasRoleName";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/checkHasRoleName.do",param);
	console.log(d);
	if(d.respCode==10000){
		if(d.data.count>0){
			Commonjs.alert('该职务名已被注册!');
			document.getElementById("roleName").focus();
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