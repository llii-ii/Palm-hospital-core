//分页索引
var pIndex = 0;
//分页页数
var pSize = 10;
//就诊人控件，选中的就诊人
var selectedMember = null;
var loading;
$(function(){
	//获取url参数-date，选中的就诊人
	var urlDate = Commonjs.getUrlParam('date');
	var urlMemberObj = Commonjs.getDecodeUrlParam('memberObj');
	//获取缓存的,date，选中的就诊人
//	if(Commonjs.isEmpty(urlDate) && window.localStorage){
//		urlDate = localStorage.getItem("OutpatientDate");
//		localStorage.removeItem("OutpatientDate");
//	}
//	if(Commonjs.isEmpty(urlMemberObj) && window.localStorage){	
//		urlMemberObj = localStorage.getItem("selectedMember");
//		localStorage.removeItem("selectedMember");	
//	}
	
	if(Commonjs.isEmpty(urlDate)){
		urlDate = Commonjs.getLocalItem("OutpatientDate",true,true);
	}
	if(Commonjs.isEmpty(urlMemberObj)){	
		urlMemberObj = Commonjs.getLocalItem("selectedMember",true,true);
	}
	//滚动条初始化
	$(window).scroll(function(){
		var wtop = $(window).scrollTop()
		if(wtop > 120){
			$('.bill-head').addClass('bh-fixed');
			$('.bill-title').addClass('bt-fixed');
		}else{
			$('.bill-head').removeClass('bh-fixed');
			$('.bill-title').removeClass('bt-fixed');	
		}
	});
	
	initDateWidget(urlDate);
	//就诊人控件
	$("#filter-jzr").memberPicker({
		isMustCardNo : true,
		cardType:Commonjs.constant.cardType_1,
		defaultlValue:urlMemberObj,
		pickerOnClose:function(obj,member){
			clearPage();
			selectedMember = member;
			queryOutpatientCostList(selectedMember.MemberId);
			queryOutpatientCostType(selectedMember.MemberId);
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			queryOutpatientCostList(selectedMember.MemberId);
			queryOutpatientCostType(selectedMember.MemberId);
		}
	})
	
//	Commonjs.memberPicker({
//		page:'outpatientCostDaily',
//		serviceId:'',
//		cardType:Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		//默认选中就诊人，格式cardno,cardtype,membername
//		defaultlValue:urlMemberObj,
//		pickerOnClose:function(obj,member){
//			clearPage();
//			selectedMember = member;
//			queryOutpatientCostList(selectedMember.memberId);
//			queryOutpatientCostType(selectedMember.memberId);
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			queryOutpatientCostList(defaultMember.memberId);
//			queryOutpatientCostType(defaultMember.memberId);
//		}
//	});
	
	//分页
	$(document.body).infinite().on("infinite", function() {
		if(loading) {
			return;
		}
		loading = true;
		setTimeout(function() {
			//执行加载
			queryOutpatientCostType(selectedMember.MemberId);
		}, 300);
	});
	
 });

//初始化时间控件
function initDateWidget(date){
	var formDate =Commonjs.isEmpty(date)? new Date():new Date(date);
	var year = formDate.getFullYear();
	var mouth = formDate.getMonth() + 1;
	mouth = (mouth<10)?'0'+mouth:mouth;
	var day = formDate.getDate();
	day = (day<10)?'0'+day:day;
	//日期选择框
	$("#filter-date").datetimePicker({
		title: '选择就诊日期',
        yearSplit: '年',
        monthSplit: '月',
        dateSplit: '日',
        times: function () {},
		onOpen:function(){
			$("#filter-date").picker("setValue", [year, mouth, day]);
		},
		onChange:function($k, values, displayValues){
			$('#dateword').html(values[0] + '-' + values[1] + '-' + values[2]);
			setLocalStorage();
		},
		onClose:function(){
			//执行筛选
			clearPage();
			queryOutpatientCostList(selectedMember.MemberId);
			queryOutpatientCostType(selectedMember.MemberId);
		}
	});
	$('#dateword').html(year+"-"+mouth+"-"+day);
	//前一天
	$('.bill-prev').on('click',function(){
		clearPage();
		var ndate = $('#dateword').html();
		ndate = new Date(ndate);
		var dtime = ndate.getTime();
		dtime = dtime - 86400000;
		var datetime = new Date();
		datetime.setTime(dtime);
		year = datetime.getFullYear(),
		mouth = datetime.getMonth() + 1,
		day = datetime.getDate();
		mouth = (mouth<10)?'0'+mouth:mouth;
		day = (day<10)?'0'+day:day;
		$('#dateword').html(year + '-' + mouth + '-' + day);
		setLocalStorage();
		queryOutpatientCostList(selectedMember.MemberId);
		queryOutpatientCostType(selectedMember.MemberId);
		
	});
	//后一天
	$('.bill-next').on('click',function(){
		clearPage();
		var ndate = $('#dateword').html();
		ndate = new Date(ndate);
		var dtime = ndate.getTime();
		dtime = dtime + 86400000;
		var datetime = new Date();
		datetime.setTime(dtime);
		year = datetime.getFullYear(),
		mouth = datetime.getMonth() + 1,
		day = datetime.getDate();
		mouth = (mouth<10)?'0'+mouth:mouth;
		day = (day<10)?'0'+day:day;
		$('#dateword').html(year + '-' + mouth + '-' + day);
		setLocalStorage();
		queryOutpatientCostList(selectedMember.MemberId);
		queryOutpatientCostType(selectedMember.MemberId);
		
	});
}

