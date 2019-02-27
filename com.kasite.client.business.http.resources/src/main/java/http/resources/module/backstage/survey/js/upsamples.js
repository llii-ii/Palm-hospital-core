$(function(){

	
	jQuery("#getYYHZInfo").validationEngine();
	getsampleslist();
	queryThemeById();
	

});

function queryThemeById()
{
	var params={};
	$('#SubjectId').val(YihuUtil.queryString('subjectid'));
	params.SubjectId=YihuUtil.queryString('subjectid');
	$.post('/WbjUI/wbj2/business/web/wtbj_queryThemeById.do', params, function (result) {
		  str='';
	   	 if(result.Code==10000)
		 {
				if(result.SubjectTitle.length<15)
				{
	  				
	  				$('#subjectspan').html(result.SubjectTitle);
				}else
				{
					
	  				$('#subjectspan').html(result.SubjectTitle.substring(0,15)+'...');
				}
	   		
	    	 
		 }else
		 {
			 YihuUtil.dialog({
		  	 		id: 'testID',
		  	 	    content: result.Message,
		  	 	    lock: true,
		  	 	    button: [
		  	 	        {
		  	 	            name: '确定',
		  	 	            callback: function () {
		  	 	        	
		  	 	                return true;
		  	 	            },
		  	 	            focus: true
		  	 	        }

		  	 	    ]
		  	 		
		  	 	});
		 }
			 
	    }, "json");
}

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
var columns =[
              
           	{field:'username',title:'姓名',width:'15%',align:'center'},  
           	{field:'sex',title:'性别',width:'20%',align:'center',formatter:function(data,val){

          		var v='其他';
          		if(val==1)
          		{
          			v ='男';
          		}else if(val==2)
          		{
          			v = '女';
          		}else if(val==3)
          		{
          			v = '未知';
          		}
          		return v;
           	 }},
           	{field:'moblie',title:'电话',width:'20%',align:'center'},
           	{field:'deptname',title:'就诊科室',width:'20%',align:'center'},
           	{field:'status',title:'状态',width:'10%',align:'center',formatter:function(data,val){
                var v='';
          		if(val==1)
          		{
          			v ='待调查';
          		}else if(val==2)
          		{
          			v = '已发送';
          		}else if(val==3)
          		{
          			v = '呼叫中';
          		}
          		return v;
          		
           	 }},
           	{field:'opt',title:'操作',width:'25%',align:'center',formatter:function(data,val){

          		var v='<a href="javascript:;" onclick="getbj('+data.sampleid+')" class="c-007">编辑</a><a href="javascript:;"  onclick="deleteSamples('+data.sampleid+')" class="ml10 c-007">删除</a>';
          		return v;
           	 }}
           	];
	var data = {
 		 	rows:[
       	{status:1,name:'张三',deptname:'妇产科',title:'主任医师',post:'教授'},
       	{status:2,name:'张三2',deptname:'妇产科2',title:'主任医师2',post:'教授2'},
       	{status:3,name:'张三3',deptname:'妇产科3',title:'主任医师3',post:'教授3'},
       	{status:4,name:'张三4',deptname:'妇产科4',title:'主任医师4',post:'教授4'}
  	]};
 	var samplegrid = {
 		 	divid : 'samplediv',
 		 	fit: true
 	 	};
