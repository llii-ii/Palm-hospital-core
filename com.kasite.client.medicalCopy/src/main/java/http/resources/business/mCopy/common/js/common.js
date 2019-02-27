	var url = document.location.pathname;
	var itmp = url.indexOf("/", 1);
	var isPay = false;
	var webpath = '';
	var getSessionUrl = '../../wsgw/getSession.do';
	var getWebAppUrl = '../../wsgw/getWebAppMenu.do';
	var logoUrlObj = {
		1024727 :"http://wx.qlogo.cn/mmhead/Q3auHgzwzM4LJVdqUh3KZiarAJ5ECBhXib6pEEiaofpjESn0IG4DyRXOg/0",
		59 :"http://wx.qlogo.cn/mmhead/Q3auHgzwzM4LJVdqUh3KZiarAJ5ECBhXib6pEEiaofpjESn0IG4DyRXOg/0",
	}
	var logoUrl = "http://wx.qlogo.cn/mmhead/Q3auHgzwzM7Y1dzCA6jicsYIDQW8cib8lYRw2Fv5viaogMwMGq2IJamlw/0";
	
	//定义一个命名空间
	var storage = {};
	//将一个对象转化为JSON字符串 存储
	storage.setData = function(key,data){
	    var _str = JSON.stringify(data);
	    var _token = Commonjs.getToken();
	    sessionStorage.setItem("_token",_token);
	    sessionStorage.setItem(key,_str);
	};
	
	//将JSON字符串转化为对象 读取
	storage.getData = function(key){
	    var _data;
	    var token = sessionStorage.getItem("_token");
	    var _token = Commonjs.getToken();
	    if(token != _token){
	    	sessionStorage.removeItem(key);
	    	return null;
	    }
	    _data = JSON.parse(sessionStorage.getItem(key));
	    return _data;
	};
	//	webpath = itmp < 0 ? url : url.substr(0, itmp);
