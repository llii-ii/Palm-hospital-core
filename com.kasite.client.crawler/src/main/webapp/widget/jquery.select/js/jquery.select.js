;(function($){
    $.fn.formSelect=function(options){
        var defaultsettings= {
            defaultText:"-请选择-",
            jsonData:""
        };
        var opts=$.extend(defaultsettings, options);

        this.find('.cus-sel-chosed-txt').text(opts.defaultText);
        return this.each(function(){
            var _this=$(this),tempStr='';
            if(opts.jsonData) {
                for(var key in opts.jsonData){
                    tempStr+='<li><a href="javascript:;" data-value="'+key+'">'+opts.jsonData[key]+'</a></li>';
                }
                _this.children('.cus-sel-list').children('ul').append(tempStr);
            }
            _this.find(".cus-sel-chosed").live("click", function () {
                $(".cus-sel").removeClass("active");
                $(".cus-sel-list").hide();
                $(this).parent(".cus-sel").addClass("active");
                $(this).siblings('.cus-sel-list').slideDown("fast");
            });
            
            _this.children(".cus-sel-list").delegate("a", "click", function () {
                $(this).closest(".cus-sel").removeClass("active");
                $(this).closest(".cus-sel-list").siblings(".cus-sel-chosed").find(".cus-sel-chosed-txt").text($(this).text()).attr("data-value", $(this).attr("data-value"));
                $(this).closest(".cus-sel-list").slideUp("fast");
                return false;
            });

            $("html,body").click(function (e) {
                var target = e.target;
                if ($(target).parents(".cus-sel").length == 0) {
                    $(".cus-sel").removeClass("active"); $(".cus-sel-list").hide();
                }
            });
        });
    }
})(jQuery);