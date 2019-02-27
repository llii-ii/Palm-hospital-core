var loading = false;
var pIndex = 0;//分页页数全局变量
var cardType='';
var cardNo='';
var memberId="";
var orderId;
var beginDate="";
var cardTypes = "";
var cardNos = "";
var setstar =false;
var noMember = false;
var localRegFlag = '';
var payState = '';
var bizState = '';
var overState = '';
var hosId='';
var queryServiceId = '';
var openServices = {
		"0":"挂号",
		"008":"处方",
		"001":"西药",
		"002":"中成药",
		"003":"草药",
		"004":"非药品",
		"007":"住院预交金"
}
$(function(){
	hosId = Commonjs.getUrlParam("HosId");
	queryServiceId = Commonjs.getUrlParam("queryServiceId");
	
	loadMemberList();
	//默认开通的业务类型列表
	var diyConfig = Commonjs.getDiyConfig();
	if(diyConfig && diyConfig.openServices){
		openServices = diyConfig.openServices;
	}
	var html = '<li class="curr"><a href="javascript:void(0);" onclick="setServiceType(\'\')">全部</a></li>';
	for(var o in openServices){
		var v = openServices[o];
		html += '<li><a href="javascript:void(0);" onclick="setServiceType(\''+o+'\')">'+v+'</a></li>';
	}
	$("#servicesidUl").html(html);
	/*
	 <li class="curr"><a href="javascript:void(0);" onclick="setServiceType('')">全部</a></li>
	<li><a href="javascript:void(0);" onclick="setServiceType('0')">挂号</a></li>
	<li><a href="javascript:void(0);" onclick="setServiceType('008')">医嘱</a></li>
	<li><a href="javascript:void(0);" onclick="setServiceType('007')">住院预交金</a></li>
	 */
	
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
		$("#orderlist").empty();
		pIndex=0;
		loadData();
	});
	//分页
	$(document.body).infinite().on("infinite", function() {
		if(loading) {
			return;
		}
		loading = true;
		pIndex += 1;
		setTimeout(function() {
			$(".nomess").css("display", "none");
			loadData();
		}, 300);
	});
	//日期选择
    var formdate = new Date(),
	setstar = false,
	setend = false,
	year = formdate.getFullYear(),
	mouth = formdate.getMonth() + 1;
	mouth = (mouth<10)?'0'+mouth:mouth;
    $("#filter-date").picker({
        title: "选择日期",
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
				$("#filter-date").picker("setValue", [year, mouth]);
				$("#dateword").html(year + '年' + mouth + '月');
				setstar = true;
			}else{
				$("#dateword").html(values[0] + '年' + values[1] + '月');
				beginDate = values[0] + '-' + values[1];
			}
        },
    	onClose:function(p){
    		beginDate = p.cols[0].value + '-' + p.cols[2].value;
    		$('.order-top-date').removeClass('curr');
    		$("#orderlist").html("");
    		loading=false;
    		pIndex = 0;
    		loadData();
    	}
    });
    //选项
    $('.c-main').on('click','.prefilter li',function(){
    	$(this).addClass('curr').siblings().removeClass('curr');
    });
    //年份
	function Draw(n) {
		var data = new Array();
		for(var i=n;i<=year;i++){
			data.push(i);
		}
		return data;
	}
	setMonth();
}); 

function setServiceType(serviceId){
	queryServiceId = serviceId;
}
//默认日期设置(当前月份前1个月)
function setMonth() {
	var formdate = new Date(),
	year = formdate.getFullYear(),
	mouth = formdate.getMonth() + 1;
    var eYear = year;
    var eMonth = mouth - 1;
    if(eMonth<1){
    	eYear--;
    	eMonth += 12;
    }
    eMonth = (eMonth<10)?'0'+eMonth:eMonth;
    $("#dateword").html(eYear+"年"+eMonth+"月");
    beginDate = eYear+"-"+eMonth;
  };
