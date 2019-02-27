$( function() {
	//设置状态
	setStatus();
	//设置问卷调查名称
	$("#subjecttitle").text(subjecttitle);
	//查询问卷模板
	judgeSurvey();
	//为统计概况设置地址
	$("#tjgk").attr("href","myd-wjtj.html?SubjectId="+subjectid)
	
	if(typeV==1){
		$("#xzyy").scc("display","");
   }
});
var session = null;
var subjecttitle=decodeURI(YihuUtil.queryString('subjecttitle'));
var subjectid=YihuUtil.queryString('subjectid');
var typeV=YihuUtil.queryString('typeV');
var Status=YihuUtil.queryString('Status');

function setStatus(){
	$("#statusV").val(Status);
}

//判断该机构是否具有问卷调查
var listSurvey={};

function init() {
	judgeSurvey();
}
function search(){
	$("#pagenumber").val(1);
	judgeSurvey();
}

function judgeSurvey(){
	var param = {};
	var Service = {};
	Service.HosId = Commonjs.getUrlParam("HosId");
	Service.SubjectId = subjectid;
	if($("#statusV").val()!=null&&$("#statusV").val()!=''){
		Service.Status=$("#statusV").val();
	}
	var page = {};
	page.PSize = Number($("#pagesize").val());
	page.PIndex = Number($("#pagenumber").val()) - 1;
	Service.Page = page;
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax("/survey/querySampleBySubjectid.do", param, function(resp) {
		 if(resp.RespCode == 10000){
			 $("#tableShow tr:not(:first):not(:last)").remove();
			 Page(resp.totalProperty,$("#pagesize").val() ,getTotalPage(resp.totalProperty, $("#pagesize").val()),'pager');
			 if(resp.Data.length>0){
				 listSurvey = resp.Data;
				  var html="";
				 $.each(listSurvey,function(i,item){
		        	html=html+'<tr>'+
					'<td>'+(i+1)+'</td>'+
				'<td>'+judgeName(item.UserName)+'</td>'+
				'<td>'+getStatus(item.Status)+'</td>'+
				'<td>'+StrToLow(item.Opertime,19)+'</td>'+
				'<td>'+getDo(item)+'</td>'+
			'</tr>';
		         });   
				 $(html).insertAfter($("#tableShow tr").eq(0));
//                  alertwmk("成功",resp.Result,"succeed");
			 }
		 } else{
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

//判断人员是否为空
function judgeName(val){
	if(val==null||val==""){
		return "--"
	}else{
		return val;
	}
}

function getDo(item){
	    if(item.Status == 2){
	    var  retV="<a style='color:blue;' href='myd-answerInf.html?SampleId="+item.SampleId+"'> 查看</a>";
	    if(typeV == 1){
             retV=retV+"&nbsp;&nbsp;&nbsp;&nbsp;<a style='color:blue;' href='javascript:;'  onclick='getDownFile("+item.SampleId+")' >下载录音</a><a style='color:blue;display:none;' id='downBtn' href='javascript:;'  ></a>"
	    }
	     return retV;
	    
	    }else{
	    	return "";
	    }
};

function getDownFile(sampleid){

	var param = {};
	param.Api="survey.SurveyApiImpl.queryDownloadPathBySampleid";
	param.Param ="{'sampleid':"+sampleid+"}";
	doAjaxLoadData("../survey/MydDhdy_doAll.action", param, function(resp) {
		 if(resp.Code == 10000){
			var result=resp.result;
			if(result.length==0){
				showpop("setting3");
				$("#Context").empty();
				$("#Context").append("没有可下载的语音文件！");
//			}else if(result.length==1){
////				openDownWin(result[0].recordfile);
//				$("#downBtn").attr("href","/WbjUI/servlet/download?fPath="+result[0].recordfile);
//				$("#downBtn").click();
//			}else if(result.length>1){
			}else{
				showpop("setting3");
				var html="";
				$.each(result,function(i,item){
					
					html=html+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='jaavascript:;' onclick='openDownWin(\""+result[i].recordfile+"\")'>下载【"+(i+
							1)+"】</a>"
				});
				$("#Context").empty();
				$("#Context").append(html);
			}
			
			
		 } else{
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

function StrToLow(string,length,start){
	if(start==null){
		start=0;
	}
	return string.substring(start,length);
}
function openDownWin(file){
	window.open("/WbjUI/servlet/download?fPath="+file,"_blank");
}

function getStatus(status){
	if(status==null){
		return "未知";
	}else if(status=="0"){
		return "无效";
	}else if(status=="1"){
		return "待调查";
	}else if(status=="2"){
		return "已完成";
	}else if(status=="3"){
		return "呼叫中";
	}else{
		return "未知";
	}
};


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
		return '未知';
	}else{
		return val;
	}
}
//显示状态
function showStatus(val){
	if(val==null||val==''){
	return '未知';
	}else if(val==1){
	return '有效';	
	}else if(val==2){
	return '调查中';	
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

