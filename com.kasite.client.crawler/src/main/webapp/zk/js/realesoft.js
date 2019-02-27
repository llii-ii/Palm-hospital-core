function showModalWindow(url, options) {
	if (window.showModalDialog) {
		window.showModalDialog(url, '', 'dialogWidth:'+options.width+';dialogHeight:'+options.height);
	} else {
		window.open(url, '', 'height='+options.height+',width='+options.width+',toolbar=no,directories=no,status=no,location=no,menubar=no,scrollbars=no,resizable=no ,modal=yes');
	}
}
function showJqueryModalWindow(url, width, height, title)
{
	if(top.jqueryWindows == null || top.jqueryWindows == 'undefined')
	{
		top.jqueryWindows = new Array();
	}
	
	var jqueryWindow = new Object();
    
	jqueryWindow.dialog = $('<iframe frameborder="0" src="'+ url +'"/>').dialog({
		resizable: true,
		height:height,
		width:width,
		autoOpen: true,
		modal: true,
		title:title || ''
	});
	
	top.jqueryWindows.push(jqueryWindow);
}

function showSimpleWindow(content, width, height, title){
	if(top.jqueryWindows == null || top.jqueryWindows == 'undefined'){
		top.jqueryWindows = new Array();
	}
	
	var jqueryWindow = new Object();
    content = '<div>'+content+'</div>';
	jqueryWindow.dialog = $(content).dialog({
		resizable: true,
		height:height,
		width:width,
		autoOpen: true,
		modal: true,
		title:title || ''
	});
	
	top.jqueryWindows.push(jqueryWindow);
}

function showDialog(sUrl, oForm, iWidth, iHeight )
{
    if(oForm == undefined){
        oForm = "";
    }
	var left = (screen.width-iWidth)/2;
	var top = (screen.height-iHeight)/2;
	var addedp = "uniDialogp_IEHO_INEQF="+Math.random();
	if(sUrl){
		if(sUrl.indexOf("?") == -1){
			sUrl = sUrl+"?";
		}
		if(sUrl.indexOf("?") != (sUrl.length -1)){
			addedp = "&" + addedp;
		}
	}
	oForm.openerWindow = window;
	if (!window.XMLHttpRequest) {
		if(iHeight!=null && iHeight != undefined){
		iHeight = parseInt(iHeight) + 55;
		}
		if(iWidth!=null && iWidth != undefined){
		iWidth = parseInt(iWidth) + 6;
		}
	}
   var winFeatures = "dialogHeight:" + iHeight + "px; dialogWidth:" + iWidth + "px; dialogLeft:" + left + "px; dialogTop:" + top + "px;help=no;status=no;scroll=yes;";
   return window.showModalDialog(sUrl + addedp, oForm, winFeatures);
}

//调用选择器
//选择器页面js返回 var ra = new Array(2);
//ra[0]="name1,name2";ra[1]="value1,value2";window.returnValue=ra;window.close();
function callSelector(sUrl, oForm, iWidth, iHeight, oHandle){
	var resultArray = showDialog(sUrl, oForm, iWidth, iHeight);// [name, code]
	oHandle(oForm, resultArray);
}

function callJquerySelector(sUrl, oForm, iWidth, iHeight, oHandle, title)
{

	if(top.selectors == null || top.selectors == 'undefined')
	{
		top.selectors = new Array();
	}
	
	var selector = new Object();
	
	selector.callback = oHandle;
	selector._oForm = oForm;
	
	var index = sUrl.indexOf("?");
	if(-1 != index){
		sUrl = sUrl + "&selectorType=Jquery"
	}
	else
	{
		sUrl = sUrl + "?selectorType=Jquery"
	}
	
	
	selector.dialog = $('<iframe frameborder="0" src="'+ sUrl +'"/>').dialog({
		resizable: true,
		height:iHeight,
		width:iWidth,
		autoOpen: true,
		modal: true,
		title:title || ''
	});
	
	top.selectors.push(selector);

}