/**加载就诊人列表*/
function loadMemberList(){
  	var apiData = {};
  	var param = {};
  	param.api = '';
	apiData.HosId = Commonjs.getUrlParam("HosId");
  	param.apiParam = Commonjs.getApiReqParams(apiData, "");
  	Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do', param, function(jsons) {
  		if(!Commonjs.isEmpty(jsons)){
  			var listHtmls='';
  			if(!Commonjs.isEmpty(jsons.RespCode)){
	  			if(jsons.RespCode==10000){
	  				if(Commonjs.ListIsNotNull(jsons.Data)){
	  					listHtmls+='<li id = \"all\" class=\"curr\"><a onclick="init(\'\',\'\')" href=\"javascript:;\">全部</a></li>';	  					
	  					var usecard = 0;
	  					Commonjs.BaseForeach(jsons.Data, function(i,item){
	  	            		if((item.MemberId!='')){
	  	            			usecard++;
	  			            	listHtmls += '<li id=\"'+item.MemberId+'\"><a  onclick="init(\''+item.MemberId+'\')" href="javascript:;">'+item.MemberName+'</a></li>';
	  	            		}
	  					});
	  					if(usecard==0){
	  						//只有有未添加卡号的就诊人相当于没有订单数据
		  					noMember = true;
	  					}
	  					$("#memberUl").html(listHtmls);
	  					
	  					if(Commonjs.ListIsNotNull(Commonjs.getLocalItem("memberId",false,false))){
	  						memberId = Commonjs.getLocalItem("memberId",false,false);
		  					$("#"+memberId +'>a').click();
		  					$("#all").removeClass("curr");
		  					$("#"+memberId).addClass("curr");
	  					}
//	  					if(Commonjs.ListIsNotNull(localStorage.getItem("memberId"))){
//	  						memberId = localStorage.getItem("memberId");
//		  					$("#"+memberId +'>a').click();
//		  					$("#all").removeClass("curr");
//		  					$("#"+memberId).addClass("curr");
//	  					}
	  					loadData();
	  				}else{
	  					noMember = true;
	  				}
	  			}else {
	  				myLayer.alert(jsons.RespMessage,3000);
	  			}
  			}else{
  				myLayer.alert("返回码为空");
  			}
  		}else{
  			//通信失败
          	myLayer.alert('网络繁忙,请刷新后重试',3000);
  		}
   });
}
/**
 * 获取卡号，卡类型
 */
function init(mid){
	memberId = mid;
	Commonjs.setLocalItem("memberId",mid,false);
//	localStorage.setItem("memberId",mid);
}

/**
 * 加载列表数据
 */
