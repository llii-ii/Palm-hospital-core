/* general.js created by zhaoboy 2014-1-20 */

//初始顶部下拉菜单方法
function iniTopNav() {
	$("ul.main-nav li").each(function() {
		if (($(this).find("ul.main-sub-nav > li > a").text() == '')&&($(this).find("ul.main-sub-nav-fixed > li > a").text() == '')) {
			$(this).css("background-image", "none").css("padding", "0 10px");
		}
	});
	if ($("ul.main-nav > li.selected").find("ul > li").text() == "") {
		$("#top").css("margin-bottom", 0);
		$("ul.main-nav > li.selected").css("background-image", "none").css(
				"background-color", "#666").css("padding", "0 10px");
		$("ul.main-nav > li.selected > ul").hide();
	}
	

	$("ul.main-sub-nav-fixed").width($("#top").width());
	//选中菜单的序列
	//var index = $("li.first-level.selected").index();
	//显示选中一级菜单对应的二级菜单
	//$("ul.main-nav li.first-level.selected ul.main-sub-nav").show();

	//鼠标enter显示对应菜单内容，leave恢复初始状态
	$("ul.main-nav > li:not('.selected')").hover(function() {
		$(this).addClass("hovered").siblings().removeClass("hovered");
		$("ul.main-sub-nav").hide();
		$("ul.main-sub-nav", this).show();
	}, function() {
		$("ul.main-nav li").removeClass("hovered");
		$("ul.main-sub-nav").hide();
	});

	$("ul.main-sub-nav li").hover(function() {
		$(this).addClass("selected").siblings().removeClass("selected");
	}, function() {
		$(this).removeClass("selected");
	});
	
	$("ul.main-sub-nav > li").hover(function() {
		$("ul.main-sub2-nav", this).show();
	}, function() {
		$("ul.main-sub2-nav").hide();
	});

	//选中二级菜单的序列
	//var sub_index = $("ul.main-nav li.first-level:eq("+index+") ul.main-sub-nav li.selected").index();
	
	var sub_index = $("ul.main-sub-nav-fixed li.selected").index();
	$("ul.main-sub-nav-fixed li")
			.hover(
					function() {
						$(this).addClass("selected").siblings().removeClass(
								"selected");
					},
					function() {
						$("ul.main-sub-nav-fixed li:eq(" + sub_index + ")")
								.addClass("selected").siblings().removeClass(
										"selected");
					});
}

//初始化左侧折叠菜单
function iniLeftNav() {
	$("ul.left-nav li").each(function() {
		if ($(this).find("ul.left-sub-nav > li > a").text() == '') {
			$(this).find("span").css("background-image", "none");
		}
	});
	$("ul.left-nav li.first-level span.open-list").next("ul.left-sub-nav")
			.show();
	$("ul.left-nav li.first-level span.open-list").live("click", function() {
		$(this).next("ul.left-sub-nav").slideUp(0);
		$(this).removeClass("open-list").addClass("close-list");
		sizePane();
	});
	$("ul.left-nav li.first-level span.close-list").live("click", function() {
		if ($(this).next("ul.left-sub-nav").html() != null) {
			$(this).next("ul.left-sub-nav").slideDown(0);
			$(this).removeClass("close-list").addClass("open-list");
			sizePane();
		}
	});
}