//	if (webpath.indexOf('/') == -1) {
//		webpath = '/' + webpath;
//	}
/*
 * 在 getSession() 的时候会  清空localStorage
 */
	Commonjs = {
		getLogoUrl:function(){
			var hosid = Commonjs.hospitalId();
			var url = logoUrlObj[hosid*1];
			if(Commonjs.isNotNull(url)){
				return url;
			}
			return logoUrl;
		},
		//获取渠道对应的名称  后续有需要页面显示的请在这里添加
		getChannelIdName:function(clientId){
			if(clientId == 'minipay'){
				return "Mini付智慧系统";
			}else if(clientId == 'wristbandcodepay'){
				return "腕带付智慧系统";
			}else if(clientId == 'cardfacecodepay'){
				return "卡面付智慧系统";
			}else if(clientId == 'selfservicepay'){
				return "自助机扫码付智慧系统";
			}else if(clientId == 'doctorstationpay'){
				return "医生工作站快捷付智慧系统";
			}else if(clientId == 'prescriptioncodepay'){
				return "处方付智慧系统";
			}else if(clientId == 'sweepcodepay'){
				return "当面付智慧系统";
			}else{
				return "未定义渠道名";
			}
		},	
		getCertTypeName : function(certType){
			if(certType=='01'){
				return '居民身份证';
			}else if(certType=='02'){
				return '居民户口簿';
			}else if(certType=='03'){
				return '护照';
			}else if(certType=='04'){
				return '军官证';
			}else if(certType=='08'){
				return '士兵证';
			}else if(certType=='23'){
				return '社保卡';
			}else if(certType=='09'){
				return '返乡证';
			}else if(certType=='24'){
				return '暂住证';
			}else if(certType=='11'){
				return '港澳通行证';
			}else if(certType=='12'){
				return '台湾通行证';
			}else if(certType=='99'){
				return '其他';
			}
		},
		//返回当前登录用户的机构ID
		hospitalId:function(){
			var session = Commonjs.getSession();
			return session.orgId;
		},
		//返回当前登录用户的openid
		getOpenId:function(){
			var session = Commonjs.getSession();
			return session.sign;
		},
		goToUrl:function(toUrl){
			var str = toUrl; //取得整个地址
			var menu = this.getWebAppMenu();
			if(Commonjs.isNotNull(menu) && Commonjs.isNotNull(menu.diyMenu)){
				var diyMenu = menu.diyMenu;
				$.each(diyMenu,function(index,val){
					var fromUrl = val.fromUrl;
					var toDiyUrl = val.toUrl;
					if(str.indexOf(fromUrl) == 0){
						var i = fromUrl.length;
						str = toDiyUrl + str.substr(i,str.length);
					}
				});
			}
			var resourceUrl = str;
		    var num=str.indexOf("?");
		    var uid = Commonjs.getUUID();
		    if(num >=0 ){
				str = str.substr(0,num); 
				var i = str.lastIndexOf('/');
				var key = str.substr(i+1,str.length);
				var val = resourceUrl.substr(num,resourceUrl.length);
				var storage = window.localStorage;
				localStorage.setItem("toUrl_"+key, val+"&UUID="+uid); 
		    } 
		    str += ("?UUID="+uid);
		    return str;
		},
		getUrlParam : function (key,uri) {
			var uid = Commonjs.getUrlParamPrivate("UUID");
			if(uid){
				var pathname = window.location.pathname;
				var i = pathname.lastIndexOf('/');
				var pkey = pathname.substr(i+1,pathname.length);
				var storage = window.localStorage;
				var toUrlParam = storage.getItem("toUrl_"+pkey);
				if(toUrlParam && toUrlParam.indexOf('&UUID='+uid) >=0 ){
					return Commonjs.getUrlParamPrivate(key,toUrlParam);
				}else{ 
					return Commonjs.getUrlParamPrivate(key);
				}
			}else{
				return Commonjs.getUrlParamPrivate(key);
			}
		},
		getUrlParamPrivate : function(key,uri){
			if(!uri){
				uri = window.location.search;
			}
			var re = new RegExp("" +key+ "\=([^\&\?]*)", "ig");
			var retVal = ((uri.match(re))?(uri.match(re)[0].substr(key.length+1)):null);
			
			if(key == "HosId" && Commonjs.isEmpty(retVal)){
				return Commonjs.hospitalId();
			}
			return retVal;
		},
		//是否开通无卡预约 是否开放门诊无卡预约  true 开放，false 不开放 默认false
		getIsOpenYYNotClinicCard:function(){
			var session = Commonjs.getSession();
			//默认 false 不开放 需要修改请在配置中心进行修改针对渠道的修改需要将支持的渠道都加上
			var isOpenYYClinicCard = session.clientConfig.isOpenYYClinicCard;
			if(Commonjs.isNull(isOpenYYClinicCard)){
				isOpenYYClinicCard = false;
			}else if(isOpenYYClinicCard == "true"){
				isOpenYYClinicCard =  true;
			}else if(isOpenYYClinicCard == "false"){
				isOpenYYClinicCard =  false;
			}
			return isOpenYYClinicCard;
		},
		//是否显示挂号费 1 不显示  0或空则显示
		getIsShowRegFee:function(){
			var session = Commonjs.getSession();
			//默认 false 不开放 需要修改请在配置中心进行修改针对渠道的修改需要将支持的渠道都加上
			var lockShwoFee = session.clientConfig.lockShowFee;
			if(Commonjs.isNull(lockShwoFee)){
				lockShwoFee = true; 
			}else if(lockShwoFee*1 == 0){
				lockShwoFee =  true;
			}else if(lockShwoFee*1 == 1){
				lockShwoFee =  false;
			}else{
				lockShwoFee = true;
			}
			return lockShwoFee;
		},
		//获取支付配置信息 configKey
		getConfigKey:function(){
			var session = Commonjs.getSession();
			return session.configKey;
		},
		//获取渠道ID
		getClientId:function(){
			var session = Commonjs.getSession();
			return session.clientId;
		},
		/********************* 订单类型 ********************/
		/**
		 * 返回是否需要在线支付模式 1 支付，2非支付
		 * 挂号订单  
		 * public static final String ORDERTYPE_0 = "0";
		 * 西药  
		 * public static final String ORDERTYPE_001 = "001";
		 * 中成药  
		 * public static final String ORDERTYPE_002 = "002";
		 * 草药 
		 * public static final String ORDERTYPE_003 = "003";
		 * 非药品类型列表（检查）  
		 * public static final String ORDERTYPE_004 = "004";
		 * 就诊卡充值  
		 * public static final String ORDERTYPE_005 = "005";
		 * 门诊 充值  
		 * public static final String ORDERTYPE_006 = "006";
		 * 住院 充值  
		 * public static final String ORDERTYPE_007 = "007";
		 * 诊间数据  
		 * public static final String ORDERTYPE_008 = "008";
		 * 当日挂号 
		 * public static final String ORDERTYPE_009 = "009";
		 */
		getIsOnlinePay:function(serviceId){
			// serviceId = '0' 预约挂号
			// serviceId = '001' 
			var session = Commonjs.getSession();
			return session.orderModel;
		},
		getOrderPayStateName: function(IsOnlinePay,PayState,BizState,OverState){
			var state_1 = "异常订单";
			var state_2 = "已完成";
			var state_3 = "已取消";
			var state_4 = "待支付";
			var state_5 = "已支付";
			var state_6 = "支付完成（待处理）";
			var state_7 = "支付中";
			var state_8 = "退费中";
			var state_9 = "已退费";
			var state_10 = "已取消";
			var state_11 = "已撤销";
			var state_other = "其它";
			
			//是否线上支付 
			if(IsOnlinePay == 2){
				//不是订单模式的 然后又没有订单的 这种如果存在者返回：其它 
				//一般是不存在这种业务 
				if(BizState == 0) return state_1;
				//订单业务已完成
				if(BizState == 1) return state_2;
				//订单业务已完成
				if(BizState == 2) return state_3;
			}else if(IsOnlinePay == 1){
				//待支付 业务未执行 未取消  未撤销
				if(PayState == 0 && BizState == 0 && OverState == 0) return state_4;
				//已支付 业务已经执行完成 未取消  未撤销
				if(PayState == 2 && BizState == 1 && OverState == 0) return state_5;
				//支付中 
				if(PayState == 1) return state_7;
				//退费中 
				if(PayState == 3) return state_8;
				//已退费
				if(PayState == 4) return state_9;
				//未支付 取消
				if(PayState == 0 && OverState == 5) return state_10;
				//未支付 已撤销
				if(PayState == 0 && OverState == 6) return state_11;
				//退费中 
				if(PayState == 2) return state_5;
			}
			return state_other;
		},
		getRegFlagStr:function(regFlag){
			var flag = {
					0:"已锁号",
					1:"已挂号",
					2:"已退号",
					3:"停诊",
					4:"替诊",
					5:"已取号",
					6:"已就诊"
			};
			return flag[regFlag];
		},
		/**
		 * 前端订单状态的显示 文本
		 * @param serviceId 业务类型：
		 * @param isOnlinePay 是否在线支付：2 否 1是
		 * @param payState 支付状态 
		 * @param bizState 业务执行状态
		 * @param overState 订单最终状态 5 已取消 6 已撤销
		 * 
		 * @author daiyanshui
		 */
		getOrderStateName:function(ServiceId,IsOnlinePay,PayState,BizState,OverState){
			var stateName = Commonjs.getOrderPayStateName(IsOnlinePay,PayState,BizState,OverState);
			IsOnlinePay = IsOnlinePay * 1;
			//预约挂号业务 并且有在线支付的订单
			if(ServiceId == '0' && (IsOnlinePay == 1))
			{
				if(PayState == 3 || PayState == 4){
					//退费中／退费完成   BizState 0:未执行业务  1:订单业务完成  2:订单业务取消
					if(BizState == 0) return stateName + "(已取消)";
					if(BizState == 1) return stateName + "(正在退号)";
					if(BizState == 2) return stateName + "(已退号)";
				}else if(PayState == 0){ 
					//待支付
					if(BizState == 0) return stateName;
				}else if(PayState == 1 || PayState == 2){
					if(BizState == 0) return stateName + "(挂号中)";
					if(BizState == 1) return stateName + "(挂号成功)";
					if(BizState == 2) return stateName + "(已退号)";
				}
				//无线上支付 挂号订单
			}else if(ServiceId == '0' && IsOnlinePay == 2){
				if(BizState == 0) return "已锁号";
				if(BizState == 1) return "挂号成功";
				if(BizState == 2) return "已退号";
			}
			return stateName;
			//未支付  已支付  已退款  已过期  已取消
			//payState 总共有4个状态 0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成 
			//bizState 总共有2个状态 0:未执行业务  1:订单业务完成  2:订单业务取消
			//overState 总共有2个状态 5:订单取消  6:订单撤销  7:已关闭
			/*
			 * 预约挂号下单流程：
			 * 选择号源的时候锁号 --> 新增锁号表 ，然后返回前端页面 返回orderId  ----当前未生成订单 失败即失败用户重新发起锁号
			 * 点击挂号按钮时 --> 新增挂号订单 O_Order 表新增记录 订单状态 待支付通过 payState=0 bizState= 0 返回orderId   ---- 当前生成订单，然后判断是否是订单模式 1 订单模式，2非订单模式
			 * 	就是新增订单O_Order 中：ISONLINEPAY 是否线上支付：1是2否
			 * 	1.线上支付模式： 
			 * 		订单模式下 跳转到收银台 让用户确认费用并支付
			 * 		收银台（统一下单）： --> 点击支付 -> 调用统一下单接口：getUnitePay 统一下单的时候传入支付回调地址，在后端配置的 
			 * 			（微信：拉起前端用户输入密码框 支付宝：跳转一个支付页面：实际上也是在支付宝容器中打开一个支付输入密码框）
			 * 			统一下单逻辑中并未新增任何表记录直接调用微信的统一下单接口得到下单的结果集后返回
			 * 						String packages = "prepay_id=" + prepayId;
			 *						finalpackage.put("appId", app_id);
			 *						finalpackage.put("timeStamp", getTimeStamp());
			 *						finalpackage.put("nonceStr", nonceStr);
			 *						finalpackage.put("package", packages);
			 *						finalpackage.put("signType", TenpayConstant.SIGNTYPE_MD5)
			 *			前端通过返回的结果集调用微信的js控件拉起输入密码框进行支付
			 *		支付完成：
			 *			前端js获取支付完成结果状态 立即调用：支付完成回调接口：payCallBack（正在支付中）
			 * 				支付回调：
			 * 					判断订单状态：当前订单（视图）状态是 0  待支付状态
			 * 						订单0待支付：在订单支付表： o_payorder 中新增 o_payorder中状态 = 1 正在支付
			 * 						如果微信那边回调比我们这里快则更新 o_payorder 业务部分的
			 * 		支付回调：
			 * 			回调接口： payNotify.do 完成业务
			 * 				微信回调通知支付完成-->先记录通知结果：P_MERCHANT_NOTIFY 返回微信成功
			 * 				启动线程：异步调用订单完成逻辑 
			 * 						1.调用支付完成接口：payForCompletion 
			 * 							这里会判断是否有支付订单 o_payorder 如果没有则新增并且业务状态 paystate 为 2 支付完成，如果有则更新成业务状态
			 * 						2.调用业务（如：挂号 -> bookService）挂号完成 执行订单业务完成
			 * 						3.调用业务订单完成结果：
			 * 								返回成功调用：bizForCompletion
			 * 								返回执行超时／失败： 重试调用 3 次调用业务执行完成逻辑
			 * 								如果3次重试都是失败最终没有业务完成状态则：
			 * 									业务 BizOrder表没有数据 订单bizState = 0:未执行业务
			 * 									支付表 payState = 2 支付完成
			 * 			
			 * 
			 * 	2.非线上支付模式： 
			 * 		调用业务执行完成（如挂号： bookService） 业务 执行业务完成逻辑
			 * 			并在逻辑中直接调用  bizForCompletion  告知业务执行成功。 bizState = 1 业务执行成功
			 * 
			 * 
			 *  业务执行退款和取消：
			 *  前端发起业务取消请求(如：bizOrderCancel) 内部先执行业务取消
			 *  	发业务取消 
			 *  			-> 取消成功：判断是否需要退费，需要退费执行退费 
			 *  			-> 取消失败：直接返回前端告知用户取消失败原因，让用户重复发起取消
			 *  
			 *  
			 *  定时任务：5分钟执行 一次：
			 *  
			 */		
		},
		ajaxDefult : {
			type : 'post',
			async : true,
			timeout : 30000,
			cache : false,
			dataType : 'json',
			showLoadding : true,
			beforeSend : function(xhr){
				
			},
			error : function(){
				
			}
		},
		ajax:function(url,param,successfun,other){
			var option = $.extend({},this.ajaxDefult,other);
			$.ajax({
				type: option.type,
				url: url,
				data: param,
				async: option.async,
				timeout : option.timeout,
				dataType: option.dataType,
				cache: option.cache,
			    headers: {
			        'token': this.getToken()
			    },
			    beforeSend:function(xhr){
			    	if(option.showLoadding){
			    		myLayer.load('正在加载');
			    	}
			    	option.beforeSend(xhr);
			    },
	            error: function () {
	            	if(option.showLoadding){
	            		myLayer.clear();
	            	}
	            	option.error();
	            },
				success: function(data){
					if(option.showLoadding){
						myLayer.clear();
					}
					Commonjs.ajaxCallBackFunction(data);
					if(successfun && typeof successfun == 'function'){
						successfun(data);
					}
				}
			});
		},
//		ajax:function (url,param,flag,time,successfun){
//			$.ajax({
//				type: 'POST',
//				url: url,
//				data: param,
//				async: flag,
//				timeout : time,
//				dataType: 'json',
//				cache: false,
//			    headers: {
//			        'token': this.getToken()
//			    },
//	            error: function () {
//	            	myLayer.clear();
//	            	myLayer.alert('网络错误,请刷新后重试',5000);
//	            },
//	            beforeSend:function(xhr){
//	            	myLayer.load('正在加载');
//	            },
//				success: function(data){
//					Commonjs.ajaxCallBackFunction(data);
//					if(successfun && typeof successfun == 'function'){
//						successfun(data);
//					} 
//				}
//			});
//		},
		ajaxCallBackFunction:function(data){
			var msg = data.RespMessage;
			if(Commonjs.isNotNull(msg) && (msg.indexOf("{")==0 || msg.indexOf("\"{")==0)){
				var t = $.parseJSON( msg );
				var code = t.code;
				if(code == -50000){
					myLayer.alert(t.msg,3000);
				}
			}
			if(data.code == -50000 || data.RespCode == -50000){
				var msg = data.RespMessage;
				myLayer.alert(msg,5000);
			}else if(data.code == 40000 || data.RespCode == -14019 || data.code == 401 || data.code == 20000){
				var msg = data.RespMessage;
				if(msg){
					if(data.msg){
						msg = msg + "|"+ data.msg;
					}
				}
				window.location = '../../pageError.html?errorcode='+data.RespCode+'&token='+this.getToken()+'&msg='+encodeURI(msg);
			}
		},
//		ajaxSync:function (url,param,successfun,other){
//			var option = $.extend({},this.ajaxDefult,other);
//			$.ajax({
//				type: option.type,
//				url: url,
//				data: param,
//				async: option.async,
//				timeout : option.timeout,
//				dataType: option.dataType,
//				cache: option.cache,
//			    headers: {
//			        'token': this.getToken()
//			    },
//			    beforeSend:function(xhr){
//			    	myLayer.load('正在加载');
//			    	option.beforeSend();
//			    },
//	            error: function () {
//	            	myLayer.clear();
//	            	option.error();
//	            },
//				success: function(data){
//					myLayer.clear();
//					Commonjs.ajaxCallBackFunction(data);
//					if(successfun && typeof successfun == 'function'){
//						successfun(data);
//					}
//				}
//			});
//		},
//		ajaxNoLayer:function (url,param,flag,time,successfun){
//			$.ajax({
//				type: 'POST',
//				url: url,
//				data: param,
//				async: flag,
//				timeout : time, 
//				headers: {
//			        'token': this.getToken()
//			    },
//				dataType: 'json',
//	            error: function () {
//	            },
//				success: successfun
//			});
//		},
		getToken:function(){
			return this.getCookie('token');
		},
		/** 
		 * 返回当前登录用户的session信息包含部分渠道对应的配置信息
		 * 
		 * 
		 * {
		 * "RespCode":10000,
		 * "configKey":"WX1000001",
		 * "code":0,
		 * "clientId":"100123",
		 * "orgName":"福建演示医院",
		 * "sign":"test", //当前登录的用户openid
		 * "clientConfig":{
		 * "isOpen":"true", //渠道是否开放
		 * "isOpenYYClinicCard":"false", //渠道是否开放无卡预约
		 * "ORDERTYPE_0":1,    //业务是否开放线上支付功能  ORDERTYPE_【serviceId】 业务id
	挂号订单 
	public static final String ORDERTYPE_0 = "0";
	/** 西药 
	public static final String ORDERTYPE_001 = "001";
	/** 中成药 
	public static final String ORDERTYPE_002 = "002";
	/** 草药 
	public static final String ORDERTYPE_003 = "003";
	/** 非药品类型列表（检查） 
	public static final String ORDERTYPE_004 = "004";
	/** 就诊卡充值 
	public static final String ORDERTYPE_005 = "005";
	/** 门诊 充值 
	public static final String ORDERTYPE_006 = "006";
	/** 住院 充值 
	public static final String ORDERTYPE_007 = "007";
	/** 诊间数据 
	public static final String ORDERTYPE_008 = "008";
	/** 当日挂号 
	public static final String ORDERTYPE_009 = "009";
	/** 所有订单 （当运维的时候需要将指定渠道的所有线上支付都关闭）
	public static final String ORDERTYPE_999 = "999";
		 * "ORDERTYPE_004":1,
		 * "ORDERTYPE_003":1,
		 * "ORDERTYPE_006":1,
		 * "ORDERTYPE_005":1,
		 * "ORDERTYPE_002":1,
		 * "ORDERTYPE_001":1,
		 * "ORDERTYPE_008":1,
		 * "ORDERTYPE_007":1,
		 * "ORDERTYPE_009":1,
		 * "ORDERTYPE_999":1
		 * },
		 * "orgId":"1024727"
		 * }
		 */
		getSession:function(){
			if(window.localStorage){
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
	            error: function () {
	            },
				success: function(data){
					Commonjs.ajaxCallBackFunction(data);
					result = data;
					if(window.localStorage){
						var storage=window.localStorage;
						storage.clear();//TODO 清空localStorage
						var d=JSON.stringify(data);
						storage.setItem("data",d);
						storage.setItem("token", token);
						//console.log(storage.data);
					}
				}
			});
			if(null != result && result.RespCode == 10000){
				return result;
			}else if(null != result && result.RespCode != 10000){
				myLayer.clear();
            	myLayer.alert('网络错误,请刷新后重试:'+result.msg,5000);
			}
		},
		getConfigKey:function(clientId,orderId,serviceId){
			//	@PostMapping("/{clientId}/{orderId}/{serviceId}/getConfigKey.do")
			var getConfigKeyUrl = '../../wsgw/'+clientId+'/'+orderId+'/'+serviceId+'/getConfigKey.do';
			var result = null;
			$.ajax({
				type: 'POST',
				url: getConfigKeyUrl,
				async: false,
				headers: {
			        'token': this.getToken()
			    },
				dataType: 'json',
	            error: function () {
	            },
				success: function(data){
					result = data;
				}
			});
			if(null != result && result.RespCode == 10000){
				return result;
			}else if(null != result && result.RespCode != 10000){
				myLayer.clear();
            	myLayer.alert('网络错误,请刷新后重试:'+result.msg,5000);
			}
		},
		
		getWebAppMenu:function(){
			var key = "WebAppMenu";
			var result = storage.getData(key);
			if(this.isNotNull(result)){
				return result;
			}
			$.ajax({
				type: 'POST',
				url: getWebAppUrl,
				async: false,
				headers: {
			        'token': this.getToken()
			    },
				dataType: 'json',
	            error: function () {
	            },
				success: function(data){
					Commonjs.ajaxCallBackFunction(data);
					result = data;
				}
			});
			if(null != result && result.RespCode == 10000){
				var data = $.parseJSON(result.result); 
				storage.setData(key, data)
				return data;
			}else if(null != result && result.RespCode != 10000){
				myLayer.clear();
            	myLayer.alert('网络错误,请刷新后重试:'+result.msg,5000);
			}
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
		isEmpty : function(s){
			if(s == undefined){
	  			return true;
	  		}else{
	  			if(s == null || s == '' ||
	  				s == 'null' || s.length < 1 || s=='undefined'){
	  				return true;
	  			}
	  		}
	  		return false;
		},
		isNull : function(s){
			return Commonjs.isEmpty(s);
		},
		isNotNull : function(s){
			return !Commonjs.isEmpty(s);
		},
		getUrl : function(model){
			if(!this.isEmpty(model)){
	  			return '../'+model+'_doAll.do';
	  		}else{
	  			return '';
	  		}
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
	   	/**
		 * 返回URL中的参数值，类似JSP中的request.getParamter('id');
		 * 用法：var strGetQuery =document.location.search; var id = getQueryString(strGetQuery,'id');
		 */
		getQueryString : function (url,name){
			var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
			if (reg.test(url)){
				return unescape(RegExp.$2.replace(/\+/g, " "));
			}
			return "";
		},
	   	getDecodeUrlParam : function (val) {
	   		var strGetQuery =document.location.search;
			strGetQuery = decodeURIComponent(strGetQuery,true);//解码
			return Commonjs.getQueryString(strGetQuery,val);
		},
		//例子： 
		//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
		//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
		DateFormat:function(date,fmt) 
		{ 
		  var o = { 
		    "M+" : date.getMonth()+1,                 //月份 
		    "d+" : date.getDate(),                    //日 
		    "h+" : date.getHours(),                   //小时 
		    "m+" : date.getMinutes(),                 //分 
		    "s+" : date.getSeconds(),                 //秒 
		    "q+" : Math.floor((date.getMonth()+3)/3), //季度 
		    "S"  : date.getMilliseconds()             //毫秒 
		  }; 
		  if(/(y+)/.test(fmt)) 
		    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
		  for(var k in o) 
		    if(new RegExp("("+ k +")").test(fmt)) 
		  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
		  return fmt; 
		},
	   	getRootPath : window.location.protocol + '//' + window.location.host + webpath,
		getRoot : window.location.protocol + '//' + window.location.host,
		ListIsNotNull : function(obj) {
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
		getCookie:function (a) {
			return document.cookie.length > 0 && (c_start = document.cookie.indexOf(a + "="), -1 != c_start) ? (c_start = c_start + a.length + 1, c_end = document.cookie.indexOf(";", c_start), -1 == c_end && (c_end = document.cookie.length), unescape(document.cookie.substring(c_start, c_end))) : "";
		},
		setCookie:function (a, b, c) {
			var d = new Date;
			d.setDate(d.getDate() + c), cookieVal = a + "=" + escape(b) + (null == c ? "" : ";expires=" + d.toGMTString()), document.cookie = cookieVal;
		}, delCookie:function (a) {
			var b = new Date;
			b.setTime(b.getDate() - 10000), this.getCookie(a) && (document.cookie = a + "=v; expires=" + b.toGMTString());
		},
		getIsPay:function () {
			return isPay;
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
		isEmpty : function(s){
			if(s == undefined){
	  			return true;
	  		}else{
	  			if(s == null || s == '' ||
	  				s == 'null' || s.length < 1 || s=='undefined'){
	  				return true;
	  			}
	  		}
	  		return false;
		},
		/**
		 * 两数相乘
		 * */
		accMul:function (arg1,arg2)
		{
		  var m=0,s1=arg1.toString(),s2=arg2.toString();
		  try{m+=s1.split(".")[1].length}catch(e){}
		  try{m+=s2.split(".")[1].length}catch(e){}
		  return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
		},
		/**
		 * 通过月，年获取最后天数日期
		 * */
		getMonthLastDate : function(month, year){
		    	var date = new Date(year+"-"+month);
		    	if(date.getMonth() == 11){
		    		//对应12月
			    	return '31';
		    	}else{
			    	date = new Date(date.getFullYear()+"-"+(date.getMonth()+2));
			    	date.setDate(date.getDate()-1);
			    	return date.getDate();
		    	}
		    },
		constant:{
			cardType_14 : '14', //住院号对应的卡类型
			cardType_1: '1', //就诊卡对应卡类型
			
			serviceId_007 :'007',//缴费记录类型：住院
			serviceId_006 :'006',//缴费记录类型：门诊充值
			
			sysUserOutTimes :'15',//支付宝用户信息过期时间单位天
		},
		//就诊人控件公用工具类
		memberPicker:function(config){
			var elementId = config.id;
			var cardType = config.cardType;
			var defaultlValue = config.defaultlValue;
			var pickerOnCloseFunction = config.pickerOnClose;
			var ajaxSuccessFunction = config.ajaxSuccess;
			var nonCardFunction = config.nonCardFunction;
			if( elementId == null || elementId == undefined){
				console.error("memberWigdet-患者控件，元素id不能为空。");
				return;
			}
			var titeDesc = "就诊卡";
			if( cardType == 14 ){
				titeDesc = "住院号";
			}
			//查询就诊人列表
			var apiData = {};
			var param = {};
			apiData.HosId = Commonjs.getUrlParam("HosId");
			apiData.OpId = this.getOpenId();
			apiData.CardType = cardType;
			param.apiParam = Commonjs.getApiReqParams(apiData,"",8002);
			this.ajax('/wsgw/basic/QueryMemberList/callApi.do?t=' + new Date().getTime(),param,function(jsons){
				if (Commonjs.ListIsNotNull(jsons)) {
		            if (jsons.RespCode == 10000) {
		                if (Commonjs.ListIsNotNull(jsons.Data)) {
		                	var disObj=new Array();
		                	var valObj=new Array();
		                	var hasDefaultMenber = false;
		                	Commonjs.BaseForeach(jsons.Data, function (i, item) {
		                		var valParam = item.CardNo+","+item.CardType+","+item.MemberName+","+item.MemberId+","+item.HisMemberId;
		                		if( cardType == 14 ){//住院号
		                			valParam = item.HospitalNo+",14,"+item.MemberName+","+item.MemberId+","+item.HisMemberId;
		                		}
		                    	if(item.IsDefaultMember == '1'){//默认
		                    		disObj.unshift(item.MemberName);
		                    		valObj.unshift(valParam);
		                    		hasDefaultMenber = true;
		                    	}else{
		                    		disObj.push(item.MemberName);
		                    		valObj.push(valParam);
		                    	}
		                    });
		                	if( !Commonjs.isEmpty(defaultlValue) ){
		                		$("#"+elementId).parent().find(".c-f15").html(defaultlValue.split(",")[2]);
		                    	$("#"+elementId).parent().find(".c-999").html(titeDesc+":"+defaultlValue.split(",")[0]);
		                    	$("#"+elementId).val(defaultlValue);
		                	}else{
		                		$("#"+elementId).parent().find(".c-f15").html(disObj[0]);
		                    	$("#"+elementId).parent().find(".c-999").html(titeDesc+"："+valObj[0].split(",")[0]);
		                    	$("#"+elementId).val(valObj[0]);
		                	}
		                	if( Commonjs.isEmpty(valObj[0].split(",")[0]) ){
		                		$("#"+elementId).parent().find(".c-999").html(titeDesc+":未绑定");
		                		if( nonCardFunction ){
		                			nonCardFunction();
		                		}else{
		                			myLayer.alert("就诊人:"+disObj[0]+"未绑定"+titeDesc+"，请绑定"+titeDesc+"，或者选择就诊人",3000);
		                		}
		                	}
		                	//就诊人切换
		        			$("#"+elementId).picker({
		        				title: "就诊人选择",
		        				cols: [
		        					{
		        						textAlign: 'center',
		        						displayValues:disObj,
		        						values: valObj
		        					}
		        				],
		        				onClose:function(obj){
		        					var memberObj = obj.cols[0].value; 
	        						$("#"+elementId).parent().find(".c-f15").html(memberObj.split(",")[2]);
		                    		$("#"+elementId).parent().find(".c-999").html(titeDesc+"："+memberObj.split(",")[0]);
		        					if(Commonjs.isEmpty(memberObj.split(",")[0])){
		        						$("#"+elementId).parent().find(".c-999").html(titeDesc+"：未绑定");
		        						if( nonCardFunction ){
				                			nonCardFunction();
				                		}else{
				                			myLayer.alert("就诊人"+memberObj.split(",")[2]+"无"+titeDesc,3000);
				                		}
		        					}
		        					//设置浏览器缓存，方便回退的时候，重新设值
		        					if (window.localStorage) {	
		        					    localStorage.setItem("selectedMember",memberObj);	
		        					}
		        					if( typeof  pickerOnCloseFunction === "function" ){
		        						var selectedMember = {};
		        						selectedMember.cardNo=memberObj.split(",")[0];
		        						selectedMember.cardType = memberObj.split(",")[1];
		        						selectedMember.memberName = memberObj.split(",")[2];
		        						selectedMember.memberId = memberObj.split(",")[3];
		        						selectedMember.hisMemberId = memberObj.split(",")[4];
		        						pickerOnCloseFunction(obj,selectedMember);
		        					}
		        				}
		        			});
		        			
		        			if( typeof  ajaxSuccessFunction === "function" ){
		        				var defaultMember = {};
		        				defaultMember.memberName = disObj[0];
		        				defaultMember.cardNo = valObj[0].split(",")[0];
		        				defaultMember.cardType = valObj[0].split(",")[1];
		        				defaultMember.memberId =  valObj[0].split(",")[3];
		        				defaultMember.hisMemberId =  valObj[0].split(",")[4];
		        				ajaxSuccessFunction(jsons,defaultMember);
        					}
		                } else {
		                	myLayer.alert("无任何就诊人，请新增就诊人",3000);
		                }
		            } else {
		            	myLayer.alert(jsons.RespMessage,3000);
		            }
		        } else {
		        	myLayer.alert('网络繁忙,请刷新后重试',3000);
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
		getParams : function(headCode,Service,page){
			var params = {};
			params.Req = {};
			params.Req.Service = {};
			if(!this.isEmpty(headCode)){
				params.Req.TransactionCode = headCode;
			}else return null;
			if(!this.isEmpty(Service)){
				params.Req.Service = Service;
				if(!this.isEmpty(page)){
					params.Req.Service.Page = {};
					params.Req.Service.Page = page;
				}
			}else return null;
			return params;
		},
		getUUID : function (len, radix) {
			if(!len){
				len = 16;
			}
			if(!radix){
				radix = 32
			}
			  var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
			  var uuid = [], i;
			  radix = radix || chars.length;
			  if (len) {
			   // Compact form
			   for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
			  } else {
			   // rfc4122, version 4 form
			   var r;
			   // rfc4122 requires these characters
			   uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
			   uuid[14] = '4';
			 
			   // Fill in random data. At i==19 set the high bits of clock sequence as
			   // per rfc4122, sec. 4.1.5
			   for (i = 0; i < 36; i++) {
			    if (!uuid[i]) {
			     r = 0 | Math.random()*16;
			     uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
			    }
			   }
			  }
			return uuid.join('');
		},
		goBack:function(index){
			if(!index){
				index = -1 ;
			}
		    if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){ // IE
		        if(history.length > 0){
		            window.history.go( index );
		        }else{
		            WeixinJSBridge.invoke('closeWindow',{},function(res){

		            });
		        }
		    }else{ //非IE浏览器
		        if (navigator.userAgent.indexOf('Firefox') >= 0 ||
		            navigator.userAgent.indexOf('Opera') >= 0 ||
		            navigator.userAgent.indexOf('Safari') >= 0 ||
		            navigator.userAgent.indexOf('Chrome') >= 0 ||
		            navigator.userAgent.indexOf('WebKit') >= 0){

		            if(window.history.length > 1){
		                window.history.go( index );
		            }else{
		                WeixinJSBridge.invoke('closeWindow',{},function(res){

		                });
		            }
		        }else{ //未知的浏览器
		            window.history.go( index );
		        }
		    }
		},
		closeWindow : function(){
			try{
				WeixinJSBridge.invoke('closeWindow',{},function(res){
					
	            });
			}catch (e) {
				
			}
		},
		getCardTypeName : function(cardType){
			if(cardType == this.constant.cardType_1){
				return "就诊卡";
			}else if(cardType == this.constant.cardType_14){
				return "住院号";
			}
		},
		defMember : [],//默认的就诊人信息
		queryDefMember : function(){
			var apiData = {};
			var param = {};
			apiData.OpId = this.getOpenId();
			apiData.IsDefaultMember = 1;
			param.apiParam = Commonjs.getApiReqParams(apiData,"",8002);
			this.ajax('/wsgw/basic/QueryMemberList/callApi.do?t=' + new Date().getTime(),param,function(d){
				if(d.RespCode==10000){
					if(d.Data.length>0){
						Commonjs.defMember = d.Data;
					}
				}else{
					myLayer.alert(d.RespMessage,3000);
				}
			},{async:false})
		},
		checkDefMember : function(url,cardType,isInputMember){
			//查询默认就在人
			if(this.defMember.length<=0){
				this.queryDefMember();
			}
			
			var isHasDefault = false;//是否设置默认就诊人
			var isSucc = false;//验证是否通过
			var cardInfo;//默认就诊人卡信息
			
			//默认就诊人不存在，抛出异常提示
			if(Commonjs.defMember.length<=0){
				isHasDefault = false;
			}else{
				isHasDefault = true;
				if(!Commonjs.ListIsNotNull(Commonjs.defMember[0].Data_1)){
					if(Commonjs.isEmpty(cardType)){
						cardType = Commonjs.constant.cardType_1;
					}
					isSucc = false;
				}else{
					//遍历默认就诊人的卡包数据
					$.each(Commonjs.defMember[0].Data_1,function(i,item){
						if(!Commonjs.isEmpty(cardType)){
							if(item.CardType == cardType && !Commonjs.isEmpty(item.CardNo)){
								cardInfo = item;
								isSucc = true;
								return;
							}
						}else{
							//没有传入需要验证的卡类型时，默认验证就诊卡
							cardType = Commonjs.constant.cardType_1;
							if(item.CardType == cardType && !Commonjs.isEmpty(item.CardNo)){
								cardInfo = item;
								isSucc = true;
								return;
							}	
						}
					})
				}
			}
			
			if(!isHasDefault){
				myLayer.clear();
				Commonjs.noPerson("执行该业务操作前需先设置默认就诊人","立即设置");	
				return;
			}
			if(!isSucc){
				myLayer.clear();
				Commonjs.noPerson("执行该业务操作前默认就诊人需先绑定"+this.getCardTypeName(cardType)+"信息","立即绑定");
				return;
			}
			if(isSucc){
				if(isInputMember){
					if(url.indexOf('?')>=0){
						url = url+"&memberId="+Commonjs.defMember[0].MemberId+"&cardType="+cardInfo.CardType+"&cardNo="+cardInfo.CardNo;
					}else{
						url = url+"?memberId="+Commonjs.defMember[0].MemberId+"&cardType="+cardInfo.CardType+"&cardNo="+cardInfo.CardNo;
					}
					window.location.href= Commonjs.goToUrl(url);
				}else{
					window.location.href=url;
				}
			}
		},
		noPerson :function(warmMsg,clickName){
			var phtml = '';
			phtml += '	<div class="addpson">';
			phtml += '		<i class="sicon icon-anclose"></i>';
			phtml += '		<i class="an-jzrtb"></i>';
			phtml += '		<div class="h50 c-999 c-f15">'+warmMsg+'</div>';
			phtml += '		<a href="../member/memberList.html" class="btn-anadd mt10">'+clickName+'</a>';
			phtml += '	</div>';
			phtml += '	<div class="addpson-mb"></div>';
			$(".addpsonpop").html(phtml);
			$('.icon-anclose').on('click',function(){
				$('.addpsonpop').html("");
			});
		}
	}