function loadData(){
	$(".nomess").css("display", "none");
	if(noMember){
		$(".nomess").css("display", "block");
		return;
	}
  	var apiData = {};
  	apiData.BeginDate=beginDate+"-01";
  	if(Commonjs.isNotNull(queryServiceId)){
  		apiData.ServiceId=queryServiceId;
  	}else{
  		var serviceIds = '';
  		for(var o in openServices){
  			serviceIds += o+",";
  		}
  		serviceIds = serviceIds.substring(0,serviceIds.length-1);
  		apiData.ServiceId= serviceIds;
  	}
  	apiData.OpenId=Commonjs.getOpenId();
  	apiData.CardNo=cardNo;
  	apiData.MemberId = memberId;
  	apiData.CardType=cardType;
  	apiData.BizState=bizState;
  	apiData.PayState=payState;
  	apiData.OverState=overState;
  	var page = {};
  	page.PIndex =pIndex;
  	page.PSize = 10;
  	var param = {};
  	param.api = ''; 
  	param.apiParam =Commonjs.getApiReqParams(apiData,page);
  	Commonjs.ajax('../../wsgw/order/OrderListLocal/callApi.do',param,function(jsons){
  		if(!Commonjs.isEmpty(jsons)){
  			if(!Commonjs.isEmpty(jsons.RespCode)){
	  			if(jsons.RespCode==10000){
	  				var listHtml='';
	  				if(Commonjs.ListIsNotNull(jsons.Data)){
	  					Commonjs.BaseForeach(jsons.Data, function(i,item){
	  							if(item.ServiceId == '0'){
//	  								//挂号订单状态
//	  								if((item.PayState==2 || item.PayState==0) && item.BizState==1){
//	  									localRegFlag = '1';
//	  								}else if((item.PayState==4 ||item.PayState==3 || item.PayState==0)&& item.BizState==2){
//	  									localRegFlag = '2';
//	  								}else if((item.PayState==4 ||item.PayState==3 )&& item.BizState==0){
//	  									localRegFlag = '3';
//	  								}else if(item.PayState==0 && (item.OverState==5||item.OverState==6)){
//	  									localRegFlag = '3';
//	  								}else if(item.PayState==0 && item.OverState==0&&item.BizState==0){
//	  			  						localRegFlag = '0';
//	  								}
	  								var isOnlinePay = item.IsOnlinePay;
	  								var url = "/business/yygh/yyghAppointmentDetails.html?orderId="+item.OrderId;
	  								if(!Commonjs.isEmpty(queryServiceId)){
	  									url+='&queryServiceId='+queryServiceId;
	  								}
	  								url+='&isOnlinePay='+isOnlinePay;
	  								listHtml+='<li>'+'<a href=\"javascript:location.href=Commonjs.goToUrl(\''+url+'\')\" class=\"c-arrow-r\" ><h4>';
	  								
	  							}else if(item.ServiceId == '008'){
	  								//医嘱订单
	  								if( item.IsOnlinePay == 1 ){
	  									listHtml+='<li>'+'<a href=\"/business/order/orderPrescriptionInfo.html?hisOrderId='+item.PrescNo+'&memberId='+item.MemberId+'&orderId='+item.OrderId+'\" class=\"c-arrow-r\" ><h4>';
	  								} 
//	  								else{
//	  									listHtml+='<li>'+'<a href=\"/business/order/orderSettlementInfo.html?hisOrderId='+item.PrescNo+'&memberId='+item.MemberId+'&orderId='+item.OrderId+'\" class=\"c-arrow-r\" ><h4>';
//	  								}
	  							}else if(item.ServiceId == '001' || item.ServiceId == '002' || item.ServiceId == '003' || item.ServiceId == '001'){
	  									listHtml+='<li>'+'<a href=\"/business/order/orderPrescriptionInfo.html?hisOrderId='+item.PrescNo+'&memberId='+item.MemberId+'&orderId='+item.OrderId+'\" class=\"c-arrow-r\" ><h4>';
	  							}else if(item.ServiceId == '007'){
	  								//住院预交金
	  								listHtml+='<li>'+'<a href=\"/business/inHospital/hospitalNoRechargeDetail.html?orderId='+item.OrderId+'\" class=\"c-arrow-r\" ><h4>';
	  							}else if(item.ServiceId == '011'){
	  								//诊间合并支付
	  								listHtml+='<li>'+'<a href=\"/business/order/orderReceiptInfo.html?orderId='+item.OrderId+'\" class=\"c-arrow-r\" ><h4>';
	  							}else{
	  								return;
	  							}
	  							var memberName = item.MemberName;
	  							if(Commonjs.isEmpty(memberName)){
	  								memberName = '';
	  							}
	  							var isOnlinePay = item.IsOnlinePay;
	  							var payState = item.PayState;
	  							var bizState = item.BizState;
	  							var overState = item.OverState;
	  							var serviceId = item.ServiceId;
	  							var orderStateName = Commonjs.getOrderStateName(serviceId,isOnlinePay,payState,bizState,overState);
	  							listHtml+='<span class=\"fr c-ffac0b\" id=\"state\">'+ orderStateName +'</span>';

	  							if(item.ServiceId == '0'){
		  							//挂号订单
			  						var memo;
			  						var doctorName = "未知医生";
			  						var deptName = "未知科室";
			  						var doctorTitle = "";
				  					if(!Commonjs.isEmpty(item.OrderMemo)){
				  						memo = eval('('+item.OrderMemo+')');
				  						doctorName = (Commonjs.isEmpty(memo.DoctorName)?doctorName:memo.DoctorName);
				  						doctorTitle = (Commonjs.isEmpty(memo.DoctorTitle)?"":memo.DoctorTitle);
				  						deptName = (Commonjs.isEmpty(memo.DeptName)?deptName:memo.DeptName);
				  					}
			  						listHtml+= memberName +'('+getServiceTyep(item.ServiceId)+')'+'</h4><div class=\"ui-grid ol-mess\">'+
			  						'<div class=\"ui-col-1 pr10\">预约医师：'+ deptName +'<span class=\"ml10\">'+ doctorName +'</span>'+
			  						'<span class=\"ml10 c-f12 c-999\">'+ doctorTitle +'</span></div><div class=\"ui-col-0 c-pack\">￥'+(Commonjs.centToYuan(item.PayMoney))+'元'+
			  						'</div></div><div class=\"ol-bot\"><span class=\"fr c-999\">'+item.BeginDate+'</span>小计：'+
			  						'<span class=\"c-ffac0b\">￥'+(Commonjs.centToYuan(item.PayMoney))+'元</span></div></a></li>';
		  						}else if(item.ServiceId == '008'){
		  							var memo;
		  							var payMoney = Commonjs.centToYuan(item.PayMoney);
		  							var title = item.PriceName;
		  							if(!Commonjs.isEmpty(item.OrderMemo)){
		  								memo = eval('('+item.OrderMemo+')'); 
		  								title = (Commonjs.isEmpty(memo.title)?title:memo.title);
		  							}
		  							//医嘱订单
		  							listHtml+= memberName +'('+getServiceTyep(item.ServiceId)+')'+'</h4><div class=\"ui-grid ol-mess\">'+
			  						'<div class=\"ui-col-1 pr10\">'+ title +'<span class=\"ml10\"></span>'+
			  						'<span class=\"ml10 c-f12 c-999\"></span></div><div class=\"ui-col-0 c-pack\">￥'+ payMoney +'元'+
			  						'</div></div><div class=\"ol-bot\"><span class=\"fr c-999\">'+item.BeginDate+'</span>小计：'+
			  						'<span class=\"c-ffac0b\">￥'+(Commonjs.centToYuan(item.PayMoney))+'元</span></div></a></li>';
		  							
		  						}else if(item.ServiceId == '001' || item.ServiceId == '002' || item.ServiceId == '003' || item.ServiceId == '001'){
		  							var memo;
		  							var payMoney = Commonjs.centToYuan(item.PayMoney);
		  							var title = item.PriceName;
		  							if(!Commonjs.isEmpty(item.OrderMemo)){
		  								memo = eval('('+item.OrderMemo+')'); 
		  								title = (Commonjs.isEmpty(memo.title)?title:memo.title);
		  							}
		  							//医嘱订单
		  							listHtml+= memberName +'('+getServiceTyep(item.ServiceId)+')'+'</h4><div class=\"ui-grid ol-mess\">'+
			  						'<div class=\"ui-col-1 pr10\">'+ title +'<span class=\"ml10\"></span>'+
			  						'<span class=\"ml10 c-f12 c-999\"></span></div><div class=\"ui-col-0 c-pack\">￥'+ payMoney +'元'+
			  						'</div></div><div class=\"ol-bot\"><span class=\"fr c-999\">'+item.BeginDate+'</span>小计：'+
			  						'<span class=\"c-ffac0b\">￥'+(Commonjs.centToYuan(item.PayMoney))+'元</span></div></a></li>';
		  							
		  						}else if(item.ServiceId == '007'){
		  							listHtml+= memberName+'('+getServiceTyep(item.ServiceId)+')'+'</h4><div class=\"ui-grid ol-mess\">'+
			  						'<div class=\"ui-col-1 pr10\">'+item.OrderMemo+'<span class=\"ml10\"></span>'+
			  						'<span class=\"ml10 c-f12 c-999\"></span></div><div class=\"ui-col-0 c-pack\">￥'+(Commonjs.centToYuan(item.PayMoney))+'元'+
			  						'</div></div><div class=\"ol-bot\"><span class=\"fr c-999\">'+item.BeginDate+'</span>小计：'+
			  						'<span class=\"c-ffac0b\">￥'+(Commonjs.centToYuan(item.PayMoney))+'元</span></div></a></li>';
		  						}else if(item.ServiceId == '011'){
		  							listHtml+= memberName+'('+getServiceTyep(item.ServiceId)+')'+'</h4><div class=\"ui-grid ol-mess\">'+
			  						'<div class=\"ui-col-1 pr10\">'+item.OrderMemo+'<span class=\"ml10\"></span>'+
			  						'<span class=\"ml10 c-f12 c-999\"></span></div><div class=\"ui-col-0 c-pack\">￥'+(Commonjs.centToYuan(item.PayMoney))+'元'+
			  						'</div></div><div class=\"ol-bot\"><span class=\"fr c-999\">'+item.BeginDate+'</span>小计：'+
			  						'<span class=\"c-ffac0b\">￥'+(Commonjs.centToYuan(item.PayMoney))+'元</span></div></a></li>';
		  						}
	  					});
	  					loading=false;
	  					}else{
							if(pIndex==0){//第一页就没有数据,则显示无数据样式
								$(".nomess").css("display", "block");
							}else{
								$(".nomess").css("display", "none");
							}
							loading=true;
	  					}
	  				$("#orderlist").append(listHtml);
	  				myLayer.clear();
	  			}else {
              	myLayer.alert(jsons.RespMessage,3000);
              }
  			}else{
  				myLayer.alert("返回码为空");
  			}
  		}else{
  			//通信失败
          	myLayer.alert('网络繁忙,请刷新后重试',3000);
  		}
    });
}

function getServiceTyep(service){
	if(service == '0'){
			//挂号订单
			return '挂号';
		}else if(service == '008'){
			//医嘱订单
			return '医嘱';			
		}else if(service == '007'){
			//医嘱订单
			return '预交金充值';			
		}else if(service == '011'){
			return '单据';			
		}else if(service == '004'){
			return '非药品';			
		}else if(service == '001'){
			return '西药';			
		}else if(service == '002'){
			return '中成药';			
		}else if(service == '003'){
			return '草药';			
		}else{
			return '其他';
		}
}


function setOrderType(type){
	if(type == '0'){
		payState = '';
		bizState = '';
		overState = '';
	}else if(type == '1'){
		payState = '0';
		bizState = '0';
		overState = '0';
		
	}else if(type == '2'){
		payState = '2';
		bizState = '1';
		overState = '0';
		
	}else if(type == '3'){
		payState = '3';
		bizState = '';
		overState = '0';
		
	}else if(type == '4'){
		payState = '4';
		bizState = '';
		overState = '0';
		
	}else if(type == '5'){
		payState = '';
		bizState = '';
		overState = '5';
		
	} 
}