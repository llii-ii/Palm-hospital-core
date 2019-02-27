$(function(){
	//分版本导航
	$('span[data-menu="header"]').on('click',function(){
		var $this=$(this).parent(),
			$menu=$this.find('.header-menu-box'),
			$bg=$this.find('.header-menu-bg');
		if($menu.css('display')=='none'){
			$menu.slideDown();
			$bg.show();
		}else{
			$menu.slideUp();
			$bg.hide();
		}
	});
	$('.header-menu-bg').on('click',function(){
		$('.header-menu-box').slideUp();
		$('.header-menu-bg').hide();
	});
	// 页面初始化
	function pageInit(){
		$('.page-sidebar').css('height',$(window).height()-$('.header-wrap').height()-$('.iframe-menu').height());
		$(".page-sidebar").niceScroll({cursorborder:"",cursorcolor:"#cccdd1"});
		if($('.header-wrap').length>0){
			$('#iframe-im').height($(window).height()-$('.header-wrap').outerHeight(true)-$('.iframe-menu').outerHeight(true));
		}else{
			$('#iframe-im').height($(window).height());
		}

		$('.page-sidebar').on('click',function(){
			$(".page-sidebar").getNiceScroll().resize();
		});
		$(".page-sidebar").on('mouseover',function() {
			$(".page-sidebar").getNiceScroll().resize();
		});
	}
	pageInit();
	$(window).resize(function(){throttle(pageInit(), 300)});



	//iframe-menu
	$('div.menu-collapse[data-iframe="true"]').on('click',"li>a",function(){
		if($(this).data('src')!=undefined){
			var $iframe=$('#iframe-im'),
				$iframeTabs=$('.iframe-menu-list>ul'),
				iframeId=$(this).data('iframe');
			if(iframeId==undefined) return;
			if($iframe.find('iframe[data-iframe="'+iframeId+'"]').length>0){
                $iframe.find('iframe[data-iframe="'+iframeId+'"]').show().siblings().hide();
                $iframeTabs.children('li[data-iframe="'+iframeId+'"]').find('a').addClass('curr').end().siblings().find('a').removeClass('curr');
                var _left=$iframeTabs.children('li[data-iframe="'+iframeId+'"]').index()*131;
                if($('.iframe-menu-btn').css('display')=='block'){
                    $('.iframe-menu-list').css({'left':"-"+_left+"px"}).data('left',-_left);
                }
				$iframe.append('<iframe frameborder="no" src="'+$(this).data('src')+'" width="100%" height="100%" data-iframe="'+iframeId+'" data-left="'+$(this).data("left")+'"></iframe>');
			}else{
				$iframeTabs.find('a').removeClass('curr');
				$iframeTabs.append('<li data-iframe="'+iframeId+'" data-left="'+$(this).data("left")+'"><a href="javascript:;" class="curr"><span class="c-nowrap">'+$(this).find('span.title').text()+'</span><i class="iconfont">&#xe605;</i></a></li>');
				$iframe.find('iframe').hide();
				$iframe.append('<iframe frameborder="no" src="'+$(this).data('src')+'" width="100%" height="100%" data-iframe="'+iframeId+'" data-left="'+$(this).data("left")+'"></iframe>');
			}
		}
	});
});