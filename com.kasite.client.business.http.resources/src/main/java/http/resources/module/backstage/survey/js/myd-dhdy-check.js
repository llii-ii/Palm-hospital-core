$( function() {
	 init();
});
var session = YihuUtil.getSession();
//判断是否选择问卷
function judgeCheckVal(){
	var checkVal= $("input[name='dhdy']:checked").val();
	if(checkVal==null||checkVal==''){
		alertwmk("提示","请先选择一份问卷","warning");
	}else{
		window.location="myd-dhdy-collect.html?SubjectId="+checkVal;
	}
}
function init(){
	judgeSurvey();
}
function judgeSurvey(){
	var param = {};
	 ComWbj.openPG();
	param.Api="survey.SurveyApiImpl.querySubjectForNetList";
	param.Param = "{'OrgId':"+ session.orgid +",'PageSize':"+$("#pagesize").val()+",'PageStart':"+$("#pagenumber").val()+"}";
	doAjaxLoadData("../MydDhdy_doAll.do", param, function(resp) {
		 if(resp.Code == 10000){
			 
			 if(resp.result.length>0){
				 listSurvey=resp.result;
				 Page(resp.totalProperty,$("#pagesize").val() ,getTotalPage(resp.totalProperty, $("#pagesize").val()),'pager');
				 $("#tableShow tr:not(:first):not(:last)").remove();
				 var html="";
				 $.each(listSurvey,function(i,item){
		        	html=html+'<tr>'+
		        	 '<td><label class="valign-m-chk"><input type="radio" name="dhdy" value="'+item.subjectid+'" /><span>'+strToLowStr(item.subjecttitle,15)+'</span></label></td>'+
                     ' <td>'+item.createtime+'</td>'+
                     ' <td class="td-act"><a href="myd-wjyl.html?SubjectId='+item.subjectid+'"><i class="icon icon-preview"></i>预览</a></td>'+
			         '</tr>';
		         });   
				 $(html).insertAfter($("#tableShow tr").eq(0));
//                  alertwmk("成功",resp.Result,"succeed");
			 }else{
				 window.location="myd-dhdy-w.html";
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


//截取时间
function StrToLow(string,length,start){
	if(start==null){
		start=0;
	}
	return string.substring(start,length);
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

