var deptMoney = 0.00;
var operMoney = 0.00;
var patientId = "";
var caseAllNum = 0;
var authentication;
if(Commonjs.getUrlParam("authentication") == null){
	authentication = 0;
}else{
	authentication = Commonjs.getUrlParam("authentication");
}

$(function(){
	//获得价钱
	getPrice();
	//得到McopyUserId获取病人信息
	var mcopyUserId = Commonjs.getUrlParam("McopyUserId");
	getPatientInfoById(mcopyUserId);
});
	
//获得病人信息
function getPatientInfoById(mcopyUserId) {
	//var patientId = "";
	var apiData = {};	
	apiData.id = mcopyUserId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/QueryPatientInfoById/callApi.do',param,function(dd){
		if(dd.RespCode == -14018){
			$("#name").text('查无此人');
			$("#PatientId").text('暂无');
			$("#IdCard").text('暂无');
			$("#Phone").text('暂无');
		}else if(dd.RespCode == 10000){
			patientId = dd.Data[0].PatientId;
			$("#name").text(dd.Data[0].Name);
			$("#PatientId").text(patientId);
			//$("#IdCard").text(plusXing(dd.Data[0].IdCard,3,2));
			//$("#Phone").text(plusXing(dd.Data[0].Phone,3,4));
			$("#IdCard").text(dd.Data[0].IdCardFormat);
			$("#Phone").text(dd.Data[0].MobileFormat);
			getMedicalRecords(patientId);
		}
	});

}	
	
//根据病案号获得病历
function getMedicalRecords(patientId) {
	var apiData = {};	
	apiData.patientId = patientId;
	apiData.orgCode = "AH01";
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/QueryMedicalRecords/callApi.do',param,function(dd){
		if(dd.RespCode == -14018){
			mui.alert('查无病历');
		}else if(dd.RespCode == 10000){
			if(dd.Data == undefined){
				mui.alert('暂查无病历（用户在出院14天后方能查询到对应病历）');
			}else{
				caseAllNum = dd.Data.length;
				loadData(dd.Data);	
			}
		}
		
	});
}

//表格渲染
function loadData(caseList){
	var html = '';
	var background = '';
	$("#caseLise").append(html);
	$.each(caseList,function(i,o) {
		var money = 0.00;
		i = i+1;
		html+= '<div class="list"><div class="list-date"><span>病历'+i+'</span></div>';
		//html+= '<span class="right">出院日期：'+o.OutHosDate+'</span></div>';
		if(o.IsExistOrder == 'true'){
			background = 'style="background:#e0e0e0;"';
		}else{
			background = '';
		}
		html+= '<div class="list-li" '+background+'><label>出院日期</label><span>'+o.OutHosDate+'</span></div>';
		html+= '<div class="list-li" '+background+'><label>住院科室</label><span>'+o.OutDeptName+'</span></div>';
		//html+= '<div class="list-li" '+background+'><label>住院科室</label><span>'+o.DeptName+'</span></div>';
		money = parseFloat(deptMoney) + parseFloat(money);
		
		//html+= '<div class="list-li" '+background+'><label>住院天数</label><span>'+o.Hospitalization+'</span></div>';
		if(o.Isoperation == 0){
			money = parseFloat(operMoney) + parseFloat(money);
		}
//		if(o.OperationName != '' && o.OperationName != null){
//			html+= '<div class="list-li" '+background+'><label>是否手术</label><span>是</span></div>';
//			html+= '<div class="list-li" '+background+'><label>手术名称</label><span>'+o.OperationName+'</span></div>';	
//		}else if(o.Isoperation == 1){
//			html+= '<div class="list-li" '+background+'><label>是否手术</label><span>无</span></div>';			
//		}		
		
		
		html+= '<div class="list-li" '+background+'><label class="select" onclick="changeCur('+i+')"><div class="round" value='+o.HospitalId+' name="'+o.ApplyTime+'" id="cur'+i+'"></div>全套病历 <span class="blue" id = "copyMoney'+i+'">￥ '+money.toFixed(2)+'</span></label>';
		html+= '<div class="list-li-box"><span style="font-size: 0.45rem;" class="list-li-reduce"  onclick="addCaseNum(-1,'+i+','+money.toFixed(2)+')">-</span><span class="list-li-number" style="font-size: 0.4rem;" id="caseNum'+i+'">0</span><span style="font-size: 0.45rem;" class="list-li-add" onclick="addCaseNum(1,'+i+','+money.toFixed(2)+')">+</span>';
		html+= '</div></div></div>';
	});
	$("#caseLise").append(html);
	
	if(Commonjs.getUrlParam("caseNumber")){
		var totalMoney = Commonjs.getUrlParam("totalMoney");
		var caseNumber = Commonjs.getUrlParam("caseNumber").split(',');
		for(var i = 1;i<=caseAllNum;i++){
			if(caseNumber[i-1] > 0){
				$("#cur"+i).addClass('cur');
				$("#caseNum"+i).text(caseNumber[i-1]);//份数
			}
		}
		$("#allMoney").text('￥ '+totalMoney);
	}

}

//获得病历复印金额
function getPrice(){
	var apiData = {};	
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/GetPriceManageInfo/callApi.do',param,function(dd){
		for(var i = 0; i < dd.Data.length; i++){
			if(dd.Data[i].PriceType == 1){
				deptMoney = dd.Data[i].Money;
			}else if(dd.Data[i].PriceType == 2){
				operMoney = dd.Data[i].Money;
			}			
		}
	});
}


