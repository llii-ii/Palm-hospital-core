var selectedMember;
var hiskeys="";
var LabNames="";
$(function(){
	//就诊人控件
	$("#filter-jzr").memberPicker({
		isMustCardNo : true,
		cardType:Commonjs.constant.cardType_1,
		pickerOnClose:function(obj,member){
			selectedMember = member;
			GetLabList();
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			GetLabList();
		}
	})
//	Commonjs.memberPicker({
//		cardType:Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		//默认选中就诊人，格式cardno,cardtype,membername
//		pickerOnClose:function(obj,member){
//			selectedMember = member;
//			GetLabList();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			GetLabList();
//		}
//	});
});

/// <summary>
/// 获取一级科室列表
/// </summary>
function GetLabList() {
	hiskeys="";
	var param = {};
	var apiData = {};
	var cardNo = selectedMember.CardNo;
	var mid = selectedMember.MemberId
	apiData.CardNo = cardNo;
	apiData.MemberId = mid;
	apiData.CardType=1;
	param.api = '';
	param.apiParam = Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/yy/QueryExamItemList/callApi.do';
	Commonjs.ajax(url, param, function(d) {
		 if(d.RespCode==10000){
			 var listheml='';
			 Commonjs.BaseForeach(d.Data, function(i, item) {
					listheml += '<li>';
					listheml += '<div class="apl_mess2 boxs">';
					listheml += '<p>项目：' + item.LabName + '</p>';
					listheml += '<p>卡号：' + cardNo + '</p>';
					listheml += '<p onclick="specShow(this)" class="skillbox">注意事项：'
							+ item.FriendlyReminder + '</p>';
					listheml += '<div class="apl_btn">';
					if(item.IsBook=='1'){								
						listheml += '<a onclick="gotoUrl(\'yjyy_numlist.html?patientType='+1+'&mid='+mid+'&patientNo='+cardNo+'&labCode='+item.LabCode+'&HisKey='+item.HisKey+'&departCode='+item.BillDept+'&ExamDept='+item.ExamDept+'&LabName='+escape(item.LabName)+'\')">马上预约</a>';
						hiskeys+=item.HisKey+",";
						LabNames+=item.LabName+",";
					}
					else{
						var LabName = item.LabName;
						if(item.PrintAppointmentID==0){//取消主项目的时候，会将子项目一起取消
							 Commonjs.BaseForeach(d.Data, function(i, item2) {
								if(item2.PrintAppointmentID==item.OrderId){//主项目
									LabName=LabName+","+item2.LabName;
								}
							})
						}
						listheml += '<a onclick="gotoUrl(\'bookinfo.html?HisKey='+item.HisKey+'&LabName='+escape(LabName)+'&CardType='+1+'\')">查看详情</a>';
					}
					listheml += '</div>';
					listheml += '</div>';
					listheml += '</li>';
      	 });
         	 $("#ULList").html(listheml);
         	
        	 if(listheml!=''){
        		$(".nomess").hide();
        	 }
        	 else{
        		 $(".nomess").show();
        		 $("#btnbook").hide();
        	 }
		 }
     	else{
     		$("#btnbook").hide();
     	}
		 });
}
function gotoUrl(url){
	location.href = Commonjs.goToUrl(url)
}
//挂号操作
function BookService() {
	var param = {};
	var apiData = {};
	var cardNo = selectedMember.CardNo;
	var cardType = selectedMember.CardType;
	var mid = selectedMember.MemberId
	apiData.MemberId = mid;
	apiData.CardType=1;
	apiData.CardNo=cardNo;
	apiData.HisKey=hiskeys;
	apiData.LabName = escape(LabNames);
	param.api = '';
	param.apiParam = Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/yy/MedicalAppoint/callApi.do';
	Commonjs.ajax(url, param, function(d) {
		 if (d.RespCode == 10000) {
         	window.location.reload();
		 }
         else {
             alert(d.RespMessage+"预约失败");
         }
	})
	
}
function specShow(is) {
	if($(is).attr('class')=='skillbox'){
		 $(is).removeClass("skillbox");
	}
	else{
		$(is).addClass("skillbox");
	}
    
}