function getsampleslist()
{
	var params={};
	$('#SubjectId').val(YihuUtil.queryString('subjectid'));
	params.SubjectId=YihuUtil.queryString('subjectid');
	params.PageStart=$("#pagenumber").val()-1;
	params.PageSize=5;
	$.post('/WbjUI/wbj2/business/web/cjwj_querySamplesBySubjectid.do', params, function (result) {
	
		if(result.Code==10000)
		{
			if(result.result.length>0)
			{
				
				
				var datas={};
				var rows=result.result;
				datas.rows=rows;
				YihuUtil.loadDataGrid(samplegrid, columns, datas);
				 if(result.totalProperty>0)
				 {
					 		 
					 var pageCount=result.totalProperty/params.PageSize==parseInt(result.totalProperty/params.PageSize)?result.totalProperty/params.PageSize:parseInt(result.totalProperty/params.PageSize)+1;

					 Page(result.totalProperty, pageCount); 
				}
				  showUpDept(params.SubjectId);
				$('#haveshow').show();
				$('#nohave').hide();
			}else
			{
				$('#samplediv').empty();
				$('#haveshow').hide();
				$('#nohave').show();
			}
		}else
		{
	    	YihuUtil.dialog({
	 		id: 'testID',
	 	    content: result.Message,
	 	    lock: true,
	 	    button: [
	 	        {
	 	            name: '确定',
	 	            callback: function () {
	 	        	
	 	                return true;
	 	            },
	 	            focus: true
	 	        }

	 	    ]
	 		
	 	   });
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
			getsampleslist();
		
		}
	});
}


