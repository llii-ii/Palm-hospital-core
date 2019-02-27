	var session = null;
	var SubjectId = 0;
 	var PageSize = 10;
	$(function(){
		$("#pagenumber").val(1);
		queryWJData(1);
	});
	
	function queryWJData(num){
//		 ComWbj.openPG();
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		var page = {};
		page.PIndex = num;
		page.PSize = PageSize;
		Service.Page = page;
		var param = {};
		param.reqParam = Commonjs.getApiReqParams(Service);
		Commonjs.ajaxSync('/survey/querySubjectlistByOrgId.do',param,function(d){
			Page(d.Page.PCount,PageSize,'pager');
			if(!Commonjs.isEmpty(d.Data)){
				initHtml(d.Data);
			}else{
				window.location.href = 'myd-cjwj.html';
			}
		});
	}
	
	function initHtml(d){
		var html = '';
		$.each(d,function(i,o) {
			html+= '<tr><td>'+getContent(o.SubjectTitle,15,'...')+'</td>';
			html+= '<td>'+geWJType_CN(o.Objtype)+'</td>';
			html+= '<td>'+getContent(o.CreateTime,16)+'</td>';
			html+= '<td>'+getContent(o.Overtime,16)+'</td>';
			html+= '<td>'+o.Countsample+'</td>';
			var type = getTypeByState(o);
			if(type == 3){
				 html+= '<td><select name="" id="'+o.SubjectId+'" disabled="disabled">'+getOptionByState(o)+'</select></td>';
			}else 
				 html+= '<td><select name="" id="'+o.SubjectId+'" onchange="changeType(this,'+o.SubjectId+',window)">'+getOptionByState(o)+'</select></td>';
	        html+= '<td class="td-act">';
	       	if(type == 3){
	        	html+= '<a  style="color:#666666">编辑</a>';
	        	html+= '<a  style="color:#666666">设置</a>';     
	        }else{
	        	html+= '<a href="javascript:;" onclick="editSubject(window,'+o.SubjectId+','+type+')">编辑</a>';
	        	html+= '<a href="javascript:;" style="color:#4095ce" onclick="editSubjectParam('+o.SubjectId+',window)">设置</a>';       
	        }
	        if(type == 1){
	        	 html+= '<a style="color:#666666" target="_blank" >问卷链接</a>';   
	        }else html+= '<a href="javascript:;" style="color:#4095ce"  onclick="linkUrl('+o.SubjectId+')">问卷链接</a>';         
	        html+= '<a href="javascript:;" style="color:#4095ce" onclick="tj('+o.SubjectId+')">统计</a>';       
	        html+= '<a href="javascript:;" style="color:#4095ce" onclick="showModel('+o.SubjectId+')">预览</a>';
//	     	html+= '<a href="javascript:;" style="color:#4095ce" onclick="copy('+o.subjectId+')">复制</a>';
	     	html+= '<a href="javascript:;" style="color:#4095ce" onclick="deleteSubject('+o.SubjectId+',0,\''+o.SubjectTitle+'\')">删除</a>';
	        html+= '</td></tr>';
		});
	 	$("#dataList tr").eq(0).nextAll().remove();
		$(html).insertAfter($("#dataList tr").eq(0));
//		 ComWbj.closePG();
	}
	
	function linkUrl(id){
		window.location.href = 'myd-wjfb.html?SubjectId='+id;
		//o.href = 'http://satisfy.yihu.com/web/SurveyHome.aspx?platformType=17&sourceType=1&sourceId='+session.orgid;
	}
	
	function getContent(s,subsize,showText){
		if(isEmpty(s)){
			return '';
		}else{
			if(showText){
				if(s.length > subsize){
					return s.substring(0,subsize)+showText;
				}else return s;
			}else{
				if(s.length > subsize){
					return s.substring(0,subsize);
				}else return s;
			}
		}
	}

	function editSubject(o,id,type){
		if(type == 2){
			art.dialog({
		 		id: 'testID',
		 	    width: '245px',
		 	    height: '109px',
		 	    content: '需要停止发布该份问卷才能进行编辑，您确定吗？',
		 	    lock: true,
		 	    button: [{
		 	      	name: '确定',
		 	       	callback: function () {
		 	       		updateSubject(id,1,false,true);
		 	       		o.location.href = '../survey/myd-wjbj.html?SubjectId='+id;
		 	       	}
		 	 	},{
		 	 		name: '取消',
		 	       	callback: function () {
		 	       		queryWJData(1);
		 	       	}
		 	 	}]
		 	});
		}else
		window.location.href = 'myd-wjbj.html?SubjectId='+id;
	}
	
	function tj(id){
		window.location.href = 'myd-wjtj.html?SubjectId='+id;
	}
		
	function showModel(id){
		window.location.href = 'myd-wjyl.html?SubjectId='+id;
	}
	
	function editSubjectParam(id,o){
		var flag = judgeQuestSumBySubjectid(id);
		if(flag){
			window.location.href = 'myd-wjsj.html?SubjectId='+id;
		}else{
			art.dialog({
		 		id: 'testID',
		 	    width: '245px',
		 	    height: '109px',
		 	    content: '该问卷还未设置问题，是否先设置问题？',
		 	    lock: true,
		 	    button: [{
		 	      	name: '确定',
		 	       	callback: function () {
		 	       		o.location.href = '../survey/myd-wjbj.html?SubjectId='+id;
		 	       	}
		 	 	},{
		 	 		name: '取消',
		 	       	callback: function () {
		 	       		queryWJData(1);
		 	       	}
		 	 	}]
		 	});
		}
	}
	
	function changeType(t,id,o){
		if(t.value == 2){
			changeType2(id,t.value,o);
		}else if(t.value == 3){
			changeType3(id,t.value);
		}else if(t.value == 1){
			changeType1(id,t.value);
		}
	}
	
	function changeType1(id,type){
		art.dialog({
	 		id: 'testID',
	 	    width: '245px',
	 	    height: '109px',
	 	    content: '您要停止收集该问卷吗？',
	 	    lock: true,
	 	    button: [{
	 	      	name: '确定',
	 	       	callback: function () {
	 	       		updateSubject(id,type);
	 	       	}
	 	 	},{
	 	 		name: '取消',
		 	       	callback: function () {
		 	       		queryWJData(1);
		 	       	}
	 	 	}]
	 	});
	}
	
	function changeType3(id,type){
		art.dialog({
	 		id: 'testID',
	 	    width: '245px',
	 	    height: '109px',
	 	    content: '您确定要结束该问卷吗？结束后不能编辑和设置问卷',
	 	    lock: true,
	 	    button: [{
	 	      	name: '确定',
	 	       	callback: function () {
	 	       		updateSubject(id,type);
	 	       	}
	 	 	},{
	 	 		name: '取消',
		 	       	callback: function () {
		 	       		queryWJData(1);
		 	       	}
	 	 	}]
	 	});
	}

	function changeType2(id,type,o){
		if(judgeQuestSumBySubjectid(id)){
			updateSubject(id,type);
		}else{
			art.dialog({
		 		id: 'testID',
		 	    width: '245px',
		 	    height: '109px',
		 	    content: '该问卷还未设置问题，是否先设置问题？',
		 	    lock: true,
		 	    button: [{
		 	      	name: '确定',
		 	       	callback: function () {
		 	       		o.location.href = '../survey/myd-wjbj.html?SubjectId='+id;
		 	       	}
		 	 	},{
		 	 		name: '取消',
		 	       	callback: function () {
		 	       		queryWJData(1);
		 	       	}
		 	 	}]
		 	});
		}
	}

	function deleteSubject(id,type,name){// 
		art.dialog({
	 		id: 'testID',
	 	    width: '245px',
	 	    height: '109px',
	 	    content: '您要删除问卷“'+name+'”吗？注意：删除后无法恢复',
	 	    lock: true,
	 	    button: [{
	 	      	name: '确定',
	 	       	callback: function () {
	 	       		var Service = {};
	 	       		Service.HosId = Commonjs.getUrlParam("HosId");
	 	       		Service.SubjectId = id;
	 	       		Service.Status = type;
	 	       		Service.OperatorId = Commonjs.getUserID();
	 	       		Service.OperatorName = Commonjs.getUserName();
			  		var param = {};
			  		param.reqParam = Commonjs.getApiReqParams(Service);
			  		Commonjs.ajaxSync('/survey/updateSubjectbegin.do',param,function(_d){
			  			if(_d.RespCode == 10000){
//							 ComWbj.alertIcon('提示：','成功','succeed');
								ComWbj.artTips("提示","succeed","成功",2,null);
								queryWJData($("#pagenumber").val());
						}else{
							ComWbj.artTips("提示","warning","操作异常",2,null);
//							ComWbj.alertIconNo('提示：','操作异常','warning');
						}
			  		});
	 	       	}
	 	 	},{
	 	 		name: '取消'
	 	 	}]
	 	});
	}

	function updateSubject(id,type,f,f1){//发布
  		var Service = {};
  		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = id;
		Service.Status = type;
		Service.OperatorId = Commonjs.getUserID();
    	Service.OperatorName = Commonjs.getUserName();
    	var param = {};
  		param.reqParam = Commonjs.getApiReqParams(Service);
  		Commonjs.ajaxSync('/survey/updateSubjectbegin.do',param,function(_d){
  			if(_d.RespCode == 10000){
  	  			if(!f1)
  	  				ComWbj.artTips("提示","succeed",_d.RespMessage,2,null);
  	  			//刷新当前页面
  	  			queryWJData($("#pagenumber").val());
//  				 ComWbj.alertIcon('提示：',_d.Message,'succeed');
  			}else{
  				ComWbj.artTips("提示","warning","添加异常",2,null);
//  				ComWbj.alertIconNo('提示：','添加异常','warning');
  			}
  		});
	}

	function judgeQuestSumBySubjectid(id){
		var flag = false;
		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = id;
		Service.OperatorId = Commonjs.getUserID();
    	Service.OperatorName = Commonjs.getUserName();
		param.reqParam = Commonjs.getApiReqParams(Service);
		Commonjs.ajaxSync('/survey/judgeQuestSumBySubjectid.do',param,function(_d){
			if(_d.RespCode == 10000){
				var count = _d.Data[0].Count;
				if(Number(count) > 0){
					flag = true;
				}else {
					flag = false;		
				}
			}else{
				ComWbj.artTips("提示","warning","添加异常",2,null);
//				ComWbj.alertIconNo('提示：','添加异常','warning');
				flag = false;
			}
		});
		return flag;
	}

	function getOptionByState(o){
		
		var html = '';
		if(o.Status == 3){
			return getOptionHtml(3);
		}else{
			if(isEmpty(o.Overtype)){
				return getOptionHtml(o.Status);
			}else if(o.Overtype == 3){//不限
				return getOptionHtml(o.Status);
			}else if(o.Overtype == 2){//时间
				if(o.Overtime){
					if(compareDate(o.Overtime,getDateStr())){
						return getOptionHtml(o.Status);
					}else{
						return getOptionHtml(3);
					}
				}
				return getOptionHtml(o.Status);
			}else if(o.Overtype == 1){//数量
				if(Number(o.Countsample) >= Number(o.Quantity)){
				//if(o.countsample >= o.quantity){
					return getOptionHtml(3);
				}else{
					return getOptionHtml(o.Status);
				}
			}else{
				return getOptionHtml(1);
			}
		}
		return html;
	}
	
	function getTypeByState(o){
		if(o.Status == 3){
			return 3;
		}else{
			if(isEmpty(o.Overtype)){
				return (o.Status);
			}else 
			if(o.Overtype == 3){//不限
				return (o.Status);
			}else if(o.Overtype == 2){//时间
				if(o.Overtime){
					if(compareDate(o.Overtime,getDateStr())){
						return (o.Status);
					}else{
						return (3);
					}
				}
				return getOptionHtml(o.Status);
			}else if(o.Overtype == 1){//数量
				if(Number(o.Countsample) >= Number(o.Quantity)){
				//if((o.countsample) >= (o.quantity)){
					return (3);
				}else{
					return (o.Status);
				}
			}else{
				return (1);
			}
		}
	}
	
	function compareDate(d1,d2){
//		var start=new Date(d1.replace("-", "/").replace("-", "/"));  
//	    var end=new Date(endTmp.replace("-", "/").replace("-", "/"));
		var start=new Date(d2.substr(0,10));  
	    var end=new Date(d1.substr(0,10));
	 	return (end.getTime()>start.getTime());
	}
	
	//暫時停用
	function copy(id){
	
		var param = {};
		param.SubjectId = id;
		param.OrgId = Commonjs.hospitalId();//
		param.OrgName = Commonjs.hospitalName();//
		param.Examtype = 4;//网络
		Commonjs.ajax('/survey/examCopy.do',param,function(){
			if(_d.RespCode == 10000){
				ComWbj.artTips("提示","warning",_d.RespMessage,2,null);
//				ComWbj.alertIconNo('提示：',_d.Message,'warning');
				queryWJData(1); 	  		
			}else{
				ComWbj.artTips("提示","warning","添加异常",2,null);
//				ComWbj.alertIconNo('提示：','添加异常','warning');
			}
		});
	}
	
	function getDateStr(){
		var dd = new Date();
		var y = dd.getFullYear();
		var m = dd.getMonth()+1;//获取当前月份的日期
		var d = dd.getDate();
		var h = dd.getHours();
		var ms = dd.getMinutes();
		if(m < 10) m = "0"+m;
		if(d<10) d = "0"+d;	
		if(h<10) h = "0"+h;	
		if(ms<10) ms = "0"+ms;	
	 	return y+"-"+m+"-"+d+' '+h+':'+ms;
	}
 
	
	function getOptionHtml(type){
		var html = '';
		var s = 'selected="selected"';
		for(var i = 1;i<=3;i++){
			if(i == type){
				html += '<option value="'+i+'" '+s+'>'+geWJStatus_CN(i)+'</option>';
			}else 
				html += '<option value="'+i+'">'+geWJStatus_CN(i)+'</option>';
		}
		return html;
	}
	
	function geWJStatus_CN(type){
		var name = '';
		switch(type){
			case 1:name = '未发布';break;
			case 2:name = '收集中';break;
			case 3:name = '已结束';break;
			default : name='未知';break;
		}
		return name;
	}
	
	function geWJType_CN(typeV){
		var name = '';
		if (typeV=="1") {
			name = '门诊患者';
		}else if (typeV=="2") {
			name = '住院患者';
		}else if (typeV=="3") {
			name = '手术患者';
		}else if (typeV=="4") {
			name = '体检患者';
		}else if (typeV=="5") {
			name = '其他';
		}else{
			name = '未知';
		}
		return name;
	}


	//分页
	function Page(totalcounts,pagecount,pager) {
		$("#"+pager).pager( {
			totalcounts : totalcounts,
			pagesize : pagecount,
			pagenumber : $("#pagenumber").val(),
			pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
			buttonClickCallback : function(al) {
				$("#pagenumber").val(al);
				queryWJData(al);
			}
		});
	}
	
    function _ajax(url,param,flag){
		
		var obj = null;
		try{
			$.ajax({
				type: 'POST',
				url: url,
				data: param,
				async: flag,
				timeout : 8000,
				dataType: 'json',
				success: function(data){
					obj = data;
	 			}
			});
		}catch(err){
			ComWbj.artTips("提示","error",err,2,null);
//			ComWbj.alertIconNo('提示：',err,'error');
		}
		if(!flag) return obj;
	}
        
  	function isEmpty(s){
  		
  		if(s == undefined){
  			return true;
  		}else{
  			if(s == null || s == '' ||
  				s == 'null' || s.length < 1){
  				return true;
  			}
  		}
  		return false;
  	}         
                 
                     
                         
                         	
                         
                         
                             
                          	 
                          	 
                             
                             
                             
                         
            
         	
         
             
             
