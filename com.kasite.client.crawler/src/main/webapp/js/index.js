var sessions;
$(function() {
	sessions = Commonjs.getSession('../Hos-Process/login/getSession.do');
	console.info(sessions);
	myInfo.cName =sessions.Name;
	myInfo.sex = "未设置";
	myInfo.phone = sessions.Phone;
	myInfo.email = "未设置";
	myInfo.birthday ="未设置";
	myInfo.provinceName = "福建";
	myInfo.cityName = "福州";
	myInfo.orgName = Commonjs.hospitalName;
	myInfo.departName = sessions.DeptName;
	myInfo.dutyName = sessions.RoleName;
	$('#usernameid').html(sessions.Name);
	loadapplist();
	if($("#iframe-main-one").attr("src")==''){
		$(".iframe-menu.c-position-r").hide();
		$("#iframe-main-one").attr("src","iframe-main.html");
	}
});

function doLogout(){
	window.location.href = './login/doLogout';	 
}
function loadapplist() {
	var rootpath = Commonjs.getRoot;
	var uid = session.UserID;
	var sid = session.SessionID;
	var ul = $(".c-blocks-3");
	
	var param = {};
	var Service = {};
	var page = {};
	var code = 91014;
	var pageIndex = 0;
	var pageSize = 100;
	var page = {};
	page.PIndex = pageIndex;
	page.PSize = pageSize;
	Service.AppType = -1;
	Service.Status = 1;
	Service.AuditState = -1;
	Service.DefaultOpenState = -1;
	var params = Commonjs.getParams(code,Service,page);//获取参数
	param.Api = "QueryApply";
	param.Params = Commonjs.jsonToString(params);
	var d = Commonjs.ajax('./account/callAccountApi',param,false);
	var data = d.Data;
	if(data.length){
		$.each(data,function(i,it){
			var appurl= it.AppUrl;
			var connStr = "?";
			if(appurl.indexOf("?")>=0){
				connStr = "&"; 
			}
			
			var AppUrl =it.AppUrl+connStr+'userid='
			+ uid + '&sessionid=' + sid + '&time=' + new Date().getTime();
			var li = "<li><a title=\""+it.AppName+"\" href=\"javascript:;\" onclick=\"showApp("
				+ it.AppUID
				+ ",'"
				+ AppUrl
				+ "','"
				+ it.AppName
				+ "');\"><span class=\"im-apply-img\"><img style='width:50px;height:50px' src='"
				+ it.Icon
				+ "'></span><span class=\"im-apply-text\">"
				+ it.AppName
				+ "</span></a></li>";
			ul.append(li);
		})
	}else{
		var data = d.Data;
		var appurl= data.AppUrl;
		var connStr = "?";
		if(appurl.indexOf("?")>=0){
			connStr = "&";
		}
		var AppUrl =data.AppUrl+connStr+'userid='
		+ uid + '&sessionid=' + sid + '&time=' + new Date();
		var li = "<li><a title=\""+data.AppName+"\" href=\"javascript:;\" onclick=\"showApp("
			+ data.AppUID
			+ ",'"
			+ AppUrl
			+ "','"
			+ data.AppName
			+ "');\"><span class=\"im-apply-img\"><img style='width:50px;height:50px' src='"
			+ data.Icon
			+ "'></span><span class=\"im-apply-text\">"
			+ data.AppName
			+ "</span></a></li>";
		ul.append(li);
	}
	
	var Icon = 'http://f1.yihuimg.com/TFS/upfile/doctor_honor/2015-11-02/006613_1446443372129.jpg';
	var li = "<li><a title='应用管理' href=\"javascript:;\" onclick=\"showApp(9999,'/Hos-Process/business/sys/application-setting.html','应用管理');\"><span class=\"im-apply-img\"><img style='width:50px;height:50px' src='"
	+ Icon
	+ "'></span><span class=\"im-apply-text\">应用管理</span></a></li>";
	ul.append(li);
//	var adouBox ='<li><a href="javascript:;" onclick="addapplist();"><span class="im-apply-iconfont"><i class="iconfont">&#xe62f;</i></span><span class="im-apply-text">添加应用</span></a></li>';
//	ul.append(adouBox);
}

