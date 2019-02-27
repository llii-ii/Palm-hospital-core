

var roleMsg1 = undefined;
var roleMsg2 = undefined;
var roleInfoList = undefined;
var roleMenuBtnList = undefined;
var currentMenuId ='4.1';//当前界面对应的menuid
$(function(){
	queryAllRole();
	var str2 = getSelectOfficeBoxHtml(2);
	roleMsg2 = str2;
	var str1 = getSelectOfficeBoxHtml(1);
	roleMsg1 = str1;
	if(!roleInfoList.length){
		$("#roleId").attr("value",roleInfoList.RoleId);
		$("#selectOffice").html(roleInfoList.RoleName);
		loadMenuByRoleId(roleInfoList.RoleId,roleInfoList.RoleName);
	}else{
		$("#roleId").attr("value",roleInfoList[0].RoleId);
		$("#selectOffice").html(roleInfoList[0].RoleName);
		loadMenuByRoleId(roleInfoList[0].RoleId,roleInfoList[0].RoleName);
	}
	
	$('.office-btns').on('click',function(){
		if(!$("#divMenuBtnTitle").attr("roleId")){
			return ;
		}
		//设置对话框中的值
		$("#txtBtnName").val("");
		$("#txtBtnType").val("");
		$("#txtBtnAPI").val("");
		$("#txtBtnAction").val("");
		//加载菜单选项
		var contents=$('.office-box').get(0);
		var artBox=art.dialog({
			lock: true,
			artIcon:'add',
			opacity:0.4,
			width: 550,
			padding:'0px 0px',
			title:'新增功能',
			content: contents,
			ok: function () {
				var btnName = $("#txtBtnName").val();
				var btnType = $("#txtBtnType").val();
				var btnAPI = $("#txtBtnAPI").val();
				var btnAction = $("#txtBtnAction").val();
				if( !btnName ){
					Commonjs.alert('按钮名称不能为空');
					document.getElementById("txtBtnName").focus();
					return false;
				}
				if( !btnType ){
					Commonjs.alert('按钮类型不能为空');
					document.getElementById("txtBtnType").focus();
					return false;
				}
				//保存菜单
				var roleMenuBtnObje ={};
				roleMenuBtnObje.btnName = btnName;
				roleMenuBtnObje.btnType = btnType;
				roleMenuBtnObje.menuName = menuName;
				roleMenuBtnObje.API = btnAPI;
				roleMenuBtnObje.action = btnAction;
				roleMenuBtnObje.roleId = $("#divMenuBtnTitle").attr("roleId");
				roleMenuBtnObje.menuId = $("#divMenuBtnTitle").attr("menuId");
				roleMenuBtnObje.menuName = $("#divMenuBtnTitle").attr("menuName");
				saveOrUpdateRoleMenuBtn(roleMenuBtnObje);
			},
			cancel: true
		});	
		return false;	
	});
	
	Commonjs.btnPermissionControl(currentMenuId);
});


