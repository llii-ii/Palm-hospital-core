var roleInfoList = undefined;
var menuInfoList = undefined;
$(function(){
	$('.office-box-list').height(250).jScrollPane({"autoReinitialise": true,"overflow":true});
	
	//新增职务
	$('.office-btns').on('click',function(){
		//设置对话框中的值
		$("#roleName").val("");
		$("#roleName").attr('readOnly',null);
		$("#roleDesc").val("");
		$("#roleId").attr("value",null);
		
		//加载菜单选项
		loadMenuTab();
		
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
				if(roleDesc.length>50){
					('职务描述不能超过50个字符');
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
						//刷新角色权限列表
						loadTb(1);
						artBox.close();
					}else{
						//异常提示
						Commonjs.alert(d.RespMessage);
					}
				});
				return false;
			},
			cancel: true
		});	
	})
	//加载职务 角色列表
	loadTb(1);
})

//加载菜单
function loadMenuTab(roleMenus){
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
								if(roleMenus){
									$.each(menu.Perms,function(j,pp){
										//权限
										var checked = '';
										if($.inArray(pp.MenuId,roleMenus)!=-1){
											checked = 'checked';
										}
										chkBoxValue = menu.ParentId+"_"+menu.MenuId+"_"+pp.MenuId;
										html +="<input type=\"checkbox\" value=\""+chkBoxValue+"\" name=\"permsChkBoxName\" id=\"chk_"+chkBoxValue+"\" "+checked+" /><label for=\"chk_"+chkBoxValue+"\" class=\"\" style=\"cursor:pointer;\">"+pp.Name+"</label></br>";
									})
								}else{
									$.each(menu.Perms,function(j,pp){
										//权限
										chkBoxValue = menu.ParentId+"_"+menu.MenuId+"_"+pp.MenuId;
										html +="<input type=\"checkbox\" value=\""+chkBoxValue+"\" name=\"permsChkBoxName\" id=\"chk_"+chkBoxValue+"\"/><label for=\"chk_"+chkBoxValue+"\" class=\"\" style=\"cursor:pointer;\">"+pp.Name+"</label></br>";
									})
								}
							}
							html +="</td>"
							html +="</tr>";
						}
					});
				}
			}
			$("#menuTb").html(html);
		}
	});
}
//加载角色
function loadTb(index){
	var param = {};
	$('#pagenumber').val(index);
	var pageIndex = index-1;
	var pageSize = 10;
	param.reqParam = Commonjs.getReqParams({'Page':{'PIndex':pageIndex,'PSize':pageSize}});
	Commonjs.ajax("/sys/queryRoleList.do",param,function(d){
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
			roleInfoList = d.Data;
			var str = "<tr><th>职务名称</th><th>职务描述</th><th class=\"w160p last\">操作</th></tr>";
			for(var i=0;i<roleInfoList.length;i++){
				var obj = roleInfoList[i];
				str += "<tr>";
				str +="<td>"+obj.RoleName+"</td>";
				str +="<td>"+obj.Remark+"</td>";
				if(obj.RoleId!=1){
					str +="<td><ul class=\"i-btn-list\"><li><a href=\"javascript:updateRoleInfo('"+obj.RoleId+"');\"  class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a href=\"javascript:deleteRole('"+obj.RoleId+"');\" class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
				}else{
					str +="<td></td>"
				}
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

function updateRoleInfo(roleId){
	var d = undefined;
	for(var i=0;i<roleInfoList.length;i++){
		var obj = roleInfoList[i];
		var rId = obj.RoleId;
		if(rId==roleId){
			d = obj;
		}
	}
	//设置对话框中的值
	$("#roleName").val(d.RoleName);
	$("#roleName").attr('readOnly','true');
	$("#roleId").attr("value",d.RoleId);
	
	//根据角色/职务查询对应的权限
	queryMenusByRoleId(d.RoleId);
	
	var contents=$('.office-box').get(0);
	var artBox = art.dialog({
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
				param.reqParam = Commonjs.getReqParams({'RoleId':roleId,'Remark':roleDesc,'MenuList':menuIds});
				Commonjs.ajax("/sys/updateRole.do",param,function(d){
					if(d.RespCode==10000){
						loadTb(1);
						artBox.close();
						Commonjs.alert("修改成功！","add");
					}else{
						//异常提示
						Commonjs.alert(d.RespMessage);
					}
				});
			},
			cancel: true
		});	
		$('.select-office-box').hide();
}

//function saveOrUpdateRole(roleId,roleName,roleDesc){
//	var Service = {};
//	var code = 2005;
//	Service.RoleId = roleId;
//	Service.RoleName = roleName;
//	Service.RoleDesc = roleDesc;
//	Service.HosId = Commonjs.hospitalId;
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/saveOrUpdateRole.do",param);
//	if(d.respCode!=10000){
//		//异常提示
//		Commonjs.alert(d.respMessage);
//	}
//	//alert(d.data.RoleId)
//	return d.data.RoleId;
//}
//function queryAllMenu(){
//	var Service = {};
//	Service.HosId = Commonjs.hospitalId;
//	var code = 2005;
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/queryAllMenu.do",param);
//	if(d.respCode==10000){
//		menuInfoList = d.data;
//	}
//}
//
//function getMenuTbHtml(){
//	queryAllMenu();
//	var str = "";
//	for(var i=0;i<menuInfoList.length;i++){
//		var obj = menuInfoList[i];
//		str +="<tr>";
//		str +="<td class=\"w\">"+obj.MenuName+"</td>";
//		str +="<td>";
//		str +="<input type=\"checkbox\" value=\"1\" name=\""+obj.Key+"\"/><label for=\"action\" class=\"\">编辑</label>";
//		str +="</td>"
//		str +="</tr>";
//	}
//	return str;
//}
//function saveRoleMenu(menuId,roleId,state,menuuuid){
//	if(state<=0) return;
//	var Service = {};
//	var code = 2005;
//	Service.MenuId = menuId;
//	Service.RoleId = roleId;
//	Service.State = state;
//	Service.MenuUUID = menuuuid;
//	//param.Api = "SaveRoleMenu";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/saveRoleMenu.do",param);
//	if(d.respCode!=10000){
//		//异常提示
//		Commonjs.alert(d.respMessage);
//	}
//}
//function getMenuIdByKey(key){
//	if(!menuInfoList.length){
//		if(key==menuInfoList.Key){
//			return menuInfoList.MenuId;
//		}
//	}else{
//		for(var i=0;i<menuInfoList.length;i++){
//			var obj = menuInfoList[i];
//			if(key==obj.Key){
//				return obj.MenuId;
//			}
//		}
//	}
//}
//function getMenuuuIdByKey(key){
//	if(!menuInfoList.length){
//		if(key==menuInfoList.Key){
//			return menuInfoList.MenuUUID;
//		}
//	}else{
//		for(var i=0;i<menuInfoList.length;i++){
//			var obj = menuInfoList[i];
//			if(key==obj.Key){
//				return obj.MenuUUID;
//			}
//		}
//	}
//}
//查询角色权限
function queryMenusByRoleId(roleId){
	var param = {};
	param.reqParam = Commonjs.getReqParams({'RoleId':roleId});
	Commonjs.ajax("/sys/queryMenusByRoleId.do",param,function(d){
		if(d.RespCode!=10000){
			//异常提示
			Commonjs.alert(d.RespMessage);
			return;
		}
		var roleMenus = new Array();
		if(!Commonjs.isEmpty(d.Data)){
			$.each(d.Data,function(i,o){
				roleMenus.push(o.MenuId);
			})
		}
		//查询菜单列表，并回填角色的权限
		loadMenuTab(roleMenus);
	});
}
//function selectMenu(key,state){
//	var menuList = $("input[name='"+key+"']");
//	for(var i=0 ;i<menuList.length;i++){
//		var menu = menuList[i];
//		if(state==(i+1) || state==3){
//			menu.checked=true;
//		}
//	}
//}
//function deleteRoleMenuByRoleId(roleId){
//	var Service = {};
//	var code = 2005;
//	Service.RoleId = roleId;
//	//param.Api = "DeleteRoleMenuByRoleId";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("/sys/delRole.do",param);
//	if(d.respCode!=10000){
//		//异常提示
//		Commonjs.alert(d.respMessage);
//	}
//}

function deleteRole(roleId){
	//判断该职务是否有用户
	art.dialog({
		lock: true,
        width: '300px',
		height: '100px',
        title:'删除确认',
        content: '您确定要删除?',
        ok : function() {
			var param = {};
			param.reqParam = Commonjs.getReqParams({'RoleId':roleId})
			Commonjs.ajax("/sys/delRole.do",param,function(d){
				if(d.RespCode!=10000){
					//异常提示
					Commonjs.alert(d.RespMessage);
				}else{
					Commonjs.alert("删除成功！","add");
				}
				loadTb(1);
			});
        },
        cancel : true
    }); 
}
//function checkHasUserByRole(roleId){
//	if(roleId==null||roleId=="") return false;
//	var Service = {};
//	var code = 2005;
//	Service.RoleId = roleId;
//	//param.Api = "CheckHasUserByRole";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/checkHasUserByRole.do",param);
//	if(d.respCode==10000){
//		if(d.data.count>0){
//			Commonjs.alert('该职务下已有用户，不能删除!');
//			return false;
//		}else if(d.data.count==0){
//			return true;
//		}else{
//			return false;
//		}
//	}else{
//		//异常提示
//		Commonjs.alert(d.respMessage);
//		return false;
//	}
//}
//function checkRoleByName(roleName){
//	if(Commonjs.isEmpty(roleName)) return false;
//	var Service = {};
//	var code = 2005;
//	Service.RoleName = roleName;
//	//param.Api = "CheckHasRoleName";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/checkHasRoleName.do",param);
//	console.log(d);
//	if(d.respCode==10000){
//		if(d.data.count>0){
//			Commonjs.alert('该职务名已被注册!');
//			document.getElementById("roleName").focus();
//			return false;
//		}else if(d.data.count==0){
//			return true;
//		}else{
//			return false;
//		}
//	}else{
//		//异常提示
//		Commonjs.alert(d.respMessage);
//		return false;
//	}
//}
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