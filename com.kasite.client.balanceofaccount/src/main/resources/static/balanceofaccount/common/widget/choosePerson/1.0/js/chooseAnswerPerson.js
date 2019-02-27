/**
 * chooseAnswerPerson --选择负责人--
 * @param htmlId
 * @param parmOrgData 组织POST data
 * @param initDataId 初始选中人员id
 * @param initDataName 初始选中人员name
 * @param ztreeJsonUrl 组织json ajax 地址
 * @param searchJsonUrl 查询json ajax 地址
 * @param checkType 0无 1单选 2 多选
 * @param selectNumber checkType为2时 最多选择几个 默认10个
 * @param selectPrompt checkType为2时 提示语 默认提示“最多只能选择10个人！”
 * @param async 是否异步加载ZTREE
 * @param ztreeFun 三个函数 {Check:function(){},AsyncSuccess:function(){},Click:function(){}}
 * @param callBack 回调函数
 */

var chooseAnswerPerson = function(options){
	chooseAnswerPerson.init(options);
};
//----------chooseAnswerPerson 默认参数
chooseAnswerPerson.defaults = {
	htmlId:'',
	parmOrgData:{},
	initDataId:null,
	initDataName:null,
	ztreeJsonUrl:'',
	searchJsonUrl:'',
	checkType:1,
	selectNumber:10,
	selectPrompt:'\u6700\u591a\u53ea\u80fd\u9009\u62e9\u0031\u0030\u4e2a\u4eba\uff01',
	async:false,
	artDialogTitle:"请选择负责人",
	ztreeFun:{
		Check:null,
		AsyncSuccess:null,
		Click:null
	},
	callBack:null,
    searchDataKey:{
        ORGNAME: 'ORGNAME',
        CNAME: 'CNAME',
        USERID: 'USERID'
    }
};
//----------入口 带参数
chooseAnswerPerson.init = function(options){
	chooseAnswerPerson.opts=$.extend(this.defaults,options || {});
	chooseAnswerPerson.initHtmlWrap();
	chooseAnswerPerson.artDialog.init();
	chooseAnswerPerson.search();
	chooseAnswerPerson.ztreeFun = chooseAnswerPerson.opts.ztreeFun;
	chooseAnswerPerson.ztree(chooseAnswerPerson.ztreeSetting());
};
//----------ztree 初始化
chooseAnswerPerson.ztree = function(setting){
	if(chooseAnswerPerson.opts.async){
		$.fn.zTree.init($("#chooseAnswerPersonTree"), setting);
	}else{
		chooseAnswerPerson.ztreeNodes(setting);
	}
};
//----------ztree 一次性读取JSON
chooseAnswerPerson.ztreeNodes=function(setting){
	$.ajax({
		dataType: "json",
		type: "POST",
		data:chooseAnswerPerson.opts.parmOrgData,
		cache:false,
		url: chooseAnswerPerson.opts.ztreeJsonUrl,
		success: function(data){
			$.fn.zTree.init($("#chooseAnswerPersonTree"), setting,data);
			$('#ztreeAnswerLoadingTips').hide();
			chooseAnswerPerson.ztreeCheckNode(chooseAnswerPerson.opts.initDataId);
		}
	});
};
//----------ztree checkbok readio 被选中后的回调
chooseAnswerPerson.ztreeOnCheck = function(event,treeId, treeNode){

};
//----------ztree 异步请求数据时的回调
chooseAnswerPerson.ztreeOnAsyncSuccess = function(event,treeId, treeNode, msg){
	$('#ztreeAnswerLoadingTips').hide();
	chooseAnswerPerson.ztreeCheckNode(chooseAnswerPerson.opts.initDataId);
};
//----------ztree 某节点单击后的回调
chooseAnswerPerson.ztreeOnClick = function(event,treeId, treeNode, msg){

};
//----------ztree 异步请求
chooseAnswerPerson.ztreeFilter = function (treeId, parentNode, responseData) {
	return responseData;
};
//----------ztree 回填时的勾选
chooseAnswerPerson.ztreeCheckNode = function(idData){
	if(idData && idData!=''){
		var treeObj = $.fn.zTree.getZTreeObj("chooseAnswerPersonTree");
		var	nodes_array = treeObj.transformToArray (treeObj.getNodes()[0].children);
		if(idData.indexOf(",")>0){
			idData=idData.split(",");
			for(var i=0;i<nodes_array.length;i++){
				$(idData).each(function(k,v){
					if(v==nodes_array[i].id){
						treeObj.checkNode(nodes_array[i], true, true);
					}
				});
			}
		}else{
			for(var i=0;i<nodes_array.length;i++){
				if(idData==nodes_array[i].id){
					treeObj.checkNode(nodes_array[i], true, true);
				}
			}
		}
	}
};
//----------ztree checkType 类型
chooseAnswerPerson.ztreeSetting = function(){
	var callbackFun= {
		onCheck: function onCheck(event, treeId, treeNode){
			if(chooseAnswerPerson.ztreeFun.Check){
				eval("chooseAnswerPerson.ztreeFun.Check(event,treeId, treeNode)");
			}else{
				eval("chooseAnswerPerson.ztreeOnCheck(event,treeId, treeNode)");
			}
		},
		onAsyncSuccess:function onAsyncSuccess(event, treeId, treeNode, msg){
			if(chooseAnswerPerson.ztreeFun.AsyncSuccess){
				eval("chooseAnswerPerson.ztreeFun.AsyncSuccess(event,treeId, treeNode, msg)");
			}else{
				eval("chooseAnswerPerson.ztreeOnAsyncSuccess(event,treeId, treeNode, msg)");
			}
		},
		onClick:function onClick(event, treeId, treeNode, msg){
			if(chooseAnswerPerson.ztreeFun.Click){
				eval("chooseAnswerPerson.ztreeFun.Click(event,treeId, treeNode, msg)");
			}else{
				eval("chooseAnswerPerson.ztreeOnClick(event,treeId, treeNode, msg)");
			}
		}
	};
	if(chooseAnswerPerson.opts.async){
		if(chooseAnswerPerson.opts.checkType==1){
			var _ztreeSettingVal = {
				async: {
					enable: true,
					url:chooseAnswerPerson.opts.ztreeJsonUrl,
					autoParam: ["id"],
					otherParam:chooseAnswerPerson.opts.parmOrgData,
					dataFilter: chooseAnswerPerson.ztreeFilter,
					type: "post"
				},
				check: {
					enable: true,
					chkStyle: "radio",
					radioType: "all"
				},
				callback:callbackFun
			};
		}else if(chooseAnswerPerson.opts.checkType==2){
			var _ztreeSettingVal = {
				async: {
					enable: true,
					url:chooseAnswerPerson.opts.ztreeJsonUrl,
					autoParam: ["id"],
					otherParam:chooseAnswerPerson.opts.parmOrgData,
					dataFilter: chooseAnswerPerson.ztreeFilter,
					type: "post"
				},
				check: {
					enable: true
				},
				callback: callbackFun
			};
		}else{
			var _ztreeSettingVal = {
				async: {
					enable: true,
					url:chooseAnswerPerson.opts.ztreeJsonUrl,
					autoParam: ["id"],
					otherParam:chooseAnswerPerson.opts.parmOrgData,
					dataFilter: chooseAnswerPerson.ztreeFilter,
					type: "post"
				},
				callback:callbackFun
			};
		}
	}else{
		if(chooseAnswerPerson.opts.checkType==1){
			var _ztreeSettingVal = {
				check: {
					enable: true,
					chkStyle: "radio",
					radioType: "all"
				},
				callback:callbackFun
			};
		}else if(chooseAnswerPerson.opts.checkType==2){
			var _ztreeSettingVal = {
				check: {
					enable: true
				},
				callback: callbackFun
			};
		}else{
			var _ztreeSettingVal = {
				callback:callbackFun
			};
		}
	}

	return _ztreeSettingVal;
};
//----------搜索
chooseAnswerPerson.search = function(){
	//输入框架焦点 focus 、 blur
	$('#popRespChoo').on('focus','.list-search-input',function(){
		$(this).parent().find('.list-search-clear').show();
		if(!$(this).parent().next('.search-overlay').is(':visible')) {
			$(this).parent().next('.search-overlay').show();
		}
	}).on('blur','.list-search-input',function(){
		var $this=$(this);
		if($this.val()=='' && $('#personSearchTbody>tr').length<=0){
			$this.parent().find('.list-search-clear').hide();
			$(this).parent().next('.search-overlay').hide();
		}
	}).on('keyup','.list-search-input',function(e){
		var ev = document.all ? window.event : e;
		if(ev.keyCode==13) {
			searchAjax($('#popRespChoo').find('input.list-search-input'));
		}
	});
	//搜索AJAX
	$('#popRespChoo').on('click','.list-search-icon',function(){
		searchAjax($('#popRespChoo').find('input.list-search-input'));
	});
	//搜索AJAX
	function searchAjax(dom){
		var _val= $.trim(dom.val());
		if(_val=='') return false;
		chooseAnswerPerson.opts.parmOrgData.userName=_val;
		$.ajax({
			dataType: "json",
			type: "POST",
			data:chooseAnswerPerson.opts.parmOrgData,
			cache:false,
			url: chooseAnswerPerson.opts.searchJsonUrl,
			success: function(data){
				var Html='';
				for(var i=0; i<data.Result.length; i++){
					var _USERID = eval('data.Result[i].'+chooseAnswerPerson.opts.searchDataKey.USERID),
                    _CNAME = eval('data.Result[i].'+chooseAnswerPerson.opts.searchDataKey.CNAME),
                    _ORGNAME = eval('data.Result[i].'+chooseAnswerPerson.opts.searchDataKey.ORGNAME);
					if(chooseAnswerPerson.opts.checkType==1){
						Html+='<tr class="c-t-center" ><td><label><input type="radio" class="radio person-search-radio" data-name="'+_CNAME+'" data-rid="'+_USERID+'"></label></td><td class="c-t-center"><span class="c-nowrap">'+_CNAME+'</span></td><td><span class="c-nowrap">'+_ORGNAME+'</span></td></tr>';
					}else{
						Html+='<tr class="c-t-center"><td><label><input type="checkbox" class="checkbox person-search-checkbox" data-name="'+_CNAME+'" data-rid="'+_USERID+'"></label></td><td class="c-t-center"><span class="c-nowrap">'+_CNAME+'</span></td><td><span class="c-nowrap">'+_ORGNAME+'</span></td></tr>';
					}

				}
				$('#personSearchTbody').html(Html);
			}
		});
	}

	//清空搜索内容
	$('#popRespChoo').on('click','.list-search-clear',function(){
		$(this).parent().find('input.list-search-input').val('').blur();
		$(this).parent().next('.search-overlay').hide();
		$('#personSearchTbody').html('');
	});
	//单选radio
	if(chooseAnswerPerson.opts.checkType==1){
		$('#personSearchCheckboxAll').parent().hide();
		//单选
		$('#popRespChoo').on('click','.person-search-radio',function(){
			$(this).parents('tr').siblings().find('.person-search-radio').prop({'checked':false});
		});
	}else{
		//全选
		$('#popRespChoo').on('click','#personSearchCheckboxAll',function(){
			if($(this).is(':checked')){
				$('#personSearchTbody').find('input[type="checkbox"]').attr("checked",'true');
			}else{
				$('#personSearchTbody').find('input[type="checkbox"]').removeAttr("checked");
			}
		});
		//单选
		$('#popRespChoo').on('click','.person-search-checkbox',function(){
			var allCheckbox=true;
			$('.person-search-checkbox').each(function(){
				if(!($(this).is(':checked'))){
					$('#personSearchCheckboxAll').removeAttr('checked');
					allCheckbox=false;
				}
			});
			if(allCheckbox){
				$('#personSearchCheckboxAll').attr("checked",'true');
			}
		});
	}
};
//----------弹出框内容HTML
chooseAnswerPerson.htmlWrap = '' +
'<div id="popRespChoo" style="display:none;">'+
	'<div class="c-main">'+
		'<div class="w420 bgc-fff">'+
			'<div class="c-border list">'+
				'<div class="plr15 ptb10 c-f14 c-666 c-position-r c-border-b">'+
					'<div class="list-search c-position-r searchuser">'+
						'<input type="text" placeholder="搜索部门人名" class="list-search-input">'+
						'<i class="iconfont list-search-clear c-hide" title="清空">&#xe635;</i>'+
						'<i class="iconfont list-search-icon" title="搜索">&#xe62c;</i>'+
					'</div>'+
					'<div class="search-overlay">'+
						'<div class="">'+
							'<table class="tb width-100">'+
								'<thead>'+
									'<tr>'+
										'<th><label><input type="checkbox" class="checkbox" id="personSearchCheckboxAll"></label></th>'+
										'<th>姓名</th>'+
										'<th>组织名称</th>'+
									'</tr>'+
								'</thead>'+
								'<tbody id="personSearchTbody"></tbody>'+
							'</table>'+
						'</div>'+
					'</div>'+
				'</div>'+
				'<div class="ptb10" style="height:280px; overflow:auto;">'+
					'<div class="pt100 c-t-center c-909090 c-f14" id="ztreeAnswerLoadingTips">数据正在加载中。。。</div>'+
					'<ul id="chooseAnswerPersonTree" class="ztree ztree-slack ztree-solid-arrow"></ul>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>'+
