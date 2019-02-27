$( function() {
//  json=jsonString;
  getAnswer();
});

var session = null;
var SampleId = YihuUtil.queryString('SampleId');
//var jsonString='{"Result":[{"SvQuestionItems":[{"OtherAnswer":"3434","ItemCont":"很满意","ItemId":"15356","IfAddblank":"1"}],"QuestType":1,"Question":"你觉的医生态度好吗","QuestId":5966,"ParentQuestId":0},{"SvQuestionItems":[{"OtherAnswer":"","ItemCont":"卫生","ItemId":"15360"},{"OtherAnswer":"","ItemCont":"综合素养","ItemId":"15361"}],"QuestType":2,"Question":"医院需要改进的有哪些","QuestId":5967,"ParentQuestId":0},{"SvQuestionItems":[{"ItemId":"","Answer":"一定会"}],"QuestType":3,"Question":"你还会来我们医院吗","QuestId":5968,"ParentQuestId":0},{"ChildrenMatrixQuestion":[{"QuestType":1,"Question":"你觉的医生态度好吗","QuestId":5966,"MatrixQuestItems":[{"ItemCont":"满意","ItemId":"15363"}]},{"QuestType":2,"Question":"医院需要改进的有哪些","QuestId":5967,"MatrixQuestItems":[{"ItemCont":"很满意","ItemId":"15364"}]}],"SvQuestionItems":[],"QuestType":4,"Question":"你喜欢哪位医生的治疗","QuestId":5969,"ParentQuestId":0},{"ChildrenMatrixQuestion":[{"QuestType":1,"Question":"你觉的医生态度好吗","QuestId":5966,"MatrixQuestItems":[{"ItemCont":"很满意","ItemId":"15366"},{"ItemCont":"满意","ItemId":"15367"}]},{"QuestType":2,"Question":"医院需要改进的有哪些","QuestId":5967,"MatrixQuestItems":[{"ItemCont":"很满意","ItemId":"15368"},{"ItemCont":"BUS很满意","ItemId":"15368"}]}],"SvQuestionItems":[],"QuestType":5,"Question":"多选喜欢的","QuestId":5972,"ParentQuestId":0}],"Message":"成功","SampleName":"用户1","Code":10000}';
var json=null;

function getAnswer(){
	var param = {};
	var Service = {};
//	 ComWbj.openPG();
//	param.Api="survey.SurveyApiImpl.PersonStatisticaBySampleId";
	Service.HosId = Commonjs.getUrlParam("HosId");
	Service.SampleId = SampleId;
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/survey/personStatisticaBySampleId.do", param, function(resp) {
		 if(resp.RespCode == 10000){
			 eachAnserd(resp.Data);
//			 ComWbj.closePG();
		 } else{
//			 ComWbj.closePG();
				if(resp.RespMessage==undefined){
//					YihuUtil.art.warning('获取科室请求错误或超时');
					alertwmk("警告","请求错误或超时","warning");
				}else{
//					YihuUtil.art.error('获取科室失败'+ resp.Result);
					alertwmk("警告",resp.RespMessage,"error");
				}
			}
	})
	}

//自定义弹出框
function alertwmk(a, b,icon) {
	ComWbj.artTips(a,icon,b,1.5,null);
}

var html="";
//遍历每个单选题
function  eachAnserd(json){
//	json=$.parseJSON(json);
	html="";
    $.each(json,function(i,item){
    	if(item.QuestType==1){
    		html=html+danxt(i,item);
    	}
    	if(item.QuestType==2){
    		html=html+duoxt(i,item);
    	}
    	if(item.QuestType==3){
    		html=html+Tkt(i,item);
    	}
    	if(item.QuestType==4){
    		html=html+Jzdanxt(i,item);
    	}
    	if(item.QuestType==5){
    		html=html+Jzduoxt(i,item);
    	}
    });
    
    $("#divV").empty();
    $("#divV").append(html);
}

//单选题
function danxt(i,item){
	var ret='<div class="qa-item-wrap">'+
		' <div class="q-tit">'+
		' <span class="q-num" style="width:40px;">第'+(i+1)+'题</span>'+
		' <div class="q-tit-txt2">'+item.Question+'(单选题)</div>'+
		' </div>'+
		' <div class="a-wrap">'+
		' <ul class="radio-options">'+
		' <li>'+
		'  <div class="valign-m">'+
		setDanxt(item.SvQuestionItems)+
		' </div>'+
		' </li>'+
		'  </ul>'+
		' </div>'+
		' </div>';
	return ret;
}
//设置单选题答案
function setDanxt(item){
	if(item.length==0){
		return '<div class="valign-m-chk mr20">答案：空</div>';
	}else if(item.length==1){
		var aa= '<div class="valign-m-chk mr20">答案：'+item[0].ItemCont+'</div>';
	   if(item[0].IfAddblank==1){
		   aa=aa+'<div class="valign-m-chk mr20">补充：'+item[0].OtherAnswer+'</div>';
	   }
	return aa;
	}
}
//多选
function duoxt(i,item){
	var ret='<div class="qa-item-wrap">'+
		' <div class="q-tit">'+
		' <span class="q-num" style="width:40px;">第'+(i+1)+'题</span>'+
		' <div class="q-tit-txt2">'+item.Question+'(多选题)</div>'+
		' </div>'+
		' <div class="a-wrap">'+
		' <ul class="radio-options">'+
		' <li>'+
		'  <div class="valign-m">'+
		setDuoxt(item.SvQuestionItems)+
		' </div>'+
		' </li>'+
		'  </ul>'+
		' </div>'+
		' </div>';
	return ret;
}
//设置多选
function setDuoxt(item){
	if(item.length==0){
		return '<div class="valign-m-chk mr20">答案：空</div>';
	}else{
		var a='';
		$.each(item,function(ii,iitem){
			a=a+"&nbsp;&nbsp;"+(ii+1)+"、"+iitem.ItemCont;
		});
		var aa= '<div class="valign-m-chk mr20">答案：'+a+'</div>';
	return aa;
	}
}

