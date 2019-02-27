// 就诊人全局对象数据
var memberGlobalData = {};
var pIndex = 0; // 分页页数全局变量
var loading = false; // 分页控制变量 false允许分页 true 禁止分页
var change = 0; // 是否为切换列表 0为否 1为是
var choose = {}; // 条件选择全局变量
choose.PayState = '';
choose.PayWay = '';
choose.BeginTime = '';
choose.EndTime = '';

$(function() {
	// 加载就诊人列表x
	loadMemberList();
	// 加载充值记录列表
	loadRecordList();
	// 初始化控件
	initWidget();
});

// 选项
$('.c-main').on('click', '.prefilter li', function() {
	$(this).addClass('curr').siblings().removeClass('curr');
});

// 日期
(function() {
	var formdate = new Date(),
		setstar = false,
		setend = false,
		year = formdate.getFullYear(),
		mouth = formdate.getMonth() + 1;
	mouth = (mouth < 10) ? '0' + mouth : mouth;
	$("#dateStar").picker({
		title: "选择起始日期",
		cols: [{
			textAlign: 'center',
			values: Draw(2000)
		}, {
			divider: true,
			content: '年'
		}, {
			textAlign: 'center',
			values: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']
		}, {
			divider: true,
			content: '月'
		}],
		onChange: function(picker, values, displayValues) {
			if(!setstar) {
				$("#dateStar").picker("setValue", [year, mouth]);
				setstar = true;
				choose.BeginTime = year + '-' + mouth + "-1";
			} else {
				choose.BeginTime = values[0] + '-' + values[1] + "-1";
			}
		}
	});
	$("#dateEnd").picker({
		title: "选择结束日期",
		cols: [{
			textAlign: 'center',
			values: Draw(2000)
		}, {
			divider: true,
			content: '年'
		}, {
			textAlign: 'center',
			values: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']
		}, {
			divider: true,
			content: '月'
		}],
		onChange: function(picker, values, displayValues) {
			if(!setend) {
				$("#dateEnd").picker("setValue", [year, mouth]);
				setend = true;
				choose.EndTime = year + '-' + mouth + "-31";
			} else {
				choose.EndTime = values[0] + '-' + values[1] + "-31";
			}
		}

	});

	// 年份
	function Draw(n) {
		var data = new Array();
		for(var i = n; i <= year; i++) {
			data.push(i);
		}
		return data;
	}
})();
/** 初始化控件 */
function initWidget() {
	// 分页
	$(document.body).infinite().on("infinite", function() {
		if(loading) {
			return;
		}
		loading = true;
		pIndex += 1;
		$(".infinite-scroll").css("display", "block"); // 显示加载图标
		setTimeout(function() {
			loadRecordList();
			$(".infinite-scroll").css("display", "none"); // 隐藏加载图标
		}, 300);
	});
	//就诊人控件
	$("#filter-jzr").memberPicker({
		cardType: Commonjs.constant.cardType_1,
		pickerOnClose:function(obj,member){
			memberGlobalData.selecedMember = member;
			// 各项参数归为默认值
			change = 1;
			pIndex = 0;
			loading = false;
			loadRecordList();
		},
		ajaxSuccess:function(data,defaultMember){
			memberGlobalData.selecedMember = defaultMember;
			var selecedMemberJson = JSON.stringify(memberGlobalData.selecedMember); // 保存js缓存
//			sessionStorage.setItem("QLCDefaultMember", selecedMemberJson);
			Commonjs.setSessionItem("QLCDefaultMember", selecedMemberJson)
		}
	})
//	$("#filter-jzr").picker({
//		title: "就诊人切换",
//		cols: [{
//			textAlign: 'center',
//			values: memberGlobalData.pickerValues,
//			displayValues: memberGlobalData.pickerDisplayValues
//		}],
//		onChange: function($k, values, displayValues) {
//			$('#jzrword').html(displayValues);
//			$("#filter-jzr").val(values);
//			if(Commonjs.isEmpty(memberGlobalData.memberMap[values].CardNo)) {
//				$("#cardNo").html("就诊卡未绑定");
//			} else {
//				$("#cardNo").html("就诊卡:" + memberGlobalData.memberMap[values].CardNo);
//			}
//			memberGlobalData.selecedMember = memberGlobalData.memberMap[values];
//			var selecedMemberJson = JSON.stringify(memberGlobalData.selecedMember); // 保存js缓存
//			sessionStorage.setItem("QLCDefaultMember", selecedMemberJson);
//		},
//		onClose: function() {
//			if(Commonjs.isEmpty(memberGlobalData.selecedMember.CardNo)) {
//				$("#cardNo").html("就诊卡未绑定");
//				myLayer.clear();
//				myLayer.alert("该账号还未绑定就诊卡", 1000);
//				loadRecordList();
//				return;
//			}
//			// 各项参数归为默认值
//			change = 1;
//			pIndex = 0;
//			loading = false;
//			loadRecordList();
//		}
//	});
	// 筛选
	$('.filter-into').on('click', function() {
		$(this).toggleClass('curr');
		$('#listPart').toggle();
		$('#filterPart').slideToggle(300);
	});

	// 确定
	$('#filter-btn').on('click', function() {
		$('.filter-into').removeClass('curr');
		$('#listPart').show();
		$('#filterPart').hide();
		pIndex = 0;
		loading = false;
		change = 1;
		loadRecordList();
	});
}

