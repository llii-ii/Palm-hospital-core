var YiHu = {
    Util: {
        returnBack: function(appType) {
            if (appType == 1) {
                //调用android后台方法
                window.javatojs.returnBack();
            } else {
                document.location = "objc://returnBack";
            }
        },
        isDate: function(sm, mystring) {
            var reg = /^(\d{4})-(\d{2})-(\d{2})$/;
            var str = mystring;
            if (str == "") return true;
            if (!reg.test(str) || RegExp.$2 > 12 || RegExp.$3 > 31) {
                YiHu.Util.confale("请保证" + sm + "中输入的日期格式为yyyy-mm-dd或正确的日期!", false, function() {
                });
                return false;
            }
            return true;
        },
        loadDialog: function() {
            if ($("#openDiv").length > 0) {
                $("#openDiv").empty();
            } else {
                $(document.body).append("<div id=\"openDiv\"/>");
            }
            var htmlDiv = "<div class=\"load_box\" style=\"text-align:center;\"><p><img src=\"/images/uploading.gif\" style=\"padding-bottom:5px;\">" +
                "</p><p>加载中…</p></div><div class=\"al_screen\"></div>";
            $("#openDiv").append(htmlDiv);
            var totH = $(window).height();
            var totW = $(window).width();
            var aleT = (totH - $(".conf_box").height()) / 2;
            var aleL = (totW - $(".conf_box").width() - 100) / 2;
            $(".load_box").css({ "top": aleT + "px", "left": aleL + "px", "position": "fixed" });
            $(".al_screen").css({ "top": "80px" });

            return false;
        },
        //日期转化自定义字符类型
        dataTimeToStr: function(dateTimeStr) {
            var hourMinuteStr = dateTimeStr.substring(11, 16);
            var dateStr = dateTimeStr.substring(0, 10).replace(/-/g, '/');
            var now = new Date();
            var value = Date.parse(dateStr) - Date.parse(now.getFullYear() + "/" + (now.getMonth() + 1) + "/" + now.getDate());
            var returnValue = dateTimeStr;
            if (value == 0) {
                returnValue = hourMinuteStr;
            }
            if (value == -86400000) {
                returnValue = "昨天 " + hourMinuteStr;
            }
            if (value < -86400000) {
                returnValue = dateTimeStr.substring(5, 10) + " " + hourMinuteStr;
            }
            if (new Date(dateStr).getFullYear() < now.getFullYear()) {
                returnValue = dateTimeStr.substring(2, 10) + " " + hourMinuteStr;
            }
            return returnValue;
        },
        dateToStr: function(dateStr) {
            dateStr = dateStr.replace(/-/g, '/');
            var now = new Date();
            var value = Date.parse(dateStr) - Date.parse(now.getFullYear() + "/" + (now.getMonth() + 1) + "/" + now.getDate());
            var returnValue = "";
            if (new Date(dateStr).getFullYear() < now.getFullYear()) {
                returnValue = dateStr.substring(2, 10);
            } else {
                returnValue = dateStr.substring(5, 10).replace(/\//, "月") + "日";
            }
            return returnValue;
        },
        compareDate: function(startDate, endDate) {
            var date1 = startDate.replace(/-/g, "");
            var date2 = endDate.replace(/-/g, "");
            var result = date1 - date2;
            return result;
        },
        timeToStr:function(timeStr) {
            timeStr = timeStr.replace(/:/g, '点');
            return timeStr.replace("00", "0");
        },
        addDate: function (date, days) {
            var d = new Date(date);
            d.setDate(d.getDate() + days);
            var m = d.getMonth() + 1;
            var day = d.getDate().toString();
            return d.getFullYear() + '-' + (m.toString().length > 1 ? m : "0" + m) + '-' + (day.toString().length > 1 ? day : "0" + day);
        },addMinutes: function (date, minutes) {
            var d = new Date(date);
            d.setMinutes(d.getMinutes() + minutes);
            return d;
        },
        getDayStrByDay:function(day) {
            switch (day) {
                case 0:
                    return "周日";
                    break;
                case 1:
                    return "周一";
                    break;
                case 2:
                    return "周二";
                    break;
                case 3:
                    return "周三";
                    break;
                case 4:
                    return "周四";
                    break;
                case 5:
                    return "周五";
                    break;
                case 6:
                    return "周六";
                    break;
                default:
            }
            return "";
        },
        getTimeByTimeId:function(timeId) {
            switch (timeId) {
            case 1:
                return "上午";
            case 2:
                return "下午";
            case 3:
                return "晚上";
            default:
        }
        return "";
        },
        getDayStr: function (date) {
            var day = date.getDay();
            switch (day) {
                case 0:
                    return "周日";
                    break;
                case 1:
                    return "周一";
                    break;
                case 2:
                    return "周二";
                    break;
                case 3:
                    return "周三";
                    break;
                case 4:
                    return "周四";
                    break;
                case 5:
                    return "周五";
                    break;
                case 6:
                    return "周六";
                    break;
                default:
            }
            return "";
        },
        confale: function (content, isHasCancle, sureFn, isLoad) {
            if ($("#openDiv").length ==0) {
                $(document.body).append("<div id=\"openDiv\"/>");
            }
            var $openDiv = $("#openDiv");
            $openDiv.empty();

            var str = "";
            str += "<div class=\"conf_box\">";
            str += "<div class=\"confcontent\" >" + content + "</div>";
            if (!isLoad)
                str += "<div class=\"confbtn\"><a href=\"javascript:;\" class=\"surebtn\">确定</a>";

            if (isHasCancle) {
                str += "<a href=\"javascript:;\" class=\"canclebtn\">取消</a>";
            }
            if (!isLoad)
                str += "</div></div>";
            else {
                str += "</div>";
            }
            str += "<div class=\"conf_screen\"></div>";
            $openDiv.html(str);

            var $surebtn = $(".surebtn");
            if (!isHasCancle)
                $surebtn.css({ "float": "none", "margin": "auto" });
            if (isLoad)
                $(".confcontent").css({ "color": "transparent" });
            //定位
            var winH = $(window).height();
            var boxH = $(".conf_box").height();
            var marT = winH - boxH - 20;
            $(".conf_screen").css({ 'height': $(document).height(), "position": "fixed" });
            $(".conf_box").css({ 'top': marT / 2 + "px", "position": "fixed" });

            $surebtn.click(function () {
                if (sureFn)
                    sureFn.apply(null);
                $("#openDiv").empty();
            });

            $(".canclebtn").click(function () {
                $("#openDiv").empty();
            });
        },
        showNoData: function () {
            YiHu.Util.confale("暂无数据", false, function () {
            });
        },
        //手机验证
        checkIsTelephoneWide: function (strValue) {
            if (!strValue || strValue == "") return false;
            var telephoneReg = /^1[3|4|5|8][0-9]\d{8}$/;
            var reg = strValue.match(telephoneReg);
            if (reg) {
                return true;
            } else {
                return false;
            }

        },htmlspecialchars: function (a) {
            return a.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&apos;").replace(/ /g, "&nbsp;");
        },
        setCookie: function (name, value) {
        	
            var largeExpDate = new Date();
            
            largeExpDate.setTime(largeExpDate.getTime() + (30 * 1000 * 3600 * 24));
            
            document.cookie = name + "=" + escape(value) + ";expires=" + largeExpDate.toGMTString();
        },
        getCookie: function (name) {
            var search = name + "=";
            if (document.cookie.length > 0) {
                var offset = document.cookie.indexOf(search);
                if (offset != -1) {
                    offset += search.length;
                    var end = document.cookie.indexOf(";", offset);
                    if (end == -1) end = document.cookie.length;
                    return unescape(document.cookie.substring(offset, end));
                } else return "";
            }
            return "";
        },
        getClipboardData:function() {
            return window.clipboardData.getData("Text");
        },
        getClipboardHtml:function() {
            var $oDiv = $(document.getElementById("divTemp"));
            if ($oDiv.length==0) {
                $(document.body).append("<div id=\"divTemp\" style=\"display:none\"><div>");
            }
            $oDiv = $(document.getElementById("divTemp"));
            $oDiv.html("");

            var oTextRange = document.body.createTextRange();
            oTextRange.moveToElementText($oDiv[0]);
            oTextRange.execCommand("Paste");

            var sData = $oDiv[0].html();
            $oDiv[0].html("");

            return sData;
        },getImageSize:function(imageSrc) { 
var i = new Image(); //新建一个图片对象 
i.src = imageSrc; //将图片的src属性赋值给新建图片对象的src 
while (i.readyState != "complete") {
    
}
return new Array(i.width, i.height); //返回图片的长宽像素 
} 
    }
};
try{
	$.ajaxSetup({cache:false});
}catch(e){
	try{
		console.log("init ajaxSetup false");
	}catch(e){}
	
}