//获取每日费用清单列表
function queryOutpatientCostType(memberId){
	$(".nomess").hide();
	$(".infinite-scroll").show();
	var page = {};
	page.PIndex = pIndex;
	page.PSize = pSize;
	var apiData = {};
	apiData.MemberId = memberId;
	apiData.CardType = Commonjs.constant.cardType_1;
	apiData.BeginDate = $('#dateword').html();
	apiData.EndDate = $('#dateword').html();
	var param = {};
	param.api = ''; 
	param.apiParam =Commonjs.getApiReqParams(apiData,page);
	Commonjs.ajax('/wsgw/order/QueryOutpatientCostType/callApi.do',param,function(jsons){
		$(".infinite-scroll").hide();
		if(!Commonjs.isEmpty(jsons)){
			if(!Commonjs.isEmpty(jsons.RespCode)){
				if(jsons.RespCode==10000){
					var listHtml = $(".c-list").html();
					var DateSize = 0 ;//后台返回数据条数
					if(Commonjs.ListIsNotNull(jsons.Data)){
						Commonjs.BaseForeach(jsons.Data, function(i,item){
							var url = "/business/outpatientDept/outpatientCostTypeItem.html?date="+$("#dateword").html()
									+"&memberId="+memberId+"&expenseTypeCode="+item.ExpenseTypeCode+"&expenseTypeName="+escape(item.ExpenseTypeName);
							var hrefUrl = "javascript:location.href=Commonjs.goToUrl(\'"+url+"\')";
							listHtml += "<li class=\"c-list-a\">";
							listHtml += "<a href=\""+hrefUrl+"\" onclick = \"setLocalStorage()\" class=\"c-arrow-r\">";
							listHtml += "<div class=\"c-list-info pr10\">";
							listHtml += "<h4 class=\"c-f15\">"+item.ExpenseTypeName+"</h4>";
							listHtml += "<p class=\"c-999\">"+item.Date+"</p></div>";
							listHtml += "<div class=\"c-list-key c-pack c-f13\">￥<span class=\"c-ffac0b\">"+Commonjs.centToYuan(item.Fee)+"</span></div>";
							listHtml += "</a></li>";
							});
						$(".c-list").html(listHtml);
						if(hasNextPage(jsons.Page.PCount)){
		           			 loading = false;
		           			 pIndex ++ ;
		           		}else{
		           			 loading = true;
		           			 $(".nomore").html("没有更多了");
		           		}
					}else if(page.PIndex == 0){
						loading = false;
						pIndex = 0;
						$(".c-list").empty();
					}
				} else {
	            	myLayer.alert(jsons.RespMessage,3000);
	            }
			}else{
				myLayer.alert("返回码为空",3000);
			}
		} else{
			//通信失败
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
		}
    },{async:false});
	
}

function queryOutpatientCostList(memberId){
	var page = {};
	page.PIndex =0;
	page.PSize = 10;
	var apiData = {};
	apiData.CardType= Commonjs.constant.cardType_1;
	apiData.MemberId=memberId;
	apiData.BeginDate=$('#dateword').html();
	apiData.EndDate=$('#dateword').html();
	var param = {};
	param.apiParam =Commonjs.getApiReqParams(apiData,page);
	Commonjs.ajax('/wsgw/order/QueryOutpatientCostList/callApi.do',param,function(jsons){
		if(!Commonjs.isEmpty(jsons)){
			var listHtml= '';
			if(!Commonjs.isEmpty(jsons.RespCode)){
				if(jsons.RespCode==10000){
					if(!Commonjs.isEmpty(jsons.Data)){
						$(".c-f28").html(Commonjs.centToYuan(jsons.Data[0].Fee));
					}else{
						myLayer.alert('未查询到门诊缴费数据！');
					}
				} else {
	            	myLayer.alert(jsons.RespMessage,3000);
	            }
			} else{
				myLayer.alert("返回码为空",3000);
			}
		} else{
			//通信失败
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
		}
    },{async:false});
}


/**
 * 重新设置页面的默认值
 * */
function clearPage(){
	loading = false;
	pIndex = 0;
	$(".c-f28").empty();
	$(".c-list").empty();
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

/**
 * 设置缓存,进入下一级页面，点击返回时，方便选中原来的患就诊人与日期
 * */
function setLocalStorage(){
	Commonjs.setLocalItem("OutpatientDate",$('#dateword').html(),true);
	Commonjs.setLocalItem("selectedMember",$('#filter-jzr').val(),true);
//	if (window.localStorage) {
//	    localStorage.setItem("OutpatientDate",$('#dateword').html());	
//	    localStorage.setItem("selectedMember",$('#filter-jzr').val());
//	}
}
