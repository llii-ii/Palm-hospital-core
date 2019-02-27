+function ($) {
  $.support = (function() {
    var support = {
      touch: !!(('ontouchstart' in window) || window.DocumentTouch && document instanceof window.DocumentTouch)
    };
    return support;
  })();
  $.touchEvents = {
    start: $.support.touch ? 'touchstart' : 'mousedown',
    move: $.support.touch ? 'touchmove' : 'mousemove',
    end: $.support.touch ? 'touchend' : 'mouseup'
  };
  $.getTouchPosition = function(e) {
    e = e.originalEvent || e; //jquery wrap the originevent
    if(e.type === 'touchstart' || e.type === 'touchmove' || e.type === 'touchend') {
      return {
        x: e.targetTouches[0].pageX,
        y: e.targetTouches[0].pageY
      };
    } else {
      return {
        x: e.pageX,
        y: e.pageY
      };
    }
  };
  $.fn.scrollHeight = function() {
    return this[0].scrollHeight;
  };
  
  var PTR = function(el) {
    this.container = $(el);
    this.distance = 50;
    this.attachEvents();
  }
  PTR.prototype.touchStart = function(e) {
    if(this.container.hasClass("refreshing")) return;
    var p = $.getTouchPosition(e);
    this.start = p;
    this.diffX = this.diffY = 0;
  };
  PTR.prototype.touchMove= function(e) {
    if(this.container.hasClass("refreshing")) return;
    if(!this.start) return false;
    if($(document.body).scrollTop() > 0) return;
    var p = $.getTouchPosition(e);
    this.diffX = p.x - this.start.x;
    this.diffY = p.y - this.start.y;
    if(this.diffY < 0) return;
    this.container.addClass("touching");
    e.preventDefault();
    e.stopPropagation();
    this.diffY = Math.pow(this.diffY, 0.8);
    this.container.css("transform", "translate3d(0, "+this.diffY+"px, 0)");

    if(this.diffY < this.distance) {
      this.container.removeClass("pull-up").addClass("pull-down");
    } else {
      this.container.removeClass("pull-down").addClass("pull-up");
    }
  };
  PTR.prototype.touchEnd = function() {
    this.start = false;
    if(this.diffY <= 0 || this.container.hasClass("refreshing")) return;
    this.container.removeClass("touching");
    this.container.removeClass("pull-down pull-up");
    this.container.css("transform", "");
    if(Math.abs(this.diffY) <= this.distance) {
    } else {
      this.container.addClass("refreshing");
      this.container.trigger("pull-to-refresh");
    }
  };
  PTR.prototype.attachEvents = function() {
    var el = this.container;
    el.addClass("pull-to-refresh");
    el.on($.touchEvents.start, $.proxy(this.touchStart, this));
    el.on($.touchEvents.move, $.proxy(this.touchMove, this));
    el.on($.touchEvents.end, $.proxy(this.touchEnd, this));
  };
  //pullToRefresh
  var pullToRefresh = function(el) {
    new PTR(el);
  };
  var pullToRefreshDone = function(el) {
    $(el).removeClass("refreshing");
  }
  $.fn.pullToRefresh = function() {
    return this.each(function() {
      pullToRefresh(this);
    });
  }
  $.fn.pullToRefreshDone = function() {
    return this.each(function() {
      pullToRefreshDone(this);
    });
  }
  //Infinite
  var Infinite = function(el, distance) {
    this.container = $(el);
    this.container.data("infinite", this);
    this.distance = distance || 50;
    this.attachEvents();
  }

  Infinite.prototype.scroll = function() {
    var container = this.container;
    var offset = container.scrollHeight() - ($(window).height() + container.scrollTop());
    if(offset <= this.distance) {
      container.trigger("infinite");
    }
  }

  Infinite.prototype.attachEvents = function(off) {
    var el = this.container;
    var scrollContainer = (el[0].tagName.toUpperCase() === "BODY" ? $(document) : el);
    scrollContainer[off ? "off" : "on"]("scroll", $.proxy(this.scroll, this));
  };
  Infinite.prototype.detachEvents = function(off) {
    this.attachEvents(true);
  }

  var infinite = function(el) {
    attachEvents(el);
  }

  $.fn.infinite = function(distance) {
    return this.each(function() {
      new Infinite(this, distance);
    });
  }
  $.fn.destroyInfinite = function() {
    return this.each(function() {
      var infinite = $(this).data("infinite");
      if(infinite && infinite.detachEvents) infinite.detachEvents();
    });
  }
}($);

