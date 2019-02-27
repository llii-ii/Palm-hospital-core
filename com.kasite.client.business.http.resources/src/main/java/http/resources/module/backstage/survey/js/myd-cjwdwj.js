
	var session = null;
	
	$(function(){
		$('.btn').click(function(){
			var t = $('#title').val();
			if(isEmpty(t) ){
//				ComWbj.alertIconNo('提示：','字数在1～25个字之间','warning');
//				ComWbj.artTips("提示","warning","标题不能为空",1.5,null);
				ComWbj.artTips("提示","warning","标题不能为空",2,null);
				return false;
			}
			if(t.length>25 ){
//				ComWbj.alertIconNo('提示：','字数在1～25个字之间','warning');
				ComWbj.artTips("提示","warning","标题不能超过25个字",1.5,null);
				
				return false;
			}
			var type = '';
			var wayid = 4;
			$('#type').children().each(function(item){
				if(!isEmpty($(this).attr('class'))){
					type = $(this).attr('type');
				}
			});
			if(isEmpty(type)){//
//				ComWbj.alertIconNo('提示：','请选择调查对象','warning');
				ComWbj.artTips("提示","warning","请选择调查对象",2,null);
				return false;
			}
			var SurveyType = 4;//网络问卷
			var param = {};
			var Service = {};
			Service.HosId = Commonjs.getUrlParam("HosId");
			Service.OrgName = Commonjs.getUrlParam("HosName");
			Service.SubjectTitle = t;
			Service.ObjType = type;
			Service.OperatorId = Commonjs.getUserID();
	    	Service.OperatorName = Commonjs.getUserName();
			Service.ProvinceId = 1;
			Service.CityId = 1;
			Service.OrgType = 1;
			Service.SurveyType = SurveyType;
			param.reqParam = Commonjs.getReqParams(Service);
			Commonjs.ajax('/survey/addSubject.do',param,function(d){
				var dataArr = new Array();
				dataArr = d.Data;
				window.location.href = 'myd-wjbj.html?SubjectId='+dataArr[0].SubjectId;
			});
		});
	});
		
  	function _ajax(url,param,flag){
		
		var obj = null;
		try{
			$.ajax({
				type: 'POST',
				url: url,
				data: param,
				async: flag,
				timeout : 8000,
				dataType: 'json',
				success: function(data){
					obj = data;
	 			}
			});
		}catch(err){
			ComWbj.alertIconNo('提示：',err,'error');
		}
		if(!flag) return obj;
	}
	  
  
  	function isEmpty(s){
  		
  		if(s == undefined){
  			return true;
  		}else{
  			if(s == null || s == '' ||
  				s == 'null' || s.length < 1){
  				return true;
  			}
  		}
  		return false;
  	}
