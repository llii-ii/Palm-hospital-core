
	var session = null;
	var MenuBtnMap = null;//按钮权限
	var url = document.location.pathname;
	var itmp = url.indexOf("/", 1);
	var webpath = '';
	
	var getSessionUrl = '/wsgw/getSession.do';
	
	Commonjs = {
//		/**
//		 * 医院ID
//		 * @return {} 
//		 */
//		hospitalId : '1024727',
//		hospitalName: '健康之路演示医院',
//		/**
//		 * 
//		 * @param {Object} url
//		 * @param {Object} param
//		 * @param {Object} flag
//		 * @return {TypeName} 
//		 */
//		getRootPath : window.location.protocol + '//' + window.location.host + webpath,
//		getRoot : window.location.protocol + '//' + window.location.host,
//		ajax :function (url,param,flag){
//			var obj = null;
//			$.ajax({
//				type: 'POST',
//				url: url+'?v='+(new Date().getTime()),
//				data: param,
//				async: flag,
//				timeout : 6000,
//				cache : false,
//				dataType: 'json',
//				success: function(data){
//					obj = data;
//	 			}
//			
//			});
//			if(!flag) return obj;
//		},
//		jsonAjax :function (url,param,flag){
//			var obj = null;
//			$.ajax({
//				type: 'POST',
//				url: url+'?v='+(new Date().getTime()),
//				contentType:"application/json",
//				data: param,
//				async: flag,
//				timeout : 6000,
//				cache : false,
//				dataType: 'json',
//				success: function(data){
//					obj = data;
//	 			}
//			
//			});
//			if(!flag) return obj;
//		},
		trim : function(str) {
			return str.replace(/(^\s*)|(\s*$)/g, "");
		},
		getUrlParam : function (val) {
			var uri = window.location.search;
			var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
			return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
		},
		isEmpty : function(s){
			if(s == undefined){
	  			return true;
	  		}else{
	  			if(s == null || s == '' || s == 'undefined' 
	  				|| s == 'null' || s.length < 1){
	  				return true;
	  			}
	  		}
	  		return false;
		},
		getUrl : function(model){
			if(!this.isEmpty(model)){
	  			return '../'+model+'_doAll.do';
	  		}else{
	  			return '';
	  		}
		},
		getReqParams : function(data){
			var params = {};
			params.Req = {'TransactionCode':''};
			params.Req.Data = {};
			if(!this.isEmpty(data)){
				params.Req.Data = data;
			}
			return JSON.stringify(params);
		},
		getParams : function(headCode,data,page){
			var params = {};
			params.Req = {};
			params.Req.Data = {};
			if(!this.isEmpty(headCode)){
				params.Req.TransactionCode = headCode;
			}
			if(!this.isEmpty(data)){
				params.Req.Data = data;
			}
			if(!this.isEmpty(page)){
				params.Req.Data.Page = page;
			}
			return params;
		},
		getApiReqParams : function(data,page,headCode){
			var apiReqParams = {};
			apiReqParams.Req = {};
			apiReqParams.Req.Data = {};
			if(!this.isEmpty(headCode)){
				apiReqParams.Req.TransactionCode = headCode;
			}
			if(!this.isEmpty(data)){
				apiReqParams.Req.Data = data;
				if(!this.isEmpty(page)){
					apiReqParams.Req.Data.Page = {};
					apiReqParams.Req.Data.Page = page;
				}
			}
			return this.jsonToString(apiReqParams);
		},
		jsonToString : function(obj){
		var THIS = this;   
     	switch(typeof(obj)){  
        	case 'string':  
            	return '"' + obj.replace(/(["\\])/g, '\\$1') + '"';  
            case 'array':  
                return '[' + obj.map(THIS.jsonToString).join(',') + ']';  
            case 'object':  
                if(obj instanceof Array){
              		var strArr = [];  
                	var len = obj.length;  
              		for(var i=0; i<len; i++){  
                 		strArr.push(THIS.jsonToString(obj[i]));  
                	}  
               		return '[' + strArr.join(',') + ']';  
             	}else if(obj==null){  
               		return 'null';  
            	}else{  
             		var string = [];  
                  	for (var property in obj) string.push(THIS.jsonToString(property) + ':' + THIS.jsonToString(obj[property]));  
               		return '{' + string.join(',') + '}';  
           		}  
        		case 'number': return obj;  
                case false: return obj;  
	     	}
	   	},
	   	ListIsNull : function(obj) {
		    if (obj == null || obj == undefined || obj.length < 1) {
		        return false;
		    }
		    if (obj instanceof Array) {
		        if (obj.length > 0) {
		            return true;
		        } else {
		            return false;
		        }
		    } else {
		        if (obj != null && obj != undefined) {
		            return true;
		        } else {
		            return false;
		        }
		    }
		},
		isNull : function(param) {
			if(typeof(param) == undefined || typeof(param) == 'undefined' || param == null || param ==='') 
				return true;
			else 
				return false; 
		},
	   	BaseForeach :  function(obj, fun) {
		    if (obj == null || obj == undefined || obj.length < 1) {
		        return;
		    }
		    if (obj instanceof Array) {
		        for (var i = 0; i < obj.length; i++) {
		            fun(i, obj[i]);
		        }
		    } else {
		        fun(0, obj);
		    }
		},
		getOrgId:function(){
			var session = this.getSession();
			return session.orgId;
		},
		getClientId:function(){
			var clientId = this.getSession().clientId;
			var arr = clientId.split(".");
			if(arr.length > 1){
				return arr[1];
			}
			return this.getSession().clientId;
		},
		//返回当前登录用户的机构名称
		hospitalName:function(){
			var session = this.getSession();
			return session.orgName;
		},
		//返回当前登录用户的userID
		getUserID:function(){
			var session = this.getSession();
			console.log(session);
			return session.sign;
		},
		//返回当前登录用户的userName
		getUserName:function(){
			var session = this.getSession();
			return session.sign;
		},
		getToken:function(){
			return this.getCookie('token');
		},
		ajaxCallBackFunction:function(data){
			if(data.code == 40000 || data.RespCode == -14019 || data.code == 401 || data.code == 20000){
				var msg = data.RespMessage;
				if(msg){
					if(data.msg){
						msg = msg + "|"+ data.msg;
					}
				}
				window.location = '/business/pageError.html?errorcode='+data.RespCode+'&token='+this.getToken()+'&msg='+encodeURI(msg);
			}
		},
		getSession:function(isRefresh){
			if((this.isEmpty(isRefresh) || isRefresh!=1) && window.localStorage){
				var token = this.getToken();
				var storage = window.localStorage;
				var stoken = storage.getItem("token");
				if(stoken == token){
					var json=storage.getItem("data");
		            var jsonObj=JSON.parse(json);
		            return jsonObj;
				}
			}
			var result = null;
			$.ajax({
				type: 'POST',
				url: getSessionUrl,
				async: false,
				headers: {
			        'token': this.getToken()
			    },
				dataType: 'json',
				success: function(data){
					Commonjs.ajaxCallBackFunction(data);
					result = data;
					if(window.localStorage){
						var storage=window.localStorage;
						var d=JSON.stringify(data);
						storage.setItem("data",d);
						storage.setItem("token", token);
					}
				}
			});
			if(null != result && result.RespCode == 10000){
				return result;
			}else if(null != result && result.RespCode != 10000){
            	this.alert('网络错误,请刷新后重试:'+result.msg);
			}
		}, setCookie:function (a, b, c) {
			var d = new Date;
			d.setDate(d.getDate() + c), cookieVal = a + "=" + escape(b) + (null == c ? "" : ";expires=" + d.toGMTString()), document.cookie = cookieVal;
		},  getCookie:function (a) {
			return document.cookie.length > 0 && (c_start = document.cookie.indexOf(a + "="), -1 != c_start) ? (c_start = c_start + a.length + 1, c_end = document.cookie.indexOf(";", c_start), -1 == c_end && (c_end = document.cookie.length), unescape(document.cookie.substring(c_start, c_end))) : "";
		}, delCookie:function (a) {
			var b = new Date;
			b.setTime(b.getDate() - 10000), this.getCookie(a) && (document.cookie = a + "=v; expires=" + b.toGMTString());
		},updateSession : function(newSession,url) {
	   		var param = {};
	   		param.Session=JSON.stringify(newSession);
	   		if(Commonjs.isEmpty(url)){
	   			this.ajax('../login/updateSession.do',param,false);
   			}else{
   				session = this.ajax(url,param,false);
   			}
	   		Session = newSession;
	   	}, 
	   	/**
	   	 * 获取渠道下拉列表
	   	 * @param {Object} elementid (select的id)
	   	 * @param {Object} selval (选中值)
	   	 * @return {TypeName} 
	   	 */
	   	loadChannelSelect : function(elementid, selval) {
			var param = {};
			var Service = {};
			Service.key = "";
			var code = 91000;
			var params = Commonjs.getParams(code,Service);//获取参数
			param.Api = "GetChannelSelect";
			param.Params = Commonjs.jsonToString(params);
			var d = Commonjs.ajax('./channel_callDemoApi.do',param,false);
			if(d.RespCode != 10000){
				return;
			}
			var options = '';
			options += "<option value=''>--请选择--</option>";
			$.each(d.Data,function(i,item){
				options += "<option value='" + item.ChannelId + "'>" + item.ChannelName + "</option>";
			});
			$("#"+elementid).empty(); 
			$("#"+elementid).append(options);
			
			if(!Commonjs.isEmpty(selval)) {
				$("#"+elementid).val(selval); 
			}
		},
		resizeAllPage : function() {
			var winHeight=$(window).height();
			$(".aside .scroll-pane").height(winHeight-61).jScrollPane({"autoReinitialise": true,"overflow":true});
			if (!($.browser.msie && ($.browser.version == "6.0") && !$.support.style)) {
				$(".container-wrap>.scroll-pane").height(winHeight-111).jScrollPane({"autoReinitialise": true});
			}else{
				$(".container-wrap>.scroll-pane").height(winHeight-111).width($(window).width()-195);
			}
		},
		/**
		 * 某个日期的相加减
		 */
		getDateAdd : function(dateStr, day) {
			dateStr = dateStr.replace(/-/g,"/");
			var thisDate = new Date(dateStr);
			var targetday_milliseconds = thisDate.getTime() + 1000 * 60 * 60 * 24 * day;
			thisDate.setTime(targetday_milliseconds); // 注意，这行是关键代码
			var tYear = thisDate.getFullYear();
			var tMonth = thisDate.getMonth();
			var tDate = thisDate.getDate();
			tMonth = Commonjs.doHandleMonth(tMonth + 1);
			tDate = Commonjs.doHandleMonth(tDate);
			return tYear + "-" + tMonth + "-" + tDate;
		},
		getDate : function(day) {
			var today = new Date();
			var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
			today.setTime(targetday_milliseconds); // 注意，这行是关键代码
			var tYear = today.getFullYear();
			var tMonth = today.getMonth();
			var tDate = today.getDate();
			tMonth = Commonjs.doHandleMonth(tMonth + 1);
			tDate = Commonjs.doHandleMonth(tDate);
			return tYear + "-" + tMonth + "-" + tDate;
		},
		getMonth : function(month) {
			var today = new Date();
			var tYear = today.getFullYear();
			var tMonth = today.getMonth();
			tMonth = Commonjs.doHandleMonth(tMonth + 1 + month);
			return tMonth;
		},
		getYear : function(year) {
			var today = new Date();
			var tYear = today.getFullYear();
			return Commonjs.doHandleMonth(tYear + year);
		},
		/**
		 * 获取某月的天数
		 */
		getMonthDays:function(nowYear, myMonth){
			var thisDate = new Date(nowYear, myMonth, 0);
			return thisDate.getDate();
		},
		/**
		 * 时间格式化
		 */
		formatDate:function(date){
			var tYear = date.getFullYear();
			var tMonth = date.getMonth()+1;
			var tDate = date.getDate();
			tMonth = tMonth < 10 ?"0"+tMonth : tMonth;
			tDate = tDate < 10 ? "0"+tDate : tDate;
			return tYear + "-" + tMonth + "-" + tDate;
		},
		/**
		 * 某月开始日期
		 */
		getBeginDateForMonth : function(nowYear, nowMonth, startDate) {
			var monthStartDate = new Date(nowYear, nowMonth-1, startDate); 
			return Commonjs.formatDate(monthStartDate);
		},
		/**
		 * 某月结束日期
		 */
		getEndDateForMonth : function(nowYear, nowMonth) {
			var monthEndDate = new Date(nowYear, nowMonth-1, Commonjs.getMonthDays(nowYear, nowMonth)); 
			return Commonjs.formatDate(monthEndDate);
		},
		doHandleMonth : function(month){
			var m = month;
			if (month.toString().length == 1) {
				m = "0" + month;
			}
			return m;
		},
		/**
		 * icon不传则为提示错误信息, 成功icon传add
		 * 
		 * @param {Object} msg
		 * @param {Object} icon
		 */
		alert: function(msg,icon) {
			var ic = icon;
			if(icon==undefined||Commonjs.isEmpty(icon)) {
				ic = 'error';
			}
			art.dialog({
				lock : true,
				artIcon : ic,
				opacity : 0.4,
				width : 250,
				title : '提示',
				time : 3,
				content : msg,
				ok : function() {
		
				}
			});	
		},
		/**
		 * 获取客户端操作系统
		 * @return {TypeName} 
		 */
		getOS : function() {
			var sUserAgent = navigator.userAgent; 
			var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows"); 
			var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel"); 
			if (isMac) return "Mac"; 
			var isUnix = (navigator.platform == "X11") && !isWin && !isMac; 
			if (isUnix) return "Unix"; 
			var isLinux = (String(navigator.platform).indexOf("Linux") > -1); 
			
			var bIsAndroid = sUserAgent.toLowerCase().match(/android/i) == "android";
			if (isLinux) {
			if(bIsAndroid) return "Android";
			else return "Linux"; 
			}
			if (isWin) { 
			var isWin2K = sUserAgent.indexOf("Windows NT 5.0") > -1 || sUserAgent.indexOf("Windows 2000") > -1; 
			if (isWin2K) return "Win2000"; 
			var isWinXP = sUserAgent.indexOf("Windows NT 5.1") > -1 || 
			sUserAgent.indexOf("Windows XP") > -1; 
			if (isWinXP) return "WinXP"; 
			var isWin2003 = sUserAgent.indexOf("Windows NT 5.2") > -1 || sUserAgent.indexOf("Windows 2003") > -1; 
			if (isWin2003) return "Win2003"; 
			var isWinVista= sUserAgent.indexOf("Windows NT 6.0") > -1 || sUserAgent.indexOf("Windows Vista") > -1; 
			if (isWinVista) return "WinVista"; 
			var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 7") > -1; 
			if (isWin7) return "Win7"; 
			} 
			return "other"; 
		},
		checkOS : function() {
			var os = Commonjs.getOS();
			if(os=='Win2000'||os=='WinXP'||os=='Win2003') 
				return true;
			return false;
		},
		/**
		 * 滚动条对象
		 * 
		 * @param {Object} formId
		 */
		getJscrollpane : null,
		/**
		 * @param {Object} menuId
		 * 设置改界面按钮的状态，建议在$(function(){最后代码添加})、还有在手动js生成html的时候添加
		 */
	   	btnPermissionControl:function(menuId){
			if ( MenuBtnMap == null ){
				//加载按钮数据
				var map = {};
				var menuData = Commonjs.getSession().Data;
				if(menuData.length!=undefined) {
					for(var i = 0; i < menuData.length; i++){
						var btnArray = menuData[i].BtnPermissionData;
						if(btnArray!=undefined && btnArray.length>0){
							var tempMenuId = btnArray[0].MenuId;
							map[tempMenuId] = btnArray;
						}
//						btnJson = btnJson.substring(9,btnJson.length);
//						btnJson = btnJson.substring(0,btnJson.length-3);
//						var btnArray = eval('(' + btnJson + ')');
//						if(btnArray.length>0){
//							var menuId = btnArray[0].menuId;
//							map[menuId] = btnArray;
//						}
					}	
				}
				MenuBtnMap = map;
			}
			//循环判断
			if( MenuBtnMap[menuId].length>0 ){
				for(var i = 0; i < MenuBtnMap[menuId].length; i++){
					var btn = MenuBtnMap[menuId][i];
					if(btn.State == 0){
						$("[btnPermission='"+btn.ButtonType+"']").unbind('click');
						$("[btnPermission='"+btn.ButtonType+"']").removeAttr('onclick');
						}
				}
			}
		},
		//用于兼容对象和对象数组方法，返回对象数组
		getObjArray:function (obj) {
			if (obj == null || obj == undefined) {
				return;
			}
			if (obj instanceof Array) {
				return obj;
			} else {
				// 把一个的对象放进数组里面返回
				var o = new Array();
				o.push(obj);
				return o;
			}
		},
		makeParam : function (api,transactionCode,service,pageIndex,pageSize){
			var obj = {};
	   		var params = {};
	   		var req = {};
	   		var page = {};
	   		page.pIndex = pageIndex;
	   		page.pSize = pageSize;
	   		obj.api = api;
	   		req.transactionCode = transactionCode;
	   		req.service = service;
	   		req.page = page;
	   		params.req = req;
	   		obj.params=params;
	   		return $.toJSON(obj);
		},
//		ajaxJson : function (url,param){
//			var result;
//			$.ajax({
//				type: 'POST',
//				url: url+'?v='+(new Date().getTime()),
//				data: param,
//				async: false,
//				timeout : 6000,
//				contentType: "application/json", //必须有
//				cache : false,
//				dataType: 'json',
//				success: function(data){
//					result = data;
//	 			}
//			});
//			return result;
//		 },
//		getOperatorId : function (){
//			var result = this.getSession("../../login/getSession.do");
//			
//			return result.UserID;
//		},
//		 getOperatorName : function (url,param){
//				var result = this.getSession("../../login/getSession.do");
//				
//				return result.Name;
//		 },
//		 getUserID : function (url,param){
//			 var result = this.getSession("../../login/getSession.do");
//		
//			 return result.UserID;
//		 },
		//开放所有号码段
		isChinaMobile:function(value) {
		     /** 只验证第一位是1 **/
		     var res = /^[1]\d{10}$/;
		     var re = new RegExp(res);
		     if (re.test(value)) {
		         return true;
		     }
		     return false;
		 },
		 /**
			 * cardNoStr:真实卡号
			 * 			截取规则:卡号长度等于36位，截取第6到第14位共8位数字出来
			 * */
		 subCardNo : function(cardNoStr){
			 if(cardNoStr == undefined){
				 return cardNoStr;
			 }
			 if(cardNoStr.length == 36){
				 return cardNoStr.substring(5,13);
			 }else{
				 return cardNoStr;
			 }
		    },
		 /**
			 * 价格:分转元
			 * */
			centToYuan:function (val){
				var test = /^(\-|\+)?\d+(\.\d+)?$/;
				if(Commonjs.isEmpty(val)){
					return 0;
				}else if(test.test(val)){
					with(Math){
					var t1=0,t2=0,r1,r2;
					try{t1=val.toString().split(".")[1].length}catch(e){}
					r1=Number(val.toString().replace(".",""));
					return r1 /pow(10,2+t1);
				}}
				return 0;
			},
			ajaxDefult : {
				type : 'post',
				async : true,
				timeout : 30000,
				cache : false,
				dataType : 'json',
				beforeSend : function(xhr){
					
				},
				error : function(){
					
				}
			},
			ajax :function (url,param,success,other){
				var option = $.extend({},this.ajaxDefult,other);
				$.ajax({
					type: option.type,
					url: url+'?v='+(new Date().getTime()),
					data: param,
					async: option.async,
					timeout : option.timeout,
					cache : option.cache,
					dataType: option.dataType,
					headers: {
						'token': this.getToken()
					},
					beforeSend:option.beforeSend,
					success: function(data){
						Commonjs.ajaxCallBackFunction(data);
						if(success && typeof success =='function'){
							success(data);
		 				}
		 			},
		 			error:option.error
				});
			},
			ajaxSync :function (url,param,success,other){
				var option = $.extend({},this.ajaxDefult,other);
				$.ajax({
					type: option.type,
					url: url+'?v='+(new Date().getTime()),
					data: param,
					async: false,
					timeout : option.timeout,
					cache : option.cache,
					dataType: option.dataType,
					headers: {
						'token': this.getToken()
					},
					beforeSend:option.beforeSend,
					success: function(data){
						Commonjs.ajaxCallBackFunction(data);
						if(success && typeof success =='function'){
							success(data);
		 				}
		 			},
		 			error:option.error
				});
			},
			/**
			  * 从当前日期加减日期
			  * type: Y-加年 M-加月 D-加天
			  * addNum:加减的数量
			  * */
			addDateFromNow : function(dateStr, type, addNum){
				 var date = new Date();
				 if(!Commonjs.isEmpty(dateStr)){
					 date = new Date(dateStr);
				 }
				 if(type == 'Y'){
					 date.setFullYear(date.getFullYear() + addNum);
					 var tYear = date.getFullYear();
					 return tYear;
				 }else if(type == 'M'){
					 date.setMonth(date.getMonth() + 1 + addNum);
					 var tYear = today.getFullYear();
					 var tMonth = today.getMonth();
					 return tYear + "-" + tMonth;
				 }else  if(type == 'D'){
					 var targetday_milliseconds = date.getTime() + 1000 * 60 * 60 * 24 * addNum;
					 date.setTime(targetday_milliseconds); // 注意，这行是关键代码
					 var tYear = today.getFullYear();
					 var tMonth = today.getMonth();
					 var tDate = today.getDate();
					 return tYear + "-" + tMonth + "-" + tDate;
				 }
				 return date;
		 },
	}
	



