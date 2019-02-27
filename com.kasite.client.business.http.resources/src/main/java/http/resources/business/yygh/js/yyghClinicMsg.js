var hosId;
var scheduleId; //排版ID
var deptCode; 
var doctorCode;
var deptName;
var doctorName;
var fee; //挂号费
var totalFee; //总费用
var cDate; //年月日时间
var doctorTitle;
var sourceCode; //号源ID
//var pmObj; //picker 就诊人对象
//var pnObj; //piker 号源对象
var regDate; 
var timeSlice;
var orderId;
var sqNo;
var isOnlinePay;//锁号时返回是否在线支付
var headImg = "";
var commendTime;
var endTime;
var bookSuc = "yySuccess.html";
var isContinue = true;
var noCardFlag = false; //true没有就诊卡
var feeInfo = "";
var isShowRegFee;//是否显示挂号费
var yyghAppointmentDetailsUrl = "/business/yygh/yyghAppointmentDetails.html";

//就诊人信息
var memberId = "";
var cardNo;
var cardType;
var certNum = "";
var mobile = "";
var name = "";
//var sex = "";
//var address = "";
$(function(){
	//是否显示挂号费 true 显示  false 不显示。
	isShowRegFee = Commonjs.getIsShowRegFee();
	hosId = Commonjs.getUrlParam("HosId");
	scheduleId = Commonjs.getUrlParam("scheduleId");
	doctorCode = Commonjs.getUrlParam("doctorCode");
	deptCode = Commonjs.getUrlParam("deptCode");
	fee = Commonjs.getUrlParam("fee");
	totalFee = Commonjs.getUrlParam("totalFee");
	doctorName = unescape(Commonjs.getUrlParam("doctorName"));
	deptName = unescape(Commonjs.getUrlParam("deptName"));
	headImg = unescape(Commonjs.getUrlParam("headImg"));
	cDate = unescape(Commonjs.getUrlParam("cDate"));
	doctorTitle = unescape(Commonjs.getUrlParam("doctorTitle"));
	regDate = Commonjs.getUrlParam("regDate");
	timeSlice = Commonjs.getUrlParam("timeSlice");
	initWidget();
	initDoctorAndSchedule(deptName,doctorName,cDate,doctorTitle,fee);
	myLayer.load('正在加载');
	
	//初始化就诊人插件
	$("#member_picker").memberPicker({
		pickerOnClose : function(obj,memberObj,memberList){
			//判断选择的不是同一个就诊人且已经选择了号源会进行解锁重新锁号操作。
			if(memberId!=memberObj.MemberId || name!= memberObj.MemberName || ((!Commonjs.isEmpty(cardNo) || !Commonjs.isEmpty(memberObj.CardNo)) && (Commonjs.isEmpty(cardNo) || cardNo!=memberObj.CardNo))){
				cardNo = memberObj.CardNo;
				cardType = memberObj.CardType;
				memberId = memberObj.MemberId;
				name = memberObj.MemberName;
				certNum = memberObj.CertNum;
				mobile = memberObj.Mobile;
				if(Commonjs.isEmpty(certNum) && memberObj.IsChildren==1){
	    			//证件号码为空且是儿童时，取监护人证件号码
	    			certNum = memberObj.GuardianCertNum;
	    		}
				numberDeal();
			}else{
				cardNo = memberObj.CardNo;
				cardType = memberObj.CardType;
				memberId = memberObj.MemberId;
				name = memberObj.MemberName;
				certNum = memberObj.CertNum;
				mobile = memberObj.Mobile;
				if(Commonjs.isEmpty(certNum) && memberObj.IsChildren==1){
	    			//证件号码为空且是儿童时，取监护人证件号码
	    			certNum = memberObj.GuardianCertNum;
	    		}
			}
		},
		ajaxSuccess : function(memberList,memberObj){
			cardNo = memberObj.CardNo;
			cardType = memberObj.CardType;
			memberId = memberObj.MemberId;
			name = memberObj.MemberName;
			certNum = memberObj.CertNum;
			mobile = memberObj.Mobile;
			if(Commonjs.isEmpty(certNum) && memberObj.IsChildren==1){
    			//证件号码为空且是儿童时，取监护人证件号码
    			certNum = memberObj.guardianCertNum;
    		}
		}
		
	});
	
//	getMemberList();
	queryNumbers();
	dealLock();
	myLayer.clear();
});
function initWidget(){
	inputFill();
	//确认
	$('#sureBtn').on('click',function(){
		var disable = $(this).hasClass('disable');
		if(disable){
			if($.trim($('#picker-person').val()) == ''){
				myLayer.alert("请选择就诊人");
				return;
			}
			if($.trim($('#picker-source').val()) == ''){
				myLayer.alert("请选择号源");
				return;
			}
		}
		addOrderLocal();
	});
}
//处理解锁
function dealLock(){
//	if(Commonjs.ListIsNotNull(localStorage.getItem("orderId"))){
//		orderId = localStorage.getItem("orderId");
//		unLock(false);
//	}
	if(Commonjs.ListIsNotNull(Commonjs.getLocalItem("orderId",false,false))){
		orderId = Commonjs.getLocalItem("orderId",false,false);
		unLock(false);
	}
}
function numberDeal(){
	myLayer.load('正在加载');
	if(!Commonjs.ListIsNotNull(sourceCode)){ //未选号不执行
		myLayer.clear();
		return;
	}
	if(Commonjs.ListIsNotNull(orderId)){ //订单ID不为空且BOOKSERVICE未发生异常 时可解锁
		unLock(true);
	}
	if(isContinue){
		lockOrder();
	}
}


