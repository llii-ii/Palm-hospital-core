$(function(){

	
	gowjdc();
	
	

});

function goreturn()
{
	
    var url="../survey/wdwj_execute.do";  
	location.href=url;
}
function gowjdc()
{
	$('#dcbegin').empty();
	$('#dccore').empty();
	$('#dcend').empty();
	$('#SubjectId').val(YihuUtil.queryString('subjectid'));
	var params={};
	params.SubjectId=$('#SubjectId').val();
	$.post('/WbjUI/wbj2/business/web/cjwj_querySubjectById.do', params, function (result) {
	 if(result.Code==10000)
	 {
			$('#dcbegin').append(result.BeginIntro);
			$('#dcend').append(result.EndingIntro);
			
			if(result.SubjectTitle.length<15)
			{
  				$('#subjecttitle').html(result.SubjectTitle);
  			
			}else
			{
				$('#subjecttitle').html(result.SubjectTitle.substring(0,15)+'...');
  			
			}
			
		    var str='';
			$.each(result.Result,function(i,item)
		{
			i+=1;
	        if(item.QuestType<3)
	        {
	        	str+='<div class="myd_q mt15"><span class="fl" style="width:25px">Q'+i+'</span>'+item.Question+'</div>';
	        	str+='<div class="myd_a ml25"> <ul>';
	        	$.each(item.SvQuestionItems,function(i,qitem)
	        	{
	        		if(item.QuestType==1)
	        		{
	        			str+=' <li><label><input type="radio" disabled name="myd_a1" />'+qitem.ItemCont+'</label></li>';
	        		}
	        		if(item.QuestType==2)
	        		{
	        			str+=' <li><label><input type="checkbox" onclick="return false" name="myd_a1" />'+qitem.ItemCont+'</label></li>';
	        		}
	        		
	        	});
	        	str+='</ul></div>';
	        }
	        if(item.QuestType==3)
	        {
	        	str+=' <div class="myd_q mt30"><span class="fl" style="width:25px">Q'+i+'</span>'+item.Question+'</div>';
	        	str+=' <div class="myd_a ml25"><textarea disabled="disabled" class="p5 mt5 inp-txtarea" style="width:695px;"></textarea></div>';
	        }
	        if(item.QuestType==4)
	        {
	        	str+=' <div class="myd_q mt15"><span class="fl" style="width:25px">Q'+i+'</span>'+item.Question+'</div>';
	        	str+='  <div class="myd_a ml25">  <table class="myd-qa-tb">';
	        	$.each(item.ChildrenMatrixQuestion,function(i,martquesion)
	        	{ 
	        		
	        		str+='<tr><td><span class="f12">'+martquesion.Question+'</span></td><td>';
	        		
	        		$.each(martquesion.MatrixQuestItems,function(i,martitem)
	        		{
	        			str+='<input type="radio" disabled name="myd-hjws" id="myd-hjws-a1" /><label for="myd-hjws-a1">'+martitem.ItemCont+'</label>';
	        		});
	        		str+='</td>  </tr>';
	        		
	        	});
	        	str+=' </table> </div>';
	        	
	        }
	        if(item.QuestType==5)
	        {
	        	str+=' <div class="myd_q mt15"><span class="fl" style="width:25px">Q'+i+'</span>'+item.Question+'</div>';
	        	str+='  <div class="myd_a ml25">  <table class="myd-qa-tb">';
	        	$.each(item.ChildrenMatrixQuestion,function(i,martquesion)
	        	{ 
	        		
	        		str+='<tr><td><span class="f12">'+martquesion.Question+'</span></td><td>';
	        		
	        		$.each(martquesion.MatrixQuestItems,function(i,martitem)
	        		{
	        			str+='<input type="checkbox" name="myd-hjws" onclick="return false" id="myd-hjws-a1" /><label for="myd-hjws-a1">'+martitem.ItemCont+'</label>';
	        		});
	        		str+='</td>  </tr>';
	        		
	        	});
	        	str+=' </table> </div>';
	        }
		 });
		   
			$('#dcore').append(str);
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