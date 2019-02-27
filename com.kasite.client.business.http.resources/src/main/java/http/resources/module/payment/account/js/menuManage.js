var menuInfoList = undefined;
var menuInfo = undefined;
var pagenumber = 0;

$(function(){
	var param = {};
	 $("#selFatherMenu").empty();
	 var opt = '<option value="">请选择父菜单</option>'+
    '<option value="0">无</option>';
	 $("#selFatherMenu").append(opt);
	var d =Commonjs.ajaxJson("../../account/queryParentMenu.do",param);
	var list = d.data.json;
	if(!Commonjs.isNull(list)){
		for (var i = 0; i < list.length; i++) {
			var rl = '<option value="'+list[i].MENUID+'" >'+list[i].MENUNAME+'</option>';
			$("#selFatherMenu").append(rl);
		}
	}
	$('.c-btn-blue').on('click',function(){
		
		var timestamp=new Date().getTime()
		//设置对话框中的值
		$("#txtMenuID").val(timestamp);
		$("#selFatherMenu").val("");
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
			width: 450,
			padding:'0px 0px',
			title:'新增菜单',
			content: contents,
			ok: function () {
				var menuID = "";
				var pMenuID = $("#selFatherMenu").val();
				var menuName = $("#txtMenuName").val();
				var menuUrl = $("#txtMenuUrl").val();
				var menuSort = $("#txtMenuSort").val();
				var menuKey = $("#txtMenuKey").val();
				if(!pMenuID){
					Commonjs.alert('请选择父菜单');
					document.getElementById("selFatherMenu").focus();
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
				var menuID = saveOrUpdateMenu(menuID,pMenuID,menuName,menuUrl,menuSort,menuKey);
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
	pagenumber = index;
	var totalcount = $("#totalcount").val();
	var pageIndex = index-1;
	var pageSize = 10;
	Service.HosId = Commonjs.hospitalId;
	var code = 2005;
	var param = Commonjs.makeParam("",code,Service,pageIndex,pageSize);
	var d =Commonjs.ajaxJson("../../account/queryAllMenuByPage.do",param);
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
		menuInfoList = d.data;
		var str = "<thead><tr><th>菜单ID</th><th>菜单名称</th><th>链接地址</th><th>父菜单</th><th>排序</th><th class=\"w160p last\">操作</th></tr></thead><tbody>";
		var value;
		var sort;
		var pname;
		if(!menuInfoList.length){
			
			if(menuInfoList.Value==undefined){
				value="无"
			}else{
				value=menuInfoList.Value;
			}
			if(menuInfoList.Sort==undefined){
				sort="无"
			}else{
				sort=menuInfoList.Sort;
			}
			if(menuInfoList.PMenuName==undefined){
				pname="无"
			}else{
				pname=menuInfoList.PMenuName;
			}
			str += "<tr>";
			str +="<td>"+menuInfoList.MenuId+"</td>";
			str +="<td>"+menuInfoList.MenuName+"</td>";
			str +="<td>"+value+"</td>";
			str +="<td>"+pname+"</td>";
			str +="<td>"+sort+"</td>";
			str += "<td><a href=\"javascript:updateMenuInfo('"+menuInfoList.MenuId+"');\" class=\"alinks-unline alinks-blue d-detail-edit\">编辑</a>";
			str += "&nbsp;&nbsp;&nbsp;<a href=\"javascript:deleteMenu('"+menuInfoList.MenuId+"');\" class=\"alinks-unline alinks-blue\">删除</a></td>";
			str +="</tr>";
		}else{
			for(var i=0;i<menuInfoList.length;i++){
				var obj = menuInfoList[i];
			
				if(obj.Value==undefined){
					value="无"
				}else{
					value=obj.Value;
				}
				if(obj.Sort==undefined){
					sort="无"
				}else{
					sort=obj.Sort;
				}
				if(obj.PMenuName==undefined){
					pname="无"
				}else{
					pname=obj.PMenuName;
				}
				str += "<tr>";
				str +="<td>"+obj.MenuId+"</td>";
				str +="<td>"+obj.MenuName+"</td>";
				str +="<td>"+value+"</td>";
				str +="<td>"+pname+"</td>";
				str +="<td>"+sort+"</td>";
				str += "<td><a href=\"javascript:updateMenuInfo('"+obj.MenuId+"');\" class=\"alinks-unline alinks-blue d-detail-edit\">编辑</a>";
				str += "&nbsp;&nbsp;&nbsp;<a href=\"javascript:deleteMenu('"+obj.MenuId+"');\" class=\"alinks-unline alinks-blue\">删除</a></td>";
				str +="</tr>";
			}
		}
		$("#menuList").html(str);
	}else{
		//异常提示
		PageModel(0,pageSize,'pager');
		Commonjs.alert(d.respMessage);
	}
}

function updateMenuInfo(menuID){
	var d = undefined;
	if(!menuInfoList.length){
		d = menuInfoList;
	}else{
		for(var i=0;i<menuInfoList.length;i++){
			var obj = menuInfoList[i];
			var mId = obj.MenuId;
			if(mId==menuID){
				d = obj;
				break;
			}
		}
	}
	//设置对话框中的值
	$("#txtMenuID").val(d.MenuId);
	$("#selFatherMenu").val(d.PMenuId);
	$("#txtMenuName").val(d.MenuName);
	$("#txtMenuKey").val(d.Key);
	$("#txtMenuUrl").val(d.Value);
	$("#txtMenuSort").val(d.Sort);
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
				var pMenuID = $("#selFatherMenu").val();
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
					Commonjs.alert('请选择父菜单');
					document.getElementById("selFatherMenu").focus();
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
				var menuID = saveOrUpdateMenu(menuID,pMenuID,menuName,menuUrl,menuSort,menuKey);
				loadTb(1);
			},
			cancel: true
		});	
		$('.select-office-box').hide();
}

function saveOrUpdateMenu(menuID,pMenuID,menuName,menuUrl,menuSort,menuKey){
	var Service = {};
	var code = 2005;
	Service.MenuID = menuID;
	Service.PMenuID = pMenuID;
	Service.MenuName = menuName;
	Service.MenuUrl = menuUrl.replace(new RegExp(/(&)/g),'&amp;');
	Service.MenuSort= menuSort;
	Service.MenuKey= menuKey;
	Service.HosId = Commonjs.hospitalId;
	//param.Api = "SaveOrUpdateMenu";
	var param = Commonjs.makeParam("",code,Service,"","");
	var d =Commonjs.ajaxJson("../../account/saveOrUpdateMenu.do",param);
	if(d.respCode!=10000){
		//异常提示
		Commonjs.alert(d.RespMessage);
	}else{
		Commonjs.alert("操作成功");
		var param = {};
		 $("#selFatherMenu").empty();
		 var opt = '<option value="">请选择父菜单</option>'+
        '<option value="0">无</option>';
		 $("#selFatherMenu").append(opt);
		var d =Commonjs.ajaxJson("../../account/queryParentMenu.do",param);
		var list = d.data.json;
		if(!Commonjs.isNull(list)){
			for (var i = 0; i < list.length; i++) {
				var rl = '<option value="'+list[i].MENUID+'" >'+list[i].MENUNAME+'</option>';
				$("#selFatherMenu").append(rl);
			}
		}
	}
	return d.data.menuID;
}
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
			var Service = {};
			var code = 2005;
			Service.MenuID = menuId;
			//Service.MenuUUID = menuUUID;
			//param.Api = "DeleteMenu";
			var param = Commonjs.makeParam("",code,Service,"","");
			var d =Commonjs.ajaxJson("../../account/deleteMenu.do",param);
			if(d.respCode!=10000){
				//异常提示
				Commonjs.alert(d.respMessage);
				return;
			}
			var status = d.data.status;
			if(status==0){
				Commonjs.alert("删除成功！","add");
				loadTb(1);
			}else if(status==-1){
				Commonjs.alert("此菜单绑定了职务，不可删除！");
			}else if(status==-2){
				Commonjs.alert("此菜单底下有子菜单，不可删除！");
			}
			
        },
        cancel : true
    }); 
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