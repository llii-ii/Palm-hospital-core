	var session = null;
	var SubjectId = 0;
 	var QuestionOption = null;
 	
	$(function(){
		SubjectId = QueryString('SubjectId');
		if(Commonjs.isEmpty(SubjectId)){
			ComWbj.artTips("提示","warning","未知问卷ID",2,null);
//			ComWbj.alertIconNo('提示：','未知问卷ID','warning');
			history.go(-1);
		}
		init();
	});
	
	function QueryString(val) {
		var uri = window.location.search;
		var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
		return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
	}
	
	//初始化 
	function init(){
		getWJData();
	}
	
		
  	function initQuestionCount(d){
  		QuestionOption = new Array();
  		dataArr = d.Data;
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
	
	function backToEdit(){//返回问卷编辑
		window.location.href = 'myd-wjlb.html';
//		history.go(-1);
	}
	
  	//获取问卷数据
  	function getWJData(){
  		
  		//var Api = 'survey.SurveyApiImpl.querySubjectById';
  		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = SubjectId;
		param.reqParam = Commonjs.getApiReqParams(Service);
		Commonjs.ajax('/survey/querySubjectById.do',param,function(_d){
			var subArr = new Array();
  			subArr = _d.Data[0];
			var SubjectTitle = subArr.SubjectTitle;
			var BeginIntro =  subArr.BeginIntro;
			var EndingIntro =  subArr.EndingIntro;
			$('#EndingIntro').text(EndingIntro);
			$('#BeginIntro').text(BeginIntro);
			$('#SubjectTitle').text(SubjectTitle);
			initQuestionCount(_d);
			initHtml(_d);
		});
  	}
  	
  	function initHtml(d){//初始化页面选题
  		$('#areaDiv').empty();
  		dataArr = d.Data[0].Result;
  		var _d = dataArr;
  		var size = _d.length;
  		if(size > 0){
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
  		}
  	}
 
 	function jzdx(d,index,type){//矩阵单选
  		var html = '<div QuestId="'+d.QuestId+'" id="div'+d.QuestId+'" class="qa-item-wrap juzhen"><div class="q-tit">';
  			html += '<span class="q-num">Q'+(index+1)+'</span>'
  			html += '<div class="q-tit-txt" >'+d.Question;
  			if(d.Mustquest == 1){
	  			html += '<span style="color:red">（选填）</span>';
	  		}
  			html += '</div></div><div class="a-wrap" id="jzshow'+d.QuestId+'"><div class="mt15 mb25" style="margin-left:31px;">'
  			html += jz_table(d,type);
            html += '</div></div>';  
		return html ;
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
  		var html = '<div QuestId=""  class="qa-item-wrap"><div class="q-tit">';
  		 	html += '<span class="q-num">Q'+(index+1)+'</span><div class="q-tit-txt" >'+d.Question;
  		 	if(d.Mustquest == 1){
	  			html += '<span style="color:red">（选填）</span>';
	  		}
  		 	html += '</div></div><div class="a-wrap"><div class="mt15" style="margin-left:31px;">';
  		 	html += '<textarea name="" id="" class="textarea textarea-w600"></textarea>';
  		 	html += '</div></div></div></div>';
		return html ;
  	}
  	
  	//选择题
  	function xzt(d,index,type){
  		var _d = d.SvQuestionItems;
  		var li = getDxtLis(_d,type);
  		var ul = getDxtUL(d,index,li,type);
  		return ul;
  	}

  	function getDxtUL(d,index,li,type){
  		var dxt_ul = '<div class="qa-item-wrap"><div class="q-tit"><span class="q-num" id="queueno">Q'+(index+1)+'</span>';
  		dxt_ul += '<div class="q-tit-txt" >'+d.Question+'';
  		if(d.Mustquest == 1){
  			dxt_ul += '<span style="color:red">（选填）</span>';
  		}
  		dxt_ul += '</div></div><div class="a-wrap"><ul class="radio-options" id="ul'+d.QuestId+'">'+li+'</ul>';
  		dxt_ul += '</div></div>';
  		return dxt_ul;
  	}
 	
 	function getDxtLis(d,type){
  		var size = d.length;
  		var dxt_li = '';
  		if(size > 0){
	  		for(var i = 0; i < size; i++){//d.IfAddblank IfAllowNull
	  			dxt_li += '<li id="'+d[i].ItemId+'"><div class="valign-m"><div class="valign-m-chk mr20">';
	  			if(type == 1){
	  				dxt_li += '<input type="radio" name="radio1" />';
	  			}else dxt_li += '<input type="checkbox" name="radio1" />';
	  			dxt_li += '<span class="radio-opt-txt" onblur="updateQuestionItemTile(this,'+d[i].ItemId+')" >'+d[i].ItemCont+'</span>'+getJumpQuest(d[i])+'';
				if(type == 1){
					if(d[i].IfAddblank == 1){
						dxt_li += '<input name="xztkk'+d[i].QuestId+'" />';
					}else dxt_li += '<input name="xztkk'+d[i].QuestId+'" style="display:none" />';
				}
				dxt_li += '</div><span name="span'+d[i].QuestId+'" style="display:none" class=""> <label class="valign-m-chk mr20">';
				if(d[i].IfAddblank == 1){
					dxt_li += '<input type="checkbox" checked=checked onclick="setItemBlankPanel(this,'+d[i].ItemId+')" />';
				}else dxt_li += '<input type="checkbox" onclick="setItemBlankPanel(this,'+d[i].ItemId+')" />';
				dxt_li += '<span>选项后增加填空框</span></label><label class="valign-m-chk mr20">';
				if(d[i].IfAllowNull == 1){
					dxt_li += '<input checked=checked type="checkbox" onclick="setItemBlank(this,'+d[i].ItemId+')" />';
				}else dxt_li += '<input type="checkbox" onclick="setItemBlank(this,'+d[i].ItemId+')" />';
			  
			 	dxt_li += '<span>该空可不填</span></label><i class="icon icon-top mr15" onclick="setQuestionItemUp(this)"></i>';
			 	dxt_li += '<i class="icon icon-bottom mr15" onclick="setQuestionItemDown(this)"></i>';
			 	dxt_li += '<i class="icon icon-del mr15" onclick="delQuestionItem(this,'+d[i].ItemId+')"></i></span></div> </li>';
	  		}
  		}
  		return dxt_li;
  	}
	
	function getJumpQuest(d){
		if(Commonjs.isEmpty(d.JumpQuest)){
			return '';
		}else{
			for(var i = 0;i<QuestionOption.length;i++){
				if(QuestionOption[i].id == d.JumpQuest){
					return "<span style='color:red'>（该题跳转至Q"+(i+1)+"）</span>";
				}
			}
		}
		return '';
	}