//初始化数据显示
function initDoctorAndSchedule(deptName,doctorName,cDate,doctorTitle,fee){
	$("#deptName").html(deptName);
	$("#doctorName").html(doctorName);
	$("#cDate").html(cDate);
	$("#doctorTitle").html(doctorTitle);
	if(headImg =="" || headImg==null){
		headImg='../../common/images/d-male.png';
	}
	$("#headImg").attr('src',headImg)
	if(!isShowRegFee){
		$("#fee").html("未知");
		$("#feeInfo").html("未知");
	}else{
		$("#fee").html("￥ "+Number(Commonjs.centToYuan(fee)));
		$("#feeInfo").html("挂号费: ￥ "+Number(Commonjs.centToYuan(fee)));
	}
}

//function getMemberList() {
//	var apiData = {};
//	var param = {};
//	apiData.HosId = Commonjs.getUrlParam("HosId");
//	apiData.OpId=Commonjs.getOpenId();
//	apiData.CardType=Commonjs.constant.cardType_1;
//	param.apiParam = Commonjs.getApiReqParams(apiData,"",8002);
//	Commonjs.ajax('../../wsgw/basic/QueryMemberCardList/callApi.do?t=' + new Date().getTime(),param,function(jsons){
//		if (Commonjs.ListIsNotNull(jsons)) {
//            if (jsons.RespCode == 10000) {
//                if (Commonjs.ListIsNotNull(jsons.Data)) {
//                	var merberList = "";
//                    if (jsons.Data != null) {
//                    	var disObj=new Array();
//                    	var valObj=new Array();
//                    	var isAddDefalut = false;
//                    	Commonjs.BaseForeach(jsons.Data, function (i, item) {
//                    		var isDefaultMember = item.IsDefaultMember;
//                    		var isDefaultCardNo = item.IsDefault;
//                    		var def = false;
//                    		if(isDefaultMember*1 == 1 && isDefaultCardNo*1 == 1){
//                    			def = true;
//                    		}
//                        	if(def && !isAddDefalut){//默认
//                        		cardNo = (item.CardNo);
//                        		isAddDefalut = true;
//                        		certNum = item.CertNum;
//                        		if(Commonjs.isEmpty(certNum) && item.IsChildren==1){
//                        			//证件号码为空且是儿童时，取监护人证件号码
//                        			certNum = item.GuardianCertNum;
//                        		}
//                        		$("#picker-person").val(item.MemberName+"("+ Commonjs.formatCardNo(item.CardNo) +")");
//                        		disObj.unshift(item.MemberName+"("+ Commonjs.formatCardNo(cardNo)+")");
//                        		valObj.unshift(item.CardNo+","+item.CardType+","+certNum+","+item.Mobile+","+item.MemberName+","+item.Sex+","+item.Address+","+item.MemberId);
//                        		cardNo = item.CardNo;
//                        		cardType = item.CardType;
////                        		idCardNo = item.IdCardNo;
//                        		
//                        		mobile = item.Mobile;
//                        		name = item.MemberName;
//                        		sex = item.Sex;
//                        		address = item.Address;
//                        		memberId = item.MemberId;
//                        		if(Commonjs.isEmpty(cardNo) && !Commonjs.getIsOpenYYNotClinicCard()){
//                        			noCard();
//                        			noCardFlag = true;
//                    			}
//                        	}else{
//                        		if(!isAddDefalut){
//                        			cardNo = (item.CardNo);
//                            		cardType = item.CardType;
////                            		idCardNo = item.IdCardNo;
//                            		certNum = item.CertNum;
//	                        		if(Commonjs.isEmpty(certNum) && item.IsChildren==1){
//	                        			//证件号码为空且是儿童时，取监护人证件号码
//	                        			certNum = item.GuardianCertNum;
//	                        		}
//                            		mobile = item.Mobile;
//                            		name = item.MemberName;
//                            		sex = item.Sex;
//                            		address = item.Address;
//                            		memberId = item.MemberId;
//                            		if(Commonjs.isEmpty(cardNo) && !Commonjs.getIsOpenYYNotClinicCard()){
//                            			noCard();
//                            			noCardFlag = true;
//                        			}
//                            		disObj.push(item.MemberName+"("+ Commonjs.formatCardNo(item.CardNo) +")");
//                            		valObj.push(item.CardNo+","+item.CardType+","+certNum+","+item.Mobile+","+item.MemberName+","+item.Sex+","+item.Address+","+item.MemberId);
//                        		}else{
//                        			disObj.push(item.MemberName+"("+ Commonjs.formatCardNo(item.CardNo) +")");
//                            		valObj.push(item.CardNo+","+item.CardType+","+certNum+","+item.Mobile+","+item.MemberName+","+item.Sex+","+item.Address+","+item.MemberId);
//                        		}
//                        	}
//                        });
//                    }
//                    initMember(disObj,valObj);
//                } else {
//                	myLayer.alert("无数据",3000);
//                }
//            } else {
//            	myLayer.alert(jsons.RespMessage,3000);
//            }
//        } else {
//        	myLayer.alert('网络繁忙,请刷新后重试',3000);
//        }
//		
//    });
//}

