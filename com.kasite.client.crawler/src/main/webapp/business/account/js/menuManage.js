var menuInfoList = undefined;
var menuInfoList = undefined;
var menuInfo = undefined;
$(function(){
	$('.office-box-list').height(250).jScrollPane({"autoReinitialise": true,"overflow":true});
	$('.office-btns').on('click',function(){
		//设置对话框中的值
		$("#txtMenuID").val("");
		$("#txtMenuUUID").val("");
		$("#txtMenuID").removeAttr('readOnly');
		$("#txtFatherMenuID").val("");
		$("#txtMenuName").val("");
		$("#txtMenuUrl").val("");
		$("#txtMenuSort").val("");
		$("#txtMenuKey").val("");
		//加载菜单选项
		var contents=$('.office-box').get(0);
		var artBox=art.dialog({
			lock: true,
			artIcon:'add',
			opacity:0.4,
			width: 550,
			padding:'0px 0px',
			title:'新增菜单',
			content: contents,
			ok: function () {
				var menuID = $("#txtMenuID").val();
				var pMenuID = $("#txtFatherMenuID").val();
				var menuName = $("#txtMenuName").val();
				var menuUrl = $("#txtMenuUrl").val();
				var menuSort = $("#txtMenuSort").val();
				var menuKey = $("#txtMenuKey").val();
				if(!menuID){
					Commonjs.alert('菜单ID不能为空');
					document.getElementById("txtMenuID").focus();
					return false;
				}
				if(!pMenuID){
					Commonjs.alert('父菜单ID不能为空');
					document.getElementById("txtFatherMenuID").focus();
					return false;
				}
				if(!menuName){
					Commonjs.alert('菜单名称不能为空');
					document.getElementById("txtMenuName").focus();
					return false;
				}
				if(!menuKey){
					Commonjs.alert('菜单名称不能为空');
					document.getElementById("txtMenuKey").focus();
					return false;
				}
				//保存菜单
				var menuID = saveOrUpdateMenu(menuID,pMenuID,menuName,menuUrl,menuSort,menuKey,null);
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
	//param.Api = "QueryAllMenuByPage";
	var param = Commonjs.makeParam("",code,Service,pageIndex,pageSize);
	var d =Commonjs.ajaxJson("../../account/queryAllMenuByPage.do",param);
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
		menuInfoList = d.data;
		var str = "<tr><th>菜单ID</th><th>菜单名称</th><th>链接地址</th><th>父菜单ID</th><th>排序</th><th class=\"w160p last\">操作</th></tr>";
		if(!menuInfoList.length){
			str += "<tr>";
			str +="<td>"+menuInfoList.MenuId+"</td>";
			str +="<td>"+menuInfoList.MenuName+"</td>";
			str +="<td>"+menuInfoList.Value+"</td>";
			str +="<td>"+menuInfoList.PMenuId+"</td>";
			str +="<td>"+menuInfoList.Sort+"</td>";
			str +="<td><ul class=\"i-btn-list\"><li><a href=\"javascript:updateMenuInfo('"+menuInfoList.MenuUUID+"');\"  class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a href=\"javascript:deleteMenu('"+menuInfoList.MenuUUID+"');\" class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
			str +="</tr>";
		}else{
			for(var i=0;i<menuInfoList.length;i++){
				var obj = menuInfoList[i];
				str += "<tr>";
				str +="<td>"+obj.MenuId+"</td>";
				str +="<td>"+obj.MenuName+"</td>";
				str +="<td>"+obj.Value+"</td>";
				str +="<td>"+obj.PMenuId+"</td>";
				str +="<td>"+obj.Sort+"</td>";
				str +="<td><ul class=\"i-btn-list\"><li><a href=\"javascript:updateMenuInfo('"+obj.MenuUUID+"');\"  class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a href=\"javascript:deleteMenu('"+obj.MenuUUID+"');\" class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
				str +="</tr>";
			}
		}
		$("#tb").html(str);
	}else{
		//异常提示
		PageModel(0,pageSize,'pager');
		Commonjs.alert(d.respMessage);
	}
}

function updateMenuInfo(menuUUID){
	var d = undefined;
	if(!menuInfoList.length){
		d = menuInfoList;
	}else{
		for(var i=0;i<menuInfoList.length;i++){
			var obj = menuInfoList[i];
			var mId = obj.MenuUUID;
			if(mId==menuUUID){
				d = obj;
				break;
			}
		}
	}
	//设置对话框中的值
	$("#txtMenuID").val(d.MenuId);
	$("#txtMenuID").attr('readOnly','true');
	$("#txtFatherMenuID").val(d.PMenuId);
	$("#txtMenuName").val(d.MenuName);
	$("#txtMenuKey").val(d.Key);
	$("#txtMenuUrl").val(d.Value);
	$("#txtMenuSort").val(d.Sort);
	$("#txtMenuUUID").val(d.MenuUUID);
	var contents=$('.office-box').get(0);
	var artBox=art.dialog({
			lock: true,
			artIcon:'add',
			opacity:0.4,
			width: 600,
			padding:'0px 0px',
			title:'修改菜单',
			content: contents,
			ok: function () {
				var menuID = $("#txtMenuID").val();
				var pMenuID = $("#txtFatherMenuID").val();
				var menuName = $("#txtMenuName").val();
				var menuUrl = $("#txtMenuUrl").val();
				var menuSort = $("#txtMenuSort").val();
				var menuKey = $("#txtMenuKey").val();
				var menuUUID = $("#txtMenuUUID").val();
				if(!menuID){
					Commonjs.alert('菜单ID不能为空');
					document.getElementById("txtMenuID").focus();
					return false;
				}
				if(!pMenuID){
					Commonjs.alert('父菜单ID不能为空');
					document.getElementById("txtFatherMenuID").focus();
					return false;
				}
				if(!menuName){
					Commonjs.alert('菜单名称不能为空');
					document.getElementById("txtMenuName").focus();
					return false;
				}
				if(!menuKey){
					Commonjs.alert('菜单名称不能为空');
					document.getElementById("txtMenuKey").focus();
					return false;
				}
				//保存菜单
				var menuID = saveOrUpdateMenu(menuID,pMenuID,menuName,menuUrl,menuSort,menuKey,menuUUID);
				loadTb(1);
			},
			cancel: true
		});	
		$('.select-office-box').hide();
}

function saveOrUpdateMenu(menuID,pMenuID,menuName,menuUrl,menuSort,menuKey,menuUUID){
	var Service = {};
	var code = 2005;
	Service.MenuID = menuID;
	Service.PMenuID = pMenuID;
	Service.MenuName = menuName;
	Service.MenuUrl = menuUrl.replace(new RegExp(/(&)/g),'&amp;');
	Service.MenuSort= menuSort;
	Service.MenuKey= menuKey;
	if( menuUUID!= null && menuUUID!=undefined ){
		Service.MenuUUID = menuUUID;
	}
	Service.HosId = Commonjs.hospitalId;
	//param.Api = "SaveOrUpdateMenu";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveOrUpdateMenu.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.RespMessage);
	}
	return d.data.menuID;
}

function queryAllMenu(){
	var Service = {};
	var code = 2005;
	var param = {};
	Service.HosId = Commonjs.hospitalId;
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
		Commonjs.alert(d.espMessage);
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

function queryMenuByRoleId(roleId){
	var Service = {};
	var code = 2005;
	Service.RoleId = roleId;
	//param.Api = "QueryMenuByRoleId";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/queryMenuByRoleId.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.respMessage);
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

//删除菜单
function deleteMenu(menuUUID,menuId){
	//todo: 是否有必要判断删除的菜单是否与职务有关联
	art.dialog({
		lock: true,
        width: '300px',
		height: '100px',
        title:'删除确认',
        content: '您确定要删除?',
        ok : function() {
			var Service = {};
			var code = 2005;
			//Service.MenuId = menuId;
			Service.MenuUUID = menuUUID;
			//param.Api = "DeleteMenu";
			var param = Commonjs.makeParam("",code,Service,"","");
			var d =Commonjs.ajaxJson("../../account/deleteMenu.do",param);
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
function checkHasUserByRole(roleId){
	if(roleId==null||roleId=="") return false;
	var Service = {};
	var code = 2005;
	Service.RoleId = roleId;
	//param.Api = "CheckHasUserByRole";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/checkHasUserByRole.do",param);
	if(d.respCode==10000){
		if(d.data>0){
			Commonjs.alert('该职务下已有用户，不能删除!');
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