var myLayer = {
	alert: function(content,time,callback){
		if($(".c-alert-box").is(":visible")){
			$(".c-alert-box").remove();
		}
		if(time == undefined || time == "" || time == null){
			time = 2000;
		}
		var ahtml = '<div class="c-alert-box">'+content+'</div><div class="c-al-screen"></div>';
		$("body").append(ahtml);
		if(typeof jQuery == 'undefined'){
			var aleL = ($(window).width() - $(".c-alert-box").width()) / 2;
		}else{
			var aleL = ($(window).width() - $(".c-alert-box").width() - 20) / 2;
		}
		$(".c-alert-box").css('left', aleL + "px");
		setTimeout(function(){
			$(".c-alert-box,.c-al-screen").remove();
			if (callback){callback();}
		},time);
	},
	load: function(content){
		if($(".c-load-box").is(":visible")){
			$(".c-load-box").remove();
		}
		if(content == undefined || content == "" || content == null){
			content = "\u52a0\u8f7d\u4e2d..."
		}
		var lhtml = '<div class="c-load-box" style="display:none"><span class="loadgif"></span><p>'+content+'</p></div><div class="c-al-screen"></div>';
		$("body").append(lhtml);
		var totW = $(window).width();
		if(typeof jQuery == 'undefined'){
			var aleL = (totW - $(".c-load-box").width()) / 2;
		}else{
			var aleL = (totW - $(".c-load-box").width() - 60) / 2;
		}
		$(".c-load-box").css('left', aleL + "px");
		$(".c-load-box").css('display',"block");
	},
	clear: function(){
		$(".c-load-box,.c-al-screen").remove();
	},
	confirm: function(options){
		var dft= {
			isClose : false,
			title:'',
			con:'',
			cancel: null,
			cancelValue:'\u53d6\u6d88',
			ok: null,
			okValue:'\u786e\u5b9a',
			closed:null
		}
		var ops = $.extend(dft,options);
		var chtml = '<div class="c-conf-screen"></div>';
		chtml += '<div class="c-conf-box">';
		if(ops.title != ""){
			chtml += '<div class="conftitle">'+ops.title+'</div>';
		}
		if(ops.isClose){
			chtml += '<i class="sicon icon-anclose"></i>';
		}
		chtml += '<div class="confcontent">'+ops.con+'</div>';
		if(ops.cancel != null){
			chtml += '<div class="c-confbtn"><a href="javascript:;" class="c-twobtn" id="popcanclebtn">'+ops.cancelValue+'</a><a href="javascript:;" class="c-twobtn" id="popsurebtn">'+ops.okValue+'</a></div>';
		}else{
			chtml += '<div class="c-confbtn"><a href="javascript:;" class="c-onebtn" id="popsurebtn">'+ops.okValue+'</a></div>';	
		}
		chtml += '</div></div>';
		$("body").append(chtml);
		if(typeof jQuery == 'undefined'){
			var aleT = ($(".c-conf-box").height() + 80) / 2;
		}else{
			var aleT = ($(".c-conf-box").height() + 15) / 2;
		}
		$(".c-conf-box").css('margin-top', -aleT);
		$("#popcanclebtn").click(function(){
			if (ops.cancel){ops.cancel();}
			$(".c-conf-box,.c-conf-screen").remove();
		});
		$("#popsurebtn").click(function(){
			if (ops.ok){ops.ok();}
			$(".c-conf-box,.c-conf-screen").remove();
		});
		if(ops.isClose){
			$(".c-conf-box").click("i.icon-anclose",function(){
				$(".c-conf-box,.c-conf-screen").remove();
				if(ops.closed && typeof ops.closed=='function'){
					ops.closed();
				}
			})
		}
	},
	tip: function(options){
		var dft= {
			con:'',
			ok: null,
			okValue:'我知道了'
		}
		var ops = $.extend(dft,options);
		
		var thtml = '';
		thtml += '<div class="addpsonpop">';
		thtml += '	<div class="addpson forremind">';
		thtml += '		<i class="sicon icon-anclose"></i>';
		thtml += '		<i class="an-remind"></i>';
		thtml += '		<div class="h50 c-999 c-f15">'+ops.con+'</div>';
		thtml += '		<a href="javascript:;" class="btn-anadd mt10">'+ops.okValue+'</a>';
		thtml += '	</div>';
		thtml += '	<div class="addpson-mb"></div>';
		thtml += '</div>';
		$("body").append(thtml);
		$('.icon-anclose').on('click',function(){
			$('.addpsonpop').remove();
		});
		$(".btn-anadd").click(function(){
			if (ops.cancel){ops.cancel();}
			$(".addpsonpop").remove();
		});
	}
}

//复选
$('.c-main').on('click','div[data-toggle=checkbox]',function(){
	var checked = $(this).find('.input-pack').hasClass('checked');
	if(checked){
		$(this).find('.input-pack').removeClass('checked').find('input').removeAttr('checked');
	}else{
		$(this).find('.input-pack').addClass('checked').find('input').attr('checked','true');
	}
});

//隐藏筛选
$('.c-main').on('click','.doc-filter li .filtertext',function(){
	if($('.filter-into').hasClass('curr')){
		$('.filter-into').removeClass('curr');
		$('#listPart').show();
		$('#filterPart').hide();	
	}
});

window.onload = function(){
	var foot = $('.footer').is('div') || $('.order-bot').is('div');
	var dept = $('.depthold').is('div');
	var result = $('.result-bot-hold').is('div');
	$('body').append('<div class="copyright"><div>福州卡思特公司提供技术支持</div></div>');
	if(foot){
		$('.copyright div').css('bottom',$('.h50').height());
	}else if(dept){
		$('.copyright').css('height','0px');
	}else if(result){
		$('.result-bot').css('bottom','30px');
		$('.copyright').css('height','0px');
	}
	
};

//document.body.addEventListener('touchstart', function () {});