//填空题
function Tkt(i,item){
	var ret='<div class="qa-item-wrap">'+
		' <div class="q-tit">'+
		' <span class="q-num" style="width:40px;">第'+(i+1)+'题</span>'+
		' <div class="q-tit-txt2">'+item.Question+'(填空题)</div>'+
		' </div>'+
		' <div class="a-wrap">'+
		' <ul class="radio-options">'+
		' <li>'+
		'  <div class="valign-m">'+
		setTkt(item.SvQuestionItems)+
		' </div>'+
		' </li>'+
		'  </ul>'+
		' </div>'+
		' </div>';
	return ret;
}
//设置填空
function setTkt(item){
	if(item.length==0){
		return '<div class="valign-m-chk mr20">答案：空</div>';
	}else{
		var aa= '<div class="valign-m-chk mr20">答案：'+item[0].Answer+'</div>';
	return aa;
	}
}


//矩阵单选题
function Jzdanxt(i,item){
	var ret='<div class="qa-item-wrap">'+
		' <div class="q-tit">'+
		' <span class="q-num" style="width:40px;">第'+(i+1)+'题</span>'+
		' <div class="q-tit-txt2">'+item.Question+'(矩阵单选题)</div>'+
		' </div>'+
		' <div class="a-wrap">'+
		' <ul class="radio-options">'+
		' <li>'+
		'  <div class="valign-m">'+
		setJzdanxt(item.ChildrenMatrixQuestion)+
		' </div>'+
		' </li>'+
		'  </ul>'+
		' </div>'+
		' </div>';
	return ret;
}
//矩阵单选题
function setJzdanxt(item){
	if(item.length==0){
		return '<div class="valign-m-chk mr20">该题未作答</span></div>';
	}else{
//		var aa= '<div class="valign-m-chk mr20">答案：<span class="radio-opt-txt2">'+item[0].Answer+'</span></div>';
		var aa="";
		$.each(item,function(ii,iitem){
			if(ii==0){
				aa= aa+'<div class="valign-m-chk mr20">题'+(ii+1)+':'+iitem.Question+'</div>'+getJzAnserd(iitem.MatrixQuestItems);
			}else{
				aa= aa+'<br><div class="valign-m-chk mr20">题'+(ii+1)+':'+iitem.Question+'</div>'+getJzAnserd(iitem.MatrixQuestItems);
			}
			
		});
	return aa;
	}
}

//矩阵多选题
function Jzduoxt(i,item){
	var ret='<div class="qa-item-wrap">'+
		' <div class="q-tit">'+
		' <span class="q-num" style="width:40px;">第'+(i+1)+'题</span>'+
		' <div class="q-tit-txt2">'+item.Question+'(矩阵多选题)</div>'+
		' </div>'+
		' <div class="a-wrap">'+
		' <ul class="radio-options">'+
		' <li>'+
		'  <div class="valign-m">'+
		setJzduoxt(item.ChildrenMatrixQuestion)+
		' </div>'+
		' </li>'+
		'  </ul>'+
		' </div>'+
		' </div>';
	return ret;
}
//矩阵多选题
function setJzduoxt(item){
	if(item.length==0){
		return '<div class="valign-m-chk mr20">该题未作答</span></div>';
	}else{
//		var aa= '<div class="valign-m-chk mr20">答案：<span class="radio-opt-txt2">'+item[0].Answer+'</span></div>';
		var aa="";
		$.each(item,function(ii,iitem){
			if(ii==0){
				aa= aa+'<div class="valign-m-chk mr20" style="width:100%;">题'+(ii+1)+':'+iitem.Question+'</div>'+getJzAnserd(iitem.MatrixQuestItems);
			}else{
				aa= aa+'<br><div class="valign-m-chk mr20" style="width:100%;">题'+(ii+1)+':'+iitem.Question+'</div>'+getJzAnserd(iitem.MatrixQuestItems);
			}
			
		});
	return aa;
	}
}

//获取矩阵答案
function getJzAnserd(item){
	if(item.length==0){
		return '<div class="valign-m-chk mr20">答案：空</div>';
	}else{
		var a='';
		$.each(item,function(ii,iitem){
			a=a+"&nbsp;&nbsp;"+(ii+1)+"、"+iitem.ItemCont;
		});
		var aa= '<div class="valign-m-chk mr20">答案：'+a+'</div>';
	return aa;
	}
}