function goToPage(){
	var ToPage=$("#ToPage").val();
	if(ToPage!=''){
		$("#pagenumber").val(ToPage);
		getsampleslist();
	}
};
function upload(id){
    //alert($('#orgid').val());
	//showpop('alertSucc');
	openPG();
	var filename = $("#"+id).val();
	var index = filename.lastIndexOf('.');
	var type = filename.substring(index+1,filename.length);
	if(type.toLowerCase() != 'xls'){
		//closepop('alertSucc');
		closePG();
		Commonjs.alert("文件格式不对，仅支持.xls");
		return ;
	}
	var session=getSession();
	var arrID = [id];
	$.yihuUpload.ajaxFileUpload({
	 url:'/WbjUI/servlet/UploadFileServlet?param={hosid:'+ session.orgid +'}&File=true&filename='+filename+"&Api=WBJ",   //用于文件上传的服务器端请求地址
     secureuri:false,//一般设置为false
     fileElementId: arrID,//文件上传空间的id属性  <input type="file" id="file" name="file" />
     dataType: 'json',//返回值类型 一般设置为json
     success: function (data,status){

        var uri = data.Uri; 
		var name = data.NewFileName;
		var fname = data.FileName;
		var size = data.Size;
		var old = $("#"+id+"_f");
		
        //alert(uri);
        var session=getSession();
      var params={};
      params.SubjectId=$('#SubjectId').val();
      params.Path=uri;
      params.OperatorId=session.userId;
      params.OperatorName=session.operatorname;
      params.OrgId=session.orgid;
      $.ajax({url:'/WbjUI/wbj2/business/web/cjwj_upSamples.do',async:false,type:"post", data: params, success:function (result) 
     {
        if(result.Code==10000)
        {   $('#drerrordetail').empty();
        	$('#drtotal').html(result.Total);
        	$('#errornum').html(result.errTotal);
        	//var subjectid=$('#Subjectid').val();
        	if(result.errTotal>0)
        	{
            	str='';
            	str+='<table width="100%" class="tb-sjtj"> <tr><th class="thead-1">姓名</th><th class="thead-1">性别</th> <th class="thead-1">电话</th><th class="thead-1">就诊科室</th><th class="thead-1">错误原因</th> </tr>';
            	$.each(result.error,function(i,item)
            	{
            		str+='<tr>';
            		str+=' <td>'+item.username+'</td>';
  
            		str+=' <td>'+item.sexch+'</td>';
            		str+=' <td>'+item.moblie+'</td>';
            		str+=' <td>'+item.deptname+'</td>';
            		str+=' <td>'+item.errorsms+'</td>';
            	});
            	str+='</table>';
            	$('#drerrordetail').append(str);
            	//closepop('alertSucc');
            	
            	//showpop('errorDataList');
            	  YihuUtil.dialog({
            		title:'错误样本数据列表',
      	  	 		id: 'testID',
      	  	 	    width:'760px',
      	  	 	   height:'250px',
      	  	 	    content: document.getElementById('errorDataList'),
      	  	 	    lock: true,
      	  	 	    button: [
      	  	 	        {
      	  	 	            name: '确定',
      	  	 	            callback: function () {
      	  	 	        	
      	  	 	                return true;
      	  	 	            },
      	  	 	            focus: true
      	  	 	        }
    
      	  	 	    ]
      	  	 		
      	  	 	});
               
        	}
        	closePG();
        	showUpDept($('#SubjectId').val());
        	getsampleslist();
        	
        }else
        {
      	  YihuUtil.dialog({
    	  	 		id: 'testID',
    	  	 	    content: result.Message,
    	  	 	    lock: true,
    	  	 	    button: [
    	  	 	        {
    	  	 	            name: '确定',
    	  	 	            callback: function () {
    	  	 	        	
    	  	 	                return true;
    	  	 	            },
    	  	 	            focus: true
    	  	 	        }
  
    	  	 	    ]
    	  	 		
    	  	 	});
      	  closePG();
        }
    	  
      }, dataType:"json"});	
		 
 	 }
 	 ,
 	 error: function (data, status, e){
    	//alert("上传失败"); 
 		Commonjs.alert("上传失败");
    }
	 	
	});

}
//编辑弹出框
function getbj(id)
{
	jQuery("#modifySample").validationEngine();
	  var params={};
	  params.SampleId=id;
	  $.post('/WbjUI/wbj2/business/web/cjwj_querySamplesbyId.do', params, function (result) {
		  if(result.Code==10000)
		  {
			  
			  $('#bj_SampleId').val(result.SampleId);
			  $('#bj_username').val(result.UserName);
			  $("#bj_sex").find("option[value='"+result.Sex+"']").attr("selected",true);
			  $('#bj_moblie').val(result.Moblie);
			  $('#bj_deptname').val(result.DeptName);
			  $('#bj_doctorname').val(result.DoctorName);
			  $('#bj_treatres').val(result.TreatRes);
			  $('#bj_casehistoryid').val(result.CaseHistoryId);
			  $('#bj_SubjectId').val(result.SubjectId);
			  showpop('modifySample');
			  
		  }else
		  {
			  YihuUtil.dialog({
	 	  	 		id: 'testID',
	 	  	 	    content: result.Message,
	 	  	 	    lock: true,
	 	  	 	    button: [
	 	  	 	        {
	 	  	 	            name: '确定',
	 	  	 	            callback: function () {
	 	  	 	        	
	 	  	 	                return true;
	 	  	 	            },
	 	  	 	            focus: true
	 	  	 	        }

	 	  	 	    ]
	 	  	 		
	 	  	 	});
		  }
	  }, "json");
		 
}
function addSamples()
{
    $("#add_f").submit(function(){return false;}); 
    var v = jQuery("#add_f").validationEngine("validate");
    if(v == false){
    	return;
    }
    var mob=$('#moblie').val();
    mob=mob.replace("-","");
    if(mob.length<10)
    {
 	   var str='不是手机或者固话格式';
       Commonjs.alert(str);
       return;
    }
   if(!checkMobileAndPhone(mob))
   {
       var str='不是手机或者固话格式';
       Commonjs.alert(str);
 	   return;
    }
      var params={};
     var session=getSession();
     params.OperatorId=session.userId;
     params.OperatorName=session.operatorname;
     params.OrgId=session.orgid;
     params.SubjectId=$('#SubjectId').val();
     params.DeptName=$('#deptname').val();
     params.UserName=$('#username').val();
     params.Moblie=mob;
     params.Sex=$('#sex').val();
     params.DoctorName=$('#doctorname').val();
     params.TreatRes=$('#treatres').val();
     params.CaseHistoryId=$('#casehistoryid').val();
     $.post('/WbjUI/wbj2/business/web/cjwj_addSamples.do', params, function (result) {
    	 if(result.Code==10000)
    	 {
    		 getsampleslist();
    		 closepop('addSample');
    	 }else
    	 {
    		 YihuUtil.dialog({
  	  	 		id: 'testID',
  	  	 	    content: result.Message,
  	  	 	    lock: true,
  	  	 	    button: [
  	  	 	        {
  	  	 	            name: '确定',
  	  	 	            callback: function () {
  	  	 	        	
  	  	 	                return true;
  	  	 	            },
  	  	 	            focus: true
  	  	 	        }

  	  	 	    ]
  	  	 		
  	  	 	});
    	 }
     }, "json");
}
function updateSamples()
{
    $("#update_f").submit(function(){return false;}); 
    var v = jQuery("#update_f").validationEngine("validate");
    if(v == false){
    	return;
    }
    var mob=$('#bj_moblie').val();
    mob=mob.replace("-","");
    if(mob.length<10)
    {
 	   var str='不是手机或者固话格式';
       Commonjs.alert(str);
       return;
    }
   if(!checkMobileAndPhone(mob))
   {
    	   var str='不是手机或者固话格式';
           Commonjs.alert(str);
	       return;
    }
	 var params={};
     var session=getSession();
     params.OperatorId=session.userId;
     params.OperatorName=session.operatorname;
     params.OrgId=session.orgid;
     params.SubjectId=$('#onlySubjectId').val();
     params.SampleId=$('#bj_SampleId').val();
     params.DeptName=$('#bj_deptname').val();
     params.UserName=$('#bj_username').val();
     params.Moblie=mob;
     params.Sex=$('#bj_sex').val();
     params.DoctorName=$('#bj_doctorname').val();
     params.TreatRes=$('#bj_treatres').val();
     params.CaseHistoryId=$('#bj_casehistoryid').val();
     params.SubjectId=$('#bj_SubjectId').val();
     $.post('/WbjUI/wbj2/business/web/cjwj_updateSamples.do', params, function (result) {
    	 if(result.Code==10000)
    	 {
    		 getsampleslist();
    		 closepop('modifySample');
    	 }else
    	 {
    		 YihuUtil.dialog({
  	  	 		id: 'testID',
  	  	 	    content: result.Message,
  	  	 	    lock: true,
  	  	 	    button: [
  	  	 	        {
  	  	 	            name: '确定',
  	  	 	            callback: function () {
  	  	 	        	
  	  	 	                return true;
  	  	 	            },
  	  	 	            focus: true
  	  	 	        }

  	  	 	    ]
  	  	 		
  	  	 	});
    	 }
     }, "json");
}


