var roleInfoList = undefined;
var menuInfoList = undefined;
var menuInfo = undefined;
var pagenumber = 0;
$(function(){
	$('.c-btn-blue').on('click',function(){
		//设置对话框中的值
		$("#roleName").val("");
		$("#roleName").attr('readOnly',null);
		$("#roleDesc").val("");
		$("#roleMark").val("");
		$("#roleId").attr("value",null);
		//加载菜单选项
		var menuStr = getMenuTbHtml(null);
		//$("#menuTb").html(menuStr);
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
				if(roleDesc.length>50){
					('职务描述不能超过50个字符');
					document.getElementById("roleDesc").focus();
					return false;
				}
				if(roleMark.length>50){
					Commonjs.alert('职务标识不能超过50个字符');
					document.getElementById("roleMark").focus();
					return false;
				}
				var idList = getIdList();
				if(Commonjs.isEmpty(idList)){
					Commonjs.alert('请选择菜单权限');
					return false;
				}
				
				var roleId = saveOrUpdateRole(null,roleMark,roleName,roleDesc)
				idList = idList.substr(0, idList.length - 1);
	 			var Service = {};
	 			Service.roleId = roleId;
	 			Service.idList = idList;
	 			var code = 2005;
	 			var param = Commonjs.makeParam("",code,Service,"","");
	 			var artLoading=art.dialog({lock: true, padding:'25px 60px 5px 60px', content: '<img src="../../widget/artDialog/4.1.7/images/loading.gif" class="mr10" />正在请求…', tips:true});
	 			//var d =Commonjs.ajaxJson("../../account/saveTreeMenuByRoleId.do",param);
	 			 $.ajax({
		 		        url:"../../account/saveTreeMenuByRoleId.do",    
		 		        dataType:"json",   
		 		        async:true,
		 		        data:param,    
		 		        type:"post",  
		 		        contentType:"application/json",
		 		        success:function(data){
		 		        	artLoading.close();
		 		            var respCode = data.respCode;
		 		            if(respCode== 10000){
		 		            	Commonjs.alert("新增成功！","add");
		 	 					loadTb(1);
		 		            }else{
		 		            	Commonjs.alert(d.respMessage);
		 		            }
		 		        },
		 		        error:function(){
		 		        	artLoading.close();
		 		        	Commonjs.alert("请求出错，请联系管理员！");
		 		        }
		 		    });
			},
			cancel: {}
		});	
		return false;	
	})
	loadTb(1);
})

