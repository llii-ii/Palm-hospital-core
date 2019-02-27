var menuInfoList = undefined;
var menuInfoList = undefined;
var menuInfo = undefined;
$(function(){
	$('.office-box-list').height(250).jScrollPane({"autoReinitialise": true,"overflow":true});
	//加载菜单列表
	loadTb(1);
	
	//新增菜单
	$('.office-btns').on('click',function(){
		//设置对话框中的值
		$("#txtFatherMenuID").val("");
		$("#txtMenuName").val("");
		$("#txtMenuUrl").val("");
		$("#txtMenuSort").val("");
		$("#txtMenuPerms").val("");
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
				var pMenuID = $("#txtFatherMenuID").val();
				var menuName = $("#txtMenuName").val();
				var menuUrl = $("#txtMenuUrl").val();
				var menuSort = $("#txtMenuSort").val();
				var menuType = $("#menuType").val();
				var menuPerms = $("#txtMenuPerms").val();
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
				if(!menuType){
					Commonjs.alert('菜单类型不能为空');
					document.getElementById("menuTypeName").focus();
					return false;
				}
				if(menuType==2 && !menuPerms){
					Commonjs.alert('菜单类型为权限时，权限KEY不能为空');
					document.getElementById("txtMenuPerms").focus();
					return false;
				}
				if(menuType==1 && !menuUrl){
					Commonjs.alert('菜单类型为菜单时，Url不能为空');
					document.getElementById("txtMenuUrl").focus();
					return false;
				}
				var param = {};
				param.reqParam = Commonjs.getReqParams({'ParentId':pMenuID,'Name':menuName,'Url':menuUrl,'OrderNum':menuSort,'Type':menuType,Perms:menuPerms});
				Commonjs.ajax('/sys/addMenu.do',param,function(d){
					if(d.RespCode!=10000){
						//异常提示
						Commonjs.alert(d.RespMessage);
					}else{
						loadTb(1);
						artBox.close();
					}
				})
				return false;	
			},
			cancel: true
		});	
	})
})