function checkMobileAndPhone(value) {
	if(/^(((13[0-9]{1})|(15[0-9]{1})|145|147|176|177|178|(18[0-9]{1}))+\d{8})$/.test(value))
		return true;
	//if(/^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/.test(value))
	if(/^(010|02\d|0[3-9]\d{2})?\d{7,8}$/.test(value))
		return true;
	return false;
}
function deleteSamples(id)
{
	 var params={};
     params.SampleId=id;
 	YihuUtil.dialog({
 		id: 'testID',
 	    width: '245px',
 	    height: '109px',
 	    content: '确认要删除？',
 	    lock: true,
 	    button: [
 	        {
 	            name: '确定',
 	            callback: function () {
 	        	$.post('/WbjUI/wbj2/business/web/cjwj_deleteSamples.do', params, function (result) {
 	       		
 	       		if(result.Code==10000)
 	       		{
 	       	     $('#pagenumber').val(1);
 	       		 getsampleslist();
 	       	    return true;

 	       		}else
 	       		{
 	       			YihuUtil.dialog({
 	       	  	 		id: 'testID',
 	       	  	 	    content: result.Message,
 	       	  	 	    lock: true,
 	       	  	 	    button: [
 	       	  	 	        {
 	       	  	 	            name: '确定',
 	       	  	 	            callback: function () {
 	       	  	 	        	
 	       	  	 	                return true;
 	       	  	 	            },
 	       	  	 	            focus: true
 	       	  	 	        }

 	       	  	 	    ]
 	       	  	 		
 	       	  	 	});
 	       		}

 	       		 
 	           }, "json");
 	                return true;
 	            },
 	            focus: true
 	        },
 	       {
 	            name: '取消'
 	        }

 	    ]
 		
 	});
	
    
}

