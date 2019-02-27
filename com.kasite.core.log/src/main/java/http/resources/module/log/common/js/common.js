/**
 * 公共js
 */
// 模块名称注册
if (jkzl == null)
	var jkzl = {};
if (jkzl.common == null)
	jkzl.common = {};

var url = document.location.pathname;
var itmp = url.indexOf("/", 1);
var webpath = itmp < 0 ? url : url.substr(0, itmp);
if (webpath.indexOf('/') == -1) {
	webpath = '/' + webpath;
}

jkzl.common = {
	//一级路径
	path : window.location.protocol + '//' + window.location.host + webpath,
					
	
	
	/**
	 * 返回URL中的参数值，类似JSP中的request.getParamter('id'); 
	 * 用法：var strGetQuery =document.location.search; var id = pcs.common.getQueryString(strGetQuery,'id');
	 */
	getQueryString : function (url,name){
		var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
		if (reg.test(url)){
			return unescape(RegExp.$2.replace(/\+/g, " "));
		}
		return "";
	},
	
	/**
	 * 去除多余的字用指定符号代替
	 */
	ellipsis : function(str, maxlength, suffix) {
		if (str.length <= maxlength) {
			return str;
		} else {
			return str.substr(0, maxlength - 0) + suffix
		}
	},
	

	/**
	 * 选中所有指定name的组件
	 */
	allCheck : function(name) {
		$("input[@name=" + name + "]").each(function() {
					$(this).attr("checked", true);
				});
	},
	
	/**
	 * 去除所有指定name的组件
	 */
	desCheck : function(name) {
		$("input[@name=" + name + "]").each(function() {
					$(this).attr("checked", false);
				});
	},
	
	/**
	 * 字符串中指定子字符串按指定样式显示
	 */ 
	setStringHtmlCss : function(t, s, h, c) {
		return t.replace(eval('/' + s + '/g'), '<' + h + ' class="' + c + '">'	+ s + '</' + h + '>');
	},
	
	/**
	 * 	imgId:随机码显示位置的id；xtype;显示类型eg,src,value；def_SessionName：定义一个session名字，空则默认
	 */
	getRandcode : function(imgId, xtype, def_SessionName) {
															
		$('#' + imgId).attr(xtype,"/public/rndcode.jsp?" + Math.random() + '&sessionName=' + def_SessionName);
	},
	
	/**
	 * 取Cookie
	 */ 
	getCookie : function(name) {
		var strCookie = document.cookie;
		var arrCookie = strCookie.split("; ");
		for (var i = 0; i < arrCookie.length; i++) {
			var arr = arrCookie[i].split("=");
			if (arr[0] == name) {
				if (arr[1] == '' || arr[1] == null || arr[1] == undefined) {
					return "";
				} else {
					return arr[1];
				}
			}
		}
		return "";
	},
	isNull:function(param){	//判断对象是否为空
		if(typeof(param) == undefined || typeof(param) == 'undefined' || param == null || param == '') 
			return true;
		else 
			return false; 
	},
	/**
	 * 过滤js脚本和html标签
	 */ 
	noHTML : function(htmlString) // 去除HTML标记
	{
		htmlString = htmlString.replace(/\\&[a-zA-Z]{1,10};/ig, '');
		htmlString = htmlString.replace(/<[^>]*>/ig, '');
		htmlString = htmlString.replace(/[(\/>)<]/ig, '');
		return htmlString;
	},
	/**
	 * 取得浏览器类型
	 */
	getBrowser : function() {
		var browser;
		if ($.browser.msie) {
			browser = "msie";
		} else if ($.browser.safari) {
			browser = "safari";
		} else if ($.browser.mozilla) {
			browser = "mozilla";
		} else if ($.browser.opera) {
			browser = "opera";
		} else {
			browser = "unknown";
		}
		return browser;
	},
	/**
	 * 获取指定天数的日期
	 */
	getDate : function(day) {
		var today = new Date();
		var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
		today.setTime(targetday_milliseconds); // 注意，这行是关键代码
		var tYear = today.getFullYear();
		var tMonth = today.getMonth();
		var tDate = today.getDate();
		tMonth = jkzl.common.doHandleMonth(tMonth + 1);
		tDate = jkzl.common.doHandleMonth(tDate);
		return tYear + "-" + tMonth + "-" + tDate;
	},
	doHandleMonth : function(month){
		var m = month;
		if (month.toString().length == 1) {
			m = "0" + month;
		}
		return m;
	},
    /**
	 * 获取URL里的参数值
     * @param val
     * @returns {*}
     */
    getUrlParam : function (val) {
        var uri = window.location.search;
        var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
        return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
    },
	/**
	 * 功能：转换时间格式
	 * 描述: datetime 为输入时间，format 为时间格式
	 */
	toChar:function(datetime, format) {
		if(datetime=="" || datetime==null || datetime==undefined){
			return "";
		}else{
			var date = new Date(datetime);
			var yyyy = date.getFullYear();
			var mm = date.getMonth()+1;
			var dd = date.getDate();
			var hh24 = date.getHours();
			var mi = date.getMinutes();
			var ss = date.getSeconds();
			var s1 = format.replace(/yyyy|YYYY/g, yyyy);
			var s2 = s1.replace(/mm|MM/g,mm<10 ? "0" + mm : mm);
			var s3 = s2.replace(/dd|DD/g,dd<10 ? "0" + dd : dd);
			var s4 = s3.replace(/hh24|HH24/g,hh24<10 ? "0" + hh24 : hh24);
			var s5 = s4.replace(/mi|MI/g,mi<10 ? "0" + mi : mi);
			var s6 = s5.replace(/ss|SS/g,ss<10 ? "0" + ss : ss);
			return s6;
		}		
	},
	
	/**
	 * 功能：判断是否为数字
	 * 描述：
	 * numstr：需要验证的字符串
	 * 用法：
	 * isNumber('123');//返回:true;
	 */
	isNumber:function(numstr) {
		var i,j,strTemp; 
		strTemp = "0123456789"; 
		if (numstr.length== 0)	{ return false; }
		for (i=0;i<numstr.length;i++) {
			j = strTemp.indexOf(numstr.charAt(i)); 
			if (j == -1)return false; 
		} 
		return true; 
	},
	
	
	/**
	 * 字符串中空格转为&nbsp;
	 */
	toNbsp : function(str){
		var result; 
		result = str.replace(/\s/g, "&nbsp;"); 
		return(result); 	
	},
	
	trim : function trim(str){
		return str.replace(/(^\s*)|(\s*$)/g, "");  
	},  
	ltrim : function ltrim(str){
		return str.replace(/(^\s*)/g,"");  
	} , 
	rtrim : function rtrim(str){
		return str.replace(/(\s*$)/g,"");  
	}
};
