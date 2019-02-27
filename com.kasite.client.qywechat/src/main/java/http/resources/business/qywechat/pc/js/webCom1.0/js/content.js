var $parentNode = window.parent.document;
function $childNode(name) {
    return window.frames[name]
}

// tooltips
$('.tooltip-demo').tooltip({
    selector: "[data-toggle=tooltip]",
    container: "body"
});

$(document).on('mouseover','.zhengxie .table-hover>tbody>tr',function(){
	var _this = $(this);
	_this.parent().find('tr.active').removeClass('active');
	_this.addClass('active');
})
$(document).on('mouseout','.zhengxie .table-hover>tbody>tr',function(){
	var _this = $(this);
	_this.parent().find('tr.active').removeClass('active');
})

// 使用animation.css修改Bootstrap Modal
$('.modal').appendTo("body");

$("[data-toggle=popover]").popover();

//折叠ibox
$('.collapse-link').click(function () {
    var ibox = $(this).closest('div.ibox');
    var button = $(this).find('i');
    var content = ibox.find('div.ibox-content');
    content.slideToggle(200);
    button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
    ibox.toggleClass('').toggleClass('border-bottom');
    setTimeout(function () {
        ibox.resize();
        ibox.find('[id^=map-]').resize();
    }, 50);
});

//关闭ibox
$('.close-link').click(function () {
    var content = $(this).closest('div.ibox');
    content.remove();
});

//判断当前页面是否在iframe中
if (this == top) {  
    var gohome = '<div class="gohome" style="top:20px;"><a href="'+realPath+'/examples/index.html" title="返回首页"><i class="fa fa-home"></i></a></div>';
    $('body').append(gohome);
}

//animation.css
function animationHover(element, animation) {
    element = $(element);
    element.hover(
        function () {
            element.addClass('animated ' + animation);
        },
        function () {
            //动画完成之前移除class
            window.setTimeout(function () {
                element.removeClass('animated ' + animation);
            }, 2000);
        });
}

//拖动面板
function WinMove() {
    var element = "[class*=col]";
    var handle = ".ibox-title";
    var connect = "[class*=col]";
    $(element).sortable({
            handle: handle,
            connectWith: connect,
            tolerance: 'pointer',
            forcePlaceholderSize: true,
            opacity: 0.8,
        })
        .disableSelection();
};
function tp(){
	$('.stip-top').each(function(){
		var _this = $(this);
		var _h = _this.outerHeight()-20;
		$('.stip-top-clone').remove();
		_this.after('<div class="stip-top-clone" style="height:'+_h+'px;"></div>');
	})
}
tp();