function queryNumbers() {
	var apiData = {};
	var param = {};
	apiData.HosId = Commonjs.hospitalId();
	apiData.DeptCode = deptCode;
	apiData.DoctorCode = doctorCode;
	apiData.RegDate = regDate;
	apiData.TimeSlice = timeSlice;
	apiData.ScheduleId = scheduleId;
	apiData.MemberName = name;
	param.api = 'QueryNumbers';
	param.apiParam = Commonjs.getApiReqParams(apiData,"",2002);
	Commonjs.ajax('../../wsgw/yy/QueryNumbers/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	var merberList = "";
                    if (jsons.Data != null) {
                    	var disObj=new Array();
                    	var valObj=new Array();
                    	Commonjs.BaseForeach(jsons.Data, function (i, item) {
                    		var disVal='',val = item.SourceCode;
                    		//如果后端拼装的号源信息显示 则直接显示后端拼装的号源信息描述
                    		var numberInfo = item.NumberInfo;
                    		if(!Commonjs.isEmpty(numberInfo)){
                    			disVal = numberInfo;
                    		} else {
                    			if(!Commonjs.isEmpty(item.StartTime)){
                        			disVal += item.StartTime;
                        		}
                        		if(!Commonjs.isEmpty(item.EndTime)){
                        			disVal += ("-"+item.EndTime);
                        		}
                        		if(!Commonjs.isEmpty(item.SqNo)){
                        			disVal += (" "+item.SqNo);
                        		}
                    		}
                    		disObj.push(disVal);
                    		valObj.push(item.SourceCode+","+item.SqNo+","+item.StartTime+","+item.EndTime);
                        });
                    }
                    initNumber(disObj,valObj);
                } else {
                	myLayer.alert("无数据",3000);
                }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		
    });
	
}

