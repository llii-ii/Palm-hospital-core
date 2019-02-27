var loading = false;
var pageIndex = 0;
var pageSize = 5;
//被选中用户的全局变量
var selectedMember = {};

//复选框选中的hisorderid
var selecedHisOrderIds = "";
var selecedPrescNos = "";
//复选框选中数
var selectedCount = 0;
//选中金额总数
var selectedMoney = 0;
$(function(){
	initWidget();
});

/**
 * 初始化控件
 */
function initWidget(){

	//筛选
	$('.filter-into').on('click', function() {
		$(this).toggleClass('curr');
		$('#listPart').toggle();
		$('#filterPart').slideToggle(300);
	});
	
	//确定
	$('#filter-btn').on('click', function() {
		$('.filter-into').removeClass('curr');
		$('#listPart').show();
		$('#filterPart').hide();
		index = 0;
		$("#totalMoney").html("0");
		$("#orderNum").html("0");
		selectCount = 0;
		selectMoney = 0;
		orderIds = "";
		pageIndex = 0; 
		queryOrderSettlementList();
	});

	//选项
	$('.c-main').on('click','.prefilter li',function(){
		$(this).addClass('curr').siblings().removeClass('curr');
	});
	
	var nowDate = new Date();
	var year = nowDate.getFullYear();
	var mouth = nowDate.getMonth() + 1;
	mouth = (mouth<10)?'0'+mouth:mouth;
	
	//开始日期
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
	//结束日期
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
			selectedMoney = 0;
			selectedCount = 0;
			selecedHisOrderIds = "";
			selecedReceiptNames = "";
			selecedReceiptPrices="";
			$("#totalMoney").html(Commonjs.centToYuan(selectedMoney));
			$("#orderNum").html(selectedCount);
			queryOrderSettlementList();
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			queryOrderSettlementList();
		}
		
	});
	
//	Commonjs.memberPicker({
//		page:'orderSettlementList',
//		cardType: Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		pickerOnClose:function(obj,member){
//			selectedMember = member;
//			pageIndex = 0;
//			selectedMoney = 0;
//			selectedCount = 0;
//			selecedHisOrderIds = "";
//			selecedReceiptNames = "";
//			selecedReceiptPrices="";
//			$("#totalMoney").html(Commonjs.centToYuan(selectedMoney));
//			$("#orderNum").html(selectedCount);
//			queryOrderSettlementList();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			queryOrderSettlementList();
//		}
//	});
	
	//分页
	$(document.body).infinite().on("infinite", function() {
		//console.info(loading);
		if(loading) return;
		loading = true;
		setTimeout(function() {
			//执行加载
			queryOrderSettlementList();
		}, 300);
	});
	
	//选择
	$('.c-main').on('click','.input-checkbox',function(){
		var checked = $(this).find('.input-pack').hasClass('checked');
		var all = $(this).attr('data-check') == 'all';
		if(checked){
			$(this).find('.input-pack').removeClass('checked').find('input').removeAttr('checked');
			if(all){
				$('div[data-check=check]').find('.input-pack').removeClass('checked').find('input').removeAttr('checked');
			}else{
				ischeckAll();	
			}
		}else{
			$(this).find('.input-pack').addClass('checked').find('input').attr('checked','true');
			if(all){
				$('div[data-check=check]').find('.input-pack').addClass('checked').find('input').attr('checked','true');
			}else{
				ischeckAll();	
			}
		}
		selectedMoney = 0;
		selectedCount = 0;
		selecedHisOrderIds = "";
		selecedPrescNos = "";
		$('div[data-check=check]').find('.input-pack').each(function(i){
			if($(this).find('input').attr('checked')){
				selectedMoney += Number($(this).parent().parent().find("span[data-info='1']").attr("data-money"));
				selecedHisOrderIds += $(this).parent().parent().find("span[data-info='1']").attr("data-cid") + ",";
				var presNo = $(this).parent().parent().find("span[data-info='1']").attr("data-presNo")+ ",";
				if( !Commonjs.isEmpty(presNo) ){
					selecedPrescNos +=presNo;
				}
				selectedCount++;
			}
		});
		selecedHisOrderIds = selecedHisOrderIds.substring(0,selecedHisOrderIds.length-1);
		selecedPrescNos = selecedPrescNos.substring(0,selecedPrescNos.length-1);
		$("#totalMoney").html(Commonjs.centToYuan(selectedMoney));
		$("#orderNum").html(selectedCount);
	});
}

/**
 * 查询订单列表
 */
