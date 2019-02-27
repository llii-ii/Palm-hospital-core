function test(){
	getwid().add('4','微官网','./mainbody.do?upid=4&icon=wgw');
}
function getwid(){
	try {
		var a = window.self;
		while ((a != null && a != undefined) && a != a.parent) {
			if (a.parent === null) {
				return a.window
			}			
			var f = a.parent;
			try{
				if(f.document.domain==a.document.domain){
					a=f;
				}else{
					return a.window
				}
			}catch(b){
				return a.window
			}
		}
		return a.window
	} catch (b) {
		return window
	}
}
function openPT(ic){
	var session = YihuUtil.getSession();
	$("#username_id").html(session.operatorname);
	var operatorid = session.operatorid;
	var param = {};
	if (session.roleId==4) {
		param.Api="baseinfo.MenuApi.queryMenuByCondition";
		param.Param="{'doctorUid':'"+session.resId+"','isPanelShow':0,'orgId':"+session.orgid+",'parentId':0}";
	}else{
//		param.Api="baseinfo.MenuApi.queryMenuByLoginId";
//		param.Param="{'loginId':'"+session.operatorid+"','isPanelShow':0}";
		param.Api="baseinfo.MenuApi.queryMenuByOrgId";
		param.Param="{'orgId':"+session.orgid+",'isPanelShow':0,'parentId':0}";
	}
	doAjaxLoadData("../web_post.do", param, function(resp){
		 if(resp.isSuccess == true){
				 var result = resp.Result;
				 if(result){
					$.each(result,function(index,val){
						var icon = val.icon;
						var url = val.url;
						var name = val.name;
						var title = val.title;
						var id = val.id;
						if(icon == ic){
							getwid().add(id,name,url + '?upid='+id+'&icon='+icon);
							return;
						}
					});
				 }
  		 }else{ 
  			 alMsg(resp.Message);
  		 }
	});
}