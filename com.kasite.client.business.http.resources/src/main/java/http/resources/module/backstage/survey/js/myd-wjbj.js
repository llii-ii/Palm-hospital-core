	var session = null;
	var SubjectId = 0;
	var SubjectTitle = '';
	var BeginIntro = '';
	var EndingIntro = '';
	var maxItem = 2;
	var QuestionOption = null;
  	var dataArr = new Array();
	
	$(function(){
		SubjectId = QueryString('SubjectId');
		if(Commonjs.isEmpty(SubjectId)){
//			ComWbj.alertIconNo('提示：','未知问卷ID','warning');
			ComWbj.artTips("提示","warning","未知问卷ID",1.5,null);
			history.go(-1);
		}
		
		
		init();
		
		$("i[name='ep']").each(function(i){
			$(this).click(function(event) {
                event.stopPropagation();
            });
		});
		
	});
	
	function showModel(){//预览
		window.location.href = 'myd-wjyl.html?SubjectId='+SubjectId;
	}
	
	function nextEdit(){//收集
	
		if(QuestionOption.length < 1){
//			ComWbj.alertIconNo('提示：','请先设计问卷题目','warning');
			ComWbj.artTips("提示","warning","请先设计问卷题目",1.5,null);
		}else
			window.location.href = 'myd-wjsj.html?SubjectId='+SubjectId;
	}
	
	function QueryString(val) {
		var uri = window.location.search;
		var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
		return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
	}
	
	//初始化
	function init(){
		$('#setting3').prev('.mask').hide().end().fadeOut();
		$('#setting2').prev('.mask').hide().end().fadeOut();
		$('#setting1').prev('.mask').hide().end().fadeOut();
		getWJData();
	}
	
  	function add_dxt(){
  	
  		$('#areaDiv').html(dxt_ul);
  		$('#dxt_ul').html(dxt_li);
  	}
	  	
  	//获取问卷数据
  	function getWJData(){
  		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = SubjectId;
		param.reqParam = Commonjs.getApiReqParams(Service);
  		Commonjs.ajaxSync('/survey/querySubjectById.do',param,function(_d){
  			if(_d.RespCode != 10000){
  				ComWbj.artTips("提示","warning","操作异常"+_d.RespMessage,1.5,null);
  				return;
  			}
  			$.each(_d.Data,function(k,v){
  				SubjectTitle = v.SubjectTitle;
  	  			BeginIntro =  v.BeginIntro;
  	  			EndingIntro = v.EndingIntro;
  	  			$('#EndingIntro').text(EndingIntro);
  	  			$('#BeginIntro').text(BeginIntro);
  	  			$('#SubjectTitle').text(SubjectTitle);
  	  			initQuestionCount(v);
  	  			initHtml(v);
  			});
  		});
  	}
   
   	function updateEndingIntro(o){
   		if(isLengthTooLong($(o).text(),250,1)){
   			ComWbj.artTips("提示","warning","结束语字数在1~250之间",1.5,null);
// 			ComWbj.alertIconNo('提示：','结束语字数在1~250之间','warning');
 		}else{
	   		o.focus();
	  		var param = {};
	   		var Service = {};
	   		Service.HosId = Commonjs.getUrlParam("HosId");
	   		Service.SubjectId = SubjectId;
	   		Service.EndingIntro = $(o).text();
	  		param.reqParam = Commonjs.getApiReqParams(Service);
	  		Commonjs.ajaxSync('/survey/updateSubjectbegin.do',param,function(_d){
	  			$(o).parent().removeClass('editable');
		  		$(o).removeAttr('contenteditable');
	  		});
  		}
   	}
   	
 	function updateSubjectTitle(o){
 		if(isLengthTooLong($(o).text(),25,1)){
 			ComWbj.artTips("提示","warning","标题字数在1~25之间",1.5,null);
// 			ComWbj.alertIconNo('提示：','标题字数在1~25之间','warning');
 		}else{
 			o.focus();
	  		var param = {};
	   		var Service = {};
	   		Service.HosId = Commonjs.getUrlParam("HosId");
	   		Service.SubjectId = SubjectId;
	   		Service.SubjectTitle = $(o).text();
	  		param.reqParam = Commonjs.getApiReqParams(Service);
	  		Commonjs.ajaxSync('/survey/updateSubjectbegin.do',param,function(_d){
	  			$(o).parent().removeClass('editable');
		  		$(o).removeAttr('contenteditable');
	  		});
 		}
   	}
   
  	function updateBeginIntro(o){
  		if(isLengthTooLong($(o).text(),250,1)){
  			ComWbj.artTips("提示","warning","开头语字数在1~250之间",1.5,null);
 		}else{
	  		o.focus();
	   		var param = {};
	   		var Service = {};
	   		Service.HosId = Commonjs.getUrlParam("HosId");
	   		Service.SubjectId = SubjectId;
	   		Service.BeginIntro = $(o).text();
	  		param.reqParam = Commonjs.getApiReqParams(Service);
	  		Commonjs.ajaxSync('/survey/updateSubjectbegin.do',param,function(){
	  			$(o).parent().removeClass('editable');
		  		$(o).removeAttr('contenteditable');
	  		});
  		}
   	}
   
	function add_tx(type){//添加题型
//  	 	var Api = 'survey.SurveyApiImpl.addDetailQuestion';
		var param = {};
  		var Service = {};
  		Service.HosId = Commonjs.getUrlParam("HosId");
  		Service.ContentType = 13;//通用
  		Service.ObjType = 5;//通用
  		Service.QuestType = type;
  		Service.SubjectId = SubjectId;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/addDetailQuestion.do';
		Commonjs.ajaxSync(url,param,null);
  	 	getWJData();
  	 	//location.href="#maodian";
  	 	$("html,body").animate({scrollTop: $("#maodian").offset().top}, 1000); 
  	 	
  	}
  	
  	function initQuestionCount(d){
  		QuestionOption = new Array();
  		dataArr = d.Result;
  		var dataArrJson = Commonjs.jsonToString(d.Result);
  		var _d = dataArr;
  		var size = _d.length;
  		if(size > 0){
  			$('#areaDiv').css('height','auto');
	  		for(var i = 0; i < size; i++){
	  			var p = {};
	  			p.id = _d[i].QuestId;
	  			p.text = _d[i].Question;
	  			QuestionOption.push(p);
	  		}
	  	}
  	}
  	
  	function initHtml(d){//初始化页面选题
  		$('#areaDiv').empty();
  		dataArr = d.Result;
  		var _d = dataArr;
  		var size = _d.length;
  		if(size > 0){
  			$('#areaDiv').css('height','auto');
	  		for(var i = 0; i < size; i++){
	  			if(_d[i].QuestType == 1)
	  			{
	  				$('#areaDiv').append(xzt(_d[i],i,1));
	  			}
	  			if(_d[i].QuestType == 2)
	  			{
	  				$('#areaDiv').append(xzt(_d[i],i,2));
	  			}
	  			if(_d[i].QuestType == 3)
	  			{
	  				$('#areaDiv').append(tkt(_d[i],i));
	  			}
	  			if(_d[i].QuestType == 4)
	  			{
	  				$('#areaDiv').append(jzdx(_d[i],i,4));
	  			}
	  			if(_d[i].QuestType == 5)
	  			{
	  				$('#areaDiv').append(jzdx(_d[i],i,5));
	  			}
	  		}
  		}else{
  			$('#areaDiv').css('height',300);
  		}
  	}
  	
  	function jzdx(d,index,type){//矩阵单选
  		var html = '<div QuestId="'+d.QuestId+'" id="div'+d.QuestId+'" class="qa-item-wrap juzhen"><div class="q-tit">';
  			html += '<span class="q-num">Q'+(index+1)+'</span>'
  			html += '<div class="q-tit-txt" onblur="updateQuestion(this,'+d.QuestId+')">'+d.Question+'</div>';
  			if(d.Mustquest == 1){
  				html += '<span class="f12" style="color:red;">（选填）</span>';
  			}
  			// --show---------------------------
  			html += '</div><div class="a-wrap" id="jzshow'+d.QuestId+'"><div class="mt15 mb25" style="margin-left:31px;">'
  			html += jz_table(d,type);
  			html += '</div></div>'
            // --edit---------------------------
       		html += '<div class="a-wrap" id="jzedit'+d.QuestId+'" style="display:none">';  
          	html += initJZQuestionHtml(d,type);
            html += initJZQuestionItemHtml(d,type);
       		html += '</div><div class="qa-edit-tools">';
  			html += '<i id="i'+d.QuestId+'" class="icon icon-edit mr25" onclick="jzEditPanel('+d.QuestId+')"></i>';
	  		if(type == 4){
	  			html += '<i id="iedit'+d.QuestId+'" class="icon icon-setting mr25" onclick="openEditQuestionPanel2('+d.QuestId+',\''+d.Mustquest+'\')"></i>';
	  		}else 
	  			html += '<i id="iedit'+d.QuestId+'" class="icon icon-setting mr25" onclick="openEditQuestionPanel3('+d.QuestId+',\''+d.Mustquest+'\',\''+d.Maxoption+'\',\''+d.Minoption+'\')"></i>';
	  		html += '<i id="iup'+d.QuestId+'" class="icon icon-up mr25" onclick="setJZQuestionUp(this)"></i>';
	  		html += '<i id="idown'+d.QuestId+'" class="icon icon-down mr25" onclick="setJZQuestionDown(this)"></i>';
	  		html += '<i id="idel'+d.QuestId+'" class="icon icon-trash" onclick="matdeleteQuestion('+d.QuestId+')"></i>';
            html += '</div></div>';  
		return html ;
  	}
  	
  	function matdeleteQuestion(id){//矩阵删除方法
  		art.dialog({
	 		id: 'testID',
	 	    width: '255px',
	 	    height: '109px',
	 	    content: '确认要删除？',
	 	    lock: true,
	 	    button: [{
	 	      	name: '确定',
	 	       	callback: function () {
				    var param = {};
			  		var Service = {};
			  		Service.HosId = Commonjs.getUrlParam("HosId");
					Service.QuestId = id;
					param.reqParam = Commonjs.getApiReqParams(Service);
				 	var url = ' /survey/matdeleteQuestion.do';
				 	Commonjs.ajaxSync(url,param,function(_d){
				 		if(_d.RespCode == 10000){
				  	  	    ComWbj.artTips("提示","warning",_d.RespMessage,1.5,null);
				  	  		getWJData();
				  	  		return true;
				  	  	}else{
				  	     	ComWbj.artTips("提示","warning","操作异常"+_d.RespMessage,1.5,null);
				  	  		return false;
				  	  	}
				 	});
	 	       	}
	 	 	},{
	 	 		name: '取消'
	 	 	}]
	 	});
  	}
  	
 	function initJZQuestionHtml(d,type){
  		var html = '<dl class="juzhen-wrap"><dt>矩阵型</dt><div id="jzdiv'+d.QuestId+'">';  
	 	var _d = d.ChildrenMatrixQuestion;
	 	if(_d.length > 0){
	 		for(var i = 0;i<_d.length;i++){
	 			html += '<dd QuestId="'+_d[i].QuestId+'" class="juzhen-opt" id="ddq'+_d[i].QuestId+'">';
	 			html += '<input onblur="updateJZQuestion(this,'+_d[i].QuestId+')" type="text" class="input-text input-text-w500 mr20" value="'+_d[i].Question+'" />'; 
			    html += '<i id="'+_d[i].QuestId+'" class="icon icon-top mr15" onclick="setJZChildrenQuestionUp(this)"></i>';
			    html += '<i id="'+_d[i].QuestId+'" class="icon icon-bottom mr15" onclick="setJZChildrenQuestionDwon(this)"></i>';
			    html += '<i class="icon icon-del" onclick="delJZQuestionByID('+_d[i].QuestId+')"></i></dd>';
	 		}
	 	} 
	 	html += '</div><dd><a href="javascript:;" class="btn btn-w-auto" onclick="addJZQuestion('+d.QuestId+','+type+')">';
	    html += '<i class="icon icon-add-w"></i>添加</a></dd></dl>';
  		return html;
  	}
 	
  	//添加矩阵问题
	function addJZQuestion(id,type){
		var name = '矩阵';
		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = SubjectId;
		Service.Question = name;
		Service.ObjType = 5;
		Service.MatrixQuestId = id;
		Service.ContentType = 13;
		Service.QuestType = type;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/addQuestion.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
			}else{
				var dataArr = _d.Data;
				$('#jzdiv'+id).append(addJZQuestionHtml(dataArr[0].QuestId,name));
				//加选项
				$('input[name="jzitem'+id+'"]').each(function(i){
					addJZQuestionItem(dataArr[0].QuestId,$(this).val());
				});
				getWJData();
				$('#i'+id).click();
			}
		});
	}
  	
	//添加选项
	function addJZQuestionItem(id,name){
		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.QuestId = id;
		Service.ItemCont = name;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/addItem.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
			}
		});
	}
  	
  	function addJZQuestionHtml(id,name){
  		var html = '';
  		html += '<dd QuestId="'+id+'" class="juzhen-opt" id="ddq'+id+'">';
		html += '<input onblur="updateJZQuestion(this,'+id+')" type="text" class="input-text input-text-w500 mr20" value="'+name+'" />'; 
	    html += '<i id="'+id+'" class="icon icon-top mr15" onclick="setJZChildrenQuestionUp(this)"></i>';
	    html += '<i id="'+id+'" class="icon icon-bottom mr15" onclick="setJZChildrenQuestionDwon(this)"></i>';
	    html += '<i class="icon icon-del" onclick="delJZQuestionByID('+id+')"></i></dd>';
	    return html;
  	}
  	
  	
  	//删除问题
	function delJZQuestionByID(id){
	
		if($('#ddq'+id).parent().children().length <= 1){
			ComWbj.artTips("提示","warning","矩阵选项不能全部删除",1.5,null);
			return ;
		}else{
		  	var param = {};
		    var Service = {};
		    Service.HosId = Commonjs.getUrlParam("HosId");
			Service.QuestId = id;
			param.reqParam = Commonjs.getApiReqParams(Service);
		 	var url = '/survey/deleteQuestion.do';
	  	  	Commonjs.ajaxSync(url,params,function(_d){
	  	  	if(_d.RespCode != 10000){
		  	  	ComWbj.artTips("提示","warning","删除异常",1.5,null);
//		  	  		ComWbj.alertIconNo('提示：','添加异常','warning');
		  	  		return false;
		  	  	}else{
		  	  		$("#ddq"+id).remove();
				}
	  	  	});
		}
	}
  	
 	//修改选题
	function updateJZQuestion(o,id){
		if(!isLengthTooLong(o.value,50,1)){
			var param={};
			var Service = {};
			Service.HosId = Commonjs.getUrlParam("HosId");
			Service.QuestId = id;
			Service.Question = o.value;
			param.reqParam = Commonjs.getApiReqParams(Service);
			var url = '/survey/updateQuest.do';
			Commonjs.ajaxSync(url,param,function(_d){
				if(!_d.RespCode == 10000){
					ComWbj.artTips("提示","warning","操作异常",1.5,null);
			 	}
			});
		}else{
			ComWbj.artTips("提示","warning","矩阵题目字数在1~50之间",1.5,null);
		}
	}
  	
  	//选项上移
	function setJZChildrenQuestionUp(o){
		var id = o.id;
	 	var p_id = $(o).parent().prev().attr('QuestId');
	 	if(p_id == undefined){
	 		ComWbj.artTips("提示","warning","已经是第一题",1.5,null);
//	 		ComWbj.alertIconNo('提示：','已经是第一题','warning');
	 		return ;
	 	}
		var param={};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.QuestId1 = id;
		Service.QuestId2 = p_id;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionSort.do';
		Commonjs.ajaxSync(url,param,function(_d){
			$("#ddq"+id).insertBefore("#ddq"+p_id);
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	}
	 
 	//选项上移
	function setJZChildrenQuestionDwon(o){
		var id = o.id;
	 	var n_id = $(o).parent().next().attr('QuestId');
	 	if(n_id == undefined){
	 		ComWbj.artTips("提示","warning","已经是最后一题",1.5,null);
//	 		ComWbj.alertIconNo('提示：','已经是最后一题','warning');
	 		return ;
	 	}
		var param={};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.QuestId1 = id;
		Service.QuestId2 = n_id;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionSort.do';
		Commonjs.ajaxSync(url,param,function(_d){
			$("#ddq"+n_id).insertBefore("#ddq"+id);
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	} 
	 
  	//选项上移
	function setJZQuestionItemUp(o){ 
		var id = o.id;
		var p_id = $(o).parent().prev().attr('QuestId');
		if(p_id == undefined){
			ComWbj.artTips("提示","warning","已经是第一个选项",1.5,null);
//	 		ComWbj.alertIconNo('提示：','已经是第一个选项','warning');
	 		return ;
	 	}
		var param={};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.ItemId1 = id;
		Service.ItemId2 = p_id;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionItemSort.do';
		$("#"+id).insertBefore("#"+p_id);
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	}
	
	//选项下移
	function setJZQuestionItemDown(o){
		var id = $(o).parent().parent().parent().attr('id');
		var n_id = $(o).parent().parent().parent().next().attr('id');
		if(n_id == undefined){
			ComWbj.artTips("提示","warning","已经是最后一个选项",1.5,null);
	 		return ;
	 	}
		//var p_id = $(o).parent().parent().parent().prev().attr('id');
		$("#"+n_id).insertBefore("#"+id);
		var param={};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.ItemId1 = id;
		Service.ItemId2 = n_id;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionItemSort.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	}
	
  	function initJZQuestionItemHtml(d){
  		var html = '<dl class="juzhen-wrap"><dt>选项</dt><div id="jzitemdiv'+d.QuestId+'">';  
	 	var _d = d.ChildrenMatrixQuestion;
	 	var ids = '';
	 	var size = 0;
	 	if(_d.length > 0){
	 		var data = _d[0].MatrixQuestItems;
	 		for(var i = 0;i<data.length;i++){
	 			size ++;
	 		}
	 	}
	 	if(size > 0){
	 		for(var j = 0;j<size;j++){
	 			var ids = '';
	 			for(var i = 0;i<_d.length;i++){
	 				var data = _d[i].MatrixQuestItems[j];
	 				if(Commonjs.isEmpty(data)){
	 					data = {};
	 					data.ItemId = 0;
	 				}
		 			if(i == 0){
		 				ids = data.ItemId;
		 			}else ids += ','+data.ItemId;
	 			}
	 			var ids1 = replaceAll(ids,',','-');
	 			html += '<dd ids="'+ids1+'" class="juzhen-opt" id="ddi'+ids1+'" >';
	 			html += '<input onblur="updateJZQuestionItem(this,\''+ids+'\')" name="jzitem'+d.QuestId+'" type="text" class="input-text input-text-w500 mr20" value="'+_d[0].MatrixQuestItems[j].ItemCont+'" />'; 
			    html += '<i id="'+ids1+'" class="icon icon-top mr15" onclick="setJZChildrenQuestionItemUp(this)"></i>';  
		      	html += '<i id="'+ids1+'" class="icon icon-bottom mr15" onclick="setJZChildrenQuestionItemDwon(this)"></i>';
		      	html += '<i class="icon icon-del" onclick="deleteQuestionItems(this,\''+ids+'\')"></i></dd>';
	 		}
	 	}
		html += '</div><dd><a href="javascript:;" class="btn btn-w-auto" onclick="addJZQuestionItemHtml(\''+d.QuestId+'\')">';
	 	html += '<i class="icon icon-add-w"></i>添加</a><a href="javascript:;" class="btn absolute" style="right:15px;bottom:17px" onclick="endEditPanel('+d.QuestId+')">保存</a></dd></dl>';
  		return html;
  	}

	function setJZChildrenQuestionItemUp(o){//矩阵选项位移
		
		var id = o.id;
	 	var p_id = $(o).parent().prev().attr('ids');
	 	if(p_id == undefined){
	 		ComWbj.artTips("提示","warning","已经是第一个选项",1.5,null);
	 		return ;
	 	}
		var param={};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.ItemIds1 = replaceAll(id,'-',',');
		Service.ItemIds2 = replaceAll(p_id,'-',',');
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeMatQuestionItemSorts.do';
	 	Commonjs.ajaxSync(url,param,function(_d){
	 		$("#ddi"+id).insertBefore("#ddi"+p_id);
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
		 	}
	 	});
	}

	function setJZChildrenQuestionItemDwon(o){//矩阵选项位移
		
		var id = o.id;
	 	var n_id = $(o).parent().next().attr('ids');
	 	if(n_id == undefined) {
	 		ComWbj.artTips("提示","warning","已经是最后一个选项",1.5,null);
//	 		ComWbj.alertIconNo('提示：','已经是最后一个选项','warning');
	 		return ;
	 	}
		var param={};
	    var Service = {};
	    Service.HosId = Commonjs.getUrlParam("HosId");
		Service.ItemIds1 = replaceAll(id,'-',',');
		Service.ItemIds2 = replaceAll(n_id,'-',',');
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeMatQuestionItemSorts.do';
	 	Commonjs.ajaxSync(url,param,function(_d){
	 		$("#ddi"+n_id).insertBefore("#ddi"+id);
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
	 	});
	}


	function addJZQuestionItemHtml(id){//新增矩阵选项
		var name = '选项';
		//新增多个矩阵
		var d = $('#jzdiv'+id).children();
		if(d.length < 1){
			ComWbj.artTips("提示","warning","请先添加矩阵问题，后再添加选项",1.5,null);
//			ComWbj.alertIconNo('提示：','请先添加矩阵问题，后再添加选项','warning');
			return ;
		}else{
			d.each(function(){
				addJZQuestionItem($(this).attr('QuestId'),name);
			});
			getWJData();
			$('#i'+id).click();
		}
	}
	
	//修改选题
	function updateJZQuestionItem(o,ids){
		if(!isLengthTooLong(o.value,50,1)){
			var arr_ids = ids.split(',');
			if(arr_ids.length > 0 ){
				for(var i = 0;i<arr_ids.length;i++){
					updateJZQuestionItemTile(arr_ids[i],o.value);
				}
			}
		}else{
			ComWbj.artTips("提示","warning","矩阵选项字数在1~50之间",1.5,null);
//			ComWbj.alertIconNo('提示：','矩阵选项字数在1~50之间','warning');
		}
	}
	
	//修改选项
	function updateJZQuestionItemTile(id,name){
		var param={};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.ItemId = id;
		Service.ItemCont = name;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/upateItem.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	}
		
	function deleteQuestionItems(o,ids){//删除多项选项
		var ids1 = replaceAll(ids,',','-');
		if($('#ddi'+ids1).parent().children().length <=1){
			ComWbj.artTips("提示","warning","矩阵选项不能全部被删除",1.5,null);
			return ;
		}else{
			var param={};
			var Service = {};
			Service.HosId = Commonjs.getUrlParam("HosId");
			Service.ItemIds = ids;
			Service.ItemCont = name;
			param.reqParam = Commonjs.getApiReqParams(Service);
			var url = '/survey/deleteQuestionItems.do';
			Commonjs.ajaxSync(url,param,function(_d){
				if(_d.RespCode != 10000){
					ComWbj.artTips("提示","warning","操作异常"+_d.RespMessage,1.5,null);
					return true;
			 	}else{
		  	  		$(o).parent().remove();
		  	  	}
			});
		}
	}
	
  	function jz_table(d,type){
  		var table = '<table class="tb tb-border">';
  		var _d = d.ChildrenMatrixQuestion;
  		for(var i = 0 ;i<_d.length;i++){
			if(i == 0){
				table +='<tr><th width="125" ></th>';
				for(var j = 0 ;j<_d[i].MatrixQuestItems.length;j++){
					table +='<th width="220" name="th'+d.QuestId+'">'+_d[i].MatrixQuestItems[j].ItemCont+'</th>';
				}
				table +='</tr>';
				table +='<tr><th width="125">'+_d[i].Question+'</th>';
				for(var j = 0 ;j<_d[i].MatrixQuestItems.length;j++){
					if(type == 4){
						table +='<td><input type="radio" name="checkbox1" value="" /></td>';
					}else 
						table +='<td><input type="checkbox" name="checkbox1" value="" /></td>';
				}
				table +='</tr>';
			}else{
				table +='<tr><th width="125">'+_d[i].Question+'</th>';
				for(var j = 0 ;j<_d[i].MatrixQuestItems.length;j++){
					if(type == 4){
						table +='<td><input type="radio" name="checkbox1" value="" /></td>';
					}else 
						table +='<td><input type="checkbox" name="checkbox1" value="" /></td>';
				}
				table +='</tr>';
			}
  		}
  		return table+='</table>';
  	}
  	
  	function tkt(d,index){//填空题
  		var html = '<div QuestId="'+d.QuestId+'" id="div'+d.QuestId+'" class="qa-item-wrap"><div class="q-tit">';
  		 	html += '<span class="q-num">Q'+(index+1)+'</span>'; 
  		 	html += '<div class="q-tit-txt"  onblur="updateQuestion(this,'+d.QuestId+')">'+d.Question+'</div>'; 
  			if(d.Mustquest == 1){
  				html += '<span class="f12" style="color:red;">（选填）</span>';
  			}
  		 	html += '</div><div class="a-wrap"><div class="mt15" style="margin-left:31px;">';
  		 	html += '<textarea name="" id="" class="textarea textarea-w600"></textarea>';
  		 	html += '</div><div class="ml30 mt20" id="btn'+d.QuestId+'" style="display:none">';
  		 	html += '<a href="javascript:;" class="btn absolute" style="right:15px;bottom:17px" onclick="endEditPanel('+d.QuestId+')">保存</a>';
  		 	html += '</div></div>';
  		 	
  		 
  		 	
  		 	html += '<div class="qa-edit-tools"><i id="i'+d.QuestId+'" class="icon icon-edit mr25" onclick="editPanel('+d.QuestId+')"></i>';
	  		html += '<i id="iedit'+d.QuestId+'" class="icon icon-setting mr25" onclick="openEditQuestionPanel2('+d.QuestId+',\''+d.Mustquest+'\')"></i>';
	  		html += '<i id="iup'+d.QuestId+'" class="icon icon-up mr25" onclick="setQuestionUp(this)"></i>';
	  		html += '<i id="idown'+d.QuestId+'" class="icon icon-down mr25" onclick="setQuestionDown(this)"></i>';
	  		html += '<i id="idel'+d.QuestId+'" class="icon icon-trash" onclick="delQuestionByID('+d.QuestId+')"></i>';
            html += '</div></div>';
		return html ;
  	}
  	
  	//选择题
  	function xzt(d,index,type){
  		var _d = d.SvQuestionItems;
  		var li = getDxtLis(_d,type);
  		var ul = getDxtUL(d,index,li,type);
  		return ul;
  	}

	//删除问题
	function delQuestionByID(id){
	
		art.dialog({
	 		id: 'testID',
	 	    width: '245px',
	 	    height: '109px',
	 	    //content: '您要删除问卷“'+name+'”吗？注意：删除后无法恢复',
	 	    content: '您要删除该问题吗？注意：删除后无法恢复',
	 	    lock: true,
	 	    button: [{
	 	      	name: '确定',
	 	       	callback: function () {
	 	    		var Api = 'survey.SurveyApiImpl.deleteQuestion.do';
				  	var param = {};
			  		var Service = {};
			  		Service.HosId = Commonjs.getUrlParam("HosId");
					Service.QuestId = id;
					param.reqParam = Commonjs.getApiReqParams(Service);
				 	var url = '/survey/deleteQuestion.do';
			  	  	Commonjs.ajaxSync(url,param,function(_d){
			  	  	if(_d.RespCode == 10000){
				  	  	ComWbj.artTips("提示","succeed",_d.RespMessage,1.5,null);
				  	  		getWJData();
				  	  		return true;
				  	  	}else{
				  	  		ComWbj.artTips("提示","warning","添加异常",1.5,null);
				  	  		return false;
				  	  	}
			  	  	});
	 	       	}
	 	    },{
	 	 		name: '取消'
	 	 	}]
	 	});
	
		
//	  	 art.dialog({
//	 		id: 'testID',
//	 	    width: '245px',
//	 	    height: '109px',
//	 	    content: '确认要删除？',
//	 	    lock: true,
//	 	    button: [{
//	 	      	name: '确定',
//	 	       	callback: function () {
//	 	       		var Api = 'survey.SurveyApiImpl.deleteQuestion';
//				  	var params = {};
//				    params.QuestId = id;
//				 	var url = '../survey/wtbj_deleteQuestion.action';
//			  	  	Commonjs.ajax(url,params,false);
//			  	  	if(_d.Code == 10000){
//			  	  	ComWbj.artTips("提示","warning","_d.Message",1.5,null);
////			  	  		ComWbj.alertIconNo('提示：',_d.Message,'warning');
//			  	  		getWJData();
//			  	  		return true;
//			  	  	}else{
//			  	  	ComWbj.artTips("提示","warning","添加异常",1.5,null);
////			  	  		ComWbj.alertIconNo('提示：','添加异常','warning');
//			  	  		return false;
//			  	  	}
//	 	       	}
//	 	 	},{
//	 	 		name: '取消'
//	 	 	}]
//	 	});
	}
  	
  	function getDxtUL(d,index,li,type){
  		var dxt_ul = '<div class="qa-item-wrap" QuestId="'+d.QuestId+'" id="div'+d.QuestId+'">';
  		dxt_ul += '<div class="q-tit"><span class="q-num" id="queueno">Q'+(index+1)+'</span>'; 
  		dxt_ul += '<div class="q-tit-txt" onblur="updateQuestion(this,'+d.QuestId+')">'+d.Question+'</div>'; 
  		if(d.Mustquest == 1){
  			dxt_ul += '<span class="f12" style="color:red;">（选填）</span>';
  		}
  		dxt_ul += '</div><div class="a-wrap"><ul class="radio-options" id="ul'+d.QuestId+'">'+li+'</ul>';
  		dxt_ul += '<div class="ml30 mt20" id="btn'+d.QuestId+'" style="display:none">';
  		dxt_ul += '<a href="javascript:;" class="btn btn-w-auto" onclick="addQuestionItem('+d.QuestId+','+d.QuestType+')"> ';
  		dxt_ul += '<i class="icon icon-add-w"></i>添加</a> ';
  		dxt_ul += '<a href="javascript:;" class="btn absolute" style="right:15px;bottom:17px" onclick="endEditPanel('+d.QuestId+')">保存</a>';
  		dxt_ul += '</div></div><div class="qa-edit-tools">';
  		dxt_ul += '<i id="i'+d.QuestId+'" class="icon icon-edit mr25" onclick="editPanel('+d.QuestId+')"></i>';
  		if(type == 1){
  			dxt_ul += '<i id="iedit'+d.QuestId+'" class="icon icon-setting mr25" onclick="openEditQuestionPanel('+d.QuestId+',\''+d.Mustquest+'\')"></i>';
  		}else 
  			dxt_ul += '<i id="iedit'+d.QuestId+'" class="icon icon-setting mr25" onclick="openEditQuestionPanel1('+d.QuestId+',\''+d.Mustquest+'\',\''+d.Maxoption+'\',\''+d.Minoption+'\')"></i>';
  		dxt_ul += '<i id="iup'+d.QuestId+'" class="icon icon-up mr25" onclick="setQuestionUp(this)"></i>';
  		dxt_ul += '<i id="idown'+d.QuestId+'" class="icon icon-down mr25" onclick="setQuestionDown(this)"></i>';
  		dxt_ul += '<i id="idel'+d.QuestId+'" class="icon icon-trash" onclick="delQuestionByID('+d.QuestId+')"></i>';
  		dxt_ul += '</div></div>';
  		return dxt_ul;
  	}
 	
 	//编辑单选题
 	function openEditQuestionPanel(id,mid){
 		$('#setting3').prev('.mask').show().end().fadeIn();
 	 	$('#dxtFlowDiv').children().eq(0).nextAll().remove();
 	 	initSelectModelDate(id);
 	 	$('#setting3_questionID').val(id);
 	 	setPopAlign('setting3');
 	 	if(Commonjs.isEmpty(mid) || mid == 0){
 	 		$('#setting3_dxt_noAnswer').removeAttr('checked');
 	 	}else $('#setting3_dxt_noAnswer').attr('checked','checked');
 	 	initCurrentItemFolw(id);
 	}
 	
 	//编辑多选题
 	function openEditQuestionPanel1(id,mid,mor,les){
 		$('#setting2').prev('.mask').show().end().fadeIn();
 	 	$('#setting2_questionID').val(id);
 	 	$('#setting2_count').val($('#ul'+id+' li').length);
 	 	if(parseInt(mor) > 0){
 	 		$('#setting2_mor').val(mor);
 	 	}else $('#setting2_mor').val('');
 	 	if(parseInt(les) > 0){
 	 		$('#setting2_les').val(les);
 	 	}else $('#setting2_les').val('');
 	 	setPopAlign('setting2');
 	 	if(Commonjs.isEmpty(mid) || mid == 0){
 	 		$('#setting2_dxt_noAnswer').removeAttr('checked');
 	 	}else $('#setting2_dxt_noAnswer').attr('checked','checked');
 	}
	 
	//编辑矩阵多选题
 	function openEditQuestionPanel3(id,mid,mor,les){
 		$('#setting2').prev('.mask').show().end().fadeIn();
 	 	$('#setting2_questionID').val(id);
 	 	$('#setting2_count').val($('th[name="th'+id+'"]').length);
 	 	if(parseInt(mor) > 0){
 	 		$('#setting2_les').val(les);
 	 	}else $('#setting2_les').val('');
 	 	if(parseInt(les) > 0){
 	 		$('#setting2_mor').val(mor);
 	 	}else $('#setting2_mor').val('');
 	 	setPopAlign('setting2');
 	 	if(Commonjs.isEmpty(mid) || mid == 0){
 	 		$('#setting2_dxt_noAnswer').removeAttr('checked');
 	 	}else $('#setting2_dxt_noAnswer').attr('checked','checked');
 	} 
	 	
 	function openEditQuestionPanel2(id,mid){//编辑
 		$('#setting1').prev('.mask').show().end().fadeIn();
 	 	$('#setting1_questionID').val(id);
 	 	setPopAlign('setting1');
 	 	if(Commonjs.isEmpty(mid) || mid == 0){
 	 		$('#setting1_dxt_noAnswer').removeAttr('checked');
 	 	}else $('#setting1_dxt_noAnswer').attr('checked','checked');
 	}
 	
 	function saveNoAnswerSet(){
 		var type = 0;
		if($('#setting1_dxt_noAnswer').attr('checked') == 'checked'){
			type = 1;
		}
 		var id = $('#setting1_questionID').val();
	    var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.QuestId = id;
		Service.MustQuest = type;
	    param.reqParam = Commonjs.getApiReqParams(Service);
	 	var url = '/survey/updateQuest.do';
	 	Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}else {
		 		ComWbj.artTips("提示","succeed","保存" + _d.RespMessage,1.5,null);
//		 		ComWbj.alertIconNo('提示：',_d.Message,'succeed');
				$('#setting1').prev('.mask').hide().end().fadeOut();
			 	getWJData();
		 	}
	 	});

 	}
 	
 	function saveMoreAnswerSet(){//保存多选题设置
 	
// 		var les = parseInt($('#setting2_les').val());
// 		var mor = parseInt($('#setting2_mor').val());
// 		var num = parseInt($('#setting2_count').val());
 		var les = $('#setting2_les').val();
 		var mor = $('#setting2_mor').val();
 		var num = $('#setting2_count').val();
 		var f = false;
 		var type = 0;
		if($('#setting2_dxt_noAnswer').attr('checked') == 'checked'){
			type = 1;
		}
		if (type!=1) {
			if(!Commonjs.isEmpty(les)){
	 		 	if(les > num || les < 1){
	 		 		ComWbj.artTips("提示","warning","最少选择数量必须小于选项数，最少为“1”",1.5,null);
//	 		 		ComWbj.alertIconNo('提示：','最少选择数量必须小于题数，最少为“1”','warning');
	 		 		return ;
	 		 	}
	 		 	f = true;
	 		}else if(les == 0){
	 			ComWbj.artTips("提示","warning","最少选择数量必须小于选项数，最少为“1”",1.5,null);
//	 			ComWbj.alertIconNo('提示：','最少选择数量必须小于题数，最少为“1”','warning');
	 		 	return ;
	 		}
	 		if(!Commonjs.isEmpty(mor)){
	 		 	if(mor > num || mor < 1){
	 		 		ComWbj.artTips("提示","warning","最多选择数量不能超过选项数，最少为“1”",1.5,null);
//	 		 		ComWbj.alertIconNo('提示：','最多选择数量不能超过题数，最少为“1”','warning');
	 		 		return ;
	 		 	}
	 		}else if(les == 0){
	 			ComWbj.artTips("提示","warning","最多选择数量不能超过选项数，最少为“1”",1.5,null);
//	 			ComWbj.alertIconNo('提示：','最多选择数量不能超过题数，最少为“1”','warning');
	 		 	return ;
	 		}
	 		if(f){
	 			if(les > mor){
	 				ComWbj.artTips("提示","warning","最多选择数量不能少于最少选择数量",1.5,null);
//	 				ComWbj.alertIconNo('提示：','最多选择数量不能少于最少选择数量','warning');
	 		 		return ;
	 			}
	 		}
		}
		var id = $('#setting2_questionID').val();
 		setQuestionMustquest(id,type);
 		setMMoption(les,mor,id);//
		 		
 	}
 	
 	function setMMoption(Minoption,Maxoption,id){//设置选项数量
	    var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.QuestId = id;
		Service.MinOption = Minoption;
		Service.MaxOption = Maxoption;
	    param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/updateQuest.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}else {
		 		ComWbj.artTips("提示","warning","保存" + _d.RespMessage,1.5,null);
//		 		ComWbj.alertIconNo('提示：',_d.Message,'succeed');
				$('#setting2').prev('.mask').hide().end().fadeOut();
			 	getWJData();
		 	}
		});
 	}
 	
 	function initSelectModelDate(id){
 		var options = '<option value="0">-请选择-</option>';
 		var num = 0;
 		$.each(dataArr,function(i,it){
 			if(it.QuestId == id){
 				for(var j=0;j<it.SvQuestionItems.length;j++){
 					num++;
 					options += '<option value="'+it.SvQuestionItems[j].ItemId+'">'
 					+it.SvQuestionItems[j].ItemCont+'</option>';
 				}
 			}
 		})
 		$('#setting3_itemCount').val(num);
 	 	$('#selectModel').children().eq(0).empty().append(options);
 	  	options = '<option value="0">-请选择-</option>';
 	  	var num = 0;
 	  	$.each(QuestionOption,function(i,it){
 		 	if(id == it.Id){
 		 		num = i;
 		 	}
 		})
 		$.each(QuestionOption,function(i,it){
 		 	if(id != it.Id){
 		 		if(i > num){
 		 			options += '<option value="'+it.Id+'">Q'+(i+1)+'</option>';
 		 		}
 		 	}
 		})
 		options += '<option  value="10">结束(计入结果)</option>';
 		options += '<option  value="11">结束(不计入结果)</option>';
 		$('#selectModel').children().eq(1).empty().append(options);
 	}
 	
 	function initCurrentItemFolw(id){//初始化 流向选项信息
 		var ids = '';
 		var obj = null;
 		$.each(dataArr,function(i,o){
 			if(o.QuestId == id){
 				obj = o.SvQuestionItems;
 			}
 		});
 		for(var i = 0;i<obj.length;i++){
 			if(!Commonjs.isEmpty(obj[i].JumpQuest)){
 				initSelectHtml(id,obj[i].JumpQuest,obj[i].ItemId);
 				if(ids == ''){
 					ids += obj[i].ItemId;
 				}else ids += ','+obj[i].ItemId;
 			}
 		}
 		$('#setting3_allFlowsItemIds').val(ids);
 	}
 	
	function initSelectHtml(id,v,v1){
	 	
		var html = '<div class="f12 mt15">';
		html += '如果本题选项中<select name="dxtSelect" id="" class="inp-sel ml10 mr10" style="width:120px;">';
		html += initQuestionItemOption(id,v1);
		html += '</select>则跳转到';
		html += '<select name="dxtSelectLink" id="" class="inp-sel ml10 mr10" style="width:120px;">';
		html += initQuestionOption(id,v);
		html += '</select><i class="icon icon-del" onclick="delDxtSelect(this)"></i></div>';
 		$("#dxtFlowDiv").append(html);
	} 	
 	
 	function initQuestionOption(id,v){//初始化选题下拉
 	 	
 	  	options = '<option value="0">-请选择-</option>';
 	  	var num = 0;
 		$.each(QuestionOption,function(i,it){
 			if(id == it.Id){
 				num = i;
 			}
 		})
 		$.each(QuestionOption,function(i,it){
 			if(id != it.Id){
 				if(v && v == it.Id){
		 			options += '<option selected="selected" value="'+it.Id+'">Q'+(i+1)+'</option>';
 				}else{
 					if(i > num){
 						options += '<option value="'+it.Id+'">Q'+(i+1)+'</option>';
 					}
 				}
 			}
 		})
 		
 		if(v == 10){
 			options += '<option selected="selected" value="10">结束(计入结果)</option>';
 			options += '<option  value="11">结束(不计入结果)</option>';
 		}else if(v == 11){
 			options += '<option  value="10">结束(计入结果)</option>';
 			options += '<option selected="selected" value="11">结束(不计入结果)</option>';
 		}else{
 			options += '<option  value="10">结束(计入结果)</option>';
 			options += '<option  value="11">结束(不计入结果)</option>';
 		}
 		return options;
 	}
 	
 	function initQuestionItemOption(id,v){//初始化选项下拉
 		var options = '<option value="0">-请选择-</option>';
 		var num = 0;
 		$.each(dataArr,function(i,it){
 			if(it.QuestId == id){
 				for(var j=0;j<it.SvQuestionItems.length;j++){
 					num++;
 					if(v && v == it.SvQuestionItems[j].ItemId){
 						options += '<option selected="selected" value="'+it.SvQuestionItems[j].ItemId+'">'
	 					+it.SvQuestionItems[j].ItemCont+'</option>'; 
 					}else 
 					options += '<option value="'+it.SvQuestionItems[j].ItemId+'">'
 					+it.SvQuestionItems[j].ItemCont+'</option>';
 				}
 			}
 		})
 		return options;
 	}
 	
 	function addDxtQuestionFlowSelect(){//添加单选题流向
 		var num = $('#setting3_itemCount').val();
		if($("#dxtFlowDiv").children().length >= (parseInt(num)+1)){
			ComWbj.artTips("提示","warning","跳转设置数量不能超过答案选项总数",1.5,null);
//			ComWbj.alertIconNo('提示：','跳转设置数量不能超过答案选项总数','warning');
			return;
		}else{
			var html = $('#selectModel').html();
			$("#dxtFlowDiv").append('<div class="f12 mt15">'+html+'</div>');
		}
 	}
 	
 	function replaceAll(s,s1,s2){//替换全部
 		s = s.replace(s1,s2);
 		if(s == s.replace(s1,s2)){
 			return s;
 		}else  return replaceAll(s,s1,s2);
 	}
 	
 	function addQuestionFlow(sl1,sl2){//添加答案逻辑流向
	    var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.ItemId = id;
		Service.NextQuestId = id;
	    param.reqParam = Commonjs.getApiReqParams(Service);
	    var del_id = obj.attr('id');
	 	var url = '/survey/addQuestionFlow.do';
		Commonjs.ajaxSync(url,param,function(_d){
			$("#"+del_id).remove();
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	}
 	
 	//选项上移
	function setQuestionUp(o){
		var id = $(o).parent().parent().attr('QuestId');
	 	var p_id = $(o).parent().parent().prev().attr('QuestId');
	 	if(p_id == undefined){
	 		ComWbj.artTips("提示","warning","已经是第一题",1.5,null);
//	 		ComWbj.alertIconNo('提示：','已经是第一题','warning');
	 		return ;
	 	}
	    var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.QuestId1 = id;
		Service.QuestId2 = p_id;
	    param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionSort.do';
		Commonjs.ajaxSync(url,param,function(_d){
			$("#div"+id).insertBefore("#div"+p_id);
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		 	getWJData();
		});
	}
	
	//选项上移
	function setJZQuestionUp(o){
		var id = $(o).parent().parent().attr('QuestId');
	 	var p_id = $(o).parent().parent().prev().attr('QuestId');
	 	if(p_id == undefined){
	 		ComWbj.artTips("提示","warning","已经是第一题",1.5,null);
//	 		ComWbj.alertIconNo('提示：','已经是第一题','warning');
	 		return ;
	 	}
	    var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.QuestId1 = id;
		Service.QuestId2 = p_id;
	    param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionSort.do';
		Commonjs.ajaxSync(url,param,function(_d){
			$("#div"+id).insertBefore("#div"+p_id);
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	}
	
	//选项下移
	function setJZQuestionDown(o){
		var id = $(o).parent().parent().attr('QuestId');
	 	var n_id = $(o).parent().parent().next().attr('QuestId');
	 	if(n_id == undefined){
	 		ComWbj.artTips("提示","warning","已经是最后一题",1.5,null);
//	 		ComWbj.alertIconNo('提示：','已经是最后一题','warning');
	 		return ;
	 	}
	    var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.QuestId1 = id;
		Service.QuestId2 = n_id;
	    param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionSort.do';
		Commonjs.ajaxSync(url,param,function(_d){
			$("#div"+n_id).insertBefore("#div"+id);
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	}
	
	//选项下移
	function setQuestionDown(o){
		var id = $(o).parent().parent().attr('QuestId');
	 	var n_id = $(o).parent().parent().next().attr('QuestId');
	 	if(n_id == undefined){
	 		ComWbj.artTips("提示","warning","已经是最后一题",1.5,null);
	 		return ;
	 	}
		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.QuestId1 = id;
		Service.QuestId2 = n_id;
	    param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionSort.do';
		Commonjs.ajaxSync(url,param,function(_d){
			$("#div"+n_id).insertBefore("#div"+id);
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		 	getWJData();
		});
	}
 	//class="radio-opt-txt" style="overflow: hidden;white-space: nowrap;" style="overflow: hidden;white-space: nowrap;" 
    
     
    
 	function getDxtLis(d,type){
  		var size = d.length;
  		var dxt_li = '';
  		if(size > 0){
	  		for(var i = 0; i < size; i++){//d.IfAddblank IfAllowNull
	  			dxt_li += '<li id="'+d[i].ItemId+'"><div class="valign-m"><div class="valign-m-chk mr20">';
	  			if(type == 1){
	  				dxt_li += '<input type="radio" name="radio1" ></input>';
	  			}else dxt_li += '<input type="checkbox" name="radio1" ></input>';
	  			dxt_li += '<span class="radio-opt-txt" style="overflow: hidden;white-space: nowrap;"';
				dxt_li += ' onblur="updateQuestionItemTile(this,'+d[i].ItemId+')" >'+d[i].ItemCont+'</span>'+getJumpQuest(d[i]);
				if(type == 1){
					if(d[i].IfAddblank == 1){
						dxt_li += '<input name="xztkk'+d[i].QuestId+'" ></input>';
					}else dxt_li += '<input name="xztkk'+d[i].QuestId+'" style="display:none" ></input>';
				}
				dxt_li += '</div><span name="span'+d[i].QuestId+'" style="display:none" class=""> <label class="valign-m-chk mr20">';
				if(type == 1){
					if(d[i].IfAddblank == 1){
						dxt_li += '<input  class="radio-opt-txt" contenteditable="true" style="width:190px;display:none"></input>';
						dxt_li += '<input type="checkbox" checked=checked onclick="setItemBlankPanel(this,'+d[i].ItemId+')" ></input>';
						dxt_li += '<span>选项后增加填空框</span></label>';
						dxt_li += '<label class="valign-m-chk mr20" id="bt'+d[i].ItemId+'" >';
						if(d[i].IfAllowNull == 1){
							dxt_li += '<input checked=checked type="checkbox" onclick="setItemBlank(this,'+d[i].ItemId+')" ></input>';
						}else dxt_li += '<input type="checkbox" onclick="setItemBlank(this,'+d[i].ItemId+')" ></input>';
				 		dxt_li += '<span>该空可不填</span></label>';
					}else{
						dxt_li += '<input  class="radio-opt-txt" contenteditable="true" style="width:190px;display:none"></input>';
					 	dxt_li += '<input type="checkbox" onclick="setItemBlankPanel(this,'+d[i].ItemId+')" ></input>';
						dxt_li += '<span>选项后增加填空框</span></label><label class="valign-m-chk mr20">';
						dxt_li += '<label class="valign-m-chk mr20" style="display:none" id="bt'+d[i].ItemId+'" >';
						if(d[i].IfAllowNull == 1){
							dxt_li += '<input checked=checked type="checkbox" onclick="setItemBlank(this,'+d[i].ItemId+')" ></input>';
						}else dxt_li += '<input type="checkbox" onclick="setItemBlank(this,'+d[i].ItemId+')" ></input>';
				 		dxt_li += '<span>该空可不填</span></label>';
					}
			 	}
			 	dxt_li += '<i name="ep" class="icon icon-top mr15" onclick="setQuestionItemUp(this,'+type+','+d[i].ItemId+')"></i>';
			 	dxt_li += '<i name="ep" class="icon icon-bottom mr15" onclick="setQuestionItemDown(this,'+type+','+d[i].ItemId+')"></i>';
			 	dxt_li += '<i name="ep" class="icon icon-del mr15" onclick="delQuestionItem(this,'+d[i].ItemId+','+type+','+d[i].QuestId+')"></i></span></div> </li>';
	  		}
  		}
  		//console.log("-----------> "+dxt_li);
  		return dxt_li;
  	}
	
	function getJumpQuest(d){
		if(Commonjs.isEmpty(d.JumpQuest)){
			return '';
		}else{
			for(var i = 0;i<QuestionOption.length;i++){
				if(QuestionOption[i].Id == d.JumpQuest){
					return "<span style='color:red'>（该题跳转至Q"+(i+1)+"）</span>";
				}
			}
		}
		return '';
	}
	
	function getDxtLi(id,title,pid,type){
 		var dxt_li = '';
  		dxt_li += '<li id="'+id+'"><div class="valign-m"><div class="valign-m-chk mr20">';
		if(type == 1){
	  		dxt_li += '<input type="radio" name="radio1" />';
		}else{
			dxt_li += '<input type="checkbox" name="radio1" />';
		}
		dxt_li += '<span class="radio-opt-txt" style="overflow: hidden;white-space: nowrap;" onblur="updateQuestionItemTile(this,'+id+')" >'+title+'</span></div>';
		//	dxt_li += '<span class="radio-opt-txt" style="overflow: hidden;white-space: nowrap;" contenteditable="true" style="display:none"></span>&nbsp;&nbsp;';
		dxt_li += '<span name="span'+pid+'" style="display:none" class=""> <label class="valign-m-chk mr20">';
		if(type == 1){
			dxt_li += '<input  class="radio-opt-txt" contenteditable="true" style="width:190px;display:none">';
			dxt_li += '<input type="checkbox" onclick="setItemBlankPanel(this,'+id+')" />';
			dxt_li += '<span>选项后增加填空框</span></label><label style="display:none" id="bt'+id+'" class="valign-m-chk mr20">';
			dxt_li += '<input type="checkbox" onclick="setItemBlank(this,'+id+')" />';
		 	dxt_li += '<span>该空可不填</span></label>';
	 	}
	 	dxt_li += '<i name="ep"  class="icon icon-top mr15" onclick="setQuestionItemUp(this,'+type+','+id+')"></i>';
	 	dxt_li += '<i name="ep"  class="icon icon-bottom mr15" onclick="setQuestionItemDown(this,'+type+','+id+')"></i>';
	 	dxt_li += '<i name="ep"  class="icon icon-del mr15" onclick="delQuestionItem(this,'+id+','+type+','+pid+')"></i></span></div> </li>';
  		return dxt_li;
  	}
	
	//Item_blank	  	
  	function setItemBlankPanel(o,id){
  		
		var type = 0;
  		if(o.checked == true){
  			type = 1;
		//	$(o).prev().show();
			$('#bt'+id).show();
		}else{
		//	$(o).prev().hide();
			$('#bt'+id).hide();
			type = 0;
  		}
  		$('#bt'+id).children().removeAttr('checked');
  		var param = {};
  		var Service = {};
  		Service.HosId = Commonjs.getUrlParam("HosId");
  		Service.ItemId = id;
  		Service.IfAddblank = type	//	是否添加空白填空 1是 0不是	
	    param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/upateItem.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
		 	}else{
		 		setItemBlank1(id,0);
		 	}
		});
  	}
  	
  	function jzEditPanel(id){
		$('#div'+id).attr('class','qa-item-wrap editable');
		$('#jzedit'+id).show();
		$('#jzshow'+id).hide();
		$('#iedit'+id).hide();
		$('#i'+id).hide();
  	 	$('#iup'+id).hide();
  	 	$('#idown'+id).hide();
  	 	$('#idel'+id).hide();
	}
  	
  	//Item_blank	
  	
  	function setItemBlank1(id,type){
  		var param={};
  		var Service = {};
  		Service.HosId = Commonjs.getUrlParam("HosId");
  		Service.ItemId = id;
  		Service.IfAllowNull = type	  //  是否该空可不填 0是 1否
	    param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/upateItem.do';
		Commonjs.ajaxSync(url,param,null);
  	}
  	  	
  	function setItemBlank(o,id){
  		var type = 0;
  		if(o.checked == true) type = 1;
		else type = 0;
  		var param = {};
  		var Service = {};
  		Service.HosId = Commonjs.getUrlParam("HosId");
 		Service.ItemId = id;
 		Service.IfAllowNull = type	  //  是否该空可不填 0是 1否
	  	param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/upateItem.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
  	}
	
	function delDxtSelect(o){
 		var obj = $(o).parent();
 		if($(o).parent().attr('id') == 'selectModel'){
 			$('#selectModel').hide();
 		}else obj.remove();
 	}
	
	function saveDxtFlow(){//保存单选题流向设置
		var type = 0;
		if($('#setting3_dxt_noAnswer').attr('checked') == 'checked'){
			type = 1;
		}
		var sl1 = new Array();
		var sl3 = new Array();
		var sl2 = new Array();
		$('select[name="dxtSelect"]').each(function(i){
			if($(this).parent().css('display') != 'none'){
				sl1.push($(this).val());
				sl3.push($(this).val());
			}
 	 	}) 
		if(sl1.length > 0){
			$('select[name="dxtSelectLink"]').each(function(i){
	 			if($(this).parent().css('display') != 'none'){
					sl2.push($(this).val());
				}
	 	 	})
	 	 	if(sl1.length > 0){
	 	 		for(var i = 0;i<sl1.length;i++){
	 	 			if(sl1[i] == 0 || sl2[i] == 0){
	 	 				ComWbj.artTips("提示","warning","请选择跳转条件",1.5,null);
//	 	 				ComWbj.alertIconNo('提示：','请选择跳转条件','warning');
	 	 				return false;
	 	 			}
	 	 		}
	 	 		sl1.sort();
	 	 		for(var i = 0;i<sl1.length;i++){
					if (sl1[i] == sl1[i+1]){
						ComWbj.artTips("提示","warning","同个选项不能进行多次跳转设置",1.5,null);
//						ComWbj.alertIconNo('提示：','同个选项不能进行多次跳转设置','warning');
						return ;
		 			}
	 	 		}
	 	 	}
		}
		var f = delAllItemFlow(sl1);
		if(f){
			setQuestionMustquest($('#setting3_questionID').val(),type);
			addQuestionFlows(sl3,sl2);
		}
	}
 	
 	function delAllItemFlow(){//删除所有流向
 		var param = {};
 		var Service = {};
 		Service.HosId = Commonjs.getUrlParam("HosId");
 		Service.ItemIds = $('#setting3_allFlowsItemIds').val();
	  	param.reqParam = Commonjs.getApiReqParams(Service);
	 	var url = '/survey/delAllQuestionFlow.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
				return false;
		 	}
		});
	 	return true;
 	}
 	
	function addQuestionFlows(sl1,sl2){//添加答案逻辑流向
		
		var ItemIds = '';
		var NextQuestIds = '';
		if(sl1.length > 0 && sl1.length == sl2.length){
			for(var i = 0;i < sl1.length;i++){
				if(i == 0){
					ItemIds = sl1[i];
					NextQuestIds = sl2[i];
				}else{
					ItemIds += ','+sl1[i];
					NextQuestIds += ','+sl2[i];
				}
			}
			var param={};
			var Service = {};
			Service.HosId = Commonjs.getUrlParam("HosId");
	 		Service.ItemIds = ItemIds;
	 		Service.NextQuestIds = NextQuestIds;
		  	param.reqParam = Commonjs.getApiReqParams(Service);
		 	var url = '/survey/addQuestionFlows.do';
			Commonjs.ajaxSync(url,param,function(_d){
				if(_d.RespCode != 10000){
					ComWbj.artTips("提示","warning","操作异常",1.5,null);
//					ComWbj.alertIconNo('提示：','操作异常','warning');
			 	}else{
			 		ComWbj.artTips("提示","succeed",_d.RespMessage,1.5,null);
//			 		ComWbj.alertIconNo('提示：',_d.Message,'succeed');
			 		$('#setting3').prev('.mask').hide().end().fadeOut();
			 		getWJData();
			 	}
			});
		}else{
			ComWbj.artTips("提示","succeed","完成",1.5,null);
//			ComWbj.alertIconNo('提示：','完成','succeed');
			$('#setting3').prev('.mask').hide().end().fadeOut();
		 	getWJData();
		}
	}
	
	function setQuestionMustquest(id,type){//设置非必答
		var param={};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
 		Service.QuestId = id;
 		Service.MustQuest = type;
		param.reqParam = Commonjs.getApiReqParams(Service);
	 	var url = '/survey/updateQuest.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	}
	
	//删除选项
	function delQuestionItem(o,id,type,pid){
	 	if($('#ul'+pid).children().length <= 1){
	 		ComWbj.artTips("提示","warning","至少存在一个答题项",1.5,null);
//	 		ComWbj.alertIconNo('提示：','至少存在一个答题项','warning');
	 		return;
	 	}else{
	 		var param={};
	 		var Service = {};
	 		Service.HosId = Commonjs.getUrlParam("HosId");
	 		Service.ItemId = id;
			param.reqParam = Commonjs.getApiReqParams(Service);
		 	var url = '/survey/deleteQuestionItem.do';
			Commonjs.ajaxSync(url,param,function(_d){
				$("#"+id).remove();
				if(_d.RespCode != 10000){
					ComWbj.artTips("提示","warning","操作异常",1.5,null);
//					ComWbj.alertIconNo('提示：','操作异常','warning');
			 	}
			});
	 	}
	}
	
	
	//选项上移
	function setQuestionItemUp(o,type,id){ 
		var p_id = null;
		p_id = $('#'+id).prev().attr('id');
		if(p_id == undefined){
			ComWbj.artTips("提示","warning","已经是第一个选项",1.5,null);
//	 		ComWbj.alertIconNo('提示：','已经是第一个选项','warning');
	 		return ;
	 	}
		var param={};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
 		Service.ItemId1 = id;
 		Service.ItemId2 = p_id;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionItemSort.do';
		$("#"+id).insertBefore("#"+p_id);
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	}
	
	//选项下移
	function setQuestionItemDown(o,type,id){
		var n_id = null;
		if(type == 1){
			n_id = $('#'+id).next().attr('id');
		}else{
			n_id = $('#'+id).next().attr('id');
		}
		if(n_id == undefined){
			ComWbj.artTips("提示","warning","已经是最后一个选项",1.5,null);
//	 		ComWbj.alertIconNo('提示：','已经是最后一个选项','warning');
	 		return ;
	 	}
		$("#"+n_id).insertBefore("#"+id);
		var param={};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
 		Service.ItemId1 = id;
 		Service.ItemId2 = n_id;
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/exchangeQuestionItemSort.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
		 	}
		});
	 	 
	}
	
	//添加选项
	function addQuestionItem(id,type){
//		var param = {};
//		param.QuestId = id;
//		param.ItemCont = '请编辑选项内容';
//		param.OperatorId =  Commonjs.getOperatorId();
//		param.OperatorName = Commonjs.getOperatorName();
		var param={};
	 	var Service = {}; 
	 	Service.HosId = Commonjs.getUrlParam("HosId");
	 	Service.QuestId = id;
	 	Service.ItemCont = '请编辑选项内容';
		param.reqParam = Commonjs.getApiReqParams(Service);
		var url = '/survey/addItem.do';
		Commonjs.ajaxSync(url,param,function(_d){
			if(_d.RespCode != 10000){
				ComWbj.artTips("提示","warning","操作异常",1.5,null);
//				ComWbj.alertIconNo('提示：','操作异常','warning');
			}else{
				var dataArr = _d.Data;
				$('#ul'+id).append(getDxtLi(dataArr[0].ItemId,Service.ItemCont,id,type));
				$('#i'+id).click();
			}
		});
	}
	
	//完成编辑
	function endEditPanel(id){
		 if($('#div'+id).attr('class') == 'qa-item-wrap editable'){
			$('#div'+id).removeClass('editable');
			$('#btn'+id).hide();
			$('#iedit'+id).show(); 
  			$('#iup'+id).show(); 
  			$('#idown'+id).show(); 
  			$('#idel'+id).show(); 
		 }
		 $('span[name="span'+id+'"]').each(function(i){
			$(this).hide();
		});
		getWJData();
	}
		
	//完成编辑
	function jzEndEditPanel(id){
		if($('#div'+id).attr('class') == 'qa-item-wrap editable'){
			$('#div'+id).removeClass('editable');
			$('#jzedit').hide();
			$('#jzshow').show();
			$('#iedit'+id).show(); 
  			$('#iup'+id).show(); 
  			$('#idown'+id).show(); 
  			$('#idel'+id).show(); 
		 }
		getWJData();
	}
	
	function editPanel(id){
		$('span[name="span'+id+'"]').each(function(i){
			$(this).show();
		});
		$('input[name="xztkk'+id+'"]').each(function(i){
			$(this).hide();
		});
		$('#i'+id).hide(); 
		$('#btn'+id).show();
		$('#iedit'+id).hide(); 
  	 	$('#iup'+id).hide(); 
  	 	$('#idown'+id).hide(); 
  	 	$('#idel'+id).hide(); 
	}
		
	//修改选题
	function updateQuestion(o,id){
		if($(o).attr('contenteditable') == 'true'){
			//alert(id+"--"+$(o).text());
			if(!isLengthTooLong($(o).text(),100,1)){
				var param = {};
				var Service = {};
				Service.HosId = Commonjs.getUrlParam("HosId");
				Service.QuestId = id;
				Service.Question = ComWbj.trimStr($(o).text());
				param.reqParam = Commonjs.getApiReqParams(Service);
				var url = '/survey/updateQuest.do';
				Commonjs.ajaxSync(url,param,function(_d){
					if(_d.RespCode != 10000){
						ComWbj.artTips("提示","warning","操作异常",1.5,null);
//						ComWbj.alertIconNo('提示：','操作异常','warning');
				 	}
				});
			}else{
				ComWbj.artTips("提示","warning","题目字数在1~100个之间",1.5,null);
//				ComWbj.alertIconNo('提示：','题目字数在1~50个之间','warning');
			}
		}
	}
  	
  	//修改选项
	function updateQuestionItemTile(o,id){
		if($(o).attr('contenteditable') == 'true'){
			if(!isLengthTooLong($(o).text(),50,1)){
			 	var param={};
			 	var Service = {}; 
			 	Service.HosId = Commonjs.getUrlParam("HosId");
			 	Service.ItemId = id;
			 	Service.ItemCont = ComWbj.trimStr($(o).text());
				param.reqParam = Commonjs.getApiReqParams(Service);
			    //	IfAddblank	//	是否添加空白填空 1是 0不是	
				//  IfAllowNull //  是否该空可不填 0是 1否
				var url = '/survey/upateItem.do';
				Commonjs.ajaxSync(url,param,function(_d){
					if(_d.RespCode != 10000){
						ComWbj.artTips("提示","warning","操作异常",1.5,null);
//						ComWbj.alertIconNo('提示：','操作异常','warning');
				 	}
				});
			}else{
				ComWbj.artTips("提示","warning","选项字数在1~50之间",1.5,null);
//				ComWbj.alertIconNo('提示：','选项字数在1~50之间','warning');
			}
		}
	}
	//填空题 250个字符
	function isLengthTooLong(s,max,min){
		if(Commonjs.isEmpty(s)){
			return true;
		}else{
			s = ComWbj.trimStr(s);
			if(s.length > max)return true;
			if(s.length < min)return true;	
		}
		return false;
	}