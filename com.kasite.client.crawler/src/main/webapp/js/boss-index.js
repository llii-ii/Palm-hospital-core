$(function() {
	
	// 分机构导航
	$('span[data-menu="header"]').on('click', function() {
		var $menu = $(this).next();
		if ($menu.css('display') == 'none') {
			$menu.slideDown();
		} else {
			$menu.slideUp();
		}
	});
	// 搜索
	$('input[data-im="search"]').on('focus', function() {
		if ($(this).val() == '搜索联系人...') {
			$(this).val('').addClass('focus');
			$('.im-search-btn').show();
		}

	}).on('blur', function() {
		if ($(this).val() == '') {
			//$('.im-menu-hd>a').eq(0).click();
			searchFun();
		}
	});
	$('.im-search-btn').on('click', function() {
		$(".search-scroll").getNiceScroll().resize();
		$('.im-main-search').show();
		$('.im-search-close').show();
		$('.im-menu-hd>a.curr').removeClass('curr');
		searchInfo();

	});

	$('input[data-im="search"]').keyup(function(e) {
		$(".search-scroll").getNiceScroll().resize();
		$('.im-main-search').show();
		$('.im-search-close').show();
		$('.im-menu-hd>a.curr').removeClass('curr');
		searchInfo();
	});

	$('#text_msg').keyup(function(e) {

		if (e.ctrlKey && e.which == 13) {
			$("#text_msg").val($("#text_msg").val()+"\n");
		}else if(e.which == 13) {
			sendMsg();
		}
	});

	$('.im-search-close').on('click', function() {
		$('input[data-im="search"]').val('').focus();
		$("#search_friend_result").html("");
	});
	

	// 页面初始化
	function scrollFun() {
		// 主iframe
		$('#iframe-main').css({width : $(window).width(),height : $(window).height() - 50});
		$('#iframe-im').css({height : $(window).height() - 50});
		// im-main-box 高度
		$('.im-main-search,.im-menu-bd,.im-main-mine,.im-main-apps').css({height : $(window).height() - 243});
		// 自定义滚动条
		$(".apps-scroll").css({height:$(window).height()-276}).niceScroll({cursorborder:"",cursorcolor:"#cccdd1"});
		$(".search-scroll").css({height : $(window).height() - 243}).niceScroll({cursorborder : "",cursorcolor : "#cccdd1"});
		$('.mine-scroll').css({height : $(window).height() - 276}).niceScroll({cursorborder : "",cursorcolor : "#cccdd1"});
		$('.apply-scroll').css({height : $(window).height() - 276}).niceScroll({cursorborder : "",cursorcolor : "#cccdd1"});
		$('.group-scroll').css({height : $(window).height() - 276}).niceScroll({cursorborder : "",cursorcolor : "#cccdd1"});
		$('.tree-scroll').css({height : $(window).height() - 276}).niceScroll({cursorborder : "",cursorcolor : "#cccdd1"});
		$('.user-scroll').css({height : $(window).height() - 276}).niceScroll({cursorborder : "",cursorcolor : "#cccdd1"});
		$('.tidings-scroll').css({height : $(window).height() - 276}).niceScroll({cursorborder : "",cursorcolor : "#cccdd1"});
	}
	scrollFun();
	// 窗口大小被改变时
	$(window).resize(function() {
		throttle(scrollFun(), 300)
	});
	// tabs
	$('.im-menu').tabs({
		callback : function(index) {
			$('.im-main-mine,.im-main-apps').hide();
			searchFun();
			switch (index) {
			case 0:
				$('.tidings-scroll').getNiceScroll().resize();
				break;
			case 1:
				$('.apply-scroll').getNiceScroll().resize();
				break;
			case 2:
				userTabsScroll($('.im-user-hd').find('a.curr').index());
				break;
			}
		}
	});
	$('.im-user-tabs').tabs({
		callback : function(index) {
			userTabsScroll(index);
		}
	});
	function userTabsScroll(index) {
		switch (index) {
		case 0:
			$('.user-scroll').getNiceScroll().resize();
			break;
		case 1:
			$('.group-scroll').getNiceScroll().resize();
			break;
		case 2:
			$('.tree-scroll').getNiceScroll().resize();
			break;
		}
	}
	// im主窗口
	$('.boss-im-arrow').on('click', function() {
		showImBox();
	});
	$('a[data-im="mine"]').on('click', function() {
		showImBox("mine");
	});
	$('a[data-im="tidings"]').on('click', function() {
		showImBox("tidings");
	});
	$('a[data-im="apply"]').on('click', function() {
		showImBox("apply");
	});
	showImBox("apply");
	
	// iframe-im
	$('.iframe-im-close').on('click', function() {
		imClosed();

	});
	$('.im-my-head').on('click', function() {

		showIframe('myUserInfo.html');
	});

	$('#detailInfo').on('click', function() {

		if ($("#isGroup").val() == 1) {
			$('#iframe-userInfo').attr("src", "");
			showIframe("myGroupInfo.html?tid=" + $("#talkingID").val());
		} else {
			$('#iframe-userInfo').attr("src", "");
			showIframe("myFriendInfo.html?tid=" + $("#talkingID").val());
		}

	});

	// 组织列表
	$(document)
			.on(
					'click',
					'div.tree-hd',
					function() {
						var $el = $(this), $next = $el.next(), $a = $el
								.children('a'), $iconfont = $el
								.find('i.iconfont'), $box = $next
								.find('.tree-bd-box');
						//$box.css('padding-left', $a.position().left);
						if ($next.css('display') == 'none') {
							$iconfont.remove();
							$a.prepend('<i class="iconfont">&#xe60f;</i>');
							$next.slideDown();
						} else {
							$iconfont.remove();
							$a.prepend('<i class="iconfont">&#xe609;</i>')
							$next.slideUp();
						}
					});

	
	//iframeMenuList();
});
;
	function searchFun() {
		$('input[data-im="search"]').val('搜索联系人...').removeClass('focus');
		$('.im-search-btn').hide();
		$('.im-search-close').hide();
		$('.im-main-search').hide();
	}
	function showImBox(dom) {
		var $im = $('.boss-im'), $arrow = $('.boss-im-arrow'), $iframe = $('#iframe-main'), $menua = $('.im-menu-hd>a'), $iframeTab = $('.iframe-menu');
		if (typeof (dom) == 'undefined') {
			$('.im-main-mine').hide();
			searchFun();
		}
		if (dom != '') {
			if ($im.is('.show')) {
				switch (dom) {
				case 'mine':
					if ($('.im-main-mine').css('display') == 'none') {
						$('.im-menu-hd>a.curr').removeClass('curr');
						$('.im-main-mine').show();
						return false;
					}
					break;
				case 'tidings':
					if (!($menua.eq(0).is('.curr'))) {
						searchFun();
						$('.im-main-mine').hide();
						$menua.eq(0).click();
						return false;
					}
					break;
				case 'apply':
					if (!($menua.eq(1).is('.curr'))) {
						searchFun();
						$('.im-main-mine').hide();
						$menua.eq(1).click();
						return false;
					}
					break;
				}

			} else {
				switch (dom) {
				case 'mine':
					$('.im-main-mine').show();
					$('.im-menu-hd>a.curr').removeClass('curr');
					break;
				case 'tidings':
					$menua.eq(0).click();
					break;
				case 'apply':
					$menua.eq(1).click();
					break;
				}
			}
		}

		if($im.is('.show')){
			$im.removeClass('show').animate({
				right: -275
			},300);
			/*$iframe.animate({
			width: $(window).width()
			},300);*/
			$iframeTab.animate({
			width: $(window).width()
			},300,function(){$arrow.css('left',"-15px")});
			$arrow.html('<i class="iconfont">&#xe61c;</i>');
			$('#iframe-im').hide();
		}else{
			$im.addClass('show').animate({
			right: 0
			},300);
			/*$iframe.animate({
			width: $(window).width()-260
			},300);*/
			$iframeTab.animate({
			width: $(window).width()-260
			},300);
			$arrow.css('left',"0px").html('<i class="iconfont">&#xe608;</i>');
		}
	}