//选择打印的份数
function addCaseNum(num,i,money){
	var id = "#caseNum"+i;
	var caseNum = $(id).text();
	if(caseNum == 0 && num == -1){
		
	}else if(caseNum == 1 && num == -1){
		$("#cur"+i).removeClass('cur');
		$(id).text(0);		
	}else{
		$("#cur"+i).addClass('cur');
		caseNum = parseInt(caseNum) + parseInt(num);
		$(id).text(caseNum);		
	}
	getAllMoney();
}

//点选病历
function changeCur(num){	
	if($("#cur"+num).attr("class").indexOf('cur') == -1){
		$("#cur"+num).addClass('cur');
		$("#caseNum"+num).text(1);	
		
	}else{
		$("#cur"+num).removeClass('cur');
		$("#caseNum"+num).text(0);	
	}
	 getAllMoney();
}

//计算总金额
function getAllMoney(){
	var allMoney = 0.00;
	for(var i = 1;i<=caseAllNum;i++){
		var id = "#caseNum"+i;
		var caseNum = parseFloat($(id).text());//份数
		var copyMoney = parseFloat($("#copyMoney"+i).text().substring(2,$("#copyMoney"+i).text().length));//每份价钱
		allMoney = Commonjs.accMul(caseNum,copyMoney) + allMoney;
	}
	$("#allMoney").text('￥ '+allMoney.toFixed(2));
}

//点击下一步
function submit(){
		var isExistOrder = true;
		var applyTime = '';
		//将选中的病历单构造成 A,B,C……
		var case_id = '';
		var case_money = '';
		var case_number = '';
		for(var i = 1;i<=caseAllNum;i++){
			var id = "#cur"+i;
			var copyMoney = $("#copyMoney"+i).text().substring(2,$("#copyMoney"+i).text().length);//每份价钱
			var caseNum = $("#caseNum"+i).text();//份数
			//选中的
			if($(id).attr("class").indexOf("cur") != -1){
				case_id = case_id + $(id).attr("value") + ",";
				case_money = case_money + copyMoney + ",";
				case_number = case_number + caseNum + ",";
				if($(id).attr("name") != ''){
					isExistOrder = true;
					if(applyTime == ''){
						applyTime = $(id).attr("name");
					}else{
						applyTime = compareTime(applyTime,$(id).attr("name"));
					}
				}else{
					isExistOrder = false;
				}
				
			}
		}
		
		if($("#allMoney").text().substring(2,$("#allMoney").text().length) == "0.00"){
			mui.alert('您未选择病历请重新选择');
		}else{
			if(!isExistOrder){
				addExpressOrder(case_id,case_money,case_number);			
			}else{
				mui.confirm('您已于'+applyTime.substring(0,10)+'申请复印过一次，是否继续申请复印？',function(e){
					if(e.index == '1'){
						addExpressOrder(case_id,case_money,case_number);
					}
				});
			}
		}
}

function addExpressOrder(case_id,case_money,case_number){
	var mediaName = Commonjs.getUrlParam("mediaName");
	var addressee = decodeURI(Commonjs.getUrlParam("addressee"));
	var address = decodeURI(Commonjs.getUrlParam("address"));
	var province = decodeURI(Commonjs.getUrlParam("province"));	
	var telephone = Commonjs.getUrlParam("telephone");
	if(addressee == null){
		addressee = '';
		telephone = '';
		address = '';
		province = '';
	}
	var total_money = $("#allMoney").text().substring(2,$("#allMoney").text().length);
	case_id = case_id.substring(0,case_id.length-1);
	case_money = case_money.substring(0,case_money.length-1);
	case_number = case_number.substring(0,case_number.length-1);
		
	var reSelectCase = Commonjs.getUrlParam("reSelectCase");
	var reSelectAddress = Commonjs.getUrlParam("reSelectAddress");
	if(reSelectCase == 1){
		var url = '../../pay.html?totalMoney='+total_money+"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+"&mediaName="+mediaName+
			"&caseId="+case_id+"&caseMoney="+case_money+"&caseNumber="+case_number+"&authentication="+authentication+
			"&addressee="+addressee+"&telephone="+telephone+"&address="+address+"&province="+province;
		location.href = Commonjs.goToUrl(url);		
	}else{
		var url = 'address.html?totalMoney='+total_money+"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+"&mediaName="+mediaName+
			"&caseId="+case_id+"&caseMoney="+case_money+"&caseNumber="+case_number+"&authentication="+authentication+
			"&addressee="+addressee+"&telephone="+telephone+"&address="+address+"&province="+province;
		location.href = Commonjs.goToUrl(url);
	}

}

//电话和姓名身份证等进行部分隐藏处理
//str：字符串，frontLen：前面保留位数，endLen：后面保留位数。
function plusXing (str,frontLen,endLen) { 
	var len = str.length-frontLen-endLen;
	var xing = '';
	for (var i=0;i<len;i++) {
	xing+='*';
	}
	return str.substring(0,frontLen)+xing+str.substring(str.length-endLen);
}


//判断日期，时间大小  
function compareTime(startDate, endDate) {
	var startDateTemp = startDate.split(" ");
	var endDateTemp = endDate.split(" ");

	var arrStartDate = startDateTemp[0].split("-");
	var arrEndDate = endDateTemp[0].split("-");

	var arrStartTime = startDateTemp[1].split(":");
	var arrEndTime = endDateTemp[1].split(":");

	var allStartDate = new Date(arrStartDate[0], arrStartDate[1],
			arrStartDate[2], arrStartTime[0], arrStartTime[1], arrStartTime[2]);
	var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2],
			arrEndTime[0], arrEndTime[1], arrEndTime[2]);

	if (allStartDate.getTime() >= allEndDate.getTime()) {
		return startDate.substring(0,10);
	} else {
		return endDate.substring(0,10);
	}
}   