//function initMember(disObj,valObj){
//	pm = $("#picker-person").picker({
//		title: "选择就诊人",
//		cols: [
//			{
//				textAlign: 'center',
//				displayValues:disObj,
//				values: valObj
//			}
//		],
//		onClose:function(p){
//			var menmberArray = p.cols[0].value.split(",");
//			if((memberId != menmberArray[7] && name!=menmberArray[4])  && Commonjs.ListIsNotNull(menmberArray[1])){ //不是首次进来且成员有发生变化
//
//				cardNo = menmberArray[0];
//				cardType = menmberArray[1];
////				idCardNo = menmberArray[2];
//				certNum = menmberArray[2];
//				mobile = menmberArray[3];
//				name = menmberArray[4];
//				sex = menmberArray[5];
//				address = menmberArray[6];
//				memberId = menmberArray[7];
//				
//				numberDeal();
//			}
//			if(!Commonjs.isEmpty(menmberArray[7])){ //不是首次进来时
//				cardNo = menmberArray[0];
//				cardType = menmberArray[1];
////				idCardNo = menmberArray[2];
//				certNum = menmberArray[2];
//				mobile = menmberArray[3];
//				name = menmberArray[4];
//				sex = menmberArray[5];
//				address = menmberArray[6];
//				memberId = menmberArray[7];
//			}
//			pmObj = p;
//			if(Commonjs.isEmpty(cardNo) && !Commonjs.getIsOpenYYNotClinicCard()){
//				noCardFlag = true;
//				noCard();
//			}else{
//				noCardFlag = false;
//			}
//		},
//		formatValue:function (p, values, displayValues){
//			return displayValues;
//		}
//	});
//}

function initNumber(disObj,valObj){
	$("#picker-source").picker({
		title: "选择预约号源",
		cols: [
			{
				textAlign: 'center',
				displayValues:disObj,
				values: valObj
			}
		],
		onClose:function(p){
			var numberArray = p.cols[0].value.split(",");
			sourceCode = numberArray[0];
			sqNo = numberArray[1];
			commendTime = numberArray[2];
			if(numberArray[3]){
				commendTime += "-"+numberArray[3]
			}
			//endTime = numberArray[3];
			pnObj = p;
			numberDeal();
		},
		formatValue:function (p, values, displayValues){
			return displayValues;
		}
	});
}

function inputFill(){
	var fill = true;
	$('.c-main').find('input').each(function() {
        if($.trim($(this).val()) == ''){
			fill = false;
		}
    });
	if(fill && Commonjs.ListIsNotNull(orderId)){
		$('#sureBtn').removeClass('disable');
	}else{
		$('#sureBtn').addClass('disable');	
	}
}

//释号
function unLock(flag) {
	var apiData = {};
	var param = {};
	//apiData.HosId=Commonjs.hospitalId();
	apiData.OrderId=orderId;
	param.api = 'Unlock';
	param.apiParam = Commonjs.getApiReqParams(apiData,"",3002);
	Commonjs.ajax("../../wsgw/yy/Unlock/callApi.do?t=" + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
//			console.log(jsons);
            if (jsons.RespCode == 10000) {
            	isContinue = true;
            	orderId = "";
            	Commonjs.removeLocalItem("orderId",false);
//            	localStorage.removeItem("orderId");
            }else if(jsons.RespCode  == -64002){
            	isContinue = true;
            	orderId = "";
//            	localStorage.removeItem("orderId");
            	Commonjs.removeLocalItem("orderId",false);
            }else {
            	if(flag){ //true时为正常的解锁
            		myLayer.alert(jsons.RespMessage,3000);
            		isContinue = false;
            	}else{  //退出后进来解锁，有可能已经解锁掉了不提示错误信息
//            		localStorage.removeItem("orderId");
            		Commonjs.removeLocalItem("orderId",false);
            		isContinue = true;
            	}
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		
    },{async:false});
}
/**
 * 判断如果为空的时候提示异常并返回false
 * @param value
 * @param message
 * @returns
 */