function loadTb(index){
	var Service = {};
	pagenumber = index;
	var totalcount = $("#totalcount").val();
	var pageIndex = index-1;
	var pageSize = 10;
	Service.HosId = Commonjs.hospitalId;
	var code = 2005;
	//param.Api = "QueryAllRole";
	var param = Commonjs.makeParam("",code,Service,pageIndex,pageSize);
	var d =Commonjs.ajaxJson("../../account/queryAllRole.do",param);
	if(d.respCode==10000){
		if(d.page.pCount!=undefined){
			if(d.page.pCount!=0){
				totalcount = d.page.pCount;
			}else{
				if(d.page.pIndex==0) totalcount = 0;
			}
		}else{
			totalcount = 0;
		}
		PageModel(totalcount,d.page.pSize,'pager');
		roleInfoList = d.data;
		var str = "<thead><tr><th>职务名称</th><th>职务描述</th><th>职务标识</th><th class=\"w160p last\">操作</th></tr></thead><tbody>";
		if(!roleInfoList.length){
			str += "<tr>";
			str +="<td>"+roleInfoList.RoleName+"</td>";
			str +="<td>"+roleInfoList.RoleDesc+"</td>";
			str +="<td>"+roleInfoList.RoleMark+"</td>";
			str += "<td><a href=\"javascript:updateRoleInfo('"+roleInfoList.RoleId+"');\" class=\"alinks-unline alinks-blue d-detail-edit\">编辑</a>";
			str += "&nbsp;&nbsp;&nbsp;<a href=\"javascript:deleteRole('"+roleInfoList.RoleId+"');\" class=\"alinks-unline alinks-blue\">删除</a></td>";
			str +="</tr>";
		}else{
			for(var i=0;i<roleInfoList.length;i++){
				var obj = roleInfoList[i];
				str += "<tr>";
				str +="<td>"+obj.RoleName+"</td>";
				str +="<td>"+obj.RoleDesc+"</td>";
				str +="<td>"+obj.RoleMark+"</td>";
				str += "<td><a href=\"javascript:updateRoleInfo('"+obj.RoleId+"');\" class=\"alinks-unline alinks-blue d-detail-edit\">编辑</a>";
				str += "&nbsp;&nbsp;&nbsp;<a href=\"javascript:deleteRole('"+obj.RoleId+"');\" class=\"alinks-unline alinks-blue\">删除</a></td>";
				str +="</tr>";
			}
		}
		$("#roleList").html(str);
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
	var menuStr = getMenuTbHtml(roleId);
	//$("#menuTb").html(menuStr);
	//设置该角色的菜单
	//queryMenuByRoleId(d.RoleId);
//	if(!menuInfo.length){
//		var state = menuInfo.State;
//		var key = menuInfo.Key;
//		selectMenu(key,state)
//	}else{
//		for(var i=0;i<menuInfo.length;i++){
//			var menu = menuInfo[i];
//			var state = menu.State;
//			var key = menu.Key;
//			selectMenu(key,state)
//		}
//	}
	
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
				//deleteRoleMenuByRoleId(roleId);
				//修改菜单权限
				var idList = getIdList();
				if(Commonjs.isEmpty(idList)){
					Commonjs.alert('请选择菜单权限');
					return false;
				}
	 			idList = idList.substr(0, idList.length - 1);
	 			var Service = {};
	 			Service.roleId = roleId;
	 			Service.idList = idList;
	 			var code = 2005;
	 			var param = Commonjs.makeParam("",code,Service,"","");
	 			var artLoading=art.dialog({lock: true, padding:'25px 60px 5px 60px', content: '<img src="../../widget/artDialog/4.1.7/images/loading.gif" class="mr10" />正在请求…', tips:true});
	 			//var d =Commonjs.jsonAjax("../../account/saveTreeMenuByRoleId.do",param,true);
	 			 $.ajax({
	 		        url:"../../account/saveTreeMenuByRoleId.do",    
	 		        dataType:"json",   
	 		        async:true,
	 		        data:param,    
	 		        type:"post",  
	 		        contentType:"application/json",
	 		        success:function(data){
	 		        	artLoading.close();
	 		            var respCode = data.respCode;
	 		            if(respCode== 10000){
	 		            	Commonjs.alert("修改成功！","add");
	 	 					loadTb(1);
	 		            }else{
	 		            	Commonjs.alert(d.respMessage);
	 		            }
	 		        },
	 		        error:function(){
	 		        	artLoading.close();
	 		        	Commonjs.alert("请求出错，请联系管理员！");
	 		        }
	 		    });
			},
			cancel: {}
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
var xtree;
function queryAllMenu(roleId){
	var Service = {};
	Service.HosId = Commonjs.hospitalId;
	Service.roleId = roleId;
	var code = 2005;
	var param = Commonjs.makeParam("",code,Service,"","");
	var d;
	if(roleId==null||roleId==undefined){
		d = Commonjs.ajaxJson("../../account/queryAllTreeMenu.do",param);
	}else{
		d = Commonjs.ajaxJson("../../account/queryTreeMenuByRoleId.do",param);
	}
	if(d.respCode==10000){
		var json = d.data.json;
		initTree(json);
	}
}
//渲染界面
function initTree(json) {
	layui.use([ 'form' ], function() {
		var form = layui.form;
		xtree = new layuiXtree({
			elem : 'xtree',
			form : form,
			data : json
		});
	});
}
//获取idList
function getIdList() {
	var oCks = xtree.GetChecked();
	var idList = '';
	var pid = '';
	for (var i = 0; i < oCks.length; i++) {
		idList += oCks[i].value + ',';
		if(xtree.GetParent(oCks[i].value) != null){
			if (pid != xtree.GetParent(oCks[i].value).value) {
				pid = xtree.GetParent(oCks[i].value).value;
				idList += pid + ',';
			}
		}
	}
	return idList;
}
function getMenuTbHtml(roleId){
	queryAllMenu(roleId);
//	var str = "";
//	if(!menuInfoList.length){
//		str +="<tr>";
//		str +="<td class=\"w\">"+menuInfoList.MenuName+"</td>";
//		str +="<td>";
//		str +="<input type=\"checkbox\" value=\"1\"  name=\""+menuInfoList.Key+"\" /><label for=\"action\" >编辑</label>";
//		str +="</td>"
//		str +="</tr>";
//	}else{
//		for(var i=0;i<menuInfoList.length;i++){
//			var obj = menuInfoList[i];
//			str +="<tr>";
//			str +="<td class=\"w\">"+obj.MenuName+"</td>";
//			str +="<td>";
//			str +="<input type=\"checkbox\" value=\"1\" name=\""+obj.Key+"\"/><label for=\"action\" class=\"\">编辑</label>";
//			str +="</td>"
//			str +="</tr>";
//		}
//	}
//	return str;
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
		pagenumber : pagenumber,
		pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
		buttonClickCallback : function(al) {
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