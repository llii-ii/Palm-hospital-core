String.prototype.Trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
var provinceDATA=[];
provinceDATA.push({'provinceid':'','selected':true,'provincename':'--请选择--'});
provinceDATA.push({'provinceid':'1','provincename':'北京市'});
provinceDATA.push({'provinceid':'2','provincename':'天津市'});
provinceDATA.push({'provinceid':'3','provincename':'河北省'});
provinceDATA.push({'provinceid':'4','provincename':'山西省'});
provinceDATA.push({'provinceid':'5','provincename':'内蒙古'});
provinceDATA.push({'provinceid':'6','provincename':'辽宁省'});
provinceDATA.push({'provinceid':'7','provincename':'吉林省'});
provinceDATA.push({'provinceid':'8','provincename':'黑龙江'});
provinceDATA.push({'provinceid':'9','provincename':'上海市'});
provinceDATA.push({'provinceid':'10','provincename':'江苏省'});
provinceDATA.push({'provinceid':'11','provincename':'浙江省'});
provinceDATA.push({'provinceid':'12','provincename':'安徽省'});
provinceDATA.push({'provinceid':'13','provincename':'福建省'});
provinceDATA.push({'provinceid':'14','provincename':'江西省'});
provinceDATA.push({'provinceid':'15','provincename':'山东省'});
provinceDATA.push({'provinceid':'16','provincename':'河南省'});
provinceDATA.push({'provinceid':'17','provincename':'湖北省'});
provinceDATA.push({'provinceid':'18','provincename':'湖南省'});
provinceDATA.push({'provinceid':'19','provincename':'广东省'});
provinceDATA.push({'provinceid':'20','provincename':'广西省'});
provinceDATA.push({'provinceid':'21','provincename':'海南省'});
provinceDATA.push({'provinceid':'22','provincename':'重庆市'});
provinceDATA.push({'provinceid':'23','provincename':'四川省'});
provinceDATA.push({'provinceid':'24','provincename':'贵州省'});
provinceDATA.push({'provinceid':'25','provincename':'云南省'});
provinceDATA.push({'provinceid':'26','provincename':'西藏'});
provinceDATA.push({'provinceid':'27','provincename':'陕西省'});
provinceDATA.push({'provinceid':'28','provincename':'甘肃省'});
provinceDATA.push({'provinceid':'29','provincename':'青海省'});
provinceDATA.push({'provinceid':'30','provincename':'宁夏'});
provinceDATA.push({'provinceid':'31','provincename':'新疆'});
provinceDATA.push({'provinceid':'32','provincename':'台湾省'});
provinceDATA.push({'provinceid':'33','provincename':'香港'});
provinceDATA.push({'provinceid':'34','provincename':'澳门'})
String.format = function() {
    var i = 1 , args = arguments;
    var str = args[0];
    var re = /\{(\d+)\}/g;
    return str.replace(re,function(){return args[i++];});
};
jQuery.yihu = {
		url: '/HosYuYue/servlet/ActionServlet'+"?datetime="+new Date(),
		fjurl:"/HosYuYue/servlet/FjActionServlet?_id_="+new Date(),
		fileurl:"/HosYuYue/servlet/UploadFileServlet?_id_="+new Date(),
		
		createUploadIframe: function(id, uri)
		{
			//create frame
            var frameId = 'jUploadFrame' + id;
            var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" style="position:absolute; top:-9999px; left:-9999px"';
			if(window.ActiveXObject)
			{
                if(typeof uri== 'boolean'){
					iframeHtml += ' src="' + 'javascript:false' + '"';

                }
                else if(typeof uri== 'string'){
					iframeHtml += ' src="' + uri + '"';

                }	
			}
			iframeHtml += ' />';
			jQuery(iframeHtml).appendTo(document.body);

            return jQuery('#' + frameId).get(0);			
    	},
	       createUploadForm: function(id, fileElementId, data)  
            {  
                //create form     
                var formId = 'jUploadForm' + id;  
                var fileId = 'jUploadFile' + id;  
                var form = jQuery('<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');      
                if (data) {  
              		var _data = data.split('&');
                 	for(var i in _data){
                  		if (_data[i]) {
                        	var _d = _data[i].split('=');
                        	if(_d){
                        	jQuery('<input type="hidden" name="' + _d[0] + '" value="' + _d[1] + '" />').appendTo(form);
                        	}
                   		}
                	}
                }  
                for (var i = 0; i < fileElementId.length; i ++) {  
                    var oldElement = jQuery('#' + fileElementId[i]);  
                    var newElement = jQuery(oldElement).clone();  
                    jQuery(oldElement).attr('id', fileElementId[i]);  
                    jQuery(oldElement).attr('name', fileElementId[i].name);  
                    jQuery(oldElement).before(newElement);  
                    jQuery(oldElement).appendTo(form);  
                }  
                //set attributes  
                jQuery(form).css('position', 'absolute');  
                jQuery(form).css('top', '-1200px');  
                jQuery(form).css('left', '-1200px');  
                jQuery(form).appendTo('body');  
                return form;  
            }  ,
	  	handleError : function( s, xhr, status, e ) 		{
			// If a local callback was specified, fire it
				if ( s.error ) {
					s.error.call( s.context || s, xhr, status, e );
				}
			// Fire the global callback
				if ( s.global ) {
					(s.context ? jQuery(s.context) : jQuery.event).trigger( "ajaxError", [xhr, s, e] );
				}
		} , 
	    ajaxFileUpload: function(s) {
	     
	        // TODO introduce global settings, allowing the client to modify them for all requests, not only timeout		
	        s = jQuery.extend({}, jQuery.ajaxSettings, s);
	        var id = new Date().getTime()   
			var form = $.yihu.createUploadForm(id, s.fileElementId,s.data);
			var io = $.yihu.createUploadIframe(id, s.secureuri);
			var frameId = 'jUploadFrame' + id;
			var formId = 'jUploadForm' + id;		
			
	        // Watch for a new set of requests
	        if ( s.global && ! jQuery.active++ )
			{
				jQuery.event.trigger( "ajaxStart" );
			}            
	        var requestDone = false;
	        // Create the request object
	        var xml = {}   
	        if ( s.global )
	            jQuery.event.trigger("ajaxSend", [xml, s]);
	        // Wait for a response to come back
	        var uploadCallback = function(isTimeout)
			{			
				var io = document.getElementById(frameId);
	            try 
				{				
					if(io.contentWindow)
					{
						 xml.responseText = io.contentWindow.document.body?io.contentWindow.document.body.innerHTML:null;
	                	 xml.responseXML = io.contentWindow.document.XMLDocument?io.contentWindow.document.XMLDocument:io.contentWindow.document;
						 
					}else if(io.contentDocument)
					{
						 xml.responseText = io.contentDocument.document.body?io.contentDocument.document.body.innerHTML:null;
	                	xml.responseXML = io.contentDocument.document.XMLDocument?io.contentDocument.document.XMLDocument:io.contentDocument.document;
					}						
	            }catch(e)
				{
					$.yihu.handleError(s, xml, null, e);
				}
	            if ( xml || isTimeout == "timeout") 
				{				
	                requestDone = true;
	                var status;
	                try {
	                    status = isTimeout != "timeout" ? "success" : "error";
	                    // Make sure that the request was successful or notmodified
	                    if ( status != "error" )
						{
	                        // process the data (runs the xml through httpData regardless of callback)
	                        var data = $.yihu.uploadHttpData( xml, s.dataType );    
	                        // If a local callback was specified, fire it and pass it the data
	                        if ( s.success )
	                            s.success( data, status );
	    
	                        // Fire the global callback
	                        if( s.global )
	                            jQuery.event.trigger( "ajaxSuccess", [xml, s] );
	                    } else
	                        $.yihu.handleError(s, xml, status);
	                } catch(e) 
					{
	                    status = "error";
	                    $.yihu.handleError(s, xml, status, e);
	                }
	
	                // The request was completed
	                if( s.global )
	                    jQuery.event.trigger( "ajaxComplete", [xml, s] );
	
	                // Handle the global AJAX counter
	                if ( s.global && ! --jQuery.active )
	                    jQuery.event.trigger( "ajaxStop" );
	
	                // Process result
	                if ( s.complete )
	                    s.complete(xml, status);
	
	                jQuery(io).unbind()
	
	                setTimeout(function()
										{	try 
											{
												jQuery(io).remove();
												jQuery(form).remove();	
												
											} catch(e) 
											{
												$.yihu.handleError(s, xml, null, e);
											}									
	
										}, 100)
	
	                xml = null
	
	            }
	        }
	        // Timeout checker
	        if ( s.timeout > 0 ) 
			{
	            setTimeout(function(){
	                // Check to see if the request is still happening
	                if( !requestDone ) uploadCallback( "timeout" );
	            }, s.timeout);
	        }
	        try 
			{
	
				var form = jQuery('#' + formId);
				jQuery(form).attr('action', s.url);
				jQuery(form).attr('method', 'POST');
				jQuery(form).attr('target', frameId);
	            if(form.encoding)
				{
					jQuery(form).attr('encoding', 'multipart/form-data');      			
	            }
	            else
				{	
					jQuery(form).attr('enctype', 'multipart/form-data');			
	            }	
	            jQuery(form).submit();
	
	        } catch(e) 
			{			
	            $.yihu.handleError(s, xml, null, e);
	        }
			
			$('#' + frameId).load(uploadCallback	);
	        return {abort: function () {}};	
	
	    },
	
	    uploadHttpData: function( r, type ) {
	     
	        var data = !type;
	        data = type == "xml" || data ? r.responseXML : r.responseText;
	        // If the type is "script", eval it in global context
	        if ( type == "script" )
	            jQuery.globalEval( data );
	        // Get the JavaScript object, if JSON is used.
	        if ( type == "json" )
	            eval( "data = " + data );
	        // evaluate scripts within html
	        if ( type == "html" )
	            jQuery("<div>").html(data).evalScripts();
	
	        return data;
	    },
		post:function(bizAction,param,successFn) 
		{
			param.bizAction=bizAction;
			$.ajax({
		        url :this.url,
		        data:param,
		        type : "POST",
		        dataType : 'json',
		        success : successFn
		    });
		},
		loadStore:function(businTypeID)
	 	{
	 		var res = new Object;
			$.ajax({
      			url : this.url,
     			data:{  
    				businTypeID: businTypeID, bizAction:'dictAction.getDict'
        		},
        		cache : false, 
        		async : false,
        		type : "POST",
        		dataType : 'json',
        		success : function (data){
        			var result = data.result;
        			for ( var iterable_element in result) {
        				res[result[iterable_element].businID] = result[iterable_element].businName;
        			}
        		}
    		});
    		return res;
	 	},
	 	loadDictSelect:function(id,businTypeID,defaultValue)
	 	{
	 			var param = {};
	 			param.bizAction = "dictAction.getDict";
	 			param.businTypeID = businTypeID;
	 			param.emptyText = '--请选择--';
	 			 $.post(this.url,param
	 			 	,function(result){
	 	 			$('#'+id).combobox('loadData',result.result);
	 	 			if(defaultValue != null && defaultValue != ''){
	 	 				$('#'+id).combobox('setValue',defaultValue);
	 	 			}
	 	  		 },"json");
	 	},
	 	loadDictSelectInIDS:function(ids,businTypeID,defaultValue)
	 	{
	 			var param = {};
	 			param.bizAction = "dictAction.getDict";
	 			param.businTypeID = businTypeID;
	 			param.emptyText = '--请选择--';
	 			 $.post(this.url,param
	 			 	,function(result){
	 			 	for(var i=0;i<ids.length;i++){
	 	 				$('#'+ids[i]).combobox('loadData',result.result);
	 	 			}
	 	  		 },"json");
	 	},
		getDictText:function(businTypeID,businID)
		{
			var businName="";
			$.ajax({
		        url : this.url,
		        data:{  
		    		businTypeID: businTypeID, businID: businID ,bizAction:'dictAction.getDictText'
		        },
		        cache : false, 
		        async : false,
		        type : "POST",
		        dataType : 'json',
		        success : function (data){
		        	
		        	businName=data.businName;
		        }
		    });
			return businName;
		},
		formatNull:function(value) {
			if (value == null || typeof(value) == "undefined") {
				return "";
			}
			return value;
		},
		open:function( strUrl,winWidth,winHeight,winLeft,winTop,id){
			
			var win_id='win_'+parseInt(Math.random()*10000);
			if(typeof(id)!="undefined")
			{
				win_id=id;
			}
			var newwin=window.open(	strUrl,
					win_id,
									"width="+ winWidth + ","
									+ "height="+winHeight + ","
									+ "left="+winLeft+","
									+ "top="+winTop+","
									+ "status=yes,toolbar=no,menubar=no,location=yes,scrollbars=yes");
			newwin.focus();
		},
		QueryString:function(val) {
			var uri = window.location.search;
			var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
			return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
		},
		//取医院信息
		getHosInfo:function(ghthosid)
		{
			var res;
			$.ajax({
      			url : this.url,
     			data:{  
					ghthospitalid: ghthosid,bizAction:'ght_JSQueryAction.getGhtHospitalInfo'
        		},
        		cache : false, 
        		async : false,
        		type : "POST",
        		dataType : 'json',
        		success : function (data){
        			res = data;
        		}
    		});
			
    		return res;
		},
		//取社区信息
		getOrgInfo:function(orgid){
			var res;
			$.ajax({
      			url : this.url,
     			data:{  
    				orgid: orgid,bizAction:'ClinicInfoAction.getClinicInfoByOrgId'
        		},
        		cache : false, 
        		async : false,
        		type : "POST",
        		dataType : 'json',
        		success : function (data){
        			res = data;
        		}
    		});
    		return res;
		},
		loadDict:function(businTypeID,emptyText)
		{
			var res;
			$.ajax({
      			url : this.url,
     			data:{  
    						businTypeID: businTypeID, emptyText: emptyText ,bizAction:'dictAction.getDict'
        		},
        		cache : false, 
        		async : false,
        		type : "POST",
        		dataType : 'json',
        		success : function (data){
        			res = data;
        		}
    		});
    		return res;
		}, 
		loadDictNoLogin:function(businTypeID)
		{
			var res;
			$.ajax({
      			url : this.url,
     			data:{  
    						businTypeID: businTypeID, isLogin: '1' ,bizAction:'dictAction.getDict'
        		},
        		cache : false, 
        		async : false,
        		type : "POST",
        		dataType : 'json',
        		success : function (data){
        			res = data;
        		}
    		});
    		return res;
		},
		loadSelect : function(id, param, defaultValue) {
			$.post(this.url, param, function(result) {
				$('#' + id).combobox('loadData', result.result);
				if (defaultValue != null && defaultValue != '') {
					$('#' + id).combobox('setValue', defaultValue);
				}
			}, "json");
		},
		getDictText:function(businTypeID,businID)
		{
			var businName="";
			$.ajax({
		        url : this.url,
		        data:{  
		    		businTypeID: businTypeID, businID: businID ,bizAction:'dictAction.getDictText'
		        },
		        cache : false, 
		        async : false,
		        type : "POST",
		        dataType : 'json',
		        success : function (data){
		        	
		        	businName=data.businName;
		        }
		    });
			return businName;
		},
		getSession:function()
		{
			var ret;
			$.ajax({
		        url : this.url,
		        data:{  
		    			bizAction:'loginAction.writeSessionJSON'
		        },
		        cache : false, 
		        async : false,
		        type : "POST",
		        dataType : 'json',
		        success : function (data){
		        	ret=data;
		        }
		    });
			return ret;
		},
		loadHosDeptTypeID:function()
		{
			var typeids  = this.loadDict("B_HosDept.typeId").result;
			var typeid_combobox = '{"result":[';
			for(var i = 0;i<typeids.length;i++){
				var id = typeids[i].businID;
				var text = typeids[i].businName;
				if(text=='--请选择--'){
					typeid_combobox += '{"businName":"'+text+'","selected":true,"businID":""}';
				}else if(id != '4'){
					typeid_combobox += ',{"businName":"'+text+'","businID":"'+id+'"}';
				}
			}
			typeid_combobox += ']}';
			return eval("("+typeid_combobox+")");
		},
		alert:function(msg){
			$.messager.alert('提示','<font color=red>'+msg+'</font>','error');
		},
		showMsg : function(msg){
			$.messager.show({
	            title:'提示',
	            msg:'<font color=red>'+msg+'</font>',  
	            showType:'show'
	       	});
		},
		jsonObjectToString : function(o) {
			if (o == null)
				return "null";

			switch (o.constructor) { 
			case String:
				var s = o; // .encodeURI();
				if (s.indexOf("}") < 0)
					s = '"' + s.replace(/(["\\])/g, '\\$1') + '"';
				s = s.replace(/\n/g, "\\n");
				s = s.replace(/\r/g, "\\r");
				return s;
			case Array:
				var v = [];
				for ( var i = 0; i < o.length; i++)
					v.push(jsonObjectToString(o[i]));
				if (v.length <= 0)
					return "\"\"";
				//return "" + v.join(",") + "";
				return "[" + v.join(",") + "]";
			case Number:
				return isFinite(o) ? o.toString() : this.jsonObjectToString(null);
			case Boolean:
				return o.toString();
			case Date:
				var d = new Object();
				d.__type = "System.DateTime";
				d.Year = o.getUTCFullYear();
				d.Month = o.getUTCMonth() + 1;
				d.Day = o.getUTCDate();
				d.Hour = o.getUTCHours();
				d.Minute = o.getUTCMinutes();
				d.Second = o.getUTCSeconds();
				d.Millisecond = o.getUTCMilliseconds();
				d.TimezoneOffset = o.getTimezoneOffset();
				return this.jsonObjectToString(d);
			default:
				if (o["toJSON"] != null && typeof o["toJSON"] == "function")
					return o.toJSON();
				if (typeof o == "object") {
					var v = [];
					for (attr in o) {
						if (typeof o[attr] != "function"){
							var attrValue = this.jsonObjectToString(o[attr]);
							if(attrValue.length>0 && attrValue!=null){
								v.push('"' + attr + '": ' + attrValue);
							}
						}
					}

					if (v.length > 0)
						return "{" + v.join(",") + "}";
					else
						return "{}";
				}
				alert(o.toString()); 
				return o.toString();
			}
		}
		
		
};	