function checkIsEmpty(value,message){
	if(Commonjs.isEmpty(value)){
		myLayer.alert(message,3000);
		return false;
	}
	return true;
}
function checkFrom(cardNo,sourceCode,orderId,certNum,mobile,name,memberId){
	myLayer.clear();
	if(!checkIsEmpty(memberId,"请选择就诊人")) return false;
	if(!Commonjs.getIsOpenYYNotClinicCard()){
		if(!checkIsEmpty(cardNo,"必须绑定就诊卡预约")) return false;
	}
	if(!checkIsEmpty(sourceCode,"请选择号码")) return false;
	if(!checkIsEmpty(orderId,"未锁号成功订单号为空")) return false;
	if(!checkIsEmpty(certNum,"就诊人证件号码不能为空")) return false;
	if(!checkIsEmpty(mobile,"就诊人手机号不能为空")) return false;
	if(!checkIsEmpty(name,"就诊人姓名不能为空")) return false;
	return true;
}
//锁号
function lockOrder() {
	var apiData = {};
	var param = {};
	apiData.HosId= hosId;
	apiData.ScheduleId = scheduleId;
	apiData.CardType = cardType; 
	apiData.CardNo = cardNo; 
	apiData.DeptCode = deptCode;
	apiData.DoctorCode = doctorCode;
	apiData.RegType = "1";
	apiData.RegDate = regDate;
	apiData.TimeSlice = timeSlice;
	if(sqNo && sqNo.length > 0){
		apiData.SqNo = sqNo;
	}
	if(commendTime && commendTime.length > 0){
		apiData.CommendTime = commendTime;
	}
	apiData.SourceCode = sourceCode;
	apiData.MemberId = memberId;
	if(!checkFrom(cardNo,sourceCode,'orderId',certNum,mobile,name,memberId)) return;
	
	param.api = 'LockOrder';
	param.apiParam = Commonjs.getApiReqParams(apiData,"",3001);
	Commonjs.ajax("../../wsgw/yy/LockOrder/callApi.do?t=" + new Date().getTime(),param,function(jsons){
		
		myLayer.clear();
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	Commonjs.BaseForeach(jsons.Data, function (i, item) {
                		//console.log(item);
                    	orderId = item.OrderId;//锁号订单
                    	isOnlinePay  = item.IsOnlinePay;//是否在线支付
//                    	localStorage.setItem("orderId",orderId);
						Commonjs.setLocalItem("orderId",orderId,false);
                    	fee = item.Fee;
                    	$("#fee").html("￥ "+Number(Commonjs.centToYuan(fee)));
                    	feeInfo = item.FeeInfo;
                    	$("#feeInfo").html(feeInfo);
                	});
                } else {
                	myLayer.alert("无数据",3000);
                }
            } else if(jsons.RespCode == -64001){
            	Commonjs.BaseForeach(jsons.Data, function (i, item) {
                	var DoctorName = item.DoctorName;
                	var RegisterDate = item.RegisterDate;
                	var TimeId = item.TimeId;
                	var DeptName = item.DeptName;
                	var SqNo = item.SqNo;
                	isOnlinePay  = item.IsOnlinePay;//是否在线支付
                	orderId = item.OrderId;
                	//直接解锁旧的挂号的号源，重新锁号。 
                	unLock(false);
                	lockOrder();
//                	var str = '【'+RegisterDate ;
//                	if(TimeId > 0){
//                		var timestr = '上午';
//                    	if(TimeId == 2){
//                    		timestr = '下午';
//                    	}
//                    	str += '-'+timestr+'】';
//                	}
//                	str += '科室：【'+ DeptName+'】 医生：【'+DoctorName+'】';
//                	str += '【'+ SqNo +'】号';
//                	myLayer.confirm({
//                		title:'',
//                		con:'您已经锁定了一个号源'+str+'，是否继续挂之前锁定号？取消则释放历史锁定的号',
//                		cancel: function(){
//                			unLock(false);
//                			queryNumbers();
//                			lockOrder();
//                		},
//                		cancelValue:'\u53d6\u6d88',
//                		ok: function(){
//                			if(!Commonjs.isEmpty(orderId)){
//                				//跳转到收银台  然后支付成功后回调到预约挂号成功页面
//                        		var url = "/business/pay/payment.html?orderId=" + orderId ;
//                        		url += "&toUrl=/business/pay/yyPaySuccess.html?orderId=" + orderId ;
//                        		window.location.href = url;
//                			}else{
//                				myLayer.tip({
//                					con:'未找到订单；该订单异常，请联系客服。'
//                				});
//                				return;
//                			}
//                		},
//                		okValue:'\u786e\u5b9a'
//                	});
            	});
            	
            	
            	
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		inputFill();
		myLayer.clear();
    });
}


