$(function(){

	
	getCore();
	
	

});



var fl1=0;
var fl2=0;
var fl3=0;
var fl4=0;
var fl5=0;
var fl6=0;
var fl7=0;
var fl8=0;
var fl9=0;
var fl10=0;
var fl11=0;
var fl12=0;
var fl13=0;

function getnewquestion()
{
	
   showpop('addQuestion');
   juzhong();
   
}
//居中
function juzhong()
{
	/*设置弹窗垂直居中*/
	popbox=$(".popbox");
	for(i=0;i<popbox.length;i++)
	{
		popbox.eq(i).css("margin-top",function(){
			return "-"+$(this).height()/2+"px";
		})
	}
}

function querySubjectsByOrgId()
{
	
	var params={};
    var session=getSession();
  	params.OrgId=session.orgid;
	$.post('/WbjUI/wbj2/business/web/wtbj_querySubjectsByOrgId.do', params, function (result) {
		  var str='<option value="">-请选择-</option>';
	   	 if(result.Code==10000)
		 {
	   		 $.each(result.Result,function(i,item)
	   		 {
	   			 str+='<option value='+item.SubjectId+'>'+item.SubjectTitle+'</option>';
	   		 })
	   		$('#myzoneselect').empty();
	   		$('#myzoneselect').append(str);
	    	 
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
//获取平台跟题库
function  getquestroom()
{
	querySubjectsByOrgId();
	myZone();
	queryPlatformQuestion();
	showpop('chooseQuestion');
	var $popTabMenu1=$("#popTabMenu1");
	var $popTabCon1=$("#popTabCon1");
	$popTabMenu1.find("li").removeClass("on").end().find("li:first-child").addClass("on");
	$popTabCon1.find(".pop-tab-con").hide().end().find(".pop-tab-con:first-child").show();
	$popTabMenu1.delegate("li","click",function(){
		var i=$(this).index();
		$popTabMenu1.find("li").removeClass("on");
		$(this).addClass("on");
		$popTabCon1.find(".pop-tab-con").hide().end().find(".pop-tab-con:eq("+i+")").show();
	})

    juzhong();


	
	
}
//设置选项不同是的颜色变化
function SetContentType(id)
{
	$("#ContentTypetd").find("a").removeClass("c-007");
	$("#ContentTypetd").delegate("a","click",function(){
		$(this).addClass("c-007");
	})
	$("#QContentType").val(id);
	queryPlatformQuestion();
}
function SetObjType(id)
{
	$("#ObjTypetd").find("a").removeClass("c-007");
	$("#ObjTypetd").delegate("a","click",function(){
		$(this).addClass("c-007");
	})
	$("#QObjType").val(id);
	queryPlatformQuestion();
}

function delQuest(id)
{
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
 	       	var params={};
 	     	params.QuestId=id;
 	       $.post('/WbjUI/wbj2/business/web/wtbj_delQuest.do', params, function (result) {
 	      	 if(result.Code==10000)
 	      	 {
 	      		 getCore();
 	      	 }
 	      	 else
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
function updateSubjectend(obj)
{
	
    if(uniteMessage($(obj).html(),200))
    {
    	return;
    }
  	var params={};
  	var session=getSession();
  	params.SubjectId=$('#SubjectId').val();
  	params.EndingIntro=$(obj).html();
    params.OperatorId=session.userId;
    params.OperatorName=session.operatorname;
	$.post('/WbjUI/wbj2/business/web/wtbj_updateSubjectend.do', params, function (result) {
	   	 if(result.Code==10000)
		 {
	   		$('#end').append(result.EndingIntro);
	   		$(obj).removeClass('cont-editable').attr('contenteditable',false);
	    	 
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
//暂时没有用到
function updateSubjectbegin(obj)
{
    if(uniteMessage($(obj).html(),200))
    {
    	return;
    }
  	var params={};
  	var session=getSession();
  	params.SubjectId=$('#SubjectId').val();
  	params.BeginIntro=$(obj).html();
    params.OperatorId=session.userId;
    params.OperatorName=session.operatorname;
	$.post('/WbjUI/wbj2/business/web/wtbj_updateSubjectbegin.do', params, function (result) {
	   	 if(result.Code==10000)
		 {
	   		$('#begin').append(result.BeginIntro);
	   		$(obj).removeClass('cont-editable').attr('contenteditable',false);
	    	 
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
function updateQuest(obj,id)
{
    if(uniteMessage($(obj).html(),30))
    {
    	return;
    }
  	var params={};
  	var session=getSession();
  	params.QuestId=id;
  	params.Question=$(obj).html();
    params.OperatorId=session.userId;
    params.OperatorName=session.operatorname;
	$(obj).removeClass('tit-editable').attr('contenteditable',false);
	$.post('/WbjUI/wbj2/business/web/wtbj_updateQuest.do', params, function (result) {
	   	 if(result.Code==10000)
		 {
	   		getCore();
	    	 
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
function upateItem(obj,id)
{
    if(uniteMessage($(obj).html(),30))
    {
    	return;
    }
  	var params={};
  	var session=getSession();
  	params.ItemId=id;
  	params.ItemCont=$(obj).html();
    params.OperatorId=session.userId;
    params.OperatorName=session.operatorname;
	$(obj).removeClass('answer-editable').attr('contenteditable',false);
	$.post('/WbjUI/wbj2/business/web/wtbj_upateItem.do', params, function (result) {
	   	 if(result.Code==10000)
		 {
	   		getCore();
	    	 
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

function getNewItem(id)
{
   $('#'+id).show();	
}
function newchilditem(id)
{
   $('#'+id).show();	
}
function addItem(obj,id)
{
	if(uniteMessage($(obj).parent().find("input").val(),30))
    {
    	return;
    }
	var params={};
	var session=getSession();
	params.QuestId=id;
	params.ItemCont=$(obj).parent().find("input").val();
    params.OperatorId=session.userId;
    params.OperatorName=session.operatorname;
	$.post('/WbjUI/wbj2/business/web/wtbj_addItem.do', params, function (result) {
	   	 if(result.Code==10000)
		 {
	   		getCore();
	    	 
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
function dellogical(obj,id)
{
	if(id==null||id=="")
	{
		$(obj).parent().remove();
		return;
	}
	if(id!=null||id!="")
	{
		var params={};
		var session=getSession();
		params.ItemId=id;
	    params.OperatorId=session.userId;
	    params.OperatorName=session.operatorname;
		$.post('/WbjUI/wbj2/business/web/wtbj_delQuestionFlow.do', params, function (result) {
		   	 if(result.Code==10000)
			 {
		   		$(obj).parent().remove();
		    	 
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
}
function getlogicalSetting(obj,Questid)
{   
	
	$('#logicalQuestid').val(Questid);
	// 清除缓存 
	$("div").removeData("keyitem");
	$("div").removeData("keyvalue");
	$('#jumpdiv').empty();
    $('#jumpbutton').empty();
    getquestItemforlogical(Questid);
    getnextQuestforlogical(obj);
	showpop('logicalSetting');
	//alert($(obj).parent().parent().parent().find("span:first").html());
	//alert($(obj).parent().parent().parent().attr("sortquestid"));
	var Qparams={};
	
	

	Qparams.QuestId=Questid;
	str='';
	$.ajax({url:'/WbjUI/wbj2/business/web/wtbj_queryQuestionFlow.do',async:false,type:"post", data: Qparams, success:function (result) {
		 if(result.Code==10000)
		 {
				$.each(result.Result,function(i,item)
				{
				   str+='<div  class="mt25"><label style="width:45px;">选中：</label>';
				   str+='<select onchange="nextQuest()" class="pop-sel" style="width:140px">';
				   var p={};
				   p.QuestId=Questid;
				 
				  $.ajax({url:'/WbjUI/wbj2/business/web/wtbj_queryQuestionItem.do',async:false,type:"post", data: Qparams, success:function (result) {
					   if(result.Code==10000)
					   {
						   $.each(result.Result,function(k,it)
						   {
							  
							   //跳转选项
							   if(item.ItemId==it.ItemId)
							   {
								   str+='<option selected value='+it.ItemId+'>'+it.ItemCont+'</option>';
							   }else
							   {
								   str+='<option value='+it.ItemId+'>'+it.ItemCont+'</option>';
							   }
							 
						   });

					   }
					   else
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
				   str+='</select>';
				   str+='<label style="width:60px;">跳转至：</label>';
				   str+='<select class="pop-sel" style="width:140px">';   
				   var divs=$(obj).parent().parent().parent().parent().children('div'),
			       len=$(obj).parent().parent().parent().parent().children('div').length;
			       objdiv=$(obj).parent().parent().parent(),
			       objindex=divs.index(objdiv);
			       for(var m=0;m<len;m++)
			       {
			    	   if(m>objindex)
			    	   {
			    		  
			    		   //跳转问题
			    		   if(item.NextQuestId==divs.eq(m).attr("sortquestid"))
			    		   {
			    			   str+='<option selected value='+divs.eq(m).attr("sortquestid")+'>'+divs.eq(m).find("span:first").html()+'</option>';
			    		   }
			    		   else
			    		   {
			    			   str+='<option value='+divs.eq(m).attr("sortquestid")+'>'+divs.eq(m).find("span:first").html()+'</option>';
			    		   }
			    		   
			    	   }
			       }
			       if(item.NextQuestId=="10")
			       {
			    	   str+='<option selected value="10">结束(计入结果)</option>';
			       }else
			       {
			    	   str+='<option value="10">结束(计入结果)</option>';
			       }
			       if(item.NextQuestId=="11")
			       {
			    	   str+=' <option selected value="11">结束(不计入结果)</option>';
			       }else
			       {
			    	   str+=' <option value="11">结束(不计入结果)</option>';  
			       }
			       
			      
			   
			   
			   str+='</select><a class="ml10" onclick=dellogical(this,"'+item.ItemId+'") href="javascript:;"><img  src="../../common/themes/default/images/nav-gb.png" /></a></div>';
					
				
				
				   
				  
				})
			$('#jumpdiv').append(str);				
			$('#jumpbutton').append('<div class="mt25"><a href="javascript:;" onclick=getJump() class="c-007">添加跳转条件</a></div>');
			juzhong();
				
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
//获取逻辑跳转的前面选项
function getquestItemforlogical(Questid)
{   
	var itemQ='';
	var p={};
	   p.QuestId=Questid;
	   $.post('/WbjUI/wbj2/business/web/wtbj_queryQuestionItem.do', p, function (result) {
		   if(result.Code==10000)
		   {
			   $.each(result.Result,function(i,it)
			   {
				   itemQ+='<option value='+it.ItemId+'>'+it.ItemCont+'</option>';
			   });
			  // alert(itemQ);
			   $("div").data("keyitem",itemQ);
			   var a='aa';
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
//获取逻辑跳转的后面选项
function getnextQuestforlogical(obj)
{   
	   var valueQ='';
	   var divs=$(obj).parent().parent().parent().parent().children('div'),
       len=$(obj).parent().parent().parent().parent().children('div').length;
       objdiv=$(obj).parent().parent().parent(),
       objindex=divs.index(objdiv);
       for(var m=0;m<len;m++)
       {
    	   if(m>objindex)
    	   {
    		   valueQ+='<option value='+divs.eq(m).attr("sortquestid")+'>'+divs.eq(m).find("span:first").html()+'</option>';
    		   
    	   }
       }
       valueQ+='<option value="10">结束(计入结果)</option><option value="11">结束(不计入结果)</option>';
      // alert(valueQ);
       $("div").data("keyvalue",valueQ);
		   
}
//显示逻辑跳转页面
function getJump()
{
	if($("div").data("keyitem")==null||$("div").data("keyitem")==undefined)
	{
		
	}
	str='';
	str+='<div class=""><label style="width:45px;">选中：</label>';
	str+='<select class="pop-sel" style="width:140px">';
	str+=$("div").data("keyitem");
	str+='</select>';
	str+='<label style="width:60px;">跳转至：</label>';
	str+='<select class="pop-sel" style="width:140px">';
	str+=$("div").data("keyvalue")
	str+='</select><a class="ml10" onclick=dellogical(this,"") href="javascript:;"><img  src="../../common/themes/default/images/nav-gb.png" /></a></div>';
	$("#jumpdiv").append(str);
	
}
//添加逻辑跳转
function addJump()
{

	var divs=$("#jumpdiv").children('div');	
	var len=$("#jumpdiv").children('div').length;
    var ids = [];
    var tag=false;
	for(var i=0;i<len;i++)
	{
		ids.push(divs.eq(i).find("select:first").val());
	}
	var str=ids.join(",")+",";
	for(var i = 0; i < ids.length; i++)
{
   if(str.replace(ids[i] + ",", "").indexOf(ids[i] +",") > -1)
    {
	   tag=true;
       break;
	    
    }
}
   if(tag)
   {  
	   var msg="选项有重复!!"
	   Commonjs.alert(msg);
	   return;
   }
	for(var i=0;i<len;i++)
	{
		var delparams={};
		delparams.ItemId=divs.eq(i).find("select:first").val();
		
			$.ajax({url:'/WbjUI/wbj2/business/web/wtbj_delQuestionFlow.do',async:false,type:"post", data: delparams, success:function (result) {
			if(result.Code==10000)
			{
				
			}
			else
			   
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
		for(var i=0;i<len;i++)
	{
		//alert(divs.eq(i).find("select:eq(0)").val());
		//alert(divs.eq(i).find("select:eq(1)").val());
		   var p={};
		   var session=getSession();
		   p.ItemId=divs.eq(i).find("select:eq(0)").val();
		   p.NextQuestId=divs.eq(i).find("select:eq(1)").val();
		   p.OperatorId=session.userId;
		   p.OperatorName=session.operatorname;
		  
		 $.ajax({url:'/WbjUI/wbj2/business/web/wtbj_addQuestionFlow.do',async:false,type:"post", data: p, success:function (result) {
			   if(result.Code==10000)
			   {
				 
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
		   },dataType:"json"});	
	}
	 getCore();
	 closepop('logicalSetting');
}
//增加一行
function addMatrQuest(obj,id)
{
    if(uniteMessage($(obj).parent().find("input").val(),30))
    {
    	return;
    }
	var Qparams={};
	Qparams.QuestId=id;
	$.post('/WbjUI/wbj2/business/web/wtbj_queryQuestion.do', Qparams, function (result) {
		if(result.Code==10000)
		{
			var params={};
			var session=getSession();
			params.SubjectId=result.SubjectId;
			params.ObjType=result.ObjType;
			params.ContentType=result.ContentType;
			params.QuestType=result.QuestType;
			params.MatrixQuestId=id;
			params.Question=$(obj).parent().find("input").val();
		    params.OperatorId=session.userId;
		    params.OperatorName=session.operatorname;
			var a= $(obj).parent().parent().find('table').first().find('tbody').find('tr').first().find('td').length;
		  	var b=$(obj).parent().parent().find('table').first().find('tbody').find('tr').first().find('td');
		    $.post('/WbjUI/wbj2/business/web/wtbj_addQuestion.do', params, function (result) {
			   	 if(result.Code==10000)
				 {
			   		for(var i=1;i<a;i++)
			   		{
			   			var par={};
						par.QuestId=result.QuestId;
						par.ItemCont=b.eq(i).find("span").html();
						par.OperatorId=session.userId;
						par.OperatorName=session.operatorname;
						 $.post('/WbjUI/wbj2/business/web/wtbj_addItem.do', par, function (result) {
							 if(result.Code==10000)
							 {
								 
							 }
							 else
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
			   		
			   		getCore();
			    	 
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
  //增加一列
  function addMatrItem(obj)
{
    if(uniteMessage($(obj).parent().find("input").val(),30))
    {
    	return;
    }
    var t= $(obj).parent().parent().find('table').first().find('tr');
    var a= $(obj).parent().parent().find('table').first().find('tr').length;
    for(var i=1;i<a;i++)
    {
    	var params={};
    	var session=getSession();
    	params.QuestId=t.eq(i).find('td:first').find("a[quesid]").attr("quesid");
    	params.ItemCont=$(obj).parent().find("input").val();
        params.OperatorId=session.userId;
        params.OperatorName=session.operatorname;
        $.ajax({url:'/WbjUI/wbj2/business/web/wtbj_addItem.do',async:false,type:"post", data: params, success:function (result) {
        	 if(result.Code==10000)
        	 {
        		 
        	 }
        	 else
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
    getCore();
}
  //删除一列
function delcol(v)
{
		var trlen=$(v).parent().parent().parent().find('tr').length;
		var trlist=$(v).parent().parent().parent().find('tr');
	   	var thistd=$(v).parent();
	    var tdlist=$(v).parent().parent().find('td');
	    _tdindex=tdlist.index(thistd);
	    for (var i = 0; i < trlen; i++) 
	    {
		    if(i>0)
		    {
		     var itemid=trlist.eq(i).find("td").eq(_tdindex).find("input[itemid]").attr("itemid");
		   
	        // trlist.eq(i).find("td").eq(_tdindex).remove();
			 var params={};
			 	
		 	 params.ItemId=itemid;
		     $.post('/WbjUI/wbj2/business/web/wtbj_deleteQuestionItem.do', params, function (result) {
		    	 if(result.Code==10000)
		    	 {
		    		
		    	 }
		    	 else
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
		    
		  
		 
		  
	 }
	 getCore();
	  
}
//删除一行
  function delrow(v)
 {
	  var trlen=$(v).parent().parent().parent().find('tr').length;
	  if(trlen<=2)
	  {
		  var msg="至少得有一项";
		  Commonjs.alert(msg);
		  return;
	  }
	
	 var quesid=$(v).parent().find("a[quesid]").attr("quesid");
//	 alert(quesid);
//	 $(v).parent().parent().remove();
	 var params={};
 	
 	 params.QuestId=quesid;
     $.post('/WbjUI/wbj2/business/web/wtbj_deleteQuestion.do', params, function (result) {
    	 if(result.Code==10000)
    	 {
    		 getCore();
    	 }
    	 else
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
  function delMatrixQuest(id)
  {
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
   	       	var params={};
   	     	params.QuestId=id;
   	       $.post('/WbjUI/wbj2/business/web/wtbj_matdeleteQuestion.do', params, function (result) {
   	      	 if(result.Code==10000)
   	      	 {
   	      		 getCore();
   	      	 }
   	      	 else
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
function Qup(obj,id)
{
	var _divs=$(obj).parent().parent().parent().parent().children("div"),
		_div=$(obj).parent().parent().parent(),
		_divindex = _divs.index(_div);
	if(_divindex>0)
	{
		    var session=getSession();
			var params={};
			params.OperatorName=session.operatorname;
			params.OperatorId=session.userId;
		  	params.QuestId1=id;
		  	params.QuestId2= _divs.eq(_divindex-1).attr("sortquestid");
		    $.post('/WbjUI/wbj2/business/web/wtbj_exchangeQuestionSort.do', params, function (result) {
		   	 if(result.Code==10000)
		   	 {
		   		 getCore();
		   	 }
		   	 else
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
		
	
}
function Qdown(obj,id)
{
	var _divs=$(obj).parent().parent().parent().parent().children("div"),
	_div=$(obj).parent().parent().parent(),
	_divindex = _divs.index(_div);
		if(_divindex<_divs.length-1)
		{
	    	var session=getSession();
			var params={};
			params.OperatorName=session.operatorname;
			params.OperatorId=session.userId;
			params.QuestId1=id;
		  	params.QuestId2= _divs.eq(_divindex+1).attr("sortquestid");
		    $.post('/WbjUI/wbj2/business/web/wtbj_exchangeQuestionSort.do', params, function (result) {
		   	 if(result.Code==10000)
		   	 {
		   		 getCore();
		   	 }
		   	 else
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
}
function delItem(id)
{
	var params={};
  	params.ItemId=id;
    $.post('/WbjUI/wbj2/business/web/wtbj_deleteQuestionItem.do', params, function (result) {
   	 if(result.Code==10000)
   	 {
   		 getCore();
   	 }
   	 else
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

function hiddenItem(obj,id)
{   
	$(obj).parent().find("input:first").val('');
	closepop(id);
}


function getCore()
{      

     fl1=0;
	 fl2=0;
	 fl3=0;
	 fl4=0;
	 fl5=0;
	 fl6=0;
	 fl7=0;
	 fl8=0;
	 fl9=0;
	 fl10=0;
	 fl11=0;
	 fl12=0;
	 fl13=0;
	 $('#zhibiaoul').empty();
	$('#core').empty();
    $('#begin').empty();
    $('#end').empty();
    var id=YihuUtil.queryString('subjectid');
    $('#SubjectId').val(id);
  	var params={};
	params.SubjectId=id;
  	$.post('/WbjUI/wbj2/business/web/wtbj_querySubjectById.do', params, function (result) {
  		if(result.Code==10000)
		{
  			$('#begin').append(result.BeginIntro);
			$('#end').append(result.EndingIntro);
			if(result.SubjectTitle.length<15)
			{
  				$('#subjecttitle').html(result.SubjectTitle);
  			
			}else
			{
				$('#subjecttitle').html(result.SubjectTitle.substring(0,15)+'...');
  			
			}
			
  			if(result.Result.length>0)
  			{
  				
  				var str='';
  	  			$.each(result.Result,function(i,item)
  	  			{
  	  					
  	  			    countContentType(item.ContentType);
  	  				if(item.QuestType==1)
  	  				{
  	  					var count=i+1;
  	  					str+='<div SortQuestId="'+item.QuestId+'" class="myd-box-gray mt15"> <div class="pl15 myd-gray-tit relative hidden"><strong class="fl"><span>Q'+count+'</span><span class="c-org">【'+zhibiao(item.ContentType)+'】</span></strong>';
  	  					str+='<span id="q'+count+'Tit" class=" fl q-tit" contenteditable="false" onblur=updateQuest(this,"'+item.QuestId+'")>'+item.Question+'</span>';
  	  				    str+='<div class="absolute" style="right:13px; top:8px; font-size:0;"><a href="javascript:;" onclick=Qup(this,"'+item.QuestId+'") class="sort-up"></a><a href="javascript:;"  onclick=Qdown(this,"'+item.QuestId+'") class="ml10 sort-down"></a><a href="javascript:;" class="ml10 btn-edit" onclick="$(\'#q'+count+'Tit\').attr(\'contenteditable\',true).focus().select().addClass(\'tit-editable\')"></a></div></div>';
  	  				    str+='<div class="myd-gray-con"><div class="mt5"><ul>';
  	  				     $.each(item.SvQuestionItems,function(i,qitem)
  	  				     {
  	  				    	
  	  				        str+='<li><span class="valign-m w100"><input type="radio" name="q2answer" class="valign-m" style="width:13px;" /><span id="item'+qitem.ItemId+'" class="answer-txt" contenteditable="false"';
  	  				        str+='onblur=upateItem(this,"'+qitem.ItemId+'")>'+qitem.ItemCont+'</span></span>';
  	  				        
  	  				       str+='<a href="javascript:;" class="ml25 btn-edit" onclick="$(\'#item'+qitem.ItemId+'\').attr(\'contenteditable\',true).focus().select().addClass(\'answer-editable\')"></a> <a class="ml10" onclick=delItem("'+qitem.ItemId+'") href="javascript:;"><img  src="../../common/themes/default/images/nav-gb.png" /></a></li>';
                           
  	  				     })
  	  				    str+=' </ul></div>';
  	  				   str+='<div  id="newitem'+item.QuestId+'" style="display:none" class="mt15"><input type="text" class="inp-txt" style="width:300px;" placeholder="请输入选项内容" /><input type="button" class="ml10 btn-blue" onclick=addItem(this,'+item.QuestId+') value="确认" /><a href="javascript:;" onclick=hiddenItem(this,"newitem'+item.QuestId+'") class="ml5 btn-gray-s">取消</a></div>';
  	  				   str+='<div class="mt20"><a href="javascript:;" onclick=getNewItem("newitem'+item.QuestId+'") class="btn-gray-l">增加选项</a><a href="javascript:;" onclick=getlogicalSetting(this,"'+item.QuestId+'") class="ml15 btn-gray-l">设置逻辑</a><a href="javascript:;" onclick=delQuest("'+item.QuestId+'") class="ml15 btn-gray-l c-org">删除题目</a></div>';
  	  				   str+='</div></div>';
  	  				}
  	  				if(item.QuestType==2)
  	  				{
  	  					var count=i+1;
  	  					str+='<div SortQuestId="'+item.QuestId+'" class="myd-box-gray mt15"> <div class="pl15 myd-gray-tit relative hidden"><strong class="fl"><span>Q'+count+'</span><span class="c-org">【'+zhibiao(item.ContentType)+'】</span></strong>';
  	  					str+='<span id="q'+count+'Tit" class=" fl q-tit" contenteditable="false" onblur=updateQuest(this,"'+item.QuestId+'")>'+item.Question+'</span>';
  	  				    str+='<div class="absolute" style="right:13px; top:8px; font-size:0;"><a href="javascript:;" onclick=Qup(this,"'+item.QuestId+'") class="sort-up"></a><a href="javascript:;"  onclick=Qdown(this,"'+item.QuestId+'") class="ml10 sort-down"></a><a href="javascript:;" class="ml10 btn-edit" onclick="$(\'#q'+count+'Tit\').attr(\'contenteditable\',true).focus().select().addClass(\'tit-editable\')"></a></div></div>';
  	  				    str+='<div class="myd-gray-con"><div class="mt5"><ul>';
  	  				     $.each(item.SvQuestionItems,function(i,qitem)
  	  				     {
  	  				    	
  	  				        str+='<li><span class="valign-m w100"><input type="checkbox" name="q2answer" class="valign-m" style="width:13px;" /><span id="item'+qitem.ItemId+'" class="answer-txt" contenteditable="false"';
  	  				        str+='onblur=upateItem(this,"'+qitem.ItemId+'")>'+qitem.ItemCont+'</span></span>';
  	  				        
  	  				       str+='<a href="javascript:;" class="ml25 btn-edit" onclick="$(\'#item'+qitem.ItemId+'\').attr(\'contenteditable\',true).focus().select().addClass(\'answer-editable\')"></a><a class="ml10" style="margin-top:5px" onclick=delItem("'+qitem.ItemId+'")  href="javascript:;"><img  src="../../common/themes/default/images/nav-gb.png" /></a> </li>';
                           
  	  				     })
  	  				    str+=' </ul></div>';
  	  				    str+='<div  id="newitem'+item.QuestId+'" style="display:none" class="mt15"><input type="text" class="inp-txt" style="width:300px;" placeholder="请输入选项内容" /><input type="button" class="ml10 btn-blue" onclick=addItem(this,'+item.QuestId+') value="确认" /><a href="javascript:;" onclick=hiddenItem(this,"newitem'+item.QuestId+'") class="ml5 btn-gray-s">取消</a></div>';

  	  				   str+='<div class="mt20"><a href="javascript:;" onclick=getNewItem("newitem'+item.QuestId+'") class="ml5 btn-gray-s">增加选项</a><a href="javascript:;" onclick=delQuest("'+item.QuestId+'") class="ml15 btn-gray-l c-org">删除题目</a></div>';
  	  				   str+='</div></div>';
  	  				}
  	  				if(item.QuestType==3)
  	  				{
  	  				    var count=i+1;
	  					str+='<div SortQuestId="'+item.QuestId+'" class="myd-box-gray mt15"> <div class="pl15 myd-gray-tit relative hidden"><strong class="fl"><span>Q'+count+'</span><span class="c-org">【'+zhibiao(item.ContentType)+'】</span></strong>';
	  					str+='<span id="q'+count+'Tit" class=" fl q-tit" contenteditable="false" onblur=updateQuest(this,"'+item.QuestId+'")>'+item.Question+'</span>';
	  				    str+='<div class="absolute" style="right:13px; top:8px; font-size:0;"><a href="javascript:;" onclick=Qup(this,"'+item.QuestId+'") class="sort-up"></a><a href="javascript:;"  onclick=Qdown(this,"'+item.QuestId+'") class="ml10 sort-down"></a><a href="javascript:;" class="ml10 btn-edit" onclick="$(\'#q'+count+'Tit\').attr(\'contenteditable\',true).focus().select().addClass(\'tit-editable\')"></a></div></div>';
  	  					str+='<div class="myd-gray-con"><div class="mt5"><textarea class="p5 inp-txtarea" style="width:520px;"></textarea></div><div class="mt20"><a href="javascript:;" onclick=delQuest("'+item.QuestId+'") class="ml15 btn-gray-l c-org">删除题目</a></div>';
  	  				    str+='</div></div>';
  	  				}
  	  				if(item.QuestType==4)
  	  				{
  	  				    var count=i+1;
	  					str+='<div SortQuestId="'+item.QuestId+'" class="myd-box-gray mt15"> <div class="pl15 myd-gray-tit relative hidden"><strong class="fl"><span>Q'+count+'</span><span class="c-org">【'+zhibiao(item.ContentType)+'】</span></strong>';
	  					str+='<span id="q'+count+'Tit" class=" fl q-tit" contenteditable="false" onblur=updateQuest(this,"'+item.QuestId+'")>'+item.Question+'</span>';
	  				    str+='<div class="absolute" style="right:13px; top:8px; font-size:0;"><a href="javascript:;" onclick=Qup(this,"'+item.QuestId+'") class="sort-up"></a><a href="javascript:;"  onclick=Qdown(this,"'+item.QuestId+'") class="ml10 sort-down"></a><a href="javascript:;" class="ml10 btn-edit" onclick="$(\'#q'+count+'Tit\').attr(\'contenteditable\',true).focus().select().addClass(\'tit-editable\')"></a></div></div>';
	  				    str+='<div class="myd-gray-con"><table class="tb-sjtj" width="100%">';
	  				    str+='<tr><td class="thead-1">子项</td>';
	  				    $.each(item.ChildrenMatrixQuestion[0].MatrixQuestItems,function(i,marxitem)
	  				    {
	  				    	str+='<td class="thead-1"><span>'+marxitem.ItemCont+'</span> <a href="javascript:;" onclick="delcol(this)"><img src="../../common/themes/default/images/nav-gb.png" /></a></td>';
	  				    })
	  				    str+='</tr>';
	  				    $.each(item.ChildrenMatrixQuestion,function(i,mquest)
	  		  			{
	  		  				str+='<tr><td>'+mquest.Question+' <a href="javascript:;" quesid='+mquest.QuestId+' onclick="delrow(this)"><img  src="../../common/themes/default/images/nav-gb.png" /></a></td>';
	  		  			   $.each(mquest.MatrixQuestItems,function(i,cmarxitem)
	  		  			   {
	  		  				 str+='<td><input type="radio" itemid='+cmarxitem.ItemId+' name="fwtd" /></td>';  
	  		  			   });
	  		  			  str+='</tr>';
	  		  			})
	  		  			str+='</table>';
	  		  			 str+='<div  id="newitem'+item.QuestId+'" style="display:none" class="mt15"><input type="text" class="inp-txt" style="width:300px;" placeholder="请输入子项内容" /><input type="button" class="ml10 btn-blue" onclick=addMatrQuest(this,"'+item.QuestId+'") value="确认" /><a href="javascript:;" onclick=hiddenItem(this,"newitem'+item.QuestId+'") class="ml5 btn-gray-s">取消</a></div>';
	  		  		     str+='<div  id="newchilditem'+item.QuestId+'" style="display:none" class="mt15"><input type="text" class="inp-txt" style="width:300px;" placeholder="请输入选项内容" /><input type="button" class="ml10 btn-blue" onclick=addMatrItem(this,"'+item.QuestId+'") value="确认" /><a href="javascript:;" onclick=hiddenItem(this,"newchilditem'+item.QuestId+'") class="ml5 btn-gray-s">取消</a></div>';

	  				    str+=' <div class="mt20"><a href="javascript::" onclick=getNewItem("newitem'+item.QuestId+'") class="ml5 btn-gray-s">增加子项</a><a href="javascript:;"onclick=newchilditem("newchilditem'+item.QuestId+'") class="ml15 btn-gray-l">增加选项</a><a href="javascript:;" onclick=delMatrixQuest("'+item.QuestId+'") class="ml15 btn-gray-l c-org">删除题目</a></div></div></div>';
  	  				}
  	  				if(item.QuestType==5)
  	  				{
  	  				    var count=i+1;
	  					str+='<div SortQuestId="'+item.QuestId+'" class="myd-box-gray mt15"> <div class="pl15 myd-gray-tit relative hidden"><strong class="fl"><span>Q'+count+'</span><span class="c-org">【'+zhibiao(item.ContentType)+'】</span></strong>';
	  					str+='<span id="q'+count+'Tit" class=" fl q-tit" contenteditable="false" onblur=updateQuest(this,"'+item.QuestId+'")>'+item.Question+'</span>';
	  				    str+='<div class="absolute" style="right:13px; top:8px; font-size:0;"><a href="javascript:;" onclick=Qup(this,"'+item.QuestId+'") class="sort-up"></a><a href="javascript:;" onclick=Qdown(this,"'+item.QuestId+'") class="ml10 sort-down"></a><a href="javascript:;" class="ml10 btn-edit" onclick="$(\'#q'+count+'Tit\').attr(\'contenteditable\',true).focus().select().addClass(\'tit-editable\')"></a></div></div>';
	  				    str+='<div class="myd-gray-con"><table class="tb-sjtj" width="100%">';
	  				    str+='<tr><td class="thead-1">子项</td>';
	  				    $.each(item.ChildrenMatrixQuestion[0].MatrixQuestItems,function(i,marxitem)
	  				    {
	  				    	str+='<td class="thead-1"><span>'+marxitem.ItemCont+'</span> <a href="javascript:;" onclick="delcol(this)"><img src="../../common/themes/default/images/nav-gb.png" /></a></td>';
	  				    })
	  				    str+='</tr>';
	  				    $.each(item.ChildrenMatrixQuestion,function(i,mquest)
	  		  			{
	  		  				str+='<tr><td>'+mquest.Question+' <a href="javascript:;" quesid='+mquest.QuestId+' onclick="delrow(this)"><img  src="../../common/themes/default/images/nav-gb.png" /></a></td>';
	  		  			   $.each(mquest.MatrixQuestItems,function(i,cmarxitem)
	  		  			   {
	  		  				 str+='<td><input type="checkbox" itemid='+cmarxitem.ItemId+' name="fwtd" /></td>';  
	  		  			   });
	  		  			  str+='</tr>';
	  		  			})
	  		  			str+='</table>';
	  		  			 str+='<div  id="newitem'+item.QuestId+'" style="display:none" class="mt15"><input type="text" class="inp-txt" style="width:300px;" placeholder="请输入子项内容" /><input type="button" class="ml10 btn-blue" onclick=addMatrQuest(this,"'+item.QuestId+'") value="确认" /><a href="javascript:;" onclick=hiddenItem(this,"newitem'+item.QuestId+'") class="ml5 btn-gray-s">取消</a></div>';
	  		  		     str+='<div  id="newchilditem'+item.QuestId+'" style="display:none" class="mt15"><input type="text" class="inp-txt" style="width:300px;" placeholder="请输入选项内容" /><input type="button" class="ml10 btn-blue" onclick=addMatrItem(this,"'+item.QuestId+'") value="确认" /><a href="javascript:;" onclick=hiddenItem(this,"newchilditem'+item.QuestId+'") class="ml5 btn-gray-s">取消</a></div>';

	  				    str+=' <div class="mt20"><a href="javascript::" onclick=getNewItem("newitem'+item.QuestId+'") class="ml5 btn-gray-s">增加子项</a><a href="javascript:;"onclick=newchilditem("newchilditem'+item.QuestId+'") class="ml15 btn-gray-l">增加选项</a><a href="javascript:;" onclick=delMatrixQuest("'+item.QuestId+'") class="ml15 btn-gray-l c-org">删除题目</a></div></div></div>';
  	  				}
  	  					
  	  				
  	  			});
  	  		    $('#ifnoQuest').hide();
  	  		    showContentType();
  	  			$('#core').append(str);
  	  			
  			}else
  			{
  				$('#ifnoQuest').show();
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
function myZone()
{
	
  	var params={};
    var session=getSession();
  	params.OrgID=session.orgid;
  	$.post('/WbjUI/wbj2/business/web/wtbj_myZone.do', params, function (result) {
  		if(result.Code==10000)
		{
  			var str='';
  			$.each(result.Result,function(i,item)
  			{
  				str+='<li><input name="myzonecheckbox" type="checkbox" value='+item.QuestId+' />'+item.Question+'</li>';
  			});
  			$('#getmyzone').empty();
  			$('#getmyzone').append(str);
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

function insertQuest()
{
	
	
	
	var params={};
	var session=getSession();
	params.ContentType=$('#ContentType').val();
	params.ObjType=$('#ObjType').val();
	params.QuestType=$('#QuestType').val();
	params.SubjectId=$('#SubjectId').val();
    params.OperatorID=session.userId;
    params.OperatorName=session.operatorname;
	$.post('/WbjUI/wbj2/business/web/wtbj_addDetailQuestion.do', params, function (result) {
		if(result.Code==10000)
		{
			closepop('addQuestion');
  			getCore();
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
function getmyquestbyid()
{
	var sbid=$('#myzoneselect').val();
	if(sbid==null||sbid==''||sbid==undefined)
	{
		
		myZone();
		return;
	}
	if(sbid==0)
	{
		myzone();
	}
	else
	{
		
		var params={};
		params.SubjectId=sbid;
		$.post('/WbjUI/wbj2/business/web/wtbj_myZoneQuestbySubjectId.do', params, function (result) {
			if(result.Code==10000)
			{
	  			var str='';
	  			$.each(result.Result,function(i,item)
	  			{
	  				str+='<li><input name="myzonecheckbox" type="checkbox" value='+item.QuestId+' />'+item.Question+'</li>';
	  			});
	  			$('#getmyzone').empty();
	  			$('#getmyzone').append(str);
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
}
function queryPlatformQuestion()
{
	$('#getPlatform').empty();
	var params={};
	params.ContentType=$('#QContentType').val();
	params.ObjType=$('#QObjType').val();
	$.post('/WbjUI/wbj2/business/web/wtbj_queryPlatformQuestion.do', params, function (result) {
		if(result.Code==10000)
		{
  			var str='';
  			$.each(result.Result,function(i,item)
  			{
  				str+='<li><input name="platformcheckbox" type="checkbox" value='+item.QuestId+' />'+item.Question+'</li>';
  			});
  			$('#getPlatform').append(str);
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
function optionmyZoneQuestion()
{
  	var params={};
  	
    var session=getSession();
  	params.SubjectId=$('#SubjectId').val();
  	params.OperatorId=session.userId;
  	params.OperatorName=session.operatorname;
	  var str='';
		
	  $("input[name='myzonecheckbox']:checked").each(function(){   
		str+=$(this).val()+",";   
   
	 }); 
		if(str==null||str==''||str==undefined)
		{
			var z="请勾选";
			Commonjs.alert(z);
			return;
		}
	params.OptionQuestionid=str;
  	$.post('/WbjUI/wbj2/business/web/wtbj_optionQuestion.do', params, function (result) {
  		if(result.Code==10000)
		{
  			getCore();
  			closepop('chooseQuestion');
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
function optionPlatQuestion()
{
  	var params={};
  	
    var session=getSession();
  	params.SubjectId=$('#SubjectId').val();
  	params.OperatorId=session.userId;
  	params.OperatorName=session.operatorname;
	  var str='';
		
	  $("input[name='platformcheckbox']:checked").each(function(){   
		str+=$(this).val()+",";   
   
	 }); 
	if(str==null||str==''||str==undefined)
	{
		var z="请勾选";
		Commonjs.alert(z);
		return;
	}
	params.OptionQuestionid=str;
  	$.post('/WbjUI/wbj2/business/web/wtbj_optionQuestion.do', params, function (result) {
  		if(result.Code==10000)
		{
  			getCore();
  			closepop('chooseQuestion');
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
function closepop(id){
	document.getElementById(id).style.display="none"
}
/*显示弹窗*/
function showpop(id){
	document.getElementById(id).style.display="block"
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

function zhibiao(id)
{
	
	str='';
	switch (parseInt(id)) {
    case 1:
    	str='医生服务';
        break;
    case 2:
    	str='护士服务';
        break;
    case 3:
    	str='医技服务';
        break;
    case 4:
    	str='药房服务';
        break;
    case 5:
    	str='收费服务';
        break;
    case 6:
    	str='就诊环境';
        break;
    case 7:
    	str='后勤保障';
        break;
    case 8:
    	str='导医导诊';
        break;
    case 9:
    	str='就诊流程';
        break;
    case 10:
    	str='医风医德';
        break;
    case 11:
    	str='忠诚指数';
        break;
    case 12:
    	str='其他问题';
        break;
    case 13:
    	str='通用指标';
        break;
    default:
    	str='未知指标';
	}
   return str;
	
}
function countContentType(id)
{
  		     
		      if(id==1)
		      {
		        fl1++;
		      }
              if(id==2)
		      {
		        fl2++;
		      }
		     if(id==3)
		      {
		        fl3++;
		      }
              if(id==4)
		      {
		        fl4++;
		      }
		      if(id==5)
		      {
		        fl5++;
		      }
              if(id==6)
		      {
		        fl6++;
		      }
		      if(id==7)
		      {
		        fl7++;
		      }
              if(id==8)
		      {
		        fl8++;
		      } 
		      if(id==9)
		      {
		        fl9++;
		      }
              if(id==10)
		      {
		        fl10++;
		      }
		      if(id==11)
		      {
		        fl11++;
		      }
              if(id==12)
		      {
		        fl12++;
		      } 
		      if(id==13)
		      {
		        fl13++;
		      } 

}
function showContentType()
{
	if(fl1==0&&fl2==0&&fl3==0&&fl4==0&&fl5==0&&fl6==0&&fl7==0&&fl8==0&&fl9==0&&fl10==0&&fl11==0&&fl12==0&&fl13==0)
	{
		
		 $('#zhibiaoul').append('<li>暂无指标</li>');
	}else
	{
		if(fl1>0)
	    {
	       $('#zhibiaoul').append('<li>医生服务('+fl1+')</li>');
	    }
	    if(fl2>0)
	    {
	      $('#zhibiaoul').append('<li>护士服务('+fl2+')</li>');
	    }
	    if(fl3>0)
	    {
	      $('#zhibiaoul').append('<li>医技服务('+fl3+')</li>');
	    }
	    if(fl4>0)
	    {
	      $('#zhibiaoul').append('<li>药房服务('+fl4+')</li>');
	    }
	    if(fl5>0)
	    {
	      $('#zhibiaoul').append('<li>收费服务('+fl5+')</li>');
	    }
	    if(fl6>0)
	    {
	      $('#zhibiaoul').append('<li>就诊环境('+fl6+')</li>');
	    }
	    if(fl7>0)
	    {
	      $('#zhibiaoul').append('<li>后勤保障('+fl7+')</li>');
	    }
	    if(fl8>0)
	    {
	      $('#zhibiaoul').append('<li>导医导诊('+fl8+')</li>');
	    }
	    if(fl9>0)
	    {
	      $('#zhibiaoul').append('<li>就诊流程('+fl9+')</li>');
	    }
	    if(fl10>0)
	    {
	      $('#zhibiaoul').append('<li>医风医德('+fl10+')</li>');
	    }
	    if(fl11>0)
	    {
	      $('#zhibiaoul').append('<li>忠诚指数('+fl11+')</li>');
	    }
	    if(fl12>0)
	    {
	      $('#zhibiaoul').append('<li>其他问题('+fl12+')</li>');
	    }
	    if(fl13>0)
	    {
	      $('#zhibiaoul').append('通用指标('+fl13+')</li>');
	    }
	}
    
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

function uniteMessage(value,alertchar)
{
	var tag=false;
	if(value==null||value==''||value==undefined)
	{
		var str="不能为空!!";
		tag=true;
		Commonjs.alert(str);
		return true;
		
	}
	value=trim(value);
	//alert("xx"+value);
	//alert(value.replace(/([^\x00-\xff])/g,'**').length);
	//if(value.replace(/([^\x00-\xff])/g,'**').length>alertchar)
	if(value.length>alertchar)
	{
		var str="请勿超过"+alertchar+"个字符";
		Commonjs.alert(str);
		tag=true;
		return true;
	}
	return false;
	
}

//前后空格
function trim(str){   
    return str.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');   
}  