//加载菜单列表
function loadTb(index){
	var param = {};
	$('#pagenumber').val(index);
	var pageIndex = index-1;
	var pageSize = 10;
	param.reqParam = Commonjs.getReqParams({'Page':{'PIndex':pageIndex,'PSize':pageSize}});
	Commonjs.ajax("/sys/queryMenuList.do",param,function(d){
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
			menuInfoList = d.Data;
			var str = "<tr><th>菜单ID</th><th>菜单名称</th><th>菜单类型</th><th>链接地址</th><th>父菜单ID</th><th>排序</th><th class=\"w160p last\">操作</th></tr>";
			for(var i=0;i<menuInfoList.length;i++){
				var obj = menuInfoList[i];
				str += "<tr>";
				str +="<td>"+obj.MenuId+"</td>";
				str +="<td>"+obj.Name+"</td>";
				if(obj.Type==0){
					str +="<td>目录</td>";
				}else if(obj.Type==1){
					str +="<td>菜单</td>";
				}else if(obj.Type==2){
					str +="<td>权限</td>";
				}
				str +="<td>"+obj.Url+"</td>";
				str +="<td>"+obj.ParentId+"</td>";
				str +="<td>"+obj.OrderNum+"</td>";
				str +="<td><ul class=\"i-btn-list\"><li><a href=\"javascript:updateMenuInfo('"+obj.MenuId+"');\"  class=\"i-btn\"><i class=\"i-edit\"></i>编辑</a></li><li><a href=\"javascript:deleteMenu('"+obj.MenuId+"');\" class=\"i-btn\"><i class=\"i-del\"></i>删除</a></li></ul></td>";
				str +="</tr>";
			}
			$("#tb").html(str);
		}else{
			//异常提示
			PageModel(0,pageSize,'pager');
			Commonjs.alert(d.RespMessage);
		}
	})
}
//修改菜单
function updateMenuInfo(menuId){
	var d = undefined;
	if(!menuInfoList.length){
		d = menuInfoList;
	}else{
		for(var i=0;i<menuInfoList.length;i++){
			var obj = menuInfoList[i];
			var mId = obj.MenuId;
			if(mId==menuId){
				d = obj;
				break;
			}
		}
	}
	//设置对话框中的值
	$("#txtFatherMenuID").val(d.ParentId);
	$("#txtMenuName").val(d.Name);
	$("#txtMenuUrl").val(d.Url);
	$("#txtMenuSort").val(d.OrderNum);
	$("#menuType").val(d.Type);
	if(d.Type==0){
		$("#menuTypeName").html("目录");
		$("#tr_menuPerms").hide();
		$("#tr_menuUrl").hide();
	}else if(d.Type==1){
		$("#menuTypeName").html("菜单");
		$("#tr_menuPerms").hide();
		$("#tr_menuUrl").show();
	}else if(d.Type==2){
		$("#menuTypeName").html("权限");
		$("#txtMenuPerms").val(d.Perms);
		$("#tr_menuPerms").show();
		$("#tr_menuUrl").hide();
	}
	
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
				var pMenuID = $("#txtFatherMenuID").val();
				var menuName = $("#txtMenuName").val();
				var menuUrl = $("#txtMenuUrl").val();
				var menuSort = $("#txtMenuSort").val();
				var menuType = $("#menuType").val();
				var menuPerms = $("#txtMenuPerms").val();
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
				if(!menuType){
					Commonjs.alert('菜单类型不能为空');
					document.getElementById("menuTypeName").focus();
					return false;
				}
				if(menuType==2 && !menuPerms){
					Commonjs.alert('菜单类型为权限时，权限KEY不能为空');
					document.getElementById("txtMenuPerms").focus();
					return false;
				}
				if(menuType==1 && !menuUrl){
					Commonjs.alert('菜单类型为菜单时，Url不能为空');
					document.getElementById("txtMenuUrl").focus();
					return false;
				}
				var param = {};
				param.reqParam = Commonjs.getReqParams({'MenuId':d.MenuId,'ParentId':pMenuID,'Name':menuName,'Url':menuUrl,'OrderNum':menuSort,'Type':menuType,'Perms':menuPerms});
				Commonjs.ajax('/sys/updateMenu.do',param,function(d){
					if(d.RespCode!=10000){
						//异常提示
						Commonjs.alert(d.RespMessage);
					}else{
						loadTb(1);
						artBox.close();
					}
				})
				return false;
			},
			cancel: true
		});	
		$('.select-office-box').hide();
}

