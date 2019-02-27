/**
 * 住院费用总清单js
 */
var loading = false;
var pIndex = 0;// 分页页数
var pSize = 8;
var selectedMember = null;
$(function() {
	initWidget();
});

//初始化页面控件
function initWidget() {
	// 页面滚动noInfo
	$(window).scroll(function() {
		var wtop = $(window).scrollTop()
		if (wtop > 90) {
			$('.forbtset').addClass('bs-fixed');
			$('.bill-title').addClass('be-fixed');
		} else {
			$('.forbtset').removeClass('bs-fixed');
			$('.bill-title').removeClass('be-fixed');
		}
	});
	// 分页
	$(document.body).infinite().on("infinite", function() {
		if (loading) {
			return;
		}
		loading = true;
		setTimeout(function() {
			getInHospitalCostListByDate(selectedMember.memberId);
		}, 300);
	});
	
	//就诊人控件
	$("#filter-jzr").memberPicker({
		isMustCardNo : true,
		cardType: Commonjs.constant.cardType_14,
		pickerOnClose:function(obj,member){
			clearMenberInfo();
			clearInfinite();
			selectedMember = member;
			
			getMemberInfoByHospitalNo(selectedMember.MemberId);
			getInHospitalCostListByDate(selectedMember.MemberId);
		},
		ajaxSuccess:function(data,defaultMember){
			clearInfinite();
			selectedMember = defaultMember;
			//查询住院患者，住院信息
			getMemberInfoByHospitalNo(defaultMember.MemberId);
			//查询住院费用列表
			getInHospitalCostListByDate(defaultMember.MemberId);
		}
	});
//	Commonjs.memberPicker({
//		page:'hospitalCost',
//		cardType: Commonjs.constant.cardType_14,
//		id:"filter-jzr",
//		pickerOnClose:function(obj,member){
//			clearMenberInfo();
//			clearInfinite();
//			selectedMember = member;
//			
//			getMemberInfoByHospitalNo(selectedMember.memberId);
//			getInHospitalCostListByDate(selectedMember.memberId);
//		},
//		ajaxSuccess:function(data,defaultMember){
//			clearInfinite();
//			selectedMember = defaultMember;
//			//查询住院患者，住院信息
//			getMemberInfoByHospitalNo(defaultMember.memberId);
//			//查询住院费用列表
//			getInHospitalCostListByDate(defaultMember.memberId);
//		}
//	});
}


// 根据ID查询就诊人信息
function getMemberInfoByHospitalNo(memberId) {
	if (Commonjs.isEmpty(memberId)) {
		return ;
	}
	var apiData = {};
	apiData.MemberId = memberId;
	//是否需要查询费用
	apiData.QueryHospitalCost = "HospitalCost"; 
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('../../wsgw/basic/QueryHospitalUserInfo/callApi.do',
			param, function(jsons) {
				if (!Commonjs.isEmpty(jsons)) {
					var listHtml = '';
					if (!Commonjs.isEmpty(jsons.RespCode)) {
						if (jsons.RespCode == 10000) {
							Commonjs.BaseForeach(jsons.Data,function(i, item) {
								$("#deptName").html(item.DeptName);
								$("#bedNo").html(item.BedNo);
								$("#inHospitalDate").html(item.InHospitalDate);
								$("#inHospitalDays").html(item.InHospitalDays);
								$("#totalFee").html(Commonjs.centToYuan(item.InHospitalTotalFee));
								if(!Commonjs.isNotNull(item.DeptName)){
									$("#p_deptName").hide();
								}
								if(!Commonjs.isNotNull(item.BedNo)){
									$("#p_bedNo").hide();
								}
								if(!Commonjs.isNotNull(item.InHospitalDate)){
									$("#p_inHospitalDate").hide();
								}
								if(!Commonjs.isNotNull(item.InHospitalDays)){
									$("#p_inHospitalDays").hide();
								}
								if(!Commonjs.isNotNull(item.InHospitalTotalFee)){
									$("#p_totalFee").hide();
								}
							});
						} else {
							myLayer.alert(jsons.RespMessage, 3000);
						}
					} else {
						myLayer.alert("返回码为空", 3000);
					}
				} else {
					// 通信失败
					myLayer.alert('网络繁忙,请刷新后重试', 3000);
				}
			});
}

// 根据ID查询就诊人费用清单
function getInHospitalCostListByDate(memberId) {
	$(".infinite-scroll").show();
	var page = {};
	page.PIndex = pIndex;
	page.PSize = pSize;
	var apiData = {};
	apiData.CardType= Commonjs.constant.cardType_14;
	apiData.MemberId = memberId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/order/QueryInHospitalCostList/callApi.do',param,function(jsons) {
		$(".infinite-scroll").hide();
		if (!Commonjs.isEmpty(jsons)) {
			var listHtml = $(".c-list").html();
			if (!Commonjs.isEmpty(jsons.RespCode)) {
				if (jsons.RespCode == 10000) {
					if (Commonjs.ListIsNotNull(jsons.Data)) {
							Commonjs.BaseForeach(jsons.Data,function(i, item) {
								listHtml += "<li class=\"c-list-a\">";
								listHtml += "<a href=\"javascript:location.href=Commonjs.goToUrl('hospitalCostDaily.html?date="
										+ formateDate(item.Date)
										+ "&memberObj="
										+ encodeURI($("#filter-jzr").val())
										+ "')\" class=\"c-arrow-r\">";
								listHtml += "<div class=\"c-list-info c-f15\">"
										+ formateDate(item.Date)
										+ "</div>";
								listHtml += "<div class=\"c-list-key c-pack c-f13\">￥<span class=\"c-ffac0b\">"
										+ Commonjs.centToYuan(item.Fee)
										+ "</span></div>";
								listHtml += "</a></li>";
							});
					} else if (pIndex == 0) {
						//如果是首次查询无数据
						$("#noInfo").show();
					}
					$(".c-list").html(listHtml);
					if(hasNextPage(jsons.Page.PCount)){
            			 loading = false;
            			 pIndex ++ ;
            		}else{
            			 loading = true;
            			 $(".nomore").html("没有更多了");
            		}
				} else {
					myLayer.alert(jsons.RespMessage, 3000);
				}
			} else {
				myLayer.alert("返回码为空", 3000);
			}
		} else {
			//通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	});

}

//住院号为空时清空页面信息
function clearMenberInfo() {
	$(".c-list").empty();
	$("#deptName").html('');
	$("#bedNo").html('');
	$("#inHospitalDate").html('');
	$("#inHospitalDays").html('');
	$("#totalFee").html('');
}

//清除分页查询信息
function clearInfinite() {
	loading = false;
	pIndex = 0;
	$(".nomore").html("&emsp;");
}

//格式化日期YYYYMMDD -> YYYY-MM-DD
function formateDate(oldDate) {
	var tmpDate = '';
	if (!Commonjs.isEmpty(oldDate) && oldDate.length == 8) {
		tmpDate = oldDate.substr(0, 4) + "-" + oldDate.substr(4, 2) + "-"
				+ oldDate.substr(6, 2);
		return tmpDate;
	} else {
		return oldDate;
	}
}

/**
 * 是否还有下一页
 */
function hasNextPage(pageCount){
	if(Math.ceil(pageCount/pSize) - 1 == pIndex ){
		return false;
	}
	return true;
}
