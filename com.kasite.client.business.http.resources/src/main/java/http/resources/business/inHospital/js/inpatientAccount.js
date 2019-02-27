/** 就诊人全局变量 */
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

function initWidget(){
	
	//就诊人控件
	$("#filter-jzr").memberPicker({
		isMustCardNo : true,
		cardType: Commonjs.constant.cardType_14,
		defaultlValue:memberGlobalData.memberPickerDefaultlVal,
		pickerOnClose:function(obj,selectedMember){
			memberGlobalData.selectedMember = selectedMember;
			clearHospitalUserInfo();
			queryHospitalUserInfo();
		},
		ajaxSuccess:function(data,defaultMember){
			memberGlobalData.selectedMember = defaultMember;
			queryHospitalUserInfo();
		}
	});
//	Commonjs.memberPicker({
//		page:'inpatientAccount',
//		cardType: Commonjs.constant.cardType_14,
//		id:"filter-jzr",
//		defaultlValue:memberGlobalData.memberPickerDefaultlVal,
//		nonCardFunction:function(){
//			myLayer.confirm({
//				con: '该就诊人还未绑定住院号 ,是否现在去绑定住院号?',
//				ok: function() {
//					var url = "/business/member/bindHospitalNo.html?memberName=" + 
//					escape(memberGlobalData.selectedMember.memberName)+"&memberId="+memberGlobalData.selectedMember.memberId;
//					location.href = Commonjs.goToUrl(url);
//				},
//				cancel:function(){
//					clearHospitalUserInfo();
//				}});
//		},
//		pickerOnClose:function(obj,selectedMember){
//			memberGlobalData.selectedMember = selectedMember;
//			clearHospitalUserInfo();
//			queryHospitalUserInfo();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			memberGlobalData.selectedMember = defaultMember;
//			queryHospitalUserInfo();
//		}
//	});
	
	//充值按钮点击事件
	$("#btnPay").on('click',function(){
		if( Commonjs.isEmpty(memberGlobalData.selectedMember.CardNo) ){
			myLayer.alert('住院号不能为空');
			return;
		}
		var url = "hospitalNoRecharge.html?hospitalNo="+ memberGlobalData.selectedMember.CardNo + "&name=" + memberGlobalData.selectedMember.MemberName+ "&memberId=" + memberGlobalData.selectedMember.MemberId;
		location.href = Commonjs.goToUrl(url);
	});
}

function queryPayConfig() {
	var param = {};
	var data = {};
	param.apiParam = Commonjs.getApiReqParams(data);
	Commonjs.ajax('/wsgw/pay/QueryFrontPayLimit/callApi.do', param,
			function(jsons) {
				if(jsons.RespCode==10000 && !Commonjs.isEmpty(jsons.Data)){
					if (1 == jsons.Data[0].IsEnablePay ) {
						$("#rechargeDiv").show();
					}
				}
			},{async:false});
}



/**
 * 查询住院信息
 * 
 * @param member
 */
