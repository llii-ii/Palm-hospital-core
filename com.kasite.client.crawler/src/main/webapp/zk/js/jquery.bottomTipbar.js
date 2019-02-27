/*
 * 提示条工具
 * version : "1.0",
 * auther : "陈 庭",
 *
 * Date: 2011-04-02
 *
 */
(function($) {
	$.bottomTipbar = {
		initCss : function(options){
			/*默认显示配置及用户当前配置*/
			var defaults = {

			}
			var config = $.extend(defaults,options);
			
			// 动态加入皮肤样式(CSS)
			$("script").each(function () { 
				if(this.src.toString().match(/jquery\.bottomTipbar.*?js$/)) { 
					// 创建样式表
					$("head").append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + this.src.toString().replace(/jquery\.bottomTipbar.*?js$/, "") + "bottomTipbar.css" + "\" id=\"bottomTipbarCss\" />");
					return false; 
				}
			});
		},
		
		/* 显示提示条 */
		show : function(options){
			/*默认显示配置及用户当前配置*/
			var defaults = {
				msg : '', // 提示信息
				bgAlpha : 0.8, // 透明度
				closeBtn : true, // 是否可手动关闭,
				zIndex : 100,    // 层次
				slideClose : true, // 是否滑动关闭
				autoClose : true,  // 是否自动关闭
				closeTime : 2000,  // 停留时间
				msgType:'success'       //消息类型 success error info block
			}
			var config = $.extend(defaults,options);
			
			var barId = 'barDiv';
			var i = 1;
			for(; i < 100; i++){
				if($('#' + barId + i).length == 0){
					barId = barId + i;
					break;
				}
			}
			
			var barDiv = '<div class="bottomBarClass"  id="bottomBar' + barId + '" style="text-align:center;z-index:' + config.zIndex + ';width:100%;">' +
				'<div class="alert alert-' + config.msgType + '" id="' + barId 
				+'" style="overflow:auto;text-align:left;margin:0 auto; width:' + $(window).width()*0.7 + 'px;max-height:568px;width:80%;"></div></div>';
			
			$('body').find('.bottomBarClass').each(function () { 
				$(this).remove();
			});
			
			$('body').append(barDiv);

			var timer = null;
			// 是否自动关闭
			if(config.autoClose){
				timer = setTimeout(function(){
					if(config.slideClose){
						$('#bottomBar' + barId).slideUp(1000, function(){$('#bottomBar' + barId).remove();});
					}else{
						$('#bottomBar' + barId).remove();
					}
				}, config.closeTime);
			}
			
			// 设置背景透明度
			if(jQuery.browser.msie){
			   	$('#' + barId).css("filter", 'progid:DXImageTransform.Microsoft.Alpha(opacity='+config.bgAlpha*100+')');
			}else{
				$('#' + barId).css("opacity", config.bgAlpha);
			}
			
			// 是否显示关闭按钮
			if(config.closeBtn){
				$('#' + barId).append("<div title='关闭' style='float:right;' class='closeClass_" + barId +"'><button id='close_" + barId +"' type='button' class='close' data-dismiss='alert'>×</button></div>");
				// 关闭事件
				$("#close_" + barId).click(function(){
					if(config.slideClose){
						$('#bottomBar' + barId).slideUp(1000, function(){$('#bottomBar' + barId).remove();});
					}else{
						$('#bottomBar' + barId).remove();
					}
					if(timer != null){
						clearTimeout(timer);
					}
				});
			}

			$('#' + barId).append(config.msg );
			// 定位
			//$('#bottomBar' + barId).css({left:0,top:$(window).height() - $('#bottomBar' + barId).height() * i});
			$('#bottomBar' + barId).css({'left':'0','bottom':'0px','position':'fixed'});
		}
	}
})(jQuery);