
// 	var pmenus = new Array();
 	var perms = new Array();
 	
    $(function(){
    	//日期控件初始化
		$('#datetimepicker').datetimepicker({
			yearOffset:0,
			lang:'ch',
			timepicker:false,
			format:'d/m/Y',
			formatDate:'Y/m/d',
			minDate:'-1970/01/02', // yesterday is minimum date
			maxDate:'+1970/01/02' // and tommorow is maximum date calendar
		});
		
//        $('#layer').click(function(){
//			var artBox=art.dialog({
//				lock: true,
//				icon:'question',
//				opacity:0.4,
//				width: 250,
//				title:'提示',
//				content: '页面模板会覆盖编辑区域已有组件，是否继续？',
//				ok: function () {
//					
//				},
//				cancel: true
//			});			
//		});
        //加载菜单
		getMenuList();
		setTimeout(function(){
			var sessions = Commonjs.getSession();
			var userName = sessions.realName;
			if(Commonjs.isNull(userName)){
				userName = sessions.roleName;
			}
            $('#usernameid').html(', '+userName);
            
            $('.a-menu-list li').on('click',function(){
            	 $('.a-menu-list li').removeClass('current');
            	 $(this).addClass('current');
            });
            
		},1000);
//		linkMenu('../business/homepage/home_page.html');
    })
 
 	//获取菜单列表
 	function getMenuList(){
 		var Service = {};
		var page = {};
		var code = 2006;
		Service.UserName = Commonjs.getUserName();
		var param = {};
		var params = Commonjs.getParams(code,Service);//获取参数
		param.params = Commonjs.jsonToString(params);
		Commonjs.ajax('/backstage/getMenuList.do',param,function(d){
			if(d.RespCode==10000){
				perms = d.permissions;
				initMenuList(d.menuList);
			}else{
				Commonjs.alert(d.RespMessage);
			}
		});
		
 	}
    //是否拥有某个权限
 	function hasPerms(obj,perm){
 		var hasPerm = false;
 		if(!Commonjs.isEmpty(obj)){
 			perm = obj.attr("perm");
 			if(!Commonjs.isEmpty(perm) && perms[perm]==1){
 				hasPerm = true;
 			}
 			if(hasPerm){
 				$(obj).show();
 			}else{
 				$(obj).hide();
 			}
 		}else{
 			if(!Commonjs.isEmpty(perm) && perms[perm]==1){
 				hasPerm = true;
 			}
 		}
 		return hasPerm;
 	}
// 	function getPmenus(d){
// 		$.each(d,function(i,it){
// 			if(pmenus.length == 0){
// 				pmenus.push(it.PName);
// 			}else{
// 				if(!isPnameExist(it.PName)){
// 					pmenus.push(it.PName);
// 				}
// 			}
// 		})
// 	}
// 	function isPnameExist(name){
// 	
// 		for(var i = 0; i < pmenus.length; i++){
// 			if(name == pmenus[i]){
// 				return true;
// 			}
// 		}
// 		return false;
// 	}
 	
 	function getMenuHtml(menuList,token){
 		var html = '';
 		$.each(menuList,function(i,menu){
 				if(menu.type==0){
 					//目录
 					html += '<div class="a-menu"><span class="arrow arrowL"></span><div class="a-menu-tit">';
	 				html += '<i class="'+menu.icon+'"></i>'+menu.name+'</div>';
	 				html += '<div class="a-menu-list"><ul>';
	 				html += getMenuHtml(menu.list);
	 				html += ' </ul></div></div><div class="a-divider"></div>';
				}else if(menu.type==1){
					//菜单
					if(!Commonjs.isEmpty(menu.url)){
 						var url = menu.url;
 	 					url = url.replace("{token}",token); 
 	 					html += '<li><a href="javascript:;" onclick="linkMenu(\''+url+'\',\''+menu.name+'\')">'+menu.name+'</a></li>';
 					}
				}
	 		});
 		return html;
 	}
 	
 	function initMenuList(menuList){
 		var token = Commonjs.getCookie("token");
 		if(!Commonjs.isEmpty(menuList) && menuList.length>0){
 			//遍历菜单
 			var html = getMenuHtml(menuList,token);
 			$('#aMenus').empty().append(html);
 		}else{
 			$('#aMenus').empty().append('&nbsp;&nbsp;&nbsp;<font color=red>没有可操作的菜单!<br/><br/>&nbsp;&nbsp;&nbsp;请联系管理员开放菜单权限!</font>');
 		}
 	}
 	