//调整页面大小
function sizePane(extraHeight) {
	var $Container = $('#container'), $PaneCenter = $('.ui-layout-center'), $ContentCenter = $('#contentCenter'), outerHeightCenter = $PaneCenter
			.outerHeight()
	// use a Layout utility to calc total height of padding+borders (also handles IE6)
	, panePaddingCenter = outerHeightCenter
			- $.layout.cssHeight($PaneCenter, outerHeightCenter), HeightCenter = $ContentCenter
			.outerHeight()
			+ panePaddingCenter

	, $PaneWest = $('.ui-layout-west'), $ContentWest = $('#contentWest'), outerHeightWest = $ContentWest
			.outerHeight()
	// use a Layout utility to calc total height of padding+borders (also handles IE6)
	, panePaddingWest = outerHeightWest
			- $.layout.cssHeight($ContentWest, outerHeightWest), HeightWest = $ContentWest
			.outerHeight()
			+ panePaddingWest

	, $ContentWestOuter = $('#contentWestOuter'), outerHeightWestOuter = $ContentWestOuter
			.outerHeight()
	// use a Layout utility to calc total height of padding+borders (also handles IE6)
	, panePaddingWestOuter = outerHeightWestOuter
			- $.layout.cssHeight($ContentWestOuter, outerHeightWestOuter), HeightWestOuter = $ContentWestOuter
			.outerHeight()
			+ panePaddingWestOuter

	, $PaneEast = $('.ui-layout-east'), $ContentEast = $('#contentEast'), outerHeightEast = $ContentEast
			.outerHeight()
	// use a Layout utility to calc total height of padding+borders (also handles IE6)
	, panePaddingEast = outerHeightEast
			- $.layout.cssHeight($ContentEast, outerHeightEast), HeightEast = $ContentEast
			.outerHeight()
			+ panePaddingEast

	, wHeight = $(window).height()
	, contentTop = $('#contentTop').outerHeight()
	// update the container height - *just* tall enough to accommodate #Content without scrolling
	if(extraHeight) {
		//HeightCenter = HeightCenter + contentTop + 90 + 10 + 45 + extraHeight;
		HeightCenter = HeightCenter + contentTop + 110 + extraHeight;
	} else {
		//HeightCenter = HeightCenter + contentTop + 90 + 10 + 45;
		HeightCenter = HeightCenter + contentTop + 110;
	}
	//HeightWestOuter = HeightWestOuter +  + contentTop + 80 + 45;
	if(extraHeight) {
		HeightWestOuter = HeightWestOuter + contentTop + 80 + 45 + extraHeight;
	}
	else
	{
		HeightWestOuter = HeightWestOuter + contentTop + 80 + 45;
	}
	//alert(HeightWestOuter);
	//alert(HeightCenter);
	//alert(HeightEast);
	//alert(wHeight);
	var height = [ HeightWest, HeightWestOuter, HeightCenter, HeightEast, wHeight ];
	
	var maxHeight = Math.max.apply(null, height);
	$Container.height(maxHeight);
	//$ContentCenter.height(maxHeight);

	//$Container.height( $Pane.position().top + $Content.outerHeight() + panePadding );
	// now resize panes to fit new container size
	myLayout.resizeAll();
	
	//设置遮罩大小
	wh = $(window).height();
	ww = $(window).width();
	dh = $(document).height();
	if(wh > dh) hh = wh;
	if(wh <= dh) hh = dh;
  	$('#mask').width(ww).height(hh).spin({color: '#fff'});
}

$(window).resize(function(){
	wh = $(window).height();
	ww = $(window).width();
	dh = $(document).height();
	if(wh > dh) hh = wh;
	if(wh <= dh) hh = dh;
	$('mask').width(ww).height(hh).spin({color: '#fff'});
});
function showMask(){
	$('#mask').show().spin({color: '#fff'});
}
function hideMask(){
	$('#mask').spin(false).hide();
}
$(function() {
	//	oTable = $('#data-table').dataTable({
	//		"bJQueryUI": false,
	//		"bAutoWidth": false,
	//		"sPaginationType": "full_numbers",
	//		"sDom": '<"datatable-header"fl>t<"datatable-footer"ip>',
	//		"oLanguage": {
	//			"sSearch": "<span>Filter records:</span> _INPUT_",
	//			"sLengthMenu": "<span>Show entries:</span> _MENU_",
	//			"oPaginate": { "sFirst": "First", "sLast": "Last", "sNext": ">", "sPrevious": "<" }
	//		}
	//    });
	//	$('.tabbable').easytabs({animate: false});
});

function isEmptyString(str){
	if(str == null ){
		return true;
	}

	if(str == ""){
		return true;
	}

	return false;

}
function convert4Ajax(str){

	if(isEmptyString(str)){
		return str
	}

	str=str.replace(/\%/g, "%25");
	str=str.replace(/\&/g, "%26");

	return str
}

function dateCompare(startdate, enddate) {
	var arr = startdate.split("-");
	var starttime = new Date(arr[0], arr[1], arr[2]);
	var starttimes = starttime.getTime();

	var arrs = enddate.split("-");
	var lktime = new Date(arrs[0], arrs[1], arrs[2]);
	var lktimes = lktime.getTime();

	if (starttimes > lktimes) {
		return true;
	} else
		return false;
}
	
function dateRange(startdate, enddate, day) {
	var startD = new Date(Date.parse(startdate.replace(/-/g, "/")));
	var endD = new Date(Date.parse(enddate.replace(/-/g, "/")));
	var days = parseInt((endD.getTime() - startD.getTime())
			/ (1000 * 60 * 60 * 24));
	if (days >= day) {
		return true;
	} else {
		return false;
	}
}