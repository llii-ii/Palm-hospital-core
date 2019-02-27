//分页控件标识
var loading = false;
var pageIndex = 0;
var pageSize = 5;
//被选中用户的全局变量
var selectedMember = {};

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
		pageIndex = 0; 
		queryOrderPrescriptionList();
	});

	//选项
	$('.c-main').on('click','.prefilter li',function(){
		$(this).addClass('curr').siblings().removeClass('curr');
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
	
	//就诊人初始化
	$("#filter-jzr").memberPicker({
		cardType: Commonjs.constant.cardType_1,
		pickerOnClose:function(obj,member){
			selectedMember = member;
			pageIndex = 0;
			queryOrderPrescriptionList();
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			queryOrderPrescriptionList();
		}
	})
	
//	Commonjs.memberPicker({
//		page:'orderPrescriptionList',
//		cardType: Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		pickerOnClose:function(obj,member){
//			selectedMember = member;
//			pageIndex = 0;
//			queryOrderPrescriptionList();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			queryOrderPrescriptionList();
//		}
//	});

	
	
	//分页
	$(document.body).infinite().on("infinite", function() {
		if(loading) return;
		loading = true;
		setTimeout(function() {
			//执行加载
			queryOrderPrescriptionList();
		}, 300);
	});
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
/**
 * 查询订单列表
 */
function queryOrderPrescriptionList(){
	var memberId = selectedMember.MemberId;
    var dateStart =$("#dateStar").val().replace(' 年 ','-')+ "-01";
    var endTimeArray = $("#dateEnd").val().split(' 年 ');
    var endYear = endTimeArray[0];
    var endMonth = endTimeArray[1];
	var dateEnd = endYear+"-"+endMonth+"-"+getLastDay(endYear,endMonth);
	var listHtml = '';
	var apiData = {};
	var page = {};
  	page.PIndex = pageIndex;
  	page.PSize = pageSize;
  	apiData.CardNo = selectedMember.CardNo;
  	apiData.CardType = selectedMember.CardType;
  	apiData.MemberId = memberId;
  	apiData.BeginDate = dateStart;
  	apiData.EndDate = dateEnd;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData,page,"");
	//是否分页查询
	if(pageIndex == 0 ){
		$("#orderList").html("");
	}
	$("#loading").show();
	Commonjs.ajax('/wsgw/order/QueryOrderPrescriptionList/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
            	 if (Commonjs.ListIsNotNull(jsons.Data)) {
            		 Commonjs.BaseForeach(jsons.Data, function (i, item) {
            			 listHtml += '<li>';
        			 	 listHtml += '<a class="c-arrow-r" href="javascript:void(0)" onclick="goToOrderDetail(\''+item.HisOrderId+'\',\''+ memberId +'\')">';
        				 listHtml += '<h4><span class="fr c-ffac0b">';
        				 if(item.OrderState == '0'){
        					 listHtml += '待支付';
        				 }else if(item.OrderState == '1'){
        					 listHtml += '正在支付';
        				 }else if(item.OrderState == '2'){
        					 listHtml += '支付完成';
        				 }else if(item.OrderState == '5'){
        					 listHtml += '已取药';
        				 }else if(item.OrderState == '3'){
        					 listHtml += '正在退费';
        				 }else if(item.OrderState == '4'){
        					 listHtml += '已退费';
        				 }else{ 
        					 listHtml += '其他';
        				 }
        				 listHtml += '</span>'+item.ServiceName+'</h4>';
        				 listHtml += '<div class="ui-grid ol-mess">';
        				 listHtml += '	<div class="ui-col-1 pr10">'+item.DeptName+"&emsp;&emsp;"+item.DoctorName+'</div>';
        				 listHtml += '	<div class="ui-col-0 c-pack" >￥'+Commonjs.centToYuan(item.PayMoney)+'</div>';
        				 listHtml += '</div>';
        				 listHtml += ' <div class="ol-bot">';
        				 listHtml += '	<span class="fr c-999">'+item.PrescTime+'</span>';
        				 listHtml += ' 	小计：<span class="c-ffac0b" data-money="'+item.PayMoney+'" data-cid="'+item.HisOrderId+'" id="fee">￥'+Commonjs.centToYuan(item.PayMoney)+'</span>';
        				 listHtml += ' </div>';
        				 listHtml += '</a>';
        				 listHtml += ' </li>';
            		 });
            		 $("#noData").hide();
            		 if(hasNextPage(jsons.Page.PCount)){
            			 loading = false;
            			 pageIndex ++ ;
            		 }else{
            			 loading = true;
            		 }
            		 $("#loading").hide();
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
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
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

function goToOrderDetail(hisOrderId,memberId) {
    location.href = Commonjs.goToUrl("/business/order/orderPrescriptionInfo.html?hisOrderId=" + hisOrderId+"&memberId="+ memberId);
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


