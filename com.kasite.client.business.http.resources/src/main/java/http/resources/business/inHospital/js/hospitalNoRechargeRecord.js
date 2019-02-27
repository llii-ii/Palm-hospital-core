var loading = true;
var cardNO = new Array();
var menberNameArr = new Array();
var pIndex = 0;//分页页数
var year = '';
var mouth = '';
var beginDate = '';
var endDate = '';

var setstar = false;
var setend = false;
var selectedMember;
$(function(){
	//分页
	$(document.body).infinite().on("infinite", function() {
		if(loading) {
			return;
		}
		loading = true;
		setTimeout(function() {
			pIndex = pIndex + 1 ;
			getOrderList();
			
		}, 300);
	});
	

	
	

	init();
});

//选项
$('.c-main').on('click','.prefilter li',function(){
	$(this).addClass('curr').siblings().removeClass('curr');
});




function init(){
	$(".nomore").hide();
	$(".infinite-scroll").hide();
	clearPage();
	$(".nomess").hide();
	setDate();	
	//就诊人切换
//	queryMemberList();
	$("#filter-jzr").memberPicker({
		cardType: Commonjs.constant.cardType_14,
		pickerOnClose:function(obj,member){
			selectedMember = member;
			//执行切换
			clearInfinite();
			$(".order-list").html('');
			if(Commonjs.isEmpty(selectedMember.CardNo)){
				$(".nomess").show();
			}else{
				$(".nomess").hide();
				getOrderList();
			}
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			clearInfinite();
			$(".order-list").html('');
			if(!Commonjs.isEmpty(selectedMember.CardNo)){
				$(".nomess").hide();
				getOrderList();
			}else{
				$(".nomess").show();
			}
		}
	})

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
		
		clearInfinite();
		$(".order-list").html('');
		getOrderList();
	});
}
//为日期控件赋值
function setDate(){
	 var formdate = new Date();
		year = formdate.getFullYear();
		setstar = false;
		setend = false;
		mouth = formdate.getMonth() + 1;
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
     onChange:function(picker, values, displayValues){
     	if(!setstar){
			$("#dateStar").picker("setValue", [year, mouth]);
			setstar = true;
		}
     	if(values.length ==2){
     		year = values[0];
     		mouth = values[1];
    		$("#dateStar").val(year + '年' + mouth + '月');
    		beginDate = year+"-"+mouth;
     	}
     },
     onClose : function (){
 		$("#dateStar").val($("#dateStar").val()+ ' 月 ');
	}
 });
	$("#dateStar").val(year + '年' + mouth + '月');
	beginDate = year+"-"+mouth;
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
     onChange:function(picker, values, displayValues){
     	if(!setend){
			$("#dateEnd").picker("setValue", [year, mouth]);
			setend = true;
		}
     	if(values.length ==2){
     		year = values[0];
     		mouth = values[1];
    		$("#dateEnd").val(year + '年' + mouth + '月');
    		endDate = year+"-"+mouth;
     	}
     },
	onClose : function (){
		$("#dateEnd").val($("#dateEnd").val()+ ' 月 ');
	}
 });
	$("#dateEnd").val(year + '年' + mouth + '月');
	endDate = year+"-"+mouth;
}

//年份
function Draw(n) {
	var data = new Array();
    for(var i=n;i<=year;i++){
        data.push(i);
    }
    return data;
}