// 	function initMenu(d){
// 		if(d.length!=undefined && pmenus[0]!="") {
// 			var html = '';
//	 		var size = d.length;
//			for(var i = 0; i < pmenus.length; i++){
//				if(pmenus[i]=='BAT管理'||pmenus[i]=='用户管理'||pmenus[i]=='通知中心'||pmenus[i]=='报表中心'||pmenus[i]=='基础信息管理') {
//					continue;
//				}
//				html += '<div class="a-menu"><span class="arrow arrowL"></span><div class="a-menu-tit">';
//				html += '<i class="'+getIconByPName(pmenus[i])+'"></i>'+pmenus[i]+'</div>';
//				html += '<div class="a-menu-list"><ul>';
//				for(j = 0 ;j < size ; j++){
//					if(d[j].PName == pmenus[i]){
//						html += '<li><a href="javascript:;" onclick="linkMenu(\''+d[j].Value+'\',\''+d[j].Name+'\')">'+d[j].Name+'</a></li>';
//					}
//				}
//				html += ' </ul></div></div><div class="a-divider"></div>';
//			}
//			$('#aMenus').empty().append(html);
// 		}else {
// 			$('#aMenus').empty().append('&nbsp;&nbsp;&nbsp;<font color=red>没有可操作的菜单!<br/><br/>&nbsp;&nbsp;&nbsp;请联系管理员开放菜单权限!</font>');
// 		}
// 	}
// 	function getIconByPName(name){
// 		if(name == '功能'){
// 			return 'icon icon-aside-1';
// 		}else if(name == '管理'){
// 			return 'icon icon-aside-2';
// 		}else if(name == '数据'){
// 			return 'icon icon-aside-3';
// 		}else if(name == '安全'){
// 			return 'icon icon-aside-4';
// 		}else if(name == '设置'){
// 			return 'icon icon-aside-5';
// 		}else {
// 			return 'icon icon-aside-5';
// 		}
// 	}
 
	//链接菜单
 	function linkMenu(url, menuname){
 		if(menuname=='sso') {
 			var rp = Commonjs.getRootPath;
 			window.location.href=rp+'/sso.html?userid='+Commonjs.getSession().UserID+'&sessionid='+Commonjs.getSession().SessionID;
 		}else {
 			if( url.indexOf('?')>0 ){
 				url = url+'&userid='+Commonjs.getSession().UserID+'&sessionid='+Commonjs.getSession().SessionID;
	 		}else{
	 			url = url+'?userid='+Commonjs.getSession().UserID+'&sessionid='+Commonjs.getSession().SessionID;
	 		}
	 		$('#mainFrame').attr('src',url);
	 		var d=0;
	 		if(menuname!=undefined&&menuname.indexOf('满意度')!=-1) {
	 			resizeFrame(61);
	 			d=61;
	 		}else {
	 			resizeFrame(0);
	 			d=0;
	 		}
	 		$(window).resize(function(){
					Throttle(resizeFrame(d),50,30);
				});
 		}
 	}
 	
 
 	//框架页链接跳转
 	function linkPage(o,url){
 		$('a[name="curr"]').each(function(i){
 			$(this).removeClass('curr');
 		})
 		$(o).attr('class',"curr");
 		$('#mainFrame').attr('src',url);
 	}
 	//退出登录
 	function doLogout(){
 		window.location.href = '/backstage/logout.do';	
	}
 	/**
		 * 修改主体框架的iframe高度
		 * 主要用于区分满意度和
		 */
	var	resizeFrame = function(h) {
		var winHeight=$(window).height();
		$('#main-frame-wrap').height(winHeight-h);
		$(".container-wrap>.scroll-pane").jScrollPane({"autoReinitialise": true});
	}
		
function Throttle(fn, delay, mustRunDelay){
	var timer = null;
	var t_start;
	return function(){
		var context = this, args = arguments, t_curr = +new Date();
		clearTimeout(timer);
		if(!t_start){
			t_start = t_curr;
		}
		if(t_curr - t_start >= mustRunDelay){
			fn.apply(context, args);
			t_start = t_curr;
		}
		else {
			timer = setTimeout(function(){
				fn.apply(context, args);
			}, delay);
		}
	};
};