//挂号操作
function addOrderLocal() {
	if(noCardFlag){
		noCard();
		return;
	}
	if(!checkFrom(cardNo,sourceCode,orderId,certNum,mobile,name,memberId)) return;
//	apiData.IdCardNo=idCardNo;
//	apiData.Mobile=mobile;
//	apiData.Name=name;
    
    myLayer.load('正在加载');
    var apiData = {};
	var param = {};
	apiData.HosId=hosId;
	apiData.OrderId=orderId;
	apiData.PrescNo="";
	apiData.PayMoney=fee;
	apiData.TotalMoney=totalFee;
	apiData.PriceName="挂号支付";
	apiData.MemberId = memberId;
	//订单描述
//	apiData.OrderMemo="挂号订单"; 
	apiData.CardNo=cardNo;
	apiData.CardType=cardType;
	apiData.IsOnlinePay = isOnlinePay;
	apiData.OperatorId=Commonjs.getOpenId(); 
	apiData.OperatorName=name; 
	apiData.ServiceId="0";//业务类型ID 0 预约挂号
	apiData.PayFeeItem="0";//支付费用类型： 0 挂号费
	apiData.EqptType="1";//设备类型

	//非必填
	apiData.BankCardNo="";
	apiData.BankSeqNo="";
	apiData.ChargeSource="";
	apiData.Data_1 = {};
	apiData.Data_1.DoctorCode=doctorCode;
	apiData.Data_1.DoctorName=doctorName;
	apiData.Data_1.DeptCode=deptCode;
	apiData.Data_1.DeptName=deptName;
	apiData.Data_1.FeeInfo=feeInfo;
	
	param.api = 'AddOrderLocal';
	param.apiParam = Commonjs.getApiReqParams(apiData,"",6001);
	Commonjs.ajax("../../wsgw/order/AddOrderLocal/callApi.do?t=" + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
//			console.log(jsons)
            if (jsons.RespCode == 10000) {
            	Commonjs.BaseForeach(jsons.Data, function (i, item) {
            		var orderid = item.OrderId;
                	if(isOnlinePay == 1 || isOnlinePay == "1"){ //需要在线支付
//                		localStorage.removeItem("orderId");
                		Commonjs.removeLocalItem("orderId",false);
                		//跳转到收银台  然后支付成功后回调到预约挂号成功页面
                		var url = "/business/pay/payment.html?orderId=" + orderid ;
                		url += "&toUrl=/business/pay/yyPaySuccess.html?orderId=" + orderid ;
                		window.location.href = url;
                	}else if(isOnlinePay == 2 || isOnlinePay == "2"){ //不需要在线支付直接挂号
                		bookService();
                	}else{
                		myLayer.alert('系统异常，存在未定义的支付方式。isOnlinePay = '+ isOnlinePay,3000);
                    	myLayer.clear();
                	}
            	});
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            	myLayer.clear();
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        	myLayer.clear();
        }
    });
}

