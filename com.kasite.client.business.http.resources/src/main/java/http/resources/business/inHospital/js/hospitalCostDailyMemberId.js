//分页索引
var pIndex = 0;
//分页页数
var pSize = 10;
//就诊人控件，选中的就诊人
var selectedMember = null;
//选择的日期
var selectDate = null;

var loading;
$(function(){
	//获取url参数-date，选中的就诊人
	selectDate = Commonjs.getUrlParam('date');
	if(Commonjs.isEmpty(selectDate)){
		selectDate = storage.getData("hospitalCostDate");
		if(Commonjs.isEmpty(selectDate)){
			selectDate = Commonjs.DateFormat(new Date(),'yyyy-MM-dd');
		}
	}
	var urlMemberObj = Commonjs.getDecodeUrlParam('memberObj');
	//获取缓存的,date，选中的就诊人
//	if( Commonjs.isEmpty(selectDate) && window.localStorage){
//		urlDate = localStorage.getItem("inHospitalDate");
//		localStorage.removeItem("inHospitalDate");
//	}
//	if( Commonjs.isEmpty(urlMemberObj) && window.localStorage){
//		urlMemberObj = localStorage.getItem("selectedMember");
//		localStorage.removeItem("selectedMember");	
//	}
	
	if( Commonjs.isEmpty(selectDate)){
		urlDate = Commonjs.getLocalItem("inHospitalDate",true,true);	
	}
	if( Commonjs.isEmpty(urlMemberObj)){
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
	
	initDateWidget(selectDate);
	
	//就诊人控件
	$("#filter-jzr").memberPicker({
		isMustCardNo : true,
		cardType: Commonjs.constant.cardType_14,
		//默认选中就诊人
		defaultlValue:urlMemberObj,
		pickerOnClose:function(obj,member){
			clearPage();
			selectedMember = member;
			queryInHospitalCostList(selectedMember.MemberId);
			queryInHospitalCostType(selectedMember.MemberId);
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			queryInHospitalCostList(defaultMember.MemberId);
			queryInHospitalCostType(defaultMember.MemberId);
		}
	});
	
//	//就诊人控件
//	Commonjs.memberPicker({
//		page:'hospitalCostDaily',
//		cardType: Commonjs.constant.cardType_14,
//		id:"filter-jzr",
//		//默认选中就诊人，格式cardno,cardtype,membername
//		defaultlValue:urlMemberObj,
//		pickerOnClose:function(obj,member){
//			clearPage();
//			selectedMember = member;
//			queryInHospitalCostList(selectedMember.memberId);
//			queryInHospitalCostType(selectedMember.memberId);
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			queryInHospitalCostList(defaultMember.memberId);
//			queryInHospitalCostType(defaultMember.memberId);
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
			queryInHospitalCostType(selectedMember.MemberId);
		}, 300);
	});
	
 });

//初始化时间控件
function initDateWidget(date){
	var formDate =Commonjs.isEmpty(date)? new Date():new Date(date);
	var year = 0;var mouth =0;var day =0;
	year = formDate.getFullYear();
	mouth = formDate.getMonth() + 1;
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
			storage.setData("hospitalCostDate",values[0] + '-' + values[1] + '-' + values[2]);
			setLocalStorage();
		},
		onClose:function(){
			//执行筛选
			clearPage();
			queryInHospitalCostList(selectedMember.MemberId);
			queryInHospitalCostType(selectedMember.MemberId);
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
		queryInHospitalCostList(selectedMember.MemberId);
		queryInHospitalCostType(selectedMember.MemberId);
		
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
		queryInHospitalCostList(selectedMember.MemberId);
		queryInHospitalCostType(selectedMember.MemberId);
		
	});
}

//获取每日费用清单列表
function queryInHospitalCostType(memberId){
	$(".nomess").hide();
	$(".infinite-scroll").show();
	var page = {};
	page.PIndex = pIndex;
	page.PSize = pSize;
	var apiData = {};
	apiData.MemberId = memberId;
	apiData.CardType = Commonjs.constant.cardType_14;
	apiData.BeginDate = $('#dateword').html();
	apiData.EndDate = $('#dateword').html();
	selectDate = $('#dateword').html();
	var param = {};
	param.api = ''; 
	param.apiParam =Commonjs.getApiReqParams(apiData,page);
	Commonjs.ajax('/wsgw/order/QueryInHospitalCostType/callApi.do',param,function(jsons){
		$(".infinite-scroll").hide();
		if(!Commonjs.isEmpty(jsons)){
			if(!Commonjs.isEmpty(jsons.RespCode)){
				if(jsons.RespCode==10000){
					var listHtml = $(".c-list").html();
					var DateSize = 0 ;//后台返回数据条数
					if(Commonjs.ListIsNotNull(jsons.Data)){ 
						Commonjs.BaseForeach(jsons.Data, function(i,item){
							var urlDate = selectDate;
							var itemDate = item.Date;
							if(urlDate == itemDate){
								var url = "/business/inHospital/inHospitalCostTypeItem.html?date="+$("#dateword").html()
								+"&memberId="+memberId+"&unit="+escape(item.Unit)+"&objNumber="+item.ObjNumber
								+"&fee="+item.Fee+"&specifications="+escape(item.Specifications)
								+"&unitPrice="+item.UnitPrice+"&expenseTypeName="+escape(item.ExpenseTypeName)
								+"&expenseTypeCode="+item.ExpenseTypeCode;
								var hrefUrl = "javascript:location.href=Commonjs.goToUrl(\'"+url+"\')";
								listHtml += "<li class=\"c-list-a\">";
								listHtml += "<a href=\""+hrefUrl+"\" onclick = \"setLocalStorage()\" class=\"c-arrow-r\">";
								listHtml += "<div class=\"c-list-info pr10\">";
								listHtml += "<h4 class=\"c-f15\">"+item.ExpenseTypeName+"</h4>";
								listHtml += "<p class=\"c-999\">"+item.Date+"</p></div>";
								listHtml += "<div class=\"c-list-key c-pack c-f13\">￥<span class=\"c-ffac0b\">"+Commonjs.centToYuan(item.Fee)+"</span></div>";
								listHtml += "</a></li>";
							}
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

function queryInHospitalCostList(memberId){
	var page = {};
	page.PIndex =0;
	page.PSize = 10;
	var apiData = {};
	apiData.CardType= Commonjs.constant.cardType_14;
	apiData.MemberId=memberId;
	apiData.BeginDate=$('#dateword').html();
	apiData.EndDate=$('#dateword').html();
	selectDate = $('#dateword').html();
	var param = {};
	param.apiParam =Commonjs.getApiReqParams(apiData,page);
	Commonjs.ajax('/wsgw/order/QueryInHospitalCostList/callApi.do',param,function(jsons){
		if(!Commonjs.isEmpty(jsons)){
			var listHtml= '';
			if(!Commonjs.isEmpty(jsons.RespCode)){
				if(jsons.RespCode==10000){
					Commonjs.BaseForeach(jsons.Data, function(i,item){
						var urlDate = selectDate;
						var itemDate = item.Date;
						if(urlDate == itemDate){
							$(".c-f28").html(Commonjs.centToYuan(item.Fee));
						}
					});
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
	Commonjs.setLocalItem("inHospitalDate",$('#dateword').html(),true);
	Commonjs.setLocalItem("selectedMember",$('#filter-jzr').val(),true);
//	if (window.localStorage) {
//	    localStorage.setItem("inHospitalDate",$('#dateword').html());	
//	    localStorage.setItem("selectedMember",$('#filter-jzr').val());
//	}
}