(function($) {
	// 选项卡
	$.fn.tabs = function(options) {
		if (this.length == 0)
			return this;

		if (this.length > 1) {
			this.each(function() {
				$(this).tabs(options)
			});
			return this;
		}
		if ($(this).data('binds') == 'yes')
			return false;
		$(this).data('binds', 'yes');
		var defaults = {};
		var opts = $.extend(defaults, options || {});
		var $this = $(this), $hd = $this.children('div.tabs-hd').children('a'), $bd = $this
				.children('div.tabs-bd').children('div.tabs-bd-box');

		$hd.on('click', function() {
			var $el = $(this), index = $el.index();
			$el.addClass('curr').siblings().removeClass('curr');
			$bd.eq(index).addClass('curr').siblings().removeClass('curr');
			if (opts.callback) {
				opts.callback(index);
			}
		});
	}
})(jQuery);
// 函数节流
function throttle(fn, delay) {
	var timer = null;
	return function() {
		var context = this, args = arguments;
		clearTimeout(timer);
		timer = setTimeout(function() {
			fn.apply(context, args);
		}, delay);
	};
};

function searchInfo() {

	var orgId = $(".c-f20.c-fff").attr("data-orgid");// myInfo.orgInfo;

	var name = jQuery.trim($('input[data-im="search"]').val());
	if (name != '') {

		$.ajax({
				url : "staff/search",
				dataType : "json",
				type : 'POST',
				data : {
					orgId : orgId,
					name : name
				},
				success : function(data) {
					$("#search_friend_result").html("");
					if (data) {
						if (data.Code == 10000) {
							if (data.Result && data.Result != '') {
	
								var ul = "<ul>";
								var liListStr = "";
								for ( var i in data.Result) {
									var item = data.Result[i];
									var clientId = item.clientId;
	
									var photoUri = item.photoUri;
									if (photoUri == undefined) {
										photoUri = "images/face.png";
									}
	
									var liStr = [
											"<li class=\"boss-user\" data-clientid=\""
													+ clientId + "\">",
											"<a href=\"javascript:;\" onclick=\"showChat('"
													+ clientId + "');\" >",
											'<div class=\"boss-user-face\"><img class=\"c-images-block\" src="'
													+ photoUri + '"/></div>',
											'<div class=\"boss-user-name c-nowrap c-f14 c-333\">'
													+ item.cName + '</div>',
											'</a>', "</li>" ];
	
									liListStr += liStr.join("");
									memberInfos[clientId] = {
										"data-id" : item.userID,
										"data-name" : item.cName,
										"data-clientId" : clientId,
										"data-photoUri" : photoUri,
										"data-sex" : item.sex
									};
								}
	
								ul += liListStr + "</ul>";
								$("#search_friend_result").append(ul);
	
							}
						}else {
						//showMsg("系统繁忙，请稍候再试");
						}
					} else {
						//showMsg("系统繁忙，请稍候再试");
					}
				},
				error : function(errorData) {
					//showMsg("系统繁忙，请稍候再试");
				}
			});
	} else {

		$("#search_friend_result").html("");
	}

}