//去除字符串的首尾的空格
function trim(str){
return str.replace(/(^\s*)|(\s*$)/g, "");
}
function getxj()
{
	$('#username').val('');
	$('#moblie').val('');
	$('#deptname').val('');
	$('#doctorname').val('');
	$('#treatres').val('');
	$('#casehistoryid').val('');
	jQuery("#addSample").validationEngine();
	showpop('addSample');
	
}
//没有选择科室
function getnoOptiondept()
{
	$('#orderdeptname').val('');
//	if(cusselect1!=undefined)
//	{
//		cusselect1.destroy(); 
//	}
	
	cusselect1=new cusSel("cusSelChosed","cusSelList","-请选择-");
	showpop('getYYHZInfo');
}
//有选择科室
function getoptiondept(deptname)
{
	
	$('#orderdeptname').val(deptname);
	$('#DateTimeStart').val('');
	$('#DateTimeEnd').val('');
	cusselect1=new cusSel("cusSelChosed","cusSelList","-请选择-");
	showpop('getYYHZInfo');
}
//显示上传后的科室和数量
function showUpDept(subjectid)
{     
	 var params={};
     params.SubjectId=subjectid;
     //$.post('/WbjUI/wbj2/business/web/cjwj_showUpDept.do', params, function (result) {
    	 $.ajax({url:'/WbjUI/wbj2/business/web/cjwj_showUpDept.do',async:false,type:"post", data: params, success:function (result) {
    	 if(result.Code==10000)
    	 {
    		 str='';
        	 $.each(result.result,function(i,item)
        	{
        			 
        		 str+='<li><div class="fl w120">'+item.deptname+'<span class="c-org">（'+item.groupnum+'）</span></div><a href="javascript:;" onclick=getoptiondept("'+item.deptname+'") class="fr c-007">提取</a></li>';
        		 
        	});
        	 $('#showdept').empty();
        	 $('#showdept').append(str);
    	 }else
    	 {
    		 YihuUtil.dialog({
 	  	 		id: 'testID',
 	  	 	    content: result.Message,
 	  	 	    lock: true,
 	  	 	    button: [
 	  	 	        {
 	  	 	            name: '确定',
 	  	 	            callback: function () {
 	  	 	        	
 	  	 	                return true;
 	  	 	            },
 	  	 	            focus: true
 	  	 	        }

 	  	 	    ]
 	  	 		
 	  	 	});
    	 }
    	 
    	 
     }, dataType:"json"});	
}

