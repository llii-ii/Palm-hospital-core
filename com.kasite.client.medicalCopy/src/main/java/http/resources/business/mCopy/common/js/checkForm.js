checkFormJs = {
		/**验证Email**/
		isEmail:function(str){
			 if (str.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) != -1)
			        return true;
			    else
			        return false;
		},
		/**数字检查**/
		isNum:function(str){
			 return str.match(/\D/) == null;
		},
		/**中文值检测**/
		isChinese:function(name){
			 if (name.length == 0)
			        return false;
			    for (i = 0; i < name.length; i++) {
			        if (name.charCodeAt(i) > 128)
			            return true;
			    }
			    return false;
		},
		/**英文值检测**/
		isEnglish:function(name){
			if (name.length == 0)
		        return false;
		    for (i = 0; i < name.length; i++) {
		        if (name.charCodeAt(i) > 128)
		            return false;
		    }
		    return true;
		},
		
		/**判断用户名是否为数字字母下滑线**/
		username:function(str) {
		    var reg = /[^A-Za-z0-9_]/g
		    if (reg.test(str)) {
		        return (false);
		    } else {
		        return (true);
		    }
		},
		
		/**判断网址**/
		isURL:function(str_url) {
		    var re = new RegExp("((^http)|(^https)|(^ftp)):\/\/(\\w)+\.(\\w)+");
		    if (re.test(str_url)) {
		        return (true);
		    }
		    else {
		        return (false);
		    }
		},
		//开放所有号码段
		chinaMobile:function(value) {
		    /** 只验证第一位是1 **/
		    var res = /^[1]\d{10}$/;
		    var re = new RegExp(res);
		    if (re.test(value)) {
		        return true;
		    }
		    return false;
		},
		/**联通号码验证**/
		chinaUnicom:function(value) {
		    /**联通号段：130-2，155-6，185-6**/
		    var res = /^[1](3[0-2]{1}|5[5-6]{1}|8[5-6]{1})\d{8}$/;
		    var re = new RegExp(res);
		    if (re.test(value)) {
		        return true;
		    }
		    return false;
		},

		/**电信号码验证 添加170号段支持**/
		ChinaTelecomNew:function (value) {
		    /**电信号段：133，153，180，181，189	**/
		    var res = /^[1](33|53|70|77|80|81|89)\d{8}$/;
		    var re = new RegExp(res);
		    if (re.test(value)) {
		        return true;
		    }
		    return false;
		},

		/**全角转换半角   电话号码格式转化**/
		 ctoH:function(obj) {
		    var str = obj.value;
		    var result = "";
		    for (var i = 0; i < str.length; i++) {
		        if (str.charCodeAt(i) == 12288) {
		            result += String.fromCharCode(str.charCodeAt(i) - 12256);
		            continue;
		        }
		        if (str.charCodeAt(i) > 65280 && str.charCodeAt(i) < 65375)
		            result += String.fromCharCode(str.charCodeAt(i) - 65248);
		        else result += String.fromCharCode(str.charCodeAt(i));
		    }
		    obj.value = result;
		},
		/**检测日期是否正确开始**/
		dateCheck:function (str) {
		    var re = new RegExp("^([0-9]{4})[.-]{1}([0-9]{1,2})[.-]{1}([0-9]{1,2})$");
		    var ar;
		    if ((ar = re.exec(str)) != null) {
		        var i;
		        i = parseFloat(ar[1]);
		        if (i <= 0 || i > 9999)
		        { return false; }
		        i = parseFloat(ar[2]);
		        if (i <= 0 || i > 12)
		        { return false; }
		        i = parseFloat(ar[3]);
		        if (i <= 0 || i > 31)
		        { return false; }
		    }
		    else
		    { return false; }
		    return true;
		},

		isIdCardNo:function (idcard) {
		    idcard = idcard.toLocaleUpperCase();
		    var Errors = new Array(
			"验证通过!",
			"身份证号码位数不对!",
			"身份证号码出生日期超出范围或含有非法字符!",
			"身份证号码校验错误!",
			"身份证地区非法!"
			);
		    var area = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外" }
		    var idcard, Y, JYM;
		    var S, M;
		    var idcard_array = new Array();
		    idcard_array = idcard.split("");
		    /** 地区检验**/
		    if (area[parseInt(idcard.substr(0, 2))] == null) return false; /** Errors[4];
		/** 身份号码位数及格式检验**/
		    switch (idcard.length) {
		        case 15:
		            if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {
		                ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; /** 测试出生日期的合法性**/
		            } else {
		                ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/; /** 测试出生日期的合法性**/
		            }
		            if (ereg.test(idcard))
		                return true; /** Errors[0];**/
		            else return false; /** Errors[2];**/
		            break;
		        case 18:
		            /** 18位身份号码检测**/
		            /** 出生日期的合法性检查**/
		            /** 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))**/
		            /** 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))**/
		            if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
		                ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; /** 闰年出生日期的合法性正则表达式**/
		            } else {
		                ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; /** 平年出生日期的合法性正则表达式**/
		            }
		            if (ereg.test(idcard)) {/**测试出生日期的合法性**/
		                /** 计算校验位**/
		                S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
				+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
				+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
				+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
				+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
				+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
				+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
				+ parseInt(idcard_array[7]) * 1
				+ parseInt(idcard_array[8]) * 6
				+ parseInt(idcard_array[9]) * 3;
		                Y = S % 11;
		                M = "F";
		                JYM = "10X98765432";
		                M = JYM.substr(Y, 1); /** 判断校验位**/
		                if (M == idcard_array[17]) return true; /** Errors[0]; /**检测ID的校验位**/
		                else return false; /** Errors[3];**/
		            }
		            else return false; /** Errors[2];**/
		            break;
		        default:
		            return false; /** Errors[1];**/
		            break;
		    }
		},
		//非法字符过滤
		stripscript:function (s) {
		    var pattern = new RegExp("[`~!@$^&*()=|{}':;'\",\\\\[\\].<>/?~！@￥……&*（）—|{}【】‘；：”“'。，、？]");
		    var rs = "";
		    for (var i = 0; i < s.length; i++) {
		        var testStr = s.substr(i, 1);
		        if (pattern.test(testStr)) {
		            rs += testStr + " ";
		        }
		        //alert("testStr:" + testStr + " | " + pattern.test(testStr));
		    }
		    return rs;
		}
};