function singleJquerySelector(id,name) {
	var ra = new Array(2);
	ra[0]=id;
	ra[1]=name;

	var selector = top.selectors.pop();
	
	if(selector != 'undefined'){
		selector.callback(selector._oForm, ra);
		selector.dialog.dialog( "close" );
	}
		
	
}

function multipleJquerySelector(ids)
{
	var selectIds = document.getElementsByName(ids);

	var idnames="";
    for(i=0; i<selectIds.length; i++){

        if(selectIds[i].type=="checkbox" && selectIds[i].checked == true){
        	idnames+=selectIds[i].value;
        	idnames+=',';
        }
        selectIds[i].checked = false;
    }
    idnames=idnames.substring(0, idnames.length-1);
	//alert(idnames);
	if(idnames==""){
		return;
	}
	
	var ra = new Array(2); 
	ra[0]="";
	ra[1]="";
	
	var ids = idnames.split(",");
	for (i=0,j=0;i<(ids.length/2);i++){
		var y = new Option();	    
		ra[0] +=ids[j++];
		ra[0] += ",";
		ra[1] +=ids[j++];
		ra[1] += ",";			
	}
		
	ra[0]=ra[0].substring(0, ra[0].length-1);
	ra[1]=ra[1].substring(0, ra[1].length-1);

	var selector = top.selectors.pop();
	
	if(selector != 'undefined'){
		selector.callback(selector._oForm, ra);
		selector.dialog.dialog( "close" );
	}
  		
}

function delete_multiple_selector_option(select){
	var objSelect = select;
	var length = objSelect.options.length - 1;    
    for(var i = length; i >= 0; i--){    
        if(objSelect[i].selected == true){    
            objSelect.options[i] = null;    
        }    
    }	
}
	
function clear_multiple_selector(select){
	var objSelect = select;
	var length = objSelect.options.length - 1;    
    for(var i = length; i >= 0; i--){       
        objSelect.options[i] = null;       
    }	
}

function addOption(text, val, sl) {
	
	var len = sl.length;
	sl.options[len] = new Option(text, val);
}


function multiple_selector_callBack(ra,select){
		if(typeof(ra) == 'object'){
		var idArray=ra[0].split(",");	
		var nameArray=ra[1].split(",");
		if(select.options.length>0){			
			for(var z=0;z<idArray.length;z++){
				var hasexist=0;
				for(var i=0;i<select.options.length;i++){
					var tmpval = select.options[i].value;
					if(idArray[z]==tmpval){
						hasexist+=1;
					}					
				}
				if(hasexist==0){
					addOption(nameArray[z],idArray[z],select);
				}				
			}
		}else{
			for(var y=0;y<idArray.length;y++){
				addOption(nameArray[y],idArray[y],select);			
			}
		}		
	}		
}

function multipleSelector(ids)
{
	var selectIds = document.getElementsByName(ids);

	var idnames="";
    for(i=0; i<selectIds.length; i++){

        if(selectIds[i].type=="checkbox" && selectIds[i].checked == true){
        	idnames+=selectIds[i].value;
        	idnames+=',';
        }
        selectIds[i].checked = false;
    }
    idnames=idnames.substring(0, idnames.length-1);
	//alert(idnames);
	if(idnames==""){
		return;
	}
	
	var ra = new Array(2); 
	ra[0]="";
	ra[1]="";
	
	var ids = idnames.split(",");
	for (i=0,j=0;i<(ids.length/2);i++){
		var y = new Option();	    
		ra[0] +=ids[j++];
		ra[0] += ",";
		ra[1] +=ids[j++];
		ra[1] += ",";			
	}
		
	ra[0]=ra[0].substring(0, ra[0].length-1);
	ra[1]=ra[1].substring(0, ra[1].length-1);
	
	window.returnValue=ra;       

	window.close();   		
}

function singleSelector(id,name) {
	var ra = new Array(2);
	ra[0]=id;
	ra[1]=name;	
	window.returnValue=ra;       

	window.close(); 
}

