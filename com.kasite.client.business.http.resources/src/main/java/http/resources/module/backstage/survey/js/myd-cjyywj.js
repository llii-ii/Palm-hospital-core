
	var session = YihuUtil.getSession();
	
	var SubjectId = 5582;
	
	$(function(){
		showMode(1,SubjectId);
	});

	function showMode(type,id){
		getWJData(type,id);
	}
	
	//获取问卷数据
  	function getWJData(type,id){
  		
  		var Api = 'survey.SurveyApiImpl.querySubjectById';
  		var Service = {};
  		Service.HosId = Commonjs.getUrlParam("HosId");
  		Service.SubjectId = id;
  		var param = {};
  		param.reqParam = Commonjs.getApiReqParams(Service);
  		Commonjs.ajax('/survey/querySubjectById.do',param,function(_d){
  			initHtml(_d.Data,type);
  		});
  	}
  	
	function initHtml(d,type){
		var html = '';
		html += '<div class="tpl-bg"><h2>'+ d.subjectTitle+'</h2>';
		html += '<p class="mt25 c-666 f12">'+d.beginIntro+'</p>'
		html += '<ol class="tpl-qlist">'
		$.each(d.Result,function(i,o){
			html += '<li><span class="q-num">Q'+(i+1)+'</span>'+o.question+'</li>'
		})
		html += '</ol><p class="f16 mt35">'+d.endingIntro+'</p>'
		html += '<div class="t-center" style="margin-top:65px;">'
		html += '<a class="btn btn-w100 mr20" href="javascript:;" onclick="copyModel('+d.subjectId+')">引用该份问卷</a>'
		html += '<a class="btn btn-w100 btn-gray" href="javascript:;" onclick="showModel('+d.subjectId+')">预览</a></div></div>'
   		$('#tab'+type).empty().append(html);
	}
	
	function showModel(id){//预览
		window.location.href = 'myd-wjyl.html?SubjectId='+id;
	}

	//暂时取消了该功能
	function copyModel(id){//预览
		var Service = {};
  		Service.OrgId = Commonjs.getUrlParam("HosId"); // 医院ID
  		Service.OrgName = Commonjs.getUrlParam("HosName");//医院名称
  		Service.SubjectId = id;
  		Service.Examtype = 4;//网络
  		var param = {};
  		param.reqParam = Commonjs.getApiReqParams(Service);
		Commonjs.ajax('/survey/examCopy',param,function(){
			if(_d.RespCode == 10000){
				parent.window.location.href = 'myd-wjbj.html?SubjectId='+_d.RubjectId;	
			}else{
				ComWbj.alertIconNo('提示：','添加异常','warning');
			}
		});
	}