//添加预约科室人员
function orderUp()
{   
    $("#yyhz").submit(function(){return false;}); 
    var v = jQuery("#yyhz").validationEngine("validate");
    if(v == false){
    	return;
    }
    if(!datecompare($('#DateTimeStart').val(),$('#DateTimeEnd').val()))
    {
    	 var str='开始时间不能比结束时间大';
    	 Commonjs.alert(str);
    	return;
    }
    var str=$('#orderdeptid').val();
    if(str==null||str==''||str==undefined)
    {
    	 var str='请选择科室';
    	 Commonjs.alert(str);
    	return;
    }
	var params={};
	var session=getSession();
	params.OperatorId=session.userId;
	params.OperatorName=session.operatorname;
	params.OrgId=session.orgid;
	params.SubjectId=$('#SubjectId').val();
	params.DeptId=$('#orderdeptid').val();
	params.DeptName=$('#orderdeptname').val();
	params.StartTimes=$('#DateTimeStart').val();
	params.EndTimes=$('#DateTimeEnd').val();
	params.Num=$('#num').val();
	YihuUtil.dialog({
 		id: 'testID',
 	    width: '245px',
 	    height: '109px',
 	    content: '确认要添加？',
 	    lock: true,
 	    button: [
 	        {
 	            name: '确定',
 	            callback: function () {
 	        	$.post('/WbjUI/wbj2/business/web/cjwj_orderUp.do', params, function (result) {
 	       		
 	       		if(result.Code==10000)
 	       		{
 	       		
 	       		 var s=result.fact;
 	       		 var e=result.error;
 	       		 var str='共导入'+s+'条,有错误的'+e+'条';
 	       	     Commonjs.alert(str);
 	       	     closepop('getYYHZInfo');
 	       		 getsampleslist();
 	       	    return true;

 	       		}else
 	       		{
 	       			YihuUtil.dialog({
 	       	  	 		id: 'testID',
 	       	  	 	    content: result.Message,
 	       	  	 	    lock: true,
 	       	  	 	    button: [
 	       	  	 	        {
 	       	  	 	            name: '确定',
 	       	  	 	            callback: function () {
 	       	  	 	        	
 	       	  	 	                return true;
 	       	  	 	            },
 	       	  	 	            focus: true
 	       	  	 	        }

 	       	  	 	    ]
 	       	  	 		
 	       	  	 	});
 	       		}

 	       		 
 	           }, "json");
 	                return true;
 	            },
 	            focus: true
 	        },
 	       {
 	            name: '取消'
 	        }

 	    ]
 		
 	});
}
//显示树的div
function showtree(levelobj)
{
//	var cityObj=$(YihuUtil.wid().art.dialog.list["testID1"].DOM.dialog[0]).find("#levelId");
//	var cityOffset =$(YihuUtil.wid().art.dialog.list["testID1"].DOM.dialog[0]).find("#levelId").offset();
	var cityObj = $(levelobj);
	var cityOffset = $(levelobj).offset();
	
	$("#deptdiv").css( {
		left : cityOffset.left + "px",
		top : cityOffset.top + cityObj.outerHeight() + "px"
	});
	$("#deptdiv").toggle();
	


}
/**
* 加载科室列表 左侧
* @return
*/

function loadDeptList(){
	
	//设置生成树需要的参数.
	var treeoption = {
	   		id : 'treeid',
	   		onclick : function(a){
				//alert($(a).attr("id"));
				 $("#cusSelChosed").text($(a).attr("deptName"));
				 $('#orderdeptid').val($(a).attr("id"));
				 //$("#cusSelList").hide(); 
				
	   		},
	   		//index 表示第几个 data 表示所有的结果集  val 表示当前这条记录的数据  必须有该函数
	   		formatli : function(index,data,val){
	   			var li = '<li><a href="#" id="'+val.deptID+'" deptName="'+val.deptName+'" >'+ val.deptName +'</a></li>';
	   			return li;
	   		}
	   	};
	
	var session = YihuUtil.getMainPageSession();
	
	var orgid = session.orgid;
//	
//	AuthInfo={"ClientId":"1","ClientVersion":"2","Sign":"3","SessionKey":"4"}
//	SequenceNo=20140422094353548
//	Api=CB-User.DepartmentApi.queryDepartmentListByOrgID
//	Param={'orgID':1021787,'parentDeptID':0}
//	ParamType=0
//	OutType=0
//	V=
//		
	//类型为1  加载有数量的列表.
	var param = {};
	param.Api = 'CB-User.DepartmentApi.queryDepartmentListByOrgID';
	param.Param = "{'orgID':"+ orgid +",'parentDeptID':0}";
	
	doAjaxLoadData("../web_post.do", param, function(resp) {
		 if(resp.isSuccess == true){

			var result = resp.Result;
			//结构化下数据 返回的数据 children 包含 下级树
			var treedata = initTreeData(result,"parentDeptID",0,"deptID");
			initTree(treedata,treeoption);
		 }else{ 
			 alMsg(resp.Message);
		 }
		
	});
}

