$( function() {
	closepop('setting3');
//	closepop('loadingV');
//	add();
	//禁用回退键
	window.onload=function(){  
	    /**************************** 
	     * 作者：q821424508@sina.com   * 
	     * 时间：2012-08-20            * 
	     * version：2.1              * 
	     *                          * 
	     ****************************/  
	    document.getElementsByTagName("body")[0].onkeydown =function(){  
	          
	        //获取事件对象  
	        var elem = event.relatedTarget || event.srcElement || event.target ||event.currentTarget;   
	          
	        if(event.keyCode==8){//判断按键为backSpace键  
	          
	                //获取按键按下时光标做指向的element  
	                var elem = event.srcElement || event.currentTarget;   
	                  
	                //判断是否需要阻止按下键盘的事件默认传递  
	                var name = elem.nodeName;  
	                  
	                if(name!='INPUT' &&name!='SPAN' && name!='TEXTAREA'){  
	                    return _stopIt(event);  
	                }  
	                var type_e = elem.type.toUpperCase();  
	                if(name=='INPUT' && (type_e!='TEXT' && type_e!='TEXTAREA' && type_e!='PASSWORD' && type_e!='FILE')){  
	                        return _stopIt(event);  
	                }  
	                if(name=='INPUT' && (elem.readOnly==true || elem.disabled ==true)){  
	                        return _stopIt(event);  
	                }  
	            }  
	        }  
	    }  
	function _stopIt(e){  
	        if(e.returnValue){  
	            e.returnValue = false ;  
	        }  
	        if(e.preventDefault ){  
	            e.preventDefault();  
	        }                 
	  
	        return false;  
	}  
});

var session = YihuUtil.getSession();
var SubjectId=YihuUtil.queryString('SubjectId');
var SubjectIdNew='';
var connectList={};
var connectListNew={};
function uploadData(){
		var fileName = $('#upload').val();
		var bb=judgeFileName(fileName);
		if(bb=="1"){
			alertwmk("提示","还未选择文件","warning");
			return;
		}else if(bb=="2"){
			alertwmk("提示","请选择xls的样本文件","warning");
			return;
		}
//        showpop("loadingV");
        ComWbj.openPG();
		if(fileName){
		var fileID = ['upload'];
		$.yihu.ajaxFileUpload({
		 	url:'../MydDhdy_fileUpload.do?orgID='+session.orgid,
		    secureuri:false,
		    fileElementId: fileID,
		    dataType: 'json',
		    success: function (d,s){
			if(d.code==10000){
			connectListNew=d.Result;
			var html="";
			if(connectListNew.length==0){
				ComWbj.closePG();
				alertwmk("提示","请选择有数据的样本","warning");
				return;
			}
			$.each(connectListNew,function(i,item){
//				var showI=parseInt(((i+1)/connectListNew.length)*100);
//				loadImg (showI);
				var num=$("#tableShow tr").length;
				html=html+' <tr>'+
                    '<td>'+num+'</td>'+'<td>'+
                    //判断员工名称
                    judgeName(item[0]) +'</td><td>'+
                    //判断性别
                    judgeSex(item[1])+
                    '</td><td>'+
                    //判断电话
                    judgePhone(item[2])+
                    '</td><td>'+
                    //判断就诊科室
                    judgeJzks(item[3])+
                    '</td><td>'+
                    //判断就诊医生
                    judgeJzys(item[4])+
                    '</td><td>'+
                    //判断疾病诊断
                    judgeJbzd(item[5])+
                    '</td><td>'+
                    //判断病案号
                    judgeBnh(item[6])+
                    '</td><td>'+
                    //判断其他
                    judgeQt(item[7])+
                    '</td>'+
                    '<td><input type="button" class="btn btn-w50 btn-gray mr10 btn-edit2" value="编辑" /><input type="button" class="btn btn-gray btn-w50 btn-del2" value="删除" /></td>'+
                '</tr>';
				if(connectListNew.length==(i+1)){
					$("#tableShow").append(html);
					tableSort();
				}
			});
		}else{
			alertwmk('提示',d.message,"warning");
		}
			ComWbj.closePG();
			//closepop("loadingV");
		 	},error: function (d, s, e){
		 		ComWbj.closePG();
		 		//closepop("loadingV");
		  		alertwmk('提示',e);
			} 
		});
	}else{
		alertwmk("提示","还未选择文件","warning");
		ComWbj.closePG();
		//closepop("loadingV");
		return ;
	} 
	}

