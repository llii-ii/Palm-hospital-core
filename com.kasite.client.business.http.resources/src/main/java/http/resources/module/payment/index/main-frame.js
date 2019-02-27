
	var perms = new Array();
	
	 $(function(){
		$("#hosName").append(Commonjs.hospitalName() + " · 智付管理系统");
		resizeFrame(0); //初始化的统计概况页面比例设置
	    //加载菜单
		getMenuList();
		setTimeout(function(){
			var sessions = Commonjs.getSession();
			var userName = sessions.realName;
			if(Commonjs.isNull(userName)){
				userName = sessions.roleName;
			}
            $('#usernameid').html(userName);
	        $('.a-menu-list li').on('click',function(){
	        	 $('.a-menu-list li').removeClass('current');
	        	 $(this).addClass('current');
	        });
	        
		},1000)
		
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
 	//初始化菜单选项
	function initMenuList(menuList){
	 		
		if(!Commonjs.isEmpty(menuList) && menuList.length>0){
			//遍历菜单
			var html = '';
			$.each(menuList,function(i,menu){
				var curr;
				if(i == 0){
					curr = "curr ";
				}else{
					curr = "";
				}
				html += '<li>';
				if(!Commonjs.isEmpty(menu.list) && menu.list.length>0){
					html += '<a href="javascript:;" class="'+curr+'first-meau"><i class="icon icon-trade"></i><label>'+menu.name+'</label><i class="icon icon-mjt"></i></a>';
					html += '<ul class="child-meau">';
					$.each(menu.list,function(j,m){
						html += '<li><a href="javascript:linkMenu(\''+m.url+'\',\''+m.name+'\');" data-src="">'+m.name+'</a></li>';
	 				})
	 				html += '</ul>';
				}else{
					html += '<a href="javascript:linkMenu(\''+menu.url+'\',\''+menu.name+'\');" class="'+curr+'first-meau" data-src=""><i class="icon icon-operate"></i><label>'+menu.name+'</label></a>';
				}
				html += '</li>';
	 		});
			$('.meau-list').empty().append(html);
		}else{
			$('.meau-list').empty().append('&nbsp;&nbsp;&nbsp;<font color=red>没有可操作的菜单!<br/><br/>&nbsp;&nbsp;&nbsp;请联系管理员开放菜单权限!</font>');
		}
	}
 
 	//获取所有的渠道场景
 	function findAllChannel(){
 		var Service = {};
		var code = 2006;
		var param = {};
		var params = Commonjs.getParams(code,Service);//获取参数
		param.params = Commonjs.jsonToString(params);
 		Commonjs.ajaxJson("/channel/queryAllChennel.do",function(){
 			if(d.RespCode==10000){
 	 			return d.Result;
 	 		}else{
 	 			Commonjs.alert(d.RespMessage);
 	 		}
 		});
 	}
 	
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
 		window.location.href = '/backstage/logoutPayment.do';	
	}
 	/**
		 * 修改主体框架的iframe高度
		 * 主要用于区分满意度和
		 */
	var	resizeFrame = function(h) {
		var winHeight=$(window).height();
		$('#iframe-main').height(winHeight-h);
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
