var selectedMember = {};
var loading = false;
var pageIndex = 0;
var pageSize = 4;
$(function() {
	var hisOrderPayType = {
		"1":"充值",
		"2":"退费"	
	};
	var hisOrderChargeType = {
		"0":"现金",
		"1":"支付宝",
		"2":"银行卡",
		"3":"微信" 
	};
	var _diyConfig = Commonjs.getDiyConfig();
	if(_diyConfig){
		var cfg = _diyConfig.outpatientPayRecordListHis;
		if(cfg){
			var _hisOrderPayType = cfg.hisOrderPayType;
			if(_hisOrderPayType){
				hisOrderPayType = _hisOrderPayType;
			}
			var _hisOrderChargeType = cfg.hisOrderChargeType;
			if(_hisOrderChargeType){
				hisOrderChargeType = _hisOrderChargeType;
			}
		}
	}
	
	var hisOrderPayTypeHtml = '<li class="curr"> <a href="javascript:;"  value="">全部</a></li>';
	for(var o in hisOrderPayType){
		var v = hisOrderPayType[o];
		hisOrderPayTypeHtml+='<li><a href="javascript:;" value="'+ o*1 +'">'+ v +'</a> </li>';
	}
	$("#orderType").html(hisOrderPayTypeHtml);

	var hisOrderChargeTypeHtml='<li class="curr"> <a href="javascript:;" value="">全部</a> </li>';
	for(var o in hisOrderChargeType){
		var v = hisOrderChargeType[o];
		hisOrderChargeTypeHtml+='<li><a href="javascript:;" value="'+ o*1 +'">'+ v +'</a> </li>';
	}
	$("#chargeType").html(hisOrderChargeTypeHtml);
	
	initWidget();
});

/** 初始化控件 */
function initWidget() {
	$("#loading").toggle();
	$('.c-main').on('click', '.prefilter li', function() {
		$(this).addClass('curr').siblings().removeClass('curr');
	});
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
		pageIndex = 0; 
		loadRecordList();
	});
	// 分页
	$(document.body).infinite().on("infinite", function() {
		if(loading) return;
		loading = true;
		setTimeout(function() {
			//执行加载
			loadRecordList();
		}, 300);
	});
	
	var nowDate = new Date();
	var year = nowDate.getFullYear();
	var mouth = nowDate.getMonth() + 1;
	mouth = (mouth<10)?'0'+mouth:mouth;
	$("#dateStar").picker({
	    title: "选择起始日期",
	    cols: [
			{
	            textAlign: 'center',
	            values: Draw(2000)
	        },
			{
				divider: true,  
				content: '年'
			},
			{
	            textAlign: 'center',
	            values: ['01','02','03','04','05','06','07','08','09','10','11','12']
	        },
			{
				divider: true,  
				content: '月'
			}
	    ],
	    value:[year,mouth]
	});
	$("#dateStar").val(year+" 年 "+mouth);
	$("#dateEnd").picker({
	    title: "选择结束日期",
	    cols: [
			{
	            textAlign: 'center',
	            values: Draw(2000)
	        },
			{
				divider: true,  
				content: '年'
			},
			{
	            textAlign: 'center',
	            values: ['01','02','03','04','05','06','07','08','09','10','11','12']
	        },
			{
				divider: true,  
				content: '月'
			}
	    ],
		value:[year,mouth]
	});
	$("#dateEnd").val(year+" 年 "+mouth);
	$("#noData").show();
	
	$("#filter-jzr").memberPicker({
		cardType: Commonjs.constant.cardType_1,
		pickerOnClose:function(obj,member){
			selectedMember = member;
			pageIndex = 0;
			loadRecordList();
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			loadRecordList();
		}
	})
	
	
//	Commonjs.memberPicker({
//		page:'outpatientPayRecordListHis',
//		cardType: Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		pickerOnClose:function(obj,member){
//			selectedMember = member;
//			pageIndex = 0;
//			loadRecordList();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			loadRecordList();
//		}
//	});
}

//年份
function Draw(n) {
	var formdate = new Date();
	var year = formdate.getFullYear();
	var data = new Array();
    for(var i=n;i<=year;i++){
        data.push(i);
    }
    return data;
}

