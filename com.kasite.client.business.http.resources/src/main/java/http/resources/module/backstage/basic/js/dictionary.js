
	var type_arr = new Array();
	var session = null;
	var channelInfo = null;
	var hasDeletePerm = false;
	var hasInsertPerm = false;
	var hasUpdatePerm = false;
	var hasListPerm = false;
	var hasInfoPerm = false;
	$(function(){
		//权限验证
		hasInsertPerm = parent.hasPerms($(".btn-add"));
		hasDeletePerm = parent.hasPerms(null,'dict:delete');
		hasUpdatePerm = parent.hasPerms(null,'dict:update');
		hasListPerm = parent.hasPerms(null,'dict:list');
		hasInfoPerm = parent.hasPerms(null,'dict:info');
		session = Commonjs.getSession();
		if(hasListPerm){
			queryDictionary(1);
		}
	});

	function updateStatus(id,o){
		var state = 0;
		var text = '确认不启用吗？';
		if($(o).attr('class') == 'my-switch-box red'){
			state = 1;
			text = '确认要启用吗？';
		}
		art.dialog({
	 		id: 'testID',
	 	    width: '245px',
	 	    height: '109px',
	 	    content: text,
	 	    lock: true,
	 	    button: [{
	 	      	name: '确定',
	 	       	callback: function () {
	 	       	 	var param = {};
					var data = {'Id':id,'Status':state};
					param.reqParam = Commonjs.getReqParams(data);
					Commonjs.ajax('/basic/updateDictionary.do',param,function(data){
						if(data.RespCode == 10000){
							var html = '';
							if(hasUpdatePerm){
								if(state == 0){
							 		html = '<div class="my-switch-box red" onclick="updateStatus(\''+id+'\',this)"><span></span></div>'+getStateCN(state);
								}else{
									html = '<div class="my-switch-box" onclick="updateStatus(\''+id+'\',this)"><span></span></div>'+getStateCN(state);
								}	
							}
							$('#'+id).empty().html(html);
						}
					});
	 	       	}
	 	 	},{
	 	 		name: '取消'
	 	 	}]
	 	});
	}
	

	function queryDictionary(index){
		var param = {};
		var pageIndex = index-1;
		var pageSize = 10;
		$('#pagenumber').val(index);
		var data = {'Page':{'PIndex':pageIndex,'PSize':pageSize}};
		param.reqParam = Commonjs.getReqParams(data);
		var url = '/basic/queryDictList.do';
		Commonjs.ajax(url,param,function(data){
			$("#dataList tr").eq(0).nextAll().remove();
			if(data.RespCode==10000){
				$(initHtml(data.Data)).insertAfter($("#dataList tr").eq(0));
				if(data.Page.PCount!=undefined){
					if(data.Page.PCount!=0){
						$("#totalcount").val(data.Page.PCount);
					}else{
						if(data.Page.PIndex==0){
							$("#totalcount").val(0);
						}
					}
				}else{
					$("#totalcount").val(0);
				}
				Page($("#totalcount").val(),data.Page.PSize,'pager');
			}
		})
	}

	function initHtml(d){
		var html = '';
		if(d.length){
			$.each(d,function(i,it){
				var str = it.Value;
				if(typeof(str) == "object"){
					str = Commonjs.jsonToString(str);
				}
				html += '<tr><td>'+it.Keyword+'</td>';
				html += '<td>'+subText(str,50,'...')+'</td>';
				html += '<td>'+it.UpId+'</td>';
				html += '<td>'+it.DicType+'</td>';
				if(it.Status == 0){
					html += '<td id="'+it.Id+'">';
					html += '<div class="my-switch-box red" onclick="updateStatus(\''+it.Id+'\',this)">';
				}else{
					html += '<td id="'+it.Id+'">';
					html += '<div class="my-switch-box" onclick="updateStatus(\''+it.Id+'\',this)">';
				}
				html += '<span></span></div>'+getStateCN(it.Status)+'</td>';
				html += '<td><ul class=\"i-btn-list\"><li>';
				html += '<a href="javascript:void(0);" class=\"i-btn\" onclick="editDictionary(\''+it.Id+'\',\''+it.Keyword+'\',\''+encodeURI(str)+'\',\''+it.UpId+'\',\''+it.DicType+'\',\''+it.Status+'\',this)" ><i class=\"i-edit\"></i>编辑</a></i>';
				html += '<li><a href="javascript:void(0);" class=\"i-btn\" onclick="delTemplate(\''+it.Id+'\')" ><i class=\"i-del\"></i>删除</a></li>';
				html+='</ul></td></tr>';
			})
		}else{
			var str = it.Value;
			if(typeof(str) == "object"){
				str = Commonjs.jsonToString(str);
			}
			html += '<tr><td>'+d.Keyword+'</td>';
			html += '<td>'+subText(str,50,'...')+'</td>';
			html += '<td>'+d.UpId+'</td>';
			html += '<td>'+d.DicType+'</td>';
			if(d.Status == 0){
				html += '<td id="'+d.Id+'"><div class="my-switch-box red" onclick="updateStatus(\''+d.Id+'\',this)">';
			}else{
				html += '<td id="'+d.Id+'"><div class="my-switch-box" onclick="updateStatus(\''+d.Id+'\',this)">';
			}
			html += '<span></span></div>'+getStateCN(d.Status)+'</td>';
			html += '<td><ul class=\"i-btn-list\"><li><a href="javascript:void(0);" class=\"i-btn\" onclick="editDictionary(\''+d.Id+'\',\''+d.Keyword+'\',\''+encodeURI(str)+'\',\''+d.UpId+'\',\''+d.DicType+'\',\''+d.Status+'\',this)" ><i class=\"i-edit\"></i>编辑</a></i>';
			html += '<li><a href="javascript:void(0);" class=\"i-btn\" onclick="delTemplate(\''+d.Id+'\')" ><i class=\"i-del\"></i>删除</a></li></ul></td></tr>';
		}
		return html;
	}
	
	function delTemplate(id){
		art.dialog({
	 		id: 'testID',
	 	    width: '245px',
	 	    height: '109px',
	 	    content: '您要删除吗？注意：删除后数据将不能恢复',
	 	    lock: true,
	 	    button: [{
	 	      	name: '确定',
	 	       	callback: function () {
	 	       	 	var param = {};
		 	       	var data = {'Id':id};
					param.reqParam = Commonjs.getReqParams(data);
					var url = '/basic/delDictionary.do';
					Commonjs.ajax(url,param,function(dd){
						Commonjs.alert(dd.RespMessage)
						if(dd.RespCode == 10000){
							queryDictionary($("#pagenumber").val());
						}
					});
	 	       	}
	 	 	},{
	 	 		name: '取消'
	 	 	}]
	 	});
	}
	
	function subText(s,size,s1){
		
		if(Commonjs.isEmpty(s)){
			return '';
		}else if(s.length > size){
			if(s1){
				return s.substring(0,size)+s1;
			}
			return s.substring(0,size);
		}else 
			return s;
	}
	
	function getTypeCN(ModeType){
	
		for(var i=0;i<type_arr.length;i++){
			if(type_arr[i].id == ModeType){
				return type_arr[i].text;
			}
		}
		return null;
	}
	
	
	function linkEditPage(id,text,modeType,msgType,UseChannel,o){
	
		var texts = $(o).parent().prev().text();
		var state = 0;
		if(texts == '未启用'){
			state = 0;
		}else{
			state = 1;
		}
		var url = 'msgRecord_edit.html?id='+id+'&text='+text+'&modeType='+modeType+'&msgType='+msgType+'&state='+state+'&useChannel='+UseChannel;
		o.href = url;
	}

	function getStateCN(state){
		switch(parseInt(state)){
			case 0:return '未启用';
			case 1:return '已启用';
			default:return '';
		}
	}
		
	//分页
	function Page(totalcounts, pagecount,pager) {
		$("#"+pager).pager( {
			totalcounts : totalcounts,
			pagesize : 10,
			pagenumber : $("#pagenumber").val(),
			pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
			buttonClickCallback : function(al) {
				$("#pagenumber").val(al);
				queryDictionary(al);
			}
		});
	}
	
	//添加消息类型
	function editDictionary(id,key,value,upId,dicType,status){
		$("#key").val(key);
		$("#value").val(decodeURI(value));
		$("#upId").val(upId);
		$("#dicType").val(dicType);
		var contents=$('#editDictionary').get(0);
		var artBox=art.dialog({
			lock: true,
			artIcon:'edit',
			opacity:0.4,
			width: 300,
			padding:'0px 0px',
			title:'字典修改',
			header:false,
			content: contents,
			ok: function () {
				var key = $("#key").val();
				var value = $("#value").val();
				var upId = $("#upId").val();
				var dicType = $("#dicType").val();
				var param = {};
				var data = {'Id':id,'Keyword':key,'Value':value,'UpId':upId,'DicType':dicType};
				param.reqParam = Commonjs.getReqParams(data);
				var url = '/basic/updateDictionary.do';
				Commonjs.ajax(url,param,function(dd){
					queryDictionary($("#pagenumber").val());
					if(dd.RespCode==10000){
						Commonjs.alert("修改成功");
					}else{
						Commonjs.alert(dd.RespMessage);
					}
				});
			},
			cancel: function(){
			}
		});
	}
	
	function addDictionary(){
		$("#key").val('');
		$("#value").val('');
		$("#upId").val('');
		$("#dicType").val('');
		var contents=$('#editDictionary').get(0);
		var artBox=art.dialog({
			lock: true,
			artIcon:'edit',
			opacity:0.4,
			width: 300,
			padding:'0px 0px',
			title:'新增字典',
			header:false,
			content: contents,
			ok: function () {
				var key = $("#key").val();
				var value = $("#value").val();
				var upId = $("#upId").val();
				var dicType = $("#dicType").val();
				var data = {'Keyword':key,'Value':value,'UpId':upId,'DicType':dicType}
				var param = {};
				param.reqParam = Commonjs.getReqParams(data);
				var url = '/basic/addDictionary.do';
				Commonjs.ajax(url,param,function(dd){
					queryDictionary(1);
					if(dd.RespCode==10000){
						Commonjs.alert("新增成功");
					}else{
						Commonjs.alert(dd.RespMessage);
					}
				});
			},
			cancel: function(){
			}
		});
	}



	function changeTabs(url){
		
		//var b = window.parent;
		//b.changeTabs(url);
		window.location.href = url;
	}
	
	function subText(s,size,s1){
		
		if(Commonjs.isEmpty(s)){
			return '';
		}else if(s.length > size){
			if(s1){
				return s.substring(0,size)+s1;
			}
			return s.substring(0,size);
		}else 
			return s;
	}
