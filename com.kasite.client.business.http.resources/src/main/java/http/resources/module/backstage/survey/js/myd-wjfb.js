	var session = null;
	var SubjectId = 0;
 	var Address = null;
 	var objType = '';
 	
	$(function(){
 		SubjectId = QueryString('SubjectId');
 		getSubjectType();
 		if(Commonjs.isEmpty(objType)){
 			return ;
 		}
 		if(objType!='2'){
 			//前台住院满意度调查和其他满意度调查区分开
 			objType='';
 		}
 		var param = {};
// 		param.key="SurveyPage";
 		Commonjs.ajaxSync('/survey/queryFbAddress.do',param,function(_d){
 			if(_d.RespCode == 10000){
 	  			Address = _d.Address;
 	  			getQRcode(_d.Address+'?token='+Commonjs.getToken()+'&subjectId='+SubjectId+'&objtype='+objType);
 	  		}
 		});
  		init();
	});
	
	function getSubjectType(){
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
  			var subArr = new Array();
  			subArr = _d.Data;
  			objType = subArr[0].Objtype;
  		});
	}
	
	function QueryString(val) {
		var uri = window.location.search;
		var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
		return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
	}
	
	//初始化 
	function init(){
		  $('#fe_text').text(Address+'?subjectId='+SubjectId+'&objtype='+objType);
		  $('#text').text(Address+'?subjectId='+SubjectId+'&objtype='+objType);
		  $('#copyBtn').attr("data-clipboard-text",Address+'?subjectId='+SubjectId+'&objtype='+objType);
		  
	    
//		
		  
		  var clipboard1 = new Clipboard('#copyBtn');   
		   clipboard1.on('success', function(e) {  
		       console.log(e);  
		        alert("复制成功！") ;
		        });  
		   clipboard1.on('error', function(e) {  
		               console.log(e);  
		   			ComWbj.artTips("提示","error","复制失败！请手动复制",2,null);
		        });  
	}
	
	//获取发布地址
  	function getFbAddress(){
  		var param = {};
  		Commonjs.ajax('/survey/queryFbAddress.do',param,function(_d){
  			if(_d.RespCode == 10000){
  	  			Address=_d.Address;
  	  		}
  		});
  	}
  	
  	//生成二维码
  	function getQRcode(URL){
  		var param = {};
  		//param.HosId = Commonjs.getUrlParam("HosId");
  		param.SubjectId = SubjectId;
  		param.URL = URL;
  		Commonjs.ajaxSync('/survey/createQRCode.do',param,function(_d){
  			if(_d.RespCode != 10000){
  				ComWbj.artTips("提示","error",_d.RespMessage,2,null);
  	  		}else{
  	  			$("#img-buffer").attr("src",location.protocol + '//' + location.host + "/" + _d.QRUrl);
  	  			var c = document.getElementById("canvas");
  	  			var ctx = c.getContext("2d");
  	  			var img = document.getElementById("img-buffer");
  	  			ctx.drawImage(img,0,0);
  	  		}
  		});
  	}
  	
	 
	//获取问卷数据
  	function getWJData(){
  		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = SubjectId;
		param.reqParam = Commonjs.getApiReqParams(Service);
  		Commonjs.ajax('/survey/querySubjectById.do',param,null);
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
			ComWbj.artTips("提示","error",err,2,null);
//			ComWbj.alertIconNo('提示：',err,'error');
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
                 
 
