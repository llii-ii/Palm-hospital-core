//就诊人全局对象数据
var selectedMember ={};
$(function(){
	
	//加载就诊人列表
//	loadMemberList();
	
	//初始化控件
	initWidget();
	
	$("#check").click(function(){
		var url = $("#check").attr("url");
		location.href = Commonjs.goToUrl(url);
	});

	$("#test").click(function(){
		var url = $("#test").attr("url");
		location.href = Commonjs.goToUrl(url);
	});
	$("#tjreport").click(function(){
		var url = $("#tjreport").attr("url");
		location.href = Commonjs.goToUrl(url);
	});
});
function initUrl(selectedMember){
//	var displayValues = selectedMember.MemberName;
//	var values = selectedMember.CardNo;
//	$('#jzrword').html(displayValues);
//	if(Commonjs.isEmpty(values)){
//		$("#cardNo").html("未绑定就诊卡");
//	}else{
//		$("#cardNo").html("就诊卡:"+ Commonjs.formatCardNo(values));
//	}
	var hrefParam = 'cardNo=' + selectedMember.CardNo +'&cardType=' + selectedMember.CardType 
	+ '&memberName=' +escape(selectedMember.MemberName) 
	+ '&memberId=' + selectedMember.MemberId+"&defaultValue="+$("#filter-jzr").val();
	//检查报告url
	var checkURL = 'reportList.html?reportType=2&' + hrefParam;
	$("#check").attr("url",checkURL);
	//检验报告url
	var testUrl = 'reportList.html?reportType=1&' + hrefParam;
	$("#test").attr("url",testUrl);
	//体检报告
	var tjUrl = 'reportList.html?reportType=3&' + hrefParam;
	$("#tjreport").attr("url",tjUrl);
}
/**
 * 初始化控件
 */
function initWidget(){ 
	//就诊人控件
	$("#filter-jzr").memberPicker({
		isMustCardNo:true,//必填卡信息
		cardType: Commonjs.constant.cardType_1,
		pickerOnClose:function(obj,member){
			selectedMember = member;
			initUrl(selectedMember);
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			initUrl(selectedMember);
		}
	});
//	Commonjs.memberPicker({
//		page:'reportIndex',
//		cardType: Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		pickerOnClose:function(obj,member){
//			selectedMember = member;
//			initUrl(selectedMember);
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			initUrl(selectedMember);
//		}
//	});
//	//就诊人切换
//	$("#filter-jzr").picker({
//		title: "就诊人切换",
//		cols: [{
//			textAlign: 'center',
//			values: memberGlobalData.pickerValues,
//			displayValues:memberGlobalData.pickerDisplayValues	
//		}],
//		onChange: function($k, values, displayValues) {
//			$('#jzrword').html(displayValues);
//			$("#filter-jzr").val(values);			
//			if(Commonjs.isEmpty(memberGlobalData.memberMap[values].CardNo)){
//				$("#cardNo").html("未绑定就诊卡");
//			}else{
//				$("#cardNo").html("就诊卡:"+memberGlobalData.memberMap[values].CardNo);
//			}
//			memberGlobalData.selecedMember = memberGlobalData.memberMap[values];
//			var selecedMemberJson = JSON.stringify(memberGlobalData.selecedMember); //保存js缓存
//			
//			sessionStorage.setItem("QLCDefaultMember", selecedMemberJson);
//			var hrefParam = 'cardNo=' + memberGlobalData.selecedMember.CardNo +'&cardType=' + memberGlobalData.selecedMember.CardType + '&memberName=' +escape(memberGlobalData.selecedMember.MemberName) + 
//			'&mobile=' + memberGlobalData.selecedMember.Mobile +'&idCardNo=' + memberGlobalData.selecedMember.IdCardNo+ '&cardTypeName=' + escape(memberGlobalData.selecedMember.CardTypeName);
//			//检查报告url
//			var checkURL = 'reportList.html?reportType=2&' + hrefParam;
//			$("#check").attr("url",checkURL);
//			//检验报告url
//			var testUrl = 'reportList.html?reportType=1&' + hrefParam;
//			$("#test").attr("url",testUrl);
//		},
//		onClose: function() {
//			if(Commonjs.isEmpty(memberGlobalData.selecedMember.CardNo)){
//				myLayer.clear();
//				myLayer.alert("该账户未绑定就诊卡", 1000);
//			}
//		}
//	});
	
}
//
///**
// * 加载就诊人列表
// */
//function loadMemberList(){
//	var apiData = {};
//	apiData.HosId = Commonjs.getUrlParam("HosId");
//	var page = {};
//	page.PIndex = 0;
//	page.PSize = 0;
//	var param = {};
//	param.apiParam = Commonjs.getApiReqParams(apiData, page);
//	Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do', param, function(jsons) {
//		if(!Commonjs.isEmpty(jsons)) {
//			if(!Commonjs.isEmpty(jsons.RespCode)) {
//				if(jsons.RespCode == 10000) {
//					memberGlobalData.pickerValues = new Array();
//					memberGlobalData.pickerDisplayValues = new Array();
//					memberGlobalData.memberMap = {};
//					if(Commonjs.ListIsNotNull(jsons.Data)) {
//						//就诊人选择器
//						Commonjs.BaseForeach(jsons.Data, function(i, item) {
//							if(item.IsDefault ==1 ){
//								//系统默认就诊人
//								memberGlobalData.defaultMember = item;
//							}
//							memberGlobalData.pickerValues.push(item.MemberId);
//							memberGlobalData.pickerDisplayValues.push(item.MemberName);
//							memberGlobalData.memberMap[item.MemberId] = item;
//						});
//						//设置默认就诊人
//						setDefaultMember();
//					}else{
//						myLayer.tip({
//							con:'请新增就诊人！'
//						});
//					}
//				}
//			} else {
//				myLayer.alert(jsons.RespMessage, 3000);
//			}
//		} else {
//			//通信失败
//			myLayer.alert('网络繁忙,请刷新后重试', 3000);
//		}
//	});
//}

///**
// * 设置页面默认就诊人
// */
//function setDefaultMember(){
//	var tempMember = null;
//	//先从js缓存获取默认就诊人
//	if( !Commonjs.isEmpty(sessionStorage.getItem('QLCDefaultMember')) ){//如果js存在默认就诊人，则取这个默认就诊人
//		tempMember= JSON.parse(sessionStorage.getItem('QLCDefaultMember'));
//	}else{
//		tempMember = memberGlobalData.defaultMember;
//	}
//	$("#filter-jzr").val(tempMember.MemberId);
//	$("#jzrword").html(tempMember.MemberName);
//	if(Commonjs.isEmpty(tempMember.CardNo)){
//		$("#cardNo").html("未绑定就诊卡");
//		myLayer.clear();
//		myLayer.alert("该账户未绑定就诊卡", 1000);
//	}else{
//		$("#cardNo").html("就诊卡:"+tempMember.CardNo);
//	}
//	
//	var hrefParam = 'cardNo=' + tempMember.CardNo +'&cardType=' + tempMember.CardType + '&memberName=' +escape(tempMember.MemberName) + 
//	'&mobile=' + tempMember.Mobile +'&idCardNo=' + tempMember.IdCardNo  + '&cardTypeName=' + escape(tempMember.CardTypeName);
//	//检查报告url
//	var checkURL = 'reportList.html?reportType=2&' + hrefParam;
//	$("#check").attr("url",checkURL);
//	//检验报告url
//	var testUrl = 'reportList.html?reportType=1&' + hrefParam;
//	$("#test").attr("url",testUrl);
//}
