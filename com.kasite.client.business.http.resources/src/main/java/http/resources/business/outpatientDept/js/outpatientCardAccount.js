/**就诊人全局变量*/
var memberGlobalData = {
	selectedMember:null,
	memberPickerDefaultlVal:null,
};

$(function() {
	var memberInfo = Commonjs.getUrlParam('memberInfo');
	if(!Commonjs.isEmpty(memberInfo)) {
		memberGlobalData.memberPickerDefaultlVal = memberInfo;
	}
	initWidget();
	
	queryPayConfig();// 查询支付配置

});

function queryPayConfig() {
	var param = {};
	var data = {};
	param.apiParam = Commonjs.getApiReqParams(data);
	Commonjs.ajax('/wsgw/pay/QueryFrontPayLimit/callApi.do', param, function(jsons) {
				if(jsons.RespCode==10000 && !Commonjs.isEmpty(jsons.Data)){
					if (1 == jsons.Data[0].IsEnablePay ) {
						$("#rechargeDiv").show();
					}
				}
			});
}

function initWidget(){
	
	//就诊人控件
	$("#filter-jzr").memberPicker({
		isMustCardNo : true,//必须有就诊卡
		defaultlValue:memberGlobalData.memberPickerDefaultlVal,
		pickerOnClose:function(obj,selectedMember){
			memberGlobalData.selectedMember = selectedMember;
			clearOutpatientCardInfo();
			queryOutpatientCardInfo();
		},
		ajaxSuccess:function(data,defaultMember){
			memberGlobalData.selectedMember = defaultMember;
			queryOutpatientCardInfo();
		}
	});
//	//就诊人控件
//	Commonjs.memberPicker({
//		page:'outpatientCardAccount',
//		cardType: Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		defaultlValue:memberGlobalData.memberPickerDefaultlVal,
//		nonCardFunction:function(){
//			myLayer.confirm({
//				con: '该就诊人还未绑定就诊卡 ,是否现在去绑定就诊卡?',
//				ok: function() {
//					var url = "/business/member/bindClinicCard.html?memberName="  + 
//					escape(memberGlobalData.selectedMember.memberName)+"&memberId="+memberGlobalData.selectedMember.memberId;
//					location.href = Commonjs.goToUrl(url);
//				},
//				cancel:function(){
//					clearOutpatientCardInfo();
//				}});
//		},
//		pickerOnClose:function(obj,selectedMember){
//			memberGlobalData.selectedMember = selectedMember;
//			clearOutpatientCardInfo();
//			queryOutpatientCardInfo();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			memberGlobalData.selectedMember = defaultMember;
//			queryOutpatientCardInfo();
//		}
//	});
	
	//充值按钮点击事件
	$("#btnPay").on('click',function(){
		if( Commonjs.isEmpty(memberGlobalData.selectedMember.CardNo) ){
			myLayer.alert('就诊卡不能为空');
			return;
		}
		var url = "outpatientRecharge.html?cardNo="+ memberGlobalData.selectedMember.CardNo +"&cardType=" + memberGlobalData.selectedMember.CardType
		+ "&name=" + memberGlobalData.selectedMember.MemberName+ "&memberId=" + memberGlobalData.selectedMember.MemberId;
		location.href = Commonjs.goToUrl(url);
	});
	
	//自主退费点击事件
	$("#btnUserSelfServiceRefund").on('click',function(){
		if( Commonjs.isEmpty(memberGlobalData.selectedMember.CardNo) ){
			myLayer.alert('就诊卡不能为空');
			return;
		}
		var url = "outpatientSelfServiceRefund.html?cardNo="+ memberGlobalData.selectedMember.CardNo 
		+"&cardType=" + memberGlobalData.selectedMember.CardType
		+ "&name=" + memberGlobalData.selectedMember.MemberName
		+ "&memberId=" + memberGlobalData.selectedMember.MemberId
		+ "&hisMemberId=" + memberGlobalData.selectedMember.HisMemberId;
		location.href = Commonjs.goToUrl(url);
	});
}

function clearOutpatientCardInfo(){
	$("#cardNo").html('');
	$("#balance").html('');
}

/**
 * 查询住院信息
 * 
 * @param member
 */
function queryOutpatientCardInfo() {
	if (Commonjs.isEmpty(memberGlobalData.selectedMember.CardNo)) {
		myLayer.alert('卡号不能为空');
		return;
	}
	var apiData = {};
	apiData.CardNo = memberGlobalData.selectedMember.CardNo;
	apiData.CardType = memberGlobalData.selectedMember.CardType;
	apiData.MemberId =  memberGlobalData.selectedMember.MemberId;
	var page = {};
	page.PIndex = 0;
	page.PSize = 1;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/basic/QueryCardBalance/callApi.do', param,function(jsons) {
		if (!Commonjs.isEmpty(jsons)) {
			if (!Commonjs.isEmpty(jsons.RespCode)) {
				if (jsons.RespCode == 10000) {
					if (Commonjs.ListIsNull(jsons.Data)) {
						var data = jsons.Data[0];
						$("#balance").html(Commonjs.centToYuan(data.Balance));
						$("#cardNo").html(memberGlobalData.selectedMember.CardNo);
					}
					//执行完把弹出层clear掉
					myLayer.clear();
				} else {
					myLayer.clear();
					myLayer.alert(jsons.RespMessage, 3000);
				}
			} else {
				myLayer.alert("返回码为空");
			}
		} else {
			myLayer.clear();
			//通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	});
}