//判断文件名称
function judgeFileName(fN){
	if(fN==null||fN==''){
		
		return "1";
	}else{
		var fileType=StrToLow(fN,fN.length,(fN.length-3));
		if(fileType!="xls"){
			
			return "2";
		}
		
	}
}


function StrToLow(string,length,start){
	if(start==null){
		start=0;
	}
	return string.substring(start,length);
}


//点击提交按键
function nextBut(){
	if(SubjectId==null||SubjectId==''){
		alertwmk('提示','您还为选择拷贝问卷','warning');
		return;
	}
	var needSurvey=$("#needSurvey").val();
	if(needSurvey==null||needSurvey.trim()==''){
		alertwmk('提示','请填写收集数量','warning');
		return;
	}else if(parseInt(needSurvey)==0){
		alertwmk('提示','请填写大于零的整数','warning');
		return;

	}
	if(errorList.length!=0){
		alertwmk('提示','请将错误样本修改后再提交！','warning');
		return;
	}
	if(rightList.length<needSurvey){
		alertwmk('提示','样本信息不能少于收集的数量！','warning');
		return;
	}
	showpop("setting3");
}
//点击确定按键
function submintBut(){
	var ContactPerson= trim($("#ContactPerson").val());
	if(ContactPerson==null||ContactPerson.trim()==''){
		alertwmk('提示','请填写业务联系人','warning');
		return;
	}else if(ContactPerson.length>10){
		alertwmk('提示','业务联系人长度不超过10','warning');
		return;

	}
	var ContactPhone=$("#ContactPhone").val();
	if(ContactPhone==null||ContactPhone.trim()==''){
		alertwmk('提示','请填联系人电话','warning');
		return;
	}else if(!/(^1[3,5,8]\d{9}$)|(^(\d{3,4}\-)?\d{7,8}$)|(^0(([1-9]\d)|([3-9]\d{2}))\d{8}$)/.test(ContactPhone)){
		alertwmk('提示','请填写正确的联系人电话','warning');
		return;
	}
	ComWbj.openPG();
	copySurvey();
}