function showorhideRole(obj){
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

//查询所有职务角色
function queryAllRole(){
	var Service = {};
	var pageIndex = 0;
	var pageSize = 100;
	var page = {};
	page.PIndex = pageIndex;
	page.PSize = pageSize;
	var code = 2005;
	var param = {};
	var params = Commonjs.getParams(code,Service,page);//获取参数
	param.Api = "QueryAllRole";
	param.Params = Commonjs.jsonToString(params);
	var d = Commonjs.ajax('../account_callAccountApi.do',param,false);
	if(d.RespCode==10000){
		roleInfoList = d.Data;
	}
}

//生成职务选择框下拉数据
function getSelectOfficeBoxHtml(index){
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
	//str +="<p><a href=\"javascript:;\" onclick=\"addRole("+index+");\" class=\"office-btns\"><i class=\"icon icon-office-add\"></i>添加职务</a></p>";
	return str;
}

// 设置选中值
function setSelectOffice(index,roleId,roleName){
	$("#roleId").attr("value",roleId);
	$("#selectOffice").html(decodeURI(decodeURI(roleName)));
	$('#selectOfficeBox').hide();
	loadMenuByRoleId(roleId,decodeURI(decodeURI(roleName)));
}

//根据选择的职务角色，加载菜单
function loadMenuByRoleId(roleId,roleName){
	
	var Service = {};
	var code = 2005;
	var param = {};
	var params = Commonjs.getParams(code,Service);//获取参数
	Service.RoleId = roleId;
	param.Api = "QueryMenuByRoleId";
	param.Params = Commonjs.jsonToString(params);
	var d = Commonjs.ajax('../account_callAccountApi.do',param,false);
	if(d.RespCode=10000){
		var menuInfo = d.Data;
		
		var str = "<tr></th><th>菜单ID</th><th>菜单名称</th></tr>";
		if(!menuInfo.length){
			str += "<tr onclick=\"showRoleMenuButton('"+roleId+"','"+roleName+"','"+menuInfo.MenuId+"','"+menuInfo.MenuName+"')\">";
			str +="<td>"+menuInfo.MenuId+"</td>";
			str +="<td>"+menuInfo.MenuName+"</td>";
			str +="</tr>";
		}else{
			for(var i=0;i<menuInfo.length;i++){
				var obj = menuInfo[i];
				str += "<tr onclick=\"showRoleMenuButton('"+roleId+"','"+roleName+"','"+obj.MenuId+"','"+obj.MenuName+"')\">";
				str +="<td>"+obj.MenuId+"</td>";
				str +="<td>"+obj.MenuName+"</td>";
				str +="</tr>";
			}
		}
		$("#tb").html(str);
	}else{
		Commonjs.alert(d.RespMessage);
	}
}

//根据角色ID查询功能	权限
function showRoleMenuButton(roleId,roleName,menuId,menuName){
	$("#divMenuBtnTitle").html("菜单功能："+roleName+"->"+menuName);
	$("#divMenuBtnTitle").attr("roleId",roleId);
	$("#divMenuBtnTitle").attr("menuId",menuId);
	$("#divMenuBtnTitle").attr("menuName",menuName);
	loadButtonTab(roleId,menuId);
}

//保存功能按钮信息
function saveOrUpdateRoleMenuBtn(RoleMenuBtnObj){
	var Service = {};
	var page = {};
	var code = 2005;
	Service.MenuID = RoleMenuBtnObj.menuId;
	Service.RoleId = RoleMenuBtnObj.roleId;
	Service.MenuName = RoleMenuBtnObj.menuName;
	Service.ButtonType = RoleMenuBtnObj.btnType;
	Service.ButtonName = RoleMenuBtnObj.btnName;
	Service.APIPermission = RoleMenuBtnObj.API;
	Service.ActionPermission = RoleMenuBtnObj.action;
	if(RoleMenuBtnObj.roleMenuButtonId){
		Service.RoleMenuButtonId = RoleMenuBtnObj.roleMenuButtonId ;
	}
	var param = {};
	var params = Commonjs.getParams(code,Service);//获取参数
	param.Api = "SaveOrUpdateRoleMenuButton";
	param.Params = Commonjs.jsonToString(params);
	//调用API
	var d = Commonjs.ajax('../account_callAccountApi.do',param,false);
	if(d.RespCode!=10000){
		//异常提示
		Commonjs.alert(d.RespMessage);
	}
	loadButtonTab(RoleMenuBtnObj.roleId,RoleMenuBtnObj.menuId);
}

function loadButtonTab(roleId,menuId){
	var Service = {};
	Service.RoleId = roleId;
	Service.MenuId = menuId;
	code=2005;
	var param = {};
	var params = Commonjs.getParams(code,Service);//获取参数
	param.Api = "QueryRoleMenuButton";
	param.Params = Commonjs.jsonToString(params);
	var d = Commonjs.ajax('../account_callAccountApi.do',param,false);
	if(d.RespCode=10000){
		roleMenuBtnList = d.Data;
		var str = "<tr><th>按钮名称</th><th>操作类型</th><th>调用API</th><th>调用Action</th><th>状态</th><th>操作</th></tr>";
		if(roleMenuBtnList.length == null){
			str += "<tr>";
			str +="<td>"+roleMenuBtnList.ButtonName+"</td>";
			str +="<td>"+roleMenuBtnList.ButtonType+"</td>";
			str +="<td>"+roleMenuBtnList.ApiPermission+"</td>";
			str +="<td>"+roleMenuBtnList.ActionPermission+"</td>";
			//str +="<td>"+roleMenuBtnList.State+"</td>";
			if(roleMenuBtnList.State == 0){
				str += "<td id='"+roleMenuBtnList.RoleMenuButtonId+"'><div class='my-switch-box red' onclick='updateState(\""+roleMenuBtnList.RoleMenuButtonId+"\","+roleMenuBtnList.State+")' btnPermission='UBTNSTATE'>";
			}else{
				str += "<td id='"+roleMenuBtnList.RoleMenuButtonId+"'><div class='my-switch-box' onclick='updateState(\""+roleMenuBtnList.RoleMenuButtonId+"\","+roleMenuBtnList.State+")' btnPermission='UBTNSTATE'>";
			}
			str += '<span></span></div>'+getStateCN(roleMenuBtnList.State)+'</td>';
			str +="<td><ul class=\"i-btn-list\"><li style=\"padding: 0px 5px;\"><a href=\"javascript:updateRoleMenuBtn('"+(-1)+"');\" class=\"i-btn\"><i class=\"i-edit\" btnPermission='UPDATEBTN'></i>编辑</a>" +
			"</li><li style=\"padding: 0px 5px;\"><a href=\"javascript:deleteRoleMenuButton('"+roleMenuBtnList.RoleMenuButtonId+"');\" class=\"i-btn\"><i class=\"i-del\" btnPermission='DELETEBTN'></i>删除</a></li></ul></td>";
			str +="</tr>";
		}else{
			for(var i=0;i<roleMenuBtnList.length;i++){
				var obj = roleMenuBtnList[i];
				str += "<tr>";
				str +="<td>"+obj.ButtonName+"</td>";
				str +="<td>"+obj.ButtonType+"</td>";
				str +="<td>"+obj.ApiPermission+"</td>";
				str +="<td>"+obj.ActionPermission+"</td>";
				//str +="<td>"+obj.State+"</td>";
				if(obj.State == 0){
					str += "<td id='"+obj.RoleMenuButtonId+"'><div class='my-switch-box red' onclick='updateState(\""+obj.RoleMenuButtonId+"\","+obj.State+")' btnPermission='UBTNSTATE' >";
				}else{
					str += "<td id='"+obj.RoleMenuButtonId+"'><div class='my-switch-box'  onclick='updateState(\""+obj.RoleMenuButtonId+"\","+obj.State+")' btnPermission='UBTNSTATE'>";
				}
				str += '<span></span></div>'+getStateCN(obj.State)+'</td>';
				str +="<td><ul class=\"i-btn-list\"><li style=\"padding: 0px 5px;\"><a href=\"javascript:void(0);\" onclick=\"updateRoleMenuBtn('"+i+"');\" class=\"i-btn\" btnPermission='UPDATEBTN'><i class=\"i-edit\"></i>编辑</a></li>" +
				"<li style=\"padding: 0px 5px;\"><a href=\"javascript:deleteRoleMenuButton('"+obj.RoleMenuButtonId+"');\" class=\"i-btn\"><i class=\"i-del\" btnPermission='DELETEBTN'></i>删除</a></li></ul></td>";
				str +="</tr>";
			}
		}
		$("#tabRoleBtn").html(str);
	}else{
		Commonjs.alert(d.RespMessage);
	}
	Commonjs.btnPermissionControl(currentMenuId);
}

function deleteRoleMenuButton(roleMenuButtonId){
	art.dialog({
		lock: true,
        width: '300px',
		height: '100px',
        title:'删除确认',
        content: '您确定要删除?',
        ok : function() {
			var Service = {};
			var page = {};
			var code = 2005;
			Service.RoleMenuButtonId = roleMenuButtonId;
			var param = {};
			var params = Commonjs.getParams(code,Service);//获取参数
			param.Api = "DeleteRoleMenuButton";
			param.Params = Commonjs.jsonToString(params);
			//调用API
			var d = Commonjs.ajax('../account_callAccountApi.do',param,false);
			if(d.RespCode!=10000){
				//异常提示
				Commonjs.alert(d.RespMessage);
			}
			Commonjs.alert("删除成功！","add");
			loadButtonTab($("#divMenuBtnTitle").attr("roleId"),$("#divMenuBtnTitle").attr("menuId"));
        },
        cancel : true
    }); 
}

//更新操作
function updateRoleMenuBtn(index){
	var btnObj = null;
	if( index <0 ){
		btnObj = roleMenuBtnList;
	}else{
		btnObj = roleMenuBtnList[index];
	}
	if( btnObj == null ){
		return;
	}
	//设置对话框中的值
	$("#txtBtnName").val(btnObj.ButtonName);
	$("#txtBtnType").val(btnObj.ButtonType);
	$("#txtBtnAPI").val(btnObj.ApiPermission);
	$("#txtBtnAction").val(btnObj.ActionPermission);
	//加载菜单选项
	var contents=$('.office-box').get(0);
	var artBox=art.dialog({
		lock: true,
		artIcon:'add',
		opacity:0.4,
		width: 550,
		padding:'0px 0px',
		title:'修改功能',
		content: contents,
		ok: function () {
			var btnName = $("#txtBtnName").val();
			var btnType = $("#txtBtnType").val();
			var btnAPI = $("#txtBtnAPI").val();
			var btnAction = $("#txtBtnAction").val();
			if( !btnName ){
				Commonjs.alert('按钮名称不能为空');
				document.getElementById("txtBtnName").focus();
				return false;
			}
			if( !btnType ){
				Commonjs.alert('按钮类型不能为空');
				document.getElementById("txtBtnType").focus();
				return false;
			}
			//保存菜单
			var roleMenuBtnObje ={};
			roleMenuBtnObje.roleMenuButtonId = btnObj.RoleMenuButtonId;
			roleMenuBtnObje.btnName = btnName;
			roleMenuBtnObje.btnType = btnType;
			roleMenuBtnObje.menuName = menuName;
			roleMenuBtnObje.API = btnAPI;
			roleMenuBtnObje.action = btnAction;
			roleMenuBtnObje.roleId = $("#divMenuBtnTitle").attr("roleId");
			roleMenuBtnObje.menuId = $("#divMenuBtnTitle").attr("menuId");
			roleMenuBtnObje.menuName = $("#divMenuBtnTitle").attr("menuName");
			saveOrUpdateRoleMenuBtn(roleMenuBtnObje);
		},
		cancel: true
	});	
}

//状态转换
function getStateCN(state){
	switch(parseInt(state)){
		case 0:return '未启用';
		case 1:return '已启用';
		default:return '';
	}
}

function updateState(roleMenuBtnId,state){
	var param = {};
	var Service = {};
	var code = 2005;
	Service.RoleMenuButtonId = roleMenuBtnId;
	Service.State = state;
					
	var params = Commonjs.getParams(code,Service);//获取参数
	param.Api = "UpdateRoleMenuButtonState";
	param.Params = Commonjs.jsonToString(params);
	var d = Commonjs.ajax('./account_callAccountApi.do',param,false);
	Commonjs.alert(d.RespMessage);
	if(d.RespCode == 10000){
		var html = '';
		if(state == 1){
	 		html = "<div class='my-switch-box red' onclick='updateState(\""+roleMenuBtnId+"\","+state+")'>";
		}else{
			html = "<div class='my-switch-box'  onclick='updateState(\""+roleMenuBtnId+"\","+state+")'>";
		}
		html += '<span></span></div>'+getStateCN(state)+'';
		$('#'+roleMenuBtnId).empty().html(html);
	}
}