//查询就诊人列表
//function queryMemberList(){
//	var apiData = {};
//	apiData.OpId=Commonjs.getOpenId();
//	apiData.HosId = Commonjs.getUrlParam("HosId");
//	var page = {};
//	page.PIndex =0;
//	page.PSize = 0;
//	var param = {};
//	param.apiParam =Commonjs.getApiReqParams(apiData,page);
//	Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do',param,function(jsons){
//		if(!Commonjs.isEmpty(jsons)){
//			if(!Commonjs.isEmpty(jsons.RespCode)){
//			if(jsons.RespCode==10000){
//				var menberIDs = new Array();
//				var menberNames = new Array();
//				menberNameArr = new Array();
//				cardNO = new Array();
//				var defauleIndex = 0 ;//默认就诊人对应下标
//				if(Commonjs.ListIsNotNull(jsons.Data)){
////					if(jsons.Data.length==undefined){
////						if(!Commonjs.isEmpty(jsons.Data.isDefault)&&jsons.Data.isDefault=='1'){
////							defauleIndex = 0;
////						}
////						menberIDs[0] = jsons.Data.MemberId,
////						menberNames[0] = jsons.Data.MemberName,
////						menberNameArr[jsons.Data.MemberId] = jsons.Data.MemberName,
////						cardNO[jsons.Data.MemberId] = jsons.Data.HospitalNo;
////					}else{
//						Commonjs.BaseForeach(jsons.Data, function(i,item){
//							if(!Commonjs.isEmpty(item.isDefault)&&item.isDefault=='1'){
//								defauleIndex = i;
//							}
//								menberIDs[i] = item.MemberId,
//								menberNames[i] = item.MemberName,
//								menberNameArr[item.MemberId] = item.MemberName,
//								cardNO[item.MemberId] = item.HospitalNo;
//						});
////					}
//					$("#filter-jzr").picker({
//						title: "就诊人切换",
//						cols: [
//							{
//						textAlign: 'center',
//						values:menberIDs,
//						displayValues:menberNames
//						
//				},
//			],
//			onChange:function($k, values, displayValue){
//			},
//			onClose:function(){
//				
//				//执行切换
//				clearInfinite();
//				$(".order-list").html('');
//				$('#jzrword').html(menberNameArr[$("#filter-jzr").val()]);
//				if(Commonjs.isEmpty(cardNO[$("#filter-jzr").val()])){
//					$(".nomess").show();
//					$('.c-999').html("住院号：未绑定");
//					}else{
//						$(".nomess").hide();
//						$('.c-999').html("住院号："+cardNO[$("#filter-jzr").val()]);
//						getOrderList();
//					}
//			}
//		});
//					clearInfinite();
//					$(".order-list").html('');
//					if(!Commonjs.isEmpty(menberIDs[defauleIndex])){
//					$("#filter-jzr").val(menberIDs[defauleIndex]);
//					$('#jzrword').html(menberNameArr[$("#filter-jzr").val()]);
//					if(Commonjs.isEmpty(cardNO[$("#filter-jzr").val()])){
//						$(".nomess").show();
//						$('.c-999').html("住院号：未绑定");
//					}else{
//						$(".nomess").hide();
//						$('.c-999').html("住院号："+cardNO[$("#filter-jzr").val()]);
//						getOrderList();
//					}
//					}else{
//						$(".nomess").show();
//					}
//				}
//			}
//			else {
//            	myLayer.alert(jsons.RespMessage,3000);
//            }
//			}
//			else{
//				myLayer.alert("返回码为空",3000);
//			}
//			
//		}
//		else{
//			//通信失败
//        	myLayer.alert('网络繁忙,请刷新后重试',3000);
//		}
//    });
//
//}	