/** 加载充值记录列表 */
function loadRecordList() {
	//是否分页查询
	if(pageIndex == 0 ){
		$("#list").html("");
	}
	//选择的就诊人没有住院号
	if(Commonjs.isEmpty(selectedMember.CardNo)){
		$("#loading").hide();
		return;
	}
	var dateStart =$("#dateStar").val().replace(' 年 ','-')+ "-01";
	var endTimeArray = $("#dateEnd").val().split(' 年 ');
	var endYear = endTimeArray[0];
	var endMonth = endTimeArray[1];
	var dateEnd = endYear+"-"+endMonth+"-"+getLastDay(endYear,endMonth);
	if( new Date(dateStart.replace(/\-/g, "\/"))>new Date(dateEnd.replace(/\-/g, "\/"))){
		myLayer.alert('开始时间不能大于结束时间', 3000);
	}
	var apiData = {};
	apiData.OderType = $("#orderType .curr").children().attr('value');
	apiData.ChargeType = $("#chargeType .curr").children().attr('value');
	apiData.BeginDate = dateStart;
  	apiData.EndDate = dateEnd;
  	apiData.CardNo = selectedMember.CardNo;
  	apiData.CardType = selectedMember.CardType;
  	var page = {};
  	page.PIndex = pageIndex;
  	page.PSize = pageSize;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	
	$("#noData").hide();
	$("#loading").show();
	var recordList = '';
	Commonjs.ajax('../../wsgw/order/QueryOutpatientRechargeList/callApi.do', param, function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					if(Commonjs.ListIsNotNull(jsons.Data)) {
						Commonjs.BaseForeach(jsons.Data, function(i, item) {
							if(i % 2 == 0) {
								recordList += "<li>";
							} else {
								recordList += "<li class='c-list-a'>";
							}
							recordList += "<div class='pm-title'>" + item.OrderMemo + "</div><ul class='visit-mess c-f14'>";
							recordList += "<li><div class='vm-key c-666'>充值金额：</div><div class='vm-info'>￥<span class='c-ffac0b'>" + item.PayMoney / 100 + "</span></div>";
							recordList += "</li><li> <div class='vm-key c-666'>充值时间：</div><div class='vm-info'>" + item.HisRechargeDate.substring(0, 16) + "</div>";
							recordList += "</li><li><div class='vm-key c-666'>充值类型：</div><div class='vm-info'>" + getRechargeType(item.ChargeType) + "</div> </li></ul></li>";
						});
						 $("#noData").hide();
	            		 if(hasNextPage(jsons.Page.PCount)){
	            			 loading = false;
	            			 pageIndex ++ ;
	            		 }else{
	            			 loading = true;
	            		 }
					}else{
						if(pageIndex==0){
							$("#loading").hide();
							$("#noData").show();
						}else{
							$("#loading").hide();
							$("#nomore").show();
						}
	            	 }
				}  else {
	            	myLayer.alert(jsons.RespMessage,3000);
	            }
			} else {
				myLayer.alert("返回码为空");
			}
			//$("#loading").hide();
			$("#list").append(recordList);
			//$("#loading").show();
		} else {

			// 通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	},{async:false});
}

function getLastDay(year,month){
	var new_year = year;//取当前的年份   
	var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）   
	if(month>12)//如果当前大于12月，则年份转到下一年   
	{
		new_month -=12;//月份减   
		new_year++;//年份增
	}
	var new_date = new Date(new_year,new_month,1);//取当年当月中的第一天   
	return (new Date(new_date.getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期
}

/** 获取充值类型 */
function getRechargeType(payWay) {
	if(payWay == 0) {
		return '现金';
	} else if(payWay == 1) {
		return '支付宝';
	} else if(payWay == 2) {
		return '银行卡';
	}else if(payWay == 3) {
		return '微信';
	}
}

/**
 * 是否还有下一页
 */
function hasNextPage(pageCount){
	if(Math.ceil(pageCount/pageSize) - 1 == pageIndex ){
		return false;
	}
	return true;
}