function showApp(AppUID, AppUrl, AppName,refalsh) {
	if($("#iframe-main-one").attr("src")==''){
	  $("#iframe-main-one").attr("src","iframe-main.html");
	}
	if (AppUrl == null || AppUrl == "") {
		return;
	}
	if(AppUID!="2179"){
		showImBox();
	}
	
	var orgId = $(".c-f20.c-fff").attr("data-orgid");
	var newURL = AppUrl;
	if (newURL.indexOf("?") > 0) {
		newURL += "&";
	} else {
		newURL += "?";
	}
	newURL += "UserID=" + myInfo.id + "&ticket=" + myInfo.ticket;
	newURL += "&OrgID=" + orgId;
//	newURL += "&userId=" + myInfo.id ;
//	newURL += "&orgId=" + orgId;	
	
	if ($("#appU" + AppUID) == null || $("#appU" + AppUID).attr("src") == null) {
		
		if (newURL.indexOf("newWindow=1") > 0) {
			window.open(newURL, AppName, "");
			return;
		}
		$(".iframe-menu.c-position-r").show();
		$("#iframe-main").children().hide();
		$("#iframe-main").append(
				"<iframe id='appU" + AppUID + "' src='" + newURL
						+ "' width='100%' height='100%'></iframe>");
		$("#tabUL").children("li").children("a.curr").removeClass("curr");
		$("#tabUL").append(
				"<li id='tabLi" + AppUID
						+ "'><a href='javascript:;' id='tabChild" + AppUID
						+ "' class='curr'><span id='tabSpan" + AppUID
						+ "' class='c-nowrap'  onclick=\"activeTab(" + AppUID
						+ ")\">" + AppName
						+ "</span>"
						+(AppUID=="2179" ? "" : "<i class='iconfont'  onclick=\"closeTab("+AppUID + ")\">&#xe605;</i>")
						+"</a></li>");
	
		$('#tabSpan' + AppUID).bind("contextmenu", function(e) {
			var left = $("#" + e.currentTarget.id).offset().left;
			var appUID = e.currentTarget.id.substring("tabSpan".length);
			$("#menuDiv").css("left", left + 20);
			$("#menuDiv").css("top", 30);
			$("#menuDiv").attr("appUID", appUID);
			$("#menuDiv").show();

			return false;

		})
		
	} else {

		activeTab(AppUID);
		if(refalsh){//刷新页面
			$("#appU" + AppUID).attr("src",newURL);
		}
	}
	iframeMenuList();
}

function activeTab(AppUID) {
	$("#iframe-main").children().hide();
	$("#tabUL").children("li").children("a.curr").removeClass("curr");
	$("#appU" + AppUID).show();
	$("#tabChild" + AppUID).addClass("curr");
}

function closeTab(AppUID) {
	if(AppUID=="2179"){
		return;
	}
	$("#iframe-main").children().hide();
	var next = $("#tabLi" + AppUID).next("li");
	var prev = $("#tabLi" + AppUID).prev("li");
	var activLi = null;
	if (next != null && next.attr("id") != null) {
		activLi = next;
	} else if (prev != null && prev.attr("id") != null) {
		activLi = prev;
	}

	$("#tabLi" + AppUID).remove();//删除li
	$("#appU" + AppUID).remove();//删除iframe
	if (activLi != null && activLi.attr("id") != null) {
		var newAppUID = activLi.attr("id").substring("tabLi".length);
		if (newAppUID != null && newAppUID != "")
			activeTab(newAppUID);//激活下一个标签页
	} else {
		$(".iframe-menu.c-position-r").hide();
		$("#iframe-main-one").show();

	}

}

//关闭本标签卡
function closeTabOne() {
	var appUID = $("#menuDiv").attr("appUID");
	if(appUID=="2179"){
		$("#menuDiv").hide();
		return;
	}
	closeTab(appUID);
	$("#menuDiv").attr("appUID", "");
	$("#menuDiv").hide();
}

//关闭其他标签
function closeTabOthers() {
	var appUID = $("#menuDiv").attr("appUID");
	var appUIDS = getAllOpenAppUID();
	for ( var i = 0; i < appUIDS.length; i++) {
		if (appUIDS[i] != appUID&&appUIDS[i] !="2179") {
			closeTab(appUIDS[i]);
		}

	}
	activeTab(appUID);
	$("#menuDiv").attr("appUID", "");
	$("#menuDiv").hide();
}

function getAllOpenAppUID() {
	var appUIDS = [];
	var y = 0;
	$("#tabUL").children("li").each(function(i, n) {
		if (n == null || n.id == null) {
			return;
		}
		var AppUID = n.id.substring("tabLi".length);
		appUIDS[y++] = AppUID;
	});
	return appUIDS;
}

//显示我的信息
function showMyInfo() {
	showIframe('myUserInfo.html');
}

function showChat(clientId, isGroup) {
	if(clientId&&clientId!=''){
		//$('#iframe-im > iframe').attr("src","");
		
		showMsgChat(clientId, isGroup);
	}
	//showMsgChat(clientId);
}
