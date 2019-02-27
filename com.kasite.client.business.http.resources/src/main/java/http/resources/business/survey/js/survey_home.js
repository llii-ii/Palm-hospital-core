
var session = null; 
var objtype = "";
$(function(){
	//session = Commonjs.getSession();
	getURLParma();
	querySubjectsByOrgId();
	
	
});

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

//获取本医院的主题列表
function querySubjectsByOrgId(){
	var apiData = {};
	apiData.ObjType= objtype;
	apiData.HosId = Commonjs.getUrlParam("HosId");
	var param = {};
	param.api = ''; 
	param.apiParam =Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/survey/QuerySubjectsByOrgId/callApi.do';
	var d = Commonjs.ajax(url,param,function(d){
    	myLayer.clear();
		var html = '';
		if(d.RespCode == 10000){
			if(!Commonjs.isEmpty(d.Data)){
				$.each(d.Data,function(i,it){
					html += '<li onclick="location.href=Commonjs.goToUrl(\'survey_enter.html?subjectId='+it.SubjectId+'&objtype='+objtype+'\');" style="cursor: pointer;"><label class="icon"></label>'+subText(it.SubjectTitle,20,'...')+'<i class="icon serjt"></i></li>';
				});
			}
		}else{
			myLayer.alert(d.RespMessage);
		}
		$('#datalist').empty().append(html);
	});
}



function getURLParma(){
	objtype = Commonjs.getUrlParam('objtype');
	if(Commonjs.isEmpty(objtype)){
		objtype = '';
	}
}
