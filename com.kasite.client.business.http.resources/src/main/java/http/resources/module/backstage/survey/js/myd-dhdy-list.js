$( function() {
	judgeSurvey();
});
var session = YihuUtil.getSession();
//判断该机构是否具有问卷调查
var listSurvey={};

function init() {
	judgeSurvey();
}
function judgeSurvey(){
	var param = {};
	 ComWbj.openPG();
	param.Api="survey.SurveyApiImpl.querySubjectForPhoneList";
	param.Param = "{'OrgId':"+ Commonjs.hospitalId; +",'PageSize':"+$("#pagesize").val()+",'PageStart':"+$("#pagenumber").val()+"}";
	doAjaxLoadData("../../survey/querySubjectForPhoneList.do", param, function(resp) {
		 if(resp.respCode == 10000){
			 
			 if(resp.result.length>0){
				 listSurvey=resp.result;
				 Page(resp.totalProperty,$("#pagesize").val() ,getTotalPage(resp.totalProperty, $("#pagesize").val()),'pager');
				 $("#tableShow tr:not(:first):not(:last)").remove();
				 var html="";
				 $.each(listSurvey,function(i,item){
		        	html=html+'<tr>'+
					'<td>'+(i+1)+'</td>'+
				'<td>'+strToLowStr(item.subjecttitle,15)+'</td>'+
				'<td>'+StrToLow(item.createtime,16)+'</td>'+
				'<td>'+showEndTime(item.endtime)+'</td>'+
				'<td>'+item.visitedsample+'/'+item.quantity+'</td>'+
				'<td>'+showStatus(item.status)+'</td>'+
				'<td class="td-act">'+
				getLink(item) 
				+
				'</td>'+
			'</tr>';
		         });   
				 $(html).insertAfter($("#tableShow tr").eq(0));
//                  alertwmk("成功",resp.Result,"succeed");
			 }else{
				 window.location="myd-dhdy-inf.html";
			 }
			 ComWbj.closePG();
		 } else{
			 ComWbj.closePG();
				if(resp.Message==undefined){
//					YihuUtil.art.warning('获取科室请求错误或超时');
					alertwmk("警告","请求错误或超时","warning");
				}else{
//					YihuUtil.art.error('获取科室失败'+ resp.Result);
					alertwmk("警告","获取失败","error");
				}
			}
	})
	}

//只显示十五个字符其余的....
function strToLowStr(string,length){
	if(string==null||string==''){
		return '';
	}else if(string.length<=15){
		return string;
	}else{
		return StrToLow(string,length,0)+"...";
	}
};



function StrToLow(string,length,start){
	if(start==null){
		start=0;
	}
	return string.substring(start,length);
}

//判断是否有问卷
function judgeHaveSurvey(){
	var param = {};
	 ComWbj.openPG();
	param.Api="survey.SurveyApiImpl.querySubjectForNetList";
	param.Param = "{'OrgId':"+ Commonjs.hospitalId; +",'PageSize':"+$("#pagesize").val()+",'PageStart':"+$("#pagenumber").val()+"}";
	doAjaxLoadData("../../survey/querySubjectForNetList.do", param, function(resp) {
		 if(resp.respCode == 10000){
			 if(resp.data.result.length>0){
				
			 }else{
				 window.location="myd-dhdy-w.html";
			 }
			 ComWbj.closePG();
		 } else{
			 ComWbj.closePG();
				if(resp.respMessage==undefined){
//					YihuUtil.art.warning('获取科室请求错误或超时');
					alertwmk("警告","请求错误或超时","warning");
				}else{
//					YihuUtil.art.error('获取科室失败'+ resp.Result);
					alertwmk("警告",resp.respMessage,"error");
				}
			}
	})
	}

function  getLink(item){
	if(item.status==1||item.status==2||item.status==3){
		return '<a href="myd-wjtj.html?SubjectId='+item.subjectid+'&subjecttitle='+item.subjecttitle+'?typeV=1"><i class="icon icon-statics"></i>统计</a><a href="myd-jgtj-list.html?subjectid='+item.subjectid+'&subjecttitle='+item.subjecttitle+'?typeV=1&Status=2"><i class="icon icon-audio"></i>查看录音</a><a href="myd-wjyl.html?SubjectId='+item.subjectid+'"><i class="icon icon-preview"></i>预览</a>';
	}else{
		return '<a href="javascript:;" style="color:#AEB5B9;"><i class="icon icon-statics"></i>统计</a><a href="javascript:;" style="color:#AEB5B9;"><i class="icon icon-audio"></i>查看录音</a><a href="myd-wjyl.html?SubjectId='+item.subjectid+'"><i class="icon icon-preview"></i>预览</a>';
	}
}

function Page(totalcounts,pageSize ,pagecount,pager) {
	$("#"+pager).pager( {
		totalcounts : totalcounts,
		pagesize :pageSize,
		pagenumber : $("#pagenumber").val(),
		pagecount : pagecount,
		buttonClickCallback : function(al) {
			$("#pagenumber").val(al);
			judgeSurvey();
		}
	});
}
function getTotalPage(total,pagesize){
	var ys=total%pagesize;
	var zs=total/pagesize;
	if(ys==0){
		return parseInt(zs);
	}else{
		return parseInt(zs)+1;
	}
}
//显示结束时间
function showEndTime(val){
	if(val==null||val==''){
		return '无';
	}else{
		return StrToLow(val,16);
	}
}
//显示状态
function showStatus(val){
	if(val==null||val==''){
	return '未知';
	}else if(val==1){
	return '未开始';	
	}else if(val==2){
	return '收集中';	
	}else if(val==3){
	return '已结束';	
	}else if(val==4){
	return '待审核';	
	}else if(val==5){
	return '审核不通过';	
	}else {
	return '未知';	
	}
}

//自定义弹出框
function alertwmk(a, b,icon) {
	ComWbj.artTips(a,icon,b,1.5,null);
}

// 消除特殊字符
function stripscript(id) {
	  var  val=$("#"+id).val();
// alert(val);
    var pattern = new RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\￥)(\……)(\*)(\&)(\【)(\】)(\。)(\，)(\%)(\^)(\&)(\*)(\-)(\_)(\+)(\=)(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/)(\?)]+/);
        var rs = "";
      
    for (var i = 0; i < val.length; i++) {
        rs = rs + val.substr(i, 1).replace(pattern, '');
    }
    return $("#"+id).val(rs);
}

function setTime() {
	setTimeout( function() {
	}, 2000);
}