function imClosed() {
	$('#talking_box').hide();
	$('#iframe-userInfo').hide();
	$('#iframe-im').hide();
	if($.browser.msie&&($.browser.version == "8.0")){
	}else{
		$('#iframe-userInfo').attr("src", "");
	}
	resetStructure();
}

function showIframe(src) {

	if ($('#iframe-userInfo').attr("src") == ''
			|| $('#iframe-userInfo').attr("src") != src) {
		$('#iframe-userInfo').attr("src", src);
	}
	resetStructure();
	$('#talking_box').hide();
	$('#iframe-userInfo').show();
	$('#iframe-im').show();
}

function resetStructure() {
	Structure.tarClientId = "";
	Structure.topic_id = "";
	Structure.cName = "";
	Structure.id = "";
	Structure.photoUri = "";
	Structure.sex = "";
	Structure.isGroup = false;
}

// 弹出框
function showMsg(content) {
	art.dialog({
		lock : true,
		artIcon : 'ask',
		opacity : 0.4,
		width : 250,
		title : '提示',
		content : content,
		ok : function() {

		}
	});
}

// 弹出框
function showHideMsg(content, time) {
	art.dialog({
		lock : true,
		artIcon : 'ask',
		opacity : 0.4,
		width : 250,
		title : '提示',
		time : time,
		content : content,
		ok : function() {

		}
	});
}

function iframeMenuList() {
		var $menu = $('.iframe-menu-list'), $li = $menu.children('ul')
				.children('li'), $wrap = $('.iframe-menu'), w;
		if ($('.boss-im').is('.show')) {
			w = $(window).width() - 260;
		} else {
			w = $(window).width();
		}
		if ($li.length * 181 > w) {
			$menu.width($li.length * 181);
			$('.iframe-menu-btn').show();
			// iframe-menu
	
	$('.iframe-menu-btn>a.prev').on('click', function() {
		var left = $('.iframe-menu-list').data('left') * 1;
		if (left == 0) {
			return false;
		} else if (left < 0) {
			$('.iframe-menu-list').animate({
				'left' : left + 180
			}, "fast", function() {
				$('.iframe-menu-list').data('left', left + 180);
			});
		}
	});
	$('.iframe-menu-btn>a.next').on('click', function() {
		var left = $('.iframe-menu-list').data('left') * 1;
		if (left < -(($('.iframe-menu-list>ul>li').length - 2) * 180)) {
			return false;
		} else if (left <= 0) {
			$('.iframe-menu-list').animate({
				'left' : left - 180
			}, "fast", function() {
				$('.iframe-menu-list').data('left', left - 180);
			});
		}
	});
		}
	}