//function saveOrUpdateMenu(menuID,pMenuID,menuName,menuUrl,menuSort,menuKey,menuUUID){
//	var Service = {};
//	var code = 2005;
//	Service.MenuID = menuID;
//	Service.PMenuID = pMenuID;
//	Service.MenuName = menuName;
//	Service.MenuUrl = menuUrl.replace(new RegExp(/(&)/g),'&amp;');
//	Service.MenuSort= menuSort;
//	Service.MenuKey= menuKey;
//	if( menuUUID!= null && menuUUID!=undefined ){
//		Service.MenuUUID = menuUUID;
//	}
//	Service.HosId = Commonjs.hospitalId;
//	//param.Api = "SaveOrUpdateMenu";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/saveOrUpdateMenu.do",param);
//	if(d.respCode!=10000){
//		//异常提示
//		Commonjs.alert(d.RespMessage);
//	}
//	return d.data.menuID;
//}
//function queryAllMenu(){
//	var Service = {};
//	var code = 2005;
//	var param = {};
//	Service.HosId = Commonjs.hospitalId;
//	//param.Api = "QueryAllMenu";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/queryAllMenu.do",param);
//	if(d.respCode==10000){
//		menuInfoList = d.data;
//	}
//}
//function getMenuTbHtml(){
//	queryAllMenu();
//	var str = "";
//	if(!menuInfoList.length){
//		str +="<tr>";
//		str +="<td class=\"w\">"+menuInfoList.MenuName+"</td>";
//		str +="<td>";
//		str +="<input type=\"checkbox\" value=\"1\"  name=\""+menuInfoList.Key+"\" /><label for=\"action\" >编辑</label>";
//		//str +="<input type=\"checkbox\" value=\"2\"  name=\""+menuInfoList.Key+"\" /><label for=\"action\" >查看</label>";
//		str +="</td>"
//		str +="</tr>";
//	}else{
//		for(var i=0;i<menuInfoList.length;i++){
//			var obj = menuInfoList[i];
//			str +="<tr>";
//			str +="<td class=\"w\">"+obj.MenuName+"</td>";
//			str +="<td>";
//			str +="<input type=\"checkbox\" value=\"1\" name=\""+obj.Key+"\"/><label for=\"action\" class=\"\">编辑</label>";
//			//str +="<input type=\"checkbox\" value=\"2\" name=\""+obj.Key+"\"/><label for=\"action\" class=\"\">查看</label>";
//			str +="</td>"
//			str +="</tr>";
//		}
//	}
//	return str;
//}
//function saveRoleMenu(menuId,roleId,state){
//	if(state<=0) return;
//	var Service = {};
//	var code = 2005;
//	Service.MenuId = menuId;
//	Service.RoleId = roleId;
//	Service.State = state;
//	//param.Api = "SaveRoleMenu";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/saveRoleMenu.do",param);
//	if(d.respCode!=10000){
//		//异常提示
//		Commonjs.alert(d.espMessage);
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
//function queryMenuByRoleId(roleId){
//	var Service = {};
//	var code = 2005;
//	Service.RoleId = roleId;
//	//param.Api = "QueryMenuByRoleId";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/queryMenuByRoleId.do",param);
//	if(d.respCode!=10000){
//		//异常提示
//		Commonjs.alert(d.respMessage);
//	}
//	menuInfo = d.data;
//}
//
//function selectMenu(key,state){
//	var menuList = $("input[name='"+key+"']");
//	for(var i=0 ;i<menuList.length;i++){
//		var menu = menuList[i];
//		if(state==(i+1) || state==3){
//			menu.checked=true;
//		}
//	}
//}
//
//function deleteRoleMenuByRoleId(roleId){
//	var Service = {};
//	var code = 2005;
//	Service.RoleId = roleId;
//	//param.Api = "DeleteRoleMenuByRoleId";
//	var param = Commonjs.makeParam("",code,Service,"","");
//	var d =Commonjs.ajaxJson("../../account/deleteRoleMenuByRoleId.do",param);
//	if(d.respCode!=10000){
//		//异常提示
//		Commonjs.alert(d.respMessage);
//	}
//}

//删除菜单
function deleteMenu(menuId){
	//todo: 是否有必要判断删除的菜单是否与职务有关联
	art.dialog({
		lock: true,
        width: '300px',
		height: '100px',
        title:'删除确认',
        content: '您确定要删除?',
        ok : function() {
			var param = {};
			param.reqParam = Commonjs.getReqParams({'MenuId':menuId});
			Commonjs.ajax("/sys/delMenu.do",param,function(d){
				if(d.RespCode!=10000){
					//异常提示
					Commonjs.alert(d.RespMessage);
					return;
				}
				Commonjs.alert("删除成功！","add");
			});
			loadTb(1);
        },
        cancel : true
    }); 
}
function setSelectOffice(val,name){
	$("#menuType").val(val);
	$("#menuTypeName").html(name);
	if(val==0){
		$("#tr_menuPerms").hide();
		$("#tr_menuUrl").hide();
	}else if(val==1){
		$("#tr_menuPerms").hide();
		$("#tr_menuUrl").show();
	}else if(val==2){
		$("#tr_menuPerms").show();
		$("#tr_menuUrl").hide();
	}
	$(".select-office").next('div').hide();
}
function showOrHide(obj){
	var a = $(obj).next('div');
	var id = a.attr('id');
	var idx = id.substring(id.length-1,id.length);
	
	var html = "<ul>";
	html +="<li><a onclick=setSelectOffice(0,'目录')>目录</a></li>";
	html +="<li><a onclick=setSelectOffice(1,'菜单')>菜单</a></li>";
	html +="<li><a onclick=setSelectOffice(2,'权限')>权限</a></li>";
	html +="</ul>"
	if(a.css('display')=="none"){
		a.show();
		var api = a.jScrollPane({"autoReinitialise": true}).data('jsp');
		api.getContentPane().html(html);
		api.reinitialise();
	}else{
		a.hide();
	}
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
//		if(d.data>0){
//			Commonjs.alert('该职务下已有用户，不能删除!');
//			return false;
//		}else if(d.Data==0){
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