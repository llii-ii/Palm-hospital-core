$(function(){

	
	
	
	querySubjectlistByOrgId();

});


var columns =[
              
//             	{field:'surveytype',title:'调查类型',width:'10%',align:'center',formatter:function(data,val){
//                    var v='';
//            		if(val==1)
//            		{
//            			v='电话调查';
//            		}
//            		if(val==2)
//            		{
//            			v='短信调查';
//            		}
//            		if(val==3)
//            		{
//            			v='短信调查';
//            		}
//            		return v;
//             	 }},  
             	{field:'subjecttitle',title:'问卷标题',width:'20%',align:'center'},
             	{field:'objtype',title:'调查对象',width:'15%',align:'center',formatter:function(data,val){
  
             		var v='';
             		if(val==1)
             		{
             			v='门诊患者';
             		}else if(val==2)
             		{
             			v='住院患者';
             		}else if(val==3)
             		{
             			v='手术患者';
             		}else if(val==4)
             		{
             			v='体检客户';
             		}else if(val==5)
             		{
             			v='通用类型';
             		}
            		return v;
             	 }},
             	{field:'shez',title:'调查需求设置',width:'15%',align:'center',formatter:function(data,v){
      			   var sb="";
    			   if(data.status==0)
    			    sb='无效';
    			   else if(data.status==1)
    			      sb="<div  class='lk-007'><a href='#' style='padding:5px;'" +" onclick ='edittheme("+data.subjectid+")'>修改设置</a></div>";
    			   else if(data.status=2)
    			    sb='<div  class="lk-007"><a href="/WbjUI/wbj2/business/survey/myd-showtheme.html?subjectid='+data.subjectid+'" >查看设置</a></div>';
    			   else if(data.status==3)
    			    sb='<div  class="lk-007"><a href="/WbjUI/wbj2/business/survey/myd-showtheme.html?subjectid='+data.subjectid+'" >查看设置</a></div>';
    				return sb;
             	 }},
             	{field:'orgsample',title:'样本导入',width:'10%',align:'center',formatter:function(data,v){
         		    if(data.status<2)
    			    {
    			      return v==0?'<div  class="lk-007"><a href="/WbjUI/wbj2/business/survey/myd-upsamples.html?subjectid='+data.subjectid+'" >未导入</a></div>':'<div  class="lk-007"><a href="/WbjUI/wbj2/business/survey/myd-upsamples.html?subjectid='+data.subjectid+'" >已导入'+v+'人</a></div>';
    			    }
    			    else
    			    {
    			      return v==0?'未导入':'已导入'+v+'人'; 
    			    }
             	 }},
             	{field:'sheji',title:'问卷设计',width:'10%',align:'center',formatter:function(data,v){
    				 var sb="";
    				   if(data.status==0)
    				    sb='无效';
    				   else if(data.status==1)
    				      sb='<div  class="lk-007"><a href="/WbjUI/wbj2/business/survey/myd-upquest.html?subjectid='+data.subjectid+'" >修改问卷</a>';
    				   else if(data.status=2)
    				    sb= '<div  class="lk-007"><a href="/WbjUI/wbj2/business/survey/myd-showquest.html?subjectid='+data.subjectid+'" >查看问卷</a></div>'
    				   else if(data.status==3)
    				   {
    				   sb= '<div  class="lk-007"><a href="/WbjUI/wbj2/business/survey/myd-showquest.html?subjectid='+data.subjectid+'" >查看问卷</a></div>'
    				   }
    					return sb;
             	 }}
//             	{field:'status',title:'操作',width:'30%',align:'center',formatter:function(data,v){
//        			var sb='';
//    				if(v==0)
//    					sb='无效';
//    				else if(v==1)
//    					sb='未开始';
//    				else if(v==2)
//    					sb='调查中';
//    				else if(v==3)
//    					sb='已完成';
//    				return sb;
//             	 }}
             	];
	var data = {
   		 	rows:[
         	{status:1,name:'张三',deptname:'妇产科',title:'主任医师',post:'教授',sheji:'未设计'},
         	{status:1,name:'张三',deptname:'妇产科',title:'主任医师',post:'教授',sheji:'未设计'},
         	{status:1,name:'张三',deptname:'妇产科',title:'主任医师',post:'教授',sheji:'未设计'},
         	{status:1,name:'张三',deptname:'妇产科',title:'主任医师',post:'教授',sheji:'未设计'},
         	{status:1,name:'张三',deptname:'妇产科',title:'主任医师',post:'教授',sheji:'未设计'}
         
    	]};
   	var grid = {
   		 	divid : 'mydiv',
   		 	fit: true
   	 	};

   	
    function edittheme(id)
    {
        var url="/WbjUI/wbj2/business/survey/myd-uptheme.html?subjectid="+id;
    	location.href=url; 	
    }
function querySubjectlistByOrgId()
{
	var params={};
    var session=getSession();
  	params.OrgId=session.orgid;
  	params.pageIndex=$("#pagenumber").val()-1;
	params.PageSize=10;
    $.post('../WbjUI/wbj2/business/web/wdwj_querySubjectlistByOrgId.do', params, function (result) {
   	 if(result.Code==10000)
   	 {
			var datas={};
			var rows=result.result;
			datas.rows=rows;
			YihuUtil.loadDataGrid(grid, columns, datas);
			 
			 var pageCount=result.totalProperty/params.PageSize==parseInt(result.totalProperty/params.PageSize)?result.totalProperty/params.PageSize:parseInt(result.totalProperty/params.PageSize)+1;

			 Page(result.totalProperty, pageCount); 
   	 }
   	 else
   	 {
   		Commonjs.alert(result.Message);
   	 }
	}, "json");	
}

//加载分页
function Page(totalcounts, pagecount) {
	$("#pager").pager( {
		totalcounts : totalcounts,
		pagesize : 5,
		pagenumber : $("#pagenumber").val(),
		pagecount : pagecount,
		buttonClickCallback : function(a) {
			$("#pagenumber").val(a);
			querySubjectlistByOrgId();
		
		}
	});
}


function goToPage(){
	var ToPage=$("#ToPage").val();
	if(ToPage!=''){

		$("#pagenumber").val(ToPage);
		querySubjectlistByOrgId();
	}
};
function alertMessage(msg,action){
	
	YihuUtil.dialog({
		title:"提示",
		content: msg,
		button : [{
				name : '确定',
				callback : action 
			}]
	});
}
function getSession(){
	
	var session = YihuUtil.getSession();
	
	return session;
	
}