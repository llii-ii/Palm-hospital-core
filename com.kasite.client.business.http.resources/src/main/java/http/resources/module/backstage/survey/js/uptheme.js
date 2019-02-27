$(function(){

	validate();
	init();
	
	

});


function validate()
{

	   var v =$("#form_uzt").validationEngine("attach",{     promptPosition:"bottomLeft",     scroll:false },{
			'custom_error_messages':{
					// 修改错误提示信息

				'#SubjectTitle':{
					"maxSize":{
						"message":"最多25汉字"
					}
				},
				'#Remark':{
					"maxSize":{
						"message":"最多100汉字"
					}
				}

			}
		});

}
function init()
{
	var params={};
	params.SubjectId=YihuUtil.queryString('subjectid');
	$.post('/WbjUI/wbj2/business/web/cjwj_querySubjectById.do', params, function (result) {
		if(result.Code==10000)
		{   $('#SubjectId').val(params.SubjectId);
			$('#SubjectTitle').val(result.SubjectTitle);
			$('#Quantity').val(result.Quantity);
			$('#Remark').val(result.Remark);
	        var objtype=result.ObjType;	
	        var SurveyType=result.SurveyType;	
	        $("#ObjType").find("option[value='"+objtype+"']").attr("selected",true);
	        $("#SurveyType").find("option[value='"+SurveyType+"']").attr("selected",true);
		}else
		{
			Commonjs.alert(result.Message);
		}
	 }, "json");	
}
function updatetheme()
{
    $("#form_uzt").submit(function(){return false;}); 
    var v = jQuery("#form_uzt").validationEngine("validate");
    if(v == false){
    	return;
    }
	var params={};
	var session=getSession();
	params.SubjectId=$('#SubjectId').val();
	params.SubjectTitle=$('#SubjectTitle').val();
	params.Quantity=$('#Quantity').val();
	params.Remark=$('#Remark').val();
	params.ObjType=$('#ObjType').val();
	params.SurveyType=$('#SurveyType').val();
    params.OperatorId=session.userId;
    params.OperatorName=session.operatorname;
	$.post('/WbjUI/wbj2/business/web/cjwj_updatetheme.do', params, function (result) {
		if(result.Code==10000)
		{  
			Commonjs.alert(result.Message,"add");
		}else
		{
			Commonjs.alert(result.Message);
		}
	 }, "json");	
}

function alertMessage(msg,action,icon){
	
	YihuUtil.dialog({
		title:"提示",
		content: msg,
		icon:icon,
		button : [{
				name : '确定',
				callback : action 
			}]
	});
}
function goreturn()
{
	
    var url="../survey/wdwj_execute.do";  
	location.href=url;
}
function getSession(){
	
	var session = YihuUtil.getSession();
	
	return session;
	
}