//获取本地订单列表
function getOrderList(){
	if(Commonjs.isEmpty(selectedMember.CardNo)){
		$(".nomess").show();
		return;
		
	}
	var page = {};
	page.PIndex = pIndex;
	page.PSize = 10;
	var apiData = {};
	
	var msg = checkMsg();
	if(!Commonjs.isEmpty(msg)){
		myLayer.clear();
    	myLayer.alert(msg,3000);
    	return ;
	}
	
	apiData.CardNo = selectedMember.CardNo;
	apiData.CardType=14;
	apiData.BeginDate=beginDate+"-01";
	apiData.EndDate=getMonthLastDate(endDate);
	apiData.PayState='2';
	apiData.BizState='1';//退费还未完善，先查询支付成功的
	apiData.ServiceId ="007";
	var param = {};
	param.api = 'OrderListLocal'; 
	param.apiParam =Commonjs.getApiReqParams(apiData,page);
	Commonjs.ajax('../../wsgw/order/OrderListLocal/callApi.do',param,function(jsons){
		if(!Commonjs.isEmpty(jsons)){
			if(!Commonjs.isEmpty(jsons.RespCode)){
			if(jsons.RespCode==10000){
				var listHtml = $(".order-list").html();
				var DateSize = 0 ;//后台返回数据条数
				if(Commonjs.ListIsNotNull(jsons.Data)){
//					if(jsons.Data.length == undefined){
//
//						listHtml += "<li><div class=\"pm-title\">预交金充值</div>";
//						listHtml += "<ul class=\"visit-mess c-f14\"><li>";
//						listHtml += "<div class=\"vm-key c-666\">充值金额：</div>";
//						listHtml += "<div class=\"vm-info\">￥<span class=\"c-ffac0b\">"+Commonjs.centToYuan(jsons.Data.PayMoney)+"</span></div>";
//						listHtml += "</li><li>";
//						listHtml += "<div class=\"vm-key c-666\">充值时间：</div>";
//						listHtml += "<div class=\"vm-info\">"+jsons.Data.BeginDate+"</div>";
//						listHtml += "</li><li>";
//						listHtml += "<div class=\"vm-key c-666\">充值类型：</div>";
//						if(jsons.Data.ChannelId=='100123'){
//							listHtml += "<div class=\"vm-info\">微信充值</div>";
//						}else if(jsons.Data.ChannelId=='100125'){
//							listHtml += "<div class=\"vm-info\">支付宝充值</div>";
//						}else{
//							listHtml += "<div class=\"vm-info\">在线充值</div>";
//						}
//						listHtml += "</li></ul></li>";
//						DateSize++;
//					}else{
						Commonjs.BaseForeach(jsons.Data, function(i,item){
							listHtml += "<li><div class=\"pm-title\">预交金充值</div>";
							listHtml += "<ul class=\"visit-mess c-f14\"><li>";
							listHtml += "<div class=\"vm-key c-666\">充值金额：</div>";
							listHtml += "<div class=\"vm-info\">￥<span class=\"c-ffac0b\">"+Commonjs.centToYuan(item.PayMoney)+"</span></div>";
							listHtml += "</li><li>";
							listHtml += "<div class=\"vm-key c-666\">充值时间：</div>";
							listHtml += "<div class=\"vm-info\">"+item.BeginDate+"</div>";
							listHtml += "</li><li>";
							listHtml += "<div class=\"vm-key c-666\">充值类型：</div>";
							if(item.ChannelId=='100123'){
								listHtml += "<div class=\"vm-info\">微信充值</div>";
							}else if(item.ChannelId=='100125'){
								listHtml += "<div class=\"vm-info\">支付宝充值</div>";
							}else{
								listHtml += "<div class=\"vm-info\">在线充值</div>";
							}
							listHtml += "</li></ul></li>";
							DateSize++;
							});
//					}
					$(".order-list").html(listHtml);
					
					if(DateSize < page.PSize){
						//后台返回数据比当前分页数据少并且加载过一次分页的动作则显示没有更多数据
						$(".nomore").show();
						loading = true;//关闭分页
					}else{
						loading = false;//为下次拉页面触发分页做准备
					}
				}else if(page.PIndex == 0){
					clearPage();
				}
			}
			else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
			}
			else{
				myLayer.alert("返回码为空",3000);
			}
			
		}
		else{
			//通信失败
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
		}
    },{async:false});
}

//清除分页查询信息
function clearInfinite(){
	loading = false;
	pIndex = 0;
	$(".nomore").hide();
}
function clearPage(){
	$(".order-list").html('');
	$(".nomess").show();
}

//根据YYYY-MM取月末返回格式yyyy-mm-dd
function getMonthLastDate(endDate){
	var date = new Date(endDate.split("-")[0],endDate.split("-")[1],0)
	return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
}

function checkMsg (){
	if(!Commonjs.isEmpty(beginDate)&&!Commonjs.isEmpty(endDate)){
		var begin = new Date(beginDate);
		var end = new Date(endDate);
		if(beginDate>endDate){
			return '开始日期不能大于结束日期!';
		}
	}
}

