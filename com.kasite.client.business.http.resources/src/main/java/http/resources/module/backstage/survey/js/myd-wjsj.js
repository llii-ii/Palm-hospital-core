	var session = null;
	var SubjectId = 0;
 
	$(function(){
		SubjectId = QueryString('SubjectId');
		if(Commonjs.isEmpty(SubjectId)){
			ComWbj.artTips("提示","warning","未知问卷ID",2,null);
//			ComWbj.alertIconNo('提示：','未知问卷ID','warning');
			history.go(-1);
		}
		init();
	});
	
	function QueryString(val) {
		var uri = window.location.search;
		var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
		return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
	}
	
	//初始化 
	function init(){
		getWJData();
	}
		
	function backToEdit(){//返回问卷编辑
		window.location.href = 'myd-wjbj.html?SubjectId='+SubjectId;
	}
	
	//获取问卷数据
  	function getWJData(){
  		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = SubjectId;
		param.reqParam = Commonjs.getApiReqParams(Service);
  		Commonjs.ajaxSync('/survey/querySubjectById.do',param,function(_d){
  			initHtml(_d.Data[0]);
  		});
  	}
	
	function initHtml(d){
		var type = 0;
		if(!Commonjs.isEmpty(d.Overtype) && d.Overtype != 0){
			type = d.Overtype;
		}else  type = 3;
		
		$('input[name="endtime"]').each(function(i){
			$('#endTime'+(i+1)).val('');
			if((i+1) != 3){
				if(type == (i+1)){
					$(this).attr('checked','checked');
					$('#endTime'+(i+1)).removeAttr('disabled');
				}else{
					$(this).removeAttr('checked');
					$('#endTime'+(i+1)).attr('disabled','');
				}
			}else{
				$('#endTime'+type).attr('checked','checked');
			}
		});
		if(type == 1){
			$('#endTime'+type).val(d.Quantity);
		}else if(type == 2){
			$('#endTime'+type).val((d.Overtime.length>16?d.Overtime.substring(0,16):d.Overtime));
		}
		var Replytype = 0;
		if(!Commonjs.isEmpty(d.Replytype) && d.Replytype != 0){
			Replytype = d.Replytype;
		}else Replytype = 3;
	 	
		$('#Overtype').val(type);
		$('#Replytype').val(Replytype);
		
		$('input[name="replysetting"]').each(function(i){
			if(Replytype == (i+1)){
				$('#Replytype'+Replytype).attr('checked','checked');
			}else $('#Replytype'+(i+1)).removeAttr('checked');
		});
		
	}

	function checkit(o,type){
		for(var i = 1 ;i<=3;i++){
			$('#endTime'+i).val('');
			$('#endTime'+i).attr('disabled','');
		}
		if(o.checked == true){
			$('#'+$(o).attr('key')).removeAttr('disabled');
			if($(o).attr('key') == 'endtime2'){
			}
		}
		$('#Overtype').val(type);
		$('#endTime3').removeAttr('disabled');
	}
	
	function checkit2(type){
		$('#Replytype').val(type);
	}
	
	function _submitWJ(){//发布
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = SubjectId;
  		var type = $('#Overtype').val();
  		Service.OverType = type;
  		Service.ReplyType = $('#Replytype').val();
  		if(type == 1){
  			if(Commonjs.isEmpty($('#endTime'+type).val())){
//  				ComWbj.alertIconNo('提示：','请填写具体数量','warning');
  				ComWbj.artTips("提示","warning","收集数量请填写大于0的整数",1.5,null);
  				return ;
  			}else if($('#endTime'+type).val() == 0){
//  				ComWbj.alertIconNo('提示：','请填写具体数量','warning');
  				ComWbj.artTips("提示","warning","收集数量请填写大于0的整数",1.5,null);
  				return ;
  			}
  			Service.Quantity = $('#endTime'+type).val();
  		}else if(type == 2){
  			if(Commonjs.isEmpty($('#endTime'+type).val())){
  				ComWbj.artTips("提示","warning","请填写截止日期",2,null);
//  				ComWbj.alertIconNo('提示：','请填写截止日期','warning');
  				return ;
  			}
  			Service.OverTime = $('#endTime'+type).val();
  		}
  		var param = {};
  		param.reqParam = Commonjs.getApiReqParams(Service);
  		Commonjs.ajaxSync('/survey/updateSubjectForNet.do',param,function(_d){//
  			if(_d.RespCode == 10000){
  	  	 		
  	  	 		art.dialog({
  			 		id: 'testID',
  			 	    width: '245px',
  			 	    height: '109px',
  			 	    content: '你确定要发布该问卷吗？',
  			 	    lock: true,
  			 	    button: [{
  			 	      	name: '确定',
  			 	       	callback: function () {
  			 	    		updateSubject(2);
  							ComWbj.artTips("提示：","succeed","发布成功",2,null);
  							setTimeout(function(){
  								window.location.href = 'myd-wjfb.html?SubjectId='+SubjectId;
  							},2000);
  			 	       	}
  			 	    },{
  			 	 		name: '取消',
  			 	 		callback: function(){
  			 	    		updateSubject(1);
  							//ComWbj.artTips("提示：","succeed","操作成功",2,null);
  							setTimeout(function(){
  								window.location.href = 'myd-wjlb.html?SubjectId='+SubjectId;
  							},2000);
  			 	 		}
  			 	 	}]
  		 	    });
  			}else{
  				ComWbj.artTips("提示","warning","操作异常",2,null);
  			}
  		});
	}
	
	function updateSubject(val){//发布
  		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = SubjectId;
		Service.Status = val;
		Service.OperatorId = Commonjs.getUserID();
    	Service.OperatorName = Commonjs.getUserName();
		param.reqParam = Commonjs.getApiReqParams(Service);
  		Commonjs.ajaxSync('/survey/updateSubjectbegin.do',param,null);
	}