//拷贝问卷
function copySurvey(){
	if(SubjectIdNew!="")
	{
		updateSurvey();
		return;
	}
	var param = {};
	param.Api="survey.SurveyApiImpl.examCopy";
	param.Param = "{'SubjectId':"+SubjectId+",'Examtype':1,'OrgId':"+session.orgid+",'OrgName':'"+session.orgname+"'}";
	doAjaxLoadData("../MydDhdy_doAll.do", param, function(resp) {
		 if(resp.Code == 10000){
			 if(resp.SubjectId!=null){
				 SubjectIdNew=resp.SubjectId; 
				 updateSurvey();
			 }
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
//修改问卷
function updateSurvey(){

	var param = {};
	param.SubjectId=SubjectIdNew;
	param.OperatorId=session.userId;
	param.OperatorName=session.operatorname;
	param.Quantity=$("#needSurvey").val();
	param.ContactPerson=$("#ContactPerson").val();
	param.ContactPhone=$("#ContactPhone").val();
	param.OrgId=session.orgid;
	param.OrgName=session.orgname;
	param.Status=4;
	doAjaxLoadData("../MydDhdy_updateSurvey.do", param, function(resp) {
		 if(resp.Code == 10000){
			 var paraset=[];
			$.each(rightList,function(i,item){
				var tdLi=$(item).find("td");
				var param2 = {};
				param2.SubjectId=SubjectIdNew;
				param2.OrgId=session.orgid;
				param2.UserName=$(tdLi[1]).find("span").text();
				param2.Moblie=$(tdLi[3]).find("span").text();
				param2.Sex=judgeSexToInt($(tdLi[2]).find("span").text());
				param2.OperatorId=session.userId;
				param2.OperatorName=session.operatorname;
				param2.DeptName=$(tdLi[4]).find("span").text();
				param2.DoctorName=$(tdLi[5]).find("span").text();
				param2.TreatRes=$(tdLi[6]).find("span").text();
				param2.CaseHistoryId=$(tdLi[7]).find("span").text();
				param2.Other=$(tdLi[8]).find("span").text();
				paraset[i]=param2;
			});
			addSamples(paraset);
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
 function judgeSexToInt(val){
	 if(val=='男'){
		 return "1";
	 }else if(val=='女'){
		 return "2";
	 }else{
		 return "3";
	 }
 }

function arrayToJson(oldArr) { 
	  var newJson = {};
      for ( var i = 0; i < oldArr.length; i++)
      {
          if (!newJson[oldArr[i].date])
          {
              newJson[oldArr[i].date] =
              {
                  "date" : oldArr[i].date
              };
          }
          if (!!newJson[oldArr[i].date]["data"])
          {
              newJson[oldArr[i].date]["data"].push (oldArr[i]);
          }
          else
          {
              newJson[oldArr[i].date]["data"] = [oldArr[i]];
          }
      }
      return newJson; 
	}
//添加单体样本
function addSamples(paraset){
	var  param={};
    param.Result=$.toJSON(paraset);
    ComWbj.closePG();
	 closepop('setting3');
	 alertwmk('提示','该问卷已提交审批,商务人员会在一个工作日内与您确认！','succeed');
	 setTimeout(function(){
		 window.location="myd-dhdy-list.html";
	 },2000);
//	 param.Result=$.(paraset).serializeArry()
	doAjaxLoadData("../MydDhdy_addSamples.do", param, function(resp) {
		 if(resp.Code == 10000){
			
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

//判断姓名
function judgeName(val){
	if(val==null||val==""){
	return '<span class="imp-name dataerror" data-alt="名字不能为空">&nbsp;</span>' ;	
	}else if(val.length>20){
		return '<span class="imp-name dataerror" data-alt="名字长度不大于20">'+val+'</span>' ;	
	}
	else{
		return '<span class="imp-name">'+val+'</span>' ;		
	}
}
//判断姓名
function judgeSex(val){
	if(val==null||val==""){
	return '<span class="imp-sex dataerror" data-alt="性别不能为空">&nbsp;</span>' ;	
	}else if(val!="男"&&val!="女"&&val!="未知"){
		return '<span class="imp-sex dataerror" data-alt="请填写男、女或未知">'+val+'</span>' ;	
	}
	else{
		return '<span class="imp-sex">'+val+'</span>' ;		
	}
}
//判断电话
function judgePhone(val){
	if(val==null||val==""){
	return '<span class="imp-phone dataerror" data-alt="电话不能为空">&nbsp;</span>' ;	
	}else if(!/(^1[3,5,8]\d{9}$)|(^(\d{3,4}\-)?\d{7,8}$)|(^0(([1-9]\d)|([3-9]\d{2}))\d{8}$)/.test(val)){
		return '<span class="imp-phone dataerror" data-alt="填写正确的电话号码">'+val+'</span>' ;	
	}
	else{
		return '<span class="imp-phone">'+val+'</span>' ;		
	}
}

//判断就诊科室
function judgeJzks(val){
	if(val.length>40){
		return '<span class="imp-jzks dataerror" data-alt="就诊科室长度不大于40">'+val+'</span>' ;	
	}
	else{
		return '<span class="imp-jzks">'+val+'</span>' ;		
	}
}

//判断就诊医生
function judgeJzys(val){
	if(val.length>20){
		return '<span class="imp-jzys dataerror" data-alt="就诊医生长度不大于20">'+val+'</span>' ;	
	}
	else{
		return '<span class="imp-jzys">'+val+'</span>' ;		
	}
}

//判断疾病诊断
function judgeJbzd(val){
	if(val.length>800){
		return '<span class="imp-jbzd dataerror" data-alt="疾病诊断长度不大于800">'+val+'</span>' ;	
	}
	else{
		return '<span class="imp-jbzd">'+val+'</span>' ;		
	}
}

//判断病案号
function judgeBnh(val){
	if(val.length>20){
		return '<span class="imp-bnh dataerror" data-alt="病案号长度不大于20">'+val+'</span>' ;	
	}
	else{
		return '<span class="imp-bnh">'+val+'</span>' ;		
	}
}


//判断病案号
function judgeQt(val){
	if(val.length>800){
		return '<span class="imp-qt dataerror" data-alt="其他长度不大于800">'+val+'</span>' ;	
	}
	else{
		return '<span class="imp-qt">'+val+'</span>' ;		
	}
}

//自定义弹出框
function alertwmk(a, b,icon) {
	ComWbj.artTips(a,icon,b,2,null);
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

function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
function setTime() {
	setTimeout( function() {
	}, 2000);
}

