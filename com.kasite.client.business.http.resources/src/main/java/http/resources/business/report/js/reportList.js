//就诊人全局对象数据
var memberGlobalData ={};
var pIndex = 0;//分页页数全局变量
var loading = false;//分页控制变量  false允许分页   true 禁止分页
var change = 0; //是否为切换列表 0为否  1为是
var reportType=0;//报告单类型  1为检验报告单  2为检查报告单  3体检报告 其他为未知
var selectedMember = {};
$(function() {
	reportType=Commonjs.getUrlParam('reportType');
	if(reportType==1){
		$("title").html("检验报告单");
	}else if(reportType==2){
		$("title").html("检查报告单");
	}
	else if(reportType==3){
		$("title").html("体检报告单");
	}
	else{
		$(".h45").html("");
		$(".nomess").css("display","block");
		$(".nomess").append("<p><a href='reportIndex.html'>返回选择报告类型</a></p>");
		myLayer.alert('请先选择报告类型', 3000);
		return;
	}
//	//加载就诊人信息列表
//	loadMemberList();
	//初始化控件
	initWidget();
});

//选项
$('.c-main').on('click', '.prefilter li', function() {
	$(this).addClass('curr').siblings().removeClass('curr');
});


/** 初始化控件*/
function initWidget(){
	//就诊人控件
	var defaultValue = Commonjs.getUrlParam('defaultValue');
	$("#filter-jzr").memberPicker({
		isMustCardNo:true,//必填卡信息
		cardType: Commonjs.constant.cardType_1,
		defaultlValue :defaultValue,
		pickerOnClose:function(obj,member){
			selectedMember = member;
			//各项参数归为默认值
			change=1;
			pIndex = 0;
			loading=false;
			//加载报告列表
			loadReportList();
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			//加载报告列表
			loadReportList();
		}
	})
//	Commonjs.memberPicker({
//		page:'reportList',
//		cardType: Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		pickerOnClose:function(obj,member){
//			selectedMember = member;
//			initUrl(selectedMember);
//			//各项参数归为默认值
//			change=1;
//			pIndex = 0;
//			loading=false;
//			//加载报告列表
//			loadReportList();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			initUrl(selectedMember);
//			//加载报告列表
//			loadReportList();
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
//			sessionStorage.setItem("QLCDefaultMember", selecedMemberJson);
//		},
//		onClose: function() {
//			if(Commonjs.isEmpty(memberGlobalData.selecedMember.CardNo)){
//				$("#cardNo").html("未绑定就诊卡");
//				myLayer.clear();
//				myLayer.alert("该账号还未绑定就诊卡", 1000);
//			}
//			//各项参数归为默认值
//			change=1;
//			pIndex = 0;
//			loading=false;
//			loadReportList();
//		}
//	});
	
	//分页
	$(document.body).infinite().on("infinite", function() {
		if(loading) {
			return;
		}
		loading = true;
		pIndex += 1;
		$(".infinite-scroll").css("display", "block"); //显示加载图标
		setTimeout(function() {
			loadReportList();
			$(".infinite-scroll").css("display", "none"); //隐藏加载图标
		}, 300);
	});
}