'</div>';
//----------生成选中人员 数组
chooseAnswerPerson.makeArray=function (Rid,Name,personArray){
	var repeated=true;
	if(personArray.length>0){
		$(personArray).each(function(i,v){
			if(Rid == v.rid){repeated=false;}
		});
	}
	if(repeated){
		var	valArray=new Array();
		valArray['rid']=Rid;
		valArray['name']=Name;
		personArray.push(valArray);
	}
	return personArray;
};
//----------取选中的人员ID、NAME
chooseAnswerPerson.choosedPersonVal=function(){
	var personArray=new Array();

	if(chooseAnswerPerson.opts.checkType==1){
		$('#personSearchTbody').find('input[type="radio"]').each(function(){
			if($(this).is(':checked')){
				personArray=chooseAnswerPerson.makeArray($(this).data('rid'),$(this).data('name'),personArray);
			}
		});
		if(personArray.length==0){
			ztreePersonArray();
		}
	}else{
		$('#personSearchTbody').find('input[type="checkbox"]').each(function(){
			if($(this).is(':checked')){
				personArray=chooseAnswerPerson.makeArray($(this).data('rid'),$(this).data('name'),personArray);
			}
		});
		ztreePersonArray();
	}

	function ztreePersonArray(){
		var treeObj = $.fn.zTree.getZTreeObj("chooseAnswerPersonTree");

		if(chooseAnswerPerson.opts.checkType==1 || chooseAnswerPerson.opts.checkType==2){
			var checked = treeObj.getCheckedNodes(true);
			if(checked.length>chooseAnswerPerson.opts.selectNumber && chooseAnswerPerson.opts.checkType==2){
				art.dialog({
					lock: true,
					artIcon:'error',
					opacity:0.4,
					width: 250,
					title:'报错',
					content: chooseAnswerPerson.opts.selectPrompt,
					ok: function () {}
				});
				return false;
			}
			for(var i=0;i<checked.length;i++){
				if(!(checked[i].isParent)){
					personArray=chooseAnswerPerson.makeArray(checked[i].id,checked[i].name,personArray);
				}
			}
		}else{
			var nodes = treeObj.getSelectedNodes();
			if(nodes.length>0 && !(checked[i].isParent)){
				personArray=chooseAnswerPerson.makeArray(nodes[0].id,nodes[0].name,personArray);
			}
		}
	}

	return personArray;
};
//----------构造弹出框
chooseAnswerPerson.artDialog = {
	init : function (){
		this.obj = art.dialog({
			artIcon:'add',
			lock: true,
			opacity:0.4,
			width: 420,
			padding: '15px 25px',
			title:chooseAnswerPerson.opts.artDialogTitle,
			content: $('#popRespChoo').get(0),
			ok: function(){
				if(chooseAnswerPerson.opts.callBack){
					chooseAnswerPerson.opts.callBack(chooseAnswerPerson.choosedPersonVal());
				}
				setTimeout(function(){
					$('#popRespChoo').find('input.list-search-input').val('').blur();
					$('#popRespChoo').find('.search-overlay').hide();
					$('#personSearchTbody').html('');
				},300);
			},
			cancel:true
		});
	},
	obj : null
};
//----------构造弹出框内容
chooseAnswerPerson.initHtmlWrap = function () {
	if($('#popRespChoo').length<=0){
		$('body').append(this.htmlWrap);
	}
};
//----------删除弹出框内容
chooseAnswerPerson.removeHtmlWrap = function () {
	if($('#popRespChoo').length){
		$('#popRespChoo').remove();
	}
};