//挂号操作
function bookService() {
	myLayer.clear();
    var apiData = {};
	var param = {};
	apiData.HosId = Commonjs.getUrlParam("HosId");
	apiData.OrderId=orderId;
	apiData.OperatorId = Commonjs.getOpenId();
	myLayer.load('正在挂号');
	param.api = 'BookService';
	param.apiParam = Commonjs.getApiReqParams(apiData,"",3003);
	Commonjs.ajax("../../wsgw/yy/BookService/callApi.do?t=" + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
//			console.log(jsons);
            if (jsons.RespCode == 10000) {
//            	addOrderOver();
            	Commonjs.removeLocalItem("orderId",false);
            	goToYYsuccess();
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            	orderId = "";
            	$("#picker-source").val("");
//            	localStorage.removeItem("orderId");
            	Commonjs.removeLocalItem("orderId",false);
            	inputFill();
            }
        } else {
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		
    },{error:function(){
    	//处理异常情况
    	myLayer.alert('网络繁忙',3000);
    	location.href = Commonjs.goToUrl(yyghAppointmentDetailsUrl + '?orderId='+ orderId +'&localRegFlag=4');
    }}); 
}
//
////添加业务状态信息
//function addOrderOver() {
//	var apiData = {};
//	var param = {};
//	apiData.HosId=Commonjs.hospitalId();
//	apiData.OrderId=orderId;
//	apiData.OverState="1";
//	apiData.OperatorId=Commonjs.getOpenId();
//	apiData.OperatorName=name; 
//	param.api = 'BizForCompletion';
//	param.apiParam = Commonjs.getApiReqParams(apiData,"",6014);
//	Commonjs.ajax("../../wsgw/order/BizForCompletion/callApi.do?t=" + new Date().getTime(),param,function(jsons){
//		if (Commonjs.ListIsNotNull(jsons)) {
//			console.log(jsons);
//            if (jsons.RespCode == 10000) {
//            	goToYYsuccess();
//            } else {
//            	myLayer.alert(jsons.RespMessage,3000);
//            }
//        } else {
//        	myLayer.alert('网络繁忙,请刷新后重试',3000);
//        }
//    });
//	myLayer.clear();
//}

function goToYYsuccess() {
    if (bookSuc.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(bookSuc   + '&HosId='+hosId+'&orderId='+ orderId + '&cardNo=' + cardNo + '&cardType=' + cardType   + '&payState=0&bizState=1'+'&operatorId='+Commonjs.getOpenId()+ "&operatorName="+escape(name));// +'&t=' + new Date().getTime();
    } else {
        location.href = Commonjs.goToUrl(bookSuc   + '?HosId='+hosId+'&orderId='+ orderId + '&cardNo=' + cardNo + "&cardType=" + cardType   + '&payState=0&bizState=1'+'&operatorId='+Commonjs.getOpenId()+ "&operatorName="+escape(name));// +'&t=' + new Date().getTime();
    }
}

function noCard(){
	myLayer.tip({
		con:'请先绑定就诊卡',
		cancel:function(){
			goToClinicMember('addc');
		}
	});
}
var clinicMember = "/business/member/memberList.html";
function goToClinicMember(toFlag) {
    if (clinicMember.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(clinicMember   + '&deptCode=' + deptCode + '&doctorCode=' + doctorCode + '&flag='+toFlag);// + '&t=' + new Date().getTime();
    } else {
        location.href = Commonjs.goToUrl(clinicMember   + '?deptCode=' + deptCode + '&doctorCode=' + doctorCode + '&flag='+toFlag);// + '&t=' + new Date().getTime();
    }
}