function requestError()
{
	$( "#request-error-dialog-confirm" ).dialog( "open" );
	return false;
}

function requestSuccess()
{
	$( "#request-success-dialog-confirm" ).dialog( "open" );
	return false;
}

function getCookieMenu()
{
	//从cook中获取已记入的菜单
	var menuObjsStr = store.get('menus');
	var menuObjs;
	
	if(menuObjsStr != null){
		
		//把字符串转换为对象（数组对象）
		menuObjs = JSON.parse(menuObjsStr);
	}
	
	return menuObjs;
}

//向cook中添加菜单（上次用户点击的菜单）
function addCookieMenu(url, title)
{
	var lastMenuUrl = store.get('lastMenuUrl');
	
	//当前点击的菜单和上回点击的菜单不相同时，才记入cook
	if(lastMenuUrl == null || url != lastMenuUrl)
	{
		//从cook中获取已记入的菜单
		var menuObjsStr = store.get('menus');
		var menuObjs;
		
		if(menuObjsStr != null){
			
			//把字符串转换为对象（数组对象）
			menuObjs = JSON.parse(menuObjsStr);
		}
		else
		{
			menuObjs = new Array();
		}
		
		var menus = get(menuObjs, "url", url);

		if(menus == null || menus.length == 0)
		{
			var menuObj = new Object();
			menuObj.url = url;
			menuObj.title = title;
			menuObj.num = 1;
			menuObjs.push(menuObj);
		}
		else
		{
			var menu = menus[0];
			menu.num = menu.num + 1;
		}
		
		//排序
		menuObjs.sort(function(x,y)
		{
			return parseInt(y.num)-parseInt(x.num);
		});
		
		menuObjsStr = JSON.stringify(menuObjs); //JSON 数据转化成字符串
		store.set('menus', menuObjsStr );
		
		store.set('lastMenuUrl', url);
	}	
}

/**

* 从对象数组中获取属性为objPropery，值为objValue元素的对象

* @param Array arrMenu 数组对象

* @param String objPropery 对象的属性

* @param String objPropery 对象的值

* @return Array 过滤后的数组

*/

function get(arrMenu,objPropery,objValue)
{
   return $.grep(arrMenu, function(cur,i){
          return cur[objPropery]==objValue;
       });
}

/**

* 从对象数组中删除属性为objPropery，值为objValue元素的对象

* @param Array arrMenu 数组对象

* @param String objPropery 对象的属性

* @param String objPropery 对象的值

* @return Array 过滤后数组

*/

function remove(arrMenu,objPropery,objValue)

{

   return $.grep(arrMenu, function(cur,i){

          return cur[objPropery]!=objValue;

       });

}

function getTrueLength(str)
{
	return str.replace(/[^\x00-\xff]/g,"**").length;   
}

function parseSearch(search) {
	var resultObj = {};
	if (!search || search.length < 1) return resultObj;
	search = search.substring(1);
	var items = search.split('&');
	for(var i = 0, size = items.length; i < size; ++i) {
		var item = items[i];
		if (!item) continue;
		var kv = item.split('=');
		resultObj[kv[0]] = typeof kv[1] === "undefined" ? "" : kv[1];
	}
	return resultObj;
}

function doOAuth2() {
	var hash   = window.location.hash;
	if (!hash) return;
	
	var params = parseSearch(hash);
	if (!params || !params['access_token']) return;
	
	var $form  = null;
	$('form').each(function() {
		if ($(this).prop('action').indexOf('j_spring_security_check') > -1) {
			$form = $(this);
		}
	});
	if (!$form || $form.length < 1) return;
	
	$.each(params, function(key, value) {
		var $hidden = $('input[name="' + key + '"]', $form);
		if ($hidden.length < 1) {
			$hidden = $('<input type="hidden" name="' + key + '">');
			$form.append($hidden);
		}
		$hidden.val(value);
	});
	$form.find('button[type="submit"]').click();
}

$(function() {
	doOAuth2();
});
