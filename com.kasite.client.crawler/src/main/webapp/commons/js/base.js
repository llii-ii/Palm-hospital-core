$(function(){
	//顶部导航
	$(".header-member-login").hover(function(){
		$(this).addClass("hover");
	},function(){
		$(this).removeClass("hover");
	});
	
	if ($.browser.msie && ($.browser.version == "6.0") && !$.support.style) { 
		$('.aside-ctrl').hide()
	} 

	
	$('.aside-ctrl').click(function(){
		if($('.aside').hasClass('aside-collapsed')) {
			$('.aside').animate({width:190},200,function(){
				$('.aside').removeClass('aside-collapsed');
				$('.a-menu-tit').unbind('mouseenter mouseleave');
			});
			$('.main-header-btns').animate({'margin-right':"230px"},200);
			$('.main').animate({marginLeft:190},200);
		} else {
			$('.aside').animate({width:60},200,function(){
				$('.aside').addClass('aside-collapsed');
				$('.a-menu').bind('mouseenter',function(){
					$(this).addClass('curr').find('.a-menu-list');
				})
				$('.a-menu').bind('mouseleave',function(e){
					$(this).removeClass('curr').find('.a-menu-list');
				})
				
			})
			$('.main-header-btns').animate({'margin-right':"90px"},200);
			$('.main').animate({marginLeft:60},200);
			
		}
		$('.logo-wrap a.curr').removeClass('curr').siblings('a').addClass('curr');
	});
	
 	//表格交互
	$('.tb tr:has("td")').hover(function(){
		$(this).addClass('hover');
	},function(){
		$(this).removeClass('hover');
	})
	//主体高度计算
	function wrapAspect(){
		var winHeight=$(window).height();
		$(".aside .scroll-pane").height(winHeight-61).jScrollPane({"autoReinitialise": true,"overflow":true});
		
		Commonjs.getJscrollpane=$(".container-wrap>.scroll-pane").height(winHeight-111).jScrollPane({"autoReinitialise": true}).data('jsp');
		
		//if (!($.browser.msie && ($.browser.version == "6.0") && !$.support.style)) {
		//	$(".container-wrap>.scroll-pane").height(winHeight-111).jScrollPane({"autoReinitialise": true});
		//}else{
		//	$(".container-wrap>.scroll-pane").height(winHeight-111).width($(window).width()-195);
		//}
		
	}
	function mainframe(){
		var winHeight=$(window).height();
		$('#main-frame-wrap').height(winHeight);
	}
	
	wrapAspect();
	mainframe();
	$(window).resize(function(){
		Throttle(wrapAspect(),50,30);
		Throttle(mainframe(),50,30);
	});
	
	$('.header-member').hover(function(){
		$('.avatar-wrap img').addClass('header-member-img')
	},function(){
		$('.avatar-wrap img').removeClass('header-member-img')
	});
	
	//多选项
	$('.custom-checkbox > label').on('click',function(){
		$(this).toggleClass('checked');
		if($(this).hasClass('checked')){			
			$(this).prev().attr("checked",true);			
		}else{
			$(this).prev().attr("checked",false);
		}
	})

    //分页
    var isAtBtnPageto;
    $(".btn-pageto").on("mouseenter",
        function() {
            isAtBtnPageto = !0
        }
    );
    $(".btn-pageto").on("mouseleave",
        function() {
            isAtBtnPageto = !1
        }
    );
    $(".pageto").delegate(".input-pageto","focus",function(){$(this).parent().addClass("pageto-focus")}).delegate(".input-pageto","blur",function(e){isAtBtnPageto||$(this).parent().removeClass("pageto-focus");});
	//开关
	$('.my-switch-box').on('click',function(){
		//预约规则展示的开关限制开放   支付配置的开关限制开放
		if($(this).attr('id')=="xhgzRuleSwitch"||$(this).attr('id')=="wyxzgzRuleSwitch"||$(this).attr('id')=="isenableapys")return;
		
		
		if($(this).is('.red')){
			$(this).removeClass('red');
		}else{
			$(this).addClass('red');
		}
	});
})


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