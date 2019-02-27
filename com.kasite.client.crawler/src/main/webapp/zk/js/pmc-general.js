/* general.js created by zhaoboy 2014-1-20 */

//初始化查询条件执行方法
function iniSelector() {
	$(".selector span").live("click",function(e){
		$(".selector ul").hide();
		selectorList = $(this).parent().find("ul");
		selectorList.show();
		e.stopPropagation();
		var selectorValue = $(this).html();
		selectorList.find("li").each(function(index, element) {
	      	if(selectorValue == $(this).html()) {
				$(this).addClass("selected").siblings("li").removeClass("selected");
			}
	    });
	});
	$(document).click(function(e){
		$(".selector ul").hide();
	});
	$(".selector ul li").live("click",function(){
		var selector = $(this).parent().parent(".selector");
		var selectorList = $(this).parent();
		var selectorDefault = $(this).parent().prev("span");
		var selectorTxt = $(this).html();
		var selectorValue = $(this).attr("tempValue");
		selectorList.hide();
		selectorDefault.html(selectorTxt);
		selector.attr("tempValue",selectorValue);
	});
}
/**
 * 时间对象的格式化;
 */
Date.prototype.format = function(format) {
    /*
     * eg:format="YYYY-MM-dd hh:mm:ss";
     */
    var o = {
        "M+" :this.getMonth() + 1, // month
        "d+" :this.getDate(), // day
        "h+" :this.getHours(), // hour
        "m+" :this.getMinutes(), // minute
        "s+" :this.getSeconds(), // second
        "q+" :Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" :this.getMilliseconds()
    // millisecond
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
    }

    for ( var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}
function trimBlank(str){
    return str.replace(/(^\s*)|(\s*$)/g, ""); 
}

function convert4Ajax(str){

	if(isEmptyString(str)){
		return str
	}

	str=str.replace(/\%/g, "%25");
	str=str.replace(/\&/g, "%26");

	return str
}