/**
 * chooseAdministrator --选择管理人--
 * @param htmlId
 * @param parmOrgData 组织POST data
 * @param artDialogTitle 弹出窗标题
 * @param initDataId 初始选中人员id
 * @param initDataName 初始选中人员name
 * @param ztreeJsonUrl 组织json ajax 地址
 * @param searchJsonUrl 查询json ajax 地址
 * @param checkType 0无 1单选 2 多选
 * @param selectNumber checkType为2时 最多选择几个 默认10个
 * @param selectPrompt checkType为2时 提示语 默认提示“最多只能选择”
 * @param async 是否异步加载ZTREE
 * @param ztreeFun 三个函数 {Check:function(){},AsyncSuccess:function(){},Click:function(){}}
 * @param callBack 回调函数
 */
var chooseAdministrator = function(options){
	chooseAdministrator.init(options);
};
//----------chooseAdministrator 默认参数
chooseAdministrator.defaults = {
	htmlId:'',
	parmOrgData:{},
	artDialogTitle:'\u8bf7\u9009\u62e9\u7ba1\u7406\u4eba',
	initDataId:null,
	initDataName:null,
	ztreeJsonUrl:'',
	searchJsonUrl:'',
	checkType:2,
	selectNumber:10,
	selectPrompt:'\u6700\u591a\u53ea\u80fd\u9009\u62e9\uff1a',
	async:false,
	ztreeFun:{
		Check:null,
		AsyncSuccess:null,
		Click:null
	},
	callBack:null
};
//----------入口 带参数
chooseAdministrator.init = function(options){
	chooseAdministrator.opts=$.extend(this.defaults,options || {});
	if(chooseAdministrator.opts.async){
		chooseAdministrator.asyncInit=true;
	}
	chooseAdministrator.initHtmlWrap();
	chooseAdministrator.artDialog.init();
	chooseAdministrator.search();
	chooseAdministrator.ztreeFun = chooseAdministrator.opts.ztreeFun;
	chooseAdministrator.ztree(chooseAdministrator.ztreeSetting());
};
//----------ztree 初始化
chooseAdministrator.ztree = function(setting){
	if(chooseAdministrator.opts.async){
		$.fn.zTree.init($("#chooseAdministratorTree"), setting);
	}else{
		chooseAdministrator.ztreeNodes(setting);
	}
};
//----------ztree 一次性读取JSON
chooseAdministrator.ztreeNodes=function(setting){
	$.ajax({
		dataType: "json",
		type: "POST",
		data:chooseAdministrator.opts.parmOrgData,
		cache:false,
		url: chooseAdministrator.opts.ztreeJsonUrl,
		success: function(data){
			$.fn.zTree.init($("#chooseAdministratorTree"), setting,data);
			$('#ztreeAdminLoadingTips').hide();
			chooseAdministrator.ztreeCheckNode(chooseAdministrator.opts.initDataId,false,chooseAdministrator.opts.initDataName);
		}
	});
};
//----------ztree checkbok readio 被选中后的回调
chooseAdministrator.ztreeOnCheck = function(event,treeId, treeNode){

};
//----------ztree 异步请求数据时的回调
chooseAdministrator.ztreeOnAsyncSuccess = function(event,treeId, treeNode, msg){
	$('#ztreeAdminLoadingTips').hide();

	if(chooseAdministrator.asyncInit){
		chooseAdministrator.asyncInit=false;
		chooseAdministrator.ztreeCheckNode(chooseAdministrator.opts.initDataId,false,chooseAdministrator.opts.initDataName);
	}else{
		chooseAdministrator.ztreeCheckNode('',false,'');
	}

};
//----------ztree 某节点单击后的回调
chooseAdministrator.ztreeOnClick = function(event,treeId, treeNode, msg){

};
//----------ztree 异步请求
chooseAdministrator.ztreeFilter = function (treeId, parentNode, responseData) {
	return responseData;
};
//----------选中的人员超出提示
chooseAdministrator.orerunDialog = function(){
	art.dialog({
		lock: true,
		artIcon:'error',
		opacity:0.4,
		width: 250,
		title:'报错',
		content: chooseAdministrator.opts.selectPrompt + chooseAdministrator.opts.selectNumber,
		ok: function () {}
	});
};
//----------勾选的值动态生成
chooseAdministrator.ztreeCheckNode = function(idData,Checked,nameData){
	if(idData && idData!=''){
		var treeObj = $.fn.zTree.getZTreeObj("chooseAdministratorTree");
		var	nodes_array = treeObj.transformToArray (treeObj.getNodes()[0].children);
		var listHtml='',listArray=new Array(),sdata={};
		if(idData.indexOf(",")>0){
			idData=idData.split(",");
			nameData=nameData.split(",");
			if($('#adminSelecteBox>li').length+$(idData).length>chooseAdministrator.opts.selectNumber){
				chooseAdministrator.orerunDialog();
				return false;
			}
			$(idData).each(function(k,v){
				listArray=chooseAdministrator.makeArray(v,nameData[k],listArray);
			});
			for(var i=0;i<nodes_array.length;i++){
				$(idData).each(function(k,v){
					if(v==nodes_array[i].id){
						funChecked(i);
					}
				});
			}
		}else{
			if(idData!=''){
				if($('#adminSelecteBox>li').length+1>chooseAdministrator.opts.selectNumber){
					chooseAdministrator.orerunDialog();
					return false;
				}
				listArray=chooseAdministrator.makeArray(idData,nameData,listArray);
				for(var i=0;i<nodes_array.length;i++){
					if(idData==nodes_array[i].id){
						funChecked(i);
					}
				}
			}

		}

		for(var n=0;n<listArray.length;n++) {
			if($('#adminSelecteBox>li').length>0){
				var oldList=true;
				for(var s=0;s<$('#adminSelecteBox>li').length;s++){
					if(listArray[n].rid==$('#adminSelecteBox>li').eq(s).data('rid')){
						oldList=false;
					}
				}
				if(oldList){
					listHtml+='<li data-rid="'+listArray[n].rid+'" data-name="'+listArray[n].name+'"><a href="javascript:;" class="alinks-line alinks-black" onclick="chooseAdministrator.delAdminer(this)">'+listArray[n].name+'</a></li>';
				}
			}else{
				listHtml+='<li data-rid="'+listArray[n].rid+'" data-name="'+listArray[n].name+'"><a href="javascript:;" class="alinks-line alinks-black" onclick="chooseAdministrator.delAdminer(this)">'+listArray[n].name+'</a></li>';
			}
		}
		$('#adminSelecteBox').append(listHtml);
	}
	function funChecked(ii){
		if(Checked){
			treeObj.checkNode(nodes_array[ii], true, true);
		}else{
			treeObj.checkNode(nodes_array[ii], false, true);
		}
	}
};
//----------ztree checkType 类型
chooseAdministrator.ztreeSetting = function(){
	var callbackFun= {
		onCheck: function onCheck(event, treeId, treeNode){
			if(chooseAdministrator.ztreeFun.Check){
				eval("chooseAdministrator.ztreeFun.Check(event,treeId, treeNode)");
			}else{
				eval("chooseAdministrator.ztreeOnCheck(event,treeId, treeNode)");
			}
		},
		onAsyncSuccess:function onAsyncSuccess(event, treeId, treeNode, msg){
			if(chooseAdministrator.ztreeFun.AsyncSuccess){
				eval("chooseAdministrator.ztreeFun.AsyncSuccess(event,treeId, treeNode, msg)");
			}else{
				eval("chooseAdministrator.ztreeOnAsyncSuccess(event,treeId, treeNode, msg)");
			}
		},
		onClick:function onClick(event, treeId, treeNode, msg){
			if(chooseAdministrator.ztreeFun.Click){
				eval("chooseAdministrator.ztreeFun.Click(event,treeId, treeNode, msg)");
			}else{
				eval("chooseAdministrator.ztreeOnClick(event,treeId, treeNode, msg)");
			}
		}
	};
	if(chooseAdministrator.opts.async){
		if(chooseAdministrator.opts.checkType==1){
			var _ztreeSettingVal = {
				async: {
					enable: true,
					url:chooseAdministrator.opts.ztreeJsonUrl,
					autoParam: ["id"],
					otherParam:chooseAdministrator.opts.parmOrgData,
					dataFilter: chooseAdministrator.ztreeFilter,
					type: "post"
				},
				check: {
					enable: true,
					chkStyle: "radio",
					radioType: "all"
				},
				callback:callbackFun
			};
		}else if(chooseAdministrator.opts.checkType==2){
			var _ztreeSettingVal = {
				async: {
					enable: true,
					url:chooseAdministrator.opts.ztreeJsonUrl,
					autoParam: ["id"],
					otherParam:chooseAdministrator.opts.parmOrgData,
					dataFilter: chooseAdministrator.ztreeFilter,
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
					url:chooseAdministrator.opts.ztreeJsonUrl,
					autoParam: ["id"],
					otherParam:chooseAdministrator.opts.parmOrgData,
					dataFilter: chooseAdministrator.ztreeFilter,
					type: "post"
				},
				callback:callbackFun
			};
		}
	}else{
		if(chooseAdministrator.opts.checkType==1){
			var _ztreeSettingVal = {
				check: {
					enable: true,
					chkStyle: "radio",
					radioType: "all"
				},
				callback:callbackFun
			};
		}else if(chooseAdministrator.opts.checkType==2){
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
chooseAdministrator.search = function(){
	//输入框架焦点 focus 、 blur
	$('#popMangChoo').on('focus','.list-search-input',function(){
		$(this).parent().find('.list-search-clear').show();
		if(!$(this).parent().next('.search-overlay').is(':visible')) {
			$(this).parent().next('.search-overlay').show();
		}
	}).on('blur','.list-search-input',function(){
		var $this=$(this);
		if($this.val()=='' && $('#adminSearchTbody>tr').length<=0){
			$this.parent().find('.list-search-clear').hide();
			$(this).parent().next('.search-overlay').hide();
		}
	}).on('keyup','.list-search-input',function(e){
		var ev = document.all ? window.event : e;
		if(ev.keyCode==13) {
			searchAjax($('#popMangChoo').find('input.list-search-input'));
		}
	});
	//搜索button
	$('#popMangChoo').on('click','.list-search-icon',function(){
		searchAjax($('#popMangChoo').find('input.list-search-input'));
	});
	//搜索AJAX
	function searchAjax(dom){
		var _val= $.trim(dom.val());
		if(_val=='') return false;
		chooseAdministrator.opts.parmOrgData.userName=_val;
		$.ajax({
			dataType: "json",
			type: "POST",
			data:chooseAdministrator.opts.parmOrgData,
			cache:false,
			url: chooseAdministrator.opts.searchJsonUrl,
			success: function(data){
				var Html='';
				for(var i=0; i<data.Result.length; i++){
					if(chooseAdministrator.opts.checkType==1){
						Html+='<tr><td><label><input type="radio" class="radio person-search-radio" data-name="'+data.Result[i].CNAME+'" data-rid="'+data.Result[i].USERID+'"></label></td><td class="c-t-center"><span class="c-nowrap">'+data.Result[i].CNAME+'</span></td><td><span class="c-nowrap">'+data.Result[i].JOBNUMBER+'</span></td><td><span class="c-nowrap">'+data.Result[i].ORGNAME+'</span></td></tr>';
					}else{
						Html+='<tr><td><label><input type="checkbox" class="checkbox person-search-checkbox" data-name="'+data.Result[i].CNAME+'" data-rid="'+data.Result[i].USERID+'"></label></td><td class="c-t-center"><span class="c-nowrap">'+data.Result[i].CNAME+'</span></td><td><span class="c-nowrap">'+data.Result[i].JOBNUMBER+'</span></td><td><span class="c-nowrap">'+data.Result[i].ORGNAME+'</span></td></tr>';
					}
				}
				$('#adminSearchTbody').html(Html);
			}
		});
	}
	//清空搜索内容
	$('#popMangChoo').on('click','.list-search-clear',function(){
		$(this).parent().find('input.list-search-input').val('').blur();
		$(this).parent().next('.search-overlay').hide();
		$('#adminSearchTbody').html('');
	});
	//全选
	$('#popMangChoo').on('click','#adminSearchCheckboxAll',function(){
		if($(this).is(':checked')){
			$('#adminSearchTbody').find('input[type="checkbox"]').attr("checked",'true');
		}else{
			$('#adminSearchTbody').find('input[type="checkbox"]').removeAttr("checked");
		}
	});
	//单选
	$('#popMangChoo').on('click','.person-search-checkbox',function(){
		var allCheckbox=true;
		$('.person-search-checkbox').each(function(){
			if(!($(this).is(':checked'))){
				$('#adminSearchCheckboxAll').removeAttr('checked');
				allCheckbox=false;
			}
		});
		if(allCheckbox){
			$('#adminSearchCheckboxAll').attr("checked",'true');
		}
	});
};
//----------弹出框内容HTML
chooseAdministrator.htmlWrap = '' +
'<div id="popMangChoo" style="display:none;">'+
	'<div class="c-main auth-org-w">'+
		'<div class="clearfix">'+
			'<div class="fl w420 bgc-fff">'+
				'<div class="c-border list">'+
					'<div class="plr15 ptb10 c-f14 c-666 c-border-b c-position-r">'+
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
											'<th><label><input type="checkbox" class="checkbox" id="adminSearchCheckboxAll"></label></th>'+
											'<th>姓名</th>'+
											'<th>工号</th>'+
											'<th>组织名称</th>'+
										'</tr>'+
									'</thead>'+
									'<tbody id="adminSearchTbody"></tbody>'+
								'</table>'+
							'</div>'+
						'</div>'+
					'</div>'+
					'<div class="ptb10" style="height:280px; overflow:auto;">'+
						'<div class="pt100 c-t-center c-909090 c-f14" id="ztreeAdminLoadingTips">数据正在加载中。。。</div>'+
						'<ul id="chooseAdministratorTree" class="ztree ztree-slack ztree-slack-arrow"></ul>'+
					'</div>'+
				'</div>'+
			'</div>'+
			'<div class="fl add-del-w">'+
				'<a class="c-btn c-btn-blue" href="javascript:;" onclick="chooseAdministrator.rightShift()">选择右移<em class="c-simsun ml5">&gt;</em></a>'+
			'</div>'+
			'<div class="fl w420 bgc-fff">'+
				'<div class="c-border list">'+
					'<div class="plr15 ptb10 c-f14 c-666 c-border-b">选择人员信息</div>'+
					'<div class="ptb10" style="height:280px; overflow:auto;">'+
						'<ul class="selectedorg c-333" id="adminSelecteBox"></ul>'+
					'</div>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>'+
'</div>';
//----------右移
chooseAdministrator.rightShift=function(){
	var _value=chooseAdministrator.choosedPersonVal(),
		_id='',_name='';
	$(_value).each(function(i,v){
		_id += v.rid + ',';
		_name += v.name + ',';
	});
	_id=_id.substring(0,_id.length-1);
	_name=_name.substring(0,_name.length-1);
	chooseAdministrator.ztreeCheckNode(_id,false,_name);
};
//----------删除选择人员
chooseAdministrator.delAdminer=function(dom) {
	var treeObj = $.fn.zTree.getZTreeObj("chooseAdministratorTree");
	var	nodes_array = treeObj.transformToArray (treeObj.getNodes()[0].children);
	idData=$(dom).parent().data("rid");
	for(var i=0;i<nodes_array.length;i++){
		if(idData==nodes_array[i].id){
			treeObj.checkNode(nodes_array[i], false, true);
		}
	}
	$(dom).parent().remove();
};
//----------生成选中人员 数组
chooseAdministrator.makeArray=function (Rid,Name,personArray){
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
chooseAdministrator.choosedPersonVal=function(){
	var personArray=new Array();

	$('#adminSearchTbody').find('input[type="checkbox"]').each(function(){
		if($(this).is(':checked')){
			personArray=chooseAdministrator.makeArray($(this).data('rid'),$(this).data('name'),personArray);
		}
	});

	var treeObj = $.fn.zTree.getZTreeObj("chooseAdministratorTree");

	if(chooseAdministrator.opts.checkType==1 || chooseAdministrator.opts.checkType==2){
		var checked = treeObj.getCheckedNodes(true);
		if(checked.length>chooseAdministrator.opts.selectNumber && chooseAdministrator.opts.checkType==2){
			chooseAdministrator.orerunDialog();
			return false;
		}
		for(var i=0;i<checked.length;i++){
			if(!(checked[i].isParent)){
				personArray=chooseAdministrator.makeArray(checked[i].id,checked[i].name,personArray);
			}
		}
	}else{
		var nodes = treeObj.getSelectedNodes();
		if(nodes.length>0 && !(checked[i].isParent)){
			personArray=chooseAdministrator.makeArray(nodes[0].id,nodes[0].name,personArray);
		}
	}
	return personArray;
};
//----------返回选中的管理人员
chooseAdministrator.selectAdminer=function(){
	var personArray=new Array(),$li=$('#adminSelecteBox>li');
	for(var i=0; i<$li.length;i++){
		personArray=chooseAdministrator.makeArray($li.eq(i).data('rid'),$li.eq(i).data('name'),personArray);
	}
	return personArray;
};
//----------构造弹出框
chooseAdministrator.artDialog = {
	init : function (){
		this.obj = art.dialog({
			artIcon:'add',
			lock: true,
			opacity:0.4,
			width: 850,
			padding: '15px 25px',
			title:chooseAdministrator.opts.artDialogTitle,
			content: $('#popMangChoo').get(0),
			ok: function(){
				if(chooseAdministrator.opts.callBack){
					if($('#adminSelecteBox>li').length>0){
						var _value=chooseAdministrator.selectAdminer();
					}
					chooseAdministrator.opts.callBack(_value);
				}

				chooseAdministrator.dialogClose();
			},
			cancel:function(){
				chooseAdministrator.dialogClose();
			}
		});
	},
	obj : null
};
//----------弹出框关闭回调
chooseAdministrator.dialogClose = function(){
	setTimeout(function(){
		$('#adminSelecteBox').html('');
		$('#popMangChoo').find('input.list-search-input').val('').blur();
		$('#popMangChoo').find('.search-overlay').hide();
		$('#adminSearchTbody').html('');
	},300);
};
//----------构造弹出框内容
chooseAdministrator.initHtmlWrap = function () {
	if($('#popMangChoo').length<=0){
		$('body').append(this.htmlWrap);
	}
};
//----------删除弹出框内容
chooseAdministrator.removeHtmlWrap = function () {
	if($('#popMangChoo').length){
		$('#popMangChoo').remove();
	}
};