/**
* 初始化树.
* @param treedata
* @param treeoption
* @return
*/
function initTree(treedata,treeoption){
	
	var htmls = '<ul>';
	
	$.each(treedata,function(index,val){
		
	var formatli = null;
	if(treeoption.formatli){
		
		formatli = treeoption.formatli;
		
	}
	
	if(formatli){
		
		var li = formatli(index,treedata,val);
		
		var children = val.children;
		
		if(children.length > 0){
			
			li += initTree(children,treeoption,true);
			
		}
		
		htmls += li;
		
	}
});
	 
	htmls += '</ul>';
	
	$("#"+treeoption.id).html(htmls);
	
$("#"+treeoption.id).SimpleTree({
       click: treeoption.onclick
});

	return htmls;
}

/**
* 生成树要用的数据类型
* 
* @param data 数据集
* @param attrpid 父级id的属性名称
* @param attrpidv  父级id的属性名称的值是多少表示改对象是父级对象
* @param id 主键属性名称
* @return
*/

function initTreeData(data,attrpid,attrpidv,id){
	
	var treedate = [];
	var chdata = {};
	$.each(data,function(index,val){
		if(val[attrpid] == attrpidv){
			treedate.push(val);
		}
		val.children = [];
		chdata[val[id]]=val;
	});
	
	$.each(data,function(index,val){
		if(val[attrpid] != attrpidv){
			if(chdata[val[attrpid]]!=undefined){
				chdata[val[attrpid]].children.push(val);                     					
			}
		}
	});
	
	return treedate;
	
}

function closepop(id){
	document.getElementById(id).style.display="none"
}
/*显示弹窗*/
function showpop(id){
	document.getElementById(id).style.display="block"
}
function getSession(){
	
	var session = YihuUtil.getSession();
	
	return session;
	
}
//比较日期
function datecompare(stattime,endtime)
{
   
    var start=new Date(stattime.replace("-", "/").replace("-", "/"));  
    var endTime=$("#endTime").val();  
    var end=new Date(endtime.replace("-", "/").replace("-", "/")); 
    if(end<start){  
        return false;  
    }  
    return true; 
}
function goreturn()
{
	
    var url="../survey/wdwj_execute.do";  
	location.href=url;
}
function cusSel(cusSelected,cusSelList,cusOption)
{
	  this.cusSelected=$("#"+cusSelected);
	  this.cusSelList=$("#"+cusSelList);
	  this.cusOption=cusOption;

	  this.init=function(){
	    if(!this.cusOption) this.cusSelected.text(this.cusSelList.find("li:first-child").text());
	    else this.cusSelected.text(this.cusOption);
	    this.cusSelList.hide();
	    //this.cusSelListClick(this.cusSelected,this.cusSelList);
	    this.cusSelectedClick(this.cusSelected,this.cusSelList);
	    loadDeptList();
	  }
	  this.cusSelectedClick=function(cusSelected,cusSelList){
	    //console.log("waawaawaw")
		  cusSelected.unbind('click');
		  cusSelected.delegate(this,"click",function(){
	      if(cusSelList.is(":visible")) cusSelList.hide();
	      else 
	      {
	    	  showtree(this);
	    	  //console.log(cusSelList.get().nodeName)
	    	  $('#cusSelList').css('display','block');
	      }
	    })
	  }
	  this.init();
//	  this.cusSelListClick=function(cusSelected,cusSelList)
//	  {
//	    cusSelList.delegate("li","click",function(e){
//	      var liClicked=$(e.target).parent("li");
//	      //console.log(liClicked)
//	      var indexLiClicked=liClicked.index();
//	      //console.log(indexLiClicked);
//	      cusSelected.text(liClicked.text());
//	      //cusSelCon.hide().eq(indexLiClicked).show().css("visibility","visible");;
//	      cusSelList.hide();
//	    })
//	  }
	}
//开启提示
function openPG(){
	YihuUtil.dialog({
		title:'消息正在加载....',
		content : '<img src="../yygh/pg1.gif"/>',
		lock : true,
		padding : 0,
		id: 'pg'
	});
}
//结束
function closePG(){
	YihuUtil.wid().art.dialog.list["pg"].close();
}