function queryHospitalUserInfo() {
	if (Commonjs.isEmpty(memberGlobalData.selectedMember.CardNo)) {
		//myLayer.alert('住院号不能为空');
		return;
	}
	var apiData = {};
	apiData.HospitalNo = memberGlobalData.selectedMember.CardNo;
	apiData.CardType = memberGlobalData.selectedMember.CardType;
	apiData.MemberId = memberGlobalData.selectedMember.MemberId;
	var page = {};
	page.PIndex = 0;
	page.PSize = 1;
	var param = {};
	param.api = 'QueryHospitalUserInfo';
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/basic/QueryHospitalUserInfo/callApi.do', param,function(jsons) {
		if (!Commonjs.isEmpty(jsons)) {
			if (!Commonjs.isEmpty(jsons.RespCode)) {
				if (jsons.RespCode == 10000) {
					if (Commonjs.ListIsNull(jsons.Data)) {
						var data = jsons.Data[0];
						$(".c-f30").html(Commonjs.centToYuan(data.Balance));
						$("#name").html(data.Name);
						$("#hosId").html(data.HospitalNo);
						$("#BedNo").html( data.DeptName + ' ' + data.BedNo + '床');
						$("#date").html(data.InHospitalDate);
					}
				} else {
					myLayer.alert(jsons.RespMessage, 3000);
				}
			} else {
				myLayer.alert("返回码为空");
			}
		} else {
			//通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	});
}

function clearHospitalUserInfo(){
	$(".c-f30").html('');
	$("#name").html('');
	$("#hosId").html("无");
	$("#BedNo").html("无");
	$("#date").html("无");
}
// $(function() {
// var type = Commonjs.getUrlParam('type'); //查询类型,1为住院号查询,其他为默认就诊人查询
// var memberId = Commonjs.getUrlParam('memberId');
// //获取就诊人信息
// getMemberInfo();
// queryPayConfig();//查询支付配置
// if(type == 1) { //住院号查询
// queryHosInfo(memberGlobalData.AppointMember);
// } else { //默认就诊人查询
// queryHosInfo(memberGlobalData.DefaultMember);
// }
// loadMemberList();//加载就诊人信息
// initWidget();//初始化控件
// });
// function queryPayConfig(){
// var param={};
// var data={};
// param.apiParam = Commonjs.getApiReqParams(data);
// Commonjs.ajax('/wsgw/pay/QueryFrontPayLimit/callApi.do',param,
// function(jsons) {
// isEnablePay=jsons.Data.IsEnablePay;
// },{async:false});
// }
// /**
// * 查询住院信息
// * @param member
// */
// function queryHosInfo(member) {
// if(Commonjs.isEmpty(member.HospitalNo)) {
// myLayer.alert('住院号不能为空');
// return;
// }
// var apiData = {};
// apiData.HospitalNo = member.HospitalNo;
// var page = {};
// page.PIndex = 0;
// page.PSize = 1;
// var param = {};
// param.api = 'QueryHospitalUserInfo';
// param.apiParam = Commonjs.getApiReqParams(apiData, page);
// Commonjs.ajax('../../wsgw/basic/QueryHospitalUserInfo/callApi.do', param,
// function(jsons) {
// if(!Commonjs.isEmpty(jsons)) {
// if(!Commonjs.isEmpty(jsons.RespCode)) {
// if(jsons.RespCode == 10000) {
// if(Commonjs.ListIsNull(jsons.Data)) {
// var data = jsons.Data;
// $(".c-f30").html(data.Balance);
// $("#name").html(data.Name);
// $("#hosId").html(data.HospitalNo);
// $("#BedNo").html(data.DeptName + ' ' + data.BedNo+'床');
// $("#date").html(data.InHospitalDate);
// if(Number(isEnablePay)==1){
// $("#recharge").attr("href", "hospitalNoRecharge.html?hospitalNo=" +
// data.HospitalNo+"&name="+data.Name);
// }
// else{
// $("#recharge").remove();
// }
// memberGlobalData.displayMember=data;//当前显示的就诊人信息
// }
// //执行完把弹出层clear掉
// myLayer.clear();
// } else {
// myLayer.clear();
// myLayer.alert(jsons.RespMessage, 3000);
// }
// } else {
// myLayer.alert("返回码为空");
// }
// } else {
// myLayer.clear();
// //通信失败
// myLayer.alert('网络繁忙,请刷新后重试', 3000);
// }
// });
// }
//
// /**
// * 获取就诊人信息
// */
// function getMemberInfo() {
// var apiData = {};
// apiData.OpId = Commonjs.getOpenId();
// apiData.ChannelId = Commonjs.getChannelId();
// var page = {};
// page.PIndex = 0;
// page.PSize = 5;
// var param = {};
// param.apiParam = Commonjs.getApiReqParams(apiData, page);
// Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do', param,
// function(jsons) {
// if(!Commonjs.isEmpty(jsons)) {
// if(!Commonjs.isEmpty(jsons.RespCode)) {
// if(jsons.RespCode == 10000) {
// if(Commonjs.ListIsNull(jsons.Data)) {
// Commonjs.BaseForeach(jsons.Data, function(i, item) {
// if(item.IsDefaultMember == 1) { //系统默认就诊人
// memberGlobalData.DefaultMember = item;
// }
// if(!Commonjs.isEmpty(memberId) && memberId == item.MemberId) {
// //根据memberId获取就诊人信息
// memberGlobalData.AppointMember = item;
// }
// });
// }
// } else {
// myLayer.alert(jsons.RespMessage, 3000);
// }
// } else {
// myLayer.alert("返回码为空");
// }
// } else {
// //通信失败
// myLayer.alert('网络繁忙,请刷新后重试', 3000);
// }
// });
// }
//
// /**初始化控件*/
// function initWidget(){
//	
// var name=$("#name").html();//保存未切换就诊人姓名
// //就诊人切换
// $("#filter-jzr").picker({
// title: "就诊人切换",
// cols: [{
// textAlign: 'center',
// values: memberGlobalData.pickerValues,
// displayValues:memberGlobalData.pickerDisplayValues
// }],
// onChange: function($k, values, displayValues) {
// $('#jzrword').html(displayValues);
// $("#filter-jzr").val(values);
// $("#name").html(memberGlobalData.memberMap[values].MemberName);
// memberGlobalData.selecedMember = memberGlobalData.memberMap[values];
// },
// onClose: function() {
// $("#account").html("账户余额(元)");
// if(Commonjs.isEmpty(memberGlobalData.selecedMember.HospitalNo)){
// $("#name").html(memberGlobalData.displayMember.MemberName);
// myLayer.confirm({
// con: '该就诊人还未绑定住院号 ,是否现在去绑定住院号?',
// ok: function() {
// window.location.href = "/business/member/bindHospitalNo.html?memberName=" +
// escape(memberGlobalData.selecedMember.MemberName)+"&memberId="+memberGlobalData.selecedMember.MemberId;
// },
// cancel: function() {
// $("#account").html("账户不存在");
// $(".c-f30").html('');
// $("#name").html(memberGlobalData.selecedMember.Name);
// $("#hosId").html("住院号未绑定");
// $("#BedNo").html("床位未绑定");
// $("#date").html("住院号未绑定");
// $(".mt10").css("display","none");
// return;
// }
// });
// return;
// }else{
// $(".mt10").css("display","block");
// }
// queryHosInfo(memberGlobalData.selecedMember);
// }
// });
// }
// /**加载就诊人列表*/
// function loadMemberList(){
// var apiData = {};
// apiData.OpId = Commonjs.getOpenId();
// apiData.ChannelId = Commonjs.getChannelId();
// var page = {};
// page.PIndex = 0;
// page.PSize = 0;
// var param = {};
// param.apiParam = Commonjs.getApiReqParams(apiData, page);
// Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do', param,
// function(jsons) {
// if(!Commonjs.isEmpty(jsons)) {
// if(!Commonjs.isEmpty(jsons.RespCode)) {
// if(jsons.RespCode == 10000) {
// memberGlobalData.pickerValues = new Array();
// memberGlobalData.pickerDisplayValues = new Array();
// memberGlobalData.memberMap = {};
// if(Commonjs.ListIsNotNull(jsons.Data)) {
// //就诊人选择器
// Commonjs.BaseForeach(jsons.Data, function(i, item) {
// memberGlobalData.pickerValues.push(item.MemberId);
// memberGlobalData.pickerDisplayValues.push(item.MemberName);
// memberGlobalData.memberMap[item.MemberId] = item;
// });
// }else{
// myLayer.tip({
// con:'请新增就诊人！'
// });
// }
// }
// } else {
// myLayer.alert(jsons.RespMessage, 3000);
// }
// } else {
// //通信失败
// myLayer.alert('网络繁忙,请刷新后重试', 3000);
//		}
//	});
//}=======