function queryOrderSettlementList(){
	var dateStart =$("#dateStar").val().replace(' 年 ','-')+ "-01";
	var endTimeArray = $("#dateEnd").val().split(' 年 ');
	var endYear = endTimeArray[0];
	var endMonth = endTimeArray[1];
	var dateEnd = endYear+"-"+endMonth+"-"+getLastDay(endYear,endMonth);
	var isSettlement = $("#isSettlementUl .curr").children().attr('value');
	var listHtml = '';
	var apiData = {};
	var page = {};
  	page.PIndex = pageIndex;
  	page.PSize = pageSize;
  	apiData.CardNo = selectedMember.CardNo;
  	apiData.CardType = selectedMember.CardType;
  	apiData.MemberId = selectedMember.MemberId;
  	apiData.IsSettlement = isSettlement;
  	apiData.BeginDate = dateStart;
  	apiData.EndDate = dateEnd;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData,page,"");
	//是否分页查询
	if(pageIndex == 0 ){
		$("#orderList").html("");
	}
	$("#noData").hide();
	$("#loading").show();
	Commonjs.ajax('/wsgw/order/QueryOrderSettlementList/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
            	 count = jsons.Page.PCount;
            	 if (Commonjs.ListIsNotNull(jsons.Data)) {
            		 Commonjs.BaseForeach(jsons.Data, function (i, item) {
            			 listHtml += '<li>';
        				 listHtml += '<div class="input-checkbox" data-check="check">';
        				 listHtml += '<div class="input-pack mr5">';
        				 if(item.IsSettlement == 0){
        					 listHtml += '<i class="sicon"></i>';
        					 listHtml += '<input type="checkbox" name="n1" >';
        				 }
        				 listHtml += '</div>';
        				 listHtml += '</div>';
        				 listHtml += '<a href="javascript:void(0)" onclick="goToOrderDetail(\''+item.HisOrderId+'\')">';
        				 listHtml += '<h4><span class="fr c-ffac0b">';
        				 if(item.IsSettlement == '1'){
        					 listHtml += '已结算';
        				 }else{
        					 listHtml += '末结算';
        				 }
        				 listHtml += '</span>'+item.PrescType+'</h4>';
        				 listHtml += '<div class="ui-grid ol-mess">';
        				 listHtml += '	<div class="ui-col-1 pr10">'+item.DeptName+"&emsp;&emsp;"+item.DoctorName+'</div>';
        				 listHtml += '	<div class="ui-col-0 c-pack" >￥'+Commonjs.centToYuan(item.Price)+'</div>';
        				 listHtml += '</div>';
        				 listHtml += ' <div class="ol-bot">';
        				 listHtml += '	<span class="fr c-999">'+item.PrescTime+'</span>';
        				 listHtml += ' 	小计：<span class="c-ffac0b" data-money="'+item.Price+'" data-presNo="'+item.PrescNo+'" data-cid="'+item.HisOrderId+'" data-info="1">￥'+Commonjs.centToYuan(item.Price)+'</span>';
        				 listHtml += ' </div>';
        				 listHtml += '</a>';
        				 listHtml += ' </li>';
            		 });
            		 $("#loading").hide();
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
            } else {
            	//$("#loading").hide();
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	//$("#loading").hide();
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		//$("#loading").hide();
		$("#orderList").append(listHtml);
    });
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

function settleNow(){
	if(Commonjs.isEmpty(selectedMoney) || selectedMoney == 0){
    	myLayer.alert("选择订单",3000);
    	return;
    }
	myLayer.confirm({
		title:'',
		con:'是否进行结算?',
		cancel: function(){
		},
		cancelValue:'\u53d6\u6d88',
		ok: function(){
			settleOrderSettlement();
		},
		okValue:'\u786e\u5b9a'
	});
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

function goToOrderDetail(hisOrderId) {
	location.href = Commonjs.goToUrl("/business/order/orderSettlementInfo.html?hisOrderId="+hisOrderId);
}

//是否全选
function ischeckAll(){
	var isall = true;
	$('div[data-check=check]').each(function() {
        if(!$(this).find('.input-pack').hasClass('checked')){
			isall = false;	
		}
    });
	if(isall){
		$('div[data-check=all]').find('.input-pack').addClass('checked').find('input').attr('checked','true');	
	}else{
		$('div[data-check=all]').find('.input-pack').removeClass('checked').find('input').removeAttr('checked');
	}
}

function settleOrderSettlement(){
	var apiData = {};
	var param = {};
	apiData.CardNo=selectedMember.CardNo;
	apiData.CardType=selectedMember.CardType;
  	apiData.MemberId = selectedMember.MemberId;
	apiData.TotalPrice = selectedMoney;
	apiData.HisOrderIds = selecedHisOrderIds;
	apiData.PrescNos = selecedPrescNos;
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax("/wsgw/order/SettleOrderSettlement/callApi.do?t=" + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
            	location.href = Commonjs.goToUrl('/business/order/settlementSuccess.html');
            }else if(jsons.RespCode == -63011) { //钱不够
            	myLayer.alert(jsons.RespMessage,3000);
            	//跳去充值界面
            	location.href = Commonjs.goToUrl("/business/outpatientDept/outpatientRecharge.html?&cardNo=" + settlementInfo.CardNo + "&cardType=" +settlementInfo.CardType);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    });
}