/*
//加载就诊人列表
function loadMemberList(){
	var apiData = {};
	apiData.HosId = Commonjs.getUrlParam("HosId");
	var page = {};
	page.PIndex = 0;
	page.PSize = 0;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do', param, function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					memberGlobalData.pickerValues = new Array();
					memberGlobalData.pickerDisplayValues = new Array();
					memberGlobalData.memberMap = {};
					if(Commonjs.ListIsNotNull(jsons.Data)) {
						//就诊人选择器
						Commonjs.BaseForeach(jsons.Data, function(i, item) {
							if(item.IsDefault ==1 ){
								//系统默认就诊人
								memberGlobalData.defaultMember = item;
							}
							memberGlobalData.pickerValues.push(item.MemberId);
							memberGlobalData.pickerDisplayValues.push(item.MemberName);
							memberGlobalData.memberMap[item.MemberId] = item;
						});
						//设置默认就诊人
						setDefaultMember();
					}else{
						myLayer.tip({
							con:'请新增就诊人！'
						});
					}
				}
			} else {
				myLayer.alert(jsons.RespMessage, 3000);
			}
		} else {
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	},{async:false});
}
*/
/** 加载报告单列表*/
function loadReportList() {
	var apiData = {};
	apiData = getParams();
	var page = {};
	page.PIndex = pIndex;
	page.PSize = 10;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/report/GetReportList/callApi.do', param, function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					reportList = '';
					if(Commonjs.ListIsNotNull(jsons.Data)) {
						Commonjs.BaseForeach(jsons.Data, function(i, item) {
							var href = '';
							if(reportType==1){
								href = '/business/report/inspectTestReportDetails.html?reportId=' + item.ReportId + '&reportType=1&eventNo='+item.EventNo+'&sampleNo='+item.SampleNo+'&submissionTime='+item.SubmissionTime+'&cardType='+apiData.CardType+'&cardNo='+apiData.CardNo;
							}
							if(reportType==2){
								href = '/business/report/inspectCheckReportDetails.html?reportId=' + item.ReportId + '&reportType=2&eventNo='+item.EventNo+'&sampleNo='+item.SampleNo+'&submissionTime='+item.SubmissionTime+'&cardType='+apiData.CardType+'&cardNo='+apiData.CardNo;
							}
							if(reportType==3){
								href = '/business/report/tjReportDetails.html?reportId=' + item.ReportId + '&reportType=3&eventNo='+item.EventNo+'&sampleNo='+item.SampleNo+'&submissionTime='+item.SubmissionTime+'&cardType='+apiData.CardType+'&cardNo='+apiData.CardNo;
							}
							reportList += '<li class="c-list-a"><a href="javascript:location.href=Commonjs.goToUrl(\'' + href + '\');" class="c-arrow-r"><div class="c-list-key pr15">';
							reportList += '<h4>' + item.ItemName + '</h4>';
							reportList += '<p class="c-f13 c-999">' + item.SubmissionTime + '</p>';
							reportList += '</div><div class="c-list-info c-t-right c-pack c-f13">';
							if( item.State == 1 ){
								reportList += '<span class="c-ffac0b">报告已出</span>';
							}else{
								reportList += '<span class="c-ffac0b">报告未出</span>';
							}
							reportList += '</div></a></li>';
						});
						$(".nomess").css("display", "none");
						loading=false;
					}else{
						if(pIndex==0){//第一页就没有数据,则显示无数据样式
							$(".nomess").css("display", "block");
						}else{
							$(".nomess").css("display", "none");
						}
						loading=true;
					}
					if(change == 0) {//分页追加列表
						$("#repotList").append(reportList);
					}else if(change == 1) {//切换就诊人显示列表
						$("#repotList").html(reportList);
						change = 0;
					}
				} else {
					myLayer.alert(jsons.RespMessage, 3000);
				}
			}else{
				myLayer.alert("返回码为空");
			}
		} else {
			// 通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	});
}

/** 参数封装*/
function getParams() {
	var apiData = {};
	if(Commonjs.isEmpty(selectedMember)) { //url传参
		apiData.CardType = Commonjs.getUrlParam('cardType');
		apiData.CardNo = Commonjs.getUrlParam('cardNo');
		apiData.Mobile = Commonjs.getUrlParam('mobile');
		apiData.IdCardNo = Commonjs.getUrlParam('idCardNo');
		apiData.PatientName = unescape(Commonjs.getUrlParam('memberName'));
		apiData.MemberId =  Commonjs.getUrlParam('memberId');
		apiData.ReportType = reportType;
	}else{ //session传参
		apiData.CardType = selectedMember.CardType;
		apiData.CardNo = selectedMember.CardNo;
		//apiData.Mobile = tempMember.mobile;
		//apiData.IdCardNo = tempMember.idCardNo;
		apiData.PatientName = selectedMember.MemberName;
		apiData.MemberId = selectedMember.MemberId;
		apiData.ReportType = reportType;
	}
	return apiData;
}

/** 设置页面默认就诊人
function setDefaultMember(){
	var tempMember = null;
	//先从js缓存获取默认就诊人
	if( !Commonjs.isEmpty(sessionStorage.getItem('QLCDefaultMember')) ){//如果js存在默认就诊人，则取这个默认就诊人
		tempMember= JSON.parse(sessionStorage.getItem('QLCDefaultMember'));
	}else{
		tempMember = memberGlobalData.defaultMember;
	}
	$("#filter-jzr").val(tempMember.MemberId);
	$("#jzrword").html(tempMember.MemberName);
	if(Commonjs.isEmpty(tempMember.CardNo)){
		$("#cardNo").html("未绑定就诊卡");
		myLayer.clear();
		myLayer.alert("该账户未绑定就诊卡", 1000);
	}else{
		$("#cardNo").html("就诊卡:"+tempMember.CardNo);
	}
}*/