/** 加载充值记录列表 */
function loadRecordList() {
	var apiData = {};
	apiData = getParams();
	if(Commonjs.isEmpty(apiData.CardNo)&&Commonjs.isEmpty(apiData.CardType)){
		$("#list").html("");
		$(".nomess").css("display", "block");
		$(".nomore").css("display", "none");
		return;
	}
	//时间筛选判断
	if(!Commonjs.isEmpty(choose.BeginTime) && Commonjs.isEmpty(choose.EndTime)) {
		apiData.BeginDate = choose.BeginTime;
		apiData.EndDate = '2999-12-31';
	} else if(Commonjs.isEmpty(choose.BeginTime) && !Commonjs.isEmpty(choose.EndTime)) {
		apiData.BeginDate = '2000-1-1';
		apiData.EndDate = choose.EndTime;
	} else {
		apiData.BeginDate = choose.BeginTime;
		apiData.EndDate = choose.EndTime;
	}
	apiData.PayState='2';
	apiData.BizState='1';//退费还未完善，先查询支付成功的
	apiData.ServiceId = Commonjs.orderType_006;
	var page = {};
	page.PIndex = pIndex;
	page.PSize = 10;
	var param = {};
	param.api = 'OrderListLocal';
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
		Commonjs.ajax('../../wsgw/order/OrderListLocal/callApi.do', param,function(jsons) {
			if(!Commonjs.isEmpty(jsons)) {
				if(!Commonjs.isEmpty(jsons.RespCode)) {
					if(jsons.RespCode == 10000) {
						var pCount=jsons.Page.PCount;
						var recordList = '';
						if(Commonjs.ListIsNotNull(jsons.Data)) {
							Commonjs.BaseForeach(jsons.Data, function(i, item) { 
								i%2==0?recordList += "<li>":recordList += "<li class='c-list-a'>";
								recordList += "<div class='pm-title'>" + item.PriceName + "</div><ul class='visit-mess c-f14'>";
								recordList += "<li><div class='vm-key c-666'>充值金额：</div><div class='vm-info'>￥<span class='c-ffac0b'>" + item.PayMoney / 100 + "</span></div>";
								recordList += "</li><li> <div class='vm-key c-666'>充值时间：</div><div class='vm-info'>" + item.BeginDate.substring(0, 16) + "</div>";
								if(item.ChannelId=='100123'){
									recordList += "</li><li><div class='vm-key c-666'>充值类型：</div><div class='vm-info'>微信充值</div> </li></ul></li>";
								}else if(item.ChannelId=='100125'){
									recordList += "</li><li><div class='vm-key c-666'>充值类型：</div><div class='vm-info'>支付宝充值</div> </li></ul></li>";
								}else{
									recordList += "</li><li><div class='vm-key c-666'>充值类型：</div><div class='vm-info'>在线充值</div> </li></ul></li>";
								}
								
							});
							$(".nomess").css("display", "none");
							$(".nomore").css("display", "none");
							loading = false;
						} else {
							if(pIndex == 0) { // 第一页就没有数据,则显示无数据样式
								$(".nomess").css("display", "block");
								$(".nomore").css("display", "none");
							} else {
								$(".nomore").css("display", "block");
								$(".nomess").css("display", "none");
							}
							loading = true;
						}
						if(change == 0) { // 分页追加列表
							$("#list").append(recordList);
						} else if(change == 1) { // 切换就诊人显示列表
							$("#list").html(recordList);
							change = 0;
						}
						// 执行完把弹出层clear掉
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
				// 通信失败
				myLayer.alert('网络繁忙,请刷新后重试', 3000);
			}
		});
}

///** 加载就诊人列表 */
//function loadMemberList() {
//	var apiData = {};
//	apiData.OpId = Commonjs.getOpenId();
//
//	apiData.HosId = Commonjs.getUrlParam("HosId");
//	apiData.ChannelId = Commonjs.getChannelId();
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
//						// 就诊人选择器
//						Commonjs.BaseForeach(jsons.Data, function(i, item) {
//							if(item.IsDefault == 1) { // 系统默认就诊人
//								memberGlobalData.defaultMember = item;
//							}
//							memberGlobalData.pickerValues.push(item.MemberId);
//							memberGlobalData.pickerDisplayValues.push(item.MemberName);
//							memberGlobalData.memberMap[item.MemberId] = item;
//						});
//						// 设置默认就诊人
//						setDefaultMember();
//					} else {
//						myLayer.tip({
//							con: '请新增就诊人！'
//						});
//					}
//				}
//			} else {
//				myLayer.alert(jsons.RespMessage, 3000);
//			}
//		} else {
//			// 通信失败
//			myLayer.alert('网络繁忙,请刷新后重试', 3000);
//		}
//	});
//}
///** 设置页面默认就诊人 */
//function setDefaultMember() {
//	var tempMember = null;
//	// 先从js缓存获取默认就诊人
//	if(!Commonjs.isEmpty(sessionStorage.getItem('QLCDefaultMember'))) { // 如果js存在默认就诊人，则取这个默认就诊人
//		tempMember = JSON.parse(sessionStorage.getItem('QLCDefaultMember'));
//	} else {
//		tempMember = memberGlobalData.defaultMember;
//	}
//	$("#filter-jzr").val(tempMember.MemberId);
//	$("#jzrword").html(tempMember.MemberName);
//	if(Commonjs.isEmpty(tempMember.CardNo)){
//		$("#cardNo").html("就诊卡未绑定");
//		myLayer.clear();
//		myLayer.alert("该账号就诊卡未绑定", 1000);
//	}else{
//		$("#cardNo").html("就诊卡:" + tempMember.CardNo);
//	}
//}

/** 参数封装 */
function getParams() {
	var apiData = {};
	var defMember = Commonjs.getSessionItem("QLCDefaultMember");
	if(Commonjs.isEmpty(defMember)) { // 默认就诊人参数
		apiData.CardNo = memberGlobalData.defaultMember.CardNo;
		apiData.CardType = memberGlobalData.defaultMember.CardType;
	} else { // session传参
		tempMember = JSON.parse(defMember);
		apiData.CardNo = tempMember.CardNo;
		apiData.CardType = tempMember.CardType;
	}
//	if(Commonjs.isEmpty(sessionStorage.getItem('QLCDefaultMember'))) { // 默认就诊人参数
//		apiData.CardNo = memberGlobalData.defaultMember.CardNo;
//		apiData.CardType = memberGlobalData.defaultMember.CardType;
//	} else { // session传参
//		tempMember = JSON.parse(sessionStorage.getItem('QLCDefaultMember'));
//		apiData.CardNo = tempMember.CardNo;
//		apiData.CardType = tempMember.CardType;
//	}
	return apiData;
}
/** 获取充值类型 */
function getRechargeType(isOnlinePay) {
	if(isOnlinePay == 1) {
		return '在线支付';
	} else if(isOnlinePay == 3) {
		return '移动支付';
	} else if(isOnlinePay == 2) {
		return '现金支付';
	}
}

/** 获取充值状态 */
function getPayState(type) {
	choose.PayState = type;
}
/** 获取支付方式 */
function getPayWay(type) {
	choose.PayWay = type;
}