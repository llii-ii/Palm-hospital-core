var DefaultPostUrl='web_post.do';
var GetSessionUrl ='web_getSession.do';
var UPFILEURL = "";

String.prototype.replaceAll = function(AFindText,ARepText){
	var raRegExp = new RegExp(AFindText.replace(/([\(\)\[\]\{\}\^\$\+\-\*\?\.\"\'\|\/\\])/g,"\\$1"),"ig");
	return this.replace(raRegExp,ARepText); 
} 

Object.extend = function(destination, source) {
	for (var property in source) {
	    destination[property] = source[property];
	}
	return destination;
}
Array.prototype.remove = function(from, to) {
	var rest = this.slice((to || from) + 1 || this.length);
	this.length = from < 0 ? this.length + from : from;
	return this.push.apply(this, rest);
	};

/**
 * 判断角色为2的不显示的内容
 * @param roleid
 * @return
 */
function roleIdIs2NotView(){	
	var session = YihuUtil.getSession();	
	var roleid = session.roleId; 	
	var orgtype = session.orgtype; 
	var account = session.operatorid;	
	if(account == '02987445014'){
		return false;
	}	
	if( orgtype*1 == -10000){		
		return false;
	}else{
		return true;
	}
}


//添加医院科室下拉框
	function loadDeptSelect(deptid) {
		 
		var param = {};
		doAjaxLoadData("../bmry_getParentDept.do", param, function(resp) {
			if(resp.Code==10000){
				var options = '';
				Dept = resp.Result;
				$.each(resp.Result, function(i, item) {
					if(i==0){
						options += "<option value='" + item.deptID + "'>--选择科室--</option>";
					}else{
						options += "<option value='" + item.deptID + "'>"
						+ item.deptName + "</option>";
					}
				});
			$("#"+deptid).empty();
			$("#"+deptid).append(options);
			}else{
				alertwmk('科室加载失败提示','科室加载失败！');
			}
		});
	}
	
	function loadDoctorSelect(al,userID) {
		
		var param = {};
		param.deptID = al;
		param.currentPage = 1;
		param.pageSize = 100;
		doAjaxLoadData("../bmry_queryUserInfoListByDeptID.do", param,
		function(resp) {
		 	var options = '';
			options += "<option value=''>--请选择--</option>";
			if(resp != null && resp.rows!=undefined){
				$.each(resp.rows, function(i, item) {
					options += "<option value='" + item.userID +"'  >"
						+ item.userName + "</option>";
				});
			}
			$("#"+userID).empty().append(options);
		});
	}

/**
 * 通过AJAX获取远程数据
 */
function doAjaxLoadData(requestUrl, param, callback,callbackparam,callbackerror) {
	if(typeof(requestUrl) == undefined || typeof(requestUrl) == 'undefined' || requestUrl == null || $.trim(requestUrl).length == 0){
		return;
	}
	if(typeof(param) == undefined || typeof(param) == 'undefined' || param == null){
		param = "";
	}
	$.ajax({
		type: 'POST',
		url: requestUrl,
		data: param,
		dataType:'json',
		async:true,
		success: function(resp) {		
			if(callbackparam){
				callback(resp,callbackparam);
			}else{
				callback(resp);
			}
		},
		error:function(data){
			//alert('错误：'+data);
			var retv = data.responseText;
			if(retv.indexOf("script")>=0){
				var i = retv.indexOf(">");
				var j = retv.indexOf(";");
				var a = retv.substring(i+1,j);
				eval(a);
			}
		}
	});
}
function isNull(param){	
	if(typeof(param) == undefined || typeof(param) == 'undefined' || param == null || param == '')return true;
	else return false; 
}
function isNotNull(param){ return !isNull(param); }
function checkAllBoxCK(allCk,subckclass){	
	var acks = $(allCk)[0].checked;	
	$("."+subckclass).attr("checked",acks);
}
// 通过AJAX获取远程数据, * 同步获取数据
function doAjaxLoadDataSync(requestUrl, param, callback,callbackparam) {
	if(typeof(requestUrl) == undefined || typeof(requestUrl) == 'undefined' || requestUrl == null || $.trim(requestUrl).length == 0){
		return;
	}
	if(typeof(param) == undefined || typeof(param) == 'undefined' || param == null){
		param = "";
	}
	$.ajax({
		type: 'POST',
		url: requestUrl,
		data: param,
		dataType:'json',
		async:false,
		success: function(resp) {
			if(callbackparam){
				callback(resp,callbackparam);
			}else{
				callback(resp);
			}
		},
		error:function(data){
			var retv = data.responseText;			
			if(retv.indexOf("script")>=0){
				var i = retv.indexOf(">");
				var j = retv.indexOf(";");
				var a = retv.substring(i+1,j);
				eval(a);
			}
		}
	});
}
// 生成GUID（全球唯一标识）
function newGuid() {
	var guid = "";
	for (var i = 1; i <= 32; i++){
		var n = Math.floor(Math.random()*16.0).toString(16);
		guid +=   n;
		if((i==8)||(i==12)||(i==16)||(i==20))
			guid += "-";
	}	
	return guid; 
}
function changeTwoDecimal(x) {  
	var f_x = parseFloat(x);  
	if (isNaN(f_x)){  
	  alert('function:changeTwoDecimal->parameter error');  
	  return false;  
	}  
	var f_x = Math.round(x*100)/100;  	  
	return f_x;  
}  
function artErrorMessage(error) {		
		YihuUtil.dialog({
			title:"错误!",
			content: error,
			ok:true
		});
}
function successAlert(content) {
	YihuUtil.dialog({
		title:"操作成功!",
		content: content,
		ok:true
	});
}
function alMsg(msg,type) {
	artErrorMessage(msg);
}

////////////////////////////////////////////   huangql添加部分  //////////////////////////////////
ComWbj = {	
//表单验证：验证单个控件，不符合弹出红色浮动提示框
checkOne:function(formId){	
	$("#"+formId).validationEngine();
},
//整个表单验证：点击提交按钮时才触发验证规则
checkFormAll:function(formId){	
		    var v = $("#"+formId).validationEngine("validate");
			if(v==false){		
				return;
				$("#"+formId).submit(function(){return false;}); 
			}
},	
telCheck:function(id){ /* 电话号码没有中杠号时，把电话号码的区号后面，自动添加一个空格号*/
	var num = $("#"+id).val();	var str = "";	
	//var reg_3 = /^(010|02[0-9]|852|853)([0-9]{4}|[0-9]{5}|[0-9]{7}|[0-9]{8})$/;  //验证电话号码区号为3位	
	var reg_3 = /^(010|02[0-9]|852|853)([0-9]{3}|[0-9]{4}|[0-9]{5}|[0-9]{6}|[0-9]{7}|[0-9]{8})$/;  //验证电话号码区号为3位	
	if( reg_3.test(num) & (num.indexOf("-") == -1 || num.indexOf("—")) ){
		str = num.substr(0,3).replace(num.substr(0,3), num.substr(0,3)+' '); 		
		$("#"+id).val(str+num.substr(3,num.length));
	}	
	var reg_4 = /^(0[3-9][0-9][0-9])([0-9]{3}|[0-9]{4}|[0-9]{5}|[0-9]{6}|[0-9]{7}|[0-9]{8})$/;  //验证电话号码区号为4位	
	if( reg_4.test(num) & (num.indexOf("-") == -1|| num.indexOf("—")) ){
		str = num.substr(0,4).replace(num.substr(0,4), num.substr(0,4)+' '); 		
		$("#"+id).val(str+num.substr(4,num.length));
	}
},
/**
清空特殊字符、自动清空null和undefined
*@param   id：input控件的ID号
*@param   objStr：过滤的对象：name或者address或者time或者all       
*@use     姓名过滤：<input type="text" onBlur="ComWbj.stripStr('deptName','name');" id="deptName" />  
*@use     地址过滤：<input type="text" onBlur="ComWbj.stripStr('address','address');" id="address" /> 
*@use     过滤全部：<textarea onblur="ComWbj.stripStr('deptIntro','all')" id="deptIntro" cols="40" rows="5"></textarea>
*/
stripStr:function(id, objStr) {	
	var val=$("#"+id).val();	
	var containSpecial = "";
	var rs = "";  
	
	if(val == "null" || val == "undefined"){
		rs="";
	}else{
		if(objStr == "name") containSpecial = RegExp(/[(\１)(\２)(\３)(\４)(\５)(\６)(\７)(\８)(\９)(\０)(\Ａ)(\Ｂ)(\Ｃ)(\Ｄ)(\Ｅ)(\Ｆ)(\Ｇ)(\Ｈ)(\Ｉ)(\Ｊ)(\Ｋ)(\Ｌ)(\Ｍ)(\Ｎ)(\Ｏ)(\Ｐ)(\Ｑ)(\Ｒ)(\Ｓ)(\Ｔ)(\Ｕ)(\Ｖ)(\Ｗ)(\Ｘ)(\Ｙ)(\Ｚ)(\Ｔ)(\Ｕ)(\Ｖ)(\Ｗ)(\Ｚ)(\ａ)(\ｂ)(\ｃ)(\ｄ)(\ｅ)(\ｆ)(\ｇ)(\ｈ)(\ｉ)(\ｊ)(\ｋ)(\ｌ)(\ｍ)(\ｎ)(\ｏ)(\ｐ)(\ｑ)(\ｒ)(\ｓ)(\ｔ)(\ｕ)(\ｖ)(\ｗ)(\ｘ)(\ｙ)(\ｚ)(\ )(\~)(\!)(\！)(\@)(\#)(\$)(\￥)(\*)(\&)(\。)(\，)(\；)(\：)(\%)(\^)(\&)(\*)(\-)(\_)(\+)(\=)(\|)(\\)(\;)(\:)(\`)(\')(\")(\‘)(\’)(\“)(\”)(\,)(\.)(\/)(\?)(\？)]+/); 
		else if(objStr == "address") containSpecial = RegExp(/[(\１)(\２)(\３)(\４)(\５)(\６)(\７)(\８)(\９)(\０)(\Ａ)(\Ｂ)(\Ｃ)(\Ｄ)(\Ｅ)(\Ｆ)(\Ｇ)(\Ｈ)(\Ｉ)(\Ｊ)(\Ｋ)(\Ｌ)(\Ｍ)(\Ｎ)(\Ｏ)(\Ｐ)(\Ｑ)(\Ｒ)(\Ｓ)(\Ｔ)(\Ｕ)(\Ｖ)(\Ｗ)(\Ｘ)(\Ｙ)(\Ｚ)(\Ｔ)(\Ｕ)(\Ｖ)(\Ｗ)(\Ｚ)(\ａ)(\ｂ)(\ｃ)(\ｄ)(\ｅ)(\ｆ)(\ｇ)(\ｈ)(\ｉ)(\ｊ)(\ｋ)(\ｌ)(\ｍ)(\ｎ)(\ｏ)(\ｐ)(\ｑ)(\ｒ)(\ｓ)(\ｔ)(\ｕ)(\ｖ)(\ｗ)(\ｘ)(\ｙ)(\ｚ)(\ )(\~)(\!)(\！)(\@)(\$)(\￥)(\&)(\。)(\%)(\^)(\&)(\_)(\+)(\=)(\|)(\\)(\;)(\；)(\:)(\')(\")(\‘)(\“)(\?)(\？)]+/);
		else if(objStr == "time") containSpecial = RegExp(/[(\１)(\２)(\３)(\４)(\５)(\６)(\７)(\８)(\９)(\０)(\Ａ)(\Ｂ)(\Ｃ)(\Ｄ)(\Ｅ)(\Ｆ)(\Ｇ)(\Ｈ)(\Ｉ)(\Ｊ)(\Ｋ)(\Ｌ)(\Ｍ)(\Ｎ)(\Ｏ)(\Ｐ)(\Ｑ)(\Ｒ)(\Ｓ)(\Ｔ)(\Ｕ)(\Ｖ)(\Ｗ)(\Ｘ)(\Ｙ)(\Ｚ)(\Ｔ)(\Ｕ)(\Ｖ)(\Ｗ)(\Ｚ)(\ａ)(\ｂ)(\ｃ)(\ｄ)(\ｅ)(\ｆ)(\ｇ)(\ｈ)(\ｉ)(\ｊ)(\ｋ)(\ｌ)(\ｍ)(\ｎ)(\ｏ)(\ｐ)(\ｑ)(\ｒ)(\ｓ)(\ｔ)(\ｕ)(\ｖ)(\ｗ)(\ｘ)(\ｙ)(\ｚ)(\ )(\~)(\!)(\@)(\#)(\$)(\￥)(\……)(\*)(\&)(\()(\))(\【)(\】)(\。)(\，)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\')(\")(\,)(\.)(\<)(\>)(\?)(\)]+/);
		else containSpecial = RegExp(/[(\１)(\２)(\３)(\４)(\５)(\６)(\７)(\８)(\９)(\０)(\Ａ)(\Ｂ)(\Ｃ)(\Ｄ)(\Ｅ)(\Ｆ)(\Ｇ)(\Ｈ)(\Ｉ)(\Ｊ)(\Ｋ)(\Ｌ)(\Ｍ)(\Ｎ)(\Ｏ)(\Ｐ)(\Ｑ)(\Ｒ)(\Ｓ)(\Ｔ)(\Ｕ)(\Ｖ)(\Ｗ)(\Ｘ)(\Ｙ)(\Ｚ)(\Ｔ)(\Ｕ)(\Ｖ)(\Ｗ)(\Ｚ)(\ａ)(\ｂ)(\ｃ)(\ｄ)(\ｅ)(\ｆ)(\ｇ)(\ｈ)(\ｉ)(\ｊ)(\ｋ)(\ｌ)(\ｍ)(\ｎ)(\ｏ)(\ｐ)(\ｑ)(\ｒ)(\ｓ)(\ｔ)(\ｕ)(\ｖ)(\ｗ)(\ｘ)(\ｙ)(\ｚ)(\ )(\~)(\!)(\@)(\#)(\$)(\￥)(\……)(\*)(\&)(\（)(\）)(\【)(\】)(\。)(\，)(\%)(\^)(\&)(\*)(\()(\))(\-)(\—)(\_)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\：)(\“)(\”)(\‘)(\’)(\`)(\《)(\》)(\！)(\')(\")(\,)(\、)(\.)(\●)(\/)(\<)(\>)(\?)(\？)]+/);
		for (var i = 0; i < val.length; i++) { rs = rs + val.substr(i, 1).replace(containSpecial, ''); } 
	}
	
	return $("#"+id).val(rs);
},
/**
清空指定的特殊字符
*@param   id：input控件的ID号
*@param   str：指定的特殊字符（中间以(\)分隔,比如要删除# +，写成：(\#)(\+)）  
*@use     比如需要清空+ & #：<textarea onblur="stripOutStr(this,'[$\']')"  id="deptIntro" cols="40" rows="5"></textarea>
*/
stripOutStr:function(id,str) {
    var pattern = new RegExp(str)
    var s=$(id).val();
    var rs = "";
    for (var i = 0; i < s.length; i++) {  rs = rs + s.substr(i, 1).replace(pattern, '');  }
},
/**
* 功能：将浮点数四舍五入，取小数点后2位   
* 用法： changeTwoDecimal(3.1415926) 返回 3.14   
* 		 changeTwoDecimal(3.1475926) 返回 3.15  
*/
changeTwoDecimal:function(x) {  
	var f_x = parseFloat(x);  
	if (isNaN(f_x)){  
	  ComWbj.alertIconNo("提示","function:changeTwoDecimal->parameter error","warning");  
	  return false;  
	}  
	var f_x = Math.round(x*100)/100;  	  
	return f_x;  
},
trim:function(id){   $("#"+id).val($("#"+id).val().replace(/(^\s*)|(\s*$)/g, ""));   }, //去除input输入框的首尾空格
trimStr:function(str){ 	return str.replace(/(^\s*)|(\s*$)/g, "");   }, //去除字符串首尾空格
/**
截取字符串
*@param   str：被截取的字符串 
*@param   start：从start位开始截取
**@param  end：从end位结束
*/
subStrChar:function(str,start,end){    return str.substr(start,end);  },
getSex:function(sex){ //根据数字1和2判断男女
	var s ='';
	switch (parseInt(sex)){
		case 1: s='男'; break;
		case 2: s='女'; break;
		default: s='未知'; break;
	}
	return s;
},
getDoctorType:function(type){ //根据数字1和2判断男女
	var s ='';
	switch (parseInt(type)){
		case 1: s='医生'; break;
		case 2: s='药师'; break;
		case 3: s='技师'; break;
		case 3: s='护士'; break;
		default: s='其他'; break;
	}
	return s;
},
isNull:function(param){	//判断对象是否为空
	if(typeof(param) == undefined || typeof(param) == 'undefined' || param == null || param == '') 
		return true;
	else 
		return false; 
},
strIsNull:function(str){	//判断字符串是否为空
	if(str == null || str == undefined){
		return '';
	}else{
		return str;
	} 
},
isNotNull:function(param){ return !ComWbj.isNull(param); }, //判断对象是否不为空
clearArry:function(arry){	//清空数组中的空值
	for(var f = 0 ;f < arry.length; f++){
	    if(arry[f] == "" || typeof(arry[f]) == "undefined"){ arry.splice(f,1);  f= f-1;}   
	}
},
/***************************************   弹窗  ****************************************************/
/*关闭弹窗*/
closePop:function(id){
	$('#'+id).hide();
},
/*显示弹窗*/
showPop:function(id){	
	$('#'+id).show();
},
//开启消息正在加载提示
openPG:function(){
	YihuUtil.dialog({
		title:'消息正在加载....',
		content : '<img src="../yygh/pg1.gif"/>',
		lock : true,
		padding : 0,
		id: 'pg'
	});
},
//结束消息正在加载
closePG:function(){
	YihuUtil.wid().art.dialog.list["pg"].close();
},
//打开进度条
openProgress:function(){
	$.messager.progress({
		title:'进度',  
		msg:'正在提交，请稍等...'  
	});
},
//关闭进度条
closeProgress:function(){
	$.messager.progress('close');
},
//确定提示: 调用：alertMsg('导入成功20条数据','');
alertMsg:function(msg,action){	
	YihuUtil.dialog({
		title:"提示",
		content: msg,
		button : [{
				name : '确定',
				callback : action 
			}]
	});
},
//自定义弹出框
alertMsgOk:function(a, b) {
	YihuUtil.dialog( {
		title : a,
		id : 'alertMsgOk',
		content : b,
		lock : true,
		button : [ {
			name : 'OK',
			focus : true
		} ]
	})
},
//自定义弹出框3,带icon标志符号（关闭时不会自动刷新当前页面）
alertIconNo:function(a, b,icon) {
	YihuUtil.dialog( {
		title : a,
		id : 'alertIconNoReload',
		content : b,
		icon:icon,
		close:function(){		
	    },
		lock : true,
		button : [ {
			name : 'OK',
			focus : true			
		} ]
	})
},
//自定义弹出框,带icon标志符号(关闭会刷新当前页面)
alertIcon:function(a, b,icon) {
	YihuUtil.dialog( {
		title : a,
		id : 'alertIcon',
		content : b,
		icon:icon,
		close:function(){
		window.location.reload();
	    },
		lock : true,
		button : [ {
			name : 'OK',
			focus : true			
		} ]
	})
},
alertAutoClose:function(a, b, icon, url) {  //未使用
	YihuUtil.dialog( {
		title : a,
		id : 'alertAutoClose',
		content : b,
		icon:icon,
		time: 3000,
		close:function(){
		   location.href = url;
	    },
		lock : true		
	})
},
alertClose:function() {  //未使用
	YihuUtil.dialog( {		
		time: 3000			
	})
},
/**
 * 短暂提示,自动关闭
 * @param content {String}	提示内容
 * @param time {Number}	显示时间 (默认1.5秒)
 * @param url {String}	要跳转的地址，不需要跳转时可以写成null
 */
artTips:function (title, icon, content, time, url) {
    return artDialog({
        id: 'Tips',
        title: title,
        icon: icon,
        cancel: false,
        fixed: true,
        lock: true,
        ok: false,
        close: function(){    	
	    	if(url != null){
	    		location.href = url;
	    	}		   
        }
    })
    .content('<div style="padding: 20px;font-size:14px;">' + content + '</div>')
    .time(time || 1); 
},
/**
 * 短暂提示,自动关闭
 * @param content {String}	提示内容
 * @param time {Number}	显示时间 (默认1.5秒)
 * @param url {String}	自定义 by wuyiqing
 */
sofunartTips:function (title, icon, content, time,fun) {
    return artDialog({
        id: 'Tips',
        title: title,
        icon: icon,
        cancel: false,
        fixed: true,
        lock: true,
        ok: false,
        close: function(){    	
    		   fun();
        }
    })
    .content('<div style="padding: 20px;font-size:14px;">' + content + '</div>')
    .time(time || 1); 
},
/**
删除提示框
* @param message: 删除提示信息
* @param action: do操作的地址
* @param param： 删除需要的具体参数数组
* @return
*/
alertDel:function(message,action,param){ 
     alertify.confirm(message, function (e) {  
         if(e) {//确认删除              
        	 doAjaxLoadData(action, param, function(resp) {
    			 if(resp.Code == 10000){ 
    				 window.location.reload();
    				 alertify.success("已删除成功！");
    			 } 		
    		});   
         }
     });		
},
/**
删除提示框（存在特定不允许删除的条件时，弹出不可删除的提示信息）
* @param url: do操作的地址
* @param param： 删除需要的具体参数数组
* @EX 
*/
alertDelTwo:function(url, params){	
	YihuUtil.dialog({
		title:"提示",
		content: '确认要删除么？',
		button : [{
	 		name: '确认',
	   		callback: function () {		      	
				doAjaxLoadData(url, params, function(result) {
					if (result.code == -1) {
						ComWbj.alertIconNo("提示",result.message,"error");
					} else {
						ComWbj.alertIcon("提示","删除成功！","succeed");				 	
					}
				});
	 		}
	 	},
	  	{  
	  		 name: '取消'
		}]
	});
},
//页面弹出：写有html的层
alertHtml:function(title,id,fun) {
	YihuUtil.dialog( {
		title : title,
		id : 'testID',
		padding: '0px 0px',
		content : document.getElementById(id),
		lock : true,
		close : function() {
		   
		},
		button : [
		          {
	  	 	            name: '确定',
	  	 	            //callback: function () {
	  	 	         callback: function fun() {		        	       
	  	 	        		Demo_getVal(this.DOM.dialog[0]);
	  	 	        		return true;
	  	 	            },
	  	 	            focus: true
	  	 	        },
	  	 	        {
	  	 	            name: '确定2',
	  	 	            callback: function () {
	  	 	                
	  	 	            }
	  	 	        },
	  	 	        {
	  	 	            name: '取消'
	  	 	        }
		]
		
		/*button : [
		          {
	  	 	            name: '确定',
	  	 	            callback: function () {
	  	 	        		Demo_getVal(this.DOM.dialog[0]);
	  	 	        		return false;
	  	 	            },
	  	 	            focus: true
	  	 	        },
	  	 	        {
	  	 	            name: '不同意',
	  	 	            callback: function () {
	  	 	                alert('你不同意')
	  	 	            }
	  	 	        },
	  	 	        {
	  	 	            name: '无效按钮',
	  	 	            disabled: true
	  	 	        },
	  	 	        {
	  	 	            name: '关闭我'
	  	 	        }
		]*/
	});
},
/*设置弹窗垂直居中*/
popBoxMiddle:function(id){	
	popbox=$("#"+id);
	for(i=0;i<popbox.length;i++){
		popbox.eq(i).css("margin-top",function(){
			return "-"+$(this).height()/2+"px";
		})
	}
},
//打开遮罩界面  （dialogName：弹窗的名称;title:标题；id:iframe的ID）
pocmessage:function(dialogName,id,title,o,index,val) { 
	YihuUtil.dialog( {
		title : title, id : dialogName, padding: '0', content : document.getElementById(id), lock : true, 
		close : function() {
		    if(o){
				if(index == 3){
			 		loadDeptSelect(o.id);
			   	}else if(index == 2 && val != null && val != 0){
			   		loadDoctorSelect(val,o.id); 
			   	}
		   	 	ComWbj.closePop(dialogName);
		    }else window.location.reload();
		}, 
		button : [  ]	    
	});
},
closeDiaLog:function(id){  YihuUtil.wid().art.dialog.list[id].close(); }, //关闭YihuUtil.dialog弹出框
/******************************************   复选框操作   **************************************************/
/**
复选框全选
* @param  checkAll: 勾选全选的复选框的name属性名称
* @param  checkChild: 列表中被选中的复选框name属性名称
*/
checkAll:function(checkAll,checkChild){
		var checked= $("input[name='"+checkAll+"']").attr('checked');
		if(checked!='checked') checked=false; 
		$.each($("input[name='"+checkChild+"']"),function(i,item){
			   $(this).attr('checked',checked);
		});
},
/**
获取复选框选中项的值
* @param  name: 列表中被选中的复选框name属性名称
*/
getChecks:function(name){
	var hy='';
	var hys=[];
	$.each($("input[name='"+checks+"']:checked"),function(i,item){
		hys.push(item.value);
	});
	hy=hys.toString();
	return hy;
},
/**
下拉列表框中有多个复选框：点击后下拉框可多选，若选择多个，中间用“，”隔开；若选择太多，框内无法显示，则以“…”号表示
* @param  name: 列表中被选中的复选框name属性名称
* @param  textId: 显示内容的文本框ID
* @param  selectDivId: 装载复选框的DIV的ID
*/
setSelectChecks:function(name,textId,selectDivId){
	
	var strText = $("#"+textId).text();  //文本框最终要显示的内容		
		
	$("#"+textId).live('click', function() {	//点击时，显示select列表
		$("#"+selectDivId).slideDown();		
	})	

	$("input[name='"+ name +"']").live('click', function() {	//选中复选框的值				

		var strSplit =  strText.split(",");
		
		if($(this).attr('checked') == "checked"){				
			
			strText = strText + ',' + $(this).val();	
			
		}else{
						
			
			if($(this).val() == strSplit[strSplit.length-1]){	//如果是最后个词，是没有逗号			
				strText = strText.replace( "," + $(this).val() , "");				
            }else{				
				strText = strText.replace( $(this).val() + "," , "");				
			}
			
			if(strSplit.length == 1){	
				strText = strText.replace( $(this).val() , "--请选择--");				
			}
		}
		
		if(strText.substr(0,1) == ","){
			$("#"+textId).text(ComWbj.subStrChar(strText,1,strText.length));
		}else if(strText.substr(0,8) == "--请选择--,"){
			$("#"+textId).text(ComWbj.subStrChar(strText,8,strText.length));
		}else{
			$("#"+textId).text(ComWbj.subStrChar(strText,0,strText.length));
		}
	    
	});	
	
	$("#"+selectDivId).live('mouseover', function() {   //显示			
		$(this).show();		
	});
	
	$("#"+selectDivId).live('mouseout', function() {	//隐藏		
		
		$(this).hide();		
	});
},
/******************************************   下拉列表操作   **************************************************/
/**
设置下拉列表框的值
*@param  id：select 的 id 
*@param  list：select 的 list值
*@param  itemId：option 的 value值
*@param  itemName：option 的 name值
*/
setSelectVal:function(id,list,itemId,itemName){
	var options = '';
	options += "<option value=''>--请选择--</option>";
	$.each(list,function(i,item){
		options += "<option value='" + itemId + "'>" + itemName + "</option>";
	});
	$("#"+id).empty(); 
	$("#"+id).append(options);
},
/******************************************   单选按钮操作   **************************************************/
radioIsChecked:function(n){ //判断一组中的某个单选按钮是否选中
	alert($(":radio[name='ReplyType']").eq(n).is(":checked"));
},
/***************************  排序 医生信息->医生排序；科室信息->科室排序   ***************************************/
ZH:function(list,i,fs){ //重排序
	if(fs==1){
		var dd={};
		dd=list[i];
		list[i]=list[i-1];
		list[i-1]=dd;
	}else if(fs==2){
		var dd={};
		dd=list[i];
		list[i]=list[i+1];
		list[i+1]=dd;
	}else{
		var dd={};
		dd=list[i];
		for(var j=i;j>0;){
			list[j]=list[j-1]
			j=j-1;
		}
		list[0]=dd;
	}
	return list;
},
goUp:function(i){	//上移
	if(i==0){ return false; }
	SORTLIST = ComWbj.ZH(SORTLIST,i,1);  //SORTLIST是全局变量	
	PlayV();
},
goFirst:function(i){ //置顶
	if(i==0){ return false; }
	SORTLIST = ComWbj.ZH(SORTLIST,i,3);   //SORTLIST是全局变量
	PlayV();
},
goDown:function(i){ //下移
	if(i==(SORTLIST.length-1)){
		return false;
	}
	SORTLIST = ComWbj.ZH(SORTLIST,i,2);   //SORTLIST是全局变量
	PlayV();
},
/**
 * 快捷：向上	
 * */
fastUpZH:function(list,i,fs,num){	//list是要排序的列表；i是当前对象；fs是向上或者向下排序；num是共跳转多少行
	
	var temp = []; 	//要排序的列表
	var rr = []; var ss = []; var a; var z; var x; var y; var j;
	var s = [];  //上
	var ff = []; //中
	var b = []; //下	
	
	if(fs==11){ //之前			
		for(var j=i-num;j<=i;j++){	temp[j] = list[j]; }  //要排序的列表	
		ComWbj.clearArry(temp); //清空数组空值
		
		for(a=0;a<=num;a++){  //重新生成正确的排序	
			if(a != num) rr[a] = temp[a];  			
			else ss[0] = temp[a]; 
		}		
		for(z=0;z<i-num;z++){	s[z] = list[z]; }  //要排序的列表
		
		ff = $.merge(ss, rr);   //将正确的排序保存到数组			 		
		y=0;
		for(x=i*1+1*1;x<list.length;x++){	b[y] = list[x]; y++; }  //要排序的列表
		
		list = $.merge( $.merge(s,ff), b); //将正确的排序保存到数组
		
	}else{ //之后			
		for(j=i*1-num*1+1*1;j<=i;j++){	temp[j] = list[j]; }  //要排序的列表	
		ComWbj.clearArry(temp); //清空数组空值
		
		for(a=0;a<num;a++){  //重新生成正确的排序	
			if(a !=num*1-1*1 ) rr[a] = temp[a];  			
			else ss[0] = temp[a]; 
		}		
		for(z=0;z<i*1-num*1+1*1;z++){	s[z] = list[z]; }  //要排序的列表
		
		ff = $.merge(ss,rr);   //将正确的排序保存到数组			 		
		y=0;
		for(x=i*1+1*1;x<list.length;x++){	b[y] = list[x]; y++; }  //要排序的列表		
		list = $.merge( $.merge(s,ff), b); //将正确的排序保存到数组
	}	
	
	return list;
},
/**
 * 快捷：向下	
 * */
fastDownZH:function(list,i,fs,num){	//list是要排序的列表；i是当前对象；fs是向上或者向下排序；num是共跳转多少行	
	var temp = []; 	//要排序的列表
	var rr = []; var ss = []; var a; var z; var x; var y; var j;
	var s = [];  //上
	var ff = []; //中
	var b = []; //下	
	
	if(fs==11){ //之前		
		for(var j=i-num;j<=i;j++){	temp[j] = list[j]; }  //要排序的列表	
		ComWbj.clearArry(temp); //清空数组空值
		
		for(a=0;a<=num;a++){  //重新生成正确的排序	
			if(a != num) rr[a] = temp[a];  			
			else ss[0] = temp[a]; 
		}		
		for(z=0;z<i-num;z++){	s[z] = list[z]; }  //要排序的列表
		
		ff = $.merge(ss, rr);   //将正确的排序保存到数组			 		
		y=0;
		for(x=i*1+1*1;x<list.length;x++){	b[y] = list[x]; y++; }  //要排序的列表
		
		list = $.merge( $.merge(s,ff), b); //将正确的排序保存到数组
		
	}else{ //之后	
		for(j=i;j<=i*1+num*1;j++){ temp[j] = list[j]; }  //要排序的列表	
		ComWbj.clearArry(temp); //清空数组空值		
		
		for(a=0;a<=num;a++){  //重新生成正确的排序	
			if(a >0 ) rr[a] = temp[a];  			
			else ss[0] = temp[a]; 
		}
		
		for(z=0;z<i;z++){	s[z] = list[z]; }  //要排序的列表		
		ff = $.merge(rr,ss);   //将正确的排序保存到数组			
		y=0;
		for(x=i*1+1*1+num*1;x<list.length;x++){	b[y] = list[x]; y++; }  //要排序的列表
		
		list = $.merge( $.merge(s,ff), b); //将正确的排序保存到数组
		ComWbj.clearArry(list); //清空数组空值
	}	
	
	return list;
},
/****  具体查看：user/js/zwqxsz.js:function init()   *****/
//在li集合中向上移动
up:function(obj){	
    var _div = $(obj).parent().parent(),
        _divp = _div.parent() ,
	    _divs = _divp.children("li") ,
	    _divindex = _divs.index(_div);
	
	if(_divindex>0){
	  _divs.eq(_divindex-1).before(_div);
	}
},
//在li集合中向下移动
down:function(obj){
	 var _div = $(obj).parent().parent() ,
	    _divp = _div.parent() ,
		_divs = _divp.children("li") ,
		_divindex = _divs.index(_div);
		
	 if(_divindex<_divs.length-1){
	    _divs.eq(_divindex+1).after(_div);
	 }
},
vagueDate:function(id,Diseasejson){		
	$("#"+id).focus(function(){
        return false;
    }).autocomplete(Diseasejson,{
		max:15,
	 	scrollHeight: 300,
		matchContains: true,
		formatItem: function(row, i, max){ return row.name },
		formatMatch: function(row, i, max) { return row.name + "[" + row.sname+"]"; },
		formatResult: function(row) { return row.name; }
	}); 
	$("#"+id).click().autocomplete(Diseasejson,{
		max:15,
	 	scrollHeight: 300,
		matchContains: true,
		formatItem: function(row, i, max){ return row.name },
		formatMatch: function(row, i, max) { return row.name + "[" + row.sname+"]"; },
		formatResult: function(row) { return row.name; }
	});
	$("#"+id).result(function(event, data, formatted){		
		if(data){		
			$("#"+id).val(data.id);  $("#"+id).val(data.name);  $("#"+id).val(data.name);
		}else{			
			$("#"+id).val(0);  $("#"+id).val('');  $("#"+id).val('');
		}
	});
},
/**
仿芒果网智能提示
*@param  TopOrgName：第一次点击时，展示出来的列表对象
*@param  FindOrdName：用户输入值时，匹配好的列表对象
*@param  inputID：隐藏域ID
*/
suggestComplete:function(TopOrgName,FindOrdName,inputID){
	
	
},
/***************************  跟时间有关  ***************************************/
/**
比较日期
*@param   stattime：开始时间  
*@param   endtime：结束时间   
*@param  str：提示的信息 
*@use     if(!compareDate($('#DateTimeStart').val(),$('#DateTimeEnd').val())){			   
			  ComWbj.alertMessage('开始时间不能比结束时间大','');
			  return;
		  }
*/
compareDate:function(stattime,endtime,str){   
    var start=new Date(stattime.replace("-", "/").replace("-", "/"));  
    var endTime=$("#endTime").val();  
    var end=new Date(endtime.replace("-", "/").replace("-", "/")); 
    if(end<start){  
    	ComWbj.alertIconNo("提示",str,"warning");  
    	return;
    }
},
/**
返回N天[前或者后]当前日期的字符串格式（-或者/格式）
*@param   Nday： 多少天后 
*@param   str：  字符串格式（-或者/格式）
*@调用EX     getDateStr(7,'/');
*/
getDateStr:function(Nday,str,opt) { 	
	var dd = new Date();
	if(opt == "+") dd.setDate(dd.getDate()+Nday);//获取Nday天前的日期
	else dd.setDate(dd.getDate()-Nday);//获取Nday天后的日期 
	var y = dd.getFullYear();
	var m = dd.getMonth()+1;//获取当前月份的日期
	var d = dd.getDate();
	if(m < 10) m = "0"+m;
	if(d<10) d = "0"+d;	
	if(str == "-") return y+"-"+m+"-"+d;
	else if(str == "/") return y+"/"+m+"/"+d; 
},
//获取完整的时间，返回EX：2014年07月18日 10:21:16 星期五
clockOn:function() {
    var now = new Date();
    var year = now.getFullYear(); 
    var month = now.getMonth();
    var date = now.getDate();
    var day = now.getDay();
    var hour = now.getHours();
    var minu = now.getMinutes();
    var sec = now.getSeconds();
    var week;
    month = month + 1;
    if (month < 10) month = "0" + month;
    if (date < 10) date = "0" + date;
    if (hour < 10) hour = "0" + hour;
    if (minu < 10) minu = "0" + minu;
    if (sec < 10) sec = "0" + sec;
    var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    week = arr_week[day];
    var time = "";
    time = year + "年" + month + "月" + date + "日" + " " + hour + ":" + minu + ":" + sec + " " + week;
    $("#bgclock").html(time);
    var timer = setTimeout("clockon()", 200);
},
//根据数字：1、2、3,返回字符串时段（上午、下午、晚上）
getTime:function(timeID){ 
	var time='';
	switch (parseInt(timeID)){
		case 1: time='上午'; break;
		case 2: time='下午'; break;
		case 3: time='晚上'; break;
		default: break;
	}
	return time;
},
getSubstrTime:function(time){
	if(time == null || time == undefined){
		return '';
	}else{
		return time.substring(0,11);
	}
},
//根据数字：1、2、3、4、5、6、7，设置字符串星期值
getWeek:function(weekID){
	var xq='未知';
	switch (weekID) {
		case 0: xq='星期日'; break;
		case 1: xq='星期一'; break;
		case 2: xq='星期二'; break;
		case 3: xq='星期三'; break;
		case 4: xq='星期四'; break;
		case 5: xq='星期五'; break;
		case 6: xq='星期六'; break;
		default: break;
	}	
	return xq;
},
//根据系统当前时间获取星期几
chinaWeek:function(){
    var weekDay = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];   
    var myDate = new Date(Date.parse(v.replace(/-/g, "/")));     
    return weekDay[myDate.getDay()];    
},
/***************************  点击input时，弹出树  ***************************************/
/**
在input下面弹出装载树的层
* @param mainId: 要弹出层的控件的ID
* @param divId: 装载内容的控件的ID名
*/
showMenu:function(mainId,divId){
	var divObj = $("#"+mainId);
	var divOffset = $("#"+mainId).offset();
	$("#"+divId).css( { left : divOffset.left + "px",
		                top : divOffset.top + divObj.outerHeight() + "px"
	}).slideDown("fast");
},
/**
* @param divId: 弹出的层内容的ID
*/
hideMenu:function(divId){  $("#"+divId).fadeOut("fast"); },
/**
选中弹出层里面的值
* @param mainId: 要弹出层的控件的ID
* @param divId: 装载内容的控件的ID名
*/
changeMenu:function(mainId,divId){
	$("#"+mainId).mouseout(function(event){
		    var div = document.getElementById(divId);  
		    var x=event.pageX;  
		    var y=event.pageY;  
		    var divx1 = div.offsetLeft;  
		    var divy1 = div.offsetTop;  
		    var divx2 = div.offsetLeft + div.offsetWidth;  
		    var divy2 = div.offsetTop + div.offsetHeight; 
		    if( x < divx1 || x > divx2 || y < divy1 || y > divy2){  
		    	ComWbj.hideMenu(divId);  
		    }  
	})
	$("#"+divId).mouseout(function(event){
		    var div = document.getElementById(divId);  
		    var x=event.pageX;  
		    var y=event.pageY;  
		    var divx1 = div.offsetLeft;  
		    var divy1 = div.offsetTop;  
		    var divx2 = div.offsetLeft + div.offsetWidth;  
		    var divy2 = div.offsetTop + div.offsetHeight; 
		    if( x < divx1 || x > divx2 || y < divy1 || y > divy2){  
		    	ComWbj.hideMenu(divId);  
		   }  
	})   
},
/***************************  图片相关  ***************************************/
/**
查看图片：放大、缩小
* @param url: 大图的url
*/
lookImg:function(url){
	YihuUtil.dialog( {
		title : '查看图片',
		id : 'lookImg',
		padding: '0px 0px',
		content : '<div  style=" width:500px; height:500px;overflow:auto;"><img src="'+url+'" id="lookImg" style="width:500px; height:500px;" /></div>',
		lock : true,
		button : [ {
			name : '放大',
				focus : true,
				callback:function(){
			var doc = this.DOM.dialog[0];
			var t1=$(doc).find("#lookImg");
			var width=t1.css('width');
			t1.css('width',parseInt(width)*1.1+'px');
          var height=t1.css('height');
			t1.css('height',parseInt(height)*1.1+'px');
			    return false;
		        }
			},{
			name : '缩小',
			callback:function(){
				var doc = this.DOM.dialog[0];
				var t1=$(doc).find("#lookImg");
				var width=t1.css('width');
				t1.css('width',parseInt(width)/1.1+'px');
	            var height=t1.css('height');
				t1.css('height',parseInt(height)/1.1+'px');
			    return false;
		        }
		} ]
	})
},
/***************************  跟页面显示有关(css)  ***************************************/
/**
颜色区分:设置一种不同颜色
* @param val: 列表显示的值
* @param name: 要显示不同颜色，所匹配的名称
* @param color: 设置的样式名
* @调用EX   setColor_1(name,"待接收","c_djs");
*/
setColor_1:function(val,name,color){	
	if(val==name){		
		return "class='"+color+"'";
	}
},
/**
颜色区分:设置俩种不同颜色
* @param val: 列表显示的值
* @param name1: 要显示不同颜色，所匹配的名称
* @param color1: 设置的颜色值
* 
* @param name2: 要显示不同颜色，所匹配的名称
* @param color2: 设置的样式名
* @调用EX   setColor_1(name,"待接收","c_djs","待就诊","c_jz");
*/
setColor_2:function(val,name1,color1,name2,color2){	
	if(val==name1){		
		return "class='"+color1+"'";
	}else if(val==name2){
		return "class='"+color2+"'";
	}
},
/**
选项卡：tab切换
*@param  tabconId：装载内容的层的ID名
*@param  menuId： 装载li的Ul的ID名   
*@use    tabs('tabcon','tabmenu'); 
*注意  ：要定义样式名：tab-con，用于控制显示和隐藏div  
*/
tabs:function(tabconId,menuId){
	$tabcon = $("#"+tabconId);
	$tabcon.find(".tab-con:first-child").show();
	$("#"+menuId).delegate(
			"li",
			"click",
			function() {
				var i = $(this).index();
				$("#"+menuId).find("li").removeClass("on");
				$(this).addClass("on");
				$tabcon.find(".tab-con").hide().end().find(
						".tab-con:eq(" + i + ")").show();
			})
},
setScreenAutoHeight:function(id){ //根据桌面可视范围设置高度
	var h = window.screen.availHeight;	
	$("#"+id).css("height",h-400);
},
/***********************************************  页面常用的辅助功能  ***************************************/
/**
新窗口预览
*@param  url：预览要跳转的页面地址  
*/
preview:function(url){
	if( $.browser.msie && ( $.browser.version == '8.0' || $.browser.version == '7.0' || $.browser.version == '6.0') ){		    
	    alert("为了让您看到更好的展示效果，请您在最新版本的IE，或者火狐、谷歌浏览器中预览您的微官网！");		   
	}else{	
	    window.open(url,'','width=380px,height=600px');
	}    
},
/**
打印
*@param  url：打印结果页面地址 
*@param  data：要传给打印结果页面的数据
*/
printHtml:function(url,data){
	var k=window.showModalDialog(url,data,"dialogWidth=750px;dialogHeight=500px;status:no;location=No;");// toolbar=No代表新窗口不显示工具栏(设置为yes则相反)  location=No窗口不显示地址栏(同上)  status=No窗口不显示状态栏(同上)  membar=No窗口不显示菜单(同上)  scrollbars=auto表示窗口滚动条自动显示  resizable=yes表示窗口可以用鼠标调整大小  width=500设置窗口宽度  height=500设置窗口高度。
},
isPC:function () { //判断是PC还是手机
	var userAgentInfo = navigator.userAgent;	
	var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
	var flag = true;
	for (var v = 0; v < Agents.length; v++) {
	    if (userAgentInfo.indexOf(Agents[v]) > 0) {
	       flag = false;
	       break;
	    }
	}
	return flag;
},
copyText: function (id) {  //复制文本内容
	var targetText = document.getElementById(id); 
	try { 
		var clipText = targetText.createTextRange(); 
		clipText.execCommand("Copy"); 
		ComWbj.alertIconNo('提示','复制成功，可以按Ctrl+V粘贴','succeed');
	} catch(e) { 
		ComWbj.alertIconNo('提示','您的浏览器不支持剪贴板复制,请手工复制文本框内容！','warning');
		targetText.focus();//获得焦点 
		targetText.select();//选中文本 		
	} 
},
/***********************************************  页面常用的控件  ***************************************/
/**
百度的富文本编辑器：Ueditor
*@param  id：装载富文本编辑器的div的ID
*@use  ue1=ComWbj.baiduUeditor('content1');
       ue2=ComWbj.baiduUeditor('content2');  //一个页面可以定义多个富文本编辑器
  
  取值：var GeContent1=ue1.getContent();
       var GeContent2=ue2.getContent();
*/
baiduUeditor:function(id){	
	//var ue = UE.getEditor(id,{ autoHeight: false ,
	return UE.getEditor(id,{ autoHeight: false ,
 		autoClearinitialContent :false,
 		autoFloatEnabled: false,
 		catchRemoteImageEnable:false,
 		toolbars: [ 		          [ 
 		          'undo', //撤销
 		          'redo', //重做
 		          'bold', //加粗
 		          'indent', //首行缩进 		          
 		          'italic', //斜体
 		          'underline', //下划线
 		          'strikethrough', //删除线
 		          'subscript', //下标
 		          'fontborder', //字符边框
 		          'superscript', //上标
 		          'formatmatch', //格式刷
 		          'blockquote', //引用
 		          'pasteplain', //纯文本粘贴模式
 		          'selectall', //全选 		          
 		          'horizontal', //分隔线
 		          'removeformat', //清除格式
 		          'time', //时间
 		          'date', //日期
 		          'unlink', //取消链接
 		          'inserttable', //插入表格
 		          'insertrow', //前插入行
 		          'insertcol', //前插入列
 		          'mergeright', //右合并单元格
 		          'mergedown', //下合并单元格
 		          'deleterow', //删除行
 		          'deletecol', //删除列
 		          'splittorows', //拆分成行
 		          'splittocols', //拆分成列
 		          'splittocells', //完全拆分单元格
 		          'deletecaption', //删除表格标题
 		          'inserttitle', //插入标题
 		          'mergecells', //合并多个单元格
 		          'deletetable', //删除表格
 		          'cleardoc', //清空文档
 		          'insertparagraphbeforetable', //"表格前插入行"
 		          'insertcode', //代码语言
 		          'fontfamily', //字体
 		          'fontsize', //字号
 		          'paragraph', //段落格式
 		          'simpleupload', //单图上传
 		          'insertimage', //多图上传
 		          'insertvideo',//插入视频
 		          'edittable', //表格属性
 		          'edittd', //单元格属性
 		          'link', //超链接
 		          'spechars', //特殊字符
 		          'searchreplace', //查询替换
 		          'map', //Baidu地图
 		          'gmap', //Google地图
 		          'justifyleft', //居左对齐
 		          'justifyright', //居右对齐
 		          'justifycenter', //居中对齐
 		          'justifyjustify', //两端对齐
 		          'forecolor', //字体颜色
 		          'backcolor', //背景色
 		          'insertorderedlist', //有序列表
 		          'insertunorderedlist', //无序列表
 		          'fullscreen', //全屏
 		          'directionalityltr', //从左向右输入
 		          'directionalityrtl', //从右向左输入
 		          'rowspacingtop', //段前距
 		          'rowspacingbottom', //段后距
 		          'pagebreak', //分页
 		          'imagecenter', //居中
 		          'lineheight', //行间距
 		          'edittip ', //编辑提示
 		          'customstyle', //自定义标题
 		          'autotypeset', //自动排版
 		          'touppercase', //字母大写
 		          'tolowercase', //字母小写
 		          'drafts', // 从草稿箱加载
 		          'charts', // 图表
 		          'source', //源代码
 		          'preview', //预览
 		          'help' //帮助
 		          ]]
	 	 });
},
/**
分页控件
*@param  totalcounts：列表总数
*@param  pagecount：一共多少页
*@param  pagesize：页面展示数量
*/
allPages: function (totalcounts, pagecount, pagesize) {
    $("#pager").pager( {
	    totalcounts : totalcounts,
	    pagesize : pagesize,
	    pagenumber : $("#pagenumber").val(),
        pagecount : pagecount,
	    buttonClickCallback : function(a) {
		   $("#pagenumber").val(a);
		   init();				
	    }
    });   
},
getTotalPage:function(total,pagesize){ //hql 2014-8-5:获取多少页
	var ys=total%pagesize; var zs=total/pagesize;
	if(ys==0) return parseInt(zs);
	else return parseInt(zs)+1;
},
/**
 * 初始化树.
 * @param treedata
 * @param treeoption
 * @return
 */
initTree:function(treedata,treeoption){	
	var htmls = '<ul>';	
	$.each(treedata,function(index,val){		
	var formatli = null;
	if(treeoption.formatli){ formatli = treeoption.formatli; }	
	if(formatli){		
		var li = formatli(index,treedata,val);		
		var children = val.children;		
		if(children.length > 0){ li += initTree(children,treeoption,true); }		
		htmls += li;		
	}
    });
	htmls += '</ul>';	
	$("#"+treeoption.id).html(htmls);	
    $("#"+treeoption.id).SimpleTree({
        click: treeoption.onclick
    });
	return htmls;
},
/**
 * 生成树要用的数据类型 * 
 * @param data 数据集
 * @param attrpid 父级id的属性名称
 * @param attrpidv  父级id的属性名称的值是多少表示改对象是父级对象
 * @param id 主键属性名称
 * @return
 */
initTreeData:function(data,attrpid,attrpidv,id){	
	var treedate = [];
	var chdata = {};
	$.each(data,function(index,val){		
		if(val[attrpid] == attrpidv) treedate.push(val); 
		val.children = [];
		chdata[val[id]]=val;
	});	
	$.each(data,function(index,val){
		if(val[attrpid] != attrpidv){
			if(chdata[val[attrpid]]!=undefined)  chdata[val[attrpid]].children.push(val);  
		}
	});
	return treedate;
},
//hql 2014-8-6: 点击新增坐诊科室,弹出树
clickTreeInput: function(treeData,treeUl,treeInput,treeDiv, setTreeNode){	
	
	var setting = {
			view : { showIcon : false, showLine : false },
			data: { key: {
					name: "deptName"
				  }
			},
			callback : {
				onAsyncSuccess : function() { },
				onClick : setTreeNode
			}
		};	
	$.fn.zTree.init($("#"+treeUl), setting, treeData);
	ComWbj.showMenu(treeInput,treeDiv);
},

/**
图片上传
*@param  id：点击触发上传事件的file控件的ID号   
*@param  imgID：用于显示上传好的img控件的ID号   
*@use    <div class="xd"><!--请注意：btn-gray-l这个class名，input和button要一致-->     	
		   <input id="up" onchange="uploadImg('up','disImg');" class="cs jd iefa   btn-gray-l" type="file"/>
		   <input type="button" class="btn-gray-l" value="上传图片"></input>
	     </div> 
*/
upLoadImg:function(id, imgID) {	
	    var filename = $("#" + id).val();
	    if (filename == null || filename == '')  return; 
		if(filename.indexOf(".")>-1){			
		    var p=filename.lastIndexOf(".");
			var strp=filename.substring(p,filename.length)+'|';
			if(".jpeg|.gif|.jpg|.png|.bmp|.pic|".indexOf(strp.toLowerCase())==-1){
				ComWbj.alertIconNo('图片选择错误提示','图片格式必须为.jpeg|.jpg|.gif|.png','warning');
			    return;
			}
		}
		var arrID = [ id ];		
		$.yihuUpload.ajaxFileUpload( {
			url : '/WbjUI/servlet/UploadFileServlet?param={hosid:' + YihuUtil.getSession().orgid
					+ '}&File=true&filename=' + filename + "&Api=WBJ", // 用于文件上传的服务器端请求地址
			secureuri : false,// 一般设置为false
			fileElementId : arrID,// 文件上传空间的id属性 <input type="file" id="file" name="file" />
			dataType : 'json',// 返回值类型 一般设置为json
			success : function(data, status) {			
				var uri = data.Uri;
				var name = data.NewFileName;
				var fname = data.FileName;
				var size = data.Size;
				var old = $("#" + id + "_f");
			    $("#" + imgID).attr("src", uri);
		    },
			error : function(data, status, e) {
				ComWbj.alertIconNo("提示","上传失败!",'error');
			}
		});
},
/**
 * 设置数据表格的参数
 * 
 * @param tableId：表格ID
 * @param url：ajax请求地址
 * @param columns：表格具体的列	
 */
dataTable:function(tableId,url,columns){	
	$("#" + tableId).dataTable({
		 "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",//定义DataTable布局的一个强大属性
	      "sPaginationType": "bootstrap",//分页样式使用bootstrap
		  "sScrollX": "100%",   //表格的宽度
		  "sScrollXInner": "100%",   //表格的内容宽度
		  "bScrollCollapse": true,  //当显示的数据不足以支撑表格的默认的高度时，依然显示纵向的滚动条。(默认是false) 
		  "bPaginate": true,  //是否显示分页
		  "bLengthChange": true,  //每页显示的记录数
		  "bFilter": true, //搜索栏
		  "bSort": true, //是否支持排序功能
		  "bInfo": true, //显示表格信息
		  "bAutoWidth": true,  //自适应宽度
		  //"sAjaxDataProp":data, //sAjaxDataProp
		  "sAjaxSource": url,  
	        //"fnServerData": executeQuery,  
	        "fnServerParams": function (aoData) {  
	            aoData.push({"name": "conds", "value": data});  
	      },  //ajax请求地址
		  "aaSorting": [[1, "asc"]],  //设置默认的列——排序 ，第一个参数表示数组 (由0开始)。1 表示Browser列。第二个参数为 desc或是asc
		  "aoColumns": columns,//列设置，表有几列，数组就有几项
		  "bProcessing": true,
		  "bStateSave": true, //保存状态到cookie *************** 很重要 ， 当搜索的时候页面一刷新会导致搜索的消失。使用这个属性就可避免了
		  "sPaginationType": "full_numbers", //分页，一共两种样式，full_numbers和two_button(默认)
		  "oLanguage": {
		      "sLengthMenu": "每页显示 _MENU_ 条记录",
		      "sZeroRecords": "Sorry~ 未找到！您可以试试搜索其他词",
		      "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
		      "sInfoEmtpy": "喔喔...查询不到相关数据了",
		      "sInfoFiltered": "数据表中共有 _MAX_ 条记录)",
		      "sProcessing": "正在加载中...",
		      "sSearch": "搜索",
		      "sUrl": "", //多语言配置文件，可将oLanguage的设置放在一个txt文件中，例：Javascript/datatable/dtCH.txt
		      "oPaginate": {
		          "sFirst":    "首页",
		          "sPrevious": " 上一页 ",
		          "sNext":     " 下一页 ",
		          "sLast":     "尾页 "
		      }
		  }, //多语言配置
		  "bJQueryUI": false, //可以添加 jqury的ui theme  需要添加css
		       "aLengthMenu": [[10, 25, 50, -1], ["每页10条", "每页25条", "每页50条", "显示所有数据"]]  //设置每页显示记录的下拉菜单
		  });
},
/**
仿芒果网智能提示
*@param  orgNameId：搜索机构名称的输入框ID   
*@param  hiddenOrdId： 
*@param  listDivId：展示查询结果的div的ID
*@param  orgId:对应机构id
*/
getAllOrgName:function(orgNameId,hiddenOrdId,listDivId,orgId){
	
	//智能联想提示
	var TopOrgName = new Array();
	var FindOrdName = new Array();
	
	var params = {};	
	params.start = 0;
	params.limit = 10;
	if(orgId!=null){
		params.orgid = orgId;
	}else{
		params.orgid = session.orgid;
	}
	
	params.status = 1 // :开启，-1：关闭，0待审核
	//params.medicalorgtype = $('#medicalorgtype').val();  //机构类别	1 医院 2 社区
	//params.provinceid = $('#sheng').val();  //省份
	//params.cityid = $('#shi').val();  //城市id
	
	$.post('/WbjUI/wbj2/business/web/ReferralZcsq_queryReferralPath.do', params, function (result) {
		if(result.Code==10000){	
			 $.each(result.Result,function(i,item){				 
				 if( result.Result.length > 10){					 
					 if(i < 10){						 
						 TopOrgName[i] = new Array('','' + item.hospitalname + '','','');
					 }				 
				 }else{	
				     
					 TopOrgName[i] = new Array('','' + item.hospitalname + '','','');
				     				 
				 }
				 FindOrdName[i] = new Array('','' + item.hospitalname + '','',''); 	
			  
			 });

			 
		}else {
			ComWbj.alertIconNo('提示',result.Message,'error');
		}
	 }, "json");
	
	$("#"+orgNameId).suggest(            
		      FindOrdName,
		      {
                hot_list:TopOrgName,
                dataContainer:'#'+hiddenOrdId,
                onSelect:function(){  
						//$("#city2").click(); 
				  }, 
				  attachObject:'#'+listDivId
            },"机构"
    );
	
	ComWbj.suggestComplete(TopOrgName,FindOrdName,"hidden_ordId"); //智能提示插件	
},
/**
仿芒果网智能提示
*@param  orgNameId：搜索机构名称的输入框ID   
*@param  hiddenOrdId： 
*@param  listDivId：展示查询结果的div的ID
*/
getAllDoctorName:function(doctorNameId,hiddenDoctorId,listDivId){
	
	//智能联想提示
	var TopDoctorName = new Array();
	var FindDoctorName = new Array();
	
	var params = {};	
	var pa = "{'orgId':" + session.orgid + ",'pageIndex':1,'userTypes':'1,3,4','pageSize':100}";  	
	param.Param=pa;
	param.Api = "baseinfo.DepartUserApi.queryUserInfoList";

	doAjaxLoadData("../bmry_doAll.do", param,function(resp) {
		if (resp.Code == '10000') {				
			
			 $.each(resp.Result,function(i,item){				 
				 if( resp.Result.length > 10){					 
					 if(i < 10){						 
						 TopDoctorName[i] = new Array('','' + item.userName + '','','');
					 }				 
				 }else{	
				     
					 TopDoctorName[i] = new Array('','' + item.userName + '','','');
				     				 
				 }
				 FindDoctorName[i] = new Array('','' + item.userName + '','',''); 	
			  
			 });
			
		}else {
			ComWbj.alertIconNo('提示',resp.Message,'error');
		}
	 }, "json");
	
	$("#"+doctorNameId).suggest(            
		      FindDoctorName,
		      {
                hot_list:TopDoctorName,
                dataContainer:'#'+hiddenDoctorId,
                onSelect:function(){  
						//$("#city2").click(); 
				  }, 
				  attachObject:'#'+listDivId
            },"医生"
    );
	
	ComWbj.suggestComplete(TopDoctorName,FindDoctorName,"hidden_ordId"); //智能提示插件	
},
suggest:function(input, options) {}

}