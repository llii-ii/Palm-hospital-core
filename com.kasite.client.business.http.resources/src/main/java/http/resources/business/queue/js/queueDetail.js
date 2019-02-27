var DoctorName;
var DeptName;
var Location;
var No;
var MaxNo;
var QueryId;
var WaitNum;
var CardNo;
var MemberName;
var CIfMsg;
var CReMindNo;
var RegisterDate;
var TimeId;
var PhoneNo;
var NextNo;
var PhoneNo;


var SelectNo;

$(function() {

	DoctorName = unescape(Commonjs.getUrlParam('DoctorName'));
	DeptName = unescape(Commonjs.getUrlParam('DeptName'));
	Location = unescape(Commonjs.getUrlParam('Location'));
	No = Commonjs.getUrlParam('No');
	MaxNo = Commonjs.getUrlParam('MaxNo');
	QueryId = Commonjs.getUrlParam('QueryId');
	CardNo = Commonjs.getUrlParam('CardNo');
	MemberName = unescape(Commonjs.getUrlParam('MemberName'));
	CIfMsg = Commonjs.getUrlParam('IfMsg');
	CReMindNo = Commonjs.getUrlParam('ReMindNo');
	RegisterDate=Commonjs.getUrlParam('RegisterDate');
	TimeId=Commonjs.getUrlParam('TimeId');
	PhoneNo=Commonjs.getUrlParam('PhoneNo');
	NextNo=Commonjs.getUrlParam('NextNo');
	CardType=Commonjs.getUrlParam('CardType');
	
	$("#jzrword").html(MemberName);
	$("#jzkid").html("就诊卡：" + CardNo);
	$("#DoctorName").html(DoctorName);
	$("#DeptName").html(DeptName);
	$("#Location").html(Location);
	$("#No").html(No);
	$("#MaxNo").html(MaxNo);

	WaitNum = No - MaxNo;

	$("#WaitNum").html(WaitNum);
	if(WaitNum <= 0){
		$("#noWaitNum").html("");
	}

	if (CIfMsg == 1) {
		$("#wait-number").val(CReMindNo);
//		if(!Commonjs.isEmpty(CReMindNo)&&CReMindNo<=MaxNo){
//			$("#warmCheck").hide();
//		}
	}
//	if(WaitNum<=1){
//		//等待人数小于等于1位不需要提醒
//		$("#warmCheck").hide();
//	}

	if (WaitNum > 1) {
		var RemindArray = new Array();
		var nowNo = MaxNo;
		for ( var i = 0; i < WaitNum - 1; i++) {
			var temp = ++nowNo;
			RemindArray.push(temp);
		}
		$('.input-group-switch').on('click', function() {
			var checked = $(this).find('input').is(':checked');
			if (checked) {
				$('.wo-bot').show();
				if (SelectNo == '' || SelectNo == null) {
					if (CReMindNo == '' || CReMindNo == null) {
						$("#wait-number").val(MaxNo);
						SetReMindNo(No>1?No-1:1, "1");
					}else
					{
						$("#wait-number").val(CReMindNo);
						SetReMindNo(CReMindNo, "1");
					}
				} else {
//					if (CReMindNo == '' || CReMindNo == null) {
						SetReMindNo(SelectNo, "1");
//					}else
//					{
//						$("#wait-number").val(CReMindNo);
//						SetReMindNo(CReMindNo, "1");
//					}
				}
			} else {
					myLayer.clear();
					myLayer.confirm({
				        con:'您确认要关闭智能提醒，关闭可能错过就诊序号？',
				        ok: function(){
				        	$('.wo-bot').hide();
							if (SelectNo == '' || SelectNo == null) {
								if (CReMindNo == '' || CReMindNo == null) {
									$("#wait-number").val(MaxNo);
									SetReMindNo(MaxNo+1, "0");
								}else
								{
									$("#wait-number").val(CReMindNo);
									SetReMindNo(CReMindNo, "0");
								}
							} else {
//								if (CReMindNo == '' || CReMindNo == null) {
									SetReMindNo(SelectNo, "0");
//								}else
//								{
//									$("#wait-number").val(CReMindNo);
//									SetReMindNo(CReMindNo, "0");
//								}
							}
				        	
				        },
						cancel:function (){
							$("#warmCheck").prop("checked",true);
				        	return ;
				        	}
				    });
				
				
			}
		});
		
		if(Commonjs.isEmpty(CReMindNo)||Number(CReMindNo)>Number(MaxNo)){
		$("#wait-number").picker({
			title : "设置提醒号序",
			cols : [ {
				textAlign : 'center',
				values : RemindArray
			}],
			onChange : function($k, values, displayValues) {
				SelectNo = displayValues[0];
			},
			onClose : function() {
				if ($('.wo-bot').is(":hidden")) {
					SetReMindNo(SelectNo, "0");
				} else {
					SetReMindNo(SelectNo, "1");
				}
			}
		});
		}
	} else {
		$('.input-group-switch').on('click', function() {
			var checked = $(this).find('input').is(':checked');
			if (checked) {
				$("#warmCheck").attr("checked",false);
				if(WaitNum == 0){
					myLayer.tip({ con: '您的候诊号序已到达就诊号序，无法设置智能提醒' });
				}else{
					myLayer.tip({ con: '您的候诊号序已超过当前就诊号序，无法设置智能提醒' });
				}
			}else{
				$('.wo-bot').hide();
				if (SelectNo == '' || SelectNo == null) {
					if (CReMindNo == '' || CReMindNo == null) {
						$("#wait-number").val(MaxNo);
						SetReMindNo(MaxNo+1, "0");
					}else
					{
						$("#wait-number").val(CReMindNo);
						SetReMindNo(CReMindNo, "0");
					}
				} else {
//					if (CReMindNo == '' || CReMindNo == null) {
						SetReMindNo(SelectNo, "0");
//					}else
//					{
//						$("#wait-number").val(CReMindNo);
//						SetReMindNo(CReMindNo, "0");
//					}
				}
			}
		});
		

	}
	
	if (CIfMsg == 1) {
		$('.input-group-switch').find('input').attr("checked", true);
		$('.wo-bot').show();
	}



});

function SetReMindNo(ReMindNo, IfMsg) {
	
	var apiData = {};
	apiData.DoctorName = DoctorName;
	apiData.DeptName = DeptName;
	apiData.CardNo = CardNo;
	apiData.Location = Location;
	apiData.No = No;
	apiData.MaxNo = MaxNo;
	apiData.QueryId = QueryId;
	apiData.ReMindNo = ReMindNo;
	apiData.IfMsg = IfMsg;
	apiData.PatientName = MemberName;
	apiData.RegisterDate=RegisterDate;
	apiData.TimeId=TimeId;
	apiData.PhoneNo=PhoneNo;
	apiData.CardType=CardType;
	apiData.NextNo=NextNo;
	
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	var alertMsg = !Commonjs.isEmpty(IfMsg)&&IfMsg=='0'?'取消':'设置';
	Commonjs.ajax('../../wsgw/queue/SetReMindNo/callApi.do', param,
			function(resp) {
				if (!Commonjs.isEmpty(resp.RespCode)) {
					if (resp.RespCode == 10000) {
						$("#wait-number").val(ReMindNo);
						myLayer.alert("智能提醒"+alertMsg+"成功");
					} else {
						myLayer.alert("智能提醒"+alertMsg+"失败");
					}
				}
			});
}
