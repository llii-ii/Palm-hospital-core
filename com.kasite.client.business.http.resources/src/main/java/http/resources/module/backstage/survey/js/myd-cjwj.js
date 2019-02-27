
	var session = null;
	
	$(function(){
		queryWJData();
	});
	
	function queryWJData(){
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		var page = {};
		page.PIndex = 1;
		page.PSize = 10;
		Service.Page = page;
		var param = {};
		param.reqParam = Commonjs.getApiReqParams(Service);
		Commonjs.ajaxSync('/survey/querySubjectlistByOrgId.do',param,function(d){
			 if(d.RespCode == 10000 && d.Page.PCount > 0){
				window.location.href = 'myd-wjlb.html';
			 }else{
//				 ComWbj.alertIconNo('提示：',d.RespMessage,'error'); 
				$("#mainV").css("display","");
			}
